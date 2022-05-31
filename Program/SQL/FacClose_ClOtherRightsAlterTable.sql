
ALTER TABLE "ClOtherRights" ADD "ReceiveFg" DECIMAL(1,0) DEFAULT 0 NOT NULL ;
ALTER TABLE "ClOtherRights" ADD   "ChoiceDate"  DECIMAL(8,0) DEFAULT 0 NOT NULL ;
ALTER TABLE "ClOtherRights" ADD "CustNo" DECIMAL(7,0) DEFAULT 0 NOT NULL ;
ALTER TABLE "ClOtherRights" ADD   "CloseNo"  DECIMAL(3,0) DEFAULT 0 NOT NULL ;
comment on column "ClOtherRights"."CustNo" is '戶號';
comment on column "ClOtherRights"."CloseNo" is '清償序號';
comment on column "ClOtherRights"."ReceiveFg" is '領取記號';
comment on column "ClOtherRights"."ChoiceDate" is '篩選資料日期';

ALTER TABLE "FacClose" ADD "PostAddress" NVARCHAR2(100) NULL;
comment on column "FacClose"."PostAddress" is '郵寄地址';