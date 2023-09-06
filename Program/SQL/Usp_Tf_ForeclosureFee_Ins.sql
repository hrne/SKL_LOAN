--------------------------------------------------------
--  DDL for Procedure Usp_Tf_ForeclosureFee_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_Tf_ForeclosureFee_Ins" 
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
    EXECUTE IMMEDIATE 'ALTER TABLE "ForeclosureFee" DISABLE PRIMARY KEY CASCADE';
    EXECUTE IMMEDIATE 'TRUNCATE TABLE "ForeclosureFee" DROP STORAGE';
    EXECUTE IMMEDIATE 'ALTER TABLE "ForeclosureFee" ENABLE PRIMARY KEY';

    -- 寫入資料
    INSERT INTO "ForeclosureFee" (
        "RecordNo"            -- 記錄號碼 DECIMAL 7 
      , "CustNo"              -- 借款人戶號 DECIMAL 7 
      , "FacmNo"              -- 額度編號 DECIMAL 3 
      , "ReceiveDate"         -- 收件日期 DecimalD 8 
      , "DocDate"             -- 單據日期 DecimalD 8 
      , "OpenAcDate"          -- 起帳日期 DecimalD 8 
      , "CloseDate"           -- 銷號日期 DecimalD 8 
      , "Fee"                 -- 法拍費用 DECIMAL 16 2
      , "FeeCode"             -- 科目 VARCHAR2 2 
      , "LegalStaff"          -- 法務人員 VARCHAR2 6 
      , "CloseNo"             -- 銷帳編號 DECIMAL 6 
      , "Rmk"                 -- 備註 NVARCHAR2 60 
      , "CaseCode"            -- 件別 DECIMAL 1 
      , "RemitBranch"         -- 匯款單位 VARCHAR2 3 
      , "Remitter"            -- 匯款人 VARCHAR2 10 
      , "CaseNo"              -- 案號 VARCHAR2 3
      , "OverdueDate"         -- 轉催收日 DecimalD 8 
      , "CreateDate"          -- 建檔日期時間 DATE  
      , "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 
      , "LastUpdate"          -- 最後更新日期時間 DATE  
      , "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 
    )
    WITH sumData AS (
      SELECT M.RECNUM
           , M.LMSACN
           , M.TRXATP
           , M.TFRBAD
           , M.TFRWFN
           , SUM(NVL(S.LGFAMT,0)) AS SUM_AMT
      FROM LN$LGFP M
      LEFT JOIN LN$LGFP S ON S.RECNUM <= M.RECNUM
                         AND S.LMSACN = M.LMSACN
                         AND CASE
                               WHEN S.TFRBAD = 0
                                    AND M.TFRBAD = 0
                               THEN 1
                             ELSE 0 END = 1
                         AND M.TRXATP = 1
                         AND S.TRXATP = 1
      GROUP BY M.RECNUM
             , M.LMSACN
             , M.TRXATP
             , M.TFRBAD
             , M.TFRWFN
    )
    , closeSumData AS (
      SELECT M.RECNUM
           , M.LMSACN
           , SUM(NVL(S.LGFAMT,0)) AS CLOSE_SUM_AMT
      FROM LN$LGFP M
      LEFT JOIN LN$LGFP S ON S.RECNUM <= M.RECNUM
                         AND S.LMSACN = M.LMSACN
                         AND S.TRXATP = M.TRXATP
                         AND CASE
                               WHEN M.ACTCOD != 'G'
                                    AND S.ACTCOD != 'G'
                               THEN 1
                             ELSE 0 END = 1
      WHERE M.TRXATP = 2
      GROUP BY M.RECNUM
             , M.LMSACN
    )
    , orderData AS (
      SELECT M.RECNUM
           , M.LMSACN
           , M.TRXATP
           , M.TFRBAD
           , M.TFRWFN
           , M.SUM_AMT
           , ROW_NUMBER()
             OVER (
              PARTITION BY M.RECNUM
                         , M.LMSACN
              ORDER BY NVL(C.RECNUM,0)
             ) AS "Seq"
           , NVL(C.RECNUM,0) AS CLOSE_NO
           , C.CLOSE_SUM_AMT
      FROM sumData M
      LEFT JOIN closeSumData C ON C.RECNUM > M.RECNUM
                              AND C.LMSACN = M.LMSACN
                              AND C.CLOSE_SUM_AMT >= M.SUM_AMT
                              AND M.TRXATP = 1
                              AND M.TFRBAD = 0
    )
    , closeNoData AS (
      SELECT O.RECNUM
           , O.LMSACN
           , CASE
               WHEN O.TFRWFN != 0
               THEN O.TFRWFN -- 催收已沖銷
               WHEN O.TFRBAD != 0
               THEN 0 -- 催收未沖銷
               WHEN O.SUM_AMT = 0
               THEN O.RECNUM -- 沖銷資料
               WHEN O.CLOSE_SUM_AMT >= O.SUM_AMT
               THEN O.CLOSE_NO -- 已沖銷
             ELSE 0 -- 未沖銷
             END AS "CloseNo"
      FROM orderData O
      LEFT JOIN LN$LGFP L ON L.RECNUM = O.RECNUM
                         AND L.LMSACN = O.LMSACN
      WHERE O."Seq" = 1
    )
    SELECT S1."RECNUM"                    AS "RecordNo"            -- 記錄號碼 DECIMAL 7 
          ,S1."LMSACN"                    AS "CustNo"              -- 借款人戶號 DECIMAL 7 
          ,NVL(S3."FacmNo",1)             AS "FacmNo"              -- 額度編號 DECIMAL 3 
          ,S1."RCVDAT"                    AS "ReceiveDate"         -- 收件日期 DecimalD 8 
          ,S1."TRXIDT"                    AS "DocDate"             -- 單據日期 DecimalD 8 
          ,S1."TRXIDT"                    AS "OpenAcDate"          -- 起帳日期 DecimalD 8 
          ,CASE
             WHEN C."CloseNo" != 0
             THEN S2.RCVDAT
           ELSE 0 END                     AS "CloseDate"           -- 銷號日期 DecimalD 8 
          -- 2022-07-11 Wei from Lai 口頭說明
          -- 貸方轉負數
          -- 借方轉正數
          ,CASE
             WHEN S1."TRXATP" = 1
             THEN S1."LGFAMT"
           ELSE 0 - S1."LGFAMT" END       AS "Fee"                 -- 法拍費用 DECIMAL 16 2
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
          ,C."CloseNo"                   AS "CloseNo"             -- 銷帳編號 DECIMAL 6 
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
    LEFT JOIN closeNoData C ON C.RECNUM = S1.RECNUM
                           AND C.LMSACN = S1.LMSACN
    LEFT JOIN "LN$LGFP" S2 ON S2."CloseNo" != 0
                          AND S2.RECNUM = C."CloseNo"
                          AND S2.LMSACN = S1.LMSACN
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
                FROM ( SELECT "LMSACN" AS "CustNo"
                            , "LMSAPN" AS "FacmNo"
                            , SUM(CASE
                                    WHEN "LMSLLD" <= "TbsDyF" 
                                    THEN "LMSLBL"
                                  ELSE 0 END) AS "LoanBalTotal"
                       FROM "LA$LMSP"
                       GROUP BY "LMSACN"
                              , "LMSAPN"
                     ) LM
              ) S3 ON S3."CustNo" = S1."LMSACN"
                  AND S3."Seq"    = 1
    WHERE S1."TRXIDT" != 0 -- 入帳日期
    ;

    -- 記錄寫入筆數
    INS_CNT := INS_CNT + sql%rowcount;

    -- END;

    -- 記錄程式結束時間
    JOB_END_TIME := SYSTIMESTAMP;

    commit;

    END;
    -- 例外處理
    Exception
    WHEN OTHERS THEN
    ERROR_MSG := SQLERRM || CHR(13) || CHR(10) || dbms_utility.format_error_backtrace;
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_ForeclosureFee_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;




/
