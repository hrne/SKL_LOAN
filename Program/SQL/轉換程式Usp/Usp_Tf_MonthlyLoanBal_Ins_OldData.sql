--------------------------------------------------------
--  DDL for Procedure Usp_Tf_MonthlyLoanBal_Ins_OldData
--------------------------------------------------------
set define off;

  CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_Tf_MonthlyLoanBal_Ins_OldData" 
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
        "DateStart" DECIMAL(6) := 201601 ; -- 資料擷取起日
    BEGIN

      -- 刪除舊資料
      EXECUTE IMMEDIATE 'ALTER TABLE "MonthlyLoanBal" DISABLE PRIMARY KEY CASCADE';
      EXECUTE IMMEDIATE 'TRUNCATE TABLE "MonthlyLoanBal" DROP STORAGE';
      EXECUTE IMMEDIATE 'ALTER TABLE "MonthlyLoanBal" ENABLE PRIMARY KEY';

      -- 寫入資料
      INSERT INTO "MonthlyLoanBal"
      SELECT S0."ADTYMT"                    AS "YearMonth"           -- 資料年月 DECIMAL 6 0
            ,S0."LMSACN"                    AS "CustNo"              -- 戶號 DECIMAL 7 0
            ,S0."LMSAPN"                    AS "FacmNo"              -- 額度編號 DECIMAL 3 0
            ,S0."LMSASQ"                    AS "BormNo"              -- 撥款序號 DECIMAL 3 0
            ,S0."ACTACT"                    AS "AcctCode"            -- 業務科目代號 VARCHAR2 3 0
            ,S0."LMSFAC"                    AS "FacAcctCode"         -- 額度業務科目 VARCHAR2 3 0
            ,'TWD'                          AS "CurrencyCode"        -- 幣別 VARCHAR2 3 0
            ,S0."LMSLBL"                    AS "LoanBalance"         -- 放款餘額 DECIMAL 16 2
            ,NVL(S6."LASHBL",S0."LMSLBL")   AS "MaxLoanBal"          -- 最高放款餘額 DECIMAL 16 2
            ,S0."IRTRAT"                    AS "StoreRate"           -- 計息利率 DECIMAL 6 4
            ,S0."MSTTIN"         -- 至本月底累計回收利息
            - NVL(S5."MSTTIN",0) -- 至上月底累計回收利息
                                            AS "IntAmtRcv"           -- 實收利息 DECIMAL 16 2 用 本月減上月的MSTTIN累計回收利息
            ,S0."MSTINT"  -- 已到期未繳息
            + S0."MSTIND" -- 未到期應收息
                                            AS "IntAmtAcc"           -- 提存利息 DECIMAL 16 2
            ,S0."MSTINT"                    AS "UnpaidInt"           -- 已到期未繳息 DECIMAL 16 2
            ,S0."MSTIND"                    AS "UnexpiredInt"        -- 未到期應收息 DECIMAL 16 2
            ,S0."MSTTIN"                    AS "SumRcvInt"           -- 累計回收利息 DECIMAL 16 2
            ,S0."MSTTIN" - NVL(S5."MSTTIN",0)         -- 本月實收利息
            + S0."MSTINT" + S0."MSTIND"               -- 本月提存利息
            - NVL(S5."MSTINT",0) + NVL(S5."MSTIND",0) -- 上月提存利息
                                            AS "IntAmt"              -- 本月利息 DECIMAL 16 2 本月利息收入= 本月實收利息+本月提存利息-上月提存利息
            ,FAC."ProdNo"                   AS "ProdNo"              -- 商品代碼 VARCHAR2 5 0
            ,'000'                          AS "AcBookCode"          -- 帳冊別 VARCHAR2 3 0
            ,S4."EntCode"                   AS "EntCode"             -- 企金別 VARCHAR2 1 
            ,''                             AS "RelsCode"            -- (準)利害關係人職稱 VARCHAR2 2 0
            ,S3."CASUNT"                    AS "DepartmentCode"      -- 案件隸屬單位 VARCHAR2 1 0
            ,NVL(CF."ClCode1",0)            AS "ClCode1"             -- 主要擔保品代號1 DECIMAL 1 
            ,NVL(CF."ClCode2",0)            AS "ClCode2"             -- 主要擔保品代號2 DECIMAL 2 
            ,NVL(CF."ClNo",0)               AS "ClNo"                -- 主要擔保品編號 DECIMAL 7 0
            ,NVL(CM."CityCode",'')          AS "CityCode"            -- 主要擔保品地區別 VARCHAR2 2 
            ,S0."LMSFPN"                    AS "OvduPrinAmt"         -- 轉催收本金 DECIMAL 16 2 
            ,S0."LMSFIN"                    AS "OvduIntAmt"          -- 轉催收利息 DECIMAL 16 2 
            ,JOB_START_TIME                 AS "CreateDate"          -- 建檔日期時間 DATE 0 
            ,'999999'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 
            ,JOB_START_TIME                 AS "LastUpdate"          -- 最後更新日期時間 DATE 0 
            ,'999999'                       AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 0
            ,CASE
              WHEN NVL(S7."ACTFSC",' ') = 'A' THEN '201'
            ELSE '00A' END                 AS "AcSubBookCode"          -- 帳冊別 VARCHAR2 3 0
      FROM "LA$MSTP" S0
      LEFT JOIN "LA$W30P" S2 ON S2."LMSACN" = S0."LMSACN"
                            AND S2."LMSAPN" = S0."LMSAPN"
                            AND S2."LMSASQ" = S0."LMSASQ"
      LEFT JOIN "LA$APLP" S3 ON S3."LMSACN" = S0."LMSACN"
                            AND S3."LMSAPN" = S0."LMSAPN"
      LEFT JOIN "CustMain" S4 ON S4."CustNo" = S0."LMSACN"
      LEFT JOIN "FacMain" FAC ON FAC."CustNo" = S0."LMSACN"
                            AND FAC."FacmNo" = S0."LMSAPN"
      LEFT JOIN "ClFac" CF ON CF."CustNo" = FAC."CustNo"
                          AND CF."FacmNo" = FAC."FacmNo"
                          AND CF."MainFlag" = 'Y'
      LEFT JOIN "ClMain" CM ON CM."ClCode1" = CF."ClCode1"
                          AND CM."ClCode2" = CF."ClCode2"
                          AND CM."ClNo"    = CF."ClNo"
                          AND NVL(CF."ClNo",0) > 0
      -- 串上個月
      LEFT JOIN "LA$MSTP" S5 ON S5."LMSACN" = S0."LMSACN"
                            AND S5."LMSAPN" = S0."LMSAPN"
                            AND S5."LMSASQ" = S0."LMSASQ"
                            AND TO_DATE(S5."ADTYMT" * 100 + 01,'YYYYMMDD') = ADD_MONTHS(TO_DATE(S0."ADTYMT" * 100 + 01,'YYYYMMDD'),-1)
      -- 串最高餘額檔
      LEFT JOIN "LN$LBLP" S6 ON S6."LMSACN" = S0."LMSACN"
                            AND S6."LMSAPN" = S0."LMSAPN"
                            AND S6."LMSASQ" = S0."LMSASQ"
                            AND S6."ADTYMT" = S0."ADTYMT"
      LEFT JOIN "LA$ACTP" S7 ON S7."LMSACN" = S0."LMSACN"
      WHERE S0.ADTYMT < "DateStart"
      ;

    END;


    -- 記錄寫入筆數
    INS_CNT := INS_CNT + sql%rowcount;

    -- 記錄程式結束時間
    JOB_END_TIME := SYSTIMESTAMP;

    commit;

    -- 例外處理
    Exception
    WHEN OTHERS THEN
    ERROR_MSG := SQLERRM || CHR(13) || CHR(10) || dbms_utility.format_error_backtrace;
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_MonthlyLoanBal_Ins_OldData',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;




/
