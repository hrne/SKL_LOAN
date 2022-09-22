ALTER TABLE "AcDetail" 
ADD (
    "SlipSumNo" decimal(2, 0) default 0 not null
);
comment on column "AcDetail"."SlipSumNo" is '彙總傳票批號';
 
 