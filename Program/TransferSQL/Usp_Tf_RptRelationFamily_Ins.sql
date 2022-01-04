--------------------------------------------------------
--  DDL for Procedure Usp_Tf_RptRelationFamily_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "Usp_Tf_RptRelationFamily_Ins" 
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
    DELETE FROM "RptRelationFamily";

    -- 寫入資料
    INSERT INTO "RptRelationFamily"
    SELECT "SKLRLBP".CusID                AS "CusId"               -- CusId NVARCHAR2 20 0
          ,"SKLRLBP".CusSCD               AS "CusSCD"              -- CusSCD NVARCHAR2 2 0
          ,"SKLRLBP".RlbID                AS "RlbID"               -- RlbID NVARCHAR2 20 0
          ,"SKLRLBP".RlbName              AS "RlbName"             -- RlbName NVARCHAR2 40 0
          ,"SKLRLBP".FamilyCD             AS "FamilyCD"            -- FamilyCD NVARCHAR2 3 0
          ,"SKLRLBP".LAW001               AS "LAW001"              -- LAW001 NVARCHAR2 1 0
          ,"SKLRLBP".LAW002               AS "LAW002"              -- LAW002 NVARCHAR2 1 0
          ,"SKLRLBP".LAW003               AS "LAW003"              -- LAW003 NVARCHAR2 1 0
          ,"SKLRLBP".LAW004               AS "LAW004"              -- LAW004 NVARCHAR2 1 0
          ,"SKLRLBP".LAW005               AS "LAW005"              -- LAW005 NVARCHAR2 1 0
          ,"SKLRLBP".LAW006               AS "LAW006"              -- LAW006 NVARCHAR2 1 0
          ,"SKLRLBP".LAW007               AS "LAW007"              -- LAW007 NVARCHAR2 1 0
          ,"SKLRLBP".LAW008               AS "LAW008"              -- LAW008 NVARCHAR2 1 0
          ,"SKLRLBP".LAW009               AS "LAW009"              -- LAW009 NVARCHAR2 1 0
          ,"SKLRLBP".LAW010               AS "LAW0010"             -- LAW0010 NVARCHAR2 1 0
          ,"SKLRLBP".RlbCusCCD            AS "RlbCusCCD"           -- RlbCusCCD NVARCHAR2 1 0
          ,JOB_START_TIME                 AS "CreateDate"          -- 建檔日期時間 DATE 0 
          ,'999999'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 
          ,JOB_START_TIME                 AS "LastUpdate"          -- 最後更新日期時間 DATE 0 
          ,'999999'                       AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 0
    FROM "SKLRLBP"
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
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_RptRelationFamily_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;


/
