CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_L8_JcicB093_Upd"
(
-- 程式功能：維護 JcicB093 每月聯徵動產及貴重物品擔保品明細檔
-- 執行時機：每月底日終批次(換日前)
-- 執行方式：EXEC "Usp_L8_JcicB093_Upd"(20200430,'999999');
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
    DBMS_OUTPUT.PUT_LINE('DELETE JcicB093');

    DELETE FROM "JcicB093"
    WHERE "DataYM" = YYYYMM
      ;


    -- 寫入資料
    DBMS_OUTPUT.PUT_LINE('INSERT JcicB093');

    INSERT INTO "JcicB093"
    SELECT
           YYYYMM                                AS "DataYM"            -- 資料年月
         , '93'                                  AS "DataType"          -- 資料別
         , '458'                                 AS "BankItem"          -- 總行代號
         , '0001'                                AS "BranchItem"        -- 分行代號
         , ' '                                   AS "Filler4"           -- 空白
         , M."ClActNo"                           AS "ClActNo"           -- 擔保品控制編碼
         , NVL("CdCl"."ClTypeJCIC",' ')          AS "ClTypeJCIC"        -- 擔保品類別
         , NVL(CM."CustId",' ')                  AS "OwnerId"           -- 擔保品所有權人或代表人IDN/BAN
         , ROUND( NVL("ClMain"."EvaAmt",0) / 1000, 0)
                                                 AS "EvaAmt"            -- 鑑估值
         , CASE
             WHEN FLOOR(NVL("ClMain"."EvaDate",0) / 100) < 191100       
                  AND FLOOR(NVL("ClMain"."EvaDate",0) / 100) > (YYYYMM - 191100)  THEN YYYYMM - 191100 -- 原日期為民國年月且大於申報年月則設為申報年月
             WHEN FLOOR(NVL("ClMain"."EvaDate",0) / 100) < 191100 THEN FLOOR(NVL("ClMain"."EvaDate",0) / 100)
             WHEN FLOOR(NVL("ClMain"."EvaDate",0) / 100) > YYYYMM  THEN YYYYMM - 191100  -- 原日期大於申報年月則設為申報年月
             ELSE FLOOR(NVL("ClMain"."EvaDate",0) / 100) - 191100
           END                                   AS "EvaDate"           -- 鑑估日期
         , ROUND( NVL("ClMain"."EvaAmt",0) * NVL(C."LoanToValue",0) / 100 / 1000, 0)
                                                 AS "LoanLimitAmt"      -- 可放款值
         , FLOOR(NVL("FacMain"."SettingDate",0) / 100) - 191100  
                                                 AS "SettingDate"       -- 設定日期
         , CASE
             WHEN FLOOR(NVL("FacMain"."SettingDate",0) / 100) = YYYYMM THEN ROUND(NVL(C."SettingAmt",0) / 1000, 0)
             ELSE 0
           END                                   AS "MonthSettingAmt"   -- 本行本月設定金額
         , 1                                     AS "SettingSeq"        -- 本月設定抵押順位
         , ROUND(NVL(C."SettingAmt",0) / 1000,0) AS "SettingAmt"        -- 本行累計已設定總金額
--         , ROUND(NVL(C."SettingAmt",0) / 1000,0) AS "PreSettingAmt"     -- 其他債權人前已設定金額
         , 0                                     AS "PreSettingAmt"     -- 其他債權人前已設定金額
         , ROUND(NVL("ClMain"."DispPrice",0) / 1000, 0)
                                                 AS "DispPrice"         -- 處分價格
         , CASE
             WHEN NVL(C."MortgageIssueEndDate",0) = 0 THEN 99999
             WHEN FLOOR(NVL(C."MortgageIssueEndDate",0) / 100) < 191100 THEN FLOOR(NVL(C."MortgageIssueEndDate",0) / 100)
             ELSE FLOOR(NVL(C."MortgageIssueEndDate",0) / 100) - 191100
           END                                   AS "IssueEndDate"      -- 權利到期年月
         , CASE
             WHEN InsuOrg."ClNo"     IS NOT NULL THEN 'Y'
             WHEN InsuRenew."ClNo"   IS NOT NULL THEN 'Y'
             ELSE 'N'
           END                                   AS "InsuFg"            -- 是否有保險
         , ' '                                   AS "Filler19"          -- 空白
         , YYYYMM - 191100                       AS "JcicDataYM"        -- 資料所屬年月
         , JOB_START_TIME                        AS "CreateDate"        -- 建檔日期時間
         , EmpNo                                 AS "CreateEmpNo"       -- 建檔人員
         , JOB_START_TIME                        AS "LastUpdate"        -- 最後更新日期時間
         , EmpNo                                 AS "LastUpdateEmpNo"   -- 最後更新人員
    FROM   "JcicB090" M
        LEFT JOIN "CdCl"             ON "CdCl"."ClCode1"  = to_number(SUBSTR(M."ClActNo",1,1))
                                    AND "CdCl"."ClCode2"  = to_number(SUBSTR(M."ClActNo",2,2))
        LEFT JOIN "ClMain"           ON "ClMain"."ClCode1"  = to_number(SUBSTR(M."ClActNo",1,1))
                                    AND "ClMain"."ClCode2"  = to_number(SUBSTR(M."ClActNo",2,2))
                                    AND "ClMain"."ClNo"     = to_number(SUBSTR(M."ClActNo",4,7))
        LEFT JOIN "ClMovables" C     ON C."ClCode1"  = to_number(SUBSTR(M."ClActNo",1,1))
                                    AND C."ClCode2"  = to_number(SUBSTR(M."ClActNo",2,2))
                                    AND C."ClNo"     = to_number(SUBSTR(M."ClActNo",4,7))                                    
        LEFT JOIN "CustMain" CM  ON  CM."CustUKey"   = C."OwnerCustUKey"
        LEFT JOIN ( SELECT  "ClCode1", "ClCode2", "ClNo", "InsuEndDate",
                            ROW_NUMBER() OVER (PARTITION BY "ClCode1", "ClCode2", "ClNo"
                                               ORDER BY "ClCode1", "ClCode2", "ClNo", "InsuEndDate" DESC) AS "RowNum"
                    FROM  "InsuOrignal"
                    WHERE FLOOR(NVL("InsuOrignal"."InsuEndDate",0) / 100) >= YYYYMM
                    ORDER BY "ClCode1", "ClCode2", "ClNo", "RowNum"
                  ) InsuOrg          ON InsuOrg."ClCode1"  = to_number(SUBSTR(M."ClActNo",1,1))
                                    AND InsuOrg."ClCode2"  = to_number(SUBSTR(M."ClActNo",2,2))
                                    AND InsuOrg."ClNo"     = to_number(SUBSTR(M."ClActNo",4,7))
                                    AND InsuOrg."RowNum"   = 1            -- 取第一筆
        LEFT JOIN ( SELECT  "ClCode1", "ClCode2", "ClNo", "InsuEndDate",
                            ROW_NUMBER() OVER (PARTITION BY "ClCode1", "ClCode2", "ClNo"
                                               ORDER BY "ClCode1", "ClCode2", "ClNo", "InsuEndDate" DESC) AS "RowNum"
                    FROM  "InsuRenew"
                    WHERE FLOOR(NVL("InsuRenew"."InsuEndDate",0) / 100) >= YYYYMM
                      AND NVL("InsuRenew"."AcDate",0) > 0
                    ORDER BY "ClCode1", "ClCode2", "ClNo", "RowNum"
                  ) InsuRenew        ON InsuRenew."ClCode1"  = to_number(SUBSTR(M."ClActNo",1,1))
                                    AND InsuRenew."ClCode2"  = to_number(SUBSTR(M."ClActNo",2,2))
                                    AND InsuRenew."ClNo"     = to_number(SUBSTR(M."ClActNo",4,7))
                                    AND InsuRenew."RowNum"   = 1          -- 取第一筆
        LEFT JOIN "FacMain"  ON "FacMain"."CustNo" = to_number(SUBSTR(M."FacmNo",1,7))  
                            AND "FacMain"."FacmNo" = to_number(SUBSTR(M."FacmNo",8,3))

    WHERE  M."DataYM"   =   YYYYMM
      AND  SUBSTR(NVL("CdCl"."ClTypeJCIC",' '),1,1) IN ('3')     -- 動產 3x
--    AND  to_number(SUBSTR(M."ClActNo",1,1)) IN (9)
--    AND  to_number(SUBSTR(M."ClActNo",2,2)) IN (01, 02)    -- 9-01: 車輛 9-02: 機器設備
      ;

    INS_CNT := INS_CNT + sql%rowcount;

    DBMS_OUTPUT.PUT_LINE('INSERT JcicB093 END: INS_CNT=' || INS_CNT);


    -- 記錄程式結束時間
    JOB_END_TIME := SYSTIMESTAMP;
    DBMS_OUTPUT.PUT_LINE('Spend Time: ' ||
        to_number(  to_date(to_char(JOB_END_TIME,'  yyyy-mm-dd hh24:mi:ss'), 'yyyy-mm-dd hh24:mi:ss')
                  - to_date(to_char(JOB_START_TIME,'yyyy-mm-dd hh24:mi:ss'), 'yyyy-mm-dd hh24:mi:ss')
                  ) * 86400       || ' Secs' );

    commit;

  END;
END;
