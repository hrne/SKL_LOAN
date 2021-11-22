ALTER TABLE "TxDataLog" ADD "CalDate" decimal(8, 0) default 0 not null;

comment on column "TxDataLog"."CalDate" is '交易日期';
