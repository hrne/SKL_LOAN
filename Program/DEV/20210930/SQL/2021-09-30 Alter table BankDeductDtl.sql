-- 2021-09-30 智偉新增 賴桑要改的
alter table "BankDeductDtl"
add "TitaTlrNo" varchar2(6) null;
alter table "BankDeductDtl"
add "TitaTxtNo" varchar2(8) null;
commit;