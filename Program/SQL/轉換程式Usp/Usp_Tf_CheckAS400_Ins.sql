--------------------------------------------------------
--  DDL for Procedure Usp_Tf_CheckAS400_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_Tf_CheckAS400_Ins" 
(
    -- 參數
    TBSDYF        IN NUMBER,   -- 系統營業日(西元)
    "EmpNo"       IN VARCHAR2  -- 修改人員
)
AS
BEGIN
    -- exec "Usp_Tf_CheckAS400_Ins"(20211228,'999999');
    DECLARE
        "vSql" VARCHAR2(3000); -- 動態SQL
        "nCheck" NUMBER(10); -- 表是否存在
        "nCount" NUMBER(10); -- 表計數
        "nLastDate" NUMBER(8); -- 表最後日期
        "vTableName" VARCHAR2(3000); -- TableName
        "vColumnName" VARCHAR2(3000); -- ColumnName
    BEGIN
        DELETE FROM "AS400_MAPPING_TABLE_AFTER";

        DBMS_OUTPUT.ENABLE();

        -- 複製Table
        FOR x
        IN (
            SELECT ORACLE_TABLE AS TB -- 資料表名
                 , TABLE_NAME   AS CH -- 資料表中文
            FROM AS400_MAPPING_TABLE
            WHERE AS400_TABLE IS NOT NULL
              AND SUBSTR(ORACLE_TABLE,0,3) IN ('DAT','TBL','REM','SAV')
        )
        LOOP
            "nCount" := 0;
            "nLastDate" := 0;
            "vTableName" := x.TB;
            "vColumnName" := NULL;

            SELECT COUNT(1) 
            INTO "nCheck"
            FROM USER_TABLES 
            WHERE TABLE_NAME = "vTableName"
            ;

            IF "nCheck" = 0  THEN 
                "nCount" := -1;
            ELSE
                "vSql" := 'SELECT COUNT(*) as CNT FROM ' || "vTableName" ;
                EXECUTE IMMEDIATE "vSql" INTO "nCount";

                -- 針對特定TABLE 查最大日期值
                CASE
                    WHEN "vTableName" = 'DAT_LA$TRXP' THEN -- 交易內容檔
                        "vColumnName" := 'TRXDAT';
                    WHEN "vTableName" = 'DAT_LA$LMSP' THEN -- 撥款檔
                        "vColumnName" := 'LMSLLD';
                    WHEN "vTableName" = 'DAT_LA$APLP' THEN -- 額度檔
                        "vColumnName" := 'APLFSD';
                    WHEN "vTableName" = 'DAT_LA$CASP' THEN -- 案件申請檔
                        "vColumnName" := 'CASIDT';
                ELSE
                    "vColumnName" := NULL;
                END CASE;

                IF NVL("vColumnName",' ') != ' ' THEN
                    "vSql" := 'SELECT MAX(' || "vColumnName" || ') as LAST_DATE FROM ' || "vTableName" ;
                    EXECUTE IMMEDIATE "vSql" INTO "nLastDate";
                END IF;
            END IF;

            INSERT INTO "AS400_MAPPING_TABLE_AFTER" (
                  "ORACLE_TABLE" -- VARCHAR2(200)
                , "COUNTS" -- DECIMAL(16)
                , "LAST_DATE" -- DECIMAL(8)
            ) VALUES (
                  "vTableName"
                , "nCount"
                , "nLastDate"
            )
            ;
        END LOOP;

        EXCEPTION
        WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE("vTableName");
        RAISE;
    END;
END;

/
