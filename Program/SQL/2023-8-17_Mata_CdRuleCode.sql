drop table "CdRuleCode" purge;

create table "CdRuleCode" (
  "RuleCode" varchar2(4),
  "RuleCodeItem" nvarchar2(30),
  "RmkItem" nvarchar2(30),
  "RuleStDate" decimal(8, 0) default 0 not null,
  "RuleEdDate" decimal(8, 0) default 0 not null,
  "EnableMark" varchar2(1),
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table "CdRuleCode" add constraint "CdRuleCode_PK" primary key("RuleCode", "RuleStDate");

create index "CdRuleCode_Index1" on "CdRuleCode"("RuleCode" asc);

comment on table "CdRuleCode" is '管制代碼檔';
comment on column "CdRuleCode"."RuleCode" is '規定管制項目代碼';
comment on column "CdRuleCode"."RuleCodeItem" is '規定管制項目中文';
comment on column "CdRuleCode"."RmkItem" is '備註';
comment on column "CdRuleCode"."RuleStDate" is '管制生效日';
comment on column "CdRuleCode"."RuleEdDate" is '管制取消日';
comment on column "CdRuleCode"."EnableMark" is '是否啟用';
comment on column "CdRuleCode"."CreateDate" is '建檔日期時間';
comment on column "CdRuleCode"."CreateEmpNo" is '建檔人員';
comment on column "CdRuleCode"."LastUpdate" is '最後更新日期時間';
comment on column "CdRuleCode"."LastUpdateEmpNo" is '最後更新人員';
