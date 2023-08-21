--------------------------------------------------------
--  DDL for Procedure Usp_Tf_FacMain_Ins
--------------------------------------------------------
set define off;

CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_Tf_FacMain_Ins" 
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
    EXECUTE IMMEDIATE 'ALTER TABLE "FacMain" DISABLE PRIMARY KEY CASCADE'; 
    EXECUTE IMMEDIATE 'TRUNCATE TABLE "FacMain" DROP STORAGE'; 
    EXECUTE IMMEDIATE 'ALTER TABLE "FacMain" ENABLE PRIMARY KEY'; 
 
    -- 寫入資料 
    INSERT INTO "FacMain" ( 
       "CustNo"              -- 借款人戶號 DECIMAL 7  
      ,"FacmNo"              -- 額度編號 DECIMAL 3  
      ,"LastBormNo"          -- 已撥款序號 DECIMAL 3  
      ,"LastBormRvNo"        -- 已預約序號 DECIMAL 3  
      ,"ApplNo"              -- 申請號碼 DECIMAL 7  
      ,"CreditSysNo"         -- 徵審系統案號 DECIMAL 7 -- 2020/10/19 Wei修改 
      ,"ProdNo"              -- 商品代碼 VARCHAR2 5  
      ,"BaseRateCode"        -- 指標利率代碼 VARCHAR2 2  
      ,"RateIncr"            -- 加碼利率 DECIMAL 6 4 -- 2020/10/19 Wei修改 
      ,"IndividualIncr"      -- 個別加碼 DECIMAL 6 4 
      ,"ApproveRate"         -- 核准利率 DECIMAL 6 4 
      ,"AnnualIncr"          -- 年繳比重優惠加減碼 DECIMAL 6 4 
      ,"EmailIncr"           -- 提供EMAIL優惠減碼 DECIMAL 6 4 
      ,"GraceIncr"           -- 寬限逾一年利率加碼 DECIMAL 6 4 
      ,"RateCode"            -- 利率區分 VARCHAR2 1  
      ,"FirstRateAdjFreq"    -- 首次利率調整週期 DECIMAL 2  
      ,"RateAdjFreq"         -- 利率調整週期 DECIMAL 2  
      ,"CurrencyCode"        -- 核准幣別 VARCHAR2 3  
      ,"LineAmt"             -- 核准額度 DECIMAL 16 2 
      ,"UtilAmt"             -- 貸出金額(放款餘額) DECIMAL 16 2 
      ,"UtilBal"             -- 已動用額度餘額 DECIMAL 16 2 循環動用還款時會減少,非循環動用還款時不會減少 
      ,"AcctCode"            -- 核准科目 VARCHAR2 3  
      ,"LoanTermYy"          -- 貸款期間年 DECIMAL 2  
      ,"LoanTermMm"          -- 貸款期間月 DECIMAL 2  
      ,"LoanTermDd"          -- 貸款期間日 DECIMAL 3  
      ,"FirstDrawdownDate"   -- 初貸日 DECIMALD 8  
      ,"MaturityDate"        -- 到期日 NUMBER(8,0) 
      ,"IntCalcCode" 
      ,"AmortizedCode"       -- 攤還方式 VARCHAR2 1  
      ,"FreqBase"            -- 週期基準 VARCHAR2 1  
      ,"PayIntFreq"          -- 繳息週期 DECIMAL 2  
      ,"RepayFreq"           -- 還本週期 DECIMAL 2  
      ,"UtilDeadline"        -- 動支期限 DECIMALD 8  
      ,"GracePeriod"         -- 寬限總月數 DECIMAL 3  
      ,"AcctFee"             -- 帳管費 DECIMAL 16 2 
      ,"HandlingFee"         -- 手續費 DECIMAL 16 2 
      ,"RuleCode"            -- 規定管制代碼 VARCHAR2 
      ,"ExtraRepayCode"      -- 攤還額異動碼 VARCHAR2 1  
      ,"CustTypeCode"        -- 客戶別 VARCHAR2 2  
      ,"RecycleCode"         -- 循環動用 VARCHAR2 1  
      ,"RecycleDeadline"     -- 循環動用期限 DECIMALD 8  
      ,"UsageCode"           -- 資金用途別 VARCHAR2 2  
      ,"DepartmentCode"      -- 案件隸屬單位 VARCHAR2 1  
      ,"IncomeTaxFlag"       -- 代繳所得稅 VARCHAR2 1  
      ,"CompensateFlag"      -- 代償碼 VARCHAR2 1  
      ,"IrrevocableFlag"     -- 不可撤銷 VARCHAR2 1  
      ,"RateAdjNoticeCode"   -- 利率調整通知 VARCHAR2 1  
      ,"PieceCode"           -- 計件代碼 VARCHAR2 1  
      ,"RepayCode"           -- 繳款方式 DECIMAL 2 
      ,"Introducer"          -- 介紹人 VARCHAR2 6  
      ,"District"            -- 區部 VARCHAR2 6  
      ,"FireOfficer"         -- 火險服務 VARCHAR2 6  
      ,"Estimate"            -- 估價 VARCHAR2 6  
      ,"CreditOfficer"       -- 授信 VARCHAR2 6  
      ,"LoanOfficer"         -- 放款業務專員 VARCHAR2 6  
      ,"BusinessOfficer"     -- 房貸專員 VARCHAR2 6  
      ,"Supervisor"          -- 核決主管 VARCHAR2 6  
      ,"InvestigateOfficer"  -- 徵信 VARCHAR2 6  
      ,"EstimateReview"      -- 估價覆核 VARCHAR2 6  
      ,"Coorgnizer"          -- 協辦人 VARCHAR2 6  
      ,"AdvanceCloseCode"    -- 提前清償原因 DECIMAL 2 
      ,"ProdBreachFlag"      -- 違約適用方式是否按商品設定 varchar2 1 
      ,"BreachDescription"   -- 違約適用說明 nvarchar2 100 
      ,"CreditScore"         -- 信用評分 DECIMAL 3  
      ,"GuaranteeDate"       -- 對保日期 DECIMALD 8  
      ,"ContractNo"          -- 合約編號 VARCHAR2 10  
      ,"ColSetFlag"          -- 擔保品設定記號 VARCHAR2 1  
      ,"ActFg"               -- 交易進行記號 DECIMAL 1  
      ,"LastAcctDate"        -- 上次交易日 NUMBER(8,0) 
      ,"LastKinbr"           -- 上次交易行別 VARCHAR2(4 BYTE) 
      ,"LastTlrNo"           -- 上次櫃員編號 VARCHAR2(6 BYTE) 
      ,"LastTxtNo"           -- 上次交易序號 VARCHAR2(8 BYTE) 
      ,"AcDate"              -- 會計日期 DECIMALD 8  
      ,"L9110Flag"           -- 是否已列印[撥款審核資料表] VARCHAR2(1 BYTE) 
      ,"BranchNo" 
      ,"ApprovedLevel"       -- 核准層級 VARCHAR2(1 BYTE) 
      ,"CreateDate"          -- 建檔日期時間 DATE   
      ,"CreateEmpNo"         -- 建檔人員 VARCHAR2 6  
      ,"LastUpdate"          -- 最後更新日期時間 DATE   
      ,"LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6  
      ,"Grcd"                -- 綠色授信註記 VARCHAR2 1 
      ,"GrKind"              -- 綠色支出類別 VARCHAR2 1 
      ,"EsGcd"               -- 永續績效連結授信註記 VARCHAR2 1 
      ,"EsGKind"             -- 永續績效連結授信類別 VARCHAR2 1 
      ,"EsGcnl"              -- 永續績效連結授信約定條件全部未達成通報 VARCHAR2 1 
      ,"RenewCnt"            -- 展期次數 DECIMAL 3 
      ,"OldFacmNo"           -- 原額度編號 DECIMAL 3 
      ,"SettingDate"         -- 額度設定日 DECIMALD 8 
      ,"PreStarBuildingYM"   -- 約定動工年月 DECIMAL 6
      ,"StarBuildingYM"      -- 實際興建年月 DECIMAL 6
      ,"BreachFlag"          -- 是否綁約
      ,"BreachCode"          -- 違約適用方式
      ,"BreachGetCode"       -- 違約金收取方式
      ,"ProhibitMonth"       -- 限制清償期限
      ,"BreachPercent"       -- 違約金百分比
      ,"BreachDecreaseMonth" -- 違約金分段月數
      ,"BreachDecrease"      -- 分段遞減百分比
      ,"BreachStartPercent"  -- 還款起算比例%
    )
    SELECT APLP."LMSACN"                  AS "CustNo"              -- 借款人戶號 DECIMAL 7  
          ,APLP."LMSAPN"                  AS "FacmNo"              -- 額度編號 DECIMAL 3  
          ,NVL(LMSP."LastBormNo",0)       AS "LastBormNo"          -- 已撥款序號 DECIMAL 3  
          ,NVL(LMSP."LastBormRvNo",900)   AS "LastBormRvNo"        -- 已預約序號 DECIMAL 3  
          ,APLP."APLNUM"                  AS "ApplNo"              -- 申請號碼 DECIMAL 7  
          ,APLP."CASNUM3"                 AS "CreditSysNo"         -- 徵審系統案號 DECIMAL 7 -- 2020/10/19 Wei修改 
          ,APLP."IRTBCD"                  AS "ProdNo"              -- 商品代碼 VARCHAR2 5  
          ,PROD."BaseRateCode"            AS "BaseRateCode"        -- 指標利率代碼 VARCHAR2 2  
          ,APLP."IRTASC"                  AS "RateIncr"            -- 加碼利率 DECIMAL 6 4 -- 2020/10/19 Wei修改 
          -- 2022-10-24 Wei 增加判斷 
          -- from 家興&賴桑口述 : 
          -- PROD."IncrFlag" = 'N' 時 , "IndividualIncr" 用 "RateIncr" 寫入
          ,CASE
             WHEN PROD."IncrFlag" = 'N'
             THEN APLP."IRTASC"
           ELSE 0 -- 2023-06-19 Wei修改 from 家興:ELSE 擺0 原為NVL(A1."ASCRAT",0)
           END                            AS "IndividualIncr"      -- 個別加碼 DECIMAL 6 4 
          ,APLP."APLRAT"                  AS "ApproveRate"         -- 核准利率 DECIMAL 6 4 
          ,0                              AS "AnnualIncr"          -- 年繳比重優惠加減碼 DECIMAL 6 4 
          ,0                              AS "EmailIncr"           -- 提供EMAIL優惠減碼 DECIMAL 6 4 
          ,0                              AS "GraceIncr"           -- 寬限逾一年利率加碼 DECIMAL 6 4 
          ,APLP."AILIRT"                  AS "RateCode"            -- 利率區分 VARCHAR2 1  
          ,APLP."IRTFSC"                  AS "FirstRateAdjFreq"    -- 首次利率調整週期 DECIMAL 2  
          ,APLP."IRTMSC"                  AS "RateAdjFreq"         -- 利率調整週期 DECIMAL 2  
          ,NVL(APLP."APLCUR",'TWD')       AS "CurrencyCode"        -- 核准幣別 VARCHAR2 3  
          ,APLP."APLPAM"                  AS "LineAmt"             -- 核准額度 DECIMAL 16 2 
          ,APLP."APLLAM"                  AS "UtilAmt"             -- 貸出金額(放款餘額) DECIMAL 16 2 
          ,APLP."APLUAM"                  AS "UtilBal"             -- 已動用額度餘額 DECIMAL 16 2 循環動用還款時會減少,非循環動用還款時不會減少 
          ,APLP."ACTACT"                  AS "AcctCode"            -- 核准科目 VARCHAR2 3  
          ,APLP."APLYER"                  AS "LoanTermYy"          -- 貸款期間年 DECIMAL 2  
          ,APLP."APLMON"                  AS "LoanTermMm"          -- 貸款期間月 DECIMAL 2  
          ,APLP."APLDAY"                  AS "LoanTermDd"          -- 貸款期間日 DECIMAL 3  
          ,CASE 
             WHEN NVL(LMSP."FirstDrawdownDate",99991231) != 99991231 -- 排除預約撥款資料 
             THEN LMSP."FirstDrawdownDate" 
           ELSE 0 
           END                            AS "FirstDrawdownDate"   -- 初貸日 DECIMALD 8  
          ,CASE 
             WHEN APLP."APLDLD" != 0 
             THEN APLP."APLDLD" 
             WHEN APLP."APLDLD" = 0 
                  AND APLP."APLLSQ" >= 1 -- 最終序號 
                  AND APLP."APLCNT" >= 1 -- 撥款筆數 
             THEN LMSP2."LMSDLD" 
           ELSE 0 END                     AS "MaturityDate"        -- 到期日 NUMBER(8,0) 
          ,CASE 
             WHEN APLP."ACTACT" = '310' THEN '1' -- 以日計息 
             WHEN S1."TRXJAC" = 1            THEN '1' -- 以日計息  
           ELSE '2' END --以月計息 
                                          AS "IntCalcCode" 
          ,APLP."LMSRTP"                  AS "AmortizedCode"       -- 攤還方式 VARCHAR2 1  
          ,'2'                            AS "FreqBase"            -- 週期基準 VARCHAR2 1  
          ,APLP."LMSISC"                  AS "PayIntFreq"          -- 繳息週期 DECIMAL 2  
          ,APLP."LMSPSC"                  AS "RepayFreq"           -- 還本週期 DECIMAL 2  
          ,APLP."APLADT"                  AS "UtilDeadline"        -- 動支期限 DECIMALD 8  
          ,APLP."LMSGPT"                  AS "GracePeriod"         -- 寬限總月數 DECIMAL 3  
          ,APLP."ACTFEE"                  AS "AcctFee"             -- 帳管費 DECIMAL 16 2 
          ,0                              AS "HandlingFee"         -- 手續費 DECIMAL 16 2 
          -- 2023-08-21 Wei 重寫 from 客戶別-央行管制代碼相關by盈如經理.xlsx
          ,CASE TRIM(APLP."CUSECD")
             WHEN 'I' THEN 'B471' -- 自然人第3戶以上購屋貸款
             WHEN 'L' THEN 'A001' -- 增貸管制戶
             WHEN 'O' THEN 'B421' -- 購地貸款上限6.5成
             WHEN 'Q' THEN 'B472' -- 自然人第4戶以上購屋貸款
             WHEN 'V' THEN 'B482' -- 自然人購置高價住宅-3戶以上
             WHEN 'W' THEN 'B430' -- 餘屋貸款
             WHEN 'X' THEN 'B481' -- 自然人購置高價住宅-2戶以下
             WHEN 'Y' THEN 'B460' -- 公司法人購置住宅
             WHEN 'Z' THEN 'B450' -- 工業區閒置土地抵押貸款
           ELSE ''
           END                            AS "RuleCode"            -- 規定管制代碼 VARCHAR2 
          ,APLP."APLPCD"                  AS "ExtraRepayCode"      -- 攤還額異動碼 VARCHAR2 1  
          -- 2023-08-21 Wei 重寫 from 客戶別-央行管制代碼相關by盈如經理.xlsx
          -- 轉換對照表在NAS:\SKL\DB\00-ref-參考資料\客戶別及規定管制代碼轉換對照表.xlsx
          ,CASE TRIM(APLP."CUSECD")
             WHEN '0' THEN 'C01' -- 一般
             WHEN '1' THEN 'C02' -- 員工
             WHEN 'U' THEN 'C03' -- 投資戶(內部規範)
             WHEN 'I' THEN 'S01' -- 央行管制
             WHEN 'Q' THEN 'S01' -- 央行管制
             WHEN 'O' THEN 'S01' -- 央行管制
             WHEN 'X' THEN 'S01' -- 央行管制
             WHEN 'V' THEN 'S01' -- 央行管制
             WHEN 'Y' THEN 'S01' -- 央行管制
             WHEN 'W' THEN 'S01' -- 央行管制
             WHEN 'Z' THEN 'S01' -- 央行管制
             WHEN 'K' THEN 'S01' -- 央行管制
             WHEN 'L' THEN 'S01' -- 央行管制
             WHEN 'M' THEN 'C04' -- 整合貸
             WHEN 'P' THEN 'C05' -- 優惠轉貸
             WHEN 'A' THEN 'C06' -- 信義房屋
             WHEN 'T' THEN 'C07' -- VIP減帳管
             WHEN '@' THEN 'C08' -- 固特利契轉
             WHEN '2' THEN 'C09' -- 首購
             WHEN '3' THEN 'C10' -- 關企公司
             WHEN '4' THEN 'C11' -- 關企員工
             WHEN '5' THEN 'C12' -- 保戶
             WHEN '6' THEN 'C13' -- 團體戶
             WHEN '7' THEN 'C14' -- 二等親屬
             WHEN '8' THEN 'C15' -- 受災戶
             WHEN '9' THEN 'C16' -- 新二階員工
             WHEN '#' THEN 'C17' -- 991231+VIP-
             WHEN '&' THEN 'C18' -- 永慶房屋
             WHEN 'B' THEN 'C19' -- 千禧房貸
             WHEN 'C' THEN 'C20' -- 青年優惠
             WHEN 'D' THEN 'C21' -- 2000億優惠
             WHEN 'E' THEN 'C22' -- 退休員工
             WHEN 'F' THEN 'C23' -- 菁英專案
             WHEN 'G' THEN 'C24' -- 東方帝國
             WHEN 'H' THEN 'C25' -- 2000億優惠
             WHEN 'J' THEN 'C26' -- 花木釀宅
             WHEN 'N' THEN 'C27' -- 8000億優惠
             WHEN 'R' THEN 'C28' -- 優惠重購
             WHEN 'S' THEN 'C29' -- 88風災
             WHEN 'Y' THEN 'C30' -- 法人購屋
           ELSE TRIM(APLP."CUSECD")
           END                            AS "CustTypeCode"        -- 客戶別 VARCHAR2 2  
          ,APLP."APLRCD"                  AS "RecycleCode"         -- 循環動用 VARCHAR2 1  
          ,APLP."APLRDT"                  AS "RecycleDeadline"     -- 循環動用期限 DECIMALD 8  
          -- 2021-02-08 補零 
          ,LPAD(APLP."APLUSG",2,'0')      AS "UsageCode"           -- 資金用途別 VARCHAR2 2  
          ,NVL(APLP."CASUNT",'0')         AS "DepartmentCode"      -- 案件隸屬單位 VARCHAR2 1  
          ,CASE 
             WHEN APLP."APLITX" = 1 THEN 'Y' -- 2021-04-15 Wei 增加判斷 
           ELSE 'N' END                   AS "IncomeTaxFlag"       -- 代繳所得稅 VARCHAR2 1  
          ,CASE 
             WHEN APLP."APLPYF" = 1 THEN 'Y' -- 代償件 
             WHEN APLP."APLPYF" = 2 THEN 'Y' -- 代償平轉件 
           ELSE 'N' END                   AS "CompensateFlag"      -- 代償碼 VARCHAR2 1  
          ,CASE 
             WHEN NVL(APLP."APLILC",0) = 1 THEN 'Y' -- REF: AS400 TB$SPLP.TB$FNM = 'APLILC' 0:可撤銷 1:不可撤銷  
           ELSE 'N' END                   AS "IrrevocableFlag"     -- 不可撤銷 VARCHAR2 1  
          ,CASE 
             WHEN NVL(CUSP."CUSNOT",' ') != ' ' 
             THEN CUSP."CUSNOT" 
           ELSE '2' END                   AS "RateAdjNoticeCode"   -- 利率調整通知 VARCHAR2 1  
          ,APLP."CASCDE"                  AS "PieceCode"           -- 計件代碼 VARCHAR2 1  
          ,APLP."LMSPYS"                  AS "RepayCode"           -- 繳款方式 DECIMAL 2 
          ,APLP."CUSEM3"                  AS "Introducer"          -- 介紹人 VARCHAR2 6  
          ,APLP."CUSRGN"                  AS "District"            -- 區部 VARCHAR2 6  
          ,APLP."CUSEM7"                  AS "FireOfficer"         -- 火險服務 VARCHAR2 6  
          ,APLP."CUSEM6"                  AS "Estimate"            -- 估價 VARCHAR2 6  
          ,APLP."CUSEM1"                  AS "CreditOfficer"       -- 授信 VARCHAR2 6  
          -- 2021-04-06 Wei修改 from 賴桑Email  
          -- LoanOfficer 放款業務專員(原名:放款專員) 原轉CUSEM2 改用CUSEM8轉 
          ,APLP."CUSEM8"                  AS "LoanOfficer"         -- 放款業務專員 VARCHAR2 6  
          -- 2021-04-06 Wei修改 from 賴桑Email  
          -- BusinessOfficer 房貸專員 原用CUSEM8轉 改用CUSEM2轉 
          ,APLP."CUSEM2"                  AS "BusinessOfficer"     -- 房貸專員 VARCHAR2 6  
          -- 2021-03-31 LM044 詢問舜雯 應轉 督辦 
          ,APLP."CUSEM4"                  AS "Supervisor"          -- 核決主管 VARCHAR2 6  
          ,APLP."CUSEM9"                  AS "InvestigateOfficer"  -- 徵信 VARCHAR2 6  
          ,APLP."CUSEMA"                  AS "EstimateReview"      -- 估價覆核 VARCHAR2 6  
          ,LS."EMPCOD"                    AS "Coorgnizer"          -- 協辦人 VARCHAR2 6  
          -- 2021-03-11 修正邏輯 
          ,APLP."APLPSN"                  AS "AdvanceCloseCode"    -- 提前清償原因 DECIMAL 2 
          ,'Y'                            AS "ProdBreachFlag"      -- 違約適用方式是否按商品設定 varchar2 1 
          ,''                             AS "BreachDescription"   -- 違約適用說明 nvarchar2 100 
          ,APLP."APLCRD"                  AS "CreditScore"         -- 信用評分 DECIMAL 3  
          ,APLP."APLCSD"                  AS "GuaranteeDate"       -- 對保日期 DECIMALD 8  
          ,CLF."CNTRCTNO"                 AS "ContractNo"          -- 合約編號 VARCHAR2 10  
          ,CASE 
             WHEN NVL(APLP."GDRNUM",0) != 0 -- 2023-01-31 Wei FROM 家興 額度主檔的核准號碼在ClFac有關聯擔保品設Y 沒有設N
             THEN 'Y'
           ELSE 'N' END                   AS "ColSetFlag"          -- 擔保品設定記號 VARCHAR2 1  
          ,0                              AS "ActFg"               -- 交易進行記號 DECIMAL 1  
          ,0                              AS "LastAcctDate"        -- 上次交易日 NUMBER(8,0) 
          ,''                             AS "LastKinbr"           -- 上次交易行別 VARCHAR2(4 BYTE) 
          ,''                             AS "LastTlrNo"           -- 上次櫃員編號 VARCHAR2(6 BYTE) 
          ,''                             AS "LastTxtNo"           -- 上次交易序號 VARCHAR2(8 BYTE) 
          ,0                              AS "AcDate"              -- 會計日期 DECIMALD 8  
          -- 2022-02-15 Wei 修改: QC1386  
          -- 未撥款轉N 
          -- 其他Y 
          , CASE 
              WHEN NVL(LMSP."LastBormNo",0) = 0 -- 已撥款序號 DECIMAL 3  
                   AND NVL(LMSP."LastBormRvNo",900) = 900 -- 已預約序號 DECIMAL 3  
              THEN 'N' 
           ELSE 'Y' 
           END                            AS "L9110Flag"           -- 是否已列印[撥款審核資料表] VARCHAR2(1 BYTE) 
          ,'0000'                         AS "BranchNo" 
          -- 核准層級為新增欄位 
          -- 待User決定就資料轉什麼值 
          -- 預設先擺1: 審查課專案經理 
          ,'1'                             AS "ApprovedLevel"       -- 核准層級 VARCHAR2(1 BYTE) 
          -- AS400 LA$APLP.APLSDT 鍵檔日期 給 L9110報表使用 
          ,CASE 
             WHEN APLP."APLSDT" > 0 
             THEN TO_DATE(APLP."APLSDT",'YYYYMMDD') 
           ELSE JOB_START_TIME 
           END                            AS "CreateDate"          -- 建檔日期時間 DATE   
          ,'999999'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6  
          ,JOB_START_TIME                 AS "LastUpdate"          -- 最後更新日期時間 DATE   
          ,'999999'                       AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6  
          ,''                             AS "Grcd"                -- 綠色授信註記 VARCHAR2 1 
          ,''                             AS "GrKind"              -- 綠色支出類別 VARCHAR2 1 
          ,''                             AS "EsGcd"               -- 永續績效連結授信註記 VARCHAR2 1 
          ,''                             AS "EsGKind"             -- 永續績效連結授信類別 VARCHAR2 1 
          ,''                             AS "EsGcnl"              -- 永續績效連結授信約定條件全部未達成通報 VARCHAR2 1 
          ,APLP."APLEPT"                  AS "RenewCnt"            -- 展期次數 DECIMAL 3 
          ,APLP."APLOAP"                  AS "OldFacmNo"           -- 原額度編號 DECIMAL 3 
          ,APLP."APLSDT"                  AS "SettingDate"         -- 額度設定日 DECIMALD 8 
          ,NVL(APLP.APLPSC,0)             AS "PreStarBuildingYM"   -- 約定動工年月 DECIMAL 6
          ,NVL(APLP.APLRSC,0)             AS "StarBuildingYM"      -- 實際興建年月 DECIMAL 6
          -- 2023-03-14 Wei 修改 from QC2339
          ,CASE
             WHEN APLP.APLPAC = 1
                  AND APLP.APLNER = 0 
             THEN 'N'
             WHEN APLP.APLPAC = 1
                  AND APLP.APLNER > 0 
             THEN 'Y'
             WHEN APLP.APLPAC = 2
                  AND APLP.APLNER = 0 
             THEN 'N'
             WHEN APLP.APLPAC = 2
                  AND APLP.APLNER > 0 
             THEN 'Y'
             WHEN APLP.APLPAC = 3
                  AND APLP.APLNER = 0 
             THEN 'N'
             WHEN APLP.APLPAC = 3
                  AND APLP.APLNER > 0 
             THEN 'Y'
             WHEN APLP.APLPAC = 4
                  AND APLP.APLNER = 0 
             THEN 'N'
             WHEN APLP.APLPAC = 4
                  AND APLP.APLNER > 0 
             THEN 'Y'
             WHEN APLP.APLPAC = 5
                  AND APLP.APLNER = 0 
             THEN 'N'
             WHEN APLP.APLPAC = 5
                  AND APLP.APLNER > 0 
             THEN 'Y'
             WHEN APLP.APLPAC = 6
                  AND APLP.APLNER = 0 
             THEN 'N'
             WHEN APLP.APLPAC = 6
                  AND APLP.APLNER > 0 
             THEN 'Y'
             WHEN APLP.APLPAC = 7
                  AND APLP.APLNER = 0 
             THEN 'N'
             WHEN APLP.APLPAC = 7
                  AND APLP.APLNER > 0 
             THEN 'Y'
             WHEN APLP.APLPAC = 8
                  AND APLP.APLNER = 0 
             THEN 'N'
             WHEN APLP.APLPAC = 8
                  AND APLP.APLNER > 0 
             THEN 'Y'
             WHEN APLP.APLPAC = 9
                  AND APLP.APLNER = 0 
             THEN 'N'
             WHEN APLP.APLPAC = 9
                  AND APLP.APLNER > 0 
             THEN 'Y'
             WHEN APLP.APLPAC = 0
                  AND APLP.APLNER = 0 
             THEN 'N'
             WHEN APLP.APLPAC = 0
                  AND APLP.APLNER > 0 
             THEN 'Y'
           ELSE '?' END                   AS "BreachFlag"          -- 是否綁約
          ,CASE
             WHEN APLP.APLPAC = 1
                  AND APLP.APLNER = 0 
             THEN '901'
             WHEN APLP.APLPAC = 1
                  AND APLP.APLNER > 0 
             THEN '901'
             WHEN APLP.APLPAC = 2
                  AND APLP.APLNER = 0 
             THEN '902'
             WHEN APLP.APLPAC = 2
                  AND APLP.APLNER > 0 
             THEN '902'
             WHEN APLP.APLPAC = 3
                  AND APLP.APLNER = 0 
             THEN '903'
             WHEN APLP.APLPAC = 3
                  AND APLP.APLNER > 0 
             THEN '903'
             WHEN APLP.APLPAC = 4
                  AND APLP.APLNER = 0 
             THEN '904'
             WHEN APLP.APLPAC = 4
                  AND APLP.APLNER > 0 
             THEN '904'
             WHEN APLP.APLPAC = 5
                  AND APLP.APLNER = 0 
             THEN '905'
             WHEN APLP.APLPAC = 5
                  AND APLP.APLNER > 0 
             THEN '905'
             WHEN APLP.APLPAC = 6
                  AND APLP.APLNER = 0 
             THEN '906'
             WHEN APLP.APLPAC = 6
                  AND APLP.APLNER > 0 
             THEN '906'
             WHEN APLP.APLPAC = 7
                  AND APLP.APLNER = 0 
             THEN '907'
             WHEN APLP.APLPAC = 7
                  AND APLP.APLNER > 0 
             THEN '907'
             WHEN APLP.APLPAC = 8
                  AND APLP.APLNER = 0 
             THEN '908'
             WHEN APLP.APLPAC = 8
                  AND APLP.APLNER > 0 
             THEN '908'
             WHEN APLP.APLPAC = 9
                  AND APLP.APLNER = 0 
             THEN '909'
             WHEN APLP.APLPAC = 9
                  AND APLP.APLNER > 0 
             THEN '909'
             WHEN APLP.APLPAC = 0
                  AND APLP.APLNER = 0 
             THEN '900'
             WHEN APLP.APLPAC = 0
                  AND APLP.APLNER > 0 
             THEN '002'
           ELSE '???' END                AS "BreachCode"          -- 違約適用方式
          ,CASE
             WHEN APLP.APLPAC = 1
                  AND APLP.APLNER = 0 
             THEN '2'
             WHEN APLP.APLPAC = 1
                  AND APLP.APLNER > 0 
             THEN '2'
             WHEN APLP.APLPAC = 2
                  AND APLP.APLNER = 0 
             THEN '2'
             WHEN APLP.APLPAC = 2
                  AND APLP.APLNER > 0 
             THEN '2'
             WHEN APLP.APLPAC = 3
                  AND APLP.APLNER = 0 
             THEN '2'
             WHEN APLP.APLPAC = 3
                  AND APLP.APLNER > 0 
             THEN '2'
             WHEN APLP.APLPAC = 4
                  AND APLP.APLNER = 0 
             THEN '2'
             WHEN APLP.APLPAC = 4
                  AND APLP.APLNER > 0 
             THEN '2'
             WHEN APLP.APLPAC = 5
                  AND APLP.APLNER = 0 
             THEN '2'
             WHEN APLP.APLPAC = 5
                  AND APLP.APLNER > 0 
             THEN '2'
             WHEN APLP.APLPAC = 6
                  AND APLP.APLNER = 0 
             THEN '2'
             WHEN APLP.APLPAC = 6
                  AND APLP.APLNER > 0 
             THEN '2'
             WHEN APLP.APLPAC = 7
                  AND APLP.APLNER = 0 
             THEN '2'
             WHEN APLP.APLPAC = 7
                  AND APLP.APLNER > 0 
             THEN '2'
             WHEN APLP.APLPAC = 8
                  AND APLP.APLNER = 0 
             THEN '2'
             WHEN APLP.APLPAC = 8
                  AND APLP.APLNER > 0 
             THEN '2'
             WHEN APLP.APLPAC = 9
                  AND APLP.APLNER = 0 
             THEN '2'
             WHEN APLP.APLPAC = 9
                  AND APLP.APLNER > 0 
             THEN '2'
             WHEN APLP.APLPAC = 0
                  AND APLP.APLNER = 0 
             THEN '2'
             WHEN APLP.APLPAC = 0
                  AND APLP.APLNER > 0 
             THEN '2'
           ELSE null END                  AS "BreachGetCode"       -- 違約金收取方式
          ,CASE
             WHEN APLP.APLPAC = 1
                  AND APLP.APLNER = 0 
             THEN 0
             WHEN APLP.APLPAC = 1
                  AND APLP.APLNER > 0 
             THEN APLP.APLNER * 12
             WHEN APLP.APLPAC = 2
                  AND APLP.APLNER = 0 
             THEN 0
             WHEN APLP.APLPAC = 2
                  AND APLP.APLNER > 0 
             THEN APLP.APLNER * 12
             WHEN APLP.APLPAC = 3
                  AND APLP.APLNER = 0 
             THEN 0
             WHEN APLP.APLPAC = 3
                  AND APLP.APLNER > 0 
             THEN APLP.APLNER * 12
             WHEN APLP.APLPAC = 4
                  AND APLP.APLNER = 0 
             THEN 0
             WHEN APLP.APLPAC = 4
                  AND APLP.APLNER > 0 
             THEN APLP.APLNER * 12
             WHEN APLP.APLPAC = 5
                  AND APLP.APLNER = 0 
             THEN 0
             WHEN APLP.APLPAC = 5
                  AND APLP.APLNER > 0 
             THEN APLP.APLNER * 12
             WHEN APLP.APLPAC = 6
                  AND APLP.APLNER = 0 
             THEN 0
             WHEN APLP.APLPAC = 6
                  AND APLP.APLNER > 0 
             THEN APLP.APLNER * 12
             WHEN APLP.APLPAC = 7
                  AND APLP.APLNER = 0 
             THEN 0
             WHEN APLP.APLPAC = 7
                  AND APLP.APLNER > 0 
             THEN APLP.APLNER * 12
             WHEN APLP.APLPAC = 8
                  AND APLP.APLNER = 0 
             THEN 0
             WHEN APLP.APLPAC = 8
                  AND APLP.APLNER > 0 
             THEN APLP.APLNER * 12
             WHEN APLP.APLPAC = 9
                  AND APLP.APLNER = 0 
             THEN 0
             WHEN APLP.APLPAC = 9
                  AND APLP.APLNER > 0 
             THEN APLP.APLNER * 12
             WHEN APLP.APLPAC = 0
                  AND APLP.APLNER = 0 
             THEN 0
             WHEN APLP.APLPAC = 0
                  AND APLP.APLNER > 0 
             THEN APLP.APLNER * 12
           ELSE null END                  AS "ProhibitMonth"       -- 限制清償期限
          ,CASE
             WHEN APLP.APLPAC = 1
                  AND APLP.APLNER = 0 
             THEN 0
             WHEN APLP.APLPAC = 1
                  AND APLP.APLNER > 0 
             THEN 0
             WHEN APLP.APLPAC = 2
                  AND APLP.APLNER = 0 
             THEN 0
             WHEN APLP.APLPAC = 2
                  AND APLP.APLNER > 0 
             THEN 0
             WHEN APLP.APLPAC = 3
                  AND APLP.APLNER = 0 
             THEN 0
             WHEN APLP.APLPAC = 3
                  AND APLP.APLNER > 0 
             THEN 0
             WHEN APLP.APLPAC = 4
                  AND APLP.APLNER = 0 
             THEN 0
             WHEN APLP.APLPAC = 4
                  AND APLP.APLNER > 0 
             THEN 0
             WHEN APLP.APLPAC = 5
                  AND APLP.APLNER = 0 
             THEN 0
             WHEN APLP.APLPAC = 5
                  AND APLP.APLNER > 0 
             THEN 0
             WHEN APLP.APLPAC = 6
                  AND APLP.APLNER = 0 
             THEN 0
             WHEN APLP.APLPAC = 6
                  AND APLP.APLNER > 0 
             THEN 0
             WHEN APLP.APLPAC = 7
                  AND APLP.APLNER = 0 
             THEN 0
             WHEN APLP.APLPAC = 7
                  AND APLP.APLNER > 0 
             THEN 0
             WHEN APLP.APLPAC = 8
                  AND APLP.APLNER = 0 
             THEN 0
             WHEN APLP.APLPAC = 8
                  AND APLP.APLNER > 0 
             THEN 0
             WHEN APLP.APLPAC = 9
                  AND APLP.APLNER = 0 
             THEN 0
             WHEN APLP.APLPAC = 9
                  AND APLP.APLNER > 0 
             THEN 0
             WHEN APLP.APLPAC = 0
                  AND APLP.APLNER = 0 
             THEN 0
             WHEN APLP.APLPAC = 0
                  AND APLP.APLNER > 0 
             THEN 1
           ELSE null END                  AS "BreachPercent"       -- 違約金百分比
          ,CASE
             WHEN APLP.APLPAC = 1
                  AND APLP.APLNER = 0 
             THEN 0
             WHEN APLP.APLPAC = 1
                  AND APLP.APLNER > 0 
             THEN 0
             WHEN APLP.APLPAC = 2
                  AND APLP.APLNER = 0 
             THEN 0
             WHEN APLP.APLPAC = 2
                  AND APLP.APLNER > 0 
             THEN 0
             WHEN APLP.APLPAC = 3
                  AND APLP.APLNER = 0 
             THEN 0
             WHEN APLP.APLPAC = 3
                  AND APLP.APLNER > 0 
             THEN 0
             WHEN APLP.APLPAC = 4
                  AND APLP.APLNER = 0 
             THEN 0
             WHEN APLP.APLPAC = 4
                  AND APLP.APLNER > 0 
             THEN 0
             WHEN APLP.APLPAC = 5
                  AND APLP.APLNER = 0 
             THEN 0
             WHEN APLP.APLPAC = 5
                  AND APLP.APLNER > 0 
             THEN 0
             WHEN APLP.APLPAC = 6
                  AND APLP.APLNER = 0 
             THEN 0
             WHEN APLP.APLPAC = 6
                  AND APLP.APLNER > 0 
             THEN 0
             WHEN APLP.APLPAC = 7
                  AND APLP.APLNER = 0 
             THEN 0
             WHEN APLP.APLPAC = 7
                  AND APLP.APLNER > 0 
             THEN 0
             WHEN APLP.APLPAC = 8
                  AND APLP.APLNER = 0 
             THEN 0
             WHEN APLP.APLPAC = 8
                  AND APLP.APLNER > 0 
             THEN 0
             WHEN APLP.APLPAC = 9
                  AND APLP.APLNER = 0 
             THEN 0
             WHEN APLP.APLPAC = 9
                  AND APLP.APLNER > 0 
             THEN 0
             WHEN APLP.APLPAC = 0
                  AND APLP.APLNER = 0 
             THEN 0
             WHEN APLP.APLPAC = 0
                  AND APLP.APLNER > 0 
             THEN 6
           ELSE null END                  AS "BreachDecreaseMonth" -- 違約金分段月數
          ,CASE
             WHEN APLP.APLPAC = 1
                  AND APLP.APLNER = 0 
             THEN 0
             WHEN APLP.APLPAC = 1
                  AND APLP.APLNER > 0 
             THEN 0
             WHEN APLP.APLPAC = 2
                  AND APLP.APLNER = 0 
             THEN 0
             WHEN APLP.APLPAC = 2
                  AND APLP.APLNER > 0 
             THEN 0
             WHEN APLP.APLPAC = 3
                  AND APLP.APLNER = 0 
             THEN 0
             WHEN APLP.APLPAC = 3
                  AND APLP.APLNER > 0 
             THEN 0
             WHEN APLP.APLPAC = 4
                  AND APLP.APLNER = 0 
             THEN 0
             WHEN APLP.APLPAC = 4
                  AND APLP.APLNER > 0 
             THEN 0
             WHEN APLP.APLPAC = 5
                  AND APLP.APLNER = 0 
             THEN 0
             WHEN APLP.APLPAC = 5
                  AND APLP.APLNER > 0 
             THEN 0
             WHEN APLP.APLPAC = 6
                  AND APLP.APLNER = 0 
             THEN 0
             WHEN APLP.APLPAC = 6
                  AND APLP.APLNER > 0 
             THEN 0
             WHEN APLP.APLPAC = 7
                  AND APLP.APLNER = 0 
             THEN 0
             WHEN APLP.APLPAC = 7
                  AND APLP.APLNER > 0 
             THEN 0
             WHEN APLP.APLPAC = 8
                  AND APLP.APLNER = 0 
             THEN 0
             WHEN APLP.APLPAC = 8
                  AND APLP.APLNER > 0 
             THEN 0
             WHEN APLP.APLPAC = 9
                  AND APLP.APLNER = 0 
             THEN 0
             WHEN APLP.APLPAC = 9
                  AND APLP.APLNER > 0 
             THEN 0
             WHEN APLP.APLPAC = 0
                  AND APLP.APLNER = 0 
             THEN 0
             WHEN APLP.APLPAC = 0
                  AND APLP.APLNER > 0 
             THEN 0.1
           ELSE null END                  AS "BreachDecrease"      -- 分段遞減百分比
          ,CASE
             WHEN APLP.APLPAC = 1
                  AND APLP.APLNER = 0 
             THEN 0
             WHEN APLP.APLPAC = 1
                  AND APLP.APLNER > 0 
             THEN 0
             WHEN APLP.APLPAC = 2
                  AND APLP.APLNER = 0 
             THEN 0
             WHEN APLP.APLPAC = 2
                  AND APLP.APLNER > 0 
             THEN 0
             WHEN APLP.APLPAC = 3
                  AND APLP.APLNER = 0 
             THEN 0
             WHEN APLP.APLPAC = 3
                  AND APLP.APLNER > 0 
             THEN 0
             WHEN APLP.APLPAC = 4
                  AND APLP.APLNER = 0 
             THEN 0
             WHEN APLP.APLPAC = 4
                  AND APLP.APLNER > 0 
             THEN 0
             WHEN APLP.APLPAC = 5
                  AND APLP.APLNER = 0 
             THEN 0
             WHEN APLP.APLPAC = 5
                  AND APLP.APLNER > 0 
             THEN 0
             WHEN APLP.APLPAC = 6
                  AND APLP.APLNER = 0 
             THEN 0
             WHEN APLP.APLPAC = 6
                  AND APLP.APLNER > 0 
             THEN 0
             WHEN APLP.APLPAC = 7
                  AND APLP.APLNER = 0 
             THEN 0
             WHEN APLP.APLPAC = 7
                  AND APLP.APLNER > 0 
             THEN 0
             WHEN APLP.APLPAC = 8
                  AND APLP.APLNER = 0 
             THEN 0
             WHEN APLP.APLPAC = 8
                  AND APLP.APLNER > 0 
             THEN 0
             WHEN APLP.APLPAC = 9
                  AND APLP.APLNER = 0 
             THEN 0
             WHEN APLP.APLPAC = 9
                  AND APLP.APLNER > 0 
             THEN 0
             WHEN APLP.APLPAC = 0
                  AND APLP.APLNER = 0 
             THEN 0
             WHEN APLP.APLPAC = 0
                  AND APLP.APLNER > 0 
             THEN 0
           ELSE null END                  AS "BreachStartPercent"  -- 還款起算比例%
    FROM "LA$APLP" APLP 
    LEFT JOIN "CU$CUSP" CUSP ON CUSP."LMSACN" = APLP."LMSACN" 
    LEFT JOIN "FacCaseAppl" APPL ON APPL."ApplNo" = APLP."APLNUM" 
    LEFT JOIN "FacProd" PROD ON PROD."ProdNo" = APLP."IRTBCD" 
    LEFT JOIN "TB$TBLP" R1 ON R1."IN$COD" = APLP."IRTBCD"  
    LEFT JOIN ( SELECT "LMSACN" 
                      ,"LMSAPN" 
                      ,MAX("TRXJAC") AS "TRXJAC" 
                FROM "LA$LMSP" 
                GROUP BY "LMSACN","LMSAPN" 
              ) S1 ON S1."LMSACN" = APLP."LMSACN" 
                  AND S1."LMSAPN" = APLP."LMSAPN" 
    LEFT JOIN "TB$CLFP" CLF ON CLF."LMSACN" = APLP."LMSACN" 
                           AND CLF."LMSAPN" = APLP."LMSAPN"  
    LEFT JOIN "LN$LSEP" LS ON LS."LMSACN" = APLP."LMSACN" -- 串取協辦人員 
                          AND LS."LMSAPN" = APLP."LMSAPN"  
    LEFT JOIN ( SELECT "LMSACN" 
                     , "LMSAPN" 
                     , MIN("LMSLLD")             AS "FirstDrawdownDate" -- 初貸日 
                     , MAX("LMSASQ")             AS "LastBormNo" -- 已撥款序號 
                     , MAX(900 + "LMSASQ")       AS "LastBormRvNo" -- 已預約撥款序號 
                     , SUM("LMSFLA")             AS "LMSFLA" -- 累計撥款金額 
                     , SUM("LMSLBL")             AS "LMSLBL" -- 放款餘額 
--                     , MIN(CASE 
--                             WHEN "LMSLLD" <= "TbsDyF" -- 撥款日期<=轉換日時,為一般撥款 
--                             THEN "LMSLLD" 
--                           ELSE 99991231 END)    AS "FirstDrawdownDate" -- 初貸日 
--                     , MAX(CASE 
--                             WHEN "LMSLLD" <= "TbsDyF" -- 撥款日期<=轉換日時,為一般撥款 
--                             THEN "LMSASQ" 
--                           ELSE 0 END)           AS "LastBormNo" -- 已撥款序號 
--                     , MAX(CASE 
--                             WHEN "LMSLLD" > "TbsDyF" -- 撥款日期>轉換日時,為預約撥款 
--                             THEN 900 + "LMSASQ" 
--                           ELSE 900 END)         AS "LastBormRvNo" -- 已預約撥款序號 
--                     , SUM(CASE 
--                             WHEN "LMSLLD" <= "TbsDyF" -- 撥款日期<=轉換日時,為一般撥款 
--                             THEN "LMSFLA" 
--                           ELSE 0 END)           AS "LMSFLA" -- 累計撥款金額 
--                     , SUM(CASE 
--                             WHEN "LMSLLD" <= "TbsDyF" -- 撥款日期<=轉換日時,為一般撥款 
--                             THEN "LMSLBL" 
--                           ELSE 0 END)           AS "LMSLBL" -- 放款餘額 
                FROM "LA$LMSP" 
                GROUP BY "LMSACN" 
                       , "LMSAPN" 
              ) LMSP ON LMSP."LMSACN" = APLP."LMSACN" 
                    AND LMSP."LMSAPN" = APLP."LMSAPN" 
    LEFT JOIN ( SELECT APLP."LMSACN" 
                     , APLP."LMSAPN" 
                     , LMSP."LMSDLD" 
                FROM ( 
                  SELECT "LMSACN" 
                       , "LMSAPN" 
                  FROM "LA$APLP"  
                  WHERE "APLDLD" = 0 
                    AND "APLLSQ" >= 1 -- 最終序號 
                    AND "APLCNT" >= 1 -- 撥款筆數 
                ) APLP 
                LEFT JOIN "LA$LMSP" LMSP ON LMSP."LMSACN" = APLP."LMSACN" 
                                        AND LMSP."LMSAPN" = APLP."LMSAPN" 
                                        AND LMSP."LMSASQ" = 1 
                WHERE LMSP."LMSDLD" != 0                 
              ) LMSP2 ON LMSP2."LMSACN" = APLP."LMSACN" 
                     AND LMSP2."LMSAPN" = APLP."LMSAPN" 
    WHERE NVL(APPL."ApplNo",0) != 0 
    ; 
 
    -- 記錄寫入筆數 
    INS_CNT := INS_CNT + sql%rowcount; 
 
    MERGE INTO "FacCaseAppl" T1 
    USING (SELECT "ApplNo" 
                 ,"ProdNo" 
           FROM "FacMain" 
          ) S1 
    ON (T1."ApplNo" = S1."ApplNo") 
    WHEN MATCHED THEN UPDATE SET 
    T1."ProdNo" = S1."ProdNo" 
    ; 
 
    -- 記錄程式結束時間 
    JOB_END_TIME := SYSTIMESTAMP; 
 
    commit; 
 
    END; 
    -- 例外處理 
    Exception 
    WHEN OTHERS THEN 
    ERROR_MSG := SQLERRM || CHR(13) || CHR(10) || dbms_utility.format_error_backtrace; 
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_FacMain_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace); 
END;

/
