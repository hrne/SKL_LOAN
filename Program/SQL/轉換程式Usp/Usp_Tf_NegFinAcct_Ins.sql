create or replace NONEDITIONABLE PROCEDURE "Usp_Tf_NegFinAcct_Ins"  
-- create by ExecSqlApp at 2023-07-12 12:22:04 
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
    DELETE FROM "NegFinAcct"; 
 
    -- 寫入資料 
    INSERT INTO "NegFinAcct" ( 
        "FinCode" -- 債權機構代號 VARCHAR2 8 0 
      , "FinItem" -- 債權機構名稱 NVARCHAR2 60 0 
      , "RemitBank" -- 匯款銀行 VARCHAR2 7 0 
      , "RemitAcct" -- 匯款帳號 VARCHAR2 16 0 
      , "DataSendSection" -- 資料傳送單位 VARCHAR2 8 0 
      , "CreateDate" -- 建檔日期時間 DATE 8 0 
      , "CreateEmpNo" -- 建檔人員 VARCHAR2 6 0 
      , "LastUpdate" -- 最後更新日期時間 DATE 8 0 
      , "LastUpdateEmpNo" -- 最後更新人員 VARCHAR2 6 0 
      , "RemitAcct2" -- 匯款帳號 VARCHAR2 16 0 
      , "RemitAcct3" -- 匯款帳號 VARCHAR2 16 0 
      , "RemitAcct4" -- 匯款帳號 VARCHAR2 16 0 
      , "Enable" 
    ) 
    SELECT JCIC.CREDIT_CODE               AS "FinCode" -- 債權機構代號 VARCHAR2 8 0 
          ,JCIC.CREDIT_NAME               AS "FinItem" -- 債權機構名稱 NVARCHAR2 60 0 
          ,CASE
             WHEN NVL(R1."FinCode",' ') != ' '
             THEN R1."RemitBank"
           ELSE JCIC.REMIT_BANK
           END                            AS "RemitBank" -- 匯款銀行 VARCHAR2 7 0 
          ,CASE
             WHEN NVL(R1."FinCode",' ') != ' '
             THEN R1."RemitAcct"
           ELSE JCIC.REMIT_ACCOUNT
           END                            AS "RemitAcct" -- 匯款帳號 VARCHAR2 16 0 
          ,CASE
             WHEN NVL(R1."FinCode",' ') != ' '
             THEN R1."DataSendSection"
           ELSE JCIC.DATA_SEND_UNIT
           END                            AS "DataSendSection" -- 資料傳送單位 VARCHAR2 8 0 
          ,JOB_START_TIME                 AS "CreateDate" -- 建檔日期時間 DATE 8 0 
          ,'999999'                       AS "CreateEmpNo" -- 建檔人員 VARCHAR2 6 0 
          ,JOB_START_TIME                 AS "LastUpdate" -- 最後更新日期時間 DATE 8 0 
          ,'999999'                       AS "LastUpdateEmpNo" -- 最後更新人員 VARCHAR2 6 0 
          ,CASE
             WHEN NVL(R2."FinCode",' ') != ' '
             THEN R2."RemitAcct2" -- 依盈倩提供資料更新
           ELSE ''
           END                            AS "RemitAcct2" -- 匯款帳號 VARCHAR2 16 0 
          ,CASE
             WHEN NVL(R3."FinCode",' ') != ' '
             THEN R3."RemitAcct3" -- 依盈倩提供資料更新
           ELSE ''
           END                            AS "RemitAcct3" -- 匯款帳號 VARCHAR2 16 0 
          ,CASE
             WHEN NVL(R4."FinCode",' ') != ' '
             THEN R4."RemitAcct4" -- 依盈倩提供資料更新
           ELSE ''
           END                            AS "RemitAcct4" -- 匯款帳號 VARCHAR2 16 0 
          ,CASE
             WHEN NVL(M."FinCode",' ') != ' '
             THEN M."Enable" -- 在M檔有值的為停用
           ELSE 'Y'
           END                            AS "Enable" 
    FROM REMIN_TBJCICACCOUNTDATA JCIC 
    LEFT JOIN "TfMaintainNegFinAcct" M ON M."FinCode" = JCIC.CREDIT_CODE
    LEFT JOIN "TfDeleteNegFinAcct" D ON D."FinCode" = JCIC.CREDIT_CODE
    LEFT JOIN "TfNegFinAcct1" R1 ON R1."FinCode" = JCIC.CREDIT_CODE 
    LEFT JOIN "TfNegFinAcct2" R2 ON R2."FinCode" = JCIC.CREDIT_CODE 
    LEFT JOIN "TfNegFinAcct3" R3 ON R3."FinCode" = JCIC.CREDIT_CODE 
    LEFT JOIN "TfNegFinAcct4" R4 ON R4."FinCode" = JCIC.CREDIT_CODE 
    WHERE NVL(D."FinCode",' ') = ' ' -- 在D檔有值的不轉入
    ; 
 
    -- 記錄寫入筆數 
    INS_CNT := INS_CNT + sql%rowcount; 

    MERGE INTO "NegFinAcct" T
    USING (
    SELECT R1."FinCode"                   AS "FinCode" -- 債權機構代號 VARCHAR2 8 0 
          ,R1."FinItem"                   AS "FinItem" -- 債權機構名稱 NVARCHAR2 60 0 
          ,R1."RemitBank"                 AS "RemitBank" -- 匯款銀行 VARCHAR2 7 0 
          ,R1."RemitAcct"                 AS "RemitAcct" -- 匯款帳號 VARCHAR2 16 0 
          ,R1."DataSendSection"           AS "DataSendSection" -- 資料傳送單位 VARCHAR2 8 0 
          ,JOB_START_TIME                 AS "CreateDate" -- 建檔日期時間 DATE 8 0 
          ,'999999'                       AS "CreateEmpNo" -- 建檔人員 VARCHAR2 6 0 
          ,JOB_START_TIME                 AS "LastUpdate" -- 最後更新日期時間 DATE 8 0 
          ,'999999'                       AS "LastUpdateEmpNo" -- 最後更新人員 VARCHAR2 6 0 
          ,CASE
             WHEN NVL(R2."FinCode",' ') != ' '
             THEN R2."RemitAcct2" -- 依盈倩提供資料更新
           ELSE ''
           END                            AS "RemitAcct2" -- 匯款帳號 VARCHAR2 16 0 
          ,CASE
             WHEN NVL(R3."FinCode",' ') != ' '
             THEN R3."RemitAcct3" -- 依盈倩提供資料更新
           ELSE ''
           END                            AS "RemitAcct3" -- 匯款帳號 VARCHAR2 16 0 
          ,CASE
             WHEN NVL(R4."FinCode",' ') != ' '
             THEN R4."RemitAcct4" -- 依盈倩提供資料更新
           ELSE ''
           END                            AS "RemitAcct4" -- 匯款帳號 VARCHAR2 16 0 
          ,CASE
             WHEN NVL(M."FinCode",' ') != ' '
             THEN M."Enable" -- 在M檔有值的為停用
           ELSE 'Y'
           END                            AS "Enable" 
    FROM "TfNegFinAcct1" R1 
    LEFT JOIN "TfMaintainNegFinAcct" M ON M."FinCode" = R1."FinCode"
    LEFT JOIN "TfDeleteNegFinAcct" D ON D."FinCode" = R1."FinCode"
    LEFT JOIN "TfNegFinAcct2" R2 ON R2."FinCode" = R1."FinCode"
    LEFT JOIN "TfNegFinAcct3" R3 ON R3."FinCode" = R1."FinCode"
    LEFT JOIN "TfNegFinAcct4" R4 ON R4."FinCode" = R1."FinCode"
    WHERE NVL(D."FinCode",' ') = ' ' -- 在D檔有值的不轉入
    ) S
    ON (
        S."FinCode" = T."FinCode"
    )
    WHEN MATCHED THEN UPDATE 
    SET "RemitBank" = S."RemitBank"
      , "RemitAcct" = S."RemitAcct"
      , "DataSendSection" = S."DataSendSection"
    WHEN NOT MATCHED THEN INSERT (
        "FinCode" -- 債權機構代號 VARCHAR2 8 0 
      , "FinItem" -- 債權機構名稱 NVARCHAR2 60 0 
      , "RemitBank" -- 匯款銀行 VARCHAR2 7 0 
      , "RemitAcct" -- 匯款帳號 VARCHAR2 16 0 
      , "DataSendSection" -- 資料傳送單位 VARCHAR2 8 0 
      , "CreateDate" -- 建檔日期時間 DATE 8 0 
      , "CreateEmpNo" -- 建檔人員 VARCHAR2 6 0 
      , "LastUpdate" -- 最後更新日期時間 DATE 8 0 
      , "LastUpdateEmpNo" -- 最後更新人員 VARCHAR2 6 0 
      , "RemitAcct2" -- 匯款帳號 VARCHAR2 16 0 
      , "RemitAcct3" -- 匯款帳號 VARCHAR2 16 0 
      , "RemitAcct4" -- 匯款帳號 VARCHAR2 16 0 
      , "Enable" 
    ) VALUES (
        S."FinCode" -- 債權機構代號 VARCHAR2 8 0 
      , S."FinItem" -- 債權機構名稱 NVARCHAR2 60 0 
      , S."RemitBank" -- 匯款銀行 VARCHAR2 7 0 
      , S."RemitAcct" -- 匯款帳號 VARCHAR2 16 0 
      , S."DataSendSection" -- 資料傳送單位 VARCHAR2 8 0 
      , S."CreateDate" -- 建檔日期時間 DATE 8 0 
      , S."CreateEmpNo" -- 建檔人員 VARCHAR2 6 0 
      , S."LastUpdate" -- 最後更新日期時間 DATE 8 0 
      , S."LastUpdateEmpNo" -- 最後更新人員 VARCHAR2 6 0 
      , S."RemitAcct2" -- 匯款帳號 VARCHAR2 16 0 
      , S."RemitAcct3" -- 匯款帳號 VARCHAR2 16 0 
      , S."RemitAcct4" -- 匯款帳號 VARCHAR2 16 0 
      , S."Enable" 
    )
    ;

    -- 記錄寫入筆數 
    INS_CNT := INS_CNT + sql%rowcount; 

    MERGE INTO "NegFinAcct" T
    USING (
    SELECT R2."FinCode"                   AS "FinCode" -- 債權機構代號 VARCHAR2 8 0 
          ,R2."FinItem"                   AS "FinItem" -- 債權機構名稱 NVARCHAR2 60 0 
          ,R2."RemitBank"                 AS "RemitBank" -- 匯款銀行 VARCHAR2 7 0 
          ,R2."RemitAcct"                 AS "RemitAcct" -- 匯款帳號 VARCHAR2 16 0 
          ,R2."DataSendSection"           AS "DataSendSection" -- 資料傳送單位 VARCHAR2 8 0 
          ,JOB_START_TIME                 AS "CreateDate" -- 建檔日期時間 DATE 8 0 
          ,'999999'                       AS "CreateEmpNo" -- 建檔人員 VARCHAR2 6 0 
          ,JOB_START_TIME                 AS "LastUpdate" -- 最後更新日期時間 DATE 8 0 
          ,'999999'                       AS "LastUpdateEmpNo" -- 最後更新人員 VARCHAR2 6 0 
          ,CASE
             WHEN NVL(R2."FinCode",' ') != ' '
             THEN R2."RemitAcct2" -- 依盈倩提供資料更新
           ELSE ''
           END                            AS "RemitAcct2" -- 匯款帳號 VARCHAR2 16 0 
          ,CASE
             WHEN NVL(R3."FinCode",' ') != ' '
             THEN R3."RemitAcct3" -- 依盈倩提供資料更新
           ELSE ''
           END                            AS "RemitAcct3" -- 匯款帳號 VARCHAR2 16 0 
          ,CASE
             WHEN NVL(R4."FinCode",' ') != ' '
             THEN R4."RemitAcct4" -- 依盈倩提供資料更新
           ELSE ''
           END                            AS "RemitAcct4" -- 匯款帳號 VARCHAR2 16 0 
          ,CASE
             WHEN NVL(M."FinCode",' ') != ' '
             THEN M."Enable" -- 在M檔有值的為停用
           ELSE 'Y'
           END                            AS "Enable" 
    FROM "TfNegFinAcct2" R2
    LEFT JOIN "TfMaintainNegFinAcct" M ON M."FinCode" = R2."FinCode"
    LEFT JOIN "TfDeleteNegFinAcct" D ON D."FinCode" = R2."FinCode"
    LEFT JOIN "TfNegFinAcct3" R3 ON R3."FinCode" = R2."FinCode"
    LEFT JOIN "TfNegFinAcct4" R4 ON R4."FinCode" = R2."FinCode"
    WHERE NVL(D."FinCode",' ') = ' ' -- 在D檔有值的不轉入
    ) S
    ON (
        S."FinCode" = T."FinCode"
    )
    WHEN MATCHED THEN UPDATE 
    SET "RemitAcct2" = S."RemitAcct2"
    WHEN NOT MATCHED THEN INSERT (
        "FinCode" -- 債權機構代號 VARCHAR2 8 0 
      , "FinItem" -- 債權機構名稱 NVARCHAR2 60 0 
      , "RemitBank" -- 匯款銀行 VARCHAR2 7 0 
      , "RemitAcct" -- 匯款帳號 VARCHAR2 16 0 
      , "DataSendSection" -- 資料傳送單位 VARCHAR2 8 0 
      , "CreateDate" -- 建檔日期時間 DATE 8 0 
      , "CreateEmpNo" -- 建檔人員 VARCHAR2 6 0 
      , "LastUpdate" -- 最後更新日期時間 DATE 8 0 
      , "LastUpdateEmpNo" -- 最後更新人員 VARCHAR2 6 0 
      , "RemitAcct2" -- 匯款帳號 VARCHAR2 16 0 
      , "RemitAcct3" -- 匯款帳號 VARCHAR2 16 0 
      , "RemitAcct4" -- 匯款帳號 VARCHAR2 16 0 
      , "Enable" 
    ) VALUES (
        S."FinCode" -- 債權機構代號 VARCHAR2 8 0 
      , S."FinItem" -- 債權機構名稱 NVARCHAR2 60 0 
      , S."RemitBank" -- 匯款銀行 VARCHAR2 7 0 
      , S."RemitAcct" -- 匯款帳號 VARCHAR2 16 0 
      , S."DataSendSection" -- 資料傳送單位 VARCHAR2 8 0 
      , S."CreateDate" -- 建檔日期時間 DATE 8 0 
      , S."CreateEmpNo" -- 建檔人員 VARCHAR2 6 0 
      , S."LastUpdate" -- 最後更新日期時間 DATE 8 0 
      , S."LastUpdateEmpNo" -- 最後更新人員 VARCHAR2 6 0 
      , S."RemitAcct2" -- 匯款帳號 VARCHAR2 16 0 
      , S."RemitAcct3" -- 匯款帳號 VARCHAR2 16 0 
      , S."RemitAcct4" -- 匯款帳號 VARCHAR2 16 0 
      , S."Enable" 
    )
    ;
    
    -- 記錄寫入筆數 
    INS_CNT := INS_CNT + sql%rowcount; 

    MERGE INTO "NegFinAcct" T
    USING (
    SELECT R3."FinCode"                   AS "FinCode" -- 債權機構代號 VARCHAR2 8 0 
          ,R3."FinItem"                   AS "FinItem" -- 債權機構名稱 NVARCHAR2 60 0 
          ,R3."RemitBank"                 AS "RemitBank" -- 匯款銀行 VARCHAR2 7 0 
          ,R3."RemitAcct"                 AS "RemitAcct" -- 匯款帳號 VARCHAR2 16 0 
          ,R3."DataSendSection"           AS "DataSendSection" -- 資料傳送單位 VARCHAR2 8 0 
          ,JOB_START_TIME                 AS "CreateDate" -- 建檔日期時間 DATE 8 0 
          ,'999999'                       AS "CreateEmpNo" -- 建檔人員 VARCHAR2 6 0 
          ,JOB_START_TIME                 AS "LastUpdate" -- 最後更新日期時間 DATE 8 0 
          ,'999999'                       AS "LastUpdateEmpNo" -- 最後更新人員 VARCHAR2 6 0 
          ,CASE
             WHEN NVL(R2."FinCode",' ') != ' '
             THEN R2."RemitAcct2" -- 依盈倩提供資料更新
           ELSE ''
           END                            AS "RemitAcct2" -- 匯款帳號 VARCHAR2 16 0 
          ,CASE
             WHEN NVL(R3."FinCode",' ') != ' '
             THEN R3."RemitAcct3" -- 依盈倩提供資料更新
           ELSE ''
           END                            AS "RemitAcct3" -- 匯款帳號 VARCHAR2 16 0 
          ,CASE
             WHEN NVL(R4."FinCode",' ') != ' '
             THEN R4."RemitAcct4" -- 依盈倩提供資料更新
           ELSE ''
           END                            AS "RemitAcct4" -- 匯款帳號 VARCHAR2 16 0 
          ,CASE
             WHEN NVL(M."FinCode",' ') != ' '
             THEN M."Enable" -- 在M檔有值的為停用
           ELSE 'Y'
           END                            AS "Enable" 
    FROM "TfNegFinAcct3" R3
    LEFT JOIN "TfMaintainNegFinAcct" M ON M."FinCode" = R3."FinCode"
    LEFT JOIN "TfDeleteNegFinAcct" D ON D."FinCode" = R3."FinCode"
    LEFT JOIN "TfNegFinAcct2" R2 ON R2."FinCode" = R3."FinCode"
    LEFT JOIN "TfNegFinAcct4" R4 ON R4."FinCode" = R3."FinCode"
    WHERE NVL(D."FinCode",' ') = ' ' -- 在D檔有值的不轉入
    ) S
    ON (
        S."FinCode" = T."FinCode"
    )
    WHEN MATCHED THEN UPDATE 
    SET "RemitAcct3" = S."RemitAcct3"
    WHEN NOT MATCHED THEN INSERT (
        "FinCode" -- 債權機構代號 VARCHAR2 8 0 
      , "FinItem" -- 債權機構名稱 NVARCHAR2 60 0 
      , "RemitBank" -- 匯款銀行 VARCHAR2 7 0 
      , "RemitAcct" -- 匯款帳號 VARCHAR2 16 0 
      , "DataSendSection" -- 資料傳送單位 VARCHAR2 8 0 
      , "CreateDate" -- 建檔日期時間 DATE 8 0 
      , "CreateEmpNo" -- 建檔人員 VARCHAR2 6 0 
      , "LastUpdate" -- 最後更新日期時間 DATE 8 0 
      , "LastUpdateEmpNo" -- 最後更新人員 VARCHAR2 6 0 
      , "RemitAcct2" -- 匯款帳號 VARCHAR2 16 0 
      , "RemitAcct3" -- 匯款帳號 VARCHAR2 16 0 
      , "RemitAcct4" -- 匯款帳號 VARCHAR2 16 0 
      , "Enable" 
    ) VALUES (
        S."FinCode" -- 債權機構代號 VARCHAR2 8 0 
      , S."FinItem" -- 債權機構名稱 NVARCHAR2 60 0 
      , S."RemitBank" -- 匯款銀行 VARCHAR2 7 0 
      , S."RemitAcct" -- 匯款帳號 VARCHAR2 16 0 
      , S."DataSendSection" -- 資料傳送單位 VARCHAR2 8 0 
      , S."CreateDate" -- 建檔日期時間 DATE 8 0 
      , S."CreateEmpNo" -- 建檔人員 VARCHAR2 6 0 
      , S."LastUpdate" -- 最後更新日期時間 DATE 8 0 
      , S."LastUpdateEmpNo" -- 最後更新人員 VARCHAR2 6 0 
      , S."RemitAcct2" -- 匯款帳號 VARCHAR2 16 0 
      , S."RemitAcct3" -- 匯款帳號 VARCHAR2 16 0 
      , S."RemitAcct4" -- 匯款帳號 VARCHAR2 16 0 
      , S."Enable" 
    )
    ;
    
    -- 記錄寫入筆數 
    INS_CNT := INS_CNT + sql%rowcount; 

    MERGE INTO "NegFinAcct" T
    USING (
    SELECT R4."FinCode"                   AS "FinCode" -- 債權機構代號 VARCHAR2 8 0 
          ,R4."FinItem"                   AS "FinItem" -- 債權機構名稱 NVARCHAR2 60 0 
          ,R4."RemitBank"                 AS "RemitBank" -- 匯款銀行 VARCHAR2 7 0 
          ,R4."RemitAcct"                 AS "RemitAcct" -- 匯款帳號 VARCHAR2 16 0 
          ,R4."DataSendSection"           AS "DataSendSection" -- 資料傳送單位 VARCHAR2 8 0 
          ,JOB_START_TIME                 AS "CreateDate" -- 建檔日期時間 DATE 8 0 
          ,'999999'                       AS "CreateEmpNo" -- 建檔人員 VARCHAR2 6 0 
          ,JOB_START_TIME                 AS "LastUpdate" -- 最後更新日期時間 DATE 8 0 
          ,'999999'                       AS "LastUpdateEmpNo" -- 最後更新人員 VARCHAR2 6 0 
          ,CASE
             WHEN NVL(R2."FinCode",' ') != ' '
             THEN R2."RemitAcct2" -- 依盈倩提供資料更新
           ELSE ''
           END                            AS "RemitAcct2" -- 匯款帳號 VARCHAR2 16 0 
          ,CASE
             WHEN NVL(R3."FinCode",' ') != ' '
             THEN R3."RemitAcct3" -- 依盈倩提供資料更新
           ELSE ''
           END                            AS "RemitAcct3" -- 匯款帳號 VARCHAR2 16 0 
          ,CASE
             WHEN NVL(R4."FinCode",' ') != ' '
             THEN R4."RemitAcct4" -- 依盈倩提供資料更新
           ELSE ''
           END                            AS "RemitAcct4" -- 匯款帳號 VARCHAR2 16 0 
          ,CASE
             WHEN NVL(M."FinCode",' ') != ' '
             THEN M."Enable" -- 在M檔有值的為停用
           ELSE 'Y'
           END                            AS "Enable" 
    FROM "TfNegFinAcct4" R4 
    LEFT JOIN "TfMaintainNegFinAcct" M ON M."FinCode" = R4."FinCode"
    LEFT JOIN "TfDeleteNegFinAcct" D ON D."FinCode" = R4."FinCode"
    LEFT JOIN "TfNegFinAcct2" R2 ON R2."FinCode" = R4."FinCode"
    LEFT JOIN "TfNegFinAcct3" R3 ON R3."FinCode" = R4."FinCode"
    WHERE NVL(D."FinCode",' ') = ' ' -- 在D檔有值的不轉入
    ) S
    ON (
        S."FinCode" = T."FinCode"
    )
    WHEN MATCHED THEN UPDATE 
    SET "RemitAcct4" = S."RemitAcct4"
    WHEN NOT MATCHED THEN INSERT (
        "FinCode" -- 債權機構代號 VARCHAR2 8 0 
      , "FinItem" -- 債權機構名稱 NVARCHAR2 60 0 
      , "RemitBank" -- 匯款銀行 VARCHAR2 7 0 
      , "RemitAcct" -- 匯款帳號 VARCHAR2 16 0 
      , "DataSendSection" -- 資料傳送單位 VARCHAR2 8 0 
      , "CreateDate" -- 建檔日期時間 DATE 8 0 
      , "CreateEmpNo" -- 建檔人員 VARCHAR2 6 0 
      , "LastUpdate" -- 最後更新日期時間 DATE 8 0 
      , "LastUpdateEmpNo" -- 最後更新人員 VARCHAR2 6 0 
      , "RemitAcct2" -- 匯款帳號 VARCHAR2 16 0 
      , "RemitAcct3" -- 匯款帳號 VARCHAR2 16 0 
      , "RemitAcct4" -- 匯款帳號 VARCHAR2 16 0 
      , "Enable" 
    ) VALUES (
        S."FinCode" -- 債權機構代號 VARCHAR2 8 0 
      , S."FinItem" -- 債權機構名稱 NVARCHAR2 60 0 
      , S."RemitBank" -- 匯款銀行 VARCHAR2 7 0 
      , S."RemitAcct" -- 匯款帳號 VARCHAR2 16 0 
      , S."DataSendSection" -- 資料傳送單位 VARCHAR2 8 0 
      , S."CreateDate" -- 建檔日期時間 DATE 8 0 
      , S."CreateEmpNo" -- 建檔人員 VARCHAR2 6 0 
      , S."LastUpdate" -- 最後更新日期時間 DATE 8 0 
      , S."LastUpdateEmpNo" -- 最後更新人員 VARCHAR2 6 0 
      , S."RemitAcct2" -- 匯款帳號 VARCHAR2 16 0 
      , S."RemitAcct3" -- 匯款帳號 VARCHAR2 16 0 
      , S."RemitAcct4" -- 匯款帳號 VARCHAR2 16 0 
      , S."Enable" 
    )
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
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_NegFinAcct_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace); 
END;