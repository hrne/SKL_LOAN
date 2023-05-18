CREATE OR REPLACE PROCEDURE "Usp_Cp_CdPerformance_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "CdPerformance" DROP STORAGE';

  INSERT INTO "CdPerformance" (
    "WorkMonth",
    "PieceCode",
    "UnitCnt",
    "UnitAmtCond",
    "UnitPercent",
    "IntrodPerccent",
    "IntrodAmtCond",
    "IntrodPfEqBase",
    "IntrodPfEqAmt",
    "IntrodRewardBase",
    "IntrodReward",
    "BsOffrCnt",
    "BsOffrCntLimit",
    "BsOffrAmtCond",
    "BsOffrPerccent",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "WorkMonth",
    "PieceCode",
    "UnitCnt",
    "UnitAmtCond",
    "UnitPercent",
    "IntrodPerccent",
    "IntrodAmtCond",
    "IntrodPfEqBase",
    "IntrodPfEqAmt",
    "IntrodRewardBase",
    "IntrodReward",
    "BsOffrCnt",
    "BsOffrCntLimit",
    "BsOffrAmtCond",
    "BsOffrPerccent",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."CdPerformance";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_CdPerformance_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;