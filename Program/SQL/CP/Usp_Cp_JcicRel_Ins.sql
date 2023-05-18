CREATE OR REPLACE PROCEDURE "Usp_Cp_JcicRel_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "JcicRel" DROP STORAGE';

  INSERT INTO "JcicRel" (
    "DataYMD",
    "BankItem",
    "BranchItem",
    "RelYM",
    "TranCode",
    "CustId",
    "Filler6",
    "RelId",
    "Filler8",
    "RelationCode",
    "Filler10",
    "EndCode",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "DataYMD",
    "BankItem",
    "BranchItem",
    "RelYM",
    "TranCode",
    "CustId",
    "Filler6",
    "RelId",
    "Filler8",
    "RelationCode",
    "Filler10",
    "EndCode",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."JcicRel";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_JcicRel_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;