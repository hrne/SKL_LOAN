CREATE OR REPLACE PROCEDURE "Usp_Cp_BankRelationCompany_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "BankRelationCompany" DROP STORAGE';

  INSERT INTO "BankRelationCompany" (
    "CustName",
    "CustId",
    "CompanyId",
    "LAW001",
    "LAW002",
    "LAW003",
    "LAW005",
    "LAW008",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "CustName",
    "CustId",
    "CompanyId",
    "LAW001",
    "LAW002",
    "LAW003",
    "LAW005",
    "LAW008",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."BankRelationCompany";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_BankRelationCompany_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;