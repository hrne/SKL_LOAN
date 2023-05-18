CREATE OR REPLACE PROCEDURE "Usp_Cp_CollTel_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "CollTel" DROP STORAGE';

  INSERT INTO "CollTel" (
    "CaseCode",
    "CustNo",
    "FacmNo",
    "AcDate",
    "TitaTlrNo",
    "TitaTxtNo",
    "TelDate",
    "TelTime",
    "ContactCode",
    "RecvrCode",
    "TelArea",
    "TelNo",
    "TelExt",
    "ResultCode",
    "Remark",
    "CallDate",
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
    "TelDate",
    "TelTime",
    "ContactCode",
    "RecvrCode",
    "TelArea",
    "TelNo",
    "TelExt",
    "ResultCode",
    "Remark",
    "CallDate",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."CollTel";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_CollTel_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;