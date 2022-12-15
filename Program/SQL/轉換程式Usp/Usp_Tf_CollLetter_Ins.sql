--------------------------------------------------------
--  DDL for Procedure Usp_Tf_CollLetter_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_Tf_CollLetter_Ins" 
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
    EXECUTE IMMEDIATE 'ALTER TABLE "CollLetter" DISABLE PRIMARY KEY CASCADE';
    EXECUTE IMMEDIATE 'TRUNCATE TABLE "CollLetter" DROP STORAGE';
    EXECUTE IMMEDIATE 'ALTER TABLE "CollLetter" ENABLE PRIMARY KEY';

    -- 寫入資料
    INSERT INTO "CollLetter"
    SELECT '1'                            AS "CaseCode"            -- 案件種類 VARCHAR2 1 0
          ,M.LMSACN                       AS "CustNo"              -- 借款人戶號 DECIMAL 7 0
          ,M.LMSAPN                       AS "FacmNo"              -- 額度編號 DECIMAL 3 0
          ,TO_NUMBER(TO_CHAR(M.Entry_Date,'YYYYMMDD'))
                                          AS "AcDate"              -- 作業日期 DecimalD 8 0
          ,SUBSTR(M.UserID,0,6)
                                          AS "TitaTlrNo"           -- 經辦 VARCHAR2 6 0
          ,LPAD(M.SerialNum,8,'0')
                                          AS "TitaTxtNo"           -- 交易序號 VARCHAR2 8 0
          -- 2022-03-02 Wei 修改: from Linda "MailTypeCode" =>舊資料若=3存證信函則轉新資料=2,其他則新資料轉成1
          ,CASE
             WHEN M.Mail_Type = 3
             THEN '2'
           ELSE '1' END                   AS "MailTypeCode"        -- 發函種類 VARCHAR2 1 0
          ,NVL(M.Mail_Date,0)
                                          AS "MailDate"            -- 發函日期 DecimalD 8 0
          ,M.Mail_Person                  AS "MailObj"             -- 發函對象 VARCHAR2 1 0
          ,M.Mail_PersonName
                                          AS "CustName"            -- 姓名 NVARCHAR2 100 0
          -- 2022-03-02 Wei 修改: from Linda "DelvrYet" =>舊資料若=1則新資料=1已送達,其他則轉成新資料=2未送達
          ,CASE
             WHEN M.Mail_Flg = '1'
             THEN '1'
           ELSE '2' END                   AS "DelvrYet"            -- 送達否 VARCHAR2 1 0
          -- 2022-03-02 Wei 修改: from Linda
          -- "DelvrCode" 送達方式舊資料1.親自送達 2.郵務-平信寄出 3.郵務-掛號寄出
          -- =>新的1:郵務-平信 2:郵務-限時專送 3:郵務-掛號 4:郵務-雙掛號 5:親自送達; 
          -- 舊資料非1~3時新的放0
          ,CASE
             WHEN TO_CHAR(TO_NUMBER(M.Mail_Service)) = '1' 
             THEN '5'
             WHEN TO_CHAR(TO_NUMBER(M.Mail_Service)) = '2'
             THEN '1'
             WHEN TO_CHAR(TO_NUMBER(M.Mail_Service)) = '3'
             THEN '3'
           ELSE '0' END                   AS "DelvrCode"           -- 送達方式 VARCHAR2 1 0
          ,0                              AS "AddressCode"         -- 寄送地點選項 DECIMAL 1 0
          ,M.Mail_Addr                    AS "Address"             -- 寄送地點 NVARCHAR2 60 0
          ,TRIM(TO_SINGLE_BYTE(M.other_record))
                                          AS "Remark"              -- 其他記錄 NVARCHAR2 500 0
          ,JOB_START_TIME                 AS "CreateDate"          -- 建檔日期時間 DATE 8 0
          ,'999999'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 0
          ,JOB_START_TIME                 AS "LastUpdate"          -- 最後更新日期時間 DATE 8 0
          ,'999999'                       AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 0
    FROM REMIN_REMINMAIL_INFO M
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
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_CollLetter_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;



/
