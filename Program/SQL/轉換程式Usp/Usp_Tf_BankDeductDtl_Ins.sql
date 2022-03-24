create or replace NONEDITIONABLE PROCEDURE "Usp_Tf_BankDeductDtl_Ins" 
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
    WITH "ADM" AS (
      SELECT "MediaDate"
            ,"MediaSeq"
            ,"CustNo"
            ,"FacmNo"
            ,"RepayType"
            ,"TransDate"
            ,"RepayAcctNo"
            ,"RepayAmt"
            ,"AcDate"
            ,ROW_NUMBER() OVER (
                 PARTITION BY "CustNo"
                            , "FacmNo"
                            , "RepayType"
                            , "TransDate"
                 ORDER BY "MediaDate" desc
                        , "MediaSeq" desc
             ) AS "Seq"
      FROM "PostDeductMedia"
    )
    , rawData AS (
      SELECT MAX(TRXIDT) AS LastTRXIDT
      FROM "LA$MBKP" MBK
      WHERE NVL(MBK.MBKCDE,' ') = 'Y'
    )
    , tmpData AS (
      SELECT "Fn_GetBusinessDate"(LastTRXIDT,-2) AS NewTRXIDT -- 找前二營業日
           , LastTRXIDT 
      FROM rawData
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
          ,CASE
             WHEN NVL(t.LastTRXIDT,0) != 0
             THEN t.NewTRXIDT
           ELSE MBK."TRXIDT"
           END                            AS "MediaDate"           -- 媒體日期 DECIMAL 8 
          ,CASE
             WHEN MBK."LMSPBK" = 4 THEN '1'
             WHEN MBK."LMSPBK" = 3 THEN '3'
           ELSE '2' END                   AS "MediaKind"           -- 媒體別 VARCHAR2 1 0
          ,NVL(ADM."MediaSeq",0)          AS "MediaSeq"            -- 媒體序號 DECIMAL 6 0
          ,MBK."TRXDAT"                   AS "AcDate"              -- 會計日期 Decimald 8 0
          ,''                             AS "TitaTlrNo"           -- 經辦 VARCHAR2 6
          ,MBK."TRXNMT"                   AS "TitaTxtNo"           -- 交易序號 VARCHAR2 8
          ,''                             AS "AmlRsp"              -- AML回應碼 VARCHAR2 1 0
          ,MBK."MBKRSN"                   AS "ReturnCode"          -- 回應代碼 VARCHAR2 2 0
          ,''                             AS "JsonFields"          -- jason格式紀錄欄 nvarchar2 300
          ,JOB_START_TIME                 AS "CreateDate"          -- 建檔日期時間 DATE  
          ,'999999'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 
          ,JOB_START_TIME                 AS "LastUpdate"          -- 異動日期 DATE 0 0
          ,'999999'                       AS "LastUpdateEmpNo"     -- 修改者櫃員編號 VARCHAR2 6 0
    FROM "LA$MBKP" MBK
    LEFT JOIN "LA$APLP" APLP ON APLP."LMSACN" = MBK."LMSACN"
                            AND APLP."LMSAPN" = MBK."MBKAPN"
    LEFT JOIN tmpData t on t.LastTRXIDT = MBK."TRXIDT"
    LEFT JOIN "ADM" ADM ON ADM."MediaDate" = CASE
                                               WHEN NVL(t.LastTRXIDT,0) != 0
                                               THEN t.NewTRXIDT
                                             ELSE MBK."TRXIDT"
                                             END
                       AND ADM."CustNo" = MBK."LMSACN"
                       AND ADM."FacmNo" = MBK."MBKAPN"
                       AND ADM."RepayType" = CASE MBK."MBKTRX"
                                               WHEN '1' THEN '5' -- 保險費
                                               WHEN '2' THEN '1' -- 期款
                                               WHEN '3' THEN '4' -- 帳管費
                                               WHEN '4' THEN '6' -- 契變手續費
                                             ELSE '0' END
                       AND ADM."TransDate" = MBK."TRXIDT" 
                       AND ADM."RepayAcctNo" = LPAD(MBK."LMSPCN",14,'0')
                       AND ADM."RepayAmt" = MBK."MBKAMT"
                       AND ADM."AcDate" = MBK."TRXDAT"
                       AND ADM."Seq" = 1
    WHERE MBK."TRXIDT" >= 20190101
      AND MBK."LMSPBK" = '3' -- 只抓郵局
      AND NVL(MBK.MBKCDE,' ') = 'Y'
    ;

    -- 記錄寫入筆數
    INS_CNT := INS_CNT + sql%rowcount;

    -- 寫入資料
    INSERT INTO "BankDeductDtl"
    WITH "ADM" AS (
      SELECT "MediaDate"
            ,"MediaKind"
            ,"CustNo"
            ,"FacmNo"
            ,"RepayType"
            ,"RepayAmt"
            ,"RepayAcctNo"
            ,"MediaSeq"
            ,ROW_NUMBER() OVER (
                 PARTITION BY "CustNo"
                            , "FacmNo"
                            , "RepayType"
                 ORDER BY "MediaDate" desc
                        , "MediaKind"
                        , "MediaSeq" desc
             ) AS "Seq"
      FROM "AchDeductMedia"
    )
    , rawData AS (
      SELECT MAX(TRXIDT) AS LastTRXIDT
      FROM "AH$MBKP" MBK
      WHERE NVL(MBK.MBKCDE,' ') = 'Y'
    )
    , tmpData AS (
      SELECT "Fn_GetBusinessDate"(LastTRXIDT,-2) AS NewTRXIDT -- 找前二營業日
           , LastTRXIDT 
      FROM rawData
    )
    SELECT MBK."TRXIDT"                   AS "EntryDate"           -- 入帳日期 Decimald 8 0
          ,MBK."LMSACN"                   AS "CustNo"              -- 戶號 DECIMAL 7 0
          ,MBK."MBKAPN"                   AS "FacmNo"              -- 額度 DECIMAL 3 0
          -- MAKTRX
          -- 1 火險費
          -- 2 帳管費
          -- 3 期款
          -- 4 契變手續費
          ,CASE MBK."MAKTRX"
             WHEN '1' THEN '5' -- 保險費
             WHEN '2' THEN '4' -- 帳管費
             WHEN '3' THEN '1' -- 期款
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
          ,CASE
             WHEN NVL(t.LastTRXIDT,0) != 0
             THEN t.NewTRXIDT
           ELSE MBK."TRXIDT"
           END                            AS "MediaDate"           -- 媒體日期 DECIMAL 8 
          ,CASE
             WHEN MBK."LMSPBK" = 4 THEN '1'
             WHEN MBK."LMSPBK" = 3 THEN '3'
           ELSE '2' END                   AS "MediaKind"           -- 媒體別 VARCHAR2 1 0
          ,NVL(ADM."MediaSeq",0)          AS "MediaSeq"            -- 媒體序號 DECIMAL 6 0
          ,MBK."TRXDAT"                   AS "AcDate"              -- 會計日期 Decimald 8 0
          ,''                             AS "TitaTlrNo"           -- 經辦 VARCHAR2 6
          ,MBK."TRXNMT"                   AS "TitaTxtNo"           -- 交易序號 VARCHAR2 8
          ,''                             AS "AmlRsp"              -- AML回應碼 VARCHAR2 1 0
          ,MBK."MBKRSN"                   AS "ReturnCode"          -- 回應代碼 VARCHAR2 2 0
          ,''                             AS "JsonFields"          -- jason格式紀錄欄 nvarchar2 300
          ,JOB_START_TIME                 AS "CreateDate"          -- 建檔日期時間 DATE  
          ,'999999'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 
          ,CASE
             WHEN MBK."CHGDTM" > 0
             THEN TO_DATE(MBK."CHGDTM",'YYYYMMDDHH24MISS')
           ELSE JOB_START_TIME
           END                            AS "LastUpdate"          -- 異動日期 DATE 0 0
          ,NVL(AEM."EmpNo",'999999')      AS "LastUpdateEmpNo"     -- 修改者櫃員編號 VARCHAR2 6 0
    FROM "AH$MBKP" MBK
    LEFT JOIN "LA$APLP" APLP ON APLP."LMSACN" = MBK."LMSACN"
                            AND APLP."LMSAPN" = MBK."MBKAPN"
    LEFT JOIN tmpData t on t.LastTRXIDT = MBK."TRXIDT"
    LEFT JOIN "ADM" ADM ON ADM."MediaDate" = CASE
                                               WHEN NVL(t.LastTRXIDT,0) != 0
                                               THEN t.NewTRXIDT
                                             ELSE MBK."TRXIDT"
                                             END
                       AND ADM."MediaKind" = CASE
                                               WHEN MBK."LMSPBK" = 4 THEN '1'
                                               WHEN MBK."LMSPBK" = 3 THEN '3'
                                             ELSE '2' END 
                       AND ADM."CustNo" = MBK."LMSACN"
                       AND ADM."FacmNo" = MBK."MBKAPN"
                       AND ADM."RepayType" = CASE MBK."MAKTRX"
                                               WHEN '1' THEN '5' -- 保險費
                                               WHEN '2' THEN '4' -- 帳管費
                                               WHEN '3' THEN '1' -- 期款
                                               WHEN '4' THEN '6' -- 契變手續費
                                             ELSE '0' END
                       AND ADM."RepayAmt" = MBK."MBKAMT"
                       AND ADM."RepayAcctNo" = MBK."LMSPCN"
                       AND ADM."Seq" = 1
    LEFT JOIN "As400EmpNoMapping" AEM ON AEM."As400TellerNo" = MBK."CHGEMP"
    WHERE MBK."TRXIDT" >= 20190101
      AND NVL(MBK.MBKCDE,' ') = 'Y'
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