--------------------------------------------------------
--  DDL for Procedure Usp_Tf_BankRelationCompany_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_Tf_BankRelationCompany_Ins" 
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
    EXECUTE IMMEDIATE 'ALTER TABLE "BankRelationCompany" DISABLE PRIMARY KEY CASCADE';
    EXECUTE IMMEDIATE 'TRUNCATE TABLE "BankRelationCompany" DROP STORAGE';
    EXECUTE IMMEDIATE 'ALTER TABLE "BankRelationCompany" ENABLE PRIMARY KEY';

    -- 寫入資料
    INSERT INTO "BankRelationCompany"
    SELECT S1."CUSTNAME"                  AS "CustName"          -- 借款戶所屬公司名稱 NVARCHAR2 70 0
          ,S1."CUSTID"                    AS "CustId"              -- 借款戶統編/親屬統編 VARCHAR2 11 0
          ,S1."COMPANYID"                 AS "CompanyId"        -- 關係企業統編 VARCHAR2 11 0
          ,NVL(S1."LAW001",'0')           AS "LAW001"              -- 金控法第44條 VARCHAR2 1 0
          ,NVL(S1."LAW002",'0')           AS "LAW002"              -- 金控法第44條(列項) VARCHAR2 1 0
          ,NVL(S1."LAW003",'0')           AS "LAW003"              -- 金控法第45條 VARCHAR2 1 0
          ,NVL(S1."LAW005",'0')           AS "LAW005"              -- 保險法(放款) VARCHAR2 1 0
          ,NVL(S1."LAW008",'0')           AS "LAW008"              -- 準利害關係人 VARCHAR2 1 0
          ,JOB_START_TIME                 AS "CreateDate"          -- 建檔日期時間 DATE 0 
          ,'999999'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 
          ,JOB_START_TIME                 AS "LastUpdate"          -- 最後更新日期時間 DATE 0 
          ,'999999'                       AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 0
    FROM "INFOR_BANKRELATIONCOMPANY" S1
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
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_BankRelationCompany_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;

/
