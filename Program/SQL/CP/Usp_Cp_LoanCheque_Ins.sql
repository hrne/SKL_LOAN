CREATE OR REPLACE PROCEDURE "Usp_Cp_LoanCheque_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "LoanCheque" DROP STORAGE';

  INSERT INTO "LoanCheque" (
    "CustNo",
    "ChequeAcct",
    "ChequeNo",
    "StatusCode",
    "ProcessCode",
    "AcDate",
    "Kinbr",
    "TellerNo",
    "TxtNo",
    "ReceiveDate",
    "EntryDate",
    "CurrencyCode",
    "ChequeAmt",
    "ChequeName",
    "ChequeDate",
    "AreaCode",
    "BankCode",
    "BankItem",
    "BranchItem",
    "OutsideCode",
    "BktwFlag",
    "MediaFlag",
    "UsageCode",
    "ServiceCenter",
    "CreditorId",
    "CreditorBankCode",
    "OtherAcctCode",
    "ReceiptNo",
    "RepaidAmt",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "CustNo",
    "ChequeAcct",
    "ChequeNo",
    "StatusCode",
    "ProcessCode",
    "AcDate",
    "Kinbr",
    "TellerNo",
    "TxtNo",
    "ReceiveDate",
    "EntryDate",
    "CurrencyCode",
    "ChequeAmt",
    "ChequeName",
    "ChequeDate",
    "AreaCode",
    "BankCode",
    "BankItem",
    "BranchItem",
    "OutsideCode",
    "BktwFlag",
    "MediaFlag",
    "UsageCode",
    "ServiceCenter",
    "CreditorId",
    "CreditorBankCode",
    "OtherAcctCode",
    "ReceiptNo",
    "RepaidAmt",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."LoanCheque";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_LoanCheque_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;