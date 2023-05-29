--------------------------------------------------------
--  DDL for Procedure Usp_Tf_Lahgtp_Ins
--------------------------------------------------------
set define off;

CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_Tf_Lahgtp_Ins" 
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
    EXECUTE IMMEDIATE 'ALTER TABLE "Lahgtp" DISABLE PRIMARY KEY CASCADE'; 
    EXECUTE IMMEDIATE 'TRUNCATE TABLE "Lahgtp" DROP STORAGE'; 
    EXECUTE IMMEDIATE 'ALTER TABLE "Lahgtp" ENABLE PRIMARY KEY'; 
 
    -- 寫入資料 
    INSERT INTO "Lahgtp" ( 
            "Cusbrh" -- '營業單位別';
          , "Gdrid1" -- '押品別1';
          , "Gdrid2" -- '押品別2';
          , "Gdrnum" -- '押品號碼';
          , "Lgtseq" -- '序號';
          , "Lgtcif" -- '提供人CIFKEY';
          , "Lgtadr" -- '門牌號碼';
          , "Hgtmhn" -- '主建物建號';
          , "Hgtmhs" -- '主建物(坪)';
          , "Hgtpsm" -- '公設(坪)';
          , "Hgtcam" -- '車位(坪)';
          , "Lgtiid" -- '鑑價公司';
          , "Lgtunt" -- '鑑價單價/坪';
          , "Lgtiam" -- '核准金額';
          , "Lgtsam" -- '設定金額';
          , "Lgtsat" -- '代償後謄本';
          , "Grtsts" -- '押品狀況碼';
          , "Hgtstr" -- '建物結構';
          , "Hgtcdt" -- '建造年份';
          , "Hgtflr" -- '樓層數';
          , "Hgtrof" -- '屋頂結構';
          , "Salnam" -- '賣方姓名';
          , "Salid1" -- '賣方ID';
          , "Hgtcap" -- '停車位形式';
          , "Hgtgus" -- '主要用途';
          , "Hgtaus" -- '附屬建物用途';
          , "Hgtfor" -- '所在樓層';
          , "Hgtcpe" -- '建築完成日';
          , "Hgtads" -- '附屬建物(坪)';
          , "Hgtad1" -- '縣市名稱';
          , "Hgtad2" -- '鄉鎮市區名稱';
          , "Hgtad3" -- '街路巷弄';
          , "Hgtgtd" -- '房屋所有權取得日';
          , "Buyamt" -- '買賣契約價格';
          , "Buydat" -- '買賣契約日期';
          , "Gdrnum2" -- '擔保品群組編號';
          , "Gdrmrk" -- '註記';
          , "Hgtmhn2" -- '主建物建號2';
          , "Hgtcip" -- '獨立產權註記';
          , "UpdateIdent" -- 'Field update / access identifier';
          , "CreateDate" -- '建檔日期時間';
          , "CreateEmpNo" -- '建檔人員';
          , "LastUpdate" -- '最後更新日期時間';
          , "LastUpdateEmpNo" -- '最後更新人員';
    )
    SELECT Cusbrh         AS "Cusbrh" -- '營業單位別';
         , Gdrid1         AS "Gdrid1" -- '押品別1';
         , Gdrid2         AS "Gdrid2" -- '押品別2';
         , Gdrnum         AS "Gdrnum" -- '押品號碼';
         , Lgtseq         AS "Lgtseq" -- '序號';
         , Lgtcif         AS "Lgtcif" -- '提供人CIFKEY';
         , Lgtadr         AS "Lgtadr" -- '門牌號碼';
         , Hgtmhn         AS "Hgtmhn" -- '主建物建號';
         , Hgtmhs         AS "Hgtmhs" -- '主建物(坪)';
         , Hgtpsm         AS "Hgtpsm" -- '公設(坪)';
         , Hgtcam         AS "Hgtcam" -- '車位(坪)';
         , Lgtiid         AS "Lgtiid" -- '鑑價公司';
         , Lgtunt         AS "Lgtunt" -- '鑑價單價/坪';
         , Lgtiam         AS "Lgtiam" -- '核准金額';
         , Lgtsam         AS "Lgtsam" -- '設定金額';
         , Lgtsat         AS "Lgtsat" -- '代償後謄本';
         , Grtsts         AS "Grtsts" -- '押品狀況碼';
         , Hgtstr         AS "Hgtstr" -- '建物結構';
         , Hgtcdt         AS "Hgtcdt" -- '建造年份';
         , Hgtflr         AS "Hgtflr" -- '樓層數';
         , Hgtrof         AS "Hgtrof" -- '屋頂結構';
         , Salnam         AS "Salnam" -- '賣方姓名';
         , Salid1         AS "Salid1" -- '賣方ID';
         , Hgtcap         AS "Hgtcap" -- '停車位形式';
         , Hgtgus         AS "Hgtgus" -- '主要用途';
         , Hgtaus         AS "Hgtaus" -- '附屬建物用途';
         , Hgtfor         AS "Hgtfor" -- '所在樓層';
         , Hgtcpe         AS "Hgtcpe" -- '建築完成日';
         , Hgtads         AS "Hgtads" -- '附屬建物(坪)';
         , Hgtad1         AS "Hgtad1" -- '縣市名稱';
         , Hgtad2         AS "Hgtad2" -- '鄉鎮市區名稱';
         , Hgtad3         AS "Hgtad3" -- '街路巷弄';
         , Hgtgtd         AS "Hgtgtd" -- '房屋所有權取得日';
         , Buyamt         AS "Buyamt" -- '買賣契約價格';
         , Buydat         AS "Buydat" -- '買賣契約日期';
         , Gdrnum2        AS "Gdrnum2" -- '擔保品群組編號';
         , Gdrmrk         AS "Gdrmrk" -- '註記';
         , Hgtmhn2        AS "Hgtmhn2" -- '主建物建號2';
         , Hgtcip         AS "Hgtcip" -- '獨立產權註記';
         , Update_Ident   AS "UpdateIdent" -- 'Field update / access identifier';
         , JOB_START_TIME AS "CreateDate" -- 建檔日期時間 DATE 
         , '999999'       AS "CreateEmpNo" -- 建檔人員 VARCHAR2 6
         , JOB_START_TIME AS "LastUpdate" -- 最後更新日期時間 DATE 
         , '999999'       AS "LastUpdateEmpNo" -- 最後更新人員 VARCHAR2 6
    FROM LA$HGTP
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
