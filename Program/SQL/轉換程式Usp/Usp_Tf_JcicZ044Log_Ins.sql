--------------------------------------------------------
--  DDL for Procedure Usp_Tf_JcicZ044Log_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_Tf_JcicZ044Log_Ins" 
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
    DELETE FROM "JcicZ044Log";

    -- 寫入資料
    INSERT INTO "JcicZ044Log"
    SELECT "Ukey"                AS "Ukey"                    -- 流水號 VARCHAR2 32 0
          ,LPAD('1', 18, '0')    AS "TxSeq"                   -- 交易序號 VARCHAR2 18 0
          ,"TranKey"             AS "TranKey"                 -- 交易代碼 VARCHAR2 1 0
          ,"DebtCode"            AS "DebtCode"                -- 負債主因 VARCHAR2 2 0
          ,"NonGageAmt"          AS "NonGageAmt"              -- 無擔保金融債務協商總金額 Decimal 9 0
          ,"Period"              AS "Period"                  -- 期數 Decimal 3 0
          ,"Rate"                AS "Rate"                    -- 利率 Decimal 5 2
          ,"MonthPayAmt"         AS "MonthPayAmt"             -- 協商方案估計月付金 Decimal 9 0
          ,"ReceYearIncome"      AS "ReceYearIncome"          -- 最近年度綜合所得總額 Decimal 9 0
          ,"ReceYear"            AS "ReceYear"                -- 最近年度別 Decimal 4 0
          ,"ReceYear2Income"     AS "ReceYear2Income"         -- 前二年度綜合所得總額 Decimal 9 0
          ,"ReceYear2"           AS "ReceYear2"               -- 前二年度別 Decimal 4 0
          ,"CurrentMonthIncome"  AS "CurrentMonthIncome"      -- 目前每月收入 Decimal 9 0
          ,"LivingCost"          AS "LivingCost"              -- 生活支出總額 Decimal 9 0
          ,"CompName"            AS "CompName"                -- 目前主要所得來源公司名稱 NVARCHAR2 40 0
          ,"CompId"              AS "CompId"                  -- 目前主要所得公司統編 VARCHAR2 8 0
          ,"CarCnt"              AS "CarCnt"                  -- 債務人名下車輛數量 Decimal 2 0
          ,"HouseCnt"            AS "HouseCnt"                -- 債務人名下建物筆數 Decimal 2 0
          ,"LandCnt"             AS "LandCnt"                 -- 債務人名下土地筆數 Decimal 2 0
          ,"ChildCnt"            AS "ChildCnt"                -- 撫養子女數 Decimal 2 0
          ,"ChildRate"           AS "ChildRate"               -- 撫養子女責任比率 Decimal 5 1
          ,"ParentCnt"           AS "ParentCnt"               -- 撫養父母人數 Decimal 2 0
          ,"ParentRate"          AS "ParentRate"              -- 撫養父母責任比率 Decimal 5 1
          ,"MouthCnt"            AS "MouthCnt"                -- 其他法定撫養人數 Decimal 2 0
          ,"MouthRate"           AS "MouthRate"               -- 其他法定撫養人之責任比率 Decimal 5 1
          ,"GradeType"           AS "GradeType"               -- 屬二階段還款方案之階段註記 VARCHAR2 1 0
          ,"PayLastAmt"          AS "PayLastAmt"              -- 第一階段最後一期應繳金額 Decimal 9 0
          ,"Period2"             AS "Period2"                 -- 第二段期數 Decimal 3 0
          ,"Rate2"               AS "Rate2"                   -- 第二階段利率 Decimal 5 2
          ,"MonthPayAmt2"        AS "MonthPayAmt2"            -- 第二階段協商方案估計月付金 Decimal 9 0
          ,"PayLastAmt2"         AS "PayLastAmt2"             -- 第二階段最後一期應繳金額 Decimal 9 0
          ,"OutJcicTxtDate"      AS "OutJcicTxtDate"          -- 轉出JCIC文字檔日期 Decimald 8 0
          ,JOB_START_TIME        AS "CreateDate"              -- 建檔日期時間 DATE 8 0
          ,'999999'              AS "CreateEmpNo"             -- 建檔人員 VARCHAR2 6 0
          ,JOB_START_TIME        AS "LastUpdate"              -- 最後更新日期時間 DATE 8 0
          ,'999999'              AS "LastUpdateEmpNo"         -- 最後更新人員 VARCHAR2 6 0
    FROM "JcicZ044"
    WHERE "OutJcicTxtDate" > 0
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
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_JcicZ044Log_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;



/
