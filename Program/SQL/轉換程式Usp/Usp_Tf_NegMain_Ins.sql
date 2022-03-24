--------------------------------------------------------
--  DDL for Procedure Usp_Tf_NegMain_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "Usp_Tf_NegMain_Ins" 
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
    DELETE FROM "NegMain";

    -- 寫入資料
    INSERT INTO "NegMain"
    SELECT "tbJCICMain".RC_ACCOUNT        AS "CustNo"              -- 戶號 DECIMAL 7 0
          ,ROW_NUMBER() OVER (PARTITION BY "tbJCICMain".RC_ACCOUNT
                              ORDER BY "tbJCICMain".RC_ACCOUNT,"tbJCICMain".RC_DATE)
                                          AS "CaseSeq"             -- 案件序號 DECIMAL 3 0
          -- 若戶號 在 JCICZ050 存在 則為 1: 協商
          -- 若戶號 在 JCICZ450 存在 則為 2: 調解
          -- 若戶號 在 JCICZ067 存在 則為 3: 更生
          -- 4: 清算 條件未知
          ,CASE
             WHEN Z050."CUSTIDN" IS NOT NULL THEN '1' -- 1: 協商
             WHEN Z450."CUSTIDN" IS NOT NULL THEN '2' -- 2: 調解
             WHEN Z067."CUSTIDN" IS NOT NULL THEN '3' -- 3: 更生
           ELSE '4' END -- 4: 清算
                                          AS "CaseKindCode"        -- 案件種類 VARCHAR2 1 0
          ,CASE
             WHEN "tbJCICMain".CustStatus = '1'
                  AND NVL(Z046."TRANSACTIONSID",'D') IN ('A','C') --A新增 C異動
                  AND NVL(Z046."CLOSE_CODE",' ') = '00' -- 結案原因代碼有值，為00時
             THEN '2' -- 毀諾
             WHEN "tbJCICMain".CustStatus = '1'
                  AND NVL(Z046."TRANSACTIONSID",'D') IN ('A','C') --A新增 C異動
                  AND NVL(Z046."CLOSE_CODE",' ') != ' ' -- 結案原因代碼有值時
             THEN '3' -- 結案
             WHEN "tbJCICMain".CustStatus = '1'
             THEN '0'
             WHEN "tbJCICMain".CustStatus = '2'
             THEN '3'
             WHEN "tbJCICMain".CustStatus = '3'
             THEN '2'
           ELSE "tbJCICMain".CustStatus END
                                          AS "Status"              -- 戶況 VARCHAR2 1 0
          ,CASE
             WHEN CM."CustTypeCode" = '05'
             THEN '2'
           ELSE '1' END                   AS "CustLoanKind"        -- 債權戶別 VARCHAR2 1 0
          ,0                              AS "PayerCustNo"         -- 付款人戶號 DECIMAL 7 -- 2021-11-19 智偉修改
          ,0                              AS "DeferYMStart"        -- 延期繳款年月(起) DECIMAL 6 0
          ,0                              AS "DeferYMEnd"          -- 延期繳款年月(訖) DECIMAL 6 0
          ,"tbJCICMain".RC_DATE           AS "ApplDate"           -- 協商申請日 DecimalD 8 0
          ,"tbJCICMain".TOTAL_PERIOD_AMT  AS "DueAmt"              -- 月付金(期款) DECIMAL 16 2
          ,"tbJCICMain"."PERIOD"          AS "TotalPeriod"         -- 期數 DECIMAL 3 0
          ,"tbJCICMain".RATE              AS "IntRate"             -- 計息條件(利率) DECIMAL 6 4
          ,"tbJCICMain".FIRST_PAY_DATE    AS "FirstDueDate"        -- 首次應繳日 DecimalD 8 0
      --     , NVL("tbJCICMain".PAY_END_DATE,0)                    AS "LastDueDate"         -- 還款結束日 DecimalD 8 0
          -- ,TO_NUMBER(TO_CHAR(ADD_MONTHS(TO_DATE(TO_NUMBER("tbJCICMain".FIRST_PAY_DATE),'yyyymmdd'), TO_NUMBER("tbJCICMain"."PERIOD")),'yyyymmdd'))
                                          -- AS "LastDueDate"         -- 還款結束日 DecimalD 8 0
          ,TO_NUMBER(TO_CHAR(ADD_MONTHS(TO_DATE(TO_NUMBER("tbJCICMain".NEXT_PAY_DATE),'yyyymmdd'), TO_NUMBER("tbJCICMain"."PERIOD") - TO_NUMBER("tbJCICMain"."PAY_PERIOD") - 1),'yyyymmdd'))
                                          AS "LastDueDate"         -- 還款結束日 DecimalD 8 0
          ,"tbJCICMain".IsMaxBank         AS "IsMainFin"           -- 是否最大債權 VARCHAR2 1 0
          ,"tbJCICMain".TOTAL_RC_AMT      AS "TotalContrAmt"       -- 簽約總金額 DECIMAL 16 2
          ,"tbJCICMain".CREDIT_CODE       AS "MainFinCode"         -- 最大債權機構 VARCHAR2 8 0
          ,"tbJCICMain".AMT_BALANCE       AS "PrincipalBal"        -- 總本金餘額 DECIMAL 16 2
          ,"tbJCICMain".PAY_AMT           AS "AccuTempAmt"         -- 累暫收金額 DECIMAL 16 2
          ,"tbJCICMain".PAT_OVER_AMT      AS "AccuOverAmt"         -- 累溢收金額 DECIMAL 16 2
          ,CASE
             WHEN Z050."CUSTIDN" IS NOT NULL THEN NVL(Z050."PAYAMT_2",0)    -- 1: 協商
             WHEN Z450."CUSTIDN" IS NOT NULL THEN NVL(Z450."PAYAMT_2",0)    -- 2: 調解
             WHEN Z067."CUSTIDN" IS NOT NULL THEN 0 -- 3: 更生
           ELSE 0 END                     AS "AccuDueAmt"          -- 累期款金額 DECIMAL 16 2
          ,NVL(S2.SUM_SHARE_AMT,0)        AS "AccuSklShareAmt"     -- 累新壽分攤金額 DECIMAL 16 2
          ,"tbJCICMain".PAY_PERIOD        AS "RepaidPeriod"        -- 已繳期數 DECIMAL 3 0
          ,'N'                            AS "TwoStepCode"         -- 二階段註記 VARCHAR2 1 0
          ,0                              AS "ChgCondDate"         -- 申請變更還款條件日 DecimalD 8 0
          ,"tbJCICMain".NEXT_PAY_DATE     AS "NextPayDate"         -- 下次應繳日 DecimalD 8 0
          ,"tbJCICMain".PAY_INT_DATE      AS "PayIntDate"          -- 繳息迄日 DecimalD 8 0
          ,"tbJCICMain".REPAY_AMT         AS "RepayPrincipal"      -- 還本本金 DECIMAL 14 0
          ,"tbJCICMain".REPAY_INT         AS "RepayInterest"       -- 還本利息 DECIMAL 14 0
          ,"tbJCICMain".STATUS_DATE       AS "StatusDate"          -- 戶況日期 DecimalD 8 0
          ,NULL                           AS "CourCode"            -- 受理調解機構代號 VARCHAR2(3 BYTE)
          ,0                              AS "ThisAcDate"          -- 本次會計日期 NUMBER(8,0) 
          ,NULL                           AS "ThisTitaTlrNo"       -- 本次經辦 VARCHAR2(6 BYTE) 
          ,0                              AS "ThisTitaTxtNo"       -- 本次交易序號 NUMBER(8,0)
          ,0                              AS "LastAcDate"          -- 上次會計日期 NUMBER(8,0)
          ,NULL                           AS "LastTitaTlrNo"       -- 上次經辦 VARCHAR2(6 BYTE)
          ,0                              AS "LastTitaTxtNo"       -- 上次交易序號 NUMBER(8,0)
          ,JOB_START_TIME                 AS "CreateDate"          -- 建檔日期時間 DATE 8 0
          ,'999999'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 0
          ,JOB_START_TIME                 AS "LastUpdate"          -- 最後更新日期時間 DATE 8 0
          ,'999999'                       AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 0
    FROM "tbJCICMain"
    LEFT JOIN (SELECT "tbJCICAmtShare".CustIDN
                     ,SUM("tbJCICAmtShare".SHARE_AMT) AS SUM_SHARE_AMT
               FROM "tbJCICAmtShare"
               WHERE "tbJCICAmtShare".CREDIT_CODE = '458'
               GROUP BY "tbJCICAmtShare".CustIDN
              ) S2 ON S2.CustIDN = "tbJCICMain".CustIDN
    LEFT JOIN (
      SELECT "SUBMITID"
           , "CUSTIDN"
           , "RC_DATE"
           , "PAY_DATE"
           , "PAYAMT_2"
           ,  ROW_NUMBER() OVER (PARTITION BY "SUBMITID"
                                             ,"CUSTIDN"
                                             ,"RC_DATE"
                                 ORDER BY "PAY_DATE" DESC
                                ) AS "Seq"
      FROM "TBJCICZ050"
    ) Z050 ON Z050."CUSTIDN" = "tbJCICMain".CustIDN
          AND Z050."RC_DATE" = "tbJCICMain".RC_DATE
          AND Z050."Seq" = 1
    LEFT JOIN (
      SELECT "SUBMITID"
           , "CUSTIDN"
           , "APPLYDATE"
           , "PAY_DATE"
           , "PAYAMT_2"
           ,  ROW_NUMBER() OVER (PARTITION BY "SUBMITID"
                                             ,"CUSTIDN"
                                             ,"APPLYDATE"
                                 ORDER BY "PAY_DATE" DESC
                                ) AS "Seq"
      FROM "TBJCICZ450"
    ) Z450 ON Z450."CUSTIDN" = "tbJCICMain".CustIDN
          AND Z450."APPLYDATE" = "tbJCICMain".RC_DATE
          AND Z450."Seq" = 1
    LEFT JOIN (
      SELECT "SUBMITID"
           , "CUSTIDN"
           , "APPLYDATE"
           , "PAYDATE"
           , "TOTALPAYAMT"
           ,  ROW_NUMBER() OVER (PARTITION BY "SUBMITID"
                                             ,"CUSTIDN"
                                             ,"APPLYDATE"
                                 ORDER BY "PAYDATE" DESC
                                ) AS "Seq"
      FROM "TBJCICZ067"
    ) Z067 ON Z067."CUSTIDN" = "tbJCICMain".CustIDN
          AND Z067."APPLYDATE" = "tbJCICMain".RC_DATE
          AND Z067."Seq" = 1
    LEFT JOIN (
      SELECT "CUSTIDN"
           , "RC_DATE"
           , "CLOSE_DATE"
           , "CLOSE_CODE"
           , "TRANSACTIONSID"
           , ROW_NUMBER()
             OVER (
               PARTITION BY "CUSTIDN"
                          , "RC_DATE"
               ORDER BY "CLOSE_DATE" DESC
             ) AS "Seq"
      FROM "TBJCICZ046"
    ) Z046 ON Z046."CUSTIDN" = "tbJCICMain".CustIDN
          AND Z046."RC_DATE" = "tbJCICMain".RC_DATE
          AND Z046."Seq" = 1
    LEFT JOIN "CustMain" CM ON CM."CustNo" = "tbJCICMain".RC_ACCOUNT
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
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_NegMain_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;



/
