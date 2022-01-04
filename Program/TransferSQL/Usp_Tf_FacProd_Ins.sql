--------------------------------------------------------
--  DDL for Procedure Usp_Tf_FacProd_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "Usp_Tf_FacProd_Ins" 
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
    EXECUTE IMMEDIATE 'ALTER TABLE "FacProd" DISABLE PRIMARY KEY CASCADE';
    EXECUTE IMMEDIATE 'TRUNCATE TABLE "FacProd" DROP STORAGE';
    EXECUTE IMMEDIATE 'ALTER TABLE "FacProd" ENABLE PRIMARY KEY';

    -- 寫入資料
    INSERT INTO "FacProd"
    SELECT "TB$TBLP"."IN$COD"             AS "ProdNo"              -- 商品代碼 VARCHAR2 5 0
          ,REPLACE(REPLACE(TRIM(TO_SINGLE_BYTE("TB$TBLP"."IN$DSC")),'Ⅱ','II'),'','') -- 10/19 Wei修改:將控制字元SI REPLACE為空
                                          AS "ProdName"            -- 商品名稱 NVARCHAR2 60 0
          ,NVL("TB$IRTP"."IN$ADT",0)      AS "StartDate"           -- 商品生效日期 DECIMALD 8 0
          ,0                              AS "EndDate"             -- 商品截止日期 DECIMALD 8 0
          ,'0'                            AS "StatusCode"          -- 商品狀態 VARCHAR2 1 0
          ,CASE
             WHEN "TB$TBLP"."IN$COD" IN ('60','61','62','63')
             THEN 'Y'
           ELSE 'N' END                   AS "AgreementFg"         -- 是否為協議商品 VARCHAR2 1 0 (Y:是 N:否)
          ,CASE
             WHEN NVL(AP."IRTBCD",' ') != ' '
             THEN 'Y'
           ELSE 'N' END                   AS "EnterpriseFg"        -- 企金可使用記號 VARCHAR2 1 0
          ,'TWD'                          AS "CurrencyCode"        -- 幣別 VARCHAR2 3 0
          ,CASE
          -- 2020-12-22 根據賴桑電話中 & 梓俊Line中的的紙條修改
          -- 1. 郵局 = ('8','81','82','83','89','JA','JB','JC','IA','IB','IC','ID','IE','IF','IG','IH','II')
          -- 2. 定期機動 = 保單分紅
             WHEN TRIM("TB$TBLP"."IN$COD")
                  IN ('8','81','82','83','89','JA','JB','JC','IA','IB','IC','ID','IE','IF','IG','IH','II')
             THEN '02'
             WHEN TRIM("TB$TBLP"."IN$COD") = 'TB' -- 2021-07-26 智偉增加判斷: 原利率別為TB時，指標利率寫03:台北金融業拆款定盤利率
             THEN '03'
             WHEN "TB$TBLP"."AILIRT" = '3'
             THEN '01'
           ELSE '99' END                  AS "BaseRateCode"        -- 指標利率代碼 VARCHAR2 2 0
          ,CASE
             WHEN TRIM("TB$TBLP"."IN$COD")
                  IN ('8','81','82','83','89','JA','JB','JC','IA','IB','IC','ID','IE','IF','IG','IH','II')
             THEN NVL("TB$IRTP"."IN$RAT",0)
             WHEN TRIM("TB$TBLP"."IN$COD") = 'TB'
             THEN NVL("TB$IRTP"."IN$RAT",0)
             WHEN "TB$TBLP"."AILIRT" = '3'
             THEN NVL("TB$IRTP"."IN$RAT",0)
           ELSE 0 END                     AS "ProdIncr"            -- 商品加碼利率 DECIMAL 6 4
          ,0                              AS "LowLimitRate"        -- 利率下限 DECIMAL 6 4
          ,CASE
             -- 2021-12-28 智偉修改,from 賴桑: 定期機動且RATFUN不為LN57Q0者為N,其餘皆為Y
             WHEN "TB$TBLP"."AILIRT" = '3' 
                   AND NVL("TB$TBLP"."RATFUN",' ') != 'LN57Q0'
             THEN 'N'
           ELSE 'Y' END                   AS "IncrFlag"            -- 加減碼是否依合約 VARCHAR2 1 0
          -- ,CASE
          --    WHEN TRIM("TB$TBLP"."IN$COD")
          --         IN ('D1','D2','D3','D4','D5','D6','D7','D8','D9'
          --            ,'EM','EN','EO','1','11','1A','38','JA','JB'
          --            ,'JC','X1','X2','60','81','82','83','1B')
          --    THEN 'N' -- 2020-12-21 Wei修改: 列出來的是N 其他為Y
          --  ELSE 'Y' END                   AS "IncrFlag"            -- 加減碼是否依合約 VARCHAR2 1 0
          ,CASE
             WHEN "TB$TBLP"."AILIRT" = '1' -- 機動
             THEN '1'
             WHEN "TB$TBLP"."AILIRT" = '2' -- 固定
             THEN '2'
             WHEN "TB$TBLP"."AILIRT" = '3' -- 定期機動
             THEN '3'
           ELSE '' END                    AS "RateCode"            -- 利率區分 VARCHAR2 1 0
          ,CASE
             WHEN NVL("TB$TBLP"."GOVIRT",' ') = 'Y'
             THEN 'Y'
           ELSE 'N' END                   AS "GovOfferFlag"        -- 政府優惠房貸 VARCHAR2 1 0
          ,CASE
             WHEN TRIM(TO_SINGLE_BYTE("TB$TBLP"."IN$COD")) IN ('P','P1','P2','P3') THEN 'Y'
           ELSE 'N' END                   AS "FinancialFlag"       -- 理財型房貸 VARCHAR2 1 0
          -- 參考資料:利率代碼整理.xlsx 利率別代號為黃色底為員工優惠貸款
          ,CASE
             WHEN TRIM(TO_SINGLE_BYTE("TB$TBLP"."IN$COD")) IN ('1','11','1A','38','EM','EN','EO','EP','EQ','ER','ES') THEN 'Y'
           ELSE 'N' END                   AS "EmpFlag"             -- 員工優惠貸款 VARCHAR2 1 0
          ,CASE
             WHEN NVL("TB$TBLP"."PSNBCD",0) > 0 THEN 'Y' -- 2021-03-22 修改為Y/N
           ELSE 'N' END                   AS "BreachFlag"          -- 是否限制清償 VARCHAR2 1 0
          ,CASE
             WHEN NVL("TB$TBLP"."PSNBCD",0) > 0 THEN LPAD(TO_CHAR(NVL("TB$TBLP"."PSNBCD",0)),3,'0') -- 10/19 Wei修改
           ELSE '999' END                 AS "BreachCode"          -- 違約適用方式 VARCHAR2 3 0
          ,CASE
             WHEN NVL("TB$TBLP"."PSNBCD",0) > 0 THEN '2' -- 2021/10/21 Wei修改
           ELSE '' END                    AS "BreachGetCode"       -- 違約金收取方式 VARCHAR2 1 0
          ,CASE
             WHEN NVL("TB$TBLP"."PSNBCD",0) = 1 THEN 36 -- 11/18 Wei修改
             WHEN NVL("TB$TBLP"."PSNBCD",0) = 2 THEN 36 -- 11/18 Wei修改
           ELSE 0 END                     AS "ProhibitMonth"        -- 限制清償年限 DECIMAL 2 0
          ,CASE
             WHEN NVL("TB$TBLP"."PSNBCD",0) = 1 THEN 1 -- 11/18 Wei修改
             WHEN NVL("TB$TBLP"."PSNBCD",0) = 2 THEN 1 -- 11/18 Wei修改
           ELSE 0 END                     AS "BreachPercent"       -- 違約金百分比 DECIMAL 3 2
          ,CASE
             WHEN NVL("TB$TBLP"."PSNBCD",0) = 1 THEN 12 -- 11/18 Wei修改
             WHEN NVL("TB$TBLP"."PSNBCD",0) = 2 THEN 6  -- 11/18 Wei修改
           ELSE 0 END                     AS "BreachDecreaseMonth" -- 違約金分段月數 DECIMAL 3 0
          ,CASE
             WHEN NVL("TB$TBLP"."PSNBCD",0) = 1 THEN 0.25 -- 11/18 Wei修改
             WHEN NVL("TB$TBLP"."PSNBCD",0) = 2 THEN 0.1  -- 11/18 Wei修改
           ELSE 0 END                     AS "BreachDecrease"      -- 分段遞減百分比 DECIMAL 2 0
          ,0                              AS "BreachStartPercent"  -- 還款起算比例% decimal 5 2
          ,NVL("TB$TBLP"."STRFLG",' ')    AS "Ifrs9StepProdCode"   -- IFRS階梯商品別 varchar2 1
          ,TRIM("TB$TBLP"."PRDGRP")       AS "Ifrs9ProdCode"       -- IFRS產品別 varchar2 2
          ,JOB_START_TIME                 AS "CreateDate"          -- 建檔日期時間 DATE 8 0
          ,'999999'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 0
          ,JOB_START_TIME                 AS "LastUpdate"          -- 最後更新日期時間 DATE 8 0
          ,'999999'                       AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 0
    FROM "TB$TBLP"
    LEFT JOIN (SELECT S1."IN$COD" -- 利率類別
                     ,S1."STRFLG" -- 階梯商品別
                     ,CASE
                        WHEN S1."STRFLG" IS NULL THEN MAX(NVL(S2."IN$SEQ",0))
                      ELSE MIN(NVL(S2."IN$SEQ",0)) END AS "Seq"
               FROM "TB$TBLP" S1 -- 基本利率名稱對照檔
               LEFT JOIN "TB$IRTP" S2 ON S2."IN$COD" = S1."IN$COD" -- 基本利率檔
               GROUP BY S1."IN$COD",S1."STRFLG"
              ) SS1 ON SS1."IN$COD" = "TB$TBLP"."IN$COD" 
    LEFT JOIN "TB$IRTP" ON "TB$IRTP"."IN$COD" = "TB$TBLP"."IN$COD" 
                       AND NVL("TB$IRTP"."IN$SEQ",0) = SS1."Seq"
    LEFT JOIN ( SELECT DISTINCT "IRTBCD"
                FROM "LA$APLP"
                WHERE NVL("CASUNT",0) = 1
              ) AP ON AP."IRTBCD" = "TB$TBLP"."IN$COD" 
    ;

    -- 記錄寫入筆數
    INS_CNT := INS_CNT + sql%rowcount;

    /* 更新加碼利率 */
    -- 2021-04-09 修改 : TB$IRTP.IN$RAT 是 最終利率
    -- 與指標利率相減後得加碼利率
    MERGE INTO "FacProd" T1
    USING (SELECT FAC."ProdNo"
                 ,FAC."StartDate"
                 ,CASE
                    WHEN NVL(FAC."ProdIncr",0) - NVL(CDRT."BaseRate",0) < 0
                    THEN 0 
                  ELSE NVL(FAC."ProdIncr",0) - NVL(CDRT."BaseRate",0)
                  END                 AS "ProdIncr"
                 ,ROW_NUMBER() OVER (PARTITION BY FAC."ProdNo"
                                                 ,FAC."StartDate"
                                     ORDER BY CDRT."EffectDate" DESC
                                    ) AS "Seq"
           FROM "FacProd" FAC
           LEFT JOIN "CdBaseRate" CDRT ON CDRT."BaseRateCode" = FAC."BaseRateCode"
                                      AND CDRT."EffectDate"  <= FAC."StartDate"
           WHERE FAC."BaseRateCode" IN ('01','02')
    ) S1 ON (    S1."ProdNo" = T1."ProdNo"
             AND S1."StartDate" = T1."StartDate"
             AND S1."Seq" = 1 )
    WHEN MATCHED THEN UPDATE SET
    T1."ProdIncr" = S1."ProdIncr"
    ;


    -- 記錄程式結束時間
    JOB_END_TIME := SYSTIMESTAMP;

    commit;

    -- 例外處理
    Exception
    WHEN OTHERS THEN
    ERROR_MSG := SQLERRM || CHR(13) || CHR(10) || dbms_utility.format_error_backtrace;
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_FacProd_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;





/
