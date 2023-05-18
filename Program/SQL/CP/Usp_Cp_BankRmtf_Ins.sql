CREATE OR REPLACE PROCEDURE "Usp_Cp_BankRmtf_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "BankRmtf" DROP STORAGE';

  INSERT INTO "BankRmtf" (
    "AcDate",
    "BatchNo",
    "DetailSeq",
    "CustNo",
    "RepayType",
    "RepayAmt",
    "DepAcctNo",
    "EntryDate",
    "DscptCode",
    "VirtualAcctNo",
    "WithdrawAmt",
    "DepositAmt",
    "Balance",
    "RemintBank",
    "TraderInfo",
    "AmlRsp",
    "ReconCode",
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
    "CustNo",
    "RepayType",
    "RepayAmt",
    "DepAcctNo",
    "EntryDate",
    "DscptCode",
    "VirtualAcctNo",
    "WithdrawAmt",
    "DepositAmt",
    "Balance",
    "RemintBank",
    "TraderInfo",
    "AmlRsp",
    "ReconCode",
    "TitaEntdy",
    "TitaTlrNo",
    "TitaTxtNo",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."BankRmtf";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_BankRmtf_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;