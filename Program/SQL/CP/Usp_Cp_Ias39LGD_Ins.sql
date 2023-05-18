CREATE OR REPLACE PROCEDURE "Usp_Cp_Ias39LGD_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "Ias39LGD" DROP STORAGE';

  INSERT INTO "Ias39LGD" (
    "Date",
    "Type",
    "TypeDesc",
    "LGDPercent",
    "Enable",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "Date",
    "Type",
    "TypeDesc",
    "LGDPercent",
    "Enable",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."Ias39LGD";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_Ias39LGD_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;