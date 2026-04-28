# GitHub-PR-Context-Codebase-Analysis

## Objective

Extract a GitHub Pull Request URL from the additionalContext field, retrieve the PR metadata (name, body, diff) via the GitHub API, clone the repository and check out the PR branch locally, build and test the branch to ensure it is in a good state, execute the AWS/comprehensive-codebase-analysis transformation definition against the checked-out PR branch using the PR data to scope the documentation changes, and then push those documentation and analysis changes back to the PR branch as a new commit.

## Summary

This transformation parses the additionalContext field to find a GitHub Pull Request URL and uses the GitHub REST API to fetch the PR metadata (name, body, and diff). It then clones the repository and checks out the PR's source branch locally. The checked-out branch is built and its tests are run to confirm the code is in a good state before proceeding. The retrieved PR context is assembled into a structured prompt that scopes what the documentation changes should focus on, and the AWS/comprehensive-codebase-analysis transformation definition is executed against the locally checked-out PR branch. After the analysis completes, the transformation applies the documentation changes and pushes them as a new commit to the PR's source branch via the GitHub API.

## Entry Criteria

1. The additionalContext field contains a valid GitHub Pull Request URL (https://github.com/{owner}/{repo}/pull/{pull_number}).
2. The GitHub repository is accessible (public, or via a configured GitHub personal access token).
3. The configured GitHub token has write permissions (contents:write) to the repository.
4. The Pull Request exists and is in an open state.
5. Git is installed and available on the local system.
6. The build tool required by the repository is installed and available.
7. The AWS/comprehensive-codebase-analysis transformation definition is available in the registry.

## Implementation Steps

1. Parse the additionalContext field and extract the GitHub PR URL.
2. Isolate the owner, repo, and pull_number from the URL.
3. Call GET /repos/{owner}/{repo}/pulls/{pull_number} to retrieve PR metadata (title, body, head ref and SHA).
4. Call the same endpoint with Accept: application/vnd.github.v3.diff to retrieve the full diff.
5. Clone the repository and check out the PR's source branch locally.
6. Build and test the checked-out branch; halt if build/tests fail.
7. Assemble PR data into a structured context block (PR Name, PR Description, PR Diff).
8. Retrieve the AWS/comprehensive-codebase-analysis transformation definition from the registry.
9. Execute the analysis transformation against the PR branch, passing the PR context to scope documentation changes.
10. Parse the analysis output to identify all suggested documentation and file changes.
11. For each changed file, retrieve the current blob SHA via GET /repos/{owner}/{repo}/contents/{path}?ref={branch_name}.
12. Create new blobs via POST /repos/{owner}/{repo}/git/blobs with updated content (base64-encoded).
13. Create a new tree referencing the updated blobs.
14. Create a new commit with the new tree and the PR branch's latest commit as parent.
15. Update the PR branch reference to point to the new commit.
16. Clean up the temporary local clone directory.
17. Return a summary including modified files, new commit SHA, and PR URL.

## Validation / Exit Criteria

1. PR URL successfully parsed with valid owner, repo, and pull_number.
2. GitHub API calls returned successful responses for PR metadata and diff.
3. PR confirmed in open state with source branch ref and SHA extracted.
4. PR name, body, and diff data retrieved (body may be empty).
5. Repository cloned and PR branch checked out successfully.
6. Build completed and all tests passed.
7. Context block assembled with all three sections in expected format.
8. AWS/comprehensive-codebase-analysis transformation retrieved from registry.
9. Analysis transformation executed successfully with PR context scoping.
10. At least one documentation/file change suggestion identified.
11. New blobs created successfully for each changed file.
12. New tree created referencing all updated blobs.
13. New commit created with correct parent and tree references.
14. PR branch reference updated to the new commit.
15. Temporary clone directory cleaned up.
16. Final output includes modified files list, new commit SHA, and PR URL.
