-- 程式功能：維護 Ias34Ep 每月IAS34資料欄位清單E檔
-- 執行時機：每月底日終批次(換日前)
-- 執行方式：EXEC "Usp_L7_Ias34Ep_Upd"(20201231,'System',0);
--


CREATE OR REPLACE PROCEDURE "Usp_L7_Ias34Ep_Upd"
(
    -- 參數
    TBSDYF         IN  INT,        -- 系統營業日(西元)
    EmpNo          IN  VARCHAR2,   -- 經辦
    NewAcFg        IN  INT         -- 0=使用舊會計科目(8碼) 1=使用新會計科目(11碼)
)
AUTHID CURRENT_USER
AS
BEGIN
	"Usp_L7_Ias34Ep_Upd_Prear"(); -- Work_EP 資料清檔

  DECLARE
    INS_CNT        INT;         -- 新增筆數
    UPD_CNT        INT;         -- 更新筆數
    JOB_START_TIME TIMESTAMP;   -- 記錄程式起始時間
    JOB_END_TIME   TIMESTAMP;   -- 記錄程式結束時間
    YYYYMM         INT;         -- 本月年月
    LYYYYMM        INT;         -- 上月年月
    MM             INT;         -- 本月月份
    YYYY           INT;         -- 本月年度
    OccursNum      NUMBER;
    YearFirstDay   DECIMAL;     -- 本年度第一天
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

    --　本年度第一天
    YearFirstDay := to_number(substr(to_char(YYYYMM),0,4) || '0101') ;
    DBMS_OUTPUT.PUT_LINE('YearFirstDay = ' || YearFirstDay);


    INSERT INTO "Work_EP"
    SELECT "AcReceivable"."CustNo"              AS  "CustNo"
         , "AcReceivable"."FacmNo"              AS  "FacmNo"
         , to_number("AcReceivable"."RvNo")     AS  "BormNo"
         , CASE WHEN NewAcFg = 0 THEN RPAD(NVL(CD."AcNoCodeOld",' '),8,' ') -- 舊
                ELSE                  RPAD(NVL(CD."AcNoCode",' '),11,' ')   -- 新
           END                                  AS  "AcCode"    -- 會計科目
         , 1                                    AS  "Status"    -- 帳上客戶
         , 0                                    AS  "BadDebtDate"
         , "AcReceivable"."AcctCode"            AS  "AcctCode"
    FROM  "AcReceivable"
      LEFT JOIN "CdAcCode" CD  ON CD."AcctCode" = "AcReceivable"."AcctCode"
    WHERE  "AcReceivable"."AcctCode" IN ('310', '320', '330', '340', '990')
      AND  "AcReceivable"."ClsFlag"  = 0     --未銷
      ;

    --合併當年度轉呆帳
    INSERT INTO "Work_EP"
    SELECT "LoanOverdue"."CustNo"                   AS  "CustNo"
         , "LoanOverdue"."FacmNo"                   AS  "FacmNo"
         , "LoanOverdue"."BormNo"                   AS  "BormNo"
         , CASE WHEN NewAcFg = 0 THEN RPAD(NVL(CD."AcNoCodeOld",' '),8,' ') -- 舊
                ELSE                  RPAD(NVL(CD."AcNoCode",' '),11,' ')   -- 新
           END                                      AS  "AcCode"    -- 會計科目
         , 2                                        AS  "Status"    -- 轉呆客戶
         , NVL(MAX("LoanOverdue"."BadDebtDate"),0)  AS  "BadDebtDate"
         , NVL("LoanOverdue"."AcctCode", ' ')       AS  "AcctCode"
    FROM  "LoanOverdue"
      LEFT JOIN "CdAcCode" CD  ON CD."AcctCode" = "LoanOverdue"."AcctCode"
    WHERE "LoanOverdue"."Status" IN (2, 3)
      AND "LoanOverdue"."BadDebtDate" BETWEEN YearFirstDay AND TMNDYF
    GROUP BY "LoanOverdue"."CustNo", "LoanOverdue"."FacmNo",
             "LoanOverdue"."BormNo", CD."AcNoCode" , CD."AcNoCodeOld", "LoanOverdue"."AcctCode"
      ;


    -- 刪除舊資料
    DBMS_OUTPUT.PUT_LINE('DELETE Ias34Ep');

    DELETE FROM "Ias34Ep"
    WHERE "DataYM" = YYYYMM
      ;

    -- 寫入資料
    DBMS_OUTPUT.PUT_LINE('INSERT Ias34Ep');
    INS_CNT := 0;

    INSERT INTO "Ias34Ep"
    SELECT
           YYYYMM                               AS "DataYM"             -- 年月份
         , W."CustNo"                           AS "CustNo"             -- 戶號
         , NVL("CustMain"."CustId", ' ')        AS "CustId"             -- 借款人ID / 統編
         , W."FacmNo"                           AS "FacmNo"             -- 額度編號(核准號碼)
         , W."BormNo"                           AS "BormNo"             -- 撥款序號
         , W."AcCode"                           AS "AcCode"             -- 會計科目
         , W."Status"                           AS "Status"             -- 狀態  --辨識是否為帳上客戶或為轉呆客戶 1=帳上客戶,2=轉呆客戶
         , CASE WHEN TRIM(NVL("CustMain"."IndustryCode", ' ')) = '' THEN ' '
                ELSE SUBSTR('000000' || TRIM("CustMain"."IndustryCode"), -6)
           END                                  AS "IndustryCode"       -- 授信行業別
         , NVL("CdCl"."ClTypeJCIC", ' ')        AS "ClTypeJCIC"         -- 擔保品類別
         , NVL("CdArea"."Zip3", ' ')            AS "Zip3"               -- 擔保品地區別(郵遞區號)
         , NVL("FacMain"."ProdNo", ' ')         AS "ProdNo"             -- 商品利率代碼
         , CASE
             WHEN "CustMain"."EntCode" = 0 THEN 2
             WHEN "CustMain"."EntCode" = 1 THEN 1
             WHEN "CustMain"."EntCode" = 2 THEN 2    --> 2:企金自然人
             ELSE 2
           END                                  AS "CustKind"           -- 企業戶/個人戶  -- 1=企業戶；2=個人戶
         , CASE
             WHEN W."Status" = 2          THEN 'Y'   -- 轉呆案件註記為Y
             WHEN W."AcctCode" IN ('990') THEN 'Y'   -- 轉催案件註記為Y
             ELSE 'N'                                -- 其他案件，後面再判斷
           END                                  AS "DerFg"              -- 資料時點是否符合減損客觀證據 Y=符合減損客觀證據條件
                                                                        --                              N=未符合減損客觀證據條件
         , NVL("FacProd"."Ifrs9ProdCode", ' ')  AS "Ifrs9ProdCode"      -- 產品別
         , JOB_START_TIME                       AS "CreateDate"         -- 建檔日期時間
         , EmpNo                                AS "CreateEmpNo"        -- 建檔人員
         , JOB_START_TIME                       AS "LastUpdate"         -- 最後更新日期時間
         , EmpNo                                AS "LastUpdateEmpNo"    -- 最後更新人員
    FROM "Work_EP" W
      LEFT JOIN "CustMain"    ON "CustMain"."CustNo"      = W."CustNo"
      LEFT JOIN "FacMain"  ON "FacMain"."CustNo"       = W."CustNo"
                          AND "FacMain"."FacmNo"       = W."FacmNo"
      LEFT JOIN "FacProd"  ON "FacProd"."ProdNo"       = "FacMain"."ProdNo"
      LEFT JOIN ( SELECT "ClFac"."ClCode1"   AS  "ClCode1"
                       , "ClFac"."ClCode2"   AS  "ClCode2"
                       , "ClFac"."ClNo"      AS  "ClNo"
                       , "ClFac"."ApproveNo" AS  "ApproveNo"
                       , "ClFac"."CustNo"    AS  "CustNo"
                       , "ClFac"."FacmNo"    AS  "FacmNo"
                  FROM "ClFac"
                  WHERE "ClFac"."MainFlag" IS NULL
                     OR "ClFac"."MainFlag"  = 'Y'
                ) CFl   ON CFl."ApproveNo"  = "FacMain"."ApplNo"
                       AND CFl."CustNo"     = W."CustNo"
                       AND CFl."FacmNo"     = W."FacmNo"
      LEFT JOIN "ClMain"  ON "ClMain"."ClCode1"   = CFl."ClCode1"
                         AND "ClMain"."ClCode2"   = CFl."ClCode2"
                         AND "ClMain"."ClNo"      = CFl."ClNo"
      LEFT JOIN "CdCl" ON "CdCl"."ClCode1"     = CFl."ClCode1"
                      AND "CdCl"."ClCode2"     = CFl."ClCode2"
      LEFT JOIN "CdArea"   ON "CdArea"."CityCode"   = "ClMain"."CityCode"
                          AND "CdArea"."AreaCode"   = "ClMain"."AreaCode"
      ;

    INS_CNT := INS_CNT + sql%rowcount;
    DBMS_OUTPUT.PUT_LINE('INSERT Ias34Ep END');
    DBMS_OUTPUT.PUT_LINE('INSERT Ias34Ep INS_CNT=' || INS_CNT);


-- 更新 DerFg 資料時點是否符合減損客觀證據   (ref: LNSP65A-LN6511, Ias34Ap)
    DBMS_OUTPUT.PUT_LINE('UPDATE DerFg 資料時點是否符合減損客觀證據');
    UPD_CNT := 0;

    MERGE INTO "Ias34Ep" M
    USING ( SELECT W."CustNo"
                 , W."FacmNo"
                 , W."BormNo"
                 , NVL(F."ProdNo", ' ')      AS "ProdNo"
                 , NVL(L."OvduDays", 0)      AS "OvduDays"
                 , NVL(F."AgreementFg", 'N') AS "AgreementFg"  -- 是否為協議商品 Y:是 N:否
                 , CASE WHEN LOS."CustNo" IS NULL THEN 'N'
                        ELSE 'Y'
                   END                       AS "LossFg"       -- 是否符合特殊客觀減損狀況檔 Y:是 N:否
            FROM "Work_EP" W
              LEFT JOIN "Ifrs9LoanData" L  ON L."DataYM"   =  YYYYMM
                                          AND L."CustNo"   =  W."CustNo"
                                          AND L."FacmNo"   =  W."FacmNo"
                                          AND L."BormNo"   =  W."BormNo"
              LEFT JOIN "Ifrs9FacData" F   ON F."DataYM"   =  YYYYMM
                                          AND F."CustNo"   =  W."CustNo"
                                          AND F."FacmNo"   =  W."FacmNo"
              LEFT JOIN "Ias39Loss" LOS    ON LOS."CustNo"  =  W."CustNo"
                                          AND LOS."FacmNo"  =  W."FacmNo"
                                          AND TRUNC(NVL(LOS."StartDate",0) / 100) <= YYYYMM
                                          AND TRUNC(NVL(LOS."EndDate",99991231) / 100) >= YYYYMM
          ) B
    ON (    M."DataYM"    =  YYYYMM
        AND M."CustNo"    =  B."CustNo"
        AND M."FacmNo"    =  B."FacmNo"
        AND M."BormNo"    =  B."BormNo"
       )
    WHEN MATCHED THEN UPDATE
      SET M."DerFg"   =   CASE WHEN M."DerFg"       =  'Y'   THEN 'Y'  -- 已註記為Y者,維持不變
                               WHEN B."AgreementFg" =  'Y'   THEN 'Y'
                               WHEN B."OvduDays"    >= 90    THEN 'Y'
                               WHEN B."LossFg"      =  'Y'   THEN 'Y'
                               ELSE 'N'
                          END
      ;

    UPD_CNT := UPD_CNT + sql%rowcount;
    DBMS_OUTPUT.PUT_LINE('UPDATE DerFg 資料時點是否符合減損客觀證據 END');


    -- 記錄程式結束時間
    JOB_END_TIME := SYSTIMESTAMP;
    DBMS_OUTPUT.PUT_LINE('Spend Time: ' ||
        to_number(  to_date(to_char(JOB_END_TIME,'  yyyy-mm-dd hh24:mi:ss'), 'yyyy-mm-dd hh24:mi:ss')
                  - to_date(to_char(JOB_START_TIME,'yyyy-mm-dd hh24:mi:ss'), 'yyyy-mm-dd hh24:mi:ss')
                  ) * 86400       || ' Secs' );

    commit;

  END;
END;
