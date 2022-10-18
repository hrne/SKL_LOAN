--------------------------------------------------------
--  DDL for Procedure Usp_Tf_AcDetail_Ins
--------------------------------------------------------
set define off;

CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_Tf_AcDetail_Ins" 
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
    EXECUTE IMMEDIATE 'ALTER TABLE "AcDetail" DISABLE PRIMARY KEY CASCADE'; 
    EXECUTE IMMEDIATE 'TRUNCATE TABLE "AcDetail" DROP STORAGE'; 
    EXECUTE IMMEDIATE 'ALTER TABLE "AcDetail" ENABLE PRIMARY KEY'; 
 
    -- 寫入資料 
    INSERT INTO "AcDetail" 
    WITH ATF AS ( 
      SELECT "TRXTRN" 
           , "ACTACT" 
           , 'D'      AS "DbCr" 
           , "ATDDAC" AS "ACNACC" 
           , "ATDDDA" AS "ACNACS" 
           , "ATDDAS" AS "ACNASS" 
      FROM "TB$ATFP" 
      UNION 
      SELECT "TRXTRN" 
           , "ACTACT" 
           , 'C'      AS "DbCr" 
           , "ATDCAC" AS "ACNACC" 
           , "ATDCDA" AS "ACNACS" 
           , "ATDCAS" AS "ACNASS" 
      FROM "TB$ATFP" 
    ) 
    -- For 暫收款-債協 
    , tempTRXP AS ( 
      SELECT TRXDAT 
           , TRXNMT 
           , LMSACN 
           , ROW_NUMBER() 
             OVER ( 
               PARTITION BY TRXDAT 
                          , TRXNMT 
                          , LMSACN 
               ORDER BY TRXNM2 
             ) AS "TxSeq" 
      FROM "LA$TRXP" 
      WHERE "LMSACN" != 0 
    )
    , txEmpData AS (
        select DISTINCT
               TRXDAT
             , TRXMEM
             , TRXNMT
        from LA$TRXP
    )
    , F1 AS ( 
      SELECT "OverdueDate" 
           , SUM("Fee") AS "DailyFeeTotal" 
      FROM "ForeclosureFee" 
      WHERE "OverdueDate" > 0 
      GROUP BY "OverdueDate" 
    ) 
    , FF AS ( 
      SELECT F0."CustNo" 
           , F0."FacmNo" 
           , F0."RecordNo" 
           , F0."OverdueDate" 
           , F0."Fee" 
           , F1."DailyFeeTotal" 
      FROM "ForeclosureFee" F0 
      LEFT JOIN F1 ON F1."OverdueDate" = F0."OverdueDate" 
      WHERE F0."OverdueDate" > 0 
    ) 
    , ACN AS ( 
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
    , TR AS ( 
      SELECT TR1."TRXDAT" 
            ,TR1."TRXNMT" 
            ,TR1."TRXTRN" 
            ,TR1."ACTACT" 
            ,TR1."LMSACN" 
            ,TR1."LMSAPN" 
            ,TR1."LMSASQ" 
            ,TR1."TRXAMT" 
            ,TR1."TRXMEM" 
            ,MIN(CASE 
                   WHEN TR1."TRXIDT" > 20400101 
                   THEN TR1."TRXDAT" 
                 ELSE TR1."TRXIDT" END 
             ) AS "TRXIDT" 
            ,MAX(CASE 
                   WHEN TR1."TRXTCT" IS NOT NULL 
                   THEN CASE 
                         --  WHEN TR1."TRXTCT" = '1' AND NVL(NOD."LMSACN",0) <> 0 THEN '2' 
                          WHEN TR1."TRXTCT" = '1' AND ACN."IsSameFac" = 1 
                                                  THEN '2' 
                          WHEN TR1."TRXTCT" = '1' THEN '1' 
                          WHEN TR1."TRXTCT" = '2' THEN '3' 
                          WHEN TR1."TRXTCT" = '3' THEN '4' 
                          WHEN TR1."TRXTCT" = '4' THEN '5' 
                          WHEN TR1."TRXTCT" = '5' THEN '6' 
                          WHEN TR1."TRXTCT" = '6' THEN '7' 
                        ELSE TR1."TRXTCT" END 
                 ELSE '' END) AS "TRXTCT" 
      FROM "LA$TRXP" TR1 
      LEFT JOIN ACN ON ACN."LMSACN" = TR1."LMSACN" 
                   AND ACN."LMSAPN1" = TR1."LMSAPN" 
                   AND ACN."LMSASQ1" = TR1."LMSASQ" 
      WHERE TR1."LMSACN" <> 0 
        AND TR1."TRXDAT" > 20190101 
      GROUP BY TR1."TRXDAT" 
              ,TR1."TRXNMT" 
              ,TR1."TRXTRN" 
              ,TR1."ACTACT" 
              ,TR1."LMSACN" 
              ,TR1."LMSAPN" 
              ,TR1."LMSASQ" 
              ,TR1."TRXAMT" 
              ,TR1."TRXMEM" 
    ) 
    , S4 AS ( 
      SELECT TR."TRXDAT" 
             ,TR."TRXNMT" 
             ,TR."TRXTRN" 
             ,TR."ACTACT" 
             ,TR."LMSACN" 
             ,TR."LMSAPN" 
             ,TR."LMSASQ" 
             ,TR."TRXAMT" 
             ,TR."TRXTCT" 
             ,TR."TRXIDT" 
             ,CASE 
                WHEN TR."TRXAMT" < 0 AND ATF."DbCr" = 'D' THEN 'C' 
                WHEN TR."TRXAMT" < 0 AND ATF."DbCr" = 'C' THEN 'D' 
              ELSE ATF."DbCr" END AS "DbCr" 
             ,ATF."DbCr"  AS "OriDbCr" 
             ,ATF."ACNACC" 
             ,ATF."ACNACS" 
             ,ATF."ACNASS" 
      FROM  TR 
      LEFT JOIN ATF ON ATF."TRXTRN" = TR."TRXTRN" 
                   AND ATF."ACTACT" = TR."ACTACT" 
    ) 
    , S AS ( 
      SELECT DISTINCT 
             S1."TRXDAT" 
            ,S1."TRXNMT" 
            ,S1."JLNVNO" 
            ,S1."CUSBRH" 
            ,S1."TRXATP" 
            ,S1."JLNAMT" 
            ,S1."JLNCRC" 
            ,S1."BSTBTN" 
            ,S1."AGLVBN" 
            ,S1."TRXTRN" 
            ,S1."TRXNTX" 
            ,S5."AcNoCode" 
            ,S5."AcSubCode" 
            ,S5."AcDtlCode" 
            ,S5."AcctCode" 
            ,S5."AcctFlag" 
            ,S5."ReceivableFlag" 
            ,S5."AcBookFlag" 
            ,S4."TRXTCT" 
            ,NVL(S4."LMSACN",FF."CustNo") AS "LMSACN" 
            ,NVL(S4."LMSAPN",FF."FacmNo") AS "LMSAPN" 
            ,NVL(S4."LMSASQ",0) AS "LMSASQ" 
            ,NVL(S4."TRXAMT",FF."Fee") AS "TRXAMT" 
            ,FF."RecordNo" 
            ,S4."TRXIDT" 
      FROM "LA$JLNP" S1 
      LEFT JOIN "LA$JLNP" S2 ON S2."TRXDAT" = S1."TRXDAT" 
                            AND S2."JLNOVN" = S1."JLNVNO" -- 串取訂正資料 
                            AND S2."JLNVNO" = 0 
      LEFT JOIN "TB$LCDP" S3 ON S3."ACNACC" = S1."ACNACC" 
                            AND NVL(S3."ACNACS",' ') = NVL(S1."ACNACS",' ') 
                            AND NVL(S3."ACNASS",' ') = NVL(S1."ACNASS",' ') 
      LEFT JOIN "CdAcCode" S5 ON S5."AcNoCodeOld" = S3."CORACC" 
                             AND S5."AcSubCode" = NVL(S3."CORACS",'     ') 
                             AND S5."AcDtlCode" = CASE 
                                                    WHEN S3."CORACC" = '40903300' 
                                                         AND NVL(S3."CORACS",'     ') = '     ' 
                                                    THEN '01' -- 放款帳管費 
                                                    -- 2022-06-30 Wei From Lai Email: 
                                                    -- AcNoCode = 20222020000 
                                                    -- 轉 AcDtlCode = 01 
                                                    --    and AcctCode = TAV 
                                                    WHEN S3."CORACC" = '20232020' 
                                                         AND NVL(S3."CORACS",'     ') = '     ' 
                                                    THEN '01' 
                                                    -- 2022-09-08 Wei FROM yoko line 
                                                    WHEN S3."CORACC" = '20232182' 
                                                         AND NVL(S3."CORACS",'     ') = '     ' 
                                                    THEN '01' 
                                                    -- 2022-09-08 Wei fix bug 
                                                    WHEN S3."CORACC" = '20232180' 
                                                         AND NVL(S3."CORACS",'     ') = '     ' 
                                                    THEN '01' 
                                                    -- 2022-09-08 Wei fix bug 
                                                    WHEN S3."CORACC" = '20232181' 
                                                         AND NVL(S3."CORACS",'     ') = '     ' 
                                                    THEN '01' 
                                                    -- 2022-09-08 Wei fix bug 
                                                    WHEN S3."CORACC" = '40907400' 
                                                         AND NVL(S3."CORACS",'     ') = '     ' 
                                                    THEN '01' 
                                                  ELSE '  ' END 
      LEFT JOIN ATF ON ATF."ACNACC"          = S1."ACNACC" 
                   AND NVL(ATF."ACNACS",' ') = NVL(S1."ACNACS",' ') 
                   AND NVL(ATF."ACNASS",' ') = NVL(S1."ACNASS",' ') 
      LEFT JOIN S4 ON S4."TRXDAT"  = S1."TRXDAT" 
                  AND S4."TRXNMT"  = S1."TRXNMT" 
                  AND CASE
                        WHEN S1."TRXTRN" IN ('3041','3031')
                             AND S4."TRXTRN" = '3084'
                        THEN 1
                        WHEN S4."TRXTRN" IN ('3041')
                             AND S1."TRXTRN" = '3084'
                        THEN 1
                        WHEN S4."TRXTRN" = CASE
                                             WHEN S1."TRXTRN" IS NOT NULL THEN S1."TRXTRN" 
                                           ELSE ATF."TRXTRN" END 
                             AND S4."ACTACT"  = ATF."ACTACT"
                             AND S4."DbCr"    = S1."TRXATP" 
                             AND S4."OriDbCr" = ATF."DbCr" 
                             AND S4."ACNACC"  = ATF."ACNACC" 
                             AND NVL(S4."ACNACS",' ') = NVL(ATF."ACNACS",' ')
                             AND NVL(S4."ACNASS",' ') =  NVL(ATF."ACNASS",' ')
                        THEN 1
                      ELSE 0 END = 1
      LEFT JOIN FF ON FF."OverdueDate" = S1."TRXDAT" 
                  AND FF."DailyFeeTotal" = S1."JLNAMT" 
                  AND NVL(S5."AcctCode",' ') NOT IN ('310','320','330','340','990') 
                  AND NVL(S4."LMSACN",0) = 0 
      WHERE NVL(S1."TRXDAT",0) > 0      -- 傳票檔會計日期不為0 *日期為0者為問題資料,則排除 
        AND NVL(S1."JLNVNO",0) > 0      -- 傳票檔傳票號碼不為0 *傳票號碼為0者為訂正資料,則排除 
        AND NVL(S2."TRXDAT",0) = 0      -- 若在S2有資料,表示S1此筆為被訂正資料,則排除 
        AND NVL(S3."CORACC",' ') != ' ' -- 有串到新會科才寫入 
        AND NVL(S3."AGLACC",' ') != ' ' -- 2021-12-08 新增判斷 有串到最新的11碼會科才寫入 
        AND NVL(S5."AcNoCode",' ') != ' ' -- 2021-07-15 新增判斷 有串到最新的11碼會科才寫入 
        AND S1."JLNCRC" = '0' 
        AND S1."TRXDAT" >= 20190101 
--        AND S1."TRXDAT" <= "TbsDyF" 
        AND CASE 
              WHEN NVL(S5."AcctCode",' ') IN ('310','320','330','340','990' 
                                             ,'IC1','IC2','IC3','IC4','IOP','IOV' 
                                             ,'F15','F16' 
                                             ,'TMI','T12' 
                                             ,'F08','F29','F10'
                                             ,'P02') 
                   AND NVL(S4."TRXTRN",' ') <> ' ' 
              THEN 1 
              WHEN NVL(S5."AcctCode",' ') NOT IN ('310','320','330','340','990' 
                                                 ,'IC1','IC2','IC3','IC4','IOP','IOV' 
                                                 ,'F15','F16' 
                                                 ,'TMI','T12' 
                                                 ,'F08','F29','F10'
                                                 ,'P02') 
                   AND NVL(S4."LMSACN",0) = 0 
              THEN 1 
              WHEN NVL(S5."AcctCode",' ') IN ('TMI','T12','P02','IC3','330') 
                   AND S1."TRXTRN" = '3079' 
              THEN 1 
              WHEN NVL(S5."AcctCode",' ') IN ('TMI','T12') 
                   AND NVL(S1."TRXTRN",' ') = ' ' 
                   AND S4."TRXTRN" IS NULL 
              THEN 1 
              WHEN NVL(S5."AcctCode",' ') IN ('F25','T13','F13','F07','F27','F24','BCK') 
                   AND NVL(S1."TRXTRN",' ') = ' ' 
                   AND S4."TRXTRN" = '3037' 
              THEN 1
              WHEN NVL(S5."AcctCode",' ') IN ('C02','OPL') 
                   AND NVL(S1."TRXTRN",' ') = ' ' 
                   AND S4."TRXTRN" = '3036' 
              THEN 1
            ELSE 0  
            END = 1 
    ) 
    , ACT AS ( 
      SELECT "LMSACN" 
           , "ACTFSC" 
      FROM "LA$ACTP" 
      WHERE "ACTFSC" IS NOT NULL 
    ) 
    SELECT S."TRXDAT"                     AS "RelDy"               -- 登放日期 Decimald 8 0 
          ,'0000' 
           || NVL(AEM1."EmpNo",'999999')
          -- 2021-11-30 修改 只紀錄TRXNMT 
           || LPAD(S."TRXNMT",8,'0')      AS "RelTxseq"            -- 登放序號 VARCHAR2 18 0 
          ,ROW_NUMBER() 
           OVER ( 
            PARTITION BY S."TRXDAT" 
                        ,S."TRXNMT" 
            ORDER BY S."JLNVNO" 
                    ,ABS(NVL(S."TRXAMT",S."JLNAMT")) 
                    ,NVL(S."LMSACN",0) 
                    ,NVL(S."LMSAPN",0) 
                    ,NVL(S."LMSASQ",0) 
           )                              AS "AcSeq"               -- 分錄序號 DECIMAL 4 0 
          ,S."TRXDAT"                     AS "AcDate"              -- 會計日期 Decimald 8 0 
          ,LPAD(S."CUSBRH",4,'0')         AS "BranchNo"            -- 單位別 VARCHAR2 4 0 
          ,'TWD'                          AS "CurrencyCode"        -- 幣別 VARCHAR2 3 0 
          ,S."AcNoCode"                   AS "AcNoCode"            -- 科目代號 VARCHAR2 11 0 -- 2021-07-15 修改為新版11碼 
          ,S."AcSubCode"                  AS "AcSubCode"           -- 子目代號 VARCHAR2 5 0 
          ,S."AcDtlCode"                  AS "AcDtlCode"           -- 細目代號 VARCHAR2 2 0 
          ,S."AcctCode"                   AS "AcctCode"            -- 業務科目代號 VARCHAR2 3 0 
          ,S."TRXATP"                     AS "DbCr"                -- 借貸別 VARCHAR2 1 0 
          ,ABS(NVL(S."TRXAMT",S."JLNAMT")) 
                                          AS "TxAmt"               -- 記帳金額 DECIMAL 16 2 
          ,CASE  
             WHEN S."JLNCRC" IS NOT NULL 
             THEN 1 
           ELSE 0 END                     AS "EntAc"               -- 入總帳記號 DECIMAL 1 0 
          ,NVL(NVL(S."LMSACN",tempTRXP."LMSACN"),0)
                                          AS "CustNo"              -- 戶號 DECIMAL 7 0 
          ,NVL(S."LMSAPN",0)              AS "FacmNo"              -- 額度編號 DECIMAL 3 0 
          ,NVL(S."LMSASQ",0)              AS "BormNo"              -- 撥款序號 DECIMAL 3 0 
          ,NVL(S."RecordNo",'')           AS "RvNo"                -- 銷帳編號 VARCHAR2 30 0 
          ,NVL(S."AcctFlag",0)            AS "AcctFlag"            -- 業務科目記號 DECIMAL 1 0 
          ,NVL(S."ReceivableFlag",0)      AS "ReceivableFlag"      -- 銷帳科目記號 DECIMAL 1 0 
          ,NVL(S."AcBookFlag",0)          AS "AcBookFlag"          -- 帳冊別記號 DECIMAL 1 0 
          ,'000'                          AS "AcBookCode"          -- 帳冊別 VARCHAR2 3  -- 2021-07-15 舊資料固定為000:全公司 
          ,CASE 
             WHEN NVL(S."AcBookFlag",0) = 1 -- 2022-09-23 Wei BugFix From 綺萍賴桑提供 
                  AND NVL(ACT."ACTFSC",' ') = 'A' -- 資金來源若有串到,且資料為A 則為利變帳冊 
             THEN '201' 
           ELSE '00A' END                 AS "AcSubBookCode"       -- 區隔帳冊 VARCHAR2 3 0 -- 2021-07-15 新增欄位,00A傳統帳冊、201利變帳冊 
          ,''                             AS "SumNo"               -- 彙總別 VARCHAR2 3 0 
          ,''                             AS "DscptCode"           -- 摘要代號 VARCHAR2 4 0 
          ,u''                            AS "SlipNote"            -- 傳票摘要 NVARCHAR2 80 0 
          ,S."AGLVBN"                     AS "SlipBatNo"           -- 傳票批號 DECIMAL 2 0 
          ,S."JLNVNO"                     AS "SlipNo"              -- 傳票號碼 DECIMAL 6 0 
          ,'0000'                         AS "TitaKinbr"           -- 登錄單位別 VARCHAR2 4 0 
          ,NVL(AEM1."EmpNo",'999999')     AS "TitaTlrNo"           -- 登錄經辦 VARCHAR2 6 0 
          ,LPAD(S."TRXNMT",8,'0')         AS "TitaTxtNo"           -- 登錄交易序號 DECIMAL 8 0 
          ,S."TRXTRN"                     AS "TitaTxCd"            -- 交易代號 VARCHAR2 5 0 
          ,''                             AS "TitaSecNo"           -- 業務類別 VARCHAR2 2 0 
          ,''                             AS "TitaBatchNo"         -- 整批批號 VARCHAR2 6 0 
          ,''                             AS "TitaBatchSeq"        -- 整批明細序號 VARCHAR2 6 0 
          ,''                             AS "TitaSupNo"           -- 核准主管 VARCHAR2 6 0 
          ,0                              AS "TitaRelCd"           -- 作業模式 DECIMAL 1 0 
          ,'{"EntryDate":"' || 
           CASE 
             WHEN S."TRXIDT" != 0 
             THEN S."TRXIDT" 
           ELSE S."TRXDAT" END 
           || '"' ||  
           CASE 
             WHEN S."TRXNTX" > 0  
             THEN ',"StampTaxFreeAmt":"' || S."TRXNTX" || '"' -- 免印花稅金額 
           ELSE '' END 
           || 
           CASE 
             WHEN S."TRXTCT" IS NOT NULL 
             THEN ',"CaseCloseCode":"' || S."TRXTCT" || '"' -- 結案區分 
           ELSE '' END 
           || '}'                         AS "JsonFields"          -- jason格式紀錄欄 VARCHAR2 300 0 
          ,TO_DATE(S."TRXIDT",'YYYYMMDD') AS "CreateDate"          -- 建檔日期時間 DATE 0 0 
          , NVL(AEM1."EmpNo",'999999')    AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 0 
          ,TO_DATE(S."TRXIDT",'YYYYMMDD') AS "LastUpdate"          -- 最後更新日期時間 DATE 0 0 
          , NVL(AEM1."EmpNo",'999999')    AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 0 
          ,S."BSTBTN"                     AS "SlipSumNo" 
          ,CASE  
             WHEN S."JLNCRC" IS NULL 
             THEN 0 
             WHEN TO_NUMBER(NVL(S."JLNCRC",0)) = 0 -- 未訂正  
             THEN 1 -- 已入帳 
             WHEN TO_NUMBER(NVL(S."JLNCRC",0)) = 1 -- 訂正 
             THEN 3 -- 沖正(隔日訂正) 
             WHEN TO_NUMBER(NVL(S."JLNCRC",0)) = 2 -- 被訂正 
             THEN 2 -- 被沖正(隔日訂正) 
             WHEN TO_NUMBER(NVL(S."JLNCRC",0)) = 3 -- 沖正 
             THEN 3 -- 沖正(隔日訂正) 
             WHEN TO_NUMBER(NVL(S."JLNCRC",0)) = 4 -- 被沖正 
             THEN 2 -- 被沖正(隔日訂正) 
           ELSE 0 END                     AS "TitaHCode" 
    FROM S 
    LEFT JOIN ACT ON ACT."LMSACN" = NVL(S."LMSACN",0) 
                 AND NVL(S."LMSACN",0) > 0 
    LEFT JOIN tempTRXP ON tempTRXP."TRXDAT" = S."TRXDAT" 
                      AND tempTRXP."TRXNMT" = S."TRXNMT" 
                      AND tempTRXP."TxSeq" = 1 
                      AND NVL(S."LMSACN",0) = 0
    LEFT JOIN txEmpData ON txEmpData.TRXDAT = S.TRXDAT 
                       AND txEmpData.TRXNMT = S.TRXNMT 
    LEFT JOIN "As400EmpNoMapping" AEM1 ON AEM1."As400TellerNo" = txEmpData."TRXMEM" 
    ; 
 
    -- 記錄寫入筆數 
    INS_CNT := INS_CNT + sql%rowcount; 
 
    /* 更新交易代號 */ 
    UPDATE "AcDetail" SET 
    "TitaTxCd" = CASE "TitaTxCd" 
                   WHEN '3021' THEN 'L3701' 
                   WHEN '3025' THEN 'L3100' -- D 
                   WHEN '3031' THEN 'L3200' -- C 
                   WHEN '3033' THEN 'L3230' --  
                   WHEN '3036' THEN 'L3210' 
                   WHEN '3037' THEN 'L3220' 
                   WHEN '3041' THEN 'L3420' 
                   WHEN '3046' THEN 'L3711' 
                   WHEN '3066' THEN 'L3200' 
                   WHEN '3080' THEN 'L3200' 
                   WHEN '3081' THEN 'L3200' 
                   WHEN '3082' THEN 'L3210' 
                   WHEN '3083' THEN 'L3230' 
                   WHEN '3084' THEN 'L3200' 
                   WHEN '3085' THEN 'L3200' 
                   WHEN '3086' THEN 'L3420' 
                   WHEN '3087' THEN 'L3420' 
                   WHEN '3088' THEN 'L3210' 
                   WHEN '3089' THEN 'L3420' 
                 ELSE "TitaTxCd" END  
    ; 
 
    -- 寫入資料 
    INSERT INTO "AcDetail" 
    WITH BOKOTHERS AS ( 
        SELECT CORACC 
             , CORACS 
             , NEWVBN 
             , TRXDAT 
             , TRXATP 
             , SUM(CORAMT) AS CORAMT 
        FROM "LA$JORP" 
        WHERE NEWVBN > 90 
          AND ACNBOK != '000' 
        GROUP BY CORACC 
               , CORACS 
               , NEWVBN 
               , TRXDAT 
               , TRXATP 
    )  
    , JORP AS ( 
        SELECT CORACC 
             , CORACS 
             , CORAMT 
             , ACNBOK 
             , NEWVBN 
             , TRXDAT 
             , TRXATP 
             , CORVNO 
             , CORVNS 
             , CORVDS 
        FROM "LA$JORP" 
        WHERE NEWVBN > 90 
    ) 
    SELECT JORP."TRXDAT"                  AS "RelDy"               -- 登放日期 Decimald 8 0 
          ,'000099999900000000'           AS "RelTxseq"            -- 登放序號 VARCHAR2 18 0 
          ,ROW_NUMBER() OVER (PARTITION BY JORP."TRXDAT" 
                              ORDER BY JORP."NEWVBN"   
                                     , JORP."CORVNS" 
                                     , JORP."ACNBOK" 
                             )            AS "AcSeq"               -- 分錄序號 DECIMAL 4 0 
          ,JORP."TRXDAT"                  AS "AcDate"              -- 會計日期 Decimald 8 0 
          ,'0000'                         AS "BranchNo"            -- 單位別 VARCHAR2 4 0 
          ,'TWD'                          AS "CurrencyCode"        -- 幣別 VARCHAR2 3 0 
          ,S5."AcNoCode"                  AS "AcNoCode"            -- 科目代號 VARCHAR2 11 0 -- 2021-07-15 修改為新版11碼 
          ,S5."AcSubCode"                 AS "AcSubCode"           -- 子目代號 VARCHAR2 5 0 
          ,S5."AcDtlCode"                 AS "AcDtlCode"           -- 細目代號 VARCHAR2 2 0 
          ,S5."AcctCode"                  AS "AcctCode"            -- 業務科目代號 VARCHAR2 3 0 
          ,JORP."TRXATP"                  AS "DbCr"                -- 借貸別 VARCHAR2 1 0 
          ,JORP."CORAMT" 
           - CASE 
               WHEN NVL(JORP."ACNBOK",' ') = '000' 
               THEN NVL(BOKOTHERS."CORAMT",0) 
             ELSE 0 END                   AS "TxAmt"               -- 記帳金額 DECIMAL 16 2 
          ,0                              AS "EntAc"               -- 入總帳記號 DECIMAL 1 0 
          ,0                              AS "CustNo"              -- 戶號 DECIMAL 7 0 
          ,0                              AS "FacmNo"              -- 額度編號 DECIMAL 3 0 
          ,0                              AS "BormNo"              -- 撥款序號 DECIMAL 3 0 
          ,''                             AS "RvNo"                -- 銷帳編號 VARCHAR2 30 0 
          ,NVL(S5."AcctFlag",0)           AS "AcctFlag"            -- 業務科目記號 DECIMAL 1 0 
          ,NVL(S5."ReceivableFlag",0)     AS "ReceivableFlag"      -- 銷帳科目記號 DECIMAL 1 0 
          ,NVL(S5."AcBookFlag",0)         AS "AcBookFlag"          -- 帳冊別記號 DECIMAL 1 0 
          ,'000'                          AS "AcBookCode"          -- 帳冊別 VARCHAR2 3  -- 2021-07-15 舊資料固定為000:全公司 
          ,CASE 
             WHEN NVL(JORP."ACNBOK",' ') = '201' 
             THEN '201' 
           ELSE '00A' END                 AS "AcSubBookCode"       -- 區隔帳冊 VARCHAR2 3 0 -- 2021-07-15 新增欄位,00A傳統帳冊、201利變帳冊 
          ,''                             AS "SumNo"               -- 彙總別 VARCHAR2 3 0 
          ,''                             AS "DscptCode"           -- 摘要代號 VARCHAR2 4 0 
          ,u''                            AS "SlipNote"            -- 傳票摘要 NVARCHAR2 80 0 
          ,JORP."NEWVBN"                  AS "SlipBatNo"           -- 傳票批號 DECIMAL 2 0 
          ,JORP."CORVNS"                  AS "SlipNo"              -- 傳票號碼 DECIMAL 6 0 
          ,'0000'                         AS "TitaKinbr"           -- 登錄單位別 VARCHAR2 4 0 
          ,'999999'                       AS "TitaTlrNo"           -- 登錄經辦 VARCHAR2 6 0 
          ,0                              AS "TitaTxtNo"           -- 登錄交易序號 DECIMAL 8 0 
          ,''                             AS "TitaTxCd"            -- 交易代號 VARCHAR2 5 0 
          ,''                             AS "TitaSecNo"           -- 業務類別 VARCHAR2 2 0 
          ,''                             AS "TitaBatchNo"         -- 整批批號 VARCHAR2 6 0 
          ,''                             AS "TitaBatchSeq"        -- 整批明細序號 VARCHAR2 6 0 
          ,''                             AS "TitaSupNo"           -- 核准主管 VARCHAR2 6 0 
          ,0                              AS "TitaRelCd"           -- 作業模式 DECIMAL 1 0 
          ,''                             AS "JsonFields"          -- jason格式紀錄欄 VARCHAR2 300 0 
          ,JOB_START_TIME                 AS "CreateDate"          -- 建檔日期時間 DATE 0 0 
          ,'999999'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 0 
          ,JOB_START_TIME                 AS "LastUpdate"          -- 最後更新日期時間 DATE 0 0 
          ,'999999'                       AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 0 
          ,JORP."NEWVBN"                  AS "SlipSumNo" 
          ,0                              AS "TitaHCode" 
    FROM JORP 
    LEFT JOIN "CdAcCode" S5 ON S5."AcNoCodeOld" = JORP."CORACC" 
                           AND S5."AcSubCode" = NVL(JORP."CORACS",'     ') 
                           AND S5."AcDtlCode" = CASE 
                                                  WHEN JORP."CORACC" = '40903300' 
                                                       AND NVL(JORP."CORACS",'     ') = '     ' 
                                                  THEN '01' -- 放款帳管費 
                                                  -- 2022-06-30 Wei From Lai Email: 
                                                  -- AcNoCode = 20222020000 
                                                  -- 轉 AcDtlCode = 01 
                                                  --    and AcctCode = TAV 
                                                  WHEN JORP."CORACC" = '20232020' 
                                                       AND NVL(JORP."CORACS",'     ') = '     ' 
                                                  THEN '01' 
                                                  -- 2022-09-08 Wei FROM yoko line 
                                                  WHEN JORP."CORACC" = '20232182' 
                                                       AND NVL(JORP."CORACS",'     ') = '     ' 
                                                  THEN '01' 
                                                  -- 2022-09-08 Wei fix bug 
                                                  WHEN JORP."CORACC" = '20232180' 
                                                       AND NVL(JORP."CORACS",'     ') = '     ' 
                                                  THEN '01' 
                                                  -- 2022-09-08 Wei fix bug 
                                                  WHEN JORP."CORACC" = '20232181' 
                                                       AND NVL(JORP."CORACS",'     ') = '     ' 
                                                  THEN '01' 
                                                  -- 2022-09-08 Wei fix bug 
                                                  WHEN JORP."CORACC" = '40907400' 
                                                       AND NVL(JORP."CORACS",'     ') = '     ' 
                                                  THEN '01' 
                                                ELSE '  ' END 
    LEFT JOIN BOKOTHERS ON NVL(JORP."ACNBOK",' ') = '000' 
                       AND BOKOTHERS."CORACC" = JORP."CORACC" 
                       AND NVL(BOKOTHERS."CORACS",' ') = NVL(JORP."CORACS",' ') 
                       AND BOKOTHERS."NEWVBN" = JORP."NEWVBN" 
                       AND BOKOTHERS."TRXDAT" = JORP."TRXDAT" 
                       AND BOKOTHERS."TRXATP" = JORP."TRXATP" 
    WHERE NVL(S5."AcNoCode",' ') != ' ' -- 2021-07-15 新增判斷 有串到最新的11碼會科才寫入 
    ; 
 
    MERGE INTO "AcDetail" T 
    USING ( 
      SELECT "AcctCode" 
           , "CustNo" 
           , "FacmNo" 
           , "RvNo" 
           , "RvAmt" 
           , "RvBal" 
           , "LastAcDate" 
           , "TitaTxtNo" 
      FROM "AcReceivable" 
      WHERE "AcctCode" IN ('F10','F29') 
    ) S 
    ON ( 
      T."AcDate" = S."LastAcDate" 
      AND T."TitaTxtNo" = S."TitaTxtNo" 
      AND T."CustNo" = S."CustNo" 
      AND T."TxAmt" = S."RvAmt" 
    ) 
    WHEN MATCHED THEN UPDATE 
    SET "FacmNo" = TO_NUMBER(S."FacmNo") 
      , "BormNo" = CASE 
                     WHEN "AcctCode" = 'F10' 
                     THEN TO_NUMBER(S."RvNo") 
                   ELSE "BormNo" END 
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
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_AcDetail_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace); 
END;

/
