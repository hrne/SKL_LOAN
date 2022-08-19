ALTER TABLE "InnDocRecord" ADD "FacmNoMemo" nvarchar2(20) null;
comment on column "InnDocRecord"."FacmNoMemo" is '額度備註';
ALTER TABLE "InnDocRecord" MODIFY "Remark" NVARCHAR2(100);