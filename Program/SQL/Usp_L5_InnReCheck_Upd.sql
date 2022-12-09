CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_L5_InnReCheck_Upd" (
   -- 參數
   TBSDYF IN INT , -- 系統營業日(西元)
   EMPNO  IN VARCHAR2 -- 經辦
) AS
BEGIN
    DECLARE
        INS_CNT         INT;        -- 新增筆數 
        UPD_CNT         INT;        -- 更新筆數 
        JOB_START_TIME  TIMESTAMP;  -- 記錄程式起始時間
        JOB_END_TIME    TIMESTAMP;  -- 記錄程式結束時間    
        TYYYYMM         INT;        -- 本年月 
        LYYYYMM         INT;        -- 上年月 
        TMM             INT;        -- 本月      
    BEGIN
        INS_CNT := 0;
        UPD_CNT := 0;
        TYYYYMM := TBSDYF / 100;
        TMM := TYYYYMM - ( TYYYYMM / 100 ) * 100;
        IF TMM = 1
        THEN LYYYYMM := ( TYYYYMM / 100 ) * 100 + 12;
        ELSE LYYYYMM := TYYYYMM - 1;
        END IF;

    -- 記錄程式起始時間
        JOB_START_TIME := SYSTIMESTAMP;

    --
    -- 刪除舊資料
        DELETE "InnReCheck"
        WHERE "YearMonth" = TYYYYMM; 

    -- 寫入資料
        INSERT INTO "InnReCheck"
            SELECT TYYYYMM                AS "YearMonth"           --資料年月  
                 , CASE
                WHEN C."EntCode" <> '1'
                     AND M."LoanBal" >= 30000000 -- 01 個金3000萬以上
                THEN 1
                WHEN C."EntCode" = '1'
                     AND M."LoanBal" >= 30000000 -- 02 企金3000萬以上
                THEN 2
                WHEN C."EntCode" <> '1'
                     AND M."LoanBal" >= 20000000  -- 03 個金2000萬以上小於3000萬
                THEN 3
                WHEN C."EntCode" <> '1'
                     AND M."LoanBal" >= 1000000   -- 04 個金100萬以上小於2000萬
                THEN 4
                WHEN C."EntCode" = '1'
                     AND M."LoanBal" > 0   -- 05 企金未達3000萬
                THEN 5
                ELSE 0
                     END                    AS "ConditionCode"      --條件代碼  
                 , L."CustNo"             AS "CustNo"              --借款人戶號  
                 , L."FacmNo"             AS "FacmNo"              --額度號碼  
                 , ' '                    AS "ReCheckCode"         --覆審記號     1.指定覆審、2.免覆審、空白
                 , ' '                    AS "FollowMark"          --是否追蹤     自行註記
                 , 0                      AS "ReChkYearMonth"      --覆審年月   
                 , L2."DrawdownDate"      AS "DrawdownDate"        --撥款日期   
                 , SUM(L."LoanBal")       AS "LoanBal"             --貸放餘額  
                 , 0                      AS "Evaluation"          --評等  
                 , C."CustTypeCode"       AS "CustTypeCode"        --客戶別  
                 , ' '                    AS "UsageCode"           --用途別  
                 , ' '                    AS "CityCode"            --地區別  
                 , ' '                    AS "ReChkUnit"           --應覆審單位    同區域中心
                 , NULL                   AS "SpecifyFg"           --指定覆審記號
                 , ' '                    AS "Remark"              --備註  
                 , 0                      AS "TraceMonth"          --追蹤年月
                 , JOB_START_TIME         AS "CreateDate"          -- 建檔日期時間  
                 , EMPNO                  AS "CreateEmpNo"         -- 建檔人員 
                 , JOB_START_TIME         AS "LastUpdate"          -- 最後更新日期時間  
                 , EMPNO                  AS "LastUpdateEmpNo"     -- 最後更新人員 
            FROM (
                SELECT "CustNo"
                     , SUM("LoanBal") AS "LoanBal"
                FROM "LoanBorMain"
                WHERE "Status" IN ( 0 )   --0: 正常戶   
                GROUP BY "CustNo"
            )              M
            LEFT JOIN "LoanBorMain"  L ON L."CustNo" = M."CustNo"
                                         AND L."LoanBal" > 0
            LEFT JOIN (
                SELECT "CustNo"
                     , "FacmNo"
                     , MIN("DrawdownDate") AS "DrawdownDate"
                FROM "LoanBorMain"
                WHERE "LoanBal" > 0
                GROUP BY "CustNo"
                       , "FacmNo"
            )              L2 ON L2."CustNo" = L."CustNo"
                    AND L2."FacmNo" = L."FacmNo"
            LEFT JOIN "CustMain"     C ON C."CustNo" = M."CustNo"   -- 客戶資料主檔
            WHERE (
                CASE
                    WHEN L."LoanBal" = 0
                    THEN 0
                    WHEN C."EntCode" <> '1'
                         AND M."LoanBal" >= 30000000
                    THEN 1
                    WHEN C."EntCode" = '1'
                         AND M."LoanBal" >= 30000000
                    THEN 2
                    WHEN C."EntCode" <> '1'
                         AND M."LoanBal" >= 20000000
                    THEN 3
                    WHEN C."EntCode" <> '1'
                         AND M."LoanBal" >= 1000000
                    THEN 4
                    WHEN C."EntCode" = '1'
                         AND M."LoanBal" > 0
                    THEN 5
                    ELSE 0
                END
            ) > 0
            GROUP BY L."CustNo"
                   , L."FacmNo"
                   , L2."DrawdownDate"
                   , C."CustTypeCode"
                   , C."EntCode"
                   , M."LoanBal";
        INS_CNT := INS_CNT + SQL%ROWCOUNT;

