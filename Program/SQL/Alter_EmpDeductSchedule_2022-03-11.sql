alter table "EmpDeductSchedule" ADD "RepayEndDate" decimal(8, 0) default 0 not null;

comment on column "EmpDeductSchedule"."RepayEndDate" is '應繳截止日';