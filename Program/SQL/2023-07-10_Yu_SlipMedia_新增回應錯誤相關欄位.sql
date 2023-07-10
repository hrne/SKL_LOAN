ALTER TABLE "SlipMedia2022" ADD "ErrorCode" varchar2(3) NULL;
ALTER TABLE "SlipMedia2022" ADD "ErrorMsg" varchar2(2000) NULL;
comment on column "SlipMedia2022"."ErrorCode" is '回應錯誤代碼';
comment on column "SlipMedia2022"."ErrorMsg" is '回應錯誤訊息';