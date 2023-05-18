CREATE OR REPLACE PROCEDURE "Usp_Cp_ClEva_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "ClEva" DROP STORAGE';

  INSERT INTO "ClEva" (
    "ClCode1",
    "ClCode2",
    "ClNo",
    "EvaNo",
    "EvaDate",
    "EvaAmt",
    "EvaNetWorth",
    "RentEvaValue",
    "EvaCompanyId",
    "EvaCompanyName",
    "EvaEmpno",
    "EvaReason",
    "OtherReason",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "ClCode1",
    "ClCode2",
    "ClNo",
    "EvaNo",
    "EvaDate",
    "EvaAmt",
    "EvaNetWorth",
    "RentEvaValue",
    "EvaCompanyId",
    "EvaCompanyName",
    "EvaEmpno",
    "EvaReason",
    "OtherReason",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."ClEva";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_ClEva_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;