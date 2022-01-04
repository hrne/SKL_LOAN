--------------------------------------------------------
--  DDL for Procedure Usp_Tf_ClOther_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "Usp_Tf_ClOther_Ins" 
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
    EXECUTE IMMEDIATE 'ALTER TABLE "ClOther" DISABLE PRIMARY KEY CASCADE';
    EXECUTE IMMEDIATE 'TRUNCATE TABLE "ClOther" DROP STORAGE';
    EXECUTE IMMEDIATE 'ALTER TABLE "ClOther" ENABLE PRIMARY KEY';

    -- 寫入資料
    INSERT INTO "ClOther"
    SELECT S1."ClCode1"                   AS "ClCode1"             -- 擔保品代號1 DECIMAL 1 
          ,S1."ClCode2"                   AS "ClCode2"             -- 擔保品代號2 DECIMAL 2 
          ,S1."ClNo"                      AS "ClNo"                -- 擔保品編號 DECIMAL 7 
          ,S2."BGTSDT"                    AS "PledgeStartDate"     -- 保證起日 decimald 8 
          ,S2."BGTEDT"                    AS "PledgeEndDate"       -- 保證迄日 decimald 8 
          ,CASE
             WHEN LENGTH(S2."BGTBNK") <= 2 THEN LPAD(TO_CHAR(S2."BGTBNK"),2,'0')
           ELSE 'XX' END                  AS "PledgeBankCode"      -- 保證銀行 VARCHAR2 2 
          ,TRIM(S2."BGTGBN")              AS "PledgeNO"            -- 保證書字號 VARCHAR2 30 
          ,''                             AS "OwnerCustUKey"       -- 所有權人客戶識別碼 VARCHAR2 32 
          ,''                             AS "IssuingId"           -- 發行機構統編 VARCHAR2 10 
          ,''                             AS "IssuingCounty"       -- 發行機構所在國別 VARCHAR2 3 
          ,''                             AS "DocNo"               -- 憑證編號 VARCHAR2 30 
          ,0                              AS "LoanToValue"	       -- 貸放成數 DECIMAL 5 2
          ,''                             AS "SecuritiesType"      -- 有價證券類別 varchar2 2 
          ,''                             AS "Listed"              -- 掛牌交易所 varchar2 2
          ,0                              AS "OfferingDate"        -- 發行日 decimald 8
          ,0                              AS "ExpirationDate"      -- 到期日 decimald 8
          ,''                             AS "TargetIssuer"        -- 發行者對象別 varchar2 2
          ,''                             AS "SubTargetIssuer"     -- 發行者次對象別 varchar2 2
          ,0                              AS "CreditDate"          -- 評等日期 decimald 8
          ,''                             AS "Credit"              -- 評等公司 varchar2 2
          ,''                             AS "ExternalCredit"      -- 外部評等 varchar2 3
          ,''                             AS "Index"               -- 主要指數 varchar2 2
          ,''                             AS "TradingMethod"       -- 交易方法 varchar2 1
          ,''                             AS "Compensation"        -- 受償順位 varchar2 3
          ,''                             AS "Investment"          -- 投資內容 nvarchar2 300
          ,''                             AS "PublicValue"         -- 公開價值 nvarchar2 300
          ,'1'                            AS "SettingStat"	       -- 設定狀態 VARCHAR2(1 BYTE)
          ,'0'                            AS "ClStat"	           -- 擔保品狀態 VARCHAR2(1 BYTE)
          ,0                              AS "SettingDate"	       -- 設定日期 NUMBER(8,0)
          ,S2."BGTAMT"                    AS "SettingAmt"	       -- 設定金額 NUMBER(16,2)
          ,JOB_START_TIME                 AS "CreateDate"          -- 建檔日期時間 DATE  
          ,'999999'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 
          ,JOB_START_TIME                 AS "LastUpdate"          -- 最後更新日期時間 DATE  
          ,'999999'                       AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 
    FROM "ClNoMapping" S1
    LEFT JOIN "LA$BGTP" S2 ON S2."GDRID1" = S1."GDRID1"
                          AND S2."GDRID2" = S1."GDRID2"
                          AND S2."GDRNUM" = S1."GDRNUM"
    WHERE S1."ClCode1" = 5
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
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_ClOther_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;




/
