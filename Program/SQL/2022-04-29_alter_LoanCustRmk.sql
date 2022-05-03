ALTER TABLE "LoanCustRmk" ADD "FacmNo" DECIMAL(3) DEFAULT 0;
comment on column "LoanCustRmk"."FacmNo" is '額度編號';

ALTER TABLE "LoanCustRmk" ADD "BormNo" DECIMAL(3) DEFAULT 0;
comment on column "LoanCustRmk"."BormNo" is '撥款序號';

ALTER TABLE "LoanCustRmk" ADD "BorxNo" DECIMAL(4) DEFAULT 0;
comment on column "LoanCustRmk"."BorxNo" is '交易內容檔序號';