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

		int iEntdy = Integer.valueOf(titaVo.get("ENTDY")) + 19110000;
		int iYear = (Integer.valueOf(titaVo.get("ENTDY")) + 19110000) / 10000;
		int iMonth = ((Integer.valueOf(titaVo.get("ENTDY")) + 19110000) / 100) % 100;

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		// 當日
		int nowDate = Integer.valueOf(iEntdy);
		Calendar calMonthDate = Calendar.getInstance();
		// 設當年月底日 0是月底
		calMonthDate.set(iYear, iMonth, 0);

		int thisMonthEndDate = Integer.valueOf(dateFormat.format(calMonthDate.getTime()));

		boolean isMonthZero = iMonth - 1 == 0;

		if (nowDate < thisMonthEndDate) {
			iYear = isMonthZero ? (iYear - 1) : iYear;
			iMonth = isMonthZero ? 12 : iMonth - 1;
		}

		int yymm = (iYear * 100) + iMonth;

		calMonthDate.set(iYear, iMonth - 1, 0);

		int lyymm = Integer.valueOf(dateFormat.format(calMonthDate.getTime())) / 100;

		this.info("lM052.findAll yymm=" + yymm + ",lyymm="+lyymm);

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

			//此年月為上個月
			yymm = lyymm;
			
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

		}else if (formNum == 5) {
			//目前無科目，等確認有科目再寫
			sql += "	SELECT SUM(\"DbAmt\" - \"CrAmt\" ) AS \"LoanBal\"";
			sql += "    FROM \"AcMain\"";
			sql += "    WHERE \"MonthEndYm\" = :yymm ";
			sql += "      AND \"AcNoCode\" IN ('10621301000','10621302000')";
			
			
		}

		this.info("sql" + formNum + "=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("yymm", yymm);
		return this.convertToMap(query);
	}

}