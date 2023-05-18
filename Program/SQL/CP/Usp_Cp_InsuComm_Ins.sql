CREATE OR REPLACE PROCEDURE "Usp_Cp_InsuComm_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "InsuComm" DROP STORAGE';

  INSERT INTO "InsuComm" (
    "InsuYearMonth",
    "InsuCommSeq",
    "ManagerCode",
    "NowInsuNo",
    "BatchNo",
    "InsuType",
    "InsuSignDate",
    "InsuredName",
    "InsuredAddr",
    "InsuredTeleph",
    "InsuStartDate",
    "InsuEndDate",
    "InsuCate",
    "InsuPrem",
    "CommRate",
    "Commision",
    "TotInsuPrem",
    "TotComm",
    "RecvSeq",
    "ChargeDate",
    "CommDate",
    "CustNo",
    "FacmNo",
    "FireOfficer",
    "EmpId",
    "EmpName",
    "DueAmt",
    "MediaCode",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "InsuYearMonth",
    "InsuCommSeq",
    "ManagerCode",
    "NowInsuNo",
    "BatchNo",
    "InsuType",
    "InsuSignDate",
    "InsuredName",
    "InsuredAddr",
    "InsuredTeleph",
    "InsuStartDate",
    "InsuEndDate",
    "InsuCate",
    "InsuPrem",
    "CommRate",
    "Commision",
    "TotInsuPrem",
    "TotComm",
    "RecvSeq",
    "ChargeDate",
    "CommDate",
    "CustNo",
    "FacmNo",
    "FireOfficer",
    "EmpId",
    "EmpName",
    "DueAmt",
    "MediaCode",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."InsuComm";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_InsuComm_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;