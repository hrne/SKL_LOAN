drop table "CustomerAmlRating" purge;

create table "CustomerAmlRating" (
  "CustId" varchar2(10),
  "AmlRating" varchar2(1),
  "IsRelated" varchar2(1),
  "IsLnrelNear" varchar2(1),
  "IsLimit" varchar2(1),
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table "CustomerAmlRating" add constraint "CustomerAmlRating_PK" primary key("CustId");

comment on table "CustomerAmlRating" is '客戶AML評級資料檔(eLoan)';
comment on column "CustomerAmlRating"."CustId" is '身份證字號/統一編號';
comment on column "CustomerAmlRating"."AmlRating" is 'AML評級';
comment on column "CustomerAmlRating"."IsRelated" is '利害關係人記號';
comment on column "CustomerAmlRating"."IsLnrelNear" is '準利害關係人記號';
comment on column "CustomerAmlRating"."IsLimit" is '授信限制對象記號';
comment on column "CustomerAmlRating"."CreateDate" is '建檔日期時間';
comment on column "CustomerAmlRating"."CreateEmpNo" is '建檔人員';
comment on column "CustomerAmlRating"."LastUpdate" is '最後更新日期時間';
comment on column "CustomerAmlRating"."LastUpdateEmpNo" is '最後更新人員';
