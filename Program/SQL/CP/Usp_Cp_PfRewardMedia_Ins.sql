CREATE OR REPLACE PROCEDURE "Usp_Cp_PfRewardMedia_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "PfRewardMedia" DROP STORAGE';

  INSERT INTO "PfRewardMedia" (
    "BonusNo",
    "BonusDate",
    "PerfDate",
    "CustNo",
    "FacmNo",
    "BormNo",
    "BonusType",
    "EmployeeNo",
    "ProdCode",
    "PieceCode",
    "Bonus",
    "AdjustBonus",
    "AdjustBonusDate",
    "WorkMonth",
    "WorkSeason",
    "Remark",
    "MediaFg",
    "MediaDate",
    "ManualFg",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "BonusNo",
    "BonusDate",
    "PerfDate",
    "CustNo",
    "FacmNo",
    "BormNo",
    "BonusType",
    "EmployeeNo",
    "ProdCode",
    "PieceCode",
    "Bonus",
    "AdjustBonus",
    "AdjustBonusDate",
    "WorkMonth",
    "WorkSeason",
    "Remark",
    "MediaFg",
    "MediaDate",
    "ManualFg",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."PfRewardMedia";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_PfRewardMedia_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;