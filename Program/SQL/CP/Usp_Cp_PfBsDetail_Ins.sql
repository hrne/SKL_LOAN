CREATE OR REPLACE PROCEDURE "Usp_Cp_PfBsDetail_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "PfBsDetail" DROP STORAGE';

  INSERT INTO "PfBsDetail" (
    "LogNo",
    "PerfDate",
    "CustNo",
    "FacmNo",
    "BormNo",
    "RepayType",
    "BsOfficer",
    "DeptCode",
    "DrawdownDate",
    "ProdCode",
    "PieceCode",
    "DrawdownAmt",
    "PerfCnt",
    "PerfAmt",
    "AdjPerfCnt",
    "AdjPerfAmt",
    "WorkMonth",
    "WorkSeason",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "LogNo",
    "PerfDate",
    "CustNo",
    "FacmNo",
    "BormNo",
    "RepayType",
    "BsOfficer",
    "DeptCode",
    "DrawdownDate",
    "ProdCode",
    "PieceCode",
    "DrawdownAmt",
    "PerfCnt",
    "PerfAmt",
    "AdjPerfCnt",
    "AdjPerfAmt",
    "WorkMonth",
    "WorkSeason",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."PfBsDetail";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_PfBsDetail_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;