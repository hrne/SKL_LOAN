--------------------------------------------------------
--  DDL for Procedure Usp_Tf_MonthlyLM052AssetClass_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_L9_MonthlyLM052AssetClass_Ins" 
(
    -- 參數
    TYYMM           IN  INT,       -- 本月資料年月(西元)
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
                 ,M."AssetClass" ||
                  CASE
                    WHEN M."AssetClass" = 2
                     AND M."AcctCode" = 990
                     AND M."ProdNo" IN ('60','61','62')
                    THEN '3'       --(23)第二類-應予注意：
                               --    有足無擔保--逾繳超過清償期7-12月者
                               --    或無擔保部分--超過清償期1-3月者         
                    WHEN M."AssetClass" = 2
                     AND M."OvduTerm" >= 7
                     AND M."OvduTerm" <= 12
                    THEN '3'        --(23)第二類-應予注意：
                                    --    有足無擔保--逾繳超過清償期7-12月者
                                    --    或無擔保部分--超過清償期1-3月者    
                    WHEN M."AssetClass" = 2
                     AND M."AcctCode" = 990
                     AND M."OvduTerm" <= 12
                    THEN '3'        --(23)第二類-應予注意：
                                    --    有足無擔保--逾繳超過清償期7-12月者
                                    --    或無擔保部分--超過清償期1-3月者    
                    WHEN M."AssetClass" = 2
                     AND M."AcctCode" <> 990
                     AND M."ProdNo" IN ('60','61','62')
                     AND M."OvduTerm" = 0
                    THEN '1'        --(21)第二類-應予注意：
                                    --    有足額擔保--但債信以不良者
                                    --    (有擔保分期協議且正常還款者)
                    WHEN M."AssetClass" = 2
                     AND M."AcctCode" <> 990
                     AND M."OvduTerm" >= 1
                     AND M."OvduTerm" <= 6
                    THEN '2'       --(22)第二類-應予注意：
                                   --    有足無擔保--逾繳超過清償期1-6月者 
                  ELSE '' END AS "AssetClass"  
                 ,M."AcSubBookCode"    AS "AcSubBookCode" --區隔帳冊
                 ,SUM(M."PrinBalance" - M."LawAmount")      AS "Amt"           --放款餘額
           FROM "MonthlyFacBal" M
           WHERE M."PrinBalance" > 0
             AND M."YearMonth" = TYYMM
             AND M."AssetClass" <> 1
           GROUP BY M."YearMonth"
                  ,M."AssetClass" ||
                  CASE
                    WHEN M."AssetClass" = 2
                     AND M."AcctCode" = 990
                     AND M."ProdNo" IN ('60','61','62')
                    THEN '3'       --(23)第二類-應予注意：
                               --    有足無擔保--逾繳超過清償期7-12月者
                               --    或無擔保部分--超過清償期1-3月者         
                    WHEN M."AssetClass" = 2
                     AND M."OvduTerm" >= 7
                     AND M."OvduTerm" <= 12
                    THEN '3'        --(23)第二類-應予注意：
                                    --    有足無擔保--逾繳超過清償期7-12月者
                                    --    或無擔保部分--超過清償期1-3月者    
                    WHEN M."AssetClass" = 2
                     AND M."AcctCode" = 990
                     AND M."OvduTerm" <= 12
                    THEN '3'        --(23)第二類-應予注意：
                                    --    有足無擔保--逾繳超過清償期7-12月者
                                    --    或無擔保部分--超過清償期1-3月者    
                    WHEN M."AssetClass" = 2
                     AND M."AcctCode" <> 990
                     AND M."ProdNo" IN ('60','61','62')
                     AND M."OvduTerm" = 0
                    THEN '1'        --(21)第二類-應予注意：
                                    --    有足額擔保--但債信以不良者
                                    --    (有擔保分期協議且正常還款者)
                    WHEN M."AssetClass" = 2
                     AND M."AcctCode" <> 990
                     AND M."OvduTerm" >= 1
                     AND M."OvduTerm" <= 6
                    THEN '2'       --(22)第二類-應予注意：
                                   --    有足無擔保--逾繳超過清償期1-6月者 
                  ELSE '' END  	  
                   ,M."AcSubBookCode"
           UNION
           SELECT M."YearMonth"
                 ,M."AssetClass" ||
                  CASE
                    WHEN M."ClCode1" IN (1,2) 
                      AND CDI."IndustryItem" LIKE '%不動產%'
                    THEN '12'              -- 特定資產放款：建築貸款
                    WHEN M."ClCode1" IN (1,2) 
                      AND CDI."IndustryItem" LIKE '%建築%'
                    THEN '12'              -- 特定資產放款：建築貸款
                    WHEN M."ClCode1" IN (1,2) 
                      AND F."FirstDrawdownDate" >= 20100101 
                      AND M."FacAcctCode" = 340
                    THEN '11'               -- 正常繳息
                    WHEN M."ClCode1" IN (1,2) 
                      AND F."FirstDrawdownDate" >= 20100101 
                      AND REGEXP_LIKE(M."ProdNo",'I[A-Z]')
                    THEN '11'               -- 正常繳息
                    WHEN M."ClCode1" IN (1,2) 
                      AND F."FirstDrawdownDate" >= 20100101 
                      AND REGEXP_LIKE(M."ProdNo",'8[1-8]')
                    THEN '11'               -- 正常繳息
                    WHEN M."ClCode1" IN (1,2) 
                      AND F."UsageCode" = '02' 
                      AND M."ProdNo" NOT IN ('60','61','62')
                      AND TRUNC(M."PrevIntDate" / 100) >= LYYYYMM
                    THEN '12'       -- 特定資產放款：購置住宅+修繕貸款              
                    ELSE '11'       
                  END                  AS "AssetClass"	--放款資產項目	  
                 ,M."AcSubBookCode"    AS "AcSubBookCode" --區隔帳冊
                 ,SUM(M."PrinBalance" - M."LawAmount")      AS "Amt"           --放款餘額
           FROM "MonthlyFacBal" M
           LEFT JOIN "FacMain" F ON F."CustNo" = M."CustNo"
                                AND F."FacmNo" = M."FacmNo"
           LEFT JOIN "CustMain" CM ON CM."CustNo" = M."CustNo"
           LEFT JOIN ( SELECT DISTINCT SUBSTR("IndustryCode",3,4) AS "IndustryCode"
                             ,"IndustryItem"
                       FROM "CdIndustry" ) CDI ON CDI."IndustryCode" = SUBSTR(CM."IndustryCode",3,4)
           WHERE M."PrinBalance" > 0
             AND M."YearMonth" = TYYMM
             AND M."AssetClass" = 1
           GROUP BY M."YearMonth"
                  ,M."AssetClass" ||
                  CASE
                    WHEN M."ClCode1" IN (1,2) 
                      AND CDI."IndustryItem" LIKE '%不動產%'
                    THEN '12'              -- 特定資產放款：建築貸款
                    WHEN M."ClCode1" IN (1,2) 
                      AND CDI."IndustryItem" LIKE '%建築%'
                    THEN '12'              -- 特定資產放款：建築貸款
                    WHEN M."ClCode1" IN (1,2) 
                      AND F."FirstDrawdownDate" >= 20100101 
                      AND M."FacAcctCode" = 340
                    THEN '11'               -- 正常繳息
                    WHEN M."ClCode1" IN (1,2) 
                      AND F."FirstDrawdownDate" >= 20100101 
                      AND REGEXP_LIKE(M."ProdNo",'I[A-Z]')
                    THEN '11'               -- 正常繳息
                    WHEN M."ClCode1" IN (1,2) 
                      AND F."FirstDrawdownDate" >= 20100101 
                      AND REGEXP_LIKE(M."ProdNo",'8[1-8]')
                    THEN '11'               -- 正常繳息
                    WHEN M."ClCode1" IN (1,2) 
                      AND F."UsageCode" = '02' 
                      AND M."ProdNo" NOT IN ('60','61','62')
                      AND TRUNC(M."PrevIntDate" / 100) >= LYYYYMM
                    THEN '12'       -- 特定資產放款：購置住宅+修繕貸款              
                    ELSE '11'       
                  END  
                   ,M."AcSubBookCode"
           UNION 
           SELECT M."YearMonth"
                 ,'5'                AS "AssetClass"    --第五類
                 ,M."AcSubBookCode"  AS "AcSubBookCode" --區隔帳冊
                 ,SUM(M."LawAmount") AS "Amt"           --無擔保放款餘額
           FROM "MonthlyFacBal" M
           WHERE M."LawAmount" > 0
             AND M."YearMonth" = TYYMM
             AND M."AssetClass" IS NOT NULL
           GROUP BY M."YearMonth"
                   ,'5'
                   ,M."AcSubBookCode"
           UNION 
           SELECT M."YearMonth"
                 ,'5'                AS "AssetClass"    --第五類
                 ,M."AcSubBookCode"  AS "AcSubBookCode" --區隔帳冊
                 ,SUM(M."LawAmount") AS "Amt"           --無擔保放款餘額
           FROM "MonthlyFacBal" M
           WHERE M."LawAmount" > 0
             AND M."YearMonth" = TYYMM
             AND M."AssetClass" IS NOT NULL
           GROUP BY M."YearMonth"
                   ,'5'
                   ,M."AcSubBookCode"
          UNION 
          --擔保放款-折溢價
          SELECT I."YearMonth"           AS "YearMonth"     --資料年月
                ,'61'                    AS "AssetClass"    --資產分類
                ,'999'                   AS "AcSubBookCode" --區隔帳冊
                ,ROUND(SUM(NVL("AccumDPAmortized",0)),0)  AS "Amt"   --(61)擔保品溢折價
          FROM "Ias39IntMethod" I
          LEFT JOIN "MonthlyLoanBal" MLB ON MLB."YearMonth" = I."YearMonth"
                                        AND MLB."CustNo" = I."CustNo"
                                        AND MLB."FacmNo" = I."FacmNo"
                                        AND MLB."BormNo" = I."BormNo"
          WHERE I."YearMonth" = TYYMM 
            AND MLB."AcctCode" <> 990
          GROUP BY I."YearMonth"
                  ,'61'
                  ,'999'
          UNION 
          --催收款項-折溢價與催收費用
          SELECT "MonthEndYm"            AS "YearMonth"     --資料年月
                ,'62'                    AS "AssetClass"    --資產分類
                ,'999'                   AS "AcSubBookCode" --區隔帳冊
                ,SUM("TdBal")            AS "Amt"           --(62)折溢價與催收費用
          FROM "AcMain"
          WHERE "AcNoCode" IN ('10601301000'    --催收款項-法務費用
                              ,'10601302000'    --催收款項-火險費用
                              ,'10601304000')   --催收款項-折溢價
            AND "MonthEndYm" = TYYMM
          GROUP BY "MonthEndYm"
                  ,'62'
                  ,'999'
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


