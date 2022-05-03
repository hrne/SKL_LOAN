--------------------------------------------------------
--  DDL for Procedure Usp_Tf_AcAcctCheck_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_Tf_AcAcctCheck_Ins" 
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
    EXECUTE IMMEDIATE 'ALTER TABLE "AcAcctCheck" DISABLE PRIMARY KEY CASCADE';
    EXECUTE IMMEDIATE 'TRUNCATE TABLE "AcAcctCheck" DROP STORAGE';
    EXECUTE IMMEDIATE 'ALTER TABLE "AcAcctCheck" ENABLE PRIMARY KEY';

    -- 寫入資料
    INSERT INTO "AcAcctCheck"
    SELECT S1."TRXDAT"                    AS "AcDate"              -- 會計日期 Decimald 8 
          ,'0000'                         AS "BranchNo"            -- 單位別 VARCHAR2 4 
          ,'TWD'                          AS "CurrencyCode"        -- 幣別 VARCHAR2 3 
          ,CASE
             WHEN NVL(S2."ACTFSC",'00A') = '00A'
             THEN '00A'
             WHEN NVL(S2."ACTFSC",'00A') = 'A'
             THEN '201'
           ELSE 'XXX' END                 AS "AcSubBookCode"
          ,S3."AcctCode"                  AS "AcctCode"            -- 業務科目代號 VARCHAR2 3 
          ,S3."AcctItem"                  AS "AcctItem"            -- 業務科目名稱 NVARCHAR2 20 
          ,0                              AS "TdBal"               -- 本日餘額 DECIMAL 18 2
          ,SUM(  S1."ACSLAN"
               + S1."ACSTNA"
               - S1."ACSTCA"
               + S1."LDGEIC"
               - S1."LDGETC"
              )                           AS "TdCnt"               -- 本日件數 DECIMAL 8 
          ,SUM(S1."ACSTNA")               AS "TdNewCnt"            -- 本日開戶件數 DECIMAL 8 
          ,SUM(S1."ACSTCA")               AS "TdClsCnt"            -- 本日結清件數 DECIMAL 8 
          ,SUM(S1."LDGETC")               AS "TdExtCnt"            -- 本日展期件數 DECIMAL 8 
          ,SUM(S1."LDGETA")               AS "TdExtAmt"            -- 本日展期金額 DECIMAL 18 2
          ,0                              AS "ReceivableBal"       -- 銷帳檔餘額 DECIMAL 18 2
          ,0                              AS "AcctMasterBal"       -- 業務檔餘額 DECIMAL 18 2
          ,'999999'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 
          ,JOB_START_TIME                 AS "CreateDate"          -- 建檔日期時間 DATE  
          ,'999999'                       AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 
          ,JOB_START_TIME                 AS "LastUpdate"          -- 最後更新日期時間 DATE  
    FROM "LA$LDGP" S1
    LEFT JOIN "TB$LCDP" S2 ON S2."ACNACC" = S1."ACNACC"
                          AND NVL(S2."ACNACS",' ') = NVL(S1."ACNACS",' ')
                          AND NVL(S2."ACNASS",' ') = NVL(S1."ACNASS",' ')
    LEFT JOIN "CdAcCode" S3 ON S3."AcNoCodeOld" = S2."CORACC"
                           AND S3."AcSubCode" = NVL(S2."CORACS",'     ')
                           AND S3."AcDtlCode" = '  '
    WHERE S1."TRXDAT" >= 20190101
      AND NVL(S2."CORACC",' ') <> ' ' -- 有串到新會科才寫入
      AND NVL(S3."AcctCode",' ') <> ' '
    GROUP BY S1."TRXDAT"
            ,S3."AcctCode"
            ,S3."AcctItem"
            ,CASE
               WHEN NVL(S2."ACTFSC",'00A') = '00A'
               THEN '00A'
               WHEN NVL(S2."ACTFSC",'00A') = 'A'
               THEN '201'
             ELSE 'XXX' END
    ORDER BY S1."TRXDAT"
            ,S3."AcctCode"
            ,S3."AcctItem"
            ,CASE
               WHEN NVL(S2."ACTFSC",'00A') = '00A'
               THEN '00A'
               WHEN NVL(S2."ACTFSC",'00A') = 'A'
               THEN '201'
             ELSE 'XXX' END
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
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_AcAcctCheck_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;



/
