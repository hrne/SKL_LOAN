--------------------------------------------------------
--  DDL for Procedure Usp_Tf_NegTrans_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_Tf_NegTrans_Ins" 
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
    DELETE FROM "NegTrans";

    -- 寫入資料ACCOUNT_DATE
    INSERT INTO "NegTrans" (
        "AcDate"          -- 會計日期 DecimalD 8 0
      , "TitaTlrNo"       -- 經辦 VARCHAR2 6 0
      , "TitaTxtNo"       -- 交易序號 DECIMAL 8 0
      , "CustNo"          -- 戶號 DECIMAL 7 0
      , "CaseSeq"         -- 案件序號 DECIMAL 3 0
      , "EntryDate"       -- 入帳日期 DecimalD 8 0
      , "TxStatus"        -- 交易狀態 DECIMAL 1 0
      , "TxKind"          -- 交易別 VARCHAR2 1 0
      , "TxAmt"           -- 交易金額 DECIMAL 16 2
      , "PrincipalBal"    -- 本金餘額 DECIMAL 16 2
      , "ReturnAmt"       -- 退還金額 DECIMAL 16 2
      , "SklShareAmt"     -- 新壽攤分 DECIMAL 16 2
      , "ApprAmt"         -- 撥付金額 DECIMAL 16 2
      , "ExportDate"      -- 撥付製檔日 DecimalD 8 0
      , "ExportAcDate"    -- 撥付出帳日 DecimalD 8 0
      , "TempRepayAmt"    -- 暫收抵繳金額 DECIMAL 16 2
      , "OverRepayAmt"    -- 溢收抵繳金額 DECIMAL 16 2
      , "PrincipalAmt"    -- 本金金額 DECIMAL 16 2
      , "InterestAmt"     -- 利息金額 DECIMAL 16 2
      , "OverAmt"         -- 轉入溢收金額 DECIMAL 16 2
      , "IntStartDate"    -- 繳息起日 DecimalD 8 0
      , "IntEndDate"      -- 繳息迄日 DecimalD 8 0
      , "RepayPeriod"     -- 還款期數 DECIMAL 3 0
      , "RepayDate"       -- 入帳還款日期 DecimalD 8 0
      , "OrgAccuOverAmt"  -- 累溢繳款(交易前) DECIMAL 16 2
      , "AccuOverAmt"     -- 累溢繳款(交易後) DECIMAL 16 2
      , "ShouldPayPeriod" -- 本次應還期數 DECIMAL 3 0
      , "DueAmt"          -- 期金 DECIMAL 16 2
      , "ThisEntdy"       -- 本次交易日 NUMBER(8,0)
      , "ThisKinbr"       -- 本次分行別 VARCHAR2(4 BYTE)
      , "ThisTlrNo"       -- 本次交易員代號 VARCHAR2(6 BYTE)
      , "ThisTxtNo"       -- 本次交易序號 VARCHAR2(8 BYTE)
      , "ThisSeqNo"       -- 本次序號 VARCHAR2(30 BYTE)
      , "LastEntdy"       -- 上次交易日 NUMBER(8,0)
      , "LastKinbr"       -- 上次分行別 VARCHAR2(4 BYTE)
      , "LastTlrNo"       -- 上次交易員代號 VARCHAR2(6 BYTE)
      , "LastTxtNo"       -- 上次交易序號 VARCHAR2(8 BYTE)
      , "LastSeqNo"       -- 上次序號 VARCHAR2(30 BYTE)
      , "CreateDate"      -- 建檔日期時間 DATE 8 0
      , "CreateEmpNo"     -- 建檔人員 VARCHAR2 6 0
      , "LastUpdate"      -- 最後更新日期時間 DATE 8 0
      , "LastUpdateEmpNo" -- 最後更新人員 VARCHAR2 6 0
    )
    SELECT MAP."AcDate"                            AS "AcDate"          -- 會計日期 DecimalD 8 0
          ,MAP."TitaTlrNo"                         AS "TitaTlrNo"       -- 經辦 VARCHAR2 6 0
          ,MAP."TitaTxtNo"                         AS "TitaTxtNo"       -- 交易序號 DECIMAL 8 0
          ,NM."CustNo"                             AS "CustNo"          -- 戶號 DECIMAL 7 0
          ,NM."CaseSeq"                            AS "CaseSeq"         -- 案件序號 DECIMAL 3 0
          ,BS.ENTER_DATE                           AS "EntryDate"       -- 入帳日期 DecimalD 8 0
          -- ,CASE
          --    WHEN BS.BUSINESS_KIND = 1 -- 客戶繳款 = 未入帳
          --    THEN 0
          --  ELSE 2
          --  END                                     AS "TxStatus"        -- 交易狀態 DECIMAL 1 0
          ,2                                       AS "TxStatus"        -- 交易狀態 DECIMAL 1 0
          -- 2023/8/16from Linda
          -- NegTrans 交易別的轉檔規則
          -- 催收債協系統      新貸中
          -- 3:提前還本 => 3:提前還本
          -- 4:提前清償 => 5:提前清償
          -- 5:註銷        => 8:註銷
          -- 其他交易別轉到新貸中都放0:正常
          -- 有告知盈倩上述規則
          ,CASE
             WHEN BS.BUSINESS_KIND = 3 -- 提前還本
             THEN '3' -- 提前還本
             WHEN BS.BUSINESS_KIND = 4 -- 提前清償
             THEN '5' -- 提前清償
             WHEN BS.BUSINESS_KIND = 5 --註銷
             THEN '8' -- 註銷
           ELSE '0' -- 正常
           END                                     AS "TxKind"          -- 交易別 VARCHAR2 1 0
          ,NVL(BS.TEMP_CREDIT,0)                   AS "TxAmt"           -- 交易金額 DECIMAL 16 2
          ,NVL(BS.AMT_BALANCE,0)                   AS "PrincipalBal"    -- 本金餘額 DECIMAL 16 2
          -- ,CASE
          --    WHEN NVL(BS.TEMP_CREDIT_LOAN_AMT,0) < 0
          --    THEN ABS(NVL(BS.TEMP_CREDIT_LOAN_AMT,0))
          --  ELSE 0 END                              AS "ReturnAmt"       -- 退還金額 DECIMAL 16 2
          ,0                                       AS "ReturnAmt"       -- 退還金額 DECIMAL 16 2
          ,NVL(S1.SUM_SHARE_AMT,0)                 AS "SklShareAmt"     -- 新壽攤分 DECIMAL 16 2
          ,NVL(S2.SUM_SHARE_AMT,0)                 AS "ApprAmt"         -- 撥付金額 DECIMAL 16 2
          ,NVL(BS.ACCOUNT_DATE,0)                  AS "ExportDate"      -- 撥付製檔日 DecimalD 8 0
          ,NVL(BS.ACCOUNT_DATE,0)                  AS "ExportAcDate"    -- 撥付出帳日 DecimalD 8 0
          ,NVL(BS.TEMP_CREDIT_LOAN_AMT,0)          AS "TempRepayAmt"    -- 暫收抵繳金額 DECIMAL 16 2
          ,NVL(BS.TEMP_CREDIT_OVER_AMT,0)          AS "OverRepayAmt"    -- 溢收抵繳金額 DECIMAL 16 2
          ,NVL(BS.WRITEOFF_AMT,0)                  AS "PrincipalAmt"    -- 本金金額 DECIMAL 16 2
          ,NVL(BS.WRITEOFF_INT,0)                  AS "InterestAmt"     -- 利息金額 DECIMAL 16 2
          ,NVL(BS.INTO_OVER_AMT,0)                 AS "OverAmt"         -- 轉入溢收金額 DECIMAL 16 2
          ,NVL(BS.PAY_START_DATE,0)                AS "IntStartDate"    -- 繳息起日 DecimalD 8 0
          ,NVL(BS.PAY_INT_DATE,0)                  AS "IntEndDate"      -- 繳息迄日 DecimalD 8 0
          ,NVL(BS.PAY_PERIOD,0)                    AS "RepayPeriod"     -- 還款期數 DECIMAL 3 0
          ,NVL(BS.ACCOUNT_DATE,0)                  AS "RepayDate"       -- 入帳還款日期 DecimalD 8 0
          ,0                                       AS "OrgAccuOverAmt"  -- 累溢繳款(交易前) DECIMAL 16 2
          ,0                                       AS "AccuOverAmt"     -- 累溢繳款(交易後) DECIMAL 16 2
          ,0                                       AS "ShouldPayPeriod" -- 本次應還期數 DECIMAL 3 0
          ,0                                       AS "DueAmt"          -- 期金 DECIMAL 16 2
          ,0                                       AS "ThisEntdy"       -- 本次交易日 NUMBER(8,0)
          ,NULL                                    AS "ThisKinbr"       -- 本次分行別 VARCHAR2(4 BYTE)
          ,NULL                                    AS "ThisTlrNo"       -- 本次交易員代號 VARCHAR2(6 BYTE)
          ,NULL                                    AS "ThisTxtNo"       -- 本次交易序號 VARCHAR2(8 BYTE)
          ,NULL                                    AS "ThisSeqNo"       -- 本次序號 VARCHAR2(30 BYTE)
          ,0                                       AS "LastEntdy"       -- 上次交易日 NUMBER(8,0)
          ,NULL                                    AS "LastKinbr"       -- 上次分行別 VARCHAR2(4 BYTE)
          ,NULL                                    AS "LastTlrNo"       -- 上次交易員代號 VARCHAR2(6 BYTE)
          ,NULL                                    AS "LastTxtNo"       -- 上次交易序號 VARCHAR2(8 BYTE)
          ,NULL                                    AS "LastSeqNo"       -- 上次序號 VARCHAR2(30 BYTE)
          ,LASTUPDATEDATE                          AS "CreateDate"      -- 建檔日期時間 DATE 8 0
          ,'999999'                                AS "CreateEmpNo"     -- 建檔人員 VARCHAR2 6 0
          ,LASTUPDATEDATE                          AS "LastUpdate"      -- 最後更新日期時間 DATE 8 0
          ,'999999'                                AS "LastUpdateEmpNo" -- 最後更新人員 VARCHAR2 6 0
    FROM "NegTranNoMapping" MAP
    LEFT JOIN REMIN_TBJCICBUSINESS BS ON BS.CustIDN       = MAP."CustIDN"
                                     AND BS.RC_DATE       = MAP."RC_DATE"
                                     AND BS.ACCOUNT_DATE  = MAP."ACCOUNT_DATE"
                                     AND BS.BUSINESS_CODE = MAP."BUSINESS_CODE"
    LEFT JOIN (SELECT CustIDN
                     ,RC_DATE
                     ,ACCOUNT_DATE
                     ,BUSINESS_CODE
                     ,ExportDate
                     ,SUM(SHARE_AMT) AS SUM_SHARE_AMT
               FROM REMIN_TBJCICAMTSHARE
               WHERE CREDIT_CODE = '458'
               GROUP BY CustIDN
                       ,RC_DATE
                       ,ACCOUNT_DATE
                       ,BUSINESS_CODE
                       ,ExportDate
              ) S1 ON S1.CustIDN       = BS.CustIDN
                  AND S1.RC_DATE       = BS.RC_DATE
                  AND S1.ACCOUNT_DATE  = BS.ACCOUNT_DATE
                  AND S1.BUSINESS_CODE = BS.BUSINESS_CODE
    LEFT JOIN (SELECT CustIDN
                     ,RC_DATE
                     ,ACCOUNT_DATE
                     ,BUSINESS_CODE
                     ,MAX(ExportDate) AS ExportDate
                     ,SUM(SHARE_AMT) AS SUM_SHARE_AMT
               FROM REMIN_TBJCICAMTSHARE
               WHERE CREDIT_CODE <> '458'
               GROUP BY CustIDN
                       ,RC_DATE
                       ,ACCOUNT_DATE
                       ,BUSINESS_CODE
              ) S2 ON S2.CustIDN       = BS.CustIDN
                  AND S2.RC_DATE       = BS.RC_DATE
                  AND S2.ACCOUNT_DATE  = BS.ACCOUNT_DATE
                  AND S2.BUSINESS_CODE = BS.BUSINESS_CODE
    LEFT JOIN REMIN_TBJCICMAIN JM ON JM.CustIDN = BS.CustIDN
                                 AND JM.RC_DATE = BS.RC_DATE
    LEFT JOIN "NegMain" NM ON NM."CustNo" = JM.RC_ACCOUNT
                          AND NM."ApplDate" = JM.RC_DATE
    WHERE NM."CustNo" IS NOT NULL
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
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_NegTrans_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;




/
