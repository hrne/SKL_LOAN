
    ALTER TABLE "FacMain" ADD "HandlingFee" DECIMAL(16,2) DEFAULT 0 NOT NULL ;
comment on column "FacMain"."HandlingFee" is '手續費';