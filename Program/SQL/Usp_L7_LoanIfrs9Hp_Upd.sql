CREATE OR REPLACE PROCEDURE "Usp_L7_LoanIfrs9Hp_Upd"
(
-- 程式功能：維護 LoanIfrs9Hp 每月IFRS9欄位清單8
-- 執行時機：每月底日終批次(換日前)
-- 執行方式：EXEC "Usp_L7_LoanIfrs9Hp_Upd"(20201231,'System');
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
    TMNDYF         INT;         -- 本月月底日
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
    -- 抓本月月底日
    SELECT "TmnDyf"
    INTO TMNDYF
    FROM "TxBizDate"
    WHERE "DateCode" = 'ONLINE'
    ;

    -- 刪除舊資料
    DBMS_OUTPUT.PUT_LINE('DELETE LoanIfrs9Hp');

    DELETE FROM "LoanIfrs9Hp"
    WHERE "DataYM" = YYYYMM
      ;

    -- 寫入資料
    DBMS_OUTPUT.PUT_LINE('INSERT LoanIfrs9Hp');

    INSERT INTO "LoanIfrs9Hp"
    WITH "Work_HP" AS (

    -- 撈符合條件額度資料，寫入 "Work_HP" 暫存檔
    SELECT A."CustNo"            AS "CustNo"
         , A."FacmNo"            AS "FacmNo"
         , SUM("PreDrawdownAmt") AS "PreDrawdownAmt"
         , A."ApproveDate"       AS "ApproveDate"
    FROM ( -- (戶況=0正常戶,2催收戶,7部分轉呆戶) or (放款餘額=0 & 循環動用 & 循環動用期限 >= 月底日)
           SELECT M."CustNo"                  AS "CustNo"
                , M."FacmNo"                  AS "FacmNo"
                , CASE WHEN TRUNC(NVL(M."DrawdownDate",0) / 100 ) > YYYYMM THEN NVL(M."DrawdownAmt",0)
                       ELSE  0
                  END                         AS "PreDrawdownAmt"
                , NVL(F."ApproveDate",0)      AS "ApproveDate"   -- 核准日期(額度)
           FROM   "Ifrs9LoanData" M
             LEFT JOIN "Ifrs9FacData"  F   ON  F."CustNo" = M."CustNo"
                                          AND  F."FacmNo" = M."FacmNo"
           WHERE M."DataYM" =  YYYYMM                              
           -- 尚未撥款的資料 (已核撥記號=0 & 動支期限>=月底日)
           UNION
           SELECT F."CustNo"                  AS "CustNo"
                , F."FacmNo"                  AS "FacmNo"
                , 0                           AS "PreDrawdownAmt"
                , NVL(F."ApproveDate",0)      AS "ApproveDate"   -- 核准日期(額度)
           FROM   "Ifrs9FacData" F
           WHERE  F."DrawdownFg" = 0
             AND  F."DataYM" =  YYYYMM
         )   A
    WHERE TRUNC(A."ApproveDate" / 100 ) <= YYYYMM      -- 核准日期＞月底日時，此筆資料不計入
    GROUP BY A."CustNo", A."FacmNo", A."ApproveDate"
    ORDER BY A."CustNo", A."FacmNo"
      )


    SELECT YYYYMM                               AS "DataYM"             -- 年月份
         , HP."CustNo"                          AS "CustNo"             -- 戶號
         , NVL(C."CustId", ' ')                 AS "CustId"             -- 借款人ID / 統編
         , HP."FacmNo"                          AS "FacmNo"             -- 額度編號
         , NVL(F."ApplNo",0)                    AS "ApplNo"             -- 核准號碼
         , CASE WHEN NVL(C."EntCode",' ') IN ('1','2') THEN 1  --企金
                ELSE 2
           END                                  AS "CustKind"           -- 企業戶/個人戶
         , NVL(HP."ApproveDate",0)              AS "ApproveDate"        -- 核准日期(額度)
         , CASE
             WHEN F."LastBormRvNo" > 900 AND F."LastBormNo" = 0
                  THEN NVL(L."DrawdownDate",0)
             ELSE NVL(F."FirstDrawdownDate",0)
           END                                  AS "FirstDrawdownDate"  -- 初貸日期
         , NVL(F."LineAmt",0)                   AS "LineAmt"            -- 核准金額(台幣)
         , NVL("FacProd"."Ifrs9ProdCode", ' ')  AS "Ifrs9ProdCode"      -- 產品別
         , CASE WHEN NVL(F."RecycleCode",0) = 0 AND NVL(F."UtilDeadline",0) >= TMNDYF --非循環且動支期限>=月底日  
                     THEN  ( NVL(F."LineAmt",0) - NVL(F."UtilBal",0) + HP."PreDrawdownAmt" )
                WHEN NVL(F."RecycleCode",0) = 1 AND NVL(F."RecycleDeadline",0) >= TMNDYF --循環且循環動支期限>=月底日    
                     THEN  ( NVL(F."LineAmt",0) - NVL(F."UtilBal",0) + HP."PreDrawdownAmt" )
                ELSE 0     
           END                                  AS "AvblBal"            -- 可動用餘額(台幣)
                                                                           -- 核准額度 - 已動用額度餘額
                                                                           -- 撥款日＞月底日時，撥款金額要加回可動用餘額
         , NVL(F."RecycleCode",0)               AS "RecycleCode"        -- 該筆額度是否可循環動用  -- 0:非循環 1:循環
         , CASE WHEN F."IrrevocableFlag" = 'Y' THEN 1
                ELSE 0
           END                                  AS "IrrevocableFlag"    -- 該筆額度是否為不可撤銷  -- 1=是 0=否
         , CASE WHEN NVL(C."EntCode",' ') IN ('1','2') THEN        -- 企金 - 第一碼為CdIndustry.MainType	主計處大類
                     CDI."MainType" || SUBSTR(C."IndustryCode"),3,4)       
                ELSE '60000'
           END                                  AS "IndustryCode"       -- 主計處行業別代碼 
         , ' '                                  AS "OriRating"          -- 原始認列時時信用評等
         , ' '                                  AS "OriModel"           -- 原始認列時信用評等模型
         , ' '                                  AS "Rating"             -- 財務報導日時信用評等
         , ' '                                  AS "Model"              -- 財務報導日時信用評等模型
         , CASE
             WHEN NVL(CL."ClCode1",0) IN (1) THEN
               CASE WHEN to_number(NVL(CL."CityCode",0)) =  5 THEN  2  -- 2=房地-台北市
                    WHEN to_number(NVL(CL."CityCode",0)) = 10 THEN  3  -- 3=房地-新北市
                    WHEN to_number(NVL(CL."CityCode",0)) = 15 THEN  4  -- 4=房地-桃園市
                    WHEN to_number(NVL(CL."CityCode",0)) = 35 THEN  5  -- 5=房地-台中市
                    WHEN to_number(NVL(CL."CityCode",0)) = 65 THEN  6  -- 6=房地-台南市
                    WHEN to_number(NVL(CL."CityCode",0)) = 70 THEN  7  -- 7=房地-高雄市
                    ELSE 8                                             -- 8=房地-其他
               END
             WHEN NVL(CL."ClCode1",0) IN (2) THEN  1                   -- 1=土地
             WHEN NVL(CL."ClCode1",0) IN (3) THEN  9                   -- 9=股票
             WHEN NVL(CL."ClCode1",0) IN (5) THEN 12                   -- 12=銀行保證
             WHEN NVL(CL."ClCode1",0) IN (9) THEN
               CASE WHEN NVL(CL."ClCode2",0) = 1 THEN  11              -- 11=車子
                    WHEN NVL(CL."ClCode2",0) = 2 THEN  10              -- 10=機器設備
               END
             ELSE   0
           END                                  AS "LGDModel"           -- 違約損失率模型
         , 0                                    AS "LGD"                -- 違約損失率          -- 後面再更新
         , 0                                    AS "LineAmtCurr"        -- 核准金額(交易幣)    -- 後面再更新
         , 0                                    AS "AvblBalCurr"        -- 可動用餘額(交易幣)  -- 後面再更新
         , JOB_START_TIME                       AS "CreateDate"         -- 建檔日期時間
         , EmpNo                                AS "CreateEmpNo"        -- 建檔人員
         , JOB_START_TIME                       AS "LastUpdate"         -- 最後更新日期時間
         , EmpNo                                AS "LastUpdateEmpNo"    -- 最後更新人員
    FROM "Work_HP" HP
         LEFT JOIN "CustMain" C   ON  C."CustNo" = HP."CustNo"
         LEFT JOIN "FacMain"  F   ON  F."CustNo" = HP."CustNo"
                                 AND  F."FacmNo" = HP."FacmNo"
        LEFT JOIN "LoanBorMain" L ON L."CustNo"  = F."CustNo"
                                 AND L."FacmNo"  = F."FacmNo"
                                 AND L."BormNo"  = F."LastBormRvNo"
         LEFT JOIN "FacProd"   ON "FacProd"."ProdNo"   =  F."ProdNo"
         LEFT JOIN "ClFac"     ON "ClFac"."ApproveNo"  =  F."ApplNo"
                              AND "ClFac"."MainFlag"   =  'Y'          -- 主要擔保品
         LEFT JOIN "ClMain" CL   ON CL."ClCode1"  =  "ClFac"."ClCode1"
                                AND CL."ClCode2"  =  "ClFac"."ClCode2"
                                AND CL."ClNo"     =  "ClFac"."ClNo"
         LEFT JOIN "CdIndustry" CDI ON CDI."IndustryCode" = C."IndustryCode"
    ORDER BY HP."CustNo", HP."FacmNo"
      ;

    INS_CNT := INS_CNT + sql%rowcount;
    DBMS_OUTPUT.PUT_LINE('INSERT LoanIfrs9Hp END: INS_CNT=' || INS_CNT);


-- 更新 LGD 違約損失率
    DBMS_OUTPUT.PUT_LINE('UPDATE LGD 違約損失率');
    UPD_CNT := 0;

    MERGE INTO "LoanIfrs9Hp" M
    USING ( SELECT L."Date"
                 , L."Type"
                 , L."LGDPercent"
                 , ROW_NUMBER() Over (Partition By L."Type"
                                          Order By L."Type", L."Date" DESC)
                                AS ROW_NO
            FROM  "Ias39LGD" L
            WHERE TRUNC(NVL(L."Date",0) / 100 ) <= YYYYMM   -- 生效日
              --AND L."Enable" = 'Y'                          -- 啟用記號
          ) LGD
    ON (    M."DataYM"    =  YYYYMM
        AND LGD.ROW_NO    =  1           -- 撈最新生效這一筆
        AND M."LGDModel"  =  to_number(LGD."Type")
       )
    WHEN MATCHED THEN UPDATE
     SET M."LGD" = LGD."LGDPercent"
       ;

    UPD_CNT := UPD_CNT + sql%rowcount;
    DBMS_OUTPUT.PUT_LINE('UPDATE LGD 違約損失率 END');


-- 更新 Curr [交易幣]金額
    DBMS_OUTPUT.PUT_LINE('UPDATE Curr [交易幣]金額');
    UPD_CNT := 0;

    UPDATE "LoanIfrs9Hp" M
    SET   M."LineAmtCurr"      =  M."LineAmt"
        , M."AvblBalCurr"      =  M."AvblBal"
    WHERE M."DataYM"  =  YYYYMM
      ;

    UPD_CNT := UPD_CNT + sql%rowcount;
    DBMS_OUTPUT.PUT_LINE('UPDATE Curr [交易幣]金額 END');


    -- 記錄程式結束時間
    JOB_END_TIME := SYSTIMESTAMP;
    DBMS_OUTPUT.PUT_LINE('Spend Time: ' ||
        to_number(  to_date(to_char(JOB_END_TIME,'  yyyy-mm-dd hh24:mi:ss'), 'yyyy-mm-dd hh24:mi:ss')
                  - to_date(to_char(JOB_START_TIME,'yyyy-mm-dd hh24:mi:ss'), 'yyyy-mm-dd hh24:mi:ss')
                  ) * 86400       || ' Secs' );

    commit;

  END;
END;

     --  , YYYYMM                               AS "DataYM"             -- 年月份
     --  , HP."CustNo"                          AS "CustNo"             -- 戶號
     --  , NVL("CustMain"."CustId", ' ')        AS "CustId"             -- 借款人ID / 統編
     --  , HP."FacmNo"                          AS "FacmNo"             -- 額度編號
     --  , 0                                    AS "ApplNo"             -- 核准號碼
     --  , 0                                    AS "CustKind"           -- 企業戶/個人戶
     --  , 0                                    AS "ApproveDate"        -- 核准日期
     --  , 0                                    AS "FirstDrawdownDate"  -- 初貸日期
     --  , 0                                    AS "LineAmt"            -- 核准金額(台幣)
     --  , ' '                                  AS "Ifrs9ProdCode"      -- 產品別
     --  , 0                                    AS "AvblBal"            -- 可動用餘額(台幣)
     --  , ' '                                  AS "RecycleCode"        -- 該筆額度是否可循環動用
     --  , ' '                                  AS "IrrevocableFlag"    -- 該筆額度是否為不可徹銷
     --  , ' '                                  AS "IndustryCode"       -- 主計處行業別代碼
     --  , ' '                                  AS "OriRating"          -- 原始認列時時信用評等
     --  , ' '                                  AS "OriModel"           -- 原始認列時信用評等模型
     --  , ' '                                  AS "Rating"             -- 財務報導日時信用評等
     --  , ' '                                  AS "Model"              -- 財務報導日時信用評等模型
     --  , 0                                    AS "LGDModel"           -- 違約損失率模型
     --  , 0                                    AS "LGD"                -- 違約損失率
     --  , 0                                    AS "LineAmtCurr"        -- 核准金額(交易幣)
     --  , 0                                    AS "AvblBalCurr"        -- 可動用餘額(交易幣)
