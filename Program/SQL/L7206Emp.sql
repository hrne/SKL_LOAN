drop table "L7206Emp" purge;

drop sequence "L7206Emp_SEQ";

create table "L7206Emp" (
  "LogNo" decimal(11,0) not null,
  "CustId" varchar2(10),
  "CustName" nvarchar2(42),
  "CustNo" decimal(7, 0) default 0 not null,
  "DataMonth" decimal(6, 0) default 0 not null,
  "SumLoanBal" decimal(15, 0) default 0 not null,
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table "L7206Emp" add constraint "L7206Emp_PK" primary key("LogNo");

create sequence "L7206Emp_SEQ" minvalue 1 maxvalue 2147483647 increment by 1 start with 1 nocache cycle;

comment on table "L7206Emp" is '利害關係人員工檔';
comment on column "L7206Emp"."LogNo" is '序號';
comment on column "L7206Emp"."CustId" is '身分證/統一編號';
comment on column "L7206Emp"."CustName" is '姓名';
comment on column "L7206Emp"."CustNo" is '戶號';
comment on column "L7206Emp"."DataMonth" is '資料年月';
comment on column "L7206Emp"."SumLoanBal" is '合計放款餘額';
comment on column "L7206Emp"."CreateDate" is '建檔日期時間';
comment on column "L7206Emp"."CreateEmpNo" is '建檔人員';
comment on column "L7206Emp"."LastUpdate" is '最後更新日期時間';
comment on column "L7206Emp"."LastUpdateEmpNo" is '最後更新人員';
