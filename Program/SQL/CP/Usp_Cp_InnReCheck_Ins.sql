CREATE OR REPLACE PROCEDURE "Usp_Cp_InnReCheck_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "InnReCheck" DROP STORAGE';

  INSERT INTO "InnReCheck" (
    "YearMonth",
    "ConditionCode",
    "CustNo",
    "FacmNo",
    "ReCheckCode",
    "FollowMark",
    "ReChkYearMonth",
    "DrawdownDate",
    "LoanBal",
    "Evaluation",
    "CustTypeItem",
    "UsageItem",
    "CityItem",
    "ReChkUnit",
    "SpecifyFg",
    "Remark",
    "TraceMonth",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "YearMonth",
    "ConditionCode",
    "CustNo",
    "FacmNo",
    "ReCheckCode",
    "FollowMark",
    "ReChkYearMonth",
    "DrawdownDate",
    "LoanBal",
    "Evaluation",
    "CustTypeItem",
    "UsageItem",
    "CityItem",
    "ReChkUnit",
    "SpecifyFg",
    "Remark",
    "TraceMonth",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."InnReCheck";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_InnReCheck_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;