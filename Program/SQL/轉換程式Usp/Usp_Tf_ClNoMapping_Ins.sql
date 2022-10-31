--------------------------------------------------------
--  DDL for Procedure Usp_Tf_ClNoMapping_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE EDITIONABLE PROCEDURE "Usp_Tf_ClNoMapping_Ins" 
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
    EXECUTE IMMEDIATE 'ALTER TABLE "ClNoMapping" DISABLE PRIMARY KEY CASCADE';
    EXECUTE IMMEDIATE 'TRUNCATE TABLE "ClNoMapping" DROP STORAGE';
    EXECUTE IMMEDIATE 'ALTER TABLE "ClNoMapping" ENABLE PRIMARY KEY';

    -- 寫入資料 (擔保品編號新舊對照檔))
    INSERT INTO "ClNoMapping"
    SELECT S1."GDRID1"             AS "ClCode1"             -- 擔保品代號1 DECIMAL 1 
          ,S1."GDRID2"             AS "ClCode2"             -- 擔保品代號2 DECIMAL 2 
          ,CASE
             WHEN S1."Sequence" = 1 THEN S1."GDRNUM"
           ELSE S2."SeqNo" 
           + ROW_NUMBER()
             OVER (
               PARTITION BY S1."GDRID1"
                          , S1."GDRID2"
               ORDER BY S1."GDRID1"
                      , S1."GDRID2"
                      , S1."GDRNUM"
                      , S1."LGTSEQ"
                      , S1."Sequence"
             )
           END                     AS "ClNo"                -- 擔保品編號 DECIMAL 7 
          ,S1."GDRID1"             AS "GDRID1"              -- 舊擔保品代號1 decimal(1, 0) default 0 not null,
          ,S1."GDRID2"             AS "GDRID2"              -- 舊擔保品代號2 decimal(2, 0) default 0 not null,
          ,S1."GDRNUM"             AS "GDRNUM"              -- 舊擔保品編號 decimal(7, 0) default 0 not null,
          ,S1."LGTSEQ"             AS "LGTSEQ"              -- 舊擔保品序號 decimal(2, 0) default 0 not null,
          ,S1."GDRNUM2"            AS "GDRNUM2"             -- 舊群組編號 decimal(10, 0) default 0 not null
    FROM "ClNoOld" S1
    LEFT JOIN "CdGseq" S2 ON S2."GseqKind" = LPAD(S1."GDRID1",2,'0') || LPAD(S1."GDRID2",2,'0') 
                         AND S2."GseqType" = 'L2'
                         AND S2."GseqCode" = 0
                         AND S2."GseqDate" = 0
    ;

    -- 記錄寫入筆數
    INS_CNT := INS_CNT + sql%rowcount;

    MERGE INTO "CdGseq" T1
    USING (SELECT LPAD("GDRID1",2,'0') || LPAD("GDRID2",2,'0') AS "ClCode"
                  ,MAX("GDRNUM") AS "MAX_GDRNUM"
           FROM "ClNoOld"
           GROUP BY "GDRID1"
                   ,"GDRID2"
          ) S1
    ON (    S1."ClCode" = T1."GseqKind"
        AND T1."GseqType" = 'L2'
        AND T1."GseqCode" = 0
        AND T1."GseqDate" = 0
       )
    WHEN MATCHED THEN UPDATE SET
    T1."SeqNo" = CASE
                   WHEN S1."MAX_GDRNUM" > 9000000
                   THEN S1."MAX_GDRNUM"
                 ELSE 9000000 END
    ;

    /* 獨立產權車位資料 */
    MERGE INTO "ClNoMapping" T1
    USING ( SELECT TPM."GDRID1"
                 , TPM."GDRID2"
                 , TPM."GDRNUM"
                 , TPM."LGTSEQ"
                 , 1 AS "ClCode1"
                 , 5 AS "ClCode2"
                 , S2."SeqNo" 
                   + ROW_NUMBER() OVER (PARTITION BY TPM."GDRID1",TPM."GDRID2"
                                        ORDER BY TPM."GDRID1",TPM."GDRID2",TPM."GDRNUM",TPM."LGTSEQ"
                                       ) AS "ClNo"
            FROM "TmpParkingMapping" TPM
            LEFT JOIN "CdGseq" S2 ON S2."GseqKind" = '0105' 
                                 AND S2."GseqType" = 'L2'
                                 AND S2."GseqCode" = 0
                                 AND S2."GseqDate" = 0
            WHERE TPM."ConfirmResult" = 'Y'
    ) S1
    ON (    S1."GDRID1" = T1."GDRID1"
        AND S1."GDRID2" = T1."GDRID2"
        AND S1."GDRNUM" = T1."GDRNUM"
        AND S1."LGTSEQ" = T1."LGTSEQ"
        AND S1."ClNo" IS NOT NULL
       )
    WHEN MATCHED THEN UPDATE SET
      T1."ClCode1" = S1."ClCode1"
    , T1."ClCode2" = S1."ClCode2"
    , T1."ClNo" = S1."ClNo"
    ;

    MERGE INTO "CdGseq" T1
    USING (SELECT LPAD("GDRID1",2,'0') || LPAD("GDRID2",2,'0') AS "ClCode"
                  ,MAX("GDRNUM") AS "MAX_GDRNUM"
           FROM "ClNoOld"
           GROUP BY "GDRID1"
                   ,"GDRID2"
          ) S1
    ON (    S1."ClCode" = T1."GseqKind"
        AND T1."GseqType" = 'L2'
        AND T1."GseqCode" = 0
        AND T1."GseqDate" = 0
       )
    WHEN MATCHED THEN UPDATE SET
    T1."SeqNo" = CASE
                   WHEN S1."MAX_GDRNUM" > 9000000
                   THEN S1."MAX_GDRNUM"
                 ELSE 9000000 END
    ;

    -- 記錄程式結束時間
    JOB_END_TIME := SYSTIMESTAMP;

    commit;

    -- 例外處理
    Exception
    WHEN OTHERS THEN
    ERROR_MSG := SQLERRM || CHR(13) || CHR(10) || dbms_utility.format_error_backtrace;
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_ClNoMapping_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;




/
