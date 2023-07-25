alter table "CdCashFlow" drop constraint "CdCashFlow_PK" ;
alter table "CdCashFlow" add "BranchNo" varchar2(4);
update "CdCashFlow" set "BranchNo" = '0000';
alter table "CdCashFlow" modify "BranchNo" varchar2(4) not null;
alter table "CdCashFlow" add "TenDayPeriods" decimal(1, 0) default 0 not null;
alter table "CdCashFlow" add constraint "CdCashFlow_PK" primary key("BranchNo", "DataYearMonth", "TenDayPeriods");
comment on column "CdCashFlow"."BranchNo" is '單位別';
comment on column "CdCashFlow"."TenDayPeriods" is '旬別';