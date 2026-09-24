@echo off
for /f "usebackq tokens=1,* delims==" %%a in (`findstr /r /v "^$ ^#" .env`) do set "%%a=%%b"
gradlew.bat bootRun
