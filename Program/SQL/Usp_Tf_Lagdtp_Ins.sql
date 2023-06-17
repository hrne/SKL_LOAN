--------------------------------------------------------
--  DDL for Procedure Usp_Tf_Lagdtp_Ins
--------------------------------------------------------
set define off;

CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_Tf_Lagdtp_Ins" 
( 
    -- 參數 
    JOB_START_TIME OUT TIMESTAMP, --程式起始時間 
    JOB_END_TIME   OUT TIMESTAMP, --程式結束時間 
    INS_CNT        OUT INT,       --新增資料筆數 
    ERROR_MSG      OUT VARCHAR2   --錯誤訊息 
) 
AS 
BEGIN 
    -- 筆數預設0 
    INS_CNT:=0; 
    -- 記錄程式起始時間 
    JOB_START_TIME := SYSTIMESTAMP; 
 
    -- 刪除舊資料 
    EXECUTE IMMEDIATE 'ALTER TABLE "Lagdtp" DISABLE PRIMARY KEY CASCADE'; 
    EXECUTE IMMEDIATE 'TRUNCATE TABLE "Lagdtp" DROP STORAGE'; 
    EXECUTE IMMEDIATE 'ALTER TABLE "Lagdtp" ENABLE PRIMARY KEY'; 
 
    -- 寫入資料 
    INSERT INTO "Lagdtp" ( 
            "Cusbrh"
          , "Gdrid1"
          , "Gdrid2"
          , "Gdrnum"
          , "Loclid"
          , "Gdtidt"
          , "Gdtrdt"
          , "Gdtpty"
          , "Gdtp1a"
          , "Gdtp1m"
          , "Gdtp2a"
          , "Gdtp2m"
          , "Gdtp3a"
          , "Gdtp3m"
          , "Gdttmr"
          , "Aplpam"
          , "Lmsacn"
          , "Lmsapn"
          , "Gdtsdt"
          , "Gdttyp"
          , "Gdtapn"
          , "Estval"
          , "Rstval"
          , "Ettval"
          , "Risval"
          , "Rntval"
          , "Mgttyp"
          , "Mtgagm"
          , "Gdrnum2"
          , "Gdrmrk"
          , "UpdateIdent"
          , "CreateDate" -- '建檔日期時間';
          , "CreateEmpNo" -- '建檔人員';
          , "LastUpdate" -- '最後更新日期時間';
          , "LastUpdateEmpNo" -- '最後更新人員';
    )
    SELECT Cusbrh
         , Gdrid1
         , Gdrid2
         , Gdrnum
         , Loclid
         , Gdtidt
         , Gdtrdt
         , Gdtpty
         , Gdtp1a
         , Gdtp1m
         , Gdtp2a
         , Gdtp2m
         , Gdtp3a
         , Gdtp3m
         , Gdttmr
         , Aplpam
         , Lmsacn
         , Lmsapn
         , Gdtsdt
         , Gdttyp
         , Gdtapn
         , Estval
         , Rstval
         , Ettval
         , Risval
         , Rntval
         , Mgttyp
         , Mtgagm
         , Gdrnum2
         , Gdrmrk
         , UpdateIdent
         , JOB_START_TIME AS "CreateDate" -- 建檔日期時間 DATE 
         , '999999'       AS "CreateEmpNo" -- 建檔人員 VARCHAR2 6
         , JOB_START_TIME AS "LastUpdate" -- 最後更新日期時間 DATE 
         , '999999'       AS "LastUpdateEmpNo" -- 最後更新人員 VARCHAR2 6
    FROM LA$GDTP
    ; 
 
    -- 記錄寫入筆數 
    INS_CNT := INS_CNT + sql%rowcount; 

    -- 記錄程式結束時間 
    JOB_END_TIME := SYSTIMESTAMP; 
 
    commit; 
 
    -- 例外處理 
    Exception 
    WHEN OTHERS THEN 
    ERROR_MSG := SQLERRM || CHR(13) || CHR(10) || dbms_utility.format_error_backtrace; 
END;

/
