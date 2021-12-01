--------------------------------------------------------
--  DDL for Procedure Usp_Tf_PfReward_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "Usp_Tf_PfReward_Ins" 
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
    SELECT ROW_NUMBER() OVER (ORDER BY S1."LMSLLD"
                                     , S1."LMSACN"
                                     , S1."LMSAPN"
                                     , S1."LMSASQ"
                             )            AS "LogNo"
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
    FROM (SELECT CASE
                   WHEN "LMSLLD" = 0 AND "PRZCMD" < 19110101 THEN "PRZCMD" + 19110000
                   WHEN "LMSLLD" = 0 THEN "PRZCMD"
                   WHEN "LMSLLD" > 0 AND "LMSLLD" < 19110101 THEN "LMSLLD" + 19110000
                 ELSE "LMSLLD" END AS "LMSLLD"
                ,"LMSACN"
                ,"LMSAPN"
                ,"LMSASQ"
                ,MAX(NVL("CUSEM3",' ')) AS "CUSEM3"
                 --  14        ********** #PRZTYP:1- 介紹獎金        2- 放款業務專員津貼           
                 --  15        **********         3- 晤談一人員津貼  4- 晤談二人員津貼             
                 --  16        **********         5- 協辦獎金        6- 專業獎勵金     
                ,SUM(CASE
                       WHEN "PRZTYP" = '1' THEN "PRZCMT"
                     ELSE 0 END) AS "IntroducerBonus"     -- 介紹人介紹獎金 decimal 16 2
                ,SUM(CASE
                       WHEN "PRZTYP" = '1' THEN "PRZCMD"
                     ELSE 0 END) AS "IntroducerBonusDate" -- 介紹獎金發放日 decimal(8,0)	
                ,SUM(CASE
                       WHEN "PRZTYP" = '5' THEN "PRZCMT"
                     ELSE 0 END) AS "CoorgnizerBonus"     -- 協辦人員協辦獎金 decimal 16 2
                ,SUM(CASE
                       WHEN "PRZTYP" = '5' THEN "PRZCMD"
                     ELSE 0 END) AS "CoorgnizerBonusDate" -- 協辦獎金發放日 decimal(8,0)	
          FROM "LA$QTAP"
          WHERE "PRZTYP" IN ('1','5') -- 只取介紹獎金及協辦獎金 排除 6156 筆後剩餘 113584 筆
            AND "LMSLLD" >= 20200610
          GROUP BY CASE
                     WHEN "LMSLLD" = 0 AND "PRZCMD" < 19110101 THEN "PRZCMD" + 19110000
                     WHEN "LMSLLD" = 0 THEN "PRZCMD"
                     WHEN "LMSLLD" > 0 AND "LMSLLD" < 19110101 THEN "LMSLLD" + 19110000
                   ELSE "LMSLLD" END
                  ,"LMSACN"
                  ,"LMSAPN"
                  ,"LMSASQ" -- 群組合併後 實際寫入 112969 筆
         ) S1
    LEFT JOIN "LN$LSEP" S2 ON S2."LMSACN" = S1."LMSACN"
                          AND S2."LMSAPN" = S1."LMSAPN"
    LEFT JOIN "TB$WKMP" S3 ON S3."DATES" <= S1."LMSLLD"
                          AND S3."DATEE" >= S1."LMSLLD"
    LEFT JOIN "FacMain" FAC ON FAC."CustNo" = S1."LMSACN"
                           AND FAC."FacmNo" = S1."LMSAPN"
    LEFT JOIN ( SELECT YAC."LMSACN"
                     , YAC."LMSAPN"
                     , YAC."LMSASQ"
                     , MAX(YAC."CASCDE") AS "CASCDE"
                FROM "LN$YACP" YAC
                GROUP BY YAC."LMSACN"
                       , YAC."LMSAPN"
                       , YAC."LMSASQ"
              ) YAC ON YAC."LMSACN" = S1."LMSACN"
                   AND YAC."LMSAPN" = S1."LMSAPN"
                   AND YAC."LMSASQ" = S1."LMSASQ"
    ;

    -- 記錄寫入筆數
    INS_CNT := INS_CNT + sql%rowcount;

    -- MERGE INTO "PfReward" T1
    -- USING (SELECT S1."PerfDate"
    --              ,S1."CustNo"
    --              ,S1."FacmNo"
    --              ,S1."BormNo"
    --              ,S3."Year" * 100 + S3."Month"   AS "WorkMonth"           -- 工作月 decimal 6 0
    --              ,TO_NUMBER(TO_CHAR(S3."Year") || 
    --                         CASE
    --                           WHEN S3."Month" >= 1 AND S3."Month" <= 3 THEN '1'
    --                           WHEN S3."Month" >= 4 AND S3."Month" <= 6 THEN '2'
    --                           WHEN S3."Month" >= 7 AND S3."Month" <= 9 THEN '3'
    --                         ELSE '4' END )       AS "WorkSeason"          -- 工作季 decimal 5 0
    --        FROM "PfReward" S1
    --        LEFT JOIN "CdWorkMonth" S3 ON S3."StartDate" <= S1."PerfDate"
    --                                  AND S3."EndDate" >= S1."PerfDate"
    --        WHERE S1."PerfDate" > 0
    --          AND NVL(S3."StartDate",0) > 0
    --       ) S1
    -- ON (S1."PerfDate" = T1."PerfDate"
    --     AND S1."CustNo" = T1."CustNo"
    --     AND S1."FacmNo" = T1."FacmNo"
    --     AND S1."BormNo" = T1."BormNo")
    -- WHEN MATCHED THEN UPDATE SET
    --  T1."WorkMonth" = S1."WorkMonth"
    -- ,T1."WorkSeason" = S1."WorkSeason"
    -- ;

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
