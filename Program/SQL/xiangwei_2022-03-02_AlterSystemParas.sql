ALTER TABLE "SystemParas" ADD "FtpUrl" varchar2(100);
ALTER TABLE "SystemParas" ADD "FtpAuth" varchar2(100);
comment on column "SystemParas"."FtpUrl" is 'FTP網址';
comment on column "SystemParas"."FtpAuth" is 'FTP帳號';