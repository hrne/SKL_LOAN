alter table "CdCode"
add ("EffectFlag" decimal(1, 0) default 0 not null )

comment on column "CdCode"."EffectFlag" is '生效記號';

