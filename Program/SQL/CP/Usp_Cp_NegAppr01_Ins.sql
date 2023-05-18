CREATE OR REPLACE PROCEDURE "Usp_Cp_NegAppr01_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "NegAppr01" DROP STORAGE';

  INSERT INTO "NegAppr01" (
    "AcDate",
    "TitaTlrNo",
    "TitaTxtNo",
    "FinCode",
    "CustNo",
    "CaseSeq",
    "CaseKindCode",
    "ApprAmt",
    "AccuApprAmt",
    "AmtRatio",
    "ExportDate",
    "ApprDate",
    "BringUpDate",
    "RemitBank",
    "RemitAcct",
    "DataSendUnit",
    "ApprAcDate",
    "ReplyCode",
    "BatchTxtNo",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "AcDate",
    "TitaTlrNo",
    "TitaTxtNo",
    "FinCode",
    "CustNo",
    "CaseSeq",
    "CaseKindCode",
    "ApprAmt",
    "AccuApprAmt",
    "AmtRatio",
    "ExportDate",
    "ApprDate",
    "BringUpDate",
    "RemitBank",
    "RemitAcct",
    "DataSendUnit",
    "ApprAcDate",
    "ReplyCode",
    "BatchTxtNo",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."NegAppr01";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_NegAppr01_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;