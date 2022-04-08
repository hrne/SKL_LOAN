--------------------------------------------------------
--  DDL for Procedure Usp_Tf_TmpLA$LGTP_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "Usp_Tf_TmpLA$LGTP_Ins" 
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
    -- EXECUTE IMMEDIATE 'ALTER TABLE "TmpLA$LGTP" DISABLE PRIMARY KEY CASCADE';
    EXECUTE IMMEDIATE 'TRUNCATE TABLE "TmpLA$LGTP" DROP STORAGE';
    -- EXECUTE IMMEDIATE 'ALTER TABLE "TmpLA$LGTP" ENABLE PRIMARY KEY';

    -- -- 寫入資料
    INSERT INTO "TmpLA$LGTP"
    SELECT ' ' AS "TfFg"
          ,S0."GroupNo"
          ,0 AS "NewGroupNo"
          ,S2.LMSACN
          ,S2.LMSAPN
          ,NVL(S3."LoanBalTotal",0) AS "LoanBalTotal"
          ,S1.*
    FROM (SELECT DENSE_RANK() OVER (ORDER BY S1.LGTCIF
                                            ,S1.LGTCTY
                                            ,NVL(S1.LGTTWN,' ')
                                            ,NVL(S1.LGTSGM,' ')
                                            ,NVL(S1.LGTSSG,' ')
                                            ,S1.LGTNM1
                                            ,S1.LGTNM2)
                                    AS "GroupNo"
                ,S1.LGTCIF
                ,S1.LGTCTY
                ,NVL(S1.LGTTWN,' ') AS "LGTTWN"
                ,NVL(S1.LGTSGM,' ') AS "LGTSGM"
                ,NVL(S1.LGTSSG,' ') AS "LGTSSG"
                ,S1.LGTNM1
                ,S1.LGTNM2
          FROM "LA$LGTP" s1
          LEFT JOIN CU$CUSP S2 ON S2.CUSCIF = S1.LGTCIF
          LEFT JOIN LA$APLP S3 ON S3.GDRID1 = S1.GDRID1
                              AND S3.GDRID2 = S1.GDRID2
                              AND S3.GDRNUM = S1.GDRNUM
          WHERE NVL(LGTCTY,' ') <> ' '
          AND NVL(LGTNM1,0) <> 0
          -- AND NVL(LGTCIF,0) <> 0
          -- AND NVL(S2.CUSCIF,0) <> 0
          AND NVL(S3.LMSACN,0) <> 0
          AND S1.GDRID1 = 2
          GROUP BY S1.LGTCIF
                  ,S1.LGTCTY
                  ,NVL(S1.LGTTWN,' ')
                  ,NVL(S1.LGTSGM,' ')
                  ,NVL(S1.LGTSSG,' ')
                  ,S1.LGTNM1
                  ,S1.LGTNM2
          HAVING COUNT(*) >= 2
          ORDER BY S1.LGTCIF
                  ,S1.LGTCTY
                  ,NVL(S1.LGTTWN,' ')
                  ,NVL(S1.LGTSGM,' ')
                  ,NVL(S1.LGTSSG,' ')
                  ,S1.LGTNM1
                  ,S1.LGTNM2
    ) S0
    LEFT JOIN (SELECT S1.*
               FROM "LA$LGTP" S1
               LEFT JOIN CU$CUSP S2 ON S2.CUSCIF = S1.LGTCIF
               LEFT JOIN LA$APLP S3 ON S3.GDRID1 = S1.GDRID1
                                   AND S3.GDRID2 = S1.GDRID2
                                   AND S3.GDRNUM = S1.GDRNUM
               WHERE NVL(LGTCTY,' ') <> ' '
                 AND NVL(LGTNM1,0) <> 0
                --  AND NVL(LGTCIF,0) <> 0
                --  AND NVL(S2.CUSCIF,0) <> 0
                 AND NVL(S3.LMSACN,0) <> 0 
                 AND S1.GDRID1 = 2
              ) S1 ON S1.LGTCIF = S0.LGTCIF
                  AND S1.LGTCTY = S0.LGTCTY
                  AND NVL(S1.LGTTWN,' ') = S0."LGTTWN"
                  AND NVL(S1.LGTSGM,' ') = S0."LGTSGM"
                  AND NVL(S1.LGTSSG,' ') = S0."LGTSSG"
                  AND S1.LGTNM1 = S0.LGTNM1
                  AND S1.LGTNM2 = S0.LGTNM2
    LEFT JOIN LA$APLP S2 ON S2.GDRID1 = S1.GDRID1
                        AND S2.GDRID2 = S1.GDRID2
                        AND S2.GDRNUM = S1.GDRNUM
    LEFT JOIN (SELECT LMSACN
                     ,LMSAPN
                     ,SUM(LMSLBL) AS "LoanBalTotal"
               FROM LA$LMSP
               WHERE LMSLBL <> 0
               GROUP BY LMSACN,LMSAPN
              ) S3 ON S3.LMSACN = S2.LMSACN
                  AND S3.LMSAPN = S2.LMSAPN
    WHERE NVL(S2.LMSACN,0) <> 0
      AND S1.GDRID1 = 2
    ;

    -- 記錄寫入筆數
    INS_CNT := INS_CNT + sql%rowcount;

    /* 更新組別號碼 已預設過GDRNUM2,GDRMRK判斷唯一性 */
    MERGE INTO "TmpLA$LGTP" T1
    USING (SELECT S2."GroupNo"
                 ,S2."NewGroupNo"
                 ,S1.LMSACN
                 ,S1.LMSAPN
                 ,S1.GDRID1
                 ,S1.GDRID2
                 ,S1.GDRNUM
                 ,S1.LGTSEQ
           FROM "TmpLA$LGTP" S1 -- 組員
           LEFT JOIN "TmpLA$LGTP" S2 ON S2.GDRNUM2 = S1.GDRNUM2
                                    AND S2.GDRMRK = 1 -- 組長
           WHERE S1.GDRNUM2 > 0 -- 有被設定過唯一性
             AND S1.GDRMRK = 0
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
       ,T1."NewGroupNo" = SC1."NewGroupNo"
    ;

    /* 更新TfFg */
    MERGE INTO "TmpLA$LGTP" T1
    USING (SELECT S1."GroupNo"
                 ,S1.GDRID1
                 ,S1.GDRID2
                 ,S1.GDRNUM
                 ,S1.LGTSEQ
                 ,S1.GDRMRK
                 ,ROW_NUMBER() OVER (PARTITION BY S1."GroupNo"
                                     ORDER BY CASE
                                                WHEN S1.GDRID1 = 2 THEN 0
                                              ELSE S1.GDRID1 END
                                             ,S1.GDRID2
                                             ,S1.GDRNUM DESC
                                             ,S1.LGTSEQ) AS "SEQ"
                 ,NVL(S2."TfFg",' ')     AS "BuildingTfFg"
                 ,CASE
                    WHEN S2."TfFg" = 'Y' THEN S3."MaxGroupNo" + RANK() OVER (ORDER BY S1."GroupNo")
                  ELSE 0 END  AS "NewGroupNo"
           FROM "TmpLA$LGTP" S1
           LEFT JOIN "ClBuildingUnique" S2 ON S2.GDRID1 = S1.GDRID1
                                          AND S2.GDRID2 = S1.GDRID2
                                          AND S2.GDRNUM = S1.GDRNUM
                                          AND S2.LGTSEQ = S1.LGTSEQ
                                          AND S2."TfFg" = 'Y'
           LEFT JOIN (SELECT MAX("GroupNo") AS "MaxGroupNo"
                      FROM "TmpLA$LGTP"
                     ) S3 ON 1 = 1
          ) SC1
    ON (    SC1."GroupNo" = T1."GroupNo"
        AND SC1."GDRID1"  = T1."GDRID1"
        AND SC1."GDRID2"  = T1."GDRID2"
        AND SC1."GDRNUM"  = T1."GDRNUM"
        AND SC1."LGTSEQ"  = T1."LGTSEQ"
    )
    WHEN MATCHED THEN UPDATE SET
        T1."TfFg" = CASE
                      WHEN SC1."GDRMRK" = 1 THEN 'Y'
                      WHEN SC1."SEQ" = 1 THEN 'Y'
                      WHEN SC1."BuildingTfFg" = 'Y' THEN 'Y'
                    ELSE ' ' END
       ,T1."NewGroupNo" = SC1."NewGroupNo"
    ;

    -- 記錄程式結束時間
    JOB_END_TIME := SYSTIMESTAMP;

    commit;

    -- 例外處理
    Exception
    WHEN OTHERS THEN
    ERROR_MSG := SQLERRM || CHR(13) || CHR(10) || dbms_utility.format_error_backtrace;
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_TmpLA$LGTP_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;


/
