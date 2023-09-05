drop table "PfInsCheck" purge;

create table "PfInsCheck" (
  "Kind" decimal(1, 0) default 0 not null,
  "CustNo" decimal(7, 0) default 0 not null,
  "FacmNo" decimal(3, 0) default 0 not null,
  "CreditSysNo" decimal(7, 0) default 0 not null,
  "CustId" varchar2(10),
  "ApplDate" decimal(8, 0) default 0 not null,
  "InsDate" decimal(8, 0) default 0 not null,
  "InsNo" varchar2(15),
  "CheckResult" varchar2(1),
  "CheckWorkMonth" decimal(6, 0) default 0 not null,
  "ReturnMsg" nvarchar2(2000),
  "ReturnMsg2" nvarchar2(2000),
  "ReturnMsg3" nvarchar2(2000),
  "PerfWorkMonth" decimal(6, 0) default 0 not null,
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table "PfInsCheck" add constraint "PfInsCheck_PK" primary key("Kind", "CustNo", "FacmNo", "CheckWorkMonth", "PerfWorkMonth");

comment on table "PfInsCheck" is '房貸獎勵保費檢核檔';
comment on column "PfInsCheck"."Kind" is '類別';
comment on column "PfInsCheck"."CustNo" is '戶號';
comment on column "PfInsCheck"."FacmNo" is '額度編號';
comment on column "PfInsCheck"."CreditSysNo" is '徵審系統案號(eLoan案件編號)';
comment on column "PfInsCheck"."CustId" is '借款人身份證字號';
comment on column "PfInsCheck"."ApplDate" is '借款書申請日';
comment on column "PfInsCheck"."InsDate" is '承保日';
comment on column "PfInsCheck"."InsNo" is '保單號碼';
comment on column "PfInsCheck"."CheckResult" is '檢核結果(Y/N)';
comment on column "PfInsCheck"."CheckWorkMonth" is '檢核工作月';
comment on column "PfInsCheck"."ReturnMsg" is '回應訊息1';
comment on column "PfInsCheck"."ReturnMsg2" is '回應訊息2';
comment on column "PfInsCheck"."ReturnMsg3" is '回應訊息3';
comment on column "PfInsCheck"."PerfWorkMonth" is '業績工作月';
comment on column "PfInsCheck"."CreateDate" is '建檔日期時間';
comment on column "PfInsCheck"."CreateEmpNo" is '建檔人員';
comment on column "PfInsCheck"."LastUpdate" is '最後更新日期時間';
comment on column "PfInsCheck"."LastUpdateEmpNo" is '最後更新人員';
