--------------------------------------------------------
--  DDL for Procedure Usp_Tf_PfRewardMedia_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_Tf_PfRewardMedia_Ins" 
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
    EXECUTE IMMEDIATE 'ALTER TABLE "PfRewardMedia" DISABLE PRIMARY KEY CASCADE';
    EXECUTE IMMEDIATE 'TRUNCATE TABLE "PfRewardMedia" DROP STORAGE';
    EXECUTE IMMEDIATE 'ALTER TABLE "PfRewardMedia" ENABLE PRIMARY KEY';

    -- 寫入資料
    INSERT INTO "PfRewardMedia" (
          "LogNo"
        , "BonusDate" -- 獎金發放日 DecimalD 8 
        , "PerfDate" -- 業績日期 DecimalD 8 
        , "CustNo" -- 戶號 DECIMAL 7 
        , "FacmNo" -- 額度編號 DECIMAL 3 
        , "BormNo" -- 撥款序號 DECIMAL 3 
        , "BonusType" -- 獎金類別 DECIMAL 1 
        , "EmployeeNo" -- 獎金發放員工編號 VARCHAR2 6 
        , "ProdCode" -- 商品代碼 VARCHAR2 5 
        , "PieceCode" -- 計件代碼 VARCHAR2 1 
        , "Bonus" -- 原始獎金 DECIMAL 14 2
        , "AdjustBonus" -- 發放獎金 DECIMAL 14 2
        , "AdjustBonusDate" -- 調整獎金日期 DECIMALD 8 
        , "WorkMonth" -- 工作月 DECIMAL 6 
        , "WorkSeason" -- 工作季 DECIMAL 5 
        , "Remark" -- 備註 NVARCHAR2 50 
        , "MediaFg" -- 產出媒體檔記號 DECIMAL 1 
        , "MediaDate" -- 產出媒體檔日期 DECIMALD 8 
        , "ManualFg" -- 人工新增記號 DECIMAL 1 
        , "CreateDate" -- 建檔日期時間 DATE  
        , "CreateEmpNo" -- 建檔人員 VARCHAR2 6 
        , "LastUpdate" -- 最後更新日期時間 DATE  
        , "LastUpdateEmpNo" -- 最後更新人員 VARCHAR2 6 
        , "BranchNo" -- 單位別 VARCHAR2 4 
    )
    SELECT "PfRewardMedia_SEQ".nextval         AS "LogNo"
         , Q.PRZCMD                            AS "BonusDate" -- 獎金發放日 DecimalD 8 
         , Q.LMSLLD                            AS "PerfDate" -- 業績日期 DecimalD 8 
         , Q.LMSACN                            AS "CustNo" -- 戶號 DECIMAL 7 
         , Q.LMSAPN                            AS "FacmNo" -- 額度編號 DECIMAL 3 
         , Q.LMSASQ                            AS "BormNo" -- 撥款序號 DECIMAL 3 
         -- 寄件者： 陳瀅如 經理 <juliechen@skl.com.tw>
         -- 2023年9月7日 下午3:15
         -- 主旨： FW: 業績獎金媒體檔 轉換問題
         -- 珮琪回覆如下
         -- 原先1~6的獎金類別就是我們整理後提供的，所以智偉的問題應該只有0跟8要怎麼處理，
         -- 類別0的有44筆，類別8的有3筆，判斷可能為早期使用者新增的發放資料，建議可轉成99其他。
         , CASE
             WHEN Q.PRZTYP IN (1,2,3,4,5,6)
             THEN Q.PRZTYP
             WHEN Q.PRZTYP IN (0,8)
             THEN 99
           ELSE RAISE_APPLICATION_ERROR(-20001, '不正確的PRZTYP值: ' || Q.PRZTYP)
           END                                 AS "BonusType" -- 獎金類別 DECIMAL 1 
         , Q.CUSEM3                            AS "EmployeeNo" -- 獎金發放員工編號 VARCHAR2 6 
         , ''                                  AS "ProdCode" -- 商品代碼 VARCHAR2 5 
         , ''                                  AS "PieceCode" -- 計件代碼 VARCHAR2 1 
         , Q.PRZCMT                            AS "Bonus" -- 原始獎金 DECIMAL 14 2
         , Q.PRZCMT                            AS "AdjustBonus" -- 發放獎金 DECIMAL 14 2
         , Q.PRZCMD                            AS "AdjustBonusDate" -- 調整獎金日期 DECIMALD 8 
         , 0                                   AS "WorkMonth" -- 工作月 DECIMAL 6 
         , LPAD(Q.PRZYER,4,'0') || LPAD(Q.PRZSEN,2,'0') AS "WorkSeason" -- 工作季 DECIMAL 5 
         , ''                                  AS "Remark" -- 備註 NVARCHAR2 50 
         , Q.PRZCNT                            AS "MediaFg" -- 產出媒體檔記號 DECIMAL 1 
         , Q.PRZDAT                            AS "MediaDate" -- 產出媒體檔日期 DECIMALD 8 
         , 0                                   AS "ManualFg" -- 人工新增記號 DECIMAL 1 
         , JOB_START_TIME                      AS "CreateDate" -- 建檔日期時間 DATE  
         , '999999'                            AS "CreateEmpNo" -- 建檔人員 VARCHAR2 6 
         , JOB_START_TIME                      AS "LastUpdate" -- 最後更新日期時間 DATE  
         , '999999'                            AS "LastUpdateEmpNo" -- 最後更新人員 VARCHAR2 6 
         , LPAD(Q.CUSBRH,4,'0')                AS "BranchNo" -- 單位別 VARCHAR2 4 
    FROM DAT_LA$QTAP Q
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
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_PfRewardMedia_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;

/
