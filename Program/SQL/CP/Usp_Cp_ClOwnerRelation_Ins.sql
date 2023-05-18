CREATE OR REPLACE PROCEDURE "Usp_Cp_ClOwnerRelation_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "ClOwnerRelation" DROP STORAGE';

  INSERT INTO "ClOwnerRelation" (
    "CreditSysNo",
    "CustNo",
    "OwnerCustUKey",
    "OwnerRelCode",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "CreditSysNo",
    "CustNo",
    "OwnerCustUKey",
    "OwnerRelCode",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."ClOwnerRelation";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_ClOwnerRelation_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;