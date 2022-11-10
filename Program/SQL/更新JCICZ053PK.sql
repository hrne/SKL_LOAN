alter table "JcicZ053" drop constraint "JcicZ053_PK";
alter table "JcicZ053" add constraint "JcicZ053_PK" primary key("SubmitKey", "CustId", "RcDate", "MaxMainCode", "ChangePayDate");
