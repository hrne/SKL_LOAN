--------------------------------------------------------
--  DDL for Procedure Usp_Tf_JcicZ056Log_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "Usp_Tf_JcicZ056Log_Ins" 
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
    DELETE FROM "JcicZ056Log";

    -- 寫入資料
    INSERT INTO "JcicZ056Log"
    SELECT "Ukey"              AS "Ukey"                 -- 流水號 VARCHAR2 32 0
          ,LPAD('1', 18, '0')  AS "TxSeq"                -- 交易序號 VARCHAR2 18 0
          ,"TranKey"           AS "TranKey"              -- 交易代碼 VARCHAR2 1 0
          ,"Year"              AS "Year"                 -- 年度別 DECIMAL 4 0
          ,"CourtDiv"          AS "CourtDiv"             -- 法院承審股別 NVARCHAR2 4 0
          ,"CourtCaseNo"       AS "CourtCaseNo"          -- 法院案號 NVARCHAR2 40 0
          ,"Approve"           AS "Approve"              -- 法院裁定免責確定 VARCHAR2 1 0
          ,"OutstandAmt"       AS "OutstandAmt"          -- 原始債權金額 Decimal 9 0
          ,"SubAmt"            AS "SubAmt"               -- 清算損失金額 Decimal 9 0
          ,"ClaimStatus1"      AS "ClaimStatus1"         -- 法院裁定保全處分 VARCHAR2 1 0
          ,"SaveDate"          AS "SaveDate"             -- 保全處分起始日 Decimald 8 0
          ,"ClaimStatus2"      AS "ClaimStatus2"         -- 法院裁定撤銷保全處分 VARCHAR2 1 0
          ,"SaveEndDate"       AS "SaveEndDate"          -- 保全處分撤銷日 Decimald 8 0
          ,"AdminName"         AS "AdminName"            -- 管理人姓名 NVARCHAR2 10 0
          ,"OutJcicTxtDate"    AS "OutJcicTxtDate"       -- 轉出JCIC文字檔日期 Decimald 8 0
          ,JOB_START_TIME      AS "CreateDate"           -- 建檔日期時間 DATE 8 0
          ,'999999'            AS "CreateEmpNo"          -- 建檔人員 VARCHAR2 6 0
          ,JOB_START_TIME      AS "LastUpdate"           -- 最後更新日期時間 DATE 8 0
          ,'999999'            AS "LastUpdateEmpNo"      -- 最後更新人員 VARCHAR2 6 0
    FROM "JcicZ056"
    WHERE "OutJcicTxtDate" > 0
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
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_JcicZ056Log_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;


/
