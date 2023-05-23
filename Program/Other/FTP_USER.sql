--FTP_USER
CREATE TABLE itxadmin."FTP_USER" (      
   userid VARCHAR2(64) NOT NULL,       
   userpassword VARCHAR2(64),      
   homedirectory VARCHAR2(128) NOT NULL,             
   enableflag VARCHAR2(1) DEFAULT 'Y',    
   writepermission VARCHAR2(1) DEFAULT 'N',       
   idletime NUMBER(19,0) DEFAULT 0,             
   uploadrate NUMBER(19,0) DEFAULT 0,             
   downloadrate NUMBER(19,0) DEFAULT 0,
   maxloginnumber NUMBER(19,0) DEFAULT 0,
   maxloginperip NUMBER(19,0) DEFAULT 0
);

ALTER TABLE itxadmin."FTP_USER"
ADD  CONSTRAINT "FTP_USER_PK" PRIMARY KEY ("USERID");

insert all
into "FTP_USER" (userid, userpassword, homedirectory, enableflag, writepermission, idletime, uploadrate, downloadrate, maxloginnumber, maxloginperip)
  values ('admin', '1qaz2wsx', 'C:\SKL\iTX_Log\', 'Y','Y',3000, 48000000, 48000000, 20, 2)
into "FTP_USER" (userid, userpassword, homedirectory, enableflag, writepermission, idletime, uploadrate, downloadrate, maxloginnumber, maxloginperip)
  values ('itx', '1qaz2wsx', 'C:\SKL\iTX_Log\', 'Y','Y',3000, 48000000, 48000000, 20, 2)
select * from dual;