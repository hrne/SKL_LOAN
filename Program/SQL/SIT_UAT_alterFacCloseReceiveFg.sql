
    ALTER TABLE "FacClose" ADD "ReceiveFg" decimal(1) default 0 not null;
  comment on column "FacClose"."ReceiveFg" is '領取記號';