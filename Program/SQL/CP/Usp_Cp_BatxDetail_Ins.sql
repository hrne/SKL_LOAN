CREATE OR REPLACE PROCEDURE "Usp_Cp_BatxDetail_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "BatxDetail" DROP STORAGE';

  INSERT INTO "BatxDetail" (
    "AcDate",
    "BatchNo",
    "DetailSeq",
    "RepayCode",
    "FileName",
    "EntryDate",
    "CustNo",
    "FacmNo",
    "RvNo",
    "RepayType",
    "ReconCode",
    "RepayAcCode",
    "AcquiredAmt",
    "RepayAmt",
    "AcctAmt",
    "DisacctAmt",
    "ProcStsCode",
    "ProcCode",
    "ProcNote",
    "OtherNote",
    "TitaTlrNo",
    "TitaTxtNo",
    "MediaDate",
    "MediaKind",
    "MediaSeq",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "AcDate",
    "BatchNo",
    "DetailSeq",
    "RepayCode",
    "FileName",
    "EntryDate",
    "CustNo",
    "FacmNo",
    "RvNo",
    "RepayType",
    "ReconCode",
    "RepayAcCode",
    "AcquiredAmt",
    "RepayAmt",
    "AcctAmt",
    "DisacctAmt",
    "ProcStsCode",
    "ProcCode",
    "ProcNote",
    "OtherNote",
    "TitaTlrNo",
    "TitaTxtNo",
    "MediaDate",
    "MediaKind",
    "MediaSeq",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."BatxDetail";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_BatxDetail_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;