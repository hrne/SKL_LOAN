ALTER TABLE "CustMain" ADD "IsLimit" VARCHAR2(1) NULL;
ALTER TABLE "CustMain" ADD "IsRelated" VARCHAR2(1) NULL;
ALTER TABLE "CustMain" ADD "IsLnrelNear" VARCHAR2(1) NULL;
ALTER TABLE "CustMain" ADD "IsDate" decimal(8, 0) default 0 not null;
comment on column "CustMain"."IsLimit" is '是否為授信限制對象';
comment on column "CustMain"."IsRelated" is '是否為利害關係人';
comment on column "CustMain"."IsLnrelNear" is '是否為準利害關係人';
comment on column "CustMain"."IsDate" is '是否資訊日期';
