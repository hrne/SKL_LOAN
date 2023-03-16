drop table "TxAmlRatingAppl" purge;

drop sequence "TxAmlRatingAppl_SEQ";

create table "TxAmlRatingAppl" (
  "LogNo" decimal(11,0) not null,
  "Unit" varchar2(6),
  "AcceptanceUnit" varchar2(6),
  "RoleId" varchar2(2),
  "TransactionId" nvarchar2(100),
  "AcctNo" varchar2(30),
  "CaseNo" varchar2(40),
  "AcctId" varchar2(30),
  "InsurCount" decimal(2, 0) default 0 not null,
  "BirthEstDt" varchar2(8),
  "SourceId" varchar2(10),
  "ModifyDate" varchar2(14),
  "OcupCd" varchar2(8),
  "OrgType" varchar2(5),
  "Bcode" varchar2(5),
  "OcupNote" nvarchar2(100),
  "PayMethod" varchar2(5),
  "PayType" varchar2(5),
  "Channel" varchar2(12),
  "PolicyType" varchar2(5),
  "InsuranceCurrency" varchar2(3),
  "InsuranceAmount" decimal(16, 0) default 0 not null,
  "AddnCd" varchar2(200),
  "InsrStakesCd" varchar2(5),
  "BnfcryNHdrGrpCd" varchar2(5),
  "AgentTradeCd" varchar2(5),
  "FstPayerNHdrGrpCd" varchar2(5),
  "FstPayerNHdrStksCd" varchar2(5),
  "FstPrmOvrseaAcctCd" varchar2(5),
  "TotalAmtCd" decimal(16, 0) default 0 not null,
  "DeclinatureCd" varchar2(5),
  "FstInsuredCd" varchar2(5),
  "TWAddrHoldCd" varchar2(5),
  "TWAddrLegalCd" varchar2(5),
  "DurationCd" varchar2(5),
  "SpecialIdentity" varchar2(5),
  "LawForceWarranty" varchar2(5),
  "MovableGrnteeCd" varchar2(5),
  "BearerScursGrnteeCd" varchar2(5),
  "AgreeDefaultFineCd" varchar2(5),
  "NonBuyingRealEstateCd" varchar2(5),
  "NonStkHolderGrnteeCd" varchar2(5),
  "ReachCase" decimal(4, 0) default 0 not null,
  "AccountTypeCd" varchar2(5),
  "QueryType" varchar2(1),
  "IdentityCd" varchar2(1),
  "RspRequestId" varchar2(36),
  "RspStatus" varchar2(10),
  "RspStatusCode" varchar2(4),
  "RspStatusDesc" varchar2(100),
  "RspUnit" varchar2(6),
  "RspTransactionId" nvarchar2(100),
  "RspAcctNo" varchar2(30),
  "RspCaseNo" varchar2(40),
  "RspInsurCount" decimal(2, 0) default 0 not null,
  "RspTotalRatingsScore" decimal(7, 2) default 0 not null,
  "RspTotalRatings" varchar2(1),
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table "TxAmlRatingAppl" add constraint "TxAmlRatingAppl_PK" primary key("LogNo");

create sequence "TxAmlRatingAppl_SEQ" minvalue 1 maxvalue 2147483647 increment by 1 start with 1 nocache cycle;

create index "TxAmlRatingAppl_Index1" on "TxAmlRatingAppl"("CaseNo" asc);

comment on table "TxAmlRatingAppl" is 'Eloan評級案件申請留存檔';
comment on column "TxAmlRatingAppl"."LogNo" is '序號';
comment on column "TxAmlRatingAppl"."Unit" is '查詢單位';
comment on column "TxAmlRatingAppl"."AcceptanceUnit" is '代辦單位';
comment on column "TxAmlRatingAppl"."RoleId" is '保單角色';
comment on column "TxAmlRatingAppl"."TransactionId" is 'AML 交易序號';
comment on column "TxAmlRatingAppl"."AcctNo" is '保單號碼/放款案號';
comment on column "TxAmlRatingAppl"."CaseNo" is '案號';
comment on column "TxAmlRatingAppl"."AcctId" is '保險證號';
comment on column "TxAmlRatingAppl"."InsurCount" is '投保次數';
comment on column "TxAmlRatingAppl"."BirthEstDt" is '個人出生日';
comment on column "TxAmlRatingAppl"."SourceId" is '查詢來源';
comment on column "TxAmlRatingAppl"."ModifyDate" is '異動時間';
comment on column "TxAmlRatingAppl"."OcupCd" is '職業代碼';
comment on column "TxAmlRatingAppl"."OrgType" is '組織型態';
comment on column "TxAmlRatingAppl"."Bcode" is '行業代碼';
comment on column "TxAmlRatingAppl"."OcupNote" is '行業別說明';
comment on column "TxAmlRatingAppl"."PayMethod" is '繳費渠道';
comment on column "TxAmlRatingAppl"."PayType" is '繳費方式';
comment on column "TxAmlRatingAppl"."Channel" is '渠道(進件通路)';
comment on column "TxAmlRatingAppl"."PolicyType" is '險種';
comment on column "TxAmlRatingAppl"."InsuranceCurrency" is '保費幣別/申貸幣別';
comment on column "TxAmlRatingAppl"."InsuranceAmount" is '保費(原幣)/申貸金額';
comment on column "TxAmlRatingAppl"."AddnCd" is '額外風險因子';
comment on column "TxAmlRatingAppl"."InsrStakesCd" is '受益人-被保險人與受益人無身分利害關係';
comment on column "TxAmlRatingAppl"."BnfcryNHdrGrpCd" is '受益人-受益人為非要保人之法人/團體';
comment on column "TxAmlRatingAppl"."AgentTradeCd" is '由代理人代理建立業務關係或交易(自然人、法人)';
comment on column "TxAmlRatingAppl"."FstPayerNHdrGrpCd" is '繳費人-首期保費繳費人為非要保人之法人/團體';
comment on column "TxAmlRatingAppl"."FstPayerNHdrStksCd" is '繳費人-首期保費繳費人與要保人間無利害關係';
comment on column "TxAmlRatingAppl"."FstPrmOvrseaAcctCd" is '繳費人-首期保費來自境外帳戶繳費';
comment on column "TxAmlRatingAppl"."TotalAmtCd" is '過往往來紀錄-所有有效保單累積總保價金(帳戶價值)(借款總額)';
comment on column "TxAmlRatingAppl"."DeclinatureCd" is '過往往來紀錄-曾遭拒保(拒絕核貸)';
comment on column "TxAmlRatingAppl"."FstInsuredCd" is '過往往來紀錄-首次投保(借款)';
comment on column "TxAmlRatingAppl"."TWAddrHoldCd" is '地緣關係-自然人(借款人)要保人為外國人在台無居住地址/通訊地址';
comment on column "TxAmlRatingAppl"."TWAddrLegalCd" is '地緣關係-外國法人客戶在台澎金馬範圍無營業地址/通訊地址';
comment on column "TxAmlRatingAppl"."DurationCd" is '存續狀態-AH2';
comment on column "TxAmlRatingAppl"."SpecialIdentity" is '特殊身分';
comment on column "TxAmlRatingAppl"."LawForceWarranty" is '法令規定強制投保之保單及其附屬保險';
comment on column "TxAmlRatingAppl"."MovableGrnteeCd" is '擔保品-是否提供動產擔保';
comment on column "TxAmlRatingAppl"."BearerScursGrnteeCd" is '擔保品-是否提供無記名有價證券擔保';
comment on column "TxAmlRatingAppl"."AgreeDefaultFineCd" is '清償方案-自然人未約定「提前清償違約金」（不綁約）';
comment on column "TxAmlRatingAppl"."NonBuyingRealEstateCd" is '資金用途-資金用途非為購置不動產';
comment on column "TxAmlRatingAppl"."NonStkHolderGrnteeCd" is '保證-非利害關係人提供保證';
comment on column "TxAmlRatingAppl"."ReachCase" is '多次借款-個人/法人申貸案件達一定筆數以上（含本次貸款申請）';
comment on column "TxAmlRatingAppl"."AccountTypeCd" is '受款帳戶-受款帳戶「非」本人或本人代償金融機構帳戶或履約保證或不動產出賣人帳戶';
comment on column "TxAmlRatingAppl"."QueryType" is '區別要保人異動為暫時資料/確定資料';
comment on column "TxAmlRatingAppl"."IdentityCd" is '身份別';
comment on column "TxAmlRatingAppl"."RspRequestId" is 'Request Id';
comment on column "TxAmlRatingAppl"."RspStatus" is '狀態';
comment on column "TxAmlRatingAppl"."RspStatusCode" is '狀態代碼';
comment on column "TxAmlRatingAppl"."RspStatusDesc" is '狀態說明';
comment on column "TxAmlRatingAppl"."RspUnit" is '查詢單位';
comment on column "TxAmlRatingAppl"."RspTransactionId" is 'AML 交易序號';
comment on column "TxAmlRatingAppl"."RspAcctNo" is '保單號碼';
comment on column "TxAmlRatingAppl"."RspCaseNo" is '案號';
comment on column "TxAmlRatingAppl"."RspInsurCount" is '投保次數';
comment on column "TxAmlRatingAppl"."RspTotalRatingsScore" is '分數';
comment on column "TxAmlRatingAppl"."RspTotalRatings" is '總評級(WLF+CDD)';
comment on column "TxAmlRatingAppl"."CreateDate" is '建檔日期時間';
comment on column "TxAmlRatingAppl"."CreateEmpNo" is '建檔人員';
comment on column "TxAmlRatingAppl"."LastUpdate" is '最後更新日期時間';
comment on column "TxAmlRatingAppl"."LastUpdateEmpNo" is '最後更新人員';
