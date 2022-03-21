create or replace NONEDITIONABLE PROCEDURE "Usp_L7_Ias34Bp_Upd"
(
-- 程式功能：維護 Ias34Bp 每月IAS34資料欄位清單B檔
-- 執行時機：每月底日終批次(換日前)
-- 執行方式：EXEC "Usp_L7_Ias34Bp_Upd"(20211230,'999999');
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
    DBMS_OUTPUT.PUT_LINE('DELETE Ias34Bp');

    DELETE FROM "Ias34Bp"
    WHERE "DataYM" = YYYYMM
    ;

    -- 寫入資料
    DBMS_OUTPUT.PUT_LINE('INSERT Ias34Bp');
    INS_CNT := 0;

    INSERT INTO "Ias34Bp"
    SELECT
           YYYYMM                                AS "DataYM"            -- 資料年月
         , NVL(M."CustNo",0)                     AS "CustNo"            -- 戶號
         , NVL(M."CustId",' ')                   AS "CustId"            -- 借款人ID / 統編
         , NVL(M."FacmNo",0)                     AS "FacmNo"            -- 額度編號
         , NVL(M."BormNo",0)                     AS "BormNo"            -- 撥款序號
         , ROUND(NVL(C."FitRate",0) / 100, 6)    AS "LoanRate"          -- 貸放利率
         , CASE
             WHEN NVL(F."Ifrs9StepProdCode",' ') = 'B' THEN 4  -- 浮動階梯
             WHEN NVL(F."Ifrs9StepProdCode",' ') = 'A' THEN 3  -- 固定階梯
             WHEN NVL(M."RateCode", '0')  IN ('1','3')    THEN 1  -- 機動
             WHEN NVL(M."RateCode", '0')  = '2'           THEN 2  -- 固定
             ELSE to_number(NVL(M."RateCode", '0'))
           END                                   AS "RateCode"          -- 利率調整方式（1=機動；2=固定；3=固定階梯；4=浮動階梯）
         , NVL(C."EffectDate",0)                 AS "EffectDate"        -- 利率欄位生效日（西元年）
         , JOB_START_TIME                        AS "CreateDate"        -- 建檔日期時間
         , EmpNo                                 AS "CreateEmpNo"       -- 建檔人員
         , JOB_START_TIME                        AS "LastUpdate"        -- 最後更新日期時間
         , EmpNo                                 AS "LastUpdateEmpNo"   -- 最後更新人員
    FROM   "Ifrs9LoanData" M
      LEFT JOIN "Ifrs9FacData" F  ON F."DataYM"  = M."DataYM"
                                 AND F."CustNo"  = M."CustNo"
                                 AND F."FacmNo"  = M."FacmNo"
      LEFT JOIN (
        SELECT "CustNo"
             , "FacmNo"
             , "BormNo"
             , "FitRate"
             , "EffectDate"
        FROM "LoanRateChange"
        UNION
        SELECT LBM."CustNo"
             , LBM."FacmNo"
             , LBM."BormNo"
             -- 利率= Ifrs9FacData.ProdNo 對應FacProd.ProdNo 找BaseRateCode => 
             -- 再找 生效日<=月底日且最接近的一筆(CdBaseRate.BaseRate 基本利率) + LoanBorMain."RateIncr" 加碼利率          
             , NVL(CBR."BaseRate",0)
               + LBM."RateIncr"       AS "FitRate"
             , LBM."FirstAdjRateDate" AS "EffectDate"
        FROM "LoanBorMain" LBM
        LEFT JOIN "LoanRateChange" LRC ON LRC."CustNo" = LBM."CustNo"
                                      AND LRC."FacmNo" = LBM."FacmNo"
                                      AND LRC."BormNo" = LBM."BormNo"
                                      AND TRUNC(NVL(LRC."EffectDate",0) / 100) > YYYYMM
        LEFT JOIN (
            SELECT DISTINCT
                   "CustNo"
                 , "FacmNo"
                 , "BormNo"
                 , "Status"
            FROM "LoanRateChange"
            WHERE "Status" = 2 -- 找出任一筆有建過加碼的資料
        ) LRC_2 ON LRC_2."CustNo" = LBM."CustNo"
               AND LRC_2."FacmNo" = LBM."FacmNo"
               AND LRC_2."BormNo" = LBM."BormNo"
        LEFT JOIN "FacMain" FAC ON FAC."CustNo" = LBM."CustNo"
                               AND FAC."FacmNo" = LBM."FacmNo"
        LEFT JOIN "FacProd" PROD ON PROD."ProdNo" = FAC."ProdNo"
        LEFT JOIN (
          SELECT "BaseRateCode"
               , "BaseRate"
               , ROW_NUMBER()
                 OVER (
                   PARTITION BY "BaseRateCode"
                   ORDER BY "EffectDate" DESC
                 ) AS "Seq"
          FROM "CdBaseRate" 
          WHERE TRUNC("EffectDate"/100) <= YYYYMM
        ) CBR ON CBR."BaseRateCode" = PROD."BaseRateCode"
             AND CBR."Seq" = 1 -- 只抓基礎利率生效日最接近本月月底日的一筆
        WHERE TRUNC(NVL(LBM."FirstAdjRateDate",0) / 100) > YYYYMM
          AND NVL(LRC."EffectDate",0) = 0 -- 在於放款利率變動檔沒有未來資料的,才寫入
          AND NVL(LRC_2."Status",0) != 2 -- 剔除有建加碼資料
        UNION
        SELECT LBM."CustNo"
             , LBM."FacmNo"
             , LBM."BormNo"
             -- 利率= Ifrs9FacData.ProdNo 對應FacProd.ProdNo 找BaseRateCode => 
             -- 再找 生效日<=月底日且最接近的一筆(CdBaseRate.BaseRate 基本利率) + LoanBorMain."RateIncr" 加碼利率          
             , NVL(CBR."BaseRate",0)
               + LBM."RateIncr"       AS "FitRate"
             , LBM."NextAdjRateDate" AS "EffectDate"
        FROM "LoanBorMain" LBM
        LEFT JOIN "LoanRateChange" LRC ON LRC."CustNo" = LBM."CustNo"
                                      AND LRC."FacmNo" = LBM."FacmNo"
                                      AND LRC."BormNo" = LBM."BormNo"
                                      AND TRUNC(NVL(LRC."EffectDate",0) / 100) > YYYYMM
        LEFT JOIN (
            SELECT DISTINCT
                   "CustNo"
                 , "FacmNo"
                 , "BormNo"
                 , "Status"
            FROM "LoanRateChange"
            WHERE "Status" = 2 -- 找出任一筆有建過加碼的資料
        ) LRC_2 ON LRC_2."CustNo" = LBM."CustNo"
               AND LRC_2."FacmNo" = LBM."FacmNo"
               AND LRC_2."BormNo" = LBM."BormNo"
        LEFT JOIN "FacMain" FAC ON FAC."CustNo" = LBM."CustNo"
                               AND FAC."FacmNo" = LBM."FacmNo"
        LEFT JOIN "FacProd" PROD ON PROD."ProdNo" = FAC."ProdNo"
        LEFT JOIN (
          SELECT "BaseRateCode"
               , "BaseRate"
               , ROW_NUMBER()
                 OVER (
                   PARTITION BY "BaseRateCode"
                   ORDER BY "EffectDate" DESC
                 ) AS "Seq"
          FROM "CdBaseRate" 
          WHERE TRUNC("EffectDate"/100) <= YYYYMM
        ) CBR ON CBR."BaseRateCode" = PROD."BaseRateCode"
             AND CBR."Seq" = 1 -- 只抓基礎利率生效日最接近本月月底日的一筆
        WHERE TRUNC(NVL(LBM."NextAdjRateDate",0) / 100) > YYYYMM
          AND NVL(LRC."EffectDate",0) = 0 -- 在於放款利率變動檔沒有未來資料的,才寫入
          AND NVL(LRC_2."Status",0) != 2 -- 剔除有建加碼資料
      ) C ON C."CustNo"  = M."CustNo"
         AND C."FacmNo"  = M."FacmNo"
         AND C."BormNo"  = M."BormNo"
    WHERE  M."DataYM"  =  YYYYMM
      AND  M."Status" IN (0, 2, 7)   -- 正常件, 催收, 部分轉呆
      AND CASE
            WHEN F."Ifrs9StepProdCode" IN ('B')                   -- 浮動階梯4
            THEN 1
            WHEN F."Ifrs9StepProdCode" IN ('A')
                 AND TRUNC(NVL(C."EffectDate",0) / 100) <= YYYYMM -- 固定階梯3, skip 尚未生效者
            THEN 1
            WHEN NVL(M."RateCode", '0')  IN ('1','3')
                 AND TRUNC(NVL(C."EffectDate",0) / 100) <= YYYYMM -- 機動1, skip 尚未生效者
            THEN 1
            WHEN NVL(M."RateCode", '0')  = '2'                    -- 固定2
            THEN 1
          ELSE 0
          END = 1
    ;

    INS_CNT := INS_CNT + sql%rowcount;
    DBMS_OUTPUT.PUT_LINE('INSERT Ias34Bp END');
    DBMS_OUTPUT.PUT_LINE('INSERT Ias34Bp INS_CNT=' || INS_CNT);


    -- 記錄程式結束時間
    JOB_END_TIME := SYSTIMESTAMP;
    DBMS_OUTPUT.PUT_LINE('Spend Time: ' ||
        to_number(  to_date(to_char(JOB_END_TIME,'  yyyy-mm-dd hh24:mi:ss'), 'yyyy-mm-dd hh24:mi:ss')
                  - to_date(to_char(JOB_START_TIME,'yyyy-mm-dd hh24:mi:ss'), 'yyyy-mm-dd hh24:mi:ss')
                  ) * 86400       || ' Secs' );

    commit;

  END;
END;
