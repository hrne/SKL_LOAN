
ALTER TABLE "FacClose" ADD   "DocNoE"  DECIMAL(4,0) DEFAULT 0 NOT NULL ;
comment on column "FacClose"."DocNoE" is '公文編號(迄)';
