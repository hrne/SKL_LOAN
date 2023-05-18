CREATE OR REPLACE PROCEDURE "Usp_Cp_Ias39IntMethod_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "Ias39IntMethod" DROP STORAGE';

  INSERT INTO "Ias39IntMethod" (
    "YearMonth",
    "CustNo",
    "FacmNo",
    "BormNo",
    "Principal",
    "BookValue",
    "AccumDPAmortized",
    "AccumDPunAmortized",
    "DPAmortized",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "YearMonth",
    "CustNo",
    "FacmNo",
    "BormNo",
    "Principal",
    "BookValue",
    "AccumDPAmortized",
    "AccumDPunAmortized",
    "DPAmortized",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."Ias39IntMethod";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_Ias39IntMethod_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;