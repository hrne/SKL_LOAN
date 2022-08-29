--------------------------------------------------------
--  DDL for Procedure Usp_Tf_AddressSplit_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE EDITIONABLE PROCEDURE "Usp_Tf_AddressSplit_Ins" 
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
    EXECUTE IMMEDIATE 'ALTER TABLE "AddressSplit" DISABLE PRIMARY KEY CASCADE';
    EXECUTE IMMEDIATE 'TRUNCATE TABLE "AddressSplit" DROP STORAGE';
    EXECUTE IMMEDIATE 'ALTER TABLE "AddressSplit" ENABLE PRIMARY KEY';

    -- 寫入資料
    INSERT INTO "AddressSplit"
    SELECT "ClBuilding"."ClCode1"         AS "ClCode1"             -- 擔保品代號1 DECIMAL 1 
          ,"ClBuilding"."ClCode2"         AS "ClCode2"             -- 擔保品代號2 DECIMAL 2 
          ,"ClBuilding"."ClNo"            AS "ClNo"                -- 擔保品編號 DECIMAL 7
          ,"ClBuilding"."Road"            AS "OriginalRoad"        -- 原檔路名 NVARCHAR2 40 
          ,CASE
             WHEN INSTR("ClBuilding"."Road",'(路)') > 0
             THEN SUBSTR("ClBuilding"."Road",0,INSTR("ClBuilding"."Road",'(路)')+2)
             WHEN INSTR("ClBuilding"."Road",'路') > 0
             THEN SUBSTR("ClBuilding"."Road",0,INSTR("ClBuilding"."Road",'路'))
             WHEN INSTR("ClBuilding"."Road",'街') > 0
             THEN SUBSTR("ClBuilding"."Road",0,INSTR("ClBuilding"."Road",'街'))
             WHEN INSTR("ClBuilding"."Road",'大道') > 0
             THEN SUBSTR("ClBuilding"."Road",0,INSTR("ClBuilding"."Road",'大道')+1)
           ELSE "Road" END                AS "SplitRoad"           -- 切割後路名 NVARCHAR2 40 
          ,CASE
             WHEN INSTR("Road",'(路)') > 0
             THEN SUBSTR("Road",INSTR("Road",'(路)')+3)
             WHEN INSTR("Road",'路') > 0
             THEN SUBSTR("Road",INSTR("Road",'路')+1)
             WHEN INSTR("Road",'街') > 0
             THEN SUBSTR("Road",INSTR("Road",'街')+1)
             WHEN INSTR("Road",'大道') > 0
             THEN SUBSTR("Road",INSTR("Road",'大道')+2)
           ELSE u'' END                  AS "SplitOther"          -- 切割後其餘資料 NVARCHAR2 40 
          ,CASE
             WHEN INSTR("ClBuilding"."Road",'(路)') > 0
             THEN 'Y'
             WHEN INSTR("ClBuilding"."Road",'路') > 0
             THEN 'Y'
             WHEN INSTR("ClBuilding"."Road",'街') > 0
             THEN 'Y'
             WHEN INSTR("ClBuilding"."Road",'大道') > 0
             THEN 'Y'
           ELSE 'N' END                   AS "SplitFg"             -- 切割是否成功 VARCHAR2 1
          ,u''                            AS "Section"             -- 段 VARCHAR2 5 
          ,u''                            AS "Alley"               -- 巷 VARCHAR2 5 
          ,u''                            AS "Lane"                -- 弄 VARCHAR2 5 
          ,u''                            AS "Num"                 -- 號 VARCHAR2 5 
          ,u''                            AS "NumDash"             -- 號之 VARCHAR2 5 
          ,u''                            AS "Floor"               -- 樓 VARCHAR2 5 
          ,u''                            AS "FloorDash"           -- 樓之 VARCHAR2 5 
    FROM "ClBuilding"
    ;

    -- 記錄寫入筆數
    INS_CNT := INS_CNT + sql%rowcount;

    -- 切割'段'
    UPDATE "AddressSplit"
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
    UPDATE "AddressSplit"
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
    UPDATE "AddressSplit"
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
    UPDATE "AddressSplit"
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
    UPDATE "AddressSplit"
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
    UPDATE "AddressSplit"
    SET "SplitFg" = CASE WHEN LENGTH("SplitOther") > 0
                         THEN 'N'
                    ELSE 'Y' END 
    WHERE "SplitFg" = 'Y';

    MERGE INTO "ClBuilding" C1
    USING (SELECT "ClCode1"   -- 擔保品代號1 DECIMAL 1 
                 ,"ClCode2"   -- 擔保品代號2 DECIMAL 2 
                 ,"ClNo"      -- 擔保品編號 DECIMAL 7
                 ,"SplitRoad" -- 切割後路名 NVARCHAR2 40 
                 ,"SplitFg"   -- 切割是否成功 VARCHAR2 1
                 ,"Section"   -- 段 VARCHAR2 5 
                 ,"Alley"     -- 巷 VARCHAR2 5 
                 ,"Lane"      -- 弄 VARCHAR2 5 
                 ,"Num"       -- 號 VARCHAR2 5 
                 ,"NumDash"   -- 號之 VARCHAR2 5 
                 ,"Floor"     -- 樓 VARCHAR2 5 
                 ,"FloorDash" -- 樓之 VARCHAR2 5 
           FROM "AddressSplit"
           WHERE "SplitFg" = 'Y') C2
    ON (C1."ClCode1" = C2."ClCode1"
        AND C1."ClCode2" = C2."ClCode2"
        AND C1."ClNo" = C2."ClNo")
    WHEN MATCHED THEN UPDATE SET
     C1."Road"      = C2."SplitRoad"  -- 路名 NVARCHAR2 40 
    ,C1."Section"   = C2."Section"    -- 段 VARCHAR2 5 
    ,C1."Alley"     = C2."Alley"      -- 巷 VARCHAR2 5 
    ,C1."Lane"      = C2."Lane"       -- 弄 VARCHAR2 5 
    ,C1."Num"       = C2."Num"        -- 號 VARCHAR2 5 
    ,C1."NumDash"   = C2."NumDash"    -- 號之 VARCHAR2 5 
    ,C1."Floor"     = C2."Floor"      -- 樓 VARCHAR2 5 
    ,C1."FloorDash" = C2."FloorDash"; -- 樓之 VARCHAR2 5 

    -- 記錄程式結束時間
    JOB_END_TIME := SYSTIMESTAMP;

    commit;

    -- 例外處理
    Exception
    WHEN OTHERS THEN
    ERROR_MSG := SQLERRM || CHR(13) || CHR(10) || dbms_utility.format_error_backtrace;
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_AddressSplit_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;



/
