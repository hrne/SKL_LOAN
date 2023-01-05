ALTER TABLE "SystemParas" ADD "InsuSettleDate" decimal(8, 0) default 0 not null ;
comment on column "SystemParas"."InsuSettleDate" is '火險保費已解付新產日期';