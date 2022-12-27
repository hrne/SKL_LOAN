alter table "SystemParas" add "IcsFg" VARCHAR2(1);
alter table "SystemParas" add "IcsUrl" VARCHAR2(100);
update "SystemParas"
set "IcsFg" = 'Y'
  , "IcsUrl" = 'http://10.11.100.22:7001/api-ics/tran-data/Loan'
;
