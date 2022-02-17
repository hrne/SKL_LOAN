-- 程式功能：維護 JcicB080 每月聯徵授信額度資料檔
-- 執行時機：每月底日終批次(換日前)
-- 執行方式：EXEC "Usp_L8_JcicB080_Upd"(20200430,'999999');
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
         , SUBSTR(M."FacmNo", 1, 7)              AS "CustNo"            -- 戶號      來源???
         , SUBSTR(M."FacmNo", 8, 3)              AS "FacmNo"            -- 額度編號  來源???
         , M."FacmNo"                            AS "B201FacmNo"        -- B201 上階額度
    FROM   "JcicB201" M
    WHERE  M."DataYM" =  YYYYMM
      AND  M."FacmNo" <> LPAD('9', 50, '9')   --???條件
    GROUP BY M."FacmNo"
      ;

    -- 寫入資料 Work_B080    -- 撈應申報之額度（B201 有上階共用額度）
    INSERT INTO "Work_B080"
    SELECT YYYYMM                                AS "DataYM"
         , SUBSTR(M."AcctNo", 1, 7)              AS "CustNo"            -- 戶號      來源???
         , SUBSTR(M."AcctNo", 8, 3)              AS "FacmNo"            -- 額度編號  來源???
         , M."AcctNo"                            AS "B201FacmNo"        -- B201 上階額度
    FROM   "JcicB201" M
    WHERE  M."DataYM" =  YYYYMM
    GROUP BY M."AcctNo"
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
         , M."CustId"                            AS "CustId"            -- 授信戶IDN/BAN
         , WK."B201FacmNo"                       AS "FacmNo"            -- 本階共用額度控制編碼
         , M."CurrencyCode"                      AS "CurrencyCode"      -- 授信幣別
         , ROUND(NVL(M."LineAmt",0) / 1000 ,0)   AS "DrawdownAmt"       -- 本階訂約金額(台幣)
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
             WHEN M."IrrevocableFlag" IN ('1') THEN 'Y'
             WHEN M."IrrevocableFlag" IN ('0') THEN 'N'
             ELSE ' '
           END                                   AS "RecycleCode"       -- 循環信用註記 'Y':是，'N':否
         , CASE
             WHEN M."IrrevocableFlag" IN ('0') THEN 'Y'
             WHEN M."IrrevocableFlag" IN ('1') THEN 'N'
             ELSE ' '
           END                                   AS "IrrevocableFlag"   -- 額度可否撤銷 'Y':可撤銷 'N':不可撤銷
         , LPAD('9', 50, '9')                    AS "UpFacmNo"          -- 上階共用額度控制編碼
         , CASE
             WHEN M."Status"   IN (2, 7)         THEN 'A' -- 催收款項
             WHEN M."Status"   IN (6)            THEN 'B' -- 呆帳
             WHEN M."AcctCode" IN ('310')        THEN 'E' -- 其他短期放款
             WHEN M."AcctCode" IN ('320')        THEN 'H' -- 中期放款
             WHEN M."AcctCode" IN ('330', '340') THEN 'I' -- 長期放款
             ELSE  'I'                                    -- 長期放款
           END                                   AS "AcctCode"          -- 科目別
         , 'S'                                   AS "SubAcctCode"       -- 科目別註記 S:有十足擔保 X:無前述情形者  --???判斷鑑估值
         , CASE
             WHEN M."ClTypeCode" IS NULL THEN ' '
             WHEN LENGTH(M."ClTypeCode") < 3 THEN M."ClTypeCode"
             ELSE SUBSTR(M."ClTypeCode",1,2)
           END                                   AS "ClTypeCode"        -- 擔保品類別  (取前兩碼對照 JCIC 擔保品類別)
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


    -- 記錄程式結束時間
    JOB_END_TIME := SYSTIMESTAMP;

    commit;

    -- 例外處理
--    Exception
--    WHEN OTHERS THEN
--   "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_L8_JcicB080_Upd',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
  END;
END;
