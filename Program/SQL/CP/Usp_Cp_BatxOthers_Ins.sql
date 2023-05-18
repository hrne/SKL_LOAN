CREATE OR REPLACE PROCEDURE "Usp_Cp_BatxOthers_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "BatxOthers" DROP STORAGE';

  INSERT INTO "BatxOthers" (
    "AcDate",
    "BatchNo",
    "DetailSeq",
    "RepayCode",
    "RepayType",
    "RepayAcCode",
    "EntryDate",
    "RepayAmt",
    "RepayId",
    "RepayName",
    "CustNo",
    "FacmNo",
    "CustNm",
    "RvNo",
    "Note",
    "TitaEntdy",
    "TitaTlrNo",
    "TitaTxtNo",
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
    "RepayType",
    "RepayAcCode",
    "EntryDate",
    "RepayAmt",
    "RepayId",
    "RepayName",
    "CustNo",
    "FacmNo",
    "CustNm",
    "RvNo",
    "Note",
    "TitaEntdy",
    "TitaTlrNo",
    "TitaTxtNo",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."BatxOthers";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_BatxOthers_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;