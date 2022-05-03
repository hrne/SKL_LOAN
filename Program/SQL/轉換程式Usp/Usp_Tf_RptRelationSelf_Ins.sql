--------------------------------------------------------
--  DDL for Procedure Usp_Tf_RptRelationSelf_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_Tf_RptRelationSelf_Ins" 
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
    DELETE FROM "RptRelationSelf";

    -- 寫入資料
    INSERT INTO "RptRelationSelf"
    SELECT "SKLRLTP".CusID                AS "CusId"               -- CusId NVARCHAR2 20 0
          ,"SKLRLTP".CusName              AS "CusName"             -- CusName NVARCHAR2 30 0
          ,"SKLRLTP".STSCD                AS "STSCD"               -- STSCD NVARCHAR2 2 0
          ,"SKLRLTP".CusCCD               AS "CusCCD"              -- CusCCD NVARCHAR2 1 0
          ,"SKLRLTP".CusSCD               AS "CusSCD"              -- CusSCD NVARCHAR2 2 0
          ,"SKLRLTP".LAW001               AS "LAW001"              -- LAW001 NVARCHAR2 1 0
          ,"SKLRLTP".LAW002               AS "LAW002"              -- LAW002 NVARCHAR2 1 0
          ,"SKLRLTP".LAW003               AS "LAW003"              -- LAW003 NVARCHAR2 1 0
          ,"SKLRLTP".LAW004               AS "LAW004"              -- LAW004 NVARCHAR2 1 0
          ,"SKLRLTP".LAW005               AS "LAW005"              -- LAW005 NVARCHAR2 1 0
          ,"SKLRLTP".LAW006               AS "LAW006"              -- LAW006 NVARCHAR2 1 0
          ,"SKLRLTP".LAW007               AS "LAW007"              -- LAW007 NVARCHAR2 1 0
          ,"SKLRLTP".LAW008               AS "LAW008"              -- LAW008 NVARCHAR2 1 0
          ,"SKLRLTP".LAW009               AS "LAW009"              -- LAW009 NVARCHAR2 1 0
          ,"SKLRLTP".LAW010               AS "LAW0010"             -- LAW0010 NVARCHAR2 1 0
          ,"SKLRLTP".Mark                 AS "Mark"                -- Mark NVARCHAR2 10 
          ,JOB_START_TIME                 AS "CreateDate"          -- 建檔日期時間 DATE 0 
          ,'999999'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 
          ,JOB_START_TIME                 AS "LastUpdate"          -- 最後更新日期時間 DATE 0 0
          ,'999999'                       AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 0
    FROM "SKLRLTP"
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
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_RptRelationSelf_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;



/
