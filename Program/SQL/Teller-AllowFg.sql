alter table "TxTeller" add("AllowFg" decimal(1,0) default 0 null);
comment on column "TxTeller"."AllowFg" is '授權等級';
