--------------------------------------------------------
--  DDL for Procedure Usp_Tf_InsuOrignal_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_Tf_InsuOrignal_Ins" 
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
    EXECUTE IMMEDIATE 'ALTER TABLE "InsuOrignal" DISABLE PRIMARY KEY CASCADE';
    EXECUTE IMMEDIATE 'TRUNCATE TABLE "InsuOrignal" DROP STORAGE';
    EXECUTE IMMEDIATE 'ALTER TABLE "InsuOrignal" ENABLE PRIMARY KEY';

    -- 寫入資料
    INSERT INTO "InsuOrignal" (
        "ClCode1"         -- 擔保品-代號1 DECIMAL 1 0
      , "ClCode2"         -- 擔保品-代號2 DECIMAL 2 0
      , "ClNo"            -- 擔保品編號 DECIMAL 7 0
      , "OrigInsuNo"      -- 原始保險單號碼 VARCHAR2 17 0
      , "EndoInsuNo"      -- 批單號碼 VARCHAR2 17 0
      , "InsuCompany"     -- 保險公司 VARCHAR2 2 0
      , "InsuTypeCode"    -- 保險類別 VARCHAR2 2 0
      , "FireInsuCovrg"   -- 火災險保險金額 DECIMAL 16 2
      , "EthqInsuCovrg"   -- 地震險保險金額 DECIMAL 16 2
      , "FireInsuPrem"    -- 火災險保費 DECIMAL 16 2
      , "EthqInsuPrem"    -- 地震險保費 DECIMAL 16 2
      , "InsuStartDate"   -- 保險起日 DECIMAL 8 0
      , "InsuEndDate"     -- 保險迄日 DECIMAL 8 0
      , "CommericalFlag"  -- 住宅險改商業險註記 VARCHAR2 1
      , "Remark"          -- 備註 VARCHAR2 50
      , "CreateDate"      -- 建檔日期時間 DATE  
      , "CreateEmpNo"     -- 建檔人員 VARCHAR2 6 
      , "LastUpdate"      -- 最後更新日期時間 DATE  
      , "LastUpdateEmpNo" -- 最後更新人員 VARCHAR2 6 
    )
    WITH NOW_INSUNO AS (
      SELECT DISTINCT 
             "INSNUM2"
      FROM "LN$FR1P"
      WHERE "INSNUM2" IS NOT NULL
    )
    , INSP AS (
      SELECT INSP."GDRID1"
           , INSP."GDRID2"
           , INSP."GDRNUM"
           , INSP."LGTSEQ"
           , INSP."INSSDT"
           , INSP."INSEDT"
           , INSP."INSNUM"
           , INSP."INSIAM"
           , INSP."INSIAE"
           , INSP."INSPRM"
           , INSP."INSEPM"
           , INSP."INSIID"
           , ROW_NUMBER()
             OVER (
              PARTITION BY INSP."INSNUM"
              ORDER BY INSP."INSSDT"
                     , INSP."GDRID1"
                     , INSP."GDRID2"
                     , INSP."GDRNUM"
                     , INSP."LGTSEQ"
             ) AS "Seq" 
      FROM "LA$INSP" INSP
    )
    , FR1P AS (
      SELECT FR1P."GDRID1"
           , FR1P."GDRID2"
           , FR1P."GDRNUM"
           , FR1P."LGTSEQ"
           , FR1P."INSNUM" -- 批單號碼 VARCHAR2 17 0
           , FR1P."INSSDT"
           , FR1P."INSEDT"
           , ROW_NUMBER()
             OVER (
              PARTITION BY CNM."ClCode1"
                         , CNM."ClCode2"
                         , CNM."ClNo"
                         , FR1P."INSNUM"
              ORDER BY FR1P."INSSDT"
                     , FR1P."GDRID1"
                     , FR1P."GDRID2"
                     , FR1P."GDRNUM"
                     , FR1P."LGTSEQ"
             ) AS "Seq" 
      FROM "LN$FR1P" FR1P
      LEFT JOIN "ClNoMap" CNM ON CNM."GdrId1" = FR1P."GDRID1"
                             AND CNM."GdrId2" = FR1P."GDRID2"
                             AND CNM."GdrNum" = FR1P."GDRNUM"
                             AND CNM."LgtSeq" = FR1P."LGTSEQ"
      WHERE FR1P."INSNUM2" IS NULL
        AND FR1P."INSSDT2" > 0
        AND FR1P."CHKPRO" = 1
    )
    , S AS (
      SELECT CNM."ClCode1"                AS "ClCode1"         -- 擔保品-代號1 DECIMAL 1 0
          , CNM."ClCode2"                AS "ClCode2"         -- 擔保品-代號2 DECIMAL 2 0
          , CNM."ClNo"                   AS "ClNo"            -- 擔保品編號 DECIMAL 7 0
          , TRIM(INSP."INSNUM")          AS "OrigInsuNo"      -- 原始保險單號碼 VARCHAR2 17 0
          , NVL(TRIM(FR1P."INSNUM"),' ') AS "EndoInsuNo"      -- 批單號碼 VARCHAR2 17 0
          , MAX(NVL(INSP."INSIID",' '))  AS "InsuCompany"     -- 保險公司 VARCHAR2 2 0
          , MAX(INSP."INSIAM")           AS "FireInsuCovrg"   -- 火災險保險金額 DECIMAL 16 2
          , MAX(INSP."INSIAE")           AS "EthqInsuCovrg"   -- 地震險保險金額 DECIMAL 16 2
          , MAX(INSP."INSPRM")           AS "FireInsuPrem"    -- 火災險保費 DECIMAL 16 2
          , MAX(INSP."INSEPM")           AS "EthqInsuPrem"    -- 地震險保費 DECIMAL 16 2
          , MIN(INSP."INSSDT")           AS "InsuStartDate"   -- 保險起日 DECIMAL 8 0
          , MAX(INSP."INSEDT")           AS "InsuEndDate"     -- 保險迄日 DECIMAL 8 0
          , JOB_START_TIME               AS "CreateDate"      -- 建檔日期時間 DATE  
          , '999999'                     AS "CreateEmpNo"     -- 建檔人員 VARCHAR2 6 
          , JOB_START_TIME               AS "LastUpdate"      -- 最後更新日期時間 DATE  
          , '999999'                     AS "LastUpdateEmpNo" -- 最後更新人員 VARCHAR2 6 
      FROM INSP
      LEFT JOIN FR1P ON FR1P."GDRID1" = INSP."GDRID1"
                    AND FR1P."GDRID2" = INSP."GDRID2"
                    AND FR1P."GDRNUM" = INSP."GDRNUM"
                    AND FR1P."LGTSEQ" = INSP."LGTSEQ"
                    AND FR1P."INSSDT" = INSP."INSSDT"
                    AND FR1P."INSEDT" = INSP."INSEDT"
                    AND FR1P."Seq"    = 1
                    AND FR1P."INSNUM" != INSP."INSNUM"
      LEFT JOIN "ClNoMap" CNM ON CNM."GdrId1" = INSP."GDRID1"
                            AND CNM."GdrId2" = INSP."GDRID2"
                            AND CNM."GdrNum" = INSP."GDRNUM"
                            AND CNM."LgtSeq" = INSP."LGTSEQ"
      LEFT JOIN NOW_INSUNO ON NOW_INSUNO.INSNUM2 = TRIM(INSP."INSNUM")
      WHERE INSP."Seq" = 1
        AND NVL(CNM."ClNo",0) > 0 -- 擔保品存在
        AND NVL(INSP."INSIID",' ') != ' '
        AND NVL(NOW_INSUNO.INSNUM2,' ') = ' '
      GROUP BY CNM."ClCode1"
            , CNM."ClCode2"
            , CNM."ClNo"
            , TRIM(INSP."INSNUM")
            , NVL(TRIM(FR1P."INSNUM"),' ')
    )
    SELECT S."ClCode1"                  AS "ClCode1"         -- 擔保品-代號1 DECIMAL 1 0
         , S."ClCode2"                  AS "ClCode2"         -- 擔保品-代號2 DECIMAL 2 0
         , S."ClNo"                     AS "ClNo"            -- 擔保品編號 DECIMAL 7 0
         , S."OrigInsuNo"               AS "OrigInsuNo"      -- 原始保險單號碼 VARCHAR2 17 0
         , S."EndoInsuNo"               AS "EndoInsuNo"      -- 批單號碼 VARCHAR2 17 0
         , S."InsuCompany"              AS "InsuCompany"     -- 保險公司 VARCHAR2 2 0
         , CASE
             -- 2022-10-31 Wei 
             -- 新增判斷
             -- 1: FAP、FAE、FEP、FEE、FFP、FNP
             -- 2: 保險起迄日少於一年時歸類短期保單
             -- from USER 淑微 PT2 UAT測試問題 第322號
             WHEN S."OrigInsuNo" LIKE '%FAP%' -- 商業火單
             THEN '08'
             WHEN S."OrigInsuNo" LIKE '%FAE%' -- 商業火單加批保單
             THEN '10'
             WHEN S."OrigInsuNo" LIKE '%FEP%' -- 住宅火險地震險
             THEN '01'
             WHEN S."OrigInsuNo" LIKE '%FEE%' -- 住宅火險地震險加批保單
             THEN '11'
             WHEN S."OrigInsuNo" LIKE '%FFP%' -- 住宅火災及地震基本保險(甲式)
             THEN '12'
             WHEN S."OrigInsuNo" LIKE '%FNP%' -- 住宅地震基本保險
             THEN '13'
             WHEN TRUNC(MONTHS_BETWEEN(
                    TO_DATE(S."InsuEndDate",'YYYYMMDD')
                    , TO_DATE(S."InsuStartDate",'YYYYMMDD')
                  )) < 12
             THEN '09' -- 短期保單
             WHEN S."FireInsuCovrg" > 0 -- 火災險保險金額
                  AND S."EthqInsuCovrg" > 0 -- 地震險保險金額
             THEN '01' -- 住宅火險地震險
             WHEN S."FireInsuCovrg" > 0 -- 火災險保險金額
             THEN '02' -- 火險
             WHEN S."EthqInsuCovrg" > 0 -- 地震險保險金額
             THEN '03' -- 地震險
           ELSE '07' -- 其他
           END                          AS "InsuTypeCode"    -- 保險類別 VARCHAR2 2 0
         , S."FireInsuCovrg"            AS "FireInsuCovrg"   -- 火災險保險金額 DECIMAL 16 2
         , S."EthqInsuCovrg"            AS "EthqInsuCovrg"   -- 地震險保險金額 DECIMAL 16 2
         , S."FireInsuPrem"             AS "FireInsuPrem"    -- 火災險保費 DECIMAL 16 2
         , S."EthqInsuPrem"             AS "EthqInsuPrem"    -- 地震險保費 DECIMAL 16 2
         , S."InsuStartDate"            AS "InsuStartDate"   -- 保險起日 DECIMAL 8 0
         , S."InsuEndDate"              AS "InsuEndDate"     -- 保險迄日 DECIMAL 8 0
         , ''                           AS "CommericalFlag"  -- 住宅險改商業險註記 VARCHAR2 1
         , ''                           AS "Remark"          -- 備註 VARCHAR2 50
         , S."CreateDate"               AS "CreateDate"      -- 建檔日期時間 DATE  
         , S."CreateEmpNo"              AS "CreateEmpNo"     -- 建檔人員 VARCHAR2 6 
         , S."LastUpdate"               AS "LastUpdate"      -- 最後更新日期時間 DATE  
         , S."LastUpdateEmpNo"          AS "LastUpdateEmpNo" -- 最後更新人員 VARCHAR2 6 
    FROM S
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
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_InsuOrignal_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;

/
