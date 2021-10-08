--------------------------------------------------------
--  DDL for Procedure Usp_Tf_MonthlyLM052LoanAsset_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_L9_MonthlyLM052LoanAsset_Ins" 
(
    -- 參數
    TYYMM           IN  INT,        -- 本月資料年月(西元)
    EmpNo          IN  VARCHAR2    -- 經辦

)
AS
BEGIN
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
    FROM( SELECT "YearMonth"          AS "YearMonth"  --資料年月
                ,CASE
                  WHEN M."ClCode1" IN (1,2) 
                    AND F."FirstDrawdownDate" >= 20100101 
                    AND (M."FacAcctCode" = 340 OR REGEXP_LIKE(M."ProdNo",'I[A-Z]')) 
                  THEN 'NS1'               -- 非特定資產放款：100年後政策性貸款
                  WHEN M."ClCode1" IN (1,2) 
                    AND CDI."IndustryCode" IS NOT NULL 
                  THEN 'S2'                -- 特定資產放款：建築貸款
                  WHEN M."ClCode1" IN (1,2) 
                    AND F."UsageCode" = '02' 
                    AND CDI."IndustryCode" IS NULL 
                  THEN 'S1'                -- 特定資產放款：購置住宅+修繕貸款
                  WHEN M."ClCode1" IN (3,4) 
                  THEN 'NS2'               -- 非特定資產放款：股票質押
                  ELSE 'NS3'               -- 非特定資產放款
                END                  AS "LoanAssetCode"	--放款資產項目代號	  
                ,SUM(M."PrinBalance") AS "LoanBal"
          FROM "MonthlyFacBal" M
          LEFT JOIN "FacMain" F ON F."CustNo" = M."CustNo"
                              AND F."FacmNo" = M."FacmNo"
          LEFT JOIN "CustMain" CM ON CM."CustNo" = M."CustNo"
          LEFT JOIN "CdIndustry" CDI ON CDI."IndustryCode" = CM."IndustryCode"
                                    AND (CDI."IndustryItem" LIKE '不動產%' OR CDI."IndustryItem" LIKE '建築%')
          WHERE M."YearMonth" = TYYMM
            AND M."PrinBalance" > 0
          GROUP BY "YearMonth"
                  ,CASE
                    WHEN M."ClCode1" IN (1,2) 
                      AND F."FirstDrawdownDate" >= 20100101 
                      AND (M."FacAcctCode" = 340 OR REGEXP_LIKE(M."ProdNo",'I[A-Z]'))
                    THEN 'NS1'
                    WHEN M."ClCode1" IN (1,2) 
                      AND CDI."IndustryCode" IS NOT NULL 
                    THEN 'S2'
                    WHEN M."ClCode1" IN (1,2) 
                      AND F."UsageCode" = '02' 
                      AND CDI."IndustryCode" IS NULL 
                    THEN 'S1'
                    WHEN M."ClCode1" IN (3,4) 
                    THEN 'NS2'
                    ELSE 'NS3'
                  END 
          UNION
          SELECT "MonthEndYm"            AS "YearMonth"     --資料年月
                      ,'NS3'                     AS "LoanAssetCode"   --放款資產項目代號
                      ,"DbAmt" - "CrAmt"       AS "LoanBal"           --折溢價與催收費用
                FROM "AcMain"
                WHERE "AcNoCode" IN ( '10600304000'    --擔保放款-折溢價
                                                ,'10601301000'    --催收款項-法務費用
                                                ,'10601302000'    --催收款項-火險費用
                                                ,'10601304000')   --催收款項-折溢價
                  AND "MonthEndYm" = TYYMM)
    GROUP BY "YearMonth"
            ,"LoanAssetCode"
    ;

    INS_CNT := INS_CNT + sql%rowcount;
    -- 記錄程式結束時間
    JOB_END_TIME := SYSTIMESTAMP;

    commit;
  END;
END;


