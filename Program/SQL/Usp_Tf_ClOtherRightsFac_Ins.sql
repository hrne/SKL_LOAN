CREATE OR REPLACE PROCEDURE "Usp_Tf_ClOtherRightsFac_Ins" 
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
    EXECUTE IMMEDIATE 'ALTER TABLE "ClOtherRightsFac" DISABLE PRIMARY KEY CASCADE';
    EXECUTE IMMEDIATE 'TRUNCATE TABLE "ClOtherRightsFac" DROP STORAGE';
    EXECUTE IMMEDIATE 'ALTER TABLE "ClOtherRightsFac" ENABLE PRIMARY KEY';

    -- 寫入資料
    INSERT INTO "ClOtherRightsFac" (
        "ClCode1"         -- 擔保品代號1 DECIMAL 1
      , "ClCode2"         -- 擔保品代號2 DECIMAL 2
      , "ClNo"            -- 擔保品編號 DECIMAL 7
      , "Seq"             -- 他項權利登記次序 VARCHAR2 8
      , "ApproveNo"       -- 核准號碼 DECIMAL 7
      , "CustNo"          -- 借款人戶號 DECIMAL 7
      , "FacmNo"          -- 額度編號 DECIMAL 3
      , "CreateDate"      -- 建檔日期時間 DATE 
      , "CreateEmpNo"     -- 建檔人員 VARCHAR2 6
      , "LastUpdate"      -- 最後更新日期時間 DATE 
      , "LastUpdateEmpNo" -- 最後更新人員 VARCHAR2 6
    )
    SELECT TFM."ClCode1"             AS "ClCode1"         -- 擔保品代號1 DECIMAL 1
         , TFM."ClCode2"             AS "ClCode2"         -- 擔保品代號2 DECIMAL 2
         , TFM."ClNo"                AS "ClNo"            -- 擔保品編號 DECIMAL 7
         , TFM."Seq"                 AS "Seq"             -- 他項權利登記次序 VARCHAR2 8
         , APLP.APLNUM               AS "ApproveNo"       -- 核准號碼 DECIMAL 7
         , TFM.LMSACN                AS "CustNo"          -- 借款人戶號 DECIMAL 7
         , TFM.LMSAPN                AS "FacmNo"          -- 額度編號 DECIMAL 3
         , JOB_START_TIME            AS "CreateDate"      -- 建檔日期時間 DATE 
         , '999999'                  AS "CreateEmpNo"     -- 建檔人員 VARCHAR2 6
         , JOB_START_TIME            AS "LastUpdate"      -- 最後更新日期時間 DATE 
         , '999999'                  AS "LastUpdateEmpNo" -- 最後更新人員 VARCHAR2 6
    FROM "TfClOtherRightsMap" TFM
    LEFT JOIN LA$APLP APLP ON APLP.LMSACN = TFM.LMSACN
                          AND APLP.LMSAPN = TFM.LMSAPN
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
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_ClOtherRightsFac_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;
/