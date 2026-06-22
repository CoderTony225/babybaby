# 无人机管理系统一键启动脚本
# 请在 PowerShell 中运行：.\启动项目.ps1

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  无人机管理系统 - 一键启动" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# 设置环境变量
$env:Path = [System.Environment]::GetEnvironmentVariable("Path","Machine") + ";" + [System.Environment]::GetEnvironmentVariable("Path","User")

# 检查环境
Write-Host "[检查] 验证环境..." -ForegroundColor Yellow
$javaVersion = java -version 2>&1 | Select-String "version"
Write-Host "  Java: $javaVersion" -ForegroundColor Green
Write-Host ""

# 启动后端
Write-Host "[启动] 启动 Spring Boot 后端..." -ForegroundColor Yellow
$backendPath = Join-Path $PSScriptRoot "baseplatform"
Write-Host "  路径：$backendPath" -ForegroundColor Gray

# 清理 target
if (Test-Path "$backendPath\target") {
    Write-Host "  清理旧编译文件..." -ForegroundColor Gray
    Remove-Item -Recurse -Force "$backendPath\target" -ErrorAction SilentlyContinue
}

# 启动后端进程
Start-Process powershell -ArgumentList "-NoExit", "-Command", @"
cd '$backendPath'; 
`$env:Path = [System.Environment]::GetEnvironmentVariable('Path','Machine') + ';' + [System.Environment]::GetEnvironmentVariable('Path','User'); 
Write-Host '正在启动 Spring Boot...' -ForegroundColor Yellow; 
.\mvnw.cmd clean spring-boot:run
"@ -WindowStyle Normal

Write-Host "  后端启动中... (端口 8080)" -ForegroundColor Green
Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  访问地址：http://localhost:8080" -ForegroundColor Green
Write-Host "  登录账号：admin / admin123" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "按任意键退出此窗口..." -ForegroundColor Gray

$null = $Host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown")
