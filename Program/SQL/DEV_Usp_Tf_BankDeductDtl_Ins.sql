--------------------------------------------------------
--  DDL for Procedure Usp_Tf_BankDeductDtl_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "Usp_Tf_BankDeductDtl_Ins" 
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
    EXECUTE IMMEDIATE 'ALTER TABLE "BankDeductDtl" DISABLE PRIMARY KEY CASCADE';
    EXECUTE IMMEDIATE 'TRUNCATE TABLE "BankDeductDtl" DROP STORAGE';
    EXECUTE IMMEDIATE 'ALTER TABLE "BankDeductDtl" ENABLE PRIMARY KEY';

    -- 寫入資料
    INSERT INTO "BankDeductDtl"
    WITH "TmpAchDeductMedia" AS (
      SELECT "MediaDate"
            ,"MediaKind"
            ,"CustNo"
            ,"FacmNo"
            ,"RepayType"
            ,"MediaSeq"
            ,ROW_NUMBER() OVER (PARTITION BY "MediaDate"
                                            ,"MediaKind"
                                            ,"CustNo"
                                            ,"FacmNo"
                                            ,"RepayType"
                                ORDER BY "MediaDate"
                                        ,"MediaKind"
                                        ,"CustNo"
                                        ,"FacmNo"
                                        ,"RepayType"
                                        ,"MediaSeq") AS "Seq"
      FROM "AchDeductMedia"
    )
    , "ADM" AS (
      SELECT "MediaDate"
            ,"MediaKind"
            ,"CustNo"
            ,"FacmNo"
            ,"RepayType"
            ,"MediaSeq"
      FROM "TmpAchDeductMedia"
      WHERE "Seq" = 1
    )
    , "DupData" AS (
      SELECT "TRXIDT"
            ,"LMSACN"
            ,"MBKAPN"
            ,"LMSLPD"
            ,"MBKTRX"
      FROM "LA$MBKP"
      GROUP BY "TRXIDT"
              ,"LMSACN"
              ,"MBKAPN"
              ,"LMSLPD"
              ,"MBKTRX"
      HAVING COUNT(*) >= 2
    )
    SELECT MBK."TRXIDT"                   AS "EntryDate"           -- 入帳日期 Decimald 8 0
          ,MBK."LMSACN"                   AS "CustNo"              -- 戶號 DECIMAL 7 0
          ,MBK."MBKAPN"                   AS "FacmNo"              -- 額度 DECIMAL 3 0
          ,CASE MBK."MBKTRX"
             WHEN '1' THEN '5' -- 保險費
             WHEN '2' THEN '1' -- 期款
             WHEN '3' THEN '4' -- 帳管費
             WHEN '4' THEN '6' -- 契變手續費
           ELSE '0' END                   AS "RepayType"           -- 還款類別 DECIMAL 2 0
          ,MBK."LMSLPD"                   AS "PayIntDate"          -- 應繳日 Decimald 8 0
          ,MBK."TRXISD"                   AS "PrevIntDate"         -- 繳息迄日 Decimald 8 0
          ,MBK."ACTACT"                   AS "AcctCode"            -- 科目 VARCHAR2 3 0
          ,CASE
             WHEN MBK."LMSPBK" = '1' THEN '812'
             WHEN MBK."LMSPBK" = '2' THEN '006'
             WHEN MBK."LMSPBK" = '3' THEN '700'
             WHEN MBK."LMSPBK" = '4' THEN '103'
           ELSE '000' END                 AS "RepayBank"           -- 扣款銀行 VARCHAR2 3 0
          ,LPAD(MBK."LMSPCN",14,'0')      AS "RepayAcctNo"         -- 扣款帳號 VARCHAR2 14 0
          ,0                              AS "RepayAcctSeq"        -- 帳號碼 VARCHAR2 2 0
          ,0                              AS "UnpaidAmt"           -- 應扣金額 DECIMAL 14 0
          ,0                              AS "TempAmt"             -- 暫收抵繳金額 DECIMAL 14 0
          ,MBK."MBKAMT"                   AS "RepayAmt"            -- 扣款金額 DECIMAL 14 0
          ,MBK."TRXISD"                   AS "IntStartDate"        -- 計息起日 Decimald 8 0
          ,MBK."TRXIED"                   AS "IntEndDate"          -- 計息迄日 Decimald 8 0
          ,APLP."POSCDE"                  AS "PostCode"            -- 郵局存款別 VARCHAR2 1 0
          ,MBK."MBKCDE"                   AS "MediaCode"           -- 媒體碼 VARCHAR2 1 0
          ,MBK."LMSPRL"                   AS "RelationCode"        -- 與借款人關係 VARCHAR2 2 0
          ,MBK."LMSPAN"                   AS "RelCustName"         -- 第三人帳戶戶名 NVARCHAR2 100 0
          ,MBK."LMSPID"                   AS "RelCustId"           -- 第三人身分證字號 VARCHAR2 10 0
          ,0                              AS "RelAcctBirthday"     -- 第三人出生日期 decimal 8 0
          ,''                             AS "RelAcctGender"       -- 第三人性別 varchar2 1
          ,MBK."TRXIDT"                   AS "MediaDate"           -- 媒體日期 DECIMAL 8 0
          ,CASE
             WHEN MBK."LMSPBK" = 4 THEN '1'
             WHEN MBK."LMSPBK" = 3 THEN '3'
           ELSE '2' END                   AS "MediaKind"           -- 媒體別 VARCHAR2 1 0
          ,NVL(ADM."MediaSeq",0)          AS "MediaSeq"            -- 媒體序號 DECIMAL 6 0
          ,MBK."TRXDAT"                   AS "AcDate"              -- 會計日期 Decimald 8 0
          ,''                             AS "TitaTlrNo"           -- 經辦 VARCHAR2 6
          ,''                             AS "TitaTxtNo"           -- 交易序號 VARCHAR2 8
          ,''                             AS "AmlRsp"              -- AML回應碼 VARCHAR2 1 0
          ,MBK."MBKRSN"                   AS "ReturnCode"          -- 回應代碼 VARCHAR2 2 0
          ,''                             AS "JsonFields"          -- jason格式紀錄欄 nvarchar2 300
          ,JOB_START_TIME                 AS "CreateDate"          -- 建檔日期時間 DATE  
          ,'999999'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 
          ,JOB_START_TIME                 AS "LastUpdate"          -- 最後更新日期時間 DATE  
          ,'999999'                       AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 
    FROM "LA$MBKP" MBK
    LEFT JOIN "LA$APLP" APLP ON APLP."LMSACN" = MBK."LMSACN"
                            AND APLP."LMSAPN" = MBK."MBKAPN"
    LEFT JOIN "DupData" S2 ON S2."TRXIDT" = MBK."TRXIDT"
                          AND S2."LMSACN" = MBK."LMSACN"
                          AND S2."MBKAPN" = MBK."MBKAPN"
                          AND S2."LMSLPD" = MBK."LMSLPD"
                          AND S2."MBKTRX" = MBK."MBKTRX"
    LEFT JOIN "ADM" ADM ON ADM."MediaDate" = MBK."TRXIDT"
                       AND ADM."MediaKind" = CASE
                                               WHEN MBK."LMSPBK" = 4 THEN '1'
                                               WHEN MBK."LMSPBK" = 3 THEN '3'
                                             ELSE '2' END 
                       AND ADM."CustNo" = MBK."LMSACN"
                       AND ADM."FacmNo" = MBK."MBKAPN"
                       AND ADM."RepayType" = CASE MBK."MBKTRX"
                                               WHEN '1' THEN '5' -- 保險費
                                               WHEN '2' THEN '1' -- 期款
                                               WHEN '3' THEN '4' -- 帳管費
                                               WHEN '4' THEN '6' -- 契變手續費
                                             ELSE '0' END
    WHERE NVL(S2."TRXIDT",0) = 0
      AND NVL(S2."LMSACN",0) = 0
      AND NVL(S2."MBKAPN",0) = 0
      AND NVL(S2."LMSLPD",0) = 0
      AND NVL(S2."MBKTRX",0) = 0
    ;

    -- 記錄寫入筆數
    INS_CNT := INS_CNT + sql%rowcount;

    -- 寫入資料
    INSERT INTO "BankDeductDtl"
    WITH "TmpAchDeductMedia" AS (
      SELECT "MediaDate"
            ,"MediaKind"
            ,"CustNo"
            ,"FacmNo"
            ,"RepayType"
            ,"MediaSeq"
            ,ROW_NUMBER() OVER (PARTITION BY "MediaDate"
                                            ,"MediaKind"
                                            ,"CustNo"
                                            ,"FacmNo"
                                            ,"RepayType"
                                ORDER BY "MediaDate"
                                        ,"MediaKind"
                                        ,"CustNo"
                                        ,"FacmNo"
                                        ,"RepayType"
                                        ,"MediaSeq") AS "Seq"
      FROM "AchDeductMedia"
    )
    , "ADM" AS (
      SELECT "MediaDate"
            ,"MediaKind"
            ,"CustNo"
            ,"FacmNo"
            ,"RepayType"
            ,"MediaSeq"
      FROM "TmpAchDeductMedia"
      WHERE "Seq" = 1
    )
    , "DupData" AS (
      SELECT "TRXIDT"
            ,"LMSACN"
            ,"MBKAPN"
            ,"LMSLPD"
            ,"MAKTRX"
      FROM "AH$MBKP"
      GROUP BY "TRXIDT"
              ,"LMSACN"
              ,"MBKAPN"
              ,"LMSLPD"
              ,"MAKTRX"
      HAVING COUNT(*) >= 2
    )
    SELECT MBK."TRXIDT"                   AS "EntryDate"           -- 入帳日期 Decimald 8 0
          ,MBK."LMSACN"                   AS "CustNo"              -- 戶號 DECIMAL 7 0
          ,MBK."MBKAPN"                   AS "FacmNo"              -- 額度 DECIMAL 3 0
          ,CASE MBK."MAKTRX"
             WHEN '1' THEN '5' -- 保險費
             WHEN '2' THEN '1' -- 期款
             WHEN '3' THEN '4' -- 帳管費
             WHEN '4' THEN '6' -- 契變手續費
           ELSE '0' END                   AS "RepayType"           -- 還款類別 DECIMAL 2 0
          ,MBK."LMSLPD"                   AS "PayIntDate"          -- 應繳日 Decimald 8 0
          ,MBK."TRXISD"                   AS "PrevIntDate"         -- 繳息迄日 Decimald 8 0
          ,MBK."ACTACT"                   AS "AcctCode"            -- 科目 VARCHAR2 3 0
          ,CASE
             WHEN MBK."LMSPBK" = '1' THEN '812'
             WHEN MBK."LMSPBK" = '2' THEN '006'
             WHEN MBK."LMSPBK" = '3' THEN '700'
             WHEN MBK."LMSPBK" = '4' THEN '103'
           ELSE '000' END                 AS "RepayBank"           -- 扣款銀行 VARCHAR2 3 0
          ,LPAD(MBK."LMSPCN",14,'0')      AS "RepayAcctNo"         -- 扣款帳號 VARCHAR2 14 0
          ,0                              AS "RepayAcctSeq"        -- 帳號碼 VARCHAR2 2 0
          ,0                              AS "UnpaidAmt"           -- 應扣金額 DECIMAL 14 0
          ,0                              AS "TempAmt"             -- 暫收抵繳金額 DECIMAL 14 0
          ,MBK."MBKAMT"                   AS "RepayAmt"            -- 扣款金額 DECIMAL 14 0
          ,MBK."TRXISD"                   AS "IntStartDate"        -- 計息起日 Decimald 8 0
          ,MBK."TRXIED"                   AS "IntEndDate"          -- 計息迄日 Decimald 8 0
          ,APLP."POSCDE"                  AS "PostCode"            -- 郵局存款別 VARCHAR2 1 0
          ,MBK."MBKCDE"                   AS "MediaCode"           -- 媒體碼 VARCHAR2 1 0
          ,MBK."LMSPRL"                   AS "RelationCode"        -- 與借款人關係 VARCHAR2 2 0
          ,MBK."LMSPAN"                   AS "RelCustName"         -- 第三人帳戶戶名 NVARCHAR2 100 0
          ,MBK."LMSPID"                   AS "RelCustId"           -- 第三人身分證字號 VARCHAR2 10 0
          ,0                              AS "RelAcctBirthday"     -- 第三人出生日期 decimal 8 0
          ,''                             AS "RelAcctGender"       -- 第三人性別 varchar2 1
          ,MBK."TRXIDT"                   AS "MediaDate"           -- 媒體日期 DECIMAL 8 0
          ,CASE
             WHEN MBK."LMSPBK" = 4 THEN '1'
             WHEN MBK."LMSPBK" = 3 THEN '3'
           ELSE '2' END                   AS "MediaKind"           -- 媒體別 VARCHAR2 1 0
          ,NVL(ADM."MediaSeq",0)          AS "MediaSeq"            -- 媒體序號 DECIMAL 6 0
          ,MBK."TRXDAT"                   AS "AcDate"              -- 會計日期 Decimald 8 0
          ,''                             AS "TitaTlrNo"           -- 經辦 VARCHAR2 6
          ,''                             AS "TitaTxtNo"           -- 交易序號 VARCHAR2 8
          ,''                             AS "AmlRsp"              -- AML回應碼 VARCHAR2 1 0
          ,MBK."MBKRSN"                   AS "ReturnCode"          -- 回應代碼 VARCHAR2 2 0
          ,''                             AS "JsonFields"          -- jason格式紀錄欄 nvarchar2 300
          ,JOB_START_TIME                 AS "CreateDate"          -- 建檔日期時間 DATE  
          ,'999999'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 
          ,JOB_START_TIME                 AS "LastUpdate"          -- 最後更新日期時間 DATE  
          ,'999999'                       AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 
    FROM "AH$MBKP" MBK
    LEFT JOIN "LA$APLP" APLP ON APLP."LMSACN" = MBK."LMSACN"
                            AND APLP."LMSAPN" = MBK."MBKAPN"
    LEFT JOIN "DupData" S2 ON S2."TRXIDT" = MBK."TRXIDT"
                         AND S2."LMSACN" = MBK."LMSACN"
                         AND S2."MBKAPN" = MBK."MBKAPN"
                         AND S2."LMSLPD" = MBK."LMSLPD"
                         AND S2."MAKTRX" = MBK."MAKTRX"
    LEFT JOIN "ADM" ADM ON ADM."MediaDate" = MBK."TRXIDT"
                       AND ADM."MediaKind" = CASE
                                               WHEN MBK."LMSPBK" = 4 THEN '1'
                                               WHEN MBK."LMSPBK" = 3 THEN '3'
                                             ELSE '2' END 
                       AND ADM."CustNo" = MBK."LMSACN"
                       AND ADM."FacmNo" = MBK."MBKAPN"
                       AND ADM."RepayType" = CASE MBK."MAKTRX"
                                               WHEN '1' THEN '5' -- 保險費
                                               WHEN '2' THEN '1' -- 期款
                                               WHEN '3' THEN '4' -- 帳管費
                                               WHEN '4' THEN '6' -- 契變手續費
                                             ELSE '0' END
    LEFT JOIN "BankDeductDtl" BDD ON BDD."EntryDate" = MBK."TRXIDT" 
                                 AND BDD."CustNo" = MBK."LMSACN" 
                                 AND BDD."FacmNo" = MBK."MBKAPN" 
                                 AND BDD."RepayType" = CASE MBK."MAKTRX"
                                                         WHEN '1' THEN '5' -- 保險費
                                                         WHEN '2' THEN '1' -- 期款
                                                         WHEN '3' THEN '4' -- 帳管費
                                                         WHEN '4' THEN '6' -- 契變手續費
                                                       ELSE '0' END
                                 AND BDD."PayIntDate" = MBK."LMSLPD" 
    WHERE NVL(S2."TRXIDT",0) = 0
      AND NVL(S2."LMSACN",0) = 0
      AND NVL(S2."MBKAPN",0) = 0
      AND NVL(S2."LMSLPD",0) = 0
      AND NVL(S2."MAKTRX",0) = 0
      AND NVL(BDD."EntryDate",0) = 0
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
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_BankDeductDtl_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;




/
