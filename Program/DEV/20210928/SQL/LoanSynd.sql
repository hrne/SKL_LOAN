drop table "LoanSynd" purge;

create table "LoanSynd" (
  "SyndNo" decimal(6, 0) default 0 not null,
  "LeadingBank" varchar2(7),
  "AgentBank" varchar2(7),
  "SigningDate" decimal(8, 0) default 0 not null,
  "SyndTypeCodeFlag" varchar2(1),
  "PartRate" decimal(6, 4) default 0 not null,
  "CurrencyCode" varchar2(3),
  "SyndAmt" decimal(16, 2) default 0 not null,
  "PartAmt" decimal(16, 2) default 0 not null,
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table "LoanSynd" add constraint "LoanSynd_PK" primary key("SyndNo");

comment on table "LoanSynd" is '聯貸案訂約檔';
comment on column "LoanSynd"."SyndNo" is '聯貸編號';
comment on column "LoanSynd"."LeadingBank" is '主辦行';
comment on column "LoanSynd"."AgentBank" is '代理行';
comment on column "LoanSynd"."SigningDate" is '簽約日';
comment on column "LoanSynd"."SyndTypeCodeFlag" is '國內或國際聯貸';
comment on column "LoanSynd"."PartRate" is '參貸費率';
comment on column "LoanSynd"."CurrencyCode" is '幣別';
comment on column "LoanSynd"."SyndAmt" is '聯貸總金額';
comment on column "LoanSynd"."PartAmt" is '參貸金額';
comment on column "LoanSynd"."CreateDate" is '建檔日期時間';
comment on column "LoanSynd"."CreateEmpNo" is '建檔人員';
comment on column "LoanSynd"."LastUpdate" is '最後更新日期時間';
comment on column "LoanSynd"."LastUpdateEmpNo" is '最後更新人員';
