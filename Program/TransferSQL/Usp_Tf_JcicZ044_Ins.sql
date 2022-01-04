--------------------------------------------------------
--  DDL for Procedure Usp_Tf_JcicZ044_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "Usp_Tf_JcicZ044_Ins" 
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
    DELETE FROM "JcicZ044";

    -- 寫入資料
    INSERT INTO "JcicZ044"
    SELECT "TBJCICZ044"."TRANSACTIONSID"  AS "TranKey"             -- 交易代碼 VARCHAR2 1 0
          ,"TBJCICZ044"."SUBMITID"        AS "SubmitKey"           -- 報送單位代號 VARCHAR2 10 0
          ,"TBJCICZ044"."CUSTIDN"         AS "CustId"              -- 債務人IDN VARCHAR2 10 0
          ,"TBJCICZ044"."RC_DATE"         AS "RcDate"              -- 協商申請日 Decimald 8 0
          ,"TBJCICZ044"."DEBT_CODE"       AS "DebtCode"            -- 負債主因 VARCHAR2 2 0
          ,NVL("TBJCICZ044"."NON_GAGE_AMT",0)    AS "NonGageAmt"          -- 無擔保金融債務協商總金額 Decimal 22 0
          ,NVL("TBJCICZ044"."PERIOD",0)          AS "Period"              -- 期數 Decimal 22 0
          ,NVL("TBJCICZ044"."RATE",0)            AS "Rate"                -- 利率 Decimal 22 4
          ,NVL("TBJCICZ044"."PAY_AMT",0)         AS "MonthPayAmt"         -- 協商方案估計月付金 Decimal 22 0
          ,NVL("TBJCICZ044"."RECE_YEAR_INCOME",0) AS "ReceYearIncome"     -- 最近年度綜合所得總額 Decimal 22 0
          ,"TBJCICZ044"."RECE_YEAR"       AS "ReceYear"            -- 最近年度別 VARCHAR2 4 0
          ,NVL("TBJCICZ044"."RECE_2YEAR_INCOME",0) AS "ReceYear2Income"   -- 前二年度綜合所得總額 Decimal 22 0
          ,"TBJCICZ044"."RECE_2YEAR"      AS "ReceYear2"           -- 前二年度別 VARCHAR2 4 0
          ,NVL("TBJCICZ044"."CURRENT_INCOME",0)  AS "CurrentMonthIncome"  -- 目前每月收入 Decimal 22 0
          ,NVL("TBJCICZ044"."LIVING_EXPENSE",0)  AS "LivingCost"          -- 生活支出總額 Decimal 22 0
          ,"TBJCICZ044"."BAN_NAME"        AS "CompName"            -- 目前主要所得來源公司名稱 NVARCHAR2 160 0
          ,"TBJCICZ044"."BAN"             AS "CompId"              -- 目前主要所得公司統編 VARCHAR2 8 0
          ,NVL("TBJCICZ044"."CAR_CNT",0)         AS "CarCnt"              -- 債務人名下車輛數量 Decimal 22 0
          ,NVL("TBJCICZ044"."HOUSE_CNT",0)       AS "HouseCnt"            -- 債務人名下建物筆數 Decimal 22 0
          ,NVL("TBJCICZ044"."LAND_CNT",0)        AS "LandCnt"             -- 債務人名下土地筆數 Decimal 22 0
          ,NVL("TBJCICZ044"."CHILD_CNT",0)       AS "ChildCnt"            -- 撫養子女數 Decimal 22 0
          ,NVL("TBJCICZ044"."CHILD_RATE",0)      AS "ChildRate"           -- 母養子女責任比率 Decimal 22 5
          ,NVL("TBJCICZ044"."PARENT_CNT",0)      AS "ParentCnt"           -- 撫養父母人數 Decimal 22 0
          ,NVL("TBJCICZ044"."PARENT_RATE",0)     AS "ParentRate"          -- 撫養父母責任比率 Decimal 22 5
          ,NVL("TBJCICZ044"."MOUTH_CNT",0)       AS "MouthCnt"            -- 其他法定撫養人數 Decimal 22 0
          ,NVL("TBJCICZ044"."MOUTH_RATE",0)      AS "MouthRate"           -- 其他法定撫養人之責任比率 Decimal 22 5
          ,"TBJCICZ044"."GRADETYPE"       AS "GradeType"           -- 屬二階段還款方案之階段註記 VARCHAR2 4 0
          ,NVL("TBJCICZ044"."PAY_LASTAMT",0)     AS "PayLastAmt"          -- 第一階段最後一期應繳金額 Decimal 22 0
          ,NVL("TBJCICZ044"."PERIOD2",0)         AS "Period2"             -- 第二段期數 Decimal 22 0
          ,NVL("TBJCICZ044"."RATE2",0)           AS "Rate2"               -- 第二階段利率 Decimal 22 4
          ,NVL("TBJCICZ044"."PAY_AMT2",0)        AS "MonthPayAmt2"        -- 第二階段協商方案估計月付金 Decimal 22 0
          ,NVL("TBJCICZ044"."PAY_LASTAMT2",0)    AS "PayLastAmt2"         -- 第二階段最後一期應繳金額 Decimal 22 0
          ,"TBJCICZ044"."JCICEXPORTDATE"  AS "OutJcictxtDate"      -- 轉出JCIC文字檔日期 Decimald 8 0
          ,SYS_GUID()                     AS "Ukey"                -- 流水號 VARCHAR2 32
          ,JOB_START_TIME                 AS "CreateDate"          -- 建檔日期時間 DATE 8 0
          ,'999999'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 0
          ,JOB_START_TIME                 AS "LastUpdate"          -- 最後更新日期時間 DATE 8 0
          ,'999999'                       AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 0
    FROM "TBJCICZ044"
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
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_JcicZ044_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;


/
