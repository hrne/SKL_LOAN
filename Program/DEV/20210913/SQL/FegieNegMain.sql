drop table "NegMain" purge;

create table "NegMain" (
  "CustNo" decimal(7, 0) default 0 not null,
  "CaseSeq" decimal(3, 0) default 0 not null,
  "CaseKindCode" varchar2(1),
  "Status" varchar2(1),
  "CustLoanKind" varchar2(1),
  "PayerCustNo" decimal(7, 0) default 0 not null,
  "DeferYMStart" decimal(6, 0) default 0 not null,
  "DeferYMEnd" decimal(6, 0) default 0 not null,
  "ApplDate" decimal(8, 0) default 0 not null,
  "DueAmt" decimal(16, 2) default 0 not null,
  "TotalPeriod" decimal(3, 0) default 0 not null,
  "IntRate" decimal(4, 2) default 0 not null,
  "FirstDueDate" decimal(8, 0) default 0 not null,
  "LastDueDate" decimal(8, 0) default 0 not null,
  "IsMainFin" varchar2(1),
  "TotalContrAmt" decimal(16, 2) default 0 not null,
  "MainFinCode" varchar2(8),
  "PrincipalBal" decimal(16, 2) default 0 not null,
  "AccuTempAmt" decimal(16, 2) default 0 not null,
  "AccuOverAmt" decimal(16, 2) default 0 not null,
  "AccuDueAmt" decimal(16, 2) default 0 not null,
  "AccuSklShareAmt" decimal(16, 2) default 0 not null,
  "RepaidPeriod" decimal(3, 0) default 0 not null,
  "TwoStepCode" varchar2(1),
  "ChgCondDate" decimal(8, 0) default 0 not null,
  "NextPayDate" decimal(8, 0) default 0 not null,
  "PayIntDate" decimal(8, 0) default 0 not null,
  "RepayPrincipal" decimal(14, 0) default 0 not null,
  "RepayInterest" decimal(14, 0) default 0 not null,
  "StatusDate" decimal(8, 0) default 0 not null,
  "CourCode" varchar2(3),
  "ThisAcDate" decimal(8, 0) default 0 not null,
  "ThisTitaTlrNo" varchar2(6),
  "ThisTitaTxtNo" decimal(8, 0) default 0 not null,
  "LastAcDate" decimal(8, 0) default 0 not null,
  "LastTitaTlrNo" varchar2(6),
  "LastTitaTxtNo" decimal(8, 0) default 0 not null,
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table "NegMain" add constraint "NegMain_PK" primary key("CustNo", "CaseSeq");

comment on table "NegMain" is '債務協商案件主檔';
comment on column "NegMain"."CustNo" is '戶號';
comment on column "NegMain"."CaseSeq" is '案件序號';
comment on column "NegMain"."CaseKindCode" is '案件種類';
comment on column "NegMain"."Status" is '債權戶況';
comment on column "NegMain"."CustLoanKind" is '債權戶別';
comment on column "NegMain"."PayerCustNo" is '付款人戶號';
comment on column "NegMain"."DeferYMStart" is '延期繳款年月(起)';
comment on column "NegMain"."DeferYMEnd" is '延期繳款年月(訖)';
comment on column "NegMain"."ApplDate" is '協商申請日';
comment on column "NegMain"."DueAmt" is '月付金(期款)';
comment on column "NegMain"."TotalPeriod" is '期數';
comment on column "NegMain"."IntRate" is '計息條件(利率)';
comment on column "NegMain"."FirstDueDate" is '首次應繳日';
comment on column "NegMain"."LastDueDate" is '還款結束日';
comment on column "NegMain"."IsMainFin" is '是否最大債權';
comment on column "NegMain"."TotalContrAmt" is '簽約總金額';
comment on column "NegMain"."MainFinCode" is '最大債權機構';
comment on column "NegMain"."PrincipalBal" is '總本金餘額';
comment on column "NegMain"."AccuTempAmt" is '累繳金額';
comment on column "NegMain"."AccuOverAmt" is '累溢繳金額';
comment on column "NegMain"."AccuDueAmt" is '累應還金額';
comment on column "NegMain"."AccuSklShareAmt" is '累新壽分攤金額';
comment on column "NegMain"."RepaidPeriod" is '已繳期數';
comment on column "NegMain"."TwoStepCode" is '二階段註記';
comment on column "NegMain"."ChgCondDate" is '申請變更還款條件日';
comment on column "NegMain"."NextPayDate" is '下次應繳日';
comment on column "NegMain"."PayIntDate" is '繳息迄日';
comment on column "NegMain"."RepayPrincipal" is '累償還本金';
comment on column "NegMain"."RepayInterest" is '累償還利息';
comment on column "NegMain"."StatusDate" is '戶況日期';
comment on column "NegMain"."CourCode" is '受理調解機構代號';
comment on column "NegMain"."ThisAcDate" is '本次會計日期';
comment on column "NegMain"."ThisTitaTlrNo" is '本次經辦';
comment on column "NegMain"."ThisTitaTxtNo" is '本次交易序號';
comment on column "NegMain"."LastAcDate" is '上次會計日期';
comment on column "NegMain"."LastTitaTlrNo" is '上次經辦';
comment on column "NegMain"."LastTitaTxtNo" is '上次交易序號';
comment on column "NegMain"."CreateDate" is '建檔日期時間';
comment on column "NegMain"."CreateEmpNo" is '建檔人員';
comment on column "NegMain"."LastUpdate" is '最後更新日期時間';
comment on column "NegMain"."LastUpdateEmpNo" is '最後更新人員';
