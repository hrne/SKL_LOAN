alter table "LifeRelHead" add "AcDate" decimal(8, 0) default 0 not null;
comment on column "LifeRelHead"."AcDate" is '會計日期';

alter table "LifeRelEmp" add "AcDate" decimal(8, 0) default 0 not null;
comment on column "LifeRelEmp"."AcDate" is '會計日期';

alter table "FinHoldRel" add "AcDate" decimal(8, 0) default 0 not null;
comment on column "FinHoldRel"."AcDate" is '會計日期';
