--------------------------------------------------------
--  DDL for Procedure Usp_Tf_CdGseq_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_Tf_CdGseq_Ins" 
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

    DECLARE 
        "TbsDyF" DECIMAL(8); --西元帳務日
    BEGIN

    SELECT "TbsDy" + 19110000
    INTO "TbsDyF"
    FROM "TxBizDate"
    WHERE "DateCode" = 'ONLINE'
    ;

    -- 刪除舊資料
    EXECUTE IMMEDIATE 'ALTER TABLE "CdGseq" DISABLE PRIMARY KEY CASCADE';
    EXECUTE IMMEDIATE 'TRUNCATE TABLE "CdGseq" DROP STORAGE';
    EXECUTE IMMEDIATE 'ALTER TABLE "CdGseq" ENABLE PRIMARY KEY';

    -- 寫入資料
    -- GseqType = L2 (業務作業)
    -- GseqKind = 0001 (戶號)
    INSERT INTO "CdGseq" (
       "GseqDate"             -- 編號日期 DECIMAL 8 
      , "GseqCode"            -- 編號方式 DECIMAL 1 
      , "GseqType"            -- 業務類別 VARCHAR2 2 
      , "GseqKind"            -- 交易種類 VARCHAR2 4 
      , "Offset"              -- 有效值 DECIMAL 8 
      , "SeqNo"               -- 流水號 DECIMAL 8 
      , "CreateDate"          -- 建檔日期時間 DATE 8 
      , "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 
      , "LastUpdate"          -- 最後更新日期時間 DATE 8 
      , "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 
    )
    SELECT 0                              AS "GseqDate"            -- 編號日期 DECIMAL 8 (編號方式為年度編號時,月日為0,月份編號時,日為0,不分時,擺0)
          ,0                              AS "GseqCode"            -- 編號方式 DECIMAL 1 (0:不分 1:年度編號 2:月份編號 3:日編號)
          ,'L2'                           AS "GseqType"            -- 業務類別 VARCHAR2 2 (業務自行編制 例:L2 = 業務作業) 
          ,'0001'                         AS "GseqKind"            -- 交易種類 VARCHAR2 4 (業務自行編制 例:GseqType = L2 AND GseqKind = 0001 為 戶號)
          ,9999999                        AS "Offset"              -- 有效值 DECIMAL 8 (例:有效值=999,流水號為999時,下一個為001)
          -- 2023-05-22 Wei from SKL 新系統要續編 
          ,MAX("LMSACN")                  AS "SeqNo"               -- 流水號 DECIMAL 8 (目前已編到第幾號)
          -- 2021-11-26 智偉修改: 新系統從2200000起編號
      --     ,2200000                        AS "SeqNo"               -- 流水號 DECIMAL 8 (目前已編到第幾號)
          ,JOB_START_TIME                 AS "CreateDate"          -- 建檔日期時間 DATE 8 
          ,'999999'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 
          ,JOB_START_TIME                 AS "LastUpdate"          -- 最後更新日期時間 DATE 8 
          ,'999999'                       AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 
    FROM CU$CUSP
    ;

    -- 記錄寫入筆數
    INS_CNT := INS_CNT + sql%rowcount;

    -- 寫入資料
    -- GseqType = L2 (業務作業)
    -- GseqKind = 0002 (案件申請編號)
    INSERT INTO "CdGseq" (
       "GseqDate"             -- 編號日期 DECIMAL 8 
      , "GseqCode"            -- 編號方式 DECIMAL 1 
      , "GseqType"            -- 業務類別 VARCHAR2 2 
      , "GseqKind"            -- 交易種類 VARCHAR2 4 
      , "Offset"              -- 有效值 DECIMAL 8 
      , "SeqNo"               -- 流水號 DECIMAL 8 
      , "CreateDate"          -- 建檔日期時間 DATE 8 
      , "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 
      , "LastUpdate"          -- 最後更新日期時間 DATE 8 
      , "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 
    )
    SELECT S0."BcYear" * 10000            AS "GseqDate"            -- 編號日期 DECIMAL 8 (編號方式為年度編號時,月日為0,月份編號時,日為0,不分時,擺0)
          ,1                              AS "GseqCode"            -- 編號方式 DECIMAL 1 (0:不分 1:年度編號 2:月份編號 3:日編號)
          ,'L2'                           AS "GseqType"            -- 業務類別 VARCHAR2 2 (業務自行編制 例:L2 = 業務作業) 
          ,'0002'                         AS "GseqKind"            -- 交易種類 VARCHAR2 4 (業務自行編制 例:GseqType = L2 AND GseqKind = 0001 為 戶號)
          ,99999                          AS "Offset"              -- 有效值 DECIMAL 8 (例:有效值=999,流水號為999時,下一個為001)
          ,S0."SeqEnd"                    AS "SeqNo"               -- 流水號 DECIMAL 8 (目前已編到第幾號)
          ,JOB_START_TIME                 AS "CreateDate"          -- 建檔日期時間 DATE 8 
          ,'999999'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 
          ,JOB_START_TIME                 AS "LastUpdate"          -- 最後更新日期時間 DATE 8 
          ,'999999'                       AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 
    FROM (SELECT CASE
                   WHEN TO_NUMBER(SUBSTR("ApplNo",0,2)) + 1911 <= 1994 -- 舊案件編號推出來的年份最早是1995
                   THEN TO_NUMBER(SUBSTR("ApplNo",0,2)) + 2011
                 ELSE TO_NUMBER(SUBSTR("ApplNo",0,2)) + 1911 END 
                                                     AS "BcYear"
                ,MIN(TO_NUMBER(SUBSTR("ApplNo",-5))) AS "SeqStart"
                ,MAX(TO_NUMBER(SUBSTR("ApplNo",-5))) AS "SeqEnd"
          FROM (SELECT LPAD("CASNUM",7,'0') AS "ApplNo"
                FROM "LA$CASP"
               ) CASP
          GROUP BY CASE
                     WHEN TO_NUMBER(SUBSTR("ApplNo",0,2)) + 1911 <= 1994
                     THEN TO_NUMBER(SUBSTR("ApplNo",0,2)) + 2011
                   ELSE TO_NUMBER(SUBSTR("ApplNo",0,2)) + 1911 END 
          ORDER BY CASE
                     WHEN TO_NUMBER(SUBSTR("ApplNo",0,2)) + 1911 <= 1994
                     THEN TO_NUMBER(SUBSTR("ApplNo",0,2)) + 2011
                   ELSE TO_NUMBER(SUBSTR("ApplNo",0,2)) + 1911 END 
         ) S0
    ;

    -- 記錄寫入筆數
    INS_CNT := INS_CNT + sql%rowcount;

    -- 寫入資料
    -- GseqType = L2 (業務作業)
    -- GseqKind = AABB (AA = ClCode1 擔保品代號1,BB = ClCode2 擔保品代號2)
    INSERT INTO "CdGseq" (
       "GseqDate"             -- 編號日期 DECIMAL 8 
      , "GseqCode"            -- 編號方式 DECIMAL 1 
      , "GseqType"            -- 業務類別 VARCHAR2 2 
      , "GseqKind"            -- 交易種類 VARCHAR2 4 
      , "Offset"              -- 有效值 DECIMAL 8 
      , "SeqNo"               -- 流水號 DECIMAL 8 
      , "CreateDate"          -- 建檔日期時間 DATE 8 
      , "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 
      , "LastUpdate"          -- 最後更新日期時間 DATE 8 
      , "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 
    )
    SELECT 0                              AS "GseqDate"            -- 編號日期 DECIMAL 8 (編號方式為年度編號時,月日為0,月份編號時,日為0,不分時,擺0)
          ,0                              AS "GseqCode"            -- 編號方式 DECIMAL 1 (0:不分 1:年度編號 2:月份編號 3:日編號)
          ,'L2'                           AS "GseqType"            -- 業務類別 VARCHAR2 2 (業務自行編制 例:L2 = 業務作業) 
          ,S0."ClCode"                    AS "GseqKind"            -- 交易種類 VARCHAR2 4 (業務自行編制 例:GseqType = L2 AND GseqKind = 0001 為 戶號)
          ,9999999                        AS "Offset"              -- 有效值 DECIMAL 8 (例:有效值=999,流水號為999時,下一個為001)
          ,S0."LastUsedNo"                AS "SeqNo"               -- 流水號 DECIMAL 8 (目前已編到第幾號)
          ,JOB_START_TIME                 AS "CreateDate"          -- 建檔日期時間 DATE 8 
          ,'999999'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 
          ,JOB_START_TIME                 AS "LastUpdate"          -- 最後更新日期時間 DATE 8 
          ,'999999'                       AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 
    FROM (SELECT LPAD("GDRID1",2,'0') || LPAD("GDRID2",2,'0')
                          AS "ClCode"
                ,"GDRLUN" AS "LastUsedNo"          -- 最後使用碼 DECIMAL 7 
          FROM "TB$GDRP"
         ) S0
    ;

    -- 記錄寫入筆數
    INS_CNT := INS_CNT + sql%rowcount;

    -- 寫入資料
    -- GseqType = L2 (業務作業)
    -- GseqKind = 2601 (法務費用檔紀錄號碼)
    INSERT INTO "CdGseq" (
       "GseqDate"             -- 編號日期 DECIMAL 8 
      , "GseqCode"            -- 編號方式 DECIMAL 1 
      , "GseqType"            -- 業務類別 VARCHAR2 2 
      , "GseqKind"            -- 交易種類 VARCHAR2 4 
      , "Offset"              -- 有效值 DECIMAL 8 
      , "SeqNo"               -- 流水號 DECIMAL 8 
      , "CreateDate"          -- 建檔日期時間 DATE 8 
      , "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 
      , "LastUpdate"          -- 最後更新日期時間 DATE 8 
      , "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 
    )
    SELECT 0                              AS "GseqDate"            -- 編號日期 DECIMAL 8 (編號方式為年度編號時,月日為0,月份編號時,日為0,不分時,擺0)
          ,0                              AS "GseqCode"            -- 編號方式 DECIMAL 1 (0:不分 1:年度編號 2:月份編號 3:日編號)
          ,'L2'                           AS "GseqType"            -- 業務類別 VARCHAR2 2 (業務自行編制 例:L2 = 業務作業) 
          ,'2601'                         AS "GseqKind"            -- 交易種類 VARCHAR2 4 (業務自行編制 例:GseqType = L2 AND GseqKind = 0001 為 戶號)
          ,9999999                        AS "Offset"              -- 有效值 DECIMAL 8 (例:有效值=999,流水號為999時,下一個為001)
          ,MAX(RECNUM)                    AS "SeqNo"               -- 流水號 DECIMAL 8 (目前已編到第幾號)
          ,JOB_START_TIME                 AS "CreateDate"          -- 建檔日期時間 DATE 8 
          ,'999999'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 
          ,JOB_START_TIME                 AS "LastUpdate"          -- 最後更新日期時間 DATE 8 
          ,'999999'                       AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 
    FROM LN$LGFP
    ;

    -- 記錄寫入筆數
    INS_CNT := INS_CNT + sql%rowcount;

    -- 記錄程式結束時間
    JOB_END_TIME := SYSTIMESTAMP;

    commit;

    END;

    -- 例外處理
    Exception
    WHEN OTHERS THEN
    ERROR_MSG := SQLERRM || CHR(13) || CHR(10) || dbms_utility.format_error_backtrace;
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_CdGseq_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;



/
