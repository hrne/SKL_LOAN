--------------------------------------------------------
--  DDL for Procedure Usp_Tf_AcDetail_Ins
--------------------------------------------------------
set define off;

CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_Tf_AcDetail_Ins" 
( 
    -- 參數 
    "ExecSeq"      IN  INT,       --執行序號
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
        "DateStart" DECIMAL(8) := 0 ; -- 資料擷取起日
        "DateEnd"   DECIMAL(8) := 0 ; -- 資料擷取止日
    BEGIN 
 
    SELECT "TbsDy" + 19110000 
    INTO "TbsDyF" 
    FROM "TxBizDate" 
    WHERE "DateCode" = 'ONLINE' 
    ; 
 
    -- 刪除舊資料 
    IF "ExecSeq" = 1 THEN
        EXECUTE IMMEDIATE 'ALTER TABLE "AcDetail" DISABLE PRIMARY KEY CASCADE'; 
        EXECUTE IMMEDIATE 'TRUNCATE TABLE "AcDetail" DROP STORAGE'; 
        EXECUTE IMMEDIATE 'ALTER TABLE "AcDetail" ENABLE PRIMARY KEY'; 
    END IF;

    SELECT "StartDate"
    INTO "DateStart"
    FROM "TfByYear"
    WHERE "Seq" = "ExecSeq"
    ;

    SELECT "EndDate"
    INTO "DateEnd"
    FROM "TfByYear"
    WHERE "Seq" = "ExecSeq"
    ;

    -- 寫入資料 
    INSERT INTO "AcDetail" (
            "RelDy"               -- 登放日期 Decimald 8 0 
          , "RelTxseq"            -- 登放序號 VARCHAR2 18 0 
          , "AcSeq"               -- 分錄序號 DECIMAL 4 0 
          , "AcDate"              -- 會計日期 Decimald 8 0 
          , "BranchNo"            -- 單位別 VARCHAR2 4 0 
          , "CurrencyCode"        -- 幣別 VARCHAR2 3 0 
          , "AcNoCode"            -- 科目代號 VARCHAR2 11 0 -- 2021-07-15 修改為新版11碼 
          , "AcSubCode"           -- 子目代號 VARCHAR2 5 0 
          , "AcDtlCode"           -- 細目代號 VARCHAR2 2 0 
          , "AcctCode"            -- 業務科目代號 VARCHAR2 3 0 
          , "DbCr"                -- 借貸別 VARCHAR2 1 0 
          , "TxAmt"               -- 記帳金額 DECIMAL 16 2 
          , "EntAc"               -- 入總帳記號 DECIMAL 1 0 
          , "CustNo"              -- 戶號 DECIMAL 7 0 
          , "FacmNo"              -- 額度編號 DECIMAL 3 0 
          , "BormNo"              -- 撥款序號 DECIMAL 3 0 
          , "RvNo"                -- 銷帳編號 VARCHAR2 30 0 
          , "AcctFlag"            -- 業務科目記號 DECIMAL 1 0 
          , "ReceivableFlag"      -- 銷帳科目記號 DECIMAL 1 0 
          , "AcBookFlag"          -- 帳冊別記號 DECIMAL 1 0 
          , "AcBookCode"          -- 帳冊別 VARCHAR2 3  -- 2021-07-15 舊資料固定為000:全公司 
          , "AcSubBookCode"       -- 區隔帳冊 VARCHAR2 3 0 -- 2021-07-15 新增欄位,00A傳統帳冊、201利變帳冊 
          , "SumNo"               -- 彙總別 VARCHAR2 3 0 
          , "DscptCode"           -- 摘要代號 VARCHAR2 4 0 
          , "SlipNote"            -- 傳票摘要 NVARCHAR2 80 0 
          , "SlipBatNo"           -- 傳票批號 DECIMAL 2 0 
          , "SlipNo"              -- 傳票號碼 DECIMAL 6 0 
          , "TitaKinbr"           -- 登錄單位別 VARCHAR2 4 0 
          , "TitaTlrNo"           -- 登錄經辦 VARCHAR2 6 0 
          , "TitaTxtNo"           -- 登錄交易序號 DECIMAL 8 0 
          , "TitaTxCd"            -- 交易代號 VARCHAR2 5 0 
          , "TitaSecNo"           -- 業務類別 VARCHAR2 2 0 
          , "TitaBatchNo"         -- 整批批號 VARCHAR2 6 0 
          , "TitaBatchSeq"        -- 整批明細序號 VARCHAR2 6 0 
          , "TitaSupNo"           -- 核准主管 VARCHAR2 6 0 
          , "TitaRelCd"           -- 作業模式 DECIMAL 1 0 
          , "JsonFields"          -- jason格式紀錄欄 VARCHAR2 300 0 
          , "CreateDate"          -- 建檔日期時間 DATE 0 0 
          , "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 0 
          , "LastUpdate"          -- 最後更新日期時間 DATE 0 0 
          , "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 0 
          , "SlipSumNo" 
          , "TitaHCode" 
          , "MediaSlipNo"
    )
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
                          -- WHEN TR1."TRXTCT" = '1' AND NVL(NOD."LMSACN",0) <> 0 THEN '2' 
                          -- 2023-06-20 From Linda :賴桑說這二支程式借新還舊的判斷不能加同額度條件
                          -- 改為 WHEN TR1."TRXTCT" = '0'  AND ACN的舊額度撥款存在 THEN '2'
                          WHEN TR1."TRXTCT" = '0' 
                               AND NVL(ACN."LMSACN",0) != 0
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
        AND TR1."TRXDAT" >= "DateStart"
        AND TR1."TRXDAT" <= "DateEnd"
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
             ,TF."TRXSAK" -- 還款來源
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
    , acMap as (
      SELECT ACNACC
           , ACNACS
           , ACNASS
           , "AcNoCode"
           , "AcSubCode"
           , "AcDtlCode"
           , '00A' AS "AcSubBookCode"
      FROM "TfActblMapping"
      UNION
      SELECT L.ACNACC
           , L.ACNACS
           , L.ACNASS
           , C."AcNoCode"
           , C."AcSubCode"
           , C."AcDtlCode"
           -- 2023-04-06 Wei 增加 from Lai
           -- AcDetail AcSubBookCode轉換
           -- 依TB$LCDP.ACNAS區分
           -- 傳統A轉1
           -- 利變A轉201
           -- 利變B轉B
           -- 其他轉00A
           , CASE
               WHEN NVL(L.ACTFSC,' ') = '1'
               THEN '1'
               WHEN NVL(L.ACTFSC,' ') = 'A'
               THEN '201'
               WHEN NVL(L.ACTFSC,' ') = 'B'
               THEN 'B'
             ELSE '00A' END AS "AcSubBookCode"
      FROM "TB$LCDP" L
      LEFT JOIN "CdAcCode" C ON C."AcNoCodeOld" = L."CORACC" 
                            AND C."AcSubCode" = NVL(L."CORACS",'     ') 
                            AND C."AcDtlCode" = CASE 
                                                  WHEN L."CORACC" IN ('40903300' -- 放款帳管費 2022-06-30 Wei From Lai Email: 
                                                                     ,'20232020' -- 2022-09-08 Wei FROM yoko line 
                                                                     ,'20232182' -- 2022-09-08 Wei fix bug 
                                                                     ,'20232180' -- 2022-09-08 Wei fix bug 
                                                                     ,'20232181' -- 2022-09-08 Wei fix bug 
                                                                     ,'40907400'  -- 2022-09-08 Wei fix bug 
                                                                     )
                                                       AND NVL(L."CORACS",'     ') = '     ' 
                                                  THEN '01' 
                                                ELSE '  ' END
      WHERE NVL(C."AcNoCode",' ') != ' '
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
            -- 2023-09-25 Wei 修改 from SKL IT 珮琪 : 新系統有多的細目為什麼不拆?
            -- from Lai mail
            -- 暫收及待結轉帳項－擔保放款科目，細目區分
            -- 01[可抵繳]TAV：客戶之暫收可抵繳餘額
            --    需轉換
            -- 02[支票]TCK ：客戶存入支票(期票的金額)餘額
            --    需轉換
            -- 03[員工扣薪] TEM ：員工扣薪暫存科目
            --    需轉換
            -- 04[AML戶]TAM：AML檢核為可疑或未確定時轉入暫收款的餘額
            --    新增功能，不需轉換
            -- 05[借新還舊]；客戶之借新還舊餘額，關帳時檢核餘額應為零
            --   新增過渡科目，不需轉換
            -- 07[費用攤提] TSL:客戶之企金費用餘額
            --   新增功能，不需轉換
            -- 09[展期]；客戶之展期餘額，關帳時檢核餘額應為零
            --            新增過渡科目，不需轉換
            ,CASE
               WHEN S5."AcNoCode" = '20222020000'
                    AND S5."AcSubCode" = '     '
                    AND S4.TRXSAK = 4 -- 還款來源=4:支票兌現
               THEN '02' -- 暫收款-支票
               WHEN S5."AcNoCode" = '20222020000'
                    AND S5."AcSubCode" = '     '
                    AND S4.TRXSAK = 3 -- 還款來源=3:員工扣薪
               THEN '03' -- 暫收款-員工扣薪
             ELSE S5."AcDtlCode" 
             END AS "AcDtlCode"
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
            ,S3."AcSubBookCode"
      FROM "LA$JLNP" S1 
      LEFT JOIN acMap S3 ON S3."ACNACC" = S1."ACNACC" 
                            AND NVL(S3."ACNACS",' ') = NVL(S1."ACNACS",' ') 
                            AND NVL(S3."ACNASS",' ') = NVL(S1."ACNASS",' ') 
      LEFT JOIN "CdAcCode" S5 ON S5."AcNoCode" = S3."AcNoCode" 
                             AND S5."AcSubCode" = NVL(S3."AcSubCode",'     ') 
                             AND S5."AcDtlCode" = NVL(S3."AcDtlCode",'  ') 
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
      WHERE S1."TRXDAT" >= "DateStart"
        AND S1."TRXDAT" <= "DateEnd"
        -- AND NVL(S1."TRXDAT",0) > 0      -- 傳票檔會計日期不為0 *日期為0者為問題資料,則排除 
        -- AND NVL(S1."JLNVNO",0) > 0      -- 傳票檔傳票號碼不為0 *傳票號碼為0者為訂正資料,則排除 
        AND NVL(S5."AcNoCode",' ') != ' ' -- 2021-07-15 新增判斷 有串到最新的11碼會科才寫入 
        -- AND S1."JLNCRC" = '0' 
        AND CASE 
              -- 必須存在於放款交易內容檔的業務科目
              WHEN NVL(S5."AcctCode",' ') IN ('310','320','330','340','990' 
                                             ,'IC1','IC2','IC3','IC4','IOP','IOV' 
                                             ,'F15','F16' 
                                             ,'TMI','T12' 
                                             ,'F08','F29','F10'
                                             ,'P02') 
                   AND NVL(S4."TRXTRN",' ') <> ' ' 
              THEN 1 
              -- 會計帳務明細檔的交易代號為3079時會產生的業務科目
              WHEN NVL(S5."AcctCode",' ') IN ('TMI','T12','P02','IC3','330') 
                   AND S1."TRXTRN" = '3079' 
              THEN 1 
              -- 會計帳務明細檔交易代號為空者在放款交易內容檔不得有資料的業務科目
              WHEN NVL(S5."AcctCode",' ') IN ('TMI','T12') 
                   AND NVL(S1."TRXTRN",' ') = ' ' 
                   AND S4."TRXTRN" IS NULL 
              THEN 1 
              -- 會計帳務明細檔交易代號為空者在放款交易內容檔的交易代號為3037的業務科目
              WHEN NVL(S5."AcctCode",' ') IN ('F25','T13','F13','F07','F27','F24','BCK') 
                   AND NVL(S1."TRXTRN",' ') = ' ' 
                   AND S4."TRXTRN" = '3037' 
              THEN 1
              -- 會計帳務明細檔交易代號為空者在放款交易內容檔的交易代號為3036的業務科目
              WHEN NVL(S5."AcctCode",' ') IN ('C02','OPL') 
                   AND NVL(S1."TRXTRN",' ') = ' ' 
                   AND S4."TRXTRN" = '3036' 
              THEN 1
              -- 下列業務科目以外,在放款交易內容檔必須戶號為0或是無資料
              WHEN NVL(S5."AcctCode",' ') NOT IN ('310','320','330','340','990' 
                                                 ,'IC1','IC2','IC3','IC4','IOP','IOV' 
                                                 ,'F15','F16' 
                                                 ,'TMI','T12' 
                                                 ,'F08','F29','F10'
                                                 ,'P02') 
                   AND NVL(S4."LMSACN",0) = 0 
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
           || NVL(AEM1."EmpNo",LPAD(txEmpData."TRXMEM",6,'0'))
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
             WHEN S."AGLVBN" > 90
             THEN 9
           ELSE 1 END                     AS "EntAc"               -- 入總帳記號 DECIMAL 1 0 
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
             WHEN NVL(S."AcSubBookCode",' ') != ' ' -- 2023-04-06 Wei 增加 from Lai
             THEN S."AcSubBookCode"
           ELSE '00A' END                 AS "AcSubBookCode"       -- 區隔帳冊 VARCHAR2 3 0 -- 2021-07-15 新增欄位,00A傳統帳冊、201利變帳冊 
          ,''                             AS "SumNo"               -- 彙總別 VARCHAR2 3 0 
          ,''                             AS "DscptCode"           -- 摘要代號 VARCHAR2 4 0 
          ,u''                            AS "SlipNote"            -- 傳票摘要 NVARCHAR2 80 0 
          ,S."AGLVBN"                     AS "SlipBatNo"           -- 傳票批號 DECIMAL 2 0 
          ,S."JLNVNO"                     AS "SlipNo"              -- 傳票號碼 DECIMAL 6 0 
          ,'0000'                         AS "TitaKinbr"           -- 登錄單位別 VARCHAR2 4 0 
          ,NVL(AEM1."EmpNo",LPAD(txEmpData."TRXMEM",6,'0'))
                                          AS "TitaTlrNo"           -- 登錄經辦 VARCHAR2 6 0 
          ,LPAD(S."TRXNMT",8,'0')         AS "TitaTxtNo"           -- 登錄交易序號 DECIMAL 8 0 
          ,CASE
             WHEN S."AGLVBN" >= 90
             THEN 'L618D'
           ELSE S."TRXTRN" END            AS "TitaTxCd"            -- 交易代號 VARCHAR2 5 0 
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
          ,TO_DATE(NVL(S."TRXIDT",S."TRXDAT"),'YYYYMMDD')
                                          AS "CreateDate"          -- 建檔日期時間 DATE 0 0 
          ,NVL(AEM1."EmpNo",LPAD(txEmpData."TRXMEM",6,'0'))     AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 0 
          ,TO_DATE(NVL(S."TRXIDT",S."TRXDAT"),'YYYYMMDD')
                                          AS "LastUpdate"          -- 最後更新日期時間 DATE 0 0 
          ,NVL(AEM1."EmpNo",LPAD(txEmpData."TRXMEM",6,'0'))     AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 0 
          ,S."BSTBTN"                     AS "SlipSumNo" 
          ,TO_NUMBER(NVL(S."JLNCRC",0))   AS "TitaHCode" 
          ,''                             AS "MediaSlipNo"
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
                   WHEN "TitaTxCd" = '3079' THEN 'L6201' -- 2023-04-24 Wei 新增 from Lai
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
 
    -- 寫入資料 
    INSERT INTO "AcDetail" (
            "RelDy"               -- 登放日期 Decimald 8 0 
          , "RelTxseq"            -- 登放序號 VARCHAR2 18 0 
          , "AcSeq"               -- 分錄序號 DECIMAL 4 0 
          , "AcDate"              -- 會計日期 Decimald 8 0 
          , "BranchNo"            -- 單位別 VARCHAR2 4 0 
          , "CurrencyCode"        -- 幣別 VARCHAR2 3 0 
          , "AcNoCode"            -- 科目代號 VARCHAR2 11 0 -- 2021-07-15 修改為新版11碼 
          , "AcSubCode"           -- 子目代號 VARCHAR2 5 0 
          , "AcDtlCode"           -- 細目代號 VARCHAR2 2 0 
          , "AcctCode"            -- 業務科目代號 VARCHAR2 3 0 
          , "DbCr"                -- 借貸別 VARCHAR2 1 0 
          , "TxAmt"               -- 記帳金額 DECIMAL 16 2 
          , "EntAc"               -- 入總帳記號 DECIMAL 1 0 
          , "CustNo"              -- 戶號 DECIMAL 7 0 
          , "FacmNo"              -- 額度編號 DECIMAL 3 0 
          , "BormNo"              -- 撥款序號 DECIMAL 3 0 
          , "RvNo"                -- 銷帳編號 VARCHAR2 30 0 
          , "AcctFlag"            -- 業務科目記號 DECIMAL 1 0 
          , "ReceivableFlag"      -- 銷帳科目記號 DECIMAL 1 0 
          , "AcBookFlag"          -- 帳冊別記號 DECIMAL 1 0 
          , "AcBookCode"          -- 帳冊別 VARCHAR2 3  -- 2021-07-15 舊資料固定為000:全公司 
          , "AcSubBookCode"       -- 區隔帳冊 VARCHAR2 3 0 -- 2021-07-15 新增欄位,00A傳統帳冊、201利變帳冊 
          , "SumNo"               -- 彙總別 VARCHAR2 3 0 
          , "DscptCode"           -- 摘要代號 VARCHAR2 4 0 
          , "SlipNote"            -- 傳票摘要 NVARCHAR2 80 0 
          , "SlipBatNo"           -- 傳票批號 DECIMAL 2 0 
          , "SlipNo"              -- 傳票號碼 DECIMAL 6 0 
          , "TitaKinbr"           -- 登錄單位別 VARCHAR2 4 0 
          , "TitaTlrNo"           -- 登錄經辦 VARCHAR2 6 0 
          , "TitaTxtNo"           -- 登錄交易序號 DECIMAL 8 0 
          , "TitaTxCd"            -- 交易代號 VARCHAR2 5 0 
          , "TitaSecNo"           -- 業務類別 VARCHAR2 2 0 
          , "TitaBatchNo"         -- 整批批號 VARCHAR2 6 0 
          , "TitaBatchSeq"        -- 整批明細序號 VARCHAR2 6 0 
          , "TitaSupNo"           -- 核准主管 VARCHAR2 6 0 
          , "TitaRelCd"           -- 作業模式 DECIMAL 1 0 
          , "JsonFields"          -- jason格式紀錄欄 VARCHAR2 300 0 
          , "CreateDate"          -- 建檔日期時間 DATE 0 0 
          , "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 0 
          , "LastUpdate"          -- 最後更新日期時間 DATE 0 0 
          , "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 0 
          , "SlipSumNo" 
          , "TitaHCode" 
          , "MediaSlipNo"
    )
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
          ,CASE
             WHEN JORP."NEWVBN" > 90
             THEN 9
           ELSE 1 END                     AS "EntAc"               -- 入總帳記號 DECIMAL 1 0 
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
          ,'L618D'                        AS "TitaTxCd"            -- 交易代號 VARCHAR2 5 0 
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
          ,''                             AS "MediaSlipNo"
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
      AND JORP."TRXDAT" >= "DateStart"
      AND JORP."TRXDAT" <= "DateEnd"
    ; 
 
    -- 寫入資料 
    INSERT INTO "AcDetail" (
            "RelDy"               -- 登放日期 Decimald 8 0 
          , "RelTxseq"            -- 登放序號 VARCHAR2 18 0 
          , "AcSeq"               -- 分錄序號 DECIMAL 4 0 
          , "AcDate"              -- 會計日期 Decimald 8 0 
          , "BranchNo"            -- 單位別 VARCHAR2 4 0 
          , "CurrencyCode"        -- 幣別 VARCHAR2 3 0 
          , "AcNoCode"            -- 科目代號 VARCHAR2 11 0 -- 2021-07-15 修改為新版11碼 
          , "AcSubCode"           -- 子目代號 VARCHAR2 5 0 
          , "AcDtlCode"           -- 細目代號 VARCHAR2 2 0 
          , "AcctCode"            -- 業務科目代號 VARCHAR2 3 0 
          , "DbCr"                -- 借貸別 VARCHAR2 1 0 
          , "TxAmt"               -- 記帳金額 DECIMAL 16 2 
          , "EntAc"               -- 入總帳記號 DECIMAL 1 0 
          , "CustNo"              -- 戶號 DECIMAL 7 0 
          , "FacmNo"              -- 額度編號 DECIMAL 3 0 
          , "BormNo"              -- 撥款序號 DECIMAL 3 0 
          , "RvNo"                -- 銷帳編號 VARCHAR2 30 0 
          , "AcctFlag"            -- 業務科目記號 DECIMAL 1 0 
          , "ReceivableFlag"      -- 銷帳科目記號 DECIMAL 1 0 
          , "AcBookFlag"          -- 帳冊別記號 DECIMAL 1 0 
          , "AcBookCode"          -- 帳冊別 VARCHAR2 3  -- 2021-07-15 舊資料固定為000:全公司 
          , "AcSubBookCode"       -- 區隔帳冊 VARCHAR2 3 0 -- 2021-07-15 新增欄位,00A傳統帳冊、201利變帳冊 
          , "SumNo"               -- 彙總別 VARCHAR2 3 0 
          , "DscptCode"           -- 摘要代號 VARCHAR2 4 0 
          , "SlipNote"            -- 傳票摘要 NVARCHAR2 80 0 
          , "SlipBatNo"           -- 傳票批號 DECIMAL 2 0 
          , "SlipNo"              -- 傳票號碼 DECIMAL 6 0 
          , "TitaKinbr"           -- 登錄單位別 VARCHAR2 4 0 
          , "TitaTlrNo"           -- 登錄經辦 VARCHAR2 6 0 
          , "TitaTxtNo"           -- 登錄交易序號 DECIMAL 8 0 
          , "TitaTxCd"            -- 交易代號 VARCHAR2 5 0 
          , "TitaSecNo"           -- 業務類別 VARCHAR2 2 0 
          , "TitaBatchNo"         -- 整批批號 VARCHAR2 6 0 
          , "TitaBatchSeq"        -- 整批明細序號 VARCHAR2 6 0 
          , "TitaSupNo"           -- 核准主管 VARCHAR2 6 0 
          , "TitaRelCd"           -- 作業模式 DECIMAL 1 0 
          , "JsonFields"          -- jason格式紀錄欄 VARCHAR2 300 0 
          , "CreateDate"          -- 建檔日期時間 DATE 0 0 
          , "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 0 
          , "LastUpdate"          -- 最後更新日期時間 DATE 0 0 
          , "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 0 
          , "SlipSumNo" 
          , "TitaHCode" 
          , "MediaSlipNo"
    )
    WITH BOKOTHERS AS ( 
        SELECT AGLACC 
             , AGLACS 
             , AGLVBN 
             , TRXDAT 
             , TRXATP 
             , SUM(CORAMT) AS CORAMT 
        FROM DAT_LN$AVRP
        WHERE AGLVBN > 90 
          AND ACNBOK != '000' 
        GROUP BY AGLACC 
               , AGLACS 
               , AGLVBN 
               , TRXDAT 
               , TRXATP 
    )  
    , JORP AS ( 
        SELECT AGLACC -- CORACC 
             , AGLACS -- CORACS 
             , CORAMT -- CORAMT 
             , ACNBOK -- ACNBOK 
             , AGLVBN -- NEWVBN 
             , TRXDAT -- TRXDAT 
             , TRXATP -- TRXATP 
             , ACRNUM -- CORVNO 
             , ACRSRN -- CORVNS 
             , CORVDS -- CORVDS 
        FROM DAT_LN$AVRP
        WHERE AGLVBN > 90 
    ) 
    SELECT JORP."TRXDAT"                  AS "RelDy"               -- 登放日期 Decimald 8 0 
          ,'000099999900000000'           AS "RelTxseq"            -- 登放序號 VARCHAR2 18 0 
          ,ROW_NUMBER() OVER (PARTITION BY JORP."TRXDAT" 
                              ORDER BY JORP."AGLVBN"   
                                     , JORP."ACRSRN" 
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
          ,CASE
             WHEN JORP.AGLVBN > 90
             THEN 9
           ELSE 1 END                     AS "EntAc"               -- 入總帳記號 DECIMAL 1 0 
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
          ,JORP.AGLVBN                    AS "SlipBatNo"           -- 傳票批號 DECIMAL 2 0 
          ,JORP.ACRSRN                    AS "SlipNo"              -- 傳票號碼 DECIMAL 6 0 
          ,'0000'                         AS "TitaKinbr"           -- 登錄單位別 VARCHAR2 4 0 
          ,'999999'                       AS "TitaTlrNo"           -- 登錄經辦 VARCHAR2 6 0 
          ,0                              AS "TitaTxtNo"           -- 登錄交易序號 DECIMAL 8 0 
          ,'L618D'                        AS "TitaTxCd"            -- 交易代號 VARCHAR2 5 0 
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
          ,JORP.AGLVBN                    AS "SlipSumNo" 
          ,0                              AS "TitaHCode" 
          ,''                             AS "MediaSlipNo"
    FROM JORP 
    LEFT JOIN "CdAcCode" S5 ON S5."AcNoCode" = JORP."AGLACC" 
                           AND S5."AcSubCode" = NVL(JORP."AGLACS",'     ') 
                           AND S5."AcDtlCode" = CASE 
                                                  WHEN JORP."AGLACC" = '40903030000' 
                                                       AND NVL(JORP."AGLACS",'     ') = '     ' 
                                                  THEN '01' -- 放款帳管費 
                                                  -- 2022-06-30 Wei From Lai Email: 
                                                  -- AcNoCode = 20222020000 
                                                  -- 轉 AcDtlCode = 01 
                                                  --    and AcctCode = TAV 
                                                  WHEN JORP."AGLACC" = '20222020000' 
                                                       AND NVL(JORP."AGLACS",'     ') = '     ' 
                                                  THEN '01' 
                                                  -- 2022-09-08 Wei FROM yoko line 
                                                  WHEN JORP."AGLACC" = '20222180200' 
                                                       AND NVL(JORP."AGLACS",'     ') = '     ' 
                                                  THEN '01' 
                                                  -- 2022-09-08 Wei fix bug 
                                                  WHEN JORP."AGLACC" = '20222180000' 
                                                       AND NVL(JORP."AGLACS",'     ') = '     ' 
                                                  THEN '01' 
                                                  -- 2022-09-08 Wei fix bug 
                                                  WHEN JORP."AGLACC" = '20222180100' 
                                                       AND NVL(JORP."AGLACS",'     ') = '     ' 
                                                  THEN '01' 
                                                  -- 2022-09-08 Wei fix bug 
                                                  WHEN JORP."AGLACC" = '40906040000' 
                                                       AND NVL(JORP."AGLACS",'     ') = '     ' 
                                                  THEN '01' 
                                                ELSE '  ' END 
    LEFT JOIN BOKOTHERS ON NVL(JORP."ACNBOK",' ') = '000' 
                       AND BOKOTHERS."AGLACC" = JORP."AGLACC" 
                       AND NVL(BOKOTHERS."AGLACS",' ') = NVL(JORP."AGLACS",' ') 
                       AND BOKOTHERS.AGLVBN = JORP.AGLVBN
                       AND BOKOTHERS."TRXDAT" = JORP."TRXDAT" 
                       AND BOKOTHERS."TRXATP" = JORP."TRXATP" 
    WHERE NVL(S5."AcNoCode",' ') != ' ' -- 2021-07-15 新增判斷 有串到最新的11碼會科才寫入 
      AND JORP."TRXDAT" >= "DateStart"
      AND JORP."TRXDAT" <= "DateEnd"
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
