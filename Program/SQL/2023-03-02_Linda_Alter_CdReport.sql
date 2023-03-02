ALTER TABLE "CdReport" ADD "ApLogFlag" DECIMAL(1,0) DEFAULT 0 NOT NULL  ;
comment on column "CdReport"."ApLogFlag" is '敏感性資料記錄記號';
comment on column "CdReport"."Confidentiality" is '機密等級';
