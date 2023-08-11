ALTER TABLE "SlipMedia" ADD "AcSubBookCode" VARCHAR2(3)  ;
comment on column "SlipMedia"."AcSubBookCode" is '區隔帳冊';
ALTER TABLE "SlipMedia" MODIFY "MediaSlipNo" VARCHAR2(12);
