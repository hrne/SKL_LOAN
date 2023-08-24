ALTER TABLE "CollTel" ADD "RecvrNote" NVARCHAR2(50)  ;
comment on column "CollTel"."RecvrNote" is '接話人備註';
ALTER TABLE "CollMeet" ADD "MeetPsnNote" NVARCHAR2(50)  ;
comment on column "CollMeet"."MeetPsnNote" is '面晤人備註';
