CREATE OR REPLACE PROCEDURE "Usp_Cp_EmpDeductSchedule_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "EmpDeductSchedule" DROP STORAGE';

  INSERT INTO "EmpDeductSchedule" (
    "WorkMonth",
    "AgType1",
    "EntryDate",
    "MediaDate",
    "RepayEndDate",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "WorkMonth",
    "AgType1",
    "EntryDate",
    "MediaDate",
    "RepayEndDate",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."EmpDeductSchedule";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_EmpDeductSchedule_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;