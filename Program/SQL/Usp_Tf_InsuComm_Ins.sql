--------------------------------------------------------
--  DDL for Procedure Usp_Tf_InsuComm_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_Tf_InsuComm_Ins" 
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
    EXECUTE IMMEDIATE 'ALTER TABLE "InsuComm" DISABLE PRIMARY KEY CASCADE';
    EXECUTE IMMEDIATE 'TRUNCATE TABLE "InsuComm" DROP STORAGE';
    EXECUTE IMMEDIATE 'ALTER TABLE "InsuComm" ENABLE PRIMARY KEY';

    -- 寫入資料
    INSERT INTO "InsuComm" (
        "InsuYearMonth"    -- 年月份 DECIMAL 6 0
      , "InsuCommSeq"      -- 佣金媒體檔序號 NUMBER(6,0)
      , "ManagerCode"      -- 經紀人代號 DECIMAL 3 0
      , "NowInsuNo"        -- 保單號碼 VARCHAR2 20 0
      , "BatchNo"          -- 批號 VARCHAR2 6 0
      , "InsuType"         -- 險別 VARCHAR2 3 0
      , "InsuSignDate"     -- 簽單日期 DECIMAL 8 0
      , "InsuredName"      -- 被保險人 NVARCHAR2 60 0
      , "InsuredAddr"      -- 被保險人地址 NVARCHAR2 60 0
      , "InsuredTeleph"    -- 被保險人電話 VARCHAR2 20 0
      , "InsuStartDate"    -- 起保日期 DECIMAL 8 0
      , "InsuEndDate"      -- 到期日期 DECIMAL 8 0
      , "InsuCate"         -- 險種 VARCHAR2 4 0
      , "InsuPrem"         -- 保費 DECIMAL 14 0
      , "CommRate"         -- 佣金率 DECIMAL 5 3
      , "Commision"        -- 佣金 DECIMAL 14 0
      , "TotInsuPrem"      -- 合計保費 DECIMAL 14 0
      , "TotComm"          -- 合計佣金 DECIMAL 14 0
      , "RecvSeq"          -- 收件號碼 VARCHAR2 14 0
      , "ChargeDate"       -- 收費日期 DECIMAL 8 0
      , "CommDate"         -- 佣金日期 DECIMAL 8 0
      , "CustNo"           -- 戶號 DECIMAL 7 0
      , "FacmNo"           -- 額度 DECIMAL 3 0
      , "FireOfficer"      -- 火險服務 VARCHAR2 6 0
      , "EmpId"            -- 統一編號 VARCHAR2 10 0
      , "EmpName"          -- 員工姓名 NVARCHAR2 4 0
      , "DueAmt"           -- 應領金額 DECIMAL 14 0
      , "MediaCode"        -- 媒體碼 VARCHAR2 1 0
      , "CreateDate"       -- 建檔日期時間 DATE  
      , "CreateEmpNo"      -- 建檔人員 VARCHAR2 6 
      , "LastUpdate"       -- 最後更新日期時間 DATE  
      , "LastUpdateEmpNo"  -- 最後更新人員 VARCHAR2 6 
    )
    SELECT "InsuYearMonth"                           -- 年月份 DECIMAL 6 0
          ,"Seq"                    AS "InsuCommSeq" -- 佣金媒體檔序號 NUMBER(6,0)
          ,"ManagerCode"                             -- 經紀人代號 DECIMAL 3 0
          ,"NowInsuNo"                               -- 保單號碼 VARCHAR2 20 0
          ,"BatchNo"                                 -- 批號 VARCHAR2 6 0
          ,"InsuType"                                -- 險別 VARCHAR2 3 0
          ,"InsuSignDate"                            -- 簽單日期 DECIMAL 8 0
          ,"InsuredName"                             -- 被保險人 NVARCHAR2 60 0
          ,"InsuredAddr"                             -- 被保險人地址 NVARCHAR2 60 0
          ,"InsuredTeleph"                           -- 被保險人電話 VARCHAR2 20 0
          ,"InsuStartDate"                           -- 起保日期 DECIMAL 8 0
          ,"InsuEndDate"                             -- 到期日期 DECIMAL 8 0
          ,"InsuCate"                                -- 險種 VARCHAR2 4 0
          ,"InsuPrem"                                -- 保費 DECIMAL 14 0
          ,"CommRate"                                -- 佣金率 DECIMAL 5 3
          ,"Commision"                               -- 佣金 DECIMAL 14 0
          ,"TotInsuPrem"                             -- 合計保費 DECIMAL 14 0
          ,"TotComm"                                 -- 合計佣金 DECIMAL 14 0
          ,"RecvSeq"                                 -- 收件號碼 VARCHAR2 14 0
          ,"ChargeDate"                              -- 收費日期 DECIMAL 8 0
          ,"CommDate"                                -- 佣金日期 DECIMAL 8 0
          ,"CustNo"                                  -- 戶號 DECIMAL 7 0
          ,"FacmNo"                                  -- 額度 DECIMAL 3 0
          ,"FireOfficer"                             -- 火險服務 VARCHAR2 6 0
          ,"EmpId"                                   -- 統一編號 VARCHAR2 10 0
          ,"EmpName"                                 -- 員工姓名 NVARCHAR2 4 0
          ,"DueAmt"                                  -- 應領金額 DECIMAL 14 0
          ,"MediaCode"                               -- 媒體碼 VARCHAR2 1 0
          ,JOB_START_TIME   AS "CreateDate"          -- 建檔日期時間 DATE  
          ,'999999'         AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 
          ,JOB_START_TIME   AS "LastUpdate"          -- 最後更新日期時間 DATE  
          ,'999999'         AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 
    FROM (SELECT "LN$CMDP"."ADTYMT"             AS "InsuYearMonth"       -- 年月份 DECIMAL 6 0
                ,TRUNC(TO_NUMBER("LN$CMDP"."CMT01"))
                                                AS "ManagerCode"         -- 經紀人代號 DECIMAL 3 0
                ,TRIM("LN$CMDP"."CMT02")        AS "NowInsuNo"           -- 保單號碼 VARCHAR2 20 0
                ,SUBSTR("LN$CMDP"."CMT03",0,6)  AS "BatchNo"             -- 批號 VARCHAR2 6 0
                ,LPAD(TRUNC(TO_NUMBER("LN$CMDP"."CMT04")),3,'0')
                                                AS "InsuType"            -- 險別 VARCHAR2 3 0
                ,"LN$CMDP"."CMT05"              AS "InsuSignDate"        -- 簽單日期 DECIMAL 8 0
                ,"LN$CMDP"."CMT06"              AS "InsuredName"         -- 被保險人 NVARCHAR2 60 0
                ,"LN$CMDP"."CMT07"              AS "InsuredAddr"         -- 被保險人地址 NVARCHAR2 60 0
                ,"LN$CMDP"."CMT08"              AS "InsuredTeleph"       -- 被保險人電話 VARCHAR2 20 0
                ,"LN$CMDP"."CMT09"              AS "InsuStartDate"       -- 起保日期 DECIMAL 8 0
                ,"LN$CMDP"."CMT10"              AS "InsuEndDate"         -- 到期日期 DECIMAL 8 0
                ,LPAD(TRUNC(TO_NUMBER("LN$CMDP"."CMT11")),4,'0')
                                                AS "InsuCate"            -- 險種 VARCHAR2 4 0
                ,"LN$CMDP"."CMT12"              AS "InsuPrem"            -- 保費 DECIMAL 14 0
                ,"LN$CMDP"."CMT13"              AS "CommRate"            -- 佣金率 DECIMAL 5 3
                ,"LN$CMDP"."CMT14"              AS "Commision"           -- 佣金 DECIMAL 14 0
                ,"LN$CMDP"."CMT15"              AS "TotInsuPrem"         -- 合計保費 DECIMAL 14 0
                ,"LN$CMDP"."CMT16"              AS "TotComm"             -- 合計佣金 DECIMAL 14 0
                ,"LN$CMDP"."CMT17"              AS "RecvSeq"             -- 收件號碼 VARCHAR2 14 0
                ,"LN$CMDP"."CMT18"              AS "ChargeDate"          -- 收費日期 DECIMAL 8 0
                ,"LN$CMDP"."CMT19"              AS "CommDate"            -- 佣金日期 DECIMAL 8 0
                ,"LN$CMDP"."LMSACN"             AS "CustNo"              -- 戶號 DECIMAL 7 0
                ,"LN$CMDP"."LMSAPN"             AS "FacmNo"              -- 額度 DECIMAL 3 0
                ,"LN$CMDP"."CUSEM7"             AS "FireOfficer"         -- 火險服務 VARCHAR2 6 0
                ,"LN$CMDP"."CUSID1"             AS "EmpId"               -- 統一編號 VARCHAR2 10 0
                ,"LN$CMDP"."EMPNAM"             AS "EmpName"             -- 員工姓名 NVARCHAR2 4 0
                ,"LN$CMDP"."CMT20"              AS "DueAmt"              -- 應領金額 DECIMAL 14 0
                ,"LN$CMDP"."PRZCDE"             AS "MediaCode"           -- 媒體碼 VARCHAR2 1 0
                ,ROW_NUMBER() OVER (PARTITION BY "LN$CMDP"."ADTYMT" -- 年月份
                                    ORDER BY "LN$CMDP"."ADTYMT"
                                            ,TRUNC(TO_NUMBER("LN$CMDP"."CMT01")) -- 經紀人代號
                                            ,TRIM("LN$CMDP"."CMT02") -- 保單號碼
                                            ,LPAD(TRUNC(TO_NUMBER("LN$CMDP"."CMT11")),4,'0') -- 險種
                                            ,"LN$CMDP"."LMSACN" -- 戶號
                                            ,"LN$CMDP"."LMSAPN" -- 額度
                                            ,"LN$CMDP"."CMT09" -- 起保日期
                                            ,"LN$CMDP"."CMT10" -- 到期日期
                                            ,"LN$CMDP"."CMT05" -- 簽單日期
                                   ) AS "Seq"
          FROM "LN$CMDP"
         ) S1
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
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_InsuComm_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;




/
