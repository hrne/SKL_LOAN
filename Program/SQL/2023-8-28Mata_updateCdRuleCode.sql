
create table "CdRuleCode" (
  "RuleCode" varchar2(5),
  "RuleCodeItem" nvarchar2(30),
  "RmkItem" nvarchar2(30),
  "RuleStDate" decimal(8, 0) default 0 not null,
  "RuleEdDate" decimal(8, 0) default 0 not null,
  "EnableMark" varchar2(1),
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table "CdRuleCode" add constraint "CdRuleCode_PK" primary key("RuleCode");

create index "CdRuleCode_Index1" on "CdRuleCode"("RuleCode" asc);

comment on table "CdRuleCode" is '管制代碼檔';
comment on column "CdRuleCode"."RuleCode" is '規定管制項目代碼';
comment on column "CdRuleCode"."RuleCodeItem" is '規定管制項目中文';
comment on column "CdRuleCode"."RmkItem" is '備註';
comment on column "CdRuleCode"."RuleStDate" is '管制生效日';
comment on column "CdRuleCode"."RuleEdDate" is '管制取消日';
comment on column "CdRuleCode"."EnableMark" is '是否啟用';
comment on column "CdRuleCode"."CreateDate" is '建檔日期時間';
comment on column "CdRuleCode"."CreateEmpNo" is '建檔人員';
comment on column "CdRuleCode"."LastUpdate" is '最後更新日期時間';
comment on column "CdRuleCode"."LastUpdateEmpNo" is '最後更新人員';

Insert into "TxTranCode" ("TranNo","TranItem","Desc","TypeFg","CancelFg","ModifyFg","MenuNo","SubMenuNo","MenuFg","Status","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo","SubmitFg","CustDataCtrlFg","CustRmkFg","ChainTranMsg","ApLogFlag","ApLogRim") values ('L6090','管制代碼檔','管制代碼檔',1,0,0,'L6','6',1,0,to_timestamp('16-8月 -23 02.09.05.950000000 下午','DD-MON-RR HH.MI.SSXFF AM'),'001719',to_timestamp('16-8月 -23 02.09.05.950000000 下午','DD-MON-RR HH.MI.SSXFF AM'),'001719',0,0,0,null,0,null);
Insert into "TxTranCode" ("TranNo","TranItem","Desc","TypeFg","CancelFg","ModifyFg","MenuNo","SubMenuNo","MenuFg","Status","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo","SubmitFg","CustDataCtrlFg","CustRmkFg","ChainTranMsg","ApLogFlag","ApLogRim") values ('L6891','管制代碼檔維護','管制代碼檔維護',1,0,0,'L6','6',0,0,to_timestamp('17-8月 -23 04.15.43.435000000 下午','DD-MON-RR HH.MI.SSXFF AM'),'001719',to_timestamp('17-8月 -23 04.15.43.435000000 下午','DD-MON-RR HH.MI.SSXFF AM'),'001719',0,0,0,'此交易從L6090進入',0,null);
