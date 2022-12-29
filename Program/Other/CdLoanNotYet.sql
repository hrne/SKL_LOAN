drop table "CdLoanNotYet" purge;

create table "CdLoanNotYet" (
  "NotYetCode" varchar2(2),
  "NotYetItem" nvarchar2(40),
  "YetDays" decimal(3, 0) default 0 not null,
  "Enable" varchar2(1),
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table "CdLoanNotYet" add constraint "CdLoanNotYet_PK" primary key("NotYetCode");

comment on table "CdLoanNotYet" is '未齊件代碼檔';
comment on column "CdLoanNotYet"."NotYetCode" is '未齊件代碼';
comment on column "CdLoanNotYet"."NotYetItem" is '未齊件說明';
comment on column "CdLoanNotYet"."YetDays" is '齊件日期計算日';
comment on column "CdLoanNotYet"."Enable" is '啟用記號';
comment on column "CdLoanNotYet"."CreateDate" is '建檔日期時間';
comment on column "CdLoanNotYet"."CreateEmpNo" is '建檔人員';
comment on column "CdLoanNotYet"."LastUpdate" is '最後更新日期時間';
comment on column "CdLoanNotYet"."LastUpdateEmpNo" is '最後更新人員';
