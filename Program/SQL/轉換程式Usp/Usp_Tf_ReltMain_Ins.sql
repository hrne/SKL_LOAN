--------------------------------------------------------
--  DDL for Procedure Usp_Tf_ReltMain_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_Tf_ReltMain_Ins" 
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
    EXECUTE IMMEDIATE 'ALTER TABLE "ReltMain" DISABLE PRIMARY KEY CASCADE';
    EXECUTE IMMEDIATE 'TRUNCATE TABLE "ReltMain" DROP STORAGE';
    EXECUTE IMMEDIATE 'ALTER TABLE "ReltMain" ENABLE PRIMARY KEY';

    -- 寫入資料
    -- INSERT INTO "ReltMain"
    -- SELECT SYS_GUID()                     AS "ReltUKey"            -- 利害關係人識別碼 VARCHAR2 32 
    --       ,RL."CUSID1"                    AS "ReltId"              -- 利害關係人身分證字號 VARCHAR2 10 
    --       ,RL."RLTNAM"                    AS "ReltName"            -- 利害關係人姓名 NVARCHAR2 100 
    --       ,LPAD(RL."RLTLAB",2,'0')        AS "ReltCode"            -- 利害關係人職稱 VARCHAR2 2 
    --       ,19110101                       AS "EffectStartDate"     -- 生效起日 DecimalD 8 
    --       ,29101231                       AS "EffectEndDate"       -- 生效止日 DecimalD 8 
    --       ,JOB_START_TIME                 AS "CreateDate"          -- 建檔日期時間 DATE  
    --       ,'DataTf'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 
    --       ,JOB_START_TIME                 AS "LastUpdate"          -- 最後更新日期時間 DATE  
    --       ,'DataTf'                       AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 
    -- -- FROM "LA$NRTP" -- 2021-02-24 改用LA$RLTP
    -- FROM "LA$RLTP" RL
    -- ;

    -- 記錄寫入筆數
    INS_CNT := INS_CNT + sql%rowcount;

    -- 記錄程式結束時間
    JOB_END_TIME := SYSTIMESTAMP;

    commit;

    -- 例外處理
    Exception
    WHEN OTHERS THEN
    ERROR_MSG := SQLERRM;
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_ReltMain_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;



/
