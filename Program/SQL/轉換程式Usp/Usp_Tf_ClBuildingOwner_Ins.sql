CREATE OR REPLACE PROCEDURE "Usp_Tf_ClBuildingOwner_Ins" 
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
    EXECUTE IMMEDIATE 'ALTER TABLE "ClBuildingOwner" DISABLE PRIMARY KEY CASCADE';
    EXECUTE IMMEDIATE 'TRUNCATE TABLE "ClBuildingOwner" DROP STORAGE';
    EXECUTE IMMEDIATE 'ALTER TABLE "ClBuildingOwner" ENABLE PRIMARY KEY';

    -- 寫入資料
    INSERT INTO "ClBuildingOwner" (
        "ClCode1"             -- 擔保品代號1 DECIMAL 1 
      , "ClCode2"             -- 擔保品代號2 DECIMAL 2 
      , "ClNo"                -- 擔保品編號 DECIMAL 7 
      , "OwnerCustUKey"       -- 所有權人客戶識別碼 VARCHAR2 32 
      , "OwnerRelCode"        -- 與授信戶關係 VARCHAR2 2
      , "OwnerPart"           -- 持分比率(分子) DECIMAL 10
      , "OwnerTotal"          -- 持分比率(分母) DECIMAL 10
      , "CreateDate"          -- 建檔日期時間 DATE  
      , "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 
      , "LastUpdate"          -- 最後更新日期時間 DATE  
      , "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 
    )
    SELECT DISTINCT
           CNM."ClCode1"                  AS "ClCode1"             -- 擔保品代號1 DECIMAL 1 
          ,CNM."ClCode2"                  AS "ClCode2"             -- 擔保品代號2 DECIMAL 2 
          ,CNM."ClNo"                     AS "ClNo"                -- 擔保品編號 DECIMAL 7 
          ,CM."CustUKey"                  AS "OwnerCustUKey"       -- 所有權人客戶識別碼 VARCHAR2 32 
          ,''                             AS "OwnerRelCode"        -- 與授信戶關係 VARCHAR2 2
          -- 擔保品持分
          ,1                              AS "OwnerPart"           -- 持分比率(分子) DECIMAL 10
          ,1                              AS "OwnerTotal"          -- 持分比率(分母) DECIMAL 10
          ,JOB_START_TIME                 AS "CreateDate"          -- 建檔日期時間 DATE  
          ,'999999'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 
          ,JOB_START_TIME                 AS "LastUpdate"          -- 最後更新日期時間 DATE  
          ,'999999'                       AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 
    FROM "ClBuildingUnique" CBU0
    LEFT JOIN "ClNoMapping" CNM ON CNM."GDRID1" = CBU0."GDRID1"
                               AND CNM."GDRID2" = CBU0."GDRID2"
                               AND CNM."GDRNUM" = CBU0."GDRNUM"
                               AND CNM."LGTSEQ" = CBU0."LGTSEQ"
    LEFT JOIN "ClBuildingUnique" CBU1 ON CBU1."GroupNo" = CBU0."GroupNo"
                                     AND CBU1."SecGroupNo" = CBU0."SecGroupNo"
    LEFT JOIN LA$HGTP HG ON HG."GDRID1" = CBU1."GDRID1"
                        AND HG."GDRID2" = CBU1."GDRID2"
                        AND HG."GDRNUM" = CBU1."GDRNUM"
                        AND HG."LGTSEQ" = CBU1."LGTSEQ"
    LEFT JOIN "CU$CUSP" CU ON CU."CUSCIF" = HG."LGTCIF"
    LEFT JOIN "CustMain" CM ON TRIM(CM."CustId") = TRIM(CU."CUSID1")
    WHERE CBU0."TfFg" = 'Y'
      AND NVL(CM."CustUKey",' ') != ' '
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
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_ClBuildingOwner_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
    DBMS_OUTPUT.PUT_LINE(dbms_utility.format_error_backtrace);

END;





/
