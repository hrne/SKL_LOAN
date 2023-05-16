CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_L9_DailyLoanBal_Upd" 
(
    -- 參數
    TBSDYF         IN  INT,        -- 系統營業日(西元)
    EmpNo          IN  VARCHAR2,   -- 經辦
    MFBSDYF        IN  INT         -- 月底營業日(西元)
)
AS
BEGIN
  -- EXEC "Usp_L9_DailyLoanBal_Upd"(20220225,'001702',20220225);
  -- 除錯查詢:
  -- select * from "DailyLoanBalTemp" where "ErrorMsg" is not null;
  DECLARE
    INS_CNT        INT;        -- 新增筆數 
    UPD_CNT        INT;        -- 更新筆數 
    JOB_START_TIME TIMESTAMP;  -- 記錄程式起始時間
    JOB_END_TIME   TIMESTAMP;   -- 記錄程式結束時間    
    EOF_YYYYMM     INT;        -- 月底年月     
    i_CustNo       INT;
    i_FacmNo       INT;
    i_BormNo       INT;
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
    DELETE FROM "DailyLoanBal"
    WHERE "DataDate" = TBSDYF
    ; 

    EXECUTE IMMEDIATE 'TRUNCATE TABLE "DailyLoanBalTemp" DROP STORAGE'; 

    -- 寫入資料
    INSERT INTO "DailyLoanBalTemp" (
        "CustNo" -- NUMBER(7)
      , "FacmNo" -- NUMBER(3)
      , "BormNo" -- NUMBER(3)
      , "JsonData" -- CLOB
    )
    WITH M AS (
      SELECT B."CustNo"                       AS "CustNo"           
           , B."FacmNo"                       AS "FacmNo"   
           , B."BormNo"                       AS "BormNo"                
           , CASE
               WHEN B."Status" IN (2,5,6,7,8,9)  
               THEN NVL(O."AcctCode",'   ')              
             ELSE F."AcctCode"   
             END                              AS "AcctCode"     
           , B."CurrencyCode"                 AS "CurrencyCode"     
           , CASE 
               WHEN B."Status" IN (0,4) 
               THEN B."LoanBal"
               WHEN B."Status" IN (2,7)  
               THEN NVL(O."OvduBal",0)
             ELSE 0 END                       AS "LoanBalance"        -- '本金餘額'
           , NVL(R."FitRate", B."StoreRate")  AS "StoreRate"  
           , ROW_NUMBER()
             Over (
               Partition By B."CustNo"
                          , B."FacmNo"
                          , B."BormNo" 
               Order By R."EffectDate" DESC -- <= 本營業日最新 
             )                                AS "Seq"
           , F."ProdNo"                       AS "ProdNo"             
           , F."AcctCode"                     AS "FacAcctCode"       
           , B."AcDate"                       AS "AcDate"                
      FROM "LoanBorMain" B
      LEFT JOIN "LoanOverdue" O  ON O."CustNo" = B."CustNo"
                                AND O."FacmNo" = B."FacmNo"
                                AND O."BormNo" = B."BormNo"
                                AND O."OvduNo" = B."LastOvduNo"
                                AND B."Status" IN (2,5,6,7,8,9)  
      LEFT JOIN "FacMain" F  ON F."CustNo" = B."CustNo"
                            AND F."FacmNo" = B."FacmNo" 
      LEFT JOIN "LoanRateChange" R ON R."CustNo" = B."CustNo"
                                  AND R."FacmNo" = B."FacmNo"
                                  AND R."BormNo" = B."BormNo"
                                  AND CASE
                                        -- 一般情況
                                        WHEN B."Status" NOT IN (2,5,6,7,8,9) -- 2022-04-14 新增:排除欠繳情況
                                             AND R."EffectDate" >= B."PrevPayIntDate" 
                                             AND R."EffectDate" <= TBSDYF
                                        THEN 1
                                        -- 轉換日當日轉催的情況  2022-04-19
                                        WHEN B."Status" IN (2,5,6,7,8,9)
                                             AND O."OvduDate" > TBSDYF --於本營業日尚未轉催
                                             AND R."EffectDate" >= B."PrevPayIntDate" 
                                             AND R."EffectDate" <= B."NextPayIntDate" 
                                             AND R."EffectDate" <= TBSDYF
                                        THEN 1
                                        -- 利息繳超過的情況 2022-04-13 新增
                                        WHEN B."PrevPayIntDate" >= TBSDYF 
                                             AND R."EffectDate" <= TBSDYF
                                        THEN 1
                                        -- 欠繳情況 2022-04-14 新增
                                        WHEN B."Status" IN (2,5,6,7,8,9)  
                                             AND R."EffectDate" >= B."PrevPayIntDate" -- 上繳日
                                             AND R."EffectDate" <= O."OvduDate" -- 轉催日期
                                             AND O."OvduDate" <= TBSDYF --於本營業日尚未轉催
                                             AND R."EffectDate" <= TBSDYF
                                        THEN 1
                                      ELSE 0 END = 1
      WHERE B."Status" in (0,1,2,3,4,5,6,7,8,9)   
        AND B."DrawdownDate" <= TBSDYF
    )
    SELECT M."CustNo"                 AS "CustNo"              -- 戶號 
         , M."FacmNo"                 AS "FacmNo"              -- 額度 
         , M."BormNo"                 AS "BormNo"              -- 撥款序號 
         , JSON_OBJECT(
            'DataDate' VALUE TO_CHAR(TBSDYF) ,
            'CustNo' VALUE TO_CHAR(M."CustNo") ,
            'FacmNo' VALUE TO_CHAR(M."FacmNo") ,
            'BormNo' VALUE TO_CHAR(M."BormNo") ,
            'LatestFlag' VALUE TO_CHAR(1) ,
            'MonthEndYm' VALUE TO_CHAR(EOF_YYYYMM) ,
            'CurrencyCode' VALUE TO_CHAR(M."CurrencyCode") ,
            'AcctCode' VALUE TO_CHAR(M."AcctCode") ,
            'FacAcctCode' VALUE TO_CHAR(M."FacAcctCode") ,
            'ProdNo' VALUE TO_CHAR(M."ProdNo") ,
            'LoanBalance' VALUE TO_CHAR(M."LoanBalance") ,
            'StoreRate' VALUE TO_CHAR(M."StoreRate")
           )                          AS "JsonData" 
    FROM M
    LEFT JOIN "DailyLoanBal" D ON D."CustNo" = M."CustNo"
                              AND D."FacmNo" = M."FacmNo"
                              AND D."BormNo" = M."BormNo"
                              AND D."LatestFlag" = 1 --	資料是否為最新 0.一般變動日 1.最後變動日 
    WHERE M."Seq" = 1
      AND CASE
            WHEN EOF_YYYYMM > 0  
                 AND NVL(D."MonthEndYm",0) > 0  
                 AND NVL(D."MonthEndYm",0) != EOF_YYYYMM -- 跨月的第一筆
            THEN 1
            WHEN M."AcctCode" != NVL(D."AcctCode",'   ') -- 業務科目有異動
            THEN 2
            WHEN M."LoanBalance" != NVL(D."LoanBalance",0) -- 放款餘額有異動
            THEN 3
            WHEN M."StoreRate" != NVL(D."StoreRate",0) -- 計息利率有異動
            THEN 4
            WHEN M."AcDate" = TBSDYF -- 此筆記錄的最後交易日為今天
            THEN 5
            WHEN TBSDYF =  MFBSDYF -- 今天是月底營業日
            THEN 6
          ELSE 0
          END != 0
    ;
    
    FOR x IN (
      SELECT "CustNo"
           , "FacmNo"
           , "BormNo"
           , "JsonData"
      FROM "DailyLoanBalTemp"
      ORDER BY "CustNo"
             , "FacmNo"
             , "BormNo"
    )
    LOOP
      i_CustNo := x."CustNo";
      i_FacmNo := x."FacmNo";
      i_BormNo := x."BormNo";
      INSERT INTO "DailyLoanBal" (
           "DataDate"            -- 資料日期
         , "CustNo"              -- 戶號 
         , "FacmNo"              -- 額度 
         , "BormNo"              -- 撥款序號 
         , "LatestFlag"          -- 資料是否為最新	0.一般變動日 1.最後變動日 
         , "MonthEndYm"          -- 月底年月 (yyyymm) 平常日-> 0, 月底日資料 -> yyyymm,ex.202005
         , "CurrencyCode"        -- 幣別 
         , "AcctCode"            -- 業務科目代號  
         , "FacAcctCode"         -- 額度業務科目
         , "ProdNo"              -- 商品代碼
         , "LoanBalance"         -- 放款餘額
         , "StoreRate"           -- 計息利率
         , "IntAmtRcv"           -- 實收利息    轉催收之利息收入AcctCode = '990'
         , "IntAmtAcc"           -- 提存利息    月底日
         , "CreateDate"          -- 建檔日期時間  
         , "CreateEmpNo"         -- 建檔人員 
         , "LastUpdate"          -- 最後更新日期時間  
         , "LastUpdateEmpNo"     -- 最後更新人員 
      )
      SELECT JSON_VALUE(x."JsonData",'$.DataDate' RETURNING NUMBER)
                                        AS "DataDate"            -- 資料日期
           , x."CustNo"                 AS "CustNo"              -- 戶號 
           , x."FacmNo"                 AS "FacmNo"              -- 額度 
           , x."BormNo"                 AS "BormNo"              -- 撥款序號 
           , JSON_VALUE(x."JsonData",'$.LatestFlag' RETURNING NUMBER)
                                        AS "LatestFlag"          -- 資料是否為最新	0.一般變動日 1.最後變動日 
           , JSON_VALUE(x."JsonData",'$.MonthEndYm' RETURNING NUMBER)
                                        AS "MonthEndYm"          -- 月底年月 (yyyymm) 平常日-> 0, 月底日資料 -> yyyymm,ex.202005
           , JSON_VALUE(x."JsonData",'$.CurrencyCode')
                                        AS "CurrencyCode"        -- 幣別 
           , JSON_VALUE(x."JsonData",'$.AcctCode')
                                        AS "AcctCode"            -- 業務科目代號  
           , JSON_VALUE(x."JsonData",'$.FacAcctCode')
                                        AS "FacAcctCode"         -- 額度業務科目
           , JSON_VALUE(x."JsonData",'$.ProdNo')
                                        AS "ProdNo"              -- 商品代碼
           , JSON_VALUE(x."JsonData",'$.LoanBalance' RETURNING NUMBER)
                                        AS "LoanBalance"         -- 放款餘額
           , JSON_VALUE(x."JsonData",'$.StoreRate' RETURNING NUMBER)
                                        AS "StoreRate"           -- 計息利率
           , 0                          AS "IntAmtRcv"           -- 實收利息    轉催收之利息收入AcctCode = '990'
           , 0                          AS "IntAmtAcc"           -- 提存利息    月底日
           , JOB_START_TIME             AS "CreateDate"          -- 建檔日期時間  
           , EmpNo                      AS "CreateEmpNo"         -- 建檔人員 
           , JOB_START_TIME             AS "LastUpdate"          -- 最後更新日期時間  
           , EmpNo                      AS "LastUpdateEmpNo"     -- 最後更新人員 
      FROM DUAL
      ;
    END LOOP;

    INS_CNT := INS_CNT + sql%rowcount;

    -- 迴圈已完成,清零    
    i_CustNo := 0;
    i_FacmNo := 0;
    i_BormNo := 0;

--   同撥款只有一筆日期最大著為最新
    MERGE INTO "DailyLoanBal" M
    USING (
      SELECT "DataDate" 
           , "CustNo"
           , "FacmNo"
           , "BormNo" 
           , ROW_NUMBER()
             Over (
               Partition By "CustNo"
                          , "FacmNo"
                          , "BormNo"
               Order By "DataDate" DESC
             )                  AS ROW_NO
            FROM "DailyLoanBal"
            WHERE "LatestFlag" = 1
    ) T
    ON (
      M."DataDate" = T."DataDate"
      AND M."CustNo"   = T."CustNo"
      AND M."FacmNo"   = T."FacmNo"
      AND M."BormNo"   = T."BormNo" 
      AND T.ROW_NO     > 1
    )
    WHEN MATCHED THEN UPDATE SET
    M."LatestFlag" = 0
    ;                     

    UPD_CNT := UPD_CNT + sql%rowcount;

--   更新 IntAmtRcv 	實收利息
    MERGE INTO "DailyLoanBal" M
    USING (
      SELECT "CustNo"
           , "FacmNo"
           , "BormNo" 
           , SUM(
               CASE
                 WHEN "DbCr" = 'C'
                 THEN "TxAmt"
               ELSE 0 - "TxAmt"
               END ) AS "IntAmtRcv"
      FROM "AcDetail"             -- 會計帳務明細檔
      WHERE "CustNo"  > 0 
       AND  "AcDate"  = TBSDYF
       AND  "AcctCode" IN ('IC1' --IC1	短擔息
                          ,'IC2' --IC2	中擔息
                          ,'IC3' --IC3	長擔息
                          ,'IC4') --IC4 三十年房貸息
       AND  "EntAc" > 0
      GROUP BY "CustNo", "FacmNo", "BormNo"
    ) T
    ON (
      M."DataDate" = TBSDYF
      AND M."CustNo"  = T."CustNo"
      AND M."FacmNo"  = T."FacmNo"
      AND M."BormNo"  = T."BormNo" 
    )
    WHEN MATCHED THEN UPDATE SET
    M."IntAmtRcv" = T."IntAmtRcv" 
    ;
 
    UPD_CNT := UPD_CNT + sql%rowcount;

--   更新 IntAmtAcc 提存利息 月底日
    IF TBSDYF =  MFBSDYF THEN
      MERGE INTO "DailyLoanBal" M
      USING (
       SELECT "CustNo"
            , "FacmNo"
            , "BormNo" 
            , SUM("Interest") AS "IntAmtAcc"
        FROM "AcLoanInt"         --提息明細檔
        WHERE "YearMonth" = EOF_YYYYMM  -- 提息年月 = 本月
         AND  "AcctCode" IN ('IC1','IC2','IC3','IC4')
        GROUP BY "CustNo", "FacmNo", "BormNo"
      ) T
      ON (
         M."DataDate" = TBSDYF
         AND M."CustNo"  = T."CustNo"
         AND M."FacmNo"  = T."FacmNo"
         AND M."BormNo"  = T."BormNo" 
      )
      WHEN MATCHED THEN UPDATE SET
      M."IntAmtAcc" = T."IntAmtAcc"    
      ;

      UPD_CNT := UPD_CNT + sql%rowcount;
    END IF;   

    -- 記錄程式結束時間
    JOB_END_TIME := SYSTIMESTAMP;
 
    commit;
   EXCEPTION
     WHEN OTHERS
     THEN 
     -- 若戶號不為零,則為迴圈內之錯誤,紀錄是哪一筆錯誤
     IF i_CustNo != 0
     THEN 
       UPDATE "DailyLoanBalTemp"
       SET "ErrorMsg" = SUBSTR(DBMS_UTILITY.FORMAT_ERROR_BACKTRACE(), 1, 200)
       WHERE "CustNo" = i_CustNo
         AND "FacmNo" = i_FacmNo
         AND "BormNo" = i_BormNo
       ;
     END IF;
     "Usp_L9_UspErrorLog_Ins"(
         'Usp_L9_DailyLoanBal_Upd' -- UspName 預存程序名稱
       , SQLCODE -- Sql Error Code (固定值)
       , CASE
           WHEN i_CustNo != 0
           THEN '戶號'
                || LPAD(i_CustNo,7,'0')
                || '-'
                || LPAD(i_FacmNo,3,'0')
                || '-'
                || LPAD(i_BormNo,3,'0')
                || ','
         ELSE '' END
         || SQLERRM -- Sql Error Message (固定值)
       , dbms_utility.format_error_backtrace -- Sql Error Trace (固定值)
       , EmpNo -- 發動預存程序的員工編號
     );
     COMMIT;
     RAISE;
  END;
END;