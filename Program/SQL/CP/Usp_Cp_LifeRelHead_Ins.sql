CREATE OR REPLACE PROCEDURE "Usp_Cp_LifeRelHead_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "LifeRelHead" DROP STORAGE';

  INSERT INTO "LifeRelHead" (
    "RelWithCompany",
    "HeadId",
    "HeadName",
    "HeadTitle",
    "RelId",
    "RelName",
    "RelKinShip",
    "RelTitle",
    "BusId",
    "BusName",
    "ShareHoldingRatio",
    "BusTitle",
    "LineAmt",
    "LoanBalance",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "RelWithCompany",
    "HeadId",
    "HeadName",
    "HeadTitle",
    "RelId",
    "RelName",
    "RelKinShip",
    "RelTitle",
    "BusId",
    "BusName",
    "ShareHoldingRatio",
    "BusTitle",
    "LineAmt",
    "LoanBalance",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."LifeRelHead";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_LifeRelHead_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;