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
          ,DENSE_RANK() OVER (ORDER BY S1.LGTCTY
                                     , NVL(S1.LGTTWN,' ')
                                     , NVL(S1.LGTSGM,' ')
                                     , NVL(S1.LGTSSG,' ')
                                     , S1.LGTNM1
                                     , S1.LGTNM2)
                                    AS "GroupNo"
          ,0 AS "NewGroupNo"
          ,S2.LMSACN
          ,S2.LMSAPN
          ,NVL(S3."LoanBalTotal",0) AS "LoanBalTotal"
          ,S1.*
    FROM "LA$LGTP" S1 
    LEFT JOIN LA$APLP S2 ON S2.GDRID1 = S1.GDRID1
                        AND S2.GDRID2 = S1.GDRID2
                        AND S2.GDRNUM = S1.GDRNUM
    LEFT JOIN (SELECT LMSACN
                     ,LMSAPN
                     ,SUM(LMSLBL) AS "LoanBalTotal"
               FROM LA$LMSP
               WHERE LMSLBL != 0
               GROUP BY LMSACN,LMSAPN
              ) S3 ON S3.LMSACN = S2.LMSACN
                 AND S3.LMSAPN = S2.LMSAPN
    WHERE NVL(S1.LGTCTY,' ') != ' '
      AND NVL(S1.LGTNM1,0) != 0
      AND NVL(S2.LMSACN,0) != 0
      AND S1.GDRID1 = 2
    ;

    -- 記錄寫入筆數
    INS_CNT := INS_CNT + sql%rowcount;

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
                    WHEN S2."TfFg" = 'Y'
                    THEN S3."MaxGroupNo" + RANK() OVER (ORDER BY S1."GroupNo")
                    WHEN S1."GDRID1" = 2
                         AND S1."GDRID2" = 9
                         AND S1."GDRNUM" = 108
                    THEN S3."MaxGroupNo" + RANK() OVER (ORDER BY S1."GroupNo",S1."LGTSEQ")
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
                      WHEN SC1."GDRID1" = 2
                           AND SC1."GDRID2" = 9
                           AND SC1."GDRNUM" = 108
                      THEN 'Y'
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
