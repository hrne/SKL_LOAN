drop table "TxAuthorize" purge;

drop sequence "TxAuthorize_SEQ";

create table "TxAuthorize" (
  "AutoSeq" decimal(11,0) not null,
  "SupNo" varchar2(6),
  "TlrNo" varchar2(6),
  "TradeReason" nvarchar2(100),
  "ReasonFAJson" varchar2(1200),
  "Entdy" decimal(8, 0) default 0 not null,
  "Txcd" varchar2(10),
  "TxSeq" varchar2(18),
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table "TxAuthorize" add constraint "TxAuthorize_PK" primary key("AutoSeq");

create sequence "TxAuthorize_SEQ" minvalue 1 maxvalue 2147483647 increment by 1 start with 1 nocache cycle;

create index "TxAuthorize_Index1" on "TxAuthorize"("SupNo" asc, "Entdy" asc);

comment on table "TxAuthorize" is '主管授權紀錄';
comment on column "TxAuthorize"."AutoSeq" is '序號';
comment on column "TxAuthorize"."SupNo" is '授權主管編號';
comment on column "TxAuthorize"."TlrNo" is '交易人員';
comment on column "TxAuthorize"."TradeReason" is '交易理由';
comment on column "TxAuthorize"."ReasonFAJson" is '授權編號和理由';
comment on column "TxAuthorize"."Entdy" is '會計日';
comment on column "TxAuthorize"."Txcd" is '交易代號';
comment on column "TxAuthorize"."TxSeq" is '交易序號';
comment on column "TxAuthorize"."CreateDate" is '建檔日期時間';
comment on column "TxAuthorize"."CreateEmpNo" is '建檔人員';
comment on column "TxAuthorize"."LastUpdate" is '最後更新日期時間';
comment on column "TxAuthorize"."LastUpdateEmpNo" is '最後更新人員';
