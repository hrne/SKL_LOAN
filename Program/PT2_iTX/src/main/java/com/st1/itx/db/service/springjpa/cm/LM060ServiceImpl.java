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
	/**
	 * 執行報表輸出
	 * 
	 * @param titaVo
	 * @param yearMonth 西元年月
	 * 
	 */
	public List<Map<String, String>> findAll(TitaVo titaVo, int yearMonth) throws Exception {

		// 取得會計日(同頁面上會計日)
		// 年月日
//		int iEntdy = Integer.valueOf(titaVo.get("ENTDY")) + 19110000;
		// 年
		int iYear = yearMonth / 100;
		// 月
		int iMonth = yearMonth % 100;

		// 格式
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

		// 當前日期
//		int nowDate = Integer.valueOf(iEntdy);

		Calendar calendar = Calendar.getInstance();

		// 設當年月底日
		calendar.set(iYear, iMonth, 0);

		// 以當前月份取得月底日期 並格式化處理
		int thisMonthEndDate = Integer.valueOf(dateFormat.format(calendar.getTime()));

		this.info("thisMonthEndDate=" + thisMonthEndDate);

		String iYearMonth = String.valueOf((iYear * 100) + iMonth);
		
		// 設上個月底日
		calendar.set(iYear, iMonth - 1, 0);

		int lastMonthEndDate = Integer.valueOf(dateFormat.format(calendar.getTime()));

		this.info("lastMonthEndDate=" + lastMonthEndDate);
		
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
		sql += "			,SUM(\"F0\") + SUM(\"F1\") + SUM(\"F2\") - SUM(\"F3\") - SUM(\"F4\") AS F5";
		sql += "			,SUM(\"F6\") AS F6";
		sql += "			,SUM(\"F7\") AS F7";
		sql += "			,SUM(\"F6\") + SUM(\"F7\") AS F8";
		sql += "	  FROM(SELECT A.\"TdBal\" AS F0";
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
		sql += "				 ,0 F5";
		sql += "				 ,0 F6";
		sql += "				 ,0 F7";
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
		sql += "				 ,CASE";
		sql += "				    WHEN F.\"CloseDate\" = 0 AND F.\"OverdueDate\" = 0 ";
		sql += "				    THEN F.\"Fee\"";
		sql += "				   ELSE 0 END  AS F6";
		sql += "				 ,CASE";
		sql += "				    WHEN F.\"CloseDate\" = 0 AND F.\"OverdueDate\" > 0 ";
		sql += "				    THEN F.\"Fee\"";
		sql += "				   ELSE 0 END  AS F7";
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