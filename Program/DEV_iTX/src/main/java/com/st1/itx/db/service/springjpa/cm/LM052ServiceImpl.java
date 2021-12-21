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
public class LM052ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	@SuppressWarnings({ "unchecked" })
	public List<Map<String, String>> findAll(TitaVo titaVo, int formNum) throws Exception {
		this.info("lM052.findAll");

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

		calendar.set(iYear, iMonth - 1, 0);

		String lyymm = String.valueOf(Integer.valueOf(dateFormat.format(calendar.getTime())) / 100);

		this.info("yymm=" + iYearMonth + ",lyymm=" + lyymm);

		String sql = " ";
		if (formNum == 1) {

			sql += "	SELECT \"AssetClassNo\"";
			sql += "          ,\"AcSubBookCode\"";
			sql += "          ,\"LoanBal\"";
			sql += "    FROM \"MonthlyLM052AssetClass\"";
			sql += "    WHERE \"YearMonth\" = :yymm ";
			sql += "    ORDER BY \"AssetClassNo\"";
			sql += "   			,\"AcSubBookCode\"";
		} else if (formNum == 2) {

			// 此年月為上個月
			iYearMonth = lyymm;

			sql += "	SELECT DECODE(\"AssetClassNo\",'11','1','12','1',\"AssetClassNo\") AS \"AssetClassNo\"";
			sql += "          ,SUM(\"LoanBal\") AS \"LoanBal\"";
			sql += "    FROM \"MonthlyLM052AssetClass\"";
			sql += "    WHERE \"YearMonth\" = :yymm ";
			sql += "    GROUP BY DECODE(\"AssetClassNo\",'11','1','12','1',\"AssetClassNo\")";
			sql += "    ORDER BY \"AssetClassNo\"";

		} else if (formNum == 3) {

			sql += "	SELECT \"OvduNo\"";
			sql += "          ,\"AcctCode\"";
			sql += "          ,\"LoanBal\"";
			sql += "    FROM \"MonthlyLM052Ovdu\"";
			sql += "    WHERE \"YearMonth\" = :yymm ";
			sql += "      AND \"OvduNo\" IN ('1','2','3')";
			sql += "    ORDER BY \"OvduNo\"";
			sql += "   			,\"AcctCode\"";

		} else if (formNum == 4) {

			sql += "	SELECT \"LoanAssetCode\"";
			sql += "          ,\"LoanBal\"";
			sql += "    FROM \"MonthlyLM052LoanAsset\"";
			sql += "    WHERE \"YearMonth\" = :yymm ";
			sql += "    ORDER BY \"LoanAssetCode\"";

		} else if (formNum == 5) {
			// 目前無科目，等確認有科目再寫
			sql += "	SELECT SUM(\"DbAmt\" - \"CrAmt\" ) AS \"LoanBal\"";
			sql += "    FROM \"AcMain\"";
			sql += "    WHERE \"MonthEndYm\" = :yymm ";
			sql += "      AND \"AcNoCode\" IN ('10621301000','10621302000')";

		}

		this.info("sql" + formNum + "=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("yymm", iYearMonth);
		return this.convertToMap(query);
	}

}