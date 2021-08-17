drop table "ClStock" purge;

create table "ClStock" (
  "ClCode1" decimal(1, 0) default 0 not null,
  "ClCode2" decimal(2, 0) default 0 not null,
  "ClNo" decimal(7, 0) default 0 not null,
  "StockCode" varchar2(10),
  "ListingType" varchar2(2),
  "StockType" varchar2(1),
  "CompanyId" varchar2(10),
  "DataYear" decimal(4, 0) default 0 not null,
  "IssuedShares" decimal(16, 2) default 0 not null,
  "NetWorth" decimal(16, 2) default 0 not null,
  "EvaStandard" varchar2(2),
  "ParValue" decimal(16, 2) default 0 not null,
  "MonthlyAvg" decimal(16, 2) default 0 not null,
  "YdClosingPrice" decimal(16, 2) default 0 not null,
  "ThreeMonthAvg" decimal(16, 2) default 0 not null,
  "EvaUnitPrice" decimal(16, 2) default 0 not null,
  "OwnerCustUKey" varchar2(32),
  "InsiderJobTitle" varchar2(2),
  "InsiderPosition" varchar2(2),
  "LegalPersonId" varchar2(10),
  "LoanToValue" decimal(5, 2) default 0 not null,
  "ClMtr" decimal(5, 2) default 0 not null,
  "NoticeMtr" decimal(5, 2) default 0 not null,
  "ImplementMtr" decimal(5, 2) default 0 not null,
  "PledgeNo" nvarchar2(14),
  "ComputeMTR" varchar2(1),
  "SettingStat" varchar2(1),
  "ClStat" varchar2(1),
  "SettingDate" decimal(8, 0) default 0 not null,
  "SettingBalance" decimal(16, 2) default 0 not null,
  "MtgDate" decimal(8, 0) default 0 not null,
  "CustodyNo" nvarchar2(5),
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table "ClStock" add constraint "ClStock_PK" primary key("ClCode1", "ClCode2", "ClNo");

alter table "ClStock" add constraint "ClStock_ClMain_FK1" foreign key ("ClCode1", "ClCode2", "ClNo") references "ClMain" ("ClCode1", "ClCode2", "ClNo") on delete cascade;

create index "ClStock_Index1" on "ClStock"("ClCode1" asc);

create index "ClStock_Index2" on "ClStock"("ClCode1" asc, "ClCode2" asc);

create index "ClStock_Index3" on "ClStock"("OwnerCustUKey" asc);

comment on table "ClStock" is '擔保品股票檔';
comment on column "ClStock"."ClCode1" is '擔保品代號1';
comment on column "ClStock"."ClCode2" is '擔保品代號2';
comment on column "ClStock"."ClNo" is '擔保品號碼';
comment on column "ClStock"."StockCode" is '股票代號';
comment on column "ClStock"."ListingType" is '掛牌別';
comment on column "ClStock"."StockType" is '股票種類';
comment on column "ClStock"."CompanyId" is '發行公司統一編號';
comment on column "ClStock"."DataYear" is '資料年度';
comment on column "ClStock"."IssuedShares" is '發行股數';
comment on column "ClStock"."NetWorth" is '非上市(櫃)每股淨值';
comment on column "ClStock"."EvaStandard" is '每股單價鑑估標準';
comment on column "ClStock"."ParValue" is '每股面額';
comment on column "ClStock"."MonthlyAvg" is '一個月平均價';
comment on column "ClStock"."YdClosingPrice" is '前日收盤價';
comment on column "ClStock"."ThreeMonthAvg" is '三個月平均價';
comment on column "ClStock"."EvaUnitPrice" is '鑑定單價';
comment on column "ClStock"."OwnerCustUKey" is '客戶識別碼';
comment on column "ClStock"."InsiderJobTitle" is '公司內部人職稱';
comment on column "ClStock"."InsiderPosition" is '公司內部人身分註記';
comment on column "ClStock"."LegalPersonId" is '法定關係人統編';
comment on column "ClStock"."LoanToValue" is '貸放成數(%)';
comment on column "ClStock"."ClMtr" is '擔保維持率(%)';
comment on column "ClStock"."NoticeMtr" is '通知追繳維持率(%)';
comment on column "ClStock"."ImplementMtr" is '實行職權維持率(%)';
comment on column "ClStock"."PledgeNo" is '質權設定書號';
comment on column "ClStock"."ComputeMTR" is '計算維持率';
comment on column "ClStock"."SettingStat" is '設定狀態';
comment on column "ClStock"."ClStat" is '擔保品狀態';
comment on column "ClStock"."SettingDate" is '股票設解(質)日期';
comment on column "ClStock"."SettingBalance" is '設質股數餘額';
comment on column "ClStock"."MtgDate" is '擔保債權確定日期';
comment on column "ClStock"."CustodyNo" is '保管條號碼';
comment on column "ClStock"."CreateDate" is '建檔日期時間';
comment on column "ClStock"."CreateEmpNo" is '建檔人員';
comment on column "ClStock"."LastUpdate" is '最後更新日期時間';
comment on column "ClStock"."LastUpdateEmpNo" is '最後更新人員';
