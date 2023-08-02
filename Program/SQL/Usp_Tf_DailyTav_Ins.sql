--------------------------------------------------------
--  DDL for Procedure Usp_Tf_DailyTav_Ins
--------------------------------------------------------
set define off;

CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_Tf_DailyTav_Ins" 
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
    EXECUTE IMMEDIATE 'ALTER TABLE "DailyTav" DISABLE PRIMARY KEY CASCADE'; 
    EXECUTE IMMEDIATE 'TRUNCATE TABLE "DailyTav" DROP STORAGE'; 
    EXECUTE IMMEDIATE 'ALTER TABLE "DailyTav" ENABLE PRIMARY KEY'; 
 
    -- 寫入資料 
    INSERT INTO "DailyTav" (
        "AcDate"              -- 會計日期 DECIMAL 8   
      , "CustNo"              -- 借款人戶號 DECIMAL 7   
      , "FacmNo"              -- 額度編號 DECIMAL 3  
      , "SelfUseFlag"         -- 額度自用記號 VARCHAR2 1   
      , "TavBal"              -- 暫收款餘額 DECIMAL 16 2 
      , "LatestFlag"          -- 最新記號 VARCHAR2 1  
      , "AcctCode"
      , "CreateDate"          -- 建檔日期時間 DATE   
      , "CreateEmpNo"         -- 建檔人員 VARCHAR2 6  
      , "LastUpdate"          -- 最後更新日期時間 DATE   
      , "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6  
    )
    WITH lastDateData AS ( 
      SELECT MAX(BKPDAT) AS MAX_BKPDAT 
      FROM LADACTP ACTP 
    ) 
    , OrderedFacmNo AS ( 
      SELECT "CustNo" 
           , "FacmNo" 
           , ROW_NUMBER () 
             OVER ( 
                PARTITION BY "CustNo" 
                ORDER BY CASE 
                           -- 0:正常戶 
                           -- 1:展期 
                           -- 2:催收戶 
                           -- 3:結案戶 
                           -- 4:逾期戶(顯示用) 
                           -- 5:催收結案戶 
                           -- 6:呆帳戶 
                           -- 7:部分轉呆戶 
                           -- 8:債權轉讓戶 
                           -- 9:呆帳結案戶 
                           -- 97:預約撥款已刪除 
                           -- 98:預約已撥款 
                           -- 99:預約撥款 
                           WHEN "Status" IN (0,1,2,4,6,7)  
                           THEN "Status" 
                         ELSE 100 + "Status"  
                         END ASC -- 戶況取未結案、非預約優先 
                       , "NextPayIntDate" ASC 
                       , "FacmNo" ASC 
             ) AS "FacmNoSeq" 
      FROM "LoanBorMain" 
    ) 
    , TMP AS ( 
      SELECT ACTP.BKPDAT 
           , ACTP.LMSACN 
           , ACTP.LMSTOA + ACTP.LMSTOH AS LMSTOA 
           , NVL(OFN."FacmNo",1) AS "FacmNo" 
           , CASE 
               WHEN NVL(lastDateData.MAX_BKPDAT,0) != 0 
               THEN 'Y' 
             ELSE 'N' END AS "LatestFlag" 
      FROM LADACTP ACTP 
      LEFT JOIN lastDateData ON lastDateData.MAX_BKPDAT = ACTP.BKPDAT 
      LEFT JOIN OrderedFacmNo OFN ON OFN."CustNo" = ACTP.LMSACN 
                                 AND OFN."FacmNoSeq" = 1 
                                --  AND NVL(lastDateData.MAX_BKPDAT,0) != 0
                                 -- 2022-10-12 Wei 最新一筆才判斷額度號碼 
                                 -- 2023-08-02 Wei from Lai 每一筆都用那個額度號碼
    ) 
    SELECT TMP.BKPDAT                     AS "AcDate"              -- 會計日期 DECIMAL 8   
         , TMP.LMSACN                     AS "CustNo"              -- 借款人戶號 DECIMAL 7   
         , TMP."FacmNo"                   AS "FacmNo"              -- 額度編號 DECIMAL 3   
         , CASE 
             WHEN NVL(LFT."CustNo",0) != 0 
             THEN 'Y' 
           ELSE 'N' END                   AS "SelfUseFlag"         -- 額度自用記號 VARCHAR2 1   
         , TMP.LMSTOA                     AS "TavBal"              -- 暫收款餘額 DECIMAL 16 2 
         , TMP."LatestFlag"               AS "LatestFlag"          -- 最新記號 VARCHAR2 1   
         -- 2022-12-08 Wei 增加 from Lai
         -- 2023-04-20 Wei 修改 from Lai : 暫收款不排除610940,601776
         , 'TAV'                          AS "AcctCode"
         , JOB_START_TIME                 AS "CreateDate"          -- 建檔日期時間 DATE   
         , '999999'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6  
         , JOB_START_TIME                 AS "LastUpdate"          -- 最後更新日期時間 DATE   
         , '999999'                       AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6  
    FROM TMP 
    LEFT JOIN "LoanFacTmp" LFT ON LFT."CustNo" = TMP.LMSACN 
                              AND LFT."FacmNo" = TMP."FacmNo" 
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
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_DailyTav_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace); 
END;

/
