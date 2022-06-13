--------------------------------------------------------
--  DDL for Procedure Usp_Tf_ClLandUnique_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_Tf_ClLandUnique_Ins" 
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
    -- EXECUTE IMMEDIATE 'ALTER TABLE "ClLandUnique" DISABLE PRIMARY KEY CASCADE';
    EXECUTE IMMEDIATE 'TRUNCATE TABLE "ClLandUnique" DROP STORAGE';
    -- EXECUTE IMMEDIATE 'ALTER TABLE "ClLandUnique" ENABLE PRIMARY KEY';

    -- -- 寫入資料 (舊擔保品編號檔)
    INSERT INTO "ClLandUnique"
    SELECT S1."TfFg"
          ,S1."GroupNo"
          ,S1."NewGroupNo"
          ,S1."LMSACN"
          ,S1."LMSAPN"
          ,S1."GDRID1"
          ,S1."GDRID2"
          ,S1."GDRNUM"
          ,S1."LGTSEQ"
    FROM "TmpLA$LGTP" S1
    ORDER BY S1."GroupNo"
            ,S1."LMSACN"
            ,S1."LMSAPN"
            ,S1."GDRID1"
            ,S1."GDRID2"
            ,S1."GDRNUM"
            ,S1."LGTSEQ"
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
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_ClLandUnique_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;



/
