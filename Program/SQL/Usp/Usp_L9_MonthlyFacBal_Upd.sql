CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_L9_MonthlyLM052LoanAsset_Ins" 
(
    -- 參數
    TYYMM           IN  INT,       -- 本月資料年月(西元)
    EmpNo          IN  VARCHAR2,   -- 經辦
    JobTxSeq       IN  VARCHAR2    -- 啟動批次的交易序號

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
    FROM( SELECT M."YearMonth"
                , CASE
                    --建築貸款
                    WHEN CDI."IndustryCode" IS NOT NULL THEN 'S2' 
                    --民國一百年後政策性貸款
                    WHEN (REGEXP_LIKE(M."ProdNo",'I[A-Z]')
                      OR REGEXP_LIKE(M."ProdNo",'8[1]')
                      OR (M."AcctCode" = '990' AND M."FacAcctCode" = '340')
                      OR M."AcctCode" = '340')
                      AND F."FirstDrawdownDate" >= 20110101 THEN 'NS1' 
                    --股票質押
                    WHEN M."ClCode1" IN (3,4) THEN 'NS2' 
                    --購置不動產+修繕貸款
                    WHEN M."ClCode1" IN (1,2) 
                      AND F."UsageCode" = '02' 
                      AND M."ProdNo" NOT IN ('60','61','62') THEN 'S1'
                    --個金不動產抵押貸款
                    ELSE 'NS3'  END        AS "LoanAssetCode"	--放款資產項目
              ,SUM(M."PrinBalance") AS "LoanBal"	  
          FROM "MonthlyFacBal" M
          LEFT JOIN "FacMain" F ON F."CustNo" = M."CustNo"
                               AND F."FacmNo" = M."FacmNo"
          LEFT JOIN "CustMain" CM ON CM."CustNo" = M."CustNo"
          LEFT JOIN ( SELECT DISTINCT SUBSTR("IndustryCode",3,4) AS "IndustryCode"
                            ,"IndustryItem"
                      FROM "CdIndustry"
                      WHERE "IndustryItem" LIKE '%不動產%'
                         OR "IndustryItem" LIKE '%建築%'
                      ) CDI ON CDI."IndustryCode" = SUBSTR(CM."IndustryCode",3,4)
          WHERE M."PrinBalance" > 0
            AND M."YearMonth" = TYYMM 
          GROUP BY  M."YearMonth"
                , CASE
                    --建築貸款
                    WHEN CDI."IndustryCode" IS NOT NULL THEN 'S2' 
                    --民國一百年後政策性貸款
                    WHEN (REGEXP_LIKE(M."ProdNo",'I[A-Z]')
                      OR REGEXP_LIKE(M."ProdNo",'8[1]')
                      OR (M."AcctCode" = '990' AND M."FacAcctCode" = '340')
                      OR M."AcctCode" = '340')
                      AND F."FirstDrawdownDate" >= 20110101 THEN 'NS1' 
                    --股票質押
                    WHEN M."ClCode1" IN (3,4) THEN 'NS2' 
                    --購置不動產+修繕貸款
                    WHEN M."ClCode1" IN (1,2) 
                      AND F."UsageCode" = '02' 
                      AND M."ProdNo" NOT IN ('60','61','62') THEN 'S1'
                    --個金不動產抵押貸款
                    ELSE 'NS3'  END 
          UNION
          SELECT TRUNC("AcDate" / 100 )   AS "YearMonth"     --資料年月
                ,'NS3'             AS "LoanAssetCode" --放款資產項目代號
                ,SUM("TdBal")  AS "LoanBal"       --折溢價與催收費用
          FROM "CoreAcMain"
          WHERE "AcNoCode" IN ( '10600304000'    --擔保放款-折溢價
                               ,'10601301000'    --催收款項-法務費用
                               ,'10601302000'    --催收款項-火險費用
                               ,'10601304000')   --催收款項-折溢價
                  AND TRUNC("AcDate" / 100 ) = TYYMM
          GROUP BY TRUNC("AcDate" / 100 )
            )
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
      , JobTxSeq -- 啟動批次的交易序號
    );
    COMMIT; 
    RAISE; 
  END;
END;


