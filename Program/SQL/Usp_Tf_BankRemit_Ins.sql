--------------------------------------------------------
--  DDL for Procedure Usp_Tf_BankRemit_Ins
--------------------------------------------------------
set define off;

CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_Tf_BankRemit_Ins" 
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
    EXECUTE IMMEDIATE 'ALTER TABLE "BankRemit" DISABLE PRIMARY KEY CASCADE'; 
    EXECUTE IMMEDIATE 'TRUNCATE TABLE "BankRemit" DROP STORAGE'; 
    EXECUTE IMMEDIATE 'ALTER TABLE "BankRemit" ENABLE PRIMARY KEY'; 
 
    -- 寫入資料 
    INSERT INTO "BankRemit" ( 
        "AcDate" -- 會計日期 Decimald 8
      , "TitaTlrNo" -- 經辦 VARCHAR2 6
      , "TitaTxtNo" -- 交易序號 VARCHAR2 8
      , "BatchNo" -- 整批批號 VARCHAR2 6
      , "DrawdownCode" -- 撥款方式 DECIMAL 2
      , "StatusCode" -- 狀態 DECIMAL 1
      , "RemitBank" -- 匯款銀行 VARCHAR2 3
      , "RemitBranch" -- 匯款分行 VARCHAR2 4
      , "RemitAcctNo" -- 匯款帳號 VARCHAR2 14
      , "CustNo" -- 收款戶號 DECIMAL 7
      , "FacmNo" -- 額度編號 DECIMAL 3
      , "BormNo" -- 撥款序號 DECIMAL 3
      , "CustName" -- 收款戶名 NVARCHAR2 100
      , "CustId" -- 收款人ID VARCHAR2 10
      , "CustBirthday" -- 收款人出生日期 DECIMALD 8
      , "CustGender" -- 收款人性別 VARCHAR2 1
      , "Remark" -- 附言 NVARCHAR2 100
      , "CurrencyCode" -- 幣別 VARCHAR2 3
      , "RemitAmt" -- 匯款金額 DECIMAL 16
      , "AmlRsp" -- AML回應碼 varchar2 1
      , "ActFg" -- 交易進行記號 DECIMAL 1
      , "ModifyContent" -- 產檔後修正後內容 NVARCHAR2 500
      , "PayCode" -- 付款狀況碼 VARCHAR2 1
      , "CreateDate" -- 建檔日期時間 DATE 
      , "CreateEmpNo" -- 建檔人員 VARCHAR2 6
      , "LastUpdate" -- 最後更新日期時間 DATE 
      , "LastUpdateEmpNo" -- 最後更新人員 VARCHAR2 6
    )
    WITH txEmpData AS (
      SELECT DISTINCT
             TRXDAT
           , TRXMEM
           , TRXNMT
      FROM LA$TRXP
    )
    SELECT EXGP.TRXDAT                         AS "AcDate" -- 會計日期 Decimald 8
         , NVL(AEM1."EmpNo",'999999')          AS "TitaTlrNo" -- 經辦 VARCHAR2 6
         , LPAD(EXGP.TRXNMT,8,'0')             AS "TitaTxtNo" -- 交易序號 VARCHAR2 8
         , 'LN01  '                            AS "BatchNo" -- 整批批號 VARCHAR2 6
         , 1                                   AS "DrawdownCode" -- 撥款方式 DECIMAL 2
         , 0                                   AS "StatusCode" -- 狀態 DECIMAL 1
         , SUBSTR(LPAD(EXGP.EXGBBC,7,'0'),0,3) AS "RemitBank" -- 匯款銀行 VARCHAR2 3
         , SUBSTR(LPAD(EXGP.EXGBBC,7,'0'),4)   AS "RemitBranch" -- 匯款分行 VARCHAR2 4
         , EXGP.EXGACN                         AS "RemitAcctNo" -- 匯款帳號 VARCHAR2 14
         , EXGP.LMSACN                         AS "CustNo" -- 收款戶號 DECIMAL 7
         , EXGP.LMSAPN                         AS "FacmNo" -- 額度編號 DECIMAL 3
         , EXGP.LMSASQ                         AS "BormNo" -- 撥款序號 DECIMAL 3
         , LBM."CompensateAcct"                AS "CustName" -- 收款戶名 NVARCHAR2 100
         , LBM."RelationId"                    AS "CustId" -- 收款人ID VARCHAR2 10
         , LBM."RelationBirthday"              AS "CustBirthday" -- 收款人出生日期 DECIMALD 8
         , LBM."RelationGender"                AS "CustGender" -- 收款人性別 VARCHAR2 1
         , EXGP.M24070                         AS "Remark" -- 附言 NVARCHAR2 100
         , 'TWD'                               AS "CurrencyCode" -- 幣別 VARCHAR2 3
         , EXGP.EXGAMT                         AS "RemitAmt" -- 匯款金額 DECIMAL 16
         , '0'                                 AS "AmlRsp" -- AML回應碼 varchar2 1
         , 0                                   AS "ActFg" -- 交易進行記號 DECIMAL 1
         , ''                                  AS "ModifyContent" -- 產檔後修正後內容 NVARCHAR2 500
         , ''                                  AS "PayCode" -- 付款狀況碼 VARCHAR2 1
         , TO_DATE(EXGP.TRXDAT,'YYYYMMDD')     AS "CreateDate" -- 建檔日期時間 DATE 
         , NVL(AEM1."EmpNo",'999999')          AS "CreateEmpNo" -- 建檔人員 VARCHAR2 6
         , TO_DATE(EXGP.TRXDAT,'YYYYMMDD')     AS "LastUpdate" -- 最後更新日期時間 DATE 
         , NVL(AEM1."EmpNo",'999999')          AS "LastUpdateEmpNo" -- 最後更新人員 VARCHAR2 6
    FROM DAT_LA$EXGP EXGP
    LEFT JOIN "LoanBorMain" LBM ON LBM."CustNo" = EXGP.LMSACN
                               AND LBM."FacmNo" = EXGP.LMSAPN
                               AND LBM."BormNo" = EXGP.LMSASQ
    LEFT JOIN txEmpData t ON t.TRXDAT = EXGP.TRXDAT
                         AND t.TRXNMT = EXGP.TRXNMT
    LEFT JOIN "As400EmpNoMapping" AEM1 ON AME1."As400TellerNo" = t.TRXMEM
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
