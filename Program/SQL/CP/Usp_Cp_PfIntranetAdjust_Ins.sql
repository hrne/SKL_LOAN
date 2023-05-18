CREATE OR REPLACE PROCEDURE "Usp_Cp_PfIntranetAdjust_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "PfIntranetAdjust" DROP STORAGE';

  INSERT INTO "PfIntranetAdjust" (
    "LogNo",
    "CustNo",
    "FacmNo",
    "BormNo",
    "PerfDate",
    "WorkMonth",
    "WorkSeason",
    "Introducer",
    "BsOfficer",
    "PerfAmt",
    "PerfCnt",
    "UnitType",
    "UnitCode",
    "DistCode",
    "DeptCode",
    "SumAmt",
    "SumCnt",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "LogNo",
    "CustNo",
    "FacmNo",
    "BormNo",
    "PerfDate",
    "WorkMonth",
    "WorkSeason",
    "Introducer",
    "BsOfficer",
    "PerfAmt",
    "PerfCnt",
    "UnitType",
    "UnitCode",
    "DistCode",
    "DeptCode",
    "SumAmt",
    "SumCnt",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."PfIntranetAdjust";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_PfIntranetAdjust_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;