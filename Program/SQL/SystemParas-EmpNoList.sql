alter table "SystemParas"
add ("EmpNoList" nvarchar2(150) );

comment on column "SystemParas"."EmpNoList" is '業績追回通知員工代碼清單'; 