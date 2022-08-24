
  ALTER TABLE "ClOtherRights" ADD "ReceiveCustNo" DECIMAL(7, 0) DEFAULT 0 NOT NULL ;
comment on column "ClOtherRights"."ReceiveCustNo" is '篩選戶號';
comment on column "ClOtherRights"."CustNo" is '戶號';