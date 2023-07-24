CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_Cp_ForeignKeyControl_Upd"
(
    -- 參數
    TBSDYF        IN NUMBER,   -- 系統營業日(西元)
    "EmpNo"       IN VARCHAR2, -- 修改人員
    "Switch"      IN NUMBER,   -- 開關(0:關;1:開)
    JobTxSeq       IN  VARCHAR2    -- 啟動批次的交易序號
)
AS
BEGIN
    -- 關閉所有ForeignKey
    -- EXEC "Usp_Cp_ForeignKeyControl_Upd"(20211230,'999999',0);
    -- 開啟所有ForeignKey
    -- EXEC "Usp_Cp_ForeignKeyControl_Upd"(20211230,'999999',1);
    DECLARE
        "vSql" VARCHAR2(3000); -- 動態SQL
        "action" VARCHAR2(10); -- enable/disable
    BEGIN
        DBMS_OUTPUT.PUT_LINE('Set action');
        "action" := 'DISABLE';

        IF "Switch" = 1 THEN
          "action" := 'ENABLE';
        END IF;
        DBMS_OUTPUT.PUT_LINE("action");
        
        FOR x
        IN (
            SELECT TABLE_NAME      AS "Tb"
                 , CONSTRAINT_NAME AS "Fk"
            FROM USER_CONSTRAINTS
            WHERE CONSTRAINT_TYPE = 'R'
            ORDER BY TABLE_NAME
        )
        LOOP
            DBMS_OUTPUT.PUT_LINE(x."Tb");
            DBMS_OUTPUT.PUT_LINE(x."Fk");
            "vSql" := 'ALTER TABLE ' 
                      || DBMS_ASSERT.ENQUOTE_NAME(x."Tb", FALSE)
                      || ' '
                      || "action" 
                      || ' CONSTRAINT ' 
                      || DBMS_ASSERT.ENQUOTE_NAME(x."Fk", FALSE);
            EXECUTE IMMEDIATE "vSql";
        END LOOP;

        -- 例外處理
        Exception
        WHEN OTHERS THEN
        "Usp_L9_UspErrorLog_Ins"(
            'Usp_Cp_ForeignKeyControl_Upd' -- UspName 預存程序名稱
          , SQLCODE -- Sql Error Code (固定值)
          , SQLERRM -- Sql Error Message (固定值)
          , dbms_utility.format_error_backtrace -- Sql Error Trace (固定值)
          , "EmpNo" -- 發動預存程序的員工編號
          , JobTxSeq -- 啟動批次的交易序號
        );
        COMMIT;
        RAISE;
    END;
END;