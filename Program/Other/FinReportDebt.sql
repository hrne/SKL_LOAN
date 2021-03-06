drop table "FinReportDebt" purge;

create table "FinReportDebt" (
  "CustUKey" varchar2(32),
  "UKey" varchar2(32),
  "StartYY" decimal(4, 0) default 0 not null,
  "StartMM" decimal(2, 0) default 0 not null,
  "EndYY" decimal(4, 0) default 0 not null,
  "EndMM" decimal(2, 0) default 0 not null,
  "AssetTotal" decimal(18, 0) default 0 not null,
  "FlowAsset" decimal(18, 0) default 0 not null,
  "Cash" decimal(18, 0) default 0 not null,
  "FinAsset" decimal(18, 0) default 0 not null,
  "ReceiveTicket" decimal(18, 0) default 0 not null,
  "ReceiveAccount" decimal(18, 0) default 0 not null,
  "ReceiveRelation" decimal(18, 0) default 0 not null,
  "OtherReceive" decimal(18, 0) default 0 not null,
  "Stock" decimal(18, 0) default 0 not null,
  "PrepayItem" decimal(18, 0) default 0 not null,
  "OtherFlowAsset" decimal(18, 0) default 0 not null,
  "AccountItem01" nvarchar2(20),
  "AccountItem02" nvarchar2(20),
  "AccountItem03" nvarchar2(20),
  "AccountValue01" decimal(18, 0) default 0 not null,
  "AccountValue02" decimal(18, 0) default 0 not null,
  "AccountValue03" decimal(18, 0) default 0 not null,
  "LongInvest" decimal(18, 0) default 0 not null,
  "FixedAsset" decimal(18, 0) default 0 not null,
  "Land" decimal(18, 0) default 0 not null,
  "HouseBuild" decimal(18, 0) default 0 not null,
  "MachineEquip" decimal(18, 0) default 0 not null,
  "OtherEquip" decimal(18, 0) default 0 not null,
  "PrepayEquip" decimal(18, 0) default 0 not null,
  "UnFinish" decimal(18, 0) default 0 not null,
  "Depreciation" decimal(18, 0) default 0 not null,
  "InvisibleAsset" decimal(18, 0) default 0 not null,
  "OtherAsset" decimal(18, 0) default 0 not null,
  "AccountItem04" nvarchar2(20),
  "AccountItem05" nvarchar2(20),
  "AccountItem06" nvarchar2(20),
  "AccountValue04" decimal(18, 0) default 0 not null,
  "AccountValue05" decimal(18, 0) default 0 not null,
  "AccountValue06" decimal(18, 0) default 0 not null,
  "DebtNetTotal" decimal(18, 0) default 0 not null,
  "FlowDebt" decimal(18, 0) default 0 not null,
  "ShortLoan" decimal(18, 0) default 0 not null,
  "PayShortTicket" decimal(18, 0) default 0 not null,
  "PayTicket" decimal(18, 0) default 0 not null,
  "PayAccount" decimal(18, 0) default 0 not null,
  "PayRelation" decimal(18, 0) default 0 not null,
  "OtherPay" decimal(18, 0) default 0 not null,
  "PreReceiveItem" decimal(18, 0) default 0 not null,
  "LongDebtOneYear" decimal(18, 0) default 0 not null,
  "Shareholder" decimal(18, 0) default 0 not null,
  "OtherFlowDebt" decimal(18, 0) default 0 not null,
  "AccountItem07" nvarchar2(20),
  "AccountItem08" nvarchar2(20),
  "AccountItem09" nvarchar2(20),
  "AccountValue07" decimal(18, 0) default 0 not null,
  "AccountValue08" decimal(18, 0) default 0 not null,
  "AccountValue09" decimal(18, 0) default 0 not null,
  "LongDebt" decimal(18, 0) default 0 not null,
  "OtherDebt" decimal(18, 0) default 0 not null,
  "DebtTotal" decimal(18, 0) default 0 not null,
  "NetValue" decimal(18, 0) default 0 not null,
  "Capital" decimal(18, 0) default 0 not null,
  "CapitalSurplus" decimal(18, 0) default 0 not null,
  "RetainProfit" decimal(18, 0) default 0 not null,
  "OtherRight" decimal(18, 0) default 0 not null,
  "TreasuryStock" decimal(18, 0) default 0 not null,
  "UnControlRight" decimal(18, 0) default 0 not null,
  "AccountItem10" nvarchar2(20),
  "AccountItem11" nvarchar2(20),
  "AccountValue10" decimal(18, 0) default 0 not null,
  "AccountValue11" decimal(18, 0) default 0 not null,
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table "FinReportDebt" add constraint "FinReportDebt_PK" primary key("CustUKey", "UKey");

comment on table "FinReportDebt" is '??????????????????.???????????????';
comment on column "FinReportDebt"."CustUKey" is '???????????????';
comment on column "FinReportDebt"."UKey" is '?????????';
comment on column "FinReportDebt"."StartYY" is '??????';
comment on column "FinReportDebt"."StartMM" is '??????_??????';
comment on column "FinReportDebt"."EndYY" is '??????_??????';
comment on column "FinReportDebt"."EndMM" is '??????_??????';
comment on column "FinReportDebt"."AssetTotal" is '????????????';
comment on column "FinReportDebt"."FlowAsset" is '????????????';
comment on column "FinReportDebt"."Cash" is '?????????????????????';
comment on column "FinReportDebt"."FinAsset" is '????????????(?????????)-??????';
comment on column "FinReportDebt"."ReceiveTicket" is '????????????(??????)';
comment on column "FinReportDebt"."ReceiveAccount" is '????????????(??????)';
comment on column "FinReportDebt"."ReceiveRelation" is '??????????????????';
comment on column "FinReportDebt"."OtherReceive" is '???????????????';
comment on column "FinReportDebt"."Stock" is '??????';
comment on column "FinReportDebt"."PrepayItem" is '????????????';
comment on column "FinReportDebt"."OtherFlowAsset" is '??????????????????';
comment on column "FinReportDebt"."AccountItem01" is '????????????_????????????01';
comment on column "FinReportDebt"."AccountItem02" is '????????????_????????????02';
comment on column "FinReportDebt"."AccountItem03" is '????????????_????????????03';
comment on column "FinReportDebt"."AccountValue01" is '????????????_???????????????01';
comment on column "FinReportDebt"."AccountValue02" is '????????????_???????????????02';
comment on column "FinReportDebt"."AccountValue03" is '????????????_???????????????03';
comment on column "FinReportDebt"."LongInvest" is '?????????????????????';
comment on column "FinReportDebt"."FixedAsset" is '????????????';
comment on column "FinReportDebt"."Land" is '??????';
comment on column "FinReportDebt"."HouseBuild" is '???????????????';
comment on column "FinReportDebt"."MachineEquip" is '????????????';
comment on column "FinReportDebt"."OtherEquip" is '??????????????????????????????';
comment on column "FinReportDebt"."PrepayEquip" is '???????????????';
comment on column "FinReportDebt"."UnFinish" is '???????????????';
comment on column "FinReportDebt"."Depreciation" is '??????????????????';
comment on column "FinReportDebt"."InvisibleAsset" is '????????????';
comment on column "FinReportDebt"."OtherAsset" is '????????????';
comment on column "FinReportDebt"."AccountItem04" is '????????????_????????????04';
comment on column "FinReportDebt"."AccountItem05" is '????????????_????????????05';
comment on column "FinReportDebt"."AccountItem06" is '????????????_????????????06';
comment on column "FinReportDebt"."AccountValue04" is '????????????_???????????????04';
comment on column "FinReportDebt"."AccountValue05" is '????????????_???????????????05';
comment on column "FinReportDebt"."AccountValue06" is '????????????_???????????????06';
comment on column "FinReportDebt"."DebtNetTotal" is '?????????????????????';
comment on column "FinReportDebt"."FlowDebt" is '????????????';
comment on column "FinReportDebt"."ShortLoan" is '????????????';
comment on column "FinReportDebt"."PayShortTicket" is '??????????????????';
comment on column "FinReportDebt"."PayTicket" is '????????????(??????)';
comment on column "FinReportDebt"."PayAccount" is '????????????(??????)';
comment on column "FinReportDebt"."PayRelation" is '??????????????????';
comment on column "FinReportDebt"."OtherPay" is '???????????????';
comment on column "FinReportDebt"."PreReceiveItem" is '????????????';
comment on column "FinReportDebt"."LongDebtOneYear" is '????????????(?????????)';
comment on column "FinReportDebt"."Shareholder" is '????????????';
comment on column "FinReportDebt"."OtherFlowDebt" is '??????????????????';
comment on column "FinReportDebt"."AccountItem07" is '???????????? _????????????07';
comment on column "FinReportDebt"."AccountItem08" is '???????????? _????????????08';
comment on column "FinReportDebt"."AccountItem09" is '???????????? _????????????09';
comment on column "FinReportDebt"."AccountValue07" is '???????????? _???????????????07';
comment on column "FinReportDebt"."AccountValue08" is '???????????? _???????????????08';
comment on column "FinReportDebt"."AccountValue09" is '???????????? _???????????????09';
comment on column "FinReportDebt"."LongDebt" is '????????????';
comment on column "FinReportDebt"."OtherDebt" is '????????????';
comment on column "FinReportDebt"."DebtTotal" is '????????????';
comment on column "FinReportDebt"."NetValue" is '??????';
comment on column "FinReportDebt"."Capital" is '??????';
comment on column "FinReportDebt"."CapitalSurplus" is '????????????';
comment on column "FinReportDebt"."RetainProfit" is '????????????';
comment on column "FinReportDebt"."OtherRight" is '????????????';
comment on column "FinReportDebt"."TreasuryStock" is '????????????';
comment on column "FinReportDebt"."UnControlRight" is '???????????????';
comment on column "FinReportDebt"."AccountItem10" is '??????_????????????10';
comment on column "FinReportDebt"."AccountItem11" is '??????_????????????11';
comment on column "FinReportDebt"."AccountValue10" is '??????_???????????????10';
comment on column "FinReportDebt"."AccountValue11" is '??????_???????????????11';
comment on column "FinReportDebt"."CreateDate" is '??????????????????';
comment on column "FinReportDebt"."CreateEmpNo" is '????????????';
comment on column "FinReportDebt"."LastUpdate" is '????????????????????????';
comment on column "FinReportDebt"."LastUpdateEmpNo" is '??????????????????';
