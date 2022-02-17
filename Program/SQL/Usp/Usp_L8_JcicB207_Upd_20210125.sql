-- 程式功能：維護 JcicB207 聯徵授信戶基本資料檔
-- 執行時機：每月日終批次(換日前)
-- 執行方式：EXEC "Usp_L8_JcicB207_Upd"(20200420,'999999');
--

DROP TABLE "Work_B207" purge;
CREATE GLOBAL TEMPORARY TABLE "Work_B207"
    ( "CustId"      varchar2(10)
    )
    ON COMMIT DELETE ROWS;


CREATE OR REPLACE PROCEDURE "Usp_L8_JcicB207_Upd"
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


    -- 寫入資料 Work_B207    -- 撈應申報之戶號
    INSERT INTO "Work_B207"
    SELECT M."CustId"                            AS "CustId"            -- ID
    FROM   "JcicB201" M
    WHERE  M."CustId" IS NOT NULL 
    GROUP BY M."CustId"
      ;


    -- 刪除舊資料
    DBMS_OUTPUT.PUT_LINE('DELETE JcicB207');

    DELETE FROM "JcicB207"
    WHERE "DataYM" = YYYYMM
      ;

    -- 寫入資料
    DBMS_OUTPUT.PUT_LINE('INSERT JcicB207');

    INSERT INTO "JcicB207"
    SELECT
           YYYYMM                                AS "DataYM"            -- 資料年月
         , 'A'                                   AS "TranCode"          -- 交易代碼   A:新增 C:異動(全部整檔報送時可使用此代號) D:刪除
         , '458'                                 AS "BankItem"          -- 總行代號
         , ' '                                   AS "Filler3"           -- 空白
         , TBSDYF - 19110000                     AS "DataDate"          -- 資料日期 (民國)
         , C."CustId"                            AS "CustId"            -- 授信戶IDN
         , SUBSTRB(NVL(C."CustName",' '),1,20)   AS "CustName"          -- 中文姓名
         , SUBSTR(NVL(C."EName",' '),1,20)       AS "EName"             -- 英文姓名
         , CASE
             WHEN NVL(C."Birthday",0) < 19110000 THEN NVL(C."Birthday",0)
             ELSE C."Birthday" - 19110000
           END                                   AS "Birthday"          -- 出生日期 (民國)
         , SUBSTRB(NVL(
             CASE
               WHEN RegCity."CityItem" IS NOT NULL THEN RegCity."CityItem"
             END ||
             TRIM(NVL2(RegAddr."AreaItem", RegAddr."AreaItem", '')) ||
             TRIM(NVL2(C."RegRoad", C."RegRoad", ''))  ||
             TRIM(NVL2(C."RegSection", C."RegSection" || '段', ''))  ||
             TRIM(NVL2(C."RegAlley", C."RegAlley" || '巷', ''))  ||
             TRIM(NVL2(C."RegLane", C."RegLane" || '弄', ''))  ||
             TRIM(NVL2(C."RegNum", C."RegNum" || '號', ''))  ||
             TRIM(NVL2(C."RegNumDash", '之' || C."RegNumDash", ''))  ||
             ( CASE
                 WHEN C."RegNumDash" IS NOT NULL AND C."RegFloor" IS NOT NULL THEN ','
                 ELSE ''
               END) ||
             TRIM(NVL2(C."RegFloor", C."RegFloor" || '樓', ''))  ||
             TRIM(NVL2(C."RegFloorDash", '之' || C."RegFloorDash", ''))
           , ' '), 1, 66)                        AS "RegAddr"           -- 戶籍地址
         , NVL(
             CASE
               WHEN C."CurrZip3" IS NOT NULL THEN C."CurrZip3"
             END ||
             CASE
               WHEN C."CurrZip2" IS NOT NULL THEN C."CurrZip2"
             END
           , ' ')                                AS "CurrZip"           -- 聯絡地址郵遞區號
         , SUBSTRB(NVL(
             CASE
               WHEN MailCity."CityItem" IS NOT NULL THEN MailCity."CityItem"
             END ||
             TRIM(NVL2(MailAddr."AreaItem", MailAddr."AreaItem", '')) ||
             TRIM(NVL2(C."CurrRoad", C."CurrRoad", ''))  ||
             TRIM(NVL2(C."CurrSection", C."CurrSection" || '段', ''))  ||
             TRIM(NVL2(C."CurrAlley", C."CurrAlley" || '巷', ''))  ||
             TRIM(NVL2(C."CurrLane", C."CurrLane" || '弄', ''))  ||
             TRIM(NVL2(C."CurrNum", C."CurrNum" || '號', ''))  ||
             TRIM(NVL2(C."CurrNumDash", '之' || C."CurrNumDash", ''))  ||
             ( CASE
                 WHEN C."CurrNumDash" IS NOT NULL AND C."CurrFloor" IS NOT NULL THEN ','
                 ELSE ''
               END) ||
             TRIM(NVL2(C."CurrFloor", C."CurrFloor" || '樓', ''))  ||
             TRIM(NVL2(C."CurrFloorDash", '之' || C."CurrFloorDash", ''))
           , ' '), 1, 66)                        AS "CurrAddr"          -- 聯絡地址
         , CASE
             WHEN Phone2."TelNo" IS NULL THEN
               CASE
                 WHEN Phone1."TelNo" IS NULL THEN ' '
                 ELSE NVL2(Phone1."TelArea", Phone1."TelArea" || '-', '') ||
                      NVL2(Phone1."TelNo",   Phone1."TelNo",          '') ||
                      NVL2(Phone1."TelExt",  '#' || Phone1."TelExt",  '')
               END
             ELSE
               CASE
                 WHEN Phone2."TelNo" IS NULL THEN ' '
                 ELSE NVL2(Phone2."TelArea", Phone2."TelArea" || '-', '') ||
                      NVL2(Phone2."TelNo",   Phone2."TelNo",          '') ||
                      NVL2(Phone2."TelExt",  '#' || Phone2."TelExt",  '')
               END
           END                                   AS "Tel"               -- 聯絡電話 (住家優先，再公司)
         , CASE
             WHEN CPhone."Mobile" IS NULL THEN ' '
             ELSE SUBSTR(CPhone."Mobile",1,4) || '-' || SUBSTR(CPhone."Mobile",5)
           END                                   AS "Mobile"            -- 行動電話
         , ' '                                   AS "Filler14"          -- 空白
         , CASE
             WHEN C."EduCode" IS NULL  THEN ' '
             WHEN C."EduCode" IN ('7') THEN '1'
             WHEN C."EduCode" IN ('6') THEN '2'
             WHEN C."EduCode" IN ('5') THEN '3'
             WHEN C."EduCode" IN ('4') THEN '4'
             WHEN C."EduCode" IN ('3') THEN '5'
             ELSE                           '6'
           END                                   AS "EduCode"           -- 教育程度代號 1:博士 2:碩士 3:大學 4:專科 5:高中高職 6:其他
         , NVL(C."OwnedHome",' ')                AS "OwnedHome"         -- 自有住宅有無 Y:有 N:無
         , SUBSTRB(NVL(C."CurrCompName",' '),1,30)
                                                 AS "CurrCompName"      -- 任職機構名稱
         , NVL(C."CurrCompId",' ')               AS "CurrCompId"        -- 任職機構統一編號
         , ' '                                   AS "JobCode"           -- 職業類別  --???
         , NVL(C."CurrCompTel",' ')              AS "CurrCompTel"       -- 任職機構電話
         , SUBSTRB(NVL(C."JobTitle",' '),1,10)   AS "JobTitle"          -- 職位名稱
         , NVL(C."JobTenure",' ')                AS "JobTenure"         -- 服務年資
         , ROUND(NVL(C."IncomeOfYearly",0) / 1000,0)
                                                 AS "IncomeOfYearly"    -- 年收入
         , CASE
             WHEN to_number(NVL(C."IncomeDataDate",0)) < 191100 THEN to_number(NVL(C."IncomeDataDate",0))
             ELSE to_number(C."IncomeDataDate") - 191100
           END                                   AS "IncomeDataDate"    -- 年收入資料年月 (民國)
         , CASE
             WHEN SUBSTR(C."CustId",1,8) BETWEEN '00000000' AND '99999999'
              AND SUBSTR(C."CustId",9,2) BETWEEN 'AA' AND 'ZZ'  THEN DECODE(C."Sex", '1', 'M',
                                                                                     '2', 'F',
                                                                                     ' ')
             ELSE ' '
           END                                   AS "Sex"               -- 性別
         , CASE
             WHEN SUBSTR(C."CustId",1,8) BETWEEN '00000000' AND '99999999'
              AND SUBSTR(C."CustId",9,2) BETWEEN 'AA' AND 'ZZ'  THEN NVL(C."NationalityCode",' ')
             ELSE ' '
           END                                   AS "NationalityCode"   -- 國籍
         , CASE
             WHEN SUBSTR(C."CustId",1,8) BETWEEN '00000000' AND '99999999'
              AND SUBSTR(C."CustId",9,2) BETWEEN 'AA' AND 'ZZ'  THEN NVL(C."PassportNo",' ')
             ELSE ' '
           END                                   AS "PassportNo"        -- 護照號碼
         , ' '                                   AS "PreTaxNo"          -- 舊有稅籍編號
         , CASE
             WHEN lengthb(C."CustName") <= 20 THEN N' '
             ELSE C."CustName"
           END                                   AS "FullCustName"      -- 中文姓名超逾10個字之全名
         , ' '                                   AS "Filler30"          -- 空白
         , JOB_START_TIME                        AS "CreateDate"        -- 建檔日期時間
         , EmpNo                                 AS "CreateEmpNo"       -- 建檔人員
         , JOB_START_TIME                        AS "LastUpdate"        -- 最後更新日期時間
         , EmpNo                                 AS "LastUpdateEmpNo"   -- 最後更新人員
    FROM  "Work_B207" WK
      LEFT JOIN "CustMain" C         ON C."CustId"      = WK."CustId"
      LEFT JOIN "CdArea"   RegAddr   ON RegAddr."CityCode"  = C."RegCityCode"
                                    AND RegAddr."AreaCode"  = C."RegAreaCode"   -- 戶籍
      LEFT JOIN "CdCity"   RegCity   ON RegCity."CityCode"  = C."RegCityCode"   -- 戶籍
      LEFT JOIN "CdArea"   MailAddr  ON MailAddr."CityCode" = C."CurrCityCode"
                                    AND MailAddr."AreaCode" = C."CurrAreaCode"  -- 通訊
      LEFT JOIN "CdCity"   MailCity  ON MailCity."CityCode" = C."RegCityCode"   -- 通訊
      LEFT JOIN ( SELECT P."CustUKey", P."TelTypeCode"
                       , P."TelArea",  P."TelNo", P."TelExt"
                       , ROW_NUMBER()  OVER (Partition By P."CustUKey", P."TelTypeCode"
                                             Order By P."LastUpdate" DESC)  AS "Seq"
                  FROM   "CustTelNo" P
                  WHERE  P."Enable" = 'Y' AND P."TelTypeCode" = '01' AND P."TelNo" IS NOT NULL
                ) Phone1   ON Phone1."CustUKey"    = C."CustUKey"
                          AND Phone1."Seq"         = 1                   -- 公司電話
      LEFT JOIN ( SELECT P."CustUKey", P."TelTypeCode"
                       , P."TelArea",  P."TelNo", P."TelExt"
                       , ROW_NUMBER()  OVER (Partition By P."CustUKey", P."TelTypeCode"
                                             Order By P."LastUpdate" DESC)  AS "Seq"
                  FROM   "CustTelNo" P
                  WHERE  P."Enable" = 'Y' AND P."TelTypeCode" = '02' AND P."TelNo" IS NOT NULL
                ) Phone2   ON Phone2."CustUKey"    = C."CustUKey"
                          AND Phone2."Seq"         = 1                   -- 住家電話
      LEFT JOIN ( SELECT P."CustUKey", P."TelTypeCode"
                       , P."Mobile"
                       , ROW_NUMBER()  OVER (Partition By P."CustUKey", P."TelTypeCode"
                                             Order By P."LastUpdate" DESC)  AS "Seq"
                  FROM   "CustTelNo" P
                  WHERE  P."Enable" = 'Y' AND P."TelTypeCode" = '03' AND P."Mobile" IS NOT NULL
                ) CPhone   ON CPhone."CustUKey"    = C."CustUKey"
                          AND CPhone."Seq"         = 1                   -- 手機
      ;

    INS_CNT := INS_CNT + sql%rowcount;

    DBMS_OUTPUT.PUT_LINE('INSERT JcicB207 END: INS_CNT=' || INS_CNT);


    -- 記錄程式結束時間
    JOB_END_TIME := SYSTIMESTAMP;

    commit;

    -- 例外處理
