CREATE OR REPLACE PROCEDURE "Usp_Cp_RptRelationFamily_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "RptRelationFamily" DROP STORAGE';

  INSERT INTO "RptRelationFamily" (
    "CusId",
    "CusSCD",
    "RlbID",
    "RlbName",
    "FamilyCD",
    "LAW001",
    "LAW002",
    "LAW003",
    "LAW004",
    "LAW005",
    "LAW006",
    "LAW007",
    "LAW008",
    "LAW009",
    "LAW010",
    "RlbCusCCD",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "CusId",
    "CusSCD",
    "RlbID",
    "RlbName",
    "FamilyCD",
    "LAW001",
    "LAW002",
    "LAW003",
    "LAW004",
    "LAW005",
    "LAW006",
    "LAW007",
    "LAW008",
    "LAW009",
    "LAW010",
    "RlbCusCCD",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."RptRelationFamily";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_RptRelationFamily_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;