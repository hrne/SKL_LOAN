--------------------------------------------------------
--  DDL for Procedure Usp_Tf_AchAuthLog_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "Usp_Tf_AchAuthLog_Ins" 
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
          ,S2."ATHCOD"                    AS "AuthStatus"          -- 授權狀態 VARCHAR2 1 
          ,S2."ACHCOD"                    AS "AuthMeth"            -- 授權方式 VARCHAR2 1 
          ,S2."ACHLAMT"                   AS "LimitAmt"            -- 每筆扣款限額 DECIMAL 14 
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
          ,'999999'                       AS "CreateEmpNo"         -- 建立者櫃員編號 VARCHAR2 6 
          ,JOB_START_TIME                 AS "CreateDate"          -- 建立日期時間 DATE  
          ,'999999'                       AS "LastUpdateEmpNo"     -- 修改者櫃員編號 VARCHAR2 6 
          ,JOB_START_TIME                 AS "LastUpdate"          -- 修改日期時間 DATE  
          ,CASE WHEN S2."ACHCDT" > 0 THEN MOD(S2."ACHCDT" , 1000000)
           ELSE 0 END                     AS "ProcessTime"
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
             WHEN S1."ACHCDT" > 0 THEN MOD(S1."ACHCDT" , 1000000)
           ELSE 0 END                     AS "ProcessDate"         -- 處理時間 Decimal 6 
          ,CASE
             WHEN S1."ATHFND" > 0 THEN S1."ATHFND"
           ELSE 0 END                     AS "StampFinishDate"     -- 核印完成日期時間 Decimald 8   
          ,NVL(S1."ATHCOD",' ')           AS "AuthStatus"          -- 授權狀態 VARCHAR2 1 
          ,S1."ACHCOD"                    AS "AuthMeth"            -- 授權方式 VARCHAR2 1 
          ,S1."ACHLAMT"                   AS "LimitAmt"            -- 每筆扣款限額 DECIMAL 14 
          ,' '                            AS "MediaCode"           -- 媒體碼 VARCHAR2 1 
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
          ,'999999'                       AS "CreateEmpNo"         -- 建立者櫃員編號 VARCHAR2 6 
          ,JOB_START_TIME                 AS "CreateDate"          -- 建立日期時間 DATE  
          ,'999999'                       AS "LastUpdateEmpNo"     -- 修改者櫃員編號 VARCHAR2 6 
          ,JOB_START_TIME                 AS "LastUpdate"          -- 修改日期時間 DATE  
          ,CASE WHEN S1."ACHCDT" > 0 THEN MOD(S1."ACHCDT" , 1000000)
           ELSE 0 END                     AS "ProcessTime"
    FROM "AH$ACHP" S1
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

    -- 記錄程式結束時間
    JOB_END_TIME := SYSTIMESTAMP;

    commit;

    -- 例外處理
    Exception
    WHEN OTHERS THEN
    ERROR_MSG := SQLERRM || CHR(13) || CHR(10) || dbms_utility.format_error_backtrace;
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_AchAuthLog_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;


/
