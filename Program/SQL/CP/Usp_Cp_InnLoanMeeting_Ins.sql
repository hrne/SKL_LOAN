CREATE OR REPLACE PROCEDURE "Usp_Cp_InnLoanMeeting_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "InnLoanMeeting" DROP STORAGE';

  INSERT INTO "InnLoanMeeting" (
    "MeetNo",
    "MeetingDate",
    "CustCode",
    "Amount",
    "Issue",
    "Remark",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "MeetNo",
    "MeetingDate",
    "CustCode",
    "Amount",
    "Issue",
    "Remark",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."InnLoanMeeting";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_InnLoanMeeting_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;