CREATE OR REPLACE PROCEDURE "Usp_Cp_AchAuthLog_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "AchAuthLog" DROP STORAGE';

  INSERT INTO "AchAuthLog" (
    "AuthCreateDate",
    "CustNo",
    "RepayBank",
    "RepayAcct",
    "CreateFlag",
    "FacmNo",
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
    "TitaTxCd",
    "CreateEmpNo",
    "CreateDate",
    "LastUpdateEmpNo",
    "LastUpdate"
)
  SELECT
    "AuthCreateDate",
    "CustNo",
    "RepayBank",
    "RepayAcct",
    "CreateFlag",
    "FacmNo",
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
    "TitaTxCd",
    "CreateEmpNo",
    "CreateDate",
    "LastUpdateEmpNo",
    "LastUpdate"
  FROM ITXADMIN."AchAuthLog";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_AchAuthLog_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;