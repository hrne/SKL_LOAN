drop table "ClOther" purge;

create table "ClOther" (
  "ClCode1" decimal(1, 0) default 0 not null,
  "ClCode2" decimal(2, 0) default 0 not null,
  "ClNo" decimal(7, 0) default 0 not null,
  "PledgeStartDate" decimal(8, 0) default 0 not null,
  "PledgeEndDate" decimal(8, 0) default 0 not null,
  "PledgeBankCode" varchar2(2),
  "PledgeNO" varchar2(30),
  "OwnerCustUKey" varchar2(32),
  "IssuingId" varchar2(10),
  "IssuingCounty" varchar2(3),
  "DocNo" nvarchar2(30),
  "LoanToValue" decimal(5, 2) default 0 not null,
  "SecuritiesType" varchar2(2),
  "Listed" varchar2(2),
  "OfferingDate" decimal(8, 0) default 0 not null,
  "ExpirationDate" decimal(8, 0) default 0 not null,
  "TargetIssuer" varchar2(2),
  "SubTargetIssuer" varchar2(2),
  "CreditDate" decimal(8, 0) default 0 not null,
  "Credit" varchar2(2),
  "ExternalCredit" varchar2(3),
  "Index" varchar2(2),
  "TradingMethod" varchar2(1),
  "Compensation" varchar2(3),
  "Investment" nvarchar2(300),
  "PublicValue" nvarchar2(300),
  "SettingStat" varchar2(1),
  "ClStat" varchar2(1),
  "SettingDate" decimal(8, 0) default 0 not null,
  "SettingAmt" decimal(16, 2) default 0 not null,
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table "ClOther" add constraint "ClOther_PK" primary key("ClCode1", "ClCode2", "ClNo");

alter table "ClOther" add constraint "ClOther_ClMain_FK1" foreign key ("ClCode1", "ClCode2", "ClNo") references "ClMain" ("ClCode1", "ClCode2", "ClNo") on delete cascade;

create index "ClOther_Index1" on "ClOther"("ClCode1" asc);

create index "ClOther_Index2" on "ClOther"("ClCode1" asc, "ClCode2" asc);

create index "ClOther_Index3" on "ClOther"("OwnerCustUKey" asc);

comment on table "ClOther" is '擔保品其他檔';
comment on column "ClOther"."ClCode1" is '擔保品代號1';
comment on column "ClOther"."ClCode2" is '擔保品代號2';
comment on column "ClOther"."ClNo" is '擔保品編號';
comment on column "ClOther"."PledgeStartDate" is '保證起日';
comment on column "ClOther"."PledgeEndDate" is '保證迄日';
comment on column "ClOther"."PledgeBankCode" is '保證銀行';
comment on column "ClOther"."PledgeNO" is '保證書字號';
comment on column "ClOther"."OwnerCustUKey" is '客戶識別碼';
comment on column "ClOther"."IssuingId" is '發行機構統編';
comment on column "ClOther"."IssuingCounty" is '發行機構所在國別';
comment on column "ClOther"."DocNo" is '憑證編號';
comment on column "ClOther"."LoanToValue" is '貸放成數(%)';
comment on column "ClOther"."SecuritiesType" is '有價證券類別';
comment on column "ClOther"."Listed" is '掛牌交易所';
comment on column "ClOther"."OfferingDate" is '發行日';
comment on column "ClOther"."ExpirationDate" is '到期日';
comment on column "ClOther"."TargetIssuer" is '發行者對象別';
comment on column "ClOther"."SubTargetIssuer" is '發行者次對象別';
comment on column "ClOther"."CreditDate" is '評等日期';
comment on column "ClOther"."Credit" is '評等公司';
comment on column "ClOther"."ExternalCredit" is '外部評等';
comment on column "ClOther"."Index" is '主要指數';
comment on column "ClOther"."TradingMethod" is '交易方法';
comment on column "ClOther"."Compensation" is '受償順位';
comment on column "ClOther"."Investment" is '投資內容';
comment on column "ClOther"."PublicValue" is '公開價值';
comment on column "ClOther"."SettingStat" is '設定狀態';
comment on column "ClOther"."ClStat" is '擔保品狀態';
comment on column "ClOther"."SettingDate" is '設定日期';
comment on column "ClOther"."SettingAmt" is '設定金額';
comment on column "ClOther"."CreateDate" is '建檔日期時間';
comment on column "ClOther"."CreateEmpNo" is '建檔人員';
comment on column "ClOther"."LastUpdate" is '最後更新日期時間';
comment on column "ClOther"."LastUpdateEmpNo" is '最後更新人員';
