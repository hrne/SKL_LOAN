ALTER TABLE "ClParking" ADD "BdNo1" varchar2(5) null;
ALTER TABLE "ClParking" ADD "BdNo2" varchar2(3) null;
ALTER TABLE "ClParking" ADD "Amount" decimal(16, 2) default 0 not null;
comment on column "ClParking"."BdNo1" is '�ظ�';
comment on column "ClParking"."BdNo2" is '�ظ�(�l��)';
comment on column "ClParking"."Amount" is '����(��)';