DELETE FROM "CdCode" WHERE "DefCode"='ReceivableFlag' and "Code"='1';
UPDATE "CdCode" SET "Item" = '總帳銷帳碼科目'  WHERE "DefCode"='ReceivableFlag' and "Code"='8' ;
comment on column "CdAcCode"."InuseFlag" is '使用記號' ;
