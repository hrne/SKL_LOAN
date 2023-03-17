drop table "ClOtherRightsFac" purge;

create table "ClOtherRightsFac" (
  "ClCode1" decimal(1, 0) default 0 not null,
  "ClCode2" decimal(2, 0) default 0 not null,
  "ClNo" decimal(7, 0) default 0 not null,
  "Seq" varchar2(8),
  "ApproveNo" decimal(7, 0) default 0 not null,
  "CustNo" decimal(7, 0) default 0 not null,
  "FacmNo" decimal(3, 0) default 0 not null,
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table "ClOtherRightsFac" add constraint "ClOtherRightsFac_PK" primary key("ClCode1", "ClCode2", "ClNo", "Seq", "CustNo", "FacmNo");

alter table "ClOtherRightsFac" add constraint "ClOtherRightsFac_ClOtherRights_FK1" foreign key ("ClCode1", "ClCode2", "ClNo", "Seq") references "ClOtherRights" ("ClCode1", "ClCode2", "ClNo", "Seq") on delete cascade;

alter table "ClOtherRightsFac" add constraint "ClOtherRightsFac_FacCaseAppl_FK2" foreign key ("ApproveNo") references "FacCaseAppl" ("ApplNo") on delete cascade;

create index "ClOtherRightsFac_Index1" on "ClOtherRightsFac"("ClCode1" asc, "ClCode2" asc, "ClNo" asc);

create index "ClOtherRightsFac_Index2" on "ClOtherRightsFac"("CustNo" asc, "FacmNo" asc);

comment on table "ClOtherRightsFac" is '擔保品他項權利額度關聯檔';
comment on column "ClOtherRightsFac"."ClCode1" is '擔保品代號1';
comment on column "ClOtherRightsFac"."ClCode2" is '擔保品代號2';
comment on column "ClOtherRightsFac"."ClNo" is '擔保品編號';
comment on column "ClOtherRightsFac"."Seq" is '他項權利登記次序';
comment on column "ClOtherRightsFac"."ApproveNo" is '核准號碼';
comment on column "ClOtherRightsFac"."CustNo" is '借款人戶號';
comment on column "ClOtherRightsFac"."FacmNo" is '額度編號';
comment on column "ClOtherRightsFac"."CreateDate" is '建檔日期時間';
comment on column "ClOtherRightsFac"."CreateEmpNo" is '建檔人員';
comment on column "ClOtherRightsFac"."LastUpdate" is '最後更新日期時間';
comment on column "ClOtherRightsFac"."LastUpdateEmpNo" is '最後更新人員';
