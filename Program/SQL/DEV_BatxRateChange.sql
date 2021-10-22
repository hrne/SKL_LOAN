drop table "BatxRateChange" purge;

create table "BatxRateChange" (
  "AdjDate" decimal(8, 0) default 0 not null,
  "CustNo" decimal(7, 0) default 0 not null,
  "FacmNo" decimal(3, 0) default 0 not null,
  "BormNo" decimal(3, 0) default 0 not null,
  "TxKind" decimal(1, 0) default 0 not null,
  "DrawdownAmt" decimal(14, 0) default 0 not null,
  "CityCode" varchar2(2),
  "AreaCode" varchar2(3),
  "IncrFlag" varchar2(1),
  "AdjCode" decimal(1, 0) default 0 not null,
  "RateKeyInCode" decimal(1, 0) default 0 not null,
  "ConfirmFlag" decimal(1, 0) default 0 not null,
  "TotBalance" decimal(14, 0) default 0 not null,
  "LoanBalance" decimal(14, 0) default 0 not null,
  "PresEffDate" decimal(8, 0) default 0 not null,
  "CurtEffDate" decimal(8, 0) default 0 not null,
  "PreNextAdjDate" decimal(8, 0) default 0 not null,
  "PreNextAdjFreq" decimal(2, 0) default 0 not null,
  "PrevIntDate" decimal(8, 0) default 0 not null,
  "CustCode" decimal(1, 0) default 0 not null,
  "ProdNo" varchar2(5),
  "RateIncr" decimal(6, 4) default 0 not null,
  "ContractRate" decimal(6, 4) default 0 not null,
  "PresentRate" decimal(6, 4) default 0 not null,
  "ProposalRate" decimal(6, 4) default 0 not null,
  "AdjustedRate" decimal(6, 4) default 0 not null,
  "ContrBaseRate" decimal(6, 4) default 0 not null,
  "ContrRateIncr" decimal(6, 4) default 0 not null,
  "IndividualIncr" decimal(6, 4) default 0 not null,
  "BaseRateCode" varchar2(2),
  "RateCode" varchar2(1),
  "CurrBaseRate" decimal(6, 4) default 0 not null,
  "TxEffectDate" decimal(8, 0) default 0 not null,
  "TxRateAdjFreq" decimal(2, 0) default 0 not null,
  "JsonFields" nvarchar2(300),
  "OvduTerm" decimal(3, 0) default 0 not null,
  "TitaTlrNo" varchar2(6),
  "TitaTxtNo" varchar2(8),
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table "BatxRateChange" add constraint "BatxRateChange_PK" primary key("AdjDate", "CustNo", "FacmNo", "BormNo");

comment on table "BatxRateChange" is '整批利率調整檔';
comment on column "BatxRateChange"."AdjDate" is '調整日期';
comment on column "BatxRateChange"."CustNo" is '戶號';
comment on column "BatxRateChange"."FacmNo" is '額度';
comment on column "BatxRateChange"."BormNo" is '撥款序號';
comment on column "BatxRateChange"."TxKind" is '作業項目';
comment on column "BatxRateChange"."DrawdownAmt" is '撥款金額';
comment on column "BatxRateChange"."CityCode" is '地區別';
comment on column "BatxRateChange"."AreaCode" is '鄉鎮區';
comment on column "BatxRateChange"."IncrFlag" is '加減碼是否依合約';
comment on column "BatxRateChange"."AdjCode" is '調整記號';
comment on column "BatxRateChange"."RateKeyInCode" is '是否輸入利率';
comment on column "BatxRateChange"."ConfirmFlag" is '確認記號';
comment on column "BatxRateChange"."TotBalance" is '全戶餘額';
comment on column "BatxRateChange"."LoanBalance" is '放款餘額';
comment on column "BatxRateChange"."PresEffDate" is '目前生效日';
comment on column "BatxRateChange"."CurtEffDate" is '本次生效日';
comment on column "BatxRateChange"."PreNextAdjDate" is '調整前下次利率調整日';
comment on column "BatxRateChange"."PreNextAdjFreq" is '調整前下次利率調整週期';
comment on column "BatxRateChange"."PrevIntDate" is '繳息迄日';
comment on column "BatxRateChange"."CustCode" is '戶別';
comment on column "BatxRateChange"."ProdNo" is '商品代碼';
comment on column "BatxRateChange"."RateIncr" is '利率加減碼';
comment on column "BatxRateChange"."ContractRate" is '合約利率';
comment on column "BatxRateChange"."PresentRate" is '目前利率';
comment on column "BatxRateChange"."ProposalRate" is '擬調利率';
comment on column "BatxRateChange"."AdjustedRate" is '調整後利率';
comment on column "BatxRateChange"."ContrBaseRate" is '合約指標利率';
comment on column "BatxRateChange"."ContrRateIncr" is '合約加減碼';
comment on column "BatxRateChange"."IndividualIncr" is '個別加減碼';
comment on column "BatxRateChange"."BaseRateCode" is '指標利率代碼';
comment on column "BatxRateChange"."RateCode" is '利率區分';
comment on column "BatxRateChange"."CurrBaseRate" is '本次指標利率';
comment on column "BatxRateChange"."TxEffectDate" is '調整時鍵入利率生效日';
comment on column "BatxRateChange"."TxRateAdjFreq" is '產檔時鍵入預調利率週期';
comment on column "BatxRateChange"."JsonFields" is 'jason格式紀錄欄';
comment on column "BatxRateChange"."OvduTerm" is '逾期期數';
comment on column "BatxRateChange"."TitaTlrNo" is '經辦';
comment on column "BatxRateChange"."TitaTxtNo" is '交易序號';
comment on column "BatxRateChange"."CreateDate" is '建檔日期時間';
comment on column "BatxRateChange"."CreateEmpNo" is '建檔人員';
comment on column "BatxRateChange"."LastUpdate" is '最後更新日期時間';
comment on column "BatxRateChange"."LastUpdateEmpNo" is '最後更新人員';
