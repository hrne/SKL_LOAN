drop table "CdSyndFee" purge;

create table "CdSyndFee" (
  "SyndFeeCode" varchar2(2),
  "SyndFeeItem" nvarchar2(30),
  "AcctCode" varchar2(3),
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table "CdSyndFee" add constraint "CdSyndFee_PK" primary key("SyndFeeCode");

create index "CdSyndFee_Index1" on "CdSyndFee"("AcctCode" asc);

comment on table "CdSyndFee" is '聯貸費用代碼檔';
comment on column "CdSyndFee"."SyndFeeCode" is '聯貸費用代碼';
comment on column "CdSyndFee"."SyndFeeItem" is '聯貸費用說明';
comment on column "CdSyndFee"."AcctCode" is '業務科目代號';
comment on column "CdSyndFee"."CreateDate" is '建檔日期時間';
comment on column "CdSyndFee"."CreateEmpNo" is '建檔人員';
comment on column "CdSyndFee"."LastUpdate" is '最後更新日期時間';
comment on column "CdSyndFee"."LastUpdateEmpNo" is '最後更新人員';
