--------------------------------------------------------
--  DDL for Procedure Usp_Tf_BankRelationSuspected_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "Usp_Tf_BankRelationSuspected_Ins" 
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
    EXECUTE IMMEDIATE 'ALTER TABLE "BankRelationSuspected" DISABLE PRIMARY KEY CASCADE';
    EXECUTE IMMEDIATE 'TRUNCATE TABLE "BankRelationSuspected" DROP STORAGE';
    EXECUTE IMMEDIATE 'ALTER TABLE "BankRelationSuspected" ENABLE PRIMARY KEY';

    -- 寫入資料
    INSERT INTO "BankRelationSuspected"
    SELECT S1."REPCUSNAME"                AS "RepCusName"          -- 自然人姓名 NVARCHAR2 100
          ,S1."CUSTID"                    AS "CustId"              -- 該自然人擔任董事長之公司統一編號 VARCHAR2 11
          ,S1."CUSTNAME"                  AS "CustName"            -- 公司名稱 NVARCHAR2 100
          ,S1."SUBCOM"                    AS "SubCom"              -- 職務名稱 NVARCHAR2 100
          ,JOB_START_TIME                 AS "CreateDate"          -- 建檔日期時間 DATE 0 
          ,'999999'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 
          ,JOB_START_TIME                 AS "LastUpdate"          -- 最後更新日期時間 DATE 0 
          ,'999999'                       AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 0
    FROM "TPBANKRELATIONSUSPECTED" S1
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
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_BankRelationSuspected_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;


/