--  EntCode 企金別 VARCHAR2  1   "共用代碼檔
-- 0:個金
-- 1:企金
-- 2:企金自然人"

-- 01-個金3000萬以上
-- 02-企金3000萬以上
-- 03-個金2000萬以上小於3000萬
-- 04-個金100萬以上小於2000萬
-- 05-企金未達3000萬
-- 06-土地追蹤
        INSERT INTO "InnReCheck"
            SELECT TYYYYMM             AS "YearMonth"           --資料年月  
                 , 6                   AS "ConditionCode"      --條件代碼  
                 , M."CustNo"          AS "CustNo"              --借款人戶號  
                 , M."FacmNo"          AS "FacmNo"              --額度號碼  
                 , ' '                 AS "ReCheckCode"         --覆審記號     1.指定覆審、2.免覆審、空白
                 , ' '                 AS "FollowMark"          --是否追蹤     自行註記
                 , 0                   AS "ReChkYearMonth"      --覆審年月   
                 , M."DrawdownDate"    AS "DrawdownDate"        --撥款日期   
                 , M."LoanBal"         AS "LoanBal"             --貸放餘額  
                 , 0                   AS "Evaluation"          --評等  
                 , CC."Item"           AS "CustTypeItem"        --客戶別  
                 , CASE
                         WHEN M."UsageCode" = '99'
                         THEN u'\8cfc\7f6e\4e0d\52d5\7522' -- 「購置不動產」
                         ELSE CU."Item"
                     END                 AS "UsageItem"           --用途別  
                 , CI."CityItem"       AS "CityItem"            --地區別  
                 , ' '                 AS "ReChkUnit"           --應覆審單位    同區域中心
                 , ''                  AS "SpecifyFg"           --指定覆審記號
                 , ' '                 AS "Remark"              --備註  
                 , 0                   AS "TraceMonth"          --追蹤年月
                 , JOB_START_TIME      AS "CreateDate"          -- 建檔日期時間  
                 , EMPNO               AS "CreateEmpNo"         -- 建檔人員 
                 , JOB_START_TIME      AS "LastUpdate"          -- 最後更新日期時間  
                 , EMPNO               AS "LastUpdateEmpNo"     -- 最後更新人員 
            FROM (
                SELECT "CustNo"
                     , "FacmNo"
                     , MIN("DrawdownDate")     AS "DrawdownDate"
                     , SUM(
                    CASE
                        WHEN "Status" = 0
                        THEN "LoanBal"
                        ELSE 0
                    END
                )                       AS "LoanBal"             --貸放餘額  
                     , MAX(
                    CASE
                        WHEN "UsageCode" = '02'
                        THEN '99'         --2: 購置不動產  優先
                        ELSE "UsageCode"
                    END
                )                       AS "UsageCode"           --用途別  
                FROM "LoanBorMain"
--                WHERE "Status" IN (
--                    0
--                  , 1
--                  , 2
--                )   -- 0: 正常戶  1:展期 3: 結案戶  
                GROUP BY "CustNo"
                       , "FacmNo"
            )           M
            LEFT JOIN "ClFac"     CF ON CF."CustNo" = M."CustNo" --擔保品與額度關聯檔 
                                    AND CF."FacmNo" = M."FacmNo"
                                    AND CF."MainFlag" = 'Y'     --主要擔保品記號
            LEFT JOIN "ClLand"    CL ON CL."ClCode1" = CF."ClCode1" -- 擔保品土地檔
                                     AND CL."ClCode2" = CF."ClCode2"
                                     AND CL."ClNo" = CF."ClNo"
            LEFT JOIN "CdCity"    CI ON CI."CityCode" = CL."CityCode"
            LEFT JOIN "CdCode"    CU ON CU."DefCode" = 'UsageCode'
                                     AND CU."Code" = M."UsageCode"
            LEFT JOIN "CustMain"  C ON C."CustNo" = M."CustNo"   -- 客戶資料主檔
            LEFT JOIN "CdCode"    CC ON CC."DefCode" = 'CustTypeCode'
                                     AND CC."Code" = C."CustTypeCode"
            WHERE CF."ClCode1" = 2            -- 土地
                  AND M."LoanBal" > 0;
        INS_CNT := INS_CNT + SQL%ROWCOUNT;
  
