--上傳檔案去更新月報額度明細
CREATE OR REPLACE PROCEDURE "Usp_L7_UploadToMonthlyFacBal_Upd" 
(
    -- 參數
    TYYMM          IN  INT,        -- 本月資料年月(西元)
    FromTXCD       IN  VARCHAR2,   -- 來自的交易代號
    EmpNo          IN  VARCHAR2,   -- 經辦
    JobTxSeq       IN  VARCHAR2    -- 啟動批次的交易序號
)
AS
BEGIN
  DECLARE
    INS_CNT        INT;         -- 新增筆數
    JOB_START_TIME TIMESTAMP;   -- 記錄程式起始時間
    JOB_END_TIME   TIMESTAMP;   -- 記錄程式結束時間

  BEGIN
    -- 記錄程式起始時間
    JOB_START_TIME := SYSTIMESTAMP;


    --L7206 利害關係人 維護MonthlyFacBal.BankRelationFlag
    IF FromTxCD = 'L7206' THEN 
     DBMS_OUTPUT.PUT_LINE('L7206');
     
     DBMS_OUTPUT.PUT_LINE('MonthlyFacBal.BankRelationFlag');
        MERGE INTO "MonthlyFacBal" M
        USING (
          SELECT M."YearMonth"
               , M."CustNo"
               , M."FacmNo" 
               , CASE 
                   WHEN R."Id" IS NOT NULL
                   THEN 'Y'
                   ELSE 'N'
                 END AS "BankRelationFlag"
          FROM "MonthlyFacBal" M
          LEFT JOIN "CustMain" CM ON CM."CustNo" = M."CustNo"
          LEFT JOIN(
              SELECT DISTINCT
                  "Id"
              FROM
                  (
                      SELECT DISTINCT
                          "HeadId"     AS "Id"
                      FROM
                          "LifeRelHead"
                      WHERE
                          trunc("AcDate" / 100) = TYYMM
                      UNION ALL
                      SELECT DISTINCT
                          "RelId"     AS "Id"
                      FROM
                          "LifeRelHead"
                      WHERE
                          trunc("AcDate" / 100) = TYYMM
                      UNION ALL
                      SELECT DISTINCT
                          "BusId"     AS "Id"
                      FROM
                          "LifeRelHead"
                      WHERE
                          trunc("AcDate" / 100) = TYYMM
                  )
                  WHERE
                  "Id" <> '-'
          )  R ON R."Id" = CM."CustId"
          WHERE M."YearMonth" = TYYMM
        ) TMP
        ON (
          TMP."YearMonth" = M."YearMonth"
          AND TMP."CustNo" = M."CustNo"
          AND TMP."FacmNo" = M."FacmNo"
        )
        WHEN MATCHED THEN UPDATE SET
        "BankRelationFlag" = TMP."BankRelationFlag";
    END IF;
    
    --L7205 上傳資產五分類 
    --維護 MonthlyFacBal.GovProjectFlag
    --維護 MonthlyFacBal.BuildingFlag
    --維護 MonthlyFacBal.SpecialAssetFlag
    IF FromTxCD = 'L7205' THEN 
     DBMS_OUTPUT.PUT_LINE('L7205');
     
        DBMS_OUTPUT.PUT_LINE('MonthlyFacBal.GovProjectFlag');
        MERGE INTO "MonthlyFacBal" M
        USING (
          SELECT "YearMonth"
                ,"CustNo"
                ,"FacmNo"
                --政策性貸款、大於(含)一百年：Y
                --政策性貸款、小於民國一百年：C
                --非政策性貸款：N
                ,CASE 
                   WHEN "SpecialKind" = 'Z' 
                    AND "OverHundredYear" = 'Y'
                   THEN 'Y'
                   WHEN "SpecialKind" = 'Z' 
                    AND "OverHundredYear" = 'N'
                   THEN 'C'
                   ELSE 'N'
                END AS "GovProjectFlag"
          FROM (      
              SELECT M."YearMonth"
                   , M."CustNo"
                   , M."FacmNo" 
                   , CASE 
                       WHEN FM."FirstDrawdownDate" >=20110101
                       THEN 'Y'
                       ELSE 'N'
                     END AS "OverHundredYear"
                   , CASE 
                       --參考"備呆提存比率1.5"報表
                       WHEN REGEXP_LIKE(M."ProdNo",'8[1-9]') 
                       THEN 'Z'
                       WHEN REGEXP_LIKE(M."ProdNo",'I[A-Z]') 
                       THEN 'Z'
                       WHEN M."AcctCode" = '990' 
                        AND M."FacAcctCode" = '340'
                       THEN 'Z'
                       WHEN M."AcctCode" = '340' 
                       THEN 'Z'
                       ELSE 'N'
                     END AS "SpecialKind"  
              FROM "MonthlyFacBal" M
              LEFT JOIN "FacMain" FM ON FM."CustNo" = M."CustNo"
                                    AND FM."FacmNo" = M."FacmNo"
              WHERE M."YearMonth" = TYYMM
          )
        ) TMP
        ON (
          TMP."YearMonth" = M."YearMonth"
          AND TMP."CustNo" = M."CustNo" 
          AND TMP."FacmNo" = M."FacmNo"
        )
        WHEN MATCHED THEN UPDATE SET
        "GovProjectFlag" = TMP."GovProjectFlag";

        DBMS_OUTPUT.PUT_LINE('MonthlyFacBal.BuildingFlag');
        MERGE INTO "MonthlyFacBal" M
        USING (
              SELECT M."YearMonth"
                   , M."CustNo"
                   , M."FacmNo" 
                   , CASE 
                       --參考"備呆提存比率1.5"報表
                       WHEN CDI."IndustryCode" IS NOT NULL  
                       THEN 'Y'
                     ELSE 'N' END AS "BuildingFlag"  
              FROM "MonthlyFacBal" M
              LEFT JOIN "CustMain" CM ON CM."CustNo" = M."CustNo"
              LEFT JOIN ( SELECT DISTINCT SUBSTR("IndustryCode",3,4) AS "IndustryCode"
                               ,"IndustryItem"
                         FROM "CdIndustry"
                         WHERE "IndustryItem" LIKE '%不動產%'
                            OR "IndustryItem" LIKE '%建築%'
                         ) CDI ON CDI."IndustryCode" = SUBSTR(CM."IndustryCode",3,4)
              WHERE M."YearMonth" = TYYMM
        ) TMP
        ON (
          TMP."YearMonth" = M."YearMonth"
          AND TMP."CustNo" = M."CustNo" 
          AND TMP."FacmNo" = M."FacmNo"
        )
        WHEN MATCHED THEN UPDATE SET
        "BuildingFlag" = TMP."BuildingFlag";
     
       DBMS_OUTPUT.PUT_LINE('MonthlyFacBal.SpecialAssetFlag');
        MERGE INTO "MonthlyFacBal" M
        USING (
            SELECT M."YearMonth"
                  ,M."CustNo"
                  ,M."FacmNo"
                  , CASE
                        --建築貸款
                        WHEN CDI."IndustryCode" IS NOT NULL THEN 'Y' 
                        --民國一百年後政策性貸款
                        WHEN (REGEXP_LIKE(M."ProdNo",'I[A-Z]')
                          OR REGEXP_LIKE(M."ProdNo",'8[1]')
                          OR (M."AcctCode" = '990' AND M."FacAcctCode" = '340')
                          OR M."AcctCode" = '340')
                          AND F."FirstDrawdownDate" >= 20110101 THEN 'N' 
                        WHEN M."ClCode1" IN (3,4) THEN 'N' 
                        --購置不動產+修繕貸款
                        WHEN M."ClCode1" IN (1,2) 
                          AND F."UsageCode" = '02' THEN 'Y'
                        --個金不動產抵押貸款 和 股票質押
                        ELSE 'N'  END        AS "SpecialAssetFlag"
              FROM "MonthlyFacBal" M
              LEFT JOIN "FacMain" F ON F."CustNo" = M."CustNo"
                                   AND F."FacmNo" = M."FacmNo"
              LEFT JOIN "CustMain" CM ON CM."CustNo" = M."CustNo"
              LEFT JOIN ( SELECT DISTINCT SUBSTR("IndustryCode",3,4) AS "IndustryCode"
                                ,"IndustryItem"
                          FROM "CdIndustry"
                          WHERE "IndustryItem" LIKE '%不動產%'
                             OR "IndustryItem" LIKE '%建築%'
                          ) CDI ON CDI."IndustryCode" = SUBSTR(CM."IndustryCode",3,4)
                               AND M."ClCode" IN ('1','2')
              WHERE  M."YearMonth" = TYYMM 
        ) TMP
        ON (
          TMP."YearMonth" = M."YearMonth"
          AND TMP."CustNo" = M."CustNo" 
          AND TMP."FacmNo" = M."FacmNo"
        )
        WHEN MATCHED THEN UPDATE SET
        "SpecialAssetFlag" = TMP."SpecialAssetFlag";
    END IF;
 
    -- 記錄程式結束時間
    JOB_END_TIME := SYSTIMESTAMP;

    commit;

    -- 例外處理
    Exception
    WHEN OTHERS THEN
    "Usp_L9_UspErrorLog_Ins"(
        'Usp_L7_UploadToMonthlyFacBal_Upd' -- UspName 預存程序名稱
      , SQLCODE -- Sql Error Code (固定值)
      , SQLERRM -- Sql Error Message (固定值)
      , dbms_utility.format_error_backtrace -- Sql Error Trace (固定值)
      , EmpNo -- 發動預存程序的員工編號
      , JobTxSeq -- 啟動批次的交易序號
    );
    COMMIT;
    RAISE;
  END;
END;

