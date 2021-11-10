ALTER TABLE "PfItDetail" ADD "AdjRange" decimal(1, 0) default 0 not null;
ALTER TABLE "PfItDetail" ADD "AdjPerfEqAmt" decimal(16, 2) default 0 not null;
ALTER TABLE "PfItDetail" ADD "AdjPerfReward" decimal(16, 2) default 0 not null;
ALTER TABLE "PfItDetail" ADD "AdjPerfAmt" decimal(16, 2) default 0 not null;
ALTER TABLE "PfItDetail" ADD "AdjCntingCode" varchar2(1);

comment on column "PfItDetail"."AdjustFg" is '調整記號';
comment on column "PfItDetail"."AdjPerfEqAmt" is '調整後換算業績';
comment on column "PfItDetail"."AdjPerfReward" is '業調整後務報酬';
comment on column "PfItDetail"."AdjPerfAmt" is '調整後業績金額';
comment on column "PfItDetail"."AdjCntingCode" is '調整後是否計件';
