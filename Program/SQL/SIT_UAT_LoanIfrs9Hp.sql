drop table "LoanIfrs9Hp" purge;

create table "LoanIfrs9Hp" (
  "DataYM" decimal(6, 0) default 0 not null,
  "CustNo" decimal(7, 0) default 0 not null,
  "CustId" varchar2(10),
  "FacmNo" decimal(3, 0) default 0 not null,
  "ApplNo" decimal(7, 0) default 0 not null,
  "CustKind" decimal(1, 0) default 0 not null,
  "ApproveDate" decimal(8, 0) default 0 not null,
  "FirstDrawdownDate" decimal(8, 0) default 0 not null,
  "LineAmt" decimal(16, 2) default 0 not null,
  "Ifrs9ProdCode" varchar2(2),
  "AvblBal" decimal(16, 2) default 0 not null,
  "RecycleCode" varchar2(1),
  "IrrevocableFlag" varchar2(1),
  "IndustryCode" varchar2(10),
  "OriRating" varchar2(1),
  "OriModel" varchar2(1),
  "Rating" varchar2(1),
  "Model" varchar2(1),
  "LGDModel" decimal(2, 0) default 0 not null,
  "LGD" decimal(10, 8) default 0 not null,
  "LineAmtCurr" decimal(16, 2) default 0 not null,
  "AvblBalCurr" decimal(16, 2) default 0 not null,
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table "LoanIfrs9Hp" add constraint "LoanIfrs9Hp_PK" primary key("DataYM", "CustNo", "FacmNo");

comment on table "LoanIfrs9Hp" is 'IFRS9欄位清單8';
comment on column "LoanIfrs9Hp"."DataYM" is '年月份';
comment on column "LoanIfrs9Hp"."CustNo" is '戶號';
comment on column "LoanIfrs9Hp"."CustId" is '借款人ID / 統編';
comment on column "LoanIfrs9Hp"."FacmNo" is '額度編號';
comment on column "LoanIfrs9Hp"."ApplNo" is '核准號碼';
comment on column "LoanIfrs9Hp"."CustKind" is '企業戶/個人戶';
comment on column "LoanIfrs9Hp"."ApproveDate" is '核准日期';
comment on column "LoanIfrs9Hp"."FirstDrawdownDate" is '初貸日期';
comment on column "LoanIfrs9Hp"."LineAmt" is '核准金額(台幣)';
comment on column "LoanIfrs9Hp"."Ifrs9ProdCode" is '產品別';
comment on column "LoanIfrs9Hp"."AvblBal" is '可動用餘額(台幣)';
comment on column "LoanIfrs9Hp"."RecycleCode" is '該筆額度是否可循環動用';
comment on column "LoanIfrs9Hp"."IrrevocableFlag" is '該筆額度是否為不可撤銷';
comment on column "LoanIfrs9Hp"."IndustryCode" is '主計處行業別代碼';
comment on column "LoanIfrs9Hp"."OriRating" is '原始認列時時信用評等';
comment on column "LoanIfrs9Hp"."OriModel" is '原始認列時信用評等模型';
comment on column "LoanIfrs9Hp"."Rating" is '財務報導日時信用評等';
comment on column "LoanIfrs9Hp"."Model" is '財務報導日時信用評等模型';
comment on column "LoanIfrs9Hp"."LGDModel" is '違約損失率模型';
comment on column "LoanIfrs9Hp"."LGD" is '違約損失率';
comment on column "LoanIfrs9Hp"."LineAmtCurr" is '核准金額(交易幣)';
comment on column "LoanIfrs9Hp"."AvblBalCurr" is '可動用餘額(交易幣)';
comment on column "LoanIfrs9Hp"."CreateDate" is '建檔日期時間';
comment on column "LoanIfrs9Hp"."CreateEmpNo" is '建檔人員';
comment on column "LoanIfrs9Hp"."LastUpdate" is '最後更新日期時間';
comment on column "LoanIfrs9Hp"."LastUpdateEmpNo" is '最後更新人員';
