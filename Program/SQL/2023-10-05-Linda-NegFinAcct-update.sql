ALTER TABLE "NegFinAcct" ADD "NewFinCode" VARCHAR2(8)  ;
comment on column "NegFinAcct"."NewFinCode" is '合併銀行';
ALTER TABLE "NegFinAcct" ADD "MergerDate" decimal(8, 0) DEFAULT 0 NOT NULL  ;
comment on column "NegFinAcct"."MergerDate" is '合併日';
ALTER TABLE "NegFinAcct" ADD "ExecuteDate" decimal(8, 0) DEFAULT 0 NOT NULL  ;  ;
comment on column "NegFinAcct"."ExecuteDate" is '執行日期';
ALTER TABLE "NegFinAcct" ADD "Note" NVARCHAR2(100)  ;
comment on column "NegFinAcct"."Note" is '備註';
