CREATE OR REPLACE PROCEDURE "Usp_Cp_LoanOverdue_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "LoanOverdue" DROP STORAGE';

  INSERT INTO "LoanOverdue" (
    "CustNo",
    "FacmNo",
    "BormNo",
    "OvduNo",
    "Status",
    "AcctCode",
    "OvduDate",
    "BadDebtDate",
    "ReplyDate",
    "OvduPrinAmt",
    "OvduIntAmt",
    "OvduBreachAmt",
    "OvduAmt",
    "OvduPrinBal",
    "OvduIntBal",
    "OvduBreachBal",
    "OvduBal",
    "ReduceInt",
    "ReduceBreach",
    "BadDebtAmt",
    "BadDebtBal",
    "ReplyReduceAmt",
    "ProcessDate",
    "OvduSituaction",
    "Remark",
    "AcDate",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "CustNo",
    "FacmNo",
    "BormNo",
    "OvduNo",
    "Status",
    "AcctCode",
    "OvduDate",
    "BadDebtDate",
    "ReplyDate",
    "OvduPrinAmt",
    "OvduIntAmt",
    "OvduBreachAmt",
    "OvduAmt",
    "OvduPrinBal",
    "OvduIntBal",
    "OvduBreachBal",
    "OvduBal",
    "ReduceInt",
    "ReduceBreach",
    "BadDebtAmt",
    "BadDebtBal",
    "ReplyReduceAmt",
    "ProcessDate",
    "OvduSituaction",
    "Remark",
    "AcDate",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."LoanOverdue";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_LoanOverdue_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;