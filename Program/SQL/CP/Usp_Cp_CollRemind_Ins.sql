CREATE OR REPLACE PROCEDURE "Usp_Cp_CollRemind_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "CollRemind" DROP STORAGE';

  INSERT INTO "CollRemind" (
    "CaseCode",
    "CustNo",
    "FacmNo",
    "AcDate",
    "TitaTlrNo",
    "TitaTxtNo",
    "CondCode",
    "RemindDate",
    "EditDate",
    "EditTime",
    "RemindCode",
    "Remark",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "CaseCode",
    "CustNo",
    "FacmNo",
    "AcDate",
    "TitaTlrNo",
    "TitaTxtNo",
    "CondCode",
    "RemindDate",
    "EditDate",
    "EditTime",
    "RemindCode",
    "Remark",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."CollRemind";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_CollRemind_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;