CREATE OR REPLACE PROCEDURE "Usp_L2_ReltMain_Ins"
(
    -- 參數
    "EmpNo" IN VARCHAR2   --批次紀錄識別碼
--     JOB_START_TIME OUT TIMESTAMP, --程式起始時間
--     JOB_END_TIME   OUT TIMESTAMP, --程式結束時間
--     INS_CNT        OUT INT        --新增資料筆數
)
AS
BEGIN
    -- exec "Usp_L2_ReltMain_Ins"('999999');

    -- 刪除舊資料
    EXECUTE IMMEDIATE 'ALTER TABLE "ReltMain" DISABLE PRIMARY KEY CASCADE';
    EXECUTE IMMEDIATE 'TRUNCATE TABLE "ReltMain" DROP STORAGE';
    EXECUTE IMMEDIATE 'ALTER TABLE "ReltMain" ENABLE PRIMARY KEY';

    DECLARE
        JOB_START_TIME DATE := SYSTIMESTAMP;
    BEGIN
        -- 寫入資料
        INSERT INTO "ReltMain"
        WITH rawData AS (
            SELECT T.CASENO                   AS "CaseNo"     -- eLoan案件編號 NUMBER(7,0) DEFAULT 0, 
                 , T.CUSTNO                   AS "CustNo"     -- 借戶人戶號 NUMBER(7,0) DEFAULT 0, 
                 , CM."CustUKey"              AS "ReltUKey"   -- 關係人客戶識別碼 VARCHAR2(32), 
                 , T.RELTCODE                 AS "ReltCode"   -- 關係 VARCHAR2(2), 
                 , T.REMARKTYPE               AS "RemarkType" -- 備註類型 NVARCHAR2(1), 
                 , T.RELTMARK                 AS "Reltmark"   -- 備註 NVARCHAR2(100), 
                 , T.FINALFG                  AS "FinalFg"    -- 最新案件記號 VARCHAR2(1), 
                 , NVL(TO_NUMBER(TO_CHAR(T.APPLDATE,'yyyymmdd')),0)
                                              AS "ApplDate"   -- 申請日期 NUMBER(8,0) DEFAULT 0, 
                 , ROW_NUMBER()
                   OVER (
                       PARTITION BY T.CASENO
                                  , T.CUSTNO
                                  , CM."CustUKey"
                       ORDER BY NVL(T.FINALFG,' ') DESC
                              , NVL(TO_NUMBER(TO_CHAR(T.APPLDATE,'yyyymmdd')),0) DESC
                   ) AS "Seq"
            FROM TPRELTMAIN T
            LEFT JOIN "CustMain" CM ON CM."CustId" = T.RELCUSTID
            WHERE NVL(CM."CustUKey",' ') != ' '
        )
        SELECT r."CaseNo"                 AS "CaseNo"          -- eLoan案件編號 NUMBER(7,0) DEFAULT 0, 
             , r."CustNo"                 AS "CustNo"          -- 借戶人戶號 NUMBER(7,0) DEFAULT 0, 
             , r."ReltUKey"               AS "ReltUKey"        -- 關係人客戶識別碼 VARCHAR2(32), 
             , r."ReltCode"               AS "ReltCode"        -- 關係 VARCHAR2(2), 
             , r."RemarkType"             AS "RemarkType"      -- 備註類型 NVARCHAR2(1), 
             , r."Reltmark"               AS "Reltmark"        -- 備註 NVARCHAR2(100), 
             , r."FinalFg"                AS "FinalFg"         -- 最新案件記號 VARCHAR2(1), 
             , r."ApplDate"               AS "ApplDate"        -- 申請日期 NUMBER(8,0) DEFAULT 0, 
             , JOB_START_TIME             AS "CreateDate"      -- 建檔日期時間 DATE 
             , SUBSTR("EmpNo",0,6)        AS "CreateEmpNo"     -- 建檔人員 VARCHAR2 6
             , JOB_START_TIME             AS "LastUpdate"      -- 最後更新日期時間 DATE 
             , SUBSTR("EmpNo",0,6)        AS "LastUpdateEmpNo" -- 最後更新人員 VARCHAR2 6
        FROM rawData r
        WHERE "Seq" = 1
        ;
    END;

    commit;

    -- 例外處理
--     Exception
--     WHEN OTHERS THEN
END;
