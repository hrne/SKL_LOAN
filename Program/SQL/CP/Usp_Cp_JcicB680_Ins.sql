CREATE OR REPLACE PROCEDURE "Usp_Cp_JcicB680_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "JcicB680" DROP STORAGE';

  INSERT INTO "JcicB680" (
    "DataYM",
    "BankItem",
    "BranchItem",
    "TranCode",
    "CustId",
    "CustIdErr",
    "CustNo",
    "FacmNo",
    "BormNo",
    "Filler6",
    "Amt",
    "JcicDataYM",
    "Filler9",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "DataYM",
    "BankItem",
    "BranchItem",
    "TranCode",
    "CustId",
    "CustIdErr",
    "CustNo",
    "FacmNo",
    "BormNo",
    "Filler6",
    "Amt",
    "JcicDataYM",
    "Filler9",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."JcicB680";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_JcicB680_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;