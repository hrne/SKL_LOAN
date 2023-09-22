drop table "L7206Cust" purge;

drop sequence "L7206Cust_SEQ";

create table "L7206Cust" (
  "LogNo" decimal(11,0) not null,
  "CustId" varchar2(10),
  "CustName" nvarchar2(42),
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

alter table "L7206Cust" add constraint "L7206Cust_PK" primary key("LogNo");

create sequence "L7206Cust_SEQ" minvalue 1 maxvalue 2147483647 increment by 1 start with 1 nocache cycle;

comment on table "L7206Cust" is '利害關係人借款人檔';
comment on column "L7206Cust"."LogNo" is '序號';
comment on column "L7206Cust"."CustId" is '身分證/統一編號';
comment on column "L7206Cust"."CustName" is '姓名';
comment on column "L7206Cust"."CustNo" is '戶號';
comment on column "L7206Cust"."FacmNo" is '額度號碼';
comment on column "L7206Cust"."DataMonth" is '資料年月';
comment on column "L7206Cust"."AvgLineAmt" is '平均核准額度';
comment on column "L7206Cust"."SumLoanBal" is '合計放款餘額';
comment on column "L7206Cust"."CreateDate" is '建檔日期時間';
comment on column "L7206Cust"."CreateEmpNo" is '建檔人員';
comment on column "L7206Cust"."LastUpdate" is '最後更新日期時間';
comment on column "L7206Cust"."LastUpdateEmpNo" is '最後更新人員';
