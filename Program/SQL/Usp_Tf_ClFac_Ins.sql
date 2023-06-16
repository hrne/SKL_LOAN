CREATE OR REPLACE PROCEDURE "Usp_Tf_ClFac_Ins" 
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
    EXECUTE IMMEDIATE 'ALTER TABLE "ClFac" DISABLE PRIMARY KEY CASCADE';
    EXECUTE IMMEDIATE 'TRUNCATE TABLE "ClFac" DROP STORAGE';
    EXECUTE IMMEDIATE 'ALTER TABLE "ClFac" ENABLE PRIMARY KEY';

    -- 寫入資料
    -- 擔保品號碼NULL者 找出舊的擔保品關係 依照其群組號碼 找出該轉入的那筆 寫入
    INSERT INTO "ClFac" (
        "ClCode1"             -- 擔保品代號1 DECIMAL 1 
      , "ClCode2"             -- 擔保品代號2 DECIMAL 2 
      , "ClNo"                -- 擔保品編號 DECIMAL 7 
      , "ApproveNo"           -- 核准號碼 DECIMAL 7 
      , "CustNo"              -- 借款人戶號 DECIMAL 7 
      , "FacmNo"              -- 額度編號 DECIMAL 3 
      , "MainFlag"            -- 主要擔保品記號 VARCHAR2 1
      , "FacShareFlag"        -- 共用額度記號 DECIMAL 1 -- 0:非共用額度 1:主要額度 2:共用額度
      , "ShareAmt"            -- 分配金額 DECIMAL 16 2
      , "OriSettingAmt"       -- 設定金額 DECIMAL 16 2 擔保品與額度綁定當下的設定金額
      , "CreateDate"          -- 建檔日期時間 DATE 0 
      , "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 
      , "LastUpdate"          -- 最後更新日期時間 DATE 0 
      , "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 
      , "OriEvaNotWorth"      -- 原評估淨值 DECIMAL 16 2
    )
    SELECT DISTINCT 
           M."ClCode1"                    AS "ClCode1"             -- 擔保品代號1 DECIMAL 1 
          ,M."ClCode2"                    AS "ClCode2"             -- 擔保品代號2 DECIMAL 2 
          ,M."ClNo"                       AS "ClNo"                -- 擔保品編號 DECIMAL 7 
          ,FAC."ApplNo"                   AS "ApproveNo"           -- 核准號碼 DECIMAL 7 
          ,FAC."CustNo"                   AS "CustNo"              -- 借款人戶號 DECIMAL 7 
          ,FAC."FacmNo"                   AS "FacmNo"              -- 額度編號 DECIMAL 3 
          ,' '                            AS "MainFlag"            -- 主要擔保品記號 VARCHAR2 1
          ,0                              AS "FacShareFlag"        -- 共用額度記號 DECIMAL 1 -- 0:非共用額度 1:主要額度 2:共用額度
          ,0                              AS "ShareAmt"            -- 分配金額 DECIMAL 16 2
          ,0                              AS "OriSettingAmt"       -- 設定金額 DECIMAL 16 2 擔保品與額度綁定當下的設定金額
          ,JOB_START_TIME                 AS "CreateDate"          -- 建檔日期時間 DATE 0 
          ,'999999'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 
          ,JOB_START_TIME                 AS "LastUpdate"          -- 最後更新日期時間 DATE 0 
          ,'999999'                       AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 
          ,NVL(GDTP.ESTVAL,0)             AS "OriEvaNotWorth"      -- 原評估淨值 DECIMAL 16 2
    FROM "FacMain" FAC
    LEFT JOIN "ClFac" CF ON CF."CustNo" = FAC."CustNo"
                        AND CF."FacmNo" = FAC."FacmNo"
    LEFT JOIN "LA$APLP" AP ON AP."LMSACN" = FAC."CustNo"
                          AND AP."LMSAPN" = FAC."FacmNo"
    LEFT JOIN "ClNoMap" M ON M."GdrId1" = AP."GDRID1"
                         AND M."GdrId2" = AP."GDRID2"
                         AND M."GdrNum" = AP."GDRNUM"
    LEFT JOIN "LA$GDTP" GDTP ON S2."GDRID1" = AP."GDRID1" 
                            AND S2."GDRID2" = AP."GDRID2" 
                            AND S2."GDRNUM" = AP."GDRNUM" 
    WHERE NVL(CF."ClNo",0) = 0
      AND AP."GDRNUM" > 0
      AND NVL(M."ClNo",0) != 0
    ;

    -- 記錄寫入筆數
    INS_CNT := INS_CNT + sql%rowcount;

    -- 更新 主要擔保品記號 case 1 : 若該額度只有綁一筆擔保品 , 則該筆關係為主要的擔保品與額度關係
    MERGE INTO "ClFac" T1
    USING (SELECT "CustNo"
                 ,"FacmNo"
           FROM "ClFac"
           GROUP BY "CustNo"
                   ,"FacmNo"
           HAVING COUNT(*) = 1
          ) S1
    ON (S1."CustNo" = T1."CustNo"
        AND S1."FacmNo" = T1."FacmNo")
    WHEN MATCHED THEN UPDATE SET
    T1."MainFlag" = 'Y'
    ;

    -- 更新 主要擔保品記號 case 2 : 若該額度有綁多筆擔保品 , 則找出擔保品號碼最大的那筆關係為主要的擔保品與額度關係
    MERGE INTO "ClFac" T1
    USING (SELECT SS1."CustNo"
                 ,SS1."FacmNo"
                 ,SS2."ClCode1"
                 ,SS2."ClCode2"
                 ,SS2."ClNo"
           FROM (SELECT "CustNo"
                       ,"FacmNo"
                 FROM "ClFac"
                 GROUP BY "CustNo"
                         ,"FacmNo"
                 HAVING COUNT(*) >= 2
                ) SS1
           LEFT JOIN (SELECT "CustNo"
                            ,"FacmNo"
                            ,"ClCode1"
                            ,"ClCode2"
                            ,"ClNo"
                            ,ROW_NUMBER() OVER (PARTITION BY "CustNo"
                                                            ,"FacmNo"
                                                ORDER BY "ApproveNo" DESC -- 2022-03-08 智偉增加判斷:案件申請號碼最新的一筆抓最新的擔保品
                                                       , "ClCode1"
                                                       , "ClCode2"
                                                       , CASE
                                                           WHEN "ClNo" > 9000000
                                                           THEN 0 
                                                         ELSE "ClNo" END DESC
                                                       , "ClNo"
                                               ) AS "SEQ"
                      FROM "ClFac"
                     ) SS2 ON SS2."CustNo" = SS1."CustNo"
                          AND SS2."FacmNo" = SS1."FacmNo"
           WHERE NVL(SS2."SEQ",0) = 1
          ) S1
    ON (S1."CustNo" = T1."CustNo"
        AND S1."FacmNo" = T1."FacmNo"
        AND S1."ClCode1" = T1."ClCode1"
        AND S1."ClCode2" = T1."ClCode2"
        AND S1."ClNo" = T1."ClNo"
        )
    WHEN MATCHED THEN UPDATE SET
    T1."MainFlag" = 'Y'
    ;

    -- 記錄程式結束時間
    JOB_END_TIME := SYSTIMESTAMP;

    commit;

    -- 例外處理
    Exception
    WHEN OTHERS THEN
    ERROR_MSG := SQLERRM || CHR(13) || CHR(10) || dbms_utility.format_error_backtrace;
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_ClFac_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;





/
