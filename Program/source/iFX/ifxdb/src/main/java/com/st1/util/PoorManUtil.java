package com.st1.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Set;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PoorManUtil {
	static final Logger logger = LoggerFactory.getLogger(PoorManUtil.class);

	public static java.sql.Date stringToSqldate(String sDate) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		return new java.sql.Date(format.parse(sDate).getTime());

	}

	public static String getToday() {
		return getNowwithFormat("yyyyMMdd");
	}

	public static String getHHmmss() {
		return getNowwithFormat("HHmmss");
	}

	public static String getNow() {
		return getNowwithFormat("yyyyMMdd_HHmmss");
	}

	public static String getNowwithFormat(String fmt) {

		SimpleDateFormat sdf = new SimpleDateFormat(fmt);
		Calendar c1 = Calendar.getInstance(); // today
		return sdf.format(c1.getTime());
	}

	public static Date daysAgo(int days) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.add(Calendar.DATE, -days);
		return gc.getTime();
	}

	@SuppressWarnings("rawtypes")
	public static String mapToJSON(HashMap map) throws Exception {
		StringWriter writer = new StringWriter();
		ObjectMapper m = new ObjectMapper();
		m.writeValue(writer, map);
		return writer.toString();

	}

	public static void dumpMap(HashMap map) {
		Set keys = map.keySet();
		for (Object o : keys) {
			logger.info(o.toString() + "=" + map.get(o));
		}
	}

	@SuppressWarnings("unchecked")
	public static void aa() {
		HashMap m = new HashMap();
		m.put("a", "aa");
	}

	public static byte[] compress(String str) throws IOException {
		if (str == null || str.length() == 0) {
			return new byte[0];
		}
		logger.debug("compressing String length : " + str.length());
		logger.info("before compress:" + str);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		GZIPOutputStream gzip = new GZIPOutputStream(out);
		gzip.write(str.getBytes("UTF-8"));
		gzip.close();
		byte[] bb = out.toByteArray();
		logger.debug("compressed size:" + bb.length);
		logger.info("test decompress:" + decompress(bb));
		return bb;
		// String outStr = out.toString("ISO-8859-1");
		// logger.info("compressed Output String lenght : " +
		// outStr.length());
		// return outStr;
	}

	public static String decompress(byte[] bytes) throws IOException {
		if (bytes == null || bytes.length == 0) {
			return "";
		}
		logger.debug("decompress size:" + bytes.length);
		GZIPInputStream gis = new GZIPInputStream(new ByteArrayInputStream(bytes));
		BufferedReader bf = new BufferedReader(new InputStreamReader(gis, "UTF-8"));
		String outStr = "";
		String line;
		while ((line = bf.readLine()) != null) {
			outStr += line;
		}
		logger.debug("decompressed Output String lenght : " + outStr.length());
		return outStr;
	}

	public static void main(String... aArgs) throws Exception {

		String archive = "{'sysvar':{'RADD':'0261','CHKDG':'0','SEQNO':'90','STAT':'0','EMPNM':' 蔡坤能     ','CASH':'0','EMPNO':'505261','FDATE':'20110103','ABRN':'0000','BNAM':'                              ','FINBRNO':'6110','OADD':'0000','OFFLINELOG':'0','LOOP':'','DEPT':'0','TLRNO':'00261','C12_WARN':'W0100:      密碼超過三個月未更改，請先變更密碼　　　　　　　　　　　　 ','RBRN':'5050','PAGEMODE':'0','BURCD':'00','C12_TEXT':'300100000000000000000000000 蔡坤能     5052610','OBRN':'0000','NTXCD':'0','ROLE':'','POUNDSIGN':'$','NDAYMOD':'0','INQPRT':'50','TRNMOD':'0','MCRR':'00000','NSEQ':'00000000','BIOEC':'0','CLINE':'0','LOOPHEIGHT':'1','INQHD':'14','RELNO':'0000000000000000','NTXBUF3':'','NTXBUF4':'','FXLVL':'1','NTXBUF1':'','NTXBUF2':'','AUTOCONT':'0','CHAIN':'0','NTXBUF5':'','RELFG':'0','INQREC':'16','INQLEN':'48','BCURCD':'00','REFNO':'','REMOTESUPER':'0000','APKIND':'00100000000000000000000000','RQSP':'0000','BYOCCURS':'0','bank':'萬事宜銀行1','BRLVL':'04','TIME':'15581358','TSKID':'GI','RQSP3':'0000','RQSP2':'0000','COPYLN':'0','RQSP1':'0000','RQSP7':'0000','RQSP6':'0000','RBKNO':'5050','RQSP5':'0000','RQSP4':'0000','RQSP9':'0000','RQSP8':'0000','LOOPCNT':'0','SBTMOD':'0','bank3':'萬事宜地下銀樓','bank2':'萬事宜商業銀行','ATTACH_TEXT':'1                              00005050ABAB001000004505061100190','FKEY':'0','ADD':'0261','ONOFF':'1','OBUBRNO':'0190','LINE':'0','FXBRNO':'5050','TXFORM':'00000','TC':'1','NTXBUF':'','RESTR':'0','ONSEQ':'00000000','TXCODE':'00000','RENO':'0','PAGE':'0','WRTMSR':'0','RBRNO':'0000','SLEVEL':'3','SUPNM':'            ','SUPNO':'00000','BRN':'5050','DATE':'01000103','NETXDAYFG':'0','TASKID':'FU','EBRN':'ABAB','TITLE':'','PASSCD$':'0','OBUCD$':'0','CHOPCD$':'0','__title':'G0220 ','DRELCD$':'0','ORELCD$':'0','DBUCD$':'1','HODECD$':'0'},'fields':{'#SYSDATE':{'v':'01000103','a':'HE'},'#SYSDATEF':{'v':'20110103','a':'HE'},'#BNAM':{'v':'                  ','a':'HE'},'#EMPNM':{'v':' 蔡坤能 ','a':'HE'},'#FKEY':{'v':'0','a':'HE'},'#SLEVEL':{'v':'3','a':'HE'},'#FINBRNO':{'v':'6110','a':'HE'},'#OBUBRNO':{'v':'0190','a':'HE'},'#FXBRNO':{'v':'5050','a':'HE'},'#KINBR':{'v':'5050','a':'HE'},'#TRMSEQ':{'v':'0261','a':'HE'},'#TXTNO':{'v':'00000000','a':'HE'},'#HCODE':{'v':'0','a':'HE'},'#TLRNO':{'v':'00261','a':'HE'},'#SUPNO':{'v':'00000','a':'HE'},'#EMPNOT':{'v':'505261','a':'HE'},'#EMPNOS':{'v':'','a':'HE'},'#TTSKID':{'v':'GI','a':'HE'},'#TRMTYP':{'v':'00','a':'HE'},'#TXCD':{'v':'G0230','a':'HE'},'#DSCPT':{'v':'','a':'HE'},'#MRKEY':{'v':'','a':'HE'},'#CRDB':{'v':'0','a':'HE'},'#NBCD':{'v':'0','a':'HE'},'#TRNMOD':{'v':'0','a':'HE'},'#SBTMOD':{'v':'0','a':'HE'},'#CURCD':{'v':'0','a':'HE'},'#TXAMT':{'v':'0','a':'HE'},'#TOTAFG':{'v':'0','a':'HE'},'#SEQFG':{'v':'0','a':'HE'},'#AUTOFG':{'v':'0','a':'HE'},'#RESULT':{'v':'T','a':'HE'},'#FORCE':{'v':'0','a':'HE'},'#PSUEDO':{'v':'0','a':'HE'},'#PURE':{'v':'0','a':'HE'},'#DOCFG':{'v':'','a':'HE'},'#ACFLG':{'v':'','a':'HE'},'#WARNFG':{'v':'0','a':'HE'},'#HFLAG':{'v':'0','a':'HE'},'#MCSCNT':{'v':'0000','a':'HE'},'#MRLDRY':{'v':'0','a':'HE'},'#SUBFLAG':{'v':'0','a':'HE'},'#BOXCNT':{'v':'00','a':'HE'},'#S6CNT':{'v':'00','a':'HE'},'#SYSFIL6':{'v':'','a':'HE'},'#TBSDY':{'v':'20110103','a':'HE'},'#ECKIN':{'v':'5050','a':'HE'},'#ECTRM':{'v':'0261','a':'HE'},'#ECTNO':{'v':'00000000','a':'HE'},'#RELNO':{'v':'0','a':'HE'},'#RETURN':{'v':'0','a':'HE'},'#TOTSEQ':{'v':'0','a':'HE'},'#OBUFG':{'v':'0','a':'HE'},'#RBRNO':{'v':'5050','a':'HE'},'#SECNO':{'v':'0','a':'HE'},'#CIFKEY':{'v':'','a':'HE'},'#CIFERR':{'v':'','a':'HE'},'#ACTFG':{'v':'0','a':'HE'},'#RELCD':{'v':'0','a':'HE'},'#TRCNT':{'v':'000000','a':'HE'},'#CMPNO':{'v':'    ','a':'HE'},'#MTTPSEQ':{'v':'00','a':'HE'},'#BRELCD':{'v':'0','a':'HE'},'#BCURCD':{'v':'0','a':'HE'},'#FBRNO':{'v':'0','a':'HE'},'#CIFENAME':{'v':'','a':'HE'},'#PSWD':{'v':'0','a':'HE'},'#FXLVL':{'v':'1','a':'HE'},'#PSCD':{'v':'','a':'HE'},'#APCMFL48':{'v':'','a':'HE'},'#ROWSEQ':{'v':'0','a':'HE'},'#INQHD':{'v':'14','a':'HE'},'#INQLEN':{'v':'48','a':'HE'},'#INQPRT':{'v':'50','a':'HE'},'#INQREC':{'v':'16','a':'HE'},'#LOOPH':{'v':'1','a':'HE'},'#aa1':{'v':'','a':'hE'},'#stat1':{'v':'','a':'HE'},'#aa2':{'v':'','a':'hE'},'#IIRTKD2':{'v':'06','a':'hE'},'#CIRTKD2':{'v':'台幣活存','a':'hE'},'#bb1':{'v':'10','a':'hE'},'#bb2':{'v':'','a':'hE'},'#IIRTKD':{'v':'03','a':'hE'},'#CIRTKD':{'v':'台幣可轉讓定期存單','a':'hE'},'#cc1':{'v':'030','a':'hE'},'#cc2':{'v':'444','a':'hE'},'#checkPoint':{'v':'','a':'HE'},'#rqsp':{'v':'0000','a':'HE'},'#SYSTIME':{'v':'0','a':'HE'},'#SYSTIME6':{'v':'0','a':'HE'},'#SUPNM':{'v':'','a':'HE'},'#OIRTKD':{'v':'0','a':'HE'},'#COIRTKD':{'v':'','a':'HE'},'#OKIRTKD':{'v':'0','a':'HE'},'#OKIRTCD':{'v':'0','a':'HE'},'#OKTCNT':{'v':'0','a':'HE'},'#OOTCNT1':{'v':'','a':'HE'},'#nouse':{'v':'','a':'HE'},'#nouse2':{'v':'','a':'HE'},'#LOOP':{'v':'0','a':'HE'},'#OOTCNT':{'v':'0','a':'HE'},'#OAMT1':{'v':'0','a':'HE'},'#REMNAM':{'v':'','a':'HE'},'#OAMT2':{'v':'0','a':'HE'},'#url':{'v':'','a':'HE'},'#url1':{'v':'','a':'HE'},'#url2':{'v':'','a':'HE'},'#OIRTCD1':{'v':'0','a':'HE'},'#OIRTNM1':{'v':'','a':'HE'},'#buf':{'v':'','a':'HE'},'#bind-Btn1':{'v':'','a':'HE'},'#it1':{'v':'','a':'He'},'#it2':{'v':'','a':'He'},'#it3':{'v':'','a':'He'},'#it4':{'v':'','a':'He'},'#it5':{'v':'','a':'He'},'#it6':{'v':'','a':'He'},'#it7':{'v':'','a':'He'},'#it8':{'v':'','a':'He'}},'etc':{'hide':[],'currentIndex':13}}";
		byte[] bb = PoorManUtil.compress(archive);
		logger.info("archive:" + archive.length());
		logger.info("bytes:" + bb.length);
		String t2 = PoorManUtil.decompress(bb);
		logger.info(t2);
		logger.info("t2:" + archive.length());
	}
}
