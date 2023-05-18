CREATE OR REPLACE PROCEDURE "Usp_Cp_InsuOrignal_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "InsuOrignal" DROP STORAGE';

  INSERT INTO "InsuOrignal" (
    "ClCode1",
    "ClCode2",
    "ClNo",
    "OrigInsuNo",
    "EndoInsuNo",
    "InsuCompany",
    "InsuTypeCode",
    "FireInsuCovrg",
    "EthqInsuCovrg",
    "FireInsuPrem",
    "EthqInsuPrem",
    "InsuStartDate",
    "InsuEndDate",
    "CommericalFlag",
    "InsuReceiptDate",
    "Remark",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "ClCode1",
    "ClCode2",
    "ClNo",
    "OrigInsuNo",
    "EndoInsuNo",
    "InsuCompany",
    "InsuTypeCode",
    "FireInsuCovrg",
    "EthqInsuCovrg",
    "FireInsuPrem",
    "EthqInsuPrem",
    "InsuStartDate",
    "InsuEndDate",
    "CommericalFlag",
    "InsuReceiptDate",
    "Remark",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."InsuOrignal";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_InsuOrignal_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;