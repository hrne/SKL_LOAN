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

comment on table "AcAcctCheck_tmp" is '�|�p�~���ˮ���';
comment on column "AcAcctCheck_tmp"."AcDate" is '�|�p���';
comment on column "AcAcctCheck_tmp"."BranchNo" is '���O';
comment on column "AcAcctCheck_tmp"."CurrencyCode" is '���O';
comment on column "AcAcctCheck_tmp"."AcSubBookCode" is '�Ϲj�b�U';
comment on column "AcAcctCheck_tmp"."AcctCode" is '�~�Ȭ�إN��';
comment on column "AcAcctCheck_tmp"."AcctItem" is '�~�Ȭ�ئW��';
comment on column "AcAcctCheck_tmp"."TdBal" is '����l�B';
comment on column "AcAcctCheck_tmp"."TdCnt" is '������';
comment on column "AcAcctCheck_tmp"."TdNewCnt" is '����}����';
comment on column "AcAcctCheck_tmp"."TdClsCnt" is '���鵲�M���';
comment on column "AcAcctCheck_tmp"."TdExtCnt" is '����i�����';
comment on column "AcAcctCheck_tmp"."TdExtAmt" is '����i�����B';
comment on column "AcAcctCheck_tmp"."ReceivableBal" is '�P�b�ɾl�B';
comment on column "AcAcctCheck_tmp"."AcctMasterBal" is '�~���ɾl�B';
comment on column "AcAcctCheck_tmp"."YdBal" is '�e��l�B';
comment on column "AcAcctCheck_tmp"."DbAmt" is '�ɤ���B';
comment on column "AcAcctCheck_tmp"."CrAmt" is '�U����B';
comment on column "AcAcctCheck_tmp"."CoreDbAmt" is '�֤߭ɤ���B';
comment on column "AcAcctCheck_tmp"."CoreCrAmt" is '�֤߶U����B';
comment on column "AcAcctCheck_tmp"."CreateEmpNo" is '���ɤH��';
comment on column "AcAcctCheck_tmp"."CreateDate" is '���ɤ��';
comment on column "AcAcctCheck_tmp"."LastUpdateEmpNo" is '�̫���@�H��';
comment on column "AcAcctCheck_tmp"."LastUpdate" is '�̫���@���';

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