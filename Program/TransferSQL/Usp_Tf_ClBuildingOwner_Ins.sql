--------------------------------------------------------
--  DDL for Procedure Usp_Tf_ClBuildingOwner_Ins
--------------------------------------------------------
set define off;

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
    INSERT INTO "ClBuildingOwner"
    SELECT S1."ClCode1"                   AS "ClCode1"             -- 擔保品代號1 DECIMAL 1 
          ,S1."ClCode2"                   AS "ClCode2"             -- 擔保品代號2 DECIMAL 2 
          ,S1."ClNo"                      AS "ClNo"                -- 擔保品編號 DECIMAL 7 
          ,S4."CustUKey"                  AS "OwnerCustUKey"       -- 所有權人客戶識別碼 VARCHAR2 32 
          ,''                             AS "OwnerRelCode"        -- 與授信戶關係 VARCHAR2 2
          -- 擔保品持分
          ,1                              AS "OwnerPart"           -- 持分比率(分子) DECIMAL 10
          ,1                              AS "OwnerTotal"          -- 持分比率(分母) DECIMAL 10
          ,JOB_START_TIME                 AS "CreateDate"          -- 建檔日期時間 DATE  
          ,'999999'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 
          ,JOB_START_TIME                 AS "LastUpdate"          -- 最後更新日期時間 DATE  
          ,'999999'                       AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 
    FROM "ClNoMapping" S1
    LEFT JOIN "LA$HGTP" S2 ON S2."GDRID1" = S1."GDRID1"
                          AND S2."GDRID2" = S1."GDRID2"
                          AND S2."GDRNUM" = S1."GDRNUM"
                          AND S2."LGTSEQ" = S1."LGTSEQ"
    LEFT JOIN "CU$CUSP" S3 ON S3."CUSCIF" = S2."LGTCIF"
    LEFT JOIN "CustMain" S4 ON TRIM(S4."CustId") = TRIM(S3."CUSID1")
    LEFT JOIN "ClBuilding" S5 ON S5."ClCode1" = S1."ClCode1"
                             AND S5."ClCode2" = S1."ClCode2"
                             AND S5."ClNo"    = S1."ClNo"
    WHERE S1."GDRID1" = '1' -- 只撈不動產
      AND NVL(S2."LGTCIF",0) > 0 -- 舊系統建物資料有"提供人識別碼"才寫入
      AND NVL(S3."CUSCIF",0) > 0 -- 舊系統顧客主檔有資料才寫入
      AND NVL(S4."CustId",' ') <> ' ' -- 新系統顧客主檔有資料才寫入
      AND NVL(S5."ClNo",0) > 0 -- 建物檔有資料才需寫入
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
