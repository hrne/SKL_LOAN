CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_Tf_TfPatchDifficultCharacter_Upd"
(
    -- 參數 
    JOB_START_TIME OUT TIMESTAMP, --程式起始時間 
    JOB_END_TIME   OUT TIMESTAMP, --程式結束時間 
    INS_CNT        OUT INT,       --新增資料筆數 
    ERROR_MSG      OUT VARCHAR2   --錯誤訊息 
)
AS 
    v_sql_statement varchar2(2000 char);
BEGIN
    -- 筆數預設0 
    INS_CNT:=0; 
    -- 記錄程式起始時間 
    JOB_START_TIME := SYSTIMESTAMP; 

    -- 讀取並執行
    FOR r IN (SELECT "PatchScript" FROM "TfPatchDifficultCharacter")
    LOOP
        v_sql_statement := r."PatchScript";
        EXECUTE IMMEDIATE v_sql_statement;
    END LOOP;

    -- 記錄程式結束時間 
    JOB_END_TIME := SYSTIMESTAMP; 
 
    commit; 
 
    -- 例外處理 
    Exception 
    WHEN OTHERS THEN 
    ERROR_MSG := SQLERRM || CHR(13) || CHR(10) || dbms_utility.format_error_backtrace; 
END;


