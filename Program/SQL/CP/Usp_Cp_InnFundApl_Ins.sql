CREATE OR REPLACE PROCEDURE "Usp_Cp_InnFundApl_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "InnFundApl" DROP STORAGE';

  INSERT INTO "InnFundApl" (
    "AcDate",
    "ResrvStndrd",
    "PosbleBorPsn",
    "PosbleBorAmt",
    "AlrdyBorAmt",
    "StockHoldersEqt",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "AcDate",
    "ResrvStndrd",
    "PosbleBorPsn",
    "PosbleBorAmt",
    "AlrdyBorAmt",
    "StockHoldersEqt",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."InnFundApl";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_InnFundApl_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;