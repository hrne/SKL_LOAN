update "CdCode"
set "Item" = '資料修正'
where "DefCode" = 'TelChgRsnCode' and "Code" = '02'

delete "CdCode"
where "DefCode" = 'TelTypeCode' and "Code" = '06'