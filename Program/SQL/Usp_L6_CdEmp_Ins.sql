create or replace NONEDITIONABLE PROCEDURE "Usp_L6_CdEmp_Ins" 
( 
    -- 參數 
    "EmpNo" IN VARCHAR2,   --執行人員員編 
    JobTxSeq       IN  VARCHAR2    -- 啟動批次的交易序號
--     JOB_START_TIME OUT TIMESTAMP, --程式起始時間 
--     JOB_END_TIME   OUT TIMESTAMP, --程式結束時間 
--     INS_CNT        OUT INT        --新增資料筆數 
) 
AS 
BEGIN 
 
-- exec "Usp_L6_CdEmp_Ins"('DataTf'); 
 
    -- 筆數預設0 
    -- INS_CNT:=0; 
    -- 記錄程式起始時間 
    -- JOB_START_TIME := SYSTIMESTAMP; 
 
    -- 刪除舊資料 
    EXECUTE IMMEDIATE 'ALTER TABLE "CdEmp" DISABLE PRIMARY KEY CASCADE'; 
    EXECUTE IMMEDIATE 'TRUNCATE TABLE "CdEmp" DROP STORAGE'; 
    EXECUTE IMMEDIATE 'ALTER TABLE "CdEmp" ENABLE PRIMARY KEY'; 
 
    -- 寫入資料 
    INSERT INTO "CdEmp" (
        "AgentCode"           -- 業務員代號 VARCHAR2 12 
      , "EmployeeNo"          -- 電腦編號 VARCHAR2 10 0
      , "Fullname"            -- 姓名 VARCHAR2 40 0
      , "CenterShortName"     -- 單位簡稱 VARCHAR2 10 0
      , "CenterCodeName"      -- 單位名稱 VARCHAR2 20 0
      , "CenterCode1"         -- 區部代號 VARCHAR2 6 0
      , "CenterCode1Short"  -- 區部簡稱 VARCHAR2 10 0
      , "CenterCode1Name"    -- 區部名稱 VARCHAR2 20 0
      , "CenterCode2"         -- 部室代號 VARCHAR2 6 0
      , "CenterCode2Short"  -- 部室簡稱 VARCHAR2 10 0
      , "CenterCode2Name"    -- 部室名稱 VARCHAR2 20 0
      , "CenterCodeAcc1"      -- 區部代號(駐在單位) VARCHAR2 6 0
      , "CenterCodeAcc1Name" -- 區部名稱(駐在單位) VARCHAR2 20 0
      , "CenterCodeAcc2"      -- 部室代號(駐在單位) VARCHAR2 6 0
      , "CenterCodeAcc2Name" -- 部室名稱(駐在單位) VARCHAR2 20 0
      , "CommLineCode"        -- 業務線代號 VARCHAR2 2 
      , "CommLineType"        -- 業務線別 VARCHAR2 1 
      , "OrigIntroducerId"   -- 介紹人 VARCHAR2 12 
      , "IntroducerInd"       -- 介紹關係碼 VARCHAR2 1 
      , "RegisterLevel"       -- 報聘職等 VARCHAR2 2 
      , "RegisterDate"        -- 在職/締約日期 decimalD 8 
      , "CenterCode"          -- 單位代號 VARCHAR2 6 0
      , "AdministratId"       -- 單位主管 VARCHAR2 12 0
      , "InputDate"           -- 建檔日期 decimalD 8 0
      , "InputUser"           -- 建檔人 VARCHAR2 8 0
      , "AgStatusCode"        -- 業務人員任用狀況碼 VARCHAR2 1 0
      , "AgStatusDate"        -- 業務人員任用狀況異動日 decimalD 8 0
      , "TranDate"            -- 作業日期(交易日期) decimalD 8 
      , "TranUser"            -- 作業者 VARCHAR2 8 
      , "ReRegisterDate"      -- 再聘日 decimalD 8 
      , "DirectorId"          -- 上層主管 VARCHAR2 12 
      , "DirectorIdF"         -- 主管_財務 VARCHAR2 12 0
      , "IntroducerId"        -- 區主任/上一代主管 VARCHAR2 12 0
      , "IntroducerIdF"       -- 推介人_財務 VARCHAR2 12 0
      , "AgLevel"             -- 業務人員職等 VARCHAR2 2 0
      , "LastLevel"           -- 前次業務人員職等 VARCHAR2 2 0
      , "LevelDate"           -- 職等異動日 decimalD 8 0
      , "TopLevel"            -- 最高職等 VARCHAR2 2 0
      , "OccpInd"             -- 任職型態 VARCHAR2 1 0
      , "QuotaAmt"            -- 責任額 NUMBER 10 0
      , "ApplType"            -- 申請登錄類別 VARCHAR2 1 0
      , "TaxRate"             -- 所得稅率 NUMBER 5 3
      , "SocialInsuClass"     -- 勞保等級 NUMBER 5 0
      , "PromotLevelYM"       -- 職等平階起始年月 VARCHAR2 7 0
      , "DirectorYM"          -- 晉陞主管年月 VARCHAR2 7 0
      , "RecordDate"          -- 登錄日期 decimalD 8 0
      , "ExRecordDate"        -- 發證日期 decimalD 8 0
      , "RxTrDate"            -- 證書日期/測驗日期 decimalD 8 0
      , "ExTrIdent"           -- 證書字號 VARCHAR2 16 0
      , "ExTrIdent2"          -- 中專證號 VARCHAR2 9 0
      , "ExTrIdent3"          -- 投資型證號 VARCHAR2 12 0
      , "ExTrDate"            -- 投資登錄日期 decimalD 8 0
      , "RegisterBefore"      -- 報聘前年資(月表示) NUMBER 5 0
      , "DirectorAfter"       -- 主管年資 NUMBER 5 0
      , "MedicalCode"         -- 免體檢授權碼 VARCHAR2 2 0
      , "ExChgDate"           -- 換證日期 decimalD 8 0
      , "ExDelDate"           -- 註銷日期 decimalD 8 0
      , "ApplCode"            -- 申請業務類別 VARCHAR2 10 0
      , "FirstRegDate"        -- 初次登錄日 decimalD 8 0
      , "AginSource"          -- 業務來源之專案 VARCHAR2 3 0
      , "AguiCenter"          -- 單位代號 VARCHAR2 9 0
      , "AgentId"             -- 業務人員身份證字號 VARCHAR2 10 0
      , "TopId"               -- 業務人員主管 VARCHAR2 12 0
      , "AgDegree"            -- 業務人員職級 VARCHAR2 2 0
      , "CollectInd"          -- 收費員指示碼 VARCHAR2 1 0
      , "AgType1"             -- 制度別 VARCHAR2 1 0
      , "ContractInd"         -- 單雙合約碼 VARCHAR2 1 0
      , "ContractIndYM"       -- 單雙合約異動工作月 VARCHAR2 7 0
      , "AgType2"             -- 身份別 VARCHAR2 1 0
      , "AgType3"             -- 特殊人員碼 VARCHAR2 1 0
      , "AgType4"             -- 新舊制別 VARCHAR2 1 0
      , "AginInd1"            -- 辦事員碼 VARCHAR2 1 0
      , "AgPoInd"             -- 可招攬指示碼 VARCHAR2 1 0
      , "AgDocInd"            -- 齊件否 VARCHAR2 1 0
      , "NewHireType"         -- 新舊人指示碼 VARCHAR2 1 0
      , "AgCurInd"            -- 現職指示碼 VARCHAR2 1 0
      , "AgSendType"          -- 發文類別 VARCHAR2 3 0
      , "AgSendNo"            -- 發文文號 VARCHAR2 80 0
      , "RegisterDate2"       -- 任職日期 decimalD 8 0
      , "AgReturnDate"        -- 回任日期 decimalD 8 0
      , "AgTransferDateF"    -- 初次轉制日期 decimalD 8 0
      , "AgTransferDate"      -- 轉制日期 decimalD 8 0
      , "PromotYM"            -- 初次晉升年月 VARCHAR2 7 0
      , "PromotYMF"           -- 生效業績年月 VARCHAR2 7 0
      , "AgPostChgDate"       -- 職務異動日 decimalD 8 0
      , "FamiliesTax"         -- 扶養人數 NUMBER 5 0
      , "AgentCodeI"          -- 原區主任代號 VARCHAR2 12 0
      , "AgLevelSys"          -- 職等_系統 VARCHAR2 2 0
      , "AgPostIn"            -- 內階職務 VARCHAR2 6 0
      , "CenterCodeAcc"       -- 駐在單位 VARCHAR2 6 0
      , "EvalueInd"           -- 考核特殊碼 VARCHAR2 1 0
      , "EvalueInd1"          -- 辦法優待碼 VARCHAR2 1 0
      , "BatchNo"             -- 批次號碼 NUMBER 10 0
      , "EvalueYM"            -- 考核年月 VARCHAR2 7 0
      , "AgTransferCode"      -- 轉檔碼 VARCHAR2 2 0
      , "Birth"               -- 出生年月日 decimalD 8 0
      , "Education"           -- 學歷 VARCHAR2 1 0
      , "LrInd"               -- 勞退狀況 VARCHAR2 1 0
      , "ProceccDate"         -- 資料處理時間 decimalD 8 0
      , "QuitDate"            -- 離職/停約日 decimalD 8 0
      , "AgPost"              -- 職務 VARCHAR2 2 0
      , "LevelNameChs"        -- 職等中文 VARCHAR2 10 0
      , "LrSystemType"        -- 勞退碼 VARCHAR2 1 0
      , "SeniorityYY"         -- 年資_年 NUMBER 5 0
      , "SeniorityMM"         -- 年資_月 NUMBER 5 0
      , "SeniorityDD"         -- 年資_日 NUMBER 5 0
      , "AglaProcessInd"      -- 登錄處理事項 VARCHAR2 2 0
      , "StatusCode"          -- 登錄狀態 VARCHAR2 1 0
      , "AglaCancelReason"   -- 註銷原因 VARCHAR2 1 0
      , "ISAnnApplDate"       -- 利變年金通報日 decimalD 8 0
      , "RecordDateC"         -- 外幣保單登入日 decimalD 8 0
      , "StopReason"          -- 停招/撤銷原因 VARCHAR2 6 0
      , "StopStrDate"         -- 停止招攬起日 decimalD 8 0
      , "StopEndDate"         -- 停止招攬迄日 decimalD 8 0
      , "IFPDate"             -- IFP登錄日 decimalD 8 0
      , "EffectStrDate"       -- 撤銷起日 decimalD 8 0
      , "EffectEndDate"       -- 撤銷迄日 decimalD 8 0
      , "AnnApplDate"         -- 一般年金通報日 decimalD 8 0
      , "CenterCodeAccName" -- 單位名稱(駐在單位) VARCHAR2 20 0
      , "ReHireCode"          -- 重僱碼 VARCHAR2 1 0
      , "RSVDAdminCode"       -- 特別碼 VARCHAR2 1 0
      , "Account"             -- 帳號 VARCHAR2 16 0
      , "PRPDate"             -- 優體測驗通過日 VARCHAR2 15 0
      , "Zip"                 -- 戶籍地址郵遞區號 VARCHAR2 5 0
      , "Address"             -- 戶籍地址 VARCHAR2 80 0
      , "PhoneH"              -- 住家電話 VARCHAR2 30 0
      , "PhoneC"              -- 手機電話 VARCHAR2 30 0
      , "SalesQualInd"        -- 基金銷售資格碼 VARCHAR2 1 0
      , "AgsqStartDate"       -- 基金銷售資格日 decimalD 8 0
      , "PinYinNameIndi"      -- 原住民羅馬拼音姓名 VARCHAR2 50 0
      , "Email"               -- 電子郵件 VARCHAR2 50
      , "CreateDate"          -- 建檔日期時間 DATE	
      , "CreateEmpNo"         -- 建檔人員 VARCHAR2 6
      , "LastUpdate"          -- 最後更新日期時間 DATE	
      , "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6
    )
    SELECT S2."AGENT_CODE"                 AS "AgentCode"           -- 業務員代號 VARCHAR2 12 
          ,TRIM(S2."EMPLOYEE_NO")          AS "EmployeeNo"          -- 電腦編號 VARCHAR2 10 0
          ,REPLACE(S2."FULLNAME",'○','X')  AS "Fullname"            -- 姓名 VARCHAR2 40 0
          ,S2."CENTER_SHORT_NAME"          AS "CenterShortName"     -- 單位簡稱 VARCHAR2 10 0
          ,S2."CENTER_CODE_NAME"           AS "CenterCodeName"      -- 單位名稱 VARCHAR2 20 0
          ,S2."CENTER_CODE_1"              AS "CenterCode1"         -- 區部代號 VARCHAR2 6 0
          ,S2."CENTER_CODE_1_SHORT"        AS "CenterCode1Short"  -- 區部簡稱 VARCHAR2 10 0
          ,S2."CENTER_CODE_1_NAME"         AS "CenterCode1Name"    -- 區部名稱 VARCHAR2 20 0
          ,S2."CENTER_CODE_2"              AS "CenterCode2"         -- 部室代號 VARCHAR2 6 0
          ,S2."CENTER_CODE_2_SHORT"
                                           AS "CenterCode2Short"  -- 部室簡稱 VARCHAR2 10 0
          ,S2."CENTER_CODE_2_NAME"         AS "CenterCode2Name"    -- 部室名稱 VARCHAR2 20 0
          ,S2."CENTER_CODE_ACC_1"          AS "CenterCodeAcc1"      -- 區部代號(駐在單位) VARCHAR2 6 0
          ,S2."CENTER_CODE_ACC_1_NAME"
                                           AS "CenterCodeAcc1Name" -- 區部名稱(駐在單位) VARCHAR2 20 0
          ,S2."CENTER_CODE_ACC_2"          AS "CenterCodeAcc2"      -- 部室代號(駐在單位) VARCHAR2 6 0
          ,S2."CENTER_CODE_ACC_2_NAME"
                                           AS "CenterCodeAcc2Name" -- 部室名稱(駐在單位) VARCHAR2 20 0
          ,S2."COMM_LINE_CODE"             AS "CommLineCode"        -- 業務線代號 VARCHAR2 2 
          ,S2."COMM_LINE_TYPE"             AS "CommLineType"        -- 業務線別 VARCHAR2 1 
          ,S2."ORIG_INTRODUCER_ID"         AS "OrigIntroducerId"   -- 介紹人 VARCHAR2 12 
          ,S2."INTRODUCER_IND"             AS "IntroducerInd"       -- 介紹關係碼 VARCHAR2 1 
          ,S2."REGISTER_LEVEL"             AS "RegisterLevel"       -- 報聘職等 VARCHAR2 2 
          ,NVL(TO_NUMBER(TO_CHAR(S2."REGISTER_DATE",'YYYYMMDD')),0)
                                           AS "RegisterDate"        -- 在職/締約日期 decimalD 8 
          ,S2."CENTER_CODE"                AS "CenterCode"          -- 單位代號 VARCHAR2 6 0
          ,S2."ADMINISTRAT_ID"             AS "AdministratId"       -- 單位主管 VARCHAR2 12 0
          ,NVL(TO_NUMBER(TO_CHAR(S2."INPUT_DATE",'YYYYMMDD')),0)
                                           AS "InputDate"           -- 建檔日期 decimalD 8 0
          ,S2."INPUT_USER"                 AS "InputUser"           -- 建檔人 VARCHAR2 8 0
          ,S2."AG_STATUS_CODE"             AS "AgStatusCode"        -- 業務人員任用狀況碼 VARCHAR2 1 0
          ,NVL(TO_NUMBER(TO_CHAR(S2."AG_STATUS_DATE",'YYYYMMDD')),0)
                                           AS "AgStatusDate"        -- 業務人員任用狀況異動日 decimalD 8 0
          ,NVL(TO_NUMBER(TO_CHAR(S2."TRAN_DATE",'YYYYMMDD')),0)
                                           AS "TranDate"            -- 作業日期(交易日期) decimalD 8 
          ,S2."TRAN_USER"                  AS "TranUser"            -- 作業者 VARCHAR2 8 
          ,NVL(TO_NUMBER(TO_CHAR(S2."RE_REGISTER_DATE",'YYYYMMDD')),0)
                                           AS "ReRegisterDate"      -- 再聘日 decimalD 8 
          ,S2."DIRECTOR_ID"                AS "DirectorId"          -- 上層主管 VARCHAR2 12 
          ,S2."DIRECTOR_ID_F"              AS "DirectorIdF"         -- 主管_財務 VARCHAR2 12 0
          ,S2."INTRODUCER_ID"              AS "IntroducerId"        -- 區主任/上一代主管 VARCHAR2 12 0
          ,S2."INTRODUCER_ID_F"            AS "IntroducerIdF"       -- 推介人_財務 VARCHAR2 12 0
          ,S2."AG_LEVEL"                   AS "AgLevel"             -- 業務人員職等 VARCHAR2 2 0
          ,S2."LAST_LEVEL"                 AS "LastLevel"           -- 前次業務人員職等 VARCHAR2 2 0
          ,NVL(TO_NUMBER(TO_CHAR(S2."LEVEL_DATE",'YYYYMMDD')),0)
                                           AS "LevelDate"           -- 職等異動日 decimalD 8 0
          ,S2."TOP_LEVEL"                  AS "TopLevel"            -- 最高職等 VARCHAR2 2 0
          ,S2."OCCP_IND"                   AS "OccpInd"             -- 任職型態 VARCHAR2 1 0
          ,S2."QUOTA_AMT"                  AS "QuotaAmt"            -- 責任額 NUMBER 10 0
          ,S2."APPL_TYPE"                  AS "ApplType"            -- 申請登錄類別 VARCHAR2 1 0
          ,S2."TAX_RATE"                   AS "TaxRate"             -- 所得稅率 NUMBER 5 3
          ,S2."SOCIAL_INSU_CLASS"          AS "SocialInsuClass"     -- 勞保等級 NUMBER 5 0
          ,S2."PROMOT_LEVEL_YM"            AS "PromotLevelYM"       -- 職等平階起始年月 VARCHAR2 7 0
          ,S2."DIRECTOR_YM"                AS "DirectorYM"          -- 晉陞主管年月 VARCHAR2 7 0
          ,NVL(TO_NUMBER(TO_CHAR(S2."RECORD_DATE",'YYYYMMDD')),0)
                                           AS "RecordDate"          -- 登錄日期 decimalD 8 0
          ,NVL(TO_NUMBER(TO_CHAR(S2."EX_RECORD_DATE",'YYYYMMDD')),0)
                                           AS "ExRecordDate"        -- 發證日期 decimalD 8 0
          ,NVL(TO_NUMBER(TO_CHAR(S2."EX_TR_DATE",'YYYYMMDD')),0)
                                           AS "RxTrDate"            -- 證書日期/測驗日期 decimalD 8 0
          ,S2."EX_TR_IDENT"                AS "ExTrIdent"           -- 證書字號 VARCHAR2 16 0
          ,S2."EX_TR_IDENT2"               AS "ExTrIdent2"          -- 中專證號 VARCHAR2 9 0
          ,S2."EX_TR_IDENT3"               AS "ExTrIdent3"          -- 投資型證號 VARCHAR2 12 0
          ,NVL(TO_NUMBER(TO_CHAR(S2."EX_TR_DATE3",'YYYYMMDD')),0)
                                           AS "ExTrDate"            -- 投資登錄日期 decimalD 8 0
          ,S2."REGISTER_BEFORE"            AS "RegisterBefore"      -- 報聘前年資(月表示) NUMBER 5 0
          ,S2."DIRECTOR_AFTER"             AS "DirectorAfter"       -- 主管年資 NUMBER 5 0
          ,S2."MEDICAL_CODE"               AS "MedicalCode"         -- 免體檢授權碼 VARCHAR2 2 0
          ,NVL(TO_NUMBER(TO_CHAR(S2."EX_CHG_DATE",'YYYYMMDD')),0)
                                           AS "ExChgDate"           -- 換證日期 decimalD 8 0
          ,NVL(TO_NUMBER(TO_CHAR(S2."EX_DEL_DATE",'YYYYMMDD')),0)
                                           AS "ExDelDate"           -- 註銷日期 decimalD 8 0
          ,S2."APPL_CODE"                  AS "ApplCode"            -- 申請業務類別 VARCHAR2 10 0
          ,NVL(TO_NUMBER(TO_CHAR(S2."FIRST_REG_DATE",'YYYYMMDD')),0)
                                           AS "FirstRegDate"        -- 初次登錄日 decimalD 8 0
          ,S2."AGIN_SOURCE"                AS "AginSource"          -- 業務來源之專案 VARCHAR2 3 0
          ,S2."AGUI_CENTER"                AS "AguiCenter"          -- 單位代號 VARCHAR2 9 0
          ,S2."AGENT_ID"                   AS "AgentId"             -- 業務人員身份證字號 VARCHAR2 10 0
          ,S2."TOP_ID"                     AS "TopId"               -- 業務人員主管 VARCHAR2 12 0
          ,S2."AG_DEGREE"                  AS "AgDegree"            -- 業務人員職級 VARCHAR2 2 0
          ,S2."COLLECT_IND"                AS "CollectInd"          -- 收費員指示碼 VARCHAR2 1 0
          ,S2."AG_TYPE_1"                  AS "AgType1"             -- 制度別 VARCHAR2 1 0
          ,S2."CONTRACT_IND"               AS "ContractInd"         -- 單雙合約碼 VARCHAR2 1 0
          ,S2."CONTRACT_IND_YM"            AS "ContractIndYM"       -- 單雙合約異動工作月 VARCHAR2 7 0
          ,S2."AG_TYPE_2"                  AS "AgType2"             -- 身份別 VARCHAR2 1 0
          ,S2."AG_TYPE_3"                  AS "AgType3"             -- 特殊人員碼 VARCHAR2 1 0
          ,S2."AG_TYPE_4"                  AS "AgType4"             -- 新舊制別 VARCHAR2 1 0
          ,S2."AGIN_IND1"                  AS "AginInd1"            -- 辦事員碼 VARCHAR2 1 0
          ,S2."AG_PO_IND"                  AS "AgPoInd"             -- 可招攬指示碼 VARCHAR2 1 0
          ,S2."AG_DOC_IND"                 AS "AgDocInd"            -- 齊件否 VARCHAR2 1 0
          ,S2."NEW_HIRE_TYPE"              AS "NewHireType"         -- 新舊人指示碼 VARCHAR2 1 0
          ,NVL(S2."AG_CUR_IND",'N')        AS "AgCurInd"            -- 現職指示碼 VARCHAR2 1 0
          ,S2."AG_SEND_TYPE"               AS "AgSendType"          -- 發文類別 VARCHAR2 3 0
          ,S2."AG_SEND_NO"                 AS "AgSendNo"            -- 發文文號 VARCHAR2 80 0
          ,NVL(TO_NUMBER(TO_CHAR(S2."REGISTER_DATE_2",'YYYYMMDD')),0)
                                           AS "RegisterDate2"       -- 任職日期 decimalD 8 0
          ,NVL(TO_NUMBER(TO_CHAR(S2."AG_RETURN_DATE",'YYYYMMDD')),0)
                                           AS "AgReturnDate"        -- 回任日期 decimalD 8 0
          ,NVL(TO_NUMBER(TO_CHAR(S2."AG_TRANSFER_DATE_F",'YYYYMMDD')),0)
                                           AS "AgTransferDateF"    -- 初次轉制日期 decimalD 8 0
          ,NVL(TO_NUMBER(TO_CHAR(S2."AG_TRANSFER_DATE",'YYYYMMDD')),0)
                                           AS "AgTransferDate"      -- 轉制日期 decimalD 8 0
          ,S2."PROMOT_YM"                  AS "PromotYM"            -- 初次晉升年月 VARCHAR2 7 0
          ,S2."PROMOT_YM_F"                AS "PromotYMF"           -- 生效業績年月 VARCHAR2 7 0
          ,NVL(TO_NUMBER(TO_CHAR(S2."AG_POST_CHG_DATE",'YYYYMMDD')),0)
                                           AS "AgPostChgDate"       -- 職務異動日 decimalD 8 0
          ,S2."FAMILIES_TAX"               AS "FamiliesTax"         -- 扶養人數 NUMBER 5 0
          ,S2."AGENT_CODE_I"               AS "AgentCodeI"          -- 原區主任代號 VARCHAR2 12 0
          ,S2."AG_LEVEL_SYS"               AS "AgLevelSys"          -- 職等_系統 VARCHAR2 2 0
          ,S2."AG_POST_IN"                 AS "AgPostIn"            -- 內階職務 VARCHAR2 6 0
          ,S2."CENTER_CODE_ACC"            AS "CenterCodeAcc"       -- 駐在單位 VARCHAR2 6 0
          ,S2."EVALUE_IND"                 AS "EvalueInd"           -- 考核特殊碼 VARCHAR2 1 0
          ,S2."EVALUE_IND_1"               AS "EvalueInd1"          -- 辦法優待碼 VARCHAR2 1 0
          ,S2."BATCH_NO"                   AS "BatchNo"             -- 批次號碼 NUMBER 10 0
          ,S2."EVALUE_YM"                  AS "EvalueYM"            -- 考核年月 VARCHAR2 7 0
          ,S2."AG_TRANSFER_CODE"           AS "AgTransferCode"      -- 轉檔碼 VARCHAR2 2 0
          ,NVL(TO_NUMBER(TO_CHAR(S2."BIRTH",'YYYYMMDD')),0)
                                           AS "Birth"               -- 出生年月日 decimalD 8 0
          ,S2."EDUCATION"                  AS "Education"           -- 學歷 VARCHAR2 1 0
          ,S2."LR_IND"                     AS "LrInd"               -- 勞退狀況 VARCHAR2 1 0
          ,NVL(TO_NUMBER(TO_CHAR(S2."PROCESS_DATE",'YYYYMMDD')),0)
                                           AS "ProceccDate"         -- 資料處理時間 decimalD 8 0
          ,NVL(TO_NUMBER(TO_CHAR(S2."QUIT_DATE",'YYYYMMDD')),0)
                                           AS "QuitDate"            -- 離職/停約日 decimalD 8 0
          ,S2."AG_POST"                    AS "AgPost"              -- 職務 VARCHAR2 2 0
          ,S2."LEVEL_NAME_CHS"             AS "LevelNameChs"        -- 職等中文 VARCHAR2 10 0
          ,S2."LR_SYSTEM_TYPE"             AS "LrSystemType"        -- 勞退碼 VARCHAR2 1 0
          ,NVL(S2."SENIORITY_YY",0)        AS "SeniorityYY"         -- 年資_年 NUMBER 5 0
          ,NVL(S2."SENIORITY_MM",0)        AS "SeniorityMM"         -- 年資_月 NUMBER 5 0
          ,NVL(S2."SENIORITY_DD",0)        AS "SeniorityDD"         -- 年資_日 NUMBER 5 0
          ,S2."AGLA_PROCESS_IND"           AS "AglaProcessInd"      -- 登錄處理事項 VARCHAR2 2 0
          ,S2."STATUS_CODE"                AS "StatusCode"          -- 登錄狀態 VARCHAR2 1 0
          ,S2."AGLA_CANCEL_REASON"         AS "AglaCancelReason"   -- 註銷原因 VARCHAR2 1 0
          ,NVL(TO_NUMBER(TO_CHAR(S2."IS_ANN_APPL_DATE",'YYYYMMDD')),0)
                                           AS "ISAnnApplDate"       -- 利變年金通報日 decimalD 8 0
          ,NVL(TO_NUMBER(TO_CHAR(S2."RECORD_DATE_C",'YYYYMMDD')),0)
                                           AS "RecordDateC"         -- 外幣保單登入日 decimalD 8 0
          ,S2."STOP_REASON"                AS "StopReason"          -- 停招/撤銷原因 VARCHAR2 6 0
          ,NVL(TO_NUMBER(TO_CHAR(S2."STOP_STR_DATE",'YYYYMMDD')),0)
                                           AS "StopStrDate"         -- 停止招攬起日 decimalD 8 0
          ,NVL(TO_NUMBER(TO_CHAR(S2."STOP_END_DATE",'YYYYMMDD')),0)
                                           AS "StopEndDate"         -- 停止招攬迄日 decimalD 8 0
          ,NVL(TO_NUMBER(TO_CHAR(S2."IFP_DATE",'YYYYMMDD')),0)
                                           AS "IFPDate"             -- IFP登錄日 decimalD 8 0
          ,NVL(TO_NUMBER(TO_CHAR(S2."EFFECT_STR_DATE",'YYYYMMDD')),0)
                                           AS "EffectStrDate"       -- 撤銷起日 decimalD 8 0
          ,NVL(TO_NUMBER(TO_CHAR(S2."EFFECT_END_DATE",'YYYYMMDD')),0)
                                           AS "EffectEndDate"       -- 撤銷迄日 decimalD 8 0
          ,NVL(TO_NUMBER(TO_CHAR(S2."ANN_APPL_DATE",'YYYYMMDD')),0)
                                           AS "AnnApplDate"         -- 一般年金通報日 decimalD 8 0
          ,S2."CENTER_CODE_ACC_NAME"
                                           AS "CenterCodeAccName" -- 單位名稱(駐在單位) VARCHAR2 20 0
          ,S2."RE_HIRE_CODE"               AS "ReHireCode"          -- 重僱碼 VARCHAR2 1 0
          ,S2."RSVD_ADMIN_CODE"            AS "RSVDAdminCode"       -- 特別碼 VARCHAR2 1 0
          ,S2."ACCOUNT"                    AS "Account"             -- 帳號 VARCHAR2 16 0
          ,S2."PRP_DATE"                   AS "PRPDate"             -- 優體測驗通過日 VARCHAR2 15 0
          ,TO_SINGLE_BYTE(S2."ZIP")
                                           AS "Zip"                 -- 戶籍地址郵遞區號 VARCHAR2 5 0
          ,REPLACE(S2."ADDRESS",'○','X')   AS "Address"             -- 戶籍地址 VARCHAR2 80 0
          ,S2."PHONE_H"                    AS "PhoneH"              -- 住家電話 VARCHAR2 30 0
          ,S2."PHONE_C"                    AS "PhoneC"              -- 手機電話 VARCHAR2 30 0
          ,S2."SALES_QUAL_IND"             AS "SalesQualInd"        -- 基金銷售資格碼 VARCHAR2 1 0
          ,NVL(TO_NUMBER(TO_CHAR(S2."AGSQ_START_DATE",'YYYYMMDD')),0)
                                           AS "AgsqStartDate"       -- 基金銷售資格日 decimalD 8 0
          ,S2."PINYIN_NAME_INDI"           AS "PinYinNameIndi"      -- 原住民羅馬拼音姓名 VARCHAR2 50 0
          ,''                              AS "Email"               -- 電子郵件 VARCHAR2 50
          ,SYSTIMESTAMP                    AS "CreateDate"          -- 建檔日期時間 DATE	
          ,SUBSTR("EmpNo",0,6)             AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6
          ,SYSTIMESTAMP                    AS "LastUpdate"          -- 最後更新日期時間 DATE	
          ,SUBSTR("EmpNo",0,6)             AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6
    FROM (SELECT TRIM("EMPLOYEE_NO") AS "EMPLOYEE_NO"
                ,"AGENT_CODE" 
                ,ROW_NUMBER() OVER (PARTITION BY TRIM("EMPLOYEE_NO")
                                    ORDER BY CASE 
                                               WHEN "AG_CUR_IND" = 'Y' THEN 0 
                                             ELSE 1 END 
                                            ,CASE 
                                               WHEN "AG_STATUS_CODE" = '1' THEN 0 
                                             ELSE 1 END  
                                            ,NVL(TO_NUMBER(TO_CHAR("AG_STATUS_DATE",'YYYYMMDD')),0) DESC 
                                            ,NVL(TO_NUMBER(TO_CHAR("LEVEL_DATE",'YYYYMMDD')),0) DESC 
                                            ,"AGENT_CODE" DESC 
                                            ) AS "Seq" 
          FROM TPSAS047
          WHERE NVL(TRIM("EMPLOYEE_NO"),' ') <> ' '    -- 電腦編號 
         ) S1 
    LEFT JOIN TPSAS047 S2 ON TRIM(S2."EMPLOYEE_NO") = S1."EMPLOYEE_NO" 
                           AND S2."AGENT_CODE" = S1."AGENT_CODE" 
    WHERE S1."Seq" = 1 
      AND NVL(S2."AGENT_CODE",' ') <> ' ' -- 業務員代號 
    ; 
 
    -- 記錄寫入筆數 
    -- INS_CNT := INS_CNT + sql%rowcount; 

    INSERT INTO "CdEmp" (
      "EmployeeNo"
      , "Fullname"
    )
    SELECT T."TlrNo" AS "EmployeeNo"
         , NVL(T."TlrItem",T."TlrNo") AS "Fullname"
    FROM "TxTeller" T
    LEFT JOIN "CdEmp" E ON E."EmployeeNo" = T."TlrNo"
    WHERE NVL(E."EmployeeNo",' ') = ' ' -- CdEmp不存在才寫入
    ;

    -- 記錄程式結束時間 
    -- JOB_END_TIME := SYSTIMESTAMP; 
 
    commit; 
 
    -- 例外處理
    Exception
    WHEN OTHERS THEN
    "Usp_L9_UspErrorLog_Ins"(
        'Usp_L6_CdEmp_Ins' -- UspName 預存程序名稱
      , SQLCODE -- Sql Error Code (固定值)
      , SQLERRM -- Sql Error Message (固定值)
      , dbms_utility.format_error_backtrace -- Sql Error Trace (固定值)
      , "EmpNo" -- 發動預存程序的員工編號
      , JobTxSeq -- 啟動批次的交易序號
    );
    COMMIT;
    RAISE;
END;