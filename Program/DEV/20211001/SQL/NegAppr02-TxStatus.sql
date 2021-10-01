
alter table "NegAppr02" add ("TxStatus" Decimal(1,0) default 0 not null) ;

comment on COLUMN "NegAppr02"."TxStatus" is '交易狀態';

