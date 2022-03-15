alter table "SystemParas" add ("ShortPrinLimit" Decimal(5,0) default 0 null );
comment on column "SystemParas"."ShortPrinLimit" is '限額';
