--------------------------------------------------------
--  DDL for Procedure Usp_Tf_ClMain_Upd
--------------------------------------------------------
set define off;

CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_Tf_ClMain_Upd" 
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
 
    -- 更新資料 (不動產擔保品) 
    MERGE INTO "ClMain" T0 
    USING ( 
          WITH CF AS ( 
               SELECT CF."ClCode1" 
                     , CF."ClCode2" 
                     , CF."ClNo" 
                     , SUM(FAC."LineAmt") AS "LineAmt" 
                FROM "ClFac" CF 
                LEFT JOIN (SELECT "CustNo" 
                                , "FacmNo" 
                                , SUM("LineAmt") AS "LineAmt" 
                           FROM "FacMain" 
                           GROUP BY "CustNo" 
                                  , "FacmNo" 
                          ) FAC ON FAC."CustNo" = CF."CustNo" 
                               AND FAC."FacmNo" = CF."FacmNo" 
                GROUP BY CF."ClCode1" 
                       , CF."ClCode2" 
                       , CF."ClNo" 
          ) 
          , CLM AS ( 
               SELECT CLM."ClCode1" 
                    , CLM."ClCode2" 
                    , CLM."ClNo" 
                    , NVL(IMM."SettingStat",0)       AS "SettingStat" 
                    , NVL(CF."LineAmt",0)            AS "LineAmt" 
                    , NVL(IMM."EvaNetWorth",0)       AS "EvaNetWorth" 
                    , NVL(CLM."EvaAmt",0)            AS "EvaAmt" 
                    , CASE 
                        WHEN NVL(CF."LineAmt",0) = 0 
                        THEN 1 -- 額度金額為0 無法計算貸放成數 
                        WHEN NVL(IMM."EvaNetWorth",0) > 0 
                        THEN ROUND(NVL(CF."LineAmt",0) / NVL(IMM."EvaNetWorth",0),8) 
                        WHEN NVL(CLM."EvaAmt",0) > 0 
                        THEN ROUND(NVL(CF."LineAmt",0) / NVL(CLM."EvaAmt",0),8) 
                      ELSE 1 END                     AS "LoanToValue" 
                    , NVL(IMM."SettingAmt",0)        AS "SettingAmt" 
               FROM "ClMain" CLM 
               LEFT JOIN "ClImm" IMM ON IMM."ClCode1" = CLM."ClCode1" 
                                   AND IMM."ClCode2" = CLM."ClCode2" 
                                   AND IMM."ClNo" = CLM."ClNo" 
               LEFT JOIN CF ON CF."ClCode1" = CLM."ClCode1" 
                         AND CF."ClCode2" = CLM."ClCode2" 
                         AND CF."ClNo" = CLM."ClNo" 
               WHERE CLM."ClCode1" IN (1,2) 
          ) 
          SELECT "ClCode1" 
               , "ClCode2" 
               , "ClNo" 
               -- 2021-08-10 智偉 新增 可分配金額 轉入邏輯 
               -- 步驟1: 評估淨值有值時取評估淨值，無值時取鑑估總值，乘上貸放成數 (四捨五入至個位數) 
               -- 步驟2: 將步驟1算出的結果與設定金額相比，較低者則為可分配金額 
               , CASE 
                   WHEN "SettingStat" = 2 -- 擔保品塗銷/解除設定時，可分配金額設為0 
                   THEN 0 
                   WHEN "LineAmt" = 0 -- 額度金額為0 無法計算貸放成數 
                   THEN CASE 
                          WHEN "SettingAmt" > 0  
                               AND "EvaNetWorth" > 0  
                               AND "EvaNetWorth" < "SettingAmt" 
                          THEN "EvaNetWorth" 
                          WHEN "SettingAmt" > 0  
                               AND "EvaAmt" > 0  
                               AND "EvaAmt" < "SettingAmt" 
                          THEN "EvaAmt" 
                          WHEN "SettingAmt" > 0  
                          THEN "SettingAmt" 
                          WHEN "EvaNetWorth" > 0  
                          THEN "EvaNetWorth" 
                          WHEN "EvaAmt" > 0  
                          THEN "EvaAmt" 
                        ELSE 0 END 
                   WHEN "EvaNetWorth" > 0 -- 評估淨值有值的情況 
                   THEN CASE 
                          WHEN "SettingAmt" = 0  
                          THEN "EvaNetWorth" * "LoanToValue" 
                          WHEN "EvaNetWorth" * "LoanToValue" < "SettingAmt" 
                          THEN "EvaNetWorth" * "LoanToValue" 
                        ELSE "SettingAmt" END 
                   WHEN "EvaAmt" > 0 -- 評估淨值沒值，鑑估總值有值的情況 
                   THEN CASE 
                          WHEN "SettingAmt" = 0  
                          THEN "EvaAmt" * "LoanToValue" 
                          WHEN "EvaAmt" * "LoanToValue" < "SettingAmt" 
                          THEN "EvaAmt" * "LoanToValue" 
                        ELSE "SettingAmt" END 
                 ELSE "SettingAmt" END AS "ShareTotal" -- 可分配金額 
          FROM CLM 
    ) S0 
    ON ( 
          T0."ClCode1" = S0."ClCode1" 
      AND T0."ClCode2" = S0."ClCode2" 
      AND T0."ClNo" = S0."ClNo" 
      AND S0."ShareTotal" > 0 
    ) 
    WHEN MATCHED THEN UPDATE 
    SET "ShareTotal" = ROUND(S0."ShareTotal",0) 
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
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_ClMain_Upd',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace); 
END;

/
