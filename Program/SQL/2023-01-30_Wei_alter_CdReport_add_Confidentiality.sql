alter table "CdReport" add "Confidentiality" VARCHAR2(1);
update "CdReport" set "Confidentiality" = '0';