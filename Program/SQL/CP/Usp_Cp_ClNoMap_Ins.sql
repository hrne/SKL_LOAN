CREATE OR REPLACE PROCEDURE "Usp_Cp_ClNoMap_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "ClNoMap" DROP STORAGE';

  INSERT INTO "ClNoMap" (
    "GdrId1",
    "GdrId2",
    "GdrNum",
    "LgtSeq",
    "MainGdrId1",
    "MainGdrId2",
    "MainGdrNum",
    "MainLgtSeq",
    "ClCode1",
    "ClCode2",
    "ClNo",
    "TfStatus",
    "OriCustNo",
    "OriFacmNo",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "GdrId1",
    "GdrId2",
    "GdrNum",
    "LgtSeq",
    "MainGdrId1",
    "MainGdrId2",
    "MainGdrNum",
    "MainLgtSeq",
    "ClCode1",
    "ClCode2",
    "ClNo",
    "TfStatus",
    "OriCustNo",
    "OriFacmNo",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."ClNoMap";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_ClNoMap_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;