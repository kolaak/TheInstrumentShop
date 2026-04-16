#!/usr/bin/env python3
"""
Queries GitHub Dependabot alerts for the magnefique-studios/instrumentShop repository,
prints them to the screen and a file, and optionally creates fix PRs.

Requirements:
    pip install requests

Usage:
    export GITHUB_TOKEN="your_personal_access_token"
    python dependabot_alerts.py

The token needs these scopes:
    - security_events  (read Dependabot alerts)
    - repo             (create branches, commits, and pull requests)
"""

import os
import sys
import json
import base64
import requests
from datetime import datetime

OWNER = "magnefique-studios"
REPO = "instrumentShop"
API_BASE = f"https://api.github.com/repos/{OWNER}/{REPO}"
ALERTS_URL = f"{API_BASE}/dependabot/alerts"
OUTPUT_FILE = "dependabot_alerts_report.txt"


def get_github_token():
    token = os.environ.get("GITHUB_TOKEN")
    if not token:
        print("Error: GITHUB_TOKEN environment variable is not set.")
        print("Export a GitHub PAT with 'security_events' and 'repo' scopes:")
        print('  export GITHUB_TOKEN="ghp_xxxxxxxxxxxx"')
        sys.exit(1)
    return token


def gh_headers(token):
    return {
        "Accept": "application/vnd.github+json",
        "Authorization": f"Bearer {token}",
        "X-GitHub-Api-Version": "2022-11-28",
    }


# ---------------------------------------------------------------------------
# Fetch alerts
# ---------------------------------------------------------------------------
def fetch_alerts(token):
    headers = gh_headers(token)
    alerts = []
    url = ALERTS_URL
    params = {"per_page": 100}

    while url:
        resp = requests.get(url, headers=headers, params=params)

        if resp.status_code == 404:
            print(f"Error 404: Repository {OWNER}/{REPO} not found or "
                  "Dependabot alerts are not enabled.")
            sys.exit(1)
        elif resp.status_code == 403:
            print("Error 403: Insufficient permissions. "
                  "Ensure your token has 'security_events' scope.")
            sys.exit(1)
        elif resp.status_code != 200:
            print(f"Error {resp.status_code}: {resp.text}")
            sys.exit(1)

        batch = resp.json()
        if not batch:
            break
        alerts.extend(batch)

        # Follow cursor-based pagination via Link header
        url = None
        params = {}
        link_header = resp.headers.get("Link", "")
        for part in link_header.split(","):
            if 'rel="next"' in part:
                url = part.split(";")[0].strip().strip("<>")
                break

    return alerts


# ---------------------------------------------------------------------------
# Formatting
# ---------------------------------------------------------------------------
def format_alert(alert):
    number = alert.get("number", "N/A")
    state = alert.get("state", "N/A")
    severity = alert.get("security_advisory", {}).get("severity", "N/A")
    summary = alert.get("security_advisory", {}).get("summary", "N/A")
    pkg = alert.get("security_vulnerability", {}).get("package", {})
    package_name = pkg.get("name", "N/A")
    ecosystem = pkg.get("ecosystem", "N/A")
    vuln_range = alert.get("security_vulnerability", {}).get(
        "vulnerable_version_range", "N/A"
    )
    created = alert.get("created_at", "N/A")
    html_url = alert.get("html_url", "")

    lines = [
        f"  Alert #{number}",
        f"    State:              {state}",
        f"    Severity:           {severity}",
        f"    Package:            {package_name} ({ecosystem})",
        f"    Vulnerable range:   {vuln_range}",
        f"    Summary:            {summary}",
        f"    Created:            {created}",
        f"    URL:                {html_url}",
    ]
    return "\n".join(lines)


# ---------------------------------------------------------------------------
# PR creation helpers
# ---------------------------------------------------------------------------

def get_default_branch(token):
    """Return the default branch name for the repo."""
    resp = requests.get(API_BASE, headers=gh_headers(token))
    resp.raise_for_status()
    return resp.json()["default_branch"]


def get_branch_sha(token, branch):
    """Return the latest commit SHA of a branch."""
    url = f"{API_BASE}/git/ref/heads/{branch}"
    resp = requests.get(url, headers=gh_headers(token))
    resp.raise_for_status()
    return resp.json()["object"]["sha"]


def create_branch(token, branch_name, sha):
    """Create a new branch from the given SHA."""
    url = f"{API_BASE}/git/refs"
    payload = {"ref": f"refs/heads/{branch_name}", "sha": sha}
    resp = requests.post(url, headers=gh_headers(token), json=payload)
    if resp.status_code == 422:
        print(f"  Branch '{branch_name}' already exists — reusing it.")
        return
    resp.raise_for_status()
    print(f"  Created branch: {branch_name}")


def get_file_content(token, path, branch):
    """Return (content_str, sha) for a file on the given branch."""
    url = f"{API_BASE}/contents/{path}"
    params = {"ref": branch}
    resp = requests.get(url, headers=gh_headers(token), params=params)
    if resp.status_code == 404:
        return None, None
    resp.raise_for_status()
    data = resp.json()
    content = base64.b64decode(data["content"]).decode("utf-8")
    return content, data["sha"]


def update_file(token, path, message, content, sha, branch):
    """Create or update a file on the given branch via the Contents API."""
    url = f"{API_BASE}/contents/{path}"
    payload = {
        "message": message,
        "content": base64.b64encode(content.encode("utf-8")).decode("ascii"),
        "branch": branch,
    }
    if sha:
        payload["sha"] = sha
    resp = requests.put(url, headers=gh_headers(token), json=payload)
    resp.raise_for_status()
    print(f"  Committed change to {path} on {branch}")


