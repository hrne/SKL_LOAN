--Update CdReport: ��s������
update "CdReport"
set "FormNo" = 'L9141'
where "FormNo" = 'L9751';

--Update TxTranCode: ��s���
update "TxTranCode"
set "TranNo" = 'L9141', "SubMenuNo" = '1'
where "TranNo" = 'L9751';

--Update TxAuthority: ��s�s�ե���v��
update "TxAuthority"
set "TranNo" = 'L9141'
where "TranNo" = 'L9751'