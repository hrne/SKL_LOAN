CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_L8_JcicB211_Upd"
(
-- 程式功能：維護 JcicB211 聯徵每日授信餘額變動資料檔
-- 執行時機：每日日終批次(換日前)
-- 執行方式：EXEC "Usp_L8_JcicB211_Upd"(20200423,'999999');
--

    -- 參數
    TBSDYF         IN  INT,        -- 系統營業日(西元)
    EmpNo          IN  VARCHAR2    -- 經辦
)
AS
BEGIN
  DECLARE
    INS_CNT        INT;         -- 新增筆數
    UPD_CNT        INT;         -- 更新筆數
    JOB_START_TIME TIMESTAMP;   -- 記錄程式起始時間
    JOB_END_TIME   TIMESTAMP;   -- 記錄程式結束時間
    YYYYMM         INT;         -- 本月年月
    LYYYYMM        INT;         -- 上月年月
    MM             INT;         -- 本月月份
    YYYY           INT;         -- 本月年度
  BEGIN
    INS_CNT := 0;
    UPD_CNT := 0;

    -- 記錄程式起始時間
    JOB_START_TIME := SYSTIMESTAMP;

    --　本月年月
    YYYYMM := TBSDYF / 100;
    --
    MM := MOD(YYYYMM, 100);
    YYYY := TRUNC(YYYYMM / 100);
    IF MM = 1 THEN
       LYYYYMM := (YYYY - 1) * 100 + 12;
    ELSE
       LYYYYMM := YYYYMM - 1;
    END IF;

    -- 刪除舊資料
    DBMS_OUTPUT.PUT_LINE('DELETE JcicB211');

    DELETE FROM "JcicB211"
    WHERE "DataYMD" = TBSDYF
      ;

    -- 寫入資料
    DBMS_OUTPUT.PUT_LINE('INSERT JcicB211');

    INSERT INTO "JcicB211"
    WITH "Work_B211" AS (

    -- 寫入資料 Work_B211

    -- 本日撥款
    SELECT  T."CustNo"                                       AS "CustNo"       -- 戶號
          , T."FacmNo"                                       AS "FacmNo"       -- 額度編號
          , T."BormNo"                                       AS "BormNo"       -- 撥款序號
          , T."BorxNo"                                       AS "BorxNo"       -- 交易內容檔序號
          , 'A'                                              AS "TranCode"     -- 交易代碼
          , TBSDYF                                           AS "AcDate"       -- 交易日期
          , 'A'                                              AS "SubTranCode"  -- 交易屬性
          , T."TxAmt"                                        AS "TxAmt"        -- 本筆撥款／還款金額
          , T."LoanBal"                                      AS "LoanBal"      -- 本筆撥款／還款餘額
          , M."Status"                                       AS "Status"       -- 戶況
          , 'X'                                              AS "RepayCode"    -- 本筆還款後之還款紀錄
          , ' '                                              AS "NegStatus"    -- 本筆還款後之債權結案註記
          , 0                                                AS "BadDebtDate"  -- 呆帳轉銷年月
     FROM ( SELECT  T."CustNo", T."FacmNo", T."BormNo", T."BorxNo", T."TxAmt", T."LoanBal"
              FROM  "LoanBorTx" T
             WHERE  T."AcDate"    =  TBSDYF
               AND  T."TitaHCode" =  0
               AND  T."TitaTxCd"  =  'L3100'
             ORDER BY T."CustNo", T."FacmNo", T."BormNo", T."BorxNo"
          ) T
       LEFT JOIN "LoanBorMain" M
             ON   M."CustNo"    =  T."CustNo"
            AND   M."FacmNo"    =  T."FacmNo"
            AND   M."BormNo"    =  T."BormNo"
     WHERE  NVL(M."RenewFlag",' ') NOT IN ( '1' , '2' , 'Y')  -- 非借新還舊

     UNION

    -- 本日還款
    SELECT  T."CustNo"                                       AS "CustNo"       -- 戶號
          , T."FacmNo"                                       AS "FacmNo"       -- 額度編號
          , T."BormNo"                                       AS "BormNo"       -- 撥款序號
          , T."BorxNo"                                       AS "BorxNo"       -- 交易內容檔序號
          , DECODE(T."TitaHCode", 3, 'D', 'A')               AS "TranCode"     -- 交易代碼
          , CASE WHEN T."OrigAcDate" > 0
                 THEN T."OrigAcDate" ELSE T."AcDate" END     AS "AcDate"       -- 交易日期
          , 'B'                                              AS "SubTranCode"  -- 交易屬性
          , T."Principal"                                    AS "TxAmt"        -- 本筆撥款／還款金額
          , T."LoanBal"                                      AS "LoanBal"      -- 本筆撥款／還款餘額
          , T."Status"                                       AS "Status"       -- 戶況
          , CASE WHEN T."OvduMon" >= 6  THEN '6'
                 WHEN T."OvduMon"  = 5  THEN '5'
                 WHEN T."OvduMon"  = 4  THEN '4'
                 WHEN T."OvduMon"  = 3  THEN '3'
                 WHEN T."OvduMon"  = 2  THEN '2'
                 WHEN T."OvduMon"  = 1  THEN '1'
                 WHEN T."OvduDay" >= 16 THEN '0'															 -- 現金卡科目才有AB,其他一個月內視同無延遲
                 WHEN T."OvduDay"  > 0  THEN '0'                               --
                 ELSE '0'  END                               AS "RepayCode"    -- 本筆還款後之還款紀錄
          , CASE --WHEN T."TitaTxCd" IN ('L3410', 'L3420') AND T."Status" IN ( 1 , 3 ) THEN '3'
                 WHEN T."TitaTxCd" IN ('L3410', 'L3420') THEN T."TxStatus"
                 ELSE ' ' END                       AS "NegStatus"    -- 本筆還款後之債權結案註記
          , CASE WHEN T."Status" = 6 AND NVL(O."BadDebtDate",0) > 0
                 THEN TRUNC(O."BadDebtDate" / 100) - 191100
                 ELSE 0 END                                  AS "BadDebtDate"  -- 呆帳轉銷年月
     FROM ( SELECT  T3."CustNo", T3."FacmNo", T3."BormNo", T3."BorxNo", T3."Principal", T3."LoanBal"
                  , T3."OrigAcDate", T3."AcDate", T3."TitaHCode", T3."TitaTxCd"
                  , CASE WHEN  M."Status" IN ( 2 , 6 ) THEN 6
                         WHEN  M."NextPayIntDate" >= T3."AcDate"  THEN 0
                         WHEN  M."NextPayIntDate" = 0 THEN 0
                         ELSE  TO_DATE(T3."AcDate",'yyyy-mm-dd')
                             - TO_DATE(M."NextPayIntDate",'yyyy-mm-dd') END "OvduDay"
                  , CASE WHEN  M."Status" IN ( 2 , 6 ) THEN 6     -- 催收戶,呆帳戶
                         WHEN  M."Status" IN ( 1, 5 , 9 ) THEN 0  -- 展期,催收結案,呆帳結案
                         WHEN  M."NextPayIntDate" >= T3."AcDate"  THEN 0
                         WHEN  M."NextPayIntDate" = 0 THEN 0
                         WHEN  MOD( NVL(M."PrevPayIntDate", 0) , 100) = MOD( NVL(T3."AcDate", 0) , 100)   THEN 
                               TRUNC(MONTHS_BETWEEN(TO_DATE(T3."AcDate",'YYYY-MM-DD')  
                                     , TO_DATE(M."PrevPayIntDate",'YYYY-MM-DD'))) - 1    -- 計算後剛好為1個月時,當成未超過1個月故需減1個月
                         ELSE  TRUNC(MONTHS_BETWEEN(TO_DATE(T3."AcDate",'YYYY-MM-DD')
                                     , TO_DATE(M."PrevPayIntDate",'YYYY-MM-DD')))  -- 由NextPayIntDate改為PrevPayIntDate
                    END    AS "OvduMon"
                  , M."Status", M."LastOvduNo",T3."TxStatus"
             FROM ( SELECT  T1."CustNo", T1."FacmNo", T1."BormNo", T1."BorxNo"
                          , ( T1."Principal"  ) AS "Principal"
                          , CASE WHEN T1."TitaHCode" = 3 THEN  NVL(LT."LoanBal",0)  -- 找原被沖正的那筆餘額
                                 ELSE T1."LoanBal"
                            END  AS "LoanBal"     
                          , T1."TitaHCode", T1."TitaTxCd", T1."AcDate"
                          , CASE WHEN T1."TitaHCode" = 3 AND T1."CorrectSeq" IS NOT NULL THEN
                                      CAST(SUBSTR(T1."CorrectSeq", 1, 8) AS decimal(8, 0))
                                 ELSE 0
                            END  AS "OrigAcDate"   -- 原交易會計日期
                          , ' ' AS "TxStatus"  -- 非結案-債權結案註記= ' ' 
                      FROM  "LoanBorTx" T1
                      LEFT JOIN "LoanBorTx" LT ON LT."AcDate" = to_number(SUBSTR(NVL(T1."CorrectSeq",'0'), 1, 8))
                                              AND LT."TitaTlrNo" = SUBSTR(NVL(T1."CorrectSeq",'0'), 13, 6)
                                              AND LT."TitaTxtNo" = SUBSTR(NVL(T1."CorrectSeq",'0'), 19, 8)
                     WHERE  T1."AcDate"    =  TBSDYF
                       AND  T1."TitaHCode" IN (0, 3)
                       AND  T1."Principal"  <> 0
                       AND  T1."TitaTxCd"  NOT IN ('L3410','L3420')
                    UNION
                    SELECT  T2."CustNo", T2."FacmNo", T2."BormNo", T2."BorxNo"
                          , CASE WHEN M."Status" IN ( 2 , 6 ) THEN 1
                                 ELSE T2."Principal"  
                            END             AS "Principal"
                          , CASE WHEN M."Status" IN ( 2 , 6 ) THEN (T2."Principal" + T2."Interest")  -- 催收呆帳戶=本金+利息
                                 WHEN T2."TitaHCode" = 3 THEN  NVL(LB."LoanBal",0)  -- 找原被沖正的那筆餘額
                                 ELSE T2."LoanBal"
                            END             AS  "LoanBal"     
                          , T2."TitaHCode", T2."TitaTxCd", T2."AcDate"
                          , CASE WHEN T2."TitaHCode" = 3 AND T2."CorrectSeq" IS NOT NULL THEN
                                      CAST(SUBSTR(T2."CorrectSeq", 1, 8) AS decimal(8, 0))
                                 ELSE 0
                            END  AS "OrigAcDate"   -- 原交易會計日期
                          , CASE WHEN M."Status" IN ( 5 , 9 ) AND 
                                    NVL( JSON_VALUE (T2."OtherFields", '$.CaseCloseCode'),0) = '4'  THEN 'P' -- 不良債權
                                 WHEN M."Status" IN ( 5 , 9 ) AND 
                                    NVL( JSON_VALUE (T2."OtherFields", '$.CaseCloseCode'),0) = '5'  THEN 'Q' -- 不良債權
                                 WHEN M."Status" IN ( 5 , 9 ) AND 
                                    NVL( JSON_VALUE (T2."OtherFields", '$.CaseCloseCode'),0) = '6'  THEN 'D' -- 不良債權
                                 WHEN M."Status" IN ( 1 , 3 ) THEN '3'  -- 展期,正常結案
                                 ELSE ' '
                            END  AS "TxStatus"  -- 結案-債權結案註記   
                    FROM  "LoanBorTx" T2
                    LEFT JOIN "LoanBorMain" M    ON  M."CustNo"    =  T2."CustNo"
                                                AND  M."FacmNo"    =  T2."FacmNo"
                                                AND  M."BormNo"    =  T2."BormNo"
                    LEFT JOIN "AcLoanRenew"  AL  ON AL."CustNo"    =  T2."CustNo"
                                                AND AL."OldFacmNo" =  T2."FacmNo"
                                                AND AL."OldBormNo" =  T2."BormNo"
                                                AND AL."AcDate"    =  T2."AcDate"   
                    LEFT JOIN "LoanBorTx" LB ON LB."AcDate" = to_number(SUBSTR(NVL(T2."CorrectSeq",'0'), 1, 8))
                                            AND LB."TitaTlrNo" = SUBSTR(NVL(T2."CorrectSeq",'0'), 13, 6)
                                            AND LB."TitaTxtNo" = SUBSTR(NVL(T2."CorrectSeq",'0'), 19, 8)
                    WHERE  T2."AcDate"    =  TBSDYF
                      AND  T2."TitaHCode" IN (0, 3)
                      AND  T2."Principal"  <> 0
                      AND  T2."TitaTxCd"  IN ('L3410', 'L3420')
                      AND  M."Status" IN (1 , 2 , 3 , 5 , 6 , 7 , 9)  -- 結案登錄戶況增列5,7,9
                      AND  NVL(AL."CustNo",0)       =    0  -- 比照204檔-展期不申報
                  ) T3
             LEFT JOIN "LoanBorMain" M    ON  M."CustNo"    =  T3."CustNo"
                                         AND  M."FacmNo"    =  T3."FacmNo"
                                         AND  M."BormNo"    =  T3."BormNo"
             LEFT JOIN ( SELECT DISTINCT A."CustNo"   AS  "CustNo"
                                       , A."FacmNo"   AS  "FacmNo"
                         FROM  "RptJcic" A
                       ) A    ON  A."CustNo"    =  T3."CustNo"
                             AND  A."FacmNo"    =  T3."FacmNo"     -- 呆帳不報送檔
             WHERE (  M."Status" IN (0, 1, 2, 3, 5, 7 ) OR
                     (M."Status" IN (6, 9) AND A."CustNo" IS NULL)
                   )
               AND (  T3."TitaHCode" = 0      OR
                     (T3."TitaHCode" = 3 AND T3."OrigAcDate" <> T3."AcDate" AND TRUNC(T3."OrigAcDate" / 100) = YYYYMM)
                   )
             ORDER BY T3."CustNo", T3."FacmNo", T3."BormNo", T3."BorxNo"
          ) T
      LEFT JOIN "LoanOverdue" O
        ON   O."CustNo"    =  T."CustNo"
        AND  O."FacmNo"    =  T."FacmNo"
        AND  O."BormNo"    =  T."BormNo"
        AND  O."OvduNo"    =  T."LastOvduNo"
      WHERE  T."Status" IN (0, 1 , 2, 3, 5, 7 )
         OR (T."Status" IN (6 , 9 ) AND O."AcctCode" = '990')
      )

    SELECT TBSDYF                                AS "DataYMD"           -- 資料日期
         , '458'                                 AS "BankItem"          -- 總行代號
         , '0001'                                AS "BranchItem"        -- 分行代號
         , T."TranCode"                          AS "TranCode"          -- 交易代碼
         , C."CustId"                            AS "CustId"            -- 授信戶IDN/BAN
         , T."SubTranCode"                       AS "SubTranCode"       -- 交易屬性
         , T."AcDate" - 19110000                 AS "AcDate"            -- 交易日期
         , TRIM(TO_CHAR(T."CustNo",'0000000'))  ||  TRIM(TO_CHAR(T."FacmNo",'000')) ||
           TRIM(TO_CHAR(T."BormNo",'000'))       AS "AcctNo"            -- 本筆撥款／還款帳號
         , T."BorxNo"                            AS "BorxNo"            -- 交易內容檔序號
         , CASE WHEN T."TxAmt" = 0 THEN 0
                WHEN ROUND(ABS(T."TxAmt") / 1000, 0) < 1 THEN 1
                ELSE ROUND(ABS(T."TxAmt") / 1000, 0)
           END                                   AS "TxAmt"             -- 本筆撥款／還款金額
         , CASE WHEN T."LoanBal" = 0 THEN 0
                WHEN ROUND(T."LoanBal" / 1000, 0) < 1 THEN 1
                ELSE ROUND(T."LoanBal" / 1000, 0)
           END                                   AS "LoanBal"           -- 本筆撥款／還款餘額
         , T."RepayCode"                         AS "RepayCode"         -- 本筆還款後之還款紀錄
         , T."NegStatus"                         AS "NegStatus"         -- 本筆還款後之債權結案註記
         , CASE WHEN T."Status" = 2  THEN 'A'
                WHEN T."Status" = 6  THEN 'B'
                WHEN FM."AcctCode" = '310' THEN 'E'
                WHEN FM."AcctCode" = '320' THEN 'H'
                ELSE 'I'
           END                                   AS "AcctCode"          -- 科目別
         , 'S'                                   AS "SubAcctCode"       -- 科目別註記
         , T."BadDebtDate"                       AS "BadDebtDate"       -- 呆帳轉銷年月月
         , CASE WHEN C."EntCode" IN ('0', '2') THEN   -- 個人
                  CASE WHEN CF."ClCode1" = 9 AND CF."ClCode2" = 1 THEN 'Y'
                       ELSE 'N'
                  END
                ELSE 'X'
           END                                   AS "ConsumeFg"         -- 個人消費性貸款註記
         , CASE WHEN C."EntCode" IN ('0', '2') THEN   -- 個人
                  CASE  WHEN CF."ClCode1" = 2 THEN '1'
                        WHEN FM."UsageCode" IN ('02','03','04','09') THEN 'M'
                        ELSE  '1'
                  END
                ELSE 'K'
            END                                  AS "FinCode"           -- 融資業務分類
         , CASE WHEN FM."UsageCode" IN ('02','03','04','09')  THEN '1'
                WHEN FM."UsageCode" IN ('06')                 THEN '2'
                WHEN FM."UsageCode" IN ('05')                 THEN '3'
                ELSE '4'
           END                                   AS "UsageCode"         -- 用途別
         , ' '                                   AS "Filler18"          -- 空白
         , JOB_START_TIME                        AS "CreateDate"        -- 建檔日期時間
         , EmpNo                                 AS "CreateEmpNo"       -- 建檔人員
         , JOB_START_TIME                        AS "LastUpdate"        -- 最後更新日期時間
         , EmpNo                                 AS "LastUpdateEmpNo"   -- 最後更新人員
     FROM "Work_B211" T
     LEFT JOIN "FacMain" FM
       ON   FM."CustNo"   =  T."CustNo"
       AND  FM."FacmNo"   =  T."FacmNo"
     LEFT JOIN "ClFac" CF
       ON   CF."CustNo"   =  T."CustNo"
       AND  CF."FacmNo"   =  T."FacmNo"
       AND  CF."MainFlag" =  'Y'
     LEFT JOIN "CustMain" C
       ON   C."CustNo"    =  T."CustNo"
      ;

    INS_CNT := INS_CNT + sql%rowcount;

    DBMS_OUTPUT.PUT_LINE('INSERT JcicB211 END: INS_CNT=' || INS_CNT);

    -- 記錄程式結束時間
    JOB_END_TIME := SYSTIMESTAMP;
    DBMS_OUTPUT.PUT_LINE('Spend Time: ' ||
        to_number(  to_date(to_char(JOB_END_TIME,'  yyyy-mm-dd hh24:mi:ss'), 'yyyy-mm-dd hh24:mi:ss')
                  - to_date(to_char(JOB_START_TIME,'yyyy-mm-dd hh24:mi:ss'), 'yyyy-mm-dd hh24:mi:ss')
                  ) * 86400       || ' Secs' );

    commit;

  END;
END;