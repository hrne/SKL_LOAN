CREATE OR REPLACE PROCEDURE "Usp_Cp_PfItDetailAdjust_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "PfItDetailAdjust" DROP STORAGE';

  INSERT INTO "PfItDetailAdjust" (
    "LogNo",
    "CustNo",
    "FacmNo",
    "BormNo",
    "WorkMonth",
    "WorkSeason",
    "AdjRange",
    "AdjPerfEqAmt",
    "AdjPerfReward",
    "AdjPerfAmt",
    "AdjCntingCode",
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
    "WorkMonth",
    "WorkSeason",
    "AdjRange",
    "AdjPerfEqAmt",
    "AdjPerfReward",
    "AdjPerfAmt",
    "AdjCntingCode",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."PfItDetailAdjust";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_PfItDetailAdjust_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;