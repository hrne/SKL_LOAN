CREATE OR REPLACE PROCEDURE "Usp_Cp_JcicB080_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "JcicB080" DROP STORAGE';

  INSERT INTO "JcicB080" (
    "DataYM",
    "DataType",
    "BankItem",
    "BranchItem",
    "TranCode",
    "Filler4",
    "CustId",
    "FacmNo",
    "CurrencyCode",
    "DrawdownAmt",
    "DrawdownAmtFx",
    "DrawdownDate",
    "MaturityDate",
    "RecycleCode",
    "IrrevocableFlag",
    "UpFacmNo",
    "AcctCode",
    "SubAcctCode",
    "ClTypeCode",
    "Filler18",
    "JcicDataYM",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "DataYM",
    "DataType",
    "BankItem",
    "BranchItem",
    "TranCode",
    "Filler4",
    "CustId",
    "FacmNo",
    "CurrencyCode",
    "DrawdownAmt",
    "DrawdownAmtFx",
    "DrawdownDate",
    "MaturityDate",
    "RecycleCode",
    "IrrevocableFlag",
    "UpFacmNo",
    "AcctCode",
    "SubAcctCode",
    "ClTypeCode",
    "Filler18",
    "JcicDataYM",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."JcicB080";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_JcicB080_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;