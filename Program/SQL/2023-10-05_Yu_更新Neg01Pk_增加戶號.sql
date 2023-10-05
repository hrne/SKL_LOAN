alter table "NegAppr01" drop constraint "NegAppr01_PK";
alter table "NegAppr01" add constraint "NegAppr01_PK" primary key("AcDate", "TitaTlrNo", "TitaTxtNo", "FinCode", "CustNo");
