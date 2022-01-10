--------------------------------------------------------
--  DDL for Procedure Usp_Tf_AcReceivable_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "Usp_Tf_AcReceivable_Ins" 
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
    EXECUTE IMMEDIATE 'ALTER TABLE "AcReceivable" DISABLE PRIMARY KEY CASCADE';
    EXECUTE IMMEDIATE 'TRUNCATE TABLE "AcReceivable" DROP STORAGE';
    EXECUTE IMMEDIATE 'ALTER TABLE "AcReceivable" ENABLE PRIMARY KEY';

    -- 310 : 短期擔保放款
    -- 320 : 中期擔保放款
    -- 330 : 長期擔保放款
    -- 340 : 三十年房貸
    -- 990 : 催收款項
    INSERT INTO "AcReceivable"
    SELECT CASE
             WHEN NVL(L2."BormNo",0) > 0
             THEN '990'
           ELSE F1."AcctCode" END
                                AS "AcctCode"         -- 業務科目代號
          ,LPAD(F1."CustNo",7,0)
                                AS "CustNo"           -- 戶號
          ,LPAD(F1."FacmNo",3,0)
                                AS "FacmNo"           -- 額度編號
          ,CASE
             WHEN NVL(L2."BormNo",0) > 0
             THEN LPAD(L2."BormNo",3,0)
           ELSE LPAD(L1."BormNo",3,0) END
                                AS "RvNo"             -- 銷帳編號
          ,C1."AcNoCode"        AS "AcNoCode"         -- 科目代號
          ,C1."AcSubCode"       AS "AcSubCode"        -- 子目代號
          ,C1."AcDtlCode"       AS "AcDtlCode"        -- 細目代號
          ,'0000'               AS "BranchNo"         -- 單位別
          ,'TWD'                AS "CurrencyCode"     -- 幣別
          ,CASE
             WHEN NVL(L2."BormNo",0) > 0 
             THEN CASE
                    WHEN L2."OvduBal" > 0
                    THEN 0
                  ELSE 1 END
           ELSE CASE
                  WHEN L1."LoanBal" > 0
                  THEN 0
                ELSE 1 END
           END                  AS "ClsFlag"          -- 銷帳記號 0:未銷 1:已銷
          ,NVL(C1."AcctFlag",0) AS "AcctFlag"         -- 業務科目記號 0:一般科目 1:資負明細科目
          ,C1."ReceivableFlag"  AS "ReceivableFlag"   -- 銷帳科目記號 0:非銷帳科目 1:會計銷帳科目 2:業務銷帳科目 3:未收費用 4:短繳期金 5:另收欠款
          ,CASE
             WHEN NVL(L2."BormNo",0) > 0 
             THEN L2."OvduAmt"
           ELSE L1."DrawdownAmt" END
                                AS "RvAmt"            -- 起帳總額
          ,CASE
             WHEN NVL(L2."BormNo",0) > 0 
             THEN L2."OvduBal"
           ELSE L1."LoanBal" END
                                AS "RvBal"            -- 未銷餘額
          ,CASE
             WHEN NVL(L2."BormNo",0) > 0 
             THEN L2."OvduBal"
           ELSE L1."LoanBal" END
                                AS "AcBal"            -- 會計日餘額
          ,''                   AS "SlipNote"         -- 傳票摘要
          ,'000'                AS "AcBookCode"       -- 帳冊別 -- 2021-07-15 修改 000:全公司
          ,CASE
             WHEN NVL(ACT."ACTFSC",' ') = 'A' THEN '201' -- 利變A
           ELSE '00A' END       AS "AcSubBookCode"       -- 區隔帳冊 -- 2021-07-15 新增00A:傳統帳冊、201:利變帳冊
          ,CASE
             WHEN NVL(L2."BormNo",0) > 0 
             THEN L2."OvduDate"
           ELSE L1."DrawdownDate" END
                                AS "OpenAcDate"       -- 起帳日期
          ,CASE
             WHEN NVL(L2."BormNo",0) > 0 
             THEN L2."AcDate"
           ELSE L1."LastEntDy" END
                                AS "LastAcDate"       -- 最後作帳日
          ,CASE
             WHEN NVL(L2."BormNo",0) > 0 
             THEN L2."AcDate"
           ELSE L1."LastEntDy" END
                                AS "LastTxDate"       -- 最後交易日
          ,''                   AS "TitaTxCd"         -- 交易代號
          ,''                   AS "TitaKinBr"        -- 
          ,''                   AS "TitaTlrNo"        -- 經辦
          ,0                    AS "TitaTxtNo"        -- 交易序號
          ,''                   AS "JsonFields"       -- jason格式紀錄
          ,'999999'             AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 
          ,JOB_START_TIME       AS "CreateDate"          -- 建檔日期時間 DATE 8 
          ,'999999'             AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 
          ,JOB_START_TIME       AS "LastUpdate"          -- 最後更新日期時間 DATE 8 
    FROM "FacMain" F1
    LEFT JOIN "LoanBorMain" L1 ON L1."CustNo" = F1."CustNo"
                              AND L1."FacmNo" = F1."FacmNo"
    LEFT JOIN "LoanOverdue" L2 ON L2."CustNo" = L1."CustNo"
                              AND L2."FacmNo" = L1."FacmNo"
                              AND L2."BormNo" = L1."BormNo"
                              AND L2."OvduNo" = L1."LastOvduNo"
                              AND L1."Status" IN (2,6,7)
    LEFT JOIN "CdAcCode" C1 ON C1."AcctCode" = CASE WHEN NVL(L2."BormNo",0) > 0 THEN '990' ELSE F1."AcctCode" END
    LEFT JOIN "LA$ACTP" ACT ON ACT."LMSACN" = F1."CustNo"
    WHERE F1."LastBormNo" >= 1
      AND NVL(NVL(L2."OvduNo",L1."BormNo"),0) > 0
      AND L1."Status" != 99 -- 排除預約撥款
    ;

    -- 記錄寫入筆數
    INS_CNT := INS_CNT + sql%rowcount;

    -- TMI : 火險保費
    -- F09 : 暫付火險保費
    -- F25 : 催收款項-火險費用
    INSERT INTO "AcReceivable"
    SELECT CASE
             WHEN S1."StatusCode" = 2 THEN 'F25'
             WHEN S1."StatusCode" = 1 THEN 'F09'
             WHEN S1."StatusCode" = 0 AND S1."NotiTempFg" = 'Y' THEN 'TMI'
           ELSE ' ' END        AS "AcctCode"         -- 業務科目代號
          ,LPAD(S1."CustNo",7,0)
                               AS "CustNo"           -- 戶號
          ,LPAD(S1."FacmNo",3,0)
                               AS "FacmNo"           -- 額度編號
          ,S1."PrevInsuNo"     AS "RvNo"             -- 銷帳編號
          ,NVL(S2."AcNoCode",'        ')
                               AS "AcNoCode"         -- 科目代號
          ,NVL(S2."AcSubCode",'     ')
                               AS "AcSubCode"        -- 子目代號
          ,NVL(S2."AcDtlCode",'  ')
                               AS "AcDtlCode"        -- 細目代號
          ,'0000'              AS "BranchNo"         -- 單位別
          ,'TWD'               AS "CurrencyCode"     -- 幣別
          ,0                   AS "ClsFlag"          -- 銷帳記號 0:未銷 1:已銷
          ,0                   AS "AcctFlag"         -- 業務科目記號 0:一般科目 1:資負明細科目
          ,CASE
             WHEN S1."StatusCode" = 0 AND S1."NotiTempFg" = 'Y' THEN 3
             WHEN S1."StatusCode" = 1 THEN 2
             WHEN S1."StatusCode" = 2 THEN 2
           ELSE 0 END                   AS "ReceivableFlag"   -- 銷帳科目記號 0:非銷帳科目 1:會計銷帳科目 2:業務銷帳科目 3:未收費用 4:短繳期金 5:另收欠款
          ,S1."TotInsuPrem"    AS "RvAmt"            -- 起帳總額
          ,S1."TotInsuPrem"    AS "RvBal"            -- 未銷餘額
          ,S1."TotInsuPrem"    AS "AcBal"            -- 會計日餘額
          ,''                  AS "SlipNote"         -- 傳票摘要
          ,'000'               AS "AcBookCode"       -- 帳冊別 -- 2021-07-15 修改 000:全公司
          ,'00A'               AS "AcSubBookCode"       -- 區隔帳冊 -- 2021-07-15 新增00A:傳統帳冊、201:利變帳冊
          ,CASE
             WHEN S1."StatusCode" = 0 AND S1."NotiTempFg" = 'Y' THEN S1."InsuStartDate"
             WHEN S1."StatusCode" = 1 THEN S1."InsuStartDate"
             WHEN S1."StatusCode" = 2 THEN S1."OvduDate"
           ELSE 0 END          AS "OpenAcDate"       -- 起帳日期
          ,0                   AS "LastAcDate"       -- 最後作帳日
          ,0                   AS "LastTxDate"       -- 最後交易日
          ,''                  AS "TitaTxCd"         -- 交易代號
          ,''                  AS "TitaKinBr"        -- 
          ,''                  AS "TitaTlrNo"        -- 經辦
          ,0                   AS "TitaTxtNo"        -- 交易序號
          ,''                  AS "JsonFields"       -- jason格式紀錄
          ,'999999'            AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 
          ,JOB_START_TIME      AS "CreateDate"          -- 建檔日期時間 DATE 8 
          ,'999999'            AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 
          ,JOB_START_TIME      AS "LastUpdate"          -- 最後更新日期時間 DATE 8 
    FROM "InsuRenew" S1
    LEFT JOIN "CdAcCode" S2 ON S2."AcctCode" = CASE
                                                 WHEN S1."StatusCode" = 0 AND S1."NotiTempFg" = 'Y' THEN 'TMI'
                                                 WHEN S1."StatusCode" = 1 THEN 'F09'
                                                 WHEN S1."StatusCode" = 2 THEN 'F25'
                                               ELSE ' ' END
    WHERE S1."AcDate" = 0
      AND S1."TotInsuPrem" > 0
      AND CASE
            WHEN S1."StatusCode" = 0 AND S1."NotiTempFg" = 'Y' THEN 'Y'
            WHEN S1."StatusCode" = 1 THEN 'Y'
            WHEN S1."StatusCode" = 2 THEN 'Y'
          ELSE 'N' END = 'Y'
    ;

    -- 記錄寫入筆數
    INS_CNT := INS_CNT + sql%rowcount;

    -- F07 : 暫付法務費
    -- F24 : 催收款項-法務費用
    INSERT INTO "AcReceivable"
    SELECT CASE
             WHEN S1."OverdueDate" > 0 THEN 'F24'
           ELSE 'F07' END               AS "AcctCode"         -- 業務科目代號
          ,LPAD(S1."CustNo",7,'0')
                               AS "CustNo"           -- 戶號
          ,LPAD(S1."FacmNo",3,'0')
                               AS "FacmNo"           -- 額度編號
          ,LPAD(S1."RecordNo",7,'0')
                               AS "RvNo"             -- 銷帳編號
          ,NVL(S2."AcNoCode",'        ')
                               AS "AcNoCode"         -- 科目代號
          ,NVL(S2."AcSubCode",'     ')
                               AS "AcSubCode"        -- 子目代號
          ,NVL(S2."AcDtlCode",'  ')
                               AS "AcDtlCode"        -- 細目代號
          ,'0000'              AS "BranchNo"         -- 單位別
          ,'TWD'               AS "CurrencyCode"     -- 幣別
          ,0                   AS "ClsFlag"          -- 銷帳記號 0:未銷 1:已銷
          ,0                   AS "AcctFlag"         -- 業務科目記號 0:一般科目 1:資負明細科目
          ,2                   AS "ReceivableFlag"   -- 銷帳科目記號 0:非銷帳科目 1:會計銷帳科目 2:業務銷帳科目 3:未收費用 4:短繳期金 5:另收欠款
          ,S1."Fee"            AS "RvAmt"            -- 起帳總額
          ,S1."Fee"            AS "RvBal"            -- 未銷餘額
          ,S1."Fee"            AS "AcBal"            -- 會計日餘額
          ,''                  AS "SlipNote"         -- 傳票摘要
          ,'000'               AS "AcBookCode"       -- 帳冊別 -- 2021-07-15 修改 000:全公司
          ,'00A'               AS "AcSubBookCode"       -- 區隔帳冊 -- 2021-07-15 新增00A:傳統帳冊、201:利變帳冊
          ,CASE
             WHEN S1."OverdueDate" > 0 THEN S1."OverdueDate"
           ELSE S1."DocDate" END
                               AS "OpenAcDate"       -- 起帳日期
          ,0                   AS "LastAcDate"       -- 最後作帳日
          ,0                   AS "LastTxDate"       -- 最後交易日
          ,''                  AS "TitaTxCd"         -- 交易代號
          ,''                  AS "TitaKinBr"        -- 
          ,''                  AS "TitaTlrNo"        -- 經辦
          ,0                   AS "TitaTxtNo"        -- 交易序號
          ,''                  AS "JsonFields"       -- jason格式紀錄
          ,'999999'            AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 
          ,JOB_START_TIME      AS "CreateDate"          -- 建檔日期時間 DATE 8 
          ,'999999'            AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 
          ,JOB_START_TIME      AS "LastUpdate"          -- 最後更新日期時間 DATE 8 
    FROM "ForeclosureFee" S1
    LEFT JOIN "CdAcCode" S2 ON S2."AcctCode" = CASE
                                                 WHEN S1."OverdueDate" > 0 THEN 'F24'
                                               ELSE 'F07' END
    WHERE S1."CloseDate" = 0
    ;

    -- 記錄寫入筆數
    INS_CNT := INS_CNT + sql%rowcount;


    -- F10 : 將未銷帳管費寫入AcReceivable
    INSERT INTO "AcReceivable"
    SELECT 'F10'               AS "AcctCode"         -- 業務科目代號
          ,LPAD(S1."LMSACN",7,0)
                               AS "CustNo"           -- 戶號
          ,LPAD(S1."LMSAPN",3,0)
                               AS "FacmNo"           -- 額度編號
          ,LPAD(S1."LMSASQ",3,0)
                               AS "RvNo"             -- 銷帳編號
          ,'        '          AS "AcNoCode"         -- 科目代號
          ,'     '             AS "AcSubCode"        -- 子目代號
          ,'  '                AS "AcDtlCode"        -- 細目代號
          ,'0000'              AS "BranchNo"         -- 單位別
          ,'TWD'               AS "CurrencyCode"     -- 幣別
          ,CASE
             WHEN S1."TRXDAT" = 0 -- 會計日期為0者未銷
             THEN 0
           ELSE 1 END          AS "ClsFlag"          -- 銷帳記號 0:未銷 1:已銷
          ,0                   AS "AcctFlag"         -- 業務科目記號 0:一般科目 1:資負明細科目
          ,3                   AS "ReceivableFlag"   -- 銷帳科目記號 0:非銷帳科目 1:會計銷帳科目 2:業務銷帳科目 3:未收費用 4:短繳期金 5:另收欠款
          ,S1."ACTFEE"         AS "RvAmt"            -- 起帳總額
          ,CASE
             WHEN S1."TRXDAT" = 0 -- 會計日期為0者未銷
             THEN S1."ACTFEE"
           ELSE 0 END          AS "RvBal"            -- 未銷餘額
          ,CASE
             WHEN S1."TRXDAT" = 0 -- 會計日期為0者未銷
             THEN S1."ACTFEE"
           ELSE 0 END          AS "AcBal"            -- 會計日餘額
          ,''                  AS "SlipNote"         -- 傳票摘要
          ,'000'               AS "AcBookCode"       -- 帳冊別 -- 2021-07-15 修改 000:全公司
          ,'00A'               AS "AcSubBookCode"       -- 區隔帳冊 -- 2021-07-15 新增00A:傳統帳冊、201:利變帳冊
          ,0                   AS "OpenAcDate"       -- 起帳日期
          ,0                   AS "LastAcDate"       -- 最後作帳日
          ,0                   AS "LastTxDate"       -- 最後交易日
          ,''                  AS "TitaTxCd"         -- 交易代號
          ,''                  AS "TitaKinBr"        -- 
          ,''                  AS "TitaTlrNo"        -- 經辦
          ,0                   AS "TitaTxtNo"        -- 交易序號
          ,''                  AS "JsonFields"       -- jason格式紀錄
          ,'999999'            AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 
          ,JOB_START_TIME      AS "CreateDate"          -- 建檔日期時間 DATE 8 
          ,'999999'            AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 
          ,JOB_START_TIME      AS "LastUpdate"          -- 最後更新日期時間 DATE 8 
    FROM "LN$ACFP" S1
    ;

    -- 記錄寫入筆數
    INS_CNT := INS_CNT + sql%rowcount;

    -- F29 : 將未銷契變手續費寫入AcReceivable
    INSERT INTO "AcReceivable"
    SELECT 'F29'               AS "AcctCode"         -- 業務科目代號
          ,LPAD(S1."LMSACN",7,0)
                               AS "CustNo"           -- 戶號
          ,LPAD(S1."LMSAPN",3,0)
                               AS "FacmNo"           -- 額度編號
          ,TRIM(TO_CHAR(S1.CFRDAT)) || TRIM(TO_CHAR(ROW_NUMBER() OVER (PARTITION BY S1."LMSACN",S1."LMSAPN" ORDER BY S1."LMSACN",S1."LMSAPN",S1."CFRDAT"),'00'))
                               AS "RvNo"             -- 銷帳編號
          ,'        '          AS "AcNoCode"         -- 科目代號
          ,'     '             AS "AcSubCode"        -- 子目代號
          ,'  '                AS "AcDtlCode"        -- 細目代號
          ,'0000'              AS "BranchNo"         -- 單位別
          ,'TWD'               AS "CurrencyCode"     -- 幣別
          ,0                   AS "ClsFlag"          -- 銷帳記號 0:未銷 1:已銷
          ,0                   AS "AcctFlag"         -- 業務科目記號 0:一般科目 1:資負明細科目
          ,3                   AS "ReceivableFlag"   -- 銷帳科目記號 0:非銷帳科目 1:會計銷帳科目 2:業務銷帳科目 3:未收費用 4:短繳期金 5:另收欠款
          ,S1."CFRAMT"         AS "RvAmt"            -- 起帳總額
          ,S1."CFRAMT"         AS "RvBal"            -- 未銷餘額
          ,S1."CFRAMT"         AS "AcBal"            -- 會計日餘額
          ,CASE TRIM(TO_CHAR(S1.CFRCOD,'00'))
             WHEN '01' THEN '寬限與年期'
             WHEN '02' THEN '變利率週期'
             WHEN '03' THEN '補清償證明'
             WHEN '04' THEN '變更抵押權'
             WHEN '05' THEN '變更保證人'
          ELSE '' END          AS "SlipNote"         -- 傳票摘要
          ,'000'               AS "AcBookCode"       -- 帳冊別 -- 2021-07-15 修改 000:全公司
          ,'00A'               AS "AcSubBookCode"       -- 區隔帳冊 -- 2021-07-15 新增00A:傳統帳冊、201:利變帳冊
          ,S1."CFRDAT"         AS "OpenAcDate"       -- 起帳日期
          ,0                   AS "LastAcDate"       -- 最後作帳日
          ,0                   AS "LastTxDate"       -- 最後交易日
          ,''                  AS "TitaTxCd"         -- 交易代號
          ,''                  AS "TitaKinBr"        -- 
          ,AEM."EmpNo"         AS "TitaTlrNo"        -- 經辦
          ,0                   AS "TitaTxtNo"        -- 交易序號
          ,'{"ContractChgCode":"' || TRIM(TO_CHAR(S1.CFRCOD,'00')) || '"}'
                               AS "JsonFields"       -- jason格式紀錄
          ,'999999'            AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 
          ,JOB_START_TIME      AS "CreateDate"          -- 建檔日期時間 DATE 8 
          ,'999999'            AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 
          ,JOB_START_TIME      AS "LastUpdate"          -- 最後更新日期時間 DATE 8 
    FROM "LN$CFRP" S1
    LEFT JOIN "As400EmpNoMapping" AEM ON AEM."As400TellerNo" = S1."CFPMEM"
    WHERE S1."TRXDAT" = 0
    ;

    -- 記錄寫入筆數
    INS_CNT := INS_CNT + sql%rowcount;

    -- TAV : 暫收款-可抵繳
    INSERT INTO "AcReceivable"
    WITH ACT AS (
      -- 篩選出基本資料
      -- 條件1:排除戶號為601776
      -- 條件2:取BKPDAT最新的第一筆
      SELECT ROW_NUMBER() OVER (PARTITION BY ACTP.LMSACN ORDER BY ACTP.BKPDAT DESC) AS "Seq"
            ,ACTP.BKPDAT
            ,ACTP.LMSACN
            ,ACTP.LMSTOA
      FROM LADACTP ACTP
      WHERE ACTP.LMSACN != 601776
        AND ACTP.LMSTOA > 0
    )
    , L1 AS (
      -- 加總各額度放款餘額
      SELECT LMSACN
           , LMSAPN
           , SUM(LMSLBL) AS LMSLBL
      FROM LA$LMSP
      WHERE LMSLBL > 0
      GROUP BY LMSACN
             , LMSAPN
    )
    , L2 AS (
      -- 加總各戶號放款餘額
      SELECT LMSACN
           , SUM(LMSLBL) AS LMSLBL
      FROM LA$LMSP
      WHERE LMSLBL > 0
      GROUP BY LMSACN
    )
    , TMP AS (
      -- 依照額度放款餘額佔戶號放款餘額的比例
      -- 將費用分配到各個額度
      SELECT ACT.BKPDAT
           , ACT.LMSACN
           , L1.LMSAPN
           , ROUND(ACT.LMSTOA * L1.LMSLBL / L2.LMSLBL ,0) AS "NewLMSTOA"
           , ACT.LMSTOA
           , ROW_NUMBER()
             OVER (
               PARTITION BY ACT.LMSACN
               ORDER BY L1.LMSAPN
             ) AS "Seq"
      FROM ACT
      LEFT JOIN L1 ON L1.LMSACN = ACT.LMSACN
      LEFT JOIN L2 ON L2.LMSACN = ACT.LMSACN
      WHERE ACT."Seq" = 1 -- 取BKPDAT最新的第一筆
        AND ACT.LMSTOA > 0 -- 有費用的才做
        AND L1.LMSLBL > 0
        AND L2.LMSLBL > 0 
    )
    , M AS (
      -- 判斷最後一筆
      SELECT LMSACN
           , MAX("Seq") AS "MaxSeq"
      FROM TMP
      GROUP BY LMSACN
    )
    , S AS (
      -- 計算前幾筆已分配金額的加總
      SELECT M.LMSACN
           , SUM(TMP."NewLMSTOA") AS "OthersLMSTOA"
      FROM M
      LEFT JOIN TMP ON TMP.LMSACN = M.LMSACN
                   AND TMP."Seq" < M."MaxSeq"
      GROUP BY M.LMSACN
    )
    , S1 AS (
      -- 處理分配費用除不盡時的尾數差
      -- 最後一筆 用 總費用 減去 前幾筆已分配金額的加總
      SELECT TMP.BKPDAT
           , TMP.LMSACN
           , TMP.LMSAPN
           , CASE
               WHEN TMP."Seq" = M."MaxSeq" -- 如果是最後一筆
               THEN TMP.LMSTOA - NVL(S."OthersLMSTOA",0) -- 用總金額 - 前幾筆的加總
             ELSE TMP."NewLMSTOA"
             END AS "LMSTOA"
      FROM TMP
      LEFT JOIN M ON M.LMSACN = TMP.LMSACN
      LEFT JOIN S ON S.LMSACN = TMP.LMSACN
    )
    SELECT 'TAV'               AS "AcctCode"         -- 業務科目代號
          ,LPAD(S1."LMSACN",7,0)
                               AS "CustNo"           -- 戶號
          ,LPAD(S1."LMSAPN",3,0)
                               AS "FacmNo"           -- 額度編號
          ,' '                 AS "RvNo"             -- 銷帳編號
          ,S2."AcNoCode"       AS "AcNoCode"         -- 科目代號
          ,S2."AcSubCode"      AS "AcSubCode"        -- 子目代號
          ,S2."AcDtlCode"      AS "AcDtlCode"        -- 細目代號
          ,'0000'              AS "BranchNo"         -- 單位別
          ,'TWD'               AS "CurrencyCode"     -- 幣別
          ,0                   AS "ClsFlag"          -- 銷帳記號 0:未銷 1:已銷
          ,0                   AS "AcctFlag"         -- 業務科目記號 0:一般科目 1:資負明細科目
          ,2                   AS "ReceivableFlag"   -- 銷帳科目記號 0:非銷帳科目 1:會計銷帳科目 2:業務銷帳科目 3:未收費用 4:短繳期金 5:另收欠款
          ,S1."LMSTOA"         AS "RvAmt"            -- 起帳總額
          ,S1."LMSTOA"         AS "RvBal"            -- 未銷餘額
          ,S1."LMSTOA"         AS "AcBal"            -- 會計日餘額
          ,''                  AS "SlipNote"         -- 傳票摘要
          ,'000'               AS "AcBookCode"       -- 帳冊別 -- 2021-07-15 修改 000:全公司
          ,'00A'               AS "AcSubBookCode"       -- 區隔帳冊 -- 2021-07-15 新增00A:傳統帳冊、201:利變帳冊
          ,S1."BKPDAT"         AS "OpenAcDate"       -- 起帳日期
          ,0                   AS "LastAcDate"       -- 最後作帳日
          ,0                   AS "LastTxDate"       -- 最後交易日
          ,''                  AS "TitaTxCd"         -- 交易代號
          ,''                  AS "TitaKinBr"        -- 
          ,''                  AS "TitaTlrNo"        -- 經辦
          ,0                   AS "TitaTxtNo"        -- 交易序號
          ,''                  AS "JsonFields"       -- jason格式紀錄
          ,'999999'            AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 
          ,JOB_START_TIME      AS "CreateDate"          -- 建檔日期時間 DATE 8 
          ,'999999'            AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 
          ,JOB_START_TIME      AS "LastUpdate"          -- 最後更新日期時間 DATE 8 
    FROM  S1
    LEFT JOIN "CdAcCode" S2 ON S2."AcctCode" = 'TAV'
    ;

    -- 記錄寫入筆數
    INS_CNT := INS_CNT + sql%rowcount;

    -- T10 : 債協暫收款-收款專戶
    INSERT INTO "AcReceivable"
    SELECT 'T10'               AS "AcctCode"         -- 業務科目代號
          ,LPAD(S1."LMSACN",7,0)
                               AS "CustNo"           -- 戶號
          ,LPAD(S1."LMSAPN",3,0)
                               AS "FacmNo"           -- 額度編號
          ,' '                 AS "RvNo"             -- 銷帳編號
          ,S2."AcNoCode"       AS "AcNoCode"         -- 科目代號
          ,S2."AcSubCode"      AS "AcSubCode"        -- 子目代號
          ,S2."AcDtlCode"      AS "AcDtlCode"        -- 細目代號
          ,'0000'              AS "BranchNo"         -- 單位別
          ,'TWD'               AS "CurrencyCode"     -- 幣別
          ,0                   AS "ClsFlag"          -- 銷帳記號 0:未銷 1:已銷
          ,0                   AS "AcctFlag"         -- 業務科目記號 0:一般科目 1:資負明細科目
          ,2                   AS "ReceivableFlag"   -- 銷帳科目記號 0:非銷帳科目 1:會計銷帳科目 2:業務銷帳科目 3:未收費用 4:短繳期金 5:另收欠款
          ,S1."LMSTOA"         AS "RvAmt"            -- 起帳總額
          ,S1."LMSTOA"         AS "RvBal"            -- 未銷餘額
          ,S1."LMSTOA"         AS "AcBal"            -- 會計日餘額
          ,''                  AS "SlipNote"         -- 傳票摘要
          ,'000'               AS "AcBookCode"       -- 帳冊別 -- 2021-07-15 修改 000:全公司
          ,'00A'               AS "AcSubBookCode"       -- 區隔帳冊 -- 2021-07-15 新增00A:傳統帳冊、201:利變帳冊
          ,S1."BKPDAT"         AS "OpenAcDate"       -- 起帳日期
          ,0                   AS "LastAcDate"       -- 最後作帳日
          ,0                   AS "LastTxDate"       -- 最後交易日
          ,''                  AS "TitaTxCd"         -- 交易代號
          ,''                  AS "TitaKinBr"        -- 
          ,''                  AS "TitaTlrNo"        -- 經辦
          ,0                   AS "TitaTxtNo"        -- 交易序號
          ,''                  AS "JsonFields"       -- jason格式紀錄
          ,'999999'            AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 
          ,JOB_START_TIME      AS "CreateDate"          -- 建檔日期時間 DATE 8 
          ,'999999'            AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 
          ,JOB_START_TIME      AS "LastUpdate"          -- 最後更新日期時間 DATE 8 
    FROM (SELECT ROW_NUMBER() OVER (PARTITION BY ACTP.LMSACN ORDER BY ACTP.BKPDAT DESC) AS "Seq"
                ,ACTP.BKPDAT
                ,ACTP.LMSACN
                ,NVL(APLP.LMSAPN,ACTP.APLAPN) AS LMSAPN
                ,ACTP.LMSTOA
          FROM LADACTP ACTP
          LEFT JOIN ( SELECT LMSACN
                           , MIN(LMSAPN) AS LMSAPN
                      FROM LA$APLP
                      WHERE APLUAM > 0
                      GROUP BY LMSACN
                    ) APLP ON APLP.LMSACN = ACTP.LMSACN
          WHERE ACTP.LMSACN = 601776
         ) S1
    LEFT JOIN "CdAcCode" S2 ON S2."AcctCode" = 'T10'
    WHERE S1."Seq" = 1
      AND S1."LMSTOA" > 0
    ;

    -- 記錄寫入筆數
    INS_CNT := INS_CNT + sql%rowcount;

    -- T10 : 債協暫收款-收款專戶
    -- INSERT INTO "AcReceivable"
    -- SELECT 'T10'               AS "AcctCode"         -- 業務科目代號
    --       ,601776              AS "CustNo"           -- 戶號
    --       ,0                   AS "FacmNo"           -- 額度編號
    --       ,' '                 AS "RvNo"             -- 銷帳編號
    --       ,S2."AcNoCode"       AS "AcNoCode"         -- 科目代號
    --       ,S2."AcSubCode"      AS "AcSubCode"        -- 子目代號
    --       ,S2."AcDtlCode"      AS "AcDtlCode"        -- 細目代號
    --       ,'0000'              AS "BranchNo"         -- 單位別
    --       ,'TWD'               AS "CurrencyCode"     -- 幣別
    --       ,0                   AS "ClsFlag"          -- 銷帳記號 0:未銷 1:已銷
    --       ,0                   AS "AcctFlag"         -- 業務科目記號 0:一般科目 1:資負明細科目
    --       ,2                   AS "ReceivableFlag"   -- 銷帳科目記號 0:非銷帳科目 1:會計銷帳科目 2:業務銷帳科目 3:未收費用 4:短繳期金 5:另收欠款
    --       ,S1."LORAMT"         AS "RvAmt"            -- 起帳總額
    --       ,S1."LORAMT"         AS "RvBal"            -- 未銷餘額
    --       ,S1."LORAMT"         AS "AcBal"            -- 會計日餘額
    --       ,''                  AS "SlipNote"         -- 傳票摘要
    --       ,'000'               AS "AcBookCode"       -- 帳冊別 -- 2021-07-15 修改 000:全公司
    --       ,'00A'               AS "AcSubBookCode"       -- 區隔帳冊 -- 2021-07-15 新增00A:傳統帳冊、201:利變帳冊
    --       ,0                   AS "OpenAcDate"       -- 起帳日期
    --       ,0                   AS "LastAcDate"       -- 最後作帳日
    --       ,0                   AS "LastTxDate"       -- 最後交易日
    --       ,''                  AS "TitaTxCd"         -- 交易代號
    --       ,''                  AS "TitaKinBr"        -- 
    --       ,''                  AS "TitaTlrNo"        -- 經辦
    --       ,0                   AS "TitaTxtNo"        -- 交易序號
    --       ,''                  AS "JsonFields"       -- jason格式紀錄
    --       ,'999999'            AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 
    --       ,JOB_START_TIME      AS "CreateDate"          -- 建檔日期時間 DATE 8 
    --       ,'999999'            AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 
    --       ,JOB_START_TIME      AS "LastUpdate"          -- 最後更新日期時間 DATE 8 
    -- FROM (SELECT SUM("LORAMT") AS "LORAMT"
    --       FROM "LN$LORP"
    --       GROUP BY "TRXIDT") S1
    -- LEFT JOIN "CdAcCode" S2 ON S2."AcctCode" = 'T10'
    -- ;

    -- 記錄寫入筆數
    INS_CNT := INS_CNT + sql%rowcount;

    -- TCK : 暫收款-支票
    INSERT INTO "AcReceivable"
    SELECT 'TCK'               AS "AcctCode"         -- 業務科目代號
          ,LPAD(S1."CustNo",7,0)
                               AS "CustNo"           -- 戶號
          ,LPAD(NVL(S2."FacmNo",0),3,0) -- 2021-10-01 from 賴桑 DueAmt與支票金額一樣時，放該額度號碼
           -- 0 -- 2021-09-30 from 綺萍 支票的額度號碼轉0
           -- LPAD(S2."FacmNo",3,0)
                               AS "FacmNo"           -- 額度編號
          ,TRIM(TO_CHAR(S1."ChequeAcct",'000000000')) || ' ' || TRIM(TO_CHAR(S1."ChequeNo",'0000000'))
                               AS "RvNo"             -- 銷帳編號
          ,S3."AcNoCode"       AS "AcNoCode"         -- 科目代號
          ,S3."AcSubCode"      AS "AcSubCode"        -- 子目代號
          ,S3."AcDtlCode"      AS "AcDtlCode"        -- 細目代號
          ,'0000'              AS "BranchNo"         -- 單位別
          ,'TWD'               AS "CurrencyCode"     -- 幣別
          ,0                   AS "ClsFlag"          -- 銷帳記號 0:未銷 1:已銷
          ,0                   AS "AcctFlag"         -- 業務科目記號 0:一般科目 1:資負明細科目
          ,2                   AS "ReceivableFlag"   -- 銷帳科目記號 0:非銷帳科目 1:會計銷帳科目 2:業務銷帳科目 3:未收費用 4:短繳期金 5:另收欠款
          ,S1."ChequeAmt"      AS "RvAmt"            -- 起帳總額
          ,S1."ChequeAmt"      AS "RvBal"            -- 未銷餘額
          ,S1."ChequeAmt"      AS "AcBal"            -- 會計日餘額
          ,''                  AS "SlipNote"         -- 傳票摘要
          ,'000'               AS "AcBookCode"       -- 帳冊別 -- 2021-07-15 修改 000:全公司
          ,'00A'               AS "AcSubBookCode"       -- 區隔帳冊 -- 2021-07-15 新增00A:傳統帳冊、201:利變帳冊
          ,S1."EntryDate"      AS "OpenAcDate"       -- 起帳日期
          ,0                   AS "LastAcDate"       -- 最後作帳日
          ,0                   AS "LastTxDate"       -- 最後交易日
          ,''                  AS "TitaTxCd"         -- 交易代號
          ,''                  AS "TitaKinBr"        -- 
          ,''                  AS "TitaTlrNo"        -- 經辦
          ,0                   AS "TitaTxtNo"        -- 交易序號
          ,''                  AS "JsonFields"       -- jason格式紀錄
          ,'999999'            AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 
          ,JOB_START_TIME      AS "CreateDate"          -- 建檔日期時間 DATE 8 
          ,'999999'            AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 
          ,JOB_START_TIME      AS "LastUpdate"          -- 最後更新日期時間 DATE 8 
    FROM "LoanCheque" S1
    LEFT JOIN (SELECT LC."ChequeNo"
                    , LC."CustNo"
                    , F1."FacmNo"
                    , ROW_NUMBER() OVER (PARTITION BY LC."ChequeNo"
                                                    , LC."CustNo"
                                         ORDER BY F1."FacmNo" DESC) AS "Seq"
               FROM "LoanCheque" LC
               LEFT JOIN "FacMain" F1 ON F1."CustNo" = LC."CustNo"
               LEFT JOIN ( SELECT "CustNo"
                                , "FacmNo"
                                , SUM("DueAmt") AS "DueAmt"
                           FROM "LoanBorMain"
                           WHERE "Status" IN (0,2,4,6,7)
                           GROUP BY "CustNo"
                                  , "FacmNo"
                         ) L1 ON L1."CustNo" = F1."CustNo"
                             AND L1."FacmNo" = F1."FacmNo"
               WHERE LC."ChequeAmt" = NVL(L1."DueAmt",0)
              ) S2 ON S2."ChequeNo" = S1."ChequeNo"
                  AND S2."CustNo" = S1."CustNo"
                  AND S2."Seq" = 1
    LEFT JOIN "CdAcCode" S3 ON S3."AcctCode" = 'TCK'
    WHERE S1."StatusCode" IN ('0','4')
    ;

    -- 記錄寫入筆數
    INS_CNT := INS_CNT + sql%rowcount;

    /* 短繳本金 */
    -- Z10 : 短期擔保放款
    -- Z20 : 中期擔保放款
    -- Z30 : 長期擔保放款
    -- Z40 : 三十年房貸
    INSERT INTO "AcReceivable"
    SELECT CASE
             WHEN FAC."AcctCode" = '310' THEN 'Z10'
             WHEN FAC."AcctCode" = '320' THEN 'Z20'
             WHEN FAC."AcctCode" = '330' THEN 'Z30'
             WHEN FAC."AcctCode" = '340' THEN 'Z40'
           ELSE '' END          AS "AcctCode"         -- 業務科目代號
          ,LPAD(ACT."LMSACN",7,0)
                                AS "CustNo"           -- 戶號
          -- 取該戶號下尚有餘額的第一筆額度號碼
          ,LPAD(FAC."FacmNo",3,0)
                                AS "FacmNo"           -- 額度編號
          -- 取該額度下尚有餘額的第一筆撥款序號
          ,LPAD(FAC."BormNo",3,0)
                                AS "RvNo"             -- 銷帳編號
          ,C1."AcNoCode"        AS "AcNoCode"         -- 科目代號
          ,C1."AcSubCode"       AS "AcSubCode"        -- 子目代號
          ,C1."AcDtlCode"       AS "AcDtlCode"        -- 細目代號
          ,'0000'               AS "BranchNo"         -- 單位別
          ,'TWD'                AS "CurrencyCode"     -- 幣別
          ,0                    AS "ClsFlag"          -- 銷帳記號 0:未銷 1:已銷
          ,0                    AS "AcctFlag"         -- 業務科目記號 0:一般科目 1:資負明細科目
          ,C1."ReceivableFlag"  AS "ReceivableFlag"   -- 銷帳科目記號 0:非銷帳科目 1:會計銷帳科目 2:業務銷帳科目 3:未收費用 4:短繳期金 5:另收欠款
          ,ABS(ACT."LMSLPN")    AS "RvAmt"            -- 起帳總額
          ,ABS(ACT."LMSLPN")    AS "RvBal"            -- 未銷餘額
          ,ABS(ACT."LMSLPN")    AS "AcBal"            -- 會計日餘額
          ,''                   AS "SlipNote"         -- 傳票摘要
          ,'000'                AS "AcBookCode"       -- 帳冊別 -- 2021-07-15 修改 000:全公司
          ,CASE
             WHEN NVL(ACT."ACTFSC",' ') = 'A' THEN '201' -- 利變A
           ELSE '00A' END       AS "AcSubBookCode"       -- 區隔帳冊 -- 2021-07-15 新增00A:傳統帳冊、201:利變帳冊
          ,"TbsDyF"             AS "OpenAcDate"       -- 起帳日期
          ,"TbsDyF"             AS "LastAcDate"       -- 最後作帳日
          ,"TbsDyF"             AS "LastTxDate"       -- 最後交易日
          ,''                   AS "TitaTxCd"         -- 交易代號
          ,''                   AS "TitaKinBr"        -- 
          ,''                   AS "TitaTlrNo"        -- 經辦
          ,0                    AS "TitaTxtNo"        -- 交易序號
          ,''                   AS "JsonFields"       -- jason格式紀錄
          ,'999999'             AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 
          ,JOB_START_TIME       AS "CreateDate"          -- 建檔日期時間 DATE 8 
          ,'999999'             AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 
          ,JOB_START_TIME       AS "LastUpdate"          -- 最後更新日期時間 DATE 8 
    FROM "LA$ACTP" ACT
    LEFT JOIN (SELECT F1."CustNo"
                     ,F1."FacmNo"
                     ,LB."BormNo"
                     ,F1."AcctCode"
                     ,ROW_NUMBER() OVER (PARTITION BY F1."CustNo"
                                         ORDER BY F1."CustNo"
                                                 ,F1."FacmNo"
                                                 ,LB."BormNo" ) AS "Seq"
               FROM "FacMain" F1
               LEFT JOIN (SELECT "CustNo"
                                ,"FacmNo"
                                ,MIN("BormNo")  AS "BormNo"
                                ,SUM("LoanBal") AS "LoanBal"
                          FROM "LoanBorMain"
                          WHERE "LoanBal" > 0
                          GROUP BY "CustNo"
                                  ,"FacmNo"
                         ) LB ON LB."CustNo" = F1."CustNo"
                             AND LB."FacmNo" = F1."FacmNo"
               WHERE LB."LoanBal" > 0
              ) FAC ON FAC."CustNo" = ACT."LMSACN"
                   AND FAC."Seq"    = 1
    LEFT JOIN "CdAcCode" C1 ON C1."AcctCode" = FAC."AcctCode"
    WHERE ACT."LMSLPN" < 0
      AND FAC."CustNo" > 0
      AND FAC."AcctCode" IN ('310','320','330','340')
    ;

    INS_CNT := INS_CNT + sql%rowcount;

    /* 短繳利息 */

    -- IC1 : 短期擔保放款
    -- IC2 : 中期擔保放款
    -- IC3 : 長期擔保放款
    -- IC4 : 三十年房貸
    INSERT INTO "AcReceivable"
    SELECT CASE
             WHEN FAC."AcctCode" = '310' THEN 'IC1'
             WHEN FAC."AcctCode" = '320' THEN 'IC2'
             WHEN FAC."AcctCode" = '330' THEN 'IC3'
             WHEN FAC."AcctCode" = '340' THEN 'IC4'
           ELSE '' END          AS "AcctCode"         -- 業務科目代號
          ,LPAD(LMSP."LMSACN",7,0)
                                AS "CustNo"           -- 戶號
          -- 取該戶號下尚有餘額的第一筆額度號碼
          ,LPAD(LMSP."LMSAPN",3,0)
                                AS "FacmNo"           -- 額度編號
          -- 取該額度下尚有餘額的第一筆撥款序號
          ,LPAD(LMSP."LMSASQ",3,0)
                                AS "RvNo"             -- 銷帳編號
          ,C1."AcNoCode"        AS "AcNoCode"         -- 科目代號 -- 2021-07-15 修改為11碼新會科
          ,C1."AcSubCode"       AS "AcSubCode"        -- 子目代號
          ,C1."AcDtlCode"       AS "AcDtlCode"        -- 細目代號
          ,'0000'               AS "BranchNo"         -- 單位別
          ,'TWD'                AS "CurrencyCode"     -- 幣別
          ,0                    AS "ClsFlag"          -- 銷帳記號 0:未銷 1:已銷
          ,0                    AS "AcctFlag"         -- 業務科目記號 0:一般科目 1:資負明細科目
          ,C1."ReceivableFlag"  AS "ReceivableFlag"   -- 銷帳科目記號 0:非銷帳科目 1:會計銷帳科目 2:業務銷帳科目 3:未收費用 4:短繳期金 5:另收欠款
          ,LMSP."LMSLIN"        AS "RvAmt"            -- 起帳總額
          ,LMSP."LMSLIN"        AS "RvBal"            -- 未銷餘額
          ,LMSP."LMSLIN"        AS "AcBal"            -- 會計日餘額
          ,''                   AS "SlipNote"         -- 傳票摘要
          ,'000'                AS "AcBookCode"       -- 帳冊別 -- 2021-07-15 修改為000:全公司
          ,CASE
             WHEN NVL(ACT."ACTFSC",' ') = 'A' THEN '201' -- 利變A
           ELSE '00A' END       AS "AcSubBookCode"       -- 區隔帳冊 -- 2021-07-15 新增00A:傳統帳冊、201:利變帳冊
          ,"TbsDyF"             AS "OpenAcDate"       -- 起帳日期
          ,"TbsDyF"             AS "LastAcDate"       -- 最後作帳日
          ,"TbsDyF"             AS "LastTxDate"       -- 最後交易日
          ,''                   AS "TitaTxCd"         -- 交易代號
          ,''                   AS "TitaKinBr"        -- 
          ,''                   AS "TitaTlrNo"        -- 經辦
          ,0                    AS "TitaTxtNo"        -- 交易序號
          ,''                   AS "JsonFields"       -- jason格式紀錄
          ,'999999'             AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 
          ,JOB_START_TIME       AS "CreateDate"          -- 建檔日期時間 DATE 8 
          ,'999999'             AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 
          ,JOB_START_TIME       AS "LastUpdate"          -- 最後更新日期時間 DATE 8 
    FROM "LA$LMSP" LMSP
    LEFT JOIN "FacMain" FAC ON FAC."CustNo" = LMSP."LMSACN"
                           AND FAC."FacmNo" = LMSP."LMSAPN"
    LEFT JOIN "CdAcCode" C1 ON C1."AcctCode" = CASE
                                                 WHEN FAC."AcctCode" = '310' THEN 'IC1'
                                                 WHEN FAC."AcctCode" = '320' THEN 'IC2'
                                                 WHEN FAC."AcctCode" = '330' THEN 'IC3'
                                                 WHEN FAC."AcctCode" = '340' THEN 'IC4'
                                               ELSE '' END
    LEFT JOIN "LA$ACTP" ACT ON ACT."LMSACN" = LMSP."LMSACN"
    WHERE LMSP."LMSLIN" > 0
      AND FAC."CustNo" > 0
      AND FAC."AcctCode" IN ('310','320','330','340')
    ;

    INS_CNT := INS_CNT + sql%rowcount;

    -- 記錄程式結束時間
    JOB_END_TIME := SYSTIMESTAMP;

    commit;

    END;

    -- 例外處理
    Exception
    WHEN OTHERS THEN
    ERROR_MSG := SQLERRM || CHR(13) || CHR(10) || dbms_utility.format_error_backtrace;
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_AcReceivable_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;





/
