ALTER TABLE "ClFac" ADD "OriEvaNotWorth" DECIMAL(16,2) DEFAULT 0 NOT NULL ;
comment on column "ClFac"."OriSettingAmt" is '原設定金額/股數';
comment on column "ClFac"."OriEvaNotWorth" is '原評估淨值';