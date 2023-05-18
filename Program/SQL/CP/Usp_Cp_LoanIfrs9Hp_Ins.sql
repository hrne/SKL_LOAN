CREATE OR REPLACE PROCEDURE "Usp_Cp_LoanIfrs9Hp_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "LoanIfrs9Hp" DROP STORAGE';

  INSERT INTO "LoanIfrs9Hp" (
    "DataYM",
    "CustNo",
    "CustId",
    "FacmNo",
    "ApplNo",
    "CustKind",
    "ApproveDate",
    "FirstDrawdownDate",
    "LineAmt",
    "Ifrs9ProdCode",
    "AvblBal",
    "RecycleCode",
    "IrrevocableFlag",
    "IndustryCode",
    "OriRating",
    "OriModel",
    "Rating",
    "Model",
    "LGDModel",
    "LGD",
    "LineAmtCurr",
    "AvblBalCurr",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "DataYM",
    "CustNo",
    "CustId",
    "FacmNo",
    "ApplNo",
    "CustKind",
    "ApproveDate",
    "FirstDrawdownDate",
    "LineAmt",
    "Ifrs9ProdCode",
    "AvblBal",
    "RecycleCode",
    "IrrevocableFlag",
    "IndustryCode",
    "OriRating",
    "OriModel",
    "Rating",
    "Model",
    "LGDModel",
    "LGD",
    "LineAmtCurr",
    "AvblBalCurr",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."LoanIfrs9Hp";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_LoanIfrs9Hp_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;