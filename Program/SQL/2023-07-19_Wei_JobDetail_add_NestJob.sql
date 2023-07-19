
alter table "JobDetail" add "NestJobCode" varchar2(10);
comment on column "JobDetail"."NestJobCode" is '子批次代號';