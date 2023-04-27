CREATE OR REPLACE PROCEDURE "Usp_Tf_ClNoOld_Ins" 
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
    EXECUTE IMMEDIATE 'ALTER TABLE "ClNoOld" DISABLE PRIMARY KEY CASCADE'; 
    EXECUTE IMMEDIATE 'TRUNCATE TABLE "ClNoOld" DROP STORAGE'; 
    EXECUTE IMMEDIATE 'ALTER TABLE "ClNoOld" ENABLE PRIMARY KEY'; 
 
    -- 寫入資料 (舊擔保品編號檔) 
    INSERT INTO "ClNoOld" 
    SELECT S1."GDRID1"      AS "GDRID1"      -- 舊擔保品代號1 decimal(1, 0) default 0 not null, 
          ,S1."GDRID2"      AS "GDRID2"      -- 舊擔保品代號2 decimal(2, 0) default 0 not null, 
          ,S1."GDRNUM"      AS "GDRNUM"      -- 舊擔保品編號 decimal(7, 0) default 0 not null, 
          ,S1."LGTSEQ"      AS "LGTSEQ"      -- 舊擔保品序號 decimal(2, 0) default 0 not null, 
          ,0                AS "GDRNUM2"     -- 舊群組編號 decimal(10, 0) default 0 not null 
          ,ROW_NUMBER() OVER (PARTITION BY S1."GDRID1",S1."GDRID2",S1."GDRNUM" 
                              ORDER BY S1."CIF_SEQ" 
                                     , S1."LGTCIF" 
                                     , CASE 
                                         WHEN S1."APLLAM" != 0 
                                         THEN 0 
                                       ELSE 1 END -- 2022-06-13 Wei 未結案者優先 
                                     , S1."GRTSTS" DESC -- 2022-05-06 Wei 有設定擔保者優先 
                                     , CASE
                                         WHEN S1."LGTSAM" != 0
                                         THEN 0
                                       ELSE 1 END -- 2022-10-31 Wei 原本已設定金額大小反序，現在僅比較設定金額有值及無值 
                                     , S1."LGTSEQ") 
                            AS "Sequence" 
    FROM ( 
          /* 建物 */ 
          SELECT S1."GDRID1" 
                ,S1."GDRID2" 
                ,S1."GDRNUM" 
                ,S1."LGTSEQ" 
                ,S1."GDRNUM2" 
                ,S1."LGTSAM" 
                ,S1."GRTSTS" 
                ,APLP.APLLAM 
                ,S1."LGTCIF" 
                ,CASE 
                   WHEN NVL(CU1."CUSCIF",0) = NVL(CU2."CUSCIF",0) 
                        AND NVL(CU1."CUSCIF",0) != 0 
                        AND NVL(CU2."CUSCIF",0) != 0 
                   THEN 0 
                 ELSE 1 END AS "CIF_SEQ" 
          FROM "LA$HGTP" S1 
          LEFT JOIN "ClBuildingUnique" S2 ON S2."GDRID1" = S1."GDRID1" 
                                         AND S2."GDRID2" = S1."GDRID2" 
                                         AND S2."GDRNUM" = S1."GDRNUM" 
                                         AND S2."LGTSEQ" = S1."LGTSEQ" 
          LEFT JOIN LA$APLP APLP ON APLP."GDRID1" = S1."GDRID1" 
                                AND APLP."GDRID2" = S1."GDRID2" 
                                AND APLP."GDRNUM" = S1."GDRNUM" 
          LEFT JOIN "CU$CUSP" CU1 ON CU1."LMSACN" = APLP."LMSACN" -- 借戶 
          LEFT JOIN "CU$CUSP" CU2 ON CU2."CUSCIF" = S1."LGTCIF" -- 擔保品提供人 
          WHERE CASE 
                  WHEN S2."TfFg" IS NOT NULL 
                       AND S2."TfFg" = 'Y' 
                  THEN 1 -- 多筆擔保品且為主要擔保品時，轉入 
                  WHEN S2."TfFg" IS NOT NULL 
                  THEN 0 -- 多筆擔保品但非為主要擔保品時，不轉入 
                ELSE 0 END  -- 其餘皆不轉 
                = 1 
            AND S1."GDRID1" = '1' -- 只撈建物 
          UNION 
          /* 土地 */ 
          SELECT S1."GDRID1" 
                ,S1."GDRID2" 
                ,S1."GDRNUM" 
                ,S1."LGTSEQ" 
                ,S1."GDRNUM2" 
                ,S1."LGTSAM" 
                ,S1."GRTSTS" 
                ,APLP.APLLAM 
                ,S1."LGTCIF" 
                ,CASE 
                   WHEN NVL(CU1."CUSCIF",0) = NVL(CU2."CUSCIF",0) 
                        AND NVL(CU1."CUSCIF",0) != 0 
                        AND NVL(CU2."CUSCIF",0) != 0 
                   THEN 0 
                 ELSE 1 END AS "CIF_SEQ" 
          FROM "LA$LGTP" S1 
          LEFT JOIN "ClLandUnique" S2 ON S2."GDRID1" = S1."GDRID1" 
                                     AND S2."GDRID2" = S1."GDRID2" 
                                     AND S2."GDRNUM" = S1."GDRNUM" 
                                     AND S2."LGTSEQ" = S1."LGTSEQ" 
          LEFT JOIN LA$APLP APLP ON APLP."GDRID1" = S1."GDRID1" 
                                AND APLP."GDRID2" = S1."GDRID2" 
                                AND APLP."GDRNUM" = S1."GDRNUM" 
          LEFT JOIN "CU$CUSP" CU1 ON CU1."LMSACN" = APLP."LMSACN" -- 借戶 
          LEFT JOIN "CU$CUSP" CU2 ON CU2."CUSCIF" = S1."LGTCIF" -- 擔保品提供人 
          WHERE CASE 
                  WHEN S2."TfFg" IS NOT NULL 
                       AND S2."TfFg" = 'Y' 
                  THEN 1 -- 多筆擔保品且為主要擔保品時，轉入 
                  WHEN S2."TfFg" IS NOT NULL 
                  THEN 0 -- 多筆擔保品但非為主要擔保品時，不轉入 
                  WHEN S1."GDRNUM2" = 0 AND NVL(S1."LGTCTY",' ') <> ' ' 
                  THEN 1 -- 單筆擔保品且群組號碼為0時，轉入 
                  WHEN TO_CHAR(S1."GDRID1") || TO_CHAR(S1."GDRID2") || TO_CHAR(S1."GDRNUM") = TO_CHAR(S1."GDRNUM2")  
                       AND S1."GDRMRK" = '1' 
                       AND NVL(S1."LGTCTY",' ') <> ' ' 
                  THEN 1 -- 單筆擔保品且擔保品號碼等同群組號碼 且 縣市別不為NULL 時，轉入 
                  WHEN LPAD(S1.GDRID1,1,'0')
                       || LPAD(S1.GDRID2,2,'0')
                       || LPAD(S1.GDRNUM,7,'0')
                       || LPAD(S1.LGTSEQ,2,'0') 
                       IN ('101170176301', -- 新北市三重區 06020-000
                           '101102220301', -- 新北市板橋區 08215-000
                           '101102072801', -- 新北市三重區 00354-000
                           '101104701701', -- 屏東縣屏東市 04496-000
                           '101102923202', -- 桃園市桃園區 00252-000
                           '101102810401'  -- 台北市大同區 01087-000
                          )
                  THEN 1 -- 2023-03-20 Wei 修改 from 新壽-金靜 email 2023年3月20日 下午3:59 回覆確認補轉資料
                ELSE 0 END  -- 其餘皆不轉 
                = 1 
            -- AND NVL(CU."CUSCIF",0) > 0 -- 擔保品提供人必須在客戶主檔有建資料 
            -- AND NVL(CM."CustId",' ') <> ' ' -- 且該筆擔保品提供人資料有轉至新系統的客戶主檔 
            AND S1."GDRID1" = '2' -- 只撈土地 
          UNION 
          /* 動產 */ 
          SELECT "LN$CGTP"."GDRID1" 
                ,"LN$CGTP"."GDRID2" 
                ,"LN$CGTP"."GDRNUM" 
                ,0 AS "LGTSEQ" 
                ,"LN$CGTP"."GDRNUM2" 
            --     ,'LN$CGTP' AS "SourceTable" 
                ,"LN$CGTP"."CGT018" AS "LGTSAM" 
                ,"LN$CGTP"."GRTSTS" 
                ,APLP.APLLAM 
                ,0 AS "LGTCIF" 
                ,0 AS "CIF_SEQ" 
          FROM "LN$CGTP" 
          LEFT JOIN LA$APLP APLP ON APLP."GDRID1" = "LN$CGTP"."GDRID1" 
                                AND APLP."GDRID2" = "LN$CGTP"."GDRID2" 
                                AND APLP."GDRNUM" = "LN$CGTP"."GDRNUM" 
          WHERE ("LN$CGTP"."GDRNUM2" = 0 
                 OR TO_CHAR("LN$CGTP"."GDRID1") || TO_CHAR("LN$CGTP"."GDRID2") || TO_CHAR("LN$CGTP"."GDRNUM") 
                    = TO_CHAR("LN$CGTP"."GDRNUM2")) 
            AND "LN$CGTP"."GDRID1" = '9' -- 只撈動產 
          UNION 
          /* 股票 */ 
          SELECT "LA$SGTP"."GDRID1" 
                ,"LA$SGTP"."GDRID2" 
                ,"LA$SGTP"."GDRNUM" 
                ,0 AS "LGTSEQ" 
                ,"LA$SGTP"."GDRNUM2" 
                ,"LA$SGTP"."SGTTOT" AS "LGTSAM" 
            --     ,'LA$SGTP' AS "SourceTable" 
                ,"LA$SGTP"."GRTSTS" 
                ,APLP.APLLAM 
                ,0 AS "LGTCIF" 
                ,0 AS "CIF_SEQ" 
          FROM "LA$SGTP" 
          LEFT JOIN LA$APLP APLP ON APLP."GDRID1" = "LA$SGTP"."GDRID1" 
                                AND APLP."GDRID2" = "LA$SGTP"."GDRID2" 
                                AND APLP."GDRNUM" = "LA$SGTP"."GDRNUM" 
          WHERE ("LA$SGTP"."GDRNUM2" = 0 
                 OR TO_CHAR("LA$SGTP"."GDRID1") || TO_CHAR("LA$SGTP"."GDRID2") || TO_CHAR("LA$SGTP"."GDRNUM") 
                    = TO_CHAR("LA$SGTP"."GDRNUM2")) 
            AND "LA$SGTP"."GDRID1" IN ('3','4') -- 只撈股票 
          UNION 
          /* 其他 */ 
          SELECT "LA$BGTP"."GDRID1" 
                ,"LA$BGTP"."GDRID2" 
                ,"LA$BGTP"."GDRNUM" 
                ,0 AS "LGTSEQ" 
                ,"LA$BGTP"."GDRNUM2" 
            --     ,'LA$BGTP' AS "SourceTable" 
                ,"LA$BGTP"."BGTAMT" AS "LGTSAM" 
                ,"LA$BGTP"."GRTSTS" 
                ,APLP.APLLAM 
                ,0 AS "LGTCIF" 
                ,0 AS "CIF_SEQ" 
          FROM "LA$BGTP" 
          LEFT JOIN LA$APLP APLP ON APLP."GDRID1" = "LA$BGTP"."GDRID1" 
                                AND APLP."GDRID2" = "LA$BGTP"."GDRID2" 
                                AND APLP."GDRNUM" = "LA$BGTP"."GDRNUM" 
          WHERE ("LA$BGTP"."GDRNUM2" = 0 
                 OR TO_CHAR("LA$BGTP"."GDRID1") || TO_CHAR("LA$BGTP"."GDRID2") || TO_CHAR("LA$BGTP"."GDRNUM") 
                    = TO_CHAR("LA$BGTP"."GDRNUM2")) 
            AND "LA$BGTP"."GDRID1" = '5' -- 只撈其他 
    ) S1 
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
 
    -- 記錄程式結束時間 
    JOB_END_TIME := SYSTIMESTAMP; 
 
    commit; 
 
    -- 例外處理 
    Exception 
    WHEN OTHERS THEN 
    ERROR_MSG := SQLERRM || CHR(13) || CHR(10) || dbms_utility.format_error_backtrace; 
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_ClNoOld_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace); 
END;

/
