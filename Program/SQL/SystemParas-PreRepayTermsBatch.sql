alter table "SystemParas" add("PreRepayTermsBatch" decimal(1,0) default 0 null);

comment on column "SystemParas"."PreRepayTermsBatch" is '批次預收期數';