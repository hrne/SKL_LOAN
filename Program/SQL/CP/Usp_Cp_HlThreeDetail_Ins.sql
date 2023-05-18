CREATE OR REPLACE PROCEDURE "Usp_Cp_HlThreeDetail_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "HlThreeDetail" DROP STORAGE';

  INSERT INTO "HlThreeDetail" (
    "BrNo",
    "CustNo",
    "FacmNo",
    "ActAmt",
    "PieceCode",
    "CntingCode",
    "TActAmt",
    "EmpNo",
    "EmpId",
    "EmpName",
    "DeptCode",
    "DistCode",
    "UnitCode",
    "DeptName",
    "DistName",
    "UnitName",
    "FirAppDate",
    "BiReteNo",
    "TwoYag",
    "ThreeYag",
    "TwoPay",
    "ThreePay",
    "UnitChiefNo",
    "UnitChiefName",
    "AreaChiefNo",
    "AreaChiefName",
    "Id3",
    "Id3Name",
    "TeamChiefNo",
    "TeamChiefName",
    "Id0",
    "Id0Name",
    "UpNo",
    "CalDate",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "BrNo",
    "CustNo",
    "FacmNo",
    "ActAmt",
    "PieceCode",
    "CntingCode",
    "TActAmt",
    "EmpNo",
    "EmpId",
    "EmpName",
    "DeptCode",
    "DistCode",
    "UnitCode",
    "DeptName",
    "DistName",
    "UnitName",
    "FirAppDate",
    "BiReteNo",
    "TwoYag",
    "ThreeYag",
    "TwoPay",
    "ThreePay",
    "UnitChiefNo",
    "UnitChiefName",
    "AreaChiefNo",
    "AreaChiefName",
    "Id3",
    "Id3Name",
    "TeamChiefNo",
    "TeamChiefName",
    "Id0",
    "Id0Name",
    "UpNo",
    "CalDate",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."HlThreeDetail";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_HlThreeDetail_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;