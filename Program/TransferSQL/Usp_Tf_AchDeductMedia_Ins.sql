--------------------------------------------------------
--  DDL for Procedure Usp_Tf_AchDeductMedia_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "Usp_Tf_AchDeductMedia_Ins" 
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
    INSERT INTO "AchDeductMedia"
    SELECT "AH$MBKP"."TRXIDT"             AS "MediaDate"           -- 媒體日期 DECIMAL 8 
          ,CASE
             WHEN "AH$MBKP"."LMSPBK" = 4 THEN '1'
             WHEN "AH$MBKP"."LMSPBK" = 3 THEN '3'
           ELSE '2' END                   AS "MediaKind"           -- 媒體別 VARCHAR2 1 
          ,ROW_NUMBER() OVER (PARTITION BY "AH$MBKP"."TRXIDT"
                                          ,CASE
                                             WHEN "AH$MBKP"."LMSPBK" = 4 THEN '1'
                                             WHEN "AH$MBKP"."LMSPBK" = 3 THEN '3'
                                           ELSE '2' END
                              ORDER BY "AH$MBKP"."LMSACN"
                                      ,"AH$MBKP"."MBKAPN"
                                      ,"AH$MBKP"."LMSPCN"
                                      ,"AH$MBKP"."TRXIDT"
                                      ,"AH$MBKP"."TRXISD"
                                      ,"AH$MBKP"."TRXIED"
                                      ,"AH$MBKP"."ACTACT"
                                      ,"AH$MBKP"."MBKAMT")
                                          AS "MediaSeq"            -- 媒體序號 DECIMAL 6 
          ,"AH$MBKP"."LMSACN"             AS "CustNo"              -- 戶號 DECIMAL 7 
          ,"AH$MBKP"."MBKAPN"             AS "FacmNo"              -- 額度號碼 DECIMAL 3 
          ,CASE "AH$MBKP"."MAKTRX"
             WHEN '1' THEN '5' -- 火險費
             WHEN '2' THEN '4' -- 帳管費
             WHEN '3' THEN '1' -- 期款
             WHEN '4' THEN '6' -- 契變手續費
           ELSE '0' END                   AS "RepayType"           -- 還款類別 DECIMAL 2 
          ,"AH$MBKP"."MBKAMT"             AS "RepayAmt"            -- 扣款金額,還款金額 DECIMAL 14 
          ,"AH$MBKP"."MBKRSN"             AS "ReturnCode"          -- 退件理由代號 VARCHAR2 2 
          ,"AH$MBKP"."TRXIDT"             AS "EntryDate"           -- 入帳日期 DECIMAL 8 
          ,"AH$MBKP"."LMSLPD"             AS "PrevIntDate"         -- 繳息迄日 DECIMAL 8 
          ,"AH$MBKP"."LMSPBK"             AS "RepayBank"           -- 扣款銀行 VARCHAR2 3 
          ,"AH$MBKP"."LMSPCN"             AS "RepayAcctNo"         -- 扣款帳號 VARCHAR2 14 
          ,"AH$MBKP"."MAKTRX"             AS "AchRepayCode"        -- 入帳扣款別 VARCHAR2 1 
          ,"AH$MBKP"."ACTACT"             AS "AcctCode"            -- 科目 VARCHAR2 3 
          ,"AH$MBKP"."TRXISD"             AS "IntStartDate"        -- 計息起日 DECIMAL 8 
          ,"AH$MBKP"."TRXIED"             AS "IntEndDate"          -- 計息迄日 DECIMAL 8 
          ,"AH$MBKP"."DPSATC"             AS "DepCode"             -- 存摺代號 VARCHAR2 2 
          ,"AH$MBKP"."LMSPRL"             AS "RelationCode"        -- 與借款人關係 VARCHAR2 2 
          ,"AH$MBKP"."LMSPAN"             AS "RelCustName"         -- 帳戶戶名 NVARCHAR2 100 
          ,"AH$MBKP"."LMSPID"             AS "RelCustId"           -- 身分證字號 VARCHAR2 10 
          ,"AH$MBKP"."TRXDAT"             AS "AcDate"              -- 會計日期 DECIMAL 8 
          ,'BATX01'                       AS "BatchNo"             -- 批號 VARCHAR2 6 
          ,ROW_NUMBER() OVER (PARTITION BY "AH$MBKP"."TRXIDT"
                              ORDER BY "AH$MBKP"."LMSACN","AH$MBKP"."MBKAPN")
                                          AS "DetailSeq"           -- 明細序號 DECIMAL 6 
          ,JOB_START_TIME                 AS "CreateDate"          -- 建檔日期時間 DATE  
          ,'999999'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 
          ,JOB_START_TIME                 AS "LastUpdate"          -- 最後更新日期時間 DATE  
          ,'999999'                       AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 
    FROM "AH$MBKP"
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
