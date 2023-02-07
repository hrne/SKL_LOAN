--------------------------------------------------------
--  DDL for Procedure Usp_Tf_AchAuthLogHistory_Ins
--------------------------------------------------------
set define off;

CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_Tf_AchAuthLogHistory_Ins" 
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
    EXECUTE IMMEDIATE 'ALTER TABLE "AchAuthLogHistory" DISABLE PRIMARY KEY CASCADE'; 
    EXECUTE IMMEDIATE 'TRUNCATE TABLE "AchAuthLogHistory" DROP STORAGE'; 
    EXECUTE IMMEDIATE 'ALTER TABLE "AchAuthLogHistory" ENABLE PRIMARY KEY'; 
 
    -- 寫入資料 
    INSERT INTO "AchAuthLogHistory" (
           "LogNo" 
          ,"CustNo"              -- 戶號 DECIMAL 7  
          ,"FacmNo"              -- 額度號碼 DECIMAL 3  
          ,"AuthCreateDate"      -- 建檔日期 Decimald 8  
          ,"RepayBank"           -- 扣款銀行 VARCHAR2 3  
          ,"RepayAcct"           -- 扣款帳號 VARCHAR2 14  
          ,"CreateFlag"          -- 新增或取消 VARCHAR2 1  
          ,"ProcessDate"         -- 處理日期時間 Decimald 8    
          ,"StampFinishDate"     -- 核印完成日期時間 Decimald 8    
          ,"AuthStatus"          -- 授權狀態 VARCHAR2 1  
          ,"AuthMeth"            -- 授權方式 VARCHAR2 1  
          ,"MediaCode"           -- 媒體碼 VARCHAR2 1  
          ,"BatchNo"             -- 批號 VARCHAR2 6  
          ,"PropDate"            -- 提出日期 Decimald 8  
          ,"RetrDate"            -- 提回日期 Decimald 8  
          ,"DeleteDate"          -- 刪除日期 Decimald 8  
          ,"RelationCode"        -- 與借款人關係 VARCHAR2 2  
          ,"RelAcctName"         -- 第三人帳戶戶名 NVARCHAR2 100  
          ,"RelationId"          -- 第三人身分證字號 VARCHAR2 10 0 
          ,"RelAcctBirthday"     -- 第三人出生日期 Decimald 8  
          ,"RelAcctGender"       -- 第三人性別 VARCHAR2 1  
          ,"AmlRsp"              -- AML回應碼 VARCHAR2 1 
          ,"CreateEmpNo"         -- 建立者櫃員編號 VARCHAR2 6 0 
          ,"CreateDate"          -- 建檔日期 DATE 0 0 
          ,"LastUpdateEmpNo"     -- 修改者櫃員編號 VARCHAR2 6 0 
          ,"LastUpdate"          -- 異動日期 DATE 0 0 
          ,"ProcessTime" 
          ,"LimitAmt"            -- 每筆扣款限額 DECIMAL 14  
    )
    SELECT "AchAuthLogHistory_SEQ".nextval AS "LogNo" 
          ,S1."CustNo"                    AS "CustNo"              -- 戶號 DECIMAL 7  
          ,S1."FacmNo"                    AS "FacmNo"              -- 額度號碼 DECIMAL 3  
          ,S1."AuthCreateDate"            AS "AuthCreateDate"      -- 建檔日期 Decimald 8  
          ,S1."RepayBank"                 AS "RepayBank"           -- 扣款銀行 VARCHAR2 3  
          ,S1."RepayAcct"                 AS "RepayAcct"           -- 扣款帳號 VARCHAR2 14  
          ,S1."CreateFlag"                AS "CreateFlag"          -- 新增或取消 VARCHAR2 1  
          ,S1."ProcessDate"               AS "ProcessDate"         -- 處理日期時間 Decimald 8    
          ,S1."StampFinishDate"           AS "StampFinishDate"     -- 核印完成日期時間 Decimald 8    
          ,S1."AuthStatus"                AS "AuthStatus"          -- 授權狀態 VARCHAR2 1  
          ,S1."AuthMeth"                  AS "AuthMeth"            -- 授權方式 VARCHAR2 1  
          ,S1."MediaCode"                 AS "MediaCode"           -- 媒體碼 VARCHAR2 1  
          ,S1."BatchNo"                   AS "BatchNo"             -- 批號 VARCHAR2 6  
          ,S1."PropDate"                  AS "PropDate"            -- 提出日期 Decimald 8  
          ,S1."RetrDate"                  AS "RetrDate"            -- 提回日期 Decimald 8  
          ,S1."DeleteDate"                AS "DeleteDate"          -- 刪除日期 Decimald 8  
          ,S1."RelationCode"              AS "RelationCode"        -- 與借款人關係 VARCHAR2 2  
          ,S1."RelAcctName"               AS "RelAcctName"         -- 第三人帳戶戶名 NVARCHAR2 100  
          ,S1."RelationId"                AS "RelationId"          -- 第三人身分證字號 VARCHAR2 10 0 
          ,S1."RelAcctBirthday"           AS "RelAcctBirthday"     -- 第三人出生日期 Decimald 8  
          ,S1."RelAcctGender"             AS "RelAcctGender"       -- 第三人性別 VARCHAR2 1  
          ,S1."AmlRsp"                    AS "AmlRsp"              -- AML回應碼 VARCHAR2 1 
          ,S1."CreateEmpNo"               AS "CreateEmpNo"         -- 建立者櫃員編號 VARCHAR2 6 0 
          ,S1."CreateDate"                AS "CreateDate"          -- 建檔日期 DATE 0 0 
          ,S1."LastUpdateEmpNo"           AS "LastUpdateEmpNo"     -- 修改者櫃員編號 VARCHAR2 6 0 
          ,S1."LastUpdate"                AS "LastUpdate"          -- 異動日期 DATE 0 0 
          ,S1."ProcessTime"               AS "ProcessTime" 
          ,S1."LimitAmt"                  AS "LimitAmt"            -- 每筆扣款限額 DECIMAL 14  
    FROM "AchAuthLog" S1 
    WHERE S1."RetrDate" > 0 -- 2022-09-22 Wei From 家興 
    ; 
 
    -- 記錄寫入筆數 
    INS_CNT := INS_CNT + sql%rowcount; 
 
    merge into "BankAuthAct" T 
    using ( 
        WITH achData AS ( 
            select "AuthCreateDate" 
                , "CustNo" 
                , "RepayBank" 
                , "RepayAcct" 
                , "AuthStatus" 
                , ROW_NUMBER() 
                  OVER ( 
                    PARTITION BY "CustNo" 
                              , "RepayAcct" 
                    ORDER BY "ProcessDate" DESC 
                           , "AuthCreateDate" DESC 
                  ) AS "Seq" 
            from "AchAuthLog" 
        ) 
        , countAch AS ( 
            select "CustNo" 
                , "RepayAcct" 
                , COUNT(*) AS "Cnt" 
            from achData 
            group by "CustNo" 
                  , "RepayAcct" 
        ) 
        , baaData AS ( 
            select DISTINCT 
                  "CustNo" 
                , "RepayAcct" 
                , "Status" 
            from "BankAuthAct" 
        ) 
        , tmpData AS ( 
            select achData."AuthCreateDate" 
                , achData."CustNo" 
                , achData."RepayBank" 
                , achData."RepayAcct" 
                , achData."AuthStatus" 
            from achData 
            left join baaData on baaData."CustNo" = achData."CustNo" 
                            AND baaData."RepayAcct" = achData."RepayAcct" 
            where achData."Seq" = 1 
              and NVL(achData."AuthStatus",'X') != NVL(BaaData."Status",'X') 
        ) 
        select tmpData."CustNo" 
             , tmpData."RepayBank" 
             , tmpData."RepayAcct" 
             , tmpData."AuthCreateDate" 
             , tmpData."AuthStatus" 
        from tmpData  
        left join countAch ca on ca."CustNo" = tmpData."CustNo" 
                            and ca."RepayAcct" = tmpData."RepayAcct" 
--        where ca."Cnt" = 1 
    ) s 
    on ( 
        s."CustNo" = t."CustNo" 
        and s."RepayBank" = t."RepayBank" 
        and s."RepayAcct" = t."RepayAcct" 
    ) 
    when matched then update 
    set "Status" = NVL(S."AuthStatus",' ') 
    ; 
 
    merge into "AchAuthLog" T 
    using ( 
        WITH achData AS ( 
            select "AuthCreateDate" 
                , "CustNo" 
                , "RepayBank" 
                , "RepayAcct" 
                , "CreateFlag" 
                , "AuthStatus" 
                , ROW_NUMBER() 
                  OVER ( 
                    PARTITION BY "CustNo" 
                              , "RepayAcct" 
                    ORDER BY "ProcessDate" DESC 
                           , "AuthCreateDate" DESC 
                  ) AS "Seq" 
            from "AchAuthLog" 
        ) 
        , countAch AS ( 
            select "CustNo" 
                , "RepayAcct" 
                , COUNT(*) AS "Cnt" 
            from achData 
            group by "CustNo" 
                  , "RepayAcct" 
        ) 
        , baaData AS ( 
            select DISTINCT 
                  "CustNo" 
                , "RepayAcct" 
                , "Status" 
            from "BankAuthAct" 
        ) 
        , tmpData AS ( 
            select achData."AuthCreateDate" 
                , achData."CustNo" 
                , achData."CreateFlag" 
                , achData."RepayBank" 
                , achData."RepayAcct" 
                , achData."AuthStatus" 
            from achData 
            left join baaData on baaData."CustNo" = achData."CustNo" 
                            AND baaData."RepayAcct" = achData."RepayAcct" 
            where achData."Seq" = 1 
              and NVL(achData."AuthStatus",'null') != NVL(BaaData."Status",'null') 
        ) 
        select ad."CustNo" 
             , ad."RepayAcct" 
             , ad."AuthCreateDate" 
             , ad."CreateFlag" 
             , ad."RepayBank" 
        from achData ad  
        left join countAch ca on ca."CustNo" = ad."CustNo" 
                             and ca."RepayAcct" = ad."RepayAcct" 
        where ca."Cnt" >= 2 
          and ad."Seq" > 1 
    ) s 
    on ( 
        s."CustNo" = t."CustNo" 
        and s."RepayAcct" = t."RepayAcct" 
        and s."AuthCreateDate" = t."AuthCreateDate" 
        and s."CreateFlag" = t."CreateFlag" 
        and s."RepayBank" = t."RepayBank" 
    ) 
    when matched then 
    update set T."LastUpdateEmpNo" = '000000' 
    ; 
    delete from "AchAuthLog" where "LastUpdateEmpNo" = '000000'; 
    -- 記錄程式結束時間 
    JOB_END_TIME := SYSTIMESTAMP; 
 
    commit; 
 
    END; 
    -- 例外處理 
    Exception 
    WHEN OTHERS THEN 
    ERROR_MSG := SQLERRM || CHR(13) || CHR(10) || dbms_utility.format_error_backtrace; 
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_AchAuthLogHistory_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace); 
END;

/
