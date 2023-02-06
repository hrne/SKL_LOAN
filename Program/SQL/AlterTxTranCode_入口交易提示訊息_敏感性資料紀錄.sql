ALTER TABLE "TxTranCode" ADD "ChainTranMsg" NVARCHAR2(200) NULL;
ALTER TABLE "TxTranCode" ADD "ApLogFlag" DECIMAL(1,0) DEFAULT 0 NOT NULL ;
ALTER TABLE "TxTranCode" ADD "ApLogRim" VARCHAR2(5) NULL;
comment on column "TxTranCode"."ChainTranMsg" is '入口交易提示訊息';
comment on column "TxTranCode"."ApLogFlag" is '敏感性資料記錄記號';
comment on column "TxTranCode"."ApLogRim" is '敏感性資料記錄Rim記號';