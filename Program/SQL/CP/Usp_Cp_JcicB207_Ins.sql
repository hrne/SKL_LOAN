CREATE OR REPLACE PROCEDURE "Usp_Cp_JcicB207_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "JcicB207" DROP STORAGE';

  INSERT INTO "JcicB207" (
    "DataYM",
    "TranCode",
    "BankItem",
    "Filler3",
    "DataDate",
    "CustId",
    "CustName",
    "EName",
    "Birthday",
    "RegAddr",
    "CurrZip",
    "CurrAddr",
    "Tel",
    "Mobile",
    "Filler14",
    "EduCode",
    "OwnedHome",
    "CurrCompName",
    "CurrCompId",
    "JobCode",
    "CurrCompTel",
    "JobTitle",
    "JobTenure",
    "IncomeOfYearly",
    "IncomeDataDate",
    "Sex",
    "NationalityCode",
    "PassportNo",
    "PreTaxNo",
    "FullCustName",
    "Filler30",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "DataYM",
    "TranCode",
    "BankItem",
    "Filler3",
    "DataDate",
    "CustId",
    "CustName",
    "EName",
    "Birthday",
    "RegAddr",
    "CurrZip",
    "CurrAddr",
    "Tel",
    "Mobile",
    "Filler14",
    "EduCode",
    "OwnedHome",
    "CurrCompName",
    "CurrCompId",
    "JobCode",
    "CurrCompTel",
    "JobTitle",
    "JobTenure",
    "IncomeOfYearly",
    "IncomeDataDate",
    "Sex",
    "NationalityCode",
    "PassportNo",
    "PreTaxNo",
    "FullCustName",
    "Filler30",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."JcicB207";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_JcicB207_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;