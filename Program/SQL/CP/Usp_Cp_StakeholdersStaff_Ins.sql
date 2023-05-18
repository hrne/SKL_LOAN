CREATE OR REPLACE PROCEDURE "Usp_Cp_StakeholdersStaff_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "StakeholdersStaff" DROP STORAGE';

  INSERT INTO "StakeholdersStaff" (
    "StaffId",
    "StaffName",
    "LoanAmount",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "StaffId",
    "StaffName",
    "LoanAmount",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."StakeholdersStaff";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_StakeholdersStaff_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;