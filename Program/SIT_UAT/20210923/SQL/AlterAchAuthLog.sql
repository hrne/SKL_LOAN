
    ALTER TABLE "AchAuthLog" ADD "TitaTxCd" VARCHAR2(5) NULL;
    comment on column "AchAuthLog"."TitaTxCd" is '交易代號';