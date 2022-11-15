alter table "LoanBorTx" modify "TitaCalTm" decimal(8, 0) default 0 ;
update "LoanBorTx"
set "TitaCalTm" = "TitaCalTm" * 100;
