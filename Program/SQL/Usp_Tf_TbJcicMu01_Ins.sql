CREATE OR REPLACE PROCEDURE "Usp_Tf_TbJcicMu01_Ins" 
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
    DELETE FROM "TbJcicMu01";

    -- 寫入資料
    INSERT INTO "TbJcicMu01"
    SELECT T.HEADOFFICE_CODE              AS "HeadOfficeCode"      -- 總行代號 VARCHAR2 3
         , T.BRANCH_CODE                  AS "BranchCode"          -- 分行代號 VARCHAR2 4
         , T.DATA_DATE                    AS "DataDate"            -- 資料日期 Decimald 8
         , T.EMP_ID                       AS "EmpId"               -- 員工代號 VARCHAR2 6
         , T.TITLE                        AS "Title"               -- 職稱 VARCHAR2 50
         , T.AUTHQRYTYPE                  AS "AuthQryType"         -- 授權查詢方式 VARCHAR2 1
         , T.QRYUSERID                    AS "QryUserId"           -- 使用者代碼 VARCHAR2 8
         , T.AUTHITEMQUERY                AS "AuthItemQuery"       -- 授權辦理事項-查詢 VARCHAR2 1
         , T.AUTHITEMREVIEW               AS "AuthItemReview"      -- 授權辦理事項-覆核 VARCHAR2 1
         , T.AUTHITEMOTHER                AS "AuthItemOther"       -- 授權辦理事項-其他 VARCHAR2 1
         , T.AUTHSTARTDAY                 AS "AuthStartDay"        -- 授權起日 Decimald 8
         , T.AUTHMGRID_S                  AS "AuthMgrIdS"          -- 起日授權主管員工代號 VARCHAR2 6
         , T.AUTHENDDAY                   AS "AuthEndDay"          -- 授權迄日 Decimald 8
         , T.AUTHMGRID_E                  AS "AuthMgrIdE"          -- 迄日授權主管員工代號 VARCHAR2 6
         , T.EMAILACCOUNT                 AS "EmailAccount"        -- E-mail信箱 VARCHAR2 50
         , T.MODIFYUSERID                 AS "ModifyUserId"        -- 異動人員ID VARCHAR2 10
         , T.JCICEXPORTDATE               AS "OutJcictxtDate"      -- 轉出JCIC文字檔日期 Decimald 8
         , T.LASTUPDATEDATE               AS "CreateDate"          -- 建檔日期時間 DATE 8 0
         , T.EMP_ID                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 0
         , T.LASTUPDATEDATE               AS "LastUpdate"          -- 最後更新日期時間 DATE 8 0
         , T.EMP_ID                       AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 0
    FROM REMIN_TBJCICMU01 T
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
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_TbJcicMu01_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;



/
