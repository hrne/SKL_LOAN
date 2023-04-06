    ALTER TABLE "AcAcctCheck" ADD "MasterClsAmt" DECIMAL(16,2) DEFAULT 0 NOT NULL ;
comment on column "AcAcctCheck"."MasterClsAmt" is '業務檔已銷金額';