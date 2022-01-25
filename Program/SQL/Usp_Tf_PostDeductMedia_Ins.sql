--------------------------------------------------------
--  DDL for Procedure Usp_Tf_PostDeductMedia_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "Usp_Tf_PostDeductMedia_Ins" 
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
    EXECUTE IMMEDIATE 'ALTER TABLE "PostDeductMedia" DISABLE PRIMARY KEY CASCADE';
    EXECUTE IMMEDIATE 'TRUNCATE TABLE "PostDeductMedia" DROP STORAGE';
    EXECUTE IMMEDIATE 'ALTER TABLE "PostDeductMedia" ENABLE PRIMARY KEY';

    -- 寫入資料
    INSERT INTO "PostDeductMedia"
    WITH rawData AS (
      SELECT MAX(TRXIDT) AS LastTRXIDT
      FROM "LA$MBKP" MBK
      WHERE MBK."LMSPBK" = '3' -- 只抓郵局
    )
    , tmpData AS (
      SELECT "Fn_GetBusinessDate"(LastTRXIDT,-2) AS NewTRXIDT -- 找前二營業日
           , LastTRXIDT 
      FROM rawData
    )
    SELECT CASE
             WHEN NVL(t.LastTRXIDT,0) != 0
             THEN t.NewTRXIDT
           ELSE MBK."TRXIDT"
           END                            AS "MediaDate"           -- 媒體日期 DECIMAL 8 
          ,ROW_NUMBER() OVER (PARTITION BY MBK."TRXIDT"
                              ORDER BY MBK."LMSACN"
                                      ,MBK."MBKAPN"
                                      ,MBK."LMSPCN"
                                      ,MBK."TRXIDT"
                                      ,MBK."LMSLPD"
                                      ,MBK."TRXISD"
                                      ,MBK."ACTACT"
                                      ,MBK."MBKAMT")
                                          AS "MediaSeq"            -- 媒體序號 DECIMAL 6 
          ,MBK."LMSACN"                   AS "CustNo"              -- 戶號 DECIMAL 7 
          ,MBK."MBKAPN"                   AS "FacmNo"              -- 額度號碼 DECIMAL 3 
          ,CASE MBK."MBKTRX"
             WHEN '1' THEN '5' -- 保險費
             WHEN '2' THEN '1' -- 期款
             WHEN '3' THEN '4' -- 帳管費
             WHEN '4' THEN '6' -- 契變手續費
           ELSE '0' END                   AS "RepayType"           -- 還款類別 DECIMAL 2 
          ,MBK."MBKAMT"                   AS "RepayAmt"            -- 扣款金額,還款金額 DECIMAL 14 
          ,LPAD(MBK."MBKRSN",2,'0')       AS "ProcNoteCode"        -- 處理說明 VARCHAR2 單格空白:成功 其他:參照需求書
          ,APLP."POSCDE"                  AS "PostDepCode"         -- 帳戶別 VARCHAR2 1 P:存簿 G:劃撥
          ,CASE
             WHEN MBK."MBKTRX" = '2' -- 期款
             THEN '846'
             WHEN MBK."MBKTRX" = '1' -- 火險
             THEN '53N'
           ELSE ' '
           END                            AS "OutsrcCode"          -- 委託機構代號 VARCHAR2 3
          ,CASE
             WHEN MBK."MBKTRX" = '2' -- 期款
             THEN '0002'
             WHEN MBK."MBKTRX" = '1' -- 火險
             THEN ' '
             WHEN MBK."MBKTRX" = '3' -- 帳管費
             THEN '0001'
             WHEN MBK."MBKTRX" = '4' -- 契變手續費
             THEN '0001' 
           ELSE ' '
           END                            AS "DistCode"            -- 區處代號 VARCHAR 4
          ,MBK."TRXIDT"                   AS "TransDate"           -- 轉帳日期 DECIMAL 8
          ,LPAD(MBK."LMSPCN",14,'0')      AS "RepayAcctNo"         -- 儲金帳號 VARCHAR2 14 0
          ,LPAD(NVL(MBK."LMSPID",' ') || APLP."POSCDE" || LPAD(MBK."LMSACN",7,'0'),20,' ')
                                          AS "PostUserNo"          -- 用戶編號 VARCHAR2 20 0 右靠左補空，大寫英數字，不得填寫中文(扣款人ID+郵局存款別(POSCDE)+戶號)預計補2位帳號碼
          ,LPAD(MBK."TRXIED",8,'0')
           || LPAD(MBK."MBKAPN",3,'0')
           || LPAD(MBK."MBKTRX",1,'0')    AS "OutsrcRemark"        -- 委託機構使用欄 NVARCHAR2 20 預計為計息迄日+額度編號+入帳扣款別
          ,MBK."TRXDAT"                   AS "AcDate"              -- 會計日期 DECIMAL 8 
          ,CASE
             WHEN NVL(MBK."MBKRSN",' ') != ' '
             THEN 'BATX01'
           ELSE '' END                    AS "BatchNo"             -- 批號 VARCHAR2 6 
          ,CASE
             WHEN NVL(MBK."MBKRSN",' ') != ' '
             THEN ROW_NUMBER()
                  OVER (
                    PARTITION BY MBK."TRXIDT"
                    ORDER BY MBK."LMSACN"
                           , MBK."MBKAPN"
                           , MBK."LMSPCN"
                           , MBK."TRXIDT"
                           , MBK."LMSLPD"
                           , MBK."TRXISD"
                           , MBK."ACTACT"
                           , MBK."MBKAMT")
           ELSE 0 END                     AS "DetailSeq"           -- 明細序號 DECIMAL 6 
          ,JOB_START_TIME                 AS "CreateDate"          -- 建檔日期時間 DATE  
          ,'999999'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 
          ,JOB_START_TIME                 AS "LastUpdate"          -- 最後更新日期時間 DATE  
          ,'999999'                       AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 
    FROM "LA$MBKP" MBK
    LEFT JOIN "LA$APLP" APLP ON APLP."LMSACN" = MBK."LMSACN"
                            AND APLP."LMSAPN" = MBK."MBKAPN"
    LEFT JOIN tmpData t on t.LastTRXIDT = MBK."TRXIDT"
    WHERE MBK."LMSPBK" = '3' -- 只抓郵局
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
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_PostDeductMedia_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;


/
