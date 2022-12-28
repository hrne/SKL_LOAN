drop table "AcAcctCheck_tmp_tmp" purge;

create table "AcAcctCheck_tmp" (
  "AcDate" decimal(8, 0) default 0 not null,
  "BranchNo" varchar2(4),
  "CurrencyCode" varchar2(3),
  "AcSubBookCode" varchar2(3),
  "AcctCode" varchar2(3),
  "AcctItem" nvarchar2(20),
  "TdBal" decimal(18, 2) default 0 not null,
  "TdCnt" decimal(8, 0) default 0 not null,
  "TdNewCnt" decimal(8, 0) default 0 not null,
  "TdClsCnt" decimal(8, 0) default 0 not null,
  "TdExtCnt" decimal(8, 0) default 0 not null,
  "TdExtAmt" decimal(18, 2) default 0 not null,
  "ReceivableBal" decimal(18, 2) default 0 not null,
  "AcctMasterBal" decimal(18, 2) default 0 not null,
  "YdBal" decimal(16, 2) default 0 not null,
  "DbAmt" decimal(16, 2) default 0 not null,
  "CrAmt" decimal(16, 2) default 0 not null,
  "CoreDbAmt" decimal(16, 2) default 0 not null,
  "CoreCrAmt" decimal(16, 2) default 0 not null,
  "CreateEmpNo" varchar2(6),
  "CreateDate" timestamp,
  "LastUpdateEmpNo" varchar2(6),
  "LastUpdate" timestamp
);

alter table "AcAcctCheck_tmp" add constraint "AcAcctCheck_tmp_PK" primary key("AcDate", "BranchNo", "CurrencyCode", "AcctCode", "AcSubBookCode");

comment on table "AcAcctCheck_tmp" is '會計業務檢核檔';
comment on column "AcAcctCheck_tmp"."AcDate" is '會計日期';
comment on column "AcAcctCheck_tmp"."BranchNo" is '單位別';
comment on column "AcAcctCheck_tmp"."CurrencyCode" is '幣別';
comment on column "AcAcctCheck_tmp"."AcSubBookCode" is '區隔帳冊';
comment on column "AcAcctCheck_tmp"."AcctCode" is '業務科目代號';
comment on column "AcAcctCheck_tmp"."AcctItem" is '業務科目名稱';
comment on column "AcAcctCheck_tmp"."TdBal" is '本日餘額';
comment on column "AcAcctCheck_tmp"."TdCnt" is '本日件數';
comment on column "AcAcctCheck_tmp"."TdNewCnt" is '本日開戶件數';
comment on column "AcAcctCheck_tmp"."TdClsCnt" is '本日結清件數';
comment on column "AcAcctCheck_tmp"."TdExtCnt" is '本日展期件數';
comment on column "AcAcctCheck_tmp"."TdExtAmt" is '本日展期金額';
comment on column "AcAcctCheck_tmp"."ReceivableBal" is '銷帳檔餘額';
comment on column "AcAcctCheck_tmp"."AcctMasterBal" is '業務檔餘額';
comment on column "AcAcctCheck_tmp"."YdBal" is '前日餘額';
comment on column "AcAcctCheck_tmp"."DbAmt" is '借方金額';
comment on column "AcAcctCheck_tmp"."CrAmt" is '貸方金額';
comment on column "AcAcctCheck_tmp"."CoreDbAmt" is '核心借方金額';
comment on column "AcAcctCheck_tmp"."CoreCrAmt" is '核心貸方金額';
comment on column "AcAcctCheck_tmp"."CreateEmpNo" is '建檔人員';
comment on column "AcAcctCheck_tmp"."CreateDate" is '建檔日期';
comment on column "AcAcctCheck_tmp"."LastUpdateEmpNo" is '最後維護人員';
comment on column "AcAcctCheck_tmp"."LastUpdate" is '最後維護日期';

Insert into "AcAcctCheck_tmp"
select 
"AcDate"
, "BranchNo"
, "CurrencyCode"
, "AcSubBookCode"
, "AcctCode"
, "AcctItem"
, "TdBal"
, "TdCnt"
, "TdNewCnt"
, "TdClsCnt"
, "TdExtCnt"
, "TdExtAmt"
, "ReceivableBal"
, "AcctMasterBal"
, 0 AS "YdBal"
, 0 AS "DbAmt"
, 0 AS "CrAmt"
, 0 AS "CoreDbAmt"
, 0 AS "CoreCrAmt"
, "CreateEmpNo"
, "CreateDate"
, "LastUpdateEmpNo"
, "LastUpdate"
from "AcAcctCheck";
commit;
drop table "AcAcctCheck";
ALTER TABLE "AcAcctCheck_tmp" RENAME TO  "AcAcctCheck";

--drop table "AcAcctCheck_tmp2";