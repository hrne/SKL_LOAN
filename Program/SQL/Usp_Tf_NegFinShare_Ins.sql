--------------------------------------------------------
--  DDL for Procedure Usp_Tf_NegFinShare_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_Tf_NegFinShare_Ins" 
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
    DELETE FROM "NegFinShare";

    -- 寫入資料
    INSERT INTO "NegFinShare"
    SELECT M."CustNo"                         AS "CustNo"              -- 債務人戶號 DECIMAL 7 0
          ,M."CaseSeq"                        AS "CaseSeq"             -- 案件序號 DECIMAL 3 0
          ,NVL(S.CREDIT_CODE,' ')             AS "FinCode"             -- 債權機構 VARCHAR2 8 0
          ,NVL(S.RC_AMT,0)                    AS "ContractAmt"         -- 簽約金額 DECIMAL 16 2
          ,NVL(S.CREDIT_RATE,0)               AS "AmtRatio"            -- 債權比例%  DECIMAL 5 2
          ,NVL(S.PERIOD_AMT,0)                AS "DueAmt"              -- 期款 DECIMAL 16 2
          ,JOB_START_TIME                     AS "CreateDate"          -- 建檔日期時間 DATE 8 0
          ,S.MODIFYUSERID                     AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 0
          ,JOB_START_TIME                     AS "LastUpdate"          -- 最後更新日期時間 DATE 8 0
          ,S.MODIFYUSERID                     AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 0
    FROM REMIN_TBJCICSHARE S
    LEFT JOIN REMIN_TBJCICMAIN JM ON JM.CustIDN = S.CustIDN
                                 AND JM.RC_DATE = S.RC_DATE
    LEFT JOIN "NegMain" M ON M."CustNo"   = JM.RC_ACCOUNT
                         AND M."ApplDate" = JM.RC_DATE
    WHERE NVL(S.DEL_DATE,0) = 0 -- 2023-02-02 Wei from Linda 有註銷日期的不寫入
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
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_NegFinShare_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;



/
