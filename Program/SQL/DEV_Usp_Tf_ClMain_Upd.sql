--------------------------------------------------------
--  DDL for Procedure Usp_Tf_ClMain_Upd
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "Usp_Tf_ClMain_Upd" 
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
          SELECT DISTINCT
                 CLM."ClCode1"
               , CLM."ClCode2"
               , CLM."ClNo"
               -- 2021-08-10 智偉 新增 可分配金額 轉入邏輯
               -- 步驟1: 評估淨值有值時取評估淨值，無值時取鑑估總值，乘上貸放成數 (四捨五入至個位數)
               -- 步驟2: 將步驟1算出的結果與設定金額相比，較低者則為可分配金額
               , CASE
                   WHEN IMM."SettingStat" = 2 -- 擔保品塗銷/解除設定時，可分配金額設為0
                   THEN 0
                   WHEN NVL(CF."LineAmt",0) = 0 -- 額度金額為0 無法計算貸放成數
                   THEN CASE
                          WHEN IMM."SettingAmt" > 0 
                               AND IMM."EvaNetWorth" > 0 
                               AND IMM."EvaNetWorth" < IMM."SettingAmt"
                          THEN IMM."EvaNetWorth"
                          WHEN IMM."SettingAmt" > 0 
                               AND CLM."EvaAmt" > 0 
                               AND CLM."EvaAmt" < IMM."SettingAmt"
                          THEN CLM."EvaAmt"
                          WHEN IMM."SettingAmt" > 0 
                          THEN IMM."SettingAmt"
                          WHEN IMM."EvaNetWorth" > 0 
                          THEN IMM."EvaNetWorth"
                          WHEN CLM."EvaAmt" > 0 
                          THEN CLM."EvaAmt"
                        ELSE 0 END
                   WHEN IMM."EvaNetWorth" > 0 -- 評估淨值有值的情況
                   THEN CASE
                          WHEN IMM."SettingAmt" = 0 
                          THEN IMM."EvaNetWorth"
                               * ROUND(NVL(CF."LineAmt",0) / IMM."EvaNetWorth",10)
                          WHEN IMM."EvaNetWorth"
                               * ROUND(NVL(CF."LineAmt",0) / IMM."EvaNetWorth",10) -- 貸放成數 舊資料不動產擔保品沒有存貸放成數，轉換時自己算
                               < IMM."SettingAmt"
                          THEN IMM."EvaNetWorth"
                               * ROUND(NVL(CF."LineAmt",0) / IMM."EvaNetWorth",10)
                        ELSE IMM."SettingAmt" END
                   WHEN CLM."EvaAmt" > 0 -- 評估淨值沒值，鑑估總值有值的情況
                   THEN CASE
                          WHEN IMM."SettingAmt" = 0 
                          THEN CLM."EvaAmt"
                               * ROUND(NVL(CF."LineAmt",0) / CLM."EvaAmt",10)
                          WHEN CLM."EvaAmt"
                               * ROUND(NVL(CF."LineAmt",0) / CLM."EvaAmt",10) -- 貸放成數 舊資料不動產擔保品沒有存貸放成數，轉換時自己算
                               < IMM."SettingAmt"
                          THEN CLM."EvaAmt"
                               * ROUND(NVL(CF."LineAmt",0) / CLM."EvaAmt",10)
                        ELSE IMM."SettingAmt" END
                 ELSE IMM."SettingAmt" END AS "ShareTotal" -- 可分配金額
          FROM "ClMain" CLM
          LEFT JOIN "ClImm" IMM ON IMM."ClCode1" = CLM."ClCode1"
                               AND IMM."ClCode2" = CLM."ClCode2"
                               AND IMM."ClNo" = CLM."ClNo"
          LEFT JOIN (SELECT CF."ClCode1"
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
                    ) CF ON CF."ClCode1" = CLM."ClCode1"
                        AND CF."ClCode2" = CLM."ClCode2"
                        AND CF."ClNo" = CLM."ClNo"
          WHERE CLM."ClCode1" IN (1,2)
    ) S0
    ON (
          T0."ClCode1" = S0."ClCode1"
      AND T0."ClCode2" = S0."ClCode2"
      AND T0."ClNo" = S0."ClNo"
     --  AND S0."ShareTotal" > 0
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
