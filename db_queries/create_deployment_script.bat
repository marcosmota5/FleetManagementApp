@echo off

REM Define the initial path
set "CURRENT_PATH=%~dp0"

REM Define the output filename
set "OUTPUT_FILENAME=%CURRENT_PATH%\main_script.sql"

REM Define the path to the text file containing the input file paths
set "INPUT_FILELIST_PATH=%CURRENT_PATH%\file_list.txt"

REM Define the error log file
set "ERROR_LOG=%CURRENT_PATH%\execution_log.txt"

REM Get the current date and time in the desired format
for /f "delims=" %%a in ('wmic os get LocalDateTime ^| find "."') do set datetime=%%a
set "formattedDateTime=%datetime:~6,2%/%datetime:~4,2%/%datetime:~0,4% %datetime:~8,2%:%datetime:~10,2%:%datetime:~12,2%"

REM Count the total number of lines in the input file list
for /f %%A in ('find /c /v "" ^< "%INPUT_FILELIST_PATH%"') do set "TotalFiles=%%A"

REM Initialize the output file
type nul > "%OUTPUT_FILENAME%"

REM Initialize variables for the progress bar
setlocal enabledelayedexpansion
set /a "CurrentFile=0"
set "ErrorOccurred=false"

REM Write date and time to the error log
echo ------------------------ >> "%ERROR_LOG%"
echo Run started at: %formattedDateTime% >> "%ERROR_LOG%"
echo ------------------------ >> "%ERROR_LOG%"

REM Merge each file in the list
for /f "usebackq delims=" %%f in ("%INPUT_FILELIST_PATH%") do (
    set /a "CurrentFile+=1"
    set "line=%%f"
    REM Check if the line starts with a comment character
    echo !line! | findstr /b /c:"--" >nul
    if !errorlevel! equ 0 (
        REM Add the comment line to the output file
        echo. !line!>>"%OUTPUT_FILENAME%"
        echo. >>"%OUTPUT_FILENAME%"
    ) else (
        REM Extract the relative path from the full file path
        set "relativeFilePath=!line:%CURRENT_PATH%=!"
        REM Merge the contents of the file using the relative path
        type "%CURRENT_PATH%!relativeFilePath!" 2>> "%ERROR_LOG%" >> "%OUTPUT_FILENAME%"
        if !errorlevel! neq 0 (
            set "ErrorOccurred=true"
            echo Error in line !CurrentFile!: !line!>>"%ERROR_LOG%"
        )
        echo. >>"%OUTPUT_FILENAME%"
        echo. >>"%OUTPUT_FILENAME%"
    )

    REM Calculate and print the progress as a percentage
    set /a "Progress=CurrentFile * 100 / TotalFiles"
    cls
    echo Creating script: !Progress!%%
)

REM Display error log and message if errors occurred
if "%ErrorOccurred%"=="true" (
    cls
    echo Errors occurred. Check "%ERROR_LOG%" for details.
    pause
) else (
    REM Output a message indicating the operation has completed
    cls
    echo Done
    pause
)