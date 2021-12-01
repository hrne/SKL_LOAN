--------------------------------------------------------
--  DDL for Procedure Usp_Tf_ClBuildingUnique_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "Usp_Tf_ClBuildingUnique_Ins" 
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
    -- EXECUTE IMMEDIATE 'ALTER TABLE "ClBuildingUnique" DISABLE PRIMARY KEY CASCADE';
    EXECUTE IMMEDIATE 'TRUNCATE TABLE "ClBuildingUnique" DROP STORAGE';
    -- EXECUTE IMMEDIATE 'ALTER TABLE "ClBuildingUnique" ENABLE PRIMARY KEY';

    -- -- 寫入資料 (舊擔保品編號檔)
    INSERT INTO "ClBuildingUnique"
    SELECT CASE
             WHEN S1."GDRMRK" = 1 THEN 'Y'
             WHEN S1."GDRNUM2" > 0 THEN ' '
             WHEN S1."GDRNUM" = "MaxGDRNUM" AND S1."LGTSEQ" = "MaxLGTSEQ" THEN 'Y'
           ELSE ' ' END AS "TfFg"
          ,S1."GroupNo"
          ,S1."SecGroupNo"
          ,S1."LMSACN"
          ,S1."LMSAPN"
          ,S1."GDRID1"
          ,S1."GDRID2"
          ,S1."GDRNUM"
          ,S1."LGTSEQ"
    FROM "TmpLA$HGTP" S1
    LEFT JOIN (SELECT "GroupNo"
                     ,"SecGroupNo"
                     ,MAX(GDRNUM) AS "MaxGDRNUM"
               FROM "TmpLA$HGTP"
               GROUP BY "GroupNo","SecGroupNo"
              ) S4 ON S4."GroupNo" = S1."GroupNo"
                  AND S4."SecGroupNo" = S1."SecGroupNo"
    LEFT JOIN (SELECT "GroupNo"
                     ,"SecGroupNo"
                     ,"GDRNUM"
                     ,MAX("LGTSEQ") AS "MaxLGTSEQ"
               FROM "TmpLA$HGTP"
               GROUP BY "GroupNo","SecGroupNo","GDRNUM"
              ) S4 ON S4."GroupNo" = S1."GroupNo"
                  AND S4."SecGroupNo" = S1."SecGroupNo"
                  AND S4."GDRNUM" = S1."GDRNUM"
    ;

    -- 記錄寫入筆數
    INS_CNT := INS_CNT + sql%rowcount;

    -- 更新ClBuildingUnique 同組內有兩個Y者 增加SecGroupNo
    MERGE INTO "ClBuildingUnique" T1
    USING (SELECT S0."GroupNo"
                 ,S0."SecGroupNo"
                 ,S1."MaxSecGroupNo"
                 ,ROW_NUMBER() OVER (PARTITION BY S0."GroupNo"
                                       ORDER BY S2."GDRID1"
                                               ,S2."GDRID2"
                                               ,S2."GDRNUM"
                                               ,S2."LGTSEQ"
                                    ) AS "Seq"
                 ,S2."GDRID1"
                 ,S2."GDRID2"
                 ,S2."GDRNUM"
                 ,S2."LGTSEQ"
           FROM (SELECT "GroupNo"
                       ,"SecGroupNo"
                       ,COUNT(*) AS CNT
                 FROM "ClBuildingUnique"
                 WHERE "TfFg" = 'Y'
                 GROUP BY "GroupNo","SecGroupNo"
                 HAVING COUNT(*) >= 2
                ) S0
           LEFT JOIN (SELECT "GroupNo"
                            ,MAX("SecGroupNo") AS "MaxSecGroupNo"
                      FROM "ClBuildingUnique"
                      GROUP BY "GroupNo"
                     ) S1 ON S1."GroupNo" = S0."GroupNo"
           LEFT JOIN "ClBuildingUnique" S2 ON S2."GroupNo" = S0."GroupNo"
                                          AND S2."SecGroupNo" = S0."SecGroupNo"
                                          AND S2."TfFg" = 'Y'
          ) SC1
    ON (    SC1."GroupNo" = T1."GroupNo"
        AND SC1."GDRID1"  = T1."GDRID1"
        AND SC1."GDRID2"  = T1."GDRID2"
        AND SC1."GDRNUM"  = T1."GDRNUM"
        AND SC1."LGTSEQ"  = T1."LGTSEQ"
    )
    WHEN MATCHED THEN UPDATE SET
    T1."SecGroupNo" = CASE 
                        WHEN SC1."MaxSecGroupNo" = 0
                        THEN SC1."Seq"
                        WHEN SC1."MaxSecGroupNo" > 0
                             AND SC1."Seq" = 1
                        THEN SC1."SecGroupNo"
                      ELSE SC1."MaxSecGroupNo" + SC1."Seq" - 1 END
    ;

    -- 更新ClBuildingUnique 同組內有兩個Y者 增加SecGroupNo
    MERGE INTO "ClBuildingUnique" T1
    USING (SELECT S2."GroupNo"
                 ,S1."MinSecGroupNo"
                 ,S2."GDRID1"
                 ,S2."GDRID2"
                 ,S2."GDRNUM"
                 ,S2."LGTSEQ"
           FROM (SELECT "GroupNo","SecGroupNo",MAX("TfFg") AS "MaxTfFg" FROM "ClBuildingUnique" GROUP BY "GroupNo","SecGroupNo"
                ) S0
           LEFT JOIN (SELECT "GroupNo"
                            ,MIN("SecGroupNo") AS "MinSecGroupNo"
                      FROM "ClBuildingUnique"
                      WHERE "TfFg" = 'Y'
                      GROUP BY "GroupNo"
                     ) S1 ON S1."GroupNo"    = S0."GroupNo"
           LEFT JOIN "ClBuildingUnique" S2 ON S2."GroupNo" = S0."GroupNo"
                                          AND S2."TfFg" = ' '
           WHERE S0."MaxTfFg" = ' '
             AND S1."MinSecGroupNo" IS NOT NULL
             AND S2."SecGroupNo" = 0
         ) SC1
    ON (    SC1."GroupNo" = T1."GroupNo"
        AND SC1."GDRID1"  = T1."GDRID1"
        AND SC1."GDRID2"  = T1."GDRID2"
        AND SC1."GDRNUM"  = T1."GDRNUM"
        AND SC1."LGTSEQ"  = T1."LGTSEQ"
    )
    WHEN MATCHED THEN UPDATE SET
    T1."SecGroupNo" = SC1."MinSecGroupNo"
    ;

    -- 更新ClBuildingUnique 同組內皆無Y者 透過GDRNUM2 去更新組號
    MERGE INTO "ClBuildingUnique" T1
    USING (SELECT S4."GroupNo"
                 ,S4."SecGroupNo"
                 ,S1."GDRID1"
                 ,S1."GDRID2"
                 ,S1."GDRNUM"
                 ,S1."LGTSEQ"
                 ,ROW_NUMBER() OVER (PARTITION BY S1."GDRID1"
                                                 ,S1."GDRID2"
                                                 ,S1."GDRNUM"
                                                 ,S1."LGTSEQ"
                                     ORDER BY S4."GroupNo"
                                             ,S4."SecGroupNo"
                                    ) AS "Seq"
           FROM (SELECT "GroupNo"
                       ,"SecGroupNo"
                 FROM "ClBuildingUnique"
                 GROUP BY "GroupNo"
                         ,"SecGroupNo"
                 HAVING SUM(CASE WHEN "TfFg" = 'Y' THEN 1 ELSE 0 END) = 0
                ) S0 -- 整組內皆無Y者的組別號碼
           -- 整組內皆無Y者的明細
           LEFT JOIN "ClBuildingUnique" S1 ON S1."GroupNo"    = S0."GroupNo"
                                          AND S1."SecGroupNo" = S0."SecGroupNo"
           -- 串工作檔取GDRNUM2
           LEFT JOIN "TmpLA$HGTP" S2 ON S2."GDRID1" = S1."GDRID1"
                                    AND S2."GDRID2" = S1."GDRID2"
                                    AND S2."GDRNUM" = S1."GDRNUM"
                                    AND S2."LGTSEQ" = S1."LGTSEQ"
           -- 串工作檔取GDRNUM2 主要的那筆
           LEFT JOIN "TmpLA$HGTP" S3 ON S3."GDRNUM2" = S2."GDRNUM2"
                                    AND S3."GDRMRK" = 1
           -- 串工作檔取主要那筆且有被轉入的最新群組號碼
           LEFT JOIN "ClBuildingUnique" S4 ON S4."GDRID1" = S3."GDRID1"
                                          AND S4."GDRID2" = S3."GDRID2"
                                          AND S4."GDRNUM" = S3."GDRNUM"
                                          AND S4."LGTSEQ" = S3."LGTSEQ"
                                          AND S4."TfFg" = 'Y'
           WHERE S2."GDRNUM2" > 0
         ) SC1
    ON (    SC1."GDRID1"  = T1."GDRID1"
        AND SC1."GDRID2"  = T1."GDRID2"
        AND SC1."GDRNUM"  = T1."GDRNUM"
        AND SC1."LGTSEQ"  = T1."LGTSEQ"
        AND SC1."Seq"     = 1
    )
    WHEN MATCHED THEN UPDATE SET
     T1."GroupNo" = SC1."GroupNo"
    ,T1."SecGroupNo" = SC1."SecGroupNo"
    ;

    -- 更新ClBuildingUnique 同組內皆無Y者 第二次 因為有GDRNUM2皆已有組別號碼 所以第二次更新的不會有沒組別號碼的 僅取同大群組內有Y的
    MERGE INTO "ClBuildingUnique" T1
    USING (SELECT S4."GroupNo"
                 ,S4."SecGroupNo"
                 ,S1."GDRID1"
                 ,S1."GDRID2"
                 ,S1."GDRNUM"
                 ,S1."LGTSEQ"
                 ,ROW_NUMBER() OVER (PARTITION BY S1."GDRID1"
                                                 ,S1."GDRID2"
                                                 ,S1."GDRNUM"
                                                 ,S1."LGTSEQ"
                                     ORDER BY S4."GroupNo"
                                             ,S4."SecGroupNo"
                                    ) AS "Seq"
           FROM (SELECT "GroupNo"
                       ,"SecGroupNo"
                 FROM "ClBuildingUnique"
                 GROUP BY "GroupNo"
                         ,"SecGroupNo"
                 HAVING SUM(CASE WHEN "TfFg" = 'Y' THEN 1 ELSE 0 END) = 0
                ) S0 -- 整組內皆無Y者的組別號碼
           -- 整組內皆無Y者的明細
           LEFT JOIN "ClBuildingUnique" S1 ON S1."GroupNo"    = S0."GroupNo"
                                          AND S1."SecGroupNo" = S0."SecGroupNo"
           -- 串工作檔取主要那筆且有被轉入的最新群組號碼
           LEFT JOIN "ClBuildingUnique" S4 ON S4."GroupNo" = S0."GroupNo"
                                          AND S4."TfFg" = 'Y'
           WHERE S4."GroupNo" > 0
         ) SC1
    ON (    SC1."GDRID1"  = T1."GDRID1"
        AND SC1."GDRID2"  = T1."GDRID2"
        AND SC1."GDRNUM"  = T1."GDRNUM"
        AND SC1."LGTSEQ"  = T1."LGTSEQ"
        AND SC1."Seq"     = 1
    )
    WHEN MATCHED THEN UPDATE SET
     T1."GroupNo" = SC1."GroupNo"
    ,T1."SecGroupNo" = SC1."SecGroupNo"
    ;

    -- 記錄程式結束時間
    JOB_END_TIME := SYSTIMESTAMP;

    commit;

    -- 例外處理
    Exception
    WHEN OTHERS THEN
    ERROR_MSG := SQLERRM || CHR(13) || CHR(10) || dbms_utility.format_error_backtrace;
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_ClBuildingUnique_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;


/
