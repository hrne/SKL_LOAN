CREATE OR REPLACE PROCEDURE "Usp_Cp_BatxRateChange_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "BatxRateChange" DROP STORAGE';

  INSERT INTO "BatxRateChange" (
    "AdjDate",
    "CustNo",
    "FacmNo",
    "BormNo",
    "TxKind",
    "DrawdownAmt",
    "CityCode",
    "AreaCode",
    "IncrFlag",
    "AdjCode",
    "RateKeyInCode",
    "ConfirmFlag",
    "TotBalance",
    "LoanBalance",
    "PresEffDate",
    "CurtEffDate",
    "PreNextAdjDate",
    "PreNextAdjFreq",
    "PrevIntDate",
    "CustCode",
    "ProdNo",
    "RateIncr",
    "ContractRate",
    "PresentRate",
    "ProposalRate",
    "AdjustedRate",
    "ContrBaseRate",
    "ContrRateIncr",
    "IndividualIncr",
    "BaseRateCode",
    "RateCode",
    "CurrBaseRate",
    "TxEffectDate",
    "TxRateAdjFreq",
    "JsonFields",
    "OvduTerm",
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
    "TxKind",
    "DrawdownAmt",
    "CityCode",
    "AreaCode",
    "IncrFlag",
    "AdjCode",
    "RateKeyInCode",
    "ConfirmFlag",
    "TotBalance",
    "LoanBalance",
    "PresEffDate",
    "CurtEffDate",
    "PreNextAdjDate",
    "PreNextAdjFreq",
    "PrevIntDate",
    "CustCode",
    "ProdNo",
    "RateIncr",
    "ContractRate",
    "PresentRate",
    "ProposalRate",
    "AdjustedRate",
    "ContrBaseRate",
    "ContrRateIncr",
    "IndividualIncr",
    "BaseRateCode",
    "RateCode",
    "CurrBaseRate",
    "TxEffectDate",
    "TxRateAdjFreq",
    "JsonFields",
    "OvduTerm",
    "TitaTlrNo",
    "TitaTxtNo",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."BatxRateChange";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_BatxRateChange_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;