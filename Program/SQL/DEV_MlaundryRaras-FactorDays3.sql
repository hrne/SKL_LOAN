alter table "MlaundryParas" add("FactorDays3" decimal(3,0) DEFAULT 0 not null );
comment on column "MlaundryParas"."FactorDays3" is '樣態三統計期間天數';
comment on column "MlaundryParas"."FactorDays" is '統計期間天數';