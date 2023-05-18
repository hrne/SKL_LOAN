CREATE OR REPLACE PROCEDURE "Usp_Cp_CdReport_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "CdReport" DROP STORAGE';

  INSERT INTO "CdReport" (
    "FormNo",
    "FormName",
    "Cycle",
    "SendCode",
    "LetterFg",
    "MessageFg",
    "EmailFg",
    "Letter",
    "Message",
    "Email",
    "UsageDesc",
    "SignCode",
    "WatermarkFlag",
    "Enable",
    "Confidentiality",
    "ApLogFlag",
    "GroupNo",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "FormNo",
    "FormName",
    "Cycle",
    "SendCode",
    "LetterFg",
    "MessageFg",
    "EmailFg",
    "Letter",
    "Message",
    "Email",
    "UsageDesc",
    "SignCode",
    "WatermarkFlag",
    "Enable",
    "Confidentiality",
    "ApLogFlag",
    "GroupNo",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."CdReport";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_CdReport_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;