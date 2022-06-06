
ALTER TABLE "FacMain" ADD   "RenewCnt"  DECIMAL(3,0) DEFAULT 0 NOT NULL ;
ALTER TABLE "FacMain" ADD   "OldFacmNo"  DECIMAL(3,0) DEFAULT 0 NOT NULL ;
comment on column "FacMain"."RenewCnt" is '展期次數';
comment on column "FacMain"."OldFacmNo" is '原額度編號';