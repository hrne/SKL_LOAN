ALTER TABLE "MlaundryDetail" MODIFY "EmpNoDesc" NVARCHAR2(200);
ALTER TABLE "MlaundryDetail" MODIFY "ManagerDesc" NVARCHAR2(200);
ALTER TABLE "MlaundryDetail" DROP COLUMN "FlEntdy";
ALTER TABLE "MlaundryDetail" DROP COLUMN "FlowNo";
