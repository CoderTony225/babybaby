@echo off
chcp 65001 >nul
echo ========================================
echo   无人机管理系统 - 后端启动脚本
echo ========================================
echo.

cd /d "%~dp0baseplatform"

echo [1] 清理旧的编译文件...
if exist "target" (
    echo     正在删除 target 目录...
    rmdir /s /q target
    echo     清理完成！
) else (
    echo     无需清理
)
echo.

echo [2] 启动 Spring Boot 后端...
echo     端口：8080
echo     数据库：SQLite (data/uav.db)
echo.

call mvnw.cmd clean spring-boot:run

pause
