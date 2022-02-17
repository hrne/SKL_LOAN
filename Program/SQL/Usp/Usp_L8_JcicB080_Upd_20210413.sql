-- 程式功能：維護 JcicB080 每月聯徵授信額度資料檔
-- 執行時機：每月底日終批次(換日前)
-- 執行方式：EXEC "Usp_L8_JcicB080_Upd"(20200430,'System');
--

DROP TABLE "Work_B080" purge;
CREATE GLOBAL TEMPORARY TABLE "Work_B080"
    (  "DataYM"      decimal(6, 0)   default 0 not null
     , "CustNo"      decimal(7, 0)   default 0 not null
     , "FacmNo"      decimal(3, 0)   default 0 not null
     , "B201FacmNo"  varchar2(50)
    )
    ON COMMIT DELETE ROWS;


CREATE OR REPLACE PROCEDURE "Usp_L8_JcicB080_Upd"
(
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


    -- 寫入資料 Work_B080    -- 撈應申報之額度（B201 有上階共用額度）
    INSERT INTO "Work_B080"
    SELECT YYYYMM                                AS "DataYM"
         , SUBSTR(M."FacmNo", 1, 7)              AS "CustNo"            -- 戶號
         , SUBSTR(M."FacmNo", 8, 3)              AS "FacmNo"            -- 額度編號
         , M."FacmNo"                            AS "B201FacmNo"        -- B201 上階額度
    FROM   "JcicB201" M
    WHERE  M."DataYM" =  YYYYMM
      AND  M."FacmNo" <> LPAD('9', 50, '9')
    GROUP BY M."FacmNo"
      ;


    -- 寫入資料 Work_B080    -- 撈預約（未申報 B201 ）
    INSERT INTO "Work_B080"
    SELECT YYYYMM                                AS "DataYM"
         , M."CustNo"                            AS "CustNo"            -- 戶號
         , M."FacmNo"                            AS "FacmNo"            -- 額度編號
         , LPAD(M."CustNo", 7, '0') || LPAD(M."FacmNo", 3, '0')
                                                 AS "B201FacmNo"        -- B201 上階額度
    FROM   "LoanBorMain" M
      LEFT JOIN "Work_B080" WK ON WK."DataYM" =  YYYYMM
                              AND M."CustNo"  =  WK."CustNo"
                              AND M."FacmNo"  =  WK."FacmNo"
    WHERE  M."Status" IN (99)
      AND  WK."CustNo" IS NULL
    GROUP BY M."CustNo", M."FacmNo"
      ;


    -- 刪除舊資料
    DBMS_OUTPUT.PUT_LINE('DELETE JcicB080');

    DELETE FROM "JcicB080"
    WHERE "DataYM" = YYYYMM
      ;


    -- 寫入資料
    DBMS_OUTPUT.PUT_LINE('INSERT JcicB080');

    INSERT INTO "JcicB080"
    SELECT
           YYYYMM                                AS "DataYM"            -- 資料年月
         , '80'                                  AS "DataType"          -- 資料別
         , '458'                                 AS "BankItem"          -- 總行代號
         , '0001'                                AS "BranchItem"        -- 分行代號
         , 'A'                                   AS "TranCode"          -- 交易代碼 A:新增 C:異動 D:刪除
         , ' '                                   AS "Filler4"           -- 空白
         , CASE WHEN NVL(WK."CustNo",0) = 398695 THEN 'E100696154'  -- 特例報送 -- (ref:LN15E1 (#M3601 15 10))
                ELSE NVL(M."CustId", ' ')
           END                                   AS "CustId"            -- 授信戶IDN/BAN
         , WK."B201FacmNo"                       AS "FacmNo"            -- 本階共用額度控制編碼
         , M."CurrencyCode"                      AS "CurrencyCode"      -- 授信幣別
         , CASE WHEN NVL( M."LineAmt",0) <= 1000 THEN 1
                ELSE TRUNC( NVL( M."LineAmt",0) / 1000, 0)
           END                                   AS "DrawdownAmt"       -- 本階訂約金額(台幣)  -- (ref:LN15E1 (#M3601 80 8))
         , 0                                     AS "DrawdownAmtFx"     -- 本階訂約金額(外幣)
         , CASE
             WHEN FLOOR( NVL(M."ApproveDate", 0) / 100 ) < 191100 THEN FLOOR( NVL(M."ApproveDate", 0) / 100 )
             ELSE FLOOR( NVL(M."ApproveDate", 19110000) / 100 ) - 191100
           END                                   AS "DrawdownDate"      -- 本階額度開始年月
         , CASE
             WHEN FLOOR( NVL(M."MaturityDate", 0) / 100 ) < 191100 THEN FLOOR( NVL(M."MaturityDate", 0) / 100 )
             ELSE FLOOR( NVL(M."MaturityDate", 19110000) / 100 ) - 191100
           END                                   AS "MaturityDate"      -- 本階額度約定截止年月
         , CASE
             WHEN M."RecycleCode" IN ('1') THEN 'Y'
             WHEN M."RecycleCode" IN ('0') THEN 'N'
             ELSE ' '
           END                                   AS "RecycleCode"       -- 循環信用註記 'Y':是，'N':否
         , CASE
             WHEN M."IrrevocableFlag" IN ('0') THEN 'Y'
             WHEN M."IrrevocableFlag" IN ('1') THEN 'N'
             ELSE ' '
           END                                   AS "IrrevocableFlag"   -- 額度可否撤銷 'Y':可撤銷 'N':不可撤銷
         , LPAD('9', 50, '9')                    AS "UpFacmNo"          -- 上階共用額度控制編碼
         , CASE
         --  WHEN M."Status"      IN (2, 7)         THEN 'A' -- 催收款項   -- (ref:LN15E1 (#M3601 160 2))
         --  WHEN M."Status"      IN (6)            THEN 'B' -- 呆帳
             WHEN M."FacAcctCode" IN ('310')        THEN 'E' -- 其他短期放款
             WHEN M."FacAcctCode" IN ('320')        THEN 'H' -- 中期放款
             WHEN M."FacAcctCode" IN ('330', '340') THEN 'I' -- 長期放款
             ELSE  'I'                                       -- 長期放款
           END                                   AS "AcctCode"          -- 科目別
         , 'S'                                   AS "SubAcctCode"       -- 科目別註記 S:有十足擔保 X:無前述情形者
                                                                           -- 一律為S (ref:LN15E1 (#M3601 160 2))
         , NVL(M."ClTypeCode",' ')               AS "ClTypeCode"        -- 擔保品類別(JCIC)
         , ' '                                   AS "Filler18"          -- 空白
         , YYYYMM - 191100                       AS "JcicDataYM"        -- 資料所屬年月
         , JOB_START_TIME                        AS "CreateDate"        -- 建檔日期時間
         , EmpNo                                 AS "CreateEmpNo"       -- 建檔人員
         , JOB_START_TIME                        AS "LastUpdate"        -- 最後更新日期時間
         , EmpNo                                 AS "LastUpdateEmpNo"   -- 最後更新人員
    FROM   "Work_B080" WK
        LEFT JOIN "Ifrs9FacData" M  ON M."DataYM"    = WK."DataYM"
                                   AND M."CustNo"    = WK."CustNo"
                                   AND M."FacmNo"    = WK."FacmNo"
      ;

    INS_CNT := INS_CNT + sql%rowcount;
    DBMS_OUTPUT.PUT_LINE('INSERT JcicB080 END: INS_CNT=' || INS_CNT);


--   更新 MaturityDate 本階額度約定截止年月

    DBMS_OUTPUT.PUT_LINE('UPDATE MaturityDate ');
    UPD_CNT := 0;

    MERGE INTO "JcicB080" M
    USING ( SELECT F."CustNo"  AS "CustNo"
                 , F."FacmNo"  AS "FacmNo"
                 , L."BormNo"  AS "BormNo"
                 , NVL(L."MaturityDate",0)  AS "MaturityDate"
            FROM "JcicB080" M
              LEFT JOIN "FacMain" F       ON SUBSTR(M."FacmNo",1,7) = F."CustNo"
                                         AND SUBSTR(M."FacmNo",8,3) = F."FacmNo"
              LEFT JOIN "LoanBorMain" L   ON L."CustNo"  = F."CustNo"
                                         AND L."FacmNo"  = F."FacmNo"
                                         AND L."BormNo"  = F."LastBormNo"
            WHERE M."DataYM"       = YYYYMM
              AND M."MaturityDate" = 0
          ) L
     on (     M."DataYM"   =   YYYYMM
          AND SUBSTR(M."FacmNo",1,7) = L."CustNo"
          AND SUBSTR(M."FacmNo",8,3) = L."FacmNo"
        )
     WHEN MATCHED THEN UPDATE
       SET M."MaturityDate" = CASE WHEN L."MaturityDate" = 0 THEN 0
                                   ELSE FLOOR( L."MaturityDate" / 100 ) - 191100
                              END
       ;

    UPD_CNT := UPD_CNT + sql%rowcount;
    DBMS_OUTPUT.PUT_LINE('UPDATE MaturityDate END');


    -- 記錄程式結束時間
    JOB_END_TIME := SYSTIMESTAMP;

    commit;

-- 例外處理
--    Exception
--    WHEN OTHERS THEN
--   "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_L8_JcicB080_Upd',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
  END;
END;
