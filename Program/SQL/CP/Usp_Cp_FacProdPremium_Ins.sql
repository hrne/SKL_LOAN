CREATE OR REPLACE PROCEDURE "Usp_Cp_FacProdPremium_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "FacProdPremium" DROP STORAGE';

  INSERT INTO "FacProdPremium" (
    "ProdNo",
    "PremiumLow",
    "PremiumHigh",
    "PremiumIncr",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "ProdNo",
    "PremiumLow",
    "PremiumHigh",
    "PremiumIncr",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."FacProdPremium";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_FacProdPremium_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;