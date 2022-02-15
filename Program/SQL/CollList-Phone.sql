ALTER TABLE "CollList" ADD "AccTelArea" VARCHAR2(5) NULL;
ALTER TABLE "CollList" ADD "AccTelNo" VARCHAR2(10) NULL;
ALTER TABLE "CollList" ADD "AccTelExt" VARCHAR2(5) NULL;
ALTER TABLE "CollList" ADD "LegalArea" VARCHAR2(5) NULL;
ALTER TABLE "CollList" ADD "LegalNo" VARCHAR2(10) NULL;
ALTER TABLE "CollList" ADD "LegalExt" VARCHAR2(5) NULL;

comment on column "CollList"."AccTelArea" is '催收人員電話-區碼';
comment on column "CollList"."AccTelNo" is '催收人員電話';
comment on column "CollList"."AccTelExt" is '催收人員電話-分機';
comment on column "CollList"."LegalArea" is '法務人員電話-區碼';
comment on column "CollList"."LegalNo" is '法務人員電話';
comment on column "CollList"."LegalExt" is '法務人員電話-分機';