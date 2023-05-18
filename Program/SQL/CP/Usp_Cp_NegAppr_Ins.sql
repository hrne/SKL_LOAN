CREATE OR REPLACE PROCEDURE "Usp_Cp_NegAppr_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "NegAppr" DROP STORAGE';

  INSERT INTO "NegAppr" (
    "YyyyMm",
    "KindCode",
    "ExportDate",
    "ApprAcDate",
    "BringUpDate",
    "ExportMark",
    "ApprAcMark",
    "BringUpMark",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "YyyyMm",
    "KindCode",
    "ExportDate",
    "ApprAcDate",
    "BringUpDate",
    "ExportMark",
    "ApprAcMark",
    "BringUpMark",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."NegAppr";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_NegAppr_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;