CREATE OR REPLACE PROCEDURE "Usp_Cp_LoanIfrs9Gp_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "LoanIfrs9Gp" DROP STORAGE';

  INSERT INTO "LoanIfrs9Gp" (
    "DataYM",
    "CustNo",
    "CustId",
    "FacmNo",
    "ApplNo",
    "BormNo",
    "CustKind",
    "Status",
    "OvduDate",
    "OriRating",
    "OriModel",
    "Rating",
    "Model",
    "OvduDays",
    "Stage1",
    "Stage2",
    "Stage3",
    "Stage4",
    "Stage5",
    "PdFlagToD",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "DataYM",
    "CustNo",
    "CustId",
    "FacmNo",
    "ApplNo",
    "BormNo",
    "CustKind",
    "Status",
    "OvduDate",
    "OriRating",
    "OriModel",
    "Rating",
    "Model",
    "OvduDays",
    "Stage1",
    "Stage2",
    "Stage3",
    "Stage4",
    "Stage5",
    "PdFlagToD",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."LoanIfrs9Gp";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_LoanIfrs9Gp_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;