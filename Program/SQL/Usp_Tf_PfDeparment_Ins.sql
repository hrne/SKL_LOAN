--------------------------------------------------------
--  DDL for Procedure Usp_Tf_PfDeparment_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_Tf_PfDeparment_Ins" 
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
    EXECUTE IMMEDIATE 'ALTER TABLE "PfDeparment" DISABLE PRIMARY KEY CASCADE';
    EXECUTE IMMEDIATE 'TRUNCATE TABLE "PfDeparment" DROP STORAGE';
    EXECUTE IMMEDIATE 'ALTER TABLE "PfDeparment" ENABLE PRIMARY KEY';

    -- 寫入資料
    INSERT INTO "PfDeparment" (
        "UnitCode"            -- 單位代號 VARCHAR2 6 0
      , "DistCode"            -- 區部代號 VARCHAR2 6 
      , "DeptCode"            -- 部室代號 VARCHAR2 6 0
      , "EmpNo"               -- 員工代號 VARCHAR2 6 0
      , "UnitItem"            -- 單位中文 NVARCHAR2 8 0
      , "DistItem"            -- 區部中文 NVARCHAR2 8 0
      , "DeptItem"            -- 部室中文 NVARCHAR2 8 0
      , "DirectorCode"        -- 處長主任別 VARCHAR2 1 0
      , "EmpName"             -- 員工姓名 NVARCHAR2 8 0
      , "DepartOfficer"       -- 專員姓名 NVARCHAR2 8 0
      , "GoalCnt"             -- 目標件數 DECIMAL 4 0
      , "SumGoalCnt"          -- 累計目標件數 DECIMAL 16 2
      , "GoalAmt"             -- 目標金額 DECIMAL 16 2
      , "SumGoalAmt"          -- 累計目標金額 DECIMAL 16 2
      , "CreateDate"          -- 建檔日期時間 DATE 8 0
      , "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 0
      , "LastUpdate"          -- 最後更新日期時間 DATE 8 0
      , "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 0
    )
    SELECT NVL("LA$QHCP"."BCMCOD",'      ')
                                          AS "UnitCode"            -- 單位代號 VARCHAR2 6 0
          ,NVL("LA$QHCP"."UNTBRN",'      ')
                                          AS "DistCode"            -- 區部代號 VARCHAR2 6 0
          ,NVL("LA$QHCP"."BCMDPT",'      ')
                                          AS "DeptCode"            -- 部室代號 VARCHAR2 6 0
          ,NVL("LA$QHCP"."EMPCOD",'      ')
                                          AS "EmpNo"               -- 員工代號 VARCHAR2 6 0
          ,TRIM(TO_SINGLE_BYTE(NVL("LA$QHCP"."UNTUTC",' ')))
                                          AS "UnitItem"            -- 單位中文 NVARCHAR2 8 0
          ,TRIM(TO_SINGLE_BYTE(NVL("LA$QHCP"."UNTBRC",' ')))
                                          AS "DistItem"            -- 區部中文 NVARCHAR2 8 0
          ,TRIM(TO_SINGLE_BYTE(NVL("LA$QHCP"."UNTDVC",' ')))
                                          AS "DeptItem"            -- 部室中文 NVARCHAR2 8 0
          ,TRIM(TO_SINGLE_BYTE(NVL("LA$QHCP"."MGTAGT",' ')))
                                          AS "DirectorCode"        -- 處長主任別 VARCHAR2 1 0
          ,TRIM(TO_SINGLE_BYTE(NVL("LA$QHCP"."EMPNAM",' ')))
                                          AS "EmpName"             -- 員工姓名 NVARCHAR2 8 0
          ,TRIM(TO_SINGLE_BYTE(NVL("LA$QHCP"."EMPNAMP",' ')))
                                          AS "DepartOfficer"       -- 專員姓名 NVARCHAR2 8 0
          ,"LA$QHCP"."GOALNO"             AS "GoalCnt"             -- 目標件數 DECIMAL 4 0
          ,"LA$QHCP"."TGOALNO"            AS "SumGoalCnt"          -- 累計目標件數 DECIMAL 16 2
          ,"LA$QHCP"."GOALAMT"            AS "GoalAmt"             -- 目標金額 DECIMAL 16 2
          ,"LA$QHCP"."TGOALAMT"           AS "SumGoalAmt"          -- 累計目標金額 DECIMAL 16 2
        --   ,TRUNC("LA$QHCP"."ADTYMD"/100)+191100 -- 10/20 Wei 修改存西元年
        --                                   AS "WorkMonth"           -- 工作年月 DECIMAL 6 0
          ,JOB_START_TIME                 AS "CreateDate"          -- 建檔日期時間 DATE 8 0
          ,'999999'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 0
          ,JOB_START_TIME                 AS "LastUpdate"          -- 最後更新日期時間 DATE 8 0
          ,'999999'                       AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 0
    FROM "LA$QHCP"
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
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_PfDeparment_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;



/
