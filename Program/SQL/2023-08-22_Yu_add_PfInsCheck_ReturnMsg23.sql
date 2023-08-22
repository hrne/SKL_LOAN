ALTER TABLE "PfInsCheck" ADD "ReturnMsg2" nvarchar2(2000) ;
ALTER TABLE "PfInsCheck" ADD "ReturnMsg3" nvarchar2(2000) ;
comment on column "PfInsCheck"."ReturnMsg" is '回應訊息1';
comment on column "PfInsCheck"."ReturnMsg2" is '回應訊息2';
comment on column "PfInsCheck"."ReturnMsg3" is '回應訊息3';