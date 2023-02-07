--------------------------------------------------------
--  DDL for Procedure Usp_Tf_CollMeet_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_Tf_CollMeet_Ins" 
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
    EXECUTE IMMEDIATE 'ALTER TABLE "CollMeet" DISABLE PRIMARY KEY CASCADE';
    EXECUTE IMMEDIATE 'TRUNCATE TABLE "CollMeet" DROP STORAGE';
    EXECUTE IMMEDIATE 'ALTER TABLE "CollMeet" ENABLE PRIMARY KEY';

    -- 寫入資料
    INSERT INTO "CollMeet" (
        "CaseCode"            -- 案件種類 VARCHAR2 1 0
      , "CustNo"              -- 借款人戶號 DECIMAL 7 0
      , "FacmNo"              -- 額度編號 DECIMAL 3 0
      , "AcDate"              -- 作業日期 DecimalD 8 0
      , "TitaTlrNo"           -- 經辦 VARCHAR2 6 0
      , "TitaTxtNo"           -- 交易序號 VARCHAR2 8 0
      , "MeetDate"            -- 面催日期 DecimalD 8 0
      , "MeetTime"            -- 面催時間 VARCHAR2 4 0
      , "ContactCode"         -- 聯絡對象 VARCHAR2 1 0
      , "MeetPsnCode"         -- 面晤人 VARCHAR2 1 0
      , "CollPsnCode"         -- 催收人員 VARCHAR2 1 0
      , "CollPsnName"         -- 催收人員姓名 NVARCHAR2 8 0
      , "MeetPlaceCode"       -- 面催地點選項 DECIMAL 1 0
      , "MeetPlace"           -- 面催地點 NVARCHAR2 60 0
      , "Remark"              -- 其他記錄 NVARCHAR2 500 0
      , "CreateDate"          -- 建檔日期時間 DATE 8 0
      , "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 0
      , "LastUpdate"          -- 最後更新日期時間 DATE 8 0
      , "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 0
    )
    SELECT '1'                            AS "CaseCode"            -- 案件種類 VARCHAR2 1 0
          ,M.LMSACN                       AS "CustNo"              -- 借款人戶號 DECIMAL 7 0
          ,M.LMSAPN                       AS "FacmNo"              -- 額度編號 DECIMAL 3 0
          ,TO_NUMBER(TO_CHAR(M.Entry_Date,'YYYYMMDD'))
                                          AS "AcDate"              -- 作業日期 DecimalD 8 0
          ,SUBSTR(M.UserID,0,6)           AS "TitaTlrNo"           -- 經辦 VARCHAR2 6 0
          ,LPAD(M.SerialNum,8,'0')        AS "TitaTxtNo"           -- 交易序號 VARCHAR2 8 0
          ,NVL(M.Presence_Date,0)         AS "MeetDate"            -- 面催日期 DecimalD 8 0
          ,SUBSTR(M.Presence_Time,1,4)    AS "MeetTime"            -- 面催時間 VARCHAR2 4 0
          ,M.Presence_Person              AS "ContactCode"         -- 聯絡對象 VARCHAR2 1 0
          -- 2022-03-02 Wei修改: from Linda MeetPsnCode =>舊資料為0~3,新資料對應為1~4
          ,M.Receive_Person + 1           AS "MeetPsnCode"         -- 面晤人 VARCHAR2 1 0
          ,M.Worker_Type                  AS "CollPsnCode"         -- 催收人員 VARCHAR2 1 0
          -- 2022-03-02 Wei修改: from Linda CollMeet.CollPsnName轉舊資料改放催收人員員工編號
          ,M.Work_User                    AS "CollPsnName"         -- 催收人員姓名 NVARCHAR2 8 0
          ,0                              AS "MeetPlaceCode"       -- 面催地點選項 DECIMAL 1 0
          ,M.Presence_Addr                AS "MeetPlace"           -- 面催地點 NVARCHAR2 60 0
          ,M.Other_Record                 AS "Remark"              -- 其他記錄 NVARCHAR2 500 0
          ,JOB_START_TIME                 AS "CreateDate"          -- 建檔日期時間 DATE 8 0
          ,'999999'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 0
          ,JOB_START_TIME                 AS "LastUpdate"          -- 最後更新日期時間 DATE 8 0
          ,'999999'                       AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 0
    FROM REMIN_REMINMEET_INFO M
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
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_CollMeet_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;




/
