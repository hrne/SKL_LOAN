ALTER TABLE "SystemParas" ADD "L7206SftpUrl" varchar2(100);
ALTER TABLE "SystemParas" ADD "L7206SftpAuth" varchar2(100);
ALTER TABLE "SystemParas" ADD "L7206SftpDir" varchar2(100);
comment on column "SystemParas"."L7206SftpUrl" is 'SFTP網址（金控利關人）';
comment on column "SystemParas"."L7206SftpAuth" is 'SFTP帳號（金控利關人）';
comment on column "SystemParas"."L7206SftpDir" is 'SFTP資料夾（金控利關人）';
update "SystemParas" 
set "L7206SftpUrl" = '10.11.100.1'
  , "L7206SftpAuth" = 'loanuser:aA23895858'
  , "L7206SftpDir" = 'inbound/L7206/'
;