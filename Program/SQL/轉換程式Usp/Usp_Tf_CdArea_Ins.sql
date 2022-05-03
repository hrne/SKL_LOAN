--------------------------------------------------------
--  DDL for Procedure Usp_Tf_CdArea_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_Tf_CdArea_Ins" 
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

    MERGE INTO "CdArea" T
    USING (
        SELECT S1."CityCode"                  AS "CityCode"            -- 縣市別代碼 VARCHAR2 2 
              ,LPAD(S2."TWNJIC",2,'0')        AS "AreaCode"            -- 鄉鎮區代碼 VARCHAR2 2 
              ,TRIM(S2."TWNNAM")              AS "AreaItem"            -- 鄉鎮區名稱 NVARCHAR2 12 
              ,TRIM(S2."CTYABB")              AS "CityShort"           -- 縣市簡稱 NVARCHAR2 6 
              ,TRIM(S2."TWNABB")              AS "AreaShort"           -- 鄉鎮簡稱 NVARCHAR2 8 
              ,S2."LOCTYE"                    AS "CityType"            -- 地區類別 VARCHAR2 2 
              ,S2."POSCOD"                    AS "Zip3"                -- 郵遞區號 VARCHAR2 3 
              ,S2."BCMDPT"                    AS "DepartCode"          -- 部室代號 VARCHAR2 6 
              ,S2."LOCGRP"                    AS "CityGroup"           -- 組合地區別 VARCHAR2 1 
              ,S2."CTYJIC"                    AS "JcicCityCode"
              ,LPAD(S2."TWNJIC",2,'0')        AS "JcicAreaCode"
              ,ROW_NUMBER () OVER (PARTITION BY S1."CityCode",LPAD(S2."TWNJIC",2,'0') 
                                   ORDER BY S1."CityCode",LPAD(S2."TWNJIC",2,'0')) AS "SEQ"
        FROM "CdCity" S1
        LEFT JOIN "LN$CTYP" S2 ON S2."LOCCTY" = S1."CityCode"
        WHERE S2.LOCCTY is not null
        ) S ON (    T."CityCode" = S."CityCode"
                AND T."AreaCode" = S."AreaCode"
                AND S."SEQ" = 1)
    WHEN MATCHED THEN UPDATE SET 
         T."CityShort"  = S."CityShort"
        ,T."AreaShort"  = S."AreaShort"
        ,T."CityType"   = S."CityType"
        ,T."DepartCode" = S."DepartCode"
        ,T."CityGroup"  = S."CityGroup"
        ,T."JcicCityCode" = S."JcicCityCode"
        ,T."JcicAreaCode" = S."JcicAreaCode"
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
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_CdArea_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;

/
