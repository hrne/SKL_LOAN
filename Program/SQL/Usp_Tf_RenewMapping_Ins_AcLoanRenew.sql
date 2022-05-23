--------------------------------------------------------
--  DDL for Procedure Usp_Tf_RenewMapping_Ins_AcLoanRenew
--------------------------------------------------------
set define off;

  CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_Tf_RenewMapping_Ins_AcLoanRenew" 
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

    -- 寫入資料
    INSERT INTO "AcLoanRenew"
    SELECT S0."CustNo"                    AS "CustNo"              -- 戶號 DECIMAL 3
          ,S0."RenewFacmNo"               AS "NewFacmNo"           -- 新額度編號 DECIMAL 3
          ,S0."RenewBormNo"               AS "NewBormNo"           -- 新撥款序號 DECIMAL 3
          ,S0."CloseFacmNo"               AS "OldFacmNo"           -- 舊額度編號 DECIMAL 6
          ,S0."CloseBormNo"               AS "OldBormNo"           -- 舊撥款序號 DECIMAL 6
          ,CASE
             WHEN NVL(NA."LMSACN",0) != 0
             THEN '2' -- 2022-05-23 Wei 新增,案例提供by Linda 戶號402 在AS400只有建協議檔沒有建借新還舊檔
           ELSE '1' END                   AS "ReNewCode"           -- 展期記號 VARCHAR2 1 (1:一般 2:協議)
          ,CASE
             WHEN S0."Seq" = 1 -- 新撥款對到舊撥款 最早的一筆 為Y
             THEN 'Y'
           ELSE 'N' END                   AS "MainFlag"            -- 主要記號 VARCHAR2 1 (Y:新撥款對到舊撥款最早的一筆 )
          ,S0."AcDate"                    AS "AcDate"              -- 會計日期 DECIMAL 8
          ,'999999'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 
          ,JOB_START_TIME                 AS "CreateDate"          -- 建檔日期時間 DATE  
          ,'999999'                       AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 
          ,JOB_START_TIME                 AS "LastUpdate"          -- 最後更新日期時間 DATE  
    FROM (SELECT "AcDate"
               , "CustNo"
               , "CloseFacmNo"
               , "CloseBormNo"
               , "RenewFacmNo"
               , "RenewBormNo"
               , ROW_NUMBER() OVER (PARTITION BY "AcDate"
                                               , "CustNo"
                                    ORDER BY "CloseFacmNo"
                                           , "CloseBormNo"
                                           , "RenewFacmNo"
                                           , "RenewBormNo"
                                   ) AS "Seq"
           FROM (SELECT MAX("AcDate") AS "AcDate"
                      , "CustNo"
                      , "CloseFacmNo"
                      , "CloseBormNo"
                      , "RenewFacmNo"
                      , "RenewBormNo"
                  FROM "RenewMapping" R
                  WHERE "CloseTotal" = "RenewAmt"
                    AND "ADTYMT" IS NULL
                  GROUP BY "CustNo"
                         , "CloseFacmNo"
                         , "CloseBormNo"
                         , "RenewFacmNo"
                         , "RenewBormNo"
                 ) S
          ) S0
    LEFT JOIN (
      SELECT DISTINCT
             "LMSACN"
           , "LMSAPN"
           , "LMSASQ"
      FROM "LN$NODP"
      WHERE "CHGFLG" = 'A'
    ) NA ON NA."LMSACN" = S0."CustNo"
        AND NA."LMSAPN" = S0."RenewFacmNo"
        AND NA."LMSASQ" = S0."RenewBormNo"
    LEFT JOIN "AcLoanRenew" S1 ON S1."CustNo" = S0."CustNo"
                              AND S1."NewFacmNo" = S0."RenewFacmNo"
                              AND S1."NewBormNo" = S0."RenewBormNo"
                              AND S1."OldFacmNo" = S0."CloseFacmNo"
                              AND S1."OldBormNo" = S0."CloseBormNo"
    WHERE S1."CustNo" IS NULL
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
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_RenewMapping_Ins_AcLoanRenew',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;




/
