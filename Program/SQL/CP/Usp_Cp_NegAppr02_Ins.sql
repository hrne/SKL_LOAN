CREATE OR REPLACE PROCEDURE "Usp_Cp_NegAppr02_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "NegAppr02" DROP STORAGE';

  INSERT INTO "NegAppr02" (
    "BringUpDate",
    "FinCode",
    "TxSeq",
    "SendUnit",
    "RecvUnit",
    "EntryDate",
    "TransCode",
    "TxAmt",
    "Consign",
    "FinIns",
    "RemitAcct",
    "CustId",
    "CustNo",
    "StatusCode",
    "AcDate",
    "TxKind",
    "TxStatus",
    "NegTransAcDate",
    "NegTransTlrNo",
    "NegTransTxtNo",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "BringUpDate",
    "FinCode",
    "TxSeq",
    "SendUnit",
    "RecvUnit",
    "EntryDate",
    "TransCode",
    "TxAmt",
    "Consign",
    "FinIns",
    "RemitAcct",
    "CustId",
    "CustNo",
    "StatusCode",
    "AcDate",
    "TxKind",
    "TxStatus",
    "NegTransAcDate",
    "NegTransTlrNo",
    "NegTransTxtNo",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."NegAppr02";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_NegAppr02_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;