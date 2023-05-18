CREATE OR REPLACE PROCEDURE "Usp_Cp_ReltMain_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "ReltMain" DROP STORAGE';

  INSERT INTO "ReltMain" (
    "CaseNo",
    "CustNo",
    "ReltUKey",
    "ReltCode",
    "RemarkType",
    "Reltmark",
    "FinalFg",
    "ApplDate",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "CaseNo",
    "CustNo",
    "ReltUKey",
    "ReltCode",
    "RemarkType",
    "Reltmark",
    "FinalFg",
    "ApplDate",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."ReltMain";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_ReltMain_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;