drop table "ReltMain" purge;

create table "ReltMain" (
  "CaseNo" decimal(7, 0) default 0 not null,
  "CustNo" decimal(7, 0) default 0 not null,
  "ReltUKey" varchar2(32),
  "ReltCode" varchar2(2),
  "RemarkType" nvarchar2(1),
  "Reltmark" nvarchar2(100),
  "FinalFg" varchar2(1),
  "ApplDate" decimal(8, 0) default 0 not null,
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table "ReltMain" add constraint "ReltMain_PK" primary key("CaseNo", "CustNo", "ReltUKey");

create index "ReltMain_Index1" on "ReltMain"("CaseNo" asc);

create index "ReltMain_Index2" on "ReltMain"("CustNo" asc);

create index "ReltMain_Index3" on "ReltMain"("ReltUKey" asc);

comment on table "ReltMain" is '借款戶關係人/關係企業主檔';
comment on column "ReltMain"."CaseNo" is 'eLoan案件編號';
comment on column "ReltMain"."CustNo" is '借戶人戶號';
comment on column "ReltMain"."ReltUKey" is '關係人客戶識別碼';
comment on column "ReltMain"."ReltCode" is '關係';
comment on column "ReltMain"."RemarkType" is '備註類型';
comment on column "ReltMain"."Reltmark" is '備註';
comment on column "ReltMain"."FinalFg" is '最新案件記號';
comment on column "ReltMain"."ApplDate" is '申請日期';
comment on column "ReltMain"."CreateDate" is '建檔日期時間';
comment on column "ReltMain"."CreateEmpNo" is '建檔人員';
comment on column "ReltMain"."LastUpdate" is '最後更新日期時間';
comment on column "ReltMain"."LastUpdateEmpNo" is '最後更新人員';
