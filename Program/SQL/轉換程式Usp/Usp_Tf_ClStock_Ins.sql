CREATE OR REPLACE PROCEDURE "Usp_Tf_ClStock_Ins" 
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
    EXECUTE IMMEDIATE 'ALTER TABLE "ClStock" DISABLE PRIMARY KEY CASCADE';
    EXECUTE IMMEDIATE 'TRUNCATE TABLE "ClStock" DROP STORAGE';
    EXECUTE IMMEDIATE 'ALTER TABLE "ClStock" ENABLE PRIMARY KEY';

    -- 寫入資料
    INSERT INTO "ClStock" (
        "ClCode1"             -- 擔保品代號1 DECIMAL 1 
      , "ClCode2"             -- 擔保品代號2 DECIMAL 2 
      , "ClNo"                -- 擔保品號碼 DECIMAL 7 
      , "StockCode"           -- 股票代號 VARCHAR2 10 
      , "ListingType"         -- 掛牌別 VARCHAR2 2 
      , "StockType"           -- 股票種類 VARCHAR2 1 
      , "CompanyId"           -- 發行公司統一編號 VARCHAR2 10 
      , "DataYear"            -- 資料年度 DECIMAL 4 
      , "IssuedShares"        -- 發行股數 DECIMAL 16 2
      , "NetWorth"            -- 非上市(櫃)每股淨值 DECIMAL 16 2
      , "EvaStandard"         -- 每股單價鑑估標準 VARCHAR2 12 
      , "ParValue"            -- 每股面額 DECIMAL 16 2
      , "MonthlyAvg"          -- 一個月平均價 DECIMAL 16 2
      , "YdClosingPrice"      -- 前日收盤價 DECIMAL 16 2
      , "ThreeMonthAvg"       -- 三個月平均價 DECIMAL 16 2
      , "EvaUnitPrice"        -- 鑑定單價 DECIMAL 16 2
      , "OwnerCustUKey"       -- 所有權人客戶識別碼 VARCHAR2 32 
      , "InsiderJobTitle"     -- 公司內部人職稱 VARCHAR2 2 
      , "InsiderPosition"     -- 公司內部人身分註記 VARCHAR2 2 
      , "LegalPersonId"       -- 法定關係人統編 VARCHAR2 10 
      , "LoanToValue"         -- 貸放成數(%) DECIMAL 5 2
      , "ClMtr"               -- 擔保維持率(%) DECIMAL 5 2
      , "NoticeMtr"           -- 通知追繳維持率(%) DECIMAL 5 2
      , "ImplementMtr"        -- 實行職權維持率(%) DECIMAL 5 2
      , "AcMtr"               -- 全戶維持率(%) DECIMAL 5 2
      , "PledgeNo"            -- 質權設定書號 VARCHAR2 14 
      , "ComputeMTR"          -- 計算維持率 VARCHAR2 1 
      , "SettingStat"	       -- 設定狀態 VARCHAR2(1 BYTE)
      , "ClStat"	             -- 擔保品狀態 VARCHAR2(1 BYTE)
      , "SettingDate"         -- 股票設解(質)日期 decimald 8 
      , "SettingBalance"      -- 設質股數餘額 DECIMAL 16 2
      , "MtgDate"             -- 擔保債權確定日期 decimald 8 
      , "CustodyNo"           -- 保管條號碼 VARCHAR2 5 
      , "CreateDate"          -- 建檔日期時間 DATE  
      , "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 
      , "LastUpdate"          -- 最後更新日期時間 DATE  
      , "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 
    )
    SELECT S1."ClCode1"                   AS "ClCode1"             -- 擔保品代號1 DECIMAL 1 
          ,S1."ClCode2"                   AS "ClCode2"             -- 擔保品代號2 DECIMAL 2 
          ,S1."ClNo"                      AS "ClNo"                -- 擔保品號碼 DECIMAL 7 
          ,S1."StockCode"                 AS "StockCode"           -- 股票代號 VARCHAR2 10 
          ,''                             AS "ListingType"         -- 掛牌別 VARCHAR2 2 
          ,''                             AS "StockType"           -- 股票種類 VARCHAR2 1 
          ,S1."CompanyId"                 AS "CompanyId"           -- 發行公司統一編號 VARCHAR2 10 
          ,0                              AS "DataYear"            -- 資料年度 DECIMAL 4 
          ,0                              AS "IssuedShares"        -- 發行股數 DECIMAL 16 2
          ,0                              AS "NetWorth"            -- 非上市(櫃)每股淨值 DECIMAL 16 2
          ,''                             AS "EvaStandard"         -- 每股單價鑑估標準 VARCHAR2 12 
          ,S1."ParValue"                  AS "ParValue"            -- 每股面額 DECIMAL 16 2
          ,0                              AS "MonthlyAvg"          -- 一個月平均價 DECIMAL 16 2
          ,S1."YdClosingPrice"            AS "YdClosingPrice"      -- 前日收盤價 DECIMAL 16 2
          ,S1."ThreeMonthAvg"             AS "ThreeMonthAvg"       -- 三個月平均價 DECIMAL 16 2
          ,S1."EvaUnitPrice"              AS "EvaUnitPrice"        -- 鑑定單價 DECIMAL 16 2
          ,CM."CustUKey"                  AS "OwnerCustUKey"       -- 所有權人客戶識別碼 VARCHAR2 32 
          ,''                             AS "InsiderJobTitle"     -- 公司內部人職稱 VARCHAR2 2 
          ,''                             AS "InsiderPosition"     -- 公司內部人身分註記 VARCHAR2 2 
          ,S1."LegalPersonId"             AS "LegalPersonId"       -- 法定關係人統編 VARCHAR2 10 
          ,S1."LoanToValue"               AS "LoanToValue"         -- 貸放成數(%) DECIMAL 5 2
          -- 下列「擔保維持率」、「通知追繳維持率」、「實行質權維持率」與批示條件不同時請自行輸入。
          -- 50:％　擔保維持率：170％　通知追繳最低維持率：160％　實行質權維持率：140％
          -- 60:％　擔保維持率：140％　通知追繳最低維持率：135％　實行質權維持率：125％
          -- 70:％　擔保維持率：125％　通知追繳最低維持率：120％　實行質權維持率：115％
          -- 80:％　擔保維持率：110％　通知追繳最低維持率：105％　實行質權維持率：100％
          ,CASE
             WHEN S1."LoanToValue" <= 50
             THEN 170
             WHEN S1."LoanToValue" <= 60
             THEN 140
             WHEN S1."LoanToValue" <= 70
             THEN 125
             WHEN S1."LoanToValue" <= 80
             THEN 110
           ELSE 110 END                   AS "ClMtr"               -- 擔保維持率(%) DECIMAL 5 2
          ,CASE
             WHEN S1."LoanToValue" <= 50
             THEN 160
             WHEN S1."LoanToValue" <= 60
             THEN 135
             WHEN S1."LoanToValue" <= 70
             THEN 120
             WHEN S1."LoanToValue" <= 80
             THEN 105
           ELSE 105 END                   AS "NoticeMtr"           -- 通知追繳維持率(%) DECIMAL 5 2
          ,CASE
             WHEN S1."LoanToValue" <= 50
             THEN 140
             WHEN S1."LoanToValue" <= 60
             THEN 125
             WHEN S1."LoanToValue" <= 70
             THEN 115
             WHEN S1."LoanToValue" <= 80
             THEN 100
           ELSE 100 END                   AS "ImplementMtr"        -- 實行職權維持率(%) DECIMAL 5 2
          ,0                              AS "AcMtr"               -- 全戶維持率(%) DECIMAL 5 2
          ,S1."PledgeNo"                  AS "PledgeNo"            -- 質權設定書號 VARCHAR2 14 
          ,''                             AS "ComputeMTR"          -- 計算維持率 VARCHAR2 1 
          ,'1'                            AS "SettingStat"	       -- 設定狀態 VARCHAR2(1 BYTE)
          ,'0'                            AS "ClStat"	             -- 擔保品狀態 VARCHAR2(1 BYTE)
          ,0                              AS "SettingDate"         -- 股票設解(質)日期 decimald 8 
          ,S1."SettingBalance"            AS "SettingBalance"      -- 設質股數餘額 DECIMAL 16 2
          ,0                              AS "MtgDate"             -- 擔保債權確定日期 decimald 8 
          ,S1."CustodyNo"                 AS "CustodyNo"           -- 保管條號碼 VARCHAR2 5 
          ,JOB_START_TIME                 AS "CreateDate"          -- 建檔日期時間 DATE  
          ,'999999'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 
          ,JOB_START_TIME                 AS "LastUpdate"          -- 最後更新日期時間 DATE  
          ,'999999'                       AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 
    FROM (
      SELECT S1."ClCode1"                   AS "ClCode1"             -- 擔保品代號1 DECIMAL 1 
            ,S1."ClCode2"                   AS "ClCode2"             -- 擔保品代號2 DECIMAL 2 
            ,S1."ClNo"                      AS "ClNo"                -- 擔保品號碼 DECIMAL 7 
            ,MAX(NVL(SPRP."STKPID",' '))    AS "CompanyId"           -- 發行公司統一編號 VARCHAR2 10 
            ,MAX(TCS."NewStockNo")
                                            AS "StockCode"           -- 股票代號 VARCHAR2 4 
            ,SUM(NVL(S2."SGTAUN",0))        AS "ParValue"            -- 每股面額 DECIMAL 16 2
            ,MAX(NVL(S2."SGTYAM",0))        AS "YdClosingPrice"      -- 前日收盤價 DECIMAL 16 2
            ,MAX(NVL(S2."SGT3MA",0))        AS "ThreeMonthAvg"       -- 三個月平均價 DECIMAL 16 2
            ,MAX(NVL(S2."SGTUNT",0))        AS "EvaUnitPrice"        -- 鑑定單價 DECIMAL 16 2
            ,MAX(NVL(CU."CUSID1",' '))      AS "OwnerId"             -- 股票持有人統編 VARCHAR2 10 
            ,MAX(NVL(S2."GDRMRK",' '))      AS "LegalPersonId"       -- 法定關係人統編 VARCHAR2 10 
            ,MAX(NVL(S2."SGTPER",0))        AS "LoanToValue"         -- 貸放成數(%) DECIMAL 5 2
            ,MAX(S2."SGTSBN")               AS "PledgeNo"            -- 質權設定書號 VARCHAR2 14 
            ,SUM(NVL(S3."SGDQTY",0))        AS "SettingBalance"      -- 設質股數餘額 DECIMAL 16 2
            ,MAX(S2."SGTMNN")               AS "CustodyNo"           -- 保管條號碼 VARCHAR2 5 
      FROM "ClNoMap" S1
      LEFT JOIN "LA$SGTP" S2 ON S2."GDRID1" = S1."GdrId1"
                            AND S2."GDRID2" = S1."GdrId2"
                            AND S2."GDRNUM" = S1."GdrNum"
      LEFT JOIN "LA$SGDP" S3 ON S3."GDRID1" = S1."GdrId1"
                            AND S3."GDRID2" = S1."GdrId2"
                            AND S3."GDRNUM" = S1."GdrNum"
      LEFT JOIN "CU$CUSP" CU ON CU."CUSCIF" = S3."LGTCIF"
                            AND S3."LGTCIF" > 0
      LEFT JOIN DAT_LN$SPRP SPRP ON SPRP."SGTNO1" = S2."SGTNO1"
                                AND SPRP."SGTNO2" = S2."SGTNO2"
      LEFT JOIN "TempCdStockMapping" TCS ON TCS."STKNO1" = S2."SGTNO1"
                                        AND TCS."STKNO2" = S2."SGTNO2"
      WHERE S1."ClCode1" >= 3
        AND S1."ClCode1" <= 4
        AND S1."TfStatus" IN (1,3)
      GROUP BY S1."ClCode1"
              ,S1."ClCode2"
              ,S1."ClNo"
    ) S1
    LEFT JOIN "CustMain" CM ON TRIM(CM."CustId") = TRIM(S1."OwnerId")
    ;

    -- 記錄寫入筆數
    INS_CNT := INS_CNT + sql%rowcount;

    MERGE INTO "ClMain" T
    USING (
      SELECT CLS."ClCode1"
           , CLS."ClCode2"
           , CLS."ClNo"
           , CDS."StockType"
      FROM "ClStock" CLS
      LEFT JOIN "CdStock" CDS ON CDS."StockCode" = CLS."StockCode"
      WHERE NVL(CDS."StockType",0) != 0
    ) S
    ON (
      S."ClCode1" = T."ClCode1"
      AND S."ClCode2" = T."ClCode2"
      AND S."ClNo" = T."ClNo"
    )
    WHEN MATCHED THEN UPDATE SET
    T."ClTypeCode" = CASE
                       WHEN S."StockType" = 1 THEN '141' -- 上市公司股票
                       WHEN S."StockType" = 2 THEN '142' -- 上櫃公司股票
                     ELSE '145' END
    ;

    -- 記錄程式結束時間
    JOB_END_TIME := SYSTIMESTAMP;

    commit;

    -- 例外處理
    Exception
    WHEN OTHERS THEN
    ERROR_MSG := SQLERRM || CHR(13) || CHR(10) || dbms_utility.format_error_backtrace;
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_ClStock_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;





/
