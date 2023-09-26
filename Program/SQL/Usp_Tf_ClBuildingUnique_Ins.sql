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
    WITH GDTP AS (
      SELECT GDRID1
           , GDRID2
           , GDRNUM
           , ESTVAL
      FROM LA$GDTP
    )
    SELECT CASE
             WHEN S1."GDRNUM" = S4."GDRNUM" 
                  AND S1."LGTSEQ" = S4."LGTSEQ" 
             THEN 'Y' -- 2022-03-10 Wei
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
    LEFT JOIN (SELECT  HGTP."GroupNo"
                     , HGTP."SecGroupNo"
                     , ROW_NUMBER ()
                       OVER (
                           PARTITION BY HGTP."GroupNo"
                                      , HGTP."SecGroupNo"
                           ORDER BY CASE
                                      WHEN HGTP."LoanBalTotal" != 0
                                      THEN 0
                                    ELSE 1 END -- 2022-06-13 Wei 未結案者優先
                                  , HGTP."GRTSTS" DESC -- 2022-05-06 Wei 有設定擔保者優先
                                  , CASE
                                      WHEN HGTP."LGTSAM" != 0
                                      THEN 0
                                    ELSE 1 END -- 2022-10-31 Wei 原本已設定金額大小反序，現在僅比較設定金額有值及無值 
                                  , CASE
                                      WHEN GDTP.ESTVAL != 0
                                      THEN 0
                                    ELSE 1 END -- 2023-04-20 Wei 鑑價總值有值優先
                                  , HGTP."LMSAPN" DESC -- 2022-03-10 Wei
                                  , HGTP."GDRID1"
                                  , HGTP."GDRID2"
                                  , HGTP."GDRNUM" DESC -- 2022-03-10 Wei
                                  , HGTP."LGTSEQ"
                       ) AS "Seq"
                     , HGTP."GDRID1"
                     , HGTP."GDRID2"
                     , HGTP."GDRNUM"
                     , HGTP."LGTSEQ"
               FROM "TmpLA$HGTP" HGTP
               LEFT JOIN GDTP ON GDTP.GDRID1 = HGTP.GDRID1
                             AND GDTP.GDRID2 = HGTP.GDRID2
                             AND GDTP.GDRNUM = HGTP.GDRNUM
              ) S4 ON S4."GroupNo" = S1."GroupNo"
                  AND S4."SecGroupNo" = S1."SecGroupNo"
                  AND S4."Seq" = 1
    ;

    -- 記錄寫入筆數
    INS_CNT := INS_CNT + sql%rowcount;

    -- 更新ClBuildingUnique 同組內有兩個Y者 增加SecGroupNo
    MERGE INTO "ClBuildingUnique" T1
    USING (SELECT S0."GroupNo"
                 ,S0."SecGroupNo"
                 ,S1."MaxSecGroupNo"
                 ,ROW_NUMBER() OVER (PARTITION BY S0."GroupNo"
                                       ORDER BY S2."LMSAPN" DESC -- 2022-03-10 Wei
                                              , S2."GDRID1"
                                              , S2."GDRID2"
                                              , S2."GDRNUM" DESC -- 2022-03-10 Wei
                                              , S2."LGTSEQ"
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
                 ,MIN(NVL(S1."MinSecGroupNo",1)) AS "MinSecGroupNo"
                 ,S2."GDRID1"
                 ,S2."GDRID2"
                 ,S2."GDRNUM"
                 ,S2."LGTSEQ"
           FROM (SELECT "GroupNo","SecGroupNo",MAX("TfFg") AS "MaxTfFg" FROM "ClBuildingUnique" GROUP BY "GroupNo","SecGroupNo"
                ) S0
           LEFT JOIN (SELECT CBU."GroupNo"
                           , MIN(CBU."SecGroupNo") AS "MinSecGroupNo"
                      FROM "ClBuildingUnique" CBU
                      WHERE CBU."TfFg" = 'Y'
                      GROUP BY CBU."GroupNo"
                     ) S1 ON S1."GroupNo"    = S0."GroupNo"
           LEFT JOIN "ClBuildingUnique" S2 ON S2."GroupNo" = S0."GroupNo"
                                          AND S2."TfFg" = ' '
           WHERE S0."MaxTfFg" = ' '
             AND S1."MinSecGroupNo" IS NOT NULL
             AND S2."SecGroupNo" = 0
           GROUP BY S2."GroupNo"
                  , S2."GDRID1"
                  , S2."GDRID2"
                  , S2."GDRNUM"
                  , S2."LGTSEQ"
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
