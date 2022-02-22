drop table "SlipMedia2022" purge;

create table "SlipMedia2022" (
  "AcBookCode" varchar2(3),
  "MediaSlipNo" varchar2(12),
  "Seq" decimal(5, 0) default 0 not null,
  "AcDate" decimal(8, 0) default 0 not null,
  "BatchNo" decimal(2, 0) default 0 not null,
  "MediaSeq" decimal(3, 0) default 0 not null,
  "AcNoCode" varchar2(11),
  "AcSubCode" varchar2(5),
  "DeptCode" varchar2(6),
  "DbCr" varchar2(1),
  "TxAmt" decimal(14, 2) default 0 not null,
  "SlipRmk" varchar2(80),
  "ReceiveCode" varchar2(15),
  "CostMonth" varchar2(2),
  "InsuNo" varchar2(10),
  "SalesmanCode" varchar2(12),
  "SalaryCode" varchar2(2),
  "CurrencyCode" varchar2(3),
  "AcSubBookCode" varchar2(3),
  "CostUnit" varchar2(6),
  "SalesChannelType" varchar2(2),
  "IfrsType" varchar2(1),
  "RelationId" varchar2(10),
  "RelateCode" varchar2(3),
  "Ifrs17Group" varchar2(9),
  "LatestFlag" varchar2(1),
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table "SlipMedia2022" add constraint "SlipMedia2022_PK" primary key("MediaSlipNo", "Seq");

comment on table "SlipMedia2022" is '傳票媒體檔2022年格式';
comment on column "SlipMedia2022"."AcBookCode" is '帳冊別';
comment on column "SlipMedia2022"."MediaSlipNo" is '傳票號碼';
comment on column "SlipMedia2022"."Seq" is '傳票明細序號';
comment on column "SlipMedia2022"."AcDate" is '傳票日期';
comment on column "SlipMedia2022"."BatchNo" is '傳票批號';
comment on column "SlipMedia2022"."MediaSeq" is '上傳核心序號';
comment on column "SlipMedia2022"."AcNoCode" is '科目代號';
comment on column "SlipMedia2022"."AcSubCode" is '子目代號';
comment on column "SlipMedia2022"."DeptCode" is '部門代號';
comment on column "SlipMedia2022"."DbCr" is '借貸別';
comment on column "SlipMedia2022"."TxAmt" is '金額';
comment on column "SlipMedia2022"."SlipRmk" is '傳票摘要';
comment on column "SlipMedia2022"."ReceiveCode" is '會計科目銷帳碼';
comment on column "SlipMedia2022"."CostMonth" is '成本月份';
comment on column "SlipMedia2022"."InsuNo" is '保單號碼';
comment on column "SlipMedia2022"."SalesmanCode" is '業務員代號';
comment on column "SlipMedia2022"."SalaryCode" is '薪碼';
comment on column "SlipMedia2022"."CurrencyCode" is '幣別';
comment on column "SlipMedia2022"."AcSubBookCode" is '區隔帳冊';
comment on column "SlipMedia2022"."CostUnit" is '成本單位';
comment on column "SlipMedia2022"."SalesChannelType" is '通路別';
comment on column "SlipMedia2022"."IfrsType" is '會計準則類型';
comment on column "SlipMedia2022"."RelationId" is '關係人ID';
comment on column "SlipMedia2022"."RelateCode" is '關聯方代號';
comment on column "SlipMedia2022"."Ifrs17Group" is 'IFRS17群組';
comment on column "SlipMedia2022"."LatestFlag" is '是否為最新';
comment on column "SlipMedia2022"."CreateDate" is '建檔日期時間';
comment on column "SlipMedia2022"."CreateEmpNo" is '建檔人員';
comment on column "SlipMedia2022"."LastUpdate" is '最後更新日期時間';
comment on column "SlipMedia2022"."LastUpdateEmpNo" is '最後更新人員';
