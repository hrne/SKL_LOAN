--------------------------------------------------------
--  DDL for Procedure Usp_Tf_AchDeductMedia_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_Tf_AchDeductMedia_Ins" 
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
    EXECUTE IMMEDIATE 'ALTER TABLE "AchDeductMedia" DISABLE PRIMARY KEY CASCADE';
    EXECUTE IMMEDIATE 'TRUNCATE TABLE "AchDeductMedia" DROP STORAGE';
    EXECUTE IMMEDIATE 'ALTER TABLE "AchDeductMedia" ENABLE PRIMARY KEY';

    -- 寫入資料
    INSERT INTO "AchDeductMedia" (
        "MediaDate"           -- 媒體日期 DECIMAL 8 
      , "MediaKind"           -- 媒體別 VARCHAR2 1 
      , "MediaSeq"            -- 媒體序號 DECIMAL 6 
      , "CustNo"              -- 戶號 DECIMAL 7 
      , "FacmNo"              -- 額度號碼 DECIMAL 3 
      , "RepayType"           -- 還款類別 DECIMAL 2 
      , "RepayAmt"            -- 扣款金額,還款金額 DECIMAL 14 
      , "ReturnCode"          -- 退件理由代號 VARCHAR2 2 
      , "EntryDate"           -- 入帳日期 DECIMAL 8 
      , "PrevIntDate"         -- 繳息迄日 DECIMAL 8 
      , "RepayBank"           -- 扣款銀行 VARCHAR2 3 
      , "RepayAcctNo"         -- 扣款帳號 VARCHAR2 14 
      , "AchRepayCode"        -- 入帳扣款別 VARCHAR2 1 
      , "AcctCode"            -- 科目 VARCHAR2 3 
      , "IntStartDate"        -- 計息起日 DECIMAL 8 
      , "IntEndDate"          -- 計息迄日 DECIMAL 8 
      , "DepCode"             -- 存摺代號 VARCHAR2 2 
      , "RelationCode"        -- 與借款人關係 VARCHAR2 2 
      , "RelCustName"         -- 帳戶戶名 NVARCHAR2 100 
      , "RelCustId"           -- 身分證字號 VARCHAR2 10 
      , "AcDate"              -- 會計日期 DECIMAL 8 
      , "BatchNo"             -- 批號 VARCHAR2 6 
      , "DetailSeq"           -- 明細序號 DECIMAL 6 
      , "CreateDate"          -- 建檔日期時間 DATE  
      , "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 
      , "LastUpdate"          -- 異動日期 DATE 0 0
      , "LastUpdateEmpNo"     -- 修改者櫃員編號 VARCHAR2 6 0
      , "RelCustIdErrFg"
    )
    WITH rawData AS (
      SELECT MAX(TRXIDT) AS LastTRXIDT
      FROM "AH$MBKP" MBK
      WHERE NVL(MBK.MBKCDE,' ') = 'Y'
    )
    , tmpData AS (
      SELECT "Fn_GetBusinessDate"(LastTRXIDT,-2) AS NewTRXIDT -- 找前二營業日
           , LastTRXIDT 
      FROM rawData
    )
    SELECT CASE
             WHEN NVL(t.LastTRXIDT,0) != 0
             THEN t.NewTRXIDT
           ELSE MBK."TRXIDT"
           END                            AS "MediaDate"           -- 媒體日期 DECIMAL 8 
          ,CASE
             WHEN MBK."LMSPBK" = 4 THEN '1'
             WHEN MBK."LMSPBK" = 3 THEN '3'
           ELSE '2' END                   AS "MediaKind"           -- 媒體別 VARCHAR2 1 
          ,ROW_NUMBER() OVER (PARTITION BY CASE
                                             WHEN NVL(t.LastTRXIDT,0) != 0
                                             THEN t.NewTRXIDT
                                           ELSE MBK."TRXIDT"
                                           END 
                                          ,CASE
                                             WHEN MBK."LMSPBK" = 4 THEN '1'
                                             WHEN MBK."LMSPBK" = 3 THEN '3'
                                           ELSE '2' END
                              ORDER BY MBK."LMSACN"
                                      ,MBK."MBKAPN"
                                      ,MBK."LMSPCN"
                                      ,MBK."TRXIDT"
                                      ,MBK."TRXISD"
                                      ,MBK."TRXIED"
                                      ,MBK."ACTACT"
                                      ,MBK."MBKAMT")
                                          AS "MediaSeq"            -- 媒體序號 DECIMAL 6 
          ,MBK."LMSACN"                   AS "CustNo"              -- 戶號 DECIMAL 7 
          ,MBK."MBKAPN"                   AS "FacmNo"              -- 額度號碼 DECIMAL 3 
          ,CASE MBK."MAKTRX"
             WHEN '1' THEN '5' -- 火險費
             WHEN '2' THEN '4' -- 帳管費
             WHEN '3' THEN '1' -- 期款
             WHEN '4' THEN '6' -- 契變手續費
           ELSE '0' END                   AS "RepayType"           -- 還款類別 DECIMAL 2 
          ,MBK."MBKAMT"                   AS "RepayAmt"            -- 扣款金額,還款金額 DECIMAL 14 
          ,MBK."MBKRSN"                   AS "ReturnCode"          -- 退件理由代號 VARCHAR2 2 
          ,MBK."TRXIDT"                   AS "EntryDate"           -- 入帳日期 DECIMAL 8 
          ,MBK."LMSLPD"                   AS "PrevIntDate"         -- 繳息迄日 DECIMAL 8 
          ,CASE 
             WHEN MBK."LMSPBK" = '1' THEN '812' 
             WHEN MBK."LMSPBK" = '2' THEN '006' 
             WHEN MBK."LMSPBK" = '3' THEN '700' 
             WHEN MBK."LMSPBK" = '4' THEN '103' 
           ELSE '000' END                 AS "RepayBank"           -- 扣款銀行 VARCHAR2 3 
          ,MBK."LMSPCN"                   AS "RepayAcctNo"         -- 扣款帳號 VARCHAR2 14 
          ,MBK."MAKTRX"                   AS "AchRepayCode"        -- 入帳扣款別 VARCHAR2 1 
          ,MBK."ACTACT"                   AS "AcctCode"            -- 科目 VARCHAR2 3 
          ,MBK."TRXISD"                   AS "IntStartDate"        -- 計息起日 DECIMAL 8 
          ,MBK."TRXIED"                   AS "IntEndDate"          -- 計息迄日 DECIMAL 8 
          ,MBK."DPSATC"                   AS "DepCode"             -- 存摺代號 VARCHAR2 2 
          ,MBK."LMSPRL"                   AS "RelationCode"        -- 與借款人關係 VARCHAR2 2 
          ,MBK."LMSPAN"                   AS "RelCustName"         -- 帳戶戶名 NVARCHAR2 100 
          ,MBK."LMSPID"                   AS "RelCustId"           -- 身分證字號 VARCHAR2 10 
          ,MBK."TRXDAT"                   AS "AcDate"              -- 會計日期 DECIMAL 8 
          ,CASE
             WHEN NVL(MBK."MBKRSN",' ') != ' '
             THEN TO_CHAR(MBK.BSTBTN)
           ELSE '' END                    AS "BatchNo"             -- 批號 VARCHAR2 6 
          ,CASE
             WHEN NVL(MBK."MBKRSN",' ') != ' '
             THEN ROW_NUMBER() OVER (PARTITION BY MBK."TRXIDT"
                                     ORDER BY MBK."LMSACN",MBK."MBKAPN")
           ELSE 0 END                     AS "DetailSeq"           -- 明細序號 DECIMAL 6 
          ,CASE
             WHEN MBK."CHGDTM" > 0
             THEN TO_DATE(MBK."CHGDTM",'YYYYMMDDHH24MISS')
           ELSE JOB_START_TIME
           END                            AS "CreateDate"          -- 建檔日期時間 DATE  
          ,NVL(AEM."EmpNo",MBK."CHGEMP")  AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 
          ,CASE
             WHEN MBK."CHGDTM" > 0
             THEN TO_DATE(MBK."CHGDTM",'YYYYMMDDHH24MISS')
           ELSE JOB_START_TIME
           END                           AS "LastUpdate"          -- 異動日期 DATE 0 0
          ,NVL(AEM."EmpNo",MBK."CHGEMP") AS "LastUpdateEmpNo"     -- 修改者櫃員編號 VARCHAR2 6 0
          -- 2023-10-03 Wei FROM SKL IT 盈倩 #A01-2
          ,MBK.LMSPER AS "RelCustIdErrFg"
    FROM "AH$MBKP" MBK
    LEFT JOIN tmpData t on t.LastTRXIDT = MBK."TRXIDT"
    LEFT JOIN "As400EmpNoMapping" AEM ON AEM."As400TellerNo" = MBK."CHGEMP"
    WHERE NVL(MBK.MBKCDE,' ') = 'Y'
      -- AND MBK."TRXIDT" >= 20190101
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
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_AchDeductMedia_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;

/
