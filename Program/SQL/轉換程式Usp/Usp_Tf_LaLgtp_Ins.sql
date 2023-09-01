--------------------------------------------------------
--  DDL for Procedure Usp_Tf_LaLgtp_Ins
--------------------------------------------------------
set define off;

CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_Tf_LaLgtp_Ins" 
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
    EXECUTE IMMEDIATE 'ALTER TABLE "LaLgtp" DISABLE PRIMARY KEY CASCADE'; 
    EXECUTE IMMEDIATE 'TRUNCATE TABLE "LaLgtp" DROP STORAGE'; 
    EXECUTE IMMEDIATE 'ALTER TABLE "LaLgtp" ENABLE PRIMARY KEY'; 
 
    -- 寫入資料 
    INSERT INTO "LaLgtp" ( 
          "Cusbrh"
        , "Gdrid1"
        , "Gdrid2"
        , "Gdrnum"
        , "Lgtseq"
        , "Gdrmrk"
        , "Gdrnum2"
        , "Grtsts"
        , "Lgtcif"
        , "Lgtiam"
        , "Lgtiid"
        , "Lgtory"
        , "Lgtpta"
        , "Lgtsam"
        , "Lgtsat"
        , "Lgtcty"
        , "Lgttwn"
        , "Lgtsgm"
        , "Lgtssg"
        , "Lgtnm1"
        , "Lgtnm2"
        , "Lgtsqm"
        , "Lgttax"
        , "Lgttay"
        , "Lgttyp"
        , "Lgttyr"
        , "Lgtunt"
        , "Lgtuse"
        , "Lgtval"
        , "Lgtvym"
        , "Updateident"
        , "CreateDate" -- '建檔日期時間';
        , "CreateEmpNo" -- '建檔人員';
        , "LastUpdate" -- '最後更新日期時間';
        , "LastUpdateEmpNo" -- '最後更新人員';
    )
    SELECT Cusbrh         AS "Cusbrh"
         , Gdrid1         AS "Gdrid1"
         , Gdrid2         AS "Gdrid2"
         , Gdrnum         AS "Gdrnum"
         , Lgtseq         AS "Lgtseq"
         , Gdrmrk         AS "Gdrmrk"
         , Gdrnum2        AS "Gdrnum2"
         , Grtsts         AS "Grtsts"
         , Lgtcif         AS "Lgtcif"
         , Lgtiam         AS "Lgtiam"
         , Lgtiid         AS "Lgtiid"
         , Lgtory         AS "Lgtory"
         , Lgtpta         AS "Lgtpta"
         , Lgtsam         AS "Lgtsam"
         , Lgtsat         AS "Lgtsat"
         , Lgtcty         AS "Lgtcty"
         , Lgttwn         AS "Lgttwn"
         , Lgtsgm         AS "Lgtsgm"
         , Lgtssg         AS "Lgtssg"
         , Lgtnm1         AS "Lgtnm1"
         , Lgtnm2         AS "Lgtnm2"
         , Lgtsqm         AS "Lgtsqm"
         , Lgttax         AS "Lgttax"
         , Lgttay         AS "Lgttay"
         , Lgttyp         AS "Lgttyp"
         , Lgttyr         AS "Lgttyr"
         , Lgtunt         AS "Lgtunt"
         , Lgtuse         AS "Lgtuse"
         , Lgtval         AS "Lgtval"
         , Lgtvym         AS "Lgtvym"
         , Update_ident   AS "Updateident"
         , JOB_START_TIME AS "CreateDate" -- 建檔日期時間 DATE 
         , '999999'       AS "CreateEmpNo" -- 建檔人員 VARCHAR2 6
         , JOB_START_TIME AS "LastUpdate" -- 最後更新日期時間 DATE 
         , '999999'       AS "LastUpdateEmpNo" -- 最後更新人員 VARCHAR2 6
    FROM LA$LGTP
    ; 
 
    -- 記錄寫入筆數 
    INS_CNT := INS_CNT + sql%rowcount; 

    -- 記錄程式結束時間 
    JOB_END_TIME := SYSTIMESTAMP; 
 
    commit; 
 
    -- 例外處理 
    Exception 
    WHEN OTHERS THEN 
    ERROR_MSG := SQLERRM || CHR(13) || CHR(10) || dbms_utility.format_error_backtrace; 
END;

/
