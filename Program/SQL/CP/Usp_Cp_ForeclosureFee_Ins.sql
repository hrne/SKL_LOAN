CREATE OR REPLACE PROCEDURE "Usp_Cp_ForeclosureFee_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "ForeclosureFee" DROP STORAGE';

  INSERT INTO "ForeclosureFee" (
    "RecordNo",
    "CustNo",
    "FacmNo",
    "ReceiveDate",
    "DocDate",
    "OpenAcDate",
    "CloseDate",
    "Fee",
    "FeeCode",
    "LegalStaff",
    "CloseNo",
    "Rmk",
    "CaseCode",
    "RemitBranch",
    "Remitter",
    "CaseNo",
    "OverdueDate",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "RecordNo",
    "CustNo",
    "FacmNo",
    "ReceiveDate",
    "DocDate",
    "OpenAcDate",
    "CloseDate",
    "Fee",
    "FeeCode",
    "LegalStaff",
    "CloseNo",
    "Rmk",
    "CaseCode",
    "RemitBranch",
    "Remitter",
    "CaseNo",
    "OverdueDate",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."ForeclosureFee";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_ForeclosureFee_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;