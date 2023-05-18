CREATE OR REPLACE PROCEDURE "Usp_Cp_PostDeductMedia_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "PostDeductMedia" DROP STORAGE';

  INSERT INTO "PostDeductMedia" (
    "MediaDate",
    "MediaSeq",
    "CustNo",
    "FacmNo",
    "RepayType",
    "RepayAmt",
    "ProcNoteCode",
    "PostDepCode",
    "OutsrcCode",
    "DistCode",
    "TransDate",
    "RepayAcctNo",
    "PostUserNo",
    "OutsrcRemark",
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
    "MediaSeq",
    "CustNo",
    "FacmNo",
    "RepayType",
    "RepayAmt",
    "ProcNoteCode",
    "PostDepCode",
    "OutsrcCode",
    "DistCode",
    "TransDate",
    "RepayAcctNo",
    "PostUserNo",
    "OutsrcRemark",
    "AcDate",
    "BatchNo",
    "DetailSeq",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."PostDeductMedia";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_PostDeductMedia_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;