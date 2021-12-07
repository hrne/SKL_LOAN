drop table "TxAttachment" purge;

drop sequence "TxAttachment_SEQ";

create table "TxAttachment" (
  "FileNo" decimal(11,0) not null,
  "TranNo" varchar2(5),
  "CustNo" decimal(7, 0) default 0 not null,
  "FacmNo" decimal(3, 0) default 0 not null,
  "BormNo" decimal(3, 0) default 0 not null,
  "MrKey" nvarchar2(100),
  "TypeItem" nvarchar2(50),
  "FileItem" nvarchar2(100),
  "FileData" blob,
  "Desc" nvarchar2(100),
  "Status" nvarchar2(1),
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table "TxAttachment" add constraint "TxAttachment_PK" primary key("FileNo");

create sequence "TxAttachment_SEQ" minvalue 1 maxvalue 2147483647 increment by 1 start with 1 nocache cycle;

comment on table "TxAttachment" is '附件檔';
comment on column "TxAttachment"."FileNo" is '檔案序號';
comment on column "TxAttachment"."TranNo" is '交易代號';
comment on column "TxAttachment"."CustNo" is '戶號';
comment on column "TxAttachment"."FacmNo" is '額度';
comment on column "TxAttachment"."BormNo" is '撥款';
comment on column "TxAttachment"."MrKey" is '交易參考編號';
comment on column "TxAttachment"."TypeItem" is '附件類別';
comment on column "TxAttachment"."FileItem" is '檔名';
comment on column "TxAttachment"."FileData" is '檔案內容';
comment on column "TxAttachment"."Desc" is '備註';
comment on column "TxAttachment"."Status" is '狀態';
comment on column "TxAttachment"."CreateDate" is '建檔日期時間';
comment on column "TxAttachment"."CreateEmpNo" is '建檔人員';
comment on column "TxAttachment"."LastUpdate" is '最後更新日期時間';
comment on column "TxAttachment"."LastUpdateEmpNo" is '最後更新人員';
