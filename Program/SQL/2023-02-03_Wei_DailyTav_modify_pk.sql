alter table "DailyTav" drop constraint "DailyTav_PK" ;
alter table "DailyTav" add constraint "DailyTav_PK" primary key("AcctCode", "AcDate", "CustNo", "FacmNo");