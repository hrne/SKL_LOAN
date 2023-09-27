
update "TxTranCode" set "TranItem" = '公會報送作業明細表', "Desc"='公會報送作業明細表' where "TranNo"='L5735';
update "CdReport" set "FormName" = '公會報送作業明細表', "UsageDesc"='公會報送作業明細表' where "FormNo"='L5735';
update "TxTranCode" set "TranItem" = '公會報送作業明細表-催收戶', "Desc"='公會報送作業明細表-催收戶' where "TranNo"='L5736';
update "CdReport" set "FormName" = '公會報送作業明細表-催收戶', "UsageDesc"='公會報送作業明細表-催收戶' where "FormNo"='L5736';
delete from "TxAuthority" where "TranNo"='L5737';
delete from "TxTranCode" where "TranNo"='L5737';
commit;
