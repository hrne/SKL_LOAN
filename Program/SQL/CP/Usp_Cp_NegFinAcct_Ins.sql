CREATE OR REPLACE PROCEDURE "Usp_Cp_NegFinAcct_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "NegFinAcct" DROP STORAGE';

  INSERT INTO "NegFinAcct" (
    "FinCode",
    "FinItem",
    "RemitBank",
    "RemitAcct",
    "DataSendSection",
    "RemitAcct2",
    "RemitAcct3",
    "RemitAcct4",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "FinCode",
    "FinItem",
    "RemitBank",
    "RemitAcct",
    "DataSendSection",
    "RemitAcct2",
    "RemitAcct3",
    "RemitAcct4",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."NegFinAcct";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_NegFinAcct_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;