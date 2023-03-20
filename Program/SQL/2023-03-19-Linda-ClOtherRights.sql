ALTER TABLE "ClOtherRights" ADD "SecuredDate" decimal(8, 0) DEFAULT 0 NOT NULL  ;
comment on column "ClOtherRights"."SecuredDate" is '擔保債權確定日期';
ALTER TABLE "ClOtherRights" ADD "Location" nvarchar2(40)  ;
comment on column "ClOtherRights"."Location" is '建物坐落地號';

