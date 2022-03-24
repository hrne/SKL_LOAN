--------------------------------------------------------
--  DDL for Procedure Usp_Tf_MonthlyLM052AssetClass_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "Usp_L9_MonthlyLM052AssetClass_Ins" 
(
    -- 參數
    TYYMM           IN  INT,        -- 本月資料年月(西元)
    EmpNo          IN  VARCHAR2    -- 經辦

)
AS
BEGIN
  -- 執行範例
  -- exec "Usp_L9_MonthlyLM052AssetClass_Ins"(202105,'999999');
  DECLARE
    INS_CNT        INT;         -- 新增筆數
    JOB_START_TIME TIMESTAMP;   -- 記錄程式起始時間
    JOB_END_TIME   TIMESTAMP;   -- 記錄程式結束時間

  BEGIN
    INS_CNT :=0;

    -- 記錄程式起始時間
    JOB_START_TIME := SYSTIMESTAMP;

    -- 刪除舊資料
    DBMS_OUTPUT.PUT_LINE('DELETE MonthlyLM052AssetClass');

    DELETE FROM "MonthlyLM052AssetClass"
    WHERE "YearMonth" = TYYMM 
    ;

    -- 筆數預設0
    INS_CNT:=0;

    -- 記錄程式起始時間
    JOB_START_TIME := SYSTIMESTAMP;

    DBMS_OUTPUT.PUT_LINE('INSERT MonthlyLM052AssetClass');

    -- 寫入資料
    INSERT INTO "MonthlyLM052AssetClass"
    SELECT "YearMonth" 							AS "YearMonth"			--資料年月 DECIMAL 6 0
          ,"AssetClass" 						AS "AssetClassNo"		--資產五分類 VARCHAR2
																				--1~5分類
																				--61：擔保放款-折溢價
                                        --62：催收放款-折溢價與催收費用
																				--7：應收利息
          ,NVL("AcSubBookCode",'N')				AS "AcSubBookCode"  --區隔帳冊 VARCHAR2
																				--00A：一般
																				--201：利變
																				--999：其他(無)
																				--N：NULL
          ,SUM("Amt")								AS "LoanBal"
				  ,JOB_START_TIME           AS "CreateDate"          -- 建檔日期時間 DATE 0 0
				  ,EmpNo                    AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 0
				  ,JOB_START_TIME           AS "LastUpdate"          -- 最後更新日期時間 DATE 0 0
				  ,EmpNo                    AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 0
    FROM ( SELECT M."YearMonth"
                 ,M."AssetClass"         AS "AssetClass"	   --放款資產項目	  
                 ,M."AcSubBookCode"    AS "AcSubBookCode" --區隔帳冊
                 ,M."PrinBalance"      AS "Amt"           --放款餘額
           FROM "MonthlyFacBal" M
           LEFT JOIN "FacMain" F ON F."CustNo" = M."CustNo"
                                AND F."FacmNo" = M."FacmNo"
           LEFT JOIN "CustMain" CM ON CM."CustNo" = M."CustNo"
           LEFT JOIN "CdIndustry" CDI ON CDI."IndustryCode" = CM."IndustryCode"
           WHERE M."PrinBalance" > 0
             AND M."YearMonth" = TYYMM
          UNION 
          --擔保放款-折溢價
          SELECT "MonthEndYm"            AS "YearMonth"     --資料年月
                ,'61'                    AS "AssetClass"    --資產分類
                ,'999'                   AS "AcSubBookCode" --區隔帳冊
                ,"DbAmt" - "CrAmt"       AS "Amt"           --(6)折溢價與催收費用
          FROM "AcMain"
          WHERE "AcNoCode" IN ( '10600304000' )  --擔保放款-折溢價
            AND "MonthEndYm" = TYYMM
          UNION 
          --催收款項-折溢價與催收費用
          SELECT "MonthEndYm"            AS "YearMonth"     --資料年月
                ,'62'                    AS "AssetClass"    --資產分類
                ,'999'                   AS "AcSubBookCode" --區隔帳冊
                ,"DbAmt" - "CrAmt"       AS "Amt"           --(6)折溢價與催收費用
          FROM "AcMain"
          WHERE "AcNoCode" IN ('10601301000'    --催收款項-法務費用
                              ,'10601302000'    --催收款項-火險費用
                              ,'10601304000')   --催收款項-折溢價
            AND "MonthEndYm" = TYYMM
          UNION 
          --應收利息
          SELECT "YearMonth"             AS "YearMonth"     --資料年月
                ,'7'                     AS "AssetClass"    --資產分類
                ,'999'                   AS "AcSubBookCode" --區隔帳冊
                ,SUM("IntAmtAcc")        AS "Amt"           --(7)應收利息
          FROM "MonthlyLoanBal"
          WHERE "LoanBalance" > 0
            AND "YearMonth" = TYYMM
          GROUP BY "YearMonth"
                  ,'7'
                  ,'999')
    GROUP BY "YearMonth" 
            ,"AssetClass"
            ,NVL("AcSubBookCode",'N')
    ;

    INS_CNT := INS_CNT + sql%rowcount;
    -- 記錄程式結束時間
    JOB_END_TIME := SYSTIMESTAMP;

    commit;

    -- 例外處理
    Exception
    WHEN OTHERS THEN
    "Usp_L9_UspErrorLog_Ins"(
        'Usp_L9_MonthlyLM052AssetClass_Ins' -- UspName 預存程序名稱
      , SQLCODE -- Sql Error Code (固定值)
      , SQLERRM -- Sql Error Message (固定值)
      , dbms_utility.format_error_backtrace -- Sql Error Trace (固定值)
      , EmpNo -- 發動預存程序的員工編號
    );
  END;
END;


