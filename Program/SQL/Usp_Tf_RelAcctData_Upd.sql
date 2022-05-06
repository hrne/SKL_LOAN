--------------------------------------------------------
--  DDL for Procedure Usp_Tf_RelAcctData_Upd
--------------------------------------------------------
set define off;

CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_Tf_RelAcctData_Upd" 
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

    -- 更新AchAuthLog
    MERGE INTO "AchAuthLog" T
    USING (
        WITH rawData AS (
            SELECT DISTINCT
                   "RelationId"
            FROM "AchAuthLog"
            WHERE"RelationId" != ' '
        )
        SELECT DISTINCT
               rawData."RelationId"
             , "CustMain"."CustId"
             , "CustMain"."CustName"
             , "CustMain"."Sex"
             , "CustMain"."Birthday"
        FROM rawData
        LEFT JOIN "CustMain" ON "CustMain"."CustId" = rawData."RelationId"
        WHERE "CustMain"."CustId" IS NOT NULL
    ) S
    ON (
        S."RelationId" = T."RelationId"
    )
    WHEN MATCHED THEN UPDATE
    SET T."RelAcctBirthday" = S."Birthday"
      , T."RelAcctGender" = S."Sex"
    ;

    -- 更新AchAuthLogHistory
    MERGE INTO "AchAuthLogHistory" T
    USING (
        WITH rawData AS (
            SELECT DISTINCT
                   "RelationId" 
            FROM "AchAuthLogHistory" 
            WHERE "RelationId" != ' '
        )
        SELECT DISTINCT
               rawData."RelationId"
             , "CustMain"."CustId"
             , "CustMain"."CustName"
             , "CustMain"."Sex"
             , "CustMain"."Birthday"
        FROM rawData
        LEFT JOIN "CustMain" ON "CustMain"."CustId" = rawData."RelationId"
        WHERE "CustMain"."CustId" IS NOT NULL
    ) S
    ON (
        S."RelationId" = T."RelationId"
    )
    WHEN MATCHED THEN UPDATE
    SET T."RelAcctBirthday" = S."Birthday"
      , T."RelAcctGender" = S."Sex"
    ;

    -- 更新BankDeductDtl
    MERGE INTO "BankDeductDtl" T
    USING (
        WITH rawData AS (
            SELECT DISTINCT
                   "RelCustId"
            FROM "BankDeductDtl"
            WHERE "RelCustId" != ' '
        )
        SELECT DISTINCT
               rawData."RelCustId"
             , "CustMain"."CustId"
             , "CustMain"."CustName"
             , "CustMain"."Sex"
             , "CustMain"."Birthday"
        FROM rawData
        LEFT JOIN "CustMain" ON "CustMain"."CustId" = rawData."RelCustId"
        WHERE "CustMain"."CustId" IS NOT NULL
    ) S
    ON (
        S."RelCustId" = T."RelCustId"
    )
    WHEN MATCHED THEN UPDATE
    SET T."RelAcctBirthday" = S."Birthday"
      , T."RelAcctGender" = S."Sex"
    ;

    -- 更新PostAuthLog
    MERGE INTO "PostAuthLog" T
    USING (
        WITH rawData AS (
            SELECT DISTINCT
                   "RelationId"
            FROM "PostAuthLog"
            WHERE "RelationId" != ' '
        )
        SELECT DISTINCT
               rawData."RelationId"
             , "CustMain"."CustId"
             , "CustMain"."CustName"
             , "CustMain"."Sex"
             , "CustMain"."Birthday"
        FROM rawData
        LEFT JOIN "CustMain" ON "CustMain"."CustId" = rawData."RelationId"
        WHERE "CustMain"."CustId" IS NOT NULL
    ) S
    ON (
        S."RelationId" = T."RelationId"
    )
    WHEN MATCHED THEN UPDATE
    SET T."RelAcctBirthday" = S."Birthday"
      , T."RelAcctGender" = S."Sex"
    ;

    -- 記錄程式結束時間
    JOB_END_TIME := SYSTIMESTAMP;

    commit;

    END;
    -- 例外處理
    Exception
    WHEN OTHERS THEN
    ERROR_MSG := SQLERRM || CHR(13) || CHR(10) || dbms_utility.format_error_backtrace;
END;

/