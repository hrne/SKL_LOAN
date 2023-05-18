CREATE OR REPLACE PROCEDURE "Usp_Cp_CreditRating_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "CreditRating" DROP STORAGE';

  INSERT INTO "CreditRating" (
    "DataYM",
    "CustNo",
    "FacmNo",
    "BormNo",
    "CreditRatingCode",
    "OriModel",
    "OriRatingDate",
    "OriRating",
    "Model",
    "RatingDate",
    "Rating",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "DataYM",
    "CustNo",
    "FacmNo",
    "BormNo",
    "CreditRatingCode",
    "OriModel",
    "OriRatingDate",
    "OriRating",
    "Model",
    "RatingDate",
    "Rating",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."CreditRating";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_CreditRating_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;