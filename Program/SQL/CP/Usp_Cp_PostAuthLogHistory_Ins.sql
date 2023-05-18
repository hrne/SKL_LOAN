CREATE OR REPLACE PROCEDURE "Usp_Cp_PostAuthLogHistory_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "PostAuthLogHistory" DROP STORAGE';

  INSERT INTO "PostAuthLogHistory" (
    "LogNo",
    "CustNo",
    "FacmNo",
    "AuthCode",
    "AuthCreateDate",
    "AuthApplCode",
    "PostDepCode",
    "RepayAcct",
    "CustId",
    "LimitAmt",
    "RepayAcctSeq",
    "ProcessDate",
    "ProcessTime",
    "StampFinishDate",
    "StampCancelDate",
    "StampCode",
    "PostMediaCode",
    "AuthErrorCode",
    "FileSeq",
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
    "AuthCode",
    "AuthCreateDate",
    "AuthApplCode",
    "PostDepCode",
    "RepayAcct",
    "CustId",
    "LimitAmt",
    "RepayAcctSeq",
    "ProcessDate",
    "ProcessTime",
    "StampFinishDate",
    "StampCancelDate",
    "StampCode",
    "PostMediaCode",
    "AuthErrorCode",
    "FileSeq",
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
  FROM ITXADMIN."PostAuthLogHistory";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_PostAuthLogHistory_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;