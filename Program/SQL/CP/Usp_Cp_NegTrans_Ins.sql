CREATE OR REPLACE PROCEDURE "Usp_Cp_NegTrans_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "NegTrans" DROP STORAGE';

  INSERT INTO "NegTrans" (
    "AcDate",
    "TitaTlrNo",
    "TitaTxtNo",
    "CustNo",
    "CaseSeq",
    "EntryDate",
    "TxStatus",
    "TxKind",
    "TxAmt",
    "PrincipalBal",
    "ReturnAmt",
    "SklShareAmt",
    "ApprAmt",
    "ExportDate",
    "ExportAcDate",
    "TempRepayAmt",
    "OverRepayAmt",
    "PrincipalAmt",
    "InterestAmt",
    "OverAmt",
    "IntStartDate",
    "IntEndDate",
    "RepayPeriod",
    "RepayDate",
    "OrgAccuOverAmt",
    "AccuOverAmt",
    "ShouldPayPeriod",
    "DueAmt",
    "ThisEntdy",
    "ThisKinbr",
    "ThisTlrNo",
    "ThisTxtNo",
    "ThisSeqNo",
    "LastEntdy",
    "LastKinbr",
    "LastTlrNo",
    "LastTxtNo",
    "LastSeqNo",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "AcDate",
    "TitaTlrNo",
    "TitaTxtNo",
    "CustNo",
    "CaseSeq",
    "EntryDate",
    "TxStatus",
    "TxKind",
    "TxAmt",
    "PrincipalBal",
    "ReturnAmt",
    "SklShareAmt",
    "ApprAmt",
    "ExportDate",
    "ExportAcDate",
    "TempRepayAmt",
    "OverRepayAmt",
    "PrincipalAmt",
    "InterestAmt",
    "OverAmt",
    "IntStartDate",
    "IntEndDate",
    "RepayPeriod",
    "RepayDate",
    "OrgAccuOverAmt",
    "AccuOverAmt",
    "ShouldPayPeriod",
    "DueAmt",
    "ThisEntdy",
    "ThisKinbr",
    "ThisTlrNo",
    "ThisTxtNo",
    "ThisSeqNo",
    "LastEntdy",
    "LastKinbr",
    "LastTlrNo",
    "LastTxtNo",
    "LastSeqNo",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."NegTrans";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_NegTrans_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;