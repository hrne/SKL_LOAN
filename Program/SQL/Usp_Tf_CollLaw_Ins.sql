--------------------------------------------------------
--  DDL for Procedure Usp_Tf_CollLaw_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_Tf_CollLaw_Ins" 
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
    EXECUTE IMMEDIATE 'ALTER TABLE "CollLaw" DISABLE PRIMARY KEY CASCADE';
    EXECUTE IMMEDIATE 'TRUNCATE TABLE "CollLaw" DROP STORAGE';
    EXECUTE IMMEDIATE 'ALTER TABLE "CollLaw" ENABLE PRIMARY KEY';

    -- 寫入資料
    INSERT INTO "CollLaw" (
        "CaseCode"            -- 案件種類 VARCHAR2 1 0
      , "ClCode1"             -- 擔保品代號1 DECIMAL 1 0
      , "ClCode2"             -- 擔保品代號2 DECIMAL 2 0
      , "ClNo"                -- 擔保品編號 DECIMAL 7 0
      , "CustNo"              -- 借款人戶號 DECIMAL 7 0
      , "FacmNo"              -- 額度編號 DECIMAL 3 0
      , "AcDate"              -- 作業日期 DecimalD 8 0
      , "TitaTlrNo"           -- 經辦 VARCHAR2 6 0
      , "TitaTxtNo"           -- 交易序號 VARCHAR2 8 0
      , "RecordDate"          -- 記錄日期 DecimalD 8 0
      , "LegalProg"           -- 法務進度 VARCHAR2 3 0
      , "Amount"              -- 金額 DECIMAL 16 2
      , "Remark"              -- 其他記錄 VARCHAR2 1 0
      , "Memo"               -- 其他紀錄內容 VARCHAR2 500 0
      , "CreateDate"          -- 建檔日期時間 DATE 8 0
      , "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 0
      , "LastUpdate"          -- 最後更新日期時間 DATE 8 0
      , "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 0
    )
    SELECT '1'                            AS "CaseCode"            -- 案件種類 VARCHAR2 1 0
          ,NVL(CF."ClCode1",0)            AS "ClCode1"             -- 擔保品代號1 DECIMAL 1 0
          ,NVL(CF."ClCode2",0)            AS "ClCode2"             -- 擔保品代號2 DECIMAL 2 0
          ,NVL(CF."ClNo",0)               AS "ClNo"                -- 擔保品編號 DECIMAL 7 0
          ,LAWP.LMSACN                    AS "CustNo"              -- 借款人戶號 DECIMAL 7 0
          ,LAWP.LMSAPN                    AS "FacmNo"              -- 額度編號 DECIMAL 3 0
          ,TO_NUMBER(TO_CHAR(LAWP.entry_date,'YYYYMMDD'))
                                          AS "AcDate"              -- 作業日期 DecimalD 8 0
          ,NVL(SUBSTR(LAWP.UserID,0,6),'999999') -- Wei 修改: from Linda TitaTlrNo不可為空白值,KEY值會找不到該筆資料
                                          AS "TitaTlrNo"           -- 經辦 VARCHAR2 6 0
          ,LPAD(LAWP.SerialNum,8,'0')     AS "TitaTxtNo"           -- 交易序號 VARCHAR2 8 0
          ,LAWP.process_date              AS "RecordDate"          -- 記錄日期 DecimalD 8 0
          ,LPAD(LAWP.LawCode,3,'0')       AS "LegalProg"           -- 法務進度 VARCHAR2 3 0
          ,NVL(LAWP.process_amt,0)        AS "Amount"              -- 金額 DECIMAL 16 2
          ,'0'                            AS "Remark"              -- 其他記錄 VARCHAR2 1 0
          -- 2021-03-22 把換行符修改為%n
          ,TRANSLATE(TRIM(TO_SINGLE_BYTE(LAWP.other_record)),CHR(13)||CHR(10),'$n')
                                          AS "Memo"               -- 其他紀錄內容 VARCHAR2 500 0
          ,CASE
             WHEN LAWP.entry_date IS NOT NULL
             THEN LAWP.entry_date
           ELSE JOB_START_TIME
           END                            AS "CreateDate"          -- 建檔日期時間 DATE 8 0
          ,NVL(LAWP.USERID,'999999')      AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 0
          ,CASE
             WHEN LAWP.entry_date IS NOT NULL
             THEN LAWP.entry_date
           ELSE JOB_START_TIME
           END                            AS "LastUpdate"          -- 最後更新日期時間 DATE 8 0
          ,NVL(LAWP.USERID,'999999')      AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 0
    FROM REMIN_LAWPROCESS_INFO LAWP
    LEFT JOIN "ClFac" CF ON CF."CustNo" = LAWP.LMSACN
                        AND CF."FacmNo" = LAWP.LMSAPN
                        AND CF."MainFlag" = 'Y'
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
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_CollLaw_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;



/
