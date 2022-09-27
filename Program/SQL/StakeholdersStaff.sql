drop table "StakeholdersStaff" purge;

create table "StakeholdersStaff" (
  "StaffId" varchar2(10),
  "StaffName" varchar2(150),
  "LoanAmount" decimal(16, 4) default 0 not null,
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table "StakeholdersStaff" add constraint "StakeholdersStaff_PK" primary key("StaffId");

comment on table "StakeholdersStaff" is '人壽利關人職員名單檔';
comment on column "StakeholdersStaff"."StaffId" is '身分證/統一編號';
comment on column "StakeholdersStaff"."StaffName" is '關係人姓名';
comment on column "StakeholdersStaff"."LoanAmount" is '放款金額';
comment on column "StakeholdersStaff"."CreateDate" is '建檔日期時間';
comment on column "StakeholdersStaff"."CreateEmpNo" is '建檔人員';
comment on column "StakeholdersStaff"."LastUpdate" is '最後更新日期時間';
comment on column "StakeholdersStaff"."LastUpdateEmpNo" is '最後更新人員';
