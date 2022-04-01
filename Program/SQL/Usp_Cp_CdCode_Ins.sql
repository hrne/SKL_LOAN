CREATE OR REPLACE PROCEDURE "Usp_Cp_CdCode_Ins"
(
    -- 參數
    "EmpNo"       IN VARCHAR2  -- 修改人員
)
AS
BEGIN
    -- 防呆:若目前連線是Online就離開
    IF USER = 'ITXADMIN' THEN
      RETURN;
    END IF;

    -- 刪除資料
    EXECUTE IMMEDIATE 'TRUNCATE TABLE "CdCode" DROP STORAGE';

    INSERT INTO "CdCode" (
        "DefCode"
        , "DefType"
        , "Code"
        , "Item"
        , "Enable"
        , "EffectFlag"
        , "MinCodeLength"
        , "MaxCodeLength"
        , "CreateDate"
        , "CreateEmpNo"
        , "LastUpdate"
        , "LastUpdateEmpNo"
    )
    SELECT "DefCode"
         , "DefType"
         , "Code"
         , "Item"
         , "Enable"
         , "EffectFlag"
         , "MinCodeLength"
         , "MaxCodeLength"
         , "CreateDate"
         , "CreateEmpNo"
         , "LastUpdate"
         , "LastUpdateEmpNo"
    FROM ITXADMIN."CdCode"
    ;

    -- 例外處理
    Exception
    WHEN OTHERS THEN
    "Usp_L9_UspErrorLog_Ins"(
        'Usp_Cp_CdCode_Ins' -- UspName 預存程序名稱
      , SQLCODE -- Sql Error Code (固定值)
      , SQLERRM -- Sql Error Message (固定值)
      , dbms_utility.format_error_backtrace -- Sql Error Trace (固定值)
      , "EmpNo" -- 發動預存程序的員工編號
    );
END;