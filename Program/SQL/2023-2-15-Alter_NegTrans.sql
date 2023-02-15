alter table "NegTrans" add constraint "NegTrans_PK" primary key("AcDate", "TitaTlrNo", "TitaTxtNo", "CustNo");

alter table "NegTrans" DROP primary key;