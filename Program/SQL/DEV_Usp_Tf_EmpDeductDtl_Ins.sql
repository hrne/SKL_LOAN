--------------------------------------------------------
--  DDL for Procedure Usp_Tf_EmpDeductDtl_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "Usp_Tf_EmpDeductDtl_Ins" 
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
    EXECUTE IMMEDIATE 'ALTER TABLE "EmpDeductDtl" DISABLE PRIMARY KEY CASCADE';
    EXECUTE IMMEDIATE 'TRUNCATE TABLE "EmpDeductDtl" DROP STORAGE';
    EXECUTE IMMEDIATE 'ALTER TABLE "EmpDeductDtl" ENABLE PRIMARY KEY';

    -- 寫入資料
    INSERT INTO "EmpDeductDtl"
    SELECT "LNMSLP"."TRXIDT"              AS "EntryDate"           -- 入帳日期 DECIMAL 8 0
          ,"LNMSLP"."LMSACN"              AS "CustNo"              -- 戶號 DECIMAL 7 0
          ,CASE "LNMSLP"."MBKTRX"
             WHEN '1' THEN '5' -- 保險費
             WHEN '2' THEN '1' -- 期款
             WHEN '3' THEN '4' -- 帳管費
             WHEN '4' THEN '6' -- 契變手續費
           ELSE '0' END                   AS "AchRepayCode"        -- 入帳扣款別 VARCHAR2 1 0
          ,"LNMSLP"."YGYYMM"              AS "PerfMonth"           -- 業績年月 DECIMAL 6 0
          ,"LNMSLP"."FLWCOD"              AS "ProcCode"            -- 流程別 VARCHAR2 1 0
          ,"LNMSLP"."DEDCOD"              AS "RepayCode"           -- 扣款代碼 VARCHAR2 1 0
          ,"LNMSLP"."ACTACT"              AS "AcctCode"            -- 科目 VARCHAR2 12 0
          ,0                              AS "FacmNo"              -- 額度編號 NUMBER(3,0)
          ,0                              AS "BormNo"              -- 撥款編號 NUMBER(3,0)
          ,"LNMSLP"."EMPCOD"              AS "EmpNo"               -- 員工代號 VARCHAR2 6 0
          ,"LNMSLP"."CUSID1"              AS "CustId"              -- 統一編號 VARCHAR2 10 0
          ,"LNMSLP"."TRXAMT"              AS "TxAmt"               -- 交易金額 DECIMAL 14 0
          ,"LNMSLP"."FALCOD"              AS "ErrMsg"              -- 失敗原因 NVARCHAR2 20 0
          ,"LNMSLP"."TRXDAT"              AS "Acdate"              -- 會計日期 DECIMAL 8 0
          ,"LNMSLP"."EMPCOD"              AS "TitaTlrNo"           -- 經辦 VARCHAR2 6 0
          ,"LNMSLP"."TRXNMT"              AS "TitaTxtNo"           -- 交易序號 VARCHAR2 8 0
          ,"LNMSLP"."BSTBTN"              AS "BatchNo"             -- 批次號碼 VARCHAR2 6 0
          ,"LNMSLP"."TPAYAMT"             AS "RepayAmt"            -- 應扣金額 DECIMAL 14 0
          ,"LNMSLP"."M06QCD"              AS "ResignCode"          -- 離職代碼 VARCHAR2 2 0
          ,"LNMSLP"."BCMDPT"              AS "DeptCode"            -- 部室代號 VARCHAR2 6 0
          ,"LNMSLP"."BCMCOD"              AS "UnitCode"            -- 單位代號 VARCHAR2 6 0
          ,"LNMSLP"."INSISD"              AS "IntStartDate"        -- 計息起日 DECIMAL 8 0
          ,"LNMSLP"."INSIED"              AS "IntEndDate"          -- 計息迄日 DECIMAL 8 0
          ,"LNMSLP"."POSRNK"              AS "PositCode"           -- 職務代號 VARCHAR2 2 0
          ,"LNMSLP"."INSPRN"              AS "Principal"           -- 本金 DECIMAL 14 0
          ,"LNMSLP"."INSINS"              AS "Interest"            -- 利息 DECIMAL 14 0
          ,"LNMSLP"."TRXAOS"              AS "SumOvpayAmt"         -- 累溢短收 DECIMAL 14 0
          ,''                             AS "JsonFields"
          ,"LNMSLP"."INSCIN"              AS "CurrIntAmt"          -- 當期利息 DECIMAL 14 0
          ,"LNMSLP"."INSCPN"              AS "CurrPrinAmt"         -- 當期本金 DECIMAL 14 0
          ,0                              AS "MediaDate"           -- decimal 8 0
          ,''                             AS "MediaKind"           -- varchar2 1
          ,0                              AS "MediaSeq"            -- decimal 6 0
          ,JOB_START_TIME                 AS "CreateDate"          -- 建檔日期時間 DATE  
          ,'999999'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 
          ,JOB_START_TIME                 AS "LastUpdate"          -- 最後更新日期時間 DATE  
          ,'999999'                       AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 
    FROM "LNMSLP" 
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
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_EmpDeductDtl_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;



/
