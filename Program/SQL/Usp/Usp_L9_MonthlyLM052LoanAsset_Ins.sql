--------------------------------------------------------
--  DDL for Procedure Usp_Tf_MonthlyLM052LoanAsset_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "Usp_L9_MonthlyLM052LoanAsset_Ins" 
(
    -- 參數
    TYYMM           IN  INT,       -- 本月資料年月(西元)
    EmpNo          IN  VARCHAR2    -- 經辦

)
AS
BEGIN
  -- 執行範例
  -- exec "Usp_L9_MonthlyLM052LoanAsset_Ins"(202105,'999999');
  DECLARE
    INS_CNT        INT;         -- 新增筆數
    JOB_START_TIME TIMESTAMP;   -- 記錄程式起始時間
    JOB_END_TIME   TIMESTAMP;   -- 記錄程式結束時間

  BEGIN
    INS_CNT :=0;

    -- 記錄程式起始時間
    JOB_START_TIME := SYSTIMESTAMP;

    -- 刪除舊資料
    DBMS_OUTPUT.PUT_LINE('DELETE MonthlyLM052LoanAsset');

    DELETE FROM "MonthlyLM052LoanAsset"
    WHERE "YearMonth" = TYYMM 
    ;

    -- 筆數預設0
    INS_CNT:=0;

    -- 記錄程式起始時間
    JOB_START_TIME := SYSTIMESTAMP;

    DBMS_OUTPUT.PUT_LINE('INSERT MonthlyLM052LoanAsset');

    -- 寫入資料
    INSERT INTO "MonthlyLM052LoanAsset"
    SELECT "YearMonth"                            AS "YearMonth"           -- 資料年月
          ,"LoanAssetCode"                        AS "LoanAssetCode"	     -- 放款資產項目代號	
          , SUM("LoanBal")                        AS "LoanBal"	           --	放款金額 
          ,JOB_START_TIME                         AS "CreateDate"          -- 建檔日期時間 DATE 0 0
          ,EmpNo                                  AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 0
          ,JOB_START_TIME                         AS "LastUpdate"          -- 最後更新日期時間 DATE 0 0
          ,EmpNo                                  AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 0
    FROM( SELECT M."YearMonth"         AS "YearMonth"  --資料年月
                ,M."AssetClass"        AS "LoanAssetCode"	--放款資產項目代號	  
                ,SUM(M."PrinBalance")  AS "LoanBal"
          FROM "MonthlyFacBal" M
          LEFT JOIN "FacMain" F ON F."CustNo" = M."CustNo"
                              AND F."FacmNo" = M."FacmNo"
          LEFT JOIN "CustMain" CM ON CM."CustNo" = M."CustNo"
          LEFT JOIN "CdIndustry" CDI ON CDI."IndustryCode" = CM."IndustryCode"
          WHERE M."YearMonth" = TYYMM
            AND M."PrinBalance" > 0
            AND SUBSTR(M."AssetClass",0,1) = '1'
          GROUP BY M."YearMonth"     --資料年月
                  ,M."AssetClass"        
          UNION
          SELECT "MonthEndYm"            AS "YearMonth"     --資料年月
                ,'NS3'                   AS "LoanAssetCode" --放款資產項目代號
                ,SUM("DbAmt" - "CrAmt")  AS "LoanBal"       --折溢價與催收費用
          FROM "AcMain"
          WHERE "AcNoCode" IN ( '10600304000'    --擔保放款-折溢價
                               ,'10601301000'    --催收款項-法務費用
                               ,'10601302000'    --催收款項-火險費用
                               ,'10601304000')   --催收款項-折溢價
                  AND "MonthEndYm" = TYYMM
          GROUP BY "MonthEndYm"
				  ,'NS3')
    GROUP BY "YearMonth"
            ,"LoanAssetCode"
    ;

    INS_CNT := INS_CNT + sql%rowcount;
    -- 記錄程式結束時間
    JOB_END_TIME := SYSTIMESTAMP;

    commit;

    -- 例外處理
    Exception
    WHEN OTHERS THEN
    "Usp_L9_UspErrorLog_Ins"(
        'Usp_L9_MonthlyLM052LoanAsset_Ins' -- UspName 預存程序名稱
      , SQLCODE -- Sql Error Code (固定值)
      , SQLERRM -- Sql Error Message (固定值)
      , dbms_utility.format_error_backtrace -- Sql Error Trace (固定值)
      , EmpNo -- 發動預存程序的員工編號
    );
  END;
END;


