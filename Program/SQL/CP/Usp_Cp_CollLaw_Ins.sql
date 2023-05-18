CREATE OR REPLACE PROCEDURE "Usp_Cp_CollLaw_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "CollLaw" DROP STORAGE';

  INSERT INTO "CollLaw" (
    "CaseCode",
    "ClCode1",
    "ClCode2",
    "ClNo",
    "CustNo",
    "FacmNo",
    "AcDate",
    "TitaTlrNo",
    "TitaTxtNo",
    "RecordDate",
    "LegalProg",
    "Amount",
    "Remark",
    "Memo",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "CaseCode",
    "ClCode1",
    "ClCode2",
    "ClNo",
    "CustNo",
    "FacmNo",
    "AcDate",
    "TitaTlrNo",
    "TitaTxtNo",
    "RecordDate",
    "LegalProg",
    "Amount",
    "Remark",
    "Memo",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."CollLaw";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_CollLaw_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;