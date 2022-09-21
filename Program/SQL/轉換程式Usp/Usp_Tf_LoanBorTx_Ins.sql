--------------------------------------------------------
--  DDL for Procedure Usp_Tf_LoanBorTx_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE EDITIONABLE PROCEDURE "Usp_Tf_LoanBorTx_Ins" 
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
    EXECUTE IMMEDIATE 'ALTER TABLE "CountLA$TRXP" DISABLE PRIMARY KEY CASCADE';
    EXECUTE IMMEDIATE 'TRUNCATE TABLE "CountLA$TRXP" DROP STORAGE';
    EXECUTE IMMEDIATE 'ALTER TABLE "CountLA$TRXP" ENABLE PRIMARY KEY';

    -- 刪除舊資料
    EXECUTE IMMEDIATE 'ALTER TABLE "LoanBorTx" DISABLE PRIMARY KEY CASCADE';
    EXECUTE IMMEDIATE 'TRUNCATE TABLE "LoanBorTx" DROP STORAGE';
    EXECUTE IMMEDIATE 'ALTER TABLE "LoanBorTx" ENABLE PRIMARY KEY';

    INSERT INTO "CountLA$TRXP"
    SELECT S1."CUSBRH"
          ,S1."TRXDAT"
          ,S1."TRXNMT"
          ,S1."TRXNM2"
          ,S1."TRXTRN"
          ,S1."SEQ"
    FROM (SELECT "CUSBRH"
                ,"TRXDAT"
                ,"TRXNMT"
                ,"TRXNM2"
                ,"TRXTRN"
                ,ROW_NUMBER() OVER (PARTITION BY "CUSBRH"
                                                ,"TRXDAT"
                                                ,"TRXNMT"
                                                ,"TRXNM2"
                                    ORDER BY CASE
                                                WHEN "TRXTRN" IN ('3066') THEN 1
                                                WHEN "TRXTRN" IN ('3025','3031','3041','3084','3086','3088') THEN 2
                                                WHEN "TRXTRN" = '3085' THEN 3
                                                WHEN "TRXTRN" = '3080' THEN 4
                                                WHEN "TRXTRN" = '3081' THEN 5
                                                WHEN "TRXTRN" IN ('3036','3082','3083') THEN 6
                                              ELSE 9 END
                                   ) AS "SEQ"
          FROM "LA$TRXP"
         ) S1
    WHERE S1."SEQ" = 1
    ;

    -- 寫入資料
    INSERT INTO "LoanBorTx"
    WITH "TmpLMSP" AS (
      SELECT "LMSACN"
           , "LMSAPN"
           , "LMSASQ"
           , "LMSLLD"
      FROM "LA$LMSP"
      WHERE "LMSLLD" > "TbsDyF"
    )
    , TR AS (
      SELECT S0."CUSBRH"
            ,S0."TRXDAT"
            ,S0."TRXNMT"
            ,S0."TRXNM2"
            ,S0."TRXTRN"
            ,SUM(CASE
                   WHEN S1."TRXTRN" IN ('3037','3083')
                   THEN 0 - S1."TRXAMT" -- 2022-04-11 修改: 暫收款退還、轉出時,交易金額改為負
                   WHEN S1."TRXTRN" IN '3025'
                        AND S1."TRXTCT" = '2'
                   THEN 0 - S1."TRXAMT" -- 2022-04-11 修改: 轉催金額
                 ELSE S1."TRXAMT" END )     AS "TRXAMT" -- 計算交易總金額
            ,SUM(CASE
                   WHEN S1."TRXTRN" = '3031' THEN S1."TRXAMT" -- 3031:回收登錄
                   WHEN S1."TRXTRN" = '3041' THEN S1."TRXAMT" -- 3041:結案登錄
                   WHEN S1."TRXTRN" = '3084' THEN S1."TRXAMT" -- 3084:部分償還本金 -- 2021-12-23賴桑要求增加
                 ELSE 0 END)                AS "Principal"           -- 本金 DECIMAL 16 2
            ,SUM(CASE
                   WHEN S1."TRXTRN" = '3085' THEN S1."TRXAMT" -- 3085:回收利息
                 ELSE 0 END)                AS "Interest"            -- 利息 DECIMAL 16 2
            ,SUM(CASE
                   WHEN S1."TRXTRN" = '3080' THEN S1."TRXAMT" -- 3080:違約金(1)
                 ELSE 0 END)                AS "DelayInt"            -- 延滯息 NUMBER(16,2)
            ,SUM(CASE
                   WHEN S1."TRXTRN" = '3081' THEN S1."TRXAMT" -- 3081:違約金(2)
                 ELSE 0 END)                AS "BreachAmt"           -- 違約金 DECIMAL 16 2
            ,SUM(CASE
                   WHEN S1."TRXTRN" = '3037' THEN S1."TRXAMT" -- 3037:暫收款退還
                   WHEN S1."TRXTRN" = '3083' THEN S1."TRXAMT" -- 3083:暫收款轉出
                 ELSE 0 END)                AS "TempAmt"             -- 暫收款 DECIMAL 16 2
            ,SUM(CASE
                   WHEN S1."TRXTRN" = '3084' THEN S1."TRXAMT" -- 3084:部分償還本金 --原本放"LA$TRXP"."TRXPRA" ,但會有其他交易代號也在TRXPRA擺值
                 ELSE 0 END)                AS "ExtraRepay"          -- 部分償還本金 DECIMAL 16 2
            ,SUM(CASE
                   WHEN S1."TRXTRN" = '3033' THEN S1."TRXAMT" -- 3033:其他費用登錄
                   WHEN S1."TRXTRN" = '3036' THEN S1."TRXAMT" -- 3036:暫收款登錄
                   WHEN S1."TRXTRN" = '3082' THEN S1."TRXAMT" -- 3082:暫付所得稅
                   WHEN S1."TRXTRN" = '3088' THEN S1."TRXAMT" -- 3088:支票兌現
                 ELSE 0 END)                AS "Overflow"             -- 暫收款 DECIMAL 16 2
      FROM "CountLA$TRXP" S0
      LEFT JOIN "LA$TRXP" S1 ON S1."CUSBRH" = S0."CUSBRH"
                            AND S1."TRXDAT" = S0."TRXDAT"
                            AND S1."TRXNMT" = S0."TRXNMT"
                            AND S1."TRXNM2" = S0."TRXNM2"
      WHERE S1."TRXTRN" IS NOT NULL
      GROUP BY S0."CUSBRH"
              ,S0."TRXDAT"
              ,S0."TRXNMT"
              ,S0."TRXNM2"
              ,S0."TRXTRN"
    )
    , TRXP_3037 AS (
      SELECT DISTINCT
             TRXDAT
           , TRXNMT
           , TRXAMT
      FROM LA$TRXP
      WHERE TRXTRN = '3037'
    )
    , JL AS (
      SELECT T.TRXDAT
           , T.TRXNMT
           , T.TRXAMT
           , J.JLNAMT
           , CAC."AcctCode"
           , ROW_NUMBER()
             OVER (
              PARTITION BY T.TRXDAT
                         , T.TRXNMT
              ORDER BY T.TRXAMT
             ) AS "Seq"
      FROM TRXP_3037 T
      LEFT JOIN LA$JLNP J ON J.TRXDAT = T.TRXDAT
                         AND J.TRXNMT = T.TRXNMT
      LEFT JOIN TB$LCDP L ON L.ACNACC = J.ACNACC
                         AND NVL(L.ACNACS,' ') = NVL(J.ACNACS,' ')
                         AND NVL(L.ACNASS,' ') = NVL(J.ACNASS,' ')
      LEFT JOIN "CdAcCode" CAC ON CAC."AcNoCodeOld" = L.CORACC
                              AND CAC."AcSubCode" = NVL(L.CORACS,'     ')
                              AND CAC."AcDtlCode" = CASE
                                                      WHEN L.CORACC IN ('40903300','20232020','20232182','20232180','20232181','40907400')
                                                           AND NVL(L.CORACS,'     ') = '     '
                                                      THEN '01'
                                                    ELSE '  ' END
      WHERE NVL(J.TRXTRN,' ') != '3037'
        AND T.TRXAMT = NVL(J.JLNAMT,0)
        AND CAC."AcctCode" IN ('F10','F29','TMI','F07')
    )
    SELECT TR1."LMSACN"                   AS "CustNo"              -- 借款人戶號 DECIMAL 7 
          ,TR1."LMSAPN"                   AS "FacmNo"              -- 額度編號 DECIMAL 3 
          ,CASE
             WHEN NVL(TL."LMSLLD",0) > "TbsDyF" THEN 900 + TR1."LMSASQ" -- 撥款日期>轉換日時,為預約撥款,撥款序號 + 900
           ELSE TR1."LMSASQ" END          AS "BormNo"              -- 撥款序號 DECIMAL 3 
          ,ROW_NUMBER() OVER (PARTITION BY TR1."LMSACN"
                                          ,TR1."LMSAPN"
                                          ,TR1."LMSASQ"
                              ORDER BY TR1."CUSBRH"
                                      ,TR1."TRXDAT"
                                      ,TR1."TRXNMT"
                                      ,TR1."TRXNM2"
                                      ,TR1."TRXTRN"
                                      ,TR1."TRXTDT"
                                      ,TR1."TRXTIM")
                                          AS "BorxNo"              -- 交易內容檔序號 DECIMAL 4 
          ,TR1."TRXTDT"                   AS "TitaCalDy"           -- 交易日期 DECIMALD 8 
          ,TR1."TRXTIM"                   AS "TitaCalTm"           -- 交易時間 DECIMAL 8 
          ,'0000'                         AS "TitaKinBr"           -- 單位別 VARCHAR2 4
          ,NVL(AEM1."EmpNo",'999999')     AS "TitaTlrNo"           -- 經辦 VARCHAR2 6 
	        -- "TRXNMT" NUMBER(7,0), 目前最大20519
	        -- "TRXNM2" NUMBER(3,0), 目前最大72
          -- 左補零,總長度8
          -- 2021-11-30 修改 只紀錄TRXNMT
          ,LPAD(TR1."TRXNMT",8,0)         AS "TitaTxtNo"           -- 交易序號 VARCHAR2 8 
          ,TR1."TRXTRN"                   AS "TitaTxCd"            -- 交易代號 VARCHAR2 5 
          ,''                             AS "TitaCrDb"            -- 借貸別 VARCHAR2 1 
          ,TR1."TRXCRC"                   AS "TitaHCode"           -- 訂正別 VARCHAR2 1 
          ,'TWD'                          AS "TitaCurCd"           -- 幣別 VARCHAR2 3 
          ,NVL(AEM2."EmpNo",TR1."TRXSID") AS "TitaEmpNoS"          -- 主管編號 VARCHAR2 6 
          ,CASE TR1."TRXSAK"
             WHEN 0 THEN 9
             WHEN 1 THEN 1
             WHEN 2 THEN 2
             WHEN 3 THEN 3
             WHEN 4 THEN 4
             WHEN 5 THEN 9
             WHEN 6 THEN 9
             WHEN 7 THEN 9
             WHEN 8 THEN 9
             WHEN 9 THEN 9
           ELSE 9 END                     AS "RepayCode"           -- 還款來源 DECIMAL 2
          ,REPLACE(TRIM(TO_SINGLE_BYTE(TCD."TRXDSC")),'','') 
                                          AS "Desc"                -- 摘要 NVARCHAR2 10 
          ,TR1."TRXDAT"                   AS "AcDate"              -- 會計日期 DECIMALD 8 
          ,TR1."TRXEDT" || '0000' || '      ' || LPAD(TR1."TRXENM",8,'0')
                                          AS "CorrectSeq"          -- 更正序號, 原交易序號 VARCHAR2 26 
          ,'Y'                            AS "Displayflag"         -- 查詢時顯示否 VARCHAR2 1 
          ,CASE
             WHEN TR1."TRXIDT" > 20400101
             THEN TR1."TRXDAT"
           ELSE TR1."TRXIDT" END          AS "EntryDate"           -- 入帳日期 DECIMALD 8 
          ,TR1."TRXIED"                   AS "DueDate"             -- 應繳日期 DECIMALD 8 
          -- 2021-11-30 智偉增加邏輯(from賴桑):訂正別為1、3時，將金額反向
          ,CASE
             WHEN TR1."TRXCRC" IN ('1','3')
             THEN 0 - TR."TRXAMT"
           ELSE TR."TRXAMT"
           END
           -- 2022-08-11 智偉增加 from 賴桑 : 交易金額不含費用
           +
           CASE
             WHEN TR1."TRXCRC" IN ('1','3')
             THEN 0 - NVL(JL."JLNAMT",0)
           ELSE NVL(JL."JLNAMT",0)
           END                            AS "TxAmt"               -- 交易金額 DECIMAL 16 2
          ,TR1."LMSLBL"                   AS "LoanBal"             -- 放款餘額 DECIMAL 16 2
          ,TR1."TRXISD"                   AS "IntStartDate"        -- 計息起日 DECIMALD 8 
          ,TR1."TRXIED"                   AS "IntEndDate"          -- 計息迄日 DECIMALD 8 
          ,TR1."TRXPRD"                   AS "RepaidPeriod"        -- 回收期數 DECIMAL 3
          ,NVL(RC."FitRate",0)            AS "Rate"                -- 利率 DECIMAL 6 4
          ,CASE
             WHEN TR1."TRXCRC" IN ('1','3')
             THEN 0 - TR."Principal"
           ELSE TR."Principal"
           END                            AS "Principal"           -- 本金 DECIMAL 16 2
          ,CASE
             WHEN TR1."TRXCRC" IN ('1','3')
             THEN 0 - TR."Interest"
           ELSE TR."Interest"
           END                            AS "Interest"            -- 利息 DECIMAL 16 2
          ,CASE
             WHEN TR1."TRXCRC" IN ('1','3')
             THEN 0 - TR."DelayInt"
           ELSE TR."DelayInt"
           END                            AS "DelayInt"            -- 延滯息 NUMBER(16,2)
          ,CASE
             WHEN TR1."TRXCRC" IN ('1','3')
             THEN 0 - TR."BreachAmt"
           ELSE TR."BreachAmt"
           END                            AS "BreachAmt"           -- 違約金 DECIMAL 16 2
          ,0                              AS "CloseBreachAmt"      -- 清償違約金 DECIMAL 16 2
          ,CASE
             WHEN TR1."TRXCRC" IN ('1','3')
             THEN 0 - TR."TempAmt"
           ELSE TR."TempAmt"
           END                            AS "TempAmt"             -- 暫收款 DECIMAL 16 2
          ,CASE
             WHEN TR1."TRXCRC" IN ('1','3')
             THEN 0 - TR."ExtraRepay"
           ELSE TR."ExtraRepay"
           END                            AS "ExtraRepay"          -- 部分償還本金 DECIMAL 16 2
          ,TR1."TRXLIN"                   AS "UnpaidInterest"      -- 欠繳利息 DECIMAL 16 2
          ,TR1."TRXLPN"                   AS "UnpaidPrincipal"     -- 欠繳本金 DECIMAL 16 2
          ,TR1."TRXLBC"                   AS "UnpaidCloseBreach"   -- 欠繳違約金 DECIMAL 16 2
          ,0                              AS "Shortfall"           -- 短收 DECIMAL 16 2
          ,CASE
             WHEN TR1."TRXCRC" IN ('1','3')
             THEN 0 - TR."Overflow"
           ELSE TR."Overflow"
           END                            AS "Overflow"            -- 溢收 DECIMAL 16 2
          ,'{'
          || '"TRXNM2":"'
          || TR1."TRXNM2"
          || '"'
          -- 結案區分
          || CASE
               WHEN TR1."TRXTCT" IS NOT NULL
               THEN ',"CaseCloseCode":"'
                    || CASE
                         WHEN TR1."TRXTCT" = '1' AND ACN."IsSameFac" = 1
                                                 THEN '2'
                         WHEN TR1."TRXTCT" = '1' THEN '1'
                         WHEN TR1."TRXTCT" = '2' THEN '3'
                         WHEN TR1."TRXTCT" = '3' THEN '4'
                         WHEN TR1."TRXTCT" = '4' THEN '5'
                         WHEN TR1."TRXTCT" = '5' THEN '6'
                         WHEN TR1."TRXTCT" = '6' THEN '7'
                       ELSE TR1."TRXTCT" END
                    || '"' -- 結案區分
             ELSE '' END
           || CASE
                WHEN JL."AcctCode" = 'F10' -- 實收帳管費
                THEN ',"AcctFee":"' 
                     || CASE
                          WHEN TR1."TRXCRC" IN ('1','3')
                          THEN TO_CHAR(0 - JL."JLNAMT")
                        ELSE TO_CHAR(JL."JLNAMT")
                        END
                     || '"'
                WHEN JL."AcctCode" = 'F29' -- 實收契變手續費
                THEN ',"ModifyFee":"' 
                     || CASE
                          WHEN TR1."TRXCRC" IN ('1','3')
                          THEN TO_CHAR(0 - JL."JLNAMT")
                        ELSE TO_CHAR(JL."JLNAMT")
                        END
                     || '"'
                WHEN JL."AcctCode" = 'TMI' -- 實收火險保費 
                THEN ',"FireFee":"' 
                     || CASE
                          WHEN TR1."TRXCRC" IN ('1','3')
                          THEN TO_CHAR(0 - JL."JLNAMT")
                        ELSE TO_CHAR(JL."JLNAMT")
                        END
                     || '"'
                WHEN JL."AcctCode" = 'F07' -- 實收法拍費用
                THEN ',"LawFee":"' 
                     || CASE
                          WHEN TR1."TRXCRC" IN ('1','3')
                          THEN TO_CHAR(0 - JL."JLNAMT")
                        ELSE TO_CHAR(JL."JLNAMT")
                        END
                     || '"'
              ELSE '' END
           || CASE -- 減免金額
                WHEN TR1."TRXCRC" IN ('1','3')
                     AND TR1."TRXDAM" != 0
                THEN ',"ReduceAmt":"'
                     || TO_CHAR(0 - TR1."TRXDAM")
                     || '"'
                WHEN TR1."TRXDAM" != 0
                THEN ',"ReduceAmt":"'
                     || TO_CHAR(TR1."TRXDAM")
                     || '"'
              ELSE ''
              END
           || CASE --  減免違約金
                WHEN TR1."TRXCRC" IN ('1','3')
                     AND TR1."TRXDBC" != 0
                THEN ',"ReduceBreachAmt":"'
                     || TO_CHAR(0 - TR1."TRXDBC")
                     || '"'
                WHEN TR1."TRXDBC" != 0
                THEN ',"ReduceBreachAmt":"'
                     || TO_CHAR(TR1."TRXDBC")
                     || '"'
              ELSE ''
              END
           || CASE 
                --  免印花稅金額
                WHEN NVL(TO_CHAR(TR1."TRXNTX"),'0') != '0'
                THEN ',"StampFreeAmt":"' 
                     || NVL(TO_CHAR(TR1."TRXNTX"),'0')
                     || '"'
              ELSE ''
              END
           || CASE 
                --  暫收原因
                WHEN LPAD(NVL(TR1."LMSRSN",0),2,'0') != '00'
                THEN ',"TempReasonCode":"' 
                     || LPAD(NVL(TR1."LMSRSN",0),2,'0')
                     || '"'
              ELSE ''
              END
           || '}'                         AS "OtherFields"         -- 其他欄位 VARCHAR2 1000 
          ,JOB_START_TIME                 AS "CreateDate"          -- 建檔日期時間 DATE  
          ,'999999'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 
          ,JOB_START_TIME                 AS "LastUpdate"          -- 最後更新日期時間 DATE  
          ,'999999'                       AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 
          ,CASE
             WHEN TR1."TRXCRC" IN ('1','3')
             THEN 0 - NVL(JL."JLNAMT",0)
           ELSE NVL(JL."JLNAMT",0)
           END                            AS "FeeAmt"
    FROM TR
    LEFT JOIN "LA$TRXP" TR1 ON TR1."CUSBRH" = TR."CUSBRH"
                           AND TR1."TRXDAT" = TR."TRXDAT"
                           AND TR1."TRXNMT" = TR."TRXNMT"
                           AND TR1."TRXNM2" = TR."TRXNM2"
                           AND TR1."TRXTRN" = TR."TRXTRN"
    LEFT JOIN "TmpLMSP" TL ON TL."LMSACN" = TR1."LMSACN"
                          AND TL."LMSAPN" = TR1."LMSAPN"
                          AND TL."LMSASQ" = TR1."LMSASQ"
    LEFT JOIN (SELECT "LMSACN"
                     ,"LMSAPN"
                     ,"LMSASQ"
                     ,"TRXISD"
                     ,NVL(RC."FitRate",LBM."StoreRate") AS "FitRate" -- 適用利率
                     ,ROW_NUMBER() OVER (PARTITION BY TX."LMSACN" 
                                                     ,TX."LMSAPN"
                                                     ,TX."LMSASQ"
                                                     ,TX."TRXISD"
                                         ORDER BY NVL(RC."EffectDate",0) DESC) AS "Seq"
               FROM (
                SELECT DISTINCT
                      "LMSACN"
                     ,"LMSAPN"
                     ,"LMSASQ"
                     ,"TRXISD"
                FROM "LA$TRXP"
                WHERE "TRXISD" > 0
               ) TX
               LEFT JOIN "LoanRateChange" RC ON RC."CustNo" = TX."LMSACN"
                                            AND RC."FacmNo" = TX."LMSAPN"
                                            AND RC."BormNo" = TX."LMSASQ"
                                            AND RC."EffectDate" <= TX."TRXISD"
               LEFT JOIN "LoanBorMain" LBM ON LBM."CustNo" = TX."LMSACN"
                                          AND LBM."FacmNo" = TX."LMSAPN"
                                          AND LBM."BormNo" = TX."LMSASQ"
               WHERE NVL(RC."FitRate",LBM."StoreRate") > 0
              ) RC ON RC."LMSACN" = TR1."LMSACN"
                  AND RC."LMSAPN" = TR1."LMSAPN"
                  AND RC."LMSASQ" = TR1."LMSASQ"
                  AND RC."TRXISD" = TR1."TRXISD"
                  AND RC."Seq" = 1
    LEFT JOIN (
      SELECT "LMSACN"
            ,"LMSAPN1"
            ,"LMSASQ1"
            ,MAX(CASE
                   WHEN "LMSAPN" = "LMSAPN1" THEN 1
                 ELSE 0 END) AS "IsSameFac"
      FROM "LNACNP"
      GROUP BY "LMSACN"
              ,"LMSAPN1"
              ,"LMSASQ1"
    ) ACN ON ACN."LMSACN" = TR1."LMSACN"
         AND ACN."LMSAPN1" = TR1."LMSAPN"
         AND ACN."LMSASQ1" = TR1."LMSASQ"
    LEFT JOIN JL ON JL."TRXDAT" = TR."TRXDAT"
                AND JL."TRXNMT" = TR."TRXNMT"
                AND JL."Seq" = 1
    LEFT JOIN "As400EmpNoMapping" AEM1 ON AEM1."As400TellerNo" = TR1."TRXMEM"
    LEFT JOIN "As400EmpNoMapping" AEM2 ON AEM2."As400TellerNo" = TR1."TRXSID"
    LEFT JOIN "TB$TCDP" TCD ON TCD."TRXTRN" = TR."TRXTRN"
    WHERE TR1."TRXDAT" <= "TbsDyF"
    ;

    -- 記錄寫入筆數
    INS_CNT := INS_CNT + sql%rowcount;

    /* 更新交易代號 */
    UPDATE "LoanBorTx" SET
    "TitaTxCd" = CASE
                   WHEN "TitaTxCd" = '3021' THEN 'L3701'
                   WHEN "TitaTxCd" = '3025' THEN 'L3100' -- D
                   WHEN "TitaTxCd" = '3031' THEN 'L3200' -- C
                   WHEN "TitaTxCd" = '3033' THEN 'L3230' -- 
                   WHEN "TitaTxCd" = '3036' THEN 'L3210'
                   WHEN "TitaTxCd" = '3037' THEN 'L3220'
                   WHEN "TitaTxCd" = '3041' THEN 'L3420'
                   WHEN "TitaTxCd" = '3046' THEN 'L3711'
                   WHEN "TitaTxCd" = '3066' THEN 'L3200'
                   WHEN "TitaTxCd" = '3080' THEN 'L3200'
                   WHEN "TitaTxCd" = '3081' THEN 'L3200'
                   WHEN "TitaTxCd" = '3082' THEN 'L3210'
                   WHEN "TitaTxCd" = '3083' THEN 'L3230'
                   WHEN "TitaTxCd" = '3084' THEN 'L3200'
                   WHEN "TitaTxCd" = '3085' THEN 'L3200'
                   WHEN "TitaTxCd" = '3086' THEN 'L3420'
                   WHEN "TitaTxCd" = '3087' THEN 'L3100'
                   WHEN "TitaTxCd" = '3088' THEN 'L3210'
                   WHEN "TitaTxCd" = '3089' THEN 'L3420'
                 ELSE "TitaTxCd" END 
    ;

    -- 記錄程式結束時間
    JOB_END_TIME := SYSTIMESTAMP;

    commit;

    END;
    -- 例外處理
    Exception
    WHEN OTHERS THEN
    ERROR_MSG := SQLERRM || CHR(13) || CHR(10) || dbms_utility.format_error_backtrace;
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_LoanBorTx_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;



/
