
alter table "CdInsurer"
add ("InsurerId" varchar2(10) );
comment on column "CdInsurer"."InsurerId" is '公司統編';