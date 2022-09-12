--------------------------------------------------------
--  DDL for Procedure Usp_Tf_LoanBorMain_Ins
--------------------------------------------------------
set define off;
DROP PROCEDURE "Usp_Tf_LoanBorMain_Ins" ;
  CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_Tf_LoanBorMain_Ins" 
(
    -- 參數
    JOB_START_TIME OUT TIMESTAMP, --程式起始時間
    JOB_END_TIME   OUT TIMESTAMP, --程式結束時間
    INS_CNT        OUT INT,       --新增資料筆數
    ERROR_MSG      OUT VARCHAR2   --錯誤訊息
)
AS
BEGIN
    -- 筆數預設0
    INS_CNT:=0;
    -- 記錄程式起始時間
    JOB_START_TIME := SYSTIMESTAMP;

    DECLARE 
        "TbsDyF" DECIMAL(8); --西元帳務日
    BEGIN

    SELECT "TbsDy" + 19110000
    INTO "TbsDyF"
    FROM "TxBizDate"
    WHERE "DateCode" = 'ONLINE'
    ;

    -- 刪除舊資料
    EXECUTE IMMEDIATE 'ALTER TABLE "LoanBorMain" DISABLE PRIMARY KEY CASCADE';
    EXECUTE IMMEDIATE 'TRUNCATE TABLE "LoanBorMain" DROP STORAGE';
    EXECUTE IMMEDIATE 'ALTER TABLE "LoanBorMain" ENABLE PRIMARY KEY';

    -- 寫入資料
    INSERT INTO "LoanBorMain"
    WITH T1 AS (
      SELECT DISTINCT
             S1."LMSACN"
            ,S1."LMSAPN1" -- 原額度號碼
            ,S1."LMSASQ1" -- 原撥款序號
      FROM (
        SELECT DISTINCT
               "LMSACN"
              ,"LMSAPN" -- 新額度號碼
              ,"LMSASQ" -- 新撥款序號
              ,"LMSAPN1" -- 原額度號碼
              ,"LMSASQ1" -- 原撥款序號
        FROM "LNACNP"
      ) S1
      LEFT JOIN "LA$LMSP" S2 ON S2."LMSACN" = S1."LMSACN"
                            AND S2."LMSAPN" = S1."LMSAPN"
                            AND S2."LMSASQ" = S1."LMSASQ"
      WHERE S2."LMSSTS" <> 3 -- 若展期後之新額度亦為結案,則結案
    ) 
    , T2RawData AS (
      SELECT DISTINCT
             "LMSACN"
            ,"LMSAPN" -- 新額度號碼
            ,"LMSASQ" -- 新撥款序號
            ,"LMSAPN1" -- 原額度號碼
            ,"LMSASQ1" -- 原撥款序號
            ,CASE
               WHEN "LMSAPN1" = "LMSAPN" -- 0:正常 1.展期(不同額度) 2.借新還舊(同額度)
               THEN 2
             ELSE 1 END AS "RenewFlag"
      FROM "LNACNP"
    )
    , T2OrderedData AS (
      SELECT "LMSACN"
            ,"LMSAPN" -- 新額度號碼
            ,"LMSASQ" -- 新撥款序號
            ,"LMSAPN1" -- 原額度號碼
            ,"LMSASQ1" -- 原撥款序號
            ,"RenewFlag"
            ,ROW_NUMBER()
             OVER (
              PARTITION BY "LMSACN"
                          ,"LMSAPN1"
                          ,"LMSASQ1"
              ORDER BY "LMSACN"
                      ,"LMSAPN"
                      ,"LMSASQ"
             ) AS "OldSeq" -- 以原額度號碼、原撥款序號為基礎的排序
            ,ROW_NUMBER()
             OVER (
              PARTITION BY "LMSACN"
                          ,"LMSAPN"
                          ,"LMSASQ"
              ORDER BY "LMSACN"
                      ,"LMSAPN1"
                      ,"LMSASQ1"
             ) AS "NewSeq" -- 以新額度號碼、新撥款序號為基礎的排序
      FROM T2RawData
    )
    , T2UnionAllData AS (
      SELECT S1."LMSACN"
            ,S1."LMSAPN" -- 新額度號碼
            ,S1."LMSASQ" -- 新撥款序號
            ,S1."RenewFlag"
            ,1 AS "OldNewFlag"
            ,S1."NewSeq" AS "Seq"
      FROM T2OrderedData S1
      UNION ALL
      SELECT S1."LMSACN"
            ,S1."LMSAPN1" AS "LMSAPN"-- 原額度號碼
            ,S1."LMSASQ1" AS "LMSASQ"-- 原撥款序號
            ,S1."RenewFlag"
            ,0 AS "OldNewFlag"
            ,S1."OldSeq" AS "Seq"
      FROM T2OrderedData S1
    )
    , T2LastOrderdData AS (
      SELECT S1."LMSACN"
            ,S1."LMSAPN"
            ,S1."LMSASQ"
            ,S1."RenewFlag"
            ,ROW_NUMBER()
             OVER (
              PARTITION BY "LMSACN"
                          ,"LMSAPN"
                          ,"LMSASQ"
              ORDER BY "OldNewFlag" DESC
                      ,"Seq" DESC
             ) AS "Seq" -- 排出最後一筆情況
      FROM T2UnionAllData S1
    )
    , T2 AS (
      SELECT S1."LMSACN"
            ,S1."LMSAPN"
            ,S1."LMSASQ"
            ,S1."RenewFlag"
      FROM T2LastOrderdData S1
      WHERE "Seq" = 1
    )
    , A1 AS (
      SELECT "LMSACN"
            ,"LMSAPN"
            ,"LMSASQ"
            ,"ASCRAT"
            ,ROW_NUMBER()
             OVER (
              PARTITION BY "LMSACN"
                          ,"LMSAPN"
              ORDER BY "ASCADT" DESC
             ) AS "SEQ"
      FROM "LA$ASCP"
      WHERE "ASCADT" <= "TbsDyF"
    )
    , IRTP AS (
      SELECT LM."LMSACN"
            ,LM."LMSAPN"
            ,LM."LMSASQ"
            ,IRTP."IRTRAT"
            ,ROW_NUMBER()
             OVER (
              PARTITION BY LM."LMSACN"
                          ,LM."LMSAPN"
                          ,LM."LMSASQ"
              ORDER BY IRTP."IRTADT" DESC
             ) AS "Seq"
      FROM "LA$LMSP" LM
      LEFT JOIN "LA$IRTP" IRTP ON IRTP."LMSACN" = LM."LMSACN"
                              AND IRTP."LMSAPN" = LM."LMSAPN"
                              AND IRTP."LMSASQ" = LM."LMSASQ"
                              AND IRTP."IRTADT" < LM."LMSLPD" -- 上繳日
    )
    , EXG AS (
      SELECT EX."LMSACN"
            ,EX."LMSAPN"
            ,EX."LMSASQ"
            ,EX."EXGCDE"
            ,EX."M24070"
            ,CB."BankCode"
            ,CB."BranchCode"
            ,CASE
               WHEN REGEXP_LIKE(EX."EXGACN",'^[0-9]+$')
               THEN TO_NUMBER(EX."EXGACN")
             ELSE 0 END AS "EXGACN" -- 匯款帳號
            ,LPAD(EX."EXGBBC",7,'0') AS "EXGBBC"
            ,ROW_NUMBER()
             OVER (
               PARTITION BY EX."LMSACN"
                           ,EX."LMSAPN"
                           ,EX."LMSASQ"
               ORDER BY EX."TRXDAT"
                       ,EX."TRXNMT"
                       ,EX."EXGASQ"
             ) AS "SEQ"
      FROM "DAT_LA$EXGP" EX
      LEFT JOIN "CdBank" CB ON CB."BankCode" || CB."BranchCode"  = LPAD(EX."EXGBBC",7,'0')
      WHERE NVL("TRXCRC",0) = 0 
    )
    SELECT LMSP."LMSACN"                  AS "CustNo"              -- 借款人戶號 DECIMAL 7 
          ,LMSP."LMSAPN"                  AS "FacmNo"              -- 額度編號 DECIMAL 3 
          ,CASE
             WHEN LMSP."LMSLLD" > "TbsDyF" 
             THEN 900 + LMSP."LMSASQ" -- 撥款日期>轉換日時,為預約撥款,撥款序號 + 900
           ELSE LMSP."LMSASQ" END         AS "BormNo"              -- 撥款序號, 預約序號 DECIMAL 3 
          ,0                              AS "LastBorxNo"          -- 已編BorTx流水號 DECIMAL 4 
          ,0                              AS "LastOvduNo"          -- 已編Overdue流水號 DECIMAL 3 
          ,CASE
             WHEN LMSP."LMSLLD" > "TbsDyF"
             THEN 99 -- 撥款日期>轉換日時,為預約撥款,戶況寫99
             WHEN LMSP."LMSSTS" = 3 AND NVL(T1."LMSASQ1",0) <> 0 
             THEN 1
           ELSE LMSP."LMSSTS" END         AS "Status"              -- 戶況 DECIMAL 2 
          ,LMSP."IRTASC"                  AS "RateIncr"            -- 加碼利率 DECIMAL 6 4
          ,0                              AS "IndividualIncr"      -- 個別加碼利率 DECIMAL 6 4
          ,NVL(APLP."APLRAT",0)           AS "ApproveRate"         -- 核准利率 DECIMAL 6 4
          ,NVL(IRTP."IRTRAT",NVL(APLP."APLRAT",0))
                                          AS "StoreRate"           -- 實際計息利率 DECIMAL 6 4
          ,LMSP."AILIRT"                  AS "RateCode"            -- 利率區分 VARCHAR2 1 
          ,LMSP."IRTMSC"                  AS "RateAdjFreq"         -- 利率調整週期 DECIMAL 2 
          ,NVL(EXG."EXGCDE",'')           AS "DrawdownCode"        -- 撥款方式 VARCHAR2 1 
          ,'TWD'                          AS "CurrencyCode"        -- 幣別 VARCHAR2 3 
          ,LMSP."LMSFLA"                  AS "DrawdownAmt"         -- 撥款金額 DECIMAL 16 2
          ,CASE
             WHEN LMSP."LMSLLD" > "TbsDyF" 
             THEN 0 -- 撥款日期>轉換日時,為預約撥款,餘額擺0
           ELSE LMSP."LMSLBL" END         AS "LoanBal"             -- 放款餘額 DECIMAL 16 2
          ,LMSP."LMSLLD"                  AS "DrawdownDate"        -- 撥款日期, 預約日期 DECIMALD 8 
          ,TRUNC(MONTHS_BETWEEN(TO_DATE(LMSP."LMSDLD",'yyyymmdd'),TO_DATE(LMSP."LMSLLD",'yyyymmdd')) / 12)
                                          AS "LoanTermYy"          -- 貸款期間年 DECIMAL 2 
          ,MOD(TRUNC(MONTHS_BETWEEN(TO_DATE(LMSP."LMSDLD",'yyyymmdd'),TO_DATE(LMSP."LMSLLD",'yyyymmdd'))),12)
                                          AS "LoanTermMm"          -- 貸款期間月 DECIMAL 2 
          ,TO_DATE(LMSP."LMSDLD",'yyyymmdd')
           -ADD_MONTHS(TO_DATE(LMSP."LMSLLD",'yyyymmdd'),TRUNC(MONTHS_BETWEEN(TO_DATE(LMSP."LMSDLD",'yyyymmdd'),TO_DATE(LMSP."LMSLLD",'yyyymmdd'))))
                                          AS "LoanTermDd"          -- 貸款期間日 DECIMAL 3 
          ,LMSP."LMSDLD"                  AS "MaturityDate"        -- 到期日 DECIMALD 8 
          ,CASE
             WHEN LMSP."ACTACT" = '310' THEN '1' -- 以日計息
             WHEN LMSP."TRXJAC" = 1     THEN '1' -- 以日計息 
           ELSE '2' END --以月計息
                                          AS "IntCalcCode"
          ,LMSP."LMSRTP"                  AS "AmortizedCode"       -- 攤還方式 VARCHAR2 1 
          ,2                              AS "FreqBase"            -- 週期基準 DECIMAL 1 
          ,LMSP."LMSISC"                  AS "PayIntFreq"          -- 繳息週期 DECIMAL 2 
          ,LMSP."LMSPSC"                  AS "RepayFreq"           -- 還本週期 DECIMAL 2 
          ,LMSP."LMSTPR"                  AS "TotalPeriod"         -- 總期數 DECIMAL 3 
          ,CASE
             WHEN LMSP."LMSPPR" > LMSP."LMSGCC" 
             THEN LMSP."LMSPPR" - LMSP."LMSGCC"
           ELSE 0 END                     AS "RepaidPeriod"        -- 已還本期數 DECIMAL 3 
          ,LMSP."LMSPPR"                  AS "PaidTerms"           -- 已繳息期數 DECIMAL 3 
          ,LMSP."LMSLPD"                  AS "PrevPayIntDate"      -- 上次繳息日,繳息迄日 DECIMALD 8 
          ,LMSP."LMSFLD"                  AS "PrevRepaidDate"      -- 上次還本日,最後還本日 DECIMALD 8 
          ,CASE
             -- 若 到期取息者
             WHEN LMSP."LMSRTP" = 2
             -- 以 到期日 為 下次繳息日
             THEN LMSP."LMSDLD"
             -- 第一期特殊處理
             -- 若 戶況為0 且 上次繳息日 = 撥款日期
             WHEN LMSP."LMSSTS" = 0 AND LMSP."LMSLPD" = LMSP."LMSLLD"
             -- 以 首次應還日 為 下次繳息日
             THEN LMSP."LMSPBD"
           -- 一般處理
           -- 上次繳息日 + 1個繳息周期 (舊資料都是1個月)
           -- TO_NUMBER(TO_CHAR(ADD_MONTHS(TO_DATE(LMSP."LMSLPD",'yyyymmdd'),1),'yyyymmdd'))
           ELSE 
             CASE
               -- 2021-04-22 智偉修改 FROM 賴桑口說 下次繳息日大於到期日時,不以到期日為基準,仍使用下次繳息日
               -- -- 若 計算結果 大於 到期日 
               -- WHEN TO_NUMBER(TO_CHAR(ADD_MONTHS(TO_DATE(LMSP."LMSLPD",'yyyymmdd'),1),'yyyymmdd')) > LMSP."LMSDLD"
               -- -- 以 到期日 為 下次繳息日
               -- THEN LMSP."LMSDLD"
               -- 若計算結果的最後兩碼 大於 指定應繳日(2碼)
               WHEN SUBSTR(TO_CHAR(ADD_MONTHS(TO_DATE(LMSP."LMSLPD",'yyyymmdd'),1),'yyyymmdd'),-2) > LPAD(LMSP."LMSPDY",2,'0')
               -- 重組下次繳息日
               THEN TO_NUMBER(SUBSTR(TO_CHAR(ADD_MONTHS(TO_DATE(LMSP."LMSLPD",'yyyymmdd'),1),'yyyymmdd'),0,6) || LPAD(LMSP."LMSPDY",2,'0'))
             ELSE TO_NUMBER(TO_CHAR(ADD_MONTHS(TO_DATE(LMSP."LMSLPD",'yyyymmdd'),1),'yyyymmdd')) END
           END                            AS "NextPayIntDate"      -- 下次繳息日,應繳息日 DECIMALD 8 
          ,LMSP."LMSNPD"                  AS "NextRepayDate"       -- 下次還本日,應還本日 DECIMALD 8 
          ,LMSP."LMSPPA"                  AS "DueAmt"              -- 每期攤還金額 DECIMAL 16 2
          ,LMSP."LMSGCC"                  AS "GracePeriod"         -- 寬限期 DECIMAL 3 
          ,LMSP."LMSGPD"                  AS "GraceDate"           -- 寬限到期日 DECIMALD 8 
          ,CASE
             WHEN LMSP."LMSRTP" = 2 THEN 0 --到期取息者,沒有指定應繳日
           ELSE LMSP."LMSPDY" END         AS "SpecificDd"          -- 指定應繳日 DECIMAL 2 
          ,CASE
             -- 2021-04-16 智偉修改 : 應繳日 >= 首次應繳日末兩碼 時, 取前一月的應繳日
             WHEN LPAD(LMSP."LMSPDY",2,'0') >= SUBSTR(TO_CHAR(LMSP."LMSPBD"),-2)
             THEN 
               CASE
                 -- 若 前一月的應繳日 大於 前一月的月底日 , 則取前一月的月底日
                 WHEN SUBSTR(TO_CHAR(ADD_MONTHS(TO_DATE(LMSP."LMSPBD",'yyyymmdd'),-1),'yyyymmdd'),0,6) || LPAD(LMSP."LMSPDY",2,'0')
                      > TO_CHAR(LAST_DAY(ADD_MONTHS(TO_DATE(LMSP."LMSPBD",'yyyymmdd'),-1)),'yyyymmdd')
                 THEN TO_NUMBER(TO_CHAR(LAST_DAY(ADD_MONTHS(TO_DATE(LMSP."LMSPBD",'yyyymmdd'),-1)),'yyyymmdd'))
               -- 否則 取前一月的應繳日
               ELSE TO_NUMBER(SUBSTR(TO_CHAR(ADD_MONTHS(TO_DATE(LMSP."LMSPBD",'yyyymmdd'),-1),'yyyymmdd'),0,6) || LPAD(LMSP."LMSPDY",2,'0'))
               END
           -- 否則 取同月的應繳日 (應繳日 小於 首次應繳日末兩碼時 , 組合後不會有日期不合法的情形)
           ELSE TO_NUMBER(SUBSTR(TO_CHAR(LMSP."LMSPBD"),0,6) || LPAD(LMSP."LMSPDY",2,'0'))
           END                            AS "SpecificDate"        -- 指定基準日期 DECIMALD 8 
          ,LMSP."LMSPBD"
           --"Fn_L3GetDate"(LMSP."LMSLLD",LMSP."LMSDLD",LMSP."LMSPDY",LMSP."LMSRTP",0)
                                          AS "FirstDueDate"        -- 首次應繳日 DECIMALD 8 
          ,LMSP."LMSFSD"                  AS "FirstAdjRateDate"    -- 首次利率調整日期 DECIMALD 8 
          ,CASE
             -- 抓 "LA$IRTP" 的利率為 5 且 生效日 > 本月
             -- 把該筆的 IRTADT 生效日寫到"下次利率調整日期"
             -- 2020-12-25 依據賴桑mail 1.機動利率 5% 不轉下次利率調整日
             -- 將此行取消
             --  WHEN LMSP."IRTMSC" = 0 AND NVL(LI."IRTADT",0) > 0 THEN NVL(LI."IRTADT",0)
             -- 有首次利率調整日期 且 下次利率調整日期為0者 放首次利率調整日期
             WHEN LMSP."LMSFSD" > 0 AND LMSP."LMSNSD" = 0 
             THEN LMSP."LMSFSD"
           ELSE LMSP."LMSNSD" END         AS "NextAdjRateDate"     -- 下次利率調整日期 DECIMALD 8 
          -- 2022-02-16 QC1435
          ,NVL(ACFP."ACTFEE",0)           AS "AcctFee"             -- 帳管費 DECIMAL 16 2
          ,0                              AS "HandlingFee"         -- 手續費 DECIMAL 16 2
          ,0                              AS "FinalBal"            -- 最後一期本金餘額 DECIMAL 16 2
          ,'N'                            AS "NotYetFlag"          -- 未齊件 VARCHAR2 1 
          /* 2021-03-19 智偉修改:原欄位值為0、1，新系統為Y/N */
          /* 2022-01-14 智偉修改:新系統修改欄位定義 0:正常 1.展期(不同額度) 2.借新還舊(同額度) */
          ,CASE
             WHEN NVL(T2."RenewFlag",0) != 0
             THEN T2."RenewFlag"
             WHEN LMSP."LMSNEW" = '1'
             THEN 1
           ELSE 0 END                     AS "RenewFlag"           -- 借新還舊 DECIMAL 1 
          ,NVL(APLP."CASCDE",' ')         AS "PieceCode"           -- 計件代碼 VARCHAR2 1 
          ,''                             AS "PieceCodeSecond"
          ,0                              AS "PieceCodeSecondAmt"
          ,LPAD(APLP."APLUSG",2,'0')      AS "UsageCode"           -- 資金用途別 VARCHAR2 2 
          ,0                              AS "SyndNo"              -- 聯貸案序號 DECIMAL 6
          ,LMSP."LMSPRL"                  AS "RelationCode"        -- 與借款人關係 VARCHAR2 2 
          ,LMSP."LMSPAN"                  AS "RelationName"        -- 第三人帳戶戶名 NVARCHAR2 100 
          ,LMSP."LMSPID"                  AS "RelationId"          -- 第三人身份證字號 VARCHAR2 10
          ,NVL(CU3."CUSBDT",0)            AS "RelationBirthday"    -- 第三人生日 DECIMALD 8 
          ,CASE
             WHEN TRIM(CU3."CUSSEX") IN ('1','2') 
             THEN TRIM(CU3."CUSSEX")
             WHEN TRIM(CU3."CUSSEX") IN ('0','6') 
             THEN '0'
           ELSE
             CASE
               WHEN LENGTHB(REPLACE(TRIM(CU3."CUSID1"),CHR(26),'')) = 10
                    AND SUBSTR(REPLACE(TRIM(CU3."CUSID1"),CHR(26),''),0,1) IN ('1','2')
               THEN SUBSTR(REPLACE(TRIM(CU3."CUSID1"),CHR(26),''),0,1)
             ELSE '0'
             END
           END                            AS "RelationGender"      -- 第三人性別 VARCHAR2 1 
          ,0                              AS "ActFg"               -- 交易進行記號 DECIMAL 1 
          ,LMSP."LMSDAT"                  AS "LastEntDy"           -- 上次交易日 DECIMALD 8 
          ,''                             AS "LastKinbr"           -- 上次交易行別 VARCHAR2 4 
          ,''                             AS "LastTlrNo"           -- 上次櫃員編號 VARCHAR2 6 
          ,LMSP."LMSNMT"                  AS "LastTxtNo"           -- 上次交易序號 VARCHAR2 8 
          ,EXG."BankCode"                 AS "RemitBank"           -- 匯款銀行 VARCHAR2 3 
          ,EXG."BranchCode"               AS "RemitBranch"         -- 匯款分行 VARCHAR2 4 
          ,NVL(EXG."EXGACN",0)            AS "RemitAcctNo"         -- 匯款帳號 DECIMAL 14 
          ,LMSP."LMSPYC"                  AS "CompensateAcct"      -- 代償專戶 NVARCHAR2 60 
          ,EXG."EXGBBC"                   AS "PaymentBank"         -- 解付單位代號 VARCHAR2 7 
          ,NVL("LN$CLMP"."M24070",EXG."M24070")
                                          AS "Remark"              -- 附言 NVARCHAR2 40 
          ,0                              AS "AcDate"              -- 會計日期 DECIMALD 8 
          ,0                              AS "NextAcDate"          -- 次日交易會計日期 DECIMALD 8 
          ,'0000'                         AS "BranchNo"
          ,JOB_START_TIME                 AS "CreateDate"          -- 建檔日期時間 DATE  
          ,'999999'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 
          ,JOB_START_TIME                 AS "LastUpdate"          -- 最後更新日期時間 DATE  
          ,'999999'                       AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 
          -- 2022-06-21 Wei 新增 from Linda
          -- 智偉~
          -- 賴桑說AS400的LA$LMSP.LMSGTP寬限區分要轉資料進來,需要在LoanBorMain加一個欄位,
          -- 賴桑會調整程式維護欄位(撥款主檔寬限區分欄位為隱藏欄位,判斷是否案件核准時有寬限期)
          ,LMSP."LMSGTP"                  AS "GraceFlag"           -- 寬限區分 DECIMAL 1
    FROM "LA$LMSP" LMSP
    LEFT JOIN "LN$CLMP" ON "LN$CLMP"."LMSACN" = LMSP."LMSACN"
                       AND "LN$CLMP"."LMSAPN" = LMSP."LMSAPN"
                       AND "LN$CLMP"."LMSASQ" = LMSP."LMSASQ"
    LEFT JOIN T1 ON T1."LMSACN" = LMSP."LMSACN"
                AND T1."LMSAPN1" = LMSP."LMSAPN"
                AND T1."LMSASQ1" = LMSP."LMSASQ"
    LEFT JOIN T2 ON T2."LMSACN" = LMSP."LMSACN"
                AND T2."LMSAPN" = LMSP."LMSAPN"
                AND T2."LMSASQ" = LMSP."LMSASQ"
    LEFT JOIN "LA$APLP" APLP ON APLP."LMSACN" = LMSP."LMSACN"
                            AND APLP."LMSAPN" = LMSP."LMSAPN"
    LEFT JOIN A1 ON A1."LMSACN" = LMSP."LMSACN"
                AND A1."LMSAPN" = LMSP."LMSAPN"
                AND A1."LMSASQ" = LMSP."LMSASQ"
                AND A1."SEQ" = 1
    LEFT JOIN  "LA$IRTP" LI ON LI."LMSACN" = LMSP."LMSACN"
                           AND LI."LMSAPN" = LMSP."LMSAPN"
                           AND LI."LMSASQ" = LMSP."LMSASQ"
                           AND LI."IRTRAT" = 5
                           AND LI."IRTADT" >= LMSP."LMSLPD"
                           AND LMSP."LMSSTS" <> 3
    LEFT JOIN EXG ON EXG."LMSACN" = LMSP."LMSACN"
                 AND EXG."LMSAPN" = LMSP."LMSAPN"
                 AND EXG."LMSASQ" = LMSP."LMSASQ"
                 AND EXG."SEQ" = 1
    LEFT JOIN IRTP ON IRTP."LMSACN" = LMSP."LMSACN"
                  AND IRTP."LMSAPN" = LMSP."LMSAPN"
                  AND IRTP."LMSASQ" = LMSP."LMSASQ"
                  AND IRTP."Seq" = 1
    LEFT JOIN "LN$ACFP" ACFP ON ACFP."LMSACN" = LMSP."LMSACN"
                            AND ACFP."LMSAPN" = LMSP."LMSAPN"
                            AND ACFP."LMSASQ" = LMSP."LMSASQ"
    LEFT JOIN "CU$CUSP" CU3 ON CU3.CUSID1 = NVL(LMSP."LMSPID",' ') -- 第三人資料
    ;

    -- 記錄寫入筆數
    INS_CNT := INS_CNT + sql%rowcount;

    -- 記錄程式結束時間
    JOB_END_TIME := SYSTIMESTAMP;

    commit;

    END;
    -- 例外處理
    Exception
    WHEN OTHERS THEN
    ERROR_MSG := SQLERRM || CHR(13) || CHR(10) || dbms_utility.format_error_backtrace;
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_LoanBorMain_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;

/