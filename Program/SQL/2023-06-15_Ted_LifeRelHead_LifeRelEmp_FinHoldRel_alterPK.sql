alter table "LifeRelEmp" drop primary key;
alter table "LifeRelEmp" add CONSTRAINT "LifeRelEmp" PRIMARY KEY ("AcDate","EmpId");

alter table "LifeRelHead" drop primary key;
alter table "LifeRelHead" add CONSTRAINT "LifeRelHead" PRIMARY KEY ("AcDate","HeadId","RelId","BusId");

alter table "FinHoldRel" drop primary key;
alter table "FinHoldRel" add CONSTRAINT "FinHoldRel" PRIMARY KEY ("AcDate","Id");

