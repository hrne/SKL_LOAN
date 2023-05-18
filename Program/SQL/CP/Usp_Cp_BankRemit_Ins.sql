CREATE OR REPLACE PROCEDURE "Usp_Cp_BankRemit_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "BankRemit" DROP STORAGE';

  INSERT INTO "BankRemit" (
    "AcDate",
    "TitaTlrNo",
    "TitaTxtNo",
    "Seq",
    "BatchNo",
    "DrawdownCode",
    "StatusCode",
    "RemitBank",
    "RemitBranch",
    "RemitAcctNo",
    "CustNo",
    "FacmNo",
    "BormNo",
    "CustName",
    "CustId",
    "CustBirthday",
    "CustGender",
    "Remark",
    "CurrencyCode",
    "RemitAmt",
    "AmlRsp",
    "ActFg",
    "ModifyContent",
    "PayCode",
    "CoreRemitFlag",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "AcDate",
    "TitaTlrNo",
    "TitaTxtNo",
    "Seq",
    "BatchNo",
    "DrawdownCode",
    "StatusCode",
    "RemitBank",
    "RemitBranch",
    "RemitAcctNo",
    "CustNo",
    "FacmNo",
    "BormNo",
    "CustName",
    "CustId",
    "CustBirthday",
    "CustGender",
    "Remark",
    "CurrencyCode",
    "RemitAmt",
    "AmlRsp",
    "ActFg",
    "ModifyContent",
    "PayCode",
    "CoreRemitFlag",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."BankRemit";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_BankRemit_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;