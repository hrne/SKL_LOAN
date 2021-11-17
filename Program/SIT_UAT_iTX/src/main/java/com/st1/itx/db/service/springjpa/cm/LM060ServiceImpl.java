package com.st1.itx.db.service.springjpa.cm;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.ASpringJpaParm;
import com.st1.itx.db.transaction.BaseEntityManager;

@Service
@Repository
/* 逾期放款明細 */
public class LM060ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {

	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {

		// 取得會計日(同頁面上會計日)
		// 年月日
		int iEntdy = Integer.valueOf(titaVo.get("ENTDY")) + 19110000;
		// 年
		int iYear = (Integer.valueOf(titaVo.get("ENTDY")) + 19110000) / 10000;
		// 月
		int iMonth = ((Integer.valueOf(titaVo.get("ENTDY")) + 19110000) / 100) % 100;

		// 格式
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

		// 當前日期
		int nowDate = Integer.valueOf(iEntdy);

		Calendar calendar = Calendar.getInstance();

		// 設當年月底日
		// calendar.set(iYear, iMonth, 0);
		calendar.set(Calendar.YEAR, iYear);
		calendar.set(Calendar.MONTH, iMonth - 1);
		calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));

		// 以當前月份取得月底日期 並格式化處理
		int thisMonthEndDate = Integer.valueOf(dateFormat.format(calendar.getTime()));

		this.info("1.thisMonthEndDate=" + thisMonthEndDate);

		String[] dayItem = { "日", "一", "二", "三", "四", "五", "六" };
		// 星期 X (排除六日用) 代號 0~6對應 日到六
		int day = calendar.get(Calendar.DAY_OF_WEEK);
		this.info("day = " + dayItem[day - 1]);
		int diff = 0;
		if (day == 1) {
			diff = -2;
		} else if (day == 6) {
			diff = 1;
		}
		this.info("diff=" + diff);
		calendar.add(Calendar.DATE, diff);
		// 矯正月底日
		thisMonthEndDate = Integer.valueOf(dateFormat.format(calendar.getTime()));
		this.info("2.thisMonthEndDate=" + thisMonthEndDate);
		// 確認是否為1月
		boolean isMonthZero = iMonth - 1 == 0;

		// 當前日期 比 當月底日期 前面 就取上個月底日
		if (nowDate < thisMonthEndDate) {
			iYear = isMonthZero ? (iYear - 1) : iYear;
			iMonth = isMonthZero ? 12 : iMonth - 1;
		}

		String iYearMonth = String.valueOf((iYear * 100) + iMonth);
		
		// 設上個月底日
		calendar.set(iYear, iMonth - 1, 0);

		int lastMonthEndDate = Integer.valueOf(dateFormat.format(calendar.getTime()));

//		String iLYYMM = "";
//		
//		if (String.valueOf(iMonth).equals("1")) {
//			iLYYMM = String.valueOf(iYear - 1) + "12";
//		} else {
//			iLYYMM = iYear + String.format("%02d", iMonth - 1);
//		}

		this.info("lM060.findAll YYMM=" +iYearMonth + ",LYYMM=" + lastMonthEndDate / 100
				+ ",lday" + lastMonthEndDate);

//		F0 : 前期餘額               
//		F1 : 借方暫付款項--法務費用   
//		F2 : 借方催收款項--法務費用  
//		F3 : 貸方暫付款項--法務費用  
//		F4 : 貸方催收款項--法務費用  
//		F5 : 本日餘額               
//		F6 : 暫付款及待結轉帳__放款法務費用
//		F7 : 催收款項__法務費用
//		F8 : 餘額合計

		String sql = "SELECT SUM(\"F0\") AS F0";
		sql += "			,SUM(\"F1\") AS F1";
		sql += "			,SUM(\"F2\") AS F2";
		sql += "			,SUM(\"F3\") AS F3";
		sql += "			,SUM(\"F4\") AS F4";
		sql += "			,SUM(\"F5\") AS F5";
		sql += "			,SUM(\"F6\") AS F6";
		sql += "			,SUM(\"F7\") AS F7";
		sql += "			,SUM(\"F8\") AS F8";
		sql += "	  FROM(SELECT DECODE(A.\"MonthEndYm\",:lyymm,A.\"TdBal\",0) F0";
		sql += "				 ,0 F1";
		sql += "				 ,0 F2";
		sql += "				 ,0 F3";
		sql += "				 ,0 F4";
		sql += "				 ,0 F5";
		sql += "				 ,0 F6";
		sql += "				 ,0 F7";
		sql += "				 ,0 F8";
		sql += "		   FROM \"AcMain\" A";
		sql += "		   WHERE A.\"AcDate\" = :lday";
		sql += "		     AND A.\"MonthEndYm\" = :lyymm";
		sql += "		     AND A.\"AcctCode\" IN('F07','F24')";
		sql += "		   UNION ALL";
		sql += "	       SELECT 0 F0";
		sql += "				 ,DECODE(A.\"AcctCode\",'F07',A.\"DbAmt\" + A.\"CoreDbAmt\",0) F1";
		sql += "				 ,DECODE(A.\"AcctCode\",'F24',A.\"DbAmt\" + A.\"CoreDbAmt\",0) F2";
		sql += "				 ,DECODE(A.\"AcctCode\",'F07',A.\"CrAmt\" + A.\"CoreCrAmt\",0) F3";
		sql += "				 ,DECODE(A.\"AcctCode\",'F24',A.\"CrAmt\" + A.\"CoreCrAmt\",0) F4";
		sql += "				 ,DECODE(A.\"MonthEndYm\",:yymm,A.\"TdBal\",0) F5";
		sql += "				 ,0 F6";
		sql += "				 ,0 F7";
		sql += "				 ,0 F8";
		sql += "		   FROM \"AcMain\" A";
		sql += "		   WHERE TRUNC(A.\"AcDate\" / 100) = :yymm";
		sql += "		     AND A.\"AcctCode\" IN('F07','F24')";
		sql += "		   UNION ALL";
		sql += "		   SELECT 0 F0";
		sql += "				 ,FO.\"Fee\" F1";
		sql += "				 ,0 F2";
		sql += "				 ,0 F3";
		sql += "				 ,0 F4";
		sql += "				 ,0 F5";
		sql += "				 ,0 F6";
		sql += "				 ,0 F7";
		sql += "				 ,0 F8";
		sql += "		   FROM \"ForeclosureFee\" FO";
		sql += "		   WHERE TRUNC(FO.\"OpenAcDate\" / 100) = :yymm ";
		sql += "			 AND FO.\"FeeCode\" NOT IN ('11','15')";
		sql += "		   UNION ALL";
		sql += "		   SELECT 0 F0";
		sql += "				 ,0 F1";
		sql += "				 ,0 F2";
		sql += "				 ,0 F3";
		sql += "				 ,0 F4";
		sql += "				 ,0 F5";
		sql += "				 ,DECODE(F.\"CloseDate\",0,DECODE(F.\"OverdueDate\",0,DECODE(TRUNC(F.\"OpenAcDate\" / 100),:yymm,F.\"Fee\",0),0),0) F6";
		sql += "				 ,DECODE(F.\"CloseDate\",0,DECODE(F.\"OverdueDate\",0,0,F.\"Fee\"),0) F7";
		sql += "				 ,DECODE(F.\"CloseDate\",0,DECODE(F.\"OverdueDate\",0,DECODE(TRUNC(F.\"OpenAcDate\" / 100),:yymm,F.\"Fee\",0),0),0)";
		sql += "				 + DECODE(F.\"CloseDate\",0,DECODE(F.\"OverdueDate\",0,0,F.\"Fee\"),0) F8";
		sql += "		   FROM \"ForeclosureFee\" F)";

		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("yymm", iYearMonth);
		query.setParameter("lyymm", lastMonthEndDate / 100);
		query.setParameter("lday", lastMonthEndDate);
		return this.convertToMap(query);
	}

}