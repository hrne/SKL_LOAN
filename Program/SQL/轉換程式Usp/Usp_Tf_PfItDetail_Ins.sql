--------------------------------------------------------
--  DDL for Procedure Usp_Tf_PfItDetail_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_Tf_PfItDetail_Ins" 
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
    EXECUTE IMMEDIATE 'ALTER TABLE "PfItDetail" DISABLE PRIMARY KEY CASCADE';
    EXECUTE IMMEDIATE 'TRUNCATE TABLE "PfItDetail" DROP STORAGE';
    EXECUTE IMMEDIATE 'ALTER TABLE "PfItDetail" ENABLE PRIMARY KEY';

    -- 寫入資料
    INSERT INTO "PfItDetail" (
        "LogNo"
      , "PerfDate"            -- 業績日期 DecimalD 8 0
      , "CustNo"              -- 戶號 DECIMAL 7 0
      , "FacmNo"              -- 額度編號 DECIMAL 3 0
      , "BormNo"              -- 撥款序號 DECIMAL 3 0
      , "RepayType"           -- 還款類別 DECIMAL 1 0
      , "DrawdownDate"        -- 撥款日 DecimalD 8 0
      , "ProdCode"            -- 商品代碼 VARCHAR2 5 0
      , "PieceCode"           -- 計件代碼 VARCHAR2 1 0
      , "CntingCode"          -- 是否計件 VARCHAR2 1 0
      , "DrawdownAmt"         -- 撥款金額 DECIMAL 16 2
      , "UnitCode"            -- 單位代號 VARCHAR2 6 0
      , "DistCode"            -- 區部代號 VARCHAR2 6 0
      , "DeptCode"            -- 部室代號 VARCHAR2 6 0
      , "Introducer"          -- 介紹人 NVARCHAR2 8 0
      , "UnitManager"         -- 處經理 NVARCHAR2 8 0
      , "DistManager"         -- 區經理 NVARCHAR2 8 0
      , "DeptManager"         -- 部經理 NVARCHAR2 8 0
      , "PerfCnt"             -- 件數 DECIMAL 5 1
      , "PerfEqAmt"           -- 換算業績 DECIMAL 16 2
      , "PerfReward"          -- 業務報酬 DECIMAL 16 2
      , "PerfAmt"             -- 業績金額 DECIMAL 16 2
      , "WorkMonth"           -- 工作月 DECIMAL 6 0
      , "WorkSeason"          -- 工作季 DECIMAL 5 0
      , "RewardDate"          -- 換算業績、業務報酬發放日 DECIMAL 8 0
      , "MediaDate"           -- 產出媒體日期 DECIMAL 8 0
      , "MediaFg"             -- 產出媒體檔記號 DECIMAL 1 0
      , "AdjRange"            -- 調整記號 DECIMAL 1 0:無調整 1:調整本月 2:調整本月及季累計
      , "CreateDate"          -- 建檔日期時間 DATE 8 0
      , "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 0
      , "LastUpdate"          -- 最後更新日期時間 DATE 8 0
      , "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 0
    )
    SELECT "PfItDetail_SEQ".nextval       AS "LogNo"
         , S1."LMSLLD"                    AS "PerfDate"            -- 業績日期 DecimalD 8 0
         , S1."LMSACN"                    AS "CustNo"              -- 戶號 DECIMAL 7 0
         , S1."LMSAPN"                    AS "FacmNo"              -- 額度編號 DECIMAL 3 0
         , S1."LMSASQ"                    AS "BormNo"              -- 撥款序號 DECIMAL 3 0
          -- 轉換資料固定擺0:撥款
        , 0                              AS "RepayType"           -- 還款類別 DECIMAL 1 0
        , S1."LMSLLD"                    AS "DrawdownDate"        -- 撥款日 DecimalD 8 0
        , RPAD(FAC."ProdNo",2,' ')       AS "ProdCode"            -- 商品代碼 VARCHAR2 5 0
        , S1."CASCDE"                    AS "PieceCode"           -- 計件代碼 VARCHAR2 1 0
        , ''                             AS "CntingCode"          -- 是否計件 VARCHAR2 1 0
        , S1."LMSFLA"                    AS "DrawdownAmt"         -- 撥款金額 DECIMAL 16 2
        , S1."BCMCOD"                    AS "UnitCode"            -- 單位代號 VARCHAR2 6 0
        , S1."UNTBRN"                    AS "DistCode"            -- 區部代號 VARCHAR2 6 0
        , S1."BCMDPT"                    AS "DeptCode"            -- 部室代號 VARCHAR2 6 0
        , S3."CUSEMP"                    AS "Introducer"          -- 介紹人 NVARCHAR2 8 0
        , S4."CUSEMP"                    AS "UnitManager"         -- 處經理 NVARCHAR2 8 0
        , S5."DistManager"               AS "DistManager"         -- 區經理 NVARCHAR2 8 0
        , S5."DeptManager"               AS "DeptManager"         -- 部經理 NVARCHAR2 8 0
        , 1                              AS "PerfCnt"             -- 件數 DECIMAL 5 1
        , NVL(S1."YAG3LV",0)             AS "PerfEqAmt"           -- 換算業績 DECIMAL 16 2
        , NVL(S1."PAY3LV",0)             AS "PerfReward"          -- 業務報酬 DECIMAL 16 2
        , 0                              AS "PerfAmt"             -- 業績金額 DECIMAL 16 2
        , NVL(S2."YGYYMM",0)             AS "WorkMonth"           -- 工作月 DECIMAL 6 0
        , CASE
             WHEN NVL(S2."YGYYMM",0) = 0 THEN 0
             WHEN SUBSTR(TO_CHAR(S2."YGYYMM"),-2) <= '03' THEN TO_NUMBER(SUBSTR(TO_CHAR(S2."YGYYMM"),0,4) || '1')
             WHEN SUBSTR(TO_CHAR(S2."YGYYMM"),-2) <= '06' THEN TO_NUMBER(SUBSTR(TO_CHAR(S2."YGYYMM"),0,4) || '2')
             WHEN SUBSTR(TO_CHAR(S2."YGYYMM"),-2) <= '09' THEN TO_NUMBER(SUBSTR(TO_CHAR(S2."YGYYMM"),0,4) || '3')
           ELSE TO_NUMBER(SUBSTR(TO_CHAR(S2."YGYYMM"),0,4) || '4') END
                                          AS "WorkSeason"          -- 工作季 DECIMAL 5 0
         , 0                              AS "RewardDate"          -- 換算業績、業務報酬發放日 DECIMAL 8 0
         , 0                              AS "MediaDate"           -- 產出媒體日期 DECIMAL 8 0
         , 0                              AS "MediaFg"             -- 產出媒體檔記號 DECIMAL 1 0
         , 0                              AS "AdjRange"            -- 調整記號 DECIMAL 1 0:無調整 1:調整本月 2:調整本月及季累計
         , JOB_START_TIME                 AS "CreateDate"          -- 建檔日期時間 DATE 8 0
         , '999999'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 0
         , JOB_START_TIME                 AS "LastUpdate"          -- 最後更新日期時間 DATE 8 0
         , '999999'                       AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 0
    FROM ( SELECT A1."LMSLLD"
                , A1."LMSACN"
                , A1."LMSAPN"
                , A1."LMSASQ"
                , A1."CASCDE"
                , A1."LMSFLA"
                , A1."BCMCOD"
                , A1."UNTBRN"
                , A1."BCMDPT"
                , QQ."CUSEMP"
                , QQ."ID1X"
                , QQ."ID7X"
                , QQ."YAG3LV"
                , QQ."PAY3LV"
                , ROW_NUMBER() OVER (PARTITION BY A1."LMSLLD"
                                                , A1."LMSACN"
                                                , A1."LMSAPN"
                                                , A1."LMSASQ"
                                      ORDER BY "LMSFLA") AS "Seq"
            FROM "LN$AA1P" A1
            LEFT JOIN "TmpQQQP" QQ ON QQ."LMSACN" = A1."LMSACN"
                                  AND QQ."LMSAPN" = A1."LMSAPN"
                                  AND QQ."CASCDE" = A1."CASCDE"
            WHERE NVL("LMSLLD",0) >= 20210101
              AND NVL("LMSLLD",0) < 29101231) S1
    -- 計算累積撥款金額 (合計到額度層 ??? )
    LEFT JOIN "FacMain" FAC ON FAC."CustNo" = S1."LMSACN"
                           AND FAC."FacmNo" = S1."LMSAPN"
    LEFT JOIN "TB$WKMP" S2 ON S2."DATES" <= S1."LMSLLD"
                          AND S2."DATEE" >= S1."LMSLLD"
    -- AS400員工檔
    LEFT JOIN "LN$DTYP" S3 ON CASE -- 介紹人 
                                WHEN S1.ID1X = NULL
                                     AND S3.CUSEMP = S1.CUSEMP -- 2022-05-13 智偉修改:ID1X NULL 時用CUSEMP補上
                                THEN 1
                                WHEN S3.CUSID1 = S1.ID1X
                                THEN 1
                              ELSE 0 END
                              = 1 
    LEFT JOIN "LN$DTYP" S4 ON S4.CUSID1 = S1.ID7X -- 處經理
    LEFT JOIN "CdBcm" S5 ON S5."UnitCode" = S1."BCMCOD"
    WHERE S1."Seq" = 1
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
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_PfItDetail_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;





/
