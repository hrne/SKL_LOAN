--------------------------------------------------------
--  DDL for Procedure Usp_Tf_PfCoOfficer_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "Usp_Tf_PfCoOfficer_Ins" 
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
    EXECUTE IMMEDIATE 'ALTER TABLE "PfCoOfficer" DISABLE PRIMARY KEY CASCADE';
    EXECUTE IMMEDIATE 'TRUNCATE TABLE "PfCoOfficer" DROP STORAGE';
    EXECUTE IMMEDIATE 'ALTER TABLE "PfCoOfficer" ENABLE PRIMARY KEY';

    -- 寫入資料
    INSERT INTO "PfCoOfficer"
    SELECT EMCP."EMPCOD"                    AS "EmpNo"               -- 員工代號 VARCHAR2 6 0
         , EMCP."CLSDAT"                    AS "EffectiveDate"       -- 生效日期 DecimalD 8 0
         , 29101231                         AS "IneffectiveDate"     -- 停效日期 DecimalD 8 0
         , CDE."CenterCode"                 AS "AreaCode"            -- 單位代號 VARCHAR2 6 0
         , CDE."CenterCode1"                AS "DistCode"            -- 區部代號 VARCHAR2 6 0
         , CDE."CenterCode2"                AS "DeptCode"            -- 部室代號 VARCHAR2 6 0
         , SUBSTR(TO_NCHAR(CDE."CenterCodeName"),0,20)
                                            AS "AreaItem"            -- 單位中文 NVARCHAR2 20 0
         , SUBSTR(TO_NCHAR(CDE."CenterCode1Name"),0,20)
                                            AS "DistItem"            -- 區部中文 NVARCHAR2 20 0
         , SUBSTR(TO_NCHAR(CDE."CenterCode2Name"),0,20)
                                            AS "DeptItem"            -- 部室中文 NVARCHAR2 20 0
         , EMCP."EMPCLS"                    AS "EmpClass"            -- 協辦等級 VARCHAR2 1 0
         , NVL(EMCP."CLSTST",' ')           AS "ClassPass"           -- 初階授信通過 VARCHAR2 1 0
         , JOB_START_TIME                   AS "CreateDate"          -- 建檔日期時間 DATE  
         , '999999'                         AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 
         , JOB_START_TIME                   AS "LastUpdate"          -- 最後更新日期時間 DATE  
         , '999999'                         AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 
    FROM "TB$EMCP" EMCP
    LEFT JOIN "CdEmp" CDE ON CDE."EmployeeNo" = EMCP."EMPCOD"
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
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_PfCoOfficer_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;




/
