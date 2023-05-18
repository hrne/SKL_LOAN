CREATE OR REPLACE PROCEDURE "Usp_Cp_ClImm_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "ClImm" DROP STORAGE';

  INSERT INTO "ClImm" (
    "ClCode1",
    "ClCode2",
    "ClNo",
    "EvaNetWorth",
    "LVITax",
    "RentEvaValue",
    "RentPrice",
    "OwnershipCode",
    "MtgCode",
    "MtgCheck",
    "MtgLoan",
    "MtgPledge",
    "Agreement",
    "EvaCompanyCode",
    "LimitCancelDate",
    "ClCode",
    "LoanToValue",
    "OtherOwnerTotal",
    "CompensationCopy",
    "BdRmk",
    "MtgReasonCode",
    "ReceivedDate",
    "ReceivedNo",
    "CancelDate",
    "CancelNo",
    "SettingStat",
    "ClStat",
    "SettingDate",
    "SettingAmt",
    "ClaimDate",
    "SettingSeq",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "ClCode1",
    "ClCode2",
    "ClNo",
    "EvaNetWorth",
    "LVITax",
    "RentEvaValue",
    "RentPrice",
    "OwnershipCode",
    "MtgCode",
    "MtgCheck",
    "MtgLoan",
    "MtgPledge",
    "Agreement",
    "EvaCompanyCode",
    "LimitCancelDate",
    "ClCode",
    "LoanToValue",
    "OtherOwnerTotal",
    "CompensationCopy",
    "BdRmk",
    "MtgReasonCode",
    "ReceivedDate",
    "ReceivedNo",
    "CancelDate",
    "CancelNo",
    "SettingStat",
    "ClStat",
    "SettingDate",
    "SettingAmt",
    "ClaimDate",
    "SettingSeq",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."ClImm";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_ClImm_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;