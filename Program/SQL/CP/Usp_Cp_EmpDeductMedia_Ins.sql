CREATE OR REPLACE PROCEDURE "Usp_Cp_EmpDeductMedia_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "EmpDeductMedia" DROP STORAGE';

  INSERT INTO "EmpDeductMedia" (
    "MediaDate",
    "MediaKind",
    "MediaSeq",
    "CustNo",
    "RepayCode",
    "PerfRepayCode",
    "RepayAmt",
    "PerfMonth",
    "FlowCode",
    "UnitCode",
    "CustId",
    "EntryDate",
    "TxAmt",
    "ErrorCode",
    "AcctCode",
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
    "RepayCode",
    "PerfRepayCode",
    "RepayAmt",
    "PerfMonth",
    "FlowCode",
    "UnitCode",
    "CustId",
    "EntryDate",
    "TxAmt",
    "ErrorCode",
    "AcctCode",
    "AcDate",
    "BatchNo",
    "DetailSeq",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."EmpDeductMedia";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_EmpDeductMedia_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;