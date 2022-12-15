CREATE OR REPLACE PROCEDURE "Usp_Tf_ClFac_Upd" 
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

    -- 寫入資料
    MERGE INTO "ClFac" T0
    USING (
      SELECT APLP."LMSACN"
           , APLP."LMSAPN"
           , SUM(CL."LGTSAM") AS "OriSettingAmt"
      FROM "LA$APLP" APLP
      LEFT JOIN (
        SELECT "GDRID1"
             , "GDRID2"
             , "GDRNUM"
             , "LGTSAM"
        FROM "LA$HGTP" -- 建物
        WHERE "LGTSAM" != 0
          AND "GDRID1" != 2
        UNION ALL
        SELECT "GDRID1"
             , "GDRID2"
             , "GDRNUM"
             , "LGTSAM"
        FROM "LA$LGTP" -- 土地
        WHERE "LGTSAM" != 0
          AND "GDRID1" != 1
        UNION ALL
        SELECT "GDRID1"
             , "GDRID2"
             , "GDRNUM"
             , "CGT018"
        FROM "LN$CGTP" -- 動產
        WHERE "CGT018" != 0
        UNION ALL
        SELECT "GDRID1"
             , "GDRID2"
             , "GDRNUM"
             , "BGTAMT"
        FROM "LA$BGTP" -- 保證
        WHERE "BGTAMT" != 0
        UNION ALL
        SELECT "GDRID1"
             , "GDRID2"
             , "GDRNUM"
             , "SGDQTY"
        FROM "LA$SGDP" -- 股票
        WHERE "SGDQTY" != 0
      ) CL ON CL."GDRID1" = APLP."GDRID1"
          AND CL."GDRID2" = APLP."GDRID2"
          AND CL."GDRNUM" = APLP."GDRNUM"
    GROUP BY APLP."LMSACN"
           , APLP."LMSAPN"
    ) S0
    ON (
      T0."CustNo" = S0."LMSACN"
      AND T0."FacmNo" = S0."LMSAPN"
      AND T0."MainFlag" = 'Y' -- 2021-08-10 智偉增加邏輯
      AND S0."OriSettingAmt" != 0
    )
    WHEN MATCHED THEN UPDATE
    SET T0."OriSettingAmt" = S0."OriSettingAmt"
    ;

    -- 記錄程式結束時間
    JOB_END_TIME := SYSTIMESTAMP;

    commit;

    -- 例外處理
    Exception
    WHEN OTHERS THEN
    ERROR_MSG := SQLERRM || CHR(13) || CHR(10) || dbms_utility.format_error_backtrace;
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_ClFac_Upd',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;

/
