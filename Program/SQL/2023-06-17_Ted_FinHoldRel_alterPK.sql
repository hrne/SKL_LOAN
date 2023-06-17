alter table "FinHoldRel" drop primary key;
alter table "FinHoldRel" add CONSTRAINT "FinHoldRel" PRIMARY KEY ("AcDate","Id","CompanyName");

