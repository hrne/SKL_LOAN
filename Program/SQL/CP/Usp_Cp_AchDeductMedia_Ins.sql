CREATE OR REPLACE PROCEDURE "Usp_Cp_AchDeductMedia_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "AchDeductMedia" DROP STORAGE';

  INSERT INTO "AchDeductMedia" (
    "MediaDate",
    "MediaKind",
    "MediaSeq",
    "CustNo",
    "FacmNo",
    "RepayType",
    "RepayAmt",
    "ReturnCode",
    "EntryDate",
    "PrevIntDate",
    "RepayBank",
    "RepayAcctNo",
    "AchRepayCode",
    "AcctCode",
    "IntStartDate",
    "IntEndDate",
    "DepCode",
    "RelationCode",
    "RelCustName",
    "RelCustId",
    "AcDate",
    "BatchNo",
    "DetailSeq",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "MediaDate",
    "MediaKind",
    "MediaSeq",
    "CustNo",
    "FacmNo",
    "RepayType",
    "RepayAmt",
    "ReturnCode",
    "EntryDate",
    "PrevIntDate",
    "RepayBank",
    "RepayAcctNo",
    "AchRepayCode",
    "AcctCode",
    "IntStartDate",
    "IntEndDate",
    "DepCode",
    "RelationCode",
    "RelCustName",
    "RelCustId",
    "AcDate",
    "BatchNo",
    "DetailSeq",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."AchDeductMedia";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_AchDeductMedia_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;