--------------------------------------------------------
--  DDL for Procedure Usp_Tf_CdBaseRate_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_Tf_CdBaseRate_Ins" 
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
    EXECUTE IMMEDIATE 'ALTER TABLE "CdBaseRate" DISABLE PRIMARY KEY CASCADE';
    EXECUTE IMMEDIATE 'TRUNCATE TABLE "CdBaseRate" DROP STORAGE';
    EXECUTE IMMEDIATE 'ALTER TABLE "CdBaseRate" ENABLE PRIMARY KEY';

    -- 寫入資料 (02:中華郵政二年期定儲機動利率)
    INSERT INTO "CdBaseRate"
    SELECT 'TWD'                          AS "CurrencyCode"        -- 幣別 VARCHAR2 3
          ,'02'                           AS "BaseRateCode"        -- 利率代碼 VARCHAR2 2
          ,"TB$POIP"."IN$ADT"             AS "EffectDate"          -- 生效日期 DECIMALD 8
          ,"TB$POIP"."IN$RAT"             AS "BaseRate"            -- 利率 DECIMAL 6 4
          ,''                             AS "Remark"              -- 備註 VARCHAR2 40
          ,1                              AS "EffectFlag"          -- 生效記號 DECIMAL 1 (0:建檔 1:已生效不可刪除)
          ,JOB_START_TIME                 AS "CreateDate"          -- 建檔日期時間 DATE  
          ,'999999'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 
          ,JOB_START_TIME                 AS "LastUpdate"          -- 最後更新日期時間 DATE  
          ,'999999'                       AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 
    FROM "TB$POIP"
    ;

    -- 記錄寫入筆數
    INS_CNT := INS_CNT + sql%rowcount;

    -- 寫入資料 (01:保單分紅利率)
    INSERT INTO "CdBaseRate"
    SELECT 'TWD'                          AS "CurrencyCode"        -- 幣別 VARCHAR2 3
          ,'01'                           AS "BaseRateCode"        -- 利率代碼 VARCHAR2 2
          ,"TB$IRTP"."IN$ADT"             AS "EffectDate"          -- 生效日期 DECIMALD 8
          ,"TB$IRTP"."IN$RAT"             AS "BaseRate"            -- 利率 DECIMAL 6 4
          ,''                             AS "Remark"              -- 備註 VARCHAR2 40
          ,1                              AS "EffectFlag"          -- 生效記號 DECIMAL 1 (0:建檔 1:已生效不可刪除)
          ,JOB_START_TIME                 AS "CreateDate"          -- 建檔日期時間 DATE  
          ,'999999'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 
          ,JOB_START_TIME                 AS "LastUpdate"          -- 最後更新日期時間 DATE  
          ,'999999'                       AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 
    FROM "TB$IRTP" 
    WHERE "IN$COD" = '30' -- 舊利率別代號30:新三十年房貸(84/12)
    ;

    -- 記錄寫入筆數
    INS_CNT := INS_CNT + sql%rowcount;

    -- 寫入資料 (03:台北金融業拆款定盤利率)
    INSERT INTO "CdBaseRate"
    SELECT 'TWD'                          AS "CurrencyCode"        -- 幣別 VARCHAR2 3
          ,'03'                           AS "BaseRateCode"        -- 利率代碼 VARCHAR2 2
          ,"TB$IRTP"."IN$ADT"             AS "EffectDate"          -- 生效日期 DECIMALD 8
          ,"TB$IRTP"."IN$RAT"             AS "BaseRate"            -- 利率 DECIMAL 6 4
          ,''                             AS "Remark"              -- 備註 VARCHAR2 40
          ,1                              AS "EffectFlag"          -- 生效記號 DECIMAL 1 (0:建檔 1:已生效不可刪除)
          ,JOB_START_TIME                 AS "CreateDate"          -- 建檔日期時間 DATE  
          ,'999999'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 
          ,JOB_START_TIME                 AS "LastUpdate"          -- 最後更新日期時間 DATE  
          ,'999999'                       AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 
    FROM "TB$IRTP" 
    WHERE "IN$COD" = 'TB' -- 舊利率別代號TB:TAIBOR指數
    ;

    -- 記錄寫入筆數
    INS_CNT := INS_CNT + sql%rowcount;

    -- 寫入資料 (99:自訂利率)
    INSERT INTO "CdBaseRate"
    SELECT 'TWD'                          AS "CurrencyCode"        -- 幣別 VARCHAR2 3
          ,'99'                           AS "BaseRateCode"        -- 利率代碼 VARCHAR2 2
          ,19110101                       AS "EffectDate"          -- 生效日期 DECIMALD 8
          ,0                              AS "BaseRate"            -- 利率 DECIMAL 6 4
          ,''                             AS "Remark"              -- 備註 VARCHAR2 40
          ,0                              AS "EffectFlag"          -- 生效記號 DECIMAL 1 (0:建檔 1:已生效不可刪除)
          ,JOB_START_TIME                 AS "CreateDate"          -- 建檔日期時間 DATE  
          ,'999999'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 
          ,JOB_START_TIME                 AS "LastUpdate"          -- 最後更新日期時間 DATE  
          ,'999999'                       AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 
    FROM DUAL
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
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_CdBaseRate_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;




/
