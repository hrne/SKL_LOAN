drop table "InsuRenew" purge;

create table "InsuRenew" (
  "ClCode1" decimal(1, 0) default 0 not null,
  "ClCode2" decimal(2, 0) default 0 not null,
  "ClNo" decimal(7, 0) default 0 not null,
  "PrevInsuNo" varchar2(17),
  "EndoInsuNo" varchar2(17),
  "InsuYearMonth" decimal(6, 0) default 0 not null,
  "CustNo" decimal(7, 0) default 0 not null,
  "FacmNo" decimal(3, 0) default 0 not null,
  "NowInsuNo" varchar2(17),
  "OrigInsuNo" varchar2(17),
  "RenewCode" decimal(1, 0) default 0 not null,
  "InsuCompany" varchar2(2),
  "InsuTypeCode" varchar2(2),
  "RepayCode" decimal(1, 0) default 0 not null,
  "FireInsuCovrg" decimal(14, 0) default 0 not null,
  "EthqInsuCovrg" decimal(14, 0) default 0 not null,
  "FireInsuPrem" decimal(14, 0) default 0 not null,
  "EthqInsuPrem" decimal(14, 0) default 0 not null,
  "InsuStartDate" decimal(8, 0) default 0 not null,
  "InsuEndDate" decimal(8, 0) default 0 not null,
  "TotInsuPrem" decimal(14, 0) default 0 not null,
  "AcDate" decimal(8, 0) default 0 not null,
  "TitaTlrNo" varchar2(6),
  "TitaTxtNo" varchar2(8),
  "NotiTempFg" varchar2(1),
  "StatusCode" decimal(1, 0) default 0 not null,
  "OvduDate" decimal(8, 0) default 0 not null,
  "OvduNo" decimal(10, 0) default 0 not null,
  "CommericalFlag" varchar2(1),
  "Remark" varchar2(50),
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table "InsuRenew" add constraint "InsuRenew_PK" primary key("ClCode1", "ClCode2", "ClNo", "PrevInsuNo", "EndoInsuNo");

create index "InsuRenew_Index1" on "InsuRenew"("InsuYearMonth" asc, "RepayCode" asc, "AcDate" asc, "StatusCode" asc);

create index "InsuRenew_Index2" on "InsuRenew"("NowInsuNo" asc);

create index "InsuRenew_Index3" on "InsuRenew"("InsuCompany" asc);

create index "InsuRenew_Index4" on "InsuRenew"("InsuTypeCode" asc);

create index "InsuRenew_Index5" on "InsuRenew"("InsuEndDate" asc);

comment on table "InsuRenew" is '火險單續保檔';
comment on column "InsuRenew"."ClCode1" is '擔保品-代號1';
comment on column "InsuRenew"."ClCode2" is '擔保品-代號2';
comment on column "InsuRenew"."ClNo" is '擔保品編號';
comment on column "InsuRenew"."PrevInsuNo" is '原保單號碼';
comment on column "InsuRenew"."EndoInsuNo" is '批單號碼';
comment on column "InsuRenew"."InsuYearMonth" is '火險單年月';
comment on column "InsuRenew"."CustNo" is '借款人戶號';
comment on column "InsuRenew"."FacmNo" is '額度';
comment on column "InsuRenew"."NowInsuNo" is '保險單號碼';
comment on column "InsuRenew"."OrigInsuNo" is '原始保險單號碼';
comment on column "InsuRenew"."RenewCode" is '是否續保';
comment on column "InsuRenew"."InsuCompany" is '保險公司';
comment on column "InsuRenew"."InsuTypeCode" is '保險類別';
comment on column "InsuRenew"."RepayCode" is '繳款方式';
comment on column "InsuRenew"."FireInsuCovrg" is '火災險保險金額';
comment on column "InsuRenew"."EthqInsuCovrg" is '地震險保險金額';
comment on column "InsuRenew"."FireInsuPrem" is '火災險保費';
comment on column "InsuRenew"."EthqInsuPrem" is '地震險保費';
comment on column "InsuRenew"."InsuStartDate" is '保險起日';
comment on column "InsuRenew"."InsuEndDate" is '保險迄日';
comment on column "InsuRenew"."TotInsuPrem" is '總保費';
comment on column "InsuRenew"."AcDate" is '會計日期';
comment on column "InsuRenew"."TitaTlrNo" is '經辦';
comment on column "InsuRenew"."TitaTxtNo" is '交易序號';
comment on column "InsuRenew"."NotiTempFg" is '入通知檔';
comment on column "InsuRenew"."StatusCode" is '處理代碼';
comment on column "InsuRenew"."OvduDate" is '轉催收日';
comment on column "InsuRenew"."OvduNo" is '轉催編號';
comment on column "InsuRenew"."CommericalFlag" is '住宅險改商業險註記';
comment on column "InsuRenew"."Remark" is '備註';
comment on column "InsuRenew"."CreateDate" is '建檔日期時間';
comment on column "InsuRenew"."CreateEmpNo" is '建檔人員';
comment on column "InsuRenew"."LastUpdate" is '最後更新日期時間';
comment on column "InsuRenew"."LastUpdateEmpNo" is '最後更新人員';
