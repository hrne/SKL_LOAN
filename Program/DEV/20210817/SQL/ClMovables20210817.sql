drop table "ClMovables" purge;

create table "ClMovables" (
  "ClCode1" decimal(1, 0) default 0 not null,
  "ClCode2" decimal(2, 0) default 0 not null,
  "ClNo" decimal(7, 0) default 0 not null,
  "OwnerCustUKey" varchar2(32),
  "ServiceLife" decimal(2, 0) default 0 not null,
  "ProductSpec" varchar2(20),
  "ProductType" varchar2(10),
  "ProductBrand" varchar2(20),
  "ProductCC" varchar2(10),
  "ProductColor" varchar2(10),
  "EngineSN" varchar2(50),
  "LicenseNo" varchar2(10),
  "LicenseTypeCode" varchar2(1),
  "LicenseUsageCode" varchar2(1),
  "LiceneIssueDate" decimal(8, 0) default 0 not null,
  "MfgYearMonth" decimal(6, 0) default 0 not null,
  "VehicleTypeCode" varchar2(2),
  "VehicleStyleCode" varchar2(2),
  "VehicleOfficeCode" varchar2(3),
  "Currency" varchar2(3),
  "ExchangeRate" decimal(8, 5) default 0 not null,
  "Insurance" varchar2(1),
  "LoanToValue" decimal(5, 2) default 0 not null,
  "ScrapValue" decimal(16, 2) default 0 not null,
  "MtgCode" varchar2(1),
  "MtgCheck" varchar2(1),
  "MtgLoan" varchar2(1),
  "MtgPledge" varchar2(1),
  "SettingStat" varchar2(1),
  "ClStat" varchar2(1),
  "SettingDate" decimal(8, 0) default 0 not null,
  "SettingAmt" decimal(16, 2) default 0 not null,
  "ReceiptNo" nvarchar2(20),
  "MtgNo" nvarchar2(20),
  "ReceivedDate" decimal(8, 0) default 0 not null,
  "MortgageIssueStartDate" decimal(8, 0) default 0 not null,
  "MortgageIssueEndDate" decimal(8, 0) default 0 not null,
  "Remark" nvarchar2(120),
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table "ClMovables" add constraint "ClMovables_PK" primary key("ClCode1", "ClCode2", "ClNo");

alter table "ClMovables" add constraint "ClMovables_ClMain_FK1" foreign key ("ClCode1", "ClCode2", "ClNo") references "ClMain" ("ClCode1", "ClCode2", "ClNo") on delete cascade;

create index "ClMovables_Index1" on "ClMovables"("ClCode1" asc);

create index "ClMovables_Index2" on "ClMovables"("ClCode1" asc, "ClCode2" asc);

create index "ClMovables_Index3" on "ClMovables"("OwnerCustUKey" asc);

comment on table "ClMovables" is '擔保品動產檔';
comment on column "ClMovables"."ClCode1" is '擔保品代號1';
comment on column "ClMovables"."ClCode2" is '擔保品代號2';
comment on column "ClMovables"."ClNo" is '擔保品編號';
comment on column "ClMovables"."OwnerCustUKey" is '客戶識別碼';
comment on column "ClMovables"."ServiceLife" is '耐用年限';
comment on column "ClMovables"."ProductSpec" is '形式/規格';
comment on column "ClMovables"."ProductType" is '產品代碼/型號';
comment on column "ClMovables"."ProductBrand" is '品牌/廠牌/船名';
comment on column "ClMovables"."ProductCC" is '排氣量';
comment on column "ClMovables"."ProductColor" is '顏色';
comment on column "ClMovables"."EngineSN" is '引擎號碼';
comment on column "ClMovables"."LicenseNo" is '牌照號碼';
comment on column "ClMovables"."LicenseTypeCode" is '牌照類別';
comment on column "ClMovables"."LicenseUsageCode" is '牌照用途';
comment on column "ClMovables"."LiceneIssueDate" is '發照日期';
comment on column "ClMovables"."MfgYearMonth" is '製造年月';
comment on column "ClMovables"."VehicleTypeCode" is '車別';
comment on column "ClMovables"."VehicleStyleCode" is '車身樣式';
comment on column "ClMovables"."VehicleOfficeCode" is '監理站';
comment on column "ClMovables"."Currency" is '幣別';
comment on column "ClMovables"."ExchangeRate" is '匯率';
comment on column "ClMovables"."Insurance" is '投保註記';
comment on column "ClMovables"."LoanToValue" is '貸放成數(%)';
comment on column "ClMovables"."ScrapValue" is '殘值';
comment on column "ClMovables"."MtgCode" is '抵押權註記';
comment on column "ClMovables"."MtgCheck" is '最高限額抵押權之擔保債權種類-票據';
comment on column "ClMovables"."MtgLoan" is '最高限額抵押權之擔保債權種類-借款';
comment on column "ClMovables"."MtgPledge" is '最高限額抵押權之擔保債權種類-保證債務';
comment on column "ClMovables"."SettingStat" is '設定狀態';
comment on column "ClMovables"."ClStat" is '擔保品狀態';
comment on column "ClMovables"."SettingDate" is '設定日期';
comment on column "ClMovables"."SettingAmt" is '抵押設定金額';
comment on column "ClMovables"."ReceiptNo" is '收件字號';
comment on column "ClMovables"."MtgNo" is '抵押登記字號';
comment on column "ClMovables"."ReceivedDate" is '抵押收件日';
comment on column "ClMovables"."MortgageIssueStartDate" is '抵押登記起日';
comment on column "ClMovables"."MortgageIssueEndDate" is '抵押登記迄日';
comment on column "ClMovables"."Remark" is '備註';
comment on column "ClMovables"."CreateDate" is '建檔日期時間';
comment on column "ClMovables"."CreateEmpNo" is '建檔人員';
comment on column "ClMovables"."LastUpdate" is '最後更新日期時間';
comment on column "ClMovables"."LastUpdateEmpNo" is '最後更新人員';