-- 1  房地
-- 2  土地
-- 3  股票
-- 4  其他有價證券
-- 5  銀行保證
-- 9  動產
--  
-- 1: 週轉金
-- 2: 購置不動產
-- 3: 營業用資產
-- 4: 固定資產
-- 5: 企業投資
-- 6: 購置動產
-- 9: 其他                 
        MERGE INTO "InnReCheck" T2
        USING (
                  SELECT "CustNo"
                       , "FacmNo"
                       , "ConditionCode"
                       , "ReCheckCode"         --覆審記號     1.指定覆審、2.免覆審、空白
                       , "FollowMark"          --是否追蹤     自行註記
                       , "ReChkYearMonth"      --覆審年月   
                       , "ReChkUnit"           --應覆審單位   同區域中心
                       , "Remark"              --備註  
                  FROM "InnReCheck"
                  WHERE "YearMonth" = LYYYYMM
              )
        T1 ON ( T2."CustNo" = T1."CustNo"
                AND T2."FacmNo" = T1."FacmNo"
                AND T2."ConditionCode" = T1."ConditionCode"
                AND T2."YearMonth" = TYYYYMM )
        WHEN MATCHED
        THEN UPDATE
        SET T2."ReCheckCode" = T1."ReCheckCode"
          , T2."FollowMark" = T1."FollowMark"
          , T2."ReChkYearMonth" = T1."ReChkYearMonth"
          , T2."ReChkUnit" = T1."ReChkUnit"
          , T2."Remark" = T1."Remark";
        UPD_CNT := UPD_CNT + SQL%ROWCOUNT;

    -- 記錄程式結束時間
        JOB_END_TIME := SYSTIMESTAMP;
        COMMIT;

    -- 例外處理
    -- Exception
    -- WHEN OTHERS THEN
    -- "Usp_L9_UspErrorLog_Ins"(
    --     'Usp_L5_InnReCheck_Upd' -- UspName 預存程序名稱
    --   , SQLCODE -- Sql Error Code (固定值)
    --   , SQLERRM -- Sql Error Message (固定值)
    --   , dbms_utility.format_error_backtrace -- Sql Error Trace (固定值)
    --   , EMPNO -- 發動預存程序的員工編號
    -- );
    END;
END;