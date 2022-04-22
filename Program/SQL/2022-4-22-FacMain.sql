ALTER TABLE "FacMain" ADD "Grcd" VARCHAR2(1);

ALTER TABLE "FacMain" ADD "GrKind" VARCHAR2(1);

ALTER TABLE "FacMain" ADD "EsGcd" VARCHAR2(1);

ALTER TABLE "FacMain" ADD "EsGKind" VARCHAR2(1);

ALTER TABLE "FacMain" ADD "EsGcnl" VARCHAR2(1);

comment on column "FacMain"."Grcd" is '綠色授信註記';
comment on column "FacMain"."GrKind" is '綠色支出類別';
comment on column "FacMain"."EsGcd" is '永續績效連結授信註記';
comment on column "FacMain"."EsGKind" is '永續績效連結授信類別';
comment on column "FacMain"."EsGcnl" is '永續績效連結授信約定條件全部未達成通報';