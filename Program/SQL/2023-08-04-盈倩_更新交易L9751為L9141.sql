--Update CdReport: 更新報表資料
update "CdReport"
set "FormNo" = 'L9141'
where "FormNo" = 'L9751';

--Update TxTranCode: 更新交易
update "TxTranCode"
set "TranNo" = 'L9141', "SubMenuNo" = '1'
where "TranNo" = 'L9751';

--Update TxAuthority: 更新群組交易權限
update "TxAuthority"
set "TranNo" = 'L9141'
where "TranNo" = 'L9751'