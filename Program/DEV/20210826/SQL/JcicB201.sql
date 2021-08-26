drop table "JcicB201" purge;

create table "JcicB201" (
  "DataYM" decimal(6, 0) default 0 not null,
  "BankItem" varchar2(3),
  "BranchItem" varchar2(4),
  "TranCode" varchar2(1),
  "SubTranCode" varchar2(1),
  "AcctNo" varchar2(50),
  "SeqNo" decimal(2, 0) default 0 not null,
  "TotalAmt" decimal(10, 0) default 0 not null,
  "CustId" varchar2(10),
  "CustIdErr" varchar2(1),
  "SuvId" varchar2(10),
  "SuvIdErr" varchar2(1),
  "OverseasId" varchar2(10),
  "IndustryCode" varchar2(6),
  "Filler12" varchar2(3),
  "AcctCode" varchar2(1),
  "SubAcctCode" varchar2(1),
  "OrigAcctCode" varchar2(1),
  "ConsumeFg" varchar2(1),
  "FinCode" varchar2(1),
  "ProjCode" varchar2(2),
  "NonCreditCode" varchar2(1),
  "UsageCode" varchar2(1),
  "ApproveRate" decimal(7, 5) default 0 not null,
  "DrawdownDate" decimal(5, 0) default 0 not null,
  "MaturityDate" decimal(5, 0) default 0 not null,
  "CurrencyCode" varchar2(3),
  "DrawdownAmt" decimal(10, 0) default 0 not null,
  "DrawdownAmtFx" decimal(10, 0) default 0 not null,
  "RecycleCode" varchar2(1),
  "IrrevocableFlag" varchar2(1),
  "FacmNo" varchar2(50),
  "UnDelayBal" decimal(10, 0) default 0 not null,
  "UnDelayBalFx" decimal(10, 0) default 0 not null,
  "DelayBal" decimal(10, 0) default 0 not null,
  "DelayBalFx" decimal(10, 0) default 0 not null,
  "DelayPeriodCode" varchar2(1),
  "RepayCode" varchar2(1),
  "PayAmt" decimal(16, 3) default 0 not null,
  "Principal" decimal(16, 3) default 0 not null,
  "Interest" decimal(16, 3) default 0 not null,
  "Fee" decimal(16, 3) default 0 not null,
  "FirstDelayCode" varchar2(3),
  "SecondDelayCode" varchar2(1),
  "BadDebtCode" varchar2(3),
  "NegStatus" varchar2(3),
  "NegCreditor" varchar2(10),
  "NegNo" varchar2(14),
  "NegTransYM" varchar2(5),
  "Filler443" varchar2(6),
  "ClType" varchar2(1),
  "ClEvaAmt" decimal(10, 0) default 0 not null,
  "ClTypeCode" varchar2(2),
  "SyndKind" varchar2(1),
  "SyndContractDate" varchar2(8),
  "SyndRatio" decimal(5, 2) default 0 not null,
  "Filler51" varchar2(2),
  "Filler52" varchar2(6),
  "PayablesFg" varchar2(1),
  "NegFg" varchar2(1),
  "Filler533" varchar2(1),
  "GuaTypeCode1" varchar2(1),
  "GuaId1" varchar2(10),
  "GuaIdErr1" varchar2(1),
  "GuaRelCode1" varchar2(2),
  "GuaTypeCode2" varchar2(1),
  "GuaId2" varchar2(10),
  "GuaIdErr2" varchar2(1),
  "GuaRelCode2" varchar2(2),
  "GuaTypeCode3" varchar2(1),
  "GuaId3" varchar2(10),
  "GuaIdErr3" varchar2(1),
  "GuaRelCode3" varchar2(2),
  "GuaTypeCode4" varchar2(1),
  "GuaId4" varchar2(10),
  "GuaIdErr4" varchar2(1),
  "GuaRelCode4" varchar2(2),
  "GuaTypeCode5" varchar2(1),
  "GuaId5" varchar2(10),
  "GuaIdErr5" varchar2(1),
  "GuaRelCode5" varchar2(2),
  "Filler741" varchar2(10),
  "Filler742" varchar2(10),
  "BadDebtDate" decimal(5, 0) default 0 not null,
  "SyndCode" varchar2(5),
  "BankruptDate" decimal(7, 0) default 0 not null,
  "BdLoanFg" varchar2(1),
  "SmallAmt" decimal(4, 0) default 0 not null,
  "ExtraAttrCode" varchar2(1),
  "ExtraStatusCode" varchar2(2),
  "Filler74A" varchar2(9),
  "JcicDataYM" decimal(5, 0) default 0 not null,
  "DataEnd" varchar2(1),
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table "JcicB201" add constraint "JcicB201_PK" primary key("DataYM", "BankItem", "BranchItem", "TranCode", "SubTranCode", "AcctNo", "SeqNo");

comment on table "JcicB201" is '聯徵授信餘額月報資料檔';
comment on column "JcicB201"."DataYM" is '資料年月';
comment on column "JcicB201"."BankItem" is '總行代號';
comment on column "JcicB201"."BranchItem" is '分行代號';
comment on column "JcicB201"."TranCode" is '交易代碼';
comment on column "JcicB201"."SubTranCode" is '帳號屬性註記';
comment on column "JcicB201"."AcctNo" is '本筆撥款帳號';
comment on column "JcicB201"."SeqNo" is '本筆撥款帳號序號';
comment on column "JcicB201"."TotalAmt" is '金額合計';
comment on column "JcicB201"."CustId" is '授信戶IDN/BAN';
comment on column "JcicB201"."CustIdErr" is '上欄IDN或BAN錯誤註記';
comment on column "JcicB201"."SuvId" is '負責人IDN/負責之事業體BAN';
comment on column "JcicB201"."SuvIdErr" is '上欄IDN或BAN錯誤註記';
comment on column "JcicB201"."OverseasId" is '外僑兼具中華民國國籍IDN';
comment on column "JcicB201"."IndustryCode" is '授信戶行業別';
comment on column "JcicB201"."Filler12" is '空白';
comment on column "JcicB201"."AcctCode" is '科目別';
comment on column "JcicB201"."SubAcctCode" is '科目別註記';
comment on column "JcicB201"."OrigAcctCode" is '轉催收款(或呆帳)前原科目別';
comment on column "JcicB201"."ConsumeFg" is '個人消費性貸款註記';
comment on column "JcicB201"."FinCode" is '融資分類';
comment on column "JcicB201"."ProjCode" is '政府專業補助貸款分類';
comment on column "JcicB201"."NonCreditCode" is '不計入授信項目';
comment on column "JcicB201"."UsageCode" is '用途別';
comment on column "JcicB201"."ApproveRate" is '本筆撥款利率';
comment on column "JcicB201"."DrawdownDate" is '本筆撥款開始年月';
comment on column "JcicB201"."MaturityDate" is '本筆撥款約定清償年月';
comment on column "JcicB201"."CurrencyCode" is '授信餘額幣別';
comment on column "JcicB201"."DrawdownAmt" is '訂約金額(台幣)';
comment on column "JcicB201"."DrawdownAmtFx" is '訂約金額(外幣)';
comment on column "JcicB201"."RecycleCode" is '循環信用註記';
comment on column "JcicB201"."IrrevocableFlag" is '額度可否撤銷';
comment on column "JcicB201"."FacmNo" is '上階共用額度控制編碼';
comment on column "JcicB201"."UnDelayBal" is '未逾期/乙類逾期/應予觀察授信餘額(台幣)';
comment on column "JcicB201"."UnDelayBalFx" is '未逾期/乙類逾期/應予觀察授信餘額(外幣)';
comment on column "JcicB201"."DelayBal" is '逾期未還餘額（台幣）';
comment on column "JcicB201"."DelayBalFx" is '逾期未還餘額（外幣）';
comment on column "JcicB201"."DelayPeriodCode" is '逾期期限';
comment on column "JcicB201"."RepayCode" is '本月還款紀錄';
comment on column "JcicB201"."PayAmt" is '本月（累計）應繳金額';
comment on column "JcicB201"."Principal" is '本月收回本金';
comment on column "JcicB201"."Interest" is '本月收取利息';
comment on column "JcicB201"."Fee" is '本月收取其他費用';
comment on column "JcicB201"."FirstDelayCode" is '甲類逾期放款分類';
comment on column "JcicB201"."SecondDelayCode" is '乙類逾期放款分類';
comment on column "JcicB201"."BadDebtCode" is '不良債權處理註記';
comment on column "JcicB201"."NegStatus" is '債權結束註記';
comment on column "JcicB201"."NegCreditor" is '債權處理後新債權人ID/債權轉讓後前手債權人ID/信保基金退理賠信用保證機構BAN';
comment on column "JcicB201"."NegNo" is '債權處理案號';
comment on column "JcicB201"."NegTransYM" is '債權轉讓年月/債權轉讓後原債權機構買回年月';
comment on column "JcicB201"."Filler443" is '空白';
comment on column "JcicB201"."ClType" is '擔保品組合型態';
comment on column "JcicB201"."ClEvaAmt" is '擔保品(合計)鑑估值';
comment on column "JcicB201"."ClTypeCode" is '擔保品類別';
comment on column "JcicB201"."SyndKind" is '國內或國際連貸';
comment on column "JcicB201"."SyndContractDate" is '聯貸合約訂定日期';
comment on column "JcicB201"."SyndRatio" is '聯貸參貸比例';
comment on column "JcicB201"."Filler51" is '空白';
comment on column "JcicB201"."Filler52" is '空白';
comment on column "JcicB201"."PayablesFg" is '代放款註記';
comment on column "JcicB201"."NegFg" is '債務協商註記';
comment on column "JcicB201"."Filler533" is '空白';
comment on column "JcicB201"."GuaTypeCode1" is '共同債務人或債務關係人身份代號1';
comment on column "JcicB201"."GuaId1" is '共同債務人或債務關係人身份統一編號1';
comment on column "JcicB201"."GuaIdErr1" is '上欄IDN或BAN錯誤註記';
comment on column "JcicB201"."GuaRelCode1" is '與主債務人關係1';
comment on column "JcicB201"."GuaTypeCode2" is '共同債務人或債務關係人身份代號2';
comment on column "JcicB201"."GuaId2" is '共同債務人或債務關係人身份統一編號2';
comment on column "JcicB201"."GuaIdErr2" is '上欄IDN或BAN錯誤註記';
comment on column "JcicB201"."GuaRelCode2" is '與主債務人關係2';
comment on column "JcicB201"."GuaTypeCode3" is '共同債務人或債務關係人身份代號3';
comment on column "JcicB201"."GuaId3" is '共同債務人或債務關係人身份統一編號3';
comment on column "JcicB201"."GuaIdErr3" is '上欄IDN或BAN錯誤註記';
comment on column "JcicB201"."GuaRelCode3" is '與主債務人關係3';
comment on column "JcicB201"."GuaTypeCode4" is '共同債務人或債務關係人身份代號4';
comment on column "JcicB201"."GuaId4" is '共同債務人或債務關係人身份統一編號4';
comment on column "JcicB201"."GuaIdErr4" is '上欄IDN或BAN錯誤註記';
comment on column "JcicB201"."GuaRelCode4" is '與主債務人關係4';
comment on column "JcicB201"."GuaTypeCode5" is '共同債務人或債務關係人身份代號5';
comment on column "JcicB201"."GuaId5" is '共同債務人或債務關係人身份統一編號5';
comment on column "JcicB201"."GuaIdErr5" is '上欄IDN或BAN錯誤註記';
comment on column "JcicB201"."GuaRelCode5" is '與主債務人關係5';
comment on column "JcicB201"."Filler741" is '空白';
comment on column "JcicB201"."Filler742" is '空白';
comment on column "JcicB201"."BadDebtDate" is '呆帳轉銷年月';
comment on column "JcicB201"."SyndCode" is '聯貸主辦(管理)行註記';
comment on column "JcicB201"."BankruptDate" is '破產宣告日(或法院裁定開始清算日)';
comment on column "JcicB201"."BdLoanFg" is '建築貸款註記';
comment on column "JcicB201"."SmallAmt" is '授信餘額列報1（千元）之原始金額（元）';
comment on column "JcicB201"."ExtraAttrCode" is '補充揭露案件註記－案件屬性';
comment on column "JcicB201"."ExtraStatusCode" is '補充揭露案件註記－案件情形';
comment on column "JcicB201"."Filler74A" is '空白';
comment on column "JcicB201"."JcicDataYM" is '資料所屬年月';
comment on column "JcicB201"."DataEnd" is '資料結束註記';
comment on column "JcicB201"."CreateDate" is '建檔日期時間';
comment on column "JcicB201"."CreateEmpNo" is '建檔人員';
comment on column "JcicB201"."LastUpdate" is '最後更新日期時間';
comment on column "JcicB201"."LastUpdateEmpNo" is '最後更新人員';
