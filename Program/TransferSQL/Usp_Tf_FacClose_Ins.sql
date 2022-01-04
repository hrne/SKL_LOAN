--------------------------------------------------------
--  DDL for Procedure Usp_Tf_FacClose_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "Usp_Tf_FacClose_Ins" 
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
    EXECUTE IMMEDIATE 'ALTER TABLE "FacClose" DISABLE PRIMARY KEY CASCADE';
    EXECUTE IMMEDIATE 'TRUNCATE TABLE "FacClose" DROP STORAGE';
    EXECUTE IMMEDIATE 'ALTER TABLE "FacClose" ENABLE PRIMARY KEY';

    -- 寫入資料
    INSERT INTO "FacClose"
    SELECT E."LMSACN"                     AS "CustNo"              -- 戶號 DECIMAL 7 0
          ,ROW_NUMBER() OVER (PARTITION BY E."LMSACN" ORDER BY E."ADTYMD")
          --E."IN$SEQ"
                                          AS "CloseNo"             -- 清償序號 DECIMAL 3 0
          ,E."LMSAPN"                     AS "FacmNo"              -- 額度編號 DECIMAL 3 0
          ,0                              AS "ActFlag"             -- 登放記號 DECIMAL 1 0
          ,'1'                            AS "FunCode"             -- 功能 VARCHAR2 1 0
          ,0                              AS "CarLoan"             -- 車貸 DECIMAL 1 0
          ,E."ADTYMD"                     AS "ApplDate"            -- 申請日期 DecimalD 8 0
          ,0                              AS "CloseDate"           -- 結案日期 DecimalD 8 0
          ,''                             AS "CloseInd"            -- 結案區分 VARCHAR2 1
          ,E."APLPSN"                     AS "CloseReasonCode"     -- 清償原因 VARCHAR2 2 0
          ,E."ASTULA"                     AS "CloseAmt"            -- 還清金額 DECIMAL 16 2
          ,''                             AS "CollectFlag"         -- 是否領取清償證明 VARCHAR2 1
          ,LPAD(E."APLGET",2,'0')         AS "CollectWayCode"      -- 領取方式 VARCHAR2 2 0
          ,0                              AS "ReceiveDate"         -- 領取日期 DecimalD 8 0
          ,E."CUSTL1"                     AS "TelNo1"              -- 連絡電話1 VARCHAR2 15 0
          ,E."CUSTL2"                     AS "TelNo2"              -- 連絡電話2 VARCHAR2 15 0
          ,''                             AS "TelNo3"              -- 連絡電話3 VARCHAR2 15 0
          ,E."TRXIDT"                     AS "EntryDate"            -- 入帳日期 DECIMALD 8 0
          ,''                             AS "AgreeNo"             -- 塗銷同意書編號 VARCHAR2 10 0
          ,0                              AS "DocNo"               -- 公文編號 DECIMALD 7 0
          ,u''                            AS "ClsNo"               -- 銷號欄 NVARCHAR2 18 0
          ,E."NGRRMK40"                   AS "Rmk"                 -- 備註 NVARCHAR2 100 0
          ,0                              AS "ClCode1"
          ,0                              AS "ClCode2"
          ,0                              AS "ClNo"
          ,0                              AS "ReceiveFg"           -- 領取記號 DECIMAL 1
          ,JOB_START_TIME                 AS "CreateDate"          -- 建檔日期時間 DATE 0 0
          ,'999999'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 0
          ,JOB_START_TIME                 AS "LastUpdate"          -- 最後更新日期時間 DATE 0 0
          ,'999999'                       AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 0
    FROM "LN$ENDP" E
    ;

    -- 記錄寫入筆數
    INS_CNT := INS_CNT + sql%rowcount;

    /* 額度下撥款序號 Status = 3 把清償作業檔的入帳日期搬到結案日期 */
    MERGE INTO "FacClose" T1
    USING (SELECT FC."CustNo"
                 ,FC."CloseNo"
                 ,FC."EntryDate"
                 ,SUM(LB."LoanBal") AS "LoanBalTotal"
           FROM "FacClose" FC
           LEFT JOIN "LoanBorMain" LB ON LB."CustNo" = FC."CustNo"
                                     AND LB."FacmNo" = CASE
                                                         WHEN FC."FacmNo" > 0 THEN FC."FacmNo"
                                                       ELSE LB."FacmNo" END
           GROUP BY FC."CustNo"
                   ,FC."CloseNo"
                   ,FC."EntryDate"
    ) S1
    ON (    T1."CustNo"   = S1."CustNo"
        AND T1."CloseNo"  = S1."CloseNo"
        AND T1."EntryDate" = S1."EntryDate"
        AND S1."LoanBalTotal" = 0
       )
    WHEN MATCHED THEN UPDATE SET
    T1."CloseDate" = S1."EntryDate"
    ;

    -- 記錄程式結束時間
    JOB_END_TIME := SYSTIMESTAMP;

    commit;

    -- 例外處理
    Exception
    WHEN OTHERS THEN
    ERROR_MSG := SQLERRM || CHR(13) || CHR(10) || dbms_utility.format_error_backtrace;
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_FacClose_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;


/
