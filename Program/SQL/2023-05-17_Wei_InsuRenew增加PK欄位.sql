alter table "InsuRenew" drop constraint "InsuRenew_PK";
alter table "InsuRenew" add constraint "InsuRenew_PK" primary key("ClCode1", "ClCode2", "ClNo", "PrevInsuNo", "EndoInsuNo", "InsuYearMonth");