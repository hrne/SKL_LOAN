drop table "PfDetail" purge;

drop sequence "PfDetail_SEQ";

create table "PfDetail" (
  "LogNo" decimal(11,0) not null,
  "PerfDate" decimal(8, 0) default 0 not null,
  "CustNo" decimal(7, 0) default 0 not null,
  "FacmNo" decimal(3, 0) default 0 not null,
  "BormNo" decimal(3, 0) default 0 not null,
  "BorxNo" decimal(4, 0) default 0 not null,
  "RepayType" decimal(1, 0) default 0 not null,
  "DrawdownDate" decimal(8, 0) default 0 not null,
  "DrawdownAmt" decimal(16, 2) default 0 not null,
  "PieceCode" varchar2(1),
  "RepaidPeriod" decimal(3, 0) default 0 not null,
  "ProdCode" varchar2(5),
  "CreditSysNo" decimal(7, 0) default 0 not null,
  "Introducer" nvarchar2(8),
  "Coorgnizer" varchar2(6),
  "InterviewerA" varchar2(6),
  "InterviewerB" varchar2(6),
  "IsReNewEmpUnit" varchar2(1),
  "UnitCode" varchar2(6),
  "DistCode" varchar2(6),
  "DeptCode" varchar2(6),
  "UnitManager" nvarchar2(8),
  "DistManager" nvarchar2(8),
  "DeptManager" nvarchar2(8),
  "ComputeItAmtFac" decimal(16, 2) default 0 not null,
  "ItPerfCnt" decimal(5, 1) default 0 not null,
  "ComputeItAmt" decimal(16, 2) default 0 not null,
  "ItPerfAmt" decimal(16, 2) default 0 not null,
  "ItPerfEqAmt" decimal(16, 2) default 0 not null,
  "ItPerfReward" decimal(16, 2) default 0 not null,
  "ComputeItBonusAmt" decimal(16, 2) default 0 not null,
  "ItBonus" decimal(16, 2) default 0 not null,
  "ComputeAddBonusAmt" decimal(16, 2) default 0 not null,
  "ItAddBonus" decimal(16, 2) default 0 not null,
  "ComputeCoBonusAmt" decimal(16, 2) default 0 not null,
  "CoorgnizerBonus" decimal(16, 2) default 0 not null,
  "BsOfficer" varchar2(6),
  "BsDeptCode" varchar2(6),
  "ComputeBsAmtFac" decimal(16, 2) default 0 not null,
  "BsPerfCnt" decimal(5, 1) default 0 not null,
  "ComputeBsAmt" decimal(16, 2) default 0 not null,
  "BsPerfAmt" decimal(16, 2) default 0 not null,
  "WorkMonth" decimal(6, 0) default 0 not null,
  "WorkSeason" decimal(5, 0) default 0 not null,
  "PieceCodeCombine" varchar2(1),
  "IsProdFinancial" varchar2(1),
  "IsIntroducerDay15" varchar2(1),
  "IsCoorgnizerDay15" varchar2(1),
  "IsProdExclude1" varchar2(1),
  "IsProdExclude2" varchar2(1),
  "IsProdExclude3" varchar2(1),
  "IsProdExclude4" varchar2(1),
  "IsProdExclude5" varchar2(1),
  "IsDeptExclude1" varchar2(1),
  "IsDeptExclude2" varchar2(1),
  "IsDeptExclude3" varchar2(1),
  "IsDeptExclude4" varchar2(1),
  "IsDeptExclude5" varchar2(1),
  "IsDay15Exclude1" varchar2(1),
  "IsDay15Exclude2" varchar2(1),
  "IsDay15Exclude3" varchar2(1),
  "IsDay15Exclude4" varchar2(1),
  "IsDay15Exclude5" varchar2(1),
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table "PfDetail" add constraint "PfDetail_PK" primary key("LogNo");

create sequence "PfDetail_SEQ" minvalue 1 maxvalue 2147483647 increment by 1 start with 1 nocache cycle;

comment on table "PfDetail" is '業績計算明細檔';
comment on column "PfDetail"."LogNo" is '序號';
comment on column "PfDetail"."PerfDate" is '業績日期';
comment on column "PfDetail"."CustNo" is '戶號';
comment on column "PfDetail"."FacmNo" is '額度編號';
comment on column "PfDetail"."BormNo" is '撥款序號';
comment on column "PfDetail"."BorxNo" is '交易內容檔序號';
comment on column "PfDetail"."RepayType" is '還款類別';
comment on column "PfDetail"."DrawdownDate" is '撥款日';
comment on column "PfDetail"."DrawdownAmt" is '撥款金額/追回金額';
comment on column "PfDetail"."PieceCode" is '計件代碼';
comment on column "PfDetail"."RepaidPeriod" is '已攤還期數';
comment on column "PfDetail"."ProdCode" is '商品代碼';
comment on column "PfDetail"."CreditSysNo" is '案件編號';
comment on column "PfDetail"."Introducer" is '介紹人';
comment on column "PfDetail"."Coorgnizer" is '協辦人員編';
comment on column "PfDetail"."InterviewerA" is '晤談一員編';
comment on column "PfDetail"."InterviewerB" is '晤談二員編';
comment on column "PfDetail"."IsReNewEmpUnit" is '業績重算時是否以新員工資料更新介紹人所屬單位Y/N/null';
comment on column "PfDetail"."UnitCode" is '單位代號';
comment on column "PfDetail"."DistCode" is '區部代號';
comment on column "PfDetail"."DeptCode" is '部室代號';
comment on column "PfDetail"."UnitManager" is '處經理代號';
comment on column "PfDetail"."DistManager" is '區經理代號';
comment on column "PfDetail"."DeptManager" is '部經理代號';
comment on column "PfDetail"."ComputeItAmtFac" is '介紹單位件數累計計算金額';
comment on column "PfDetail"."ItPerfCnt" is '介紹單位件數';
comment on column "PfDetail"."ComputeItAmt" is '介紹人業績金額計算金額';
comment on column "PfDetail"."ItPerfAmt" is '介紹人業績金額';
comment on column "PfDetail"."ItPerfEqAmt" is '介紹人換算業績';
comment on column "PfDetail"."ItPerfReward" is '介紹人業務報酬';
comment on column "PfDetail"."ComputeItBonusAmt" is '介紹人介紹獎金計算金額';
comment on column "PfDetail"."ItBonus" is '介紹人介紹獎金';
comment on column "PfDetail"."ComputeAddBonusAmt" is '介紹人加碼獎勵津貼計算金額';
comment on column "PfDetail"."ItAddBonus" is '介紹人加碼獎勵津貼';
comment on column "PfDetail"."ComputeCoBonusAmt" is '協辦人員協辦獎金計算金額';
comment on column "PfDetail"."CoorgnizerBonus" is '協辦人員協辦獎金';
comment on column "PfDetail"."BsOfficer" is '房貸專員';
comment on column "PfDetail"."BsDeptCode" is '部室代號';
comment on column "PfDetail"."ComputeBsAmtFac" is '房貸專員件數累計計算金額';
comment on column "PfDetail"."BsPerfCnt" is '房貸專員件數';
comment on column "PfDetail"."ComputeBsAmt" is '房貸專員計算金額';
comment on column "PfDetail"."BsPerfAmt" is '房貸專員業績金額';
comment on column "PfDetail"."WorkMonth" is '工作月';
comment on column "PfDetail"."WorkSeason" is '工作季';
comment on column "PfDetail"."PieceCodeCombine" is '連同計件代碼';
comment on column "PfDetail"."IsProdFinancial" is '理財型房貸(Y/N)';
comment on column "PfDetail"."IsIntroducerDay15" is '介紹人是否為15日薪(Y/Null)';
comment on column "PfDetail"."IsCoorgnizerDay15" is '協辦人員是否為15日薪(Y/Null)';
comment on column "PfDetail"."IsProdExclude1" is '計算業績排除商品別(Y/Null)';
comment on column "PfDetail"."IsProdExclude2" is '計算業績排除商品別(Y/Null)';
comment on column "PfDetail"."IsProdExclude3" is '計算業績排除商品別(Y/Null)';
comment on column "PfDetail"."IsProdExclude4" is '計算業績排除商品別(Y/Nnull)';
comment on column "PfDetail"."IsProdExclude5" is '計算業績排除商品別(Y/Null)';
comment on column "PfDetail"."IsDeptExclude1" is '是否屬排徐部門別(Y/Null)';
comment on column "PfDetail"."IsDeptExclude2" is '是否屬排徐部門別(Y/Null)';
comment on column "PfDetail"."IsDeptExclude3" is '是否屬排徐部門別(Y/Null)';
comment on column "PfDetail"."IsDeptExclude4" is '是否屬排徐部門別(Y/Null)';
comment on column "PfDetail"."IsDeptExclude5" is '是否屬排徐部門別(Y/Null)';
comment on column "PfDetail"."IsDay15Exclude1" is '是否屬15日薪被排除(Y/Null/E-屬15日薪未設排除條件)';
comment on column "PfDetail"."IsDay15Exclude2" is '是否屬15日薪被排除(Y/Null/E-屬15日薪未設排除條件)';
comment on column "PfDetail"."IsDay15Exclude3" is '是否屬15日薪被排除(Y/Null/E-屬15日薪未設排除條件)';
comment on column "PfDetail"."IsDay15Exclude4" is '是否屬15日薪被排除(Y/Null/E-屬15日薪未設排除條件)';
comment on column "PfDetail"."IsDay15Exclude5" is '是否屬15日薪被排除(Y/Null/E-屬15日薪未設排除條件)';
comment on column "PfDetail"."CreateDate" is '建檔日期時間';
comment on column "PfDetail"."CreateEmpNo" is '建檔人員';
comment on column "PfDetail"."LastUpdate" is '最後更新日期時間';
comment on column "PfDetail"."LastUpdateEmpNo" is '最後更新人員';
