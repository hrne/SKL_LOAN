CREATE OR REPLACE PROCEDURE "Usp_Cp_CustNotice_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "CustNotice" DROP STORAGE';

  INSERT INTO "CustNotice" (
    "CustNo",
    "FacmNo",
    "FormNo",
    "PaperNotice",
    "MsgNotice",
    "EmailNotice",
    "ApplyDate",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "CustNo",
    "FacmNo",
    "FormNo",
    "PaperNotice",
    "MsgNotice",
    "EmailNotice",
    "ApplyDate",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."CustNotice";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_CustNotice_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;