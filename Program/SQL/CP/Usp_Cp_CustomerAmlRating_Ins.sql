CREATE OR REPLACE PROCEDURE "Usp_Cp_CustomerAmlRating_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "CustomerAmlRating" DROP STORAGE';

  INSERT INTO "CustomerAmlRating" (
    "CustId",
    "AmlRating",
    "IsRelated",
    "IsLnrelNear",
    "IsLimit",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "CustId",
    "AmlRating",
    "IsRelated",
    "IsLnrelNear",
    "IsLimit",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."CustomerAmlRating";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_CustomerAmlRating_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;