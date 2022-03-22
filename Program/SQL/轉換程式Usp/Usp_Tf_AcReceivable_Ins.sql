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
           ELSE LPAD(NVL(L1."BormNo",0),3,0) END
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
             WHEN L1."Status" = 99
             THEN 0
             WHEN NVL(L2."BormNo",0) > 0 
             THEN L2."OvduAmt"
           ELSE NVL(L1."DrawdownAmt",0) END
                                AS "RvAmt"            -- 起帳總額
          ,CASE
             WHEN L1."Status" = 99
             THEN 0
             WHEN NVL(L2."BormNo",0) > 0 
             THEN L2."OvduBal"
           ELSE NVL(L1."LoanBal",0) END
                                AS "RvBal"            -- 未銷餘額
          ,CASE
             WHEN L1."Status" = 99
             THEN 0
             WHEN NVL(L2."BormNo",0) > 0 
             THEN L2."OvduBal"
           ELSE NVL(L1."LoanBal",0) END
                                AS "AcBal"            -- 會計日餘額
          ,''                   AS "SlipNote"         -- 傳票摘要
          ,'000'                AS "AcBookCode"       -- 帳冊別 -- 2021-07-15 修改 000:全公司
          ,CASE
             WHEN NVL(ACT."ACTFSC",' ') = 'A' THEN '201' -- 利變A
           ELSE '00A' END       AS "AcSubBookCode"       -- 區隔帳冊 -- 2021-07-15 新增00A:傳統帳冊、201:利變帳冊
          ,CASE
             WHEN L1."Status" = 99
             THEN "TbsDyF"
             WHEN NVL(L2."BormNo",0) > 0 
             THEN L2."OvduDate"
           ELSE NVL(L1."DrawdownDate","TbsDyF") END
                                AS "OpenAcDate"       -- 起帳日期
          ,CASE
             WHEN L1."Status" = 99
             THEN "TbsDyF"
             WHEN NVL(L2."BormNo",0) > 0 
             THEN L2."AcDate"
           ELSE NVL(L1."LastEntDy","TbsDyF") END
                                AS "LastAcDate"       -- 最後作帳日
          ,CASE
             WHEN L1."Status" = 99
             THEN "TbsDyF"
             WHEN NVL(L2."BormNo",0) > 0 
             THEN L2."AcDate"
           ELSE NVL(L1."LastEntDy","TbsDyF") END
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
          ,''                   AS "OpenTxCd" -- 起帳交易代號 VARCHAR2 5
          ,'0000'               AS "OpenKinBr" -- 起帳單位別 VARCHAR2 4
          ,'999999'             AS "OpenTlrNo" -- 起帳經辦 VARCHAR2 6
          ,0                    AS "OpenTxtNo" -- 起帳交易序號 DECIMAL 8
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
    WHERE CASE
            WHEN F1."LastBormNo" >= 1
                 AND NVL(NVL(L2."OvduNo",L1."BormNo"),0) > 0 -- 已撥款
            THEN 1
            WHEN NVL(ACT."ACTFSC",' ') = 'A' -- 未撥款但區隔帳冊為利變A
            THEN 1
          ELSE 0 
          END = 1
      -- AND L1."Status" != 99 -- 排除預約撥款
    ;

    -- 記錄寫入筆數
    INS_CNT := INS_CNT + sql%rowcount;

    -- TMI : 火險保費
    -- F09 : 暫付火險保費
    -- F25 : 催收款項-火險費用
    INSERT INTO "AcReceivable"
    SELECT CASE
             WHEN S1."StatusCode" = 2
             THEN 'F25'
             WHEN S1."StatusCode" = 1
             THEN 'F09'
             WHEN S1."StatusCode" = 0 
                  -- AND S1."NotiTempFg" = 'Y' -- 2022-03-21 from Lai
                  AND S1."TotInsuPrem" != 0  -- 2022-03-21 from Lai
             THEN 'TMI'
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
             WHEN S1."StatusCode" = 0 AND S1."NotiTempFg" = 'Y' THEN S1."InsuYearMonth" * 100 + 1
             WHEN S1."StatusCode" = 1 THEN S1."InsuYearMonth" * 100 + 1
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
          ,''                   AS "OpenTxCd" -- 起帳交易代號 VARCHAR2 5
          ,'0000'               AS "OpenKinBr" -- 起帳單位別 VARCHAR2 4
          ,'999999'             AS "OpenTlrNo" -- 起帳經辦 VARCHAR2 6
          ,0                    AS "OpenTxtNo" -- 起帳交易序號 DECIMAL 8
    FROM "InsuRenew" S1
    LEFT JOIN "CdAcCode" S2 ON S2."AcctCode" = CASE
                                                 WHEN S1."StatusCode" = 0
                                                      AND S1."TotInsuPrem" != 0
                                                 THEN 'TMI'
                                                 WHEN S1."StatusCode" = 1
                                                 THEN 'F09'
                                                 WHEN S1."StatusCode" = 2
                                                 THEN 'F25'
                                               ELSE ' ' END
    WHERE S1."AcDate" = 0
      AND S1."TotInsuPrem" > 0
      AND CASE
            WHEN S1."StatusCode" = 0 
                 -- AND S1."NotiTempFg" = 'Y' -- 2022-03-21 from Lai
                 AND S1."TotInsuPrem" != 0  -- 2022-03-21 from Lai
            WHEN S1."StatusCode" = 1
            THEN 'Y'
            WHEN S1."StatusCode" = 2
            THEN 'Y'
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
           ELSE S1."OpenAcDate" END
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
          ,''                   AS "OpenTxCd" -- 起帳交易代號 VARCHAR2 5
          ,'0000'               AS "OpenKinBr" -- 起帳單位別 VARCHAR2 4
          ,'999999'             AS "OpenTlrNo" -- 起帳經辦 VARCHAR2 6
          ,0                    AS "OpenTxtNo" -- 起帳交易序號 DECIMAL 8
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
          ,NVL(S2."LMSLLD",0)  AS "OpenAcDate"       -- 起帳日期
          ,S1."TRXDAT"         AS "LastAcDate"       -- 最後作帳日
          ,S1."TRXDAT"         AS "LastTxDate"       -- 最後交易日
          ,''                  AS "TitaTxCd"         -- 交易代號
          ,''                  AS "TitaKinBr"        -- 
          ,''                  AS "TitaTlrNo"        -- 經辦
          ,0                   AS "TitaTxtNo"        -- 交易序號
          ,''                  AS "JsonFields"       -- jason格式紀錄
          ,'999999'            AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 
          ,JOB_START_TIME      AS "CreateDate"          -- 建檔日期時間 DATE 8 
          ,'999999'            AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 
          ,JOB_START_TIME      AS "LastUpdate"          -- 最後更新日期時間 DATE 8 
          ,''                   AS "OpenTxCd" -- 起帳交易代號 VARCHAR2 5
          ,'0000'               AS "OpenKinBr" -- 起帳單位別 VARCHAR2 4
          ,'999999'             AS "OpenTlrNo" -- 起帳經辦 VARCHAR2 6
          ,0                    AS "OpenTxtNo" -- 起帳交易序號 DECIMAL 8
    FROM "LN$ACFP" S1
    LEFT JOIN "LA$LMSP" S2 ON S2."LMSACN" = S1."LMSACN"
                          AND S2."LMSAPN" = S1."LMSAPN"
                          AND S2."LMSASQ" = S1."LMSASQ"
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
          ,CASE
             WHEN S1."TRXDAT" != 0
             THEN 1
           ELSE 0
           END                 AS "ClsFlag"          -- 銷帳記號 0:未銷 1:已銷
          ,0                   AS "AcctFlag"         -- 業務科目記號 0:一般科目 1:資負明細科目
          ,3                   AS "ReceivableFlag"   -- 銷帳科目記號 0:非銷帳科目 1:會計銷帳科目 2:業務銷帳科目 3:未收費用 4:短繳期金 5:另收欠款
          ,S1."CFRAMT"         AS "RvAmt"            -- 起帳總額
          ,CASE
             WHEN S1."TRXDAT" != 0
             THEN 0
           ELSE S1."CFRAMT"
           END                 AS "RvBal"            -- 未銷餘額
          ,CASE
             WHEN S1."TRXDAT" != 0
             THEN 0
           ELSE S1."CFRAMT"
           END                 AS "AcBal"            -- 會計日餘額
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
          ,''                   AS "OpenTxCd" -- 起帳交易代號 VARCHAR2 5
          ,'0000'               AS "OpenKinBr" -- 起帳單位別 VARCHAR2 4
          ,'999999'             AS "OpenTlrNo" -- 起帳經辦 VARCHAR2 6
          ,0                    AS "OpenTxtNo" -- 起帳交易序號 DECIMAL 8
    FROM "LN$CFRP" S1
    LEFT JOIN "As400EmpNoMapping" AEM ON AEM."As400TellerNo" = S1."CFPMEM"
    ;

    -- 記錄寫入筆數
    INS_CNT := INS_CNT + sql%rowcount;

    -- TAV : 暫收款-可抵繳
    -- 2022-03-02 轉入該戶號下依條件排序後之第一額度
    -- 排序:依應繳日順序由小到大、利率順序由大到小、額度由小到大
    INSERT INTO "AcReceivable"
    WITH ACT AS (
      -- 篩選出基本資料
      -- 條件1:排除戶號為601776
      -- 條件2:BKPDAT = 轉換基準日
      SELECT ACTP.BKPDAT
           , ACTP.LMSACN
           , ACTP.LMSTOA
      FROM LADACTP ACTP
      WHERE ACTP.LMSACN != 601776
        AND ACTP.BKPDAT = "TbsDyF"
    )
    , OrderedFacmNo AS (
      SELECT "CustNo"
           , "FacmNo"
           , ROW_NUMBER ()
             OVER (
                PARTITION BY "CustNo"
                ORDER BY CASE
                           -- 0:正常戶
                           -- 1:展期
                           -- 2:催收戶
                           -- 3:結案戶
                           -- 4:逾期戶(顯示用)
                           -- 5:催收結案戶
                           -- 6:呆帳戶
                           -- 7:部分轉呆戶
                           -- 8:債權轉讓戶
                           -- 9:呆帳結案戶
                           -- 97:預約撥款已刪除
                           -- 98:預約已撥款
                           -- 99:預約撥款
                           WHEN "Status" IN (0,1,2,4,6,7) 
                           THEN "Status"
                         ELSE 100 + "Status" 
                         END ASC -- 戶況取未結案、非預約優先
                       , "NextPayIntDate" ASC
                       , "FacmNo" ASC
             ) AS "FacmNoSeq"
      FROM "LoanBorMain"
    )
    , TMP AS (
      -- 依照額度放款餘額佔戶號放款餘額的比例
      -- 將費用分配到各個額度
      SELECT ACT.BKPDAT
           , ACT.LMSACN
           , ACT.LMSTOA
           , NVL(OFN."FacmNo",1) AS "FacmNo"
      FROM ACT
      LEFT JOIN OrderedFacmNo OFN ON OFN."CustNo" = ACT.LMSACN
                                 AND OFN."FacmNoSeq" = 1
      WHERE ACT.LMSTOA > 0 -- 有費用的才做
    )
    SELECT 'TAV'               AS "AcctCode"         -- 業務科目代號
          ,LPAD(S1."LMSACN",7,0)
                               AS "CustNo"           -- 戶號
          ,LPAD(S1."FacmNo",3,0)
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
          ,''                   AS "OpenTxCd" -- 起帳交易代號 VARCHAR2 5
          ,'0000'               AS "OpenKinBr" -- 起帳單位別 VARCHAR2 4
          ,'999999'             AS "OpenTlrNo" -- 起帳經辦 VARCHAR2 6
          ,0                    AS "OpenTxtNo" -- 起帳交易序號 DECIMAL 8
    FROM TMP S1
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
          ,''                   AS "OpenTxCd" -- 起帳交易代號 VARCHAR2 5
          ,'0000'               AS "OpenKinBr" -- 起帳單位別 VARCHAR2 4
          ,'999999'             AS "OpenTlrNo" -- 起帳經辦 VARCHAR2 6
          ,0                    AS "OpenTxtNo" -- 起帳交易序號 DECIMAL 8
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

    -- TCK : 暫收款-支票
    INSERT INTO "AcReceivable"
    SELECT 'TCK'               AS "AcctCode"         -- 業務科目代號
          ,LPAD(S1."CustNo",7,0)
                               AS "CustNo"           -- 戶號
          ,LPAD(NVL(S2."FacmNo",1),3,0) -- 2021-10-01 from 賴桑 DueAmt與支票金額一樣時，放該額度號碼
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
          ,S1."ReceiveDate"    AS "OpenAcDate"       -- 起帳日期
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
          ,''                   AS "OpenTxCd" -- 起帳交易代號 VARCHAR2 5
          ,'0000'               AS "OpenKinBr" -- 起帳單位別 VARCHAR2 4
          ,'999999'             AS "OpenTlrNo" -- 起帳經辦 VARCHAR2 6
          ,0                    AS "OpenTxtNo" -- 起帳交易序號 DECIMAL 8
    FROM "LoanCheque" S1
    LEFT JOIN (
      SELECT LC."ChequeNo"
           , LC."ChequeAcct"
           , LC."CustNo"
           , L1."FacmNo"
           , LC."ChequeAmt"
           , NVL(L1."DueAmt",0) AS "DueAmt"
           , ABS(LC."ChequeAmt" - NVL(L1."DueAmt",0)) AS "Match" -- 吻合程度,越低者越高
           , ROW_NUMBER()
             OVER (
               PARTITION BY LC."ChequeNo"
                          , LC."ChequeAcct"                    
                         , LC."CustNo"
               ORDER BY ABS(LC."ChequeAmt" - NVL(L1."DueAmt",0))
                      , L1."FacmNo"
             ) AS "MatchSeq"
      FROM "LoanCheque" LC
      LEFT JOIN (
        SELECT "CustNo"
             , "FacmNo"
             , SUM("DueAmt") AS "DueAmt"
        FROM "LoanBorMain"
        WHERE "Status" IN (0,2,4,6,7)
        GROUP BY "CustNo"
               , "FacmNo"
      ) L1 ON L1."CustNo" = LC."CustNo"
      WHERE LC."StatusCode" IN ('0','4')
    ) S2 ON S2."ChequeNo" = S1."ChequeNo"
        AND S2."ChequeAcct" = S1."ChequeAcct"
        AND S2."CustNo" = S1."CustNo"
        AND S2."MatchSeq" = 1
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
          ,LPAD(LMSP."LMSACN",7,0)
                                AS "CustNo"           -- 戶號
          -- 取該戶號下尚有餘額的第一筆額度號碼
          ,LPAD(LMSP."LMSAPN",3,0)
                                AS "FacmNo"           -- 額度編號
          -- 取該額度下尚有餘額的第一筆撥款序號
          ,LPAD(LMSP."LMSASQ",3,0)
                                AS "RvNo"             -- 銷帳編號
          ,C1."AcNoCode"        AS "AcNoCode"         -- 科目代號
          ,C1."AcSubCode"       AS "AcSubCode"        -- 子目代號
          ,C1."AcDtlCode"       AS "AcDtlCode"        -- 細目代號
          ,'0000'               AS "BranchNo"         -- 單位別
          ,'TWD'                AS "CurrencyCode"     -- 幣別
          ,0                    AS "ClsFlag"          -- 銷帳記號 0:未銷 1:已銷
          ,0                    AS "AcctFlag"         -- 業務科目記號 0:一般科目 1:資負明細科目
          ,4                    AS "ReceivableFlag"   -- 銷帳科目記號 0:非銷帳科目 1:會計銷帳科目 2:業務銷帳科目 3:未收費用 4:短繳期金 5:另收欠款
          ,LMSP."LMSLPN"        AS "RvAmt"            -- 起帳總額
          ,LMSP."LMSLPN"        AS "RvBal"            -- 未銷餘額
          ,LMSP."LMSLPN"        AS "AcBal"            -- 會計日餘額
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
          ,''                   AS "OpenTxCd" -- 起帳交易代號 VARCHAR2 5
          ,'0000'               AS "OpenKinBr" -- 起帳單位別 VARCHAR2 4
          ,'999999'             AS "OpenTlrNo" -- 起帳經辦 VARCHAR2 6
          ,0                    AS "OpenTxtNo" -- 起帳交易序號 DECIMAL 8
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
    WHERE LMSP."LMSLPN" > 0
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
          ,4                    AS "ReceivableFlag"   -- 銷帳科目記號 0:非銷帳科目 1:會計銷帳科目 2:業務銷帳科目 3:未收費用 4:短繳期金 5:另收欠款
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
          ,''                   AS "OpenTxCd" -- 起帳交易代號 VARCHAR2 5
          ,'0000'               AS "OpenKinBr" -- 起帳單位別 VARCHAR2 4
          ,'999999'             AS "OpenTlrNo" -- 起帳經辦 VARCHAR2 6
          ,0                    AS "OpenTxtNo" -- 起帳交易序號 DECIMAL 8
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
END;
/
