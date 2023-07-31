
alter table "LoanNotYet" add "BranchNo" varchar2(4);
update "LoanNotYet" set "BranchNo" = '0000';
comment on column "LoanNotYet"."BranchNo" is '單位別';