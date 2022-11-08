--------------------------------------------------------
--  DDL for Procedure Usp_Tf_TmpLA$HGTP_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_Tf_TmpLA$HGTP_Ins" 
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
    -- EXECUTE IMMEDIATE 'ALTER TABLE "TmpLA$HGTP" DISABLE PRIMARY KEY CASCADE';
    EXECUTE IMMEDIATE 'TRUNCATE TABLE "TmpLA$HGTP" DROP STORAGE';
    -- EXECUTE IMMEDIATE 'ALTER TABLE "TmpLA$HGTP" ENABLE PRIMARY KEY';

    -- -- 寫入資料
    INSERT INTO "TmpLA$HGTP"
    WITH LBL AS (
      SELECT LMSACN
           , LMSAPN
           , SUM(LMSLBL) AS "LoanBalTotal"
      FROM LA$LMSP
      WHERE LMSLBL <> 0
      GROUP BY LMSACN
             , LMSAPN
    )
    , HGData AS (
      SELECT HG.*
      FROM "LA$HGTP" HG
      LEFT JOIN LA$APLP APLP ON APLP.GDRID1 = HG.GDRID1
                            AND APLP.GDRID2 = HG.GDRID2
                            AND APLP.GDRNUM = HG.GDRNUM
      WHERE HG.LGTADR IS NOT NULL -- 地址非空,空白地址由下一段程式處理
        AND NVL(HG.HGTMHN,0) != 0 -- 主建號
        AND NVL(APLP.LMSACN,0) != 0 -- 已跟額度綁定
        AND HG.GDRNUM2 = 0 -- 新光沒做過唯一性處理的,才由本支程式處理
    )
    , DistinctData AS (
      SELECT S5.CUSENT
            ,S3.LMSACN
            ,S3.LMSAPN
            ,NVL(S4."LoanBalTotal",0) AS "LoanBalTotal"
            ,S2.*
      FROM HGData S2
      LEFT JOIN LA$APLP S3 ON S3.GDRID1 = S2.GDRID1
                          AND S3.GDRID2 = S2.GDRID2
                          AND S3.GDRNUM = S2.GDRNUM
      LEFT JOIN LBL S4 ON S4.LMSACN = S3.LMSACN
                      AND S4.LMSAPN = S3.LMSAPN
      LEFT JOIN CU$CUSP S5 on S5.LMSACN = S3.LMSACN
      WHERE NVL(S3.LMSACN,0) != 0
        AND S2.GDRID1 = 1
    )
    SELECT DENSE_RANK() OVER (ORDER BY S1."LMSACN"
                                      ,S1."HGTAD1"
                                      ,S1."HGTAD2"
                                    ) AS "GroupNo"
          ,0 AS "SecGroupNo"
          ,S1.*
    FROM DistinctData S1
    ;

    -- 記錄寫入筆數
    INS_CNT := INS_CNT + sql%rowcount;

    -- 寫入資料
    -- 2021-10-06 智偉增加此段
    -- 理由: 新壽原本就有要求地址為Null的資料照轉
    INSERT INTO "TmpLA$HGTP"
    WITH "GetGroupNoMax" AS (
      SELECT MAX("GroupNo") AS "Value"
      FROM "TmpLA$HGTP"
    )
    SELECT M."Value" +
           DENSE_RANK() OVER (ORDER BY S1."LMSACN" -- 戶號
                                      ,S1."HGTAD1"
                                      ,S1."HGTAD2"
                                    ) AS "GroupNo"
         , 0 AS "SecGroupNo"
         , S1.*
    FROM ( SELECT DISTINCT
                  S5.CUSENT
                 ,S3.LMSACN
                 ,S3.LMSAPN
                 ,NVL(S4."LoanBalTotal",0) AS "LoanBalTotal"
                 ,S1.*
           FROM "LA$HGTP" S1
           LEFT JOIN LA$APLP S3 ON S3.GDRID1 = S1.GDRID1
                               AND S3.GDRID2 = S1.GDRID2
                               AND S3.GDRNUM = S1.GDRNUM
           LEFT JOIN (SELECT LMSACN
                            ,LMSAPN
                            ,SUM(LMSLBL) AS "LoanBalTotal"
                      FROM LA$LMSP
                      WHERE LMSLBL != 0
                      GROUP BY LMSACN
                              ,LMSAPN
                     ) S4 ON S4.LMSACN = S3.LMSACN
                         AND S4.LMSAPN = S3.LMSAPN
           LEFT JOIN CU$CUSP S5 on S5.LMSACN = S3.LMSACN
           WHERE NVL(S3.LMSACN,0) != 0
             AND S1.GDRID1 = 1
             AND NVL(S1.LGTADR,' ') = ' '
             AND S1.GDRNUM2 = 0 -- 新光沒做過唯一性處理的,才由本支程式處理
         ) S1
        , "GetGroupNoMax" M
    ;

    -- 記錄寫入筆數
    INS_CNT := INS_CNT + sql%rowcount;

    /* 更新組別號碼 已預設過GDRNUM2 未被列入唯一性處理者 */
    INSERT INTO "TmpLA$HGTP"
    SELECT s6."MaxGroupNo" 
          + DENSE_RANK() OVER (ORDER BY S1.GDRNUM2
                              ) AS "GroupNo"
          ,0 AS "SecGroupNo"
          ,NVL(S5.CUSENT,0) AS CUSENT
          ,NVL(S3.LMSACN,0) AS LMSACN
          ,NVL(S3.LMSAPN,0) AS LMSAPN
          ,NVL(S4."LoanBalTotal",0) AS "LoanBalTotal"
          ,S1.*
    FROM "LA$HGTP" S1 -- 組員
    LEFT JOIN "TmpLA$HGTP" S2 ON S2.GDRID1 = S1.GDRID1
                             AND S2.GDRID2 = S1.GDRID2
                             AND S2.GDRNUM = S1.GDRNUM
                             AND S2.LGTSEQ = S1.LGTSEQ
    LEFT JOIN LA$APLP S3 ON S3.GDRID1 = S1.GDRID1
                        AND S3.GDRID2 = S1.GDRID2
                        AND S3.GDRNUM = S1.GDRNUM
    LEFT JOIN (SELECT LMSACN
                     ,LMSAPN
                     ,SUM(LMSLBL) AS "LoanBalTotal"
               FROM LA$LMSP
               WHERE LMSLBL != 0
               GROUP BY LMSACN
                       ,LMSAPN
              ) S4 ON S4.LMSACN = S3.LMSACN
                  AND S4.LMSAPN = S3.LMSAPN
    LEFT JOIN CU$CUSP S5 on S5.LMSACN = S3.LMSACN
    ,(SELECT MAX("GroupNo") AS "MaxGroupNo"
      FROM "TmpLA$HGTP"
     ) S6
    WHERE S1.GDRNUM2 > 0 -- 新光做過唯一性處理的直接寫入
      AND S1.GDRID1 = 1 -- 只取房地
      AND S2.GDRID1 IS NULL -- 未被寫入唯一性處理工作檔
    ;

    -- 記錄寫入筆數
    INS_CNT := INS_CNT + sql%rowcount;

    /* 更新組別號碼 已預設過GDRNUM2,GDRMRK判斷唯一性 */
    MERGE INTO "TmpLA$HGTP" T1
    USING (SELECT S2."GroupNo"
                 ,S2."SecGroupNo"
                 ,S1.LMSACN
                 ,S1.LMSAPN
                 ,S1.GDRID1
                 ,S1.GDRID2
                 ,S1.GDRNUM
                 ,S1.LGTSEQ
                 ,ROW_NUMBER() OVER (PARTITION BY S1.GDRID1
                                                 ,S1.GDRID2
                                                 ,S1.GDRNUM
                                                 ,S1.LGTSEQ
                                     ORDER BY S2.LGTCIF
                                             ,S2.GDRID1
                                             ,S2.GDRID2
                                             ,S2.GDRNUM
                                             ,S2.LGTSEQ) AS SEQ
           FROM "TmpLA$HGTP" S1 -- 組員
           LEFT JOIN "TmpLA$HGTP" S2 ON S2.GDRNUM2 = S1.GDRNUM2
                                    AND S2.GDRMRK = 1 -- 組長
                                    AND S2.LGTCIF = S1.LGTCIF
--                                    AND NVL(S2.HGTCIP,0) = NVL(S1.HGTCIP,0)
           WHERE S1.GDRNUM2 > 0 -- 新光做過唯一性處理的直接寫入
             AND S1.GDRMRK = 0
          ) SC1
    ON (    SC1."LMSACN"  = T1."LMSACN"
        AND SC1."LMSAPN"  = T1."LMSAPN"
        AND SC1."GDRID1"  = T1."GDRID1"
        AND SC1."GDRID2"  = T1."GDRID2"
        AND SC1."GDRNUM"  = T1."GDRNUM"
        AND SC1."LGTSEQ"  = T1."LGTSEQ"
        AND SC1."SEQ"     = 1
    )
    WHEN MATCHED THEN UPDATE SET
        T1."GroupNo"    = SC1."GroupNo"
       ,T1."SecGroupNo" = SC1."SecGroupNo"
    ;

    /* 更新組別號碼 將同組但擔保品提供人不同的資料更新上去 */
    MERGE INTO "TmpLA$HGTP" T1
    USING (SELECT DISTINCT
                  S2."GroupNo"
                 ,S2."SecGroupNo"
                 ,S1.LMSACN
                 ,S1.LMSAPN
                 ,S1.GDRID1
                 ,S1.GDRID2
                 ,S1.GDRNUM
                 ,S1.LGTSEQ
           FROM "TmpLA$HGTP" S1 -- 組員
           LEFT JOIN "TmpLA$HGTP" S2 ON S2.GDRNUM2 = S1.GDRNUM2
                                    AND S2.GDRMRK = 1 -- 組長
--                                    AND NVL(S2.HGTCIP,0) = NVL(S1.HGTCIP,0)
           WHERE S1.GDRNUM2 > 0 -- 有被設定過唯一性
             AND S1.GDRMRK = 0
             AND S1."GroupNo" IS NULL
             AND S1."SecGroupNo" IS NULL
          ) SC1
    ON (    SC1."LMSACN"  = T1."LMSACN"
        AND SC1."LMSAPN"  = T1."LMSAPN"
        AND SC1."GDRID1"  = T1."GDRID1"
        AND SC1."GDRID2"  = T1."GDRID2"
        AND SC1."GDRNUM"  = T1."GDRNUM"
        AND SC1."LGTSEQ"  = T1."LGTSEQ"
    )
    WHEN MATCHED THEN UPDATE SET
        T1."GroupNo"    = SC1."GroupNo"
       ,T1."SecGroupNo" = SC1."SecGroupNo"
    ;

    /* 更新組別子號 */
    MERGE INTO "TmpLA$HGTP" T1
    USING (SELECT S1."GroupNo"
                 ,dense_rank() over (partition by S1."GroupNo" order by S1.hgtmhn)  AS "SecGroupNo"
                 ,S1.LMSACN
                 ,S1.LMSAPN
                 ,S1.GDRID1
                 ,S1.GDRID2
                 ,S1.GDRNUM
                 ,S1.LGTSEQ
           FROM "TmpLA$HGTP" S1
          ) SC1
    ON (    SC1."GroupNo" = T1."GroupNo"
        AND SC1."LMSACN"  = T1."LMSACN"
        AND SC1."LMSAPN"  = T1."LMSAPN"
        AND SC1."GDRID1"  = T1."GDRID1"
        AND SC1."GDRID2"  = T1."GDRID2"
        AND SC1."GDRNUM"  = T1."GDRNUM"
        AND SC1."LGTSEQ"  = T1."LGTSEQ"
    )
    WHEN MATCHED THEN UPDATE SET
        T1."SecGroupNo" = SC1."SecGroupNo"
    ;

    /* 更新組別子號 - 若該組的組別子號只有1 則把組別子號清0 */
    MERGE INTO "TmpLA$HGTP" T1
    USING (SELECT "GroupNo"
                 ,MAX("SecGroupNo") AS "MaxSecGroupNo"
           FROM "TmpLA$HGTP"
           GROUP BY "GroupNo"
          ) SC1
    ON (    SC1."GroupNo" = T1."GroupNo"
        AND SC1."MaxSecGroupNo" = 1
    )
    WHEN MATCHED THEN UPDATE SET
        T1."SecGroupNo" = 0
    ;

    -- 記錄程式結束時間
    JOB_END_TIME := SYSTIMESTAMP;

    commit;

    -- 例外處理
    Exception
    WHEN OTHERS THEN
    ERROR_MSG := SQLERRM || CHR(13) || CHR(10) || dbms_utility.format_error_backtrace;
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_TmpLA$HGTP_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;

/
