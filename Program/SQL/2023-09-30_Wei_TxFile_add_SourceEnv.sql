ALTER TABLE "TxFile" ADD "SourceEnv" VARCHAR2(1);
comment on column "TxFile"."SourceEnv" is '產表環境';
update "TxFile" set "SourceEnv" = 'O';
ALTER TABLE "TxPrinter" ADD "SourceEnv" VARCHAR2(1);
comment on column "TxPrinter"."SourceEnv" is '產表環境';
update "TxPrinter" set "SourceEnv" = 'O';