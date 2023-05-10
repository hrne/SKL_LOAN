--------------------------------------------------------
--  DDL for Procedure Usp_Tf_CustTelNo_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_Tf_CustTelNo_Ins" 
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

    EXECUTE IMMEDIATE 'TRUNCATE TABLE "TempCustTelNo" DROP STORAGE';

     INSERT INTO "TempCustTelNo"
     SELECT DISTINCT 
            S0.CUSTID
           ,translate(NVL(S0.CUSTEL,' '), ' *~())#,.:=￠', '----------') AS "CUSTEL"
           ,S0.CUSNA4
           ,s0."TelTypeCode"
           ,NULL AS "TelArea"
           ,NULL AS "TelNo"
           ,NULL AS "TelExt"
     FROM (
          -- N120***884
          -- 戶籍電話 02-28729863 -- CUSTLA
          -- 公司電話 02-29151254*109 29151254*105 -- CUSTL1 CUSTL2
          -- 通訊電話 02-28729863 -- CUSTL3
          -- 手機 0932044937 -- CUSTL4
          -- 傳真 2915-9863 -- CUSFX1
          SELECT TRIM("CUSID1")     AS "CUSTID"
               , TRIM("CUSTLA")     AS "CUSTEL"
               , '02'               AS "TelTypeCode" -- 住家電話(戶籍)
               , "CUSNA4"
          FROM "CU$CUSP"
          UNION
          SELECT TRIM("CUSID1")     AS "CUSTID"
               , TRIM("CUSTL1")     AS "CUSTEL"
               , '01'               AS "TelTypeCode" -- 公司電話1
               , "CUSNA4"
          FROM "CU$CUSP"
          UNION
          SELECT TRIM("CUSID1")     AS "CUSTID"
               , TRIM("CUSTL2")     AS "CUSTEL"
               , '01'               AS "TelTypeCode" -- 公司電話2
               , "CUSNA4"
          FROM "CU$CUSP"
          UNION
          SELECT TRIM("CUSID1")     AS "CUSTID"
               , TRIM("CUSTL3")     AS "CUSTEL"
               , '02'               AS "TelTypeCode" -- 住家電話(通訊)
               , "CUSNA4"
          FROM "CU$CUSP"
          UNION
          SELECT TRIM("CUSID1")     AS "CUSTID"
               , TRIM("CUSBBC")     AS "CUSTEL"
               , '03'               AS "TelTypeCode" -- 手機
               , "CUSNA4"
          FROM "CU$CUSP"
          UNION
          SELECT TRIM("CUSID1")     AS "CUSTID"
               , TRIM("CUSTL4")     AS "CUSTEL"
               , '03'               AS "TelTypeCode" -- 手機
               , "CUSNA4"
          FROM "CU$CUSP"
          UNION
          SELECT TRIM("CUSID1")     AS "CUSTID"
               , TRIM("CUSFX1")     AS "CUSTEL"
               , '04'               AS "TelTypeCode" -- 傳真
               , "CUSNA4"
          FROM "CU$CUSP"
          UNION
          SELECT TRIM("CUSID1")     AS "CUSTID"
               , TRIM("CUSTLA")     AS "CUSTEL"
               , '02'               AS "TelTypeCode" -- 住家電話(戶籍)
               , "CUSNA4"
          FROM "CU$CUSP"
          UNION
          -- 2022-01-21 FROM 新壽IT 珮琪
          -- 目前AS/400發送簡訊主要分以下兩種欄位順序依序檢核，第一隻符合手機格式的即以該隻手機發送對應簡訊
          -- LNM56OP、AHM71NP銀行扣款不成功簡訊通知檔
          -- LNM56WP火險通知簡訊通知檔
          -- CUSTL1+CUSTL2+CUSTL3+CUSTL4+CUSBBC+CUSFX1    
          -- LNM56YP、AHM71FP銀行扣款前簡訊通知檔 
          -- LNM56ZP火險未繳簡訊通知檔 
          -- CUSTL1+CUSTL2+CUSTL3+CUSTL4
          SELECT TRIM("CUSID1")     AS "CUSTID"
               , CASE
                    WHEN REGEXP_LIKE(TRIM(TRANSLATE("CUSTL1",'0123456789- ','0123456789')),'^09\d{8}$')
                    THEN TRIM(TRANSLATE("CUSTL1",'0123456789- ','0123456789'))
                    WHEN REGEXP_LIKE(TRIM(TRANSLATE("CUSTL2",'0123456789- ','0123456789')),'^09\d{8}$')
                    THEN TRIM(TRANSLATE("CUSTL2",'0123456789- ','0123456789'))
                    WHEN REGEXP_LIKE(TRIM(TRANSLATE("CUSTL3",'0123456789- ','0123456789')),'^09\d{8}$')
                    THEN TRIM(TRANSLATE("CUSTL3",'0123456789- ','0123456789'))
                    WHEN REGEXP_LIKE(TRIM(TRANSLATE("CUSTL4",'0123456789- ','0123456789')),'^09\d{8}$')
                    THEN TRIM(TRANSLATE("CUSTL4",'0123456789- ','0123456789'))
                    WHEN REGEXP_LIKE(TRIM(TRANSLATE("CUSBBC",'0123456789- ','0123456789')),'^09\d{8}$')
                    THEN TRIM(TRANSLATE("CUSBBC",'0123456789- ','0123456789'))
                    WHEN REGEXP_LIKE(TRIM(TRANSLATE("CUSFX1",'0123456789- ','0123456789')),'^09\d{8}$')
                    THEN TRIM(TRANSLATE("CUSFX1",'0123456789- ','0123456789'))
                    -- 2023-05-09 Wei 因為新壽要遮罩又要看到資料 特別增加以下判斷 09開頭,十碼長就轉入簡訊
                    WHEN SUBSTR("CUSTL1",0,2) = '09' 
                         AND LENGTH(TRANSLATE("CUSTL1",'0123456789- ','0123456789')) = 10
                    THEN TRANSLATE("CUSTL1",'0123456789- ','0123456789')
                    WHEN SUBSTR("CUSTL2",0,2) = '09' 
                         AND LENGTH(TRANSLATE("CUSTL2",'0123456789- ','0123456789')) = 10
                    THEN TRANSLATE("CUSTL2",'0123456789- ','0123456789')
                    WHEN SUBSTR("CUSTL3",0,2) = '09' 
                         AND LENGTH(TRANSLATE("CUSTL3",'0123456789- ','0123456789')) = 10
                    THEN TRANSLATE("CUSTL3",'0123456789- ','0123456789')
                    WHEN SUBSTR("CUSTL4",0,2) = '09' 
                         AND LENGTH(TRANSLATE("CUSTL4",'0123456789- ','0123456789')) = 10
                    THEN TRANSLATE("CUSTL4",'0123456789- ','0123456789')
                    WHEN SUBSTR("CUSBBC",0,2) = '09' 
                         AND LENGTH(TRANSLATE("CUSBBC",'0123456789- ','0123456789')) = 10
                    THEN TRANSLATE("CUSBBC",'0123456789- ','0123456789')
                    WHEN SUBSTR("CUSFX1",0,2) = '09' 
                         AND LENGTH(TRANSLATE("CUSFX1",'0123456789- ','0123456789')) = 10
                    THEN TRANSLATE("CUSFX1",'0123456789- ','0123456789')
                 ELSE 'X'
                 END                AS "CUSTEL"
               , '05'               AS "TelTypeCode" -- 05:簡訊
               , "CUSNA4"
          FROM "CU$CUSP"
     ) s0
     WHERE "CUSTEL" != 'X'
     ORDER BY S0."CUSTID";

     /* 漏打0的手機號碼 */
     UPDATE "TempCustTelNo"
     SET "CUSTEL" = '0' || "CUSTEL"
     WHERE REGEXP_LIKE("CUSTEL",'^9\d{8}$')
     ;

     /* EXT 改為 - */
     UPDATE "TempCustTelNo"
     SET "CUSTEL" = REPLACE("CUSTEL",'EXT','-')
     WHERE INSTR("CUSTEL",'EXT') > 0
     ;

     /* 把重複的-去掉 9 */
     UPDATE "TempCustTelNo"
     SET "CUSTEL" = REPLACE("CUSTEL",'---------','-')
     WHERE LENGTH("CUSTEL") - LENGTH(REPLACE("CUSTEL",'---------','-')) > 0
     ;

     /* 把重複的-去掉 8 */
     UPDATE "TempCustTelNo"
     SET "CUSTEL" = REPLACE("CUSTEL",'--------','-')
     WHERE LENGTH("CUSTEL") - LENGTH(REPLACE("CUSTEL",'--------','-')) > 0
     ;

     /* 把重複的-去掉 7 */
     UPDATE "TempCustTelNo"
     SET "CUSTEL" = REPLACE("CUSTEL",'-------','-')
     WHERE LENGTH("CUSTEL") - LENGTH(REPLACE("CUSTEL",'-------','-')) > 0
     ;

     /* 把重複的-去掉 6 */
     UPDATE "TempCustTelNo"
     SET "CUSTEL" = REPLACE("CUSTEL",'------','-')
     WHERE LENGTH("CUSTEL") - LENGTH(REPLACE("CUSTEL",'------','-')) > 0
     ;

     /* 把重複的-去掉 5 */
     UPDATE "TempCustTelNo"
     SET "CUSTEL" = REPLACE("CUSTEL",'-----','-')
     WHERE LENGTH("CUSTEL") - LENGTH(REPLACE("CUSTEL",'-----','-')) > 0
     ;

     /* 把重複的-去掉 4 */
     UPDATE "TempCustTelNo"
     SET "CUSTEL" = REPLACE("CUSTEL",'----','-')
     WHERE LENGTH("CUSTEL") - LENGTH(REPLACE("CUSTEL",'----','-')) > 0
     ;

     /* 把重複的-去掉 3 */
     UPDATE "TempCustTelNo"
     SET "CUSTEL" = REPLACE("CUSTEL",'---','-')
     WHERE LENGTH("CUSTEL") - LENGTH(REPLACE("CUSTEL",'---','-')) > 0
     ;

     /* 把重複的-去掉 2 */
     UPDATE "TempCustTelNo"
     SET "CUSTEL" = REPLACE("CUSTEL",'--','-')
     WHERE LENGTH("CUSTEL") - LENGTH(REPLACE("CUSTEL",'--','-')) > 0
     ;

     /* 若-在最後一碼 刪除 */
     UPDATE "TempCustTelNo"
     SET "CUSTEL" = SUBSTR("CUSTEL",0,LENGTH("CUSTEL") - 1)
     WHERE SUBSTR("CUSTEL",-1) = '-'
     ;

     /* 若-在第一碼 刪除 */
     UPDATE "TempCustTelNo"
     SET "CUSTEL" = SUBSTR("CUSTEL",2)
     WHERE SUBSTR("CUSTEL", 0, 1) = '-'
     ;

     /* FAX開頭者,種類切換為04 */
     UPDATE "TempCustTelNo"
     SET "TelTypeCode" = '04'
        ,"CUSTEL" = SUBSTR("CUSTEL",4)
     WHERE "CUSTEL" LIKE '%FAX%'
     ;

     /* 若-在第一碼 刪除 */
     UPDATE "TempCustTelNo"
     SET "CUSTEL" = SUBSTR("CUSTEL",2)
     WHERE SUBSTR("CUSTEL", 0, 1) = '-'
     ;

     /* 先切傳真號碼 */
     UPDATE "TempCustTelNo"
     SET "TelArea" = CASE
                       WHEN INSTR("CUSTEL",'-') > 0 AND INSTR("CUSTEL",'-') < 5
                       THEN SUBSTR("CUSTEL",0,INSTR("CUSTEL",'-') - 1)
                     ELSE '' END
        ,"TelNo" = CASE
                     WHEN INSTR("CUSTEL",'-') > 0 AND INSTR("CUSTEL",'-') < 5
                     THEN SUBSTR("CUSTEL",INSTR("CUSTEL",'-') + 1)
                     WHEN INSTR("CUSTEL",'-') > 0 AND INSTR("CUSTEL",'-') = 5
                     THEN SUBSTR("CUSTEL",0,INSTR("CUSTEL",'-') - 1) || SUBSTR("CUSTEL",INSTR("CUSTEL",'-') + 1)
                     WHEN INSTR("CUSTEL",'-') > 0 AND INSTR("CUSTEL",'-') > 5
                     THEN SUBSTR("CUSTEL",0,INSTR("CUSTEL",'-') - 1)
                     WHEN LENGTHB("CUSTEL") > 10
                     THEN SUBSTR("CUSTEL",0,10)
                   ELSE "CUSTEL" END
        ,"TelExt" = CASE
                     WHEN INSTR("CUSTEL",'-') > 0 AND INSTR("CUSTEL",'-') > 5
                     THEN SUBSTR("CUSTEL",INSTR("CUSTEL",'-') + 1)
                     WHEN LENGTHB("CUSTEL") > 10
                     THEN SUBSTR("CUSTEL",11)
                   ELSE '' END
     WHERE "TelTypeCode" = '04'
     ;

     /* 篩選手機號碼 */
     UPDATE "TempCustTelNo"
     SET "TelTypeCode" = '03'
     ,"TelNo" = CASE
                  WHEN LENGTH(TRANSLATE("CUSTEL",'0123456789-','0123456789')) > 10
                  THEN SUBSTR(TRANSLATE("CUSTEL",'0123456789-','0123456789'),0,10)
                ELSE TRANSLATE("CUSTEL",'0123456789-','0123456789') END
     ,"TelExt" = CASE
                   WHEN LENGTH(TRANSLATE("CUSTEL",'0123456789-','0123456789')) > 10
                   THEN SUBSTR(TRANSLATE("CUSTEL",'0123456789-','0123456789'),11)
                 ELSE '' END
     WHERE SUBSTR("CUSTEL",0,2) = '09'
       AND "TelTypeCode" != '05' -- 05:簡訊
     ;

     /* -數量大於3 */
     UPDATE "TempCustTelNo"
     SET "TelArea" = SUBSTR("CUSTEL",0,INSTR("CUSTEL",'-') - 1)
        ,"TelNo" = TRANSLATE(SUBSTR("CUSTEL",INSTR("CUSTEL",'-') + 1),'0123456789-','0123456789')
     WHERE "TelTypeCode" IN ('01','02')
       AND LENGTH("CUSTEL") - LENGTH(REPLACE("CUSTEL",'-','')) > 3
     ;

     /* -數量等於3 */ 
     UPDATE "TempCustTelNo"
     SET "TelArea" = CASE
                       WHEN INSTR("CUSTEL",'-') <= 4
                       THEN SUBSTR("CUSTEL",0,INSTR("CUSTEL",'-') - 1)
                     ELSE '' END
        ,"TelNo" = CASE
                     WHEN INSTR("CUSTEL",'-') <= 4
                     THEN CASE
                            WHEN INSTR(SUBSTR("CUSTEL",INSTR("CUSTEL",'-') + 1),'-') >= 7
                            THEN SUBSTR("CUSTEL",INSTR("CUSTEL",'-') + 1 , INSTR(SUBSTR("CUSTEL",INSTR("CUSTEL",'-') + 1),'-') - 1)
                          ELSE SUBSTR(REPLACE(SUBSTR("CUSTEL",INSTR("CUSTEL",'-') + 1), '-', ''),0,10) END
                     WHEN INSTR("CUSTEL",'-') = 5
                     THEN REPLACE(SUBSTR("CUSTEL", 0 , INSTR("CUSTEL",'-') + INSTR(SUBSTR("CUSTEL",INSTR("CUSTEL",'-') + 1),'-') - 1), '-', '')
                   ELSE SUBSTR("CUSTEL",0,INSTR("CUSTEL",'-') - 1) END
        ,"TelExt" = CASE
                      WHEN INSTR("CUSTEL",'-') > 0 AND INSTR("CUSTEL",'-') <= 4
                      THEN CASE
                             WHEN INSTR(SUBSTR("CUSTEL",INSTR("CUSTEL",'-') + 1),'-') >= 7
                             THEN SUBSTR("CUSTEL",INSTR("CUSTEL",'-') + 1 + INSTR(SUBSTR("CUSTEL",INSTR("CUSTEL",'-') + 1),'-' ) )
                           ELSE SUBSTR(REPLACE(SUBSTR("CUSTEL",INSTR("CUSTEL",'-') + 1), '-', ''),11) END
                      WHEN INSTR("CUSTEL",'-') = 5
                      THEN REPLACE(SUBSTR("CUSTEL", INSTR("CUSTEL",'-') + 1 + INSTR(SUBSTR("CUSTEL",INSTR("CUSTEL",'-') + 1),'-') + 1), '-', '')
                    ELSE SUBSTR("CUSTEL",INSTR("CUSTEL",'-') + 1) END
     WHERE "TelTypeCode" IN ('01','02')
       AND LENGTH("CUSTEL") - LENGTH(REPLACE("CUSTEL",'-','')) = 3
     ;

     /* -數量等於2 */
     UPDATE "TempCustTelNo"
     SET "TelArea" = CASE
                       WHEN INSTR("CUSTEL",'-') <= 4
                       THEN SUBSTR("CUSTEL",0,INSTR("CUSTEL",'-') - 1)
                     ELSE '' END
        ,"TelNo" = CASE
                     WHEN INSTR("CUSTEL",'-') <= 4
                     THEN CASE
                            WHEN INSTR(SUBSTR("CUSTEL",INSTR("CUSTEL",'-') + 1),'-') >= 7
                            THEN SUBSTR("CUSTEL",INSTR("CUSTEL",'-') + 1 , INSTR(SUBSTR("CUSTEL",INSTR("CUSTEL",'-') + 1),'-') - 1)
                          ELSE SUBSTR(REPLACE(SUBSTR("CUSTEL",INSTR("CUSTEL",'-') + 1), '-', ''),0,10) END
                     WHEN INSTR("CUSTEL",'-') = 5
                     THEN REPLACE(SUBSTR("CUSTEL", 0 , INSTR("CUSTEL",'-') + INSTR(SUBSTR("CUSTEL",INSTR("CUSTEL",'-') + 1),'-') - 1), '-', '')
                   ELSE SUBSTR("CUSTEL",0,INSTR("CUSTEL",'-') - 1) END
        ,"TelExt" = CASE
                      WHEN INSTR("CUSTEL",'-') <= 4
                      THEN CASE
                             WHEN INSTR(SUBSTR("CUSTEL",INSTR("CUSTEL",'-') + 1),'-') >= 7
                             THEN SUBSTR("CUSTEL",INSTR("CUSTEL",'-') + INSTR(SUBSTR("CUSTEL",INSTR("CUSTEL",'-') + 1),'-') + 1)
                           ELSE SUBSTR(REPLACE(SUBSTR("CUSTEL",INSTR("CUSTEL",'-') + 1), '-', ''),11,5) END
                      WHEN INSTR("CUSTEL",'-') = 5
                      THEN REPLACE(SUBSTR("CUSTEL", INSTR("CUSTEL",'-') + 1 + INSTR(SUBSTR("CUSTEL",INSTR("CUSTEL",'-') + 1),'-') + 1), '-', '')
                    ELSE SUBSTR("CUSTEL",INSTR("CUSTEL",'-') + 1,5) END
     WHERE "TelTypeCode" IN ('01','02')
       AND LENGTH("CUSTEL") - LENGTH(REPLACE("CUSTEL",'-','')) = 2
     ;

     /* -數量等於1 */
     UPDATE "TempCustTelNo"
     SET "TelArea" = CASE
                       WHEN INSTR("CUSTEL",'-') <= 4
                       THEN SUBSTR("CUSTEL",0,INSTR("CUSTEL",'-') - 1)
                     ELSE '' END
        ,"TelNo" = CASE
                     WHEN INSTR("CUSTEL",'-') > 0 AND INSTR("CUSTEL",'-') <= 5
                     THEN SUBSTR(SUBSTR("CUSTEL",INSTR("CUSTEL",'-') + 1),0,10)
                   ELSE SUBSTR("CUSTEL",0,INSTR("CUSTEL",'-') - 1) END
        ,"TelExt" = CASE
                      WHEN INSTR("CUSTEL",'-') <= 4
                      THEN ''
                      WHEN INSTR("CUSTEL",'-') = 5
                      THEN SUBSTR(SUBSTR("CUSTEL",INSTR("CUSTEL",'-') + 1),11)
                    ELSE SUBSTR("CUSTEL",INSTR("CUSTEL",'-') + 1) END
     WHERE "TelTypeCode" IN ('01','02')
       AND LENGTH("CUSTEL") - LENGTH(REPLACE("CUSTEL",'-','')) = 1
       AND LENGTH(CASE
                     WHEN INSTR("CUSTEL",'-') > 0 AND INSTR("CUSTEL",'-') <= 5
                     THEN SUBSTR(SUBSTR("CUSTEL",INSTR("CUSTEL",'-') + 1),0,10)
                   ELSE SUBSTR("CUSTEL",0,INSTR("CUSTEL",'-') - 1) END) <= 10
       AND LENGTH(NVL(CASE
                    WHEN INSTR("CUSTEL",'-') <= 4
                    THEN ''
                    WHEN INSTR("CUSTEL",'-') = 5
                    THEN SUBSTR(SUBSTR("CUSTEL",INSTR("CUSTEL",'-') + 1),11)
                  ELSE SUBSTR("CUSTEL",INSTR("CUSTEL",'-') + 1) END,' ')) <= 5
     ;

     /* 總長度<=10 直接放入TelNo */
     UPDATE "TempCustTelNo"
     SET "TelNo" = "CUSTEL"
     WHERE "TelTypeCode" IN ('01','02')
       AND LENGTH("CUSTEL") <= 10
       AND NVL("TelNo",' ') = ' '
     ;

     /* UPDATE 77069880213 */
     UPDATE "TempCustTelNo"
     SET "TelTypeCode" = '01'
        ,"TelArea" = '02'
        ,"TelNo" = '77069880'
        ,"TelExt" = '213'
     WHERE "TelTypeCode" IN ('01','02')
       AND "CUSTEL" = '77069880213'
     ;

     /* 05:簡訊 總長度=10 直接放入TelNo */
     UPDATE "TempCustTelNo"
     SET "TelNo" = TRIM(TRANSLATE("CUSTEL",'0123456789- ','0123456789'))
     WHERE "TelTypeCode" = '05'
       AND LENGTH(TRIM(TRANSLATE("CUSTEL",'0123456789- ','0123456789'))) = 10
       AND NVL("TelNo",' ') = ' '
     ;

    -- 刪除舊資料
    EXECUTE IMMEDIATE 'ALTER TABLE "CustTelNo" DISABLE PRIMARY KEY CASCADE';
    EXECUTE IMMEDIATE 'TRUNCATE TABLE "CustTelNo" DROP STORAGE';
    EXECUTE IMMEDIATE 'ALTER TABLE "CustTelNo" ENABLE PRIMARY KEY';

    -- 寫入資料
    INSERT INTO "CustTelNo" (
        "TelNoUKey"           -- 電話識別碼 VARCHAR2 32 
      , "CustUKey"            -- 客戶識別碼 VARCHAR2 32 
      , "TelTypeCode"         -- 電話種類 VARCHAR2 2 
      , "TelArea"             -- 電話區碼 VARCHAR2 5 
      , "TelNo"               -- 電話號碼 VARCHAR2 10
      , "TelExt"              -- 分機號碼 VARCHAR2 6 
      , "TelChgRsnCode"       -- 異動原因 VARCHAR2 2 
      , "RelationCode"        -- 與借款人關係 VARCHAR2 2 
      , "LiaisonName"         -- 聯絡人姓名 NVARCHAR2 100 
      , "Rmk"                 -- 備註 NVARCHAR2 40 
      , "StopReason"          -- 停用原因 NVARCHAR2 40 
      , "Enable"              -- 啟用記號 VARCHAR2 1 
      , "CreateDate"          -- 建檔日期時間 DATE  
      , "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 
      , "LastUpdate"          -- 最後更新日期時間 DATE  
      , "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 
    )
    -- 2023-05-10 Wei 新增 相同電話種類相同號碼只取一筆寫入
    WITH TC AS (
      SELECT DISTINCT
             "CUSTID"
           , "TelTypeCode"
           , "TelArea"
           , "TelNo"
           , "TelExt"
           , "CUSTNA4"
      FROM "TempCustTelNo"
    )
    SELECT SYS_GUID()                     AS "TelNoUKey"           -- 電話識別碼 VARCHAR2 32 
         , CM."CustUKey"                  AS "CustUKey"            -- 客戶識別碼 VARCHAR2 32 
         , TC."TelTypeCode"               AS "TelTypeCode"         -- 電話種類 VARCHAR2 2 
         , TC."TelArea"                   AS "TelArea"             -- 電話區碼 VARCHAR2 5 
         , TC."TelNo"                     AS "TelNo"               -- 電話號碼 VARCHAR2 10
         , TC."TelExt"                    AS "TelExt"              -- 分機號碼 VARCHAR2 6 
         , '01'                           AS "TelChgRsnCode"       -- 異動原因 VARCHAR2 2 
         , CASE
             WHEN TRIM(TC."CUSTNA4") = TRIM(CM."CustName")
             THEN '00'
           ELSE '99' END                  AS "RelationCode"        -- 與借款人關係 VARCHAR2 2 
         , TC."CUSTNA4"                   AS "LiaisonName"         -- 聯絡人姓名 NVARCHAR2 100 
         , u''                            AS "Rmk"                 -- 備註 NVARCHAR2 40 
         , u''                            AS "StopReason"          -- 停用原因 NVARCHAR2 40 
         , 'Y'                            AS "Enable"              -- 啟用記號 VARCHAR2 1 
         , JOB_START_TIME                 AS "CreateDate"          -- 建檔日期時間 DATE  
         , '999999'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 
         , JOB_START_TIME                 AS "LastUpdate"          -- 最後更新日期時間 DATE  
         , '999999'                       AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 
    FROM "CustMain" CM
    LEFT JOIN TC ON TC."CUSTID" = CM."CustId"
    WHERE NVL(TC."TelNo",' ') <> ' '
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
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_CustTelNo_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;





/
