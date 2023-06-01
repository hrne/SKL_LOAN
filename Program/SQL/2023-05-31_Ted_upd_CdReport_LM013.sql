update  "CdReport"  set "FormName"='金檢報表',"UsageDesc"='金檢報表' where "FormNo"='LM013';
Insert into "CdReport" ("FormNo","FormName","Cycle","SendCode","Letter","Message","Email","UsageDesc","Enable","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo","SignCode","WatermarkFlag","MessageFg","EmailFg","LetterFg","Confidentiality","ApLogFlag","GroupNo") 
values ('LM013-1','金檢報表(非關係自然人)',2,0,0,0,0,'金檢報表(非關係自然人)','Y',to_timestamp('04-8月 -21 04.00.27.000000000 下午','DD-MON-RR HH.MI.SSXFF AM'),'001718',to_timestamp('10-3月 -23 02.27.01.818000000 下午','DD-MON-RR HH.MI.SSXFF AM'),'001717',0,1,null,null,null,'1',1,'2');
Insert into "CdReport" ("FormNo","FormName","Cycle","SendCode","Letter","Message","Email","UsageDesc","Enable","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo","SignCode","WatermarkFlag","MessageFg","EmailFg","LetterFg","Confidentiality","ApLogFlag","GroupNo") 
values ('LM013-2','金檢報表(關係自然人)',2,0,0,0,0,'金檢報表(關係自然人)','Y',to_timestamp('04-8月 -21 04.00.27.000000000 下午','DD-MON-RR HH.MI.SSXFF AM'),'001718',to_timestamp('10-3月 -23 02.27.01.818000000 下午','DD-MON-RR HH.MI.SSXFF AM'),'001717',0,1,null,null,null,'1',1,'2');
Insert into "CdReport" ("FormNo","FormName","Cycle","SendCode","Letter","Message","Email","UsageDesc","Enable","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo","SignCode","WatermarkFlag","MessageFg","EmailFg","LetterFg","Confidentiality","ApLogFlag","GroupNo") 
values ('LM013-3','金檢報表(非關係法人)',2,0,0,0,0,'金檢報表(非關係法人)','Y',to_timestamp('04-8月 -21 04.00.27.000000000 下午','DD-MON-RR HH.MI.SSXFF AM'),'001718',to_timestamp('10-3月 -23 02.27.01.818000000 下午','DD-MON-RR HH.MI.SSXFF AM'),'001717',0,1,null,null,null,'1',1,'2');
Insert into "CdReport" ("FormNo","FormName","Cycle","SendCode","Letter","Message","Email","UsageDesc","Enable","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo","SignCode","WatermarkFlag","MessageFg","EmailFg","LetterFg","Confidentiality","ApLogFlag","GroupNo") 
values ('LM013-4','金檢報表(關係法人)',2,0,0,0,0,'金檢報表(關係法人)','Y',to_timestamp('04-8月 -21 04.00.27.000000000 下午','DD-MON-RR HH.MI.SSXFF AM'),'001718',to_timestamp('10-3月 -23 02.27.01.818000000 下午','DD-MON-RR HH.MI.SSXFF AM'),'001717',0,1,null,null,null,'1',1,'2');
commit;

