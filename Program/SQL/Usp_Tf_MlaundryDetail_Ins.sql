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
         , NVL(AC1.DOCSEQ,1) AS "TotalCnt" -- 累積筆數 DECIMAL 4  
         , A03.W08AM1        AS "TotalAmt" -- 累積金額   DECIMAL 16 2 
         , AC1.AMLCMD        AS "Rational" -- 合理性記號 VARCHAR2 1  "Y:是 N:否 空白:未註記"
         , TRIM(NVL(AC1.DOCTXT1,' ') || NVL(AC1.DOCTXT2,' ') || NVL(AC1.DOCTXT3,' ')) 
                             AS "EmpNoDesc" -- 經辦合理性說明 NVARCHAR2 150  2022/8/25長度放大150
         , CASE
             WHEN TRIM(NVL(AC1.DOCTXTA,' ') || NVL(AC1.DOCTXTB,' ') || NVL(AC1.DOCTXTC,' ')) IS NOT NULL
             THEN 'Y'
           ELSE NULL END     AS "ManagerCheck" -- 主管覆核 VARCHAR2 1  "Y:同意 N:不同意 空白:未覆核"
         , CASE
             WHEN AC1.CHGDAT > 0
             THEN TO_DATE(AC1.CHGDAT,'YYYYMMDD')
           ELSE 0
           END               AS "ManagerDate" -- 主管同意日期 Decimald 8  
         , CASE
             WHEN AC1.CHGDAT > 0
             THEN TO_DATE(AC1.CHGDAT,'YYYYMMDD')
           ELSE 0
           END               AS "ManagerCheckDate" -- 主管覆核日期 Decimald 8  主管第二次覆核時顯示欄位
         , TRIM(NVL(AC1.DOCTXTA,' ') || NVL(AC1.DOCTXTB,' ') || NVL(AC1.DOCTXTC,' ')) 
                             AS "ManagerDesc" -- 主管覆核說明 NVARCHAR2 150  2022/8/25長度放大150
         , CASE
             WHEN AC1.CRTDAT > 0
             THEN TO_DATE(AC1.CRTDAT,'YYYYMMDD')
           ELSE JOB_START_TIME
           END               AS "CreateDate" -- 建檔日期時間 DATE 8  
         , NVL(AEM1."EmpNo",'999999')
                             AS "CreateEmpNo" -- 建檔人員 VARCHAR2 6  
         , CASE
             WHEN AC1.CHGDAT > 0
             THEN TO_DATE(AC1.CHGDAT,'YYYYMMDD')
             WHEN AC1.CRTDAT > 0
             THEN TO_DATE(AC1.CRTDAT,'YYYYMMDD')
           ELSE JOB_START_TIME
           END               AS "LastUpdate" -- 最後更新日期時間 DATE 8  
         , NVL(AEM2."EmpNo",NVL(AEM1."EmpNo",'999999'))
                             AS "LastUpdateEmpNo" -- 最後更新人員 VARCHAR2 6 
    FROM DAT_LNAML03P A03 
    LEFT JOIN DAT_LNAMlC1P AC1 ON AC1.TRXIDT = A03.TRXIDT
                              AND AC1.AMLTPE = A03.AMLTPE
                              AND AC1.LMSACN = A03.LMSACN
    LEFT JOIN "As400EmpNoMapping" AEM1 ON AEM1."As400TellerNo" = AC1.CRTEMP
    LEFT JOIN "As400EmpNoMapping" AEM2 ON AEM2."As400TellerNo" = AC1.CHGEMP
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