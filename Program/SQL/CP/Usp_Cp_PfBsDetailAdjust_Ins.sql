CREATE OR REPLACE PROCEDURE "Usp_Cp_PfBsDetailAdjust_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "PfBsDetailAdjust" DROP STORAGE';

  INSERT INTO "PfBsDetailAdjust" (
    "LogNo",
    "CustNo",
    "FacmNo",
    "BormNo",
    "WorkMonth",
    "WorkSeason",
    "AdjPerfCnt",
    "AdjPerfAmt",
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
    "AdjPerfCnt",
    "AdjPerfAmt",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."PfBsDetailAdjust";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_PfBsDetailAdjust_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;