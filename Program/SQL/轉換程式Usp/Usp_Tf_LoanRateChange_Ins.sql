--------------------------------------------------------
--  DDL for Procedure Usp_Tf_LoanRateChange_Ins
--------------------------------------------------------
set define off;

CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_Tf_LoanRateChange_Ins" 
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
 
    DECLARE  
        "TbsDyF" DECIMAL(8); --西元帳務日 
    BEGIN 
 
    SELECT "TbsDy" + 19110000 
    INTO "TbsDyF" 
    FROM "TxBizDate" 
    WHERE "DateCode" = 'ONLINE' 
    ; 
 
    -- 刪除舊資料 
    EXECUTE IMMEDIATE 'ALTER TABLE "LoanRateChange" DISABLE PRIMARY KEY CASCADE'; 
    EXECUTE IMMEDIATE 'TRUNCATE TABLE "LoanRateChange" DROP STORAGE'; 
    EXECUTE IMMEDIATE 'ALTER TABLE "LoanRateChange" ENABLE PRIMARY KEY'; 
 
    -- 寫入資料 
    /*  放款主檔(LA$LMSP)與放款戶利率檔(LA$IRTP)關係 (2020/10/26紀錄) 
        1.有8筆放款戶在利率檔無資料,餘額皆為0 戶況為:已結案、債權轉讓、呆帳結案戶. 
        因此篩選放款戶利率檔生效日期不為0者,才寫入. 
        2.在此條件下以"生效日期"相同的條件串取加碼利率檔(LA$ASCP),有加碼利率值者為22,198筆. 
        3.寫入筆數1216875筆,為放款戶利率檔(LA$IRTP)全部筆數. 
 
        12/16修改 
        1.增加判斷"加減碼是否依合約" 
        2.增加判斷 LA$IRTP.IRTADT 早於西元帳務日 
 
        2022/5/6修改 
        不排除早於帳號撥款日的利率、加碼利率 
    */ 
    INSERT INTO "LoanRateChange" (
        "CustNo"              -- 借款人戶號 DECIMAL 7 0 
      , "FacmNo"              -- 額度編號 DECIMAL 3 0 
      , "BormNo"              -- 撥款序號 DECIMAL 3 0 
      , "EffectDate"          -- 生效日期 DECIMALD 8 0 
      , "Status"              -- 狀態 DECIMAL 1 0             Lai 2021/1/14 
      , "RateCode"            -- 利率區分 VARCHAR2 1 0 
      , "ProdNo"              -- 商品代碼 VARCHAR2 5 0        Lai 2021/1/14 Wei 2021/4/13 
      , "BaseRateCode"        -- 指標利率代碼 VARCHAR2 2 0    Lai 2021/1/14 
      , "IncrFlag"            -- 加減碼是否依合約 VARCHAR2 1 0 
      , "RateIncr"            -- 加碼利率 DECIMAL 6 4 
      , "IndividualIncr"      -- 個別加碼利率 DECIMAL 6 4 
      , "FitRate"             -- 適用利率 DECIMAL 6 4 
      , "Remark"              -- 備註 NVARCHAR2 60 0 
      , "AcDate"              -- 交易序號-會計日期 DECIMALD 8 0 
      , "TellerNo"            -- 交易序號-櫃員別 VARCHAR2 6 0 
      , "TxtNo"               -- 交易序號-流水號 VARCHAR2 8 0 
      , "CreateDate"          -- 建檔日期時間 DATE 8 0 
      , "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 0 
      , "LastUpdate"          -- 最後更新日期時間 DATE 8 0 
      , "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 0 
      , "OtherFields"         -- JsonFields VARCHAR2 2000 
    )
    SELECT R."LMSACN"                     AS "CustNo"              -- 借款人戶號 DECIMAL 7 0 
          ,R."LMSAPN"                     AS "FacmNo"              -- 額度編號 DECIMAL 3 0 
          ,R."LMSASQ"                     AS "BormNo"              -- 撥款序號 DECIMAL 3 0 
          ,R."EffectDate"                 AS "EffectDate"          -- 生效日期 DECIMALD 8 0 
          ,CASE 
             WHEN R."IncrEffectDate" > 0  
                  AND R."IncrEffectDate" <= R."EffectDate" 
                  -- 有建加碼利率者,放2 
             THEN 2 
           ELSE 0 END                     AS "Status"              -- 狀態 DECIMAL 1 0             Lai 2021/1/14 
          ,R."AILIRT"                     AS "RateCode"            -- 利率區分 VARCHAR2 1 0 
          ,R."IRTBCD"                     AS "ProdNo"              -- 商品代碼 VARCHAR2 5 0        Lai 2021/1/14 Wei 2021/4/13 
          ,FP."BaseRateCode"              AS "BaseRateCode"        -- 指標利率代碼 VARCHAR2 2 0    Lai 2021/1/14 
          ,FP."IncrFlag"                  AS "IncrFlag"            -- 加減碼是否依合約 VARCHAR2 1 0 
          ,R."RateIncr"                   AS "RateIncr"            -- 加碼利率 DECIMAL 6 4 
          ,0                              AS "IndividualIncr"      -- 個別加碼利率 DECIMAL 6 4 
          ,R."FitRate"                    AS "FitRate"             -- 適用利率 DECIMAL 6 4 
          ,''                             AS "Remark"              -- 備註 NVARCHAR2 60 0 
          ,0                              AS "AcDate"              -- 交易序號-會計日期 DECIMALD 8 0 
          ,''                             AS "TellerNo"            -- 交易序號-櫃員別 VARCHAR2 6 0 
          ,''                             AS "TxtNo"               -- 交易序號-流水號 VARCHAR2 8 0 
          ,JOB_START_TIME                 AS "CreateDate"          -- 建檔日期時間 DATE 8 0 
          ,'999999'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 0 
          ,JOB_START_TIME                 AS "LastUpdate"          -- 最後更新日期時間 DATE 8 0 
          ,'999999'                       AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 0 
          ,'{' 
           || '"IncrEffectDate":' 
           || CASE 
                WHEN R."IncrEffectDate" > 0 
                THEN TO_CHAR(R."IncrEffectDate") 
              ELSE '' END 
           || '}'                         AS "OtherFields"         -- JsonFields VARCHAR2 2000 
    FROM (SELECT LM."LMSACN" 
                ,LM."LMSAPN" 
                ,LM."LMSASQ" 
                ,LM."IRTBCD" 
                ,LM."AILIRT" 
                ,NVL(LA."ASCADT",0)  AS "IncrEffectDate" -- 加碼利率生效日 新系統 無此欄位 
                ,LI."IRTADT"         AS "EffectDate" -- 基本利率生效日 
                ,NVL(LA."ASCRAT",0) 
                                     AS "RateIncr" -- 加碼利率 
                ,LI."IRTRAT"         AS "FitRate" -- 基本利率 
                ,ROW_NUMBER() OVER (PARTITION BY LM."LMSACN" 
                                                ,LM."LMSAPN" 
                                                ,LM."LMSASQ" 
                                                ,LI."IRTADT" 
                                    ORDER BY NVL(LA."ASCADT",0) DESC -- 加碼利率生效日早於適用利率生效日且最近的一筆 
                                   ) AS "Seq" 
          FROM "LA$LMSP" LM  
          LEFT JOIN "LA$IRTP" LI ON LI."LMSACN" = LM."LMSACN" 
                                AND LI."LMSAPN" = LM."LMSAPN" 
                                AND LI."LMSASQ" = LM."LMSASQ" 
          LEFT JOIN "LA$ASCP" LA ON LA."LMSACN" = LI."LMSACN" 
                                AND LA."LMSAPN" = LI."LMSAPN" 
                                AND LA."LMSASQ" = LI."LMSASQ" 
                                AND LA."ASCADT" <= LI."IRTADT" 
                                -- AND LA."ASCADT" >= LM."LMSLLD" -- 加碼利率生效日期>=撥款日 
          WHERE NVL(LI."IRTADT",0) > 0 -- 篩選放款戶利率檔生效日期不為0者 
--            AND LM."LMSLLD" <= "TbsDyF" -- 排除預約撥款 
            -- AND LI."IRTADT" >= LM."LMSLLD" -- 基本利率生效日期>=撥款日 
         ) R 
    LEFT JOIN "FacProd" FP ON FP."ProdNo" = R."IRTBCD" 
    WHERE R."Seq" = 1 -- 加碼利率生效日早於適用利率生效日且最近的一筆 
    ; 
 
    -- 記錄寫入筆數 
    INS_CNT := INS_CNT + sql%rowcount; 
 
    -- 寫入資料 
    /* 放款主檔(LA$LMSP)與加碼利率檔(LA$ASCP)關係 (2020/10/26紀錄) 
      1.加碼利率檔總筆28198數 
      2.排除前一查詢寫入條件範圍(在放款戶利率檔已寫入22198筆) 
        這此次把生效日期不同的加碼利率寫入,計5917筆. 
      3.加碼利率檔尚有83筆未寫入(28198-22198-5917=83),原因是該83筆在放款主檔無資料 
     * 
     * 2021-04-12 智偉 修改 將未來的加碼利率轉入 
     * 2022-03-16 智偉 修改 將未被寫入的加碼利率轉入 
     *  
     * 2022/5/6修改 
     * 不排除早於帳號撥款日的利率、加碼利率 
    */ 
    INSERT INTO "LoanRateChange"  (
        "CustNo"              -- 借款人戶號 DECIMAL 7 0 
      , "FacmNo"              -- 額度編號 DECIMAL 3 0 
      , "BormNo"              -- 撥款序號 DECIMAL 3 0 
      , "EffectDate"          -- 生效日期 DECIMALD 8 0 
      , "Status"              -- 狀態 DECIMAL 1 0             Lai 2021/1/14 
      , "RateCode"            -- 利率區分 VARCHAR2 1 0 
      , "ProdNo"              -- 商品代碼 VARCHAR2 5 0        Lai 2021/1/14 Wei 2021/4/13 
      , "BaseRateCode"        -- 指標利率代碼 VARCHAR2 2 0    Lai 2021/1/14 
      , "IncrFlag"            -- 加減碼是否依合約 VARCHAR2 1 0 
      , "RateIncr"            -- 加碼利率 DECIMAL 6 4 
      , "IndividualIncr"      -- 個別加碼利率 DECIMAL 6 4 
      , "FitRate"             -- 適用利率 DECIMAL 6 4 
      , "Remark"              -- 備註 NVARCHAR2 60 0 
      , "AcDate"              -- 交易序號-會計日期 DECIMALD 8 0 
      , "TellerNo"            -- 交易序號-櫃員別 VARCHAR2 6 0 
      , "TxtNo"               -- 交易序號-流水號 VARCHAR2 8 0 
      , "CreateDate"          -- 建檔日期時間 DATE 8 0 
      , "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 0 
      , "LastUpdate"          -- 最後更新日期時間 DATE 8 0 
      , "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 0 
      , "OtherFields"         -- JsonFields VARCHAR2 2000 
    )
    WITH rawData AS ( 
      SELECT LA."LMSACN" 
           , LA."LMSAPN" 
           , LA."LMSASQ" 
           , LA."ASCADT" 
           , LM."AILIRT" 
           , LM."IRTBCD" 
           , FP."BaseRateCode" 
           , FP."IncrFlag" 
           , LA."ASCRAT" 
           , NVL(CB."BaseRate",0) AS "BaseRate" 
           , ROW_NUMBER() 
             OVER ( 
               PARTITION BY CB."BaseRateCode" 
                          , LA."LMSACN" 
                          , LA."LMSAPN" 
                          , LA."LMSASQ" 
                          , LA."ASCADT" 
               ORDER BY CB."EffectDate" DESC 
             ) AS "CbSeq" 
      FROM "LA$ASCP" LA 
      LEFT JOIN "LA$LMSP" LM ON LM."LMSACN" = LA."LMSACN" 
                            AND LM."LMSAPN" = LA."LMSAPN" 
                            AND LM."LMSASQ" = LA."LMSASQ" 
      LEFT JOIN "FacProd" FP ON FP."ProdNo" = LM."IRTBCD" 
      LEFT JOIN "LoanRateChange" LRC ON LRC."CustNo" = LA."LMSACN" 
                                    AND LRC."FacmNo" = LA."LMSAPN" 
                                    AND LRC."BormNo" = LA."LMSASQ" 
                                    AND LRC."EffectDate" = LA."ASCADT" 
      LEFT JOIN ( 
        SELECT "BaseRateCode" 
             , "BaseRate" 
             , "EffectDate" 
        FROM "CdBaseRate" 
        WHERE "BaseRateCode" IN ('01','02') 
          AND "EffectFlag" = 1 
      ) CB ON CB."BaseRateCode" = FP."BaseRateCode" 
          AND CB."EffectDate" <= LA."ASCADT" 
      WHERE NVL(LM."LMSACN",0) > 0 -- 有串到放款主檔的資料才寫入 
        AND NVL(LRC."EffectDate",0) = 0 -- 尚未被記錄在放款利率變動檔的加碼利率才寫入 
        AND NVL(FP."BaseRateCode",'00') IN ('01','02') 
--        AND LM."LMSLLD" <= "TbsDyF" -- 排除預約撥款 
        -- AND LA."ASCADT" >= LM."LMSLLD" -- 加碼利率生效日期>=撥款日 
        AND LA."ASCADT" > "TbsDyF" -- 未來的加碼利率 
    ) 
    SELECT "LMSACN"                       AS "CustNo"              -- 借款人戶號 DECIMAL 7 0 
          ,"LMSAPN"                       AS "FacmNo"              -- 額度編號 DECIMAL 3 0 
          ,"LMSASQ"                       AS "BormNo"              -- 撥款序號 DECIMAL 3 0 
          ,"ASCADT"                       AS "EffectDate"          -- 生效日期 DECIMALD 8 0 
          -- 有建加碼利率者,放2 
          ,2                              AS "Status"              -- 狀態 DECIMAL 1 0 
          ,"AILIRT"                       AS "RateCode"            -- 利率區分 VARCHAR2 1 0 
          ,"IRTBCD"                       AS "ProdNo"              -- 商品代碼 VARCHAR2 5 0 
          ,"BaseRateCode"                 AS "BaseRateCode"        -- 指標利率代碼 VARCHAR2 2 0 
          ,"IncrFlag"                     AS "IncrFlag"            -- 加減碼是否依合約 VARCHAR2 1 0 
          ,"ASCRAT"                       AS "RateIncr"            -- 加碼利率 DECIMAL 6 4 
          ,0                              AS "IndividualIncr"      -- 個別加碼利率 DECIMAL 6 4 
          ,NVL("BaseRate",0) 
           + "ASCRAT"                     AS "FitRate"             -- 適用利率 DECIMAL 6 4 
          ,''                             AS "Remark"              -- 備註 NVARCHAR2 60 0 
          ,0                              AS "AcDate"              -- 交易序號-會計日期 DECIMALD 8 0 
          ,''                             AS "TellerNo"            -- 交易序號-櫃員別 VARCHAR2 6 0 
          ,''                             AS "TxtNo"               -- 交易序號-流水號 VARCHAR2 8 0 
          ,JOB_START_TIME                 AS "CreateDate"          -- 建檔日期時間 DATE 8 0 
          ,'999999'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 0 
          ,JOB_START_TIME                 AS "LastUpdate"          -- 最後更新日期時間 DATE 8 0 
          ,'999999'                       AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 0 
          ,'{' 
           || '"IncrEffectDate":' 
           || CASE 
                WHEN "ASCADT" > 0 
                THEN TO_CHAR("ASCADT") 
              ELSE '' END 
           || '}'                         AS "OtherFields"         -- JsonFields VARCHAR2 2000 
    FROM rawData 
    WHERE NVL("CbSeq",1) = 1 
    ; 

    -- 2023-03-09 Wei from 會議中SKL User珮瑜說明:原AS400系統可修改[適用利率]所以
    -- [指標利率]+[加碼利率]≠[適用利率]是正常的，轉換時不應該重算[加碼利率]。
    /* 更新 "LoanRateChange"."IndividualIncr" "IncrFlag" = "N" 個別加碼利率 DECIMAL 6 4 */ 
    -- MERGE INTO "LoanRateChange" T1 
    -- USING ( 
    --   SELECT L."CustNo"              -- 借款人戶號 DECIMAL 7 0 
    --         ,L."FacmNo"              -- 額度編號 DECIMAL 3 0 
    --         ,L."BormNo"              -- 撥款序號 DECIMAL 3 0 
    --         ,L."EffectDate"          -- 生效日期 DECIMALD 8 0 
    --         ,L."FitRate" - L."BaseRate" AS "IndividualIncr" -- 個別加碼利率 DECIMAL 6 4 
    --   FROM ( SELECT         
    --            R."CustNo"              AS "CustNo"  
    --           ,R."FacmNo"              AS "FacmNo"  
    --           ,R."BormNo"              AS "BormNo" 
    --           ,R."EffectDate"          AS "EffectDate"   
    --           ,R."FitRate"             AS "FitRate" 
    --           ,C."BaseRate"            AS "BaseRate" 
    --           ,ROW_NUMBER() OVER (PARTITION BY   R."CustNo", R."FacmNo" ,R."BormNo" ,R."EffectDate" 
    --                               ORDER BY C."EffectDate" DESC) AS "Seq" 
    --          FROM "LoanRateChange" R 
    --          LEFT JOIN "CdBaseRate" C ON C."BaseRateCode" = R."BaseRateCode" 
    --                                  AND C."EffectFlag" = 1 
    --                                  AND C."EffectDate" <= R."EffectDate" 
    --          WHERE R."BaseRateCode"  IN ('01','02')   
    --           AND  R."IncrFlag" = 'N' 
    --         ) L 
    --    WHERE L."Seq" = 1 
    -- ) S1 ON (    S1."CustNo" = T1."CustNo" 
    --          AND S1."FacmNo" = T1."FacmNo" 
    --          AND S1."BormNo" = T1."BormNo" 
    --          AND S1."EffectDate" = T1."EffectDate" 
    --          AND S1."IndividualIncr" IS NOT NULL) 
    -- WHEN MATCHED THEN UPDATE SET 
    -- T1."IndividualIncr" = S1."IndividualIncr" 
    -- ; 
 
    /* 更新 "RateIncr" if  "RateIncr" = 0  "IncrFlag" = "N" 加碼利率 DECIMAL 6 4 */ 
    MERGE INTO "LoanRateChange" T1 
    USING ( 
      SELECT L."CustNo"              -- 借款人戶號 DECIMAL 7 0 
            ,L."FacmNo"              -- 額度編號 DECIMAL 3 0 
            ,L."BormNo"              -- 撥款序號 DECIMAL 3 0 
            ,L."EffectDate"          -- 生效日期 DECIMALD 8 0 
            ,NVL(L."ASCRAT",M."RateIncr") AS "RateIncr"  
      FROM ( SELECT         
               R."CustNo"              AS "CustNo"  
              ,R."FacmNo"              AS "FacmNo"  
              ,R."BormNo"              AS "BormNo" 
              ,R."EffectDate"          AS "EffectDate"   
              ,LA."ASCRAT"             AS "ASCRAT" 
              ,ROW_NUMBER() OVER (PARTITION BY  R."CustNo", R."FacmNo" ,R."BormNo" ,R."EffectDate" 
                                  ORDER BY  LA."ASCADT" DESC) AS "Seq" 
             FROM "LoanRateChange" R 
             LEFT JOIN "LA$ASCP" LA  ON LA."LMSACN" = R."CustNo"  
                                    AND LA."LMSAPN" = R."FacmNo" 
                                    AND LA."LMSASQ" = R."BormNo" 
                                    AND LA."ASCADT" <= R."EffectDate" 
             LEFT JOIN "LoanBorMain" M ON M."CustNo" = R."CustNo"  
                                       AND M."FacmNo" = R."FacmNo" 
                                       AND M."BormNo" = R."BormNo" 
             WHERE R."BaseRateCode"  IN ('01','02') 
              AND  R."IncrFlag" = 'N' 
            ) L 
      LEFT JOIN "LoanBorMain" M ON M."CustNo" = L."CustNo"  
                                AND M."FacmNo" = L."FacmNo" 
                                AND M."BormNo" = L."BormNo" 
      WHERE L."Seq" = 1 
    ) S1 ON (    S1."CustNo" = T1."CustNo" 
             AND S1."FacmNo" = T1."FacmNo" 
             AND S1."BormNo" = T1."BormNo" 
             AND S1."EffectDate" = T1."EffectDate" 
             AND S1."RateIncr" IS NOT NULL) 
    WHEN MATCHED THEN UPDATE SET 
     T1."RateIncr"  = S1."RateIncr"  
    ; 
 
    -- 2023-03-09 Wei from 會議中SKL User珮瑜說明:原AS400系統可修改[適用利率]所以
    -- [指標利率]+[加碼利率]≠[適用利率]是正常的，轉換時不應該重算[加碼利率]。
   /* 更新 "LoanRateChange"."RateIncr" if  "RateIncr" = 0  "IncrFlag" = "Y"  -- 加碼利率 DECIMAL 6 4 */ 
    -- MERGE INTO "LoanRateChange" T1 
    -- USING ( 
    --   SELECT L."CustNo"              -- 借款人戶號 DECIMAL 7 0 
    --         ,L."FacmNo"              -- 額度編號 DECIMAL 3 0 
    --         ,L."BormNo"              -- 撥款序號 DECIMAL 3 0 
    --         ,L."EffectDate"          -- 生效日期 DECIMALD 8 0 
    --         ,L."FitRate" - L."BaseRate" AS "RateIncr" -- 加碼利率 DECIMAL 6 4 
    --   FROM ( SELECT         
    --            R."CustNo"              AS "CustNo"  
    --           ,R."FacmNo"              AS "FacmNo"  
    --           ,R."BormNo"              AS "BormNo" 
    --           ,R."EffectDate"          AS "EffectDate"   
    --           ,R."FitRate"             AS "FitRate" 
    --           ,C."BaseRate"            AS "BaseRate" 
    --           ,ROW_NUMBER() OVER (PARTITION BY R."CustNo", R."FacmNo" ,R."BormNo" ,R."EffectDate" 
    --                               ORDER BY C."EffectDate" DESC) AS "Seq" 
    --          FROM "LoanRateChange" R 
    --          LEFT JOIN "CdBaseRate" C ON C."BaseRateCode" = R."BaseRateCode" 
    --                                  AND C."EffectFlag" = 1 
    --                                  AND C."EffectDate" <= R."EffectDate" 
    --          WHERE R."BaseRateCode"  IN ('01','02')   
    --           AND  R."RateIncr" = 0                   
    --           AND  R."IncrFlag" = 'Y'                   
    --         ) L 
    --    WHERE L."Seq" = 1 
    -- ) S1 ON (    S1."CustNo" = T1."CustNo" 
    --          AND S1."FacmNo" = T1."FacmNo" 
    --          AND S1."BormNo" = T1."BormNo" 
    --          AND S1."EffectDate" = T1."EffectDate" 
    --          AND S1."RateIncr" IS NOT NULL) 
    -- WHEN MATCHED THEN UPDATE SET 
    -- T1."RateIncr"  = S1."RateIncr"  
    -- ; 
 
    -- 2022-03-23 智偉新增 
    -- 將預調利率更新為前一筆利率的數字 
    MERGE INTO "LoanRateChange" T1 
    USING ( 
      WITH LBM AS ( 
        SELECT "CustNo" 
             , "FacmNo" 
             , "BormNo" 
             , "Status" 
        FROM "LoanBorMain" 
      ) 
      , orderedData AS ( 
        SELECT LRC."CustNo" 
             , LRC."FacmNo" 
             , LRC."BormNo" 
             , LRC."EffectDate" 
             , LRC."Status" 
             , LRC."RateCode" 
             , LRC."ProdNo" 
             , LRC."BaseRateCode" 
             , LRC."IncrFlag" 
             , LRC."RateIncr" 
             , LRC."IndividualIncr" 
             , LRC."FitRate" 
             , ROW_NUMBER() 
               OVER ( 
                 PARTITION BY LRC."CustNo" 
                            , LRC."FacmNo" 
                            , LRC."BormNo" 
                 ORDER BY LRC."EffectDate" DESC 
               ) AS "Seq" 
        FROM "LoanRateChange" LRC 
        LEFT JOIN LBM ON LBM."CustNo" = LRC."CustNo" 
                     AND LBM."FacmNo" = LRC."FacmNo" 
                     AND LBM."BormNo" = LRC."BormNo" 
        LEFT JOIN "FacProd" FP ON FP."ProdNo" = LRC."ProdNo" 
        WHERE NVL(LBM."Status",-1) = 0 
          AND LRC."BaseRateCode" = '99' 
          AND FP."EmpFlag" = 'N' -- 排除員工商品 
      )  
      SELECT O1."CustNo" 
           , O1."FacmNo" 
           , O1."BormNo" 
           , O1."EffectDate" 
           , O2."RateIncr" 
           , O2."IndividualIncr" 
           , O2."FitRate" 
      FROM orderedData O1 
      LEFT JOIN orderedData O2 ON O2."CustNo" = O1."CustNo" 
                              AND O2."FacmNo" = O1."FacmNo" 
                              AND O2."BormNo" = O1."BormNo" 
                              AND O2."Seq" = 2 
      WHERE O1."Seq" = 1 
        AND TO_DATE(O1."EffectDate",'YYYYMMDD') > ADD_MONTHS(TO_DATE("TbsDyF",'YYYYMMDD'),1) 
    ) S1 
    ON ( 
      S1."CustNo" = T1."CustNo" 
      AND S1."FacmNo" = T1."FacmNo" 
      AND S1."BormNo" = T1."BormNo" 
      AND S1."EffectDate" = T1."EffectDate" 
    ) 
    WHEN MATCHED THEN UPDATE SET 
    T1."RateIncr" = NVL(S1."RateIncr",0) 
    , T1."IndividualIncr" = NVL(S1."IndividualIncr",0) 
    , T1."FitRate" = NVL(S1."FitRate",0) 
    , T1."Remark" = '預調利率' 
    ; 
 
    -- 記錄程式結束時間 
    JOB_END_TIME := SYSTIMESTAMP; 
 
    commit; 
 
    END; 
 
    -- 例外處理 
    Exception 
    WHEN OTHERS THEN 
    ERROR_MSG := SQLERRM || CHR(13) || CHR(10) || dbms_utility.format_error_backtrace; 
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_LoanRateChange_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace); 
END;

/
