drop table "CustDataCtrl" purge;

create table "CustDataCtrl" (
  "CustNo" decimal(7, 0) default 0 not null,
  "CustUKey" varchar2(32),
  "Enable" varchar2(1),
  "ApplMark" decimal(1, 0) default 0 not null,
  "Reason" varchar2(50),
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table "CustDataCtrl" add constraint "CustDataCtrl_PK" primary key("CustNo");

alter table "CustDataCtrl" add constraint "CustDataCtrl_CustMain_FK1" foreign key ("CustUKey") references "CustMain" ("CustUKey") on delete cascade;

comment on table "CustDataCtrl" is '結清戶個資控管檔';
comment on column "CustDataCtrl"."CustNo" is '借款人戶號';
comment on column "CustDataCtrl"."CustUKey" is '客戶識別碼';
comment on column "CustDataCtrl"."Enable" is '啟用記號';
comment on column "CustDataCtrl"."ApplMark" is '申請記號';
comment on column "CustDataCtrl"."Reason" is '解除原因';
comment on column "CustDataCtrl"."CreateDate" is '建檔日期時間';
comment on column "CustDataCtrl"."CreateEmpNo" is '建檔人員';
comment on column "CustDataCtrl"."LastUpdate" is '最後更新日期時間';
comment on column "CustDataCtrl"."LastUpdateEmpNo" is '最後更新人員';
