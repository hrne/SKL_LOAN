CREATE OR REPLACE PROCEDURE "Usp_L7_Ias39LoanCommit_Upd"
(
-- 程式功能：維護 Ias39LoanCommit 每月IAS39放款承諾明細檔
-- 執行時機：每月底日終批次(換日前)
-- 執行方式：EXEC "Usp_L7_Ias39LoanCommit_Upd"(20200521,'999999');
--

    -- 參數
    TBSDYF         IN  INT,        -- 系統營業日(西元)
    EmpNo          IN  VARCHAR2    -- 經辦
)
AS
BEGIN
  DECLARE
    YYYYMM         INT;         -- 本月年月
  BEGIN

    --　本月年月
    YYYYMM := TBSDYF / 100;

    -- 刪除舊資料
    DELETE FROM "Ias39LoanCommit"
    WHERE "DataYm" = YYYYMM
    ;

    -- 寫入資料
    INSERT INTO "Ias39LoanCommit"
    SELECT YYYYMM                               AS "DataYm"            -- 資料年月
         , M."CustNo"                           AS "CustNo"            -- 戶號
         , M."FacmNo"                           AS "FacmNo"            -- 額度編號
         , NVL(M."ApplNo",0)                    AS "ApplNo"            -- 核准號碼
         , NVL(M."ApproveDate",0)               AS "ApproveDate"       -- 核准日期
         , NVL(M."FirstDrawdownDate",0)         AS "FirstDrawdownDate" -- 初貸日期
         , NVL(M."MaturityDate",0)              AS "MaturityDate"      -- 到期日
         , NVL(F."LoanTermYy",0)                AS "LoanTermYy"        -- 貸款期間年
         , NVL(F."LoanTermMm",0)                AS "LoanTermMm"        -- 貸款期間月
         , NVL(F."LoanTermDd",0)                AS "LoanTermDd"        -- 貸款期間日
         , NVL(M."UtilDeadline",0)              AS "UtilDeadline"      -- 動支期限
         , NVL(F."RecycleDeadline",0)           AS "RecycleDeadline"   -- 循環動用期限
         , NVL(M."LineAmt",0)                   AS "LineAmt"           -- 核准額度
         , NVL(M."UtilAmt",0)                   AS "UtilBal"           -- 放款餘額
         , CASE
             WHEN NVL(M."LineAmt",0) > NVL(M."UtilBal",0)
             THEN NVL(M."LineAmt",0) - NVL(M."UtilBal",0)
           ELSE 0
           END                                  AS "AvblBal"           -- 可動用餘額
         , M."RecycleCode"                      AS "RecycleCode"       -- 該筆額度是否可循環動用  0:非循環動用 1:循環動用
         , M."IrrevocableFlag"                  AS "IrrevocableFlag"   -- 該筆額度是否為不可撤銷  0:可撤銷     1:不可撤銷
         , M."AcBookCode"                       AS "AcBookCode"        -- 帳冊別
         , M."AcSubBookCode"                    AS "AcSubBookCode"     -- 區隔帳冊
         , CASE
             WHEN ( NVL(F."LoanTermYy",0) * 12 + NVL(F."LoanTermMm",0) + CEIL( NVL(F."LoanTermDd",0) / 12) ) <= 12
             THEN 20
           ELSE 50
           END                                  AS "Ccf"               -- 信用風險轉換係數  --對照表 
         , 0                                    AS "ExpLimitAmt"       -- 表外曝險金額  --對照表 
         -- 固定會計科目:90800000000-不可撤銷放款承諾-表外曝險金額
         , '90800000000'                        AS "DbAcNoCode"        -- 借方：備忘分錄會計科目
         -- 固定會計科目:90810000000-待抵銷不可撤銷放款承諾-表外曝險金額
         , '90810000000'                        AS "CrAcNoCode"        -- 貸方：備忘分錄會計科目
         , M."DrawdownFg"                       AS "DrawdownFg"        -- 已核撥記號  0:未核撥  1:已核撥
         , SYSTIMESTAMP                         AS "CreateDate"        -- 建檔日期時間
         , EmpNo                                AS "CreateEmpNo"       -- 建檔人員
         , SYSTIMESTAMP                         AS "LastUpdate"        -- 最後更新日期時間
         , EmpNo                                AS "LastUpdateEmpNo"   -- 最後更新人員
    FROM "Ifrs9FacData" M 
    LEFT JOIN "FacMain" F ON F."CustNo" = M."CustNo"
                         AND F."FacmNo" = M."FacmNo"
    WHERE M."DataYM" = YYYYMM
      AND M."IrrevocableFlag" = 1   -- 該筆額度為不可徹銷
      AND ( TRUNC(NVL(M."UtilDeadline", 0) / 100 )  > YYYYMM  OR 
            TRUNC(NVL(F."RecycleDeadline", 0) / 100 )  > YYYYMM )
      AND TRUNC(NVL(M."ApproveDate", 0) / 100 )  <= YYYYMM      
    ;

    --   更新 ExpLimitAmt 表外曝險金額
    MERGE INTO "Ias39LoanCommit" M
    USING (
      SELECT "DataYm"
           , "CustNo"
           , "FacmNo"
           , "AvblBal"
           , "Ccf"
           , "ExpLimitAmt"
      FROM  "Ias39LoanCommit"
      WHERE "DataYm" = YYYYMM
    ) B
    ON (    M."DataYm"    = B."DataYm"
        AND M."CustNo"    = B."CustNo"
        AND M."FacmNo"    = B."FacmNo" )
    WHEN MATCHED THEN UPDATE
    SET M."ExpLimitAmt" = ROUND( NVL(B."AvblBal",0) * NVL(B."Ccf",0) / 100 ,2) 
    ;

    commit;

  END;
END;
