CREATE OR REPLACE PROCEDURE "Usp_Cp_Ias39Loss_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "Ias39Loss" DROP STORAGE';

  INSERT INTO "Ias39Loss" (
    "CustNo",
    "FacmNo",
    "MarkDate",
    "MarkCode",
    "MarkRsn",
    "MarkRsnDesc",
    "LosCod",
    "StartDate",
    "EndDate",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "CustNo",
    "FacmNo",
    "MarkDate",
    "MarkCode",
    "MarkRsn",
    "MarkRsnDesc",
    "LosCod",
    "StartDate",
    "EndDate",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."Ias39Loss";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_Ias39Loss_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;