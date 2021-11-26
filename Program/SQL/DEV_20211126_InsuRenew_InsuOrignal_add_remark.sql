ALTER TABLE "InsuOrignal" ADD "Remark" varchar2(50);
comment on column "InsuOrignal"."Remark" is '備註';
ALTER TABLE "InsuRenew" ADD "Remark" varchar2(50);
comment on column "InsuRenew"."Remark" is '備註';
ALTER TABLE "ClStock" ADD "AcMtr" decimal(5, 2) default 0 not null;
comment on column "ClStock"."AcMtr" is '全戶維持率(%)';
