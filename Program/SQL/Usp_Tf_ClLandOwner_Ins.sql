CREATE OR REPLACE PROCEDURE "Usp_Tf_ClLandOwner_Ins" 
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
    EXECUTE IMMEDIATE 'ALTER TABLE "ClLandOwner" DISABLE PRIMARY KEY CASCADE';
    EXECUTE IMMEDIATE 'TRUNCATE TABLE "ClLandOwner" DROP STORAGE';
    EXECUTE IMMEDIATE 'ALTER TABLE "ClLandOwner" ENABLE PRIMARY KEY';

    -- 寫入資料
    INSERT INTO "ClLandOwner"
    SELECT LG."ClCode1"                   AS "ClCode1"             -- 擔保品代號1 DECIMAL 1 
          ,LG."ClCode2"                   AS "ClCode2"             -- 擔保品代號2 DECIMAL 2 
          ,LG."ClNo"                      AS "ClNo"                -- 擔保品編號 DECIMAL 7 
          ,LG."LandSeq"                   AS "LandSeq"             -- 土地序號 DECIMAL 3
          ,CM."CustUKey"                  AS "OwnerCustUKey"       -- 所有權人客戶識別碼 VARCHAR2 32 
          ,''                             AS "OwnerRelCode"        -- 與授信戶關係 VARCHAR2 2
          ,1                              AS "OwnerPart"           -- 持分比率(分子) DECIMAL 10 
          ,1                              AS "OwnerTotal"          -- 持分比率(分母) DECIMAL 10 
          ,JOB_START_TIME                 AS "CreateDate"          -- 建檔日期時間 DATE  
          ,'999999'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 
          ,JOB_START_TIME                 AS "LastUpdate"          -- 最後更新日期時間 DATE  
          ,'999999'                       AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 
    FROM (SELECT S0."ClCode1"
                ,S0."ClCode2"
                ,S0."ClNo"
                ,S0."LandSeq"
                ,NVL(S2."LGTCIF",0) AS "LGTCIF"
          FROM "ClLand" S0
          LEFT JOIN "ClNoMapping" S1 ON S1."ClCode1" = S0."ClCode1"
                                    AND S1."ClCode2" = S0."ClCode2"
                                    AND S1."ClNo"    = S0."ClNo"
          LEFT JOIN "LA$LGTP" S2 ON S2."GDRID1" = S1."GDRID1"
                                AND S2."GDRID2" = S1."GDRID2"
                                AND S2."GDRNUM" = S1."GDRNUM"
                                AND LPAD(REPLACE(TRIM(S2."LGTNM1"),'-',''),4,'0') = S0."LandNo1"
                                AND LPAD(REPLACE(TRIM(S2."LGTNM2"),'-',''),4,'0') = S0."LandNo2"
          WHERE NVL(S2."LGTCIF",0) > 0
          GROUP BY  S0."ClCode1"
                   ,S0."ClCode2"
                   ,S0."ClNo"
                   ,S0."LandSeq"
                   ,NVL(S2."LGTCIF",0)
         ) LG
    LEFT JOIN "CU$CUSP" CU ON CU."CUSCIF" = LG."LGTCIF"
    LEFT JOIN "CustMain" CM ON TRIM(CM."CustId") = TRIM(CU."CUSID1")
    WHERE NVL(CU."CUSCIF",0) > 0
      AND NVL(CM."CustId",' ') <> ' ' 
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
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_ClLandOwner_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
    DBMS_OUTPUT.PUT_LINE(dbms_utility.format_error_backtrace);

END;




/
