CREATE OR REPLACE NONEDITIONABLE FUNCTION "Fn_GetUnpaidInsuFee" 
(
    "InputCustNo"         IN NUMBER,  -- 戶號
    "InputFacmNo"         IN NUMBER,  -- 額度號碼
    "InputBormNo"         IN NUMBER,  -- 撥款序號
    "InputDerogationDate" IN NUMBER   -- 減損發生日
) RETURN NUMBER IS "UnpaidInsuFee" DECIMAL(16,2);
BEGIN
     --------------------------------------------------------
     --  DDL for Function Fn_GetUnpaidInsuFee
     --  Example:
     --  SELECT "Fn_GetUnpaidInsuFee"(M."CustNo", M."FacmNo", M."BormNo", M."DerDate")
     --  FROM "Ias34Dp" M
     --------------------------------------------------------
     -- 2022-7-12 Wei from Linda
     -- 上述發生日期時之法拍及火險費用(台幣),要照餘額分攤=
     -- 火險: InsuYearMonth火險年月<=發生日年月
     --       & AcDate會計日期=0 或>發生日(發生日當時未銷)
     --       & RenewCode是否續保=2續保
     --       費用累加
     -- *** 火險費用 以同戶號加總 後照 各撥款序號的放款餘額分攤

     DECLARE
          haveUnpaidInsuFee NUMBER;
     BEGIN
          SELECT COUNT(1)
          INTO haveUnpaidInsuFee
          FROM "InsuRenew" IR
          WHERE IR."CustNo" = "InputCustNo"
            -- InsuYearMonth火險年月<=發生日年月
            AND IR."InsuYearMonth" <= TRUNC("InputDerogationDate" / 100 )
            -- AcDate會計日期=0 或>發生日(發生日當時未銷)
            AND CASE
                  WHEN IR."AcDate" = 0
                  THEN 1
                  WHEN IR."AcDate" > "InputDerogationDate"
                  THEN 1
                ELSE 0 END = 1
            -- RenewCode是否續保=2續保
            AND IR."RenewCode" = 2
            AND IR."TotInsuPrem" > 0
          ;

          IF haveUnpaidInsuFee = 0 THEN
              RETURN 0;
          END IF;
     END;

     WITH feeData AS (
          SELECT IR."CustNo"
               , SUM("TotInsuPrem") AS "TotalInsuFee"
          FROM "InsuRenew" IR
          WHERE IR."CustNo" = "InputCustNo"
            -- InsuYearMonth火險年月<=發生日年月
            AND IR."InsuYearMonth" <= TRUNC("InputDerogationDate" / 100 )
            -- AcDate會計日期=0 或>發生日(發生日當時未銷)
            AND CASE
                  WHEN IR."AcDate" = 0
                  THEN 1
                  WHEN IR."AcDate" > "InputDerogationDate"
                  THEN 1
                ELSE 0 END = 1
            -- RenewCode是否續保=2續保
            AND IR."RenewCode" = 2
          GROUP BY IR."CustNo"
     )
     , loanBalData AS (
          SELECT ML."CustNo"
               , ML."FacmNo"
               , ML."BormNo"
               , ML."LoanBalance"
               , ROW_NUMBER()
                 OVER (
                    ORDER BY ML."CustNo"
                           , ML."FacmNo"
                           , ML."BormNo"
                 ) AS "LoanBalSeq"
          FROM "MonthlyLoanBal" ML
          WHERE ML."YearMonth"  = TRUNC("InputDerogationDate" / 100)
            AND ML."CustNo" = "InputCustNo"
            AND ML."LoanBalance" != 0
     )
     , sumData AS (
          SELECT "CustNo"
               , SUM("LoanBalance") AS "SumLoanBalance"
               , MAX("LoanBalSeq")  AS "MaxSeq"
          FROM loanBalData
          GROUP BY "CustNo"
     )
     , sharedData AS (
          SELECT l."CustNo"
               , l."FacmNo"
               , l."BormNo"
               , ROUND(f."TotalInsuFee" * l."LoanBalance" / s."SumLoanBalance" , 0) AS "SharedInsuFee"
          FROM feeData f
          LEFT JOIN loanBalData l ON l."CustNo" = f."CustNo"
          LEFT JOIN sumData s ON s."CustNo" = f."CustNo"
     )
     , otherSharedData AS (
          SELECT "CustNo"
               , SUM("SharedInsuFee") AS "SharedInsuFee"
          FROM sharedData
          WHERE NOT ("FacmNo" = "InputFacmNo"
                     AND "BormNo" = "InputBormNo"
                    )
          GROUP BY "CustNo"
     )
     SELECT CASE
              WHEN s."MaxSeq" = 1
              THEN f."TotalInsuFee"
              WHEN l."LoanBalSeq" = s."MaxSeq"
              THEN f."TotalInsuFee" - o."SharedInsuFee"
            ELSE sh."SharedInsuFee" END
     INTO "UnpaidInsuFee"
     FROM feeData f
     LEFT JOIN loanBalData l ON l."CustNo" = f."CustNo"
                            AND l."FacmNo" = "InputFacmNo"
                            AND l."BormNo" = "InputBormNo"
     LEFT JOIN sumData s ON s."CustNo" = f."CustNo"
     LEFT JOIN sharedData sh ON sh."CustNo" = f."CustNo"
                            AND sh."FacmNo" = "InputFacmNo"
                            AND sh."BormNo" = "InputBormNo"
     LEFT JOIN otherSharedData o ON o."CustNo" = f."CustNo"
     ;

     RETURN "UnpaidInsuFee";

     -- 例外處理
     Exception
     WHEN OTHERS THEN
     RETURN 0;
END;

/
