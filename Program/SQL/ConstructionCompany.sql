drop table "ConstructionCompany" purge;

create table "ConstructionCompany" (
  "CustNo" decimal(7, 0) default 0 not null,
  "DeleteFlag" varchar2(1),
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table "ConstructionCompany" add constraint "ConstructionCompany_PK" primary key("CustNo");

comment on table "ConstructionCompany" is '建商名單檔';
comment on column "ConstructionCompany"."CustNo" is '戶號';
comment on column "ConstructionCompany"."DeleteFlag" is '刪除碼';
comment on column "ConstructionCompany"."CreateDate" is '建檔日期時間';
comment on column "ConstructionCompany"."CreateEmpNo" is '建檔人員';
comment on column "ConstructionCompany"."LastUpdate" is '最後更新日期時間';
comment on column "ConstructionCompany"."LastUpdateEmpNo" is '最後更新人員';
