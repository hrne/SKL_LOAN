--------------------------------------------------------
--  DDL for Procedure Usp_Tf_Ias39LoanCommit_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "Usp_Tf_Ias39LoanCommit_Ins" 
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
        "ThisMonth" DECIMAL(6); --西元年月
    BEGIN

    SELECT TRUNC(("TbsDy" + 19110000) / 100)
    INTO "ThisMonth"
    FROM "TxBizDate"
    WHERE "DateCode" = 'ONLINE'
    ;

    -- 刪除舊資料
    EXECUTE IMMEDIATE 'ALTER TABLE "Ias39LoanCommit" DISABLE PRIMARY KEY CASCADE';
    EXECUTE IMMEDIATE 'TRUNCATE TABLE "Ias39LoanCommit" DROP STORAGE';
    EXECUTE IMMEDIATE 'ALTER TABLE "Ias39LoanCommit" ENABLE PRIMARY KEY';

    -- 寫入資料
    INSERT INTO "Ias39LoanCommit"
    SELECT "ThisMonth"                    AS "DataYm"              -- 年月份 DECIMAL 6 0
          ,"LNWLCTP"."LMSACN"             AS "CustNo"              -- 戶號 DECIMAL 7 0
          ,"LNWLCTP"."LMSAPN"             AS "FacmNo"              -- 額度編號 DECIMAL 3 0
          ,"LNWLCTP"."APLNUM"             AS "ApplNo"              -- 核准號碼 DECIMAL 7 0
          ,"LNWLCTP"."APLCSD"             AS "ApproveDate"         -- 核准日期 DECIMALD 8 0
          ,"LNWLCTP"."APLFSD"             AS "FirstDrawdownDate"   -- 初貸日期 DECIMALD 8 0
          ,"LNWLCTP"."APLDLD"             AS "MaturityDate"        -- 到期日 DECIMALD 8 0
          ,"LNWLCTP"."APLYER"             AS "LoanTermYy"          -- 貸款期間年 DECIMAL 2 0
          ,"LNWLCTP"."APLMON"             AS "LoanTermMm"          -- 貸款期間月 DECIMAL 2 0
          ,"LNWLCTP"."APLDAY"             AS "LoanTermDd"          -- 貸款期間日 DECIMAL 3 0
          ,"LNWLCTP"."APLADT"             AS "UtilDeadline"        -- 動支期限 DECIMALD 8 0
          ,"LNWLCTP"."APLRDT"             AS "RecycleDeadline"     -- 循環動用期限 DECIMALD 8 0
          ,"LNWLCTP"."APLPAM"             AS "LineAmt"             -- 核准額度 DECIMAL 13 2
          ,"LNWLCTP"."LMSLBL"             AS "UtilBal"             -- 放款餘額 DECIMAL 13 2
          ,"LNWLCTP"."W06AM4"             AS "AvblBal"             -- 可動用餘額 DECIMAL 13 2
          ,"LNWLCTP"."APLRCD"             AS "RecycleCode"         -- 該筆額度是否可循環動用 DECIMAL 1 0
          ,"LNWLCTP"."APLILC"             AS "IrrevocableFlag"     -- 該筆額度是否為不可徹銷 DECIMAL 1 0
          ,'000'                          AS "AcBookCode"          -- 帳冊別 VARCHAR2 3 0
          ,CASE
             WHEN "LNWLCTP"."FSCFLG" = '1'
             THEN '00A'
             WHEN "LNWLCTP"."FSCFLG" = '3'
             THEN '201'
           ELSE "LNWLCTP"."FSCFLG" END    AS "AcSubBookCode"       -- 區隔帳冊 VARCHAR2 3 0
          ,"LNWLCTP"."LCTCCF"             AS "Ccf"                 -- 信用風險轉換係數 DECIMAL 5 2
          ,"LNWLCTP"."LOVAMT"             AS "ExpLimitAmt"         -- 表外曝險金額 DECIMAL 13 2
          ,"LNWLCTP"."CORDAC"             AS "DbAcNoCode"          -- 借方：備忘分錄會計科目 VARCHAR2 8 0
          ,"LNWLCTP"."CORCAC"             AS "CrAcNoCode"          -- 貸方：備忘分錄會計科目 VARCHAR2 8 0
          ,0                              AS "DrawdownFg"          -- 已核撥記號 DECIMAL 1 0
          ,JOB_START_TIME                 AS "CreateDate"          -- 建檔日期時間 DATE 0 0
          ,'999999'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 0
          ,JOB_START_TIME                 AS "LastUpdate"          -- 最後更新日期時間 DATE 0 0
          ,'999999'                       AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 0
    FROM "LNWLCTP"
    ;

    -- 記錄寫入筆數
    INS_CNT := INS_CNT + sql%rowcount;

    INSERT INTO "Ias39LoanCommit"
    SELECT "ThisMonth"                    AS "DataYm"              -- 年月份 DECIMAL 6 0
          ,"LNWLCAP"."LMSACN"             AS "CustNo"              -- 戶號 DECIMAL 7 0
          ,"LNWLCAP"."LMSAPN"             AS "FacmNo"              -- 額度編號 DECIMAL 3 0
          ,"LNWLCAP"."APLNUM"             AS "ApplNo"              -- 核准號碼 DECIMAL 7 0
          ,"LNWLCAP"."APLCSD"             AS "ApproveDate"         -- 核准日期 DECIMALD 8 0
          ,"LNWLCAP"."APLFSD"             AS "FirstDrawdownDate"   -- 初貸日期 DECIMALD 8 0
          ,"LNWLCAP"."APLDLD"             AS "MaturityDate"        -- 到期日 DECIMALD 8 0
          ,"LNWLCAP"."APLYER"             AS "LoanTermYy"          -- 貸款期間年 DECIMAL 2 0
          ,"LNWLCAP"."APLMON"             AS "LoanTermMm"          -- 貸款期間月 DECIMAL 2 0
          ,"LNWLCAP"."APLDAY"             AS "LoanTermDd"          -- 貸款期間日 DECIMAL 3 0
          ,"LNWLCAP"."APLADT"             AS "UtilDeadline"        -- 動支期限 DECIMALD 8 0
          ,"LNWLCAP"."APLRDT"             AS "RecycleDeadline"     -- 循環動用期限 DECIMALD 8 0
          ,"LNWLCAP"."APLPAM"             AS "LineAmt"             -- 核准額度 DECIMAL 13 2
          ,"LNWLCAP"."LMSLBL"             AS "UtilBal"             -- 放款餘額 DECIMAL 13 2
          ,"LNWLCAP"."W06AM4"             AS "AvblBal"             -- 可動用餘額 DECIMAL 13 2
          ,"LNWLCAP"."APLRCD"             AS "RecycleCode"         -- 該筆額度是否可循環動用 DECIMAL 1 0
          ,"LNWLCAP"."APLILC"             AS "IrrevocableFlag"     -- 該筆額度是否為不可徹銷 DECIMAL 1 0
          ,'000'                          AS "AcBookCode"          -- 帳冊別 VARCHAR2 3 0
          ,CASE
             WHEN "LNWLCAP"."FSCFLG" = '1'
             THEN '00A'
             WHEN "LNWLCAP"."FSCFLG" = '3'
             THEN '201'
           ELSE "LNWLCAP"."FSCFLG" END    AS "AcSubBookCode"       -- 區隔帳冊 VARCHAR2 3 0
          ,"LNWLCAP"."LCTCCF"             AS "Ccf"                 -- 信用風險轉換係數 DECIMAL 5 2
          ,"LNWLCAP"."LOVAMT"             AS "ExpLimitAmt"         -- 表外曝險金額 DECIMAL 13 2
          ,0                              AS "DbAcNoCode"          -- 借方：備忘分錄會計科目 VARCHAR2 8 0
          ,0                               AS "CrAcNoCode"          -- 貸方：備忘分錄會計科目 VARCHAR2 8 0
          ,1                              AS "DrawdownFg"          -- 已核撥記號 DECIMAL 1 0
          ,JOB_START_TIME                 AS "CreateDate"          -- 建檔日期時間 DATE 0 0
          ,'999999'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 0
          ,JOB_START_TIME                 AS "LastUpdate"          -- 最後更新日期時間 DATE 0 0
          ,'999999'                       AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 0
    FROM "LNWLCAP"
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
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_Ias39LoanCommit_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;



/
