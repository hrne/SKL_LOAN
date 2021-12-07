drop table "TxAttachType" purge;

drop sequence "TxAttachType_SEQ";

create table "TxAttachType" (
  "TypeNo" decimal(11,0) not null,
  "TranNo" varchar2(5),
  "TypeItem" nvarchar2(50),
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table "TxAttachType" add constraint "TxAttachType_PK" primary key("TypeNo");

create sequence "TxAttachType_SEQ" minvalue 1 maxvalue 2147483647 increment by 1 start with 1 nocache cycle;

create index "TxAttachType_Index1" on "TxAttachType"("TranNo" asc, "TypeItem" asc);

comment on table "TxAttachType" is '附件類別檔';
comment on column "TxAttachType"."TypeNo" is '檔案序號';
comment on column "TxAttachType"."TranNo" is '交易代號';
comment on column "TxAttachType"."TypeItem" is '附件類別';
comment on column "TxAttachType"."CreateDate" is '建檔日期時間';
comment on column "TxAttachType"."CreateEmpNo" is '建檔人員';
comment on column "TxAttachType"."LastUpdate" is '最後更新日期時間';
comment on column "TxAttachType"."LastUpdateEmpNo" is '最後更新人員';
