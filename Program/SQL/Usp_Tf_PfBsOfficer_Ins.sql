--------------------------------------------------------
--  DDL for Procedure Usp_Tf_PfBsOfficer_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_Tf_PfBsOfficer_Ins" 
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
    EXECUTE IMMEDIATE 'ALTER TABLE "PfBsOfficer" DISABLE PRIMARY KEY CASCADE';
    EXECUTE IMMEDIATE 'TRUNCATE TABLE "PfBsOfficer" DROP STORAGE';
    EXECUTE IMMEDIATE 'ALTER TABLE "PfBsOfficer" ENABLE PRIMARY KEY';

    -- 寫入資料
    INSERT INTO "PfBsOfficer" (
        "WorkMonth"           -- 年月份 DECIMAL 6 0
      , "EmpNo"               -- 員工代號 VARCHAR2 6 0
      , "Fullname"            -- 員工姓名 VARCHAR2 40 0
      , "AreaCode"            -- 區域中心 VARCHAR2 6 0
      , "AreaItem"            -- 中心中文 NVARCHAR2 6 0 -- 待刪除
      , "DeptCode"            -- 部室代號 VARCHAR2 6 0
      , "DepItem"          -- 部室中文 NVARCHAR2 12 0
      , "DistCode"            -- 區部代號 VARCHAR2 6 0
      , "DistItem"            -- 區部中文 NVARCHAR2 30 0
      , "StationName"         -- 駐在地 NVARCHAR2 30 0
      , "GoalAmt"             -- 目標金額 DECIMAL 16 2
      , "SmryGoalAmt"         -- 累計目標金額 DECIMAL 16 2
      , "CreateDate"          -- 建檔日期時間 DATE  
      , "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 
      , "LastUpdate"          -- 最後更新日期時間 DATE  
      , "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 
    )
    SELECT "LN$YG5P"."ADTYMT"             AS "WorkMonth"           -- 年月份 DECIMAL 6 0
          ,"LN$YG5P"."EMPCOD"             AS "EmpNo"               -- 員工代號 VARCHAR2 6 0
          ,"LN$YG5P"."EMPNAM"             AS "Fullname"            -- 員工姓名 VARCHAR2 40 0
          ,"LN$YG5P"."BCMCOD"             AS "AreaCode"            -- 區域中心 VARCHAR2 6 0
          ,CASE -- 2021-02-04 新增欄位
             WHEN "LN$YG5P"."BCMCOD" = '10HC00' THEN '北部區域中心'
             WHEN "LN$YG5P"."BCMCOD" = '10HL00' THEN '南部區域中心'
             WHEN "LN$YG5P"."BCMCOD" = '10HJ00' THEN '中部區域中心'
           ELSE '' END                    AS "AreaItem"            -- 中心中文 NVARCHAR2 6 0 -- 待刪除
          ,"LN$YG5P"."BCMDPT"             AS "DeptCode"            -- 部室代號 VARCHAR2 6 0
          ,"LN$YG5P"."UNTDVC"             AS "DepartItem"          -- 部室中文 NVARCHAR2 12 0
          ,S1."DistCode"                  AS "DistCode"            -- 區部代號 VARCHAR2 6 0
          ,"LN$YG5P"."UNTBRC"             AS "DistItem"            -- 區部中文 NVARCHAR2 30 0
          -- 2023-10-03 Wei FROM SKL 葛經理 #F05
          ,"LN$YG5P"."LOCPOT"             AS "StationName"         -- 駐在地 NVARCHAR2 30 0
          ,"LN$YG5P"."GALFLA"             AS "GoalAmt"             -- 目標金額 DECIMAL 16 2
          ,"LN$YG5P"."TGOALAMT"           AS "SmryGoalAmt"         -- 累計目標金額 DECIMAL 16 2
          ,JOB_START_TIME                 AS "CreateDate"          -- 建檔日期時間 DATE  
          ,'999999'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 
          ,JOB_START_TIME                 AS "LastUpdate"          -- 最後更新日期時間 DATE  
          ,'999999'                       AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 
    FROM "LN$YG5P"
    LEFT JOIN (SELECT DISTINCT
                      "DistCode"
                     ,"DistItem"
               FROM "CdBcm"
               WHERE "DistCode" IS NOT NULL
              ) S1 ON TRIM(S1."DistItem") = TRIM("LN$YG5P"."UNTBRC")
                   AND TRIM("LN$YG5P"."UNTBRC") IS NOT NULL
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
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_PfBsOfficer_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;




/
