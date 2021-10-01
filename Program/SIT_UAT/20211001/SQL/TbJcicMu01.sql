drop table "TbJcicMu01" purge;

create table "TbJcicMu01" (
  "HeadOfficeCode" varchar2(3),
  "BranchCode" varchar2(4),
  "DataDate" decimal(8, 0) default 0 not null,
  "EmpId" varchar2(6),
  "Title" varchar2(50),
  "AuthQryType" varchar2(1),
  "QryUserId" varchar2(8),
  "AuthItemQuery" varchar2(1),
  "AuthItemReview" varchar2(1),
  "AuthItemOther" varchar2(1),
  "AuthStartDay" decimal(8, 0) default 0 not null,
  "AuthMgrIdS" varchar2(6),
  "AuthEndDay" decimal(8, 0) default 0 not null,
  "AuthMgrIdE" varchar2(6),
  "EmailAccount" varchar2(50),
  "ModifyUserId" varchar2(10),
  "OutJcictxtDate" decimal(8, 0) default 0 not null,
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table "TbJcicMu01" add constraint "TbJcicMu01_PK" primary key("HeadOfficeCode", "BranchCode", "DataDate", "EmpId");

create index "TbJcicMu01_Index1" on "TbJcicMu01"("HeadOfficeCode" asc);

create index "TbJcicMu01_Index2" on "TbJcicMu01"("BranchCode" asc);

create index "TbJcicMu01_Index3" on "TbJcicMu01"("DataDate" asc);

create index "TbJcicMu01_Index4" on "TbJcicMu01"("EmpId" asc);

comment on table "TbJcicMu01" is '聯徵人員名冊';
comment on column "TbJcicMu01"."HeadOfficeCode" is '總行代號';
comment on column "TbJcicMu01"."BranchCode" is '分行代號';
comment on column "TbJcicMu01"."DataDate" is '資料日期';
comment on column "TbJcicMu01"."EmpId" is '員工代號';
comment on column "TbJcicMu01"."Title" is '職稱';
comment on column "TbJcicMu01"."AuthQryType" is '授權查詢方式';
comment on column "TbJcicMu01"."QryUserId" is '使用者代碼';
comment on column "TbJcicMu01"."AuthItemQuery" is '授權辦理事項-查詢';
comment on column "TbJcicMu01"."AuthItemReview" is '授權辦理事項-覆核';
comment on column "TbJcicMu01"."AuthItemOther" is '授權辦理事項-其他';
comment on column "TbJcicMu01"."AuthStartDay" is '授權起日';
comment on column "TbJcicMu01"."AuthMgrIdS" is '起日授權主管員工代號';
comment on column "TbJcicMu01"."AuthEndDay" is '授權迄日';
comment on column "TbJcicMu01"."AuthMgrIdE" is '迄日授權主管員工代號';
comment on column "TbJcicMu01"."EmailAccount" is 'E-mail信箱';
comment on column "TbJcicMu01"."ModifyUserId" is '異動人員ID';
comment on column "TbJcicMu01"."OutJcictxtDate" is '轉出JCIC文字檔日期';
comment on column "TbJcicMu01"."CreateDate" is '建檔日期時間';
comment on column "TbJcicMu01"."CreateEmpNo" is '建檔人員';
comment on column "TbJcicMu01"."LastUpdate" is '最後更新日期時間';
comment on column "TbJcicMu01"."LastUpdateEmpNo" is '最後更新人員';
