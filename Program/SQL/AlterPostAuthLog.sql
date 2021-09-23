
    ALTER TABLE "PostAuthLog" ADD "TitaTxCd" VARCHAR2(5) NULL;
    comment on column "PostAuthLog"."TitaTxCd" is '交易代號';