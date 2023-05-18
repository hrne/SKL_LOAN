CREATE OR REPLACE PROCEDURE "Usp_Cp_LoanIntDetail_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "LoanIntDetail" DROP STORAGE';

  INSERT INTO "LoanIntDetail" (
    "CustNo",
    "FacmNo",
    "BormNo",
    "AcDate",
    "TlrNo",
    "TxtNo",
    "IntSeq",
    "IntStartDate",
    "IntEndDate",
    "IntDays",
    "BreachDays",
    "MonthLimit",
    "IntFlag",
    "CurrencyCode",
    "Amount",
    "IntRate",
    "Principal",
    "Interest",
    "DelayInt",
    "BreachAmt",
    "CloseBreachAmt",
    "BreachGetCode",
    "LoanBal",
    "ExtraRepayFlag",
    "ProdNo",
    "BaseRateCode",
    "RateIncr",
    "IndividualIncr",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "CustNo",
    "FacmNo",
    "BormNo",
    "AcDate",
    "TlrNo",
    "TxtNo",
    "IntSeq",
    "IntStartDate",
    "IntEndDate",
    "IntDays",
    "BreachDays",
    "MonthLimit",
    "IntFlag",
    "CurrencyCode",
    "Amount",
    "IntRate",
    "Principal",
    "Interest",
    "DelayInt",
    "BreachAmt",
    "CloseBreachAmt",
    "BreachGetCode",
    "LoanBal",
    "ExtraRepayFlag",
    "ProdNo",
    "BaseRateCode",
    "RateIncr",
    "IndividualIncr",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."LoanIntDetail";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_LoanIntDetail_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;