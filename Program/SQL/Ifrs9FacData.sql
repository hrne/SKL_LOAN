drop table "Ifrs9FacData" purge;

create table "Ifrs9FacData" (
  "DataYM" decimal(6, 0) default 0 not null,
  "CustNo" decimal(7, 0) default 0 not null,
  "FacmNo" decimal(3, 0) default 0 not null,
  "ApplNo" decimal(7, 0) default 0 not null,
  "CustId" varchar2(10),
  "DrawdownFg" decimal(1, 0) default 0 not null,
  "ApproveDate" decimal(8, 0) default 0 not null,
  "UtilDeadline" decimal(8, 0) default 0 not null,
  "FirstDrawdownDate" decimal(8, 0) default 0 not null,
  "MaturityDate" decimal(8, 0) default 0 not null,
  "LineAmt" decimal(16, 2) default 0 not null,
  "AcctFee" decimal(16, 2) default 0 not null,
  "LawFee" decimal(16, 2) default 0 not null,
  "FireFee" decimal(16, 2) default 0 not null,
  "GracePeriod" decimal(3, 0) default 0 not null,
  "AmortizedCode" varchar2(1),
  "RateCode" varchar2(1),
  "RepayFreq" decimal(2, 0) default 0 not null,
  "PayIntFreq" decimal(2, 0) default 0 not null,
  "IfrsStepProdCode" varchar2(1),
  "IndustryCode" varchar2(6),
  "ClTypeJCIC" varchar2(2),
  "CityCode" varchar2(2),
  "AreaCode" varchar2(3),
  "Zip3" varchar2(3),
  "ProdNo" varchar2(5),
  "AgreementFg" varchar2(1),
  "EntCode" varchar2(1),
  "AssetClass" decimal(1, 0) default 0 not null,
  "IfrsProdCode" varchar2(2),
  "EvaAmt" decimal(16, 2) default 0 not null,
  "UtilAmt" decimal(16, 2) default 0 not null,
  "UtilBal" decimal(16, 2) default 0 not null,
  "TotalLoanBal" decimal(16, 2) default 0 not null,
  "RecycleCode" decimal(1, 0) default 0 not null,
  "IrrevocableFlag" decimal(1, 0) default 0 not null,
  "TempAmt" decimal(16, 2) default 0 not null,
  "AcBookCode" varchar2(3),
  "AcSubBookCode" varchar2(3),
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table "Ifrs9FacData" add constraint "Ifrs9FacData_PK" primary key("DataYM", "CustNo", "FacmNo");

comment on table "Ifrs9FacData" is 'IFRS9額度資料檔';
comment on column "Ifrs9FacData"."DataYM" is '資料年月';
comment on column "Ifrs9FacData"."CustNo" is '戶號';
comment on column "Ifrs9FacData"."FacmNo" is '額度編號';
comment on column "Ifrs9FacData"."ApplNo" is '核准號碼';
comment on column "Ifrs9FacData"."CustId" is '借款人ID / 統編';
comment on column "Ifrs9FacData"."DrawdownFg" is '已核撥記號';
comment on column "Ifrs9FacData"."ApproveDate" is '核准日期(額度)';
comment on column "Ifrs9FacData"."UtilDeadline" is '動支期限';
comment on column "Ifrs9FacData"."FirstDrawdownDate" is '初貸日期';
comment on column "Ifrs9FacData"."MaturityDate" is '到期日(額度)';
comment on column "Ifrs9FacData"."LineAmt" is '核准金額';
comment on column "Ifrs9FacData"."AcctFee" is '帳管費';
comment on column "Ifrs9FacData"."LawFee" is '法務費';
comment on column "Ifrs9FacData"."FireFee" is '火險費';
comment on column "Ifrs9FacData"."GracePeriod" is '初貸時約定還本寬限期';
comment on column "Ifrs9FacData"."AmortizedCode" is '契約當時還款方式(月底日)';
comment on column "Ifrs9FacData"."RateCode" is '契約當時利率調整方式(月底日)';
comment on column "Ifrs9FacData"."RepayFreq" is '契約約定當時還本週期(月底日)';
comment on column "Ifrs9FacData"."PayIntFreq" is '契約約定當時繳息週期(月底日)';
comment on column "Ifrs9FacData"."IfrsStepProdCode" is 'IFRS階梯商品別';
comment on column "Ifrs9FacData"."IndustryCode" is '授信行業別';
comment on column "Ifrs9FacData"."ClTypeJCIC" is '擔保品類別';
comment on column "Ifrs9FacData"."CityCode" is '擔保品地區別';
comment on column "Ifrs9FacData"."AreaCode" is '擔保品鄉鎮區';
comment on column "Ifrs9FacData"."Zip3" is '擔保品郵遞區號';
comment on column "Ifrs9FacData"."ProdNo" is '商品利率代碼';
comment on column "Ifrs9FacData"."AgreementFg" is '是否為協議商品';
comment on column "Ifrs9FacData"."EntCode" is '企金別';
comment on column "Ifrs9FacData"."AssetClass" is '資產五分類代號';
comment on column "Ifrs9FacData"."IfrsProdCode" is '產品別';
comment on column "Ifrs9FacData"."EvaAmt" is '原始鑑價金額';
comment on column "Ifrs9FacData"."UtilAmt" is '累計撥款金額(額度層)';
comment on column "Ifrs9FacData"."UtilBal" is '已動用餘額(額度層)';
comment on column "Ifrs9FacData"."TotalLoanBal" is '本金餘額(額度層)合計';
comment on column "Ifrs9FacData"."RecycleCode" is '該筆額度是否可循環動用';
comment on column "Ifrs9FacData"."IrrevocableFlag" is '該筆額度是否為不可撤銷';
comment on column "Ifrs9FacData"."TempAmt" is '暫收款金額(台幣)';
comment on column "Ifrs9FacData"."AcBookCode" is '帳冊別';
comment on column "Ifrs9FacData"."AcSubBookCode" is '區隔帳冊';
comment on column "Ifrs9FacData"."CreateDate" is '建檔日期時間';
comment on column "Ifrs9FacData"."CreateEmpNo" is '建檔人員';
comment on column "Ifrs9FacData"."LastUpdate" is '最後更新日期時間';
comment on column "Ifrs9FacData"."LastUpdateEmpNo" is '最後更新人員';
