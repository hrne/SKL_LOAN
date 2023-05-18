CREATE OR REPLACE PROCEDURE "Usp_Cp_CdBaseRate_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "CdBaseRate" DROP STORAGE';

  INSERT INTO "CdBaseRate" (
    "CurrencyCode",
    "BaseRateCode",
    "EffectDate",
    "BaseRate",
    "Remark",
    "EffectFlag",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "CurrencyCode",
    "BaseRateCode",
    "EffectDate",
    "BaseRate",
    "Remark",
    "EffectFlag",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."CdBaseRate";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_CdBaseRate_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;