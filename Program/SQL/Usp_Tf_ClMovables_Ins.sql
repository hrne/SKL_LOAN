CREATE OR REPLACE PROCEDURE "Usp_Tf_ClMovables_Ins" 
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
    EXECUTE IMMEDIATE 'ALTER TABLE "ClMovables" DISABLE PRIMARY KEY CASCADE';
    EXECUTE IMMEDIATE 'TRUNCATE TABLE "ClMovables" DROP STORAGE';
    EXECUTE IMMEDIATE 'ALTER TABLE "ClMovables" ENABLE PRIMARY KEY';

    -- 寫入資料
    INSERT INTO "ClMovables"
    SELECT S1."ClCode1"                   AS "ClCode1"             -- 擔保品代號1 DECIMAL 1 
          ,S1."ClCode2"                   AS "ClCode2"             -- 擔保品代號2 DECIMAL 2 
          ,S1."ClNo"                      AS "ClNo"                -- 擔保品編號 DECIMAL 7 
          ,CM."CustUKey"                  AS "OwnerCustUKey"       -- 所有權人客戶識別碼 VARCHAR2 32 
          ,0                              AS "ServiceLife"         -- 耐用年限 DECIMAL 2 
          ,S2."CGT004"                    AS "ProductSpec"         -- 形式/規格 VARCHAR2 20 
          ,S2."CGT001"                    AS "ProductType"         -- 產品代碼/型號 VARCHAR2 10 
          ,S2."CGT002"                    AS "ProductBrand"        -- 品牌/廠牌/船名 VARCHAR2 20 
          ,TRIM(S2."CGT005")              AS "ProductCC"           -- 排氣量 VARCHAR2 10 
          ,S2."CGT003"                    AS "ProductColor"        -- 顏色 VARCHAR2 10 
          ,S2."CGT012"                    AS "EngineSN"            -- 引擎號碼 VARCHAR2 50 
          ,S2."CGT006"                    AS "LicenseNo"           -- 牌照號碼 VARCHAR2 10 
          ,S2."CGT007"                    AS "LicenseTypeCode"     -- 牌照類別 VARCHAR2 1 
          ,S2."CGT008"                    AS "LicenseUsageCode"    -- 牌照用途 VARCHAR2 1 
          ,S2."CGT009"                    AS "LiceneIssueDate"     -- 發照日期 decimald 8 
          ,S2."CGT010"                    AS "MfgYearMonth"        -- 製造年月 DECIMAL 6 
          ,S2."CGT011"                    AS "VehicleTypeCode"     -- 車別 VARCHAR2 2 
          ,LPAD(S2."CGT013",2,'0')        AS "VehicleStyleCode"    -- 車身樣式 VARCHAR2 2 
          ,S2."CGT017"                    AS "VehicleOfficeCode"   -- 監理站 VARCHAR2 3 
          ,CASE
             WHEN TRIM(S2."CGT019") = 'TW' THEN '01'
           ELSE '' END                    AS "Currency"            -- 幣別 VARCHAR2 2 
          ,0                              AS "ExchangeRate"        -- 匯率 DECIMAL 8 5
          ,''                             AS "Insurance"           -- 投保註記 VARCHAR2 1 
          ,0                              AS "LoanToValue"         -- 貸放成數(%) DECIMAL 5 2
          ,0                              AS "ScrapValue"          -- 殘值 DECIMAL 16 2
          ,''                             AS "MtgCode"             -- 抵押權註記 VARCHAR2 1 
          ,''                             AS "MtgCheck"            -- 最高限額抵押權之擔保債權種類-票據 VARCHAR2 1 
          ,''                             AS "MtgLoan"             -- 最高限額抵押權之擔保債權種類-借款 VARCHAR2 1 
          ,''                             AS "MtgPledge"           -- 最高限額抵押權之擔保債權種類-保證債務 VARCHAR2 1 
          ,'1'                            AS "SettingStat"	       -- 設定狀態 VARCHAR2(1 BYTE)
          ,'0'                            AS "ClStat"	             -- 擔保品狀態 VARCHAR2(1 BYTE)
          ,0                              AS "SettingDate"	       -- 設定日期 NUMBER(8,0)
          ,S2."CGT018"                    AS "SettingAmt"          -- 抵押設定金額 DECIMAL 16 2
          ,S2."CGT020"                    AS "ReceiptNo"           -- 收件字號 VARCHAR2 20 
          ,S2."CGT021"                    AS "MtgNo"               -- 抵押登記字號 VARCHAR2 20 
          ,S2."CGT022"                    AS "ReceivedDate"        -- 抵押收件日 decimald 8 
          ,S2."CGT023"                    AS "MortgageIssueStartDate" -- 抵押登記起日 decimald 8 
          ,S2."CGT024"                    AS "MortgageIssueEndDate" -- 抵押登記迄日 decimald 8 
          ,S2."NGRRMK60"                  AS "Remark"              -- 備註 NVARCHAR2 120 
          ,JOB_START_TIME                 AS "CreateDate"          -- 建檔日期時間 DATE 0 
          ,'999999'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 
          ,JOB_START_TIME                 AS "LastUpdate"          -- 最後更新日期時間 DATE 0 
          ,'999999'                       AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 
    FROM "ClNoMapping" S1
    LEFT JOIN "LN$CGTP" S2 ON S2."GDRID1" = S1."GDRID1"
                          AND S2."GDRID2" = S1."GDRID2"
                          AND S2."GDRNUM" = S1."GDRNUM"
    LEFT JOIN "CustMain" CM ON TRIM(CM."CustId") = SUBSTR(TRIM(S2."CGT015"),0,10)
    WHERE S1."ClCode1" = 9
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
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_ClMovables_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;

/
