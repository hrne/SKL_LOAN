CREATE OR REPLACE PROCEDURE "Usp_Tf_ClOtherRights_Ins" 
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
    EXECUTE IMMEDIATE 'ALTER TABLE "ClOtherRights" DISABLE PRIMARY KEY CASCADE';
    EXECUTE IMMEDIATE 'TRUNCATE TABLE "ClOtherRights" DROP STORAGE';
    EXECUTE IMMEDIATE 'ALTER TABLE "ClOtherRights" ENABLE PRIMARY KEY';

    -- 寫入資料
    INSERT INTO "ClOtherRights" (
        "ClCode1"         -- 擔保品代號1 DECIMAL 1
      , "ClCode2"         -- 擔保品代號2 DECIMAL 2
      , "ClNo"            -- 擔保品編號 DECIMAL 7
      , "Seq"             -- 他項權利登記次序 VARCHAR2 8
      , "City"            -- 縣市    VARCHAR2 4
      , "OtherCity"       -- 其他縣市 VARCHAR2 40
      , "LandAdm"         -- 地政 VARCHAR2 2
      , "OtherLandAdm"    -- 其他地政 VARCHAR2 40
      , "RecYear"         -- 收件年   DECIMAL 3
      , "RecWord"         -- 收件字  VARCHAR2 3
      , "OtherRecWord"    -- 其他收件字  VARCHAR2 40
      , "RecNumber"       -- 收件號  VARCHAR2 6
      , "RightsNote"      -- 權利價值說明 VARCHAR2 2
      , "SecuredTotal"    -- 擔保債權總金額 DECIMAL 16
      , "ReceiveFg"       -- 領取記號 DECIMAL 1
      , "ChoiceDate"      -- 篩選資料日期 DecimalD 8
      , "ReceiveCustNo"   -- 篩選戶號 DECIMAL 7
      , "CloseNo"         -- 清償序號 DECIMAL 3
      , "SecuredDate"     -- 擔保債權確定日期 DecimalD 8
      , "Location"        -- 建物坐落地號 NVARCHAR2 40
      , "CreateDate"      -- 建檔日期時間 DATE 
      , "CreateEmpNo"     -- 建檔人員 VARCHAR2 6
      , "LastUpdate"      -- 最後更新日期時間 DATE 
      , "LastUpdateEmpNo" -- 最後更新人員 VARCHAR2 6
    )
    WITH rawData AS (
        SELECT TFM."ClCode1"
             , TFM."ClCode2"
             , TFM."ClNo"
             , TFM."Seq"
             , CASE 
                 WHEN TFM."ClCode1" = 1 
                 THEN NVL(S4."LGTSAM",0) 
                 WHEN TFM."ClCode1" = 2 
                 THEN NVL(S5."LGTSAM",0) 
                 WHEN TFM."ClCode1" = 5
                 THEN NVL(BG."BGTAMT",0) 
               ELSE 0 END           AS LGTSAM
             , TFM.GDTRDT           AS GDTRDT
        FROM "TfClOtherRightsMap" TFM
        LEFT JOIN "ClNoMapping" CNM ON CNM."ClCode1" = TFM."ClCode1"
                                   AND CNM."ClCode2" = TFM."ClCode2"
                                   AND CNM."ClNo" = TFM."ClNo"
        LEFT JOIN "LA$HGTP" S4 ON S4."GDRID1" = CNM."GDRID1" 
                              AND S4."GDRID2" = CNM."GDRID2" 
                              AND S4."GDRNUM" = CNM."GDRNUM" 
                              AND S4."LGTSEQ" = CNM."LGTSEQ" 
                              AND CNM."ClCode1" = 1 
        LEFT JOIN "LA$LGTP" S5 ON S5."GDRID1" = CNM."GDRID1" 
                              AND S5."GDRID2" = CNM."GDRID2" 
                              AND S5."GDRNUM" = CNM."GDRNUM" 
                              AND S5."LGTSEQ" = CNM."LGTSEQ" 
                              AND CNM."ClCode1" = 2
    )
    SELECT "ClCode1"                 AS "ClCode1"         -- 擔保品代號1 DECIMAL 1
         , "ClCode2"                 AS "ClCode2"         -- 擔保品代號2 DECIMAL 2
         , "ClNo"                    AS "ClNo"            -- 擔保品編號 DECIMAL 7
         , "Seq"                     AS "Seq"             -- 他項權利登記次序 VARCHAR2 8
         , ''                        AS "City"            -- 縣市    VARCHAR2 4
         , ''                        AS "OtherCity"       -- 其他縣市 VARCHAR2 40
         , ''                        AS "LandAdm"         -- 地政 VARCHAR2 2
         , ''                        AS "OtherLandAdm"    -- 其他地政 VARCHAR2 40
         , 0                         AS "RecYear"         -- 收件年   DECIMAL 3
         , ''                        AS "RecWord"         -- 收件字  VARCHAR2 3
         , ''                        AS "OtherRecWord"    -- 其他收件字  VARCHAR2 40
         , ''                        AS "RecNumber"       -- 收件號  VARCHAR2 6
         , ''                        AS "RightsNote"      -- 權利價值說明 VARCHAR2 2
         , LGTSAM                    AS "SecuredTotal"    -- 擔保債權總金額 DECIMAL 16
         , 0                         AS "ReceiveFg"       -- 領取記號 DECIMAL 1
         , 0                         AS "ChoiceDate"      -- 篩選資料日期 DecimalD 8
         , 0                         AS "ReceiveCustNo"   -- 篩選戶號 DECIMAL 7
         , 0                         AS "CloseNo"         -- 清償序號 DECIMAL 3
         , GDTRDT                    AS "SecuredDate"     -- 擔保債權確定日期 DecimalD 8
         , ''                        AS "Location"        -- 建物坐落地號 NVARCHAR2 40
         , JOB_START_TIME            AS "CreateDate"      -- 建檔日期時間 DATE 
         , '999999'                  AS "CreateEmpNo"     -- 建檔人員 VARCHAR2 6
         , JOB_START_TIME            AS "LastUpdate"      -- 最後更新日期時間 DATE 
         , '999999'                  AS "LastUpdateEmpNo" -- 最後更新人員 VARCHAR2 6
    FROM rawData
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
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_ClOtherRights_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;
/