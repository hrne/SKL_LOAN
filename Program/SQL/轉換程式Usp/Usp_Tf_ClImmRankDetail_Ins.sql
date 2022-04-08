
set define off;

  CREATE OR REPLACE PROCEDURE "Usp_Tf_ClImmRankDetail_Ins" 
(
    -- 參數
    JOB_START_TIME OUT TIMESTAMP, --程式起始時間
    JOB_END_TIME   OUT TIMESTAMP, --程式結束時間
    INS_CNT        OUT INT,       --新增資料筆數
    ERROR_MSG      OUT VARCHAR2   --錯誤訊息
)
AS
BEGIN
    --------------------------------------------------------
    --  DDL for Procedure Usp_Tf_ClImmRankDetail_Ins
    --  St1 Chih Wei 2021-12-16
    --------------------------------------------------------
    -- 筆數預設0
    INS_CNT:=0;
    -- 記錄程式起始時間
    JOB_START_TIME := SYSTIMESTAMP;

    -- 刪除舊資料
    EXECUTE IMMEDIATE 'ALTER TABLE "ClImmRankDetail" DISABLE PRIMARY KEY CASCADE';
    EXECUTE IMMEDIATE 'TRUNCATE TABLE "ClImmRankDetail" DROP STORAGE';
    EXECUTE IMMEDIATE 'ALTER TABLE "ClImmRankDetail" ENABLE PRIMARY KEY';

    -- 寫入資料
    INSERT INTO "ClImmRankDetail" (
      "ClCode1"             -- 擔保品代號1 DECIMAL 1 
    , "ClCode2"             -- 擔保品代號2 DECIMAL 2 
    , "ClNo"                -- 擔保品編號 DECIMAL 7 
    , "SettingSeq"          -- 設定順位(1~9) VARCHAR2 1 
    , "FirstCreditor"       -- 前一順位債權人 NVARCHAR2 40 
    , "FirstAmt"            -- 前一順位金額 DECIMAL 16 2
    , "CreateDate"          -- 建檔日期時間 DATE  
    , "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 
    , "LastUpdate"          -- 最後更新日期時間 DATE  
    , "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 
    )
    WITH GDTP AS (
      SELECT GDRID1
           , GDRID2
           , GDRNUM
           , CASE
               WHEN GDTPTY = 2 -- 此擔保品設定順位為2時,則前一順位債權人的設定順位為1
               THEN 1
               WHEN GDTPTY = 3 -- 此擔保品設定順位為3時,則前一順位債權人的設定順位為2
               THEN 2
               WHEN GDTPTY = 4 -- 此擔保品設定順位為4時,則前一順位債權人的設定順位為3
               THEN 3
             ELSE 0
             END               AS "SettingSeq"
           , GDTP1M            AS "FirstCreditor"
           , GDTP1A            AS "FirstAmt"
      FROM "LA$GDTP"
      WHERE GDTPTY IN (2,3,4)
      UNION
      SELECT GDRID1
           , GDRID2
           , GDRNUM
           , CASE
               WHEN GDTPTY = 3 -- 此擔保品設定順位為3時,則前二順位債權人的設定順位為1
               THEN 1
               WHEN GDTPTY = 4 -- 此擔保品設定順位為4時,則前二順位債權人的設定順位為2
               THEN 2
             ELSE 0
             END               AS "SettingSeq"
           , GDTP2M            AS "FirstCreditor"
           , GDTP2A            AS "FirstAmt"
      FROM "LA$GDTP"
      WHERE GDTPTY IN (3,4)
      UNION
      SELECT GDRID1
           , GDRID2
           , GDRNUM
           , CASE
               WHEN GDTPTY = 4 -- 此擔保品設定順位為4時,則前三順位債權人的設定順位為1
               THEN 1
             ELSE 0
             END               AS "SettingSeq"
           , GDTP3M            AS "FirstCreditor"
           , GDTP3A            AS "FirstAmt"
      FROM "LA$GDTP"
      WHERE GDTPTY = 4
    )
    SELECT S1."ClCode1"                   AS "ClCode1"             -- 擔保品代號1 DECIMAL 1 
         , S1."ClCode2"                   AS "ClCode2"             -- 擔保品代號2 DECIMAL 2 
         , S1."ClNo"                      AS "ClNo"                -- 擔保品編號 DECIMAL 7 
         , S2."SettingSeq"                AS "SettingSeq"          -- 設定順位(1~9) VARCHAR2 1 
         , S2."FirstCreditor"             AS "FirstCreditor"       -- 前一順位債權人 NVARCHAR2 40 
         , S2."FirstAmt"                  AS "FirstAmt"            -- 前一順位金額 DECIMAL 16 2
          ,JOB_START_TIME                 AS "CreateDate"          -- 建檔日期時間 DATE  
          ,'999999'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 
          ,JOB_START_TIME                 AS "LastUpdate"          -- 最後更新日期時間 DATE  
          ,'999999'                       AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 
    FROM "ClNoMapping" S1
    LEFT JOIN GDTP S2 ON S2."GDRID1" = S1."GDRID1"
                     AND S2."GDRID2" = S1."GDRID2"
                     AND S2."GDRNUM" = S1."GDRNUM"
    WHERE S1."ClCode1" >= 1
      AND S1."ClCode1" <= 2
      AND S2."SettingSeq" > 0
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
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_ClImmRankDetail_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;



/
