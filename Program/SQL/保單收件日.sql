ALTER TABLE "InsuRenew" ADD "InsuReceiptDate" decimal(8,0) default 0 not null;
ALTER TABLE "InsuOrignal" ADD "InsuReceiptDate" decimal(8,0) default 0 not null;
comment on column "InsuRenew"."InsuReceiptDate" is '保單收件日';
comment on column "InsuOrignal"."InsuReceiptDate" is '保單收件日';

