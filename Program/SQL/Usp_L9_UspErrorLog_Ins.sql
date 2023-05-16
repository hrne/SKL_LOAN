--------------------------------------------------------
--  DDL for Procedure Usp_L9_UspErrorLog_Ins
--------------------------------------------------------
set define off;

CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_L9_UspErrorLog_Ins" 
(
    -- 參數
      UspName        IN VARCHAR2 -- 預存程序名稱 VARCHAR2 100
    , ErrorCode      IN NUMBER   -- 錯誤代碼 DECIMAL 14
    , ErrorMessage   IN VARCHAR2 -- 錯誤訊息 VARCHAR2 100
    , ErrorBackTrace IN VARCHAR2 -- ErrorBackTrace VARCHAR2 3000
    , ExecEmpNo      IN VARCHAR2 -- 啟動人員員編 VARCHAR2 6
)
AS
BEGIN
  DECLARE
      LOG_START_TIME TIMESTAMP; -- 記錄程式起始時間   
  BEGIN

  LOG_START_TIME := SYSTIMESTAMP;

  INSERT INTO "UspErrorLog"
  SELECT SYS_GUID()                 AS "LogUkey"         -- 記錄識別碼 VARCHAR2 32
       , TO_NUMBER(TO_CHAR(LOG_START_TIME,'YYYYMMDD'))
                                    AS "LogDate"         -- 記錄日期 DECIMAL 8
       , TO_NUMBER(TO_CHAR(LOG_START_TIME,'HH24MISS'))
                                    AS "LogTime"         -- 記錄時間 DECIMAL 6
       , CASE
           WHEN LENGTHB(UspName) > 100
           THEN SUBSTR(UspName,0,100)
         ELSE UspName
         END                        AS "UspName"         -- 預存程序名稱 VARCHAR2 100
       , ErrorCode                  AS "ErrorCode"       -- 錯誤代碼 DECIMAL 14
       , CASE
           WHEN LENGTHB(ErrorMessage) > 1500
           THEN SUBSTR(ErrorMessage,0,1500)
         ELSE ErrorMessage
         END                        AS "ErrorMessage"    -- 錯誤訊息 VARCHAR2 1500
       , CASE
           WHEN LENGTHB(ErrorBackTrace) > 1500
           THEN SUBSTR(ErrorBackTrace,0,1500)
         ELSE ErrorBackTrace
         END                        AS "ErrorBackTrace"  -- ErrorBackTrace VARCHAR2 1500
       , CASE
           WHEN LENGTHB(ExecEmpNo) > 6
           THEN SUBSTR(ExecEmpNo,0,6)
         ELSE ExecEmpNo
         END                        AS "ExecEmpNo"       -- 啟動人員員編 VARCHAR2 6
       , SYSTIMESTAMP               AS "CreateDate"      -- 建檔日期時間 DATE 
       , '999999'                   AS "CreateEmpNo"     -- 建檔人員 VARCHAR2 6
       , SYSTIMESTAMP               AS "LastUpdate"      -- 最後更新日期時間 DATE 
       , '999999'                   AS "LastUpdateEmpNo" -- 最後更新人員 VARCHAR2 6
  FROM DUAL
  ;

  -- 例外處理
  -- Exception
  -- WHEN OTHERS THEN
  END;
END;

/