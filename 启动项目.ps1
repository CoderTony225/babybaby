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

$nodeVersion = node --version
Write-Host "  Node.js: $nodeVersion" -ForegroundColor Green
Write-Host ""

# 启动后端
Write-Host "[1/2] 启动后端..." -ForegroundColor Yellow
$backendPath = Join-Path $PSScriptRoot "baseplatform"
Write-Host "  路径：$backendPath" -ForegroundColor Gray

# 清理 target 目录
if (Test-Path "$backendPath\target") {
    Write-Host "  清理旧编译文件..." -ForegroundColor Gray
    Remove-Item -Recurse -Force "$backendPath\target" -ErrorAction SilentlyContinue
}

# 启动后端进程
$backendJob = Start-Process powershell -ArgumentList "-NoExit", "-Command", @"
cd '$backendPath'; 
`$env:Path = [System.Environment]::GetEnvironmentVariable('Path','Machine') + ';' + [System.Environment]::GetEnvironmentVariable('Path','User'); 
Write-Host '正在启动 Spring Boot...' -ForegroundColor Yellow; 
.\mvnw.cmd clean spring-boot:run
"@ -PassThru -WindowStyle Normal

Write-Host "  后端启动中... (端口 8080)" -ForegroundColor Green
Start-Sleep -Seconds 3
Write-Host ""

# 启动前端
Write-Host "[2/2] 启动前端..." -ForegroundColor Yellow
$frontendPath = Join-Path $PSScriptRoot "frontend"
Write-Host "  路径：$frontendPath" -ForegroundColor Gray

# 清理 vite 缓存
if (Test-Path "$frontendPath\node_modules\.vite") {
    Write-Host "  清理 Vite 缓存..." -ForegroundColor Gray
    Remove-Item -Recurse -Force "$frontendPath\node_modules\.vite" -ErrorAction SilentlyContinue
}

# 启动前端进程
$frontendJob = Start-Process powershell -ArgumentList "-NoExit", "-Command", @"
cd '$frontendPath'; 
`$env:Path = [System.Environment]::GetEnvironmentVariable('Path','Machine') + ';' + [System.Environment]::GetEnvironmentVariable('Path','User'); 
Write-Host '正在启动 Vite...' -ForegroundColor Yellow; 
npm run dev
"@ -PassThru -WindowStyle Normal

Write-Host "  前端启动中... (端口 5173)" -ForegroundColor Green
Write-Host ""

# 等待启动
Write-Host "等待服务启动..." -ForegroundColor Yellow
Start-Sleep -Seconds 10

Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  启动完成！" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "访问地址:" -ForegroundColor White
Write-Host "  前端：http://localhost:5173" -ForegroundColor Blue
Write-Host "  后端：http://localhost:8080" -ForegroundColor Blue
Write-Host ""
Write-Host "按任意键退出此窗口..." -ForegroundColor Gray

$null = $Host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown")
