--------------------------------------------------------
--  DDL for Procedure Usp_Tf_FacCaseAppl_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "Usp_Tf_FacCaseAppl_Ins" 
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
    EXECUTE IMMEDIATE 'ALTER TABLE "FacCaseAppl" DISABLE PRIMARY KEY CASCADE';
    EXECUTE IMMEDIATE 'TRUNCATE TABLE "FacCaseAppl" DROP STORAGE';
    EXECUTE IMMEDIATE 'ALTER TABLE "FacCaseAppl" ENABLE PRIMARY KEY';

    -- 寫入資料
    INSERT INTO "FacCaseAppl"
    SELECT "LA$CASP"."CASNUM"             AS "ApplNo"              -- 申請號碼 DECIMAL 7 
          ,"CustMain"."CustUKey"          AS "CustUKey"            -- 客戶識別碼 VARCHAR2 32 
          ,"LA$CASP"."CASIDT"             AS "ApplDate"            -- 申請日期 DECIMALD 8 
          ,NVL(APLP."CASNUM3",0)          AS "CreditSysNo"         -- 案件編號 DECIMAL 7
          ,0                              AS "SyndNo"              -- 聯貸案編號 DECIMAL 6
          ,'TWD'                          AS "CurrencyCode"        -- 申請幣別 VARCHAR2 3 
          ,"LA$CASP"."CASAMT"             AS "ApplAmt"             -- 申請金額 DECIMAL 16 2
          ,'ZZ999'                        AS "ProdNo"              -- 申請商品代碼 VARCHAR2 5 
          ,"LA$CASP"."CUSEM6"             AS "Estimate"            -- 估價 VARCHAR2 6 
          ,APLP."CASUNT"                  AS "DepartmentCode"      -- 案件隸屬單位 VARCHAR2 1
          ,"LA$CASP"."CASCDE"             AS "PieceCode"           -- 計件代碼 VARCHAR2 1 
          ,"LA$CASP"."CUSEM1"             AS "CreditOfficer"       -- 授信 VARCHAR2 6 
          ,"LA$CASP"."CUSEM2"             AS "LoanOfficer"         -- 放款專員 VARCHAR2 6 
          ,"LA$CASP"."CUSEM3"             AS "Introducer"          -- 介紹人 VARCHAR2 6 
          ,LS."EMPCOD"                    AS "Coorgnizer"          -- 協辦人 VARCHAR2(6 BYTE)
          ,''                             AS "InterviewerA"        -- 晤談一 VARCHAR2(6 BYTE)
          ,''                             AS "InterviewerB"        -- 晤談二 VARCHAR2(6 BYTE)	
          ,"LA$CASP"."CUSEM4"             AS "Supervisor"          -- 核決主管 VARCHAR2 6 
          ,"LA$CASP"."CASSTS"             AS "ProcessCode"         -- 處理情形 VARCHAR2 1 
          ,"LA$CASP"."CASDAT"             AS "ApproveDate"         -- 准駁日期 DECIMALD 8 
          ,NVL("GP"."GroupUKey",'')       AS "GroupUKey"           -- 團體戶識別碼 VARCHAR2 32 
          ,'0000'                         AS "BranchNo"
          ,''                             AS "IsLimit"
          ,''                             AS "IsRelated"
          ,''                             AS "IsLnrelNear"
          ,JOB_START_TIME                 AS "CreateDate"          -- 建檔日期時間 DATE  
          ,'999999'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 
          ,JOB_START_TIME                 AS "LastUpdate"          -- 最後更新日期時間 DATE  
          ,'999999'                       AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 
    FROM "LA$CASP"
    LEFT JOIN "CU$CUSP" ON "CU$CUSP"."CUSCIF" = "LA$CASP"."CUSCIF"
    LEFT JOIN "CustMain" ON TRIM("CustMain"."CustId") = TRIM("CU$CUSP"."CUSID1")
    LEFT JOIN ( SELECT DISTINCT
                       CAS."CASGCI"
                     , CM."CustUKey" AS "GroupUKey"
                FROM "LA$CASP" CAS
                LEFT JOIN "CU$CUSP" CU ON CU."CUSCIF" = CAS."CASGCI"
                LEFT JOIN "CustMain" CM ON TRIM(CM."CustId") = TRIM(CU."CUSID1")
                WHERE NVL(CAS."CASGCI",0) > 0
                  AND CM."CustUKey" IS NOT NULL
              ) GP ON GP."CASGCI" = "LA$CASP"."CASGCI"
    LEFT JOIN "LA$APLP" APLP ON APLP."APLNUM" = "LA$CASP"."CASNUM"
    LEFT JOIN "LN$LSEP" LS ON LS."LMSACN" = APLP."LMSACN" -- 串取協辦人員
                          AND LS."LMSAPN" = APLP."LMSAPN" 
    WHERE NVL("CustMain"."CustUKey",' ') <> ' '
    ;

    -- 記錄寫入筆數
    INS_CNT := INS_CNT + sql%rowcount;

    INSERT INTO "FacCaseAppl"
    SELECT 315855                         AS "ApplNo"              -- 申請號碼 DECIMAL 7 
          ,"CustMain"."CustUKey"          AS "CustUKey"            -- 客戶識別碼 VARCHAR2 32 
          ,19960409                       AS "ApplDate"            -- 申請日期 DECIMALD 8 
          ,0                              AS "CreditSysNo"         -- 案件編號 DECIMAL 7
          ,0                              AS "SyndNo"              -- 聯貸案編號 DECIMAL 6
          ,'TWD'                          AS "CurrencyCode"        -- 申請幣別 VARCHAR2 3 
          ,5000000                        AS "ApplAmt"             -- 申請金額 DECIMAL 16 2
          ,'ZZ999'                        AS "ProdNo"              -- 申請商品代碼 VARCHAR2 5 
          ,''                             AS "Estimate"            -- 估價 VARCHAR2 6 
          ,'1'                            AS "DepartmentCode"      -- 案件隸屬單位 VARCHAR2 1
          ,'3'                            AS "PieceCode"           -- 計件代碼 VARCHAR2 1 
          ,'G17500'                       AS "CreditOfficer"       -- 授信 VARCHAR2 6 
          ,'BB9215'                       AS "LoanOfficer"         -- 放款專員 VARCHAR2 6 
          ,''                             AS "Introducer"          -- 介紹人 VARCHAR2 6 
          ,''                             AS "Coorgnizer"          -- 協辦人 VARCHAR2(6 BYTE)
          ,''                             AS "InterviewerA"        -- 晤談一 VARCHAR2(6 BYTE)
          ,''                             AS "InterviewerB"        -- 晤談二 VARCHAR2(6 BYTE)	
          ,'E65168'                       AS "Supervisor"          -- 核決主管 VARCHAR2 6 
          ,'1'                            AS "ProcessCode"         -- 處理情形 VARCHAR2 1 
          ,19960409                       AS "ApproveDate"         -- 准駁日期 DECIMALD 8 
          ,''                             AS "GroupUKey"           -- 團體戶識別碼 VARCHAR2 32 
          ,'0000'                         AS "BranchNo" 
          ,''                             AS "IsLimit"
          ,''                             AS "IsRelated"
          ,''                             AS "IsLnrelNear"
          ,JOB_START_TIME                 AS "CreateDate"          -- 建檔日期時間 DATE  
          ,'999999'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 
          ,JOB_START_TIME                 AS "LastUpdate"          -- 最後更新日期時間 DATE  
          ,'999999'                       AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6
    FROM "CustMain" 
    WHERE "CustNo" = 188400
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
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_FacCaseAppl_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;


/
