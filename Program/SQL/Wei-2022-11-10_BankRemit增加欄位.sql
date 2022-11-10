alter table "BankRemit" ADD "Seq" decimal(6, 0) default 0 not null;
alter table "BankRemit" drop constraint "BankRemit_PK";
alter table "BankRemit" add constraint "BankRemit_PK" primary key("AcDate", "TitaTlrNo", "TitaTxtNo", "Seq");