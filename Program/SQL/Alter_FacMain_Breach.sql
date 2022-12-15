ALTER TABLE "FacMain" ADD "BreachFlag" varchar2(1);
ALTER TABLE "FacMain" ADD "BreachCode" varchar2(3);
ALTER TABLE "FacMain" ADD "BreachGetCode" varchar2(1);
ALTER TABLE "FacMain" ADD "ProhibitMonth" decimal(3, 0) default 0 not null;
ALTER TABLE "FacMain" ADD "BreachPercent" decimal(5, 2) default 0 not null;
ALTER TABLE "FacMain" ADD "BreachDecreaseMonth" decimal(3, 0) default 0 not null;
ALTER TABLE "FacMain" ADD "BreachDecrease" decimal(5, 2) default 0 not null;
ALTER TABLE "FacMain" ADD "BreachStartPercent" decimal(3, 0) default 0 not null;
  
comment on column "FacMain"."BreachFlag" is '是否綁約';
comment on column "FacMain"."BreachCode" is '違約適用方式';
comment on column "FacMain"."BreachGetCode" is '違約金收取方式';
comment on column "FacMain"."ProhibitMonth" is '限制清償期限';
comment on column "FacMain"."BreachPercent" is '違約金百分比';
comment on column "FacMain"."BreachDecreaseMonth" is '違約金分段月數';
comment on column "FacMain"."BreachDecrease" is '分段遞減百分比';
comment on column "FacMain"."BreachStartPercent" is '還款起算比例%';