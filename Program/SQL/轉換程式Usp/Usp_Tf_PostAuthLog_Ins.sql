--------------------------------------------------------
--  DDL for Procedure Usp_Tf_PostAuthLog_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "Usp_Tf_PostAuthLog_Ins" 
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

    DECLARE 
        "TbsDyF" DECIMAL(8); --西元帳務日
    BEGIN

    SELECT "TbsDy" + 19110000
    INTO "TbsDyF"
    FROM "TxBizDate"
    WHERE "DateCode" = 'ONLINE'
    ;

    -- 刪除舊資料
    EXECUTE IMMEDIATE 'ALTER TABLE "PostAuthLog" DISABLE PRIMARY KEY CASCADE';
    EXECUTE IMMEDIATE 'TRUNCATE TABLE "PostAuthLog" DROP STORAGE';
    EXECUTE IMMEDIATE 'ALTER TABLE "PostAuthLog" ENABLE PRIMARY KEY';

    -- 寫入資料
    INSERT INTO "PostAuthLog"
    SELECT "PO$AARP"."CUSCDT"             AS "AuthCreateDate"      -- 建檔日期 Decimald 8 0
          ,'1'                            AS "AuthApplCode"        -- 申請代號，狀態碼 VARCHAR2 1 0
          ,"PO$AARP"."LMSACN"             AS "CustNo"              -- 戶號 DECIMAL 7 0
          ,"PO$AARP"."POSCDE"             AS "PostDepCode"         -- 帳戶別 VARCHAR2 1 0
          ,LPAD("PO$AARP"."LMSPCN",14,'0')
                                          AS "RepayAcct"           -- 儲金帳號 VARCHAR2 14 0
          ,SUBSTR("PO$AARP"."POATYP",2,1) AS "AuthCode"            -- 授權方式 VARCHAR2 1 0
          ,NVL(FACM."LMSAPN",0)           AS "FacmNo"              -- 額度 DECIMAL 3 0
          ,"PO$AARP"."CUSID1"             AS "CustId"              -- 統一編號 VARCHAR2 10 0
          ,''                             AS "RepayAcctSeq"        -- 帳號碼 VARCHAR2 2 0
          ,CASE WHEN "PO$AARP"."PRCCDT" > 0 THEN TRUNC("PO$AARP"."PRCCDT" / 1000000)
           ELSE 0 END                     AS "ProcessDate"         -- 處理日期 Decimald 8 0
          ,"PO$AARP"."FNDDAT"             AS "StampFinishDate"     -- 核印完成日期 Decimald 8 0
          ,"PO$AARP"."CNLDAT"             AS "StampCancelDate"     -- 核印取消日期 Decimald 8 0
          ,"PO$AARP"."POACD2"             AS "StampCode"           -- 核印註記 VARCHAR2 1 0
          ,"PO$AARP"."POACDE"             AS "PostMediaCode"       -- 媒體碼 VARCHAR2 1 0
          ,"PO$AARP"."POACOD"             AS "AuthErrorCode"       -- 狀況代號，授權狀態 VARCHAR2 2 0
          ,"PO$AARP"."POANUM"             AS "FileSeq"             -- 媒體檔流水編號 DECIMAL 6 0
          ,"PO$AARP"."CUSCDT"             AS "PropDate"            -- 提出日期 Decimald 8 0
          ,CASE
             WHEN "PO$AARP"."POACOD" = '00' THEN "PO$AARP"."FNDDAT" -- 授權成功時將核印完成日期視為提回日期
           ELSE 0 END                     AS "RetrDate"            -- 提回日期 Decimald 8 0
          ,0                              AS "DeleteDate"          -- 暫停授權日期 Decimald 8 0
          ,NVL(FACM."LMSPRL",'00')        AS "RelationCode"        -- 與借款人關係 VARCHAR2 2 0
          ,NVL(FACM."LMSPAN",u' ')        AS "RelAcctName"         -- 第三人帳戶戶名 NVARCHAR2 100 0
          ,NVL(FACM."LMSPID",' ')         AS "RelationId"          -- 第三人身分證字號 VARCHAR2 10 0
          ,0                              AS "RelAcctBirthday"     -- 第三人出生日期 Decimald 8 0
          ,''                             AS "RelAcctGender"       -- 第三人性別 VARCHAR2 1 0
          ,''                             AS "AmlRsp"              -- AML回應碼 VARCHAR2 1 0
          ,''                             AS "TitaTxCd"            -- 交易代號 VARCHAR2 5 
          ,NVL(AEM1."EmpNo",'999999')     AS "CreateEmpNo"         -- 建立者櫃員編號 VARCHAR2 6 0
          ,CASE
             WHEN "CRTDAT" > 0
             THEN TO_DATE("CRTDAT",'YYYYMMDD')
           ELSE JOB_START_TIME
           END                 AS "CreateDate"          -- 建檔日期 DATE 0 0
          ,NVL(AEM2."EmpNo",'999999')     AS "LastUpdateEmpNo"     -- 修改者櫃員編號 VARCHAR2 6 0
          ,CASE
             WHEN "CHGDAT" > 0
             THEN TO_DATE("CHGDAT",'YYYYMMDD')
           ELSE JOB_START_TIME
           END                 AS "LastUpdate"          -- 異動日期 DATE 0 0
          ,CASE WHEN "PO$AARP"."PRCCDT" > 0 THEN MOD("PO$AARP"."PRCCDT" , 1000000)
           ELSE 0 END                     AS "ProcessTime"         -- 處理時間 Decimal 6 0
          ,"PO$AARP"."POLAMT"             AS "LimitAmt"
          ,"PO$AARP"."POAMTD"             AS "AuthMeth"
    FROM "PO$AARP"
    LEFT JOIN "As400EmpNoMapping" AEM1 ON AEM1."As400TellerNo" = "PO$AARP"."CRTEMP"
    LEFT JOIN "As400EmpNoMapping" AEM2 ON AEM2."As400TellerNo" = "PO$AARP"."CHGEMP"
    LEFT JOIN (SELECT "LMSACN"
                     ,"LMSAPN"
                     ,"LMSPCN"
                     ,"LMSPRL"
                     ,"LMSPAN"
                     ,"LMSPID"
                     ,ROW_NUMBER() OVER (PARTITION BY "LMSACN"
                                                     ,"LMSPCN"
                                         ORDER BY "LMSPRL"
                                                 ,"LMSAPN"
                                        ) AS "SEQ"
               FROM "LA$APLP"
               WHERE "LMSPYS" = 2
              ) FACM ON FACM."LMSACN" = "PO$AARP"."LMSACN"
                    AND FACM."LMSPCN" = "PO$AARP"."LMSPCN"
                    AND FACM."SEQ" = 1
    ;

    -- 記錄寫入筆數
    INS_CNT := INS_CNT + sql%rowcount;
    
    -- LA$APLP有授權帳號資料，PO$AARP沒資料的資料補寫入
    INSERT INTO "PostAuthLog"
    SELECT "TbsDyF"                       AS "AuthCreateDate"      -- 建檔日期 Decimald 8 0
          ,'1'                            AS "AuthApplCode"        -- 申請代號，狀態碼 VARCHAR2 1 0
          ,BAA."CustNo"                   AS "CustNo"              -- 戶號 DECIMAL 7 0
          ,BAA."PostDepCode"              AS "PostDepCode"         -- 帳戶別 VARCHAR2 1 0
          ,BAA."RepayAcct"                AS "RepayAcct"           -- 儲金帳號 VARCHAR2 14 0
          ,BAA."AuthType"                 AS "AuthCode"            -- 授權方式 VARCHAR2 1 0
          ,BAA."FacmNo"                   AS "FacmNo"              -- 額度 DECIMAL 3 0
          ,CM."CustId"                    AS "CustId"              -- 統一編號 VARCHAR2 10 0
          ,''                             AS "RepayAcctSeq"        -- 帳號碼 VARCHAR2 2 0
          ,0                              AS "ProcessDate"         -- 處理日期 Decimald 8 0
          ,0                              AS "StampFinishDate"     -- 核印完成日期 Decimald 8 0
          ,0                              AS "StampCancelDate"     -- 核印取消日期 Decimald 8 0
          ,''                             AS "StampCode"           -- 核印註記 VARCHAR2 1 0
          ,' '                            AS "PostMediaCode"       -- 媒體碼 VARCHAR2 1 0
          ,' '                            AS "AuthErrorCode"       -- 狀況代號，授權狀態 VARCHAR2 2 0
          ,0                              AS "FileSeq"             -- 媒體檔流水編號 DECIMAL 6 0
          ,0                              AS "PropDate"            -- 提出日期 Decimald 8 0
          ,0                              AS "RetrDate"            -- 提回日期 Decimald 8 0
          ,0                              AS "DeleteDate"          -- 暫停授權日期 Decimald 8 0
          ,NVL(FACM."LMSPRL",'00')        AS "RelationCode"        -- 與借款人關係 VARCHAR2 2 0
          ,NVL(FACM."LMSPAN",u' ')        AS "RelAcctName"         -- 第三人帳戶戶名 NVARCHAR2 100 0
          ,NVL(FACM."LMSPID",' ')         AS "RelationId"          -- 第三人身分證字號 VARCHAR2 10 0
          ,0                              AS "RelAcctBirthday"     -- 第三人出生日期 Decimald 8 0
          ,''                             AS "RelAcctGender"       -- 第三人性別 VARCHAR2 1 0
          ,''                             AS "AmlRsp"              -- AML回應碼 VARCHAR2 1 0
          ,''                             AS "TitaTxCd"            -- 交易代號 VARCHAR2 5 
          ,BAA."CreateEmpNo"              AS "CreateEmpNo"         -- 建立者櫃員編號 VARCHAR2 6 0
          ,BAA."CreateDate"               AS "CreateDate"          -- 建檔日期 DATE 0 0
          ,BAA."LastUpdateEmpNo"          AS "LastUpdateEmpNo"     -- 修改者櫃員編號 VARCHAR2 6 0
          ,BAA."LastUpdate"               AS "LastUpdate"          -- 異動日期 DATE 0 0
          ,0                              AS "ProcessTime"         -- 處理時間 Decimal 6 0
          ,BAA."LimitAmt"                 AS "LimitAmt"
          ,'A'                            AS "AuthMeth"
    FROM "BankAuthAct" BAA
    LEFT JOIN "CustMain" CM ON CM."CustNo" = BAA."CustNo"
    LEFT JOIN (
      SELECT DISTINCT
             "LMSACN"
           , "LMSPCN"
           , "LMSPRL"
           , "LMSPAN"
           , "LMSPID"
           , ROW_NUMBER()
             OVER (
               PARTITION BY "LMSACN"
                          , "LMSPCN"
               ORDER BY "LMSPRL"
             ) AS "SEQ"
      FROM "LA$APLP"
      WHERE "LMSPYS" = 2
    ) FACM ON FACM."LMSACN" = S1."LMSACN"
          AND FACM."LMSPCN" = S1."LMSPCN"
          AND FACM."SEQ" = 1
    WHERE BAA."Status" = ' ' -- 空白:未授權
      AND BAA."RepayBank" = '700' -- 郵局
    ;

    -- 記錄寫入筆數
    INS_CNT := INS_CNT + sql%rowcount;

    -- 記錄程式結束時間
    JOB_END_TIME := SYSTIMESTAMP;

    commit;

    END;
    -- 例外處理
    Exception
    WHEN OTHERS THEN
    ERROR_MSG := SQLERRM || CHR(13) || CHR(10) || dbms_utility.format_error_backtrace;
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_PostAuthLog_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;


/
