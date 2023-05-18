CREATE OR REPLACE PROCEDURE "Usp_Cp_Ias34Ep_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "Ias34Ep" DROP STORAGE';

  INSERT INTO "Ias34Ep" (
    "DataYM",
    "CustNo",
    "CustId",
    "FacmNo",
    "BormNo",
    "AcCode",
    "Status",
    "IndustryCode",
    "ClTypeJCIC",
    "Zip3",
    "ProdNo",
    "CustKind",
    "DerFg",
    "Ifrs9ProdCode",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "DataYM",
    "CustNo",
    "CustId",
    "FacmNo",
    "BormNo",
    "AcCode",
    "Status",
    "IndustryCode",
    "ClTypeJCIC",
    "Zip3",
    "ProdNo",
    "CustKind",
    "DerFg",
    "Ifrs9ProdCode",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."Ias34Ep";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_Ias34Ep_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;