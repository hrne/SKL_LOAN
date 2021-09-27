drop table "CdBranchGroup" purge;

create table "CdBranchGroup" (
  "BranchNo" varchar2(4),
  "GroupNo" varchar2(2),
  "GroupItem" nvarchar2(10),
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table "CdBranchGroup" add constraint "CdBranchGroup_PK" primary key("BranchNo", "GroupNo");

comment on table "CdBranchGroup" is '營業單位課組別檔';
comment on column "CdBranchGroup"."BranchNo" is '單位別';
comment on column "CdBranchGroup"."GroupNo" is '課組別代號';
comment on column "CdBranchGroup"."GroupItem" is '課組別說明';
comment on column "CdBranchGroup"."CreateDate" is '建檔日期時間';
comment on column "CdBranchGroup"."CreateEmpNo" is '建檔人員';
comment on column "CdBranchGroup"."LastUpdate" is '最後更新日期時間';
comment on column "CdBranchGroup"."LastUpdateEmpNo" is '最後更新人員';