def create_pull_request(token, title, body, head, base):
    """Open a pull request and return its HTML URL."""
    url = f"{API_BASE}/pulls"
    payload = {
        "title": title,
        "body": body,
        "head": head,
        "base": base,
    }
    resp = requests.post(url, headers=gh_headers(token), json=payload)
    if resp.status_code == 422:
        # PR may already exist for this head branch
        print("  A pull request for this branch may already exist.")
        return None
    resp.raise_for_status()
    pr_url = resp.json()["html_url"]
    print(f"  Pull request created: {pr_url}")
    return pr_url


# ---------------------------------------------------------------------------
# Interactive fix flow
# ---------------------------------------------------------------------------

def prompt_fix_for_alert(alert, token, default_branch):
    """Walk the user through creating a fix PR for a single alert."""
    number = alert.get("number", "unknown")
    pkg = alert.get("security_vulnerability", {}).get("package", {})
    package_name = pkg.get("name", "unknown")
    summary = alert.get("security_advisory", {}).get("summary", "")

    answer = input(f"\n  Create a fix PR for alert #{number}? [y/N]: ").strip().lower()
    if answer != "y":
        return None

    # 1. Determine which file to edit
    file_path = input("  Path to the dependency file to update "
                      "(e.g. pom.xml, package.json): ").strip()
    if not file_path:
        print("  Skipped — no file path provided.")
        return None

    # 2. Create a fix branch
    base_sha = get_branch_sha(token, default_branch)
    branch_name = f"fix/dependabot-alert-{number}"
    create_branch(token, branch_name, base_sha)

    # 3. Show current file content so the user can decide on the fix
    current_content, file_sha = get_file_content(token, file_path, branch_name)
    if current_content is None:
        print(f"  File '{file_path}' not found on branch {branch_name}.")
        new_file = input("  Create it as a new file? [y/N]: ").strip().lower()
        if new_file != "y":
            return None
        file_sha = None
        current_content = ""

    print(f"\n  --- Current content of {file_path} (first 80 lines) ---")
    for i, line in enumerate(current_content.splitlines()[:80], 1):
        print(f"  {i:>4} | {line}")
    print("  --- end ---\n")

    # 4. Collect the fix from the user
    print("  Provide the updated file content. Options:")
    print("    a) Enter a local file path whose content will be used")
    print("    b) Type the content directly (end with a line containing only 'EOF')")
    choice = input("  Local file path or press Enter to type inline: ").strip()

    if choice and os.path.isfile(choice):
        with open(choice, "r") as f:
            new_content = f.read()
        print(f"  Read content from {choice}")
    else:
        print("  Type the new file content (end with a line containing only 'EOF'):")
        lines = []
        while True:
            line = input()
            if line.strip() == "EOF":
                break
            lines.append(line)
        new_content = "\n".join(lines) + "\n"

    # 5. Commit the change
    commit_msg = f"fix: update {package_name} to address Dependabot alert #{number}"
    update_file(token, file_path, commit_msg, new_content, file_sha, branch_name)

    # 6. Open the PR
    pr_title = f"Fix Dependabot alert #{number}: {package_name}"
    pr_body = (
        f"## Dependabot Alert #{number}\n\n"
        f"**Package:** {package_name}\n"
        f"**Summary:** {summary}\n\n"
        f"This PR updates `{file_path}` to remediate the vulnerability."
    )
    pr_url = create_pull_request(token, pr_title, pr_body, branch_name, default_branch)
    return pr_url


# ---------------------------------------------------------------------------
# Main
# ---------------------------------------------------------------------------

def main():
    token = get_github_token()

    print(f"Fetching Dependabot alerts for {OWNER}/{REPO}...\n")
    alerts = fetch_alerts(token)

    timestamp = datetime.now().strftime("%Y-%m-%d %H:%M:%S")
    header = (
        f"Dependabot Alerts Report\n"
        f"Repository: {OWNER}/{REPO}\n"
        f"Generated:  {timestamp}\n"
        f"Total alerts: {len(alerts)}\n"
        f"{'-' * 60}"
    )

    if not alerts:
        body = "\nNo Dependabot alerts found."
    else:
        body = "\n" + f"\n{'-' * 60}\n".join(format_alert(a) for a in alerts)

    report = f"{header}{body}\n"

    # Print to screen
    print(report)

    # Write to file
    with open(OUTPUT_FILE, "w") as f:
        f.write(report)
    print(f"Report written to {OUTPUT_FILE}")

    # --- Interactive PR creation (disabled by default) ---
    enable_pr_creation = False  # Set to True when ready to create fix PRs

    if not enable_pr_creation or not alerts:
        return

    print(f"\n{'=' * 60}")
    print("PR CREATION")
    print(f"{'=' * 60}")

    default_branch = get_default_branch(token)
    created_prs = []

    for alert in alerts:
        if alert.get("state") != "open":
            continue
        print(f"\n{'-' * 60}")
        print(format_alert(alert))
        pr_url = prompt_fix_for_alert(alert, token, default_branch)
        if pr_url:
            created_prs.append((alert.get("number"), pr_url))

    # Summary
    if created_prs:
        print(f"\n{'=' * 60}")
        print(f"Created {len(created_prs)} PR(s):")
        for num, url in created_prs:
            print(f"  Alert #{num} -> {url}")
    else:
        print("\nNo PRs were created.")


if __name__ == "__main__":
    main()
