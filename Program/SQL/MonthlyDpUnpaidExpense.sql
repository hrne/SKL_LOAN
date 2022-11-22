drop table "MonthlyDpUnpaidExpense" purge;

create table "MonthlyDpUnpaidExpense" (
  "YearMonth" decimal(6, 0) default 0 not null,
  "CustNo" decimal(7, 0) default 0 not null,
  "FacmNo" decimal(3, 0) default 0 not null,
  "BormNo" decimal(3, 0) default 0 not null,
  "DataFlag" varchar2(1),
  "CustTotal" decimal(16, 2) default 0 not null,
  "FacTotal" decimal(16, 2) default 0 not null,
  "ForeclosureShare" decimal(16, 2) default 0 not null,
  "InsuShare" decimal(16, 2) default 0 not null,
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table "MonthlyDpUnpaidExpense" add constraint "MonthlyDpUnpaidExpense_PK" primary key("YearMonth", "CustNo", "FacmNo", "BormNo");

comment on table "MonthlyDpUnpaidExpense" is '每月未銷火險及法拍費明細檔';
comment on column "MonthlyDpUnpaidExpense"."YearMonth" is '資料年月';
comment on column "MonthlyDpUnpaidExpense"."CustNo" is '戶號';
comment on column "MonthlyDpUnpaidExpense"."FacmNo" is '額度號碼';
comment on column "MonthlyDpUnpaidExpense"."BormNo" is '撥款序號';
comment on column "MonthlyDpUnpaidExpense"."DataFlag" is '資料區分記號';
comment on column "MonthlyDpUnpaidExpense"."CustTotal" is '客戶加總放款餘額';
comment on column "MonthlyDpUnpaidExpense"."FacTotal" is '額度加總放款餘額';
comment on column "MonthlyDpUnpaidExpense"."ForeclosureShare" is '分擔法拍費';
comment on column "MonthlyDpUnpaidExpense"."InsuShare" is '分擔火險費';
comment on column "MonthlyDpUnpaidExpense"."CreateDate" is '建檔日期時間';
comment on column "MonthlyDpUnpaidExpense"."CreateEmpNo" is '建檔人員';
comment on column "MonthlyDpUnpaidExpense"."LastUpdate" is '最後更新日期時間';
comment on column "MonthlyDpUnpaidExpense"."LastUpdateEmpNo" is '最後更新人員';
