drop table "CdEmp" purge;

create table "CdEmp" (
  "AgentCode" varchar2(12),
  "EmployeeNo" varchar2(10),
  "Fullname" nvarchar2(40),
  "CenterShortName" nvarchar2(10),
  "CenterCodeName" nvarchar2(20),
  "CenterCode1" varchar2(6),
  "CenterCode1Short" nvarchar2(10),
  "CenterCode1Name" nvarchar2(20),
  "CenterCode2" varchar2(6),
  "CenterCode2Short" nvarchar2(10),
  "CenterCode2Name" nvarchar2(20),
  "CenterCodeAcc1" varchar2(6),
  "CenterCodeAcc1Name" nvarchar2(20),
  "CenterCodeAcc2" varchar2(6),
  "CenterCodeAcc2Name" nvarchar2(20),
  "CommLineCode" varchar2(2),
  "CommLineType" varchar2(1),
  "OrigIntroducerId" varchar2(12),
  "IntroducerInd" varchar2(1),
  "RegisterLevel" varchar2(2),
  "RegisterDate" decimal(8, 0) default 0 not null,
  "CenterCode" varchar2(6),
  "AdministratId" varchar2(12),
  "InputDate" decimal(8, 0) default 0 not null,
  "InputUser" varchar2(8),
  "AgStatusCode" varchar2(1),
  "AgStatusDate" decimal(8, 0) default 0 not null,
  "TranDate" decimal(8, 0) default 0 not null,
  "TranUser" varchar2(8),
  "ReRegisterDate" decimal(8, 0) default 0 not null,
  "DirectorId" varchar2(12),
  "DirectorIdF" varchar2(12),
  "IntroducerId" varchar2(12),
  "IntroducerIdF" varchar2(12),
  "AgLevel" varchar2(2),
  "LastLevel" varchar2(2),
  "LevelDate" decimal(8, 0) default 0 not null,
  "TopLevel" varchar2(2),
  "OccpInd" varchar2(1),
  "QuotaAmt" decimal(10, 0) default 0 not null,
  "ApplType" varchar2(1),
  "TaxRate" decimal(5, 3) default 0 not null,
  "SocialInsuClass" decimal(5, 0) default 0 not null,
  "PromotLevelYM" varchar2(7),
  "DirectorYM" varchar2(7),
  "RecordDate" decimal(8, 0) default 0 not null,
  "ExRecordDate" decimal(8, 0) default 0 not null,
  "RxTrDate" decimal(8, 0) default 0 not null,
  "ExTrIdent" varchar2(16),
  "ExTrIdent2" varchar2(9),
  "ExTrIdent3" varchar2(12),
  "ExTrDate" decimal(8, 0) default 0 not null,
  "RegisterBefore" decimal(5, 0) default 0 not null,
  "DirectorAfter" decimal(5, 0) default 0 not null,
  "MedicalCode" varchar2(2),
  "ExChgDate" decimal(8, 0) default 0 not null,
  "ExDelDate" decimal(8, 0) default 0 not null,
  "ApplCode" varchar2(10),
  "FirstRegDate" decimal(8, 0) default 0 not null,
  "AginSource" varchar2(3),
  "AguiCenter" varchar2(9),
  "AgentId" varchar2(10),
  "TopId" varchar2(12),
  "AgDegree" varchar2(2),
  "CollectInd" varchar2(1),
  "AgType1" varchar2(1),
  "ContractInd" varchar2(1),
  "ContractIndYM" varchar2(7),
  "AgType2" varchar2(1),
  "AgType3" varchar2(1),
  "AgType4" varchar2(1),
  "AginInd1" varchar2(1),
  "AgPoInd" varchar2(1),
  "AgDocInd" varchar2(1),
  "NewHireType" varchar2(1),
  "AgCurInd" varchar2(1),
  "AgSendType" varchar2(3),
  "AgSendNo" nvarchar2(100),
  "RegisterDate2" decimal(8, 0) default 0 not null,
  "AgReturnDate" decimal(8, 0) default 0 not null,
  "AgTransferDateF" decimal(8, 0) default 0 not null,
  "AgTransferDate" decimal(8, 0) default 0 not null,
  "PromotYM" varchar2(7),
  "PromotYMF" varchar2(7),
  "AgPostChgDate" decimal(8, 0) default 0 not null,
  "FamiliesTax" decimal(5, 0) default 0 not null,
  "AgentCodeI" varchar2(12),
  "AgLevelSys" varchar2(2),
  "AgPostIn" varchar2(6),
  "CenterCodeAcc" varchar2(6),
  "EvalueInd" varchar2(1),
  "EvalueInd1" varchar2(1),
  "BatchNo" decimal(10, 0) default 0 not null,
  "EvalueYM" varchar2(7),
  "AgTransferCode" varchar2(2),
  "Birth" decimal(8, 0) default 0 not null,
  "Education" varchar2(1),
  "LrInd" varchar2(1),
  "ProceccDate" decimal(8, 0) default 0 not null,
  "QuitDate" decimal(8, 0) default 0 not null,
  "AgPost" varchar2(2),
  "LevelNameChs" nvarchar2(10),
  "LrSystemType" varchar2(1),
  "SeniorityYY" decimal(5, 0) default 0 not null,
  "SeniorityMM" decimal(5, 0) default 0 not null,
  "SeniorityDD" decimal(5, 0) default 0 not null,
  "AglaProcessInd" varchar2(2),
  "StatusCode" varchar2(1),
  "AglaCancelReason" varchar2(1),
  "ISAnnApplDate" decimal(8, 0) default 0 not null,
  "RecordDateC" decimal(8, 0) default 0 not null,
  "StopReason" nvarchar2(20),
  "StopStrDate" decimal(8, 0) default 0 not null,
  "StopEndDate" decimal(8, 0) default 0 not null,
  "IFPDate" decimal(8, 0) default 0 not null,
  "EffectStrDate" decimal(8, 0) default 0 not null,
  "EffectEndDate" decimal(8, 0) default 0 not null,
  "AnnApplDate" decimal(8, 0) default 0 not null,
  "CenterCodeAccName" nvarchar2(20),
  "ReHireCode" varchar2(1),
  "RSVDAdminCode" varchar2(1),
  "Account" varchar2(16),
  "PRPDate" varchar2(15),
  "Zip" varchar2(5),
  "Address" nvarchar2(80),
  "PhoneH" varchar2(30),
  "PhoneC" varchar2(30),
  "SalesQualInd" varchar2(1),
  "AgsqStartDate" decimal(8, 0) default 0 not null,
  "PinYinNameIndi" nvarchar2(50),
  "Email" varchar2(50),
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table "CdEmp" add constraint "CdEmp_PK" primary key("EmployeeNo");

create index "CdEmp_Index1" on "CdEmp"("EmployeeNo" asc);

comment on table "CdEmp" is '員工資料檔';
comment on column "CdEmp"."AgentCode" is '業務員代號';
comment on column "CdEmp"."EmployeeNo" is '電腦編號';
comment on column "CdEmp"."Fullname" is '姓名';
comment on column "CdEmp"."CenterShortName" is '單位簡稱';
comment on column "CdEmp"."CenterCodeName" is '單位名稱';
comment on column "CdEmp"."CenterCode1" is '區部代號';
comment on column "CdEmp"."CenterCode1Short" is '區部簡稱';
comment on column "CdEmp"."CenterCode1Name" is '區部名稱';
comment on column "CdEmp"."CenterCode2" is '部室代號';
comment on column "CdEmp"."CenterCode2Short" is '部室簡稱';
comment on column "CdEmp"."CenterCode2Name" is '部室名稱';
comment on column "CdEmp"."CenterCodeAcc1" is '區部代號(駐在單位)';
comment on column "CdEmp"."CenterCodeAcc1Name" is '區部名稱(駐在單位)';
comment on column "CdEmp"."CenterCodeAcc2" is '部室代號(駐在單位)';
comment on column "CdEmp"."CenterCodeAcc2Name" is '部室名稱(駐在單位)';
comment on column "CdEmp"."CommLineCode" is '業務線代號';
comment on column "CdEmp"."CommLineType" is '業務線別';
comment on column "CdEmp"."OrigIntroducerId" is '介紹人';
comment on column "CdEmp"."IntroducerInd" is '介紹關係碼';
comment on column "CdEmp"."RegisterLevel" is '報聘職等';
comment on column "CdEmp"."RegisterDate" is '在職/締約日期';
comment on column "CdEmp"."CenterCode" is '單位代號';
comment on column "CdEmp"."AdministratId" is '單位主管';
comment on column "CdEmp"."InputDate" is '建檔日期';
comment on column "CdEmp"."InputUser" is '建檔人';
comment on column "CdEmp"."AgStatusCode" is '業務人員任用狀況碼';
comment on column "CdEmp"."AgStatusDate" is '業務人員任用狀況異動日';
comment on column "CdEmp"."TranDate" is '作業日期(交易日期)';
comment on column "CdEmp"."TranUser" is '作業者';
comment on column "CdEmp"."ReRegisterDate" is '再聘日';
comment on column "CdEmp"."DirectorId" is '上層主管';
comment on column "CdEmp"."DirectorIdF" is '主管_財務';
comment on column "CdEmp"."IntroducerId" is '區主任/上一代主管';
comment on column "CdEmp"."IntroducerIdF" is '推介人_財務';
comment on column "CdEmp"."AgLevel" is '業務人員職等';
comment on column "CdEmp"."LastLevel" is '前次業務人員職等';
comment on column "CdEmp"."LevelDate" is '職等異動日';
comment on column "CdEmp"."TopLevel" is '最高職等';
comment on column "CdEmp"."OccpInd" is '任職型態';
comment on column "CdEmp"."QuotaAmt" is '責任額';
comment on column "CdEmp"."ApplType" is '申請登錄類別';
comment on column "CdEmp"."TaxRate" is '所得稅率';
comment on column "CdEmp"."SocialInsuClass" is '勞保等級';
comment on column "CdEmp"."PromotLevelYM" is '職等平階起始年月';
comment on column "CdEmp"."DirectorYM" is '晉陞主管年月';
comment on column "CdEmp"."RecordDate" is '登錄日期';
comment on column "CdEmp"."ExRecordDate" is '發證日期';
comment on column "CdEmp"."RxTrDate" is '證書日期/測驗日期';
comment on column "CdEmp"."ExTrIdent" is '證書字號';
comment on column "CdEmp"."ExTrIdent2" is '中專證號';
comment on column "CdEmp"."ExTrIdent3" is '投資型證號';
comment on column "CdEmp"."ExTrDate" is '投資登錄日期';
comment on column "CdEmp"."RegisterBefore" is '報聘前年資(月表示)';
comment on column "CdEmp"."DirectorAfter" is '主管年資';
comment on column "CdEmp"."MedicalCode" is '免體檢授權碼';
comment on column "CdEmp"."ExChgDate" is '換證日期';
comment on column "CdEmp"."ExDelDate" is '註銷日期';
comment on column "CdEmp"."ApplCode" is '申請業務類別';
comment on column "CdEmp"."FirstRegDate" is '初次登錄日';
comment on column "CdEmp"."AginSource" is '業務來源之專案';
comment on column "CdEmp"."AguiCenter" is '單位代號';
comment on column "CdEmp"."AgentId" is '業務人員身份證字號';
comment on column "CdEmp"."TopId" is '業務人員主管';
comment on column "CdEmp"."AgDegree" is '業務人員職級';
comment on column "CdEmp"."CollectInd" is '收費員指示碼';
comment on column "CdEmp"."AgType1" is '制度別';
comment on column "CdEmp"."ContractInd" is '單雙合約碼';
comment on column "CdEmp"."ContractIndYM" is '單雙合約異動工作月';
comment on column "CdEmp"."AgType2" is '身份別';
comment on column "CdEmp"."AgType3" is '特殊人員碼';
comment on column "CdEmp"."AgType4" is '新舊制別';
comment on column "CdEmp"."AginInd1" is '辦事員碼';
comment on column "CdEmp"."AgPoInd" is '可招攬指示碼';
comment on column "CdEmp"."AgDocInd" is '齊件否';
comment on column "CdEmp"."NewHireType" is '新舊人指示碼';
comment on column "CdEmp"."AgCurInd" is '現職指示碼';
comment on column "CdEmp"."AgSendType" is '發文類別';
comment on column "CdEmp"."AgSendNo" is '發文文號';
comment on column "CdEmp"."RegisterDate2" is '任職日期';
comment on column "CdEmp"."AgReturnDate" is '回任日期';
comment on column "CdEmp"."AgTransferDateF" is '初次轉制日期';
comment on column "CdEmp"."AgTransferDate" is '轉制日期';
comment on column "CdEmp"."PromotYM" is '初次晉升年月';
comment on column "CdEmp"."PromotYMF" is '生效業績年月';
comment on column "CdEmp"."AgPostChgDate" is '職務異動日';
comment on column "CdEmp"."FamiliesTax" is '扶養人數';
comment on column "CdEmp"."AgentCodeI" is '原區主任代號';
comment on column "CdEmp"."AgLevelSys" is '職等_系統';
comment on column "CdEmp"."AgPostIn" is '內階職務';
comment on column "CdEmp"."CenterCodeAcc" is '駐在單位';
comment on column "CdEmp"."EvalueInd" is '考核特殊碼';
comment on column "CdEmp"."EvalueInd1" is '辦法優待碼';
comment on column "CdEmp"."BatchNo" is '批次號碼';
comment on column "CdEmp"."EvalueYM" is '考核年月';
comment on column "CdEmp"."AgTransferCode" is '轉檔碼';
comment on column "CdEmp"."Birth" is '出生年月日';
comment on column "CdEmp"."Education" is '學歷';
comment on column "CdEmp"."LrInd" is '勞退狀況';
comment on column "CdEmp"."ProceccDate" is '資料處理時間';
comment on column "CdEmp"."QuitDate" is '離職/停約日';
comment on column "CdEmp"."AgPost" is '職務';
comment on column "CdEmp"."LevelNameChs" is '職等中文';
comment on column "CdEmp"."LrSystemType" is '勞退碼';
comment on column "CdEmp"."SeniorityYY" is '年資_年';
comment on column "CdEmp"."SeniorityMM" is '年資_月';
comment on column "CdEmp"."SeniorityDD" is '年資_日';
comment on column "CdEmp"."AglaProcessInd" is '登錄處理事項';
comment on column "CdEmp"."StatusCode" is '登錄狀態';
comment on column "CdEmp"."AglaCancelReason" is '註銷原因';
comment on column "CdEmp"."ISAnnApplDate" is '利變年金通報日';
comment on column "CdEmp"."RecordDateC" is '外幣保單登入日';
comment on column "CdEmp"."StopReason" is '停招/撤銷原因';
comment on column "CdEmp"."StopStrDate" is '停止招攬起日';
comment on column "CdEmp"."StopEndDate" is '停止招攬迄日';
comment on column "CdEmp"."IFPDate" is 'IFP登錄日';
comment on column "CdEmp"."EffectStrDate" is '撤銷起日';
comment on column "CdEmp"."EffectEndDate" is '撤銷迄日';
comment on column "CdEmp"."AnnApplDate" is '一般年金通報日';
comment on column "CdEmp"."CenterCodeAccName" is '單位名稱(駐在單位)';
comment on column "CdEmp"."ReHireCode" is '重僱碼';
comment on column "CdEmp"."RSVDAdminCode" is '特別碼';
comment on column "CdEmp"."Account" is '帳號';
comment on column "CdEmp"."PRPDate" is '優體測驗通過日';
comment on column "CdEmp"."Zip" is '戶籍地址郵遞區號';
comment on column "CdEmp"."Address" is '戶籍地址';
comment on column "CdEmp"."PhoneH" is '住家電話';
comment on column "CdEmp"."PhoneC" is '手機電話';
comment on column "CdEmp"."SalesQualInd" is '基金銷售資格碼';
comment on column "CdEmp"."AgsqStartDate" is '基金銷售資格日';
comment on column "CdEmp"."PinYinNameIndi" is '原住民羅馬拼音姓名';
comment on column "CdEmp"."Email" is '電子郵件';
comment on column "CdEmp"."CreateDate" is '建檔日期時間';
comment on column "CdEmp"."CreateEmpNo" is '建檔人員';
comment on column "CdEmp"."LastUpdate" is '最後更新日期時間';
comment on column "CdEmp"."LastUpdateEmpNo" is '最後更新人員';
