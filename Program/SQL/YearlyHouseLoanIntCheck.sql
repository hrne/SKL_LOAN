drop table "YearlyHouseLoanIntCheck" purge;

create table "YearlyHouseLoanIntCheck" (
  "YearMonth" decimal(6, 0) default 0 not null,
  "CustNo" decimal(7, 0) default 0 not null,
  "FacmNo" decimal(3, 0) default 0 not null,
  "UsageCode" varchar2(2),
  "R1" varchar2(1),
  "R2" varchar2(1),
  "R3" varchar2(1),
  "R4" varchar2(1),
  "R5" varchar2(1),
  "R6" varchar2(1),
  "R7" varchar2(1),
  "R8" varchar2(1),
  "R10" varchar2(1),
  "R11" varchar2(1),
  "R12" varchar2(1),
  "C1" varchar2(1),
  "C2" varchar2(1),
  "C3" varchar2(1),
  "C4" varchar2(1),
  "C5" varchar2(1),
  "JsonFields" nvarchar2(300),
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table "YearlyHouseLoanIntCheck" add constraint "YearlyHouseLoanIntCheck_PK" primary key("YearMonth", "CustNo", "FacmNo", "UsageCode");

comment on table "YearlyHouseLoanIntCheck" is '每年房屋擔保借款繳息檢核檔';
comment on column "YearlyHouseLoanIntCheck"."YearMonth" is '資料年月';
comment on column "YearlyHouseLoanIntCheck"."CustNo" is '戶號';
comment on column "YearlyHouseLoanIntCheck"."FacmNo" is '額度編號';
comment on column "YearlyHouseLoanIntCheck"."UsageCode" is '資金用途別';
comment on column "YearlyHouseLoanIntCheck"."R1" is '借戶姓名空白';
comment on column "YearlyHouseLoanIntCheck"."R2" is '統一編號空白';
comment on column "YearlyHouseLoanIntCheck"."R3" is '貸款帳號空白';
comment on column "YearlyHouseLoanIntCheck"."R4" is '最初貸款金額為0';
comment on column "YearlyHouseLoanIntCheck"."R5" is '最初貸款金額大於核准額度';
comment on column "YearlyHouseLoanIntCheck"."R6" is '最初貸款金額小於放款餘額';
comment on column "YearlyHouseLoanIntCheck"."R7" is '貸款起日空白';
comment on column "YearlyHouseLoanIntCheck"."R8" is '貸款迄日空白';
comment on column "YearlyHouseLoanIntCheck"."R10" is '繳息所屬年月空白';
comment on column "YearlyHouseLoanIntCheck"."R11" is '繳息金額為0';
comment on column "YearlyHouseLoanIntCheck"."R12" is '科子細目代號暨說明空白';
comment on column "YearlyHouseLoanIntCheck"."C1" is '一額度一撥款';
comment on column "YearlyHouseLoanIntCheck"."C2" is '一額度多撥款';
comment on column "YearlyHouseLoanIntCheck"."C3" is '多額度多撥款';
comment on column "YearlyHouseLoanIntCheck"."C4" is '借新還舊件';
comment on column "YearlyHouseLoanIntCheck"."C5" is '清償件';
comment on column "YearlyHouseLoanIntCheck"."JsonFields" is 'jason格式紀錄欄';
comment on column "YearlyHouseLoanIntCheck"."CreateDate" is '建檔日期時間';
comment on column "YearlyHouseLoanIntCheck"."CreateEmpNo" is '建檔人員';
comment on column "YearlyHouseLoanIntCheck"."LastUpdate" is '最後更新日期時間';
comment on column "YearlyHouseLoanIntCheck"."LastUpdateEmpNo" is '最後更新人員';
