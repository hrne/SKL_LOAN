
    ALTER TABLE "FacClose" ADD "TelNo3" VARCHAR2(15) NULL;
comment on column "FacClose"."TelNo3" is '連絡電話3';


    ALTER TABLE "FacClose" ADD "CloseInd" varchar2(1) NULL;
comment on column "FacClose"."CloseInd" is '結案區分';