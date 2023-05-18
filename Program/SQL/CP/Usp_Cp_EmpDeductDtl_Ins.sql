CREATE OR REPLACE PROCEDURE "Usp_Cp_EmpDeductDtl_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "EmpDeductDtl" DROP STORAGE';

  INSERT INTO "EmpDeductDtl" (
    "EntryDate",
    "CustNo",
    "AchRepayCode",
    "PerfMonth",
    "ProcCode",
    "RepayCode",
    "AcctCode",
    "FacmNo",
    "BormNo",
    "EmpNo",
    "CustId",
    "TxAmt",
    "ErrMsg",
    "Acdate",
    "TitaTlrNo",
    "TitaTxtNo",
    "BatchNo",
    "RepayAmt",
    "ResignCode",
    "DeptCode",
    "UnitCode",
    "IntStartDate",
    "IntEndDate",
    "PositCode",
    "Principal",
    "Interest",
    "SumOvpayAmt",
    "JsonFields",
    "CurrIntAmt",
    "CurrPrinAmt",
    "MediaDate",
    "MediaKind",
    "MediaSeq",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "EntryDate",
    "CustNo",
    "AchRepayCode",
    "PerfMonth",
    "ProcCode",
    "RepayCode",
    "AcctCode",
    "FacmNo",
    "BormNo",
    "EmpNo",
    "CustId",
    "TxAmt",
    "ErrMsg",
    "Acdate",
    "TitaTlrNo",
    "TitaTxtNo",
    "BatchNo",
    "RepayAmt",
    "ResignCode",
    "DeptCode",
    "UnitCode",
    "IntStartDate",
    "IntEndDate",
    "PositCode",
    "Principal",
    "Interest",
    "SumOvpayAmt",
    "JsonFields",
    "CurrIntAmt",
    "CurrPrinAmt",
    "MediaDate",
    "MediaKind",
    "MediaSeq",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."EmpDeductDtl";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_EmpDeductDtl_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;