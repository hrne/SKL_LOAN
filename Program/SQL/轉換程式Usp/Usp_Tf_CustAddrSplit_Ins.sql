--------------------------------------------------------
--  DDL for Procedure Usp_Tf_CustAddrSplit_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_Tf_CustAddrSplit_Ins" 
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
    EXECUTE IMMEDIATE 'ALTER TABLE "RegAddrSplit" DISABLE PRIMARY KEY CASCADE';
    EXECUTE IMMEDIATE 'TRUNCATE TABLE "RegAddrSplit" DROP STORAGE';
    EXECUTE IMMEDIATE 'ALTER TABLE "RegAddrSplit" ENABLE PRIMARY KEY';

    -- 寫入資料
    INSERT INTO "RegAddrSplit"
    SELECT S1."CustId"           AS "CustId"       -- 客戶統編 VARCHAR2 10
          ,S1."RegRoad"          AS "OriginalRoad" -- 原檔路名 NVARCHAR2 40 
          ,S2."CityCode"         AS "CityCode"     -- 比對縣市代碼 VARCHAR2 2 
          ,S2."CityItem"         AS "CityItem"     -- 比對縣市名稱 NVARCHAR2 10
          ,S3."AreaCode"         AS "AreaCode"     -- 比對鄉鎮市區代碼 VARCHAR2 3 
          ,S3."AreaItem"         AS "AreaItem"     -- 比對鄉鎮區名稱 NVARCHAR2 12 
          ,CASE
             WHEN NVL(S2."CityCode",' ') <> ' ' AND NVL(S3."AreaCode",' ') <> ' '
             THEN SUBSTR(S1."RegRoad",3+LENGTH(S3."AreaItem")+1)
             WHEN NVL(S2."CityCode",' ') <> ' '
             THEN SUBSTR(S1."RegRoad",4)
           ELSE S1."RegRoad" END AS "SplitRoad"    -- 切割後路名 NVARCHAR2 40 
          ,u''                   AS "SplitOther"   -- 切割後其餘資料 NVARCHAR2 40 
          ,CASE
             WHEN NVL(S2."CityCode",' ') <> ' ' AND NVL(S3."AreaCode",' ') <> ' '
             THEN 'Y'
             WHEN NVL(S2."CityCode",' ') <> ' '
             THEN 'Y'
           ELSE 'N' END          AS "SplitFg"      -- 切割是否成功 VARCHAR2 1
          ,u''                   AS "Section"      -- 段 NVARCHAR2 5 
          ,u''                   AS "Alley"        -- 巷 NVARCHAR2 5 
          ,u''                   AS "Lane"         -- 弄 NVARCHAR2 5 
          ,u''                   AS "Num"          -- 號 NVARCHAR2 5 
          ,u''                   AS "NumDash"      -- 號之 NVARCHAR2 5 
          ,u''                   AS "Floor"        -- 樓 NVARCHAR2 5 
          ,u''                   AS "FloorDash"    -- 樓之 NVARCHAR2 5 
    FROM "CustMain" S1
    LEFT JOIN "CdCity" S2 ON S2."CityItem" = SUBSTR(S1."RegRoad",0,3)
    LEFT JOIN "CdArea" S3 ON S3."CityCode" = S2."CityCode"
                         AND S3."AreaItem" = SUBSTR(S1."RegRoad",4,LENGTH(S3."AreaItem"))
                         AND NVL(S2."CityCode",' ') <> ' '
    ;

    -- 記錄寫入筆數
    INS_CNT := INS_CNT + sql%rowcount;

    -- 切割'路'
    UPDATE "RegAddrSplit"
    SET "SplitRoad" = CASE
                        WHEN INSTR("SplitRoad",'(路)') > 0
                        THEN "Fn_Split"("SplitRoad",'(路)',1) || '路'
                        WHEN INSTR("SplitRoad",'路') > 0
                        THEN "Fn_Split"("SplitRoad",'路',1) || '路'
                        WHEN INSTR("SplitRoad",'街') > 0
                        THEN "Fn_Split"("SplitRoad",'街',1) || '街'
                        WHEN INSTR("SplitRoad",'大道') > 0
                        THEN "Fn_Split"("SplitRoad",'大道',1) || '大道'
                      ELSE "SplitRoad" END 
       ,"SplitOther" = CASE
                         WHEN INSTR("SplitRoad",'(路)') > 0
                         THEN "Fn_Split"("SplitRoad",'(路)',2)
                         WHEN INSTR("SplitRoad",'路') > 0
                         THEN "Fn_Split"("SplitRoad",'路',2)
                         WHEN INSTR("SplitRoad",'街') > 0
                         THEN "Fn_Split"("SplitRoad",'街',2)
                         WHEN INSTR("SplitRoad",'大道') > 0
                         THEN "Fn_Split"("SplitRoad",'大道',2)
                       ELSE u'' END
       ,"SplitFg" = CASE
                      WHEN INSTR("SplitRoad",'(路)') > 0
                      THEN 'Y'
                      WHEN INSTR("SplitRoad",'路') > 0
                      THEN 'Y'
                      WHEN INSTR("SplitRoad",'街') > 0
                      THEN 'Y'
                      WHEN INSTR("SplitRoad",'大道') > 0
                      THEN 'Y'
                    ELSE 'N' END 
    WHERE "SplitFg" = 'Y';

    -- 切割'段'
    UPDATE "RegAddrSplit"
    SET "Section" = CASE
                      WHEN INSTR("SplitOther",'段') > 0
                           AND LENGTH("Fn_Split"("SplitOther",'段',1)) <= 5
                      THEN "Fn_Split"("SplitOther",'段',1)
                    ELSE u'' END 
       ,"SplitOther" = CASE
                         WHEN INSTR("SplitOther",'段') > 0
                              AND LENGTH("Fn_Split"("SplitOther",'段',1)) <= 5
                         THEN "Fn_Split"("SplitOther",'段',2)
                       ELSE "SplitOther" END
    WHERE "SplitFg" = 'Y';

    -- 切割'巷'
    UPDATE "RegAddrSplit"
    SET "Alley" = CASE
                    WHEN INSTR("SplitOther",'巷') > 0 
                         AND LENGTH("Fn_Split"("SplitOther",'巷',1)) <= 5
                    THEN "Fn_Split"("SplitOther",'巷',1)
                  ELSE u'' END
       ,"SplitOther" = CASE
                         WHEN INSTR("SplitOther",'巷') > 0
                              AND LENGTH("Fn_Split"("SplitOther",'巷',1)) <= 5
                         THEN "Fn_Split"("SplitOther",'巷',2)
                       ELSE "SplitOther" END
    WHERE "SplitFg" = 'Y';

    -- 切割'弄'
    UPDATE "RegAddrSplit"
    SET "Lane" = CASE
                   WHEN INSTR("SplitOther",'弄') > 0
                        AND LENGTH("Fn_Split"("SplitOther",'弄',1)) <= 5
                   THEN "Fn_Split"("SplitOther",'弄',1)
                 ELSE u'' END 
       ,"SplitOther" = CASE
                         WHEN INSTR("SplitOther",'弄') > 0
                              AND LENGTH("Fn_Split"("SplitOther",'弄',1)) <= 5
                         THEN "Fn_Split"("SplitOther",'弄',2)
                       ELSE "SplitOther" END
    WHERE "SplitFg" = 'Y';

    -- 切割'號'、'號之'
    UPDATE "RegAddrSplit"
    SET "Num" = CASE
                  WHEN INSTR("SplitOther",'號之') > 0
                       AND LENGTH("Fn_Split"("SplitOther",'號之',1)) <= 5
                       AND LENGTH("Fn_Split"("SplitOther",'號之',2)) <= 5
                       AND INSTR("SplitOther",'樓') <= 0
                  THEN "Fn_Split"("SplitOther",'號之',1)
                  WHEN INSTR("SplitOther",'號之') > 0
                  THEN u''
                  WHEN INSTR("SplitOther",'號') > 0 
                       AND INSTR("SplitOther",'之') > 0 
                       AND INSTR("SplitOther",'之') < INSTR("SplitOther",'號')
                       AND LENGTH("Fn_Split"("SplitOther",'之',1)) <= 5
                       AND LENGTH("Fn_Split"("Fn_Split"("SplitOther",'之',2),'號',1)) <= 5
                  THEN "Fn_Split"("SplitOther",'之',1)
                  WHEN INSTR("SplitOther",'號') > 0 
                       AND INSTR("SplitOther",'之') > 0 
                       AND INSTR("SplitOther",'之') < INSTR("SplitOther",'號')
                  THEN u''
                  WHEN INSTR("SplitOther",'號') > 0 
                       AND INSTR("SplitOther",'-') > 0 
                       AND INSTR("SplitOther",'-') < INSTR("SplitOther",'號')
                       AND LENGTH("Fn_Split"("SplitOther",'-',1)) <= 5
                       AND LENGTH("Fn_Split"("Fn_Split"("SplitOther",'-',2),'號',1)) <= 5
                  THEN "Fn_Split"("SplitOther",'-',1)
                  WHEN INSTR("SplitOther",'號') > 0 
                       AND INSTR("SplitOther",'-') > 0 
                       AND INSTR("SplitOther",'-') < INSTR("SplitOther",'號')
                  THEN u''
                  WHEN INSTR("SplitOther",'號') > 0 
                       AND LENGTH("Fn_Split"("SplitOther",'號',1)) <= 5
                  THEN "Fn_Split"("SplitOther",'號',1)
                  WHEN INSTR("SplitOther",'號') > 0 
                  THEN u''
                ELSE u'' END
       ,"NumDash" = CASE
                      WHEN INSTR("SplitOther",'號之') > 0
                           AND LENGTH("Fn_Split"("SplitOther",'號之',1)) <= 5
                           AND LENGTH("Fn_Split"("SplitOther",'號之',2)) <= 5
                           AND INSTR("SplitOther",'樓') <= 0
                      THEN "Fn_Split"("SplitOther",'號之',2)
                      WHEN INSTR("SplitOther",'號之') > 0 
                      THEN u''
                      WHEN INSTR("SplitOther",'號-') > 0
                           AND LENGTH("Fn_Split"("SplitOther",'號-',1)) <= 5
                           AND LENGTH("Fn_Split"("SplitOther",'號-',2)) <= 5
                           AND INSTR("SplitOther",'樓') <= 0
                      THEN "Fn_Split"("SplitOther",'號-',2)
                      WHEN INSTR("SplitOther",'號-') > 0 
                      THEN u''
                      WHEN INSTR("SplitOther",'號') > 0 
                           AND INSTR("SplitOther",'之') > 0 
                           AND INSTR("SplitOther",'之') < INSTR("SplitOther",'號')
                           AND LENGTH("Fn_Split"("SplitOther",'之',1)) <= 5
                           AND LENGTH("Fn_Split"("Fn_Split"("SplitOther",'之',2),'號',1)) <= 5
                      THEN "Fn_Split"("Fn_Split"("SplitOther",'之',2),'號',1)
                      WHEN INSTR("SplitOther",'號') > 0 
                           AND INSTR("SplitOther",'-') > 0 
                           AND INSTR("SplitOther",'-') < INSTR("SplitOther",'號')
                           AND LENGTH("Fn_Split"("SplitOther",'-',1)) <= 5
                           AND LENGTH("Fn_Split"("Fn_Split"("SplitOther",'-',2),'號',1)) <= 5
                      THEN "Fn_Split"("Fn_Split"("SplitOther",'-',2),'號',1)
                    ELSE u'' END
       ,"SplitOther" = CASE 
                         WHEN INSTR("SplitOther",'號之') > 0
                              AND LENGTH("Fn_Split"("SplitOther",'號之',1)) <= 5
                              AND LENGTH("Fn_Split"("SplitOther",'號之',2)) <= 5
                              AND INSTR("SplitOther",'樓') <= 0
                         THEN u''
                         WHEN INSTR("SplitOther",'號之') > 0 
                         THEN "SplitOther"
                         WHEN INSTR("SplitOther",'號-') > 0
                              AND LENGTH("Fn_Split"("SplitOther",'號-',1)) <= 5
                              AND LENGTH("Fn_Split"("SplitOther",'號-',2)) <= 5
                              AND INSTR("SplitOther",'樓') <= 0
                         THEN u''
                         WHEN INSTR("SplitOther",'號-') > 0 
                         THEN "SplitOther"
                         WHEN INSTR("SplitOther",'號') > 0 
                              AND INSTR("SplitOther",'之') > 0
                              AND INSTR("SplitOther",'之') < INSTR("SplitOther",'號')
                              AND LENGTH("Fn_Split"("SplitOther",'之',1)) <= 5
                              AND LENGTH("Fn_Split"("Fn_Split"("SplitOther",'之',2),'號',1)) <= 5
                         THEN "Fn_Split"("SplitOther",'號',2)
                         WHEN INSTR("SplitOther",'號') > 0 
                              AND INSTR("SplitOther",'-') > 0
                              AND INSTR("SplitOther",'-') < INSTR("SplitOther",'號')
                              AND LENGTH("Fn_Split"("SplitOther",'-',1)) <= 5
                              AND LENGTH("Fn_Split"("Fn_Split"("SplitOther",'-',2),'號',1)) <= 5
                         THEN "Fn_Split"("SplitOther",'號',2)
                         WHEN INSTR("SplitOther",'號') > 0 
                              AND LENGTH("Fn_Split"("SplitOther",'號',1)) <= 5
                         THEN "Fn_Split"("SplitOther",'號',2)
                       ELSE "SplitOther" END
    WHERE "SplitFg" = 'Y';

    -- 切割'樓'、'樓之'
    UPDATE "RegAddrSplit"
    SET "Floor" = CASE
                    WHEN INSTR("SplitOther",'樓') > 0
                         AND LENGTH("Fn_Split"("SplitOther",'樓',1)) <= 5
                    THEN "Fn_Split"("SplitOther",'樓',1)
                    WHEN INSTR("SplitOther",'F') > 0
                         AND LENGTH("Fn_Split"("SplitOther",'F',1)) <= 5
                    THEN "Fn_Split"("SplitOther",'F',1)
                  ELSE u'' END
       ,"FloorDash" = CASE
                        WHEN INSTR("SplitOther",'樓之') > 0 
                             AND LENGTH("Fn_Split"("SplitOther",'樓之',2)) <= 5
                        THEN "Fn_Split"("SplitOther",'樓之',2)
                        WHEN INSTR("SplitOther",'樓之') > 0 
                        THEN u''
                        WHEN INSTR("SplitOther",'樓-') > 0 
                             AND LENGTH("Fn_Split"("SplitOther",'樓-',2)) <= 5
                        THEN "Fn_Split"("SplitOther",'樓-',2)
                        WHEN INSTR("SplitOther",'樓-') > 0 
                        THEN u''
                        WHEN INSTR("SplitOther",'F之') > 0 
                             AND LENGTH("Fn_Split"("SplitOther",'F之',2)) <= 5
                        THEN "Fn_Split"("SplitOther",'F之',2)
                        WHEN INSTR("SplitOther",'F之') > 0 
                        THEN u''
                        WHEN INSTR("SplitOther",'F-') > 0 
                             AND LENGTH("Fn_Split"("SplitOther",'F-',2)) <= 5
                        THEN "Fn_Split"("SplitOther",'F-',2)
                        WHEN INSTR("SplitOther",'F-') > 0 
                        THEN u''
                      ELSE u'' END
       ,"SplitOther" = CASE
                         WHEN INSTR("SplitOther",'樓之') > 0 
                              AND LENGTH("Fn_Split"("SplitOther",'樓之',1)) <= 5
                              AND LENGTH("Fn_Split"("SplitOther",'樓之',2)) <= 5
                         THEN u''
                         WHEN INSTR("SplitOther",'樓-') > 0 
                              AND LENGTH("Fn_Split"("SplitOther",'樓-',1)) <= 5
                              AND LENGTH("Fn_Split"("SplitOther",'樓-',2)) <= 5
                         THEN u''
                         WHEN INSTR("SplitOther",'樓') > 0
                              AND LENGTH("Fn_Split"("SplitOther",'樓',1)) <= 5
                              AND NVL(LENGTH("Fn_Split"("SplitOther",'樓',2)),0) <= 0
                         THEN u''
                         WHEN INSTR("SplitOther",'F之') > 0
                              AND LENGTH("Fn_Split"("SplitOther",'F之',1)) <= 5
                              AND LENGTH("Fn_Split"("SplitOther",'F之',2)) <= 5
                         THEN u''
                         WHEN INSTR("SplitOther",'F-') > 0
                              AND LENGTH("Fn_Split"("SplitOther",'F-',1)) <= 5
                              AND LENGTH("Fn_Split"("SplitOther",'F-',2)) <= 5
                         THEN u''
                         WHEN INSTR("SplitOther",'F') > 0
                              AND LENGTH("Fn_Split"("SplitOther",'F',1)) <= 5
                              AND NVL(LENGTH("Fn_Split"("SplitOther",'F',2)),0) <= 0
                         THEN u''
                       ELSE "SplitOther" END
    WHERE "SplitFg" = 'Y';

    -- 修改"切割是否成功"記號
    UPDATE "RegAddrSplit"
    SET "SplitFg" = CASE
                      WHEN LENGTH("SplitOther") > 0
                      THEN 'N'
                    ELSE 'Y' END 
    WHERE "SplitFg" = 'Y';

    MERGE INTO "CustMain" C1
    USING (
         SELECT "CustId"       -- 客戶統編 VARCHAR2 10
              , "OriginalRoad" -- 原檔路名 NVARCHAR2 40 
              , "CityCode"     -- 比對縣市代碼 VARCHAR2 2 
              , "CityItem"     -- 比對縣市名稱 NVARCHAR2 10
              , "AreaCode"     -- 比對鄉鎮市區代碼 VARCHAR2 3 
              , "AreaItem"     -- 比對鄉鎮區名稱 NVARCHAR2 12 
              , "SplitRoad"    -- 切割後路名 NVARCHAR2 40 
              , "SplitFg"      -- 切割是否成功 VARCHAR2 1
              , "Section"      -- 段 VARCHAR2 5 
              , "Alley"        -- 巷 VARCHAR2 5 
              , "Lane"         -- 弄 VARCHAR2 5 
              , "Num"          -- 號 VARCHAR2 5 
              , "NumDash"      -- 號之 VARCHAR2 5 
              , "Floor"        -- 樓 VARCHAR2 5 
              , "FloorDash"    -- 樓之 VARCHAR2 5 
         FROM "RegAddrSplit"
    ) C2
    ON (
         C1."CustId" = C2."CustId"
     --     AND C2."SplitFg" = 'Y'
    )
    WHEN MATCHED THEN UPDATE SET
     C1."RegCityCode"  = C2."CityCode"   -- 縣市代碼 VARCHAR2 2 
    ,C1."RegAreaCode"  = C2."AreaCode"   -- 鄉鎮市區代碼 VARCHAR2 3 
    ,C1."RegRoad"      = CASE
                           WHEN C2."SplitFg" = 'Y'
                           THEN C2."SplitRoad"  -- 路名 NVARCHAR2 40 
                         ELSE C1."RegRoad" END
    ,C1."RegSection"   = CASE
                           WHEN C2."SplitFg" = 'Y'
                           THEN C2."Section"    -- 段 VARCHAR2 5 
                         ELSE u'' END
    ,C1."RegAlley"     = CASE
                           WHEN C2."SplitFg" = 'Y'
                           THEN C2."Alley"      -- 巷 VARCHAR2 5 
                         ELSE u'' END
    ,C1."RegLane"      = CASE
                           WHEN C2."SplitFg" = 'Y'
                           THEN C2."Lane"       -- 弄 VARCHAR2 5 
                         ELSE u'' END
    ,C1."RegNum"       = CASE
                           WHEN C2."SplitFg" = 'Y'
                           THEN C2."Num"        -- 號 VARCHAR2 5 
                         ELSE u'' END
    ,C1."RegNumDash"   = CASE
                           WHEN C2."SplitFg" = 'Y'
                           THEN C2."NumDash"    -- 號之 VARCHAR2 5 
                         ELSE u'' END
    ,C1."RegFloor"     = CASE
                           WHEN C2."SplitFg" = 'Y'
                           THEN C2."Floor"      -- 樓 VARCHAR2 5 
                         ELSE u'' END
    ,C1."RegFloorDash" = CASE
                           WHEN C2."SplitFg" = 'Y'
                           THEN C2."FloorDash"  -- 樓之 VARCHAR2 5 
                         ELSE u'' END
    ;

    -- 刪除舊資料
    EXECUTE IMMEDIATE 'ALTER TABLE "CurrAddrSplit" DISABLE PRIMARY KEY CASCADE';
    EXECUTE IMMEDIATE 'TRUNCATE TABLE "CurrAddrSplit" DROP STORAGE';
    EXECUTE IMMEDIATE 'ALTER TABLE "CurrAddrSplit" ENABLE PRIMARY KEY';

    -- 寫入資料
    INSERT INTO "CurrAddrSplit"
    SELECT S1."CustId"           AS "CustId"       -- 客戶統編 VARCHAR2 10
          ,S1."CurrRoad"          AS "OriginalRoad" -- 原檔路名 NVARCHAR2 40 
          ,S2."CityCode"         AS "CityCode"     -- 比對縣市代碼 VARCHAR2 2 
          ,S2."CityItem"         AS "CityItem"     -- 比對縣市名稱 NVARCHAR2 10
          ,S3."AreaCode"         AS "AreaCode"     -- 比對鄉鎮市區代碼 VARCHAR2 3 
          ,S3."AreaItem"         AS "AreaItem"     -- 比對鄉鎮區名稱 NVARCHAR2 12 
          ,CASE
             WHEN NVL(S2."CityCode",' ') <> ' ' AND NVL(S3."AreaCode",' ') <> ' '
             THEN SUBSTR(S1."CurrRoad",3+LENGTH(S3."AreaItem")+1)
             WHEN NVL(S2."CityCode",' ') <> ' '
             THEN SUBSTR(S1."CurrRoad",4)
           ELSE S1."CurrRoad" END AS "SplitRoad"    -- 切割後路名 NVARCHAR2 40 
          ,u''                   AS "SplitOther"   -- 切割後其餘資料 NVARCHAR2 40 
          ,CASE
             WHEN NVL(S2."CityCode",' ') <> ' ' AND NVL(S3."AreaCode",' ') <> ' '
             THEN 'Y'
             WHEN NVL(S2."CityCode",' ') <> ' '
             THEN 'Y'
           ELSE 'N' END          AS "SplitFg"      -- 切割是否成功 VARCHAR2 1
          ,u''                   AS "Section"      -- 段 NVARCHAR2 5 
          ,u''                   AS "Alley"        -- 巷 NVARCHAR2 5 
          ,u''                   AS "Lane"         -- 弄 NVARCHAR2 5 
          ,u''                   AS "Num"          -- 號 NVARCHAR2 5 
          ,u''                   AS "NumDash"      -- 號之 NVARCHAR2 5 
          ,u''                   AS "Floor"        -- 樓 NVARCHAR2 5 
          ,u''                   AS "FloorDash"    -- 樓之 NVARCHAR2 5 
    FROM "CustMain" S1
    LEFT JOIN "CdCity" S2 ON S2."CityItem" = SUBSTR(S1."CurrRoad",0,3)
    LEFT JOIN "CdArea" S3 ON S3."CityCode" = S2."CityCode"
                         AND S3."AreaItem" = SUBSTR(S1."CurrRoad",4,LENGTH(S3."AreaItem"))
                         AND NVL(S2."CityCode",' ') <> ' '
    ;

    -- 記錄寫入筆數
    INS_CNT := INS_CNT + sql%rowcount;

    -- 切割'路'
    UPDATE "CurrAddrSplit"
    SET "SplitRoad" = CASE
                        WHEN INSTR("SplitRoad",'(路)') > 0
                        THEN "Fn_Split"("SplitRoad",'(路)',1) || '路'
                        WHEN INSTR("SplitRoad",'路') > 0
                        THEN "Fn_Split"("SplitRoad",'路',1) || '路'
                        WHEN INSTR("SplitRoad",'街') > 0
                        THEN "Fn_Split"("SplitRoad",'街',1) || '街'
                        WHEN INSTR("SplitRoad",'大道') > 0
                        THEN "Fn_Split"("SplitRoad",'大道',1) || '大道'
                      ELSE "SplitRoad" END 
       ,"SplitOther" = CASE
                         WHEN INSTR("SplitRoad",'(路)') > 0
                         THEN "Fn_Split"("SplitRoad",'(路)',2)
                         WHEN INSTR("SplitRoad",'路') > 0
                         THEN "Fn_Split"("SplitRoad",'路',2)
                         WHEN INSTR("SplitRoad",'街') > 0
                         THEN "Fn_Split"("SplitRoad",'街',2)
                         WHEN INSTR("SplitRoad",'大道') > 0
                         THEN "Fn_Split"("SplitRoad",'大道',2)
                       ELSE u'' END
       ,"SplitFg" = CASE
                      WHEN INSTR("SplitRoad",'(路)') > 0
                      THEN 'Y'
                      WHEN INSTR("SplitRoad",'路') > 0
                      THEN 'Y'
                      WHEN INSTR("SplitRoad",'街') > 0
                      THEN 'Y'
                      WHEN INSTR("SplitRoad",'大道') > 0
                      THEN 'Y'
                    ELSE 'N' END 
    WHERE "SplitFg" = 'Y';

    -- 切割'段'
    UPDATE "CurrAddrSplit"
    SET "Section" = CASE
                      WHEN INSTR("SplitOther",'段') > 0
                           AND LENGTH("Fn_Split"("SplitOther",'段',1)) <= 5
                      THEN "Fn_Split"("SplitOther",'段',1)
                    ELSE u'' END 
       ,"SplitOther" = CASE
                         WHEN INSTR("SplitOther",'段') > 0
                              AND LENGTH("Fn_Split"("SplitOther",'段',1)) <= 5
                         THEN "Fn_Split"("SplitOther",'段',2)
                       ELSE "SplitOther" END
    WHERE "SplitFg" = 'Y';

    -- 切割'巷'
    UPDATE "CurrAddrSplit"
    SET "Alley" = CASE
                    WHEN INSTR("SplitOther",'巷') > 0 
                         AND LENGTH("Fn_Split"("SplitOther",'巷',1)) <= 5
                    THEN "Fn_Split"("SplitOther",'巷',1)
                  ELSE u'' END
       ,"SplitOther" = CASE
                         WHEN INSTR("SplitOther",'巷') > 0
                              AND LENGTH("Fn_Split"("SplitOther",'巷',1)) <= 5
                         THEN "Fn_Split"("SplitOther",'巷',2)
                       ELSE "SplitOther" END
    WHERE "SplitFg" = 'Y';

    -- 切割'弄'
    UPDATE "CurrAddrSplit"
    SET "Lane" = CASE
                   WHEN INSTR("SplitOther",'弄') > 0
                        AND LENGTH("Fn_Split"("SplitOther",'弄',1)) <= 5
                   THEN "Fn_Split"("SplitOther",'弄',1)
                 ELSE u'' END 
       ,"SplitOther" = CASE
                         WHEN INSTR("SplitOther",'弄') > 0
                              AND LENGTH("Fn_Split"("SplitOther",'弄',1)) <= 5
                         THEN "Fn_Split"("SplitOther",'弄',2)
                       ELSE "SplitOther" END
    WHERE "SplitFg" = 'Y';

    -- 切割'號'、'號之'
    UPDATE "CurrAddrSplit"
    SET "Num" = CASE
                  WHEN INSTR("SplitOther",'號之') > 0
                       AND LENGTH("Fn_Split"("SplitOther",'號之',1)) <= 5
                       AND LENGTH("Fn_Split"("SplitOther",'號之',2)) <= 5
                       AND INSTR("SplitOther",'樓') <= 0
                  THEN "Fn_Split"("SplitOther",'號之',1)
                  WHEN INSTR("SplitOther",'號之') > 0
                  THEN u''
                  WHEN INSTR("SplitOther",'號') > 0 
                       AND INSTR("SplitOther",'之') > 0 
                       AND INSTR("SplitOther",'之') < INSTR("SplitOther",'號')
                       AND LENGTH("Fn_Split"("SplitOther",'之',1)) <= 5
                       AND LENGTH("Fn_Split"("Fn_Split"("SplitOther",'之',2),'號',1)) <= 5
                  THEN "Fn_Split"("SplitOther",'之',1)
                  WHEN INSTR("SplitOther",'號') > 0 
                       AND INSTR("SplitOther",'之') > 0 
                       AND INSTR("SplitOther",'之') < INSTR("SplitOther",'號')
                  THEN u''
                  WHEN INSTR("SplitOther",'號') > 0 
                       AND INSTR("SplitOther",'-') > 0 
                       AND INSTR("SplitOther",'-') < INSTR("SplitOther",'號')
                       AND LENGTH("Fn_Split"("SplitOther",'-',1)) <= 5
                       AND LENGTH("Fn_Split"("Fn_Split"("SplitOther",'-',2),'號',1)) <= 5
                  THEN "Fn_Split"("SplitOther",'-',1)
                  WHEN INSTR("SplitOther",'號') > 0 
                       AND INSTR("SplitOther",'-') > 0 
                       AND INSTR("SplitOther",'-') < INSTR("SplitOther",'號')
                  THEN u''
                  WHEN INSTR("SplitOther",'號') > 0 
                       AND LENGTH("Fn_Split"("SplitOther",'號',1)) <= 5
                  THEN "Fn_Split"("SplitOther",'號',1)
                  WHEN INSTR("SplitOther",'號') > 0 
                  THEN u''
                ELSE u'' END
       ,"NumDash" = CASE
                      WHEN INSTR("SplitOther",'號之') > 0
                           AND LENGTH("Fn_Split"("SplitOther",'號之',1)) <= 5
                           AND LENGTH("Fn_Split"("SplitOther",'號之',2)) <= 5
                           AND INSTR("SplitOther",'樓') <= 0
                      THEN "Fn_Split"("SplitOther",'號之',2)
                      WHEN INSTR("SplitOther",'號之') > 0 
                      THEN u''
                      WHEN INSTR("SplitOther",'號-') > 0
                           AND LENGTH("Fn_Split"("SplitOther",'號-',1)) <= 5
                           AND LENGTH("Fn_Split"("SplitOther",'號-',2)) <= 5
                           AND INSTR("SplitOther",'樓') <= 0
                      THEN "Fn_Split"("SplitOther",'號-',2)
                      WHEN INSTR("SplitOther",'號-') > 0 
                      THEN u''
                      WHEN INSTR("SplitOther",'號') > 0 
                           AND INSTR("SplitOther",'之') > 0 
                           AND INSTR("SplitOther",'之') < INSTR("SplitOther",'號')
                           AND LENGTH("Fn_Split"("SplitOther",'之',1)) <= 5
                           AND LENGTH("Fn_Split"("Fn_Split"("SplitOther",'之',2),'號',1)) <= 5
                      THEN "Fn_Split"("Fn_Split"("SplitOther",'之',2),'號',1)
                      WHEN INSTR("SplitOther",'號') > 0 
                           AND INSTR("SplitOther",'-') > 0 
                           AND INSTR("SplitOther",'-') < INSTR("SplitOther",'號')
                           AND LENGTH("Fn_Split"("SplitOther",'-',1)) <= 5
                           AND LENGTH("Fn_Split"("Fn_Split"("SplitOther",'-',2),'號',1)) <= 5
                      THEN "Fn_Split"("Fn_Split"("SplitOther",'-',2),'號',1)
                    ELSE u'' END
       ,"SplitOther" = CASE 
                         WHEN INSTR("SplitOther",'號之') > 0
                              AND LENGTH("Fn_Split"("SplitOther",'號之',1)) <= 5
                              AND LENGTH("Fn_Split"("SplitOther",'號之',2)) <= 5
                              AND INSTR("SplitOther",'樓') <= 0
                         THEN u''
                         WHEN INSTR("SplitOther",'號之') > 0 
                         THEN "SplitOther"
                         WHEN INSTR("SplitOther",'號-') > 0
                              AND LENGTH("Fn_Split"("SplitOther",'號-',1)) <= 5
                              AND LENGTH("Fn_Split"("SplitOther",'號-',2)) <= 5
                              AND INSTR("SplitOther",'樓') <= 0
                         THEN u''
                         WHEN INSTR("SplitOther",'號-') > 0 
                         THEN "SplitOther"
                         WHEN INSTR("SplitOther",'號') > 0 
                              AND INSTR("SplitOther",'之') > 0
                              AND INSTR("SplitOther",'之') < INSTR("SplitOther",'號')
                              AND LENGTH("Fn_Split"("SplitOther",'之',1)) <= 5
                              AND LENGTH("Fn_Split"("Fn_Split"("SplitOther",'之',2),'號',1)) <= 5
                         THEN "Fn_Split"("SplitOther",'號',2)
                         WHEN INSTR("SplitOther",'號') > 0 
                              AND INSTR("SplitOther",'-') > 0
                              AND INSTR("SplitOther",'-') < INSTR("SplitOther",'號')
                              AND LENGTH("Fn_Split"("SplitOther",'-',1)) <= 5
                              AND LENGTH("Fn_Split"("Fn_Split"("SplitOther",'-',2),'號',1)) <= 5
                         THEN "Fn_Split"("SplitOther",'號',2)
                         WHEN INSTR("SplitOther",'號') > 0 
                              AND LENGTH("Fn_Split"("SplitOther",'號',1)) <= 5
                         THEN "Fn_Split"("SplitOther",'號',2)
                       ELSE "SplitOther" END
    WHERE "SplitFg" = 'Y';

    -- 切割'樓'、'樓之'
    UPDATE "CurrAddrSplit"
    SET "Floor" = CASE
                    WHEN INSTR("SplitOther",'樓') > 0
                         AND LENGTH("Fn_Split"("SplitOther",'樓',1)) <= 5
                    THEN "Fn_Split"("SplitOther",'樓',1)
                    WHEN INSTR("SplitOther",'F') > 0
                         AND LENGTH("Fn_Split"("SplitOther",'F',1)) <= 5
                    THEN "Fn_Split"("SplitOther",'F',1)
                  ELSE u'' END
       ,"FloorDash" = CASE
                        WHEN INSTR("SplitOther",'樓之') > 0 
                             AND LENGTH("Fn_Split"("SplitOther",'樓之',2)) <= 5
                        THEN "Fn_Split"("SplitOther",'樓之',2)
                        WHEN INSTR("SplitOther",'樓之') > 0 
                        THEN u''
                        WHEN INSTR("SplitOther",'樓-') > 0 
                             AND LENGTH("Fn_Split"("SplitOther",'樓-',2)) <= 5
                        THEN "Fn_Split"("SplitOther",'樓-',2)
                        WHEN INSTR("SplitOther",'樓-') > 0 
                        THEN u''
                        WHEN INSTR("SplitOther",'F之') > 0 
                             AND LENGTH("Fn_Split"("SplitOther",'F之',2)) <= 5
                        THEN "Fn_Split"("SplitOther",'F之',2)
                        WHEN INSTR("SplitOther",'F之') > 0 
                        THEN u''
                        WHEN INSTR("SplitOther",'F-') > 0 
                             AND LENGTH("Fn_Split"("SplitOther",'F-',2)) <= 5
                        THEN "Fn_Split"("SplitOther",'F-',2)
                        WHEN INSTR("SplitOther",'F-') > 0 
                        THEN u''
                      ELSE u'' END
       ,"SplitOther" = CASE
                         WHEN INSTR("SplitOther",'樓之') > 0 
                              AND LENGTH("Fn_Split"("SplitOther",'樓之',1)) <= 5
                              AND LENGTH("Fn_Split"("SplitOther",'樓之',2)) <= 5
                         THEN u''
                         WHEN INSTR("SplitOther",'樓-') > 0 
                              AND LENGTH("Fn_Split"("SplitOther",'樓-',1)) <= 5
                              AND LENGTH("Fn_Split"("SplitOther",'樓-',2)) <= 5
                         THEN u''
                         WHEN INSTR("SplitOther",'樓') > 0
                              AND LENGTH("Fn_Split"("SplitOther",'樓',1)) <= 5
                              AND NVL(LENGTH("Fn_Split"("SplitOther",'樓',2)),0) <= 0
                         THEN u''
                         WHEN INSTR("SplitOther",'F之') > 0
                              AND LENGTH("Fn_Split"("SplitOther",'F之',1)) <= 5
                              AND LENGTH("Fn_Split"("SplitOther",'F之',2)) <= 5
                         THEN u''
                         WHEN INSTR("SplitOther",'F-') > 0
                              AND LENGTH("Fn_Split"("SplitOther",'F-',1)) <= 5
                              AND LENGTH("Fn_Split"("SplitOther",'F-',2)) <= 5
                         THEN u''
                         WHEN INSTR("SplitOther",'F') > 0
                              AND LENGTH("Fn_Split"("SplitOther",'F',1)) <= 5
                              AND NVL(LENGTH("Fn_Split"("SplitOther",'F',2)),0) <= 0
                         THEN u''
                       ELSE "SplitOther" END
    WHERE "SplitFg" = 'Y';

    -- 修改"切割是否成功"記號
    UPDATE "CurrAddrSplit"
    SET "SplitFg" = CASE WHEN LENGTH("SplitOther") > 0
                         THEN 'N'
                    ELSE 'Y' END 
    WHERE "SplitFg" = 'Y';

    MERGE INTO "CustMain" C1
    USING (
         SELECT "CustId"       -- 客戶統編 VARCHAR2 10
              , "OriginalRoad" -- 原檔路名 NVARCHAR2 40 
              , "CityCode"     -- 比對縣市代碼 VARCHAR2 2 
              , "CityItem"     -- 比對縣市名稱 NVARCHAR2 10
              , "AreaCode"     -- 比對鄉鎮市區代碼 VARCHAR2 3 
              , "AreaItem"     -- 比對鄉鎮區名稱 NVARCHAR2 12 
              , "SplitRoad"    -- 切割後路名 NVARCHAR2 40 
              , "SplitFg"      -- 切割是否成功 VARCHAR2 1
              , "Section"      -- 段 VARCHAR2 5 
              , "Alley"        -- 巷 VARCHAR2 5 
              , "Lane"         -- 弄 VARCHAR2 5 
              , "Num"          -- 號 VARCHAR2 5 
              , "NumDash"      -- 號之 VARCHAR2 5 
              , "Floor"        -- 樓 VARCHAR2 5 
              , "FloorDash"    -- 樓之 VARCHAR2 5 
         FROM "CurrAddrSplit"
    ) C2
    ON (
         C1."CustId" = C2."CustId"
     --    AND C2."SplitFg" = 'Y'
    )
    WHEN MATCHED THEN UPDATE SET
     C1."CurrCityCode"  = C2."CityCode"   -- 縣市代碼 VARCHAR2 2 
    ,C1."CurrAreaCode"  = C2."AreaCode"   -- 鄉鎮市區代碼 VARCHAR2 3 
    ,C1."CurrRoad"      = CASE
                            WHEN C2."SplitFg" = 'Y'
                            THEN C2."SplitRoad"  -- 路名 NVARCHAR2 40 
                          ELSE C1."CurrRoad" END
    ,C1."CurrSection"   = CASE
                            WHEN C2."SplitFg" = 'Y'
                            THEN C2."Section"    -- 段 VARCHAR2 5 
                          ELSE u'' END
    ,C1."CurrAlley"     = CASE
                            WHEN C2."SplitFg" = 'Y'
                            THEN C2."Alley"      -- 巷 VARCHAR2 5 
                          ELSE u'' END
    ,C1."CurrLane"      = CASE
                            WHEN C2."SplitFg" = 'Y'
                            THEN C2."Lane"       -- 弄 VARCHAR2 5 
                          ELSE u'' END
    ,C1."CurrNum"       = CASE
                            WHEN C2."SplitFg" = 'Y'
                            THEN C2."Num"        -- 號 VARCHAR2 5 
                          ELSE u'' END
    ,C1."CurrNumDash"   = CASE
                            WHEN C2."SplitFg" = 'Y'
                            THEN C2."NumDash"    -- 號之 VARCHAR2 5 
                          ELSE u'' END
    ,C1."CurrFloor"     = CASE
                            WHEN C2."SplitFg" = 'Y'
                            THEN C2."Floor"      -- 樓 VARCHAR2 5 
                          ELSE u'' END
    ,C1."CurrFloorDash" = CASE
                            WHEN C2."SplitFg" = 'Y'
                            THEN C2."FloorDash" -- 樓之 VARCHAR2 5 
                          ELSE u'' END
    ;

    -- 記錄程式結束時間
    JOB_END_TIME := SYSTIMESTAMP;

    commit;

    -- 例外處理
    Exception
    WHEN OTHERS THEN
    ERROR_MSG := SQLERRM || CHR(13) || CHR(10) || dbms_utility.format_error_backtrace;
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_RegAddrSplit_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;



/
