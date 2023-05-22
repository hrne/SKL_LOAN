ALTER TABLE "BankRemit" ADD "CoreRemitFlag" decimal(1, 0) default 0 not null ;
comment on column "BankRemit"."CoreRemitFlag" is '核心匯款記號';