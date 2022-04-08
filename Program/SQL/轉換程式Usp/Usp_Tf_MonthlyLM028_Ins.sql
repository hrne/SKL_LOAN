--------------------------------------------------------
--  DDL for Procedure Usp_Tf_MonthlyLM028_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "Usp_Tf_MonthlyLM028_Ins" 
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
    DELETE FROM "MonthlyLM028";

    -- 寫入資料
    INSERT INTO "MonthlyLM028"
    SELECT "LN$DTAP"."LMSSTS"             AS "LMSSTS"              -- 戶況 DECIMAL 2 0
          ,"LN$DTAP"."CUSENT"             AS "CUSENT"              -- 企金別 DECIMAL 1 0
          ,"LN$DTAP"."CUSBRH"             AS "CUSBRH"              -- 營業單位別 VARCHAR2 4 0
          ,"LN$DTAP"."LMSACN"             AS "LMSACN"              -- 借款人戶號 DECIMAL 7 0
          ,"LN$DTAP"."LMSAPN"             AS "LMSAPN"              -- 額度編號 DECIMAL 3 0
          ,"LN$DTAP"."LMSASQ"             AS "LMSASQ"              -- 撥款序號 DECIMAL 3 0
          ,"LN$DTAP"."IRTRAT"             AS "IRTRAT"              -- 利率 DECIMAL 6 4
          ,"LN$DTAP"."LMSISC"             AS "LMSISC"              -- 繳息週期 DECIMAL 2 0
          ,SUBSTR("LN$DTAP"."LMSPBK",0,3)
                                          AS "LMSPBK"              -- 扣款銀行 VARCHAR2 3 0
          ,"LN$DTAP"."APLMON"             AS "APLMON"              -- 貸款期間－月 DECIMAL 2 0
          ,"LN$DTAP"."APLDAY"             AS "APLDAY"              -- 貸款期間－日 DECIMAL 3 0
          ,"LN$DTAP"."LMSLBL"             AS "LMSLBL"              -- 放款餘額 DECIMAL  
          ,"LN$DTAP"."AILIRT"             AS "AILIRT"              -- 利率區分 VARCHAR2 1 
          ,"LN$DTAP"."POSCDE"             AS "POSCDE"              -- 郵局存款別 VARCHAR2  
          ,"LN$DTAP"."LMSPDY"             AS "LMSPDY"              -- 應繳日 DECIMAL 2 
          ,"LN$DTAP"."IRTFSC"             AS "IRTFSC"              -- 首次調整週期 DECIMAL 2 
          ,"LN$DTAP"."IRTBCD"             AS "IRTBCD"              -- 基本利率代碼 VARCHAR2 2 
          ,"LN$DTAP"."IRTRATYR1"          AS "IRTRATYR1"           -- 利率1 DECIMAL 6 
          ,"LN$DTAP"."IRTRATYR2"          AS "IRTRATYR2"           -- 利率2 DECIMAL 6 
          ,"LN$DTAP"."IRTRATYR3"          AS "IRTRATYR3"           -- 利率3 DECIMAL 6 
          ,"LN$DTAP"."IRTRATYR4"          AS "IRTRATYR4"           -- 利率4 DECIMAL 6 
          ,"LN$DTAP"."IRTRATYR5"          AS "IRTRATYR5"           -- 利率5 DECIMAL 6 
          ,"LN$DTAP"."GDRID1"             AS "GDRID1"              -- 押品別１ DECIMAL 1 
          ,"LN$DTAP"."GDRID2"             AS "GDRID2"              -- 押品別２ DECIMAL 2 
          ,"LN$DTAP"."YYYY"               AS "YYYY"                -- 撥款日-年 DECIMAL 4 
          ,"LN$DTAP"."MONTH"              AS "MONTH"               -- 撥款日-月 DECIMAL 2 
          ,"LN$DTAP"."DAY"                AS "DAY"                 -- 撥款日-日 DECIMAL 2 
          ,"LN$DTAP"."W08CDE"             AS "W08CDE"              -- 到期日碼 DECIMAL 1 
          ,''                             AS "RELATION"            -- 是否為關係人 VARCHAR2 1 
          ,''                             AS "DPTLVL"              -- 制度別 VARCHAR2 1 
          ,''                             AS "ACTFSC"              -- 資金來源 VARCHAR2 1 
          ,0                              AS "LIRTRATYR"           -- 最新利率 DECIMAL 6 
          ,0                              AS "LIRTDAY"             -- 最新利率生效起日 DECIMAL 8 
          ,JOB_START_TIME                 AS "CreateDate"          -- 建檔日期時間 DATE 0 
          ,'999999'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 
          ,JOB_START_TIME                 AS "LastUpdate"          -- 最後更新日期時間 DATE 0 
          ,'999999'                       AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 
    FROM "LN$DTAP"
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
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_MonthlyLM028_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;


/
