CREATE OR REPLACE PROCEDURE "Usp_Cp_LoanRateChange_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "LoanRateChange" DROP STORAGE';

  INSERT INTO "LoanRateChange" (
    "CustNo",
    "FacmNo",
    "BormNo",
    "EffectDate",
    "Status",
    "RateCode",
    "ProdNo",
    "BaseRateCode",
    "IncrFlag",
    "RateIncr",
    "IndividualIncr",
    "FitRate",
    "Remark",
    "AcDate",
    "TellerNo",
    "TxtNo",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo",
    "OtherFields"
)
  SELECT
    "CustNo",
    "FacmNo",
    "BormNo",
    "EffectDate",
    "Status",
    "RateCode",
    "ProdNo",
    "BaseRateCode",
    "IncrFlag",
    "RateIncr",
    "IndividualIncr",
    "FitRate",
    "Remark",
    "AcDate",
    "TellerNo",
    "TxtNo",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo",
    "OtherFields"
  FROM ITXADMIN."LoanRateChange";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_LoanRateChange_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;