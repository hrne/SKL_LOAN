--------------------------------------------------------
--  DDL for Procedure Usp_Tf_LoanCheque_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_Tf_LoanCheque_Ins" 
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
    EXECUTE IMMEDIATE 'ALTER TABLE "LoanCheque" DISABLE PRIMARY KEY CASCADE';
    EXECUTE IMMEDIATE 'TRUNCATE TABLE "LoanCheque" DROP STORAGE';
    EXECUTE IMMEDIATE 'ALTER TABLE "LoanCheque" ENABLE PRIMARY KEY';

    -- 寫入資料
    INSERT INTO "LoanCheque"
    WITH txEmpData AS (
      SELECT DISTINCT
             TRXDAT
           , TRXMEM
           , TRXNMT
      FROM LA$TRXP
    )
    SELECT S1."LMSACN"                    AS "CustNo"              -- 借款人戶號 DECIMAL 7 
          ,S1."CHKACN"                    AS "ChequeAcct"          -- 支票帳號 DECIMAL 9 
          ,S1."CHKASQ"                    AS "ChequeNo"            -- 支票號碼 DECIMAL 7 
          ,S1."CHKCDE"                    AS "StatusCode"          -- 票據狀況碼 VARCHAR2 1 
          ,S1."CHKPRO"                    AS "ProcessCode"         -- 處理代碼 VARCHAR2 1 
          ,S1."TRXDAT"                    AS "AcDate"              -- 交易序號-會計日期 DECIMALD 8 
          ,'0000'                         AS "Kinbr"               -- 交易單位 VARCHAR2 4 
          ,NVL(AEM1."EmpNo",'999999')     AS "TellerNo"            -- 交易序號-櫃員 VARCHAR2 6 
          ,LPAD(S1."TRXNMT",8,'0')        AS "TxtNo"               -- 交易序號-流水號 VARCHAR2 8 
          ,S1."CHKRDT"                    AS "ReceiveDate"         -- 收票日 DECIMALD 8  
          ,S1."CHKLTD"                    AS "EntryDate"           -- 入帳日 DECIMALD 8 
          ,'TWD'                          AS "CurrencyCode"        -- 幣別 VARCHAR2 3 
          ,S1."CHKAMT"                    AS "ChequeAmt"           -- 支票金額 DECIMAL 16 2
          ,S1."CHKPAN"                    AS "ChequeName"          -- 發票人姓名 VARCHAR2 60 
          ,S1."CHKDLD"                    AS "ChequeDate"          -- 支票到期日 DECIMALD 8 
          ,S1."CHKEXG"                    AS "AreaCode"            -- 交換區號 VARCHAR2 2  
          -- 左補零,總長度7
          ,LPAD(S1."BBRCDE",7,0)          AS "BankCode"            -- 行庫代號 VARCHAR2 7 
          ,S1."CHKARA"                    AS "OutsideCode"         -- 本埠外埠 VARCHAR2 1 
          ,''                             AS "BktwFlag"            -- 是否為台支 VARCHAR2 1 
          ,''                             AS "TsibFlag"            -- 是否為台新 VARCHAR2 1 
          ,S1."CHKMED"                    AS "MediaFlag"           -- 入媒體檔 VARCHAR2 1 
          ,''                             AS "UsageCode"           -- 支票用途 VARCHAR2 2 
          ,S1."CNTRCD"                    AS "ServiceCenter"       -- 服務中心別 VARCHAR2 1 
          ,''                             AS "CreditorId"          -- 債權統一編號 VARCHAR2 10 
          ,''                             AS "CreditorBankCode"    -- 債權機構 VARCHAR2 7 
          ,''                             AS "OtherAcctCode"       -- 對方業務科目 VARCHAR2 3 
          ,LPAD(NVL(S2."RECPNO",''),5,0)  AS "ReceiptNo"           -- 收據號碼 VARCHAR2 5 
          ,NVL(S2."CHKAMT",0)             AS "RepaidAmt"           -- 已入帳金額 DECIMAL 16 2
         ,JOB_START_TIME                  AS "CreateDate"          -- 建檔日期時間 DATE  
         ,NVL(AEM1."EmpNo",'999999')      AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 
         ,JOB_START_TIME                  AS "LastUpdate"          -- 最後更新日期時間 DATE  
         ,NVL(AEM1."EmpNo",'999999')      AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 
    FROM "LA$CHKP" S1
    LEFT JOIN (SELECT "LMSACN" -- 戶號
                     ,"CHKACN" -- 支票帳號
                     ,"CHKASQ" -- 支票號碼
                     ,"TRXIDT" -- 交易日期
                     ,"TRXDAT" -- 會計日期
                     ,"RECPNO" -- 收據號碼
                     ,"CHKAMT" -- 支票金額
               FROM "LA$CTRP"
               WHERE "PAYCOD" = '1') S2 ON S2."LMSACN" = S1."LMSACN"
                                       AND S2."CHKACN" = S1."CHKACN"
                                       AND S2."CHKASQ" = S1."CHKASQ"
                                       AND S2."TRXDAT" = S1."TRXDAT"
    LEFT JOIN txEmpData ON txEmpData.TRXDAT = S1.TRXDAT
                       AND txEmpData.TRXNMT = S1.TRXNMT
    LEFT JOIN "As400EmpNoMapping" AEM1 ON AEM1."As400TellerNo" = txEmpData."TRXMEM"
    ;

    -- 記錄寫入筆數
    INS_CNT := INS_CNT + sql%rowcount;

    -- 記錄程式結束時間
    JOB_END_TIME := SYSTIMESTAMP;

    commit;

    -- 例外處理
    Exception
    WHEN OTHERS THEN
    ERROR_MSG := SQLERRM || CHR(13) || CHR(10) || dbms_utility.format_error_backtrace;
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_LoanCheque_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;



/
