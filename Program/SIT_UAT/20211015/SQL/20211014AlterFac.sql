
    ALTER TABLE "FacProd" ADD "EnterpriseFg" VARCHAR(1)  NULL ;
    comment on column "FacProd"."EnterpriseFg" is '企金可使用記號';

    ALTER TABLE "FacCaseAppl" ADD "DepartmentCode" VARCHAR(1)  NULL ;
    comment on column "FacCaseAppl"."DepartmentCode" is '案件隸屬單位';