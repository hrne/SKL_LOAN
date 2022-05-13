CREATE OR REPLACE PROCEDURE "Usp_Cp_BatxBaseRateChange_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "BatxBaseRateChange" DROP STORAGE';

  INSERT INTO "BatxBaseRateChange" (
    "AdjDate",
    "CustNo",
    "FacmNo",
    "BormNo",
    "ProdNo",
    "BaseRateCode",
    "OriBaseRate",
    "BaseRateEffectDate",
    "BaseRate",
    "FitRate",
    "TxEffectDate",
    "JsonFields",
    "TitaTlrNo",
    "TitaTxtNo",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "AdjDate",
    "CustNo",
    "FacmNo",
    "BormNo",
    "ProdNo",
    "BaseRateCode",
    "OriBaseRate",
    "BaseRateEffectDate",
    "BaseRate",
    "FitRate",
    "TxEffectDate",
    "JsonFields",
    "TitaTlrNo",
    "TitaTxtNo",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."BatxBaseRateChange";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_BatxBaseRateChange_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;