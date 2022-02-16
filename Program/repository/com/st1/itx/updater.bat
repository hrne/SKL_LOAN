@echo off
:start
setlocal ENABLEDELAYEDEXPANSION
set TD=%~dp0
cd /d %TD%

:SVN_UPDATE
svn update

:SVN_ADD_NOTUNDERCONTROL
FOR /F "usebackq delims=" %%i IN (`svn status`) DO (
  set LN="%%i"
  set FC=!LN:~1,1!
  set FN=!LN:~9,-1!
  IF "!FC!"=="?" (
    svn add "!FN!"
  )
)

:SVN_COMMIT
set MSG="Auto-committed on %date% %time% from .5"
svn commit -m %MSG%

:end

echo.
echo Done
choice /T 5 /D N /N
exit