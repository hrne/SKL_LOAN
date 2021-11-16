alter table "InnDocRecord"
add ("JsonFields" nvarchar2(300));

comment on column "InnDocRecord"."JsonFields" is 'json格式紀錄欄';