--------------------------------------------------------
--  DDL for Procedure Usp_Tf_MonthlyFacBal_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_Tf_MonthlyFacBal_Ins" 
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
        "DateStart" DECIMAL(6) := 0 ; -- 資料擷取起日
        "DateEnd"   DECIMAL(6) := 0 ; -- 資料擷取止日
    BEGIN

        SELECT "TbsDy" + 19110000
        INTO "TbsDyF"
        FROM "TxBizDate"
        WHERE "DateCode" = 'ONLINE'
        ;

        -- 刪除舊資料
        IF "ExecSeq" = 1 THEN
          EXECUTE IMMEDIATE 'ALTER TABLE "MonthlyFacBal" DISABLE PRIMARY KEY CASCADE';
          EXECUTE IMMEDIATE 'TRUNCATE TABLE "MonthlyFacBal" DROP STORAGE';
          EXECUTE IMMEDIATE 'ALTER TABLE "MonthlyFacBal" ENABLE PRIMARY KEY';
        END IF;

        SELECT "StartMonth"
        INTO "DateStart"
        FROM "TfByYear"
        WHERE "Seq" = "ExecSeq"
        ;

        SELECT "EndMonth"
        INTO "DateEnd"
        FROM "TfByYear"
        WHERE "Seq" = "ExecSeq"
        ;

        -- 寫入資料
        INSERT INTO "MonthlyFacBal" (
            "YearMonth"           -- 資料年月 DECIMAL 6 0
          , "CustNo"              -- 戶號 DECIMAL 7 0
          , "FacmNo"              -- 額度 DECIMAL 3 0
          , "PrevIntDate"         -- 繳息迄日 DecimalD 8 0
          , "NextIntDate"         -- 應繳息日 DecimalD 8 0
          , "DueDate"             -- 最近應繳日
          , "OvduTerm"            -- 逾期期數 DECIMAL 3 0
          , "OvduDays"            -- 逾期天數 DECIMAL 6 0
          , "CurrencyCode"        -- 幣別 VARCHAR2 3 0
          , "PrinBalance"         -- 本金餘額 DECIMAL 16 2
          , "BadDebtBal"          -- 呆帳餘額 DECIMAL 16 2
          , "AccCollPsn"          -- 催收員 VARCHAR2 6 0
          , "LegalPsn"            -- 法務人員 VARCHAR2 6 0
          , "Status"              -- 戶況 DECIMAL 2 0
          , "AcctCode"            -- 業務科目代號 VARCHAR2 3 0
          , "FacAcctCode"         -- 額度業務科目 VARCHAR2 3 0
          , "ClCustNo"            -- 同擔保品戶號 DECIMAL 7 0
          , "ClFacmNo"            -- 同擔保品額度 DECIMAL 3 0
          , "ClRowNo"             -- 同擔保品序列號 DECIMAL 3 0
          , "RenewCode"           -- 展期記號 VARCHAR2 1 0
          , "ProdNo"              -- 商品代碼
          , "AcBookCode"          -- 帳冊別 VARCHAR2 3 0
          , "EntCode"             -- 企金別 VARCHAR2 1 
          , "RelsCode"            -- (準)利害關係人職稱 VARCHAR2 2 0
          , "DepartmentCode"      -- 案件隸屬單位 VARCHAR2 1 0
          , "UnpaidPrincipal"     -- 已到期回收本金 DECIMAL 16 2
          , "UnpaidInterest"      -- 已到期利息 DECIMAL 16 2
          , "UnpaidBreachAmt"     -- 已到期違約金 DECIMAL 16 2
          , "UnpaidDelayInt"      -- 已到期延滯息 DECIMAL 16 2
          , "AcdrPrincipal"       -- 未到期回收本金 DECIMAL 16 2
          , "AcdrInterest"        -- 未到期利息 DECIMAL 16 2
          , "AcdrBreachAmt"       -- 未到期違約金 DECIMAL 16 2
          , "AcdrDelayInt"        -- 未到期延滯息 DECIMAL 16 2
          , "FireFee"             -- 火險費用 DECIMAL 16 2
          , "LawFee"              -- 法務費用 DECIMAL 16 2
          , "ModifyFee"           -- 契變手續費 DECIMAL 16 2
          , "AcctFee"             -- 帳管費用 DECIMAL 16 2
          , "ShortfallPrin"       -- 短繳本金 DECIMAL 16 2
          , "ShortfallInt"        -- 短繳利息 DECIMAL 16 2
          , "TempAmt"             -- 暫收金額 DECIMAL 16 2
          , "ClCode1"             -- 主要擔保品代號1 DECIMAL 1 
          , "ClCode2"             -- 主要擔保品代號2 DECIMAL 2 
          , "ClNo"                -- 主要擔保品編號 DECIMAL 7 0
          , "CityCode"            -- 主要擔保品地區別 VARCHAR2 2 
          , "OvduDate"            -- 轉催收日期 DECIMALD 8 0
          , "OvduPrinBal"         -- 催收本金餘額 DECIMAL 16 2
          , "OvduIntBal"          -- 催收利息餘額 DECIMAL 16 2
          , "OvduBreachBal"       -- 催收違約金餘額 DECIMAL 16 2
          , "OvduBal"             -- 催收餘額 DECIMAL 16 2
          , "LawAmount"           -- 法催擔保金額 DECIMAL 16 2
          , "AssetClass"          -- 資產五分類代號 VARCHAR2 2 0
          , "StoreRate"           -- 計息利率 DECIMAL 6 4
          , "CreateDate"          -- 建檔日期時間 DATE 8 0
          , "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 0
          , "LastUpdate"          -- 最後更新日期時間 DATE 8 0
          , "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 0
          , "AcSubBookCode"        -- 區隔帳冊別 VARCHAR2 3 0
        )
        SELECT DLY."ADTYMT"                   AS "YearMonth"           -- 資料年月 DECIMAL 6 0
              ,DLY."LMSACN"                   AS "CustNo"              -- 戶號 DECIMAL 7 0
              ,DLY."LMSAPN"                   AS "FacmNo"              -- 額度 DECIMAL 3 0
              ,DLY."W08LPD"                   AS "PrevIntDate"         -- 繳息迄日 DecimalD 8 0
              ,TO_NUMBER(TO_CHAR(ADD_MONTHS(TO_DATE(TO_CHAR(DLY."ADTYMT" * 100 + 1),'YYYYMMDD'),1) - 1 - DLY."W08DLY",'YYYYMMDD'))
                                              AS "NextIntDate"         -- 應繳息日 DecimalD 8 0
              -- 最近應繳日:已到期又逾期時,使用月底日曆日(SKL待查)
              ,DLY."W08NPD"                   AS "DueDate"             -- 最近應繳日
              ,DLY."W08PPR"                   AS "OvduTerm"            -- 逾期期數 DECIMAL 3 0
              ,DLY."W08DLY"                   AS "OvduDays"            -- 逾期天數 DECIMAL 6 0
              ,'TWD'                          AS "CurrencyCode"        -- 幣別 VARCHAR2 3 0
              ,DLY."W08LBL"                   AS "PrinBalance"         -- 本金餘額 DECIMAL 16 2
              ,0                              AS "BadDebtBal"          -- 呆帳餘額 DECIMAL 16 2
              ,DLY."W08EM5"                   AS "AccCollPsn"          -- 催收員 VARCHAR2 6 0
              ,''                             AS "LegalPsn"            -- 法務人員 VARCHAR2 6 0
              ,0                              AS "Status"              -- 戶況 DECIMAL 2 0
              ,FAC."AcctCode"                 AS "AcctCode"            -- 業務科目代號 VARCHAR2 3 0
              ,FAC."AcctCode"                 AS "FacAcctCode"         -- 額度業務科目 VARCHAR2 3 0
              ,0                              AS "ClCustNo"            -- 同擔保品戶號 DECIMAL 7 0
              ,0                              AS "ClFacmNo"            -- 同擔保品額度 DECIMAL 3 0
              ,0                              AS "ClRowNo"             -- 同擔保品序列號 DECIMAL 3 0
              ,''                             AS "RenewCode"           -- 展期記號 VARCHAR2 1 0
              ,FAC."ProdNo"                   AS "ProdNo"              -- 商品代碼
              ,'000'                          AS "AcBookCode"          -- 帳冊別 VARCHAR2 3 0
              ,CU."EntCode"                   AS "EntCode"             -- 企金別 VARCHAR2 1 
              ,''                             AS "RelsCode"            -- (準)利害關係人職稱 VARCHAR2 2 0
              ,FAC."DepartmentCode"           AS "DepartmentCode"      -- 案件隸屬單位 VARCHAR2 1 0
              ,DLY."W08PRN"                   AS "UnpaidPrincipal"     -- 已到期回收本金 DECIMAL 16 2
              ,DLY."W08INS"                   AS "UnpaidInterest"      -- 已到期利息 DECIMAL 16 2
              ,DLY."W08BCA"                   AS "UnpaidBreachAmt"     -- 已到期違約金 DECIMAL 16 2
              ,0                              AS "UnpaidDelayInt"      -- 已到期延滯息 DECIMAL 16 2
              ,0                              AS "AcdrPrincipal"       -- 未到期回收本金 DECIMAL 16 2
              ,0                              AS "AcdrInterest"        -- 未到期利息 DECIMAL 16 2
              ,0                              AS "AcdrBreachAmt"       -- 未到期違約金 DECIMAL 16 2
              ,0                              AS "AcdrDelayInt"        -- 未到期延滯息 DECIMAL 16 2
              ,0                              AS "FireFee"             -- 火險費用 DECIMAL 16 2
              ,0                              AS "LawFee"              -- 法務費用 DECIMAL 16 2
              ,0                              AS "ModifyFee"           -- 契變手續費 DECIMAL 16 2
              ,0                              AS "AcctFee"             -- 帳管費用 DECIMAL 16 2
              ,DLY."W08LPN"                   AS "ShortfallPrin"       -- 短繳本金 DECIMAL 16 2
              ,DLY."W08LIN"                   AS "ShortfallInt"        -- 短繳利息 DECIMAL 16 2
              ,CASE
                  WHEN DLY."W08TOS" > 0 THEN DLY."W08TOS"
               ELSE 0 END                     AS "TempAmt"             -- 暫收金額 DECIMAL 16 2
              ,NVL(CF."ClCode1",0)            AS "ClCode1"             -- 主要擔保品代號1 DECIMAL 1 
              ,NVL(CF."ClCode2",0)            AS "ClCode2"             -- 主要擔保品代號2 DECIMAL 2 
              ,NVL(CF."ClNo",0)               AS "ClNo"                -- 主要擔保品編號 DECIMAL 7 0
              ,NVL(CM."CityCode",'')          AS "CityCode"            -- 主要擔保品地區別 VARCHAR2 2 
              ,0                              AS "OvduDate"            -- 轉催收日期 DECIMALD 8 0
              ,0                              AS "OvduPrinBal"         -- 催收本金餘額 DECIMAL 16 2
              ,0                              AS "OvduIntBal"          -- 催收利息餘額 DECIMAL 16 2
              ,0                              AS "OvduBreachBal"       -- 催收違約金餘額 DECIMAL 16 2
              ,0                              AS "OvduBal"             -- 催收餘額 DECIMAL 16 2
              ,0                              AS "LawAmount"           -- 法催擔保金額 DECIMAL 16 2
              ,''                             AS "AssetClass"          -- 資產五分類代號 VARCHAR2 2 0
              ,0                              AS "StoreRate"           -- 計息利率 DECIMAL 6 4
              ,JOB_START_TIME                 AS "CreateDate"          -- 建檔日期時間 DATE 8 0
              ,'999999'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 0
              ,JOB_START_TIME                 AS "LastUpdate"          -- 最後更新日期時間 DATE 8 0
              ,'999999'                       AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 0
              ,CASE
                 WHEN NVL(ACT."ACTFSC",' ') = 'A' THEN '201'
               ELSE '00A' END                 AS "AcSubBookCode"        -- 區隔帳冊別 VARCHAR2 3 0
        FROM "LNMDLYP" DLY
        LEFT JOIN "CustMain" CU ON CU."CustNo" = DLY."LMSACN"
        LEFT JOIN "FacMain" FAC ON FAC."CustNo" = DLY."LMSACN"
                               AND FAC."FacmNo" = DLY."LMSAPN"
        LEFT JOIN "ClFac" CF ON CF."CustNo" = FAC."CustNo"
                            AND CF."FacmNo" = FAC."FacmNo"
                            AND CF."MainFlag" = 'Y'
        LEFT JOIN "ClMain" CM ON CM."ClCode1" = CF."ClCode1"
                             AND CM."ClCode2" = CF."ClCode2"
                             AND CM."ClNo"    = CF."ClNo"
                             AND NVL(CF."ClNo",0) > 0
        LEFT JOIN "LA$ACTP" ACT ON ACT."LMSACN" = DLY."LMSACN"
        WHERE DLY."ADTYMT" >= "DateStart"
          AND DLY."ADTYMT" <= "DateEnd"
        ;

        -- 記錄寫入筆數
        INS_CNT := INS_CNT + sql%rowcount;

        -- 寫入資料
        INSERT INTO "MonthlyFacBal" (
            "YearMonth"           -- 資料年月 DECIMAL 6 0
          , "CustNo"              -- 戶號 DECIMAL 7 0
          , "FacmNo"              -- 額度 DECIMAL 3 0
          , "PrevIntDate"         -- 繳息迄日 DecimalD 8 0
          , "NextIntDate"         -- 應繳息日 DecimalD 8 0
          , "DueDate"             -- 最近應繳日
          , "OvduTerm"            -- 逾期期數 DECIMAL 3 0
          , "OvduDays"            -- 逾期天數 DECIMAL 6 0
          , "CurrencyCode"        -- 幣別 VARCHAR2 3 0
          , "PrinBalance"         -- 本金餘額 DECIMAL 16 2
          , "BadDebtBal"          -- 呆帳餘額 DECIMAL 16 2
          , "AccCollPsn"          -- 催收員 VARCHAR2 6 0
          , "LegalPsn"            -- 法務人員 VARCHAR2 6 0
          , "Status"              -- 戶況 DECIMAL 2 0
          , "AcctCode"            -- 業務科目代號 VARCHAR2 3 0
          , "FacAcctCode"         -- 額度業務科目 VARCHAR2 3 0
          , "ClCustNo"            -- 同擔保品戶號 DECIMAL 7 0
          , "ClFacmNo"            -- 同擔保品額度 DECIMAL 3 0
          , "ClRowNo"             -- 同擔保品序列號 DECIMAL 3 0
          , "RenewCode"           -- 展期記號 VARCHAR2 1 0
          , "ProdNo"              -- 商品代碼
          , "AcBookCode"          -- 帳冊別 VARCHAR2 3 0
          , "EntCode"             -- 企金別 VARCHAR2 1 
          , "RelsCode"            -- (準)利害關係人職稱 VARCHAR2 2 0
          , "DepartmentCode"      -- 案件隸屬單位 VARCHAR2 1 0
          , "UnpaidPrincipal"     -- 已到期回收本金 DECIMAL 16 2
          , "UnpaidInterest"      -- 已到期利息 DECIMAL 16 2
          , "UnpaidBreachAmt"     -- 已到期違約金 DECIMAL 16 2
          , "UnpaidDelayInt"      -- 已到期延滯息 DECIMAL 16 2
          , "AcdrPrincipal"       -- 未到期回收本金 DECIMAL 16 2
          , "AcdrInterest"        -- 未到期利息 DECIMAL 16 2
          , "AcdrBreachAmt"       -- 未到期違約金 DECIMAL 16 2
          , "AcdrDelayInt"        -- 未到期延滯息 DECIMAL 16 2
          , "FireFee"             -- 火險費用 DECIMAL 16 2
          , "LawFee"              -- 法務費用 DECIMAL 16 2
          , "ModifyFee"           -- 契變手續費 DECIMAL 16 2
          , "AcctFee"             -- 帳管費用 DECIMAL 16 2
          , "ShortfallPrin"       -- 短繳本金 DECIMAL 16 2
          , "ShortfallInt"        -- 短繳利息 DECIMAL 16 2
          , "TempAmt"             -- 暫收金額 DECIMAL 16 2
          , "ClCode1"             -- 主要擔保品代號1 DECIMAL 1 
          , "ClCode2"             -- 主要擔保品代號2 DECIMAL 2 
          , "ClNo"                -- 主要擔保品編號 DECIMAL 7 0
          , "CityCode"            -- 主要擔保品地區別 VARCHAR2 2 
          , "OvduDate"            -- 轉催收日期 DECIMALD 8 0
          , "OvduPrinBal"         -- 催收本金餘額 DECIMAL 16 2
          , "OvduIntBal"          -- 催收利息餘額 DECIMAL 16 2
          , "OvduBreachBal"       -- 催收違約金餘額 DECIMAL 16 2
          , "OvduBal"             -- 催收餘額 DECIMAL 16 2
          , "LawAmount"           -- 法催擔保金額 DECIMAL 16 2
          , "AssetClass"          -- 資產五分類代號 VARCHAR2 2 0
          , "StoreRate"           -- 計息利率 DECIMAL 6 4
          , "CreateDate"          -- 建檔日期時間 DATE 8 0
          , "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 0
          , "LastUpdate"          -- 最後更新日期時間 DATE 8 0
          , "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 0
          , "AcSubBookCode"        -- 區隔帳冊別 VARCHAR2 3 0
        )
        WITH stsData AS (
          SELECT MSTP.ADTYMT
               , MSTP.LMSACN
               , MSTP.LMSAPN
               , LMSP.LMSSTS
               , ROW_NUMBER ()
                 OVER (
                   PARTITION BY MSTP.ADTYMT
                              , MSTP.LMSACN
                              , MSTP.LMSAPN
                   ORDER BY LMSP.LMSASQ DESC
                 ) AS "Seq"
          FROM LA$MSTP MSTP
          LEFT JOIN LA$LMSP LMSP ON LMSP.LMSACN = MSTP.LMSACN
                                AND LMSP.LMSAPN = MSTP.LMSAPN
                                AND TRUNC(LMSP.LMSLLD / 100) <= MSTP.ADTYMT
        )
        , tx3037Data AS (
          SELECT LMSACN
               , MAX(TRXDAT) AS TRXDAT
          FROM LA$TRXP
          WHERE TRXTRN = '3037'
          GROUP BY LMSACN
        )
        , badDeptCloseData AS (
          SELECT DISTINCT
                 LMSACN
               , LMSAPN
          FROM LA$LMSP
          WHERE LMSSTS = 9
        )
        , badDeptCloseDateData AS (
          SELECT b.LMSACN
               , b.LMSAPN
               , t.TRXDAT
          FROM badDeptCloseData b
          LEFT JOIN tx3037Data t ON t.LMSACN = b.LMSACN
        )
        , badData AS (
          SELECT MSTP.ADTYMT
               , MSTP.LMSACN
               , MSTP.LMSAPN
               , CASE
                   WHEN LMSP.LMSSTS = 9
                        AND NVL(bd.TRXDAT,0) != 0
                   THEN 6
                 ELSE LMSP.LMSSTS END      AS LMSSTS
               , ROW_NUMBER ()
                 OVER (
                   PARTITION BY MSTP.ADTYMT
                              , MSTP.LMSACN
                              , MSTP.LMSAPN
                   ORDER BY LMSP.LMSASQ DESC
                 ) AS "Seq"
          FROM LA$MSTP MSTP
          LEFT JOIN LA$LMSP LMSP ON LMSP.LMSACN = MSTP.LMSACN
                                AND LMSP.LMSAPN = MSTP.LMSAPN
                                AND TRUNC(LMSP.LMSLLD / 100) <= MSTP.ADTYMT
          LEFT JOIN badDeptCloseDateData bd ON bd.LMSACN = MSTP.LMSACN
                                           AND bd.LMSAPN = MSTP.LMSAPN
                                           AND TRUNC(bd.TRXDAT / 100) >= MSTP.ADTYMT
          WHERE LMSP.LMSSTS IN (6,9)
        )
        SELECT S0."ADTYMT"                    AS "YearMonth"           -- 資料年月 DECIMAL 6 0
              ,S0."LMSACN"                    AS "CustNo"              -- 戶號 DECIMAL 7 0
              ,S0."LMSAPN"                    AS "FacmNo"              -- 額度 DECIMAL 3 0
              ,0                              AS "PrevIntDate"         -- 繳息迄日 DecimalD 8 0
              ,0                              AS "NextIntDate"         -- 應繳息日 DecimalD 8 0
              -- 最近應繳日:已到期又逾期時,使用月底日曆日(SKL待查)
              ,0                              AS "DueDate"             -- 最近應繳日
              ,0                              AS "OvduTerm"            -- 逾期期數 DECIMAL 3 0
              ,0                              AS "OvduDays"            -- 逾期天數 DECIMAL 6 0
              ,'TWD'                          AS "CurrencyCode"        -- 幣別 VARCHAR2 3 0
              ,S0."LMSLBL"                    AS "PrinBalance"         -- 本金餘額 DECIMAL 16 2
              ,0                              AS "BadDebtBal"          -- 呆帳餘額 DECIMAL 16 2
              ,''                             AS "AccCollPsn"          -- 催收員 VARCHAR2 6 0
              ,''                             AS "LegalPsn"            -- 法務人員 VARCHAR2 6 0
              ,S0."Status"                    AS "Status"              -- 戶況 DECIMAL 2 0
              ,S1."ACTACT"                    AS "AcctCode"            -- 業務科目代號 VARCHAR2 3 0
              ,S1."LMSFAC"                    AS "FacAcctCode"         -- 額度業務科目 VARCHAR2 3 0
              ,0                              AS "ClCustNo"            -- 同擔保品戶號 DECIMAL 7 0
              ,0                              AS "ClFacmNo"            -- 同擔保品額度 DECIMAL 3 0
              ,0                              AS "ClRowNo"             -- 同擔保品序列號 DECIMAL 3 0
              ,''                             AS "RenewCode"           -- 展期記號 VARCHAR2 1 0
              ,FAC."ProdNo"                   AS "ProdNo"              -- 商品代碼
              ,'000'                          AS "AcBookCode"          -- 帳冊別 VARCHAR2 3 0
              ,CU."EntCode"                   AS "EntCode"             -- 企金別 VARCHAR2 1 
              ,''                             AS "RelsCode"            -- (準)利害關係人職稱 VARCHAR2 2 0
              ,FAC."DepartmentCode"           AS "DepartmentCode"      -- 案件隸屬單位 VARCHAR2 1 0
              ,S0."LMSFPN"                    AS "UnpaidPrincipal"     -- 已到期回收本金 DECIMAL 16 2
              ,S0."LMSFIN"                    AS "UnpaidInterest"      -- 已到期利息 DECIMAL 16 2
              ,0                              AS "UnpaidBreachAmt"     -- 已到期違約金 DECIMAL 16 2
              ,0                              AS "UnpaidDelayInt"      -- 已到期延滯息 DECIMAL 16 2
              ,0                              AS "AcdrPrincipal"       -- 未到期回收本金 DECIMAL 16 2
              ,0                              AS "AcdrInterest"        -- 未到期利息 DECIMAL 16 2
              ,0                              AS "AcdrBreachAmt"       -- 未到期違約金 DECIMAL 16 2
              ,0                              AS "AcdrDelayInt"        -- 未到期延滯息 DECIMAL 16 2
              ,0                              AS "FireFee"             -- 火險費用 DECIMAL 16 2
              ,0                              AS "LawFee"              -- 法務費用 DECIMAL 16 2
              ,0                              AS "ModifyFee"           -- 契變手續費 DECIMAL 16 2
              ,0                              AS "AcctFee"             -- 帳管費用 DECIMAL 16 2
              ,0                              AS "ShortfallPrin"       -- 短繳本金 DECIMAL 16 2
              ,0                              AS "ShortfallInt"        -- 短繳利息 DECIMAL 16 2
              ,0                              AS "TempAmt"             -- 暫收金額 DECIMAL 16 2
              ,NVL(CF."ClCode1",0)            AS "ClCode1"             -- 主要擔保品代號1 DECIMAL 1 
              ,NVL(CF."ClCode2",0)            AS "ClCode2"             -- 主要擔保品代號2 DECIMAL 2 
              ,NVL(CF."ClNo",0)               AS "ClNo"                -- 主要擔保品編號 DECIMAL 7 0
              ,NVL(CM."CityCode",'')          AS "CityCode"            -- 主要擔保品地區別 VARCHAR2 2 
              ,S1."LMSFBD"                    AS "OvduDate"            -- 轉催收日期 DECIMALD 8 0
              , -- 催收本金餘額 : 若 催收餘額 <= 轉催收本金 , 則 催收本金餘額 = 催收餘額
                -- 否則 催收本金餘額 = 轉催收本金
               CASE
                 WHEN S0."LMSFPN" + S0."LMSFIN" - S0."LMSTPN" <= S0."LMSFPN"
                 THEN S0."LMSFPN" + S0."LMSFIN" - S0."LMSTPN"
               ELSE S0."LMSFPN" END           AS "OvduPrinBal"         -- 催收本金餘額 DECIMAL 16 2
              , -- 催收利息餘額 :
                -- 若 轉催收利息 > 催收還款金額 則 催收利息餘額 = 轉催收利息 - 催收還款金額
                -- 否則 催收利息餘額 = 0
               CASE
                 WHEN S0."LMSFIN" > S0."LMSTPN"
                 THEN S0."LMSFIN" - S0."LMSTPN"
               ELSE 0 END                     AS "OvduIntBal"          -- 催收利息餘額 DECIMAL 16 2
              ,0                              AS "OvduBreachBal"       -- 催收違約金餘額 DECIMAL 16 2
              , -- 催收餘額 = 轉催收本金 + 轉催收利息 - 催收還款金額
                S0."LMSFPN"
              + S0."LMSFIN"
              - S0."LMSTPN"                   AS "OvduBal"             -- 催收餘額 DECIMAL 16 2
              ,0                              AS "LawAmount"           -- 法催擔保金額 DECIMAL 16 2
              ,''                             AS "AssetClass"          -- 資產五分類代號 VARCHAR2 2 0
              ,0                              AS "StoreRate"           -- 計息利率 DECIMAL 6 4
              ,JOB_START_TIME                 AS "CreateDate"          -- 建檔日期時間 DATE 8 0
              ,'999999'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 0
              ,JOB_START_TIME                 AS "LastUpdate"          -- 最後更新日期時間 DATE 8 0
              ,'999999'                       AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 0
              ,CASE
                 WHEN NVL(ACT."ACTFSC",' ') = 'A' THEN '201'
               ELSE '00A' END                 AS "AcSubBookCode"        -- 區隔帳冊別 VARCHAR2 3 0
        FROM (-- 子查詢:找出催收戶資料
              SELECT MSTP."ADTYMT"
                    ,MSTP."LMSACN"
                    ,MSTP."LMSAPN"
                    -- 2:催收戶
                    -- 6:呆帳戶
                    ,MAX(
                      CASE
                        WHEN NVL(badData.LMSSTS,0) != 0 -- 呆帳戶判斷
                        THEN NVL(badData.LMSSTS,0)
                        WHEN NVL(stsData.LMSSTS,0) != 0
                        THEN NVL(stsData.LMSSTS,0)
                        WHEN MSTP."LMSFDB" = 0 -- 轉呆金額為0,才是催收戶
                        THEN 2
                      ELSE 6 END
                     )                  AS "Status"
                    ,SUM(MSTP."LMSLBL") AS "LMSLBL" -- 本金餘額
                    ,SUM(MSTP."LMSFPN") AS "LMSFPN" -- 轉催收本金
                    ,SUM(MSTP."LMSFIN") AS "LMSFIN" -- 轉催收利息
                    ,SUM(MSTP."LMSTPN") AS "LMSTPN" -- 催收還款金額
              FROM "LA$MSTP" MSTP
              LEFT JOIN stsData ON stsData.ADTYMT = MSTP.ADTYMT
                               AND stsData.LMSACN = MSTP.LMSACN
                               AND stsData.LMSAPN = MSTP.LMSAPN
                               AND stsData."Seq" = 1
              LEFT JOIN badData ON badData.ADTYMT = MSTP.ADTYMT
                               AND badData.LMSACN = MSTP.LMSACN
                               AND badData.LMSAPN = MSTP.LMSAPN
                               AND badData."Seq" = 1
              WHERE MSTP."ADTYMT" >= "DateStart"
                AND MSTP."ADTYMT" <= "DateEnd"
                AND MSTP."ACTACT" = '990' 
                AND MSTP."LMSFBD" > 0 -- 催收開始日不為0
              GROUP BY MSTP."ADTYMT"
                     , MSTP."LMSACN"
                     , MSTP."LMSAPN"
             ) S0
        LEFT JOIN (SELECT "ADTYMT"
                         ,"LMSACN"
                         ,"LMSAPN"
                         ,"ACTACT"
                         ,"LMSFAC"
                         ,"LMSFBD" -- 催收開始日
                         ,ROW_NUMBER() OVER (PARTITION BY "ADTYMT"
                                                         ,"LMSACN"
                                                         ,"LMSAPN"
                                             ORDER BY CASE WHEN "LMSLBL" = 0 THEN 1 ELSE 0 END -- 無餘額的往後排
                                                     ,"LMSASQ" ASC
                                            ) AS "Seq"
                   FROM "LA$MSTP"
                   WHERE "ADTYMT" >= "DateStart"
                     AND "ADTYMT" <= "DateEnd"
                     AND "ACTACT" = '990' 
                     AND "LMSFBD" > 0 -- 催收開始日不為0
                  ) S1 ON S1."ADTYMT" = S0."ADTYMT"
                      AND S1."LMSACN" = S0."LMSACN"
                      AND S1."LMSAPN" = S0."LMSAPN"
                      AND S1."Seq"    = 1
        LEFT JOIN "LNMDLYP" DLY ON DLY."ADTYMT" = S0."ADTYMT"
                               AND DLY."LMSACN" = S0."LMSACN"
                               AND DLY."LMSAPN" = S0."LMSAPN"
                               AND DLY."ADTYMT" >= "DateStart"
                               AND DLY."ADTYMT" <= "DateEnd"
        LEFT JOIN "CustMain" CU ON CU."CustNo" = S0."LMSACN"
        LEFT JOIN "FacMain" FAC ON FAC."CustNo" = S0."LMSACN"
                               AND FAC."FacmNo" = S0."LMSAPN"
        LEFT JOIN "ClFac" CF ON CF."CustNo" = FAC."CustNo"
                            AND CF."FacmNo" = FAC."FacmNo"
                            AND CF."MainFlag" = 'Y'
        LEFT JOIN "ClMain" CM ON CM."ClCode1" = CF."ClCode1"
                             AND CM."ClCode2" = CF."ClCode2"
                             AND CM."ClNo"    = CF."ClNo"
                             AND NVL(CF."ClNo",0) > 0
        LEFT JOIN "LA$ACTP" ACT ON ACT."LMSACN" = S0."LMSACN"
        WHERE DLY."ADTYMT" IS NULL
        ;

        -- 記錄寫入筆數
        INS_CNT := INS_CNT + sql%rowcount;

        -- 寫入資料
        INSERT INTO "MonthlyFacBal" (
            "YearMonth"           -- 資料年月 DECIMAL 6 0
          , "CustNo"              -- 戶號 DECIMAL 7 0
          , "FacmNo"              -- 額度 DECIMAL 3 0
          , "PrevIntDate"         -- 繳息迄日 DecimalD 8 0
          , "NextIntDate"         -- 應繳息日 DecimalD 8 0
          , "DueDate"             -- 最近應繳日
          , "OvduTerm"            -- 逾期期數 DECIMAL 3 0
          , "OvduDays"            -- 逾期天數 DECIMAL 6 0
          , "CurrencyCode"        -- 幣別 VARCHAR2 3 0
          , "PrinBalance"         -- 本金餘額 DECIMAL 16 2
          , "BadDebtBal"          -- 呆帳餘額 DECIMAL 16 2
          , "AccCollPsn"          -- 催收員 VARCHAR2 6 0
          , "LegalPsn"            -- 法務人員 VARCHAR2 6 0
          , "Status"              -- 戶況 DECIMAL 2 0
          , "AcctCode"            -- 業務科目代號 VARCHAR2 3 0
          , "FacAcctCode"         -- 額度業務科目 VARCHAR2 3 0
          , "ClCustNo"            -- 同擔保品戶號 DECIMAL 7 0
          , "ClFacmNo"            -- 同擔保品額度 DECIMAL 3 0
          , "ClRowNo"             -- 同擔保品序列號 DECIMAL 3 0
          , "RenewCode"           -- 展期記號 VARCHAR2 1 0
          , "ProdNo"              -- 商品代碼
          , "AcBookCode"          -- 帳冊別 VARCHAR2 3 0
          , "EntCode"             -- 企金別 VARCHAR2 1 
          , "RelsCode"            -- (準)利害關係人職稱 VARCHAR2 2 0
          , "DepartmentCode"      -- 案件隸屬單位 VARCHAR2 1 0
          , "UnpaidPrincipal"     -- 已到期回收本金 DECIMAL 16 2
          , "UnpaidInterest"      -- 已到期利息 DECIMAL 16 2
          , "UnpaidBreachAmt"     -- 已到期違約金 DECIMAL 16 2
          , "UnpaidDelayInt"      -- 已到期延滯息 DECIMAL 16 2
          , "AcdrPrincipal"       -- 未到期回收本金 DECIMAL 16 2
          , "AcdrInterest"        -- 未到期利息 DECIMAL 16 2
          , "AcdrBreachAmt"       -- 未到期違約金 DECIMAL 16 2
          , "AcdrDelayInt"        -- 未到期延滯息 DECIMAL 16 2
          , "FireFee"             -- 火險費用 DECIMAL 16 2
          , "LawFee"              -- 法務費用 DECIMAL 16 2
          , "ModifyFee"           -- 契變手續費 DECIMAL 16 2
          , "AcctFee"             -- 帳管費用 DECIMAL 16 2
          , "ShortfallPrin"       -- 短繳本金 DECIMAL 16 2
          , "ShortfallInt"        -- 短繳利息 DECIMAL 16 2
          , "TempAmt"             -- 暫收金額 DECIMAL 16 2
          , "ClCode1"             -- 主要擔保品代號1 DECIMAL 1 
          , "ClCode2"             -- 主要擔保品代號2 DECIMAL 2 
          , "ClNo"                -- 主要擔保品編號 DECIMAL 7 0
          , "CityCode"            -- 主要擔保品地區別 VARCHAR2 2 
          , "OvduDate"            -- 轉催收日期 DECIMALD 8 0
          , "OvduPrinBal"         -- 催收本金餘額 DECIMAL 16 2
          , "OvduIntBal"          -- 催收利息餘額 DECIMAL 16 2
          , "OvduBreachBal"       -- 催收違約金餘額 DECIMAL 16 2
          , "OvduBal"             -- 催收餘額 DECIMAL 16 2
          , "LawAmount"           -- 法催擔保金額 DECIMAL 16 2
          , "AssetClass"          -- 資產五分類代號 VARCHAR2 2 0
          , "StoreRate"           -- 計息利率 DECIMAL 6 4
          , "CreateDate"          -- 建檔日期時間 DATE 8 0
          , "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 0
          , "LastUpdate"          -- 最後更新日期時間 DATE 8 0
          , "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 0
          , "AcSubBookCode"        -- 區隔帳冊別 VARCHAR2 3 0
        )
        SELECT S0."ADTYMT"                    AS "YearMonth"           -- 資料年月 DECIMAL 6 0
              ,S0."LMSACN"                    AS "CustNo"              -- 戶號 DECIMAL 7 0
              ,S0."LMSAPN"                    AS "FacmNo"              -- 額度 DECIMAL 3 0
              ,0                              AS "PrevIntDate"         -- 繳息迄日 DecimalD 8 0
              ,0                              AS "NextIntDate"         -- 應繳息日 DecimalD 8 0
              -- 最近應繳日:已到期又逾期時,使用月底日曆日(SKL待查)
              ,0                              AS "DueDate"             -- 最近應繳日
              ,0                              AS "OvduTerm"            -- 逾期期數 DECIMAL 3 0
              ,0                              AS "OvduDays"            -- 逾期天數 DECIMAL 6 0
              ,'TWD'                          AS "CurrencyCode"        -- 幣別 VARCHAR2 3 0
              ,S0."LMSLBL"                    AS "PrinBalance"         -- 本金餘額 DECIMAL 16 2
              ,0                              AS "BadDebtBal"          -- 呆帳餘額 DECIMAL 16 2
              ,''                             AS "AccCollPsn"          -- 催收員 VARCHAR2 6 0
              ,''                             AS "LegalPsn"            -- 法務人員 VARCHAR2 6 0
              ,S0."Status"                    AS "Status"              -- 戶況 DECIMAL 2 0
              ,S0."ACTACT"                    AS "AcctCode"            -- 業務科目代號 VARCHAR2 3 0
              ,FAC."AcctCode"                 AS "FacAcctCode"         -- 額度業務科目 VARCHAR2 3 0
              ,0                              AS "ClCustNo"            -- 同擔保品戶號 DECIMAL 7 0
              ,0                              AS "ClFacmNo"            -- 同擔保品額度 DECIMAL 3 0
              ,0                              AS "ClRowNo"             -- 同擔保品序列號 DECIMAL 3 0
              ,''                             AS "RenewCode"           -- 展期記號 VARCHAR2 1 0
              ,FAC."ProdNo"                   AS "ProdNo"              -- 商品代碼
              ,'000'                          AS "AcBookCode"          -- 帳冊別 VARCHAR2 3 0
              ,CU."EntCode"                   AS "EntCode"             -- 企金別 VARCHAR2 1 
              ,''                             AS "RelsCode"            -- (準)利害關係人職稱 VARCHAR2 2 0
              ,FAC."DepartmentCode"           AS "DepartmentCode"      -- 案件隸屬單位 VARCHAR2 1 0
              ,0                              AS "UnpaidPrincipal"     -- 已到期回收本金 DECIMAL 16 2
              ,0                              AS "UnpaidInterest"      -- 已到期利息 DECIMAL 16 2
              ,0                              AS "UnpaidBreachAmt"     -- 已到期違約金 DECIMAL 16 2
              ,0                              AS "UnpaidDelayInt"      -- 已到期延滯息 DECIMAL 16 2
              ,0                              AS "AcdrPrincipal"       -- 未到期回收本金 DECIMAL 16 2
              ,0                              AS "AcdrInterest"        -- 未到期利息 DECIMAL 16 2
              ,0                              AS "AcdrBreachAmt"       -- 未到期違約金 DECIMAL 16 2
              ,0                              AS "AcdrDelayInt"        -- 未到期延滯息 DECIMAL 16 2
              ,0                              AS "FireFee"             -- 火險費用 DECIMAL 16 2
              ,0                              AS "LawFee"              -- 法務費用 DECIMAL 16 2
              ,0                              AS "ModifyFee"           -- 契變手續費 DECIMAL 16 2
              ,0                              AS "AcctFee"             -- 帳管費用 DECIMAL 16 2
              ,0                              AS "ShortfallPrin"       -- 短繳本金 DECIMAL 16 2
              ,0                              AS "ShortfallInt"        -- 短繳利息 DECIMAL 16 2
              ,0                              AS "TempAmt"             -- 暫收金額 DECIMAL 16 2
              ,NVL(CF."ClCode1",0)            AS "ClCode1"             -- 主要擔保品代號1 DECIMAL 1 
              ,NVL(CF."ClCode2",0)            AS "ClCode2"             -- 主要擔保品代號2 DECIMAL 2 
              ,NVL(CF."ClNo",0)               AS "ClNo"                -- 主要擔保品編號 DECIMAL 7 0
              ,NVL(CM."CityCode",'')          AS "CityCode"            -- 主要擔保品地區別 VARCHAR2 2 
              ,0                              AS "OvduDate"            -- 轉催收日期 DECIMALD 8 0
              ,0                              AS "OvduPrinBal"         -- 催收本金餘額 DECIMAL 16 2
              ,0                              AS "OvduIntBal"          -- 催收利息餘額 DECIMAL 16 2
              ,0                              AS "OvduBreachBal"       -- 催收違約金餘額 DECIMAL 16 2
              ,0                              AS "OvduBal"             -- 催收餘額 DECIMAL 16 2
              ,0                              AS "LawAmount"           -- 法催擔保金額 DECIMAL 16 2
              ,''                             AS "AssetClass"          -- 資產五分類代號 VARCHAR2 2 0
              ,0                              AS "StoreRate"           -- 計息利率 DECIMAL 6 4
              ,JOB_START_TIME                 AS "CreateDate"          -- 建檔日期時間 DATE 8 0
              ,'999999'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 0
              ,JOB_START_TIME                 AS "LastUpdate"          -- 最後更新日期時間 DATE 8 0
              ,'999999'                       AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 0
              ,CASE
                 WHEN NVL(ACT."ACTFSC",' ') = 'A' THEN '201'
               ELSE '00A' END                 AS "AcSubBookCode"        -- 區隔帳冊別 VARCHAR2 3 0
        FROM (-- 子查詢:找出非催收戶資料
              SELECT MSTP."ADTYMT"
                   , MSTP."LMSACN"
                   , MSTP."LMSAPN"
                   , MIN(
                       CASE
                         WHEN MSTP."LMSLBL" != 0
                         THEN 0
                       ELSE 3 END
                     )         AS "Status"
                   , MAX(MSTP."ACTACT") AS "ACTACT"
                   , SUM(MSTP."LMSLBL") AS "LMSLBL" -- 本金餘額
              FROM "LA$MSTP" MSTP
              LEFT JOIN "MonthlyFacBal" MFB ON MFB."YearMonth" = MSTP."ADTYMT"
                                           AND MFB."CustNo" = MSTP."LMSACN"
                                           AND MFB."FacmNo" = MSTP."LMSAPN"
              WHERE MSTP."ADTYMT" >= "DateStart"
                AND MSTP."ADTYMT" <= "DateEnd"
                AND NVL(MFB."YearMonth",0) = 0 -- 沒寫入過的資料
              GROUP BY MSTP."ADTYMT"
                      ,MSTP."LMSACN"
                      ,MSTP."LMSAPN"
             ) S0
        LEFT JOIN "CustMain" CU ON CU."CustNo" = S0."LMSACN"
        LEFT JOIN "FacMain" FAC ON FAC."CustNo" = S0."LMSACN"
                               AND FAC."FacmNo" = S0."LMSAPN"
        LEFT JOIN "ClFac" CF ON CF."CustNo" = FAC."CustNo"
                            AND CF."FacmNo" = FAC."FacmNo"
                            AND CF."MainFlag" = 'Y'
        LEFT JOIN "ClMain" CM ON CM."ClCode1" = CF."ClCode1"
                             AND CM."ClCode2" = CF."ClCode2"
                             AND CM."ClNo"    = CF."ClNo"
                             AND NVL(CF."ClNo",0) > 0
        LEFT JOIN "LA$ACTP" ACT ON ACT."LMSACN" = S0."LMSACN"
        ;

        -- 記錄寫入筆數
        INS_CNT := INS_CNT + sql%rowcount;

        -- 更新戶況
        -- UPDATE "MonthlyFacBal"
        -- SET "Status" = 3
        -- WHERE "PrinBalance" + "OvduBal" + "BadDebtBal" = 0

        --   更新 FeeAmt 費用

        DBMS_OUTPUT.PUT_LINE('UPDATE FeeAmt ');

        -- 2021-02-19 更新 須配合新增欄位
        MERGE INTO "MonthlyFacBal" M
        USING ( 
          WITH LastMonthData AS (
            SELECT MAX("YearMonth") AS "LastMonth"
            FROM "MonthlyFacBal"
          )
          SELECT "YearMonth"
                ,"CustNo"
                ,"FacmNo"
                ,NVL(SUM("FireFee"), 0)       AS "FireFee"
                ,NVL(SUM("LawFee"), 0)        AS "LawFee"
                ,NVL(SUM("ModifyFee"), 0)     AS "ModifyFee"
                ,NVL(SUM("AcctFee"), 0)       AS "AcctFee"
                ,NVL(SUM("ShortfallPrin"), 0) AS "ShortfallPrin"
                ,NVL(SUM("ShortfallInt"), 0)  AS "ShortfallInt"
                ,NVL(SUM("TempAmt"), 0)       AS "TempAmt"
          FROM ( 
            SELECT M."YearMonth" 
                  ,M."CustNo"
                  ,M."FacmNo"
                  ,CASE
                      WHEN A."AcctCode" IN ('TMI')
                      THEN A."RvBal"
                      WHEN A."AcctCode" IN ('F09', 'F25')
                      THEN A."RvBal"
                      WHEN IR."CustNo" IS NOT NULL
                      THEN IR."TotInsuPrem"
                    ELSE 0 END AS "FireFee" -- 火險費用
                  ,CASE
                      WHEN A."AcctCode" IN ('F07', 'F24')
                      THEN A."RvBal"
                    ELSE 0 END AS "LawFee" -- 法務費用
                  ,CASE
                      WHEN A."AcctCode" IN ('F29')
                      THEN A."RvBal"
                    ELSE 0 END AS "ModifyFee" -- 契變手續費
                  ,CASE
                      WHEN A."AcctCode" IN ('F10')
                      THEN A."RvBal"
                    ELSE 0 END AS "AcctFee" -- 帳管費用
                  ,CASE
                      WHEN SUBSTR(A."AcctCode",0,1) IN ('Z')
                      THEN A."RvBal"
                    ELSE 0 END AS "ShortfallPrin" -- 短繳本金
                  ,CASE
                      WHEN SUBSTR(A."AcctCode",0,1) IN ('I')
                      THEN A."RvBal"
                    ELSE 0 END AS "ShortfallInt" -- 短繳利息
                  ,CASE
                      WHEN A."AcctCode" = 'TAV'
                      THEN A."RvBal"
                    ELSE 0 END AS "TempAmt" -- 暫收金額
            FROM "MonthlyFacBal" M
            LEFT JOIN LastMonthData LMD ON LMD."LastMonth" = M."YearMonth"
            LEFT JOIN "AcReceivable" A ON A."CustNo" = M."CustNo"
                                      AND A."FacmNo" = M."FacmNo"
            LEFT JOIN "InsuRenew" IR ON IR."CustNo" = A."CustNo"
                                    AND IR."FacmNo" = A."FacmNo"
                                    AND IR."PrevInsuNo" = A."RvNo"                
            WHERE M."YearMonth" = NVL(LMD."LastMonth",0)
              AND (A."AcctCode" IN ('F10','F29','TMI', 'F09', 'F25', 'F07', 'F24','TAV')
                    OR SUBSTR(A."AcctCode",0,1) IN ('I','Z'))
          ) M
          GROUP BY "YearMonth","CustNo", "FacmNo" 
        ) D
        ON (    M."YearMonth" = D."YearMonth"
            AND M."CustNo"    = D."CustNo"
            AND M."FacmNo"    = D."FacmNo")
        WHEN MATCHED THEN UPDATE
        SET M."FireFee"       = D."FireFee"
          ,M."LawFee"        = D."LawFee"
          ,M."ModifyFee"     = D."ModifyFee"
          ,M."AcctFee"       = D."AcctFee"
          ,M."ShortfallPrin" = D."ShortfallPrin"
          ,M."ShortfallInt"  = D."ShortfallInt"
          ,M."TempAmt"       = D."TempAmt"
        ;
        -- 記錄程式結束時間
        JOB_END_TIME := SYSTIMESTAMP;

        commit;
    END;
    -- 例外處理
    Exception
    WHEN OTHERS THEN
    ERROR_MSG := SQLERRM || CHR(13) || CHR(10) || dbms_utility.format_error_backtrace;
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_MonthlyFacBal_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;
