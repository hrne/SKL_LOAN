
    ALTER TABLE "BankRemit" ADD "CustId" NVARCHAR2(10) NULL;
    ALTER TABLE "BankRemit" ADD "CustGender" NVARCHAR2(1) NULL;
    ALTER TABLE "BankRemit" ADD "CustBirthday" DECIMAL(8,0) DEFAULT 0 NOT NULL ;
comment on column "BankRemit"."CustId" is '收款人ID';
comment on column "BankRemit"."CustBirthday" is '收款人出生日期';
comment on column "BankRemit"."CustGender" is '收款人性別';

    