CREATE OR REPLACE PROCEDURE "Usp_Tf_TmpLA$HGTP_Ins" 
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
      SELECT HG.CUSBRH
           , HG.GDRID1
           , HG.GDRID2
           , HG.GDRNUM
           , HG.LGTSEQ
           , HG.LGTCIF
           , HG.LGTADR
           , HG.HGTMHN
           , HG.HGTMHS
           , HG.HGTPSM
           , HG.HGTCAM
           , HG.LGTIID
           , HG.LGTUNT
           , HG.LGTIAM
           , HG.LGTSAM
           , HG.LGTSAT
           , HG.GRTSTS
           , HG.HGTSTR
           , HG.HGTCDT
           , HG.HGTFLR
           , HG.HGTROF
           , HG.SALNAM
           , HG.SALID1
           , HG.HGTCAP
           , HG.HGTGUS
           , HG.HGTAUS
           , HG.HGTFOR
           , HG.HGTCPE
           , HG.HGTADS
           , HG.HGTAD1
           , HG.HGTAD2
           , HG.HGTAD3
           , HG.HGTGTD
           , HG.BUYAMT
           , HG.BUYDAT
           , '0' AS GDRNUM2 -- 2023-04-27 Wei 修改 from SKL 佳怡 email SKL-會議記錄-首撥表相關-20230426 與文齡經理確認過先前討論唯一性轉換原則已經完全不需要額外判斷這2個欄位
           , '0' AS GDRMRK -- 2023-04-27 Wei 修改 from SKL 佳怡 email SKL-會議記錄-首撥表相關-20230426 與文齡經理確認過先前討論唯一性轉換原則已經完全不需要額外判斷這2個欄位
           , HG.HGTMHN2
           , HG.HGTCIP
           , HG.UPDATE_IDENT
      FROM "LA$HGTP" HG
    )
    , DistinctData AS (
      SELECT S5.CUSENT
            ,S3.LMSACN
            ,S3.LMSAPN
            ,NVL(S4."LoanBalTotal",0) AS "LoanBalTotal"
            ,S2.CUSBRH
           , S2.GDRID1
           , S2.GDRID2
           , S2.GDRNUM
           , S2.LGTSEQ
           , S2.LGTCIF
           , S2.LGTADR
           , S2.HGTMHN
           , S2.HGTMHS
           , S2.HGTPSM
           , S2.HGTCAM
           , S2.LGTIID
           , S2.LGTUNT
           , S2.LGTIAM
           , S2.LGTSAM
           , S2.LGTSAT
           , S2.GRTSTS
           , S2.HGTSTR
           , S2.HGTCDT
           , S2.HGTFLR
           , S2.HGTROF
           , S2.SALNAM
           , S2.SALID1
           , S2.HGTCAP
           , S2.HGTGUS
           , S2.HGTAUS
           , S2.HGTFOR
           , S2.HGTCPE
           , S2.HGTADS
           , S2.HGTAD1
           , S2.HGTAD2
           , S2.HGTAD3
           , S2.HGTGTD
           , S2.BUYAMT
           , S2.BUYDAT
           , S2.GDRNUM2
           , S2.GDRMRK
           , S2.HGTMHN2
           , S2.HGTCIP
           , S2.UPDATE_IDENT
            -- 2023-04-27 Wei 增加 from SKL 佳怡 email SKL-會議記錄-首撥表相關-20230426
           , CASE 
               WHEN S2.GRTSTS = 0 -- 已塗銷的不做唯一性直接轉入
               THEN LPAD(S2.GDRID1,1,'0') ||
                    LPAD(S2.GDRID2,2,'0') ||
                    LPAD(S2.GDRNUM,7,'0') ||
                    LPAD(S2.LGTSEQ,2,'0')
               WHEN NVL(S2.LGTADR,' ') = ' ' -- 地址為空 2023-05-04 Wei 增加: 新壽每筆資料都很重要 地址為空要照轉
               THEN LPAD(S2.GDRID1,1,'0') ||
                    LPAD(S2.GDRID2,2,'0') ||
                    LPAD(S2.GDRNUM,7,'0') ||
                    LPAD(S2.LGTSEQ,2,'0')
               WHEN NVL(S2.HGTMHN,0) = 0 -- 建號為0 2023-05-04 Wei 增加: 新壽每筆資料都很重要 建號為0要照轉
               THEN LPAD(S2.GDRID1,1,'0') ||
                    LPAD(S2.GDRID2,2,'0') ||
                    LPAD(S2.GDRNUM,7,'0') ||
                    LPAD(S2.LGTSEQ,2,'0')
               WHEN NVL(S3.LMSACN,0) = 0 -- 戶號為0 2023-05-04 Wei 增加: 新壽每筆資料都很重要 戶號為0要照轉
               THEN LPAD(S2.GDRID1,1,'0') ||
                    LPAD(S2.GDRID2,2,'0') ||
                    LPAD(S2.GDRNUM,7,'0') ||
                    LPAD(S2.LGTSEQ,2,'0')
             ELSE '0' END AS "ProcessUniqueFlag" -- 是否經過唯一性處理記號 0:要經過;else:不經過
      FROM HGData S2
      LEFT JOIN LA$APLP S3 ON S3.GDRID1 = S2.GDRID1
                          AND S3.GDRID2 = S2.GDRID2
                          AND S3.GDRNUM = S2.GDRNUM
      LEFT JOIN LBL S4 ON S4.LMSACN = S3.LMSACN
                      AND S4.LMSAPN = S3.LMSAPN
      LEFT JOIN CU$CUSP S5 on S5.LMSACN = S3.LMSACN
      WHERE S2.GDRID1 = 1
    )
    SELECT DENSE_RANK() 
           OVER (
            ----------------------------------------2023-05-25 Wei 修改 FROM SKL 佳怡 
            -- 因昨天再次針對擔保品唯一性規則進行討論，
            -- 為不違反擔保品唯一性的原則，
            -- 需要再請你修改轉換的規則如下：
            -- [縣市別]、[鄉鎮市區]、[建號]、[門牌號碼]、[提供人]相同者，只取1筆寫入新系統，
            -- 先前討論的[主建物(坪) ]、[公設(坪) ]、[車位(坪) ]、[附屬建物(坪) ]不需考慮，
            ----------------------------------------2023-05-25 Wei 修改 FROM SKL 佳怡 
            ORDER BY NVL(S1.LMSACN,0) -- 戶號
                   , NVL(S1.HGTAD1,' ') -- 縣市別
                   , NVL(S1.HGTAD2,' ') -- 鄉鎮市區
                   , NVL(S1.LGTADR,' ') -- 2023-05-08 Wei 增加 from SKL佳怡 2023-05-05 mail 增加唯一性判斷條件-門牌號碼
                   , NVL(S1.LGTCIF,' ') -- 2023-05-08 Wei 增加 from SKL佳怡 2023-05-05 mail 增加唯一性判斷條件-擔保品提供人
                  --  , NVL(S1.HGTMHS,' ') -- 2023-05-10 Wei 增加 from SKL佳怡 2023-05-09 會議 增加唯一性判斷條件-主建物(坪)
                  --  , NVL(S1.HGTPSM,' ') -- 2023-05-10 Wei 增加 from SKL佳怡 2023-05-09 會議 增加唯一性判斷條件-公設(坪)
                  --  , NVL(S1.HGTCAM,' ') -- 2023-05-10 Wei 增加 from SKL佳怡 2023-05-09 會議 增加唯一性判斷條件-車位(坪)
                  --  , NVL(S1.HGTADS,' ') -- 2023-05-10 Wei 增加 from SKL佳怡 2023-05-09 會議 增加唯一性判斷條件-附屬建物(坪)
                   , S1."ProcessUniqueFlag" -- 2023-04-27 Wei 修改 from SKL 佳怡 email SKL-會議記錄-首撥表相關-20230426
           ) AS "GroupNo"
          ,0 AS "SecGroupNo"
          ,S1.CUSENT
          ,S1.LMSACN
          ,S1.LMSAPN
          ,S1."LoanBalTotal"
          ,S1.CUSBRH
          ,S1.GDRID1
          ,S1.GDRID2
          ,S1.GDRNUM
          ,S1.LGTSEQ
          ,S1.LGTCIF
          ,S1.LGTADR
          ,S1.HGTMHN
          ,S1.HGTMHS
          ,S1.HGTPSM
          ,S1.HGTCAM
          ,S1.LGTIID
          ,S1.LGTUNT
          ,S1.LGTIAM
          ,S1.LGTSAM
          ,S1.LGTSAT
          ,S1.GRTSTS
          ,S1.HGTSTR
          ,S1.HGTCDT
          ,S1.HGTFLR
          ,S1.HGTROF
          ,S1.SALNAM
          ,S1.SALID1
          ,S1.HGTCAP
          ,S1.HGTGUS
          ,S1.HGTAUS
          ,S1.HGTFOR
          ,S1.HGTCPE
          ,S1.HGTADS
          ,S1.HGTAD1
          ,S1.HGTAD2
          ,S1.HGTAD3
          ,S1.HGTGTD
          ,S1.BUYAMT
          ,S1.BUYDAT
          ,S1.GDRNUM2
          ,S1.GDRMRK
          ,S1.HGTMHN2
          ,S1.HGTCIP
          ,S1.UPDATE_IDENT
    FROM DistinctData S1
    ;

    -- 記錄寫入筆數
    INS_CNT := INS_CNT + sql%rowcount;
    
    /* 更新組別子號 */
    MERGE INTO "TmpLA$HGTP" T1
    USING (SELECT S1."GroupNo"
                 ,DENSE_RANK()
                  OVER (
                    PARTITION BY S1."GroupNo"
                    ORDER BY NVL(S1.hgtmhn,0)
                  )  AS "SecGroupNo"
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
