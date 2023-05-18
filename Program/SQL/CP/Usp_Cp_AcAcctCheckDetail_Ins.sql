CREATE OR REPLACE PROCEDURE "Usp_Cp_AcAcctCheckDetail_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "AcAcctCheckDetail" DROP STORAGE';

  INSERT INTO "AcAcctCheckDetail" (
    "AcDate",
    "BranchNo",
    "CurrencyCode",
    "AcSubBookCode",
    "AcctCode",
    "AcctItem",
    "CustNo",
    "FacmNo",
    "BormNo",
    "AcBal",
    "AcctMasterBal",
    "DiffBal",
    "CreateEmpNo",
    "CreateDate",
    "LastUpdateEmpNo",
    "LastUpdate"
)
  SELECT
    "AcDate",
    "BranchNo",
    "CurrencyCode",
    "AcSubBookCode",
    "AcctCode",
    "AcctItem",
    "CustNo",
    "FacmNo",
    "BormNo",
    "AcBal",
    "AcctMasterBal",
    "DiffBal",
    "CreateEmpNo",
    "CreateDate",
    "LastUpdateEmpNo",
    "LastUpdate"
  FROM ITXADMIN."AcAcctCheckDetail";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_AcAcctCheckDetail_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;