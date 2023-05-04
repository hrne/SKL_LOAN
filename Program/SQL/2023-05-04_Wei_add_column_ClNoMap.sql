ALTER TABLE "ClNoMap" ADD "OriCustNo" DECIMAL(7) DEFAULT 0 NOT NULL;
ALTER TABLE "ClNoMap" ADD "OriFacmNo" DECIMAL(3) DEFAULT 0 NOT NULL;
comment on column "ClNoMap"."OriCustNo" is '原擔保品綁定戶號';
comment on column "ClNoMap"."OriFacmNo" is '原擔保品綁定額度號碼';