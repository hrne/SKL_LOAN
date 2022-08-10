CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_L8_JcicB207_Upd"
(
-- 程式功能：維護 JcicB207 聯徵授信戶基本資料檔
-- 執行時機：每月日終批次(換日前)
-- 執行方式：EXEC "Usp_L8_JcicB207_Upd"(20200430,'999999');
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
    DBMS_OUTPUT.PUT_LINE('DELETE JcicB207');

    DELETE FROM "JcicB207"
    WHERE "DataYM" = YYYYMM
      ;

    -- 寫入資料
    DBMS_OUTPUT.PUT_LINE('INSERT JcicB207');

    INSERT INTO "JcicB207"
    WITH "Work_B207" AS (

    -- 寫入資料 Work_B207    -- 撈應申報之戶號,從201撈出全部自然人,且有餘額取撥款日
    SELECT DISTINCT
           B."CustId"                    AS "CustId"            -- ID
         , first_value(M."DrawdownDate") Over (Partition By M."CustId" Order By M."DrawdownDate" ASC)
                                         AS "DrawdownDate"      -- 本筆撥款開始年月 (最近貸放的那一筆)
    FROM   "JcicB201" B
      LEFT JOIN "JcicMonthlyLoanData" M ON M."DataYM" = YYYYMM 
                                       AND M."CustNo" = to_number(SUBSTR(B."AcctNo",1, 7))
    WHERE  B."DataYM"   =  YYYYMM
      AND  B."CustId"   IS NOT NULL
      AND  M."DrawdownDate" <= TMNDYF
      AND  M."LoanBal"  >  0 -- 有餘額
      AND  M."EntCode"  IN ('0', '2')  -- 自然人
      )
    , "Work_B207_All" AS (
    -- 寫入資料 Work_B207_ALL    -- 撈應申報之戶號,從201撈出全部自然人
    SELECT DISTINCT
           B."CustId"                    AS "CustId"            -- ID
         , CASE
             WHEN NVL(WK."DrawdownDate",0) = 0
             THEN first_value(M."DrawdownDate") Over (Partition By M."CustId" Order By M."DrawdownDate" DESC)
           ELSE NVL(WK."DrawdownDate",0) END
                                         AS "DrawdownDate"      -- 本筆撥款開始年月 (若該戶全部已結清則為最近貸放的那一筆)
    FROM   "JcicB201" B
      LEFT JOIN "Work_B207" WK          ON B."CustId" = WK."CustId"
      LEFT JOIN "JcicMonthlyLoanData" M ON M."DataYM" = YYYYMM 
                                       AND M."CustNo" = to_number(SUBSTR(B."AcctNo",1, 7))
    WHERE  B."DataYM"   =  YYYYMM
      AND  B."CustId"   IS NOT NULL
      AND  M."DrawdownDate" <= TMNDYF
      AND  M."EntCode"  IN ('0', '2')  -- 自然人
      )

    SELECT
           YYYYMM                                AS "DataYM"            -- 資料年月
         , 'C'                                   AS "TranCode"          -- 交易代碼   A:新增 C:異動(全部整檔報送時可使用此代號) D:刪除
         , '458'                                 AS "BankItem"          -- 總行代號
         , '0001'                                AS "Filler3"           -- 空白(填分行代號)
         , TMNDYF - 19110000                     AS "DataDate"          -- 資料日期 (民國)
         , C."CustId"                            AS "CustId"            -- 授信戶IDN
         , CASE
             WHEN TRIM(NVL(C."CustName", ' ')) = ''  THEN TO_NCHAR('　　　　　　　　　　')
             ELSE RPAD(REPLACE(C."CustName",'　'),20,TO_NCHAR('　'))   
           END                                   AS "CustName"          -- 中文姓名
         --, RPAD(C."CustName",18,'　')            AS "CustName"          -- 中文姓名
         , SUBSTR(NVL(C."EName",' '),1,20)       AS "EName"             -- 英文姓名
         , CASE
             WHEN NVL(C."Birthday",0) < 19110000 THEN NVL(C."Birthday",0)
             ELSE C."Birthday" - 19110000
           END                                   AS "Birthday"          -- 出生日期 (民國)
         , RPAD("Fn_GetCustAddr"(C."CustUKey",0),60,TO_NCHAR('　')) AS "RegAddr"  -- 戶籍地址
         , NVL(
             CASE
               WHEN C."CurrZip3" IS NOT NULL THEN C."CurrZip3"
               ELSE '000'
             END ||
             CASE
               WHEN C."CurrZip2" IS NOT NULL THEN SUBSTR(C."CurrZip2",1,2)
               ELSE '00'
             END
           , ' ')                                AS "CurrZip"           -- 聯絡地址郵遞區號
         , RPAD("Fn_GetCustAddr"(C."CustUKey",1),60,TO_NCHAR('　')) AS "CurrAddr" -- 聯絡地址
         , CASE
             WHEN Phone1."TelNo" IS NULL AND  Phone2."TelNo" IS NULL THEN
               CASE
                 WHEN CPhone."Mobile" IS NULL THEN ' '
                 ELSE SUBSTR(CPhone."Mobile",1,4) || '-' || SUBSTR(CPhone."Mobile",5)
               END
             WHEN Phone1."TelNo" IS NULL THEN
               CASE
                 WHEN Phone2."TelNo" IS NULL THEN ' '
                 ELSE NVL2(Phone2."TelArea", Phone2."TelArea" || '-', '') ||
                      NVL2(Phone2."TelNo",   Phone2."TelNo",          '') ||
                      NVL2(Phone2."TelExt",  '#' || Phone2."TelExt",  '')
               END
             ELSE
               CASE
                 WHEN Phone1."TelNo" IS NULL THEN ' '
                 ELSE NVL2(Phone1."TelArea", Phone1."TelArea" || '-', '') ||
                      NVL2(Phone1."TelNo",   Phone1."TelNo",          '') ||
                      NVL2(Phone1."TelExt",  '#' || Phone1."TelExt",  '')
               END
           END                                   AS "Tel"               -- 聯絡電話 (公司Phone1優先，再住家Phone2)
         , CASE
             WHEN CPhone."Mobile" IS NULL THEN ' '
             ELSE CPhone."Mobile"
           END                                   AS "Mobile"            -- 行動電話
         , ' '                                   AS "Filler14"          -- 空白
         , CASE
             WHEN C."EduCode" IS NULL  THEN '6'
             WHEN C."EduCode" IN ('7') THEN '1'
             WHEN C."EduCode" IN ('6') THEN '2'
             WHEN C."EduCode" IN ('5') THEN '3'
             WHEN C."EduCode" IN ('4') THEN '4'
             WHEN C."EduCode" IN ('3') THEN '5'
             ELSE                           '6'
           END                                   AS "EduCode"           -- 教育程度代號 1:博士 2:碩士 3:大學 4:專科 5:高中高職 6:其他
         , NVL(C."OwnedHome",' ')                AS "OwnedHome"         -- 自有住宅有無 Y:有 N:無
         , CASE 
             WHEN TRIM(NVL(C."CurrCompName", ' ')) = ''  THEN TO_NCHAR('　　　　　　　　　　　　　　')
             ELSE RPAD(C."CurrCompName",28,TO_NCHAR('　')) 
           END                                   AS "CurrCompName"      -- 任職機構名稱
         , CASE WHEN TRIM(NVL(C."CurrCompId",'0')) = '0' THEN '00000000'
                  ELSE LPAD(C."CurrCompId",8,'0')
           END                                   AS "CurrCompId"        -- 任職機構統一編號
         , NVL(C."IndustryCode",'060000')        AS "JobCode"           -- 職業類別  (ref:LN15J1 (#M4019 1))
         , NVL(C."CurrCompTel",' ')              AS "CurrCompTel"       -- 任職機構電話
         , CASE 
             WHEN TRIM(NVL(C."JobTitle", ' ')) = ''  THEN TO_NCHAR('　　　　')
             ELSE RPAD(C."JobTitle",8,TO_NCHAR('　')) 
           END                                   AS "JobTitle"          -- 職位名稱
         , NVL(C."JobTenure",' ')                AS "JobTenure"         -- 服務年資
         , CASE
             WHEN NVL(C."IncomeOfYearly",0) = 0 THEN 600  -- (ref:LN15J1 (#M4023 1))
             ELSE NVL(C."IncomeOfYearly",0)
           END                                   AS "IncomeOfYearly"    -- 年收入
         , CASE
             WHEN to_number(TRIM(NVL(C."IncomeDataDate",0))) >  0 
                  AND (YYYY - TRUNC(to_number(C."IncomeDataDate")/100) > 30) THEN ( YYYYMM - 191100) -- 與報送年月不可差超過30年
             WHEN to_number(TRIM(NVL(C."IncomeDataDate",0))) >  0 THEN to_number(C."IncomeDataDate") - 191100
             WHEN NVL(WK."DrawdownDate",0) > 0 
                  AND (YYYY - TRUNC(NVL(WK."DrawdownDate",0) / 10000)  > 30 ) THEN ( YYYYMM - 191100)
             WHEN NVL(WK."DrawdownDate",0) > 0                    THEN TRUNC((WK."DrawdownDate" - 19110000) / 100)
             ELSE YYYYMM - 191100
           END                                   AS "IncomeDataDate"    -- 年收入資料年月 (民國)
         , DECODE(C."Sex",'1','M','2','F','F')   AS "Sex"               -- 性別,若代碼非1或2則值放F
         , NVL(C."NationalityCode",'TW')         AS "NationalityCode"   -- 國籍
         , CASE
             WHEN SUBSTR(C."CustId",1,8) BETWEEN '00000000' AND '99999999'
              AND SUBSTR(C."CustId",9,2) BETWEEN 'AA' AND 'ZZ'  THEN NVL(C."PassportNo",' ')
             ELSE ' '
           END                                   AS "PassportNo"        -- 護照號碼
         , ' '                                   AS "PreTaxNo"          -- 舊有稅籍編號
         , CASE
             WHEN lengthb(REPLACE(C."CustName",'　')) <= 20 THEN N' '
             ELSE REPLACE(C."CustName",'　')
           END                                   AS "FullCustName"      -- 中文姓名超逾10個字之全名
         , ' '                                   AS "Filler30"          -- 空白
         , JOB_START_TIME                        AS "CreateDate"        -- 建檔日期時間
         , EmpNo                                 AS "CreateEmpNo"       -- 建檔人員
         , JOB_START_TIME                        AS "LastUpdate"        -- 最後更新日期時間
         , EmpNo                                 AS "LastUpdateEmpNo"   -- 最後更新人員
    FROM  "Work_B207_All" WK
      LEFT JOIN "CustMain" C         ON C."CustId"      = WK."CustId"
      LEFT JOIN "CdArea"   RegAddr   ON RegAddr."CityCode"  = C."RegCityCode"
                                    AND RegAddr."AreaCode"  = C."RegAreaCode"   -- 戶籍
      LEFT JOIN "CdCity"   RegCity   ON RegCity."CityCode"  = C."RegCityCode"   -- 戶籍
      LEFT JOIN "CdArea"   MailAddr  ON MailAddr."CityCode" = C."CurrCityCode"
                                    AND MailAddr."AreaCode" = C."CurrAreaCode"  -- 通訊
      LEFT JOIN "CdCity"   MailCity  ON MailCity."CityCode" = C."CurrCityCode"  -- 通訊
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
                       , P."TelNo"     AS "Mobile"
                       , ROW_NUMBER()  OVER (Partition By P."CustUKey", P."TelTypeCode"
                                             Order By P."LastUpdate" DESC)  AS "Seq"
                  FROM   "CustTelNo" P
                  WHERE  P."Enable" = 'Y' AND P."TelTypeCode" = '03' AND P."TelNo" IS NOT NULL
                ) CPhone   ON CPhone."CustUKey"    = C."CustUKey"
                          AND CPhone."Seq"         = 1                   -- 手機
    WHERE C."EntCode" NOT IN ('1')
      ;

    INS_CNT := INS_CNT + sql%rowcount;

    DBMS_OUTPUT.PUT_LINE('INSERT JcicB207 END: INS_CNT=' || INS_CNT);


-- 轉全形

    DBMS_OUTPUT.PUT_LINE('UPDATE 轉全形 ');

    UPDATE "JcicB207" M
    SET   M."CustName"     = SUBSTRB(TO_MULTI_BYTE(TRIM(M."CustName")), 1, 30)
        , M."RegAddr"      = SUBSTRB(TO_MULTI_BYTE(TRIM(M."RegAddr")), 1, 99)
        , M."CurrAddr"     = SUBSTRB(TO_MULTI_BYTE(TRIM(M."CurrAddr")), 1, 99)
        , M."CurrCompName" = SUBSTRB(TO_MULTI_BYTE(TRIM(M."CurrCompName")), 1, 45)
        , M."JobTitle"     = SUBSTRB(TO_MULTI_BYTE(TRIM(M."JobTitle")), 1, 15)
        , M."FullCustName" = SUBSTRB(TO_MULTI_BYTE(TRIM(M."FullCustName")), 1, 300)
    WHERE M."DataYM" = YYYYMM
      ;

    DBMS_OUTPUT.PUT_LINE('UPDATE END 轉全形 ');

    -- 記錄程式結束時間
    JOB_END_TIME := SYSTIMESTAMP;
    DBMS_OUTPUT.PUT_LINE('Spend Time: ' ||
        to_number(  to_date(to_char(JOB_END_TIME,'  yyyy-mm-dd hh24:mi:ss'), 'yyyy-mm-dd hh24:mi:ss')
                  - to_date(to_char(JOB_START_TIME,'yyyy-mm-dd hh24:mi:ss'), 'yyyy-mm-dd hh24:mi:ss')
                  ) * 86400       || ' Secs' );

    commit;

  END;
END;


