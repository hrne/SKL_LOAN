--------------------------------------------------------
--  DDL for Procedure Usp_Tf_CdBranch_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_Tf_CdBranch_Ins" 
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
    EXECUTE IMMEDIATE 'ALTER TABLE "CdBranch" DISABLE PRIMARY KEY CASCADE';
    EXECUTE IMMEDIATE 'TRUNCATE TABLE "CdBranch" DROP STORAGE';
    EXECUTE IMMEDIATE 'ALTER TABLE "CdBranch" ENABLE PRIMARY KEY';

    -- 寫入資料
    INSERT INTO "CdBranch" (
        "BranchNo"            -- 單位別 VARCHAR2 4 
      , "AcBranchNo"            -- 單位別 VARCHAR2 4 
      , "CRH"                 -- 總分處 VARCHAR2 2 
      , "BranchStatusCode"    -- 單位控制碼 VARCHAR2 1 
      , "BranchShort"         -- 單位簡稱 NVARCHAR2 14 
      , "BranchItem"          -- 單位全名 NVARCHAR2 40 
      , "BranchAddress1"      -- 單位住址1 NVARCHAR2 30 
      , "BranchAddress2"      -- 單位住址2 NVARCHAR2 30 
      , "Zip3"                -- 郵遞區號前三碼 VARCHAR2 3 
      , "Zip2"                -- 郵遞區號後兩碼 VARCHAR2 2 
      , "Owner"               -- 負責人 NVARCHAR2 14 
      , "BusinessID"          -- 營利統一編號 VARCHAR2 10 
      , "RSOCode"             -- 稽徵機關代號 VARCHAR2 3 
      , "MediaUnitCode"       -- 媒體單位代號 VARCHAR2 4 
      , "CIFKey"              -- CIF KEY VARCHAR2 6 
      , "LastestCustNo"       -- 最終戶號 VARCHAR2 7 
      , "Group1"              -- 課組別1 NVARCHAR2 10 
      , "Group2"              -- 課組別2 NVARCHAR2 10 
      , "Group3"              -- 課組別3 NVARCHAR2 10 
      , "Group4"              -- 課組別4 NVARCHAR2 10 
      , "Group5"              -- 課組別5 NVARCHAR2 10 
      , "Group6"              -- 課組別6 NVARCHAR2 10 
      , "Group7"              -- 課組別7 NVARCHAR2 10 
      , "Group8"              -- 課組別8 NVARCHAR2 10 
      , "Group9"              -- 課組別9 NVARCHAR2 10 
      , "Group10"             -- 課組別10 NVARCHAR2 10 
      , "CreateDate"          -- 建檔日期時間 DATE 0 
      , "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 
      , "LastUpdate"          -- 最後更新日期時間 DATE 0 
      , "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 
    )
    SELECT LPAD("TB$BRHP"."CUSBRH",4,0)   AS "BranchNo"            -- 單位別 VARCHAR2 4 
          ,LPAD("TB$BRHP"."CUSBRH",4,0)   AS "AcBranchNo"            -- 單位別 VARCHAR2 4 
          ,"TB$BRHP"."BRHCRH"             AS "CRH"                 -- 總分處 VARCHAR2 2 
          ,"TB$BRHP"."BRHSTS"             AS "BranchStatusCode"    -- 單位控制碼 VARCHAR2 1 
          ,"TB$BRHP"."BRHNAM"             AS "BranchShort"         -- 單位簡稱 NVARCHAR2 14 
          ,"TB$BRHP"."BRHANM"             AS "BranchItem"          -- 單位全名 NVARCHAR2 40 
          ,"TB$BRHP"."BRHAR1"             AS "BranchAddress1"      -- 單位住址1 NVARCHAR2 30 
          ,"TB$BRHP"."BRHAR2"             AS "BranchAddress2"      -- 單位住址2 NVARCHAR2 30 
          ,DECODE(LENGTH(TRIM("TB$BRHP"."BRHARN")),3,SUBSTR("TB$BRHP"."BRHARN",0,3),
                                                   4,SUBSTR("TB$BRHP"."BRHARN",0,3),
                                                   5,SUBSTR("TB$BRHP"."BRHARN",0,3),
                  ' ')                    AS "Zip3"                -- 郵遞區號前三碼 VARCHAR2 3 
          ,DECODE(LENGTH(TRIM("TB$BRHP"."BRHARN")),5,SUBSTR("TB$BRHP"."BRHARN",3,2),
                  ' ')                    AS "Zip2"                -- 郵遞區號後兩碼 VARCHAR2 2 
          ,"TB$BRHP"."BRHMAN"             AS "Owner"               -- 負責人 NVARCHAR2 14 
          ,"TB$BRHP"."BRHBTN"             AS "BusinessID"          -- 營利統一編號 VARCHAR2 10 
          ,"TB$BRHP"."BRHTNO"             AS "RSOCode"             -- 稽徵機關代號 VARCHAR2 3 
          ,"TB$BRHP"."BRHTNN"             AS "MediaUnitCode"       -- 媒體單位代號 VARCHAR2 4 
          ,"TB$BRHP"."CUSCIF"             AS "CIFKey"              -- CIF KEY VARCHAR2 6 
          ,"TB$BRHP"."LMSLCN"             AS "LastestCustNo"       -- 最終戶號 VARCHAR2 7 
          ,u''                            AS "Group1"              -- 課組別1 NVARCHAR2 10 
          ,u''                            AS "Group2"              -- 課組別2 NVARCHAR2 10 
          ,u''                            AS "Group3"              -- 課組別3 NVARCHAR2 10 
          ,u''                            AS "Group4"              -- 課組別4 NVARCHAR2 10 
          ,u''                            AS "Group5"              -- 課組別5 NVARCHAR2 10 
          ,u''                            AS "Group6"              -- 課組別6 NVARCHAR2 10 
          ,u''                            AS "Group7"              -- 課組別7 NVARCHAR2 10 
          ,u''                            AS "Group8"              -- 課組別8 NVARCHAR2 10 
          ,u''                            AS "Group9"              -- 課組別9 NVARCHAR2 10 
          ,u''                            AS "Group10"             -- 課組別10 NVARCHAR2 10 
          ,JOB_START_TIME                 AS "CreateDate"          -- 建檔日期時間 DATE 0 
          ,'999999'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 
          ,JOB_START_TIME                 AS "LastUpdate"          -- 最後更新日期時間 DATE 0 
          ,'999999'                       AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 
    FROM "TB$BRHP"
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
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_CdBranch_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;



/
