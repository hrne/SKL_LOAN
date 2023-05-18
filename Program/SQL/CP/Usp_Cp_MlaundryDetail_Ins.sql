CREATE OR REPLACE PROCEDURE "Usp_Cp_MlaundryDetail_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "MlaundryDetail" DROP STORAGE';

  INSERT INTO "MlaundryDetail" (
    "EntryDate",
    "Factor",
    "CustNo",
    "TotalCnt",
    "TotalAmt",
    "Rational",
    "EmpNoDesc",
    "ManagerCheck",
    "ManagerDate",
    "ManagerCheckDate",
    "ManagerDesc",
    "FlEntdy",
    "FlowNo",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "EntryDate",
    "Factor",
    "CustNo",
    "TotalCnt",
    "TotalAmt",
    "Rational",
    "EmpNoDesc",
    "ManagerCheck",
    "ManagerDate",
    "ManagerCheckDate",
    "ManagerDesc",
    "FlEntdy",
    "FlowNo",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."MlaundryDetail";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_MlaundryDetail_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;