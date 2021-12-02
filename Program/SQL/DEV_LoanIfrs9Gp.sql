drop table "LoanIfrs9Gp" purge;

create table "LoanIfrs9Gp" (
  "DataYM" decimal(6, 0) default 0 not null,
  "CustNo" decimal(7, 0) default 0 not null,
  "CustId" varchar2(10),
  "FacmNo" decimal(3, 0) default 0 not null,
  "ApplNo" decimal(7, 0) default 0 not null,
  "BormNo" decimal(3, 0) default 0 not null,
  "CustKind" decimal(1, 0) default 0 not null,
  "Status" decimal(1, 0) default 0 not null,
  "OvduDate" decimal(8, 0) default 0 not null,
  "OriRating" varchar2(1),
  "OriModel" varchar2(1),
  "Rating" varchar2(1),
  "Model" varchar2(1),
  "OvduDays" decimal(4, 0) default 0 not null,
  "Stage1" decimal(1, 0) default 0 not null,
  "Stage2" decimal(1, 0) default 0 not null,
  "Stage3" decimal(1, 0) default 0 not null,
  "Stage4" decimal(1, 0) default 0 not null,
  "Stage5" decimal(1, 0) default 0 not null,
  "PdFlagToD" varchar2(1),
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table "LoanIfrs9Gp" add constraint "LoanIfrs9Gp_PK" primary key("DataYM", "CustNo", "FacmNo", "BormNo");

comment on table "LoanIfrs9Gp" is 'IFRS9欄位清單7';
comment on column "LoanIfrs9Gp"."DataYM" is '年月份';
comment on column "LoanIfrs9Gp"."CustNo" is '戶號';
comment on column "LoanIfrs9Gp"."CustId" is '借款人ID / 統編';
comment on column "LoanIfrs9Gp"."FacmNo" is '額度編號';
comment on column "LoanIfrs9Gp"."ApplNo" is '核准號碼';
comment on column "LoanIfrs9Gp"."BormNo" is '撥款序號';
comment on column "LoanIfrs9Gp"."CustKind" is '企業戶/個人戶';
comment on column "LoanIfrs9Gp"."Status" is '戶況';
comment on column "LoanIfrs9Gp"."OvduDate" is '轉催收款日期';
comment on column "LoanIfrs9Gp"."OriRating" is '原始認列時時信用評等';
comment on column "LoanIfrs9Gp"."OriModel" is '原始認列時信用評等模型';
comment on column "LoanIfrs9Gp"."Rating" is '財務報導日時信用評等';
comment on column "LoanIfrs9Gp"."Model" is '財務報導日時信用評等模型';
comment on column "LoanIfrs9Gp"."OvduDays" is '逾期繳款天數';
comment on column "LoanIfrs9Gp"."Stage1" is '債務人屬企業戶，且其歸戶下任一債務逾期90天(含)以上';
comment on column "LoanIfrs9Gp"."Stage2" is '個人消費性放款逾期超逾90天(含)以上';
comment on column "LoanIfrs9Gp"."Stage3" is '債務人屬企業戶，且其歸戶任一債務已經本行轉催收款、或符合列報逾期放款條件、或本行對該債務讓步(如協議)';
comment on column "LoanIfrs9Gp"."Stage4" is '個人消費性放款已經本行轉催收、或符合列報逾期放款條件、或本行對該債務讓步(如協議)';
comment on column "LoanIfrs9Gp"."Stage5" is '債務人申請重組、破產或其他等程序，而進行該等程序可能使債務人免除或延遲償還債務';
comment on column "LoanIfrs9Gp"."PdFlagToD" is '內部違約機率降至D評等';
comment on column "LoanIfrs9Gp"."CreateDate" is '建檔日期時間';
comment on column "LoanIfrs9Gp"."CreateEmpNo" is '建檔人員';
comment on column "LoanIfrs9Gp"."LastUpdate" is '最後更新日期時間';
comment on column "LoanIfrs9Gp"."LastUpdateEmpNo" is '最後更新人員';
