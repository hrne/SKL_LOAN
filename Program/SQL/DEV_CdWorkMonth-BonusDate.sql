alter table "CdWorkMonth"
add ("BonusDate" Decimal(8,0) default 0 null );
comment on column "CdWorkMonth"."BonusDate" is '獎金發放日';