ALTER TABLE "FacMain" ADD "Grcd" VARCHAR2(1);

ALTER TABLE "FacMain" ADD "GrKind" VARCHAR2(1);

ALTER TABLE "FacMain" ADD "EsGcd" VARCHAR2(1);

ALTER TABLE "FacMain" ADD "EsGKind" VARCHAR2(1);

ALTER TABLE "FacMain" ADD "EsGcnl" VARCHAR2(1);

comment on column "FacMain"."Grcd" is '���«H���O';
comment on column "FacMain"."GrKind" is '����X���O';
comment on column "FacMain"."EsGcd" is '�����Z�ĳs���«H���O';
comment on column "FacMain"."EsGKind" is '�����Z�ĳs���«H���O';
comment on column "FacMain"."EsGcnl" is '�����Z�ĳs���«H���w����������F���q��';