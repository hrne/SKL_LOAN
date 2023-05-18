CREATE OR REPLACE PROCEDURE "Usp_Cp_AchAuthLogHistory_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "AchAuthLogHistory" DROP STORAGE';

  INSERT INTO "AchAuthLogHistory" (
    "LogNo",
    "CustNo",
    "FacmNo",
    "AuthCreateDate",
    "RepayBank",
    "RepayAcct",
    "CreateFlag",
    "ProcessDate",
    "ProcessTime",
    "StampFinishDate",
    "AuthStatus",
    "AuthMeth",
    "LimitAmt",
    "MediaCode",
    "BatchNo",
    "PropDate",
    "RetrDate",
    "DeleteDate",
    "RelationCode",
    "RelAcctName",
    "RelationId",
    "RelAcctBirthday",
    "RelAcctGender",
    "AmlRsp",
    "CreateEmpNo",
    "CreateDate",
    "LastUpdateEmpNo",
    "LastUpdate"
)
  SELECT
    "LogNo",
    "CustNo",
    "FacmNo",
    "AuthCreateDate",
    "RepayBank",
    "RepayAcct",
    "CreateFlag",
    "ProcessDate",
    "ProcessTime",
    "StampFinishDate",
    "AuthStatus",
    "AuthMeth",
    "LimitAmt",
    "MediaCode",
    "BatchNo",
    "PropDate",
    "RetrDate",
    "DeleteDate",
    "RelationCode",
    "RelAcctName",
    "RelationId",
    "RelAcctBirthday",
    "RelAcctGender",
    "AmlRsp",
    "CreateEmpNo",
    "CreateDate",
    "LastUpdateEmpNo",
    "LastUpdate"
  FROM ITXADMIN."AchAuthLogHistory";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_AchAuthLogHistory_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;