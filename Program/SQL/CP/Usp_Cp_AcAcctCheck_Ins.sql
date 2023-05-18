CREATE OR REPLACE PROCEDURE "Usp_Cp_AcAcctCheck_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "AcAcctCheck" DROP STORAGE';

  INSERT INTO "AcAcctCheck" (
    "AcDate",
    "BranchNo",
    "CurrencyCode",
    "AcSubBookCode",
    "AcctCode",
    "AcctItem",
    "TdBal",
    "TdCnt",
    "TdNewCnt",
    "TdClsCnt",
    "TdExtCnt",
    "TdExtAmt",
    "ReceivableBal",
    "AcctMasterBal",
    "YdBal",
    "DbAmt",
    "CrAmt",
    "CoreDbAmt",
    "CoreCrAmt",
    "MasterClsAmt",
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
    "TdBal",
    "TdCnt",
    "TdNewCnt",
    "TdClsCnt",
    "TdExtCnt",
    "TdExtAmt",
    "ReceivableBal",
    "AcctMasterBal",
    "YdBal",
    "DbAmt",
    "CrAmt",
    "CoreDbAmt",
    "CoreCrAmt",
    "MasterClsAmt",
    "CreateEmpNo",
    "CreateDate",
    "LastUpdateEmpNo",
    "LastUpdate"
  FROM ITXADMIN."AcAcctCheck";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_AcAcctCheck_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;