CREATE OR REPLACE PROCEDURE "Usp_Cp_BatxCheque_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "BatxCheque" DROP STORAGE';

  INSERT INTO "BatxCheque" (
    "AcDate",
    "BatchNo",
    "ChequeAcct",
    "ChequeNo",
    "ChequeAmt",
    "CustNo",
    "StatusCode",
    "AdjDate",
    "TitaTlrNo",
    "TitaTxtNo",
    "ChequeDate",
    "EntryDate",
    "ProcessCode",
    "OutsideCode",
    "MediaCode",
    "BankCode",
    "MediaBatchNo",
    "OfficeCode",
    "ExchangeAreaCode",
    "ChequeId",
    "ChequeName",
    "AmlRsp",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "AcDate",
    "BatchNo",
    "ChequeAcct",
    "ChequeNo",
    "ChequeAmt",
    "CustNo",
    "StatusCode",
    "AdjDate",
    "TitaTlrNo",
    "TitaTxtNo",
    "ChequeDate",
    "EntryDate",
    "ProcessCode",
    "OutsideCode",
    "MediaCode",
    "BankCode",
    "MediaBatchNo",
    "OfficeCode",
    "ExchangeAreaCode",
    "ChequeId",
    "ChequeName",
    "AmlRsp",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."BatxCheque";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_BatxCheque_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;