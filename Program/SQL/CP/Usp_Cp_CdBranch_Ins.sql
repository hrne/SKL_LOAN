CREATE OR REPLACE PROCEDURE "Usp_Cp_CdBranch_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "CdBranch" DROP STORAGE';

  INSERT INTO "CdBranch" (
    "BranchNo",
    "AcBranchNo",
    "CRH",
    "BranchStatusCode",
    "BranchShort",
    "BranchItem",
    "BranchAddress1",
    "BranchAddress2",
    "Zip3",
    "Zip2",
    "Owner",
    "BusinessID",
    "RSOCode",
    "MediaUnitCode",
    "CIFKey",
    "LastestCustNo",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "BranchNo",
    "AcBranchNo",
    "CRH",
    "BranchStatusCode",
    "BranchShort",
    "BranchItem",
    "BranchAddress1",
    "BranchAddress2",
    "Zip3",
    "Zip2",
    "Owner",
    "BusinessID",
    "RSOCode",
    "MediaUnitCode",
    "CIFKey",
    "LastestCustNo",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."CdBranch";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_CdBranch_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;