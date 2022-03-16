ALTER TABLE "SystemParas" ADD "JcicEmpName"  NVARCHAR2(4) ;  
ALTER TABLE "SystemParas" ADD "JcicEmpTel"   VARCHAR2(16) ;  
comment on column "SystemParas"."JcicEmpName" is 'JCIC放款報送聯絡人姓名';
comment on column "SystemParas"."JcicEmpTel" is 'JCIC放款報送聯絡人電話';
