CREATE OR REPLACE PROCEDURE "Usp_Cp_FacProd_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "FacProd" DROP STORAGE';

  INSERT INTO "FacProd" (
    "ProdNo",
    "ProdName",
    "StartDate",
    "EndDate",
    "StatusCode",
    "AgreementFg",
    "EnterpriseFg",
    "CurrencyCode",
    "BaseRateCode",
    "ProdIncr",
    "LowLimitRate",
    "IncrFlag",
    "RateCode",
    "GovOfferFlag",
    "FinancialFlag",
    "EmpFlag",
    "BreachFlag",
    "BreachCode",
    "BreachGetCode",
    "ProhibitMonth",
    "BreachPercent",
    "BreachDecreaseMonth",
    "BreachDecrease",
    "BreachStartPercent",
    "Ifrs9StepProdCode",
    "Ifrs9ProdCode",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "ProdNo",
    "ProdName",
    "StartDate",
    "EndDate",
    "StatusCode",
    "AgreementFg",
    "EnterpriseFg",
    "CurrencyCode",
    "BaseRateCode",
    "ProdIncr",
    "LowLimitRate",
    "IncrFlag",
    "RateCode",
    "GovOfferFlag",
    "FinancialFlag",
    "EmpFlag",
    "BreachFlag",
    "BreachCode",
    "BreachGetCode",
    "ProhibitMonth",
    "BreachPercent",
    "BreachDecreaseMonth",
    "BreachDecrease",
    "BreachStartPercent",
    "Ifrs9StepProdCode",
    "Ifrs9ProdCode",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."FacProd";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_FacProd_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;