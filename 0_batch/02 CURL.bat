@echo on
@set SITE=http://localhost:8080
@set CURL=c:\tools\curl-7.58.0\bin\curl.exe
@set CURL=%CURL% -g -i -H "Accept: application/json" -H "Content-Type: application/json"
@set HR_YELLOW=@powershell -Command Write-Host "----------------------------------------------------------------------" -foreground "Yellow"
@set HR_RED=@powershell    -Command Write-Host "----------------------------------------------------------------------" -foreground "Red"

:create_account
%HR_YELLOW%
@powershell -Command Write-Host "POST create account" -foreground "Green"
%CURL% -X POST -d @data/starter.txt "%SITE%/create_account"
@echo.
pause

:exchange_currency
%HR_YELLOW%
@powershell -Command Write-Host "POST exchange currency" -foreground "Green"
%CURL% -X POST -d @data/exchangedFunds.txt "%SITE%/exchange_currency"
@echo.
pause

:account_statement
%HR_YELLOW%
@powershell -Command Write-Host "GET account statement" -foreground "Green"
%CURL% -X GET "%SITE%/account_statement?pesel=70010171133"
@echo.
pause

:finish
%HR_RED%
@powershell -Command Write-Host "FINISH" -foreground "Red"
pause