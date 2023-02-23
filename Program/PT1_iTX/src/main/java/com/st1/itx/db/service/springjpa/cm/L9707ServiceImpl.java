package com.st1.itx.db.service.springjpa.cm;

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
public class L9707ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public List<Map<String, String>> findAll(TitaVo titaVo, String StartDate, String EndDate) throws Exception {
		String startDate = String.valueOf((Integer.valueOf(StartDate) + 19110000));
		this.info("StartDate = " + StartDate + "    EndDate = " + EndDate);
		String endDate = String.valueOf((Integer.valueOf(EndDate) + 19110000));
		this.info("l9707.findAll");
		String sql = "";
		sql += " SELECT A.\"ApplDate\" ";
		sql += "       ,A.\"ApproveDate\" ";
		sql += "       ,F.\"CustNo\" ";
		sql += "       ,F.\"FacmNo\" ";
		sql += "       ,F.\"LineAmt\" ";
		sql += "       ,F.\"UtilAmt\" ";
		sql += "       ,F.\"UtilBal\" ";
		sql += "       ,M.\"YearMonth\" ";
		sql += "       ,M.\"OvduDays\" ";
		sql += "       ,M.\"PrevIntDate\" ";
		sql += "       ,CASE WHEN NVL(M.\"DueDate\", 0) != 0 ";
		sql += "             THEN M.\"DueDate\" ";
		sql += "             WHEN M.\"NextIntDate\" > M.\"YearMonth\" * 100 ";
		sql += "             THEN M.\"NextIntDate\" ";

		// 避免兩日期中有非正確格式的情況
		sql += "             WHEN M.\"NextIntDate\" > 9999999 AND M.\"YearMonth\" > 99999";
		// 此時已確定NextIntDate的年月 < YearMonth
		//
		// 將NextIntDate轉為DATE，然後計算NextIntDate與YearMonth之間差幾個月（ABS+CEIL），
		// 接著將NextIntDate加上此月差後，
		// 轉回NUMBER，即為新的下次應繳日。
		// 不考慮月份日數差異時，應該會是 YearMonth的YYYYMM || NextIntDate的DD。
		// 考慮時，如20210531/202106，Oracle SQL的邏輯會得出20210630。
		//
		// 但這是月曆日，如果要考慮會計日，可能需要等MonthlyFacBal.DueDate邏輯完善。
		//
		// 2021.08.09 xiangwei
		sql += "             THEN TO_NUMBER(TO_CHAR(ADD_MONTHS(TO_DATE(TO_CHAR(M.\"NextIntDate\"), 'YYYYMMDD'), CEIL(ABS(MONTHS_BETWEEN(TO_DATE(TO_CHAR(M.\"NextIntDate\"), 'YYYYMMDD'), TO_DATE(TO_CHAR(M.\"YearMonth\"), 'YYYYMM'))))), 'YYYYMMDD')) ";
		sql += "        ELSE M.\"NextIntDate\" END \"NextIntDate\" ";
		sql += "       ,CASE WHEN M.\"YearMonth\" IS NULL ";
		sql += "             THEN 0 ";
		sql += "        ELSE M.\"PrinBalance\" END \"UnpaidAmt\"";
		sql += "       ,C.\"EntCode\" ";
		sql += "       ,A.\"PieceCode\" ";
		sql += " FROM \"FacCaseAppl\" A ";
		sql += " LEFT JOIN \"FacMain\" F ON F.\"ApplNo\" = A.\"ApplNo\" ";
		sql += " LEFT JOIN \"MonthlyFacBal\" M ON M.\"CustNo\" = F.\"CustNo\" ";
		sql += "                              AND M.\"FacmNo\" = F.\"FacmNo\" ";
		sql += "                              AND M.\"OvduDays\" > 0";
		sql += " LEFT JOIN \"CustMain\" C ON C.\"CustNo\" = F.\"CustNo\" ";
		sql += " WHERE A.\"ApproveDate\" BETWEEN :StartDate AND :EndDate ";
		sql += " ORDER BY NVL(M.\"YearMonth\", 0) ASC";
		sql += "         ,F.\"CustNo\" ASC";
		sql += "         ,F.\"FacmNo\" ASC";

		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);

		query.setParameter("StartDate", startDate);
		query.setParameter("EndDate", endDate);

		return this.convertToMap(query);
	}

}