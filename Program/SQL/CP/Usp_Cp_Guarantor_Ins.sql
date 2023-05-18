CREATE OR REPLACE PROCEDURE "Usp_Cp_Guarantor_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "Guarantor" DROP STORAGE';

  INSERT INTO "Guarantor" (
    "ApproveNo",
    "GuaUKey",
    "GuaRelCode",
    "GuaAmt",
    "GuaTypeCode",
    "GuaDate",
    "GuaStatCode",
    "CancelDate",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "ApproveNo",
    "GuaUKey",
    "GuaRelCode",
    "GuaAmt",
    "GuaTypeCode",
    "GuaDate",
    "GuaStatCode",
    "CancelDate",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."Guarantor";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_Guarantor_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;