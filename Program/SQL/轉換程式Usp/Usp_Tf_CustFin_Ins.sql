--------------------------------------------------------
--  DDL for Procedure Usp_Tf_CustFin_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_Tf_CustFin_Ins" 
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
    EXECUTE IMMEDIATE 'ALTER TABLE "CustFin" DISABLE PRIMARY KEY CASCADE';
    EXECUTE IMMEDIATE 'TRUNCATE TABLE "CustFin" DROP STORAGE';
    EXECUTE IMMEDIATE 'ALTER TABLE "CustFin" ENABLE PRIMARY KEY';

    -- 寫入資料
    INSERT INTO "CustFin"
    SELECT "CustMain"."CustUKey"          AS "CustUKey"            -- 客戶識別碼 VARCHAR2 32 
          ,"LA$CFSP"."CFSYER"             AS "DataYear"            -- 年度 DECIMAL 4 
          ,"LA$CFSP"."CFSAST"             AS "AssetTotal"          -- 資產總額 DECIMAL 16 2
          ,"LA$CFSP"."CFSCAH"             AS "Cash"                -- 現金/銀存 DECIMAL 16 2
          ,"LA$CFSP"."CFSSTI"             AS "ShortInv"            -- 短期投資 DECIMAL 16 2
          ,"LA$CFSP"."CFSARA"             AS "AR"                  -- 應收帳款票據 DECIMAL 16 2
          ,"LA$CFSP"."CFSINV"             AS "Inventory"           -- 存貨 DECIMAL 16 2
          ,"LA$CFSP"."CFSLTI"             AS "LongInv"             -- 長期投資 DECIMAL 16 2
          ,"LA$CFSP"."CFSFAA"             AS "FixedAsset"          -- 固定資產 DECIMAL 16 2
          ,"LA$CFSP"."CFSOAS"             AS "OtherAsset"          -- 其他資產 DECIMAL 16 2
          ,"LA$CFSP"."CFSLBA"             AS "LiabTotal"           -- 負債總額 DECIMAL 16 2
          ,"LA$CFSP"."CFSLFB"             AS "BankLoan"            -- 銀行借款 DECIMAL 16 2
          ,"LA$CFSP"."CFSVLI"             AS "OtherCurrLiab"       -- 其他流動負債 DECIMAL 16 2
          ,"LA$CFSP"."CFSLLI"             AS "LongLiab"            -- 長期負債 DECIMAL 16 2
          ,"LA$CFSP"."CFSOTL"             AS "OtherLiab"           -- 其他負債 DECIMAL 16 2
          ,"LA$CFSP"."CFSNPT"             AS "NetWorthTotal"       -- 淨值總額 DECIMAL 16 2
          ,"LA$CFSP"."CFSCAP"             AS "Capital"             -- 資本 DECIMAL 16 2
          ,"LA$CFSP"."CFSRIC"             AS "RetainEarning"       -- 公積保留盈餘 DECIMAL 16 2
          ,"LA$CFSP"."CFSOIC"             AS "OpIncome"            -- 營業收入 DECIMAL 16 2
          ,"LA$CFSP"."CFSOCS"             AS "OpCost"              -- 營業成本 DECIMAL 16 2
          ,"LA$CFSP"."CFSOGR"             AS "OpProfit"            -- 營業毛利 DECIMAL 16 2
          ,"LA$CFSP"."CFSEMS"             AS "OpExpense"           -- 管銷費用 DECIMAL 16 2
          ,"LA$CFSP"."CFSORV"             AS "OpRevenue"           -- 營業利益 DECIMAL 16 2
          ,"LA$CFSP"."CFSOOI"             AS "NopIncome"           -- 營業外收入 DECIMAL 16 2
          ,"LA$CFSP"."CFSFEX"             AS "FinExpense"          -- 財務支出 DECIMAL 16 2
          ,"LA$CFSP"."CFSOOE"             AS "NopExpense"          -- 其他營業外支 DECIMAL 16 2
          ,"LA$CFSP"."CFSPBT"             AS "NetIncome"           -- 稅後淨利 DECIMAL 16 2
          ,"LA$CFSP"."CFSACT"             AS "Accountant"          -- 簽證會計師 NVARCHAR2 14 0
          ,"LA$CFSP"."CFSADT"             AS "AccountDate"         -- 簽證日期 Decimald 8 0
          ,JOB_START_TIME                 AS "CreateDate"          -- 建檔日期時間 DATE 0 0
          ,'999999'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 0
          ,JOB_START_TIME                 AS "LastUpdate"          -- 最後更新日期時間 DATE 0 0
          ,'999999'                       AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 0
    FROM "LA$CFSP"
    LEFT JOIN "CU$CUSP" ON "CU$CUSP"."CUSCIF" = "LA$CFSP"."CUSCIF"
    LEFT JOIN "CustMain" ON TRIM("CustMain"."CustId") = TRIM("CU$CUSP"."CUSID1")
    WHERE NVL("CustMain"."CustUKey",' ') <> ' '
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
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_CustFin_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;



/
