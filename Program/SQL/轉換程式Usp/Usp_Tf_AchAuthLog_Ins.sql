--------------------------------------------------------
--  DDL for Procedure Usp_Tf_AchAuthLog_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_Tf_AchAuthLog_Ins" 
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
    EXECUTE IMMEDIATE 'ALTER TABLE "AchAuthLog" DISABLE PRIMARY KEY CASCADE';
    EXECUTE IMMEDIATE 'TRUNCATE TABLE "AchAuthLog" DROP STORAGE';
    EXECUTE IMMEDIATE 'ALTER TABLE "AchAuthLog" ENABLE PRIMARY KEY';

    -- 寫入資料
    INSERT INTO "AchAuthLog"
    SELECT S2."CUSCDT"                    AS "AuthCreateDate"      -- 建檔日期 Decimald 8 
          ,S2."LMSACN"                    AS "CustNo"              -- 戶號 DECIMAL 7 
          -- 左補0,長度3
          ,CASE
             WHEN S2."LMSPBK" = '1' THEN '812'
             WHEN S2."LMSPBK" = '2' THEN '006'
             WHEN S2."LMSPBK" = '3' THEN '700'
             WHEN S2."LMSPBK" = '4' THEN '103'
           ELSE '000' END                 AS "RepayBank"           -- 扣款銀行 VARCHAR2 3 
          ,LPAD(S2."LMSPCN",14,'0')       AS "RepayAcct"           -- 扣款帳號 VARCHAR2 14 
          ,'A'                            AS "CreateFlag"          -- 新增或取消 VARCHAR2 1 
          ,S2."LMSAPN"                    AS "FacmNo"              -- 額度號碼 DECIMAL 3 
          ,CASE WHEN S2."ACHCDT" > 0 THEN TRUNC(S2."ACHCDT" / 1000000)
           ELSE 0 END                     AS "ProcessDate"         -- 處理日期時間 Decimald 8   
          ,CASE WHEN S2."ATHFND" > 0 THEN S2."ATHFND"
           ELSE 0 END                     AS "StampFinishDate"     -- 核印完成日期時間 Decimald 8   
          ,NVL(S2."ATHCOD",' ')           AS "AuthStatus"          -- 授權狀態 VARCHAR2 1 
          ,S2."ACHCOD"                    AS "AuthMeth"            -- 授權方式 VARCHAR2 1 
          ,S2."ACHCDE"                    AS "MediaCode"           -- 媒體碼 VARCHAR2 1 
          ,''                             AS "BatchNo"             -- 批號 VARCHAR2 6 
          ,CASE WHEN S2."ACHCDT" > 0 THEN TRUNC(S2."ACHCDT" / 1000000)
           ELSE 0 END                     AS "PropDate"            -- 提出日期 Decimald 8 
          ,CASE WHEN S2."ATHFND" > 0 THEN S2."ATHFND"
           ELSE 0 END                     AS "RetrDate"            -- 提回日期 Decimald 8 
          ,0                              AS "DeleteDate"          -- 刪除日期 Decimald 8 
          ,NVL(FACM."LMSPRL",'00')        AS "RelationCode"        -- 與借款人關係 VARCHAR2 2 
          ,NVL(FACM."LMSPAN",u' ')        AS "RelAcctName"         -- 第三人帳戶戶名 NVARCHAR2 100 
          ,NVL(FACM."LMSPID",' ')         AS "RelationId"          -- 第三人身分證字號 VARCHAR2 10 0
          ,0                              AS "RelAcctBirthday"     -- 第三人出生日期 Decimald 8 
          ,''                             AS "RelAcctGender"       -- 第三人性別 VARCHAR2 1 
          ,''                             AS "AmlRsp"              -- AML回應碼 VARCHAR2 1
          ,''                             AS "TitaTxCd"            -- 交易代號 VARCHAR2 5
          ,NVL(AEM1."EmpNo",'999999')     AS "CreateEmpNo"         -- 建立者櫃員編號 VARCHAR2 6 0
          ,CASE
             WHEN S2."CRTDTM" > 0
             THEN TO_DATE(S2."CRTDTM",'YYYYMMDDHH24MISS')
           ELSE JOB_START_TIME
           END                 AS "CreateDate"          -- 建檔日期 DATE 0 0
          ,NVL(AEM2."EmpNo",'999999')     AS "LastUpdateEmpNo"     -- 修改者櫃員編號 VARCHAR2 6 0
          ,CASE
             WHEN S2."CHGDTM" > 0
             THEN TO_DATE(S2."CHGDTM",'YYYYMMDDHH24MISS')
           ELSE JOB_START_TIME
           END                 AS "LastUpdate"          -- 異動日期 DATE 0 0
          ,CASE WHEN S2."ACHCDT" > 0 THEN MOD(S2."ACHCDT" , 1000000)
           ELSE 0 END                     AS "ProcessTime"
          ,S2."ACHLAMT"                   AS "LimitAmt"            -- 每筆扣款限額 DECIMAL 14 
    FROM (SELECT "AH$ACRP"."CUSCDT"
                ,"AH$ACRP"."LMSACN"
                ,"AH$ACRP"."LMSPBK"
                ,"AH$ACRP"."LMSPCN"
                ,MIN("AH$ACRP"."LMSAPN") AS "LMSAPN"
          FROM "AH$ACRP"
          GROUP BY "AH$ACRP"."CUSCDT"
                  ,"AH$ACRP"."LMSACN"
                  ,"AH$ACRP"."LMSPBK"
                  ,"AH$ACRP"."LMSPCN" ) S1
    LEFT JOIN "AH$ACRP" S2 ON S2."CUSCDT" = S1."CUSCDT"
                          AND S2."LMSACN" = S1."LMSACN"
                          AND S2."LMSPBK" = S1."LMSPBK"
                          AND S2."LMSPCN" = S1."LMSPCN"
                          AND S2."LMSAPN" = S1."LMSAPN"
    LEFT JOIN "As400EmpNoMapping" AEM1 ON AEM1."As400TellerNo" = S2."CRTEMP"
    LEFT JOIN "As400EmpNoMapping" AEM2 ON AEM2."As400TellerNo" = S2."CHGEMP"
    LEFT JOIN (SELECT DISTINCT
                      "LMSACN"
                     ,"LMSPCN"
                     ,"LMSPRL"
                     ,"LMSPAN"
                     ,"LMSPID"
                     ,ROW_NUMBER() OVER (PARTITION BY "LMSACN","LMSPCN" ORDER BY "LMSPRL") AS "SEQ"
               FROM "LA$APLP"
               WHERE "LMSPYS" = 2
              ) FACM ON FACM."LMSACN" = S2."LMSACN"
                    AND FACM."LMSPCN" = S2."LMSPCN"
                    AND FACM."SEQ" = 1
    WHERE S2."CUSCDT" <= 19110000 * 2
    ;

    -- 記錄寫入筆數
    INS_CNT := INS_CNT + sql%rowcount;

    -- 寫入資料
    -- 2022-02-14 智偉新增: 寫入"送出授權待回覆"的資料
    INSERT INTO "AchAuthLog"
    SELECT TRUNC(S1."CRTDTM" / 1000000)      AS "AuthCreateDate"      -- 建檔日期 Decimald 8 
          ,S1."LMSACN"                       AS "CustNo"              -- 戶號 DECIMAL 7 
          -- 左補0,長度3
          ,CASE
             WHEN S1."LMSPBK" = '1' THEN '812'
             WHEN S1."LMSPBK" = '2' THEN '006'
             WHEN S1."LMSPBK" = '3' THEN '700'
             WHEN S1."LMSPBK" = '4' THEN '103'
           ELSE '000' END                 AS "RepayBank"           -- 扣款銀行 VARCHAR2 3 
          ,LPAD(S1."LMSPCN",14,'0')       AS "RepayAcct"           -- 扣款帳號 VARCHAR2 14 
          ,'A'                            AS "CreateFlag"          -- 新增或取消 VARCHAR2 1 
          ,S1."LMSAPN"                    AS "FacmNo"              -- 額度號碼 DECIMAL 3 
          ,CASE
             WHEN S1."ACHCDT" > 0 THEN TRUNC(S1."ACHCDT" / 1000000)
           ELSE 0 END                     AS "ProcessDate"         -- 處理日期 Decimald 8   
          ,CASE
             WHEN S1."ATHFND" > 0 THEN S1."ATHFND"
           ELSE 0 END                     AS "StampFinishDate"     -- 核印完成日期時間 Decimald 8   
          ,NVL(S1."ATHCOD",' ')           AS "AuthStatus"          -- 授權狀態 VARCHAR2 1 
          ,S1."ACHCOD"                    AS "AuthMeth"            -- 授權方式 VARCHAR2 1 
          ,''                             AS "MediaCode"           -- 媒體碼 VARCHAR2 1 
          ,''                             AS "BatchNo"             -- 批號 VARCHAR2 6 
          ,CASE
             WHEN S1."ACHCDT" > 0 THEN TRUNC(S1."ACHCDT" / 1000000)
           ELSE 0 END                     AS "PropDate"            -- 提出日期 Decimald 8 
          ,CASE
             WHEN S1."ATHFND" > 0 THEN S1."ATHFND"
           ELSE 0 END                     AS "RetrDate"            -- 提回日期 Decimald 8 
          ,0                              AS "DeleteDate"          -- 刪除日期 Decimald 8 
          ,NVL(FACM."LMSPRL",'00')        AS "RelationCode"        -- 與借款人關係 VARCHAR2 2 
          ,NVL(FACM."LMSPAN",u' ')        AS "RelAcctName"         -- 第三人帳戶戶名 NVARCHAR2 100 
          ,NVL(FACM."LMSPID",' ')         AS "RelationId"          -- 第三人身分證字號 VARCHAR2 10 0
          ,0                              AS "RelAcctBirthday"     -- 第三人出生日期 Decimald 8 
          ,''                             AS "RelAcctGender"       -- 第三人性別 VARCHAR2 1 
          ,''                             AS "AmlRsp"              -- AML回應碼 VARCHAR2 1
          ,''                             AS "TitaTxCd"            -- 交易代號 VARCHAR2 5
          ,NVL(AEM1."EmpNo",'999999')     AS "CreateEmpNo"         -- 建立者櫃員編號 VARCHAR2 6 0
          ,CASE
             WHEN S1."CRTDTM" > 0
             THEN TO_DATE(S1."CRTDTM",'YYYYMMDDHH24MISS')
           ELSE JOB_START_TIME
           END                 AS "CreateDate"          -- 建檔日期 DATE 0 0
          ,NVL(AEM2."EmpNo",'999999')     AS "LastUpdateEmpNo"     -- 修改者櫃員編號 VARCHAR2 6 0
          ,CASE
             WHEN S1."CHGDTM" > 0
             THEN TO_DATE(S1."CHGDTM",'YYYYMMDDHH24MISS')
           ELSE JOB_START_TIME
           END                 AS "LastUpdate"          -- 異動日期 DATE 0 0
          ,CASE WHEN S1."ACHCDT" > 0 THEN MOD(S1."ACHCDT" , 1000000)
           ELSE 0 END                     AS "ProcessTime"
          ,S1."ACHLAMT"                   AS "LimitAmt"            -- 每筆扣款限額 DECIMAL 14 
    FROM "AH$ACHP" S1
    LEFT JOIN "As400EmpNoMapping" AEM1 ON AEM1."As400TellerNo" = S1."CRTEMP"
    LEFT JOIN "As400EmpNoMapping" AEM2 ON AEM2."As400TellerNo" = S1."CHGEMP"
    LEFT JOIN (SELECT DISTINCT
                      "LMSACN"
                     ,"LMSPCN"
                     ,"LMSPRL"
                     ,"LMSPAN"
                     ,"LMSPID"
                     ,ROW_NUMBER() OVER (PARTITION BY "LMSACN","LMSPCN" ORDER BY "LMSPRL") AS "SEQ"
               FROM "LA$APLP"
               WHERE "LMSPYS" = 2
              ) FACM ON FACM."LMSACN" = S1."LMSACN"
                    AND FACM."LMSPCN" = S1."LMSPCN"
                    AND FACM."SEQ" = 1
    WHERE NVL(S1."ATHCOD",' ') = ' '
    ;

    -- 記錄寫入筆數
    INS_CNT := INS_CNT + sql%rowcount;

    -- LA$APLP有授權帳號資料，AH$ACHP、AH$ACRP都沒資料的資料補寫入
    INSERT INTO "AchAuthLog"
    WITH BAA AS (
    SELECT BAA."CustNo"
          ,BAA."RepayBank"
          ,BAA."RepayAcct"
          ,BAA."FacmNo"
          ,BAA."CreateEmpNo"
          ,BAA."CreateDate"
          ,BAA."LastUpdateEmpNo"
          ,BAA."LastUpdate"
          ,BAA."LimitAmt"
          ,ROW_NUMBER()
           OVER (
            PARTITION BY BAA."CustNo"
                       , BAA."RepayBank"
                       , BAA."RepayAcct"
            ORDER BY BAA."FacmNo"
           ) AS "BAASeq"           
    FROM "BankAuthAct" BAA
    LEFT JOIN "AchAuthLog" AAL ON AAL."AuthCreateDate" = "TbsDyF"
                              AND AAL."CustNo" = BAA."CustNo"
                              AND AAL."RepayBank" = BAA."RepayBank"
                              AND AAL."RepayAcct" = BAA."RepayAcct"
                              AND AAL."CreateFlag" = 'A'
    WHERE BAA."Status" = ' ' -- 空白:未授權
      AND BAA."RepayBank" != '700' -- 非郵局
      AND NVL(AAL."CustNo",0) = 0 -- 不存在的才寫入
    )
    SELECT "TbsDyF"                       AS "AuthCreateDate"      -- 建檔日期 Decimald 8 
          ,BAA."CustNo"                   AS "CustNo"  
          ,BAA."RepayBank"                AS "RepayBank"           -- 扣款銀行 VARCHAR2 3 
          ,BAA."RepayAcct"                AS "RepayAcct"           -- 扣款帳號 VARCHAR2 14 
          ,'A'                            AS "CreateFlag"          -- 新增或取消 VARCHAR2 1 
          ,BAA."FacmNo"                   AS "FacmNo"              -- 額度號碼 DECIMAL 3 
          ,0                              AS "ProcessDate"         -- 處理日期 Decimald 8   
          ,0                              AS "StampFinishDate"     -- 核印完成日期時間 Decimald 8   
          ,' '                            AS "AuthStatus"          -- 授權狀態 VARCHAR2 1 
          ,'A'                            AS "AuthMeth"            -- 授權方式 VARCHAR2 1 
          ,''                             AS "MediaCode"           -- 媒體碼 VARCHAR2 1 
          ,''                             AS "BatchNo"             -- 批號 VARCHAR2 6 
          ,0                              AS "PropDate"            -- 提出日期 Decimald 8 
          ,0                              AS "RetrDate"            -- 提回日期 Decimald 8 
          ,0                              AS "DeleteDate"          -- 刪除日期 Decimald 8 
          ,NVL(FACM."LMSPRL",'00')        AS "RelationCode"        -- 與借款人關係 VARCHAR2 2 
          ,NVL(FACM."LMSPAN",u' ')        AS "RelAcctName"         -- 第三人帳戶戶名 NVARCHAR2 100 
          ,NVL(FACM."LMSPID",' ')         AS "RelationId"          -- 第三人身分證字號 VARCHAR2 10 0
          ,0                              AS "RelAcctBirthday"     -- 第三人出生日期 Decimald 8 
          ,''                             AS "RelAcctGender"       -- 第三人性別 VARCHAR2 1 
          ,''                             AS "AmlRsp"              -- AML回應碼 VARCHAR2 1
          ,''                             AS "TitaTxCd"            -- 交易代號 VARCHAR2 5
          ,BAA."CreateEmpNo"              AS "CreateEmpNo"         -- 建立者櫃員編號 VARCHAR2 6 0
          ,BAA."CreateDate"               AS "CreateDate"          -- 建檔日期 DATE 0 0
          ,BAA."LastUpdateEmpNo"          AS "LastUpdateEmpNo"     -- 修改者櫃員編號 VARCHAR2 6 0
          ,BAA."LastUpdate"               AS "LastUpdate"          -- 異動日期 DATE 0 0
          ,0                              AS "ProcessTime"
          ,BAA."LimitAmt"                 AS "LimitAmt"            -- 每筆扣款限額 DECIMAL 14 
    FROM BAA
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
    ) FACM ON FACM."LMSACN" = BAA."CustNo"
          AND FACM."LMSPCN" = BAA."RepayAcct"
          AND FACM."SEQ" = 1
    WHERE BAA."BAASeq" = 1 -- 只取一筆
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
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_AchAuthLog_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;

/
