drop table "LoanSyndItem" purge;

create table "LoanSyndItem" (
  "SyndNo" decimal(6, 0) default 0 not null,
  "SyndSeq" decimal(3, 0) default 0 not null,
  "Item" varchar2(10),
  "SyndAmt" decimal(16, 2) default 0 not null,
  "SyndMark" nvarchar2(40),
  "SyndBal" decimal(16, 2) default 0 not null,
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table "LoanSyndItem" add constraint "LoanSyndItem_PK" primary key("SyndNo", "SyndSeq");

comment on table "LoanSyndItem" is '聯貸案費用檔';
comment on column "LoanSyndItem"."SyndNo" is '聯貸案編號';
comment on column "LoanSyndItem"."SyndSeq" is '聯貸案序號';
comment on column "LoanSyndItem"."Item" is '項別';
comment on column "LoanSyndItem"."SyndAmt" is '金額';
comment on column "LoanSyndItem"."SyndMark" is '備註';
comment on column "LoanSyndItem"."SyndBal" is '已銷金額';
comment on column "LoanSyndItem"."CreateDate" is '建檔日期時間';
comment on column "LoanSyndItem"."CreateEmpNo" is '建檔人員';
comment on column "LoanSyndItem"."LastUpdate" is '最後更新日期時間';
comment on column "LoanSyndItem"."LastUpdateEmpNo" is '最後更新人員';
