
    ALTER TABLE "PostAuthLog" ADD "LimitAmt" DECIMAL(8,2) DEFAULT 0 NOT NULL ;
    comment on column "PostAuthLog"."LimitAmt" is '每筆扣款限額';
    
    ALTER TABLE "PostAuthLogHistory" ADD "LimitAmt" DECIMAL(8,2) DEFAULT 0 NOT NULL ;
    comment on column "PostAuthLogHistory"."LimitAmt" is '每筆扣款限額';
    
    
    ALTER TABLE "PostAuthLog" ADD "AuthMeth" varchar2(1);
comment on column "PostAuthLog"."AuthMeth" is '授權方式';
    
