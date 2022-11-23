ALTER TABLE "NegFinAcct" ADD "RemitAcct2" VARCHAR2(16) null;
comment on column "NegFinAcct"."RemitAcct2" is '調解匯款帳號';
ALTER TABLE "NegFinAcct" ADD "RemitAcct3" VARCHAR2(16) null;
comment on column "NegFinAcct"."RemitAcct3" is '更生匯款帳號';
ALTER TABLE "NegFinAcct" ADD "RemitAcct4" VARCHAR2(16) null;
comment on column "NegFinAcct"."RemitAcct4" is '清算匯款帳號';
comment on column "NegFinAcct"."RemitAcct" is '債協匯款帳號';