--    Exception
--    WHEN OTHERS THEN
--   "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_L8_JcicB207_Upd',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);

--         , ' '             AS "CustName"          -- 中文姓名
--         , ' '             AS "EName"             -- 英文姓名
--         , 0               AS "Birthday"          -- 出生日期 (民國)
--         , ' '             AS "RegAddr"           -- 戶籍地址
--         , ' '             AS "CurrZip"           -- 聯絡地址郵遞區號
--         , ' '             AS "CurrAddr"          -- 聯絡地址
--         , ' '             AS "Tel"               -- 聯絡電話 (住家優先，再公司)
--         , ' '             AS "Mobile"            -- 行動電話
--         , ' '                                   AS "Filler14"          -- 空白
--         , ' '             AS "EduCode"           -- 教育程度代號 1:博士 2:碩士 3:大學 4:專科 5:高中高職 6:其他
--         , ' '             AS "OwnedHome"         -- 自有住宅有無 Y:有 N:無
--         , ' '             AS "CurrCompName"      -- 任職機構名稱
--         , ' '             AS "CurrCompId"        -- 任職機構統一編號
--         , ' '                                   AS "JobCode"           -- 職業類別  --???
--         , ' '             AS "CurrCompTel"       -- 任職機構電話
--         , ' '             AS "JobTitle"          -- 職位名稱
--         , ' '             AS "JobTenure"         -- 服務年資
--         , 0               AS "IncomeOfYearly"    -- 年收入
--         , 0               AS "IncomeDataDate"    -- 年收入資料年月 (民國)
--         , ' '             AS "Sex"               -- 性別
--         , ' '             AS "NationalityCode"   -- 國籍
--         , ' '             AS "PassportNo"        -- 護照號碼
--         , ' '                                   AS "PreTaxNo"          -- 舊有稅籍編號
--         , ' '             AS "FullCustName"      -- 中文姓名超逾10個字之全名

  END;
END;
