drop table "BankRemit" purge;

create table "BankRemit" (
  "AcDate" decimal(8, 0) default 0 not null,
  "TitaTlrNo" varchar2(8),
  "TitaTxtNo" varchar2(8),
  "BatchNo" varchar2(6),
  "DrawdownCode" decimal(2, 0) default 0 not null,
  "StatusCode" decimal(1, 0) default 0 not null,
  "RemitBank" varchar2(3),
  "RemitBranch" varchar2(4),
  "RemitAcctNo" varchar2(14),
  "CustNo" decimal(7, 0) default 0 not null,
  "FacmNo" decimal(3, 0) default 0 not null,
  "BormNo" decimal(3, 0) default 0 not null,
  "CustName" nvarchar2(100),
  "Remark" nvarchar2(100),
  "CurrencyCode" varchar2(3),
  "RemitAmt" decimal(16, 2) default 0 not null,
  "AmlRsp" varchar2(1),
  "ActFg" decimal(1, 0) default 0 not null,
  "ModifyContent" nvarchar2(500),
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table "BankRemit" add constraint "BankRemit_PK" primary key("AcDate", "TitaTlrNo", "TitaTxtNo");

create index "BankRemit_Index1" on "BankRemit"("CustNo" asc);

comment on table "BankRemit" is '撥款匯款檔';
comment on column "BankRemit"."AcDate" is '會計日期';
comment on column "BankRemit"."TitaTlrNo" is '經辦';
comment on column "BankRemit"."TitaTxtNo" is '交易序號';
comment on column "BankRemit"."BatchNo" is '整批批號';
comment on column "BankRemit"."DrawdownCode" is '撥款方式';
comment on column "BankRemit"."StatusCode" is '狀態';
comment on column "BankRemit"."RemitBank" is '匯款銀行';
comment on column "BankRemit"."RemitBranch" is '匯款分行';
comment on column "BankRemit"."RemitAcctNo" is '匯款帳號';
comment on column "BankRemit"."CustNo" is '收款戶號';
comment on column "BankRemit"."FacmNo" is '額度編號';
comment on column "BankRemit"."BormNo" is '撥款序號';
comment on column "BankRemit"."CustName" is '收款戶名';
comment on column "BankRemit"."Remark" is '附言';
comment on column "BankRemit"."CurrencyCode" is '幣別';
comment on column "BankRemit"."RemitAmt" is '匯款金額';
comment on column "BankRemit"."AmlRsp" is 'AML回應碼';
comment on column "BankRemit"."ActFg" is '交易進行記號';
comment on column "BankRemit"."ModifyContent" is '產檔後修正後內容';
comment on column "BankRemit"."CreateDate" is '建檔日期時間';
comment on column "BankRemit"."CreateEmpNo" is '建檔人員';
comment on column "BankRemit"."LastUpdate" is '最後更新日期時間';
comment on column "BankRemit"."LastUpdateEmpNo" is '最後更新人員';
