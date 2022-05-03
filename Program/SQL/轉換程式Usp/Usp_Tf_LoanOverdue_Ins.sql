--------------------------------------------------------
--  DDL for Procedure Usp_Tf_LoanOverdue_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_Tf_LoanOverdue_Ins" 
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

    -- 刪除舊資料
    EXECUTE IMMEDIATE 'ALTER TABLE "LoanOverdue" DISABLE PRIMARY KEY CASCADE';
    EXECUTE IMMEDIATE 'TRUNCATE TABLE "LoanOverdue" DROP STORAGE';
    EXECUTE IMMEDIATE 'ALTER TABLE "LoanOverdue" ENABLE PRIMARY KEY';

    -- 寫入資料
    INSERT INTO "LoanOverdue"
    SELECT FT."LMSACN"                    AS "CustNo"              -- 借款人戶號 DECIMAL 7 
          ,FT."LMSAPN"                    AS "FacmNo"              -- 額度編號 DECIMAL 3 
          ,FT."LMSASQ"                    AS "BormNo"              -- 撥款序號, 預約序號 DECIMAL 3 
          -- 須將此號碼Update回撥款主檔
          ,ROW_NUMBER() OVER (PARTITION BY FT."LMSACN"
                                          ,FT."LMSAPN"
                                          ,FT."LMSASQ"
                              ORDER BY FT."LMSACN"
                                      ,FT."LMSAPN"
                                      ,FT."LMSASQ"
                                      ,FT."LMSFBD")
                                          AS "OvduNo"              -- 催收序號 DECIMAL 3 
          -- 需轉成正確戶況
          /* LoanOverdue.Status
             1 : 催收
             2 : 部分轉呆
             3 : 呆帳
             4 : 催收回復
             5 : 催收收回
          */
          ,CASE
             WHEN LM."LMSSTS" = 6      THEN 3 -- 呆帳
             WHEN LM."LMSSTS" = 7      THEN 2 -- 部分轉呆
             WHEN LM."LMSSTS" = 5      THEN 5 -- 催收結案 = 催收收回
             WHEN LM."LMSSTS" = 9      THEN 3 -- 呆帳結案 = 呆帳
             WHEN LM."LMSSTS" IN (1,3) THEN 4 -- 正常戶,結案戶 = 催收回復
           ELSE 1 END                     AS "Status"              -- 狀態 DECIMAL 1 
          ,'990'                          AS "AcctCode"            -- 帳務科目 VARCHAR2 3 
          ,FT."LMSFBD"                    AS "OvduDate"            -- 轉催收日期 DECIMALD 8 
          ,NVL(BD."BadDebtDate",0)        AS "BadDebtDate"         -- 轉呆帳日期 DECIMALD 8 
          ,0                              AS "ReplyDate"           -- 催收回復日期 DECIMALD 8 
          ,FT."LMSFPN"                    AS "OvduPrinAmt"         -- 轉催收本金 DECIMAL 16 2
          ,FT."LMSFIN"                    AS "OvduIntAmt"          -- 轉催收利息 DECIMAL 16 2
          ,FT."LMSFPL"                    AS "OvduBreachAmt"       -- 轉催收違約金 DECIMAL 16 2
          ,FT."LMSFPN"
          +FT."LMSFIN"
          +FT."LMSFPL"                    AS "OvduAmt"             -- 轉催收金額 DECIMAL 16 2
          ,CASE
             WHEN   FT."LMSFPN" -- 轉催收本金
                  + FT."LMSFIN" -- 轉催收利息
                  + FT."LMSFPL" -- 轉催收違約金
                  - FT."LMSTPN" -- 催收還款金額
                  - FT."LMSFDB" -- 轉呆帳金額
                  <= 0 -- 催收餘額小於等於0
             THEN 0
             WHEN   FT."LMSFPN" -- 轉催收本金
                  + FT."LMSFIN" -- 轉催收利息
                  + FT."LMSFPL" -- 轉催收違約金
                  - FT."LMSTPN" -- 催收還款金額
                  - FT."LMSFDB" -- 轉呆帳金額
                  > 0 -- 催收餘額大於0
                  AND FT."LMSFPN" -- 轉催收本金
                      + FT."LMSFIN" -- 轉催收利息
                      + FT."LMSFPL" -- 轉催收違約金
                      - FT."LMSTPN" -- 催收還款金額
                      - FT."LMSFDB" -- 轉呆帳金額
                      <= FT."LMSFPN" -- 催收餘額 小於等於 轉催本金
             THEN FT."LMSFPN" + FT."LMSFIN" + FT."LMSFPL" - FT."LMSTPN" - FT."LMSFDB"
           ELSE FT."LMSFPN" END    AS "OvduPrinBal"         -- 催收本金餘額 DECIMAL 16 2
          ,CASE
             WHEN   FT."LMSFPN" -- 轉催收本金
                  + FT."LMSFIN" -- 轉催收利息
                  + FT."LMSFPL" -- 轉催收違約金
                  - FT."LMSTPN" -- 催收還款金額
                  - FT."LMSFDB" -- 轉呆帳金額
                  <= FT."LMSFPN"-- 催收餘額 小於等於 轉催本金
             THEN 0
             WHEN   FT."LMSFPN" -- 轉催收本金
                  + FT."LMSFIN" -- 轉催收利息
                  + FT."LMSFPL" -- 轉催收違約金
                  - FT."LMSTPN" -- 催收還款金額
                  - FT."LMSFDB" -- 轉呆帳金額
                  > FT."LMSFPN" -- 催收餘額 大於 轉催本金
                  AND  FT."LMSFPN" -- 轉催收本金
                     + FT."LMSFIN" -- 轉催收利息
                     + FT."LMSFPL" -- 轉催收違約金
                     - FT."LMSTPN" -- 催收還款金額
                     - FT."LMSFDB" -- 轉呆帳金額
                     <= FT."LMSFPN" + FT."LMSFIN" -- 催收餘額 小於等於 轉催本金+利息
             THEN FT."LMSFIN"+FT."LMSFPL"-FT."LMSTPN"-FT."LMSFDB"
           ELSE FT."LMSFIN" END    AS "OvduIntBal"          -- 催收利息餘額 DECIMAL 16 2
          ,CASE
             WHEN   FT."LMSFPN" -- 轉催收本金
                  + FT."LMSFIN" -- 轉催收利息
                  + FT."LMSFPL" -- 轉催收違約金
                  - FT."LMSTPN" -- 催收還款金額
                  - FT."LMSFDB" -- 轉呆帳金額
                  <= FT."LMSFPN" + FT."LMSFIN" -- 催收餘額 小於等於 轉催本金+利息
             THEN 0
             WHEN   FT."LMSFPN" -- 轉催收本金
                  + FT."LMSFIN" -- 轉催收利息
                  + FT."LMSFPL" -- 轉催收違約金
                  - FT."LMSTPN" -- 催收還款金額
                  - FT."LMSFDB" -- 轉呆帳金額
                  > FT."LMSFPN" + FT."LMSFIN" -- 催收餘額 大於 轉催本金+利息
             THEN FT."LMSFPL"-FT."LMSTPN"-FT."LMSFDB" 
           ELSE FT."LMSFPL" END    AS "OvduBreachBal"       -- 催收違約金餘額 DECIMAL 16 2
          ,FT."LMSFPN" -- 轉催收本金
          +FT."LMSFIN" -- 轉催收利息
          +FT."LMSFPL" -- 轉催收違約金
          -FT."LMSTPN" -- 催收還款金額
          -FT."LMSFDB" -- 轉呆帳金額
                                          AS "OvduBal"             -- 催收餘額 DECIMAL 16 2
          ,0                              AS "ReduceInt"           -- 減免利息金額 NUMBER(16,2)
          ,0                              AS "ReduceBreach"        -- 減免違約金金額 NUMBER(16,2)
          ,NVL(BD."BadDebtAmt",0)         AS "BadDebtAmt"          -- 轉呆帳金額 DECIMAL 16 2
          -- 呆帳餘額先放0,後面UPDATE
          ,0                              AS "BadDebtBal"          -- 呆帳餘額 DECIMAL 16 2
          ,0                              AS "ReplyReduceAmt"      -- 催收回復減免金額 DECIMAL 16 2
          ,0                              AS "ProcessDate"         -- 處理日期 NUMBER(8,0)
          ,NVL(LA."EULSTS",u'')           AS "OvduSituaction"      -- 催收處理情形 NVARCHAR2 30 
          ,u''                            AS "Remark"              -- 附言 NVARCHAR2 60 
          ,FT."LMSLTD"                    AS "AcDate"              -- 會計日期 DECIMALD 8 
          ,JOB_START_TIME                 AS "CreateDate"          -- 建檔日期時間 DATE 8 
          ,'999999'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 
          ,JOB_START_TIME                 AS "LastUpdate"          -- 最後更新日期時間 DATE 8 
          ,'999999'                       AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 
    FROM "LA$FTRP" FT
    LEFT JOIN "CustMain" CM ON CM."CustNo" = FT."LMSACN"
    LEFT JOIN "LA$ASSP" LA ON LA."CUSID1" = CM."CustId"
    LEFT JOIN "LA$LMSP" LM ON LM."LMSACN" = FT."LMSACN"
                          AND LM."LMSAPN" = FT."LMSAPN"
                          AND LM."LMSASQ" = FT."LMSASQ"
    LEFT JOIN (SELECT "LMSACN"
                     ,"LMSAPN"
                     ,"LMSASQ"
                     ,MIN(TRXDAT) AS "BadDebtDate" -- 轉呆日期
                     ,SUM(TRXAMT) AS "BadDebtAmt" -- 部呆、轉呆金額合計
               FROM "LA$TRXP" 
               WHERE "TRXTRN" IN ('3086','3089')
                 AND "TRXCRC" = 0
                 --AND "TRXTCT" = 6 -- 因為部分轉呆時，結案區分欄位為null，所以改為不判斷
                 AND "TRXTAC" = 990
               GROUP BY "LMSACN"
                       ,"LMSAPN"
                       ,"LMSASQ"
             ) BD ON BD."LMSACN" = FT."LMSACN"
                 AND BD."LMSAPN" = FT."LMSAPN"
                 AND BD."LMSASQ" = FT."LMSASQ"
    ;

    -- 記錄寫入筆數
    INS_CNT := INS_CNT + sql%rowcount;

    MERGE INTO "LoanBorMain" T1
    USING (
         SELECT "CustNo"              -- 借款人戶號 DECIMAL 7 
               ,"FacmNo"              -- 額度編號 DECIMAL 3 
               ,"BormNo"              -- 撥款序號, 預約序號 DECIMAL 3 
               ,"OvduNo"              -- 催收序號 DECIMAL 3 
         FROM "LoanOverdue" S1
    ) S
    ON (
         T1."CustNo" = S."CustNo"
         AND T1."FacmNo" = S."FacmNo"
         AND T1."BormNo" = S."BormNo"
    )
    WHEN MATCHED THEN UPDATE
    SET T1."LastOvduNo" = S."OvduNo";

    /* 更新呆帳餘額 */
    MERGE INTO "LoanOverdue" T1
    USING (
          SELECT "CustNo"
                ,"FacmNo"
                ,SUM(NVL(TRX."TRXAMT",0)) AS "RecevieTotal" -- 本戶累計回收金額
          FROM (SELECT "CustNo"
                      ,LISTAGG("FacmNo",'、') WITHIN GROUP(ORDER BY "FacmNo") AS "FacmNo" 
                      ,MIN("BadDebtDate") AS "CustBadDebtDate"
                FROM "LoanOverdue" LO
                LEFT JOIN "LA$LMSP" LM ON LM."LMSACN" = LO."CustNo"
                                      AND LM."LMSAPN" = LO."FacmNo"
                                      AND LM."LMSASQ" = LO."BormNo"
                WHERE LO."Status" IN (2,3) -- 部呆、呆帳
                  AND LM."LMSSTS" <> 9  -- 非呆帳結案戶
                GROUP BY "CustNo"
               ) LO 
          LEFT JOIN "LA$TRXP" TRX ON TRX."LMSACN" = LO."CustNo"
                                 AND TRX."LMSAPN" = 0
                                 AND TRX."LMSASQ" = 0
                                 AND TRX."TRXDAT" > LO."CustBadDebtDate"
                                 AND "TRXTRN" = '3037'
                                 AND "TRXCRC" = 0
          GROUP BY "CustNo","FacmNo"
          ORDER BY "CustNo","FacmNo"
    ) S
    ON (
             T1."CustNo" = S."CustNo"
         AND S."RecevieTotal" > 0
    )
    WHEN MATCHED THEN UPDATE
    SET T1."BadDebtBal" = CASE
                            WHEN T1."BadDebtAmt" - S."RecevieTotal" > 0
                            THEN T1."BadDebtAmt" - S."RecevieTotal"
                          ELSE 0 END
    ;

    -- 記錄程式結束時間
    JOB_END_TIME := SYSTIMESTAMP;

    commit;

    -- 例外處理
    Exception
    WHEN OTHERS THEN
    ERROR_MSG := SQLERRM || CHR(13) || CHR(10) || dbms_utility.format_error_backtrace;
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_LoanOverdue_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;




/
