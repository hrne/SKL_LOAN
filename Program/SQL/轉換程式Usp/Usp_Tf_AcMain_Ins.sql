--------------------------------------------------------
--  DDL for Procedure Usp_Tf_AcMain_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_Tf_AcMain_Ins" 
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
    EXECUTE IMMEDIATE 'ALTER TABLE "AcMain" DISABLE PRIMARY KEY CASCADE';
    EXECUTE IMMEDIATE 'TRUNCATE TABLE "AcMain" DROP STORAGE';
    EXECUTE IMMEDIATE 'ALTER TABLE "AcMain" ENABLE PRIMARY KEY';

    DECLARE 
        "TbsDyF" DECIMAL(8); --西元帳務日
    BEGIN

    SELECT "TbsDy" + 19110000
    INTO "TbsDyF"
    FROM "TxBizDate"
    WHERE "DateCode" = 'ONLINE'
    ;

    -- 寫入資料
    INSERT INTO "AcMain"
    SELECT '000'                          AS "AcBookCode"          -- 帳冊別 VARCHAR2 3 0
          ,CASE
             WHEN NVL(S2."ACTFSC",' ') = 'A'
             THEN '201'
           ELSE '00A' END                 AS "AcSubBookCode"       -- 區隔帳冊 VARCHAR2 3 0 -- 2021-07-15 新增
          ,'0000'                         AS "BranchNo"            -- 單位別 VARCHAR2 4 0
          ,'TWD'                          AS "CurrencyCode"        -- 幣別 VARCHAR2 3 0
          ,S3."AcNoCode"                  AS "AcNoCode"            -- 科目代號 VARCHAR2 11 0 -- 2021-07-15 修改 取11碼會科
          ,NVL(S2."CORACS",'     ')       AS "AcSubCode"           -- 子目代號 VARCHAR2 5 0
          ,CASE
             WHEN S3."AcNoCode" = '40903030000' -- 手續費收入－放款帳管費
             THEN '01'
           ELSE '  ' END                  AS "AcDtlCode"           -- 細目代號 VARCHAR2 2 0
          ,S1."TRXDAT"                    AS "AcDate"              -- 會計日期 Decimald 8 0
          ,SUM(CASE
             WHEN S1."LCDPDA" > 0 THEN S1."LCDPDA" -- 前日借方金額
           ELSE S1."LCDPCA" END) -- 前日貸方金額
                                          AS "YdBal"               -- 前日餘額 DECIMAL 16 2
          ,SUM(CASE -- "LCDPDA" > 0 表示為借方科目 , 用前日借方金額 + 本日借方金額 - 本日貸方金額
             WHEN S1."LCDPDA" > 0 THEN S1."LCDPDA" + S1."LDGCDA" - S1."LDGCCA"
           ELSE S1."LCDPCA" - S1."LDGCDA" + S1."LDGCCA" END) -- 貸方科目 , 用前日貸方金額 - 本日借方金額 + 本日貸方金額
                                          AS "TdBal"               -- 本日餘額 DECIMAL 16 2
          ,0                              AS "DbCnt"               -- 借方筆數 DECIMAL 8 0
          ,SUM(S1."LDGCDA")               AS "DbAmt"               -- 借方金額 DECIMAL 16 2
          ,0                              AS "CrCnt"               -- 貸方筆數 DECIMAL 8 0
          ,SUM(S1."LDGCCA")               AS "CrAmt"               -- 貸方金額 DECIMAL 16 2
          ,0                              AS "CoreDbCnt"           -- 核心借方筆數 DECIMAL 8 0
          ,0                              AS "CoreDbAmt"           -- 核心借方金額 DECIMAL 16 2
          ,0                              AS "CoreCrCnt"           -- 核心貸方筆數 DECIMAL 8 0
          ,0                              AS "CoreCrAmt"           -- 核心貸方金額 DECIMAL 16 2
          ,S3."AcctCode"                  AS "AcctCode"            -- 業務科目代號 VARCHAR2 3 0
          ,0                              AS "MonthEndYm"          -- 月底年月 DECIMAL 6 0
          ,JOB_START_TIME                 AS "CreateDate"          -- 建檔日期時間 DATE 0 0
          ,'999999'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 0
          ,JOB_START_TIME                 AS "LastUpdate"          -- 最後更新日期時間 DATE 0 0
          ,'999999'                       AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 0
    FROM "LA$LDGP" S1
    LEFT JOIN "TB$LCDP" S2 ON S2."ACNACC" = S1."ACNACC"
                          AND NVL(S2."ACNACS",' ') = NVL(S1."ACNACS",' ')
                          AND NVL(S2."ACNASS",' ') = NVL(S1."ACNASS",' ')
    LEFT JOIN "CdAcCode" S3 ON S3."AcNoCodeOld" = S2."CORACC" -- 2021-07-15 修改以8碼會科串接
                           AND S3."AcSubCode" = NVL(S2."CORACS",'     ')
                           AND S3."AcDtlCode" = '  '
    WHERE S1."TRXDAT" >= 20190101
      AND S1."TRXDAT" <= "TbsDyF"
      AND NVL(S2."CORACC",' ') <> ' ' -- 有串到新會科才寫入
      AND NVL(S3."AcNoCode",' ') <> ' ' -- 2021-07-15 新增判斷 有串到最新的11碼會科才寫入
    GROUP BY CASE
               WHEN NVL(S2."ACTFSC",' ') = 'A'
               THEN '201'
             ELSE '00A' END
           , S3."AcNoCode"
           , NVL(S2."CORACS",'     ') 
           , S1."TRXDAT"
           , S3."AcctCode"
    ;

    -- 記錄寫入筆數
    INS_CNT := INS_CNT + sql%rowcount;

    /* 更新 月底年月 "MonthEndYm" */
    MERGE INTO "AcMain" T1
    USING (
      SELECT TRUNC("AcDate" / 100) AS "MonthEndYm"
            ,MAX("AcDate") AS "MonthEndDate"
      FROM "AcMain"
      GROUP BY TRUNC("AcDate" / 100)
    ) S1 ON (    S1."MonthEndYm"   = TRUNC(T1."AcDate" / 100)
             AND S1."MonthEndDate" = T1."AcDate" )
    WHEN MATCHED THEN UPDATE SET
    T1."MonthEndYm" = S1."MonthEndYm"
    ;

    /* 更新 催收款項餘額 作法待確認??? */
    UPDATE "AcMain"
    SET "TdBal" = CASE
                    WHEN "AcDate" = 20201231
                    THEN 26834128
                    WHEN "AcDate" = 20210531
                    THEN 16683233
                  ELSE "TdBal" END
      , "YdBal" = CASE
                    WHEN "AcDate" = 20201231
                    THEN 26834128
                    WHEN "AcDate" = 20210531
                    THEN 16683233
                  ELSE "YdBal" END
    WHERE "AcctCode" = '990'
      AND "AcDate" IN (20201231,20210531)
    ;

    MERGE INTO "AcMain" AM
    USING (
      SELECT AR."AcctCode"
           , AR."AcBookCode"
           , AR."AcSubBookCode"
           , CA."AcNoCode"
           , CA."AcSubCode"
           , CA."AcDtlCode"
           , SUM(AR."RvBal") AS "RvBal"
      FROM "AcReceivable" AR
      LEFT JOIN "CdAcCode" CA ON CA."AcctCode" = AR."AcctCode"
      WHERE AR."ReceivableFlag" = '2'
      GROUP BY AR."AcctCode"
             , AR."AcBookCode"
             , AR."AcSubBookCode"
             , CA."AcNoCode"
             , CA."AcSubCode"
             , CA."AcDtlCode"
    ) AR
    ON (
      AM."AcctCode" = AR."AcctCode"
      AND AM."AcDate" = "TbsDyF"
    )
    WHEN MATCHED THEN UPDATE SET
      "YdBal" = AR."RvBal"
    , "TdBal" = AR."RvBal"
    , "DbAmt" = 0
    , "CrAmt" = 0
    WHEN NOT MATCHED THEN INSERT (
        "AcBookCode"          -- 帳冊別 VARCHAR2 3 0
      , "AcSubBookCode"       -- 區隔帳冊 VARCHAR2 3 0 -- 2021-07-15 新增
      , "BranchNo"            -- 單位別 VARCHAR2 4 0
      , "CurrencyCode"        -- 幣別 VARCHAR2 3 0
      , "AcNoCode"            -- 科目代號 VARCHAR2 11 0 -- 2021-07-15 修改 取11碼會科
      , "AcSubCode"           -- 子目代號 VARCHAR2 5 0
      , "AcDtlCode"           -- 細目代號 VARCHAR2 2 0
      , "AcDate"              -- 會計日期 Decimald 8 0
      , "YdBal"               -- 前日餘額 DECIMAL 16 2
      , "TdBal"               -- 本日餘額 DECIMAL 16 2
      , "DbCnt"               -- 借方筆數 DECIMAL 8 0
      , "DbAmt"               -- 借方金額 DECIMAL 16 2
      , "CrCnt"               -- 貸方筆數 DECIMAL 8 0
      , "CrAmt"               -- 貸方金額 DECIMAL 16 2
      , "CoreDbCnt"           -- 核心借方筆數 DECIMAL 8 0
      , "CoreDbAmt"           -- 核心借方金額 DECIMAL 16 2
      , "CoreCrCnt"           -- 核心貸方筆數 DECIMAL 8 0
      , "CoreCrAmt"           -- 核心貸方金額 DECIMAL 16 2
      , "AcctCode"            -- 業務科目代號 VARCHAR2 3 0
      , "MonthEndYm"          -- 月底年月 DECIMAL 6 0
      , "CreateDate"          -- 建檔日期時間 DATE 0 0
      , "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 0
      , "LastUpdate"          -- 最後更新日期時間 DATE 0 0
      , "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 0
    ) VALUES (
        AR."AcBookCode"
      , AR."AcSubBookCode"
      , '0000' -- "BranchNo"
      , 'TWD' -- "CurrencyCode"
      , AR."AcNoCode"
      , AR."AcSubCode"
      , AR."AcDtlCode"
      , "TbsDyF" -- "AcDate"
      , AR."RvBal" -- "YdBal"
      , AR."RvBal" -- "TdBal"
      , 0 -- "DbCnt"
      , 0 -- "DbAmt"
      , 0 -- "CrCnt"
      , 0 -- "CrAmt"
      , 0 -- "CoreDbCnt"
      , 0 -- "CoreDbAmt"
      , 0 -- "CoreCrCnt"
      , 0 -- "CoreCrAmt"
      , AR."AcctCode"
      , 0
      , JOB_START_TIME
      , '999999'
      , JOB_START_TIME
      , '999999'
    )
    ;
    -- 記錄程式結束時間
    JOB_END_TIME := SYSTIMESTAMP;

    commit;

    END;
    -- 例外處理
    Exception
    WHEN OTHERS THEN
    ERROR_MSG := SQLERRM || CHR(13) || CHR(10) || dbms_utility.format_error_backtrace;
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_AcMain_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;

/
