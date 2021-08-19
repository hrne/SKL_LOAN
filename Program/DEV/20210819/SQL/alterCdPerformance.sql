update "CdPerformance"
set "BsOffrAmtCond" = "BsOffrCntAmt" where "BsOffrCntAmt" !=0;
alter table "CdPerformance" drop column "BsOffrCntAmt";
commit;