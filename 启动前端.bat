@echo off
chcp 65001 >nul
echo ========================================
echo   无人机管理系统 - 前端启动脚本
echo ========================================
echo.

cd /d "%~dp0frontend"

echo [1] 检查 Node.js 环境...
where node >nul 2>&1
if errorlevel 1 (
    echo [错误] 未检测到 Node.js！
    echo.
    echo 请先安装 Node.js:
    echo 1. 访问 https://nodejs.org/
    echo 2. 下载并安装 LTS 版本
    echo 3. 重启此脚本
    echo.
    pause
    exit /b 1
)

echo Node.js 已安装
node --version
echo.

echo [2] 检查依赖是否已安装...
if not exist "node_modules" (
    echo     首次运行，正在安装依赖...
    echo     这可能需要几分钟...
    echo.
    call npm install --legacy-peer-deps
    if errorlevel 1 (
        echo [错误] 依赖安装失败！
        pause
        exit /b 1
    )
    echo     依赖安装完成！
    echo.
) else (
    echo     依赖已存在
    echo.
)

echo [3] 清理 Vite 缓存...
if exist "node_modules\.vite" (
    rmdir /s /q node_modules\.vite
    echo     Vite 缓存已清理
)
echo.

echo [4] 启动 Vite 开发服务器...
echo     访问地址：http://localhost:5173
echo     后端代理：http://localhost:8080
echo.

call npm run dev

pause
