alter table "CdCity" add ("AccTelArea" VARCHAR2(5),"AccTelNo" VARCHAR2(10),"AccTelExt" VARCHAR2(5),"LegalArea" VARCHAR2(5),"LegalNo" VARCHAR2(10),"LegalExt" VARCHAR2(5));

comment on column "CdCity"."AccTelArea" is '催收人員電話-區碼';
comment on column "CdCity"."AccTelNo" is '催收人員電話';
comment on column "CdCity"."AccTelExt" is '催收人員電話-分機';

comment on column "CdCity"."LegalArea" is '法務人員電話-區碼';
comment on column "CdCity"."LegalNo" is '法務人員電話';
comment on column "CdCity"."LegalExt" is '法務人員電話-分機';

commit;