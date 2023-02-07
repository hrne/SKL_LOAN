--------------------------------------------------------
--  DDL for Procedure Usp_Tf_InnReCheck_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_Tf_InnReCheck_Ins" 
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
        "tLmnDyf" DECIMAL(8); --西元上月月底日
        "tTbsDyf" DECIMAL(8); --西元帳務日
        "tLastMonth" DECIMAL(2); -- 上月月份
    BEGIN

    SELECT "LmnDyf"
    INTO "tLmnDyf"
    FROM "TxBizDate"
    WHERE "DateCode" = 'ONLINE'
    ;

    SELECT "TbsDyf"
    INTO "tTbsDyf"
    FROM "TxBizDate"
    WHERE "DateCode" = 'ONLINE'
    ;

    SELECT TO_NUMBER(SUBSTR(TO_CHAR("tLmnDyf"),5,2))
    INTO "tLastMonth"
    FROM DUAL
    ;

    -- 刪除舊資料
    EXECUTE IMMEDIATE 'ALTER TABLE "InnReCheck" DISABLE PRIMARY KEY CASCADE';
    EXECUTE IMMEDIATE 'TRUNCATE TABLE "InnReCheck" DROP STORAGE';
    EXECUTE IMMEDIATE 'ALTER TABLE "InnReCheck" ENABLE PRIMARY KEY';

    -- 寫入資料
    INSERT INTO "InnReCheck" (
        "YearMonth"           -- 資料年月 Decimal 6 0
      , "ConditionCode"       -- 條件代碼 Decimal 2 0
      , "CustNo"              -- 借款人戶號 Decimal 7 0
      , "FacmNo"              -- 額度號碼 Decimal 3 0
      , "ReCheckCode"         -- 覆審記號 VARCHAR2 1 0
      , "FollowMark"          -- 追蹤記號 VARCHAR2 1 0
      , "ReChkYearMonth"      -- 覆審年月 Decimal 6 0
      , "DrawdownDate"        -- 撥款日期 DecimalD 8 0
      , "LoanBal"             -- 貸放餘額 Decimal 16 2
      , "Evaluation"          -- 評等 Decimal 2 0       須從eLoan匯入
      , "CustTypeItem"        -- 客戶別 NVARCHAR2 10 0
      , "UsageItem"           -- 用途別 NVARCHAR2 10 0
      , "CityItem"            -- 地區別 NVARCHAR2 10 0
      , "ReChkUnit"           -- 應覆審單位 NVARCHAR2 10 0
      , "SpecifyFg"           -- 指定複審記號 VARCHAR2 2	 Y-指定覆審 null-非指定
      , "Remark"              -- 備註 NVARCHAR2 60 0
      , "TraceMonth"          -- 追蹤年月 Decimal 6 FollowMark=2時輸入
      , "CreateDate"          -- 建檔日期時間 DATE 0 0
      , "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 0
      , "LastUpdate"          -- 最後更新日期時間 DATE 0 0
      , "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 0
    )
    SELECT TRUNC("tLmnDyf" / 100)         AS "YearMonth"           -- 資料年月 Decimal 6 0
          ,S0."DA$RTP"                    AS "ConditionCode"       -- 條件代碼 Decimal 2 0
          ,S0."LMSACN"                    AS "CustNo"              -- 借款人戶號 Decimal 7 0
          ,S0."LMSAPN"                    AS "FacmNo"              -- 額度號碼 Decimal 3 0
          ,''                             AS "ReCheckCode"         -- 覆審記號 VARCHAR2 1 0
          ,''                             AS "FollowMark"          -- 追蹤記號 VARCHAR2 1 0
          ,CASE
             WHEN S0."REVWMM" <= "tLastMonth" THEN TO_NUMBER(TO_CHAR(TRUNC("tTbsDyf" / 10000)) || LPAD(S0."REVWMM",2,'0'))
           ELSE TO_NUMBER(TO_CHAR(TRUNC("tTbsDyf" / 10000) - 1) || LPAD(S0."REVWMM",2,'0')) END
                                          AS "ReChkYearMonth"      -- 覆審年月 Decimal 6 0
          ,19110000 + S1."LMSLLD"         AS "DrawdownDate"        -- 撥款日期 DecimalD 8 0
          ,S0."SumLMSLBL"                 AS "LoanBal"             -- 貸放餘額 Decimal 16 2
          ,0                              AS "Evaluation"          -- 評等 Decimal 2 0       須從eLoan匯入
          ,CC."Item"                      AS "CustTypeItem"        -- 客戶別 NVARCHAR2 10 0
          ,u''                            AS "UsageItem"           -- 用途別 NVARCHAR2 10 0
          ,S1."LGTCTY"                    AS "CityItem"            -- 地區別 NVARCHAR2 10 0
          ,S1."UNTUTC"                    AS "ReChkUnit"           -- 應覆審單位 NVARCHAR2 10 0
          ,''                             AS "SpecifyFg"           -- 指定複審記號 VARCHAR2 2	 Y-指定覆審 null-非指定
          ,S1."DTARSN"                    AS "Remark"              -- 備註 NVARCHAR2 60 0
          ,0                              AS "TraceMonth"          -- 追蹤年月 Decimal 6 FollowMark=2時輸入
          ,CASE
             WHEN S1."CRTDTM" > 0
             THEN TO_DATE(S1."CRTDTM",'YYYYMMDDHH24MISS')
           ELSE JOB_START_TIME
           END                            AS "CreateDate"          -- 建檔日期時間 DATE 0 0
          ,NVL(AEM1."EmpNo",'999999')     AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 0
          ,CASE
             WHEN S1."CHGDTM" > 0
             THEN TO_DATE(S1."CHGDTM",'YYYYMMDDHH24MISS')
           ELSE JOB_START_TIME
           END                            AS "LastUpdate"          -- 最後更新日期時間 DATE 0 0
          ,NVL(AEM2."EmpNo",'999999')     AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 0
    FROM (SELECT "REVWMM"
                ,"DA$RTP"
                ,"LMSACN"
                ,"LMSAPN"
                ,SUM(LMSLBL) AS "SumLMSLBL"
          FROM "LNMRVHP"
          GROUP BY "REVWMM"
                  ,"DA$RTP"
                  ,"LMSACN"
                  ,"LMSAPN"
         ) S0
    LEFT JOIN (SELECT "REVWMM"
                     ,"DA$RTP"
                     ,"LMSACN"
                     ,"LMSAPN"
                     ,"LMSLLD" -- 撥款日期
                     ,"REVECD" -- 客戶別
                     ,"LGTCTY" -- 地區別
                     ,"UNTUTC" -- 應覆審單位
                     ,"DTARSN" -- 備註
                     ,"CRTEMP" -- 建立者櫃員編號
                     ,"CRTDTM" -- 建立日期時間  
                     ,"CHGEMP" -- 修改者櫃員編號
                     ,"CHGDTM" -- 修改日期時間  
                     ,ROW_NUMBER() OVER (PARTITION BY "REVWMM"
                                                     ,"DA$RTP"
                                                     ,"LMSACN"
                                                     ,"LMSAPN"
                                         ORDER BY "LMSLLD" ASC) AS "Seq"
               FROM "LNMRVHP"
              ) S1 ON S1."REVWMM" = S0."REVWMM"
                  AND S1."DA$RTP" = S0."DA$RTP"
                  AND S1."LMSACN" = S0."LMSACN"
                  AND S1."LMSAPN" = S0."LMSAPN"
                  AND S1."Seq"    = 1
    LEFT JOIN "CdCode" CC ON CC."DefCode" = 'CustTypeCode'
                         AND CC."Code" = S1."REVECD"
    LEFT JOIN "As400EmpNoMapping" AEM1 ON AEM1."As400TellerNo" = S1."CRTEMP"
    LEFT JOIN "As400EmpNoMapping" AEM2 ON AEM2."As400TellerNo" = S1."CHGEMP"
    ;

    -- 記錄寫入筆數
    INS_CNT := INS_CNT + sql%rowcount;

    -- 寫入6:土地
    -- 寫入資料
    INSERT INTO "InnReCheck" (
        "YearMonth"           -- 資料年月 Decimal 6 0
      , "ConditionCode"       -- 條件代碼 Decimal 2 0
      , "CustNo"              -- 借款人戶號 Decimal 7 0
      , "FacmNo"              -- 額度號碼 Decimal 3 0
      , "ReCheckCode"         -- 覆審記號 VARCHAR2 1 0
      , "FollowMark"          -- 追蹤記號 VARCHAR2 1 0
      , "ReChkYearMonth"      -- 覆審年月 Decimal 6 0
      , "DrawdownDate"        -- 撥款日期 DecimalD 8 0
      , "LoanBal"             -- 貸放餘額 Decimal 16 2
      , "Evaluation"          -- 評等 Decimal 2 0       須從eLoan匯入
      , "CustTypeItem"        -- 客戶別 NVARCHAR2 10 0
      , "UsageItem"           -- 用途別 NVARCHAR2 10 0
      , "CityItem"            -- 地區別 NVARCHAR2 10 0
      , "ReChkUnit"           -- 應覆審單位 NVARCHAR2 10 0
      , "SpecifyFg"           -- 指定複審記號 VARCHAR2 2	 Y-指定覆審 null-非指定
      , "Remark"              -- 備註 NVARCHAR2 60 0
      , "TraceMonth"          -- 追蹤年月 Decimal 6 FollowMark=2時輸入
      , "CreateDate"          -- 建檔日期時間 DATE 0 0
      , "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 0
      , "LastUpdate"          -- 最後更新日期時間 DATE 0 0
      , "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 0
    )
    SELECT TRUNC("tLmnDyf" / 100)         AS "YearMonth"           -- 資料年月 Decimal 6 0
          ,6                              AS "ConditionCode"       -- 條件代碼 Decimal 2 0
          ,S0."LMSACN"                    AS "CustNo"              -- 借款人戶號 Decimal 7 0
          ,S0."LMSAPN"                    AS "FacmNo"              -- 額度號碼 Decimal 3 0
          ,''                             AS "ReCheckCode"         -- 覆審記號 VARCHAR2 1 0
          ,''                             AS "FollowMark"          -- 追蹤記號 VARCHAR2 1 0
          ,CASE
             WHEN S0."REVWMM" <= "tLastMonth" THEN TO_NUMBER(TO_CHAR(TRUNC("tTbsDyf" / 10000)) || LPAD(S0."REVWMM",2,'0'))
           ELSE TO_NUMBER(TO_CHAR(TRUNC("tTbsDyf" / 10000) - 1) || LPAD(S0."REVWMM",2,'0')) END
                                          AS "ReChkYearMonth"      -- 覆審年月 Decimal 6 0
          ,19110000 + S1."LMSLLD"         AS "DrawdownDate"        -- 撥款日期 DecimalD 8 0
          ,S0."APLLAM"                    AS "LoanBal"             -- 貸放餘額 Decimal 16 2
          ,0                              AS "Evaluation"          -- 評等 Decimal 2 0       須從eLoan匯入
          ,S1."CUSECDDSC"                 AS "CustTypeItem"        -- 客戶別 NVARCHAR2 10 0
          ,S1."TB$FDS"                    AS "UsageItem"           -- 用途別 NVARCHAR2 10 0
          ,S1."LOCLID"                    AS "CityItem"            -- 地區別 NVARCHAR2 10 0
          ,''                             AS "ReChkUnit"           -- 應覆審單位 NVARCHAR2 10 0
          ,''                             AS "SpecifyFg"           -- 指定複審記號 VARCHAR2 2	 Y-指定覆審 null-非指定
          ,S1."DTARSN"                    AS "Remark"              -- 備註 NVARCHAR2 60 0
          ,0                              AS "TraceMonth"          -- 追蹤年月 Decimal 6 FollowMark=2時輸入
          ,CASE
             WHEN S1."CRTDTM" > 0
             THEN TO_DATE(S1."CRTDTM",'YYYYMMDDHH24MISS')
           ELSE JOB_START_TIME
           END                            AS "CreateDate"          -- 建檔日期時間 DATE 0 0
          ,NVL(AEM1."EmpNo",'999999')     AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 0
          ,CASE
             WHEN S1."CHGDTM" > 0
             THEN TO_DATE(S1."CHGDTM",'YYYYMMDDHH24MISS')
           ELSE JOB_START_TIME
           END                            AS "LastUpdate"          -- 最後更新日期時間 DATE 0 0
          ,NVL(AEM2."EmpNo",'999999')     AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 0
    FROM (SELECT "REVWMM"
                ,"LMSACN"
                ,"LMSAPN"
                ,SUM("APLLAM") AS "APLLAM"
          FROM "LNH1480P"
          WHERE "REVWMM" > 0
          GROUP BY "REVWMM"
                  ,"LMSACN"
                  ,"LMSAPN"
         ) S0
    LEFT JOIN (SELECT "REVWMM"
                     ,"LMSACN"
                     ,"LMSAPN"
                     ,"LMSLLD" -- 撥款日期
                     ,"CUSECDDSC"
                     ,"TB$FDS"
                     ,"LOCLID" -- 地區別
                     ,"DTARSN" -- 備註
                     ,"CRTEMP" -- 建立者櫃員編號
                     ,"CRTDTM" -- 建立日期時間  
                     ,"CHGEMP" -- 修改者櫃員編號
                     ,"CHGDTM" -- 修改日期時間  
                     ,ROW_NUMBER() OVER (PARTITION BY "REVWMM"
                                                     ,"LMSACN"
                                                     ,"LMSAPN"
                                         ORDER BY "LMSLLD" ASC) AS "Seq"
               FROM "LNH1480P"
              ) S1 ON S1."REVWMM" = S0."REVWMM"
                  AND S1."LMSACN" = S0."LMSACN"
                  AND S1."LMSAPN" = S0."LMSAPN"
                  AND S1."Seq"    = 1
    LEFT JOIN "As400EmpNoMapping" AEM1 ON AEM1."As400TellerNo" = S1."CRTEMP"
    LEFT JOIN "As400EmpNoMapping" AEM2 ON AEM2."As400TellerNo" = S1."CHGEMP"
    ;

    -- 記錄寫入筆數
    INS_CNT := INS_CNT + sql%rowcount;

    -- 記錄程式結束時間
    JOB_END_TIME := SYSTIMESTAMP;

    commit;

    END;
    -- 例外處理
    Exception
    WHEN OTHERS THEN
    ERROR_MSG := SQLERRM || CHR(13) || CHR(10) || dbms_utility.format_error_backtrace;
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_InnReCheck_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;




/
