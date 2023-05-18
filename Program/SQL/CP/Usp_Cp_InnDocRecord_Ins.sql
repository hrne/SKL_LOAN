CREATE OR REPLACE PROCEDURE "Usp_Cp_InnDocRecord_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "InnDocRecord" DROP STORAGE';

  INSERT INTO "InnDocRecord" (
    "CustNo",
    "FacmNo",
    "ApplSeq",
    "TitaActFg",
    "ApplCode",
    "ApplEmpNo",
    "KeeperEmpNo",
    "UsageCode",
    "CopyCode",
    "ApplDate",
    "ReturnDate",
    "ReturnEmpNo",
    "Remark",
    "ApplObj",
    "TitaEntDy",
    "TitaTlrNo",
    "TitaTxtNo",
    "JsonFields",
    "FacmNoMemo",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "CustNo",
    "FacmNo",
    "ApplSeq",
    "TitaActFg",
    "ApplCode",
    "ApplEmpNo",
    "KeeperEmpNo",
    "UsageCode",
    "CopyCode",
    "ApplDate",
    "ReturnDate",
    "ReturnEmpNo",
    "Remark",
    "ApplObj",
    "TitaEntDy",
    "TitaTlrNo",
    "TitaTxtNo",
    "JsonFields",
    "FacmNoMemo",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."InnDocRecord";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_InnDocRecord_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;