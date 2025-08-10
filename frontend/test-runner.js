#!/usr/bin/env node

/**
 * 测试运行脚本
 * 用于运行前端项目的所有测试
 */

const { execSync } = require('child_process');
const path = require('path');

console.log('🚀 开始运行前端测试...\n');

try {
  // 运行所有测试
  console.log('📋 运行所有测试...');
  execSync('npm test -- --watchAll=false --coverage --verbose', {
    stdio: 'inherit',
    cwd: path.join(__dirname),
  });
  
  console.log('\n✅ 所有测试运行完成！');
  
} catch (error) {
  console.error('\n❌ 测试运行失败:', error.message);
  process.exit(1);
}

console.log('\n📊 测试覆盖率报告已生成');
console.log('💡 提示: 运行 "npm test" 可以进入交互式测试模式');
console.log('💡 提示: 运行 "npm test -- --coverage" 可以生成覆盖率报告');
console.log('💡 提示: 运行 "npm test -- --watch" 可以进入监听模式'); 