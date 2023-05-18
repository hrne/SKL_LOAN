CREATE OR REPLACE PROCEDURE "Usp_Cp_TbJcicMu01_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "TbJcicMu01" DROP STORAGE';

  INSERT INTO "TbJcicMu01" (
    "HeadOfficeCode",
    "BranchCode",
    "DataDate",
    "EmpId",
    "Title",
    "AuthQryType",
    "QryUserId",
    "AuthItemQuery",
    "AuthItemReview",
    "AuthItemOther",
    "AuthStartDay",
    "AuthMgrIdS",
    "AuthEndDay",
    "AuthMgrIdE",
    "EmailAccount",
    "ModifyUserId",
    "OutJcictxtDate",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "HeadOfficeCode",
    "BranchCode",
    "DataDate",
    "EmpId",
    "Title",
    "AuthQryType",
    "QryUserId",
    "AuthItemQuery",
    "AuthItemReview",
    "AuthItemOther",
    "AuthStartDay",
    "AuthMgrIdS",
    "AuthEndDay",
    "AuthMgrIdE",
    "EmailAccount",
    "ModifyUserId",
    "OutJcictxtDate",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."TbJcicMu01";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_TbJcicMu01_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;