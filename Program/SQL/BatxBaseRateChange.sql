drop table "BatxBaseRateChange" purge;

create table "BatxBaseRateChange" (
  "AdjDate" decimal(8, 0) default 0 not null,
  "CustNo" decimal(7, 0) default 0 not null,
  "FacmNo" decimal(3, 0) default 0 not null,
  "BormNo" decimal(3, 0) default 0 not null,
  "ProdNo" varchar2(5),
  "BaseRateCode" varchar2(2),
  "OriBaseRate" decimal(6, 4) default 0 not null,
  "BaseRateEffectDate" decimal(8, 0) default 0 not null,
  "BaseRate" decimal(6, 4) default 0 not null,
  "FitRate" decimal(6, 4) default 0 not null,
  "TxEffectDate" decimal(8, 0) default 0 not null,
  "JsonFields" nvarchar2(2000),
  "TitaTlrNo" varchar2(6),
  "TitaTxtNo" varchar2(8),
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table "BatxBaseRateChange" add constraint "BatxBaseRateChange_PK" primary key("AdjDate", "CustNo", "FacmNo", "BormNo");

comment on table "BatxBaseRateChange" is '整批指標利率調整檔';
comment on column "BatxBaseRateChange"."AdjDate" is '調整日期';
comment on column "BatxBaseRateChange"."CustNo" is '戶號';
comment on column "BatxBaseRateChange"."FacmNo" is '額度';
comment on column "BatxBaseRateChange"."BormNo" is '撥款序號';
comment on column "BatxBaseRateChange"."ProdNo" is '商品代碼';
comment on column "BatxBaseRateChange"."BaseRateCode" is '指標利率代碼';
comment on column "BatxBaseRateChange"."OriBaseRate" is '原指標利率';
comment on column "BatxBaseRateChange"."BaseRateEffectDate" is '指標利率生效日';
comment on column "BatxBaseRateChange"."BaseRate" is '指標利率';
comment on column "BatxBaseRateChange"."FitRate" is '適用利率';
comment on column "BatxBaseRateChange"."TxEffectDate" is '放款利率變動檔生效日';
comment on column "BatxBaseRateChange"."JsonFields" is 'jason格式紀錄欄';
comment on column "BatxBaseRateChange"."TitaTlrNo" is '經辦';
comment on column "BatxBaseRateChange"."TitaTxtNo" is '交易序號';
comment on column "BatxBaseRateChange"."CreateDate" is '建檔日期時間';
comment on column "BatxBaseRateChange"."CreateEmpNo" is '建檔人員';
comment on column "BatxBaseRateChange"."LastUpdate" is '最後更新日期時間';
comment on column "BatxBaseRateChange"."LastUpdateEmpNo" is '最後更新人員';
