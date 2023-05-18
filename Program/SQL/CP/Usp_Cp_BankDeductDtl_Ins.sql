CREATE OR REPLACE PROCEDURE "Usp_Cp_BankDeductDtl_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "BankDeductDtl" DROP STORAGE';

  INSERT INTO "BankDeductDtl" (
    "EntryDate",
    "CustNo",
    "FacmNo",
    "RepayType",
    "PayIntDate",
    "PrevIntDate",
    "AcctCode",
    "RepayBank",
    "RepayAcctNo",
    "RepayAcctSeq",
    "UnpaidAmt",
    "TempAmt",
    "RepayAmt",
    "IntStartDate",
    "IntEndDate",
    "PostCode",
    "MediaCode",
    "RelationCode",
    "RelCustName",
    "RelCustId",
    "RelAcctBirthday",
    "RelAcctGender",
    "MediaDate",
    "MediaKind",
    "MediaSeq",
    "AcDate",
    "TitaTlrNo",
    "TitaTxtNo",
    "AmlRsp",
    "ReturnCode",
    "JsonFields",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "EntryDate",
    "CustNo",
    "FacmNo",
    "RepayType",
    "PayIntDate",
    "PrevIntDate",
    "AcctCode",
    "RepayBank",
    "RepayAcctNo",
    "RepayAcctSeq",
    "UnpaidAmt",
    "TempAmt",
    "RepayAmt",
    "IntStartDate",
    "IntEndDate",
    "PostCode",
    "MediaCode",
    "RelationCode",
    "RelCustName",
    "RelCustId",
    "RelAcctBirthday",
    "RelAcctGender",
    "MediaDate",
    "MediaKind",
    "MediaSeq",
    "AcDate",
    "TitaTlrNo",
    "TitaTxtNo",
    "AmlRsp",
    "ReturnCode",
    "JsonFields",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."BankDeductDtl";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_BankDeductDtl_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;