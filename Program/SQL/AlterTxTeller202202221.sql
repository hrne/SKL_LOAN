    ALTER TABLE "TxTeller" ADD "AdminFg" DECIMAL(1) DEFAULT 0 NOT NULL ;
    comment on column "TxTeller"."AdminFg" is '管理者權限記號';