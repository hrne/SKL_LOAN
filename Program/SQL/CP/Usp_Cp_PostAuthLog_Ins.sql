CREATE OR REPLACE PROCEDURE "Usp_Cp_PostAuthLog_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "PostAuthLog" DROP STORAGE';

  INSERT INTO "PostAuthLog" (
    "AuthCreateDate",
    "AuthApplCode",
    "CustNo",
    "PostDepCode",
    "RepayAcct",
    "AuthCode",
    "FacmNo",
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
    "AuthMeth",
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
    "TitaTxCd",
    "CreateEmpNo",
    "CreateDate",
    "LastUpdateEmpNo",
    "LastUpdate"
)
  SELECT
    "AuthCreateDate",
    "AuthApplCode",
    "CustNo",
    "PostDepCode",
    "RepayAcct",
    "AuthCode",
    "FacmNo",
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
    "AuthMeth",
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
    "TitaTxCd",
    "CreateEmpNo",
    "CreateDate",
    "LastUpdateEmpNo",
    "LastUpdate"
  FROM ITXADMIN."PostAuthLog";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_PostAuthLog_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;