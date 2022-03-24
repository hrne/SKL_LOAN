drop table "HlAreaLnYg6Pt" purge;

create table "HlAreaLnYg6Pt" (
  "WorkYM" decimal(6, 0) default 0 not null,
  "AreaCode" varchar2(6),
  "LstAppNum" decimal(14, 2) default 0 not null,
  "LstAppAmt" decimal(14, 2) default 0 not null,
  "TisAppNum" decimal(14, 2) default 0 not null,
  "TisAppAmt" decimal(14, 2) default 0 not null,
  "CalDate" decimal(8, 0) default 0 not null,
  "UpNo" decimal(7, 0) default 0 not null,
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table "HlAreaLnYg6Pt" add constraint "HlAreaLnYg6Pt_PK" primary key("WorkYM", "AreaCode");

create index "HlAreaLnYg6Pt_Index1" on "HlAreaLnYg6Pt"("CalDate" asc);

comment on table "HlAreaLnYg6Pt" is '區域中心房貸專員業績統計';
comment on column "HlAreaLnYg6Pt"."WorkYM" is '年月份';
comment on column "HlAreaLnYg6Pt"."AreaCode" is '單位代號';
comment on column "HlAreaLnYg6Pt"."LstAppNum" is '上月達成件數';
comment on column "HlAreaLnYg6Pt"."LstAppAmt" is '上月達成金額';
comment on column "HlAreaLnYg6Pt"."TisAppNum" is '本月達成件數';
comment on column "HlAreaLnYg6Pt"."TisAppAmt" is '本月達成金額';
comment on column "HlAreaLnYg6Pt"."CalDate" is '年月日';
comment on column "HlAreaLnYg6Pt"."UpNo" is 'UpdateIdentifier';
comment on column "HlAreaLnYg6Pt"."CreateDate" is '建檔日期時間';
comment on column "HlAreaLnYg6Pt"."CreateEmpNo" is '建檔人員';
comment on column "HlAreaLnYg6Pt"."LastUpdate" is '最後更新日期時間';
comment on column "HlAreaLnYg6Pt"."LastUpdateEmpNo" is '最後更新人員';
