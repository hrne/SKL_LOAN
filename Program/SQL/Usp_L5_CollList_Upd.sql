CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_L5_CollList_Upd" 
(
    -- 參數
    TBSDYF         IN  INT,        -- 系統營業日(西元)
    EmpNo          IN  VARCHAR2,   -- 經辦
    TxtNo          IN  VARCHAR2,   -- 交易序號
    L6BSDYF        IN  INT,        -- 前六營業日(西元) 9/28(一) -> 9/18(五)
    L7BSDYF        IN  INT,        -- 前七營業日(西元) 9/28(一) -> 9/17(四)
    JobTxSeq       IN  VARCHAR2    -- 啟動批次的交易序號
)
AS
BEGIN
  DECLARE
    INS_CNT        INT;        -- 新增筆數 
    UPD_CNT        INT;        -- 更新筆數 
    JOB_START_TIME TIMESTAMP;  -- 記錄程式起始時間
    JOB_END_TIME   TIMESTAMP;   -- 記錄程式結束時間    
  BEGIN   
    -- EXEC "Usp_L5_CollList_Upd"(20211029,'999999','99991231',20211021,20211020);
    INS_CNT :=0;       
    UPD_CNT :=0;        

    -- 記錄程式起始時間
    JOB_START_TIME := SYSTIMESTAMP;

    -- 刪除舊資料
    DELETE FROM "CollListTmp";

     -- step 1  同擔保品取嚴重等級最高者為同擔保品額度
     -- step 2  同額度有多筆同擔保品額度取嚴重等級最高者為新同擔保品額度 
     -- step 3  以新同擔保品額度更新同擔保品額度 

    -- 寫入暫存資料
    INSERT INTO "CollListTmp"
    SELECT M."CustNo"                        AS "CustNo"              -- '戶號';
          ,M."FacmNo"                        AS "FacmNo"              -- '額度';
          ,NVL(C."ClCode1",0)                AS "ClCode1"             -- '擔保品代號1';
          ,NVL(C."ClCode2",0)                AS "ClCode2"             -- '擔保品代號2';
          ,NVL(C."ClNo",0)                   AS "ClNo"                -- '擔保品編號';
          ,NVL(C."CustNo", M."CustNo")       AS "ClCustNo"            -- '同擔保品戶號';
          ,NVL(C."FacmNo", M."FacmNo")       AS "ClFacmNo"            -- '同擔保品額度';
          ,CASE WHEN M."PrinBalance" = 0                              --  已還清不排序
                     THEN 0 
                WHEN NVL(C."ClCode1",0) = 0                           --  擔保品Notfound
                    THEN 0                  
                ELSE                                         
                     ROW_NUMBER() Over (Partition By C."ClCode1", C."ClCode2", C."ClNo"
                                      Order By M."Status" DESC, M."NextIntDate" ASC)
                END                          AS "ClRowNo"             -- '同擔保品序列號';
          , '1'                              AS "CaseCode"            -- '案件種類';
          ,CASE WHEN M."PrevIntDate" = 99991231 
                     THEN 0 
                ELSE M."PrevIntDate"
           END                               AS "PrevIntDate"         -- '繳息迄日';
          ,CASE WHEN M."NextIntDate" = 99991231 
                     THEN 0
                ELSE  M."NextIntDate"
           END                               AS "NextIntDate"         -- '下次應繳日';
          ,M."CurrencyCode"                  AS "CurrencyCode"        -- '幣別';
          ,M."PrinBalance"                   AS "PrinBalance"         -- '本金餘額';
          ,M."BadDebtBal"                    AS "BadDebtBal"          -- '呆帳餘額'; 
           ,CASE WHEN M."Status" = '40'
                  AND M."NextIntDate" > 0
                  AND M."NextIntDate" < TBSDYF 
                  AND TRUNC(MONTHS_BETWEEN(TO_DATE(TBSDYF,'YYYYMMDD'), TO_DATE(M."NextIntDate",'YYYYMMDD'))) >= 1
                 THEN 4
                 ELSE TO_NUMBER(SUBSTR(M."Status" ,2,1))  
           END                               AS "Status"              -- '戶況';
          ,NVL(M."AcctCode", F."AcctCode")   AS "AcctCode"            -- 業務科目代號
          ,F."AcctCode"                      AS "FacAcctCode"         -- 額度業務科目 
          ,M."RenewCode"                     AS "RenewCode"           -- 展期記號 空白、1.展期一般 2.展期協議  
          ,M."AcDate"                        AS "AcDate"              -- 會計日期
          ,JOB_START_TIME                    AS "CreateDate"          -- 建檔日期時間  
          ,EmpNo                             AS "CreateEmpNo"         -- 建檔人員 
          ,JOB_START_TIME                    AS "LastUpdate"          -- 最後更新日期時間  
          ,EmpNo                             AS "LastUpdateEmpNo"     -- 最後更新人員 
    FROM ( SELECT
            B."CustNo"                   AS "CustNo"            -- '戶號';
           ,B."FacmNo"                   AS "FacmNo"            -- '額度';
           ,MIN(CASE WHEN B."Status" IN (1,3,5,6,8,9) 
                          THEN  99991231     
                     WHEN B."PrevPayIntDate" = 0        
                          THEN  B."DrawdownDate"
                     ELSE B."PrevPayIntDate"        
                END )                    AS "PrevIntDate"       -- '繳息迄日';
           ,MIN(CASE WHEN B."Status" IN (1,3,5,6,8,9) 
                          THEN 99991231   
                     ELSE B."NextPayIntDate"
                END )                    AS "NextIntDate"       -- '下次應繳日';
           ,B."CurrencyCode"             AS "CurrencyCode"      -- '幣別';
           ,SUM(CASE WHEN B."Status" IN (0,4) 
                          THEN B."LoanBal"
                     WHEN B."Status" IN (1,3,5,6,8,9)  
                          THEN 0
                     WHEN B."Status" IN (2,7)  
                          THEN NVL(O."OvduBal",0)                -- 催收餘額
                     ELSE 0                               
                 END)                  AS "PrinBalance"          -- '本金餘額'
           ,SUM(CASE WHEN B."Status" IN (6,7)  
                          THEN NVL(O."BadDebtBal",0)             -- 呆帳餘額 
                     ELSE 0
                 END)                  AS "BadDebtBal"           
           ,MAX(O."AcctCode")          AS "AcctCode"     
           ,MIN(CASE B."Status" 
                     WHEN  0  THEN  '40'
                     WHEN  1  THEN  '91'
                     WHEN  2  THEN  '22' 
                     WHEN  3  THEN  '83'                          
                     WHEN  4  THEN  '34'                            
                     WHEN  5  THEN  '55'
                     WHEN  6  THEN  '06'
                     WHEN  7  THEN  '17'
                     WHEN  8  THEN  '68'
                     WHEN  9  THEN  '79' 
                 END)                                                                                                           
                       AS "Status"      
 		 		 		 		-- priority status
		 		 		 		--       0 -- 6.呆帳戶
		 		 		 		--       1 -- 7.部分轉呆戶
		 		 		 		--       2 -- 2.催收戶
		 		 		 		--       3 -- 4.逾期戶(正常戶逾期超過一個月)
		 		 		 		--       4 -- 0.正常戶
		 		 		 		--       5 -- 5.催收結案戶
		 		 		 		--       6 -- 8.債權轉讓戶
		 		 		 		--       7 -- 9.呆帳結案戶
		 		 		 		--       8 -- 3.結案戶
		 		 		 		--       9 -- 1.展期戶
                 -- '戶況';                                  BorMain                Overdue                  NegMain          
                 -- 01: 正常戶                               0: 正常戶                                       0:正常
                 -- 01: 展期戶                               1: 展期                                         
                 -- 02: 催收戶                               2: 催收戶              1: 催收                           
                 -- 03: 結案戶                               3: 結案戶                                       3:結案  2:毀諾  5.調解不成立            
                 -- 04: 逾期戶                               4: 逾期戶 
                 -- 05: 催收結案戶                           5: 催收結案戶
                 -- 06: 呆帳戶                               6: 呆帳戶              3: 呆帳
                 -- 07: 部分轉呆戶                           7: 部分轉呆戶          2. 部分轉呆
                 -- 08: 債權轉讓戶                           8: 債權轉讓戶
                 -- 09: 呆帳結案戶                           9: 呆帳結案戶
                 --                                                                X 4: 催收回復             X 1:已變更
                 --                                                                                          X 4:未生效
                 --                                                                           
           ,MAX(NVL(R."RenewCode",' '))  AS "RenewCode"  
           ,MAX(B."AcDate")              AS "AcDate"    
           FROM "LoanBorMain" B
           LEFT  JOIN  "LoanOverdue" O ON O."CustNo" = B."CustNo"
                                      AND O."FacmNo"  = B."FacmNo" 
                                      AND O."BormNo"  = B."BormNo"
                                      AND O."OvduNo" = B."LastOvduNo"
                                      AND B."Status" in (2,6,7) 
           LEFT  JOIN  "AcLoanRenew" R ON B."RenewFlag" != '0' -- 2022-01-14 放款主檔的此欄位不為0才串會計借新還舊檔
                                      AND R."CustNo" = B."CustNo"
                                      AND R."NewFacmNo"  = B."FacmNo"
                                      AND R."NewBormNo"  = B."BormNo" 
                                      AND R."MainFlag"  = 'Y'
           WHERE B."DrawdownDate" <= TBSDYF
            AND  B."Status" < 10
           GROUP BY  B."CustNo",  B."FacmNo",  B."CurrencyCode"
          ) M
        LEFT JOIN  "ClFac" C ON M."PrinBalance" > 0                --  已還清不排序
                     AND C."CustNo"      = M."CustNo" 
                     AND C."FacmNo"      = M."FacmNo"        -- 擔保品與額度關聯檔
                     AND C."MainFlag"    = 'Y'               -- 主要擔保品
        LEFT  JOIN  "FacMain" F  ON F."CustNo" = M."CustNo" AND F."FacmNo"  = M."FacmNo"  
        ;
     --    step 1  擔保品相同取嚴重等級最高者為同擔保品額度
     --     100-4 A       1 100-4 
     --     100-1 A       2 100-1 -> 100-4 
     --     200-1 A       3 200-1 -> 100-4 

     --     100-3 B       1 100-3 
     --     100-4 B       2 100-4 -> 100-3
    MERGE INTO "CollListTmp" T2
    USING (SELECT "ClCustNo"
                 ,"ClFacmNo"
                 ,"ClCode1"
                 ,"ClCode2"
                 ,"ClNo"
           FROM "CollListTmp"
           WHERE "ClRowNo" = 1
          ) T1
    ON (    T2."ClCode1" = T1."ClCode1"
        AND T2."ClCode2" = T1."ClCode2"
        AND T2."ClNo"    = T1."ClNo"
        AND T2."ClRowNo" > 1
       )
    WHEN MATCHED THEN UPDATE SET T2."ClCustNo" = T1."ClCustNo"
                                ,T2."ClFacmNo" = T1."ClFacmNo"
    ;
     --      step 2  同額度有多筆同擔保品額度取嚴重等級最高者為新同擔保品額度 
     --     100-4 A       1         100-4    2
     --     100-1 A       2         100-4    1
     --     200-1 A       3         100-4    1
     --     100-3 B       1         100-3    1
     --     100-4 B       2         100-3    1
    MERGE INTO "CollListTmp" T2
    USING (SELECT ROW_NUMBER() Over (Partition By "CustNo", "FacmNo" Order By CASE "Status" 
                                            WHEN  0  THEN  '40'
                                            WHEN  1  THEN  '91'
                                            WHEN  2  THEN  '22' 
                                            WHEN  3  THEN  '83'                          
                                            WHEN  4  THEN  '34'                            
                                            WHEN  5  THEN  '55'
                                            WHEN  6  THEN  '06'
                                            WHEN  7  THEN  '17'
                                            WHEN  8  THEN  '68'
                                            WHEN  9  THEN  '79' 
                                            END DESC
                                           , "NextIntDate" ASC ) AS ROW_NO
                 ,"CustNo"
                 ,"FacmNo"
                 ,"ClCode1"
                 ,"ClCode2"
                 ,"ClNo"
            FROM "CollListTmp"
           ) T1
    ON (   
            T2."CustNo"  = T1."CustNo"
        AND T2."FacmNo"  = T1."FacmNo"
        AND T2."ClCode1" = T1."ClCode1"
        AND T2."ClCode2" = T1."ClCode2"
        AND T2."ClNo"    = T1."ClNo"
       )
     WHEN MATCHED THEN UPDATE SET T2."ClRowNo" = T1.ROW_NO
     ;

     --      step 3  以新同擔保品額度更新同擔保品額度 
     --     100-4 B       1         100-3  
     --     100-4 A       2         100-4  -> 100-3 
     --     100-4 A       2         100-4  -> 100-3 
     --     100-1 A       1         100-4  -> 100-3 
     --     200-1 A       1         100-4  -> 100-3  
     --     100-3 B       1         100-3 
     --     100-4 B       1         100-3  
    MERGE INTO "CollListTmp" T3
    USING ( SELECT
             T2."CustNo"     AS "CustNo"
            ,T2."FacmNo"     AS "FacmNo"
            ,T2."ClCode1"    AS "ClCode1"
            ,T2."ClCode2"    AS "ClCode2"
            ,T2."ClNo"       AS "ClNo"
            ,T0."ClCustNo"   AS "ClCustNo"            
            ,T0."ClFacmNo"   AS "ClFacmNo" 
            FROM "CollListTmp"  T1
            LEFT JOIN "CollListTmp"  T0 
                   ON T0."CustNo" = T1."CustNo" 
                  AND T0."FacmNo" = T1."FacmNo"
                  AND T0."ClRowNo" = 1  
            LEFT JOIN "CollListTmp"  T2 
                   ON T2."ClCustNo" = T1."ClCustNo" 
                  AND T2."ClFacmNo" = T1."ClFacmNo"
                  AND T0."ClRowNo"  > 1  
            WHERE T1."ClRowNo" > 1 
          ) S1            
    ON (    T3."CustNo"  = S1."CustNo"
        AND T3."FacmNo"  = S1."FacmNo"
        AND T3."ClCode1" = S1."ClCode1"
        AND T3."ClCode2" = S1."ClCode2"
        AND T3."ClNo"    = S1."ClNo"
       )
    WHEN MATCHED THEN UPDATE SET T3."ClCustNo" = S1."ClCustNo"
                                ,T3."ClFacmNo" = S1."ClFacmNo"
    ;

    -- step 4  刪除同擔保品額度非嚴重等級最高的重複額度
    DELETE FROM "CollListTmp" T
    WHERE T."ClRowNo" > 1
    ;

    -- step 5  重編同擔保品序列號
    MERGE INTO "CollListTmp" T2
    USING (SELECT ROW_NUMBER() Over (Partition By "ClCustNo", "ClFacmNo" Order By CASE "Status" 
                                            WHEN  0  THEN  '40'
                                            WHEN  1  THEN  '91'
                                            WHEN  2  THEN  '22' 
                                            WHEN  3  THEN  '83'                          
                                            WHEN  4  THEN  '34'                            
                                            WHEN  5  THEN  '55'
                                            WHEN  6  THEN  '06'
                                            WHEN  7  THEN  '17'
                                            WHEN  8  THEN  '68'
                                            WHEN  9  THEN  '79' 
                                            END DESC
                                           , "NextIntDate" ASC ) AS ROW_NO
                 ,"CustNo"
                 ,"FacmNo"
                 ,"ClCode1"
                 ,"ClCode2"
                 ,"ClNo"
            FROM "CollListTmp"
           ) T1
    ON (   
            T2."CustNo"  = T1."CustNo"
        AND T2."FacmNo"  = T1."FacmNo"
        AND T2."ClCode1" = T1."ClCode1"
        AND T2."ClCode2" = T1."ClCode2"
        AND T2."ClNo"    = T1."ClNo"
       )
     WHEN MATCHED THEN UPDATE SET T2."ClRowNo" = T1.ROW_NO
     ;

    ------- 債務協商案件---------------
    -- 寫入暫存資料

    INSERT INTO "CollListTmp"
    SELECT N."CustNo"                 AS "CustNo"              -- '戶號';
          ,0                          AS "FacmNo"              -- '額度';
          ,0                          AS "ClCode1"             -- '擔保品代號1';
          ,0                          AS "ClCode2"             -- '擔保品代號2';
          ,0                          AS "ClNo"                -- '擔保品編號';
          ,N."CustNo"                 AS "ClCustNo"            -- '同擔保品戶號';
          ,0                          AS "ClFacmNo"            -- '同擔保品額度';
          ,1                          AS "ClRowNo"             -- '同擔保品序列號';
          ,'2'                        AS "CaseCode"            -- '案件種類';
          ,N."PayIntDate"             AS "PrevIntDate"         -- '繳息迄日';
          ,N."NextPayDate"            AS "NextIntDate"         -- '下次應繳日';
          ,'TWD'                      AS "CurrencyCode"        -- '幣別';
          ,CASE WHEN N."Status" = 3   THEN 0
                ELSE N."PrinBalance"  
           END                        AS "PrinBalance"         -- '本金餘額';
          ,0                          AS "BadDebtBal"          -- '呆帳餘額'; 
          ,CASE WHEN  N."Status" = 0
                  AND N."NextPayDate" > 0
                  AND N."NextPayDate" < TBSDYF 
                  AND TRUNC(MONTHS_BETWEEN(TO_DATE(TBSDYF,'YYYYMMDD'), TO_DATE(N."NextPayDate",'YYYYMMDD'))) >= 1
                 THEN 4
                ELSE  N."Status"      
           END                        AS "Status"              -- '戶況';
          ,'   '                      AS "AcctCode"            -- 業務科目代號
          ,'   '                      AS "FacAcctCode"         -- 額度業務科目   
          ,' '                        AS "RenewCode"           -- 展期記號  
          ,0                          AS "AcDate"              -- 會計日期
          ,JOB_START_TIME             AS "CreateDate"          -- 建檔日期時間  
          ,EmpNo                      AS "CreateEmpNo"         -- 建檔人員 
          ,JOB_START_TIME             AS "LastUpdate"          -- 最後更新日期時間  
          ,EmpNo                      AS "LastUpdateEmpNo"     -- 最後更新人員 
    FROM ( SELECT
            "CustNo"            
           ,ROW_NUMBER() Over (Partition By "CustNo" Order By "CaseSeq" DESC) -- 最新案件 = 1
                               AS  ROW_NUMBER
           ,"PayIntDate" 
           ,"NextPayDate"         
           ,"PrincipalBal"                AS  "PrinBalance"        
           ,CASE "Status"
                 WHEN '0' THEN 0            -- 0.正常       ==> 0.正常戶
                 WHEN '2' THEN 3            -- 2.毀諾       ==> 3.結案戶
                 WHEN '3' THEN 3            -- 3.結案       ==> 3.結案戶
                 WHEN '5' THEN 3            -- 5.調解不成立  ==> 3.結案戶
               END             AS "Status"              -- '戶況';
           FROM "NegMain" 
           WHERE "Status" not in ('1','4')  -- 排除 1.已變更 4.未生效
          ) N
     WHERE N.ROW_NUMBER = 1
     ;
    
    -------  更新資料  -------
    MERGE INTO "CollList" C
    USING ( 
      WITH "rawData" AS (
        SELECT "CaseCode"
             , "CustNo"
             , "FacmNo"
             , "AcDate" AS "TxDate"
             , '2'      AS "TxCode" -- 2:函催登錄
             , "LastUpdate"
        FROM "CollLetter"
        UNION
        SELECT "CaseCode"
             , "CustNo"
             , "FacmNo"
             , "AcDate" AS "TxDate"
             , '3'      AS "TxCode" -- 3:電催登錄
             , "LastUpdate"
        FROM "CollTel"
        UNION
        SELECT "CaseCode"
             , "CustNo"
             , "FacmNo"
             , "AcDate" AS "TxDate"
             , '4'      AS "TxCode" -- 4:面催登錄
             , "LastUpdate"
        FROM "CollMeet"
        UNION
        SELECT "CaseCode"
             , "CustNo"
             , "FacmNo"
             , "AcDate" AS "TxDate"
             , '5'      AS "TxCode" -- 5:法務進度登錄
             , "LastUpdate"
        FROM "CollLaw"
        UNION
        SELECT "CaseCode"
             , "CustNo"
             , "FacmNo"
             , "AcDate" AS "TxDate"
             , '6'      AS "TxCode" -- 6:提醒登錄
             , "LastUpdate"
        FROM "CollRemind"
      )
      , "lastTxData" AS (
        SELECT "CaseCode"
             , "CustNo"
             , "FacmNo"
             , "TxDate"
             , "TxCode"
             , ROW_NUMBER()
               OVER (
                 PARTITION BY "CaseCode"
                            , "CustNo"
                            , "FacmNo"
                 ORDER BY "TxDate" DESC
                        , "LastUpdate" DESC
               ) AS "Seq"
        FROM "rawData"
      )
      , LBM AS (
        SELECT "CustNo"
             , "FacmNo"
             , "SpecificDd"
             , ROW_NUMBER()
               OVER (
                 PARTITION BY "CustNo"
                            , "FacmNo"
                 ORDER BY CASE
                            WHEN "Status" in (2,6,7)
                            THEN 10 + "Status"
                          ELSE "Status" END -- 排序時,把戶況2、6、7的優先度往後排
                         ,"NextPayIntDate" 
               ) AS "Seq"
             , CASE WHEN "Status" IN (1,3,5,6,8,9) THEN 99991231
                    WHEN "SpecificDd" = 0 THEN "NextPayIntDate"
                    ELSE to_number(to_char(ADD_MONTHS(TO_DATE(19110100 + "SpecificDd" ,'YYYYMMDD')
                                            ,TRUNC(MONTHS_BETWEEN(TO_DATE(TBSDYF,'YYYYMMDD'), TO_DATE(19110100 + DECODE("SpecificDd", 0, 1, "SpecificDd"),'YYYYMMDD')))),'YYYYMMDD'))
               END        AS "LatestPayDate"  -- 最近應繳日
              , CASE WHEN "Status" IN (1,3,5,6,8,9) THEN 99991231
                     WHEN "SpecificDd" = 0 THEN 0
                     ELSE TRUNC(MONTHS_BETWEEN(TO_DATE(TBSDYF,'YYYYMMDD'), TO_DATE(19110100 + "SpecificDd" ,'YYYYMMDD')))  
               END        AS "TbsDyTerm"     -- 本日應繳日期數
               , CASE WHEN "Status" IN (1,3,5,6,8,9) THEN 99991231
                     WHEN "SpecificDd" = 0 THEN 0
                     ELSE TRUNC(MONTHS_BETWEEN(TO_DATE("NextPayIntDate",'YYYYMMDD'), TO_DATE(19110100 + + "SpecificDd" ,'YYYYMMDD'))) 
               END        AS "NextPayTerm"   -- 下次應繳日期數 
       FROM "LoanBorMain"
        WHERE "DrawdownDate" <= TBSDYF
          AND  "Status" < 10
      )
      SELECT C1."CustNo"                AS "CustNo"              -- '戶號';
            ,C1."FacmNo"                AS "FacmNo"              -- '額度';
            ,C1."CaseCode"              AS "CaseCode"            -- '案件種類';
            ,NVL(LTD."TxDate",0)        AS "TxDate"              -- '作業日期';
            ,NVL(LTD."TxCode",' ')      AS "TxCode"              -- '作業項目'; 
            ,C1."PrevIntDate"           AS "PrevIntDate"         -- '繳息迄日';
            ,C1."NextIntDate"           AS "NextIntDate"         -- '下次應繳日';
            -- 若 下次應繳日 > 系統營業日(TBSDYF) => 擺零
            -- 則 最近應繳日 < 系統營業日(TBSDYF) => 本日應繳日期數 -  下次應繳日期數 + 1
            ,CASE
               WHEN C1."NextIntDate" > TBSDYF THEN 0
               WHEN NVL(LBM."LatestPayDate",0) = 0 THEN 0
               WHEN LBM."LatestPayDate" < TBSDYF THEN  LBM."TbsDyTerm" - LBM."NextPayTerm" + 1
               ELSE LBM."TbsDyTerm" - LBM."NextPayTerm"
             END                        AS "OvduTerm"            -- '逾期期數';
            -- 若 下次應繳日 < 系統營業日(TBSDYF)
            -- 則 計算逾期天數
            -- 否則 擺零
            ,CASE
               WHEN C1."NextIntDate" < TBSDYF AND C1."NextIntDate" > 0 AND C1."PrinBalance" + C1."BadDebtBal" <> 0
               THEN TO_DATE(TBSDYF,'YYYYMMDD')  - TO_DATE(C1."NextIntDate",'YYYYMMDD') 
             ELSE 0 END                 AS "OvduDays"            -- '逾期天數';
            ,C1."CurrencyCode"          AS "CurrencyCode"        -- '幣別';
            ,C1."PrinBalance"           AS "PrinBalance"         -- '本金餘額';
            ,C1."BadDebtBal"            AS "BadDebtBal"          -- '呆帳餘額'; 
            -- 若 下次應繳日 < 系統營業日(TBSDYF)
            -- 則 擺擔保品地區別在"CdCity"的催收員 (若該擔保品無地區別,擺台北市的催收員)
            -- 否則 擺空白
            -- 2022-03-22 新增條件 from Linda: CaseCode=2債協的時候,如果是否指定IsSpecify=N,催收人員AccCollPsn跟法務人員LegalPsn都固定放怡婷的員編CB7541
            ,CASE
               WHEN C1."CaseCode" = '2'
                    AND NVL(Ori."IsSpecify",'N') = 'N'
               THEN 'CB7541'
               WHEN NVL(Ori."IsSpecify",'N') = 'Y'
               THEN NVL(Ori."AccCollPsn",' ')
             ELSE NVL(S1."AccCollPsn",' ')
             END                        AS "AccCollPsn"          -- '催收員';
            -- 若 下次應繳日 < 系統營業日(TBSDYF)
            -- 則 擺擔保品地區別在"CdCity"的法務人員 (若該擔保品無地區別,擺台北市的法務人員)
            -- 否則 擺空白
            -- 2022-03-22 新增條件 from Linda: CaseCode=2債協的時候,如果是否指定IsSpecify=N,催收人員AccCollPsn跟法務人員LegalPsn都固定放怡婷的員編CB7541
            ,CASE
               WHEN C1."CaseCode" = '2'
                    AND NVL(Ori."IsSpecify",'N') = 'N'
               THEN 'CB7541'
               WHEN NVL(Ori."IsSpecify",'N') = 'Y'
               THEN NVL(Ori."LegalPsn",' ')
             ELSE NVL(S1."LegalPsn",' ')
             END                        AS "LegalPsn"            -- '法務人員';
            ,C1."Status"                AS "Status"              -- '戶況';
            ,C1."AcctCode"              AS "AcctCode"            -- 業務科目代號
            ,C1."FacAcctCode"           AS "FacAcctCode"         -- 額度業務科目   
            ,C1."ClCustNo"              AS "ClCustNo"            -- '同擔保品戶號';
            ,C1."ClFacmNo"              AS "ClFacmNo"            -- '同擔保品額度';
            ,C1."ClRowNo"               AS "ClRowNo"             -- '同擔保品序列號';
            ,NVL(S1."ClCode1",0)        AS "ClCode1"             -- '擔保品代號1';
            ,NVL(S1."ClCode2",0)        AS "ClCode2"             -- '擔保品代號2';
            ,NVL(S1."ClNo",0)           AS "ClNo"                -- '擔保品號碼';
            ,C1."RenewCode"             AS "RenewCode"           -- 空白、1.展期一般 2.展期協議 
            ,C1."AcDate"                AS "AcDate"              -- 會計日期
            ,C1."CreateDate"            AS "CreateDate"          -- 建檔日期時間  
            ,C1."CreateEmpNo"           AS "CreateEmpNo"         -- 建檔人員 
            ,C1."LastUpdate"            AS "LastUpdate"          -- 最後更新日期時間  
            ,C1."LastUpdateEmpNo"       AS "LastUpdateEmpNo"     -- 最後更新人員 
            ,S1."CityCode"              AS "CityCode"            -- 地區別
            ,NVL(Ori."IsSpecify",'N')   AS "IsSpecify"           -- 是否指定 VARCHAR2 1 "空白 Y:是 N:否 若催收或法務人員非CdCity裡設定的人，則此欄位為Y，否則預設N"
            ,CASE
               WHEN NVL(Ori."IsSpecify",'N') = 'Y'
               THEN Ori."AccTelArea"
             ELSE S1."AccTelArea" END   AS "AccTelArea"          -- 催收人員電話-區碼 VARCHAR2 5 由連線交易維護此欄
            ,CASE
               WHEN NVL(Ori."IsSpecify",'N') = 'Y'
               THEN Ori."AccTelNo"
             ELSE S1."AccTelNo" END     AS "AccTelNo"            -- 催收人員電話 VARCHAR2 10 由連線交易維護此欄
            ,CASE
               WHEN NVL(Ori."IsSpecify",'N') = 'Y'
               THEN Ori."AccTelExt"
             ELSE S1."AccTelExt" END    AS "AccTelExt"           -- 催收人員電話-分機 VARCHAR2 5 由連線交易維護此欄
            ,CASE
               WHEN NVL(Ori."IsSpecify",'N') = 'Y'
               THEN Ori."LegalArea"
             ELSE S1."LegalArea" END    AS "LegalArea"           -- 法務人員電話-區碼 VARCHAR2 5 由連線交易維護此欄
            ,CASE
               WHEN NVL(Ori."IsSpecify",'N') = 'Y'
               THEN Ori."LegalNo"
             ELSE S1."LegalNo" END      AS "LegalNo"             -- 法務人員電話 VARCHAR2 10 由連線交易維護此欄
            ,CASE
               WHEN NVL(Ori."IsSpecify",'N') = 'Y'
               THEN Ori."LegalExt"
             ELSE S1."LegalExt" END     AS "LegalExt"            -- 法務人員電話-分機 VARCHAR2 5 由連線交易維護此欄
      FROM "CollListTmp" C1
      LEFT JOIN ( 
        SELECT CF."CustNo"
              ,CF."FacmNo"
              ,NVL(CM."ClCode1",0)      AS "ClCode1"
              ,NVL(CM."ClCode2",0)      AS "ClCode2"
              ,NVL(CM."ClNo",0)         AS "ClNo"
              ,CM."CityCode"            AS "CityCode" 
              ,NVL(CC."AccCollPsn",' ') AS "AccCollPsn"
              ,NVL(CC."LegalPsn",' ')   AS "LegalPsn"
              ,NVL(CC."AccTelArea",' ') AS "AccTelArea"
              ,NVL(CC."AccTelNo",' ')   AS "AccTelNo"
              ,NVL(CC."AccTelExt",' ')  AS "AccTelExt"
              ,NVL(CC."LegalArea",' ')  AS "LegalArea"
              ,NVL(CC."LegalNo",' ')    AS "LegalNo"
              ,NVL(CC."LegalExt",' ')   AS "LegalExt"
              ,ROW_NUMBER() OVER (PARTITION BY CF."CustNo"
                                              ,CF."FacmNo"
                                  ORDER BY NVL(CM."ClCode1",0)
                                          ,NVL(CM."ClCode2",0)
                                          ,NVL(CM."ClNo",0))
                                        AS "Seq"
        FROM "ClFac" CF
        LEFT JOIN "ClMain" CM ON CM."ClCode1"  = CF."ClCode1"
                             AND CM."ClCode1"  = CF."ClCode2"
                             AND CM."ClNo"     = CF."ClNo"
        LEFT JOIN "CdCity" CC ON CC."CityCode" = NVL(CM."CityCode",'A')
        WHERE CF."MainFlag" = 'Y'
      ) S1 ON S1."CustNo" = C1."CustNo"
          AND S1."FacmNo" = C1."FacmNo"
          AND S1."Seq" = 1
      LEFT JOIN "CollList" Ori ON Ori."CustNo" = C1."CustNo"
                              AND Ori."FacmNo" = C1."FacmNo"
      LEFT JOIN "lastTxData" LTD ON LTD."CustNo" = C1."CustNo"
                                AND LTD."FacmNo" = C1."FacmNo"
                                AND LTD."CaseCode" = C1."CaseCode"
                                AND LTD."Seq" = 1
      LEFT JOIN LBM ON LBM."CustNo" = C1."CustNo"
                   AND LBM."FacmNo" = C1."FacmNo"
                   AND LBM."Seq" = 1
    ) T    
    ON  (     C."CustNo" = T."CustNo"
          AND C."FacmNo" = T."FacmNo"
        )  
    WHEN MATCHED THEN UPDATE SET C."ClCustNo"        = T."ClCustNo"
                                ,C."ClFacmNo"        = T."ClFacmNo"
                                ,C."ClRowNo"         = T."ClRowNo"
                                ,C."PrevIntDate"     = T."PrevIntDate"
                                ,C."NextIntDate"     = T."NextIntDate"
                                ,C."OvduTerm"        = T."OvduTerm"
                                ,C."OvduDays"        = T."OvduDays"
                                ,C."CurrencyCode"    = T."CurrencyCode"
                                ,C."PrinBalance"     = T."PrinBalance"
                                ,C."BadDebtBal"      = T."BadDebtBal"
                                ,C."AccCollPsn"      = T."AccCollPsn"
                                ,C."LegalPsn"        = T."LegalPsn"
                                ,C."Status"          = T."Status"
                                ,C."AcctCode"        = T."AcctCode"
                                ,C."FacAcctCode"     = T."FacAcctCode"
                                ,C."ClCode1"         = T."ClCode1"
                                ,C."ClCode2"         = T."ClCode2"
                                ,C."ClNo"            = T."ClNo"
                                ,C."RenewCode"       = T."RenewCode"
                                ,C."LastUpdate"      = T."LastUpdate"
                                ,C."LastUpdateEmpNo" = T."LastUpdateEmpNo"
                                ,C."IsSpecify"       = T."IsSpecify"
                                ,C."CityCode"        = T."CityCode"
                                ,C."AccTelArea"      = T."AccTelArea"          -- 催收人員電話-區碼 VARCHAR2 5 由連線交易維護此欄
                                ,C."AccTelNo"        = T."AccTelNo"            -- 催收人員電話 VARCHAR2 10 由連線交易維護此欄
                                ,C."AccTelExt"       = T."AccTelExt"           -- 催收人員電話-分機 VARCHAR2 5 由連線交易維護此欄
                                ,C."LegalArea"       = T."LegalArea"           -- 法務人員電話-區碼 VARCHAR2 5 由連線交易維護此欄
                                ,C."LegalNo"         = T."LegalNo"             -- 法務人員電話 VARCHAR2 10 由連線交易維護此欄
                                ,C."LegalExt"        = T."LegalExt"            -- 法務人員電話-分機 VARCHAR2 5 由連線交易維護此欄
    WHEN NOT MATCHED THEN INSERT ( "CustNo"              -- '戶號';
                                  ,"FacmNo"              -- '額度';
                                  ,"CaseCode"            -- '案件種類';
                                  ,"TxDate"              -- '作業日期';
                                  ,"TxCode"              -- '作業項目';
                                  ,"PrevIntDate"         -- '繳息迄日';
                                  ,"NextIntDate"         -- '下次應繳日';
                                  ,"OvduTerm"            -- '逾期期數';
                                  ,"OvduDays"            -- '逾期天數';
                                  ,"CurrencyCode"        -- '幣別';
                                  ,"PrinBalance"         -- '本金餘額';
                                  ,"BadDebtBal"          -- '呆帳餘額';
                                  ,"AccCollPsn"          -- '催收員';
                                  ,"LegalPsn"            -- '法務人員';
                                  ,"Status"              -- '戶況';
                                  ,"AcctCode"            -- 業務科目代號
                                  ,"FacAcctCode"         -- 額度業務科目   
                                  ,"ClCustNo"            -- '同擔保品戶號';
                                  ,"ClFacmNo"            -- '同擔保品額度';
                                  ,"ClRowNo"             -- '同擔保品序列號';
                                  ,"ClCode1"             -- '擔保品代號1';
                                  ,"ClCode2"             -- '擔保品代號2';
                                  ,"ClNo"                -- '擔保品號碼';
                                  ,"RenewCode"           -- 空白、1.展期一般 2.展期協議
                                  ,"AcDate"              -- 會計日期
                                  ,"CreateDate"          -- 建檔日期時間
                                  ,"CreateEmpNo"         -- 建檔人員
                                  ,"LastUpdate"          -- 最後更新日期時間
                                  ,"LastUpdateEmpNo"     -- 最後更新人員
                                  ,"IsSpecify"
                                  ,"CityCode"
                                  ,"AccTelArea"          -- 催收人員電話-區碼 VARCHAR2 5 由連線交易維護此欄
                                  ,"AccTelNo"            -- 催收人員電話 VARCHAR2 10 由連線交易維護此欄
                                  ,"AccTelExt"           -- 催收人員電話-分機 VARCHAR2 5 由連線交易維護此欄
                                  ,"LegalArea"           -- 法務人員電話-區碼 VARCHAR2 5 由連線交易維護此欄
                                  ,"LegalNo"             -- 法務人員電話 VARCHAR2 10 由連線交易維護此欄
                                  ,"LegalExt"            -- 法務人員電話-分機 VARCHAR2 5 由連線交易維護此欄
    ) VALUES ( T."CustNo"
              ,T."FacmNo"
              ,T."CaseCode"
              ,T."TxDate"
              ,T."TxCode"
              ,T."PrevIntDate"
              ,T."NextIntDate"
              ,T."OvduTerm"
              ,T."OvduDays"
              ,T."CurrencyCode"
              ,T."PrinBalance"
              ,T."BadDebtBal"
              ,T."AccCollPsn"
              ,T."LegalPsn"
              ,T."Status"
              ,T."AcctCode"
              ,T."FacAcctCode"
              ,T."ClCustNo"
              ,T."ClFacmNo"
              ,T."ClRowNo"
              ,T."ClCode1"
              ,T."ClCode2"
              ,T."ClNo"
              ,T."RenewCode"
              ,T."AcDate"
              ,T."CreateDate"
              ,T."CreateEmpNo"
              ,T."LastUpdate"
              ,T."LastUpdateEmpNo"
              ,'N'
              ,T."CityCode"
              ,T."AccTelArea"
              ,T."AccTelNo"
              ,T."AccTelExt"
              ,T."LegalArea"
              ,T."LegalNo"
              ,T."LegalExt"
    );

    UPD_CNT := UPD_CNT + sql%rowcount;

    /* 若 下次應繳日 > 前七營業日 且 下次應繳日 <= 前六營業日 寫入 CollRemind */
    MERGE INTO "CollRemind" C
    USING ( SELECT C1."CaseCode"        AS "CaseCode"        -- 案件種類 VARCHAR2 1
                  ,C1."CustNo"          AS "CustNo"          -- 借款人戶號 DECIMAL 7
                  ,C1."FacmNo"          AS "FacmNo"          -- 額度編號 DECIMAL 3
                  ,C1."AcDate"          AS "AcDate"          -- 作業日期 DecimalD 8
                  ,SUBSTR(EmpNo,0,6)    AS "TitaTlrNo"       -- 經辦 VARCHAR2 6
                  ,SUBSTR(TxtNo,0,8)    AS "TitaTxtNo"       -- 交易序號 VARCHAR2 8
                  ,'1'                  AS "CondCode"        -- 狀態 VARCHAR2 1
                  ,TBSDYF               AS "RemindDate"      -- 提醒日期 DecimalD 8
                  ,TBSDYF               AS "EditDate"        -- 維護日期 DecimalD 8
                  ,''                   AS "EditTime"        -- 維護時間 VARCHAR2 5
                  ,'01'                 AS "RemindCode"      -- 提醒項目 VARCHAR2 2
                  ,''                   AS "Remark"          -- 其他記錄 VARCHAR2 160
                  ,C1."CreateDate"      AS "CreateDate"      -- 建檔日期時間 DATE 
                  ,C1."CreateEmpNo"     AS "CreateEmpNo"     -- 建檔人員 VARCHAR2 6
                  ,C1."LastUpdate"      AS "LastUpdate"      -- 最後更新日期時間 DATE 
                  ,C1."LastUpdateEmpNo" AS "LastUpdateEmpNo" -- 最後更新人員 VARCHAR2 6
            FROM "CollList" C1
            WHERE C1."NextIntDate" > L7BSDYF AND C1."NextIntDate" <= L6BSDYF 
    ) T
    ON  (     C."CaseCode" = T."CaseCode"
          AND C."CustNo"   = T."CustNo"
          AND C."FacmNo"   = T."FacmNo"
          AND C."AcDate"   = T."AcDate"
          AND C."TitaTlrNo" = T."TitaTlrNo"
          AND C."TitaTxtNo" = T."TitaTxtNo"
        )  
    WHEN MATCHED THEN UPDATE SET C."CondCode"   = T."CondCode"
                                ,C."RemindDate" = T."RemindDate"
                                ,C."EditDate"   = T."EditDate"
                                ,C."EditTime"   = T."EditTime"
                                ,C."RemindCode" = T."RemindCode"
                                ,C."Remark"     = T."Remark"
    WHEN NOT MATCHED THEN INSERT ( "CaseCode"        -- 案件種類 VARCHAR2 1
                                  ,"CustNo"          -- 借款人戶號 DECIMAL 7
                                  ,"FacmNo"          -- 額度編號 DECIMAL 3
                                  ,"AcDate"          -- 作業日期 DecimalD 8
                                  ,"TitaTlrNo"       -- 經辦 VARCHAR2 6
                                  ,"TitaTxtNo"       -- 交易序號 VARCHAR2 8
                                  ,"CondCode"        -- 狀態 VARCHAR2 1
                                  ,"RemindDate"      -- 提醒日期 DecimalD 8
                                  ,"EditDate"        -- 維護日期 DecimalD 8
                                  ,"EditTime"        -- 維護時間 VARCHAR2 5
                                  ,"RemindCode"      -- 提醒項目 VARCHAR2 2
                                  ,"Remark"          -- 其他記錄 VARCHAR2 160
                                  ,"CreateDate"      -- 建檔日期時間 DATE 
                                  ,"CreateEmpNo"     -- 建檔人員 VARCHAR2 6
                                  ,"LastUpdate"      -- 最後更新日期時間 DATE 
                                  ,"LastUpdateEmpNo" -- 最後更新人員 VARCHAR2 6
    ) VALUES ( T."CaseCode"        -- 案件種類 VARCHAR2 1
              ,T."CustNo"          -- 借款人戶號 DECIMAL 7
              ,T."FacmNo"          -- 額度編號 DECIMAL 3
              ,T."AcDate"          -- 作業日期 DecimalD 8
              ,T."TitaTlrNo"       -- 經辦 VARCHAR2 6
              ,T."TitaTxtNo"       -- 交易序號 VARCHAR2 8
              ,T."CondCode"        -- 狀態 VARCHAR2 1
              ,T."RemindDate"      -- 提醒日期 DecimalD 8
              ,T."EditDate"        -- 維護日期 DecimalD 8
              ,T."EditTime"        -- 維護時間 VARCHAR2 5
              ,T."RemindCode"      -- 提醒項目 VARCHAR2 2
              ,T."Remark"          -- 其他記錄 VARCHAR2 160
              ,T."CreateDate"      -- 建檔日期時間 DATE 
              ,T."CreateEmpNo"     -- 建檔人員 VARCHAR2 6
              ,T."LastUpdate"      -- 最後更新日期時間 DATE 
              ,T."LastUpdateEmpNo" -- 最後更新人員 VARCHAR2 6
    );

    DBMS_OUTPUT.PUT_LINE('UPDATE END');

    -- 記錄程式結束時間
    JOB_END_TIME := SYSTIMESTAMP;

    commit;

    -- 例外處理
   Exception
   WHEN OTHERS THEN
    "Usp_L9_UspErrorLog_Ins"(
        'Usp_L5_CollList_Upd' -- UspName 預存程序名稱
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



