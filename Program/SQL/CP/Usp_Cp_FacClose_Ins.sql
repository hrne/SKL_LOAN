CREATE OR REPLACE PROCEDURE "Usp_Cp_FacClose_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "FacClose" DROP STORAGE';

  INSERT INTO "FacClose" (
    "CustNo",
    "CloseNo",
    "FacmNo",
    "ActFlag",
    "FunCode",
    "CarLoan",
    "ApplDate",
    "CloseDate",
    "CloseInd",
    "CloseReasonCode",
    "CloseAmt",
    "CollectFlag",
    "CollectWayCode",
    "ReceiveDate",
    "TelNo1",
    "TelNo2",
    "TelNo3",
    "EntryDate",
    "AgreeNo",
    "DocNo",
    "DocNoE",
    "ClsNo",
    "Rmk",
    "ReceiveFg",
    "PostAddress",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "CustNo",
    "CloseNo",
    "FacmNo",
    "ActFlag",
    "FunCode",
    "CarLoan",
    "ApplDate",
    "CloseDate",
    "CloseInd",
    "CloseReasonCode",
    "CloseAmt",
    "CollectFlag",
    "CollectWayCode",
    "ReceiveDate",
    "TelNo1",
    "TelNo2",
    "TelNo3",
    "EntryDate",
    "AgreeNo",
    "DocNo",
    "DocNoE",
    "ClsNo",
    "Rmk",
    "ReceiveFg",
    "PostAddress",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."FacClose";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_FacClose_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;