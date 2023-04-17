--------------------------------------------------------
--  DDL for Procedure Usp_Tf_LoanBorTx_Ins
--------------------------------------------------------
set define off;

CREATE OR REPLACE PROCEDURE "Usp_Tf_LoanBorTx_Ins" 
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
    INSERT INTO "LoanBorTx" ( 
           "CustNo"              -- 借款人戶號 DECIMAL 7  
          ,"FacmNo"              -- 額度編號 DECIMAL 3  
          ,"BormNo"              -- 撥款序號 DECIMAL 3  
          ,"BorxNo"              -- 交易內容檔序號 DECIMAL 4  
          ,"TitaCalDy"           -- 交易日期 DECIMALD 8  
          ,"TitaCalTm"           -- 交易時間 DECIMAL 8  
          ,"TitaKinBr"           -- 單位別 VARCHAR2 4 
          ,"TitaTlrNo"           -- 經辦 VARCHAR2 6  
          ,"TitaTxtNo"           -- 交易序號 VARCHAR2 8  
          ,"TitaTxCd"            -- 交易代號 VARCHAR2 5  
          ,"TitaCrDb"            -- 借貸別 VARCHAR2 1  
          ,"TitaHCode"           -- 訂正別 VARCHAR2 1  
          ,"TitaCurCd"           -- 幣別 VARCHAR2 3  
          ,"TitaEmpNoS"          -- 主管編號 VARCHAR2 6  
          ,"RepayCode"           -- 還款來源 DECIMAL 2 
          ,"Desc"                -- 摘要 NVARCHAR2 10  
          ,"AcDate"              -- 會計日期 DECIMALD 8  
          ,"CorrectSeq"          -- 更正序號, 原交易序號 VARCHAR2 26  
          ,"Displayflag"         -- 查詢時顯示否 VARCHAR2 1  
          ,"EntryDate"           -- 入帳日期 DECIMALD 8  
          ,"DueDate"             -- 應繳日期 DECIMALD 8  
          ,"TxAmt"               -- 交易金額 DECIMAL 16 2 
          ,"LoanBal"             -- 放款餘額 DECIMAL 16 2 
          ,"IntStartDate"        -- 計息起日 DECIMALD 8  
          ,"IntEndDate"          -- 計息迄日 DECIMALD 8  
          ,"PaidTerms"           -- 回收期數 DECIMAL 3 
          ,"Rate"                -- 利率 DECIMAL 6 4 
          ,"Principal"           -- 本金 DECIMAL 16 2 
          ,"Interest"            -- 利息 DECIMAL 16 2 
          ,"DelayInt"            -- 延滯息 NUMBER(16,2) 
          ,"BreachAmt"           -- 違約金 DECIMAL 16 2 
          ,"CloseBreachAmt"      -- 清償違約金 DECIMAL 16 2 
          ,"TempAmt"             -- 暫收款 DECIMAL 16 2 
          ,"ExtraRepay"          -- 部分償還本金 DECIMAL 16 2 
          ,"UnpaidInterest"      -- 欠繳利息 DECIMAL 16 2 
          ,"UnpaidPrincipal"     -- 欠繳本金 DECIMAL 16 2 
          ,"UnpaidCloseBreach"   -- 欠繳違約金 DECIMAL 16 2 
          ,"Shortfall"           -- 短收 DECIMAL 16 2 
          ,"Overflow"            -- 溢收 DECIMAL 16 2 
          ,"OtherFields"         -- 其他欄位 VARCHAR2 1000  
          ,"CreateDate"          -- 建檔日期時間 DATE   
          ,"CreateEmpNo"         -- 建檔人員 VARCHAR2 6  
          ,"LastUpdate"          -- 最後更新日期時間 DATE   
          ,"LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6  
          ,"FeeAmt" 
          ,"AcSeq" 
          ,"SlipSumNo" 
          ,"AcctCode" 
          ,"TxDescCode" 
    ) 
    WITH "TmpLMSP" AS ( 
      SELECT "LMSACN" 
           , "LMSAPN" 
           , "LMSASQ" 
           , "LMSLLD" 
      FROM "LA$LMSP" 
--      WHERE "LMSLLD" > "TbsDyF" 
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
                   WHEN S1."TRXTRN" = '3025' 
                        AND S1."TRXTCT" = '2' 
                   THEN 0 -- 2022-04-11 修改: 轉催金額 
                   WHEN S1."TRXTRN" = '3086' 
                        AND S1."TRXTCT" = '6' 
                   THEN 0 -- 2022-10-28 修改: 轉呆金額 
                 ELSE S1."TRXAMT" END
                 +
                 -- 2023-01-30 智偉增加 from 賴桑 : 交易金額不含支票兌現
                 CASE
                   WHEN S1."TRXCRC" IN ('1','3') AND S1."TRXNTX" != 0
                   THEN S1."TRXNTX"
                   WHEN S1."TRXNTX" != 0
                   THEN 0 - S1."TRXNTX"
                 ELSE 0
                 END   
                 )     AS "TRXAMT" -- 計算交易總金額 
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
               --     WHEN S1."TRXTRN" = '3088' THEN S1."TRXAMT" -- 3088:支票兌現 
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
    , JLData AS ( 
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
                                                      WHEN L.CORACC IN ('40903300'
                                                                       ,'20232020'
                                                                       ,'20232182'
                                                                       ,'20232180'
                                                                       ,'20232181'
                                                                       ,'40907400') 
                                                           AND NVL(L.CORACS,'     ') = '     ' 
                                                      THEN '01' 
                                                    ELSE '  ' END 
      WHERE NVL(J.TRXTRN,' ') != '3037' 
        AND T.TRXAMT = NVL(J.JLNAMT,0) 
        AND CAC."AcctCode" IN ('F07' -- 暫付法務費
                              ,'F08' -- 收回呆帳及過期帳
                              ,'TMI' -- 火險保費
                              ,'F09' -- 暫付火險保費
                              ,'F10' -- 帳管費/手續費
                              ,'F12' -- 企金件
                              ,'F13' -- 沖什項收入
                              ,'F14' -- NPL-銷項稅額
                              ,'F15' -- 921貸款戶
                              ,'F16' -- 3200億專案
                              ,'F17' -- 3200億-利變
                              ,'F18' -- 沖備抵呆帳
                              ,'F19' -- 轉債協暫收款
                              ,'F20' -- 轉應付代收
                              ,'F21' -- 88風災
                              ,'F22' -- 88風災-保費
                              ,'F23' -- 3200億傳統A
                              ,'F24' -- 催收款項-法務費用
                              ,'F25' -- 催收款項-火險費用
                              ,'F27' -- 聯貸管理費
                              ,'F29' -- 契變手續費
                              ,'F30' -- 呆帳戶法務費墊付
                              ) 
    ) 
    , JLSeq1 AS ( 
      SELECT TRXDAT 
           , TRXNMT 
           , TRXAMT 
           , JLNAMT 
           , "AcctCode" 
      FROM JLData
      WHERE "Seq" = 1
    ) 
    , correctTx AS ( 
      SELECT DISTINCT 
             TRXDAT 
           , TRXNMT 
           , TRXMEM 
      FROM LA$TRXP 
    ) 
    , AcnpData AS ( 
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
    )
    SELECT TR1."LMSACN"                   AS "CustNo"              -- 借款人戶號 DECIMAL 7  
          ,TR1."LMSAPN"                   AS "FacmNo"              -- 額度編號 DECIMAL 3  
          ,TR1."LMSASQ"                   AS "BormNo"              -- 撥款序號 DECIMAL 3  
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
          ,TR1."TRXTIM" * 100             AS "TitaCalTm"           -- 交易時間 DECIMAL 8  
          ,'0000'                         AS "TitaKinBr"           -- 單位別 VARCHAR2 4 
          ,NVL(AEM1."EmpNo",'999999')     AS "TitaTlrNo"           -- 經辦 VARCHAR2 6  
	        -- "TRXNMT" NUMBER(7,0), 目前最大20519 
	        -- "TRXNM2" NUMBER(3,0), 目前最大72 
          -- 左補零,總長度8 
          -- 2021-11-30 修改 只紀錄TRXNMT 
          ,LPAD(TR1."TRXNMT",8,0)         AS "TitaTxtNo"           -- 交易序號 VARCHAR2 8  
          ,CASE 
             WHEN CASE 
                    WHEN TR1."TRXCRC" IN ('1','3') 
                    THEN 0 - NVL(JL."JLNAMT",0) 
                    ELSE NVL(JL."JLNAMT",0) 
                  END != 0
                  AND TR1."TRXTRN" = 3037
             THEN 'L3230' -- 2023-01-11 Wei 增加此條件 from Lai : FeeAmt>0,且目前轉L3220的,改轉為L3230
           ELSE TR1."TRXTRN" END          AS "TitaTxCd"            -- 交易代號 VARCHAR2 5  
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
          ,TR1."TRXEDT" || '0000' || NVL(AEM3."EmpNo",'999999') || LPAD(TR1."TRXENM",8,'0') 
                                          AS "CorrectSeq"          -- 更正序號, 原交易序號 VARCHAR2 26  
          -- 2022-09-22 賴桑增加邏輯 
          -- A:帳務 = 除了L3701之外都是帳務 
          -- Y:是 = L3701 
          ,CASE 
             WHEN TR1."TRXTRN" = '3021' -- AS400交易代碼3021 = 新系統交易代號L3701 
             THEN 'Y' 
           ELSE 'A' END                   AS "Displayflag"         -- 查詢時顯示否 VARCHAR2 1  
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
          ,TR1."TRXPRD"                   AS "PaidTerms"           -- 回收期數 DECIMAL 3 
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
          ,JSON_OBJECT(
               'TRXNM2' VALUE TO_CHAR(TR1.TRXNM2) ,
               'CaseCloseCode' VALUE TO_CHAR(CASE -- 結案區分
                                       WHEN TR1."TRXTCT" = '1' AND ACN."IsSameFac" = 1 
                                                               THEN '2' 
                                       WHEN TR1."TRXTCT" = '1' THEN '1' 
                                       WHEN TR1."TRXTCT" = '2' THEN '3' 
                                       WHEN TR1."TRXTCT" = '3' THEN '4' 
                                       WHEN TR1."TRXTCT" = '4' THEN '5' 
                                       WHEN TR1."TRXTCT" = '5' THEN '6' 
                                       WHEN TR1."TRXTCT" = '6' THEN '7' 
                                     ELSE TR1."TRXTCT" END) ,
               'AcctFee' VALUE CASE -- 實收帳管費
                                 WHEN JL."AcctCode" = 'F10'
                                      AND TR1."TRXCRC" IN ('1','3') 
                                 THEN TO_CHAR(0 - JL."JLNAMT") 
                                 WHEN JL."AcctCode" = 'F10'
                                 THEN TO_CHAR(JL."JLNAMT") 
                               ELSE NULL END ,
               'ModifyFee' VALUE CASE -- 實收契變手續費
                                 WHEN JL."AcctCode" = 'F29'
                                      AND TR1."TRXCRC" IN ('1','3') 
                                 THEN TO_CHAR(0 - JL."JLNAMT") 
                                 WHEN JL."AcctCode" = 'F29'
                                 THEN TO_CHAR(JL."JLNAMT") 
                               ELSE NULL END ,
               'FireFee' VALUE CASE -- 實收火險保費
                                 WHEN JL."AcctCode" = 'TMI'
                                      AND TR1."TRXCRC" IN ('1','3') 
                                 THEN TO_CHAR(0 - JL."JLNAMT") 
                                 WHEN JL."AcctCode" = 'TMI'
                                 THEN TO_CHAR(JL."JLNAMT") 
                               ELSE NULL END ,
               'LawFee' VALUE CASE -- 實收法拍費用
                                WHEN JL."AcctCode" = 'F07'
                                     AND TR1."TRXCRC" IN ('1','3') 
                                THEN TO_CHAR(0 - JL."JLNAMT") 
                                WHEN JL."AcctCode" = 'F07'
                                THEN TO_CHAR(JL."JLNAMT") 
                              ELSE NULL END ,
               'ReduceAmt' VALUE CASE -- 減免金額
                                   WHEN TR1."TRXDAM" != 0
                                        AND TR1."TRXCRC" IN ('1','3')
                                   THEN TO_CHAR(0 - TR1."TRXDAM") 
                                   WHEN TR1."TRXDAM" != 0 
                                   THEN TO_CHAR(TR1."TRXDAM") 
                                 ELSE NULL END ,
               'ReduceBreachAmt' VALUE CASE -- 減免違約金
                                         WHEN TR1."TRXDBC" != 0
                                              AND TR1."TRXCRC" IN ('1','3')
                                         THEN TO_CHAR(0 - TR1."TRXDBC") 
                                         WHEN TR1."TRXDBC" != 0 
                                         THEN TO_CHAR(TR1."TRXDBC") 
                                       ELSE NULL END ,
               'StampFreeAmt' VALUE CASE -- 免印花稅金額
                                      WHEN NVL(TO_CHAR(TR1."TRXNTX"),'0') != '0'
                                      THEN NVL(TO_CHAR(TR1."TRXNTX"),'0')
                                    ELSE NULL END ,
               'TempReasonCode' VALUE CASE -- 暫收原因
                                        WHEN TR1.TRXTRN IN ('3036','3037','3082','3083')
                                        THEN LPAD(NVL(TR1."LMSRSN",0),2,'0') 
                                      ELSE NULL END ,
               'NewDueAmt' VALUE CASE -- 新每期攤還金額 2022-12-20 Wei新增
                                   WHEN TR1.TRXNPA != 0
                                   THEN TO_CHAR(TR1.TRXNPA)
                                 ELSE NULL END ,
               'NewTotalPeriod' VALUE CASE -- 新繳款總期數 2022-12-20 Wei新增
                                        WHEN TR1.TRXNPR != 0
                                        THEN TO_CHAR(TR1.TRXNPR)
                                      ELSE NULL END ,
               'ChequeAcctNo' VALUE CASE -- 支票帳號 2022-12-20 Wei新增
                                        WHEN TR1.CHKACN != 0
                                        THEN TO_CHAR(TR1.CHKACN)
                                      ELSE NULL END ,
               'ChequeNo' VALUE CASE -- 支票號碼 2022-12-20 Wei新增
                                  WHEN TR1.CHKASQ != 0
                                  THEN TO_CHAR(TR1.CHKASQ)
                                ELSE NULL END ,
               'RemitSeq' VALUE CASE -- 匯款序號 2022-12-20 Wei新增
                                  WHEN TR1.DPSSEQ != 0
                                  THEN TO_CHAR(TR1.DPSSEQ)
                                ELSE NULL END ,
               'AcctDivisionCode' VALUE CASE -- 帳戶區分 2022-12-20 Wei新增
                                          WHEN TR1.TRXACD >= 0
                                          THEN TO_CHAR(TR1.TRXACD)
                                        ELSE NULL END ,
               'RepayBank' VALUE CASE -- 扣款銀行 2022-12-20 Wei新增
                                   WHEN TR1.LMSPBK >= 0
                                   THEN TO_CHAR(TR1.LMSPBK)
                                 ELSE NULL END ,
               'RECPNO' VALUE TO_CHAR(TR1.RECPNO) , -- 收據號碼 2022-12-20 Wei新增
               'PAYCOD' VALUE TO_CHAR(TR1.PAYCOD) , -- 代收繳款方式 2022-12-20 Wei新增
               'Excessive' VALUE TO_CHAR(TR1.TRXAOS) -- 累溢短收 2022-12-20 Wei新增
          )                               AS "OtherFields"         -- 其他欄位 VARCHAR2 1000
          ,JOB_START_TIME                 AS "CreateDate"          -- 建檔日期時間 DATE   
          ,'999999'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6  
          ,JOB_START_TIME                 AS "LastUpdate"          -- 最後更新日期時間 DATE   
          ,'999999'                       AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6  
          ,CASE 
             WHEN TR1."TRXCRC" IN ('1','3') 
             THEN 0 - NVL(JL."JLNAMT",0) 
           ELSE NVL(JL."JLNAMT",0) 
           END                            AS "FeeAmt" 
          ,TR1."TRXNM2" AS "AcSeq" 
          ,TR1."BSTBTN" AS "SlipSumNo" 
          ,CASE
             -- 2022-12-01 Wei from Lai 口頭說要新增
             -- 2023-01-30 Wei from Lai 增加這些科目
             WHEN JL."AcctCode"
                  IN ('F07' -- 暫付法務費
                     ,'F08' -- 收回呆帳及過期帳
                     ,'TMI' -- 火險保費
                     ,'F09' -- 暫付火險保費
                     ,'F10' -- 帳管費/手續費
                     ,'F12' -- 企金件
                     ,'F13' -- 沖什項收入
                     ,'F14' -- NPL-銷項稅額
                     ,'F15' -- 921貸款戶
                     ,'F16' -- 3200億專案
                     ,'F17' -- 3200億-利變
                     ,'F18' -- 沖備抵呆帳
                     ,'F19' -- 轉債協暫收款
                     ,'F20' -- 轉應付代收
                     ,'F21' -- 88風災
                     ,'F22' -- 88風災-保費
                     ,'F23' -- 3200億傳統A
                     ,'F24' -- 催收款項-法務費用
                     ,'F25' -- 催收款項-火險費用
                     ,'F27' -- 聯貸管理費
                     ,'F29' -- 契變手續費
                     ,'F30' -- 呆帳戶法務費墊付
                     )  
             THEN JL."AcctCode"
           ELSE TO_CHAR(TR1."ACTACT") END          AS "AcctCode" 
          /* 更新交易別代碼 */ 
          -- 2022-11-07 Wei 新增 from Lai 寫在LoanBorTx.xlsx 的 交易別 Sheet
          ,CASE 
             WHEN CASE 
                    WHEN TR1."TRXCRC" IN ('1','3') 
                    THEN 0 - NVL(JL."JLNAMT",0) 
                    ELSE NVL(JL."JLNAMT",0) 
                  END != 0
             THEN 'Fee' -- 2023-01-11 Wei 增加此條件 from Lai : FeeAmt>0,TxDescCode轉Fee
             WHEN TR1.TRXTRN='3025'
             THEN '3100'
             WHEN TR1.TRXTRN='3087' AND LBM."RenewFlag"='1'
             THEN '3101'
             WHEN TR1.TRXTRN='3087' AND LBM."RenewFlag"='2'
             THEN '3102'
             WHEN TR1.TRXTRN='3031'
             THEN '3201'
             WHEN TR1.TRXTRN='3085'
             THEN '3202'
             WHEN TR1.TRXTRN='3080'
             THEN '3202'
             WHEN TR1.TRXTRN='3084'
             THEN '3203'
             WHEN TR1.TRXTRN='3066'
             THEN '3204'
             WHEN TR1.TRXTRN='3081'
             THEN '3205'
             -- 2023-04-17 Wei 修改 from 賴桑:債協暫收款是在暫收款退還時用不是在暫收款登錄時用
            --  WHEN TR1.TRXTRN='3036' AND TR1.LMSRSN=0
            --  THEN '3211'
             WHEN TR1.TRXTRN='3036' AND TR1.LMSRSN=3
             THEN '3212'
             WHEN TR1.TRXTRN='3036' AND TR1.LMSRSN=6
             THEN '3213'
             WHEN TR1.TRXTRN='3036'
             THEN '3210'
             WHEN TR1.TRXTRN='3082'
             THEN '3210'
             WHEN TR1.TRXTRN='3088'
             THEN '3214'
             WHEN TR1.TRXTRN='3037' AND JL."AcctCode" = 'T11'
             THEN '3236' -- 2023-04-17 Wei 新增 from 賴桑:轉入債協暫收款
             WHEN TR1.TRXTRN='3037'
             THEN '3221'
             WHEN TR1.TRXTRN='3083'
             THEN '3230'
             WHEN TR1.TRXTRN='3033' AND JL."AcctCode" = 'F07'
             THEN '3240'
             WHEN TR1.TRXTRN='3033' AND JL."AcctCode" = 'TMI'
             THEN '3242'
             WHEN TR1.TRXTRN='3033' AND JL."AcctCode" = 'F10'
             THEN '3244'
             WHEN TR1.TRXTRN='3033' AND JL."AcctCode" = 'F29'
             THEN '3260'
             WHEN TR1.TRXTRN='3041' AND TR1.TRXTCT=1 AND ACN."IsSameFac" = 1 
             THEN '3422'
             WHEN TR1.TRXTRN='3041' AND TR1.TRXTCT=1
             THEN '3421'
             WHEN TR1.TRXTRN='3041' AND TR1.TRXTCT=2
             THEN '3423'
             WHEN TR1.TRXTRN='3041' AND TR1.TRXTCT=3
             THEN '3424'
             WHEN TR1.TRXTRN='3041' AND TR1.TRXTCT=4
             THEN '3425'
             WHEN TR1.TRXTRN='3041' AND TR1.TRXTCT=5
             THEN '3426'
             WHEN TR1.TRXTRN='3041' AND TR1.TRXTCT=6
             THEN '3427'
             WHEN TR1.TRXTRN='3041'
             THEN '3420'
             WHEN TR1.TRXTRN='3086'
             THEN '3427'
             WHEN TR1.TRXTRN='3089'
             THEN '3428'
             WHEN TR1.TRXTRN='3021'
             THEN '3701'
             WHEN TR1.TRXTRN='3046'
             THEN '3711'
           ELSE '9999' END AS "TxDescCode"
    FROM TR 
    LEFT JOIN "LA$TRXP" TR1 ON TR1."CUSBRH" = TR."CUSBRH" 
                           AND TR1."TRXDAT" = TR."TRXDAT" 
                           AND TR1."TRXNMT" = TR."TRXNMT" 
                           AND TR1."TRXNM2" = TR."TRXNM2" 
                           AND TR1."TRXTRN" = TR."TRXTRN" 
    LEFT JOIN "TmpLMSP" TL ON TL."LMSACN" = TR1."LMSACN" 
                          AND TL."LMSAPN" = TR1."LMSAPN" 
                          AND TL."LMSASQ" = TR1."LMSASQ" 
    LEFT JOIN "TfRcData" RC ON RC."LMSACN" = TR1."LMSACN" 
                           AND RC."LMSAPN" = TR1."LMSAPN" 
                           AND RC."LMSASQ" = TR1."LMSASQ" 
                           AND RC."TRXISD" = TR1."TRXISD" 
    LEFT JOIN AcnpData ACN ON ACN."LMSACN" = TR1."LMSACN" 
                          AND ACN."LMSAPN1" = TR1."LMSAPN" 
                          AND ACN."LMSASQ1" = TR1."LMSASQ" 
    LEFT JOIN JLSeq1 JL ON JL."TRXDAT" = TR."TRXDAT" 
                       AND JL."TRXNMT" = TR."TRXNMT" 
    LEFT JOIN "As400EmpNoMapping" AEM1 ON AEM1."As400TellerNo" = TR1."TRXMEM" 
    LEFT JOIN "As400EmpNoMapping" AEM2 ON AEM2."As400TellerNo" = TR1."TRXSID" 
    LEFT JOIN "TB$TCDP" TCD ON TCD."TRXTRN" = TR."TRXTRN" 
    LEFT JOIN correctTx COR ON COR."TRXDAT" = TR1."TRXEDT" 
                           AND COR."TRXNMT" = TR1."TRXENM" 
    LEFT JOIN "As400EmpNoMapping" AEM3 ON AEM3."As400TellerNo" = COR."TRXMEM"                     
    LEFT JOIN "LoanBorMain" LBM ON LBM."CustNo" = TR1.LMSACN
                               AND LBM."FacmNo" = TR1.LMSAPN
                               AND LBM."BormNo" = TR1.LMSASQ
--    WHERE TR1."TRXDAT" <= "TbsDyF" 
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
