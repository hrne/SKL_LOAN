CREATE OR REPLACE PROCEDURE "Usp_Cp_CdAcCode_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "CdAcCode" DROP STORAGE';

  INSERT INTO "CdAcCode" (
    "AcNoCode",
    "AcSubCode",
    "AcDtlCode",
    "AcNoItem",
    "AcctCode",
    "AcctItem",
    "ClassCode",
    "AcBookFlag",
    "DbCr",
    "AcctFlag",
    "ReceivableFlag",
    "ClsChkFlag",
    "InuseFlag",
    "AcNoCodeOld",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "AcNoCode",
    "AcSubCode",
    "AcDtlCode",
    "AcNoItem",
    "AcctCode",
    "AcctItem",
    "ClassCode",
    "AcBookFlag",
    "DbCr",
    "AcctFlag",
    "ReceivableFlag",
    "ClsChkFlag",
    "InuseFlag",
    "AcNoCodeOld",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."CdAcCode";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_CdAcCode_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;