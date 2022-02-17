-- 程式功能：維護 MonthlyLM051 月報LM051工作檔 
-- 執行時機：每月底日終批次(換日前)
-- 執行方式：EXEC "Usp_L9_MonthlyLM051_Upd"(20200131,'AB0101');
--

CREATE OR REPLACE PROCEDURE "Usp_L9_MonthlyLM051_Upd"
(
    -- 參數
    TBSDYF         IN  INT,        -- 系統營業日(西元) 
    EmpNo          IN  VARCHAR2    -- 經辦
)
AS
BEGIN
  DECLARE
    INS_CNT        INT;        -- 新增筆數 
    UPD_CNT        INT;        -- 更新筆數 
    JOB_START_TIME TIMESTAMP;  -- 記錄程式起始時間
    JOB_END_TIME   TIMESTAMP;   -- 記錄程式結束時間  
    YYYYMM         INT;         -- 本月年月 
  BEGIN   
    INS_CNT :=0;       
    UPD_CNT :=0;   
    
    -- 記錄程式起始時間
    JOB_START_TIME := SYSTIMESTAMP;

    --　本月年月
    YYYYMM := TBSDYF / 100;     
    --  
    
    -- 刪除舊資料
    DBMS_OUTPUT.PUT_LINE('DELETE MonthlyLM051');
    
    DELETE FROM "MonthlyLM051"; 

    -- 寫入資料
    DBMS_OUTPUT.PUT_LINE('INSERT MonthlyLM051');

    INSERT INTO "MonthlyLM051"
    SELECT 
           D."CustNo"            AS "CustNo"              -- 借款人戶號              
          ,D."FacmNo"            AS "FacmNo"              -- 額度編號               
          ,D."AcBookCode"        AS "AcBookCode"          -- 帳冊別                
          ,D."PrinBalance"       AS "PrinBalance"         -- 本金餘額               
          ,D."FacAcctCode"       AS "FacAcctCode"         -- 額度業務科目             
          ,D."OvduTerm"          AS "OvduTerm"            -- 逾期期數               
          ,D."CityCode"          AS "CityCode"            -- 主要擔保品地區別           
          ,D."PrevIntDate"       AS "PrevIntDate"         -- 繳息迄日               
          ,D."Status"            AS "Status"              -- 戶況                 
          ,D."RenewCode"         AS "RenewCode"           -- 展期記號               
          ,D."ClType"            AS "ClType"              -- 有、無擔保金額記號            
          ,D."AssetClass"        AS "AssetClass"          -- 資料分類代號             
          ,D."LegalProg"         AS "LegalProg"           -- 法務進度               
          ,D."Amount"            AS "Amount"              -- 金額                 
          ,D."Memo"              AS "Memo"                -- 其他紀錄內容             
          ,D."ProdNo"            AS "ProdNo"              -- 商品代碼
          ,JOB_START_TIME        AS "CreateDate"          -- 建檔日期時間  
          ,EmpNo                 AS "CreateEmpNo"         -- 建檔人員 
          ,JOB_START_TIME        AS "LastUpdate"          -- 最後更新日期時間  
          ,EmpNo                 AS "LastUpdateEmpNo"     -- 最後更新人員 
      FROM ( 
       SELECT M."CustNo"         AS "CustNo"      
            , M."FacmNo"         AS "FacmNo"      
            , M."AcBookCode"     AS "AcBookCode"  
            , M."PrinBalance"    AS "PrinBalance" 
            , M."FacAcctCode"    AS "FacAcctCode" 
            , M."OvduTerm"       AS "OvduTerm"    
            , M."CityCode"       AS "CityCode"    
            , M."PrevIntDate"    AS "PrevIntDate" 
            , M."Status"         AS "Status"      
            , M."RenewCode"      AS "RenewCode"   
            , 0                  AS "ClType"      
            , NULL               AS "AssetClass"  
            , NULL               AS "LegalProg"   
            , 0                  AS "Amount"      
            , NULL               AS "Memo"        
            , F."ProdNo"         AS "ProdNo" 
       FROM "MonthlyFacBal" M   
       LEFT JOIN "FacMain"  F
         ON   F."CustNo"    =  M."CustNo"
         AND  F."FacmNo"    =  M."FacmNo"     
       WHERE  M."YearMonth" =  YYYYMM
	      AND   M."Status"    IN (0, 2, 6) 
        AND  (M."OvduTerm"  >  0
          OR  M."RenewCode" =  '2')        
	      ) D;
    
    INS_CNT := INS_CNT + sql%rowcount;


--  更新 ClType  有、無擔保金額記號 
    DBMS_OUTPUT.PUT_LINE('UPDATE ClType');
    
    MERGE INTO "MonthlyLM051" M
    USING (SELECT * FROM (
             SELECT M."CustNo", M."FacmNo"
                  , CASE WHEN M."PrinBalance" <= L."Amount"
                    THEN 1 ELSE 0 END "ClType"
                  , ROW_NUMBER() OVER (PARTITION BY 
                     M."CustNo", M."FacmNo" 
                     ORDER BY L."RecordDate" DESC) AS SEQ
              FROM "MonthlyLM051" M
              LEFT JOIN "CollLaw" L 
                ON  L."CaseCode"  =   '1'   
                AND L."CustNo"    =   M."CustNo"  
                AND L."FacmNo"    =   M."FacmNo" 
                AND L."LegalProg" =   '901' ) D
            WHERE D.SEQ =  1) D
     ON (   M."CustNo"    = D."CustNo"
        AND M."FacmNo"    = D."FacmNo")
    WHEN MATCHED THEN UPDATE 
      SET M."ClType" = D."ClType"; 
    
    UPD_CNT := UPD_CNT + sql%rowcount;    
                         
    DBMS_OUTPUT.PUT_LINE('UPDATE ClType END');    
    
                                             
