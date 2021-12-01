--------------------------------------------------------
--  DDL for Procedure Usp_Tf_ForeclosureFee_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "Usp_Tf_ForeclosureFee_Ins" 
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
    EXECUTE IMMEDIATE 'ALTER TABLE "ForeclosureFee" DISABLE PRIMARY KEY CASCADE';
    EXECUTE IMMEDIATE 'TRUNCATE TABLE "ForeclosureFee" DROP STORAGE';
    EXECUTE IMMEDIATE 'ALTER TABLE "ForeclosureFee" ENABLE PRIMARY KEY';

    -- INSERT INTO "ForeclosureFee"
    -- SELECT "LN$LG2P"."RECNUM"             AS "RecordNo"            -- 記錄號碼 DECIMAL 7 
    --       ,"LN$LG2P"."LMSACN"             AS "CustNo"              -- 借款人戶號 DECIMAL 7 
    --       ,0                              AS "FacmNo"              -- 額度編號 DECIMAL 3 
    --       ,"LN$LG2P"."RCVDAT"             AS "ReceiveDate"         -- 收件日期 DecimalD 8 
    --       ,"LN$LG2P"."RCVDAT"             AS "DocDate"             -- 單據日期 DecimalD 8 
    --       ,"LN$LG2P"."TRXIDT"             AS "OpenAcDate"          -- 起帳日期 DecimalD 8 
    --       ,"LN$LG2P"."DLTDAT"             AS "CloseDate"           -- 銷號日期 DecimalD 8 
    --       ,"LN$LG2P"."LGFAMT"             AS "Fee"                 -- 法拍費用 DECIMAL 16 2
    --       ,LPAD("LN$LG2P"."ACTCOD",2,'0') AS "FeeCode"             -- 科目 VARCHAR2 2 
    --       ,''                             AS "LegalStaff"          -- 法務人員 VARCHAR2 6 
    --       ,0                              AS "CloseNo"             -- 銷帳編號 DECIMAL 6 
    --       ,"LN$LG2P"."NGRRMK"             AS "Rmk"                 -- 備註 NVARCHAR2 60 
    --       ,NVL("LN$LG2P"."LGFCOD",0)      AS "CaseCode"            -- 件別 DECIMAL 1 
    --       ,NVL("LN$LG2P"."EXGBRH",'')     AS "RemitBranch"         -- 匯款單位 VARCHAR2 3 
    --       ,"LN$LG2P"."EXGEMP"             AS "Remitter"            -- 匯款人 VARCHAR2 10 
    --       ,NVL("LN$LG2P"."CASNUM2",'')    AS "CaseNo"              -- 案號 VARCHAR2 3
    --       ,0                              AS "OverdueDate"         -- 轉催收日 DecimalD 8 
    --       ,JOB_START_TIME                 AS "CreateDate"          -- 建檔日期時間 DATE  
    --       ,'999999'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 
    --       ,JOB_START_TIME                 AS "LastUpdate"          -- 最後更新日期時間 DATE  
    --       ,'999999'                       AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 
    -- FROM "LN$LG2P"
    -- ;

    -- -- 記錄寫入筆數
    -- INS_CNT := INS_CNT + sql%rowcount;

    -- DECLARE
    --     "d_MaxRecordNo" DECIMAL(7);         --記錄號碼
    -- BEGIN

    -- SELECT MAX("RecordNo") INTO "d_MaxRecordNo" FROM "ForeclosureFee";

    -- 寫入資料
    INSERT INTO "ForeclosureFee"
    SELECT S1."RECNUM"                    AS "RecordNo"            -- 記錄號碼 DECIMAL 7 
          ,S1."LMSACN"                    AS "CustNo"              -- 借款人戶號 DECIMAL 7 
          ,NVL(S3."FacmNo",1)             AS "FacmNo"              -- 額度編號 DECIMAL 3 
          ,S1."RCVDAT"                    AS "ReceiveDate"         -- 收件日期 DecimalD 8 
          ,S1."RCVDAT"                    AS "DocDate"             -- 單據日期 DecimalD 8 
          ,S1."RCVDAT"                    AS "OpenAcDate"          -- 起帳日期 DecimalD 8 
          ,CASE
             WHEN S1."TFRBAD" <> 0 -- 轉催收日
                  AND S1."TFRWFN" = 0 -- 催收沖銷號碼
             THEN 0 -- CASE 1 : 轉催後尚未被沖銷
             WHEN S1."RCVDAT" < 20061231
             THEN 20061231
           ELSE NVL(S2."LastRCVDAT",0) END -- 其他 : 依 收件日期 排序,若後面筆數已有全額沖銷則本筆為已沖銷 (2021-03-05 舜雯提供)
                                          AS "CloseDate"           -- 銷號日期 DecimalD 8 
          ,S1."LGFAMT"                    AS "Fee"                 -- 法拍費用 DECIMAL 16 2
          ,CASE
             WHEN S1."ACTCOD" = '1' THEN '01' -- 郵費
             WHEN S1."ACTCOD" = '2' THEN '02' -- 支付命令
             WHEN S1."ACTCOD" = '3' THEN '03' -- 公示送達
             WHEN S1."ACTCOD" = '4' THEN '04' -- 裁定費
             WHEN S1."ACTCOD" = '5' THEN '05' -- 執行費
             WHEN S1."ACTCOD" = '6' THEN '06' -- 測量費
             WHEN S1."ACTCOD" = '7' THEN '07' -- 鑑價費
             WHEN S1."ACTCOD" = '8' THEN '08' -- 刊報費
             WHEN S1."ACTCOD" = '9' THEN '09' -- 假扣押擔保金
             WHEN S1."ACTCOD" = 'A' THEN '10' -- 前項結餘
             WHEN S1."ACTCOD" = 'B' THEN '99' -- 其它
             WHEN S1."ACTCOD" = 'C' THEN '11' -- 全項沖銷
             WHEN S1."ACTCOD" = 'D' THEN '12' -- 退出納課
             WHEN S1."ACTCOD" = 'E' THEN '13' -- 警察陪同費
             WHEN S1."ACTCOD" = 'F' THEN '14' -- 查財產費用
             WHEN S1."ACTCOD" = 'G' THEN '15' -- 催收沖銷
           ELSE '99' END                  AS "FeeCode"             -- 科目 VARCHAR2 2 
          ,''                             AS "LegalStaff"          -- 法務人員 VARCHAR2 6 
          -- 2021-10-04 from 家興
          ,S1."RECNUM"                    AS "CloseNo"             -- 銷帳編號 DECIMAL 6 
          ,S1."NGRRMK"                    AS "Rmk"                 -- 備註 NVARCHAR2 60 
          ,NVL(S1."LGFCOD",0)             AS "CaseCode"            -- 件別 DECIMAL 1 
          ,NVL(S1."EXGBRH",'')            AS "RemitBranch"         -- 匯款單位 VARCHAR2 3 
          ,S1."EXGEMP"                    AS "Remitter"            -- 匯款人 VARCHAR2 10 
          ,NVL(S1."CASNUM2",'')           AS "CaseNo"              -- 案號 VARCHAR2 3
          ,NVL(S1."TFRBAD",0)             AS "OverdueDate"         -- 轉催收日 DecimalD 8 
          ,JOB_START_TIME                 AS "CreateDate"          -- 建檔日期時間 DATE  
          ,'999999'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 
          ,JOB_START_TIME                 AS "LastUpdate"          -- 最後更新日期時間 DATE  
          ,'999999'                       AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 
    FROM "LN$LGFP" S1
    LEFT JOIN ( SELECT S1."RECNUM"
                      ,S1."LMSACN"
                      ,S1."RCVDAT"
                      ,MIN(NVL(S2."RCVDAT",0)) AS "LastRCVDAT"
                FROM "LN$LGFP" S1
                LEFT JOIN "LN$LGFP" S2 ON S2."LMSACN" = S1."LMSACN"
                                      AND S2."RECNUM" >= S1."RECNUM" -- 依 記錄號碼 排序 , 若後面筆數已有全額沖銷則本筆為已沖銷 (2021-03-05 舜雯提供)
                                      AND S2."ACTCOD" IN ('C','G')
                GROUP BY S1."RECNUM"
                        ,S1."LMSACN"
                        ,S1."RCVDAT") S2 ON S2."RECNUM" = S1."RECNUM"
                                        AND S2."LMSACN" = S1."LMSACN"
                                        AND S2."RCVDAT" = S1."RCVDAT"
    LEFT JOIN ( SELECT "CustNo"
                      ,"FacmNo"
                      ,ROW_NUMBER() OVER (PARTITION BY "CustNo"
                                          ORDER BY CASE
                                                     WHEN "LoanBalTotal" > 0 THEN 0
                                                   ELSE 1 END
                                                  ,"FacmNo"
                                          -- 尚有餘額時,取有餘額的最早額度號碼
                                          -- 若皆無餘額時,直接取該戶號下最早的額度號碼
                                         ) "Seq"
                FROM ( SELECT "CustNo"
                             ,"FacmNo"
                             ,SUM("LoanBal") AS "LoanBalTotal"
                       FROM "LoanBorMain"
                       GROUP BY "CustNo"
                               ,"FacmNo"
                     ) LM
              ) S3 ON S3."CustNo" = S1."LMSACN"
                  AND S3."Seq"    = 1
    WHERE S1."TRXIDT" <> 0 -- 入帳日期
    ;

    -- 記錄寫入筆數
    INS_CNT := INS_CNT + sql%rowcount;

    -- END;

    -- 記錄程式結束時間
    JOB_END_TIME := SYSTIMESTAMP;

    commit;

    -- 例外處理
    Exception
    WHEN OTHERS THEN
    ERROR_MSG := SQLERRM || CHR(13) || CHR(10) || dbms_utility.format_error_backtrace;
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_ForeclosureFee_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;



/
