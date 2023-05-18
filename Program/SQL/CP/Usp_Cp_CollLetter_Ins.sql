CREATE OR REPLACE PROCEDURE "Usp_Cp_CollLetter_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "CollLetter" DROP STORAGE';

  INSERT INTO "CollLetter" (
    "CaseCode",
    "CustNo",
    "FacmNo",
    "AcDate",
    "TitaTlrNo",
    "TitaTxtNo",
    "MailTypeCode",
    "MailDate",
    "MailObj",
    "CustName",
    "DelvrYet",
    "DelvrCode",
    "AddressCode",
    "Address",
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
    "MailTypeCode",
    "MailDate",
    "MailObj",
    "CustName",
    "DelvrYet",
    "DelvrCode",
    "AddressCode",
    "Address",
    "Remark",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."CollLetter";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_CollLetter_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;