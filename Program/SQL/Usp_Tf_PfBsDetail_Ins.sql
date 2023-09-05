--------------------------------------------------------
--  DDL for Procedure Usp_Tf_PfBsDetail_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_Tf_PfBsDetail_Ins" 
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
    EXECUTE IMMEDIATE 'ALTER TABLE "PfBsDetail" DISABLE PRIMARY KEY CASCADE';
    EXECUTE IMMEDIATE 'TRUNCATE TABLE "PfBsDetail" DROP STORAGE';
    EXECUTE IMMEDIATE 'ALTER TABLE "PfBsDetail" ENABLE PRIMARY KEY';

    -- 寫入資料
    INSERT INTO "PfBsDetail" (
        "LogNo"
      , "PerfDate"            -- 業績日期 DecimalD 8 0
      , "CustNo"              -- 戶號 DECIMAL 7 0
      , "FacmNo"              -- 額度編號 DECIMAL 3 0
      , "BormNo"              -- 撥款序號 DECIMAL 3 0
      , "RepayType"           -- 還款類別 DECIMAL 1 0
      , "BsOfficer"           -- 房貸專員 VARCHAR2 6 0
      , "DeptCode"            -- 部室代號 VARCHAR2 6 0
      , "DrawdownDate"        -- 撥款日 DecimalD 8 0
      , "ProdCode"            -- 商品代碼 VARCHAR2 5 0
      , "PieceCode"           -- 計件代碼 VARCHAR2 1 0
      , "DrawdownAmt"         -- 撥款金額 DECIMAL 16 2
      , "PerfCnt"             -- 件數 DECIMAL 2 1
      , "PerfAmt"             -- 業績金額 DECIMAL 16 2
      , "AdjPerfCnt"          -- 調整加減件數 DECIMAL 5 1 by eric 2021.11.4
      , "AdjPerfAmt"          -- 調整加減業績金額 DECIMAL 16 2 by eric 2021.11.4
      , "WorkMonth"           -- 工作月 DECIMAL 6 0
      , "WorkSeason"          -- 工作季 DECIMAL 5 0
      , "CreateDate"          -- 建檔日期時間 DATE 8 0
      , "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 0
      , "LastUpdate"          -- 最後更新日期時間 DATE 8 0
      , "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 0
    )
    SELECT "PfBsDetail_SEQ".nextval       AS "LogNo"
         , S1."LMSLLD"                    AS "PerfDate"            -- 業績日期 DecimalD 8 0
         , S1."LMSACN"                    AS "CustNo"              -- 戶號 DECIMAL 7 0
         , S1."LMSAPN"                    AS "FacmNo"              -- 額度編號 DECIMAL 3 0
         , S1."LMSASQ"                    AS "BormNo"              -- 撥款序號 DECIMAL 3 0
         , 0                              AS "RepayType"           -- 還款類別 DECIMAL 1 0
         , S1."EMPCOD"                    AS "BsOfficer"           -- 房貸專員 VARCHAR2 6 0
         , S1."BCMDPT"                    AS "DeptCode"            -- 部室代號 VARCHAR2 6 0
         , S1."LMSLLD"                    AS "DrawdownDate"        -- 撥款日 DecimalD 8 0
         , RPAD(FAC."ProdNo",2,' ')       AS "ProdCode"            -- 商品代碼 VARCHAR2 5 0
         , S1."CASCDE"                    AS "PieceCode"           -- 計件代碼 VARCHAR2 1 0
         , S1.LMSFLA                      AS "DrawdownAmt"         -- 撥款金額 DECIMAL 16 2
         , 0                              AS "PerfCnt"             -- 件數 DECIMAL 2 1
         , S1.LMSFLA                      AS "PerfAmt"             -- 業績金額 DECIMAL 16 2
         , 0                              AS "AdjPerfCnt"          -- 調整加減件數 DECIMAL 5 1 by eric 2021.11.4
         , 0                              AS "AdjPerfAmt"          -- 調整加減業績金額 DECIMAL 16 2 by eric 2021.11.4
         , S1."ADTYMT"                    AS "WorkMonth"           -- 工作月 DECIMAL 6 0
         , CASE
             WHEN SUBSTR(TO_CHAR(S1."ADTYMT"),-2) IN ('01','02','03') THEN TO_NUMBER(SUBSTR(TO_CHAR(S1."ADTYMT"),0,4) || '1')
             WHEN SUBSTR(TO_CHAR(S1."ADTYMT"),-2) IN ('04','05','06') THEN TO_NUMBER(SUBSTR(TO_CHAR(S1."ADTYMT"),0,4) || '2')
             WHEN SUBSTR(TO_CHAR(S1."ADTYMT"),-2) IN ('07','08','09') THEN TO_NUMBER(SUBSTR(TO_CHAR(S1."ADTYMT"),0,4) || '3')
           ELSE TO_NUMBER(SUBSTR(TO_CHAR(S1."ADTYMT"),0,4) || '4') END
                                          AS "WorkSeason"          -- 工作季 DECIMAL 5 0
         , JOB_START_TIME                 AS "CreateDate"          -- 建檔日期時間 DATE 8 0
         , '999999'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 0
         , JOB_START_TIME                 AS "LastUpdate"          -- 最後更新日期時間 DATE 8 0
         , '999999'                       AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 0
    FROM (SELECT YAC."LMSACN"
               , YAC."LMSAPN"
               , YAC."LMSASQ"
               , YAC."EMPCOD"
               , YAC."LMSLLD"
               , YAC."CASCDE"
               , YAC."LMSFLA"
               , YAC."ADTYMT"
               , YG5."BCMDPT"
               , ROW_NUMBER() OVER (PARTITION BY YAC."LMSACN"
                                               , YAC."LMSAPN"
                                               , YAC."LMSASQ"
                                    ORDER BY YAC."LMSACN"
                                           , YAC."LMSAPN"
                                           , YAC."LMSASQ"
                                           , YAC."ADTYMT" DESC) AS SEQ
          FROM "LN$YACP" YAC
          LEFT JOIN "LN$YG5P" YG5 ON YG5."EMPCOD" = YAC."EMPCOD"
                                 AND YG5."ADTYMT" = YAC."ADTYMT"
         ) S1
    LEFT JOIN "FacMain" FAC ON FAC."CustNo" = S1."LMSACN"
                           AND FAC."FacmNo" = S1."LMSAPN"
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
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_PfBsDetail_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;




/
