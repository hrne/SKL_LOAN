drop table "SlipEbsRecord" purge;

drop sequence "SlipEbsRecord_SEQ";

create table "SlipEbsRecord" (
  "UploadNo" decimal(11,0) not null,
  "GroupId" varchar2(11),
  "RequestData" clob,
  "ResultData" clob,
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table "SlipEbsRecord" add constraint "SlipEbsRecord_PK" primary key("UploadNo");

create sequence "SlipEbsRecord_SEQ" minvalue 1 maxvalue 2147483647 increment by 1 start with 1 nocache cycle;

comment on table "SlipEbsRecord" is '傳票上傳EBS紀錄檔';
comment on column "SlipEbsRecord"."UploadNo" is '上傳序號';
comment on column "SlipEbsRecord"."GroupId" is 'ETL批號';
comment on column "SlipEbsRecord"."RequestData" is '上傳資料';
comment on column "SlipEbsRecord"."ResultData" is '收到資料';
comment on column "SlipEbsRecord"."CreateDate" is '建檔日期時間';
comment on column "SlipEbsRecord"."CreateEmpNo" is '建檔人員';
comment on column "SlipEbsRecord"."LastUpdate" is '最後更新日期時間';
comment on column "SlipEbsRecord"."LastUpdateEmpNo" is '最後更新人員';
