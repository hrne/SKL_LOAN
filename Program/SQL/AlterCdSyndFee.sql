ALTER TABLE "CdSyndFee" ADD "IsAllocation" VARCHAR2(1) NULL;
comment on column "CdSyndFee"."IsAllocation" is '是否攤提';