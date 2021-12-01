--------------------------------------------------------
--  DDL for Procedure Usp_Tf_LoanBorMain_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "Usp_Tf_LoanBorMain_Ins" 
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
    SELECT "LA$LMSP"."LMSACN"             AS "CustNo"              -- 借款人戶號 DECIMAL 7 
          ,"LA$LMSP"."LMSAPN"             AS "FacmNo"              -- 額度編號 DECIMAL 3 
          ,CASE
             WHEN "LA$LMSP"."LMSLLD" > "TbsDyF" THEN 900 + "LA$LMSP"."LMSASQ" -- 撥款日期>轉換日時,為預約撥款,撥款序號 + 900
           ELSE "LA$LMSP"."LMSASQ" END    AS "BormNo"              -- 撥款序號, 預約序號 DECIMAL 3 
          ,0                              AS "LastBorxNo"          -- 已編BorTx流水號 DECIMAL 4 
          ,0                              AS "LastOvduNo"          -- 已編Overdue流水號 DECIMAL 3 
          ,CASE
             WHEN "LA$LMSP"."LMSLLD" > "TbsDyF"                      THEN 99 -- 撥款日期>轉換日時,為預約撥款,戶況寫99
             WHEN "LA$LMSP"."LMSSTS" = 3 AND NVL(T1."LMSASQ1",0) <> 0 THEN 1
           ELSE "LA$LMSP"."LMSSTS" END    AS "Status"              -- 戶況 DECIMAL 2 
          ,"LA$LMSP"."IRTASC"             AS "RateIncr"            -- 加碼利率 DECIMAL 6 4
          ,0                              AS "IndividualIncr"      -- 個別加碼利率 DECIMAL 6 4
          ,NVL(APLP."APLRAT",0)           AS "ApproveRate"         -- 核准利率 DECIMAL 6 4
          ,NVL(IRTP."IRTRAT",NVL(APLP."APLRAT",0))
                                          AS "StoreRate"           -- 實際計息利率 DECIMAL 6 4
          ,"LA$LMSP"."AILIRT"             AS "RateCode"            -- 利率區分 VARCHAR2 1 
          ,"LA$LMSP"."IRTMSC"             AS "RateAdjFreq"         -- 利率調整週期 DECIMAL 2 
          ,NVL(EXG."EXGCDE",'')           AS "DrawdownCode"        -- 撥款方式 VARCHAR2 1 
          ,'TWD'                          AS "CurrencyCode"        -- 幣別 VARCHAR2 3 
          ,"LA$LMSP"."LMSFLA"             AS "DrawdownAmt"         -- 撥款金額 DECIMAL 16 2
          ,CASE
             WHEN "LA$LMSP"."LMSLLD" > "TbsDyF" THEN 0 -- 撥款日期>轉換日時,為預約撥款,餘額擺0
           ELSE "LA$LMSP"."LMSLBL" END    AS "LoanBal"             -- 放款餘額 DECIMAL 16 2
          ,"LA$LMSP"."LMSLLD"             AS "DrawdownDate"        -- 撥款日期, 預約日期 DECIMALD 8 
          ,TRUNC(MONTHS_BETWEEN(TO_DATE("LA$LMSP"."LMSDLD",'yyyymmdd'),TO_DATE("LA$LMSP"."LMSLLD",'yyyymmdd')) / 12)
                                          AS "LoanTermYy"          -- 貸款期間年 DECIMAL 2 
          ,MOD(TRUNC(MONTHS_BETWEEN(TO_DATE("LA$LMSP"."LMSDLD",'yyyymmdd'),TO_DATE("LA$LMSP"."LMSLLD",'yyyymmdd'))),12)
                                          AS "LoanTermMm"          -- 貸款期間月 DECIMAL 2 
          ,TO_DATE("LA$LMSP"."LMSDLD",'yyyymmdd')-ADD_MONTHS(TO_DATE("LA$LMSP"."LMSLLD",'yyyymmdd'),TRUNC(MONTHS_BETWEEN(TO_DATE("LA$LMSP"."LMSDLD",'yyyymmdd'),TO_DATE("LA$LMSP"."LMSLLD",'yyyymmdd'))))
                                          AS "LoanTermDd"          -- 貸款期間日 DECIMAL 3 
          ,"LA$LMSP"."LMSDLD"             AS "MaturityDate"        -- 到期日 DECIMALD 8 
          ,CASE
             WHEN "LA$LMSP"."ACTACT" = '310' THEN '1' -- 以日計息
             WHEN "LA$LMSP"."TRXJAC" = 1     THEN '1' -- 以日計息 
           ELSE '2' END --以月計息
                                          AS "IntCalcCode"
          ,"LA$LMSP"."LMSRTP"             AS "AmortizedCode"       -- 攤還方式 VARCHAR2 1 
          ,2                              AS "FreqBase"            -- 週期基準 DECIMAL 1 
          ,"LA$LMSP"."LMSISC"             AS "PayIntFreq"          -- 繳息週期 DECIMAL 2 
          ,"LA$LMSP"."LMSPSC"             AS "RepayFreq"           -- 還本週期 DECIMAL 2 
          ,"LA$LMSP"."LMSTPR"             AS "TotalPeriod"         -- 總期數 DECIMAL 3 
          ,CASE
             WHEN "LA$LMSP"."LMSPPR" > "LA$LMSP"."LMSGCC" THEN "LA$LMSP"."LMSPPR" - "LA$LMSP"."LMSGCC"
           ELSE 0 END                     AS "RepaidPeriod"        -- 已還本期數 DECIMAL 3 
          ,"LA$LMSP"."LMSPPR"             AS "PaidTerms"           -- 已繳息期數 DECIMAL 3 
          ,"LA$LMSP"."LMSLPD"             AS "PrevPayIntDate"      -- 上次繳息日,繳息迄日 DECIMALD 8 
          ,"LA$LMSP"."LMSFLD"             AS "PrevRepaidDate"      -- 上次還本日,最後還本日 DECIMALD 8 
          ,CASE
             -- 若 到期取息者
             WHEN "LA$LMSP"."LMSRTP" = 2
             -- 以 到期日 為 下次繳息日
             THEN "LA$LMSP"."LMSDLD"
             -- 第一期特殊處理
             -- 若 戶況為0 且 上次繳息日 = 撥款日期
             WHEN "LA$LMSP"."LMSSTS" = 0 AND "LA$LMSP"."LMSLPD" = "LA$LMSP"."LMSLLD"
             -- 以 首次應還日 為 下次繳息日
             THEN "LA$LMSP"."LMSPBD"
           -- 一般處理
           -- 上次繳息日 + 1個繳息周期 (舊資料都是1個月)
           -- TO_NUMBER(TO_CHAR(ADD_MONTHS(TO_DATE("LA$LMSP"."LMSLPD",'yyyymmdd'),1),'yyyymmdd'))
           ELSE CASE
                  -- 2021-04-22 智偉修改 FROM 賴桑口說 下次繳息日大於到期日時,不以到期日為基準,仍使用下次繳息日
                  -- -- 若 計算結果 大於 到期日 
                  -- WHEN TO_NUMBER(TO_CHAR(ADD_MONTHS(TO_DATE("LA$LMSP"."LMSLPD",'yyyymmdd'),1),'yyyymmdd')) > "LA$LMSP"."LMSDLD"
                  -- -- 以 到期日 為 下次繳息日
                  -- THEN "LA$LMSP"."LMSDLD"
                  -- 若計算結果的最後兩碼 大於 指定應繳日(2碼)
                  WHEN SUBSTR(TO_CHAR(ADD_MONTHS(TO_DATE("LA$LMSP"."LMSLPD",'yyyymmdd'),1),'yyyymmdd'),-2) > LPAD("LA$LMSP"."LMSPDY",2,'0')
                  -- 重組下次繳息日
                  THEN TO_NUMBER(SUBSTR(TO_CHAR(ADD_MONTHS(TO_DATE("LA$LMSP"."LMSLPD",'yyyymmdd'),1),'yyyymmdd'),0,6) || LPAD("LA$LMSP"."LMSPDY",2,'0'))
                ELSE TO_NUMBER(TO_CHAR(ADD_MONTHS(TO_DATE("LA$LMSP"."LMSLPD",'yyyymmdd'),1),'yyyymmdd')) END
           END                            AS "NextPayIntDate"      -- 下次繳息日,應繳息日 DECIMALD 8 
          ,"LA$LMSP"."LMSNPD"             AS "NextRepayDate"       -- 下次還本日,應還本日 DECIMALD 8 
          ,"LA$LMSP"."LMSPPA"             AS "DueAmt"              -- 每期攤還金額 DECIMAL 16 2
          ,"LA$LMSP"."LMSGCC"             AS "GracePeriod"         -- 寬限期 DECIMAL 3 
          ,"LA$LMSP"."LMSGPD"             AS "GraceDate"           -- 寬限到期日 DECIMALD 8 
          ,CASE
             WHEN "LA$LMSP"."LMSRTP" = 2 THEN 0 --到期取息者,沒有指定應繳日
           ELSE "LA$LMSP"."LMSPDY" END    AS "SpecificDd"          -- 指定應繳日 DECIMAL 2 
          ,CASE
             -- 2021-04-16 智偉修改 : 應繳日 >= 首次應繳日末兩碼 時, 取前一月的應繳日
             WHEN LPAD("LA$LMSP"."LMSPDY",2,'0') >= SUBSTR(TO_CHAR("LA$LMSP"."LMSPBD"),-2)
             THEN CASE
                    -- 若 前一月的應繳日 大於 前一月的月底日 , 則取前一月的月底日
                    WHEN SUBSTR(TO_CHAR(ADD_MONTHS(TO_DATE("LA$LMSP"."LMSPBD",'yyyymmdd'),-1),'yyyymmdd'),0,6) || LPAD("LA$LMSP"."LMSPDY",2,'0')
                         > TO_CHAR(LAST_DAY(ADD_MONTHS(TO_DATE("LA$LMSP"."LMSPBD",'yyyymmdd'),-1)),'yyyymmdd')
                    THEN TO_NUMBER(TO_CHAR(LAST_DAY(ADD_MONTHS(TO_DATE("LA$LMSP"."LMSPBD",'yyyymmdd'),-1)),'yyyymmdd'))
                  -- 否則 取前一月的應繳日
                  ELSE TO_NUMBER(SUBSTR(TO_CHAR(ADD_MONTHS(TO_DATE("LA$LMSP"."LMSPBD",'yyyymmdd'),-1),'yyyymmdd'),0,6) || LPAD("LA$LMSP"."LMSPDY",2,'0'))
                  END
           -- 否則 取同月的應繳日 (應繳日 小於 首次應繳日末兩碼時 , 組合後不會有日期不合法的情形)
           ELSE TO_NUMBER(SUBSTR(TO_CHAR("LA$LMSP"."LMSPBD"),0,6) || LPAD("LA$LMSP"."LMSPDY",2,'0'))
           END                            AS "SpecificDate"        -- 指定基準日期 DECIMALD 8 
          ,"LA$LMSP"."LMSPBD"
           --"Fn_L3GetDate"("LA$LMSP"."LMSLLD","LA$LMSP"."LMSDLD","LA$LMSP"."LMSPDY","LA$LMSP"."LMSRTP",0)
                                          AS "FirstDueDate"        -- 首次應繳日 DECIMALD 8 
          ,"LA$LMSP"."LMSFSD"             AS "FirstAdjRateDate"    -- 首次利率調整日期 DECIMALD 8 
          ,CASE
             -- 抓 "LA$IRTP" 的利率為 5 且 生效日 > 本月
             -- 把該筆的 IRTADT 生效日寫到"下次利率調整日期"
             -- 2020-12-25 依據賴桑mail 1.機動利率 5% 不轉下次利率調整日
             -- 將此行取消
             --  WHEN "LA$LMSP"."IRTMSC" = 0 AND NVL(LI."IRTADT",0) > 0 THEN NVL(LI."IRTADT",0)
             -- 有首次利率調整日期 且 下次利率調整日期為0者 放首次利率調整日期
             WHEN "LA$LMSP"."LMSFSD" > 0 AND "LA$LMSP"."LMSNSD" = 0 THEN "LA$LMSP"."LMSFSD"
           ELSE "LA$LMSP"."LMSNSD" END    AS "NextAdjRateDate"     -- 下次利率調整日期 DECIMALD 8 
          ,0                              AS "AcctFee"             -- 帳管費 DECIMAL 16 2
          ,0                              AS "FinalBal"            -- 最後一期本金餘額 DECIMAL 16 2
          ,'N'                            AS "NotYetFlag"          -- 未齊件 VARCHAR2 1 
          /* 2021-03-19 智偉修改:原欄位值為0、1，新系統為Y/N */
          ,CASE
             WHEN "LA$LMSP"."LMSNEW" = '1'
             THEN 'Y' 
           ELSE 'N' END                   AS "RenewFlag"           -- 借新還舊 VARCHAR2 1 
          ,NVL(APLP."CASCDE",' ')         AS "PieceCode"           -- 計件代碼 VARCHAR2 1 
          ,''                             AS "PieceCodeSecond"
          ,0                              AS "PieceCodeSecondAmt"
          ,LPAD(APLP."APLUSG",2,'0')      AS "UsageCode"           -- 資金用途別 VARCHAR2 2 
          ,0                              AS "SyndNo"              -- 聯貸案序號 DECIMAL 3 
          ,''                             AS "RelationCode"        -- 與借款人關係 VARCHAR2 2 
          ,"LA$LMSP"."LMSPAN"             AS "RelationName"        -- 第三人帳戶戶名 NVARCHAR2 100 
          ,"LA$LMSP"."LMSPID"             AS "RelationId"          -- 第三人身份證字號 VARCHAR2 10 
          ,0                              AS "RelationBirthday"    -- 第三人生日 DECIMALD 8 
          ,''                             AS "RelationGender"      -- 第三人性別 VARCHAR2 1 
          ,0                              AS "ActFg"               -- 交易進行記號 DECIMAL 1 
          ,"LA$LMSP"."LMSDAT"             AS "LastEntDy"           -- 上次交易日 DECIMALD 8 
          ,''                             AS "LastKinbr"           -- 上次交易行別 VARCHAR2 4 
          ,''                             AS "LastTlrNo"           -- 上次櫃員編號 VARCHAR2 6 
          ,"LA$LMSP"."LMSNMT"             AS "LastTxtNo"           -- 上次交易序號 VARCHAR2 8 
          ,EXG."BankCode"                 AS "RemitBank"           -- 匯款銀行 VARCHAR2 3 
          ,EXG."BranchCode"               AS "RemitBranch"         -- 匯款分行 VARCHAR2 4 
          ,NVL(EXG."EXGACN",0)            AS "RemitAcctNo"         -- 匯款帳號 DECIMAL 14 
          ,"LA$LMSP"."LMSPYC"             AS "CompensateAcct"      -- 代償專戶 NVARCHAR2 60 
          ,EXG."EXGBBC"                   AS "PaymentBank"         -- 解付單位代號 VARCHAR2 7 
          ,"LN$CLMP"."M24070"             AS "Remark"              -- 附言 NVARCHAR2 40 
          ,0                              AS "AcDate"              -- 會計日期 DECIMALD 8 
          ,0                              AS "NextAcDate"          -- 次日交易會計日期 DECIMALD 8 
          ,'0000'                         AS "BranchNo"
          ,JOB_START_TIME                 AS "CreateDate"          -- 建檔日期時間 DATE  
          ,'999999'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 
          ,JOB_START_TIME                 AS "LastUpdate"          -- 最後更新日期時間 DATE  
          ,'999999'                       AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 
          ,0                              AS "HandlingFee"         -- 手續費 DECIMAL 16 2
    FROM "LA$LMSP"
    LEFT JOIN "LN$CLMP" ON "LN$CLMP"."LMSACN" = "LA$LMSP"."LMSACN"
                       AND "LN$CLMP"."LMSAPN" = "LA$LMSP"."LMSAPN"
                       AND "LN$CLMP"."LMSASQ" = "LA$LMSP"."LMSASQ"
    LEFT JOIN (SELECT DISTINCT
                      S1."LMSACN"
                     ,S1."LMSAPN1" -- 原額度號碼
                     ,S1."LMSASQ1" -- 原撥款序號
               FROM (SELECT "LMSACN"
                           ,"LMSAPN" -- 新額度號碼
                           ,"LMSASQ" -- 新撥款序號
                           ,"LMSAPN1" -- 原額度號碼
                           ,"LMSASQ1" -- 原撥款序號
                           ,ROW_NUMBER() OVER (PARTITION BY "LMSACN","LMSAPN","LMSASQ"
                                               ORDER BY "LMSAPN1","LMSASQ1") AS "Seq"
                     FROM "LNACNP"
                     GROUP BY "LMSACN"
                             ,"LMSAPN"
                             ,"LMSASQ"
                             ,"LMSAPN1"
                             ,"LMSASQ1") S1
               LEFT JOIN "LA$LMSP" S2 ON S2."LMSACN" = S1."LMSACN"
                                     AND S2."LMSAPN" = S1."LMSAPN"
                                     AND S2."LMSASQ" = S1."LMSASQ"
               WHERE S2."LMSSTS" <> 3 -- 若展期後之新額度亦為結案,則結案
              ) T1 ON T1."LMSACN" = "LA$LMSP"."LMSACN"
                  AND T1."LMSAPN1" = "LA$LMSP"."LMSAPN"
                  AND T1."LMSASQ1" = "LA$LMSP"."LMSASQ"
    LEFT JOIN "LA$APLP" APLP
               ON APLP."LMSACN" = "LA$LMSP"."LMSACN"
              AND APLP."LMSAPN" = "LA$LMSP"."LMSAPN"
    LEFT JOIN (SELECT "LMSACN"
                     ,"LMSAPN"
                     ,"LMSASQ"
                     ,"ASCRAT"
                     ,ROW_NUMBER() OVER (PARTITION BY "LMSACN","LMSAPN" ORDER BY "ASCADT" DESC) AS "SEQ"
               FROM "LA$ASCP"
               WHERE "ASCADT" <= "TbsDyF"
               ) A1
               ON A1."LMSACN" = "LA$LMSP"."LMSACN"
              AND A1."LMSAPN" = "LA$LMSP"."LMSAPN"
              AND A1."LMSASQ" = "LA$LMSP"."LMSASQ"
              AND A1."SEQ" = 1
    LEFT JOIN  "LA$IRTP" LI ON LI."LMSACN" = "LA$LMSP"."LMSACN"
                           AND LI."LMSAPN" = "LA$LMSP"."LMSAPN"
                           AND LI."LMSASQ" = "LA$LMSP"."LMSASQ"
                           AND LI."IRTRAT" = 5
                           AND LI."IRTADT" >= "LA$LMSP"."LMSLPD"
                           AND "LA$LMSP"."LMSSTS" <> 3
    LEFT JOIN (SELECT EX."LMSACN"
                     ,EX."LMSAPN"
                     ,EX."LMSASQ"
                     ,EX."EXGCDE"
                     ,CB."BankCode"
                     ,CB."BranchCode"
                     ,CASE
                        WHEN REGEXP_LIKE(EX."EXGACN",'^[0-9]+$')
                        THEN TO_NUMBER(EX."EXGACN")
                      ELSE 0 END AS "EXGACN" -- 匯款帳號
                     ,LPAD(EX."EXGBBC",7,'0') AS "EXGBBC"
                     ,ROW_NUMBER() OVER (PARTITION BY EX."LMSACN",EX."LMSAPN",EX."LMSASQ" ORDER BY EX."TRXDAT",EX."TRXNMT",EX."EXGASQ") AS "SEQ"
               FROM "DAT_LA$EXGP" EX
               LEFT JOIN "CdBank" CB ON CB."BankCode" || CB."BranchCode"  = LPAD(EX."EXGBBC",7,'0')
               WHERE NVL("TRXCRC",0) = 0 
              ) EXG ON EXG."LMSACN" = "LA$LMSP"."LMSACN"
                   AND EXG."LMSAPN" = "LA$LMSP"."LMSAPN"
                   AND EXG."LMSASQ" = "LA$LMSP"."LMSASQ"
                   AND EXG."SEQ" = 1
    LEFT JOIN (SELECT LM."LMSACN"
                     ,LM."LMSAPN"
                     ,LM."LMSASQ"
                     ,IRTP."IRTRAT"
                     ,ROW_NUMBER() OVER (PARTITION BY LM."LMSACN"
                                                     ,LM."LMSAPN"
                                                     ,LM."LMSASQ"
                                         ORDER BY IRTP."IRTADT" DESC) AS "Seq"
               FROM "LA$LMSP" LM
               LEFT JOIN "LA$IRTP" IRTP ON IRTP."LMSACN" = LM."LMSACN"
                                       AND IRTP."LMSAPN" = LM."LMSAPN"
                                       AND IRTP."LMSASQ" = LM."LMSASQ"
                                       AND IRTP."IRTADT" <= LM."LMSLPD" -- 上繳日
              ) IRTP ON IRTP."LMSACN" = "LA$LMSP"."LMSACN"
                    AND IRTP."LMSAPN" = "LA$LMSP"."LMSAPN"
                    AND IRTP."LMSASQ" = "LA$LMSP"."LMSASQ"
                    AND IRTP."Seq" = 1
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
