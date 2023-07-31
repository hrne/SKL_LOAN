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
    WITH INSP_FIX AS (
      -- 2023-05-30 Wei 補正資料 from Sharepoint\19\資料轉換\不動產押品建物火險檔-擔保品號碼為0
      SELECT CASE
               WHEN INSP."GDRID1" = 0
                    AND INSP."GDRID2" = 0
                    AND INSP."GDRNUM" = 0
                    AND INSP."LGTSEQ" = 0
               THEN CASE
                      WHEN INSNUM = '130094FEP0182908'
                      THEN 1
                      WHEN INSNUM = '130094FEP0134611'
                      THEN 1
                      WHEN INSNUM = '130095FEP0233806'
                      THEN 1
                    ELSE 0 END
             ELSE INSP."GDRID1"
             END                            AS GDRID1
           , CASE
               WHEN INSP."GDRID1" = 0
                    AND INSP."GDRID2" = 0
                    AND INSP."GDRNUM" = 0
                    AND INSP."LGTSEQ" = 0
               THEN CASE
                      WHEN INSNUM = '130094FEP0182908'
                      THEN 1
                      WHEN INSNUM = '130094FEP0134611'
                      THEN 1
                      WHEN INSNUM = '130095FEP0233806'
                      THEN 1
                    ELSE 0 END
             ELSE INSP."GDRID2"
             END                            AS GDRID2
           , CASE
               WHEN INSP."GDRID1" = 0
                    AND INSP."GDRID2" = 0
                    AND INSP."GDRNUM" = 0
                    AND INSP."LGTSEQ" = 0
               THEN CASE
                      WHEN INSNUM = '130094FEP0182908'
                      THEN 44930
                      WHEN INSNUM = '130094FEP0134611'
                      THEN 53354
                      WHEN INSNUM = '130095FEP0233806'
                      THEN 44930
                    ELSE 0 END
             ELSE INSP."GDRNUM"
             END                            AS GDRNUM
           , CASE
               WHEN INSP."GDRID1" = 0
                    AND INSP."GDRID2" = 0
                    AND INSP."GDRNUM" = 0
                    AND INSP."LGTSEQ" = 0
               THEN CASE
                      WHEN INSNUM = '130094FEP0182908'
                      THEN 1
                      WHEN INSNUM = '130094FEP0134611'
                      THEN 1
                      WHEN INSNUM = '130095FEP0233806'
                      THEN 1
                    ELSE 0 END
             ELSE INSP."LGTSEQ"
             END                            AS LGTSEQ
           , INSP."INSSDT"
           , INSP."INSEDT"
           , INSP."INSNUM"
           , INSP."INSIAM"
           , INSP."INSIAE"
           , INSP."INSPRM"
           , INSP."INSEPM"
           , INSP."INSIID"
      FROM "LA$INSP" INSP
    )
    , DISTINCT_INSP_FIX AS (
      SELECT DISTINCT
             GDRID1
           , GDRID2
           , GDRNUM
           , LGTSEQ
      FROM INSP_FIX
    )
    , COUNT_INSP_FIX AS (
      SELECT GDRID1
           , GDRID2
           , GDRNUM
           , COUNT(*) AS CNT
      FROM DISTINCT_INSP_FIX
      GROUP BY GDRID1
             , GDRID2
             , GDRNUM
    )
    , HGTP_RAW AS (
      SELECT GDRID1
           , GDRID2
           , GDRNUM
      FROM LA$HGTP
      GROUP BY GDRID1
             , GDRID2
             , GDRNUM
      HAVING COUNT(*) >= 2
    )
    , HGTP AS (
      SELECT R.GDRID1
           , R.GDRID2
           , R.GDRNUM
           , H.LGTSEQ
           , H.LGTADR
      FROM HGTP_RAW R
      LEFT JOIN LA$HGTP H ON H.GDRID1 = R.GDRID1
                         AND H.GDRID2 = R.GDRID2
                         AND H.GDRNUM = R.GDRNUM
    )
    , INSP_FIX_BY_CASE AS (
      SELECT CASE
               -- 一個擔保品有多個序號,且共用同一張火險單,每個新擔保品編號皆須轉入保險單
               WHEN NVL(H.LGTSEQ,0) != 0
               THEN '1'
               -- 一個擔保品有多個序號,且多個序號各自有火險單,各自轉入
               WHEN C.CNT >= 2
               THEN '2'
             ELSE '3' -- 其他情況
             END               AS "Case"
           , I.GDRID1
           , I.GDRID2
           , I.GDRNUM
           , NVL(H.LGTSEQ,I.LGTSEQ) AS LGTSEQ
           , I.INSSDT
           , I.INSEDT
           , I.INSNUM
           , I.INSIAM
           , I.INSIAE
           , I.INSPRM
           , I.INSEPM
           , I.INSIID
      FROM COUNT_INSP_FIX C
      LEFT JOIN INSP_FIX I ON I.GDRID1 = C.GDRID1
                          AND I.GDRID2 = C.GDRID2
                          AND I.GDRNUM = C.GDRNUM
      LEFT JOIN HGTP H ON H.GDRID1 = C.GDRID1
                      AND H.GDRID2 = C.GDRID2
                      AND H.GDRNUM = C.GDRNUM
                      AND C.CNT = 1 -- 一個擔保品在初保檔,只有用一筆擔保品序號去寫
    )
    , INSP AS (
      SELECT DISTINCT
             CNM."ClCode1"
           , CNM."ClCode2"
           , CNM."ClNo"
           , INSP.INSSDT
           , INSP.INSEDT
           , INSP.INSNUM
           , INSP.INSIAM
           , INSP.INSIAE
           , INSP.INSPRM
           , INSP.INSEPM
           , INSP.INSIID
           , INSP."Case"
           -- 2023-07-31 Wei from Lai : 戶號1426923-002的續保火險單掛在擔保品9xx底下，續保資料應掛在原擔保品號碼
           -- 增加判斷,以原擔保品號碼優先
           , CASE
               WHEN CNM."GdrNum" = "ClNo"
               THEN 0 -- 新舊擔保品號碼相同者 優先
             ELSE CNM."LgtSeq" -- 否則依原擔保品明細序號由小到大排序
             END                            AS "MappingSeq"
      FROM INSP_FIX_BY_CASE INSP
      LEFT JOIN "ClNoMap" CNM ON CNM."GdrId1" = INSP.GDRID1
                             AND CNM."GdrId2" = INSP.GDRID2
                             AND CNM."GdrNum" = INSP.GDRNUM
      WHERE CNM."TfStatus" IN (1,3) -- 轉換留存排掉
    )
    , S AS (
      SELECT NVL(INSP."ClCode1",0)       AS "ClCode1"         -- 擔保品-代號1 DECIMAL 1 0
          , NVL(INSP."ClCode2",0)        AS "ClCode2"         -- 擔保品-代號2 DECIMAL 2 0
          , NVL(INSP."ClNo",0)           AS "ClNo"            -- 擔保品編號 DECIMAL 7 0
          , TRIM(INSP."INSNUM")          AS "OrigInsuNo"      -- 原始保險單號碼 VARCHAR2 17 0
          , ' '                          AS "EndoInsuNo"      -- 批單號碼 VARCHAR2 17 0
          , NVL(INSP."INSIID",' ')       AS "InsuCompany"     -- 保險公司 VARCHAR2 2 0
          , INSP."INSIAM"                AS "FireInsuCovrg"   -- 火災險保險金額 DECIMAL 16 2
          , INSP."INSIAE"                AS "EthqInsuCovrg"   -- 地震險保險金額 DECIMAL 16 2
          , INSP."INSPRM"                AS "FireInsuPrem"    -- 火災險保費 DECIMAL 16 2
          , INSP."INSEPM"                AS "EthqInsuPrem"    -- 地震險保費 DECIMAL 16 2
          , INSP."INSSDT"                AS "InsuStartDate"   -- 保險起日 DECIMAL 8 0
          , INSP."INSEDT"                AS "InsuEndDate"     -- 保險迄日 DECIMAL 8 0
          , JOB_START_TIME               AS "CreateDate"      -- 建檔日期時間 DATE  
          , '999999'                     AS "CreateEmpNo"     -- 建檔人員 VARCHAR2 6 
          , JOB_START_TIME               AS "LastUpdate"      -- 最後更新日期時間 DATE  
          , '999999'                     AS "LastUpdateEmpNo" -- 最後更新人員 VARCHAR2 6 
          , INSP."Case"                  AS "Remark"
      FROM INSP
      WHERE INSP."MappingSeq" = 1 -- 取1筆
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
             WHEN TRUNC(MONTHS_BETWEEN(TO_DATE(DECODE(S."InsuEndDate",0,19110101,S."InsuEndDate"),'YYYYMMDD')
                                      ,TO_DATE(DECODE(S."InsuStartDate",0,19110101,S."InsuStartDate"),'YYYYMMDD')
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
         , S."Remark"                   AS "Remark"          -- 備註 VARCHAR2 50
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
