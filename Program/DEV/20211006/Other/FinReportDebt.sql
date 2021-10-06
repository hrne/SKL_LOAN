drop table "FinReportDebt" purge;

create table "FinReportDebt" (
  "CustUKey" varchar2(32),
  "UKey" varchar2(32),
  "StartYY" decimal(4, 0) default 0 not null,
  "StartMM" decimal(2, 0) default 0 not null,
  "EndYY" decimal(4, 0) default 0 not null,
  "EndMM" decimal(2, 0) default 0 not null,
  "AssetTotal" decimal(18, 0) default 0 not null,
  "FlowAsset" decimal(18, 0) default 0 not null,
  "Cash" decimal(18, 0) default 0 not null,
  "FinAsset" decimal(18, 0) default 0 not null,
  "ReceiveTicket" decimal(18, 0) default 0 not null,
  "ReceiveAccount" decimal(18, 0) default 0 not null,
  "ReceiveRelation" decimal(18, 0) default 0 not null,
  "OtherReceive" decimal(18, 0) default 0 not null,
  "Stock" decimal(18, 0) default 0 not null,
  "PrepayItem" decimal(18, 0) default 0 not null,
  "OtherFlowAsset" decimal(18, 0) default 0 not null,
  "AccountItem01" nvarchar2(20),
  "AccountItem02" nvarchar2(20),
  "AccountItem03" nvarchar2(20),
  "AccountValue01" decimal(18, 0) default 0 not null,
  "AccountValue02" decimal(18, 0) default 0 not null,
  "AccountValue03" decimal(18, 0) default 0 not null,
  "LongInvest" decimal(18, 0) default 0 not null,
  "FixedAsset" decimal(18, 0) default 0 not null,
  "Land" decimal(18, 0) default 0 not null,
  "HouseBuild" decimal(18, 0) default 0 not null,
  "MachineEquip" decimal(18, 0) default 0 not null,
  "OtherEquip" decimal(18, 0) default 0 not null,
  "PrepayEquip" decimal(18, 0) default 0 not null,
  "UnFinish" decimal(18, 0) default 0 not null,
  "Depreciation" decimal(18, 0) default 0 not null,
  "InvisibleAsset" decimal(18, 0) default 0 not null,
  "OtherAsset" decimal(18, 0) default 0 not null,
  "AccountItem04" nvarchar2(20),
  "AccountItem05" nvarchar2(20),
  "AccountItem06" nvarchar2(20),
  "AccountValue04" decimal(18, 0) default 0 not null,
  "AccountValue05" decimal(18, 0) default 0 not null,
  "AccountValue06" decimal(18, 0) default 0 not null,
  "DebtNetTotal" decimal(18, 0) default 0 not null,
  "FlowDebt" decimal(18, 0) default 0 not null,
  "ShortLoan" decimal(18, 0) default 0 not null,
  "PayShortTicket" decimal(18, 0) default 0 not null,
  "PayTicket" decimal(18, 0) default 0 not null,
  "PayAccount" decimal(18, 0) default 0 not null,
  "PayRelation" decimal(18, 0) default 0 not null,
  "OtherPay" decimal(18, 0) default 0 not null,
  "PreReceiveItem" decimal(18, 0) default 0 not null,
  "LongDebtOneYear" decimal(18, 0) default 0 not null,
  "Shareholder" decimal(18, 0) default 0 not null,
  "OtherFlowDebt" decimal(18, 0) default 0 not null,
  "AccountItem07" nvarchar2(20),
  "AccountItem08" nvarchar2(20),
  "AccountItem09" nvarchar2(20),
  "AccountValue07" decimal(18, 0) default 0 not null,
  "AccountValue08" decimal(18, 0) default 0 not null,
  "AccountValue09" decimal(18, 0) default 0 not null,
  "LongDebt" decimal(18, 0) default 0 not null,
  "OtherDebt" decimal(18, 0) default 0 not null,
  "DebtTotal" decimal(18, 0) default 0 not null,
  "NetValue" decimal(18, 0) default 0 not null,
  "Capital" decimal(18, 0) default 0 not null,
  "CapitalSurplus" decimal(18, 0) default 0 not null,
  "RetainProfit" decimal(18, 0) default 0 not null,
  "OtherRight" decimal(18, 0) default 0 not null,
  "TreasuryStock" decimal(18, 0) default 0 not null,
  "UnControlRight" decimal(18, 0) default 0 not null,
  "AccountItem10" nvarchar2(20),
  "AccountItem11" nvarchar2(20),
  "AccountValue10" decimal(18, 0) default 0 not null,
  "AccountValue11" decimal(18, 0) default 0 not null,
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table "FinReportDebt" add constraint "FinReportDebt_PK" primary key("CustUKey", "UKey");

comment on table "FinReportDebt" is '客戶財務報表.資產負債表';
comment on column "FinReportDebt"."CustUKey" is '客戶識別碼';
comment on column "FinReportDebt"."UKey" is '識別碼';
comment on column "FinReportDebt"."StartYY" is '年度';
comment on column "FinReportDebt"."StartMM" is '年度_起月';
comment on column "FinReportDebt"."EndYY" is '年度_迄年';
comment on column "FinReportDebt"."EndMM" is '年度_迄月';
comment on column "FinReportDebt"."AssetTotal" is '資產總額';
comment on column "FinReportDebt"."FlowAsset" is '流動資產';
comment on column "FinReportDebt"."Cash" is '現金及約當現金';
comment on column "FinReportDebt"."FinAsset" is '金融資產(含其他)-流動';
comment on column "FinReportDebt"."ReceiveTicket" is '應收票據(淨額)';
comment on column "FinReportDebt"."ReceiveAccount" is '應收帳款(淨額)';
comment on column "FinReportDebt"."ReceiveRelation" is '應收關係人款';
comment on column "FinReportDebt"."OtherReceive" is '其他應收款';
comment on column "FinReportDebt"."Stock" is '存貨';
comment on column "FinReportDebt"."PrepayItem" is '預付款項';
comment on column "FinReportDebt"."OtherFlowAsset" is '其他流動資產';
comment on column "FinReportDebt"."AccountItem01" is '流動資產_會計科目01';
comment on column "FinReportDebt"."AccountItem02" is '流動資產_會計科目02';
comment on column "FinReportDebt"."AccountItem03" is '流動資產_會計科目03';
comment on column "FinReportDebt"."AccountValue01" is '流動資產_會計科目值01';
comment on column "FinReportDebt"."AccountValue02" is '流動資產_會計科目值02';
comment on column "FinReportDebt"."AccountValue03" is '流動資產_會計科目值03';
comment on column "FinReportDebt"."LongInvest" is '基金及長期投資';
comment on column "FinReportDebt"."FixedAsset" is '固定資產';
comment on column "FinReportDebt"."Land" is '土地';
comment on column "FinReportDebt"."HouseBuild" is '房屋及建築';
comment on column "FinReportDebt"."MachineEquip" is '機器設備';
comment on column "FinReportDebt"."OtherEquip" is '運輸、辦公、其他設備';
comment on column "FinReportDebt"."PrepayEquip" is '預付設備款';
comment on column "FinReportDebt"."UnFinish" is '未完成工程';
comment on column "FinReportDebt"."Depreciation" is '減︰累計折舊';
comment on column "FinReportDebt"."InvisibleAsset" is '無形資產';
comment on column "FinReportDebt"."OtherAsset" is '其他資產';
comment on column "FinReportDebt"."AccountItem04" is '其他資產_會計科目04';
comment on column "FinReportDebt"."AccountItem05" is '其他資產_會計科目05';
comment on column "FinReportDebt"."AccountItem06" is '其他資產_會計科目06';
comment on column "FinReportDebt"."AccountValue04" is '其他資產_會計科目值04';
comment on column "FinReportDebt"."AccountValue05" is '其他資產_會計科目值05';
comment on column "FinReportDebt"."AccountValue06" is '其他資產_會計科目值06';
comment on column "FinReportDebt"."DebtNetTotal" is '負債及淨值總額';
comment on column "FinReportDebt"."FlowDebt" is '流動負債';
comment on column "FinReportDebt"."ShortLoan" is '短期借款';
comment on column "FinReportDebt"."PayShortTicket" is '應付短期票券';
comment on column "FinReportDebt"."PayTicket" is '應付票據(淨額)';
comment on column "FinReportDebt"."PayAccount" is '應付帳款(淨額)';
comment on column "FinReportDebt"."PayRelation" is '應付關係人款';
comment on column "FinReportDebt"."OtherPay" is '其他應付款';
comment on column "FinReportDebt"."PreReceiveItem" is '預收款項';
comment on column "FinReportDebt"."LongDebtOneYear" is '長期負債(一年內)';
comment on column "FinReportDebt"."Shareholder" is '股東墊款';
comment on column "FinReportDebt"."OtherFlowDebt" is '其他流動負債';
comment on column "FinReportDebt"."AccountItem07" is '流動負債 _會計科目07';
comment on column "FinReportDebt"."AccountItem08" is '流動負債 _會計科目08';
comment on column "FinReportDebt"."AccountItem09" is '流動負債 _會計科目09';
comment on column "FinReportDebt"."AccountValue07" is '流動負債 _會計科目值07';
comment on column "FinReportDebt"."AccountValue08" is '流動負債 _會計科目值08';
comment on column "FinReportDebt"."AccountValue09" is '流動負債 _會計科目值09';
comment on column "FinReportDebt"."LongDebt" is '長期負債';
comment on column "FinReportDebt"."OtherDebt" is '其它負債';
comment on column "FinReportDebt"."DebtTotal" is '負債總額';
comment on column "FinReportDebt"."NetValue" is '淨值';
comment on column "FinReportDebt"."Capital" is '資本';
comment on column "FinReportDebt"."CapitalSurplus" is '資本公積';
comment on column "FinReportDebt"."RetainProfit" is '保留盈餘';
comment on column "FinReportDebt"."OtherRight" is '其他權益';
comment on column "FinReportDebt"."TreasuryStock" is '庫藏股票';
comment on column "FinReportDebt"."UnControlRight" is '非控制權益';
comment on column "FinReportDebt"."AccountItem10" is '淨值_會計科目10';
comment on column "FinReportDebt"."AccountItem11" is '淨值_會計科目11';
comment on column "FinReportDebt"."AccountValue10" is '淨值_會計科目值10';
comment on column "FinReportDebt"."AccountValue11" is '淨值_會計科目值11';
comment on column "FinReportDebt"."CreateDate" is '建檔日期時間';
comment on column "FinReportDebt"."CreateEmpNo" is '建檔人員';
comment on column "FinReportDebt"."LastUpdate" is '最後更新日期時間';
comment on column "FinReportDebt"."LastUpdateEmpNo" is '最後更新人員';
