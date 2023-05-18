CREATE OR REPLACE PROCEDURE "Usp_Cp_Ifrs9LoanData_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "Ifrs9LoanData" DROP STORAGE';

  INSERT INTO "Ifrs9LoanData" (
    "DataYM",
    "CustNo",
    "FacmNo",
    "BormNo",
    "CustId",
    "AcctCode",
    "AcCode",
    "AcCodeOld",
    "Status",
    "DrawdownDate",
    "MaturityDate",
    "DrawdownAmt",
    "LoanBal",
    "IntAmt",
    "Rate",
    "OvduDays",
    "OvduDate",
    "BadDebtDate",
    "BadDebtAmt",
    "ApproveRate",
    "AmortizedCode",
    "RateCode",
    "RepayFreq",
    "PayIntFreq",
    "FirstDueDate",
    "TotalPeriod",
    "AgreeBefFacmNo",
    "AgreeBefBormNo",
    "AcBookCode",
    "PrevPayIntDate",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "DataYM",
    "CustNo",
    "FacmNo",
    "BormNo",
    "CustId",
    "AcctCode",
    "AcCode",
    "AcCodeOld",
    "Status",
    "DrawdownDate",
    "MaturityDate",
    "DrawdownAmt",
    "LoanBal",
    "IntAmt",
    "Rate",
    "OvduDays",
    "OvduDate",
    "BadDebtDate",
    "BadDebtAmt",
    "ApproveRate",
    "AmortizedCode",
    "RateCode",
    "RepayFreq",
    "PayIntFreq",
    "FirstDueDate",
    "TotalPeriod",
    "AgreeBefFacmNo",
    "AgreeBefBormNo",
    "AcBookCode",
    "PrevPayIntDate",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."Ifrs9LoanData";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_Ifrs9LoanData_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;