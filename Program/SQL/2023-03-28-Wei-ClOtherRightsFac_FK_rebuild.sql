alter table "ClOtherRightsFac" add constraint "ClOtherRightsFac_ClOtherRights_FK1" foreign key ("ClCode1", "ClCode2", "ClNo", "Seq") references "ClOtherRights" ("ClCode1", "ClCode2", "ClNo", "Seq") on delete cascade;

alter table "ClOtherRightsFac" add constraint "ClOtherRightsFac_FacCaseAppl_FK2" foreign key ("ApproveNo") references "FacCaseAppl" ("ApplNo") on delete cascade;
