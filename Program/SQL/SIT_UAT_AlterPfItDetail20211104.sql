ALTER TABLE "PfItDetail" ADD "AdjRange" decimal(1, 0) default 0 not null;
ALTER TABLE "PfItDetail" ADD "AdjPerfEqAmt" decimal(16, 2) default 0 not null;
ALTER TABLE "PfItDetail" ADD "AdjPerfReward" decimal(16, 2) default 0 not null;
ALTER TABLE "PfItDetail" ADD "AdjPerfAmt" decimal(16, 2) default 0 not null;
ALTER TABLE "PfItDetail" ADD "AdjCntingCode" varchar2(1);

comment on column "PfItDetail"."AdjustFg" is '�վ�O��';
comment on column "PfItDetail"."AdjPerfEqAmt" is '�վ�ᴫ��~�Z';
comment on column "PfItDetail"."AdjPerfReward" is '�~�վ��ȳ��S';
comment on column "PfItDetail"."AdjPerfAmt" is '�վ��~�Z���B';
comment on column "PfItDetail"."AdjCntingCode" is '�վ��O�_�p��';
