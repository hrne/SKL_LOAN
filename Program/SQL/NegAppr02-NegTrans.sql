
alter table "NegAppr02" add "NegTransAcDate" decimal(8, 0) default 0 not null  ;
alter table "NegAppr02" add "NegTransTlrNo" varchar2(6)  ;
alter table "NegAppr02" add "NegTransTxtNo" decimal(8, 0) default 0 not null  ;


comment on COLUMN "NegAppr02"."NegTransAcDate" is '交易檔會計日';
comment on COLUMN "NegAppr02"."NegTransTlrNo" is '交易檔經辦';
comment on COLUMN "NegAppr02"."NegTransTxtNo" is '交易檔序號';

