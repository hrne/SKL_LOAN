CREATE OR REPLACE PROCEDURE "Usp_L9_DailyBackup_Copy"(
    v_tbsdyf IN NUMBER,
    v_empNo IN VARCHAR2,
    JobTxSeq       IN  VARCHAR2    -- 啟動批次的交易序號
) IS
BEGIN
    -- exec "Usp_L9_DailyBackup_Copy"(20230531,'001702');
    DECLARE
        v_schemaSource VARCHAR2(20) := 'ITXADMIND'; -- DEV/SIT/UAT/PT1/PT2
        CURSOR table_cur IS
            SELECT table_name 
            FROM all_tables 
            WHERE owner = v_schemaSource; -- Change 'SCHEMA_A' to your Schema A name
        sql_str VARCHAR2(1000);
    BEGIN
        FOR table_rec in table_cur LOOP
        BEGIN
            sql_str := 'TRUNCATE TABLE "' || table_rec.table_name || '" DROP STORAGE';
            EXECUTE IMMEDIATE sql_str;

            sql_str := 'INSERT INTO "' || table_rec.table_name || 
                       '" SELECT * FROM ' || v_schemaSource || '."' || table_rec.table_name || '"';
            EXECUTE IMMEDIATE sql_str;
            COMMIT;
            
            EXCEPTION
            WHEN OTHERS THEN
            ROLLBACK;
            "Usp_L9_UspErrorLog_Ins"(
                'Usp_L9_DailyBackup_Copy' -- UspName 預存程序名稱
                , SQLCODE -- Sql Error Code (固定值)
                ,  'Table ' || table_rec.table_name
                    || ','
                    || SQLERRM -- Sql Error Message (固定值)
                , dbms_utility.format_error_backtrace -- Sql Error Trace (固定值)
                , v_empNo -- 發動預存程序的員工編號
                , JobTxSeq -- 啟動批次的交易序號
            );
            COMMIT;
            RAISE;
        END;
        END LOOP;
    END;
END;
/

