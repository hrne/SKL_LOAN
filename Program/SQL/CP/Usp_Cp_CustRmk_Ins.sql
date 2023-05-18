CREATE OR REPLACE PROCEDURE "Usp_Cp_CustRmk_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "CustRmk" DROP STORAGE';

  INSERT INTO "CustRmk" (
    "CustNo",
    "RmkNo",
    "CustUKey",
    "RmkCode",
    "RmkDesc",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "CustNo",
    "RmkNo",
    "CustUKey",
    "RmkCode",
    "RmkDesc",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."CustRmk";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_CustRmk_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;