ALTER TABLE "FacCaseAppl" ADD "IsSuspected" VARCHAR2(1) NULL;
ALTER TABLE "FacCaseAppl" ADD "IsSuspectedCheck" VARCHAR2(1) NULL;
ALTER TABLE "FacCaseAppl" ADD "IsSuspectedCheckType" VARCHAR2(1) NULL;
ALTER TABLE "FacCaseAppl" ADD "IsDate" decimal(8, 0) default 0 not null;
comment on column "FacCaseAppl"."IsSuspected" is '是否為金控「疑似準利害關係人」名單';
comment on column "FacCaseAppl"."IsSuspectedCheck" is '是否為金控疑似利害關係人';
comment on column "FacCaseAppl"."IsSuspectedCheckType" is '是否為金控疑似利害關係人_確認狀態';
comment on column "FacCaseAppl"."IsDate" is '是否資訊日期';
