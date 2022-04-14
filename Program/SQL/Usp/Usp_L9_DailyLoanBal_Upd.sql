CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_L9_DailyLoanBal_Upd" 
(
    -- 參數
    TBSDYF         IN  INT,        -- 系統營業日(西元)
    EmpNo          IN  VARCHAR2,   -- 經辦
    MFBSDYF        IN  INT         -- 月底營業日(西元)
)
AS
BEGIN
  DECLARE
    INS_CNT        INT;        -- 新增筆數 
    UPD_CNT        INT;        -- 更新筆數 
    JOB_START_TIME TIMESTAMP;  -- 記錄程式起始時間
    JOB_END_TIME   TIMESTAMP;   -- 記錄程式結束時間    
    EOF_YYYYMM     INT;        -- 月底年月     
  BEGIN   
    INS_CNT :=0;       
    UPD_CNT :=0;   
    IF TBSDYF = MFBSDYF THEN
       EOF_YYYYMM   :=  TRUNC(MFBSDYF / 100);
    ELSE
       EOF_YYYYMM   :=  0;
    END IF;
    -- 記錄程式起始時間
    JOB_START_TIME := SYSTIMESTAMP;

    -- 刪除舊資料
    DBMS_OUTPUT.PUT_LINE('DELETE DailyLoanBal');

    DELETE FROM "DailyLoanBal"
    WHERE "DataDate" = TBSDYF
    ; 

    -- 寫入資料
    DBMS_OUTPUT.PUT_LINE('INSERT DailyLoanBal');

    INSERT INTO "DailyLoanBal"
    SELECT TBSDYF                     AS "DataDate"            -- 資料日期
          ,M."CustNo"                 AS "CustNo"              -- 戶號 
          ,M."FacmNo"                 AS "FacmNo"              -- 額度 
          ,M."BormNo"                 AS "BormNo"              -- 撥款序號 
          ,1                          AS "LatestFlag"          -- 資料是否為最新	0.一般變動日 1.最後變動日 
          ,EOF_YYYYMM                 AS "MonthEndYm"          -- 月底年月 (yyyymm) 平常日-> 0, 月底日資料 -> yyyymm,ex.202005
          ,M."CurrencyCode"           AS "CurrencyCode"        -- 幣別 
          ,M."AcctCode"               AS "AcctCode"            -- 業務科目代號  
          ,M."FacAcctCode"            AS "FacAcctCode"         -- 額度業務科目
          ,M."ProdNo"                 AS "ProdNo"              -- 商品代碼
          ,M."LoanBalance"            AS "LoanBalance"         -- 放款餘額
          ,M."StoreRate"              AS "StoreRate"           -- 計息利率
          ,0                          AS "IntAmtRcv"           -- 實收利息    轉催收之利息收入AcctCode = '990'
          ,0                          AS "IntAmtAcc"           -- 提存利息    月底日
          ,JOB_START_TIME             AS "CreateDate"          -- 建檔日期時間  
          ,EmpNo                      AS "CreateEmpNo"         -- 建檔人員 
          ,JOB_START_TIME             AS "LastUpdate"          -- 最後更新日期時間  
          ,EmpNo                      AS "LastUpdateEmpNo"     -- 最後更新人員 

    FROM ( SELECT
            B."CustNo"                        AS "CustNo"           
           ,B."FacmNo"                        AS "FacmNo"   
           ,B."BormNo"                        AS "BormNo"                
           ,CASE WHEN B."Status" IN (2,5,6,7,8,9)  
                      THEN NVL(O."AcctCode",'   ')              
                 ELSE F."AcctCode"   
            END                               AS "AcctCode"     
           ,B."CurrencyCode"                  AS "CurrencyCode"     
           ,CASE WHEN B."Status" IN (0,4) 
                          THEN B."LoanBal"
                     WHEN B."Status" IN (2,7)  
                          THEN NVL(O."OvduBal",0)           -- 催收餘額
                     ELSE 0
            END                               AS "LoanBalance"        -- '本金餘額'
           ,NVL(R."FitRate", B."StoreRate")   AS "StoreRate"  
           ,ROW_NUMBER() Over (Partition By B."CustNo", B."FacmNo", B."BormNo" 
                               Order By R."EffectDate" DESC)    -- <= 本營業日最新 
                                              AS "Seq"
           ,F."ProdNo"                        AS "ProdNo"             
           ,F."AcctCode"                      AS "FacAcctCode"       
           ,B."AcDate"                        AS "AcDate"                
           FROM "LoanBorMain" B
           LEFT  JOIN  "LoanOverdue" O  ON O."CustNo" = B."CustNo"
                                       AND O."FacmNo" = B."FacmNo"
                                       AND O."BormNo" = B."BormNo"
                                       AND O."OvduNo" = B."LastOvduNo"
                                       AND B."Status" IN (2,5,6,7,8,9)  
           LEFT  JOIN  "FacMain" F  ON F."CustNo" = B."CustNo"
                                   AND F."FacmNo" = B."FacmNo" 
           LEFT  JOIN   "LoanRateChange" R ON R."CustNo" = B."CustNo"
                                          AND R."FacmNo" = B."FacmNo"
                                          AND R."BormNo" = B."BormNo"
                                          AND CASE
                                                -- 一般情況
                                                WHEN R."EffectDate" >= B."PrevPayIntDate" 
                                                     AND R."EffectDate" <= TBSDYF
                                                THEN 1
                                                -- 利息繳超過的情況 2022-04-13 新增
                                                WHEN B."PrevPayIntDate" >= TBSDYF 
                                                     AND R."EffectDate" <= TBSDYF
                                                THEN 1
                                              ELSE 0 END = 1
           WHERE B."Status" in (0,1,2,3,4,5,6,7,8,9)   
          ) M
     LEFT  JOIN  "DailyLoanBal" D ON D."CustNo" = M."CustNo"
                                 AND D."FacmNo" = M."FacmNo"
                                 AND D."BormNo" = M."BormNo"
                                 AND D."LatestFlag" = 1          --	資料是否為最新 0.一般變動日 1.最後變動日 
     WHERE M."Seq" = 1
      AND  ( CASE WHEN EOF_YYYYMM > 0  AND  NVL(D."MonthEndYm",0)  > 0  AND  NVL(D."MonthEndYm",0) <>  EOF_YYYYMM
                     THEN  1
                  WHEN M."AcctCode" <> NVL(D."AcctCode",'   ')
                     THEN  2
                  WHEN M."LoanBalance" <> NVL(D."LoanBalance",0)
                     THEN  3
                  WHEN M."StoreRate" <> NVL(D."StoreRate",0)
                     THEN  4     
                  WHEN M."AcDate" = TBSDYF      --會計日期	
                     THEN  5     
                  WHEN TBSDYF =  MFBSDYF        -- 月底營業日
                     THEN  6     
                  ELSE 0 
              END )  > 0      
    ;
                 --                                  BorMain                        Overdue                
                 --                                  0: 正常戶                                      
                 --                                  2: 催收戶                      
                 --                                  3: 結案戶                                      
                 --                                  4: 逾期戶 
                 --                                  5: 催收結案戶
                 --                                  6: 呆帳戶
                 --                                  7: 部分轉呆戶                  2. 部分轉呆
                 --                                  8: 債權轉讓戶
                  --                                 X  1: 展期 
                --                                   X  9: 呆帳結案戶
                 --                                                                 1: 催收                                  
                 --                                                                 3: 呆帳                                  
                 --                                                                 4: 催收回復               
                 --                                  97~99: 預約撥款

    INS_CNT := INS_CNT + sql%rowcount;

--   同撥款只有一筆日期最大著為最新
    DBMS_OUTPUT.PUT_LINE('UPDATE LatestFlag');

    MERGE INTO "DailyLoanBal" M
    USING (SELECT "DataDate" 
                 ,"CustNo"
                 ,"FacmNo"
                 ,"BormNo" 
                 ,ROW_NUMBER() Over (Partition By "CustNo", "FacmNo", "BormNo"
                                     Order By "DataDate" DESC)
                               AS ROW_NO

            FROM "DailyLoanBal"
            WHERE "LatestFlag" = 1
           ) T
    ON (    M."DataDate" = T."DataDate"
        AND M."CustNo"   = T."CustNo"
        AND M."FacmNo"   = T."FacmNo"
        AND M."BormNo"   = T."BormNo" 
        AND T.ROW_NO     > 1
       )
    WHEN MATCHED THEN UPDATE SET M."LatestFlag" = 0
    ;                     
    DBMS_OUTPUT.PUT_LINE('UPDATE LatestFlag END');

    UPD_CNT := UPD_CNT + sql%rowcount;

--   更新 IntAmtRcv 	實收利息

    DBMS_OUTPUT.PUT_LINE('UPDATE IntAmtRcv');

    MERGE INTO "DailyLoanBal" M
    USING (SELECT "CustNo"
                 ,"FacmNo"
                 ,"BormNo" 
                 ,SUM(CASE WHEN "DbCr" = 'C' THEN "TxAmt"
                           ELSE              0 - "TxAmt"
                      END ) AS "IntAmtRcv"
            FROM "AcDetail"             -- 會計帳務明細檔
            WHERE "CustNo"  > 0 
             AND  "AcDate"  = TBSDYF
             AND  "AcctCode" IN ('IC1','IC2','IC3','IC4')
            GROUP BY "CustNo", "FacmNo", "BormNo"
           ) T
--IC1	短擔息
--IC2	中擔息
--IC3	長擔息
--IC4	三十年房貸息

    ON (    M."DataDate" = TBSDYF
        AND M."CustNo"  = T."CustNo"
        AND M."FacmNo"  = T."FacmNo"
        AND M."BormNo"  = T."BormNo" 
       )
    WHEN MATCHED THEN UPDATE SET M."IntAmtRcv" = T."IntAmtRcv" 
    ;      
    UPD_CNT := UPD_CNT + sql%rowcount;


--   更新 IntAmtAcc 提存利息 月底日

   IF TBSDYF =  MFBSDYF THEN
    DBMS_OUTPUT.PUT_LINE('UPDATE IntAmtAcc');   
    MERGE INTO "DailyLoanBal" M
    USING (SELECT "CustNo"
                 ,"FacmNo"
                 ,"BormNo" 
                 ,SUM("Interest") AS "IntAmtAcc"
            FROM "AcLoanInt"         --提息明細檔
            WHERE "YearMonth" = EOF_YYYYMM  -- 提息年月 = 本月
             AND  "AcctCode" IN ('IC1','IC2','IC3','IC4')
            GROUP BY "CustNo", "FacmNo", "BormNo"
           ) T
    ON (    M."DataDate" = TBSDYF
        AND M."CustNo"  = T."CustNo"
        AND M."FacmNo"  = T."FacmNo"
        AND M."BormNo"  = T."BormNo" 
       )
    WHEN MATCHED THEN UPDATE SET M."IntAmtAcc" = T."IntAmtAcc"    
    ;       
    UPD_CNT := UPD_CNT + sql%rowcount;
    END IF;   


    DBMS_OUTPUT.PUT_LINE('UPDATE LatestFlag END');


    -- 記錄程式結束時間
    JOB_END_TIME := SYSTIMESTAMP;

    commit;

    -- 例外處理
    Exception
    WHEN OTHERS THEN
    "Usp_L9_UspErrorLog_Ins"(
        'Usp_L9_DailyLoanBal_Upd' -- UspName 預存程序名稱
      , SQLCODE -- Sql Error Code (固定值)
      , SQLERRM -- Sql Error Message (固定值)
      , dbms_utility.format_error_backtrace -- Sql Error Trace (固定值)
      , EmpNo -- 發動預存程序的員工編號
    );
  END;
END;