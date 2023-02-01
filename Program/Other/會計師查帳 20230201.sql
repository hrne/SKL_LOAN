------------1------------   珮瑜確認正確
select 
    lbm."CustNo" as 戶號
   ,lbm."FacmNo" as 額度編號
   ,lbm."BormNo" as 撥款序號
   ,lbm."DrawdownDate" as 撥款日期
   ,lbm."DrawdownAmt" as 撥款金額
   ,cdl."Item" as 企金別
   ,fm."CompensateFlag" as 代償碼
from "LoanBorMain" lbm
left join "FacMain" fm on fm."CustNo" = lbm."CustNo" and fm."FacmNo" = lbm."FacmNo"
left join "CdCode" cdl on cdl."DefCode" = 'DepartmentCode' and cdl."Code" = fm."DepartmentCode"
where lbm."RenewFlag" =0
  and lbm."DrawdownDate" BETWEEN 20220401 AND 20220729
order by     lbm."CustNo" ,lbm."FacmNo" ,lbm."BormNo" ,lbm."DrawdownDate" 
;
------------2------------
2.某區間展期案件 - 輸入撥款日區間查詢			顯示戶號	額度	撥款序號	撥款日期	撥款金額	企金別	代償碼

WITH rn AS (
select "CustNo","NewFacmNo","NewBormNo"
FROM "AcLoanRenew"
WHERE "NewFacmNo" = "OldFacmNo"
    AND "AcDate" BETWEEN 20220401 AND 20220729
GROUP BY "CustNo","NewFacmNo","NewBormNo"
ORDER BY "CustNo","NewFacmNo","NewBormNo")
select 
    lbm."CustNo" as 戶號
   ,lbm."FacmNo" as 額度編號
   ,lbm."BormNo" as 撥款序號
   ,lbm."DrawdownDate" as 撥款日期
   ,lbm."DrawdownAmt" as 撥款金額
   ,cdl."Item" as 企金別
   ,fm."CompensateFlag" as 代償碼
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
3.某區間契變案件 - 輸入會計日區間查詢			顯示戶號	契約變更類型


select 
    ari."CustNo" as 戶號
   ,Json_VALUE(ari."JsonFields",'$.ContractChgCode') as 契約變更類型
   ,cdl."Item" as 契約變更中文
from "AcReceivable" ari
left join "CdCode" cdl 
  on cdl."DefCode" = 'ChangeItemCode' 
  and cdl."Code" = Json_VALUE(ari."JsonFields",'$.ContractChgCode')
where ari."AcctCode"= 'F29'  
  and ari."OpenAcDate" BETWEEN 20220401 AND 20220729
  and Json_VALUE(ari."JsonFields",'$.ContractChgCode') = '01'
order by ari."CustNo"
;

------------4------------   珮瑜確認正確
select 
    fac."CustNo" as 戶號
   ,fac."FacmNo" as 額度編號
   ,fac."EntryDate" as 入帳日
   ,fac."CloseReasonCode" as 提前清償原因
   ,cdl."Item" as 提前清償原因中文
from "FacClose" fac
left join "CdCode" cdl on cdl."DefCode" = 'AdvanceCloseCode' and cdl."Code" = fac."CloseReasonCode"
where fac."CloseDate" > 0
  and fac."EntryDate" BETWEEN 20220401 AND 20220729
order by fac."CustNo" ,fac."FacmNo" ,fac."EntryDate"
;