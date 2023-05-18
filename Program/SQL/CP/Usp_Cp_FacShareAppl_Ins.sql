CREATE OR REPLACE PROCEDURE "Usp_Cp_FacShareAppl_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "FacShareAppl" DROP STORAGE';

  INSERT INTO "FacShareAppl" (
    "ApplNo",
    "MainApplNo",
    "CustNo",
    "FacmNo",
    "KeyinSeq",
    "JcicMergeFlag",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "ApplNo",
    "MainApplNo",
    "CustNo",
    "FacmNo",
    "KeyinSeq",
    "JcicMergeFlag",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."FacShareAppl";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_FacShareAppl_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;