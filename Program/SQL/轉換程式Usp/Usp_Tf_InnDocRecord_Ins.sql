create or replace NONEDITIONABLE PROCEDURE "Usp_Tf_InnDocRecord_Ins" 
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
    EXECUTE IMMEDIATE 'ALTER TABLE "InnDocRecord" DISABLE PRIMARY KEY CASCADE';
    EXECUTE IMMEDIATE 'TRUNCATE TABLE "InnDocRecord" DROP STORAGE';
    EXECUTE IMMEDIATE 'ALTER TABLE "InnDocRecord" ENABLE PRIMARY KEY';

    -- 寫入資料
    INSERT INTO "InnDocRecord"
    SELECT "LN$DOCP"."LMSACN"             AS "CustNo"              -- 借款人戶號 DECIMAL 7 0
          ,"LN$DOCP"."LMSAPN"             AS "FacmNo"              -- 額度號碼 DECIMAL 3 0
          ,LPAD(
           ROW_NUMBER()
           OVER (
            PARTITION BY "LN$DOCP"."LMSACN"
                        ,"LN$DOCP"."LMSAPN"
            ORDER BY "LN$DOCP"."DOCLDT"
           ),3,'0')                       AS "ApplSeq"             -- 申請序號 VARCHAR2 3 0
          ,''                             AS "TitaActFg"           -- 登放記號 VARCHAR2 1 0
          ,CASE
             WHEN "LN$DOCP"."DOCBDT" != 0
             THEN '2'
           ELSE '1' END                   AS "ApplCode"            -- 申請或歸還 VARCHAR2 1 0
          ,NVL(T."CUSEMP","CdEmp"."EmployeeNo")
                                          AS "ApplEmpNo"           -- 借閱人 VARCHAR2 6 0
          ,''                             AS "KeeperEmpNo"         -- 管理人 VARCHAR2 6 0
          ,CASE LPAD("LN$DOCP"."DOCPUR",2,'0')
             WHEN '01'
             THEN '01'
             WHEN '02'
             THEN '02'
             WHEN '03'
             THEN '03'
             WHEN '04'
             THEN '04'
             WHEN '05'
             THEN '05'
             WHEN '06'
             THEN '06'
             WHEN '07'
             THEN '07'
             WHEN '08'
             THEN '08'
           ELSE '08' END                  AS "UsageCode"           -- 用途 VARCHAR2 2 0
          ,''                             AS "CopyCode"            -- 正本/影本 VARCHAR2 1 0
          ,"LN$DOCP"."DOCLDT"             AS "ApplDate"            -- 借閱日期 DecimalD 8 0
          ,"LN$DOCP"."DOCBDT"             AS "ReturnDate"          -- 歸還日期 DecimalD 8 0
          ,''                             AS "ReturnEmpNo"         -- 歸還人 VARCHAR2 6 0
          ,"LN$DOCP"."NGRRMK60"           AS "Remark"              -- 備註 NVARCHAR2 60 0
          ,'4'                            AS "ApplObj"             -- 借閱項目 VARCHAR2 1 0
          ,0                              AS "TitaEntDy"           -- 登錄日期 DecimalD 8 0
          ,''                             AS "TitaTlrNo"           -- 登錄經辦 VARCHAR2 6 0
          ,0                              AS "TitaTxtNo"           -- 登錄交易序號 DECIMAL 8 0
          ,'{"DOCEMN":"'
          || TRIM("LN$DOCP"."DOCEMN")
          ||'"}'                           AS "JsonFields"          -- JsonFields
          ,JOB_START_TIME                 AS "CreateDate"          -- 建檔日期時間 DATE  
          ,'999999'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 
          ,JOB_START_TIME                 AS "LastUpdate"          -- 最後更新日期時間 DATE  
          ,'999999'                       AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 
    FROM "LN$DOCP"
    LEFT JOIN "CdEmp" ON TRIM("CdEmp"."Fullname") = TRIM("LN$DOCP"."DOCEMN")
    LEFT JOIN LN$DTYP T on TRIM(TO_SINGLE_BYTE(T.EMPNAM)) = TRIM("LN$DOCP".DOCEMN)
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
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_InnDocRecord_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;