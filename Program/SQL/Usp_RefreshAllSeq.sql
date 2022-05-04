--------------------------------------------------------
--  DDL for Procedure Usp_RefreshAllSeq
--------------------------------------------------------
set define off;

CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_RefreshAllSeq" 
AS
    MAXVALUE  NUMBER := 0;
    CURRVALUE  NUMBER := 0;
    "vSql" VARCHAR2(3000); -- 動態SQL
BEGIN
    -- 使用範例
    -- EXEC "Usp_RefreshAllSeq"();
    FOR x
    IN (
        SELECT SEQUENCE_NAME AS SQ
             , SUBSTR(SEQUENCE_NAME, 1, INSTR(SEQUENCE_NAME, '_SEQ') - 1) AS TB
        FROM USER_SEQUENCES
    )
    LOOP
        SELECT COUNT(*)
        INTO MAXVALUE
        FROM USER_TAB_COLUMNS
        WHERE TABLE_NAME = x.TB
          AND COLUMN_NAME = 'LogNo'
        ;

        -- 檢核欄位是否存在
        IF MAXVALUE = 0
        THEN CONTINUE;
        END IF;

        -- 取表內目前最大號碼
        EXECUTE IMMEDIATE 'select MAX(NVL("LogNo",0)) from "'
                          || x.TB
                          || '"'
        INTO MAXVALUE
        ;

        LOOP
            -- 取SEQ紀錄的下一號
            EXECUTE IMMEDIATE 'select "'
                              || x.SQ
                              || '".NEXTVAL from DUAL '
            INTO CURRVALUE;

            -- 比較
            IF CURRVALUE >= MAXVALUE
            THEN EXIT;
            END IF;
        END LOOP;
    END LOOP;
END;

/
