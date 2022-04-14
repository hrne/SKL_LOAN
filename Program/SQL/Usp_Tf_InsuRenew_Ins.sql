--------------------------------------------------------
--  DDL for Procedure Usp_Tf_InsuRenew_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "Usp_Tf_InsuRenew_Ins" 
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
        "TbsDyf" DECIMAL(8); --系統會計日期
        "MfbsDyf" DECIMAL(8); --當月月底營業日
        "LmnDyf" DECIMAL(8); --前月月底營業日
    BEGIN

    /* 抓系統會計日期 */
    SELECT "TbsDyf"
    INTO "TbsDyf"
    FROM "TxBizDate"
    WHERE "DateCode" = 'ONLINE'
    ;

    /* 抓當月月底營業日 */
    SELECT "MfbsDyf"
    INTO "MfbsDyf"
    FROM "TxBizDate"
    WHERE "DateCode" = 'ONLINE'
    ;

    /* 抓前月月底營業日 */
    SELECT "LmnDyf"
    INTO "LmnDyf"
    FROM "TxBizDate"
    WHERE "DateCode" = 'ONLINE'
    ;

    -- 刪除舊資料
    EXECUTE IMMEDIATE 'ALTER TABLE "InsuRenew" DISABLE PRIMARY KEY CASCADE';
    EXECUTE IMMEDIATE 'TRUNCATE TABLE "InsuRenew" DROP STORAGE';
    EXECUTE IMMEDIATE 'ALTER TABLE "InsuRenew" ENABLE PRIMARY KEY';

    -- 寫入資料
    INSERT INTO "InsuRenew"
    SELECT S0."ClCode1"                   AS "ClCode1"             -- 擔保品-代號1 DECIMAL 1 0
          ,S0."ClCode2"                   AS "ClCode2"             -- 擔保品-代號2 DECIMAL 2 0
          ,S0."ClNo"                      AS "ClNo"                -- 擔保品編號 DECIMAL 7 0
          ,S0."PrevInsuNo"                AS "PrevInsuNo"          -- 原保單號碼 VARCHAR2 17 0
          ,' '                            AS "EndoInsuNo"          -- 批單號碼 VARCHAR2 17 0
          ,S0."InsuYearMonth"             AS "InsuYearMonth"       -- 火險到期年月 DECIMAL 6 0
          ,S0."CustNo"                    AS "CustNo"              -- 借款人戶號 DECIMAL 7 0
          ,S0."FacmNo"                    AS "FacmNo"              -- 額度 DECIMAL 3 0
          ,S0."NowInsuNo"                 AS "NowInsuNo"           -- 保險單號碼 VARCHAR2 17 0
          ,' '                            AS "OrigInsuNo"          -- 原始保險單號碼 VARCHAR2 17 0
          ,CASE
             WHEN FR1P."CHKPRO" = 1
             THEN 1 -- 1.自保
           ELSE 2 -- 2.續保
           END                            AS "RenewCode"           -- 是否續保 DECIMAL 1 0
          ,S0."InsuCompany"               AS "InsuCompany"         -- 保險公司 VARCHAR2 2 0
          ,S0."InsuTypeCode"              AS "InsuTypeCode"        -- 保險類別 VARCHAR2 2 0
          ,S0."RepayCode"                 AS "RepayCode"           -- 繳款方式 DECIMAL 1 0
          ,S0."FireInsuCovrg"             AS "FireInsuCovrg"       -- 火災險保險金額 DECIMAL 14 0
          ,S0."EthqInsuCovrg"             AS "EthqInsuCovrg"       -- 地震險保險金額 DECIMAL 14 0
          ,S0."FireInsuPrem"              AS "FireInsuPrem"        -- 火災險保費 DECIMAL 14 0
          ,S0."EthqInsuPrem"              AS "EthqInsuPrem"        -- 地震險保費 DECIMAL 14 0
          ,S0."InsuStartDate"             AS "InsuStartDate"       -- 保險起日 Decimald 8 0
          ,S0."InsuEndDate"               AS "InsuEndDate"         -- 保險迄日 Decimald 8 0
          ,S0."TotInsuPrem"               AS "TotInsuPrem"         -- 總保費 DECIMAL 14 0
          ,S0."AcDate"                    AS "AcDate"              -- 會計日期 Decimald 8 0
          ,S0."TitaTlrNo"                 AS "TitaTlrNo"           -- 經辦 VARCHAR2 6 0
          ,S0."TitaTxtNo"                 AS "TitaTxtNo"           -- 交易序號 VARCHAR2 8 0
          ,S0."NotiTempFg"                AS "NotiTempFg"          -- 入通知檔 VARCHAR2 1 0
          ,S0."StatusCode"                AS "StatusCode"          -- 處理代碼 DECIMAL 1 0
          ,S0."OvduDate"                  AS "OvduDate"            -- 轉催收日 DECIMAL 8 0
          ,S0."OvduNo"                    AS "OvduNo"              -- 轉催編號 DECIMAL 10 0
          ,''                             AS "CommericalFlag"      -- 住宅險改商業險註記 VARCHAR2 1
          ,''                             AS "Remark"              -- 備註 VARCHAR2 50
          ,JOB_START_TIME                 AS "CreateDate"          -- 建檔日期時間 DATE  
          ,'999999'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 
          ,JOB_START_TIME                 AS "LastUpdate"          -- 最後更新日期時間 DATE  
          ,'999999'                       AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 
    FROM (
      SELECT CNM."ClCode1"                  AS "ClCode1"             -- 擔保品-代號1 DECIMAL 1 0
           , CNM."ClCode2"                  AS "ClCode2"             -- 擔保品-代號2 DECIMAL 2 0
           , CNM."ClNo"                     AS "ClNo"                -- 擔保品編號 DECIMAL 7 0
           , FR1P."INSNUM"                  AS "PrevInsuNo"          -- 原保單號碼 VARCHAR2 17 0
           , FR1P."ADTYMT"                  AS "InsuYearMonth"       -- 火險到期年月 DECIMAL 6 0
           , FR1P."LMSACN"                  AS "CustNo"              -- 借款人戶號 DECIMAL 7 0
           , FR1P."LMSAPN"                  AS "FacmNo"              -- 額度 DECIMAL 3 0
           , FR1P."INSNUM2"                 AS "NowInsuNo"           -- 保險單號碼 VARCHAR2 17 0
           , FR1P."INSIID"                  AS "InsuCompany"         -- 保險公司 VARCHAR2 2 0
           , CASE
               WHEN FR1P."INSIAM2" > 0 -- 火災險保險金額
                    AND FR1P."INSIAE2" > 0 -- 地震險保險金額
               THEN '01' -- 住宅火險地震險
               WHEN FR1P."INSIAM2" > 0 -- 火災險保險金額
               THEN '02' -- 火險
               WHEN FR1P."INSIAE2" > 0 -- 地震險保險金額
               THEN '03' -- 地震險
             ELSE '07' -- 其他
             END                            AS "InsuTypeCode"        -- 保險類別 VARCHAR2 2 0
           , NVL(FR1P."LMSPYS",0)           AS "RepayCode"           -- 繳款方式 DECIMAL 1 0
           , NVL(FR1P."INSIAM2",0)          AS "FireInsuCovrg"       -- 火災險保險金額 DECIMAL 14 0
           , NVL(FR1P."INSIAE2",0)          AS "EthqInsuCovrg"       -- 地震險保險金額 DECIMAL 14 0
           , NVL(FR1P."INSPRM2",0)          AS "FireInsuPrem"        -- 火災險保費 DECIMAL 14 0
           , NVL(FR1P."INSEPM2",0)          AS "EthqInsuPrem"        -- 地震險保費 DECIMAL 14 0
           , NVL(FR1P."INSSDT2",0)          AS "InsuStartDate"       -- 保險起日 Decimald 8 0
           , NVL(FR1P."INSEDT2",0)          AS "InsuEndDate"         -- 保險迄日 Decimald 8 0
           , NVL(FR1P."INSTOT",0)           AS "TotInsuPrem"         -- 總保費 DECIMAL 14 0
           , NVL(FR1P."TRXDAT",0)           AS "AcDate"              -- 會計日期 Decimald 8 0
           , FR1P."UPDATE_IDENT"            AS "TitaTlrNo"           -- 經辦 VARCHAR2 6 0
           , FR1P."TRXNMT"                  AS "TitaTxtNo"           -- 交易序號 VARCHAR2 8 0
           , FR1P."CHKPRT"                  AS "NotiTempFg"          -- 入通知檔 VARCHAR2 1 0
           , CASE
               WHEN FR1P."CHKPRO" = 1 -- 不處理
               THEN 0
               WHEN FR1P."TFRBAD" > 0
               THEN 2 -- 催收
               WHEN "TbsDyf" = "MfbsDyf" -- 若轉換日是月底日
                    AND FR1P."ADTYMT" < TRUNC("TbsDyf" / 100) -- 若資料年月 < 系統會計日期年月
                    AND FR1P."TRXDAT" = 0
               THEN 1 -- 借支
               WHEN "TbsDyf" != "MfbsDyf" -- 若轉換日不是月底日
                    AND FR1P."ADTYMT" < TRUNC("LmnDyf" / 100) -- 若資料年月 < 前月年月
                    AND FR1P."TRXDAT" = 0
               THEN 1 -- 借支
             ELSE 0 END                     AS "StatusCode"          -- 處理代碼 DECIMAL 1 0
           , NVL(FR1P."TFRBAD",0)           AS "OvduDate"            -- 轉催收日 DECIMAL 8 0
           , NVL(FR1P."TFRNO",0)            AS "OvduNo"              -- 轉催編號 DECIMAL 10 0
           , ROW_NUMBER() OVER (PARTITION BY FR1P."INSNUM"
                                ORDER BY FR1P."TRXDAT" DESC
                                       , FR1P."ADTYMT" DESC
                               )            AS "Seq"
      FROM "LN$FR1P" FR1P
      LEFT JOIN "ClNoMap" CNM ON CNM."GdrId1" = FR1P."GDRID1"
                             AND CNM."GdrId2" = FR1P."GDRID2"
                             AND CNM."GdrNum" = FR1P."GDRNUM"
                             AND CNM."LgtSeq" = FR1P."LGTSEQ"
      WHERE NVL(TRIM(FR1P."ADTYMT"),0) > 0
        AND NVL(TRIM(FR1P."INSNUM"),' ') <> ' '
        -- AND NVL(TRIM(FR1P."INSNUM2"),' ') <> ' '
        -- AND NVL(FR1P."CHKPRO",1) = 0 -- 若為1.不處理時,不轉入
        AND NVL(CNM."ClNo",0) > 0
    ) S0
    WHERE S0."Seq" = 1
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
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_InsuRenew_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;




/
