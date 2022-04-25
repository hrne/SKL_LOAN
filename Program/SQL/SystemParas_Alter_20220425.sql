alter table "SystemParas" rename column "FtpUrl" to "LoanMediaFtpUrl";
comment on column "SystemParas"."LoanMediaFtpUrl" is 'FTP網址（撥款匯款媒體檔）';

alter table "SystemParas" rename column "FtpAuth" to "LoanMediaFtpAuth";
comment on column "SystemParas"."LoanMediaFtpAuth" is 'FTP帳號（撥款匯款媒體檔）';

alter table "SystemParas" add "SmsFtpUrl" VARCHAR2(100);
comment on column "SystemParas"."SmsFtpUrl" is 'FTP網址（簡訊媒體檔）';

alter table "SystemParas" add "SmsFtpAuth" VARCHAR2(100);
comment on column "SystemParas"."SmsFtpAuth" is 'FTP帳號（簡訊媒體檔）';

alter table "SystemParas" add "SmsFtpFlag" VARCHAR(1);
comment on column "SystemParas"."SmsFtpFlag" is '是否上傳簡訊媒體檔';