CREATE OR REPLACE PROCEDURE "Usp_Tf_AcAcctCheck_Ins" 
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
    INSERT INTO "AcAcctCheck" (
        "AcDate"              -- 會計日期 Decimald 8  
      , "BranchNo"            -- 單位別 VARCHAR2 4  
      , "CurrencyCode"        -- 幣別 VARCHAR2 3  
      , "AcSubBookCode" 
      , "AcctCode"            -- 業務科目代號 VARCHAR2 3  
      , "AcctItem"            -- 業務科目名稱 NVARCHAR2 20  
      , "TdBal"               -- 本日餘額 DECIMAL 18 2 
      , "TdCnt"               -- 本日件數 DECIMAL 8  
      , "TdNewCnt"            -- 本日開戶件數 DECIMAL 8  
      , "TdClsCnt"            -- 本日結清件數 DECIMAL 8  
      , "TdExtCnt"            -- 本日展期件數 DECIMAL 8  
      , "TdExtAmt"            -- 本日展期金額 DECIMAL 18 2 
      , "ReceivableBal"       -- 銷帳檔餘額 DECIMAL 18 2 
      , "AcctMasterBal"       -- 業務檔餘額 DECIMAL 18 2 
      , "YdBal"               -- 前日餘額 DECIMAL 18 2
      , "DbAmt"               -- 借方金額 DECIMAL 18 2
      , "CrAmt"               -- 貸方金額 DECIMAL 18 2
      , "CoreDbAmt"           -- 核心借方金額 DECIMAL 18 2
      , "CoreCrAmt"           -- 核心貸方金額 DECIMAL 18 2
      , "CreateEmpNo"         -- 建檔人員 VARCHAR2 6  
      , "CreateDate"          -- 建檔日期時間 DATE   
      , "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6  
      , "LastUpdate"          -- 最後更新日期時間 DATE 
    )
    with acMap as (
      SELECT ACNACC
           , ACNACS
           , ACNASS
           , "AcNoCode"
           , "AcSubCode"
           , "AcDtlCode"
           , '00A' AS "AcSubBookCode"
      FROM "TfActblMapping"
      UNION
      SELECT L.ACNACC
           , L.ACNACS
           , L.ACNASS
           , C."AcNoCode"
           , C."AcSubCode"
           , C."AcDtlCode"
           -- 2023-04-06 Wei 增加 from Lai
           -- AcDetail AcSubBookCode轉換
           -- 依TB$LCDP.ACNAS區分
           -- 傳統A轉1
           -- 利變A轉201
           -- 利變B轉B
           -- 其他轉00A
           , CASE
               WHEN NVL(L.ACTFSC,' ') = '1'
               THEN '1'
               WHEN NVL(L.ACTFSC,' ') = 'A'
               THEN '201'
               WHEN NVL(L.ACTFSC,' ') = 'B'
               THEN 'B'
             ELSE '00A' END AS "AcSubBookCode"
      FROM "TB$LCDP" L
      LEFT JOIN "CdAcCode" C ON C."AcNoCodeOld" = L."CORACC" 
                            AND C."AcSubCode" = NVL(L."CORACS",'     ') 
                            AND C."AcDtlCode" = CASE 
                                                  WHEN L."CORACC" IN ('40903300' -- 放款帳管費 2022-06-30 Wei From Lai Email: 
                                                                     ,'20232020' -- 2022-09-08 Wei FROM yoko line 
                                                                     ,'20232182' -- 2022-09-08 Wei fix bug 
                                                                     ,'20232180' -- 2022-09-08 Wei fix bug 
                                                                     ,'20232181' -- 2022-09-08 Wei fix bug 
                                                                     ,'40907400'  -- 2022-09-08 Wei fix bug 
                                                                     )
                                                       AND NVL(L."CORACS",'     ') = '     ' 
                                                  THEN '01' 
                                                ELSE '  ' END
      WHERE NVL(C."AcNoCode",' ') != ' '
    )
    SELECT S1."TRXDAT"                    AS "AcDate"              -- 會計日期 Decimald 8  
          ,'0000'                         AS "BranchNo"            -- 單位別 VARCHAR2 4  
          ,'TWD'                          AS "CurrencyCode"        -- 幣別 VARCHAR2 3  
          ,S2."AcSubBookCode"             AS "AcSubBookCode" 
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
          ,SUM(CASE 
             WHEN S3."DbCr" = 'D' THEN S1."LCDPDA" - S1."LCDPCA" -- 前日借方金額 
           ELSE 0 - S1."LCDPDA" + S1."LCDPCA" END) -- 前日貸方金額 
                                          AS "YdBal"               -- 前日餘額 DECIMAL 18 2
          ,SUM(S1."LDGCDA")               AS "DbAmt"               -- 借方金額 DECIMAL 18 2
          ,SUM(S1."LDGCCA")               AS "CrAmt"               -- 貸方金額 DECIMAL 18 2
          ,0                              AS "CoreDbAmt"           -- 核心借方金額 DECIMAL 18 2
          ,0                              AS "CoreCrAmt"           -- 核心貸方金額 DECIMAL 18 2
          ,'999999'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6  
          ,JOB_START_TIME                 AS "CreateDate"          -- 建檔日期時間 DATE   
          ,'999999'                       AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6  
          ,JOB_START_TIME                 AS "LastUpdate"          -- 最後更新日期時間 DATE   
    FROM "LA$LDGP" S1 
    LEFT JOIN acMap S2 ON S2."ACNACC" = S1."ACNACC" 
                          AND NVL(S2."ACNACS",' ') = NVL(S1."ACNACS",' ') 
                          AND NVL(S2."ACNASS",' ') = NVL(S1."ACNASS",' ')
    LEFT JOIN "CdAcCode" S3 ON S3."AcNoCode" = S2."AcNoCode"
                           AND S3."AcSubCode" = S2."AcSubCode"
                           AND S3."AcDtlCode" = S2."AcDtlCode"
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
            ,S2."AcSubBookCode"
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
