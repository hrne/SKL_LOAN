--------------------------------------------------------
--  DDL for Procedure Usp_Tf_MlaundryDetail_Ins
--------------------------------------------------------
set define off;
CREATE OR REPLACE PROCEDURE "Usp_Tf_MlaundryDetail_Ins" 
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
    EXECUTE IMMEDIATE 'ALTER TABLE "MlaundryDetail" DISABLE PRIMARY KEY CASCADE';
    EXECUTE IMMEDIATE 'TRUNCATE TABLE "MlaundryDetail" DROP STORAGE';
    EXECUTE IMMEDIATE 'ALTER TABLE "MlaundryDetail" ENABLE PRIMARY KEY';

    -- 寫入資料
    INSERT INTO "MlaundryDetail" (
        "EntryDate" -- 入帳日期 Decimald 8  
      , "Factor" -- 交易樣態 DECIMAL 2  "1:樣態1 2:樣態2 3:樣態3"
      , "CustNo" -- 戶號 DECIMAL 7  
      , "TotalCnt" -- 累積筆數 DECIMAL 4  
      , "TotalAmt" -- 累積金額   DECIMAL 16 2 
      , "Rational" -- 合理性記號 VARCHAR2 1  "Y:是 N:否 空白:未註記"
      , "EmpNoDesc" -- 經辦合理性說明 NVARCHAR2 150  2022/8/25長度放大150
      , "ManagerCheck" -- 主管覆核 VARCHAR2 1  "Y:同意 N:不同意 空白:未覆核"
      , "ManagerDate" -- 主管同意日期 Decimald 8  
      , "ManagerCheckDate" -- 主管覆核日期 Decimald 8  主管第二次覆核時顯示欄位
      , "ManagerDesc" -- 主管覆核說明 NVARCHAR2 150  2022/8/25長度放大150
      , "CreateDate" -- 建檔日期時間 DATE 8  
      , "CreateEmpNo" -- 建檔人員 VARCHAR2 6  
      , "LastUpdate" -- 最後更新日期時間 DATE 8  
      , "LastUpdateEmpNo" -- 最後更新人員 VARCHAR2 6 
    )
    SELECT A03.TRXIDT        AS "EntryDate" -- 入帳日期 Decimald 8  
         , A03.AMLTPE        AS "Factor" -- 交易樣態 DECIMAL 2  "1:樣態1 2:樣態2 3:樣態3"
         , A03.LMSACN        AS "CustNo" -- 戶號 DECIMAL 7  
         , NVL(AC2.DOCSEQ,NVL(AC1.DOCSEQ,1))
                             AS "TotalCnt" -- 累積筆數 DECIMAL 4  
         , A03.W08AM1        AS "TotalAmt" -- 累積金額   DECIMAL 16 2 
         , AC1.AMLCMD        AS "Rational" -- 合理性記號 VARCHAR2 1  "Y:是 N:否 空白:未註記"
         , TRIM(NVL(AC1.DOCTXT1,' ') || NVL(AC1.DOCTXT2,' ') || NVL(AC1.DOCTXT3,' ')) 
                             AS "EmpNoDesc" -- 經辦合理性說明 NVARCHAR2 150  2022/8/25長度放大150
         , CASE
             WHEN TRIM(NVL(AC2.DOCTXTA,' ') || NVL(AC2.DOCTXTB,' ') || NVL(AC2.DOCTXTC,' ')) IS NOT NULL
             THEN 'Y'
             WHEN TRIM(NVL(AC1.DOCTXTA,' ') || NVL(AC1.DOCTXTB,' ') || NVL(AC1.DOCTXTC,' ')) IS NOT NULL
             THEN 'Y'
           ELSE NULL END     AS "ManagerCheck" -- 主管覆核 VARCHAR2 1  "Y:同意 N:不同意 空白:未覆核"
         , CASE
             WHEN NVL(AC1.CHGDAT,0) > 0
             THEN AC1.CHGDAT
           ELSE 0
           END               AS "ManagerDate" -- 主管同意日期 Decimald 8  
         , CASE
             WHEN NVL(AC1.CHGDAT,0) > 0
             THEN AC1.CHGDAT -- 2022-10-20 Wei 增加 from Linda line:
                             -- 請問MlaundryDetail裡的主管覆核日期轉檔程式備註是寫主管第二次覆核時顯示欄位
                             -- 之前珮瑜測的時候說主管同意日期已經押了,主管覆核日期就不應該空白,
                             -- 轉檔用的判斷第二次覆核是指什麼?
             WHEN NVL(AC2.CHGDAT,0) > 0
             THEN AC2.CHGDAT
           ELSE 0
           END               AS "ManagerCheckDate" -- 主管覆核日期 Decimald 8  主管第二次覆核時顯示欄位
         , CASE
             WHEN TRIM(NVL(AC2.DOCTXTA,' ') || NVL(AC2.DOCTXTB,' ') || NVL(AC2.DOCTXTC,' ')) IS NOT NULL
             THEN TRIM(NVL(AC2.DOCTXTA,' ') || NVL(AC2.DOCTXTB,' ') || NVL(AC2.DOCTXTC,' '))
             WHEN TRIM(NVL(AC1.DOCTXTA,' ') || NVL(AC1.DOCTXTB,' ') || NVL(AC1.DOCTXTC,' ')) IS NOT NULL
             THEN TRIM(NVL(AC1.DOCTXTA,' ') || NVL(AC1.DOCTXTB,' ') || NVL(AC1.DOCTXTC,' '))
           ELSE NULL END     AS "ManagerDesc" -- 主管覆核說明 NVARCHAR2 150  2022/8/25長度放大150
         , CASE
             WHEN NVL(AC1.CRTDAT,0) > 0
             THEN TO_DATE(AC1.CRTDAT,'YYYYMMDD')
           ELSE JOB_START_TIME
           END               AS "CreateDate" -- 建檔日期時間 DATE 8  
         , NVL(AEM1."EmpNo",AC1.CRTEMP)
                             AS "CreateEmpNo" -- 建檔人員 VARCHAR2 6  
         , CASE
             WHEN NVL(AC2.CHGDAT,0) > 0
             THEN TO_DATE(AC2.CHGDAT,'YYYYMMDD')
             WHEN NVL(AC1.CHGDAT,0) > 0
             THEN TO_DATE(AC1.CHGDAT,'YYYYMMDD')
             WHEN NVL(AC1.CRTDAT,0) > 0
             THEN TO_DATE(AC1.CRTDAT,'YYYYMMDD')
           ELSE JOB_START_TIME
           END               AS "LastUpdate" -- 最後更新日期時間 DATE 8  
         , NVL(AEM2."EmpNo",NVL(AEM1."EmpNo",NVL(AC2.CHGEMP,AC1.CHGEMP)))
                             AS "LastUpdateEmpNo" -- 最後更新人員 VARCHAR2 6 
    FROM DAT_LNAML03P A03 
    LEFT JOIN DAT_LNAMlC1P AC1 ON AC1.TRXIDT = A03.TRXIDT
                              AND AC1.AMLTPE = A03.AMLTPE
                              AND AC1.LMSACN = A03.LMSACN
                              AND AC1.DOCSEQ = 1
    LEFT JOIN DAT_LNAMlC1P AC2 ON AC2.TRXIDT = A03.TRXIDT
                              AND AC2.AMLTPE = A03.AMLTPE
                              AND AC2.LMSACN = A03.LMSACN
                              AND AC2.DOCSEQ = 2
    LEFT JOIN "As400EmpNoMapping" AEM1 ON AEM1."As400TellerNo" = AC1.CRTEMP
    LEFT JOIN "As400EmpNoMapping" AEM2 ON AEM2."As400TellerNo" = NVL(AC2.CHGEMP,AC1.CHGEMP)
    ;

    -- 寫入資料
    INSERT INTO "MlaundryDetail" (
        "EntryDate" -- 入帳日期 Decimald 8  
      , "Factor" -- 交易樣態 DECIMAL 2  "1:樣態1 2:樣態2 3:樣態3"
      , "CustNo" -- 戶號 DECIMAL 7  
      , "TotalCnt" -- 累積筆數 DECIMAL 4  
      , "TotalAmt" -- 累積金額   DECIMAL 16 2 
      , "Rational" -- 合理性記號 VARCHAR2 1  "Y:是 N:否 空白:未註記"
      , "EmpNoDesc" -- 經辦合理性說明 NVARCHAR2 150  2022/8/25長度放大150
      , "ManagerCheck" -- 主管覆核 VARCHAR2 1  "Y:同意 N:不同意 空白:未覆核"
      , "ManagerDate" -- 主管同意日期 Decimald 8  
      , "ManagerCheckDate" -- 主管覆核日期 Decimald 8  主管第二次覆核時顯示欄位
      , "ManagerDesc" -- 主管覆核說明 NVARCHAR2 150  2022/8/25長度放大150
      , "CreateDate" -- 建檔日期時間 DATE 8  
      , "CreateEmpNo" -- 建檔人員 VARCHAR2 6  
      , "LastUpdate" -- 最後更新日期時間 DATE 8  
      , "LastUpdateEmpNo" -- 最後更新人員 VARCHAR2 6 
    )
    SELECT A04.TRXIDT        AS "EntryDate" -- 入帳日期 Decimald 8  
         , A04.AMLTPE        AS "Factor" -- 交易樣態 DECIMAL 2  "1:樣態1 2:樣態2 3:樣態3"
         , A04.LMSACN        AS "CustNo" -- 戶號 DECIMAL 7  
         , NVL(AD2.DOCSEQ,NVL(AD1.DOCSEQ,1))
                             AS "TotalCnt" -- 累積筆數 DECIMAL 4  
         , A04.W08AM1        AS "TotalAmt" -- 累積金額   DECIMAL 16 2 
         , AD1.AMLCMD        AS "Rational" -- 合理性記號 VARCHAR2 1  "Y:是 N:否 空白:未註記"
         , TRIM(NVL(AD1.DOCTXT1,' ') || NVL(AD1.DOCTXT2,' ') || NVL(AD1.DOCTXT3,' ')) 
                             AS "EmpNoDesc" -- 經辦合理性說明 NVARCHAR2 150  2022/8/25長度放大150
         , CASE
             WHEN TRIM(NVL(AD2.DOCTXTA,' ') || NVL(AD2.DOCTXTB,' ') || NVL(AD2.DOCTXTC,' ')) IS NOT NULL
             THEN 'Y'
             WHEN TRIM(NVL(AD1.DOCTXTA,' ') || NVL(AD1.DOCTXTB,' ') || NVL(AD1.DOCTXTC,' ')) IS NOT NULL
             THEN 'Y'
           ELSE NULL END     AS "ManagerCheck" -- 主管覆核 VARCHAR2 1  "Y:同意 N:不同意 空白:未覆核"
         , CASE
             WHEN NVL(AD1.CHGDAT,0) > 0
             THEN AD1.CHGDAT
           ELSE 0
           END               AS "ManagerDate" -- 主管同意日期 Decimald 8  
         , CASE
             WHEN NVL(AD1.CHGDAT,0) > 0
             THEN AD1.CHGDAT -- 2022-10-20 Wei 增加 from Linda line:
                             -- 請問MlaundryDetail裡的主管覆核日期轉檔程式備註是寫主管第二次覆核時顯示欄位
                             -- 之前珮瑜測的時候說主管同意日期已經押了,主管覆核日期就不應該空白,
                             -- 轉檔用的判斷第二次覆核是指什麼?
             WHEN NVL(AD2.CHGDAT,0) > 0
             THEN AD2.CHGDAT
           ELSE 0
           END               AS "ManagerCheckDate" -- 主管覆核日期 Decimald 8  主管第二次覆核時顯示欄位
         , CASE
             WHEN TRIM(NVL(AD2.DOCTXTA,' ') || NVL(AD2.DOCTXTB,' ') || NVL(AD2.DOCTXTC,' ')) IS NOT NULL
             THEN TRIM(NVL(AD2.DOCTXTA,' ') || NVL(AD2.DOCTXTB,' ') || NVL(AD2.DOCTXTC,' '))
             WHEN TRIM(NVL(AD1.DOCTXTA,' ') || NVL(AD1.DOCTXTB,' ') || NVL(AD1.DOCTXTC,' ')) IS NOT NULL
             THEN TRIM(NVL(AD1.DOCTXTA,' ') || NVL(AD1.DOCTXTB,' ') || NVL(AD1.DOCTXTC,' '))
           ELSE NULL END     AS "ManagerDesc" -- 主管覆核說明 NVARCHAR2 150  2022/8/25長度放大150
         , CASE
             WHEN NVL(AD1.CRTDAT,0) > 0
             THEN TO_DATE(AD1.CRTDAT,'YYYYMMDD')
           ELSE JOB_START_TIME
           END               AS "CreateDate" -- 建檔日期時間 DATE 8  
         , NVL(AEM1."EmpNo",AD1.CRTEMP)
                             AS "CreateEmpNo" -- 建檔人員 VARCHAR2 6  
         , CASE
             WHEN NVL(AD2.CHGDAT,0) > 0
             THEN TO_DATE(AD2.CHGDAT,'YYYYMMDD')
             WHEN NVL(AD1.CHGDAT,0) > 0
             THEN TO_DATE(AD1.CHGDAT,'YYYYMMDD')
             WHEN NVL(AD1.CRTDAT,0) > 0
             THEN TO_DATE(AD1.CRTDAT,'YYYYMMDD')
           ELSE JOB_START_TIME
           END               AS "LastUpdate" -- 最後更新日期時間 DATE 8  
         , NVL(AEM2."EmpNo",NVL(AEM1."EmpNo",NVL(AD2.CHGEMP,AD1.CHGEMP)))
                             AS "LastUpdateEmpNo" -- 最後更新人員 VARCHAR2 6 
    FROM DAT_LNAML04P A04 
    LEFT JOIN DAT_LNAMlD1P AD1 ON AD1.TRXIDT = A04.TRXIDT
                              AND AD1.AMLTPE = A04.AMLTPE
                              AND AD1.LMSACN = A04.LMSACN
                              AND AD1.DOCSEQ = 1
    LEFT JOIN DAT_LNAMlD1P AD2 ON AD2.TRXIDT = A04.TRXIDT
                              AND AD2.AMLTPE = A04.AMLTPE
                              AND AD2.LMSACN = A04.LMSACN
                              AND AD2.DOCSEQ = 2
    LEFT JOIN "As400EmpNoMapping" AEM1 ON AEM1."As400TellerNo" = AD1.CRTEMP
    LEFT JOIN "As400EmpNoMapping" AEM2 ON AEM2."As400TellerNo" = NVL(AD2.CHGEMP,AD1.CHGEMP)
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