CREATE OR REPLACE PROCEDURE "Usp_Cp_CollMeet_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "CollMeet" DROP STORAGE';

  INSERT INTO "CollMeet" (
    "CaseCode",
    "CustNo",
    "FacmNo",
    "AcDate",
    "TitaTlrNo",
    "TitaTxtNo",
    "MeetDate",
    "MeetTime",
    "ContactCode",
    "MeetPsnCode",
    "CollPsnCode",
    "CollPsnName",
    "MeetPlaceCode",
    "MeetPlace",
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
    "MeetDate",
    "MeetTime",
    "ContactCode",
    "MeetPsnCode",
    "CollPsnCode",
    "CollPsnName",
    "MeetPlaceCode",
    "MeetPlace",
    "Remark",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."CollMeet";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_CollMeet_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;