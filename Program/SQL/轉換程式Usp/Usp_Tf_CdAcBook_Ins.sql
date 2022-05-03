--------------------------------------------------------
--  DDL for Procedure Usp_Tf_CdAcBook_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_Tf_CdAcBook_Ins" 
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
    EXECUTE IMMEDIATE 'ALTER TABLE "CdAcBook" DISABLE PRIMARY KEY CASCADE';
    EXECUTE IMMEDIATE 'TRUNCATE TABLE "CdAcBook" DROP STORAGE';
    EXECUTE IMMEDIATE 'ALTER TABLE "CdAcBook" ENABLE PRIMARY KEY';

    -- 寫入資料
    INSERT INTO "CdAcBook"
    SELECT '000'                          AS "AcBookCode"          -- 帳冊別 VARCHAR2 3 -- 2021-07-15 修改: 000:全公司
         , CASE
             WHEN FSC."ACTFSC" = 'A' THEN '201'
           ELSE '00A' END                 AS "AcSubBookCode"       -- 區隔帳冊 VARCHAR2 3 -- 2021-07-15 新增 00A:傳統帳冊、201:利變帳冊
         , 'TWD'                          AS "CurrencyCode"        -- 幣別
         , FSC."FSCQTA"                   AS "TargetAmt"           -- 放款目標金額 DECIMAL 16 2
         , G1."LoanBalSum"                AS "ActualAmt"           -- 放款實際金額 DECIMAL 16 2
         , FSC."FSCPTY"                   AS "AssignSeq"           -- 分配順序 DECIMAL 2 
         , 'A'                            AS "AcctSource"          --	VARCHAR2(1 BYTE)	Yes		6	資金來源
         , JOB_START_TIME                 AS "CreateDate"          -- 建檔日期時間 DATE 8 
         , '999999'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 
         , JOB_START_TIME                 AS "LastUpdate"          -- 最後更新日期時間 DATE 8 
         , '999999'                       AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 
    FROM "LN$FSCP" FSC
    LEFT JOIN (
      SELECT S2."ACTFSC"
           , SUM(S1."LMSLBL") AS "LoanBalSum"
      FROM "LA$LMSP" S1 
      LEFT JOIN "LA$ACTP" S2 ON S2."LMSACN" = S1."LMSACN"
      WHERE S2."ACTFSC" = 'A'
        AND S1."LMSLBL" > 0
        AND S1."LMSLLD" <= "TbsDyF"
      GROUP BY S2."ACTFSC"
    ) G1 ON G1."ACTFSC" = FSC."ACTFSC"
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
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_CdAcBook_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;

/
