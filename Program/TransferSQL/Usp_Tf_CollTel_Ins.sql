--------------------------------------------------------
--  DDL for Procedure Usp_Tf_CollTel_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "Usp_Tf_CollTel_Ins" 
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
    EXECUTE IMMEDIATE 'ALTER TABLE "CollTel" DISABLE PRIMARY KEY CASCADE';
    EXECUTE IMMEDIATE 'TRUNCATE TABLE "CollTel" DROP STORAGE';
    EXECUTE IMMEDIATE 'ALTER TABLE "CollTel" ENABLE PRIMARY KEY';

    -- 寫入資料
    INSERT INTO "CollTel"
    SELECT '1'                            AS "CaseCode"            -- 案件種類 VARCHAR2 1 0
          ,S0.LMSACN                      AS "CustNo"              -- 借款人戶號 DECIMAL 7 0
          ,S0.LMSAPN                      AS "FacmNo"              -- 額度編號 DECIMAL 3 0
          ,TO_NUMBER(TO_CHAR(S0.Entry_Date,'YYYYMMDD'))
                                          AS "AcDate"              -- 系統日期 DecimalD 8 0
          ,SUBSTR(S0.UserID,0,6)
                                          AS "TitaTlrNo"           -- 經辦 VARCHAR2 6 0
          ,LPAD(S0.SerialNum,8,'0') -- 2021-03-18 智偉修改 補零
                                          AS "TitaTxtNo"           -- 交易序號 VARCHAR2 8 0
          ,NVL(S0.Calling_Date,0)
                                          AS "TelDate"             -- 電催日期 DecimalD 8 0
          ,S0.Calling_Time                AS "TelTime"             -- 電催時間 VARCHAR2 4 0
          ,S0.Calling_Person              AS "ContactCode"         -- 聯絡對象 VARCHAR2 1 0
          ,S0.Receive_Person              AS "RecvrCode"           -- 接話人 VARCHAR2 1 0
          ,CASE
             WHEN LENGTHB(TO_CHAR(TRIM(TO_SINGLE_BYTE(S0.Calling_Tel)))) >= 1
                  AND LENGTHB(SUBSTR(TO_CHAR(TRIM(TO_SINGLE_BYTE(S0.Calling_Tel))),0,5)) <= 5
             THEN SUBSTR(TO_CHAR(TRIM(TO_SINGLE_BYTE(S0.Calling_Tel))),0,5)
           ELSE '' END                    AS "TelArea"            -- 連絡電話 VARCHAR2 5 0
          ,CASE
             WHEN LENGTHB(TO_CHAR(TRIM(TO_SINGLE_BYTE(S0.Calling_Tel)))) >= 6 
                  AND LENGTHB(SUBSTR(TO_CHAR(TRIM(TO_SINGLE_BYTE(S0.Calling_Tel))),6,10)) <= 10
             THEN SUBSTR(TO_CHAR(TRIM(TO_SINGLE_BYTE(S0.Calling_Tel))),6,10)
           ELSE '' END                    AS "TelNo"            -- 連絡電話 VARCHAR2 10 0
          ,CASE
             WHEN LENGTHB(TO_CHAR(TRIM(TO_SINGLE_BYTE(S0.Calling_Tel)))) >= 16
                  AND LENGTHB(SUBSTR(TO_CHAR(TRIM(TO_SINGLE_BYTE(S0.Calling_Tel))),16)) <= 5
             THEN SUBSTR(TO_CHAR(TRIM(TO_SINGLE_BYTE(S0.Calling_Tel))),16)
           ELSE '' END                    AS "TelExt"            -- 連絡電話 VARCHAR2 5 0
          ,SUBSTR(S0.Calling_Result,-1,1)
                                          AS "ResultCode"          -- 通話結果 VARCHAR2 1 0
          ,S0.Other_Record                AS "Remark"              -- 其他記錄 NVARCHAR2 500 0
          ,NVL(S0.Calling_Date,0)
                                          AS "CallDate"            -- 通話日期 DecimalD 8 0
          ,JOB_START_TIME                 AS "CreateDate"          -- 建檔日期時間 DATE 8 0
          ,'999999'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 0
          ,JOB_START_TIME                 AS "LastUpdate"          -- 最後更新日期時間 DATE 8 0
          ,'999999'                       AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 0
    FROM "ReminTel_Info" S0
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
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_CollTel_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;



/
