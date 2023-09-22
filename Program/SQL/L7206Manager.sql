drop table "L7206Manager" purge;

drop sequence "L7206Manager_SEQ";

create table "L7206Manager" (
  "LogNo" decimal(11,0) not null,
  "CustId" varchar2(10),
  "CustName" nvarchar2(42),
  "ManagerId" varchar2(10),
  "CustNo" decimal(7, 0) default 0 not null,
  "FacmNo" decimal(3, 0) default 0 not null,
  "DataMonth" decimal(6, 0) default 0 not null,
  "AvgLineAmt" decimal(15, 0) default 0 not null,
  "SumLoanBal" decimal(15, 0) default 0 not null,
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table "L7206Manager" add constraint "L7206Manager_PK" primary key("LogNo");

create sequence "L7206Manager_SEQ" minvalue 1 maxvalue 2147483647 increment by 1 start with 1 nocache cycle;

comment on table "L7206Manager" is '利害關係人負責人檔';
comment on column "L7206Manager"."LogNo" is '序號';
comment on column "L7206Manager"."CustId" is '身分證/統一編號';
comment on column "L7206Manager"."CustName" is '姓名';
comment on column "L7206Manager"."ManagerId" is '負責人身分證/統一編號';
comment on column "L7206Manager"."CustNo" is '戶號';
comment on column "L7206Manager"."FacmNo" is '額度號碼';
comment on column "L7206Manager"."DataMonth" is '資料年月';
comment on column "L7206Manager"."AvgLineAmt" is '平均核准額度';
comment on column "L7206Manager"."SumLoanBal" is '合計放款餘額';
comment on column "L7206Manager"."CreateDate" is '建檔日期時間';
comment on column "L7206Manager"."CreateEmpNo" is '建檔人員';
comment on column "L7206Manager"."LastUpdate" is '最後更新日期時間';
comment on column "L7206Manager"."LastUpdateEmpNo" is '最後更新人員';