--  更新 AssetClass  資料分類代號 
    DBMS_OUTPUT.PUT_LINE('UPDATE AssetClass');
    
    MERGE INTO "MonthlyLM051" M
    USING (SELECT  M."CustNo", M."FacmNo"
                 , CASE WHEN M."ClType"    =  1 
                         AND M."RenewCode" = '2'
                         AND M."OvduTerm"  =  0  THEN '21'
                        WHEN M."ClType"    =  1 
                         AND M."OvduTerm"  >= 1
                         AND M."OvduTerm"  <= 6  THEN '22'
                        WHEN M."ClType"    =  1 
                         AND M."OvduTerm"  >= 7
                         AND M."OvduTerm"  <= 12 THEN '23'
                        WHEN M."ClType"    =  0 
                         AND M."OvduTerm"  >= 1
                         AND M."OvduTerm"  <= 3  THEN '23'
                        WHEN M."ClType"    =  1 
                         AND M."OvduTerm"  >  12 THEN '3'
                        WHEN M."ClType"    =  0 
                         AND M."OvduTerm"  >= 4
                         AND M."OvduTerm"  <= 6  THEN '3'
                        WHEN M."ClType"    =  0 
                         AND M."OvduTerm"  >= 7
                         AND M."OvduTerm"  <= 12 THEN '4'
                        WHEN M."ClType"    =  0 
                         AND M."RenewCode" = '2'
                         AND M."OvduTerm"  =  0  THEN '4'
                        WHEN M."ClType"    =  0  
                         AND M."OvduTerm"  >  12 THEN '5'
                        ELSE '5' END "AssetClass" 
            FROM "MonthlyLM051" M ) D 
     ON (   M."CustNo"    = D."CustNo"
        AND M."FacmNo"    = D."FacmNo")
    WHEN MATCHED THEN UPDATE 
      SET M."AssetClass" = D."AssetClass"; 
    
    UPD_CNT := UPD_CNT + sql%rowcount;    
                         
    DBMS_OUTPUT.PUT_LINE('UPDATE AssetClass END');     
    
--  更新 LegalProg法務進度, Amount金額 , Memo其他紀錄內容
    DBMS_OUTPUT.PUT_LINE('UPDATE LegalProg, Amount , Memo');
    
    MERGE INTO "MonthlyLM051" M
    USING (SELECT D."CustNo"
                 ,D."FacmNo"
                 ,D."LegalProg"
                 ,D."Amount"
                 ,D."Memo"
                 ,D."SEQ"
           FROM (SELECT M."CustNo"
                       ,M."FacmNo"
                       ,L."LegalProg"
                       ,NVL(L."Amount",0) AS "Amount"
                       ,L."Memo"
                       ,ROW_NUMBER() OVER (PARTITION BY M."CustNo", M."FacmNo" ORDER BY L."RecordDate" DESC) AS SEQ
                 FROM "MonthlyLM051" M
                 LEFT JOIN "CollLaw" L 
                   ON  L."CaseCode"  = '1'   
                   AND L."CustNo"    = M."CustNo"  
                   AND L."FacmNo"    = M."FacmNo" 
                   AND L."LegalProg" < '900' 
                 WHERE M."ClType"    = 1
                 UNION ALL
                 SELECT M."CustNo"
                       ,M."FacmNo"
                       ,L."LegalProg"
                       ,NVL(L."Amount",0) AS "Amount"
                       ,L."Memo"
                       ,ROW_NUMBER() OVER (PARTITION BY M."CustNo", M."FacmNo" ORDER BY L."RecordDate" DESC) AS SEQ
                 FROM "MonthlyLM051" M
                 LEFT JOIN "CollLaw" L 
                   ON  L."CaseCode"  = '1'   
                   AND L."CustNo"    = M."CustNo"  
                   AND L."FacmNo"    = M."FacmNo" 
                   AND L."LegalProg" = '901' 
                 WHERE M."ClType"    = 0  ) D
            WHERE D.SEQ   =  1) D
     ON (   M."CustNo"    =  D."CustNo"
        AND M."FacmNo"    =  D."FacmNo")
    WHEN MATCHED THEN UPDATE 
      SET M."LegalProg"   =  D."LegalProg"
         ,M."Amount"      =  D."Amount"
         ,M."Memo"        =  D."Memo"  ; 
    
    UPD_CNT := UPD_CNT + sql%rowcount;    
                         
    DBMS_OUTPUT.PUT_LINE('UPDATE LegalProg, Amount , Memo END');
    
    

    -- 記錄程式結束時間
    JOB_END_TIME := SYSTIMESTAMP;

    commit;
 
    -- 例外處理
--    Exception
--    WHEN OTHERS THEN
--   "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_MonthlyLoanBa_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
  END;
END;
