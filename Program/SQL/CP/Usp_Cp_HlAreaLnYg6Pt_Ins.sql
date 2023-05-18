CREATE OR REPLACE PROCEDURE "Usp_Cp_HlAreaLnYg6Pt_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "HlAreaLnYg6Pt" DROP STORAGE';

  INSERT INTO "HlAreaLnYg6Pt" (
    "WorkYM",
    "AreaCode",
    "LstAppNum",
    "LstAppAmt",
    "TisAppNum",
    "TisAppAmt",
    "CalDate",
    "UpNo",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "WorkYM",
    "AreaCode",
    "LstAppNum",
    "LstAppAmt",
    "TisAppNum",
    "TisAppAmt",
    "CalDate",
    "UpNo",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."HlAreaLnYg6Pt";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_HlAreaLnYg6Pt_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;