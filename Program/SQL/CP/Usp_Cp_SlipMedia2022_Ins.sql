CREATE OR REPLACE PROCEDURE "Usp_Cp_SlipMedia2022_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "SlipMedia2022" DROP STORAGE';

  INSERT INTO "SlipMedia2022" (
    "AcBookCode",
    "MediaSlipNo",
    "Seq",
    "AcDate",
    "BatchNo",
    "MediaSeq",
    "AcNoCode",
    "AcSubCode",
    "DeptCode",
    "DbCr",
    "TxAmt",
    "SlipRmk",
    "ReceiveCode",
    "CostMonth",
    "InsuNo",
    "SalesmanCode",
    "SalaryCode",
    "CurrencyCode",
    "AcSubBookCode",
    "CostUnit",
    "SalesChannelType",
    "IfrsType",
    "RelationId",
    "RelateCode",
    "Ifrs17Group",
    "LatestFlag",
    "TransferFlag",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "AcBookCode",
    "MediaSlipNo",
    "Seq",
    "AcDate",
    "BatchNo",
    "MediaSeq",
    "AcNoCode",
    "AcSubCode",
    "DeptCode",
    "DbCr",
    "TxAmt",
    "SlipRmk",
    "ReceiveCode",
    "CostMonth",
    "InsuNo",
    "SalesmanCode",
    "SalaryCode",
    "CurrencyCode",
    "AcSubBookCode",
    "CostUnit",
    "SalesChannelType",
    "IfrsType",
    "RelationId",
    "RelateCode",
    "Ifrs17Group",
    "LatestFlag",
    "TransferFlag",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."SlipMedia2022";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_SlipMedia2022_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;