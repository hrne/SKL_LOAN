ALTER TABLE "ClMain" ADD "LastClOtherSeq" DECIMAL(3,0) DEFAULT 0 NOT NULL ;
comment on column "ClMain"."LastClOtherSeq" is '他項權利最後序號';