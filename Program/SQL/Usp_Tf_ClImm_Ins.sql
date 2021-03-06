--------------------------------------------------------
--  DDL for Procedure Usp_Tf_ClImm_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "Usp_Tf_ClImm_Ins" 
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
    EXECUTE IMMEDIATE 'ALTER TABLE "ClImm" DISABLE PRIMARY KEY CASCADE';
    EXECUTE IMMEDIATE 'TRUNCATE TABLE "ClImm" DROP STORAGE';
    EXECUTE IMMEDIATE 'ALTER TABLE "ClImm" ENABLE PRIMARY KEY';

    -- 寫入資料
    INSERT INTO "ClImm"
    SELECT S1."ClCode1"                   AS "ClCode1"             -- 擔保品代號1 DECIMAL 1 
          ,S1."ClCode2"                   AS "ClCode2"             -- 擔保品代號2 DECIMAL 2 
          ,S1."ClNo"                      AS "ClNo"                -- 擔保品編號 DECIMAL 7 
          ,CASE WHEN NVL(S2."ESTVAL",0) > 0 THEN S2."ESTVAL" ELSE 0 END
                                          AS "EvaNetWorth"         -- 評估淨值 DECIMAL 16 2
          ,CASE WHEN NVL(S2."RISVAL",0) > 0 THEN S2."RISVAL" ELSE 0 END
                                          AS "LVITax"              -- 土地增值稅 DECIMAL 16 2
          ,CASE WHEN NVL(S2."RSTVAL",0) > 0 THEN S2."RSTVAL" ELSE 0 END
                                          AS "RentEvaValue"        -- 出租評估淨值 DECIMAL 16 2
          ,CASE WHEN NVL(S2."RNTVAL",0) > 0 THEN S2."RNTVAL" ELSE 0 END
                                          AS "RentPrice"           -- 押租金 DECIMAL 16 2
          ,''                             AS "OwnershipCode"       -- 權利種類 VARCHAR2 1 
          -- 0:最高;1:普通
          ,NVL(S2."MTGTYP",'0')           AS "MtgCode"             -- 抵押權註記 VARCHAR2 1 
          ,'N'                            AS "MtgCheck"            -- 最高限額抵押權之擔保債權種類-票據 VARCHAR2 1 
          ,'N'                            AS "MtgLoan"             -- 最高限額抵押權之擔保債權種類-借款 VARCHAR2 1 
          ,'N'                            AS "MtgPledge"           -- 最高限額抵押權之擔保債權種類-保證債務 VARCHAR2 1 
          ,CASE
             WHEN NVL(S2."MTGAGM",'0') = '1'
             THEN 'Y'
           ELSE 'N' END                   AS "Agreement"           -- 檢附同意書 VARCHAR2 1 
          ,CASE
             WHEN S1."ClCode1" = 1
             THEN S4."LGTIID"
             WHEN S1."ClCode1" = 2
             THEN S5."LGTIID"
           ELSE '' END                    AS "EvaCompanyCode"      -- 鑑價公司 VARCHAR2 2 
          ,0                              AS "LimitCancelDate"     -- 限制塗銷日期 decimald 8 
          ,'1'                            AS "ClCode"              -- 擔保註記 VARCHAR2 1 
          ,0                              AS "LoanToValue"         -- 貸放成數(%) DECIMAL 5 2
          ,0                              AS "OtherOwnerTotal"     -- 其他債權人設定總額 DECIMAL 16 2
          ,'0'                            AS "CompensationCopy"    -- 代償後謄本 VARCHAR2 1 
          ,CASE
             WHEN NVL(S2."GDTTMR",' ') <> ' ' 
             THEN TRIM(TO_SINGLE_BYTE(S2."GDTTMR"))
           ELSE '' END                    AS "BdRmk"               -- 建物標示備註 NVARCHAR2 40 
          ,''                             AS "MtgReasonCode"       -- 最高抵押權確定事由 VARCHAR2 1
          ,0                              AS "ReceivedDate"        -- 收文日期 decimald 8
          ,''                             AS "ReceivedNo"          -- 收文案號 VARCHAR2 20
          ,0                              AS "CancelDate"          -- 撤銷日期 decimald 8
          ,''                             AS "CancelNo"            -- 撤銷案號 VARCHAR2 20
          ,CASE
             WHEN S3."GTRCDE" = 0 THEN '2'
             WHEN S3."GTRCDE" = 1 THEN '1'
           ELSE '1' END                   AS "SettingStat"         -- 設定狀態 VARCHAR2 1
          ,'0'                            AS "ClStat"              -- 擔保品狀態 VARCHAR2 1
          ,CASE WHEN NVL(S2."GDTSDT",0) > 0 THEN S2."GDTSDT" ELSE 0 END
                                          AS "SettingDate"         -- 設定日期 decimald 8 
          ,CASE
             WHEN S1."ClCode1" = 1
             THEN NVL(S4."LGTSAM",0)
             WHEN S1."ClCode1" = 2
             THEN NVL(S5."LGTSAM",0)
           ELSE 0 END             AS "SettingAmt"          -- 設定金額 DECIMAL 16 2
          ,NVL(S2."GDTRDT",0)             AS "ClaimDate"           -- 擔保債權確定日期 decimald 8 
          ,CASE WHEN NVL(S2."GDTPTY",0) <> 0 THEN TO_CHAR(S2."GDTPTY") ELSE '' END
                                          AS "SettingSeq"          -- 設定順位(1~4) VARCHAR2 1 
          -- ,CASE WHEN NVL(S2."GDTP1M",' ') <> ' ' THEN S2."GDTP1M" ELSE '' END
          --                                 AS "FirstCreditor"       -- 前一順位債權人 NVARCHAR2 10 
          -- ,CASE WHEN NVL(S2."GDTP1A",0) > 0 THEN S2."GDTP1A" ELSE 0 END
          --                                 AS "FirstAmt"            -- 前一順位金額 DECIMAL 16 2
          -- ,CASE WHEN NVL(S2."GDTP2M",' ') <> ' ' THEN S2."GDTP2M" ELSE '' END
          --                                 AS "SecondCreditor"      -- 前二順位債權人 NVARCHAR2 10 
          -- ,CASE WHEN NVL(S2."GDTP2A",0) > 0 THEN S2."GDTP2A" ELSE 0 END
          --                                 AS "SecondAmt"           -- 前二順位金額 DECIMAL 16 2
          -- ,CASE WHEN NVL(S2."GDTP3M",' ') <> ' ' THEN S2."GDTP3M" ELSE '' END
          --                                 AS "ThirdCreditor"       -- 前三順位債權人 NVARCHAR2 10 
          -- ,CASE WHEN NVL(S2."GDTP3A",0) > 0 THEN S2."GDTP3A" ELSE 0 END
          --                                 AS "ThirdAmt"            -- 前三順位金額 DECIMAL 16 2
          ,JOB_START_TIME                 AS "CreateDate"          -- 建檔日期時間 DATE  
          ,'999999'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 
          ,JOB_START_TIME                 AS "LastUpdate"          -- 最後更新日期時間 DATE  
          ,'999999'                       AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 
    FROM "ClNoMapping" S1
    LEFT JOIN "LA$GDTP" S2 ON S2."GDRID1" = S1."GDRID1"
                          AND S2."GDRID2" = S1."GDRID2"
                          AND S2."GDRNUM" = S1."GDRNUM"
    LEFT JOIN (SELECT "GDRID1"
                     ,"GDRID2"
                     ,"GDRNUM"
                     ,"GTRDAT"
                     ,"GTRCDE"
                     ,ROW_NUMBER() OVER (PARTITION BY "GDRID1"
                                                     ,"GDRID2"
                                                     ,"GDRNUM"
                                         ORDER BY "GTRDAT" DESC) AS "Seq"
               FROM "LA$GTRP"
    ) S3 ON S3."GDRID1" = S1."GDRID1"
        AND S3."GDRID2" = S1."GDRID2"
        AND S3."GDRNUM" = S1."GDRNUM"
        AND S3."Seq" = 1
    LEFT JOIN "LA$HGTP" S4 ON S4."GDRID1" = S1."GDRID1"
                          AND S4."GDRID2" = S1."GDRID2"
                          AND S4."GDRNUM" = S1."GDRNUM"
                          AND S4."LGTSEQ" = S1."LGTSEQ"
                          AND S1."ClCode1" = 1
      LEFT JOIN "LA$LGTP" S5 ON S5."GDRID1" = S1."GDRID1"
                            AND S5."GDRID2" = S1."GDRID2"
                            AND S5."GDRNUM" = S1."GDRNUM"
                            AND S5."LGTSEQ" = S1."LGTSEQ"
                            AND S1."ClCode1" = 2
    WHERE S1."ClCode1" >= 1
      AND S1."ClCode1" <= 2
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
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_ClImm_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;



/
