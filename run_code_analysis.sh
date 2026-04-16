#!/usr/bin/env bash
set -euo pipefail

# -------------------------------------------------------------------
# run_code_analysis.sh
# Runs an AWS Transform Custom transformation in headless mode.
# Retries up to MAX_RETRIES times on failure.
#
# Usage:
#   ./run_code_analysis.sh [-n <transformation-name>] [-p <path>] [-c <build-command>]
#
# Defaults:
#   -n  AWS/comprehensive-codebase-analysis
#   -p  .                   (current directory)
#   -c  mvn clean install   (Maven build)
# -------------------------------------------------------------------

TRANSFORMATION_NAME="AWS/comprehensive-codebase-analysis"
CODE_PATH="."
BUILD_CMD="mvn clean install"
MAX_RETRIES=3

while getopts "n:p:c:" opt; do
  case $opt in
    n) TRANSFORMATION_NAME="$OPTARG" ;;
    p) CODE_PATH="$OPTARG" ;;
    c) BUILD_CMD="$OPTARG" ;;
    *) echo "Usage: $0 [-n <transformation-name>] [-p <path>] [-c <build-command>]" && exit 1 ;;
  esac
done

echo "=== AWS Transform Custom ==="
echo "Transformation: $TRANSFORMATION_NAME"
echo "Code path:      $CODE_PATH"
echo "Build command:  $BUILD_CMD"
echo "============================"

attempt=1
while [ $attempt -le $MAX_RETRIES ]; do
  echo "--- Attempt $attempt of $MAX_RETRIES ---"

  if atx custom def exec \
    -n "$TRANSFORMATION_NAME" \
    -p "$CODE_PATH" \
    -c "$BUILD_CMD" \
    -x -t; then
    echo "=== Transformation completed successfully ==="
    exit 0
  fi

  echo "Attempt $attempt failed."
  attempt=$((attempt + 1))

  if [ $attempt -le $MAX_RETRIES ]; then
    echo "Retrying in 10 seconds..."
    sleep 10
  fi
done

echo "=== All $MAX_RETRIES attempts failed ==="
exit 1
