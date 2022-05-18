CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_L6_CdBcm_Ins"
(
    -- 參數
    "EmpNo" IN VARCHAR2   --批次紀錄識別碼
--     JOB_START_TIME OUT TIMESTAMP, --程式起始時間
--     JOB_END_TIME   OUT TIMESTAMP, --程式結束時間
--     INS_CNT        OUT INT        --新增資料筆數
)
AS
BEGIN

-- exec "Usp_L6_CdBcm_Ins"('999999');

    -- 筆數預設0
--     INS_CNT:=0;
    -- 記錄程式起始時間
--     JOB_START_TIME := SYSTIMESTAMP;

    -- 刪除舊資料
    EXECUTE IMMEDIATE 'ALTER TABLE "CdBcm" DISABLE PRIMARY KEY CASCADE';
    EXECUTE IMMEDIATE 'TRUNCATE TABLE "CdBcm" DROP STORAGE';
    EXECUTE IMMEDIATE 'ALTER TABLE "CdBcm" ENABLE PRIMARY KEY';

    -- 寫入資料
    INSERT INTO "CdBcm"
    SELECT S1."CenterCode"            AS "UnitCode"        -- 單位代號 VARCHAR2 6
          ,S1."CenterCodeName"        AS "UnitItem"        -- 單位名稱 NVARCHAR2 20
          ,S1."CenterCode2"           AS "DeptCode"        -- 部室代號 VARCHAR2 6
          ,S1."CenterCode2Name"       AS "DeptItem"        -- 部室名稱 NVARCHAR2 20
          ,S1."CenterCode1"           AS "DistCode"        -- 區部代號 VARCHAR2 6
          ,S1."CenterCode1Name"       AS "DistItem"        -- 區部名稱 NVARCHAR2 20
          ,''                         AS "UnitManager"     -- 單位經理代號 VARCHAR2 6
          ,''                         AS "DeptManager"     -- 部室經理代號 VARCHAR2 6
          ,''                         AS "DistManager"     -- 區部經理代號 VARCHAR2 6
          ,''                         AS "ShortDeptItem"   -- 部室名稱簡寫 VARCHAR2 6
          ,''                         AS "ShortDistItem"   -- 區部名稱簡寫 VARCHAR2 6
          ,SYSTIMESTAMP               AS "CreateDate"      -- 建檔日期時間 DATE 
          ,SUBSTR("EmpNo",0,6)        AS "CreateEmpNo"     -- 建檔人員 VARCHAR2 6
          ,SYSTIMESTAMP               AS "LastUpdate"      -- 最後更新日期時間 DATE 
          ,SUBSTR("EmpNo",0,6)        AS "LastUpdateEmpNo" -- 最後更新人員 VARCHAR2 6
          ,'Y'                        AS "Enable"          -- 啟用記號 VARCHAR2 1
    FROM (SELECT "CenterCode"                                --單位代號
                ,MAX("CenterCodeName")  AS "CenterCodeName"  --單位名稱
                ,MAX("CenterCode1")     AS "CenterCode1"     --區部代號
                ,MAX("CenterCode1Name") AS "CenterCode1Name" --區部名稱
                ,MAX("CenterCode2")     AS "CenterCode2"     --部室代號
                ,MAX("CenterCode2Name") AS "CenterCode2Name" --部室名稱
          FROM (SELECT "CenterCode"        --單位代號
                      ,"CenterCodeName"    --單位名稱
                      ,"CenterCode1"       --區部代號
                      ,"CenterCode1Name"   --區部名稱
                      ,"CenterCode2"       --部室代號
                      ,"CenterCode2Name"   --部室名稱
                FROM "CdEmp"
                WHERE "CenterCode" IS NOT NULL
                UNION ALL
                SELECT "CenterCode1"        --區部代號
                      ,"CenterCode1Name"    --區部名稱
                      ,NULL
                      ,NULL
                      ,NULL
                      ,NULL
                FROM "CdEmp"
                WHERE "CenterCode1" IS NOT NULL
                UNION ALL
                SELECT "CenterCode2"        --部室代號
                      ,"CenterCode2Name"    --部室名稱
                      ,NULL
                      ,NULL
                      ,NULL
                      ,NULL
                FROM "CdEmp"
                WHERE "CenterCode2" IS NOT NULL
               ) S0
          GROUP BY "CenterCode"        --單位代號
         ) S1
    ;

    -- 記錄寫入筆數
--     INS_CNT := INS_CNT + sql%rowcount;

    -- 寫入單位經理代號 改用DirectorId,找出最上層主管
    -- *** DirectorId的資料為自己時,即為最上層主管
    MERGE INTO "CdBcm" T1 USING (
        SELECT S1."UnitCode"   --單位代號
            --   ,S1."UnitItem"   --單位名稱
              ,S2."EmployeeNo" --電腦編號
            --   ,S2."Fullname"   --姓名
            --   ,S2."AgCurInd"   --現職指示碼
              ,ROW_NUMBER() OVER (PARTITION BY S1."UnitCode" ORDER BY S2."AgPost") AS "Seq"
        FROM "CdBcm" S1
        LEFT JOIN "CdEmp" S2 ON S2."CenterCode" = S1."UnitCode"
                            -- 2021-04-07 AdministratId 改用 DirectorId 比對過結果一致
                            AND SUBSTR(S2."DirectorId",0,10) = SUBSTR(S2."AgentId",0,10)
                            AND S2."AgCurInd" = 'Y'
        WHERE NVL(S2."EmployeeNo",' ') <> ' '
    ) T2 
    ON (T2."UnitCode" = T1."UnitCode"
        AND T2."Seq" = 1)
    WHEN MATCHED THEN UPDATE SET
    T1."UnitManager" = T2."EmployeeNo"
    ;

    -- 寫入部室經理代號、區部經理代號
    MERGE INTO "CdBcm" T1 USING (
        SELECT S1."UnitCode"
              ,S1."UnitItem"
              ,S1."DeptCode"
              ,S1."DeptItem"
              ,S1."DistCode"
              ,S1."DistItem"
              ,S2."UnitCode"    AS "TmpDeptCode"
              ,S2."UnitManager" AS "DeptManager"
              ,S3."UnitCode"    AS "TmpDistCode"
              ,S3."UnitManager" AS "DistManager"
        FROM "CdBcm" S1
        LEFT JOIN (SELECT "UnitCode"
                         ,"UnitManager"
                   FROM "CdBcm"
                  ) S2 ON S2."UnitCode" = S1."DeptCode"
        LEFT JOIN (SELECT "UnitCode"
                         ,"UnitManager"
                   FROM "CdBcm"
                  ) S3 ON S3."UnitCode" = S1."DistCode"
    ) T2 
    ON (T2."UnitCode" = T1."UnitCode")
    WHEN MATCHED THEN UPDATE SET
     T1."DeptManager" = T2."DeptManager"
    ,T1."DistManager" = T2."DistManager"
    ;
    -- 記錄程式結束時間
--     JOB_END_TIME := SYSTIMESTAMP;

    commit;

    -- 例外處理
    Exception
    WHEN OTHERS THEN
    "Usp_L9_UspErrorLog_Ins"(
        'Usp_L6_CdBcm_Ins' -- UspName 預存程序名稱
      , SQLCODE -- Sql Error Code (固定值)
      , SQLERRM -- Sql Error Message (固定值)
      , dbms_utility.format_error_backtrace -- Sql Error Trace (固定值)
      , "EmpNo" -- 發動預存程序的員工編號
    );
END;
