ALTER TABLE "PfBsDetail" ADD "AdjPerfCnt" decimal(5, 1) default 0 not null;
ALTER TABLE "PfBsDetail" ADD "AdjPerfAmt" decimal(16, 2) default 0 not null;

comment on column "PfBsDetail"."AdjPerfCnt" is '週整加減件數';
comment on column "PfBsDetail"."AdjPerfAmt" is '週整加減業績金額';
