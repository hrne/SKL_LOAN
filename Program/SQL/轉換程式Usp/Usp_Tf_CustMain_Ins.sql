--------------------------------------------------------
--  DDL for Procedure Usp_Tf_CustMain_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_Tf_CustMain_Ins" 
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

    -- 固定CustUKey
    MERGE INTO "TfCust" T
    USING (
      SELECT CASE
               WHEN CUSP."LMSACN" = 601776 -- 2021-12-10 智偉修改 from Linda
               THEN 'A111111131'
             ELSE REPLACE(TRIM(CUSP."CUSID1"),CHR(26),'')
             END             AS "CustId"
      FROM "CU$CUSP" CUSP
    ) S
    ON (
      S."CustId" = T."CustId"
    )
    WHEN NOT MATCHED THEN 
    INSERT (
        "CustId"
      , "CustUKey"
    ) VALUES (
        S."CustId"
      , SYS_GUID()
    )
    ;

    -- 刪除舊資料
    EXECUTE IMMEDIATE 'ALTER TABLE "CustMain" DISABLE PRIMARY KEY CASCADE';
    EXECUTE IMMEDIATE 'TRUNCATE TABLE "CustMain" DROP STORAGE';
    EXECUTE IMMEDIATE 'ALTER TABLE "CustMain" ENABLE PRIMARY KEY';

    -- 寫入資料
    INSERT INTO "CustMain" (
            "CustUKey"            -- 客戶識別碼 VARCHAR2 32 
          , "CustId"              -- 身份證字號/統一編號 VARCHAR2 10 
          , "CustNo"              -- 戶號 DECIMAL 7 
          , "BranchNo"            -- 單位別 VARCHAR2 4 
          , "CustName"            -- 戶名/公司名稱 NVARCHAR2 100 
          , "Birthday"            -- 出生年月日/設立日期 decimald 8 
          , "Sex"                 -- 性別 VARCHAR2 1 
          , "CustTypeCode"        -- 客戶別 VARCHAR2 2 
          , "IndustryCode"        -- 行業別 VARCHAR2 6 -- 2021-07-22修改: 位數不足6碼者，前補零
          , "NationalityCode"     -- 自然人:出生地國籍 / 法人:註冊地國籍 VARCHAR2 2 
          , "BussNationalityCode" -- 自然人:居住地國籍 / 法人:營業地國籍 VARCHAR2 2 
          , "SpouseId"            -- 配偶身份證號/負責人身分證 VARCHAR2 10 
          , "SpouseName"          -- 配偶姓名/負責人姓名 NVARCHAR2 100 
          , "RegZip3"             -- 戶籍-郵遞區號前三碼 VARCHAR2 3 
          , "RegZip2"             -- 戶籍-郵遞區號後兩碼 VARCHAR2 2 
          , "RegCityCode"         -- 戶籍-縣市代碼 VARCHAR2 2 
          , "RegAreaCode"         -- 戶籍-鄉鎮市區代碼 VARCHAR2 3 
          , "RegRoad"             -- 戶籍-路名 NVARCHAR2 40 
          , "RegSection"          -- 戶籍-段 VARCHAR2 5 
          , "RegAlley"            -- 戶籍-巷 VARCHAR2 5 
          , "RegLane"             -- 戶籍-弄 VARCHAR2 5 
          , "RegNum"              -- 戶籍-號 VARCHAR2 5 
          , "RegNumDash"          -- 戶籍-號之 VARCHAR2 5 
          , "RegFloor"            -- 戶籍-樓 VARCHAR2 5 
          , "RegFloorDash"        -- 戶籍-樓之 VARCHAR2 5 
          , "CurrZip3"            -- 通訊-郵遞區號前三碼 VARCHAR2 3 
          , "CurrZip2"            -- 通訊-郵遞區號後兩碼 VARCHAR2 2 
          , "CurrCityCode"        -- 通訊-縣市代碼 VARCHAR2 2 
          , "CurrAreaCode"        -- 通訊-鄉鎮市區代碼 VARCHAR2 3 
          , "CurrRoad"            -- 通訊-路名 NVARCHAR2 40 
          , "CurrSection"         -- 通訊-段 VARCHAR2 5 
          , "CurrAlley"           -- 通訊-巷 VARCHAR2 5 
          , "CurrLane"            -- 通訊-弄 VARCHAR2 5 
          , "CurrNum"             -- 通訊-號 VARCHAR2 5 
          , "CurrNumDash"         -- 通訊-號之 VARCHAR2 5 
          , "CurrFloor"           -- 通訊-樓 VARCHAR2 5 
          , "CurrFloorDash"       -- 通訊-樓之 VARCHAR2 5 
          , "CuscCd"              -- 身份別 VARCHAR2 1 
          , "EntCode"             -- 企金別 VARCHAR2 1 
          , "EmpNo"               -- 員工代號 VARCHAR2 6 
          , "EName"               -- 英文姓名 VARCHAR2 20 
          , "EduCode"             -- 教育程度代號 VARCHAR2 1 
          , "OwnedHome"           -- 自有住宅有無 VARCHAR2 1 
          , "CurrCompName"        -- 任職機構名稱 NVARCHAR2 60 
          , "CurrCompId"          -- 任職機構統編 VARCHAR2 8 
          , "CurrCompTel"         -- 任職機構電話 VARCHAR2 16 
          , "JobTitle"            -- 職位名稱 NVARCHAR2 20 
          , "JobTenure"           -- 服務年資 VARCHAR2 2 
          , "IncomeOfYearly"      -- 年收入 DECIMAL 9 
          , "IncomeDataDate"      -- 年收入資料年月 VARCHAR2 6 
          , "PassportNo"          -- 護照號碼 VARCHAR2 20 
          , "AMLJobCode"          -- AML職業別 VARCHAR2 3 
          , "AMLGroup"            -- AML組織 VARCHAR2 3 
          , "IndigenousName"      -- 原住民姓名 NVARCHAR2 100 
          , "LastFacmNo"          -- 已編額度編號 DECIMAL 3 
          , "LastSyndNo"          -- 已編聯貸案序號 DECIMAL  
          , "AllowInquire"        -- 開放查詢 VARCHAR2 1 
          , "Email"               -- Email Address VARCHAR2 50 
          , "ActFg"               -- 交易進行記號 DECIMAL 1 
          , "Introducer"          -- 介紹人 VARCHAR2 6 
          , "BusinessOfficer"     -- 房貸專員/企金人員 VARCHAR2 6
          , "IsSuspected"         -- 是否為金控「疑似準利害關係人」名單 VARCHAR2 1 
          , "IsSuspectedCheck"    -- 是否為金控疑似準利害關係人 VARCHAR2 1 
          , "IsSuspectedCheckType"-- 是否為金控疑似準利害關係人_確認狀態 VARCHAR2 1 
          , "DataStatus"          -- 資料狀態 DECIMAL 1 
          , "TypeCode"            -- 建檔身分別 DECIMAL 1 
          , "Station"              -- 站別 VARCHAR2 3
          , "CreateDate"          -- 建檔日期時間 DATE  
          , "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 
          , "LastUpdate"          -- 最後更新日期時間 DATE  
          , "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 
          , "IsLimit"             -- 是否為授信限制對象
          , "IsRelated"           -- 是否為利害關係人
          , "IsLnrelNear"         -- 是否為準利害關係人
          , "IsDate"              -- 是否資訊日期
    )
    SELECT TF."CustUKey"                  AS "CustUKey"            -- 客戶識別碼 VARCHAR2 32 
          ,CASE
             WHEN CUSP."LMSACN" = 601776 -- 2021-12-10 智偉修改 from Linda
             THEN 'A111111131'
           ELSE REPLACE(TRIM(CUSP."CUSID1"),CHR(26),'')
           END                            AS "CustId"              -- 身份證字號/統一編號 VARCHAR2 10 
          ,CUSP."LMSACN"                  AS "CustNo"              -- 戶號 DECIMAL 7 
          ,LPAD(CUSP."CUSBRH",4,'0')      AS "BranchNo"            -- 單位別 VARCHAR2 4 
          ,REPLACE(REPLACE(TRIM(CUSP."CUSNA1"),' ','')||REPLACE(TRIM(CUSP."CUSNA5"),' ',''),'○','o')
                                          AS "CustName"            -- 戶名/公司名稱 NVARCHAR2 100 
          ,CUSP."CUSBDT"                  AS "Birthday"            -- 出生年月日/設立日期 decimald 8 
          ,CASE
             -- 2022-08-10 from Linda & SKL 珮琪 
             -- 都是個人戶怎麼會有沒性別的時候，資料轉換應該要清理資料
             WHEN TRIM(CUSP."CUSSEX") IN ('1','2')
             THEN TRIM(CUSP."CUSSEX")
             WHEN LENGTHB(REPLACE(TRIM(CUSP."CUSID1"),CHR(26),'')) = 10
                  AND SUBSTR(REPLACE(TRIM(CUSP."CUSID1"),CHR(26),''),2,1) IN ('1','2')
             THEN SUBSTR(REPLACE(TRIM(CUSP."CUSID1"),CHR(26),''),2,1)
           ELSE '0'
           END                           AS "Sex"                 -- 性別 VARCHAR2 1 
          ,CASE
             WHEN TRIM(CUSP."CUSECD") IN ('@','0','8','A','B','C','D','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y')
             THEN '00'
             WHEN TRIM(CUSP."CUSECD") IN ('1','2','3','4','5','6','7','9') -- 2021-07-22 賴桑確認修改 : 增加06:團體戶
             THEN LPAD(TRIM(CUSP."CUSECD"),2,'0')
             WHEN TRIM(CUSP."CUSECD") = 'E'
             THEN '01'
           ELSE TRIM(CUSP."CUSECD") END
                                          AS "CustTypeCode"        -- 客戶別 VARCHAR2 2 
          ,CASE
             -- 2023-03-07 Wei from QC:2091
             WHEN NVL(QC2091.NEW_CUSOCD,' ') != ' '
             THEN LPAD(QC2091.NEW_CUSOCD,6,'0')
             -- 2022-08-10 from Linda & SKL 珮琪 
             -- 職業類別:資料轉換經過清理後，會有自然人不是060000？如果不會為什麼不是直接取客戶檔的職業類別
             WHEN NVL(ENPP."ENPUSE",' ') = 'Y' -- 在企金自然人檔有值者，且啟用記號為Y者，為企金自然人
             THEN '060000'
             WHEN TRIM(CUSP."CUSENT") IN ('0','2')
             THEN '060000'
           ELSE LPAD(CUSP."CUSOCD",6,'0')
           END                            AS "IndustryCode"        -- 行業別 VARCHAR2 6 -- 2021-07-22修改: 位數不足6碼者，前補零
          -- 2022-01-19 智偉一併修改:null時擺TW,有請清河確認此邏輯是否正確
          ,NVL(CUAP."CUSNAT",'TW')        AS "NationalityCode"     -- 自然人:出生地國籍 / 法人:註冊地國籍 VARCHAR2 2 
          -- 2022-01-19 新壽IT清河:目前轉入客戶主檔(CustMain)的居住地國籍是空的，因該欄位為新增欄位且必填，轉換時直接將該欄位寫入TW(中華民國)，謝謝。
          ,'TW'                           AS "BussNationalityCode" -- 自然人:居住地國籍 / 法人:營業地國籍 VARCHAR2 2 
          ,TRIM(CUSP."CUSID2")            AS "SpouseId"            -- 配偶身份證號/負責人身分證 VARCHAR2 10 
          ,REPLACE(CUSP."CUSNA2",'○','o') AS "SpouseName"          -- 配偶姓名/負責人姓名 NVARCHAR2 100 
          ,CASE 
             WHEN LENGTH(TRIM(CUSP."CUSZPA")) >= 3 THEN SUBSTR(TRIM(CUSP."CUSZPA"),0,3)
           ELSE '' END                    AS "RegZip3"             -- 戶籍-郵遞區號前三碼 VARCHAR2 3 
          ,CASE 
             WHEN LENGTH(TRIM(CUSP."CUSZPA")) = 5 THEN SUBSTR(TRIM(CUSP."CUSZPA"),4,2)
           ELSE '' END                    AS "RegZip2"             -- 戶籍-郵遞區號後兩碼 VARCHAR2 2 
          ,''                             AS "RegCityCode"         -- 戶籍-縣市代碼 VARCHAR2 2 
          ,''                             AS "RegAreaCode"         -- 戶籍-鄉鎮市區代碼 VARCHAR2 3 
          ,TRIM(TO_SINGLE_BYTE(TRIM(CUSP."CUSADA")||TRIM(CUSP."CUSADB")))
                                          AS "RegRoad"             -- 戶籍-路名 NVARCHAR2 40 
          ,''                             AS "RegSection"          -- 戶籍-段 VARCHAR2 5 
          ,''                             AS "RegAlley"            -- 戶籍-巷 VARCHAR2 5 
          ,''                             AS "RegLane"             -- 戶籍-弄 VARCHAR2 5 
          ,''                             AS "RegNum"              -- 戶籍-號 VARCHAR2 5 
          ,''                             AS "RegNumDash"          -- 戶籍-號之 VARCHAR2 5 
          ,''                             AS "RegFloor"            -- 戶籍-樓 VARCHAR2 5 
          ,''                             AS "RegFloorDash"        -- 戶籍-樓之 VARCHAR2 5 
          ,CASE 
             WHEN LENGTH(TRIM(CUSP."CUSZP1")) >= 3 THEN SUBSTR(TRIM(CUSP."CUSZP1"),0,3)
           ELSE '' END                    AS "CurrZip3"            -- 通訊-郵遞區號前三碼 VARCHAR2 3 
          ,CASE 
             WHEN LENGTH(TRIM(CUSP."CUSZP1")) = 5 THEN SUBSTR(TRIM(CUSP."CUSZP1"),4,2)
           ELSE '' END                    AS "CurrZip2"            -- 通訊-郵遞區號後兩碼 VARCHAR2 2 
          ,''                             AS "CurrCityCode"        -- 通訊-縣市代碼 VARCHAR2 2 
          ,''                             AS "CurrAreaCode"        -- 通訊-鄉鎮市區代碼 VARCHAR2 3 
          ,TRIM(TO_SINGLE_BYTE(TRIM(CUSP."CUSAD1")||TRIM(CUSP."CUSAD2")))
                                          AS "CurrRoad"            -- 通訊-路名 NVARCHAR2 40 
          ,''                             AS "CurrSection"         -- 通訊-段 VARCHAR2 5 
          ,''                             AS "CurrAlley"           -- 通訊-巷 VARCHAR2 5 
          ,''                             AS "CurrLane"            -- 通訊-弄 VARCHAR2 5 
          ,''                             AS "CurrNum"             -- 通訊-號 VARCHAR2 5 
          ,''                             AS "CurrNumDash"         -- 通訊-號之 VARCHAR2 5 
          ,''                             AS "CurrFloor"           -- 通訊-樓 VARCHAR2 5 
          ,''                             AS "CurrFloorDash"       -- 通訊-樓之 VARCHAR2 5 
          ,CASE
             WHEN NVL(CUSP."CUSCCD",'0') != '0'
             THEN CUSP."CUSCCD"
             WHEN LENGTHB(CUSP."CUSID1") = 8
             THEN '2'
             WHEN LENGTHB(CUSP."CUSID1") = 10
             THEN '1'
           ELSE '1'
           END                            AS "CuscCd"              -- 身份別 VARCHAR2 1 
          ,CASE
             -- 2023-03-07 Wei 修改 from QC:2091
             WHEN NVL(QC2091.NEW_CUSENT,0) != 0
             THEN TO_CHAR(QC2091.NEW_CUSENT)
             -- 在企金自然人檔有值者，且啟用記號為Y者，為企金自然人
             WHEN NVL(ENPP."ENPUSE",' ') = 'Y'         THEN '2'
             WHEN TRIM(CUSP."CUSENT") IN ('0','1','2') THEN TRIM(CUSP."CUSENT")
           ELSE TRIM(CUSP."CUSENT") END
                                          AS "EntCode"             -- 企金別 VARCHAR2 1 
          ,NVL(CUSP."CUSEMP",EmpFix."EmpNo")
                                          AS "EmpNo"               -- 員工代號 VARCHAR2 6 
          ,TRIM(CUAP."CUSENM")            AS "EName"               -- 英文姓名 VARCHAR2 20 
          ,CASE
             WHEN TRIM(CUAP."CUSEDU") IN ('1','2','3','4','5','6','7')
             THEN TRIM(CUAP."CUSEDU")
           ELSE TRIM(CUAP."CUSEDU") END
                                          AS "EduCode"             -- 教育程度代號 VARCHAR2 1 
          ,CUAP."CUSHOU"                  AS "OwnedHome"           -- 自有住宅有無 VARCHAR2 1 
          ,TRIM(CUAP."CUSWCM")            AS "CurrCompName"        -- 任職機構名稱 NVARCHAR2 60 
          ,TRIM(CUAP."CUSWID")            AS "CurrCompId"          -- 任職機構統編 VARCHAR2 8 
          ,CUAP."CUSWTL"                  AS "CurrCompTel"         -- 任職機構電話 VARCHAR2 16 
          ,CUAP."CUSTIT"                  AS "JobTitle"            -- 職位名稱 NVARCHAR2 20 
          ,CUAP."CUSSVY"                  AS "JobTenure"           -- 服務年資 VARCHAR2 2 
          -- 2022-06-28 Wei From 家興:資料轉換轉入時單位改為仟元
          ,TRUNC(NVL(CUAP."CUSYIN",0) / 1000)
                                          AS "IncomeOfYearly"      -- 年收入 DECIMAL 9 
          ,CUAP."CUSIYM"                  AS "IncomeDataDate"      -- 年收入資料年月 VARCHAR2 6 
          ,CUAP."CUSPNO"                  AS "PassportNo"          -- 護照號碼 VARCHAR2 20 
          ,CUAP."AMLOCD"                  AS "AMLJobCode"          -- AML職業別 VARCHAR2 3 
          ,CUAP."AMLOTP"                  AS "AMLGroup"            -- AML組織 VARCHAR2 3 
          --,TRIM(CUAP."CUSNA8")       AS "IndigenousName"      -- 原住民姓名 NVARCHAR2 100 
          ,''                             AS "IndigenousName"      -- 原住民姓名 NVARCHAR2 100 
          ,NVL(ACT.APLAPN,0)       AS "LastFacmNo"          -- 已編額度編號 DECIMAL 3 
          ,0                              AS "LastSyndNo"          -- 已編聯貸案序號 DECIMAL  
          -- 2021-11-09 新增邏輯 原值為2者轉2,其他皆轉1
          ,CASE
             WHEN CUSP."ALWINQ" = 2
             THEN 2
           ELSE 1 END                     AS "AllowInquire"        -- 開放查詢 VARCHAR2 1 
          ,TRIM(CUSP."CUSMAL")            AS "Email"               -- Email Address VARCHAR2 50 
          ,0                              AS "ActFg"               -- 交易進行記號 DECIMAL 1 
          ,CUSP."CUSEM3"                  AS "Introducer"          -- 介紹人 VARCHAR2 6 
          ,CUSP."CUSEM2"                  AS "BusinessOfficer"     -- 房貸專員/企金人員 VARCHAR2 6
          ,'N'                            AS "IsSuspected"         -- 是否為金控「疑似準利害關係人」名單 VARCHAR2 1 
          ,'N'                            AS "IsSuspectedCheck"    -- 是否為金控疑似準利害關係人 VARCHAR2 1 
          ,'N'                            AS "IsSuspectedCheckType"-- 是否為金控疑似準利害關係人_確認狀態 VARCHAR2 1 
          ,0                              AS "DataStatus"          -- 資料狀態 DECIMAL 1 
          ,0                              AS "TypeCode"            -- 建檔身分別 DECIMAL 1 
          ,CUSP."CUSSTN"                  AS "Station"              -- 站別 VARCHAR2 3
          -- 2022-01-04 智偉修改:若原檔案的建檔日期有值,使用該值,否則用轉換時的日期
          ,CASE
             WHEN CUSP."CUSCDT" > 0
             THEN TO_DATE(CUSP."CUSCDT",'YYYYMMDD')
           ELSE JOB_START_TIME
           END                            AS "CreateDate"          -- 建檔日期時間 DATE  
          ,'999999'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 
          -- 2022-01-04 智偉修改:若原檔案的建檔日期有值,使用該值,否則用轉換時的日期
          ,CASE
             WHEN CUSP."CUSMDT" > 0
             THEN TO_DATE(CUSP."CUSMDT",'YYYYMMDD')
           ELSE JOB_START_TIME
           END                            AS "LastUpdate"          -- 最後更新日期時間 DATE  
          ,'999999'                       AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 
          ,'' AS "IsLimit"
          ,'' AS "IsRelated"
          ,'' AS "IsLnrelNear"
          ,0 AS "IsDate"
    FROM "CU$CUSP" CUSP
    LEFT JOIN "CU$CUAP" CUAP ON CUAP."CUSID1" = CUSP."CUSID1"
    LEFT JOIN "LN$ENPP" ENPP ON ENPP."LMSACN" = CUSP."LMSACN"
                       AND CUSP."LMSACN" > 0
    LEFT JOIN "LA$ACTP" ACT ON ACT."LMSACN" = CUSP."LMSACN" -- 取最後額度號碼
    LEFT JOIN "TfCust" TF ON TF."CustId" = CASE
                                             WHEN CUSP."LMSACN" = 601776 -- 2021-12-10 智偉修改 from Linda
                                             THEN 'A111111131'
                                           ELSE REPLACE(TRIM(CUSP."CUSID1"),CHR(26),'')
                                           END
    LEFT JOIN "TfQC2091" QC2091 ON QC2091.CUSID1 = CUSP."CUSID1"
    LEFT JOIN "TfCustMainEmpNoFix" EmpFix ON EmpFix."CustNo" = CUSP.LMSACN
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
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_CustMain_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;

/
