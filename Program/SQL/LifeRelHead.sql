drop table "LifeRelHead" purge;

create table "LifeRelHead" (
  "RelWithCompany" varchar2(1),
  "HeadId" varchar2(10),
  "HeadName" varchar2(100),
  "HeadTitle" varchar2(50),
  "RelId" varchar2(10),
  "RelName" varchar2(50),
  "RelKinShip" varchar2(1),
  "RelTitle" varchar2(10),
  "BusId" varchar2(10),
  "BusName" varchar2(100),
  "ShareHoldingRatio" decimal(3, 0) default 0 not null,
  "BusTitle" varchar2(50),
  "LineAmt" decimal(16, 2) default 0 not null,
  "LoanBalance" decimal(16, 2) default 0 not null,
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table "LifeRelHead" add constraint "LifeRelHead_PK" primary key("HeadId", "RelId", "BusId");

comment on table "LifeRelHead" is '人壽利關人負責人檔T07、TA07
(使用報表：LM013、LM042、LM050)';
comment on column "LifeRelHead"."RelWithCompany" is '與本公司關之關係';
comment on column "LifeRelHead"."HeadId" is '負責人身分證/統一編號';
comment on column "LifeRelHead"."HeadName" is '負責人名稱';
comment on column "LifeRelHead"."HeadTitle" is '關係人職稱';
comment on column "LifeRelHead"."RelId" is '負責人關係人身分證/統一編號';
comment on column "LifeRelHead"."RelName" is '關係人親屬姓名';
comment on column "LifeRelHead"."RelKinShip" is '關係人親屬親等';
comment on column "LifeRelHead"."RelTitle" is '關係人親屬稱謂';
comment on column "LifeRelHead"."BusId" is '事業負責人身分證/統一編號';
comment on column "LifeRelHead"."BusName" is '事業名稱';
comment on column "LifeRelHead"."ShareHoldingRatio" is '事業持股比率';
comment on column "LifeRelHead"."BusTitle" is '事業擔任職務';
comment on column "LifeRelHead"."LineAmt" is '核貸金額';
comment on column "LifeRelHead"."LoanBalance" is '放款金額';
comment on column "LifeRelHead"."CreateDate" is '建檔日期時間';
comment on column "LifeRelHead"."CreateEmpNo" is '建檔人員';
comment on column "LifeRelHead"."LastUpdate" is '最後更新日期時間';
comment on column "LifeRelHead"."LastUpdateEmpNo" is '最後更新人員';
