CREATE OR REPLACE NONEDITIONABLE FUNCTION "Fn_GetUnpaidForeclosureFee" 
(
    "InputCustNo"         IN NUMBER,  -- 戶號
    "InputFacmNo"         IN NUMBER,  -- 額度號碼
    "InputBormNo"         IN NUMBER,  -- 撥款序號
    "InputDerogationDate" IN NUMBER   -- 減損發生日
) RETURN NUMBER IS "UnpaidForeclosureFee" DECIMAL(16,2);
BEGIN
     --------------------------------------------------------
     --  DDL for Function Fn_GetUnpaidForeclosureFee
     --  Example:
     --  SELECT "Fn_GetUnpaidForeclosureFee"(M."CustNo", M."FacmNo", M."BormNo", M."DerDate")
     --  FROM "Ias34Dp" M
     --------------------------------------------------------
     -- 2022-7-12 Wei from Linda
     -- 上述發生日期時之法拍及火險費用(台幣),要照餘額分攤=
     -- 法拍: OpenAcDate起帳日期<=發生日 
     --       ,Fee費用金額>0
     --       ,CloseDate銷號日期=0 或 >發生日(發生日當時未銷)
     --       ,費用累加
     -- *** 法拍費用 以同戶號加總 後照 各撥款序號的放款餘額分攤

     DECLARE
          haveUnpaidForeclosureFee NUMBER;
     BEGIN
          SELECT COUNT(1)
          INTO haveUnpaidForeclosureFee
          FROM "ForeclosureFee" FF
          WHERE FF."CustNo" = "InputCustNo"
            -- OpenAcDate起帳日期<=發生日 
            AND FF."OpenAcDate" <= "InputDerogationDate"
            -- CloseDate銷號日期=0 或 >發生日(發生日當時未銷)
            AND CASE
                  WHEN FF."CloseDate" = 0
                  THEN 1
                  WHEN FF."CloseDate" > "InputDerogationDate"
                  THEN 1
                ELSE 0 END = 1
            AND FF."Fee" > 0
          ;

          IF haveUnpaidForeclosureFee = 0 THEN
              RETURN 0;
          END IF;
     END;

     WITH feeData AS (
          SELECT FF."CustNo"
               , SUM("Fee") AS "TotalFee"
          FROM "ForeclosureFee" FF
          WHERE FF."CustNo" = "InputCustNo"
            -- OpenAcDate起帳日期<=發生日 
            AND FF."OpenAcDate" <= "InputDerogationDate"
            -- CloseDate銷號日期=0 或 >發生日(發生日當時未銷)
            AND CASE
                  WHEN FF."CloseDate" = 0
                  THEN 1
                  WHEN FF."CloseDate" > "InputDerogationDate"
                  THEN 1
                ELSE 0 END = 1
            AND FF."Fee" > 0
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
               , ROUND(f."TotalFee" * l."LoanBalance" / s."SumLoanBalance" , 0) AS "SharedFee"
          FROM feeData f
          LEFT JOIN loanBalData l ON l."CustNo" = f."CustNo"
          LEFT JOIN sumData s ON s."CustNo" = f."CustNo"
     )
     , otherSharedData AS (
          SELECT "CustNo"
               , SUM("SharedFee") AS "SharedFee"
          FROM sharedData
          WHERE NOT ("FacmNo" = "InputFacmNo"
                     AND "BormNo" = "InputBormNo"
                    )
          GROUP BY "CustNo"
     )
     SELECT CASE
              WHEN s."MaxSeq" = 1
              THEN f."TotalFee"
              WHEN l."LoanBalSeq" = s."MaxSeq"
              THEN f."TotalFee" - o."SharedFee"
            ELSE sh."SharedFee" END
     INTO "UnpaidForeclosureFee"
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

     RETURN "UnpaidForeclosureFee";

     -- 例外處理
     Exception
     WHEN OTHERS THEN
     RETURN 0;
END;

/
