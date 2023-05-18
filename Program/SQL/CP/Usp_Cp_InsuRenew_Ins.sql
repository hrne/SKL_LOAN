CREATE OR REPLACE PROCEDURE "Usp_Cp_InsuRenew_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "InsuRenew" DROP STORAGE';

  INSERT INTO "InsuRenew" (
    "ClCode1",
    "ClCode2",
    "ClNo",
    "PrevInsuNo",
    "EndoInsuNo",
    "InsuYearMonth",
    "CustNo",
    "FacmNo",
    "NowInsuNo",
    "OrigInsuNo",
    "RenewCode",
    "InsuCompany",
    "InsuTypeCode",
    "RepayCode",
    "FireInsuCovrg",
    "EthqInsuCovrg",
    "FireInsuPrem",
    "EthqInsuPrem",
    "InsuStartDate",
    "InsuEndDate",
    "TotInsuPrem",
    "AcDate",
    "TitaTlrNo",
    "TitaTxtNo",
    "NotiTempFg",
    "StatusCode",
    "OvduDate",
    "OvduNo",
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
    "PrevInsuNo",
    "EndoInsuNo",
    "InsuYearMonth",
    "CustNo",
    "FacmNo",
    "NowInsuNo",
    "OrigInsuNo",
    "RenewCode",
    "InsuCompany",
    "InsuTypeCode",
    "RepayCode",
    "FireInsuCovrg",
    "EthqInsuCovrg",
    "FireInsuPrem",
    "EthqInsuPrem",
    "InsuStartDate",
    "InsuEndDate",
    "TotInsuPrem",
    "AcDate",
    "TitaTlrNo",
    "TitaTxtNo",
    "NotiTempFg",
    "StatusCode",
    "OvduDate",
    "OvduNo",
    "CommericalFlag",
    "InsuReceiptDate",
    "Remark",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."InsuRenew";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_InsuRenew_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;