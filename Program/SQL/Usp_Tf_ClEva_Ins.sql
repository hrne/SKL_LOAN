CREATE OR REPLACE PROCEDURE "Usp_Tf_ClEva_Ins" 
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
    EXECUTE IMMEDIATE 'ALTER TABLE "ClEva" DISABLE PRIMARY KEY CASCADE';
    EXECUTE IMMEDIATE 'TRUNCATE TABLE "ClEva" DROP STORAGE';
    EXECUTE IMMEDIATE 'ALTER TABLE "ClEva" ENABLE PRIMARY KEY';

    -- 寫入資料
    -- 擔保品號碼NULL者 找出舊的擔保品關係 依照其群組號碼 找出該轉入的那筆 寫入
    INSERT INTO "ClEva" (
      "ClCode1" -- 擔保品代號1 DECIMAL 1  擔保品代號檔CdCl
      , "ClCode2" -- 擔保品代號2 DECIMAL 2  擔保品代號檔CdCl
      , "ClNo" -- 擔保品編號 DECIMAL 7  
      , "EvaNo" -- 鑑估序號 DECIMAL 2  
      , "EvaDate" -- 鑑價日期 DecimalD 8  
      , "EvaAmt" -- 評估總價 DECIMAL 16 2 必須輸入
      , "EvaNetWorth" -- 評估淨值 DECIMAL 16 2 可不輸入
      , "RentEvaValue" -- 出租評估淨值 DECIMAL 16 2 可不輸入
      , "EvaCompanyId" -- 估價公司代碼 VARCHAR2 2  
      , "EvaCompanyName" -- 估價公司名稱 NVARCHAR2 100  
      , "EvaEmpno" -- 估價人員 VARCHAR2 6  
      , "EvaReason" -- 重評原因 DECIMAL 2  "01:原因1;02:原因2;99:其他原因"
      , "OtherReason" -- 其他重評原因 NVARCHAR2 100  
      , "CreateDate" -- 建檔日期時間 DATE   
      , "CreateEmpNo" -- 建檔人員 VARCHAR2 6  
      , "LastUpdate" -- 最後更新日期時間 DATE   
      , "LastUpdateEmpNo" -- 最後更新人員 VARCHAR2 6 
    )
    SELECT CNM."ClCode1"               AS "ClCode1" -- 擔保品代號1 DECIMAL 1  擔保品代號檔CdCl
         , CNM."ClCode2"               AS "ClCode2" -- 擔保品代號2 DECIMAL 2  擔保品代號檔CdCl
         , CNM."ClNo"                  AS "ClNo" -- 擔保品編號 DECIMAL 7  
         , ROW_NUMBER()
           OVER (
            PARTITION BY CNM."ClCode1"
                       , CNM."ClCode2"
                       , CNM."ClNo"
            ORDER BY NVL(GDTP."GDTIDT",0)
                   , CNM.GDRID1
                   , CNM.GDRID2
                   , CNM.GDRNUM
           )                           AS "EvaNo" -- 鑑估序號 DECIMAL 2  
         , NVL(GDTP."GDTIDT",0)        AS "EvaDate" -- 鑑價日期 DecimalD 8  
         , NVL(GDTP."ETTVAL",0)        AS "EvaAmt" -- 評估總價 DECIMAL 16 2 必須輸入
         , NVL(S2."ESTVAL",0)          AS "EvaNetWorth" -- 評估淨值 DECIMAL 16 2 可不輸入
         , NVL(S2."RSTVAL",0)          AS "RentEvaValue" -- 出租評估淨值 DECIMAL 16 2 可不輸入
         , CASE 
             WHEN CNM."ClCode1" = 1 
             THEN S4."LGTIID" 
             WHEN CNM."ClCode1" = 2 
             THEN S5."LGTIID" 
           ELSE '' END                 AS "EvaCompanyId" -- 估價公司代碼 VARCHAR2 2  
         , "Fn_GetCdCode"('EvaCompanyCode'
             ,CASE 
                WHEN CNM."ClCode1" = 1 
                THEN S4."LGTIID" 
                WHEN CNM."ClCode1" = 2 
                THEN S5."LGTIID" 
              ELSE '' END )           AS "EvaCompanyName" -- 估價公司名稱 NVARCHAR2 100  
         , APLP."CUSEM6"              AS "EvaEmpno" -- 估價人員 VARCHAR2 6  
         -- 舊資料轉入時，重評原因以計件代碼判斷後轉入
         , CASE
             WHEN APLP."CASCDE" IN ('1','2','A','B','F','G')
             THEN 1 -- 新貸件
             WHEN APLP."CASCDE" IN ('5','E')
             THEN 2 -- 展期件
             WHEN APLP."CASCDE" IN ('4','D')
             THEN 3 -- 增貸件
             WHEN APLP."CASCDE" = '6'
             THEN 4 -- 動支件
           ELSE 99 END                AS "EvaReason" -- 重評原因 DECIMAL 2  "01:原因1;02:原因2;99:其他原因"
         , '資料轉換'                  AS "OtherReason" -- 其他重評原因 NVARCHAR2 100  
         , JOB_START_TIME             AS "CreateDate" -- 建檔日期時間 DATE   
         , '999999'                   AS "CreateEmpNo" -- 建檔人員 VARCHAR2 6  
         , JOB_START_TIME             AS "LastUpdate" -- 最後更新日期時間 DATE   
         , '999999'                   AS "LastUpdateEmpNo" -- 最後更新人員 VARCHAR2 6 
    FROM "ClNoMap" CNM
    LEFT JOIN LA$GDTP GDTP 
      ON GDTP.GDRID1 = CNM."GdrId1"
     AND GDTP.GDRID2 = CNM."GdrId2"
     AND GDTP.GDRNUM = CNM."GdrNum"
    LEFT JOIN LA$HGTP HGTP 
      ON HGTP.GDRID1 = CNM."GdrId1" 
     AND HGTP.GDRID2 = CNM."GdrId2" 
     AND HGTP.GDRNUM = CNM."GdrNum" 
     AND HGTP.LGTSEQ = CNM."LgtSeq" 
     AND CNM."ClCode1" = 1 
    LEFT JOIN LA$LGTP LGTP
      ON LGTP.GDRID1 = CNM."GdrId1" 
     AND LGTP.GDRID2 = CNM."GdrId2" 
     AND LGTP.GDRNUM = CNM."GdrNum" 
     AND LGTP.LGTSEQ = CNM."LgtSeq" 
     AND CNM."ClCode1" = 2 
    LEFT JOIN LA$APLP APLP
      ON APLP.GDRID1 = CNM."GdrId1"
     AND APLP.GDRID2 = CNM."GdrId2"
     AND APLP.GDRNUM = CNM."GdrNum"
    WHERE CNM."ClCode1" IN (1,2)
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
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_ClEva_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;

/
