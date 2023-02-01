------------1------------   �\��T�{���T
select 
    lbm."CustNo" as �ḹ
   ,lbm."FacmNo" as �B�׽s��
   ,lbm."BormNo" as ���ڧǸ�
   ,lbm."DrawdownDate" as ���ڤ��
   ,lbm."DrawdownAmt" as ���ڪ��B
   ,cdl."Item" as �����O
   ,fm."CompensateFlag" as �N�v�X
from "LoanBorMain" lbm
left join "FacMain" fm on fm."CustNo" = lbm."CustNo" and fm."FacmNo" = lbm."FacmNo"
left join "CdCode" cdl on cdl."DefCode" = 'DepartmentCode' and cdl."Code" = fm."DepartmentCode"
where lbm."RenewFlag" =0
  and lbm."DrawdownDate" BETWEEN 20220401 AND 20220729
order by     lbm."CustNo" ,lbm."FacmNo" ,lbm."BormNo" ,lbm."DrawdownDate" 
;
------------2------------
2.�Y�϶��i���ץ� - ��J���ڤ�϶��d��			��ܤḹ	�B��	���ڧǸ�	���ڤ��	���ڪ��B	�����O	�N�v�X

WITH rn AS (
select "CustNo","NewFacmNo","NewBormNo"
FROM "AcLoanRenew"
WHERE "NewFacmNo" = "OldFacmNo"
    AND "AcDate" BETWEEN 20220401 AND 20220729
GROUP BY "CustNo","NewFacmNo","NewBormNo"
ORDER BY "CustNo","NewFacmNo","NewBormNo")
select 
    lbm."CustNo" as �ḹ
   ,lbm."FacmNo" as �B�׽s��
   ,lbm."BormNo" as ���ڧǸ�
   ,lbm."DrawdownDate" as ���ڤ��
   ,lbm."DrawdownAmt" as ���ڪ��B
   ,cdl."Item" as �����O
   ,fm."CompensateFlag" as �N�v�X
from rn
left join "LoanBorMain" lbm on lbm."CustNo" = rn."CustNo" 
                            AND lbm."FacmNo" = rn."NewFacmNo" 
                            AND lbm."BormNo" = rn."NewBormNo" 
left join "FacMain" fm 
    on fm."CustNo" = lbm."CustNo" 
    and fm."FacmNo" = lbm."FacmNo"
left join "CdCode" cdl 
    on cdl."DefCode" = 'DepartmentCode' 
    and cdl."Code" = fm."DepartmentCode"
order by     lbm."CustNo" ,lbm."FacmNo" ,lbm."BormNo" ,lbm."DrawdownDate" 
;
  
------------3------------   
3.�Y�϶����ܮץ� - ��J�|�p��϶��d��			��ܤḹ	�����ܧ�����


select 
    ari."CustNo" as �ḹ
   ,Json_VALUE(ari."JsonFields",'$.ContractChgCode') as �����ܧ�����
   ,cdl."Item" as �����ܧ󤤤�
from "AcReceivable" ari
left join "CdCode" cdl 
  on cdl."DefCode" = 'ChangeItemCode' 
  and cdl."Code" = Json_VALUE(ari."JsonFields",'$.ContractChgCode')
where ari."AcctCode"= 'F29'  
  and ari."OpenAcDate" BETWEEN 20220401 AND 20220729
  and Json_VALUE(ari."JsonFields",'$.ContractChgCode') = '01'
order by ari."CustNo"
;

------------4------------   �\��T�{���T
select 
    fac."CustNo" as �ḹ
   ,fac."FacmNo" as �B�׽s��
   ,fac."EntryDate" as �J�b��
   ,fac."CloseReasonCode" as ���e�M�v��]
   ,cdl."Item" as ���e�M�v��]����
from "FacClose" fac
left join "CdCode" cdl on cdl."DefCode" = 'AdvanceCloseCode' and cdl."Code" = fac."CloseReasonCode"
where fac."CloseDate" > 0
  and fac."EntryDate" BETWEEN 20220401 AND 20220729
order by fac."CustNo" ,fac."FacmNo" ,fac."EntryDate"
;