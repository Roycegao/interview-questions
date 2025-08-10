#!/usr/bin/env node

/**
 * Test Runner Script
 * Used to run all tests for the frontend project
 */

const { execSync } = require('child_process');
const path = require('path');


try {

  execSync('npm test -- --watchAll=false --coverage --verbose', {
    stdio: 'inherit',
    cwd: path.join(__dirname),
  });
  
  
} catch (error) {
  console.error('\n❌ Test execution failed:', error.message);
  process.exit(1);
}
