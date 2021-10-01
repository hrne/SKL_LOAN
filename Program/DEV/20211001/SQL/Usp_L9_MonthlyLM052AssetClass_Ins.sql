--------------------------------------------------------
--  DDL for Procedure Usp_Tf_MonthlyLM052AssetClass_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_L9_MonthlyLM052AssetClass_Ins" 
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
    SELECT "YearMonth"              AS "YearMonth"           -- 資料年月 DECIMAL 6 0
          ,"AssetClass"             AS "AssetClassNo"        -- 資產五分類 VARCHAR2
          ,NVL("AcSubBookCode",'N') AS "AcSubBookCode"       -- 區隔帳冊 VARCHAR2 3
                                                             --   00A：一般
                                                             --   201：利變
                                                             --   999：其他(無)
                                                             --   N：NULL
          ,SUM("Amt")               AS "LoanBal"             -- 放款餘額 DECIMAL 16 2
          ,JOB_START_TIME           AS "CreateDate"          -- 建檔日期時間 DATE 0 0
          ,EmpNo                    AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 0
          ,JOB_START_TIME           AS "LastUpdate"          -- 最後更新日期時間 DATE 0 0
          ,EmpNo                    AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 0
    FROM ( SELECT "YearMonth" 
                  ,CASE
                    WHEN M."PrinBalance" = 1
                    THEN '5'        --(5)第五類-收回無望(應為法務進度901，現暫以餘額掛1為第五類)
                                    --   無擔保部分--超過清償期12月者
                                    --   或拍訂貨拍賣無實益之損失者
                                    --   或放款資產經評估無法回收者                         
                    WHEN M."OvduTerm" >= 12
                      AND F."ProdNo" IN ('60','61','62') 
                    THEN '23'                             
                    WHEN M."OvduTerm" >= 12
                    THEN '3'        --(3)第三類-可望收回：
                                    --   有足無擔保--逾繳超過清償期12月者
                                    --   或無擔保部分--超過清償期3-6月者   
                    WHEN M."OvduTerm" >= 7
                    THEN '23'       --(23)第二類-應予注意：
                                    --    有足無擔保--逾繳超過清償期7-12月者
                                    --    或無擔保部分--超過清償期1-3月者
                    WHEN M."OvduTerm" >= 1
                    THEN '22'       --(22)第二類-應予注意：
                                    --    有足無擔保--逾繳超過清償期1-6月者
                    WHEN F."ProdNo" IN ('60','61','62') 
                    THEN '21'       --(21)第二類-應予注意：
                                    --    有足額擔保--但債信以不良者
                                    --    (有擔保分期協議且正常還款者)
                    WHEN CDI."IndustryCode" IS NULL 
                      AND F."UsageCode" = '02'
                    THEN '11'       --(11)第一類-正常類：正常繳息
                    ELSE '12'       --12)第一類-正常類：特定放款資產項目
                    END                           AS "AssetClass"     --資產五分類
                  ,"AcSubBookCode"                AS "AcSubBookCode"    --區隔帳冊
                  ,M."PrinBalance"                AS "Amt"              --放款餘額
           FROM "MonthlyFacBal" M
           LEFT JOIN "FacMain" F ON F."CustNo" = M."CustNo"
									              AND F."FacmNo" = M."FacmNo"
				   LEFT JOIN "CustMain" CM ON CM."CustNo" = M."CustNo"
				   LEFT JOIN "CdIndustry" CDI ON CDI."IndustryCode" = CM."IndustryCode"
										                 AND (CDI."IndustryItem" LIKE '不動產%' OR CDI."IndustryItem" LIKE '建築%')
				   WHERE M."PrinBalance" > 0
             AND M."YearMonth" = TYYMM
				   UNION 
          --折溢價與催收費用
           SELECT "MonthEndYm"            AS "YearMonth"     --資料年月
                 ,'6'                     AS "AssetClass"    --資產分類
                 ,'999'                   AS "AcSubBookCode" --區隔帳冊
                 ,"DbAmt" - "CrAmt"       AS "Amt"           --(6)折溢價與催收費用
					 FROM "AcMain"
					 WHERE "AcNoCode" IN ( '10600304000'    --擔保放款-折溢價
                                          ,'10601301000'    --催收款項-法務費用
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
           GROUP BY "YearMonth")
    GROUP BY "YearMonth"
            ,"AssetClass"
            ,NVL("AcSubBookCode",'N')
    ;

    INS_CNT := INS_CNT + sql%rowcount;
    -- 記錄程式結束時間
    JOB_END_TIME := SYSTIMESTAMP;

    commit;
  END;
END;


