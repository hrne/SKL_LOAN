CREATE OR REPLACE PROCEDURE "Usp_Cp_JcicB085_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "JcicB085" DROP STORAGE';

  INSERT INTO "JcicB085" (
    "DataYM",
    "DataType",
    "RenewYM",
    "CustId",
    "BefBankItem",
    "BefBranchItem",
    "Filler6",
    "BefAcctNo",
    "AftBankItem",
    "AftBranchItem",
    "Filler10",
    "AftAcctNo",
    "Filler12",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "DataYM",
    "DataType",
    "RenewYM",
    "CustId",
    "BefBankItem",
    "BefBranchItem",
    "Filler6",
    "BefAcctNo",
    "AftBankItem",
    "AftBranchItem",
    "Filler10",
    "AftAcctNo",
    "Filler12",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."JcicB085";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_JcicB085_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;