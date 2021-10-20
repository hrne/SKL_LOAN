alter table "MlaundryRecord" add("ActualRepayAmt" decimal(16,2) default 0 null);
comment on column "MlaundryRecord"."ActualRepayAmt" is '實際還款金額';