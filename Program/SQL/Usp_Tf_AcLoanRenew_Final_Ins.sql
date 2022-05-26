CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_Tf_AcLoanRenew_Final_Ins" 
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

    -- 尚未被建立在AcLoanRenew的交叉相乘寫入
    INSERT INTO "AcLoanRenew"
    WITH noExistOldData AS (
      SELECT NB.LMSACN
           , NB.LMSAPN
           , NB.LMSASQ
           , NB.NEGNUM
           , NVL(ALR."CustNo",0) AS ALR_CustNo
      FROM LN$NODP NB
      LEFT JOIN "AcLoanRenew" ALR ON ALR."CustNo" = NB.LMSACN
                                 AND ALR."OldFacmNo" = NB.LMSAPN
                                 AND ALR."OldBormNo" = NB.LMSASQ
      WHERE CHGFLG = 'B'
    )
    , noExistNewData AS (
      SELECT NA.LMSACN
           , NA.LMSAPN
           , NA.LMSASQ
           , NA.NEGNUM
           , NVL(ALR."CustNo",0) AS ALR_CustNo
      FROM LN$NODP NA
      LEFT JOIN "AcLoanRenew" ALR ON ALR."CustNo" = NA.LMSACN
                                 AND ALR."NewFacmNo" = NA.LMSAPN
                                 AND ALR."NewBormNo" = NA.LMSASQ
      WHERE CHGFLG = 'A'
    )
    , noExistData AS (
        select O.LMSACN AS "CustNo"
             , N.LMSAPN AS "NewFacmNo"
             , N.LMSASQ AS "NewBormNo"
             , O.LMSAPN AS "OldFacmNo"
             , O.LMSASQ AS "OldBormNo"
             , O.NEGNUM
        from noExistOldData O
           , noExistNewData N
        WHERE O.LMSACN = N.LMSACN
          AND O.NEGNUM = N.NEGNUM
          AND CASE
                WHEN O.ALR_CustNo = 0 AND N.ALR_CustNo = 0
                THEN 0
              ELSE 1 END = 1
    )
    SELECT n."CustNo"
         , n."NewFacmNo"
         , n."NewBormNo"
         , n."OldFacmNo"
         , n."OldBormNo"
         , '2'                AS "RenewCode"
         , 'N'                AS "MainFlag"
         , LBM."DrawdownDate" AS "AcDate"
         , '999999'           AS "CreateEmpNo"
         , JOB_START_TIME     AS "CreateDate"
         , '999999'           AS "LastUpdateEmpNo"
         , JOB_START_TIME     AS "LastUpdate"
         ,'{"NegNo":"'
           || n."NEGNUM" -- 協議件編號
           || '"'
           || '}'             AS "OtherFields"
    FROM noExistData n
    LEFT JOIN "LoanBorMain" LBM ON LBM."CustNo" = n."CustNo"
                               AND LBM."FacmNo" = n."NewFacmNo"
                               AND LBM."BormNo" = n."NewBormNo"
    ;

    MERGE INTO "AcLoanRenew" ALR
    USING (
      SELECT "CustNo"              -- 戶號 DECIMAL 3
           , "NewFacmNo"           -- 新額度編號 DECIMAL 3
           , "NewBormNo"           -- 新撥款序號 DECIMAL 3
           , "OldFacmNo"           -- 舊額度編號 DECIMAL 6
           , "OldBormNo"           -- 舊撥款序號 DECIMAL 6
           -- 2022-01-03 智偉修改:最後統一更新
           -- 主要記號 VARCHAR2 1 (Y:新撥款對到舊撥款最早的一筆 )
           , ROW_NUMBER()
             OVER (
               PARTITION BY "CustNo"
                          , "NewFacmNo"
                          , "NewBormNo"
               ORDER BY "OldFacmNo"
                      , "OldBormNo"
             ) AS "Seq"
      FROM "AcLoanRenew"
    ) N
    ON (
      N."CustNo" = ALR."CustNo"
      AND N."NewFacmNo" = ALR."NewFacmNo"
      AND N."NewBormNo" = ALR."NewBormNo"
      AND N."OldFacmNo" = ALR."OldFacmNo"
      AND N."OldBormNo" = ALR."OldBormNo"
      AND N."Seq" = 1
    )
    WHEN MATCHED THEN UPDATE SET
    "MainFlag" = 'Y' -- 主要記號 VARCHAR2 1 (Y:新撥款對到舊撥款最早的一筆 )
    ;

    -- 記錄程式結束時間
    JOB_END_TIME := SYSTIMESTAMP;

    commit;

    -- 例外處理
    Exception
    WHEN OTHERS THEN
    ERROR_MSG := SQLERRM || CHR(13) || CHR(10) || dbms_utility.format_error_backtrace;
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_AcLoanRenew_Final_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;



/
