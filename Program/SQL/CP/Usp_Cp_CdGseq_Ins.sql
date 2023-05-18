CREATE OR REPLACE PROCEDURE "Usp_Cp_CdGseq_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "CdGseq" DROP STORAGE';

  INSERT INTO "CdGseq" (
    "GseqDate",
    "GseqCode",
    "GseqType",
    "GseqKind",
    "Offset",
    "SeqNo",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "GseqDate",
    "GseqCode",
    "GseqType",
    "GseqKind",
    "Offset",
    "SeqNo",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."CdGseq";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_CdGseq_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;