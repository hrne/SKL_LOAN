drop table "MonthlyLM028" purge;

create table "MonthlyLM028" (
  "DataMonth" decimal(6, 0) default 0 not null,
  "Status" decimal(2, 0) default 0 not null,
  "EntCode" decimal(1, 0) default 0 not null,
  "BranchNo" varchar2(4),
  "CustNo" decimal(7, 0) default 0 not null,
  "FacmNo" decimal(3, 0) default 0 not null,
  "BormNo" decimal(3, 0) default 0 not null,
  "StoreRate" decimal(6, 4) default 0 not null,
  "PayIntFreq" decimal(2, 0) default 0 not null,
  "MaturityYear" decimal(4, 0) default 0 not null,
  "MaturityMonth" decimal(2, 0) default 0 not null,
  "MaturityDay" decimal(2, 0) default 0 not null,
  "LoanBal" decimal(16, 2) default 0 not null,
  "RateCode" varchar2(1),
  "PostDepCode" varchar2(1),
  "SpecificDd" decimal(2, 0) default 0 not null,
  "FirstRateAdjFreq" decimal(2, 0) default 0 not null,
  "BaseRateCode" varchar2(2),
  "FitRate1" decimal(6, 4) default 0 not null,
  "FitRate2" decimal(6, 4) default 0 not null,
  "FitRate3" decimal(6, 4) default 0 not null,
  "FitRate4" decimal(6, 4) default 0 not null,
  "FitRate5" decimal(6, 4) default 0 not null,
  "ClCode1" decimal(1, 0) default 0 not null,
  "ClCode2" decimal(2, 0) default 0 not null,
  "DrawdownYear" decimal(4, 0) default 0 not null,
  "DrawdownMonth" decimal(2, 0) default 0 not null,
  "DrawdownDay" decimal(2, 0) default 0 not null,
  "W08Code" decimal(1, 0) default 0 not null,
  "IsRelation" varchar2(1),
  "AgType1" varchar2(1),
  "AcctSource" varchar2(1),
  "LastestRate" decimal(6, 4) default 0 not null,
  "LastestRateStartDate" decimal(8, 0) default 0 not null,
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table "MonthlyLM028" add constraint "MonthlyLM028_PK" primary key("DataMonth", "CustNo", "FacmNo", "BormNo");

comment on table "MonthlyLM028" is 'LM028預估現金流量月報工作檔';
comment on column "MonthlyLM028"."DataMonth" is '資料年月';
comment on column "MonthlyLM028"."Status" is '戶況';
comment on column "MonthlyLM028"."EntCode" is '企金別';
comment on column "MonthlyLM028"."BranchNo" is '營業單位別';
comment on column "MonthlyLM028"."CustNo" is '借款人戶號';
comment on column "MonthlyLM028"."FacmNo" is '額度編號';
comment on column "MonthlyLM028"."BormNo" is '撥款序號';
comment on column "MonthlyLM028"."StoreRate" is '利率';
comment on column "MonthlyLM028"."PayIntFreq" is '繳息週期';
comment on column "MonthlyLM028"."MaturityYear" is '額度主檔到期日-年';
comment on column "MonthlyLM028"."MaturityMonth" is '額度主檔到期日-月';
comment on column "MonthlyLM028"."MaturityDay" is '額度主檔到期日-日';
comment on column "MonthlyLM028"."LoanBal" is '放款餘額';
comment on column "MonthlyLM028"."RateCode" is '利率區分';
comment on column "MonthlyLM028"."PostDepCode" is '郵局存款別';
comment on column "MonthlyLM028"."SpecificDd" is '應繳日';
comment on column "MonthlyLM028"."FirstRateAdjFreq" is '首次調整週期';
comment on column "MonthlyLM028"."BaseRateCode" is '基本利率代碼';
comment on column "MonthlyLM028"."FitRate1" is '利率1';
comment on column "MonthlyLM028"."FitRate2" is '利率2';
comment on column "MonthlyLM028"."FitRate3" is '利率3';
comment on column "MonthlyLM028"."FitRate4" is '利率4';
comment on column "MonthlyLM028"."FitRate5" is '利率5';
comment on column "MonthlyLM028"."ClCode1" is '押品別１';
comment on column "MonthlyLM028"."ClCode2" is '押品別２';
comment on column "MonthlyLM028"."DrawdownYear" is '撥款日-年';
comment on column "MonthlyLM028"."DrawdownMonth" is '撥款日-月';
comment on column "MonthlyLM028"."DrawdownDay" is '撥款日-日';
comment on column "MonthlyLM028"."W08Code" is '到期日碼';
comment on column "MonthlyLM028"."IsRelation" is '是否為關係人';
comment on column "MonthlyLM028"."AgType1" is '制度別';
comment on column "MonthlyLM028"."AcctSource" is '資金來源';
comment on column "MonthlyLM028"."LastestRate" is '最新利率';
comment on column "MonthlyLM028"."LastestRateStartDate" is '最新利率生效起日';
comment on column "MonthlyLM028"."CreateDate" is '建檔日期時間';
comment on column "MonthlyLM028"."CreateEmpNo" is '建檔人員';
comment on column "MonthlyLM028"."LastUpdate" is '最後更新日期時間';
comment on column "MonthlyLM028"."LastUpdateEmpNo" is '最後更新人員';
