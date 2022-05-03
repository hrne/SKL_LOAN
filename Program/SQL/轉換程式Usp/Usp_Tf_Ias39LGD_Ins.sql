--------------------------------------------------------
--  DDL for Procedure Usp_Tf_Ias39LGD_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_Tf_Ias39LGD_Ins" 
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
    --  DDL for Procedure Usp_Tf_Ias39LGD_Ins
    --  St1 Chih Wei 2021.12.15
    --------------------------------------------------------
    -- 筆數預設0
    INS_CNT:=0;
    -- 記錄程式起始時間
    JOB_START_TIME := SYSTIMESTAMP;

    -- 刪除舊資料
    EXECUTE IMMEDIATE 'ALTER TABLE "Ias39LGD" DISABLE PRIMARY KEY CASCADE';
    EXECUTE IMMEDIATE 'TRUNCATE TABLE "Ias39LGD" DROP STORAGE';
    EXECUTE IMMEDIATE 'ALTER TABLE "Ias39LGD" ENABLE PRIMARY KEY';

    -- 寫入資料
    INSERT INTO "Ias39LGD"
    SELECT LGDP.LGDADT                    AS "Date"                -- 生效日期 Decimald 8  
         , LGDP.LGDTYP                    AS "Type"                -- 類別           VARCHAR2 2  
         , LGDP.LGDDSC                    AS "TypeDesc"            -- 類別說明       NVARCHAR2 30  
         , LGDP.LGDPCT                    AS "LGDPercent"          -- 違約損失率％   Decimal 7 5 
         , LGDP.LGDUSE                    AS "Enable"              -- 啟用記號 VARCHAR2 1   Y:啟用 , N:未啟用
         , JOB_START_TIME                 AS "CreateDate"          -- 建檔日期時間 DATE  
         , '999999'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 
         , JOB_START_TIME                 AS "LastUpdate"          -- 最後更新日期時間 DATE  
         , '999999'                       AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 
    FROM DAT_LN$LGDP LGDP
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
END;

/
