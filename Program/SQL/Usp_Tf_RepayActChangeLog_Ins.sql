CREATE OR REPLACE PROCEDURE "Usp_Tf_RepayActChangeLog_Ins" 
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
    EXECUTE IMMEDIATE 'ALTER TABLE "RepayActChangeLog" DISABLE PRIMARY KEY CASCADE';
    EXECUTE IMMEDIATE 'TRUNCATE TABLE "RepayActChangeLog" DROP STORAGE';
    EXECUTE IMMEDIATE 'ALTER TABLE "RepayActChangeLog" ENABLE PRIMARY KEY';

    -- 寫入資料
    INSERT INTO "RepayActChangeLog" (
        "LogNo"
      , "CustNo"
      , "FacmNo"
      , "RepayCode"
      , "RepayBank"
      , "PostDepCode"
      , "RepayAcct"
      , "Status"
      , "RelDy"
      , "RelTxseq"
      , "CreateDate"
      , "CreateEmpNo"
      , "LastUpdate"
      , "LastUpdateEmpNo"
    )
    SELECT "RepayActChangeLog_SEQ".nextval AS "LogNo"
         , F."CustNo"                      AS "CustNo"
         , F."FacmNo"                      AS "FacmNo"
         , F."RepayCode"                   AS "RepayCode"
         , A."RepayBank"                   AS "RepayBank"
         , A."PostDepCode"                 AS "PostDepCode"
         , A."RepayAcct"                   AS "RepayAcct"
         , A."Status"                      AS "Status"
         , 0                               AS "RelDy"
         , ''                              AS "RelTxseq"
         , null                            AS "CreateDate"
         , '999999'                        AS "CreateEmpNo"
         , null                            AS "LastUpdate"
         , '999999'                        AS "LastUpdateEmpNo"
    FROM "FacMain" F
    LEFT JOIN "BankAuthAct" A
      ON A."CustNo" = F."CustNo"
      AND A."FacmNo" = F."FacmNo"
    WHERE A."AuthType" IN ('00','01')
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
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_RepayActChangeLog_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;