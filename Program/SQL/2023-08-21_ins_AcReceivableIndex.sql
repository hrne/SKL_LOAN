
create index "AcReceivable_Index1" on "AcReceivable"("ClsFlag" asc, "BranchNo" asc, "CurrencyCode" asc, "AcNoCode" asc, "AcSubCode" asc, "AcDtlCode" asc, "CustNo" asc, "FacmNo" asc, "RvNo" asc);

create index "AcReceivable_Index2" on "AcReceivable"("ClsFlag" asc, "CustNo" asc, "AcctFlag" asc, "FacmNo" asc, "AcDtlCode" asc, "RvNo" asc);

create index "AcReceivable_Index3" on "AcReceivable"("AcctCode" asc, "ClsFlag" asc, "CustNo" asc, "FacmNo" asc, "RvNo" asc);

create index "AcReceivable_Index4" on "AcReceivable"("AcctCode" asc, "CustNo" asc, "FacmNo" asc, "ClsFlag" asc, "RvNo" asc);
