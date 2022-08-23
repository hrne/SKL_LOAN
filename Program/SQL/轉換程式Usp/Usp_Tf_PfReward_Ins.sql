--------------------------------------------------------
--  DDL for Procedure Usp_Tf_PfReward_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_Tf_PfReward_Ins" 
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
    EXECUTE IMMEDIATE 'ALTER TABLE "PfReward" DISABLE PRIMARY KEY CASCADE';
    EXECUTE IMMEDIATE 'TRUNCATE TABLE "PfReward" DROP STORAGE';
    EXECUTE IMMEDIATE 'ALTER TABLE "PfReward" ENABLE PRIMARY KEY';

    -- 寫入資料
    INSERT INTO "PfReward"
    WITH rawData AS (
      SELECT DISTINCT
             QTAP."PRZCMD"
           , CASE
               WHEN QTAP."LMSLLD" = 0
               THEN APLP."APLFSD"
             ELSE QTAP."LMSLLD" END AS "LMSLLD"
           , QTAP."LMSACN"
           , QTAP."LMSAPN"
           , QTAP."LMSASQ"
      FROM "LA$QTAP" QTAP
      LEFT JOIN "LA$APLP" APLP ON APLP."LMSACN" = QTAP."LMSACN"
                              AND APLP."LMSAPN" = QTAP."LMSAPN"
      WHERE QTAP."PRZTYP" IN ('1','5')
        AND QTAP."PRZCMD" >= 20211201
    )
    , keyData AS (
      SELECT r."PRZCMD"
           , r."LMSLLD"
           , r."LMSACN"
           , r."LMSAPN"
           , r."LMSASQ"
           , ROW_NUMBER()
             OVER (
                 PARTITION BY r."PRZCMD"
                            , r."LMSLLD"
                            , r."LMSACN"
                            , r."LMSAPN"
                 ORDER BY "LMSASQ"
             ) AS "FacSeq"
      FROM rawData r
    )
    , QI AS (
        SELECT QTAP."PRZCMD"
             , QTAP."LMSLLD"
             , QTAP."LMSACN"
             , QTAP."LMSAPN"
             , QTAP."LMSASQ"
             , QTAP."CUSEM3"
             , LSEP."EMPCOD" -- 協辦人員員編
             , QTAP."PRZCMT" -- 介紹人介紹獎金
             , QTAP."PRZCMD" -- 介紹獎金發放日
        FROM "LA$QTAP" QTAP
        WHERE QTAP."PRZTYP" = '1' -- 介紹獎金
          AND QTAP."PRZCMD" >= 20211201
    )
    , QC AS (
        SELECT QTAP."PRZCMD"
             , CASE
                 WHEN QTAP."LMSLLD" = 0
                 THEN APLP."APLFSD"
               ELSE QTAP."LMSLLD" END AS "LMSLLD" -- *** 協辦獎金的撥款日期會放0
             , QTAP."LMSACN"
             , QTAP."LMSAPN"
             , QTAP."LMSASQ" -- *** 協辦獎金的撥款序號會放0
             , QTAP."CUSEM3" -- *** 協辦獎金的CUSEM3 是協辦人員
             , QTAP."PRZCMT" -- 協辦人員協辦獎金
             , QTAP."PRZCMD" -- 協辦獎金發放日
        FROM "LA$QTAP" QTAP
        LEFT JOIN "LA$APLP" APLP ON APLP."LMSACN" = QTAP."LMSACN"
                                AND APLP."LMSAPN" = QTAP."LMSAPN"
        WHERE QTAP."PRZTYP" = '5' -- 協辦獎金
          AND QTAP."PRZCMD" >= 20211201
    )
    , joinedData AS (
      SELECT K."PRZCMD"                     AS "PRZCMD"
           , K."LMSLLD"                     AS "LMSLLD"
           , K."LMSACN"                     AS "LMSACN"
           , K."LMSAPN"                     AS "LMSAPN"
           , K."LMSASQ"                     AS "LMSASQ"
           , QI."CUSEM3"                    AS "CUSEM3" -- 介紹人
           , NVL(QC."CUSEM3",LSEP."EMPCOD") AS "EMPCOD" -- 協辦人員員編
           , NVL(QI."PRZCMT",0)             AS "IntroducerBonus"     -- 介紹人介紹獎金
           , NVL(QI."PRZCMD",0)             AS "IntroducerBonusDate" -- 介紹獎金發放日
           , NVL(QC."PRZCMT",0)             AS "CoorgnizerBonus"     -- 協辦人員協辦獎金
           , NVL(QC."PRZCMD",0)             AS "CoorgnizerBonusDate" -- 協辦獎金發放日
      FROM keyData K
      LEFT JOIN QI ON QI."PRZCMD" = K."PRZCMD" -- 介紹人
                  AND QI."LMSLLD" = K."LMSLLD"
                  AND QI."LMSACN" = K."LMSACN"
                  AND QI."LMSAPN" = K."LMSAPN"
                  AND QI."LMSASQ" = K."LMSASQ"
      LEFT JOIN QC ON QC."PRZCMD" = K."PRZCMD" -- 協辦人員
                  AND QC."LMSLLD" = K."LMSLLD"
                  AND QC."LMSACN" = K."LMSACN"
                  AND QC."LMSAPN" = K."LMSAPN"
                  AND K."FacSeq" = 1
      LEFT JOIN "LN$LSEP" LSEP ON LSEP."LMSACN" = K."LMSACN"
                              AND LSEP."LMSAPN" = K."LMSAPN"
    )
    SELECT "PfReward_SEQ".nextval         AS "LogNo"
         , S1."LMSLLD"                    AS "PerfDate"            -- 業績日期 DecimalD 8 0
         , S1."LMSACN"                    AS "CustNo"              -- 戶號 DECIMAL 7 0
         , S1."LMSAPN"                    AS "FacmNo"              -- 額度編號 DECIMAL 3 0
         , S1."LMSASQ"                    AS "BormNo"              -- 撥款序號 DECIMAL 3 0
          -- 轉換資料固定擺0:撥款
         , 0                              AS "RepayType"           -- 還款類別 DECIMAL 1 0
         , YAC."CASCDE"                   AS "PieceCode"           -- 計件代碼 VARCHAR2 1 0
         , RPAD(FAC."ProdNo",2,' ')       AS "ProdCode"            -- 商品代碼 VARCHAR2 5 0
         , S1."CUSEM3"                    AS "Introducer"          -- 介紹人員編 VARCHAR2 6 0
         , S2."EMPCOD"                    AS "Coorgnizer"          -- 協辦人員編 VARCHAR2 6 0
         , S2."LSMEM1"                    AS "InterviewerA"        -- 晤談一員編 VARCHAR2 6 0
         , S2."LSMEM2"                    AS "InterviewerB"        -- 晤談二員編 VARCHAR2 6 0
         , S1."IntroducerBonus"           AS "IntroducerBonus"     -- 介紹人介紹獎金 decimal 16 2
         , S1."IntroducerBonusDate"       AS "IntroducerBonusDate" -- 介紹獎金發放日 decimal(8,0)	
         , 0                              AS "IntroducerAddBonus"  -- 介紹人加碼獎勵津貼 decimal 16 2
         , 0                              AS "IntroducerAddBonusDate"
                                                                   -- 獎勵津貼發放日 decimal(8,0)	
         , S1."CoorgnizerBonus"           AS "CoorgnizerBonus"     -- 協辦人員協辦獎金 decimal 16 2
         , S1."CoorgnizerBonusDate"       AS "CoorgnizerBonusDate" -- 協辦獎金發放日 decimal(8,0)	
         , NVL(S3."YGYYMM",0)             AS "WorkMonth"           -- 工作月 decimal 6 0
         , CASE
             WHEN NVL(S3."YGYYMM",0) = 0 THEN 0
             WHEN SUBSTR(TO_CHAR(S3."YGYYMM"),-2) <= '03' THEN TO_NUMBER(SUBSTR(TO_CHAR(S3."YGYYMM"),0,4) || '1')
             WHEN SUBSTR(TO_CHAR(S3."YGYYMM"),-2) <= '06' THEN TO_NUMBER(SUBSTR(TO_CHAR(S3."YGYYMM"),0,4) || '2')
             WHEN SUBSTR(TO_CHAR(S3."YGYYMM"),-2) <= '09' THEN TO_NUMBER(SUBSTR(TO_CHAR(S3."YGYYMM"),0,4) || '3')
           ELSE TO_NUMBER(SUBSTR(TO_CHAR(S3."YGYYMM"),0,4) || '4') END
                                          AS "WorkSeason"          -- 工作季 decimal 5 0
         , JOB_START_TIME                 AS "CreateDate"          -- 建檔日期時間 DATE 8 0
         , '999999'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 0
         , JOB_START_TIME                 AS "LastUpdate"          -- 最後更新日期時間 DATE 8 0
         , '999999'                       AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 0
    FROM joinedData S1
    LEFT JOIN "LN$LSEP" S2 ON S2."LMSACN" = S1."LMSACN"
                          AND S2."LMSAPN" = S1."LMSAPN"
    LEFT JOIN "TB$WKMP" S3 ON S3."DATES" <= S1."LMSLLD"
                          AND S3."DATEE" >= S1."LMSLLD"
    LEFT JOIN "FacMain" FAC ON FAC."CustNo" = S1."LMSACN"
                           AND FAC."FacmNo" = S1."LMSAPN"
    LEFT JOIN ( SELECT DISTINCT
                       YAC."LMSACN"
                     , YAC."LMSAPN"
                     , YAC."LMSASQ"
                     , YAC."CASCDE"
                FROM "LN$YACP" YAC
              ) YAC ON YAC."LMSACN" = S1."LMSACN"
                   AND YAC."LMSAPN" = S1."LMSAPN"
                   AND YAC."LMSASQ" = S1."LMSASQ"
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
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_PfReward_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;

/
