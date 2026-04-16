#!/bin/bash

# Build only script for CI/CD - no Docker operations
mvn clean install

echo "Build completed successfully!"
