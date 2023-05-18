CREATE OR REPLACE PROCEDURE "Usp_Cp_JcicB204_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "JcicB204" DROP STORAGE';

  INSERT INTO "JcicB204" (
    "DataYMD",
    "BankItem",
    "BranchItem",
    "DataDate",
    "AcctNo",
    "CustId",
    "AcctCode",
    "SubAcctCode",
    "SubTranCode",
    "LineAmt",
    "DrawdownAmt",
    "DBR22Amt",
    "SeqNo",
    "Filler13",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "DataYMD",
    "BankItem",
    "BranchItem",
    "DataDate",
    "AcctNo",
    "CustId",
    "AcctCode",
    "SubAcctCode",
    "SubTranCode",
    "LineAmt",
    "DrawdownAmt",
    "DBR22Amt",
    "SeqNo",
    "Filler13",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."JcicB204";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_JcicB204_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;