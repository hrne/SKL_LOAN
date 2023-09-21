--------------------------------------------------------
--  DDL for Procedure Usp_Tf_BankRmtf_Ins
--------------------------------------------------------
set define off;

CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_Tf_BankRmtf_Ins" 
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
    EXECUTE IMMEDIATE 'ALTER TABLE "BankRmtf" DISABLE PRIMARY KEY CASCADE'; 
    EXECUTE IMMEDIATE 'TRUNCATE TABLE "BankRmtf" DROP STORAGE'; 
    EXECUTE IMMEDIATE 'ALTER TABLE "BankRmtf" ENABLE PRIMARY KEY'; 
 
    -- 寫入資料 
    INSERT INTO "BankRmtf" ( 
      "AcDate"              -- 會計日 DECIMALD 8 
      ,"BatchNo"             -- 批號 VARCHAR2 6 
      ,"DetailSeq"           -- 明細序號 DECIMAL 6 
      ,"CustNo"              -- 戶號 DECIMAL 7 
      ,"RepayType"           -- 還款類別 VARCHAR2 2 
      ,"RepayAmt"            -- 還款金額 DECIMAL 14 
      ,"DepAcctNo"           -- 存摺帳號 VARCHAR2 14 
      ,"EntryDate"           -- 入帳日期 DECIMALD 8 
      ,"DscptCode"           -- 摘要代碼 VARCHAR2 14 
      ,"VirtualAcctNo"       -- 虛擬帳號 NVARCHAR2 14 
      ,"WithdrawAmt"         -- 提款 DECIMAL 14 
      ,"DepositAmt"          -- 存款 DECIMAL 14 
      ,"Balance"             -- 結餘 DECIMAL 14 
      ,"RemintBank"          -- 匯款銀行代碼 VARCHAR2 7 
      ,"TraderInfo"          -- 交易人資料 NVARCHAR2 20 
      ,"AmlRsp"              -- AML 回應碼 VARCHAR2 1 
      ,"ReconCode"           -- 對帳類別 VARCHAR2 3 
      ,"TitaTlrNo"           -- 經辦 VARCHAR2 6 
      ,"TitaTxtNo"           -- 交易序號 VARCHAR2 8 
      ,"CreateDate"          -- 建檔日期時間 DATE   
      ,"CreateEmpNo"         -- 建檔人員 VARCHAR2 6  
      ,"LastUpdate"          -- 最後更新日期時間 DATE   
      ,"LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6  
      ,"TitaEntdy" -- 2023-04-17 Wei 新增 from 家興,賴桑
      -- 2023-09-20 Wei from SKL IT 佳怡
      ,"CloseReasonCode" -- varchar2(2);
      ,"CloseFacmNo" -- decimal(3, 0) default 0 not null;
      ,"BranchNo" -- varchar2(3);
      ,"Dpssts" -- varchar2(1);
    )
    WITH txEmpData AS (
      SELECT DISTINCT
             TRXDAT
           , TRXMEM
           , TRXNMT
           , TRXIDT
      FROM LA$TRXP
    )
    SELECT CASE WHEN DPSP.TRXDAT = 0 
                THEN DPSP.TRXIDT 
           ELSE DPSP.TRXDAT END           AS "AcDate"              -- 會計日 DECIMALD 8 
          ,'BATX'  
           || LPAD(DENSE_RANK() OVER (PARTITION BY CASE WHEN DPSP.TRXDAT = 0 
                                                        THEN DPSP.TRXIDT 
                                                   ELSE DPSP.TRXDAT END 
                                      ORDER BY DPSP.TRXIDT) 
                   , 2, 0) -- 以 AcDate 分堆, EntryDate 排序做 DENSE_RANK 
                                          AS "BatchNo"             -- 批號 VARCHAR2 6 
          ,ROW_NUMBER() OVER (PARTITION BY CASE WHEN DPSP.TRXDAT = 0 
                                                THEN DPSP.TRXIDT 
                                           ELSE DPSP.TRXDAT END 
                              ORDER BY DPSP.TRXIDT 
                                      ,DPSP.DPSSEQ) -- 以 AcDate 分堆, EntryDate 與 DPSSEQ 排序做 Row_Number() 
                                                    -- 原 AcDate, BatchNo, DetailSeq 直接轉入會有無法滿足 PK 的問題, 因此這兩個欄位新編序號 
                                          AS "DetailSeq"           -- 明細序號 DECIMAL 6 
          ,DPSP.LMSACN                    AS "CustNo"              -- 戶號 DECIMAL 7 
          ,0                              AS "RepayType"           -- 還款類別 VARCHAR2 2 
          ,DPSP.DPSAMT                    AS "RepayAmt"            -- 還款金額 DECIMAL 14 
          ,' '                            AS "DepAcctNo"           -- 存摺帳號 VARCHAR2 14 
          ,DPSP.TRXIDT                    AS "EntryDate"           -- 入帳日期 DECIMALD 8 
          ,DPSP.DPSMEM                    AS "DscptCode"           -- 摘要代碼 VARCHAR2 14 
          ,SPLP.TB$FDS  
           || LPAD(LMSACN, 7, 0)          AS "VirtualAcctNo"       -- 虛擬帳號 NVARCHAR2 14 
          ,0                              AS "WithdrawAmt"         -- 提款 DECIMAL 14 
          ,0                              AS "DepositAmt"          -- 存款 DECIMAL 14 
          ,0                              AS "Balance"             -- 結餘 DECIMAL 14 
          ,CASE
             WHEN DPSP.DPSBN3 IS NOT NULL
             THEN DPSP.DPSBN3
             WHEN DPSP.BDTBN2 IS NOT NULL
             THEN DPSP.BDTBN2
           ELSE ' ' END                   AS "RemintBank"          -- 匯款銀行代碼 VARCHAR2 7 
          ,DPSP.DPSTRA                    AS "TraderInfo"          -- 交易人資料 NVARCHAR2 20 
          ,'0'                            AS "AmlRsp"              -- AML 回應碼 VARCHAR2 1 
          ,DPSP.DPSATC                    AS "ReconCode"           -- 對帳類別 VARCHAR2 3 
          ,NVL(AEM1."EmpNo",NVL(t.TRXMEM,'999999')) AS "TitaTlrNo"           -- 經辦 VARCHAR2 6 
          ,LPAD(DPSP."TRXNMT",8,'0')      AS "TitaTxtNo"           -- 交易序號 VARCHAR2 8 
          ,JOB_START_TIME                 AS "CreateDate"          -- 建檔日期時間 DATE   
          ,NVL(AEM1."EmpNo",NVL(t.TRXMEM,'999999')) AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6  
          ,JOB_START_TIME                 AS "LastUpdate"          -- 最後更新日期時間 DATE   
          ,NVL(AEM1."EmpNo",NVL(t.TRXMEM,'999999')) AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6  
          ,DPSP.TRXDAT                    AS "TitaEntdy"
          -- 2023-09-20 Wei from SKL IT 佳怡
          ,DPSP.APLPSN AS "CloseReasonCode" -- varchar2(2);
          ,DPSP.DPSAPN AS "CloseFacmNo" -- decimal(3, 0) default 0 not null;
          ,DPSP.CUSBRH AS "BranchNo" -- varchar2(3);
          ,DPSP.DPSSTS AS "Dpssts" -- varchar2(1);
    FROM DAT_LA$DPSP DPSP -- 匯款轉帳檔 
    LEFT JOIN TB$SPLP SPLP ON SPLP.TB$FNM = 'DPSATC' -- 特殊代碼檔: 調存摺代號 DPSATC 對應的定義 
                          AND SPLP.TB$FCD = DPSP.DPSATC 
    LEFT JOIN txEmpData t ON t.TRXDAT = DPSP.TRXDAT
                         AND t.TRXNMT = DPSP.TRXNMT
    LEFT JOIN "As400EmpNoMapping" AEM1 ON AEM1."As400TellerNo" = t.TRXMEM
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
END;

/
