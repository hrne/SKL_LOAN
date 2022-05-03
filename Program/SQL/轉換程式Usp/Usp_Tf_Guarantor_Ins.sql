--------------------------------------------------------
--  DDL for Procedure Usp_Tf_Guarantor_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_Tf_Guarantor_Ins" 
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
    EXECUTE IMMEDIATE 'ALTER TABLE "Guarantor" DISABLE PRIMARY KEY CASCADE';
    EXECUTE IMMEDIATE 'TRUNCATE TABLE "Guarantor" DROP STORAGE';
    EXECUTE IMMEDIATE 'ALTER TABLE "Guarantor" ENABLE PRIMARY KEY';

    -- 寫入資料
    INSERT INTO "Guarantor"
    SELECT "LA$GRTP"."APLNUM"             AS "ApproveNo"           -- 核准號碼 DECIMAL 7 
          ,"CustMain"."CustUKey"          AS "GuaUKey"             -- 保證人客戶識別碼 VARCHAR2 32 
          ,LPAD("LA$GRTP"."GRTTYP",2,'0') AS "GuaRelCode"          -- 保證人關係代碼 VARCHAR2 2 
          ,"LA$GRTP"."GRTAMT"             AS "GuaAmt"              -- 保證金額 DECIMAL 16 2
          -- 2021-08-20 智偉更新 REF "CdCode"."DefCode" = 'GuaTypeCode'
          ,CASE
             WHEN LPAD("LA$GRTP"."GRTCLS",2,'0') = '01' -- 連帶保證人 REF.TB$SPLP.TB$FNM = 'GRTCLS'
             THEN '01' -- 01:連帶保證人
             WHEN LPAD("LA$GRTP"."GRTCLS",2,'0') = '02' -- 一般保證人 REF.TB$SPLP.TB$FNM = 'GRTCLS'
             THEN '03' -- 03:一般保證人
             WHEN LPAD("LA$GRTP"."GRTCLS",2,'0') = '03' -- 共同借款人 REF.TB$SPLP.TB$FNM = 'GRTCLS'
             THEN '06' -- 06:共同保證人
             WHEN LPAD("LA$GRTP"."GRTCLS",2,'0') = '04' -- 擔保品提供人 REF.TB$SPLP.TB$FNM = 'GRTCLS'
             THEN '05' -- 05:擔保品提供人
           ELSE LPAD("LA$GRTP"."GRTCLS",2,'0')
           END                            AS "GuaTypeCode"         -- 保證類別代碼 VARCHAR2 2 
          ,"LA$GRTP"."GRTDAT"             AS "GuaDate"             -- 對保日期 DecimalD 8 
          ,"LA$GRTP"."GRTGST"             AS "GuaStatCode"         -- 保證狀況碼 VARCHAR2 1 
          ,"LA$GRTP"."GRTDLD"             AS "CancelDate"          -- 解除日期 DecimalD 8 
          ,JOB_START_TIME                 AS "CreateDate"          -- 建檔日期時間 DATE  
          ,'999999'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 
          ,JOB_START_TIME                 AS "LastUpdate"          -- 最後更新日期時間 DATE  
          ,'999999'                       AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 
    FROM "LA$GRTP"
    LEFT JOIN "CU$CUSP" ON "CU$CUSP"."CUSCIF"  = "LA$GRTP"."CUSCIF"
    LEFT JOIN "CustMain" ON TRIM("CustMain"."CustId") = TRIM("CU$CUSP"."CUSID1")
    WHERE NVL("CustMain"."CustUKey",' ') <> ' '
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
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_Guarantor_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;




/
