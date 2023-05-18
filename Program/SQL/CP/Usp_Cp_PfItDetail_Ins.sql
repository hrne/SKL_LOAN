CREATE OR REPLACE PROCEDURE "Usp_Cp_PfItDetail_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "PfItDetail" DROP STORAGE';

  INSERT INTO "PfItDetail" (
    "LogNo",
    "PerfDate",
    "CustNo",
    "FacmNo",
    "BormNo",
    "RepayType",
    "DrawdownDate",
    "ProdCode",
    "PieceCode",
    "CntingCode",
    "DrawdownAmt",
    "UnitCode",
    "DistCode",
    "DeptCode",
    "Introducer",
    "UnitManager",
    "DistManager",
    "DeptManager",
    "PerfCnt",
    "PerfEqAmt",
    "PerfReward",
    "PerfAmt",
    "WorkMonth",
    "WorkSeason",
    "RewardDate",
    "MediaDate",
    "MediaFg",
    "AdjRange",
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
    "DrawdownDate",
    "ProdCode",
    "PieceCode",
    "CntingCode",
    "DrawdownAmt",
    "UnitCode",
    "DistCode",
    "DeptCode",
    "Introducer",
    "UnitManager",
    "DistManager",
    "DeptManager",
    "PerfCnt",
    "PerfEqAmt",
    "PerfReward",
    "PerfAmt",
    "WorkMonth",
    "WorkSeason",
    "RewardDate",
    "MediaDate",
    "MediaFg",
    "AdjRange",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."PfItDetail";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_PfItDetail_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;