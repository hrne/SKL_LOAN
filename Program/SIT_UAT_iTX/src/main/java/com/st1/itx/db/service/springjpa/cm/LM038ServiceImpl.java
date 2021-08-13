package com.st1.itx.db.service.springjpa.cm;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
public class LM038ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {

		String lastdy = String.valueOf((Integer.valueOf(titaVo.get("ENTDY").toString()) + 19110000));

		lastdy = String.format("%s-%s-%s", lastdy.substring(0, 4), lastdy.substring(4, 6), lastdy.substring(6, 8));

		// 設定日期格式
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		// 進行轉換
		Date date = sdf.parse(lastdy);

		Calendar cal = Calendar.getInstance();

		cal.setTime(date);

		cal.add(Calendar.MONTH, -1);

		lastdy = sdf.format(cal.getTime());
		// xxxx-xx-xx
		lastdy = lastdy.substring(0, 4) + lastdy.substring(5, 7);

		String entdy = String.valueOf((Integer.valueOf(titaVo.get("ENTDY").toString()) + 19110000) / 100);
		this.info("entdy = " + entdy + "    lastdy = " + lastdy);
		this.info("lM038.findAll ");
		String sql = "";
		sql += " SELECT M.\"OvduTerm\"                               F0 ";
		sql += "       ,L.\"OvduTerm\"                               F1 ";
		sql += "       ,NVL(B.\"AcctSource\", '')                    F2 ";
		sql += "       ,M.\"CustNo\"                                 F3 ";
		sql += "       ,M.\"FacmNo\"                                 F4 ";
		sql += "       ,C.\"CustName\"                               F5 ";
		sql += "       ,M.\"PrevIntDate\"                            F6 ";
		sql += "       ,M.\"PrinBalance\"                            F7 ";
		sql += "       ,F.\"FirstDrawdownDate\"                      F8 ";
		sql += "       ,CASE WHEN NVL(M.\"DueDate\", 0) != 0 ";
		sql += "             THEN M.\"DueDate\"  ";
		sql += "             WHEN M.\"NextIntDate\" > M.\"YearMonth\" * 100 ";
		sql += "             THEN M.\"NextIntDate\"  ";
		// 避免兩日期中有非正確格式的情況
		sql += "             WHEN M.\"NextIntDate\" > 9999999 AND M.\"YearMonth\" > 99999 ";
		// 此時已確定NextIntDate的年月 < YearMonth
		//
		// 將NextIntDate轉為DATE，然後計算NextIntDate與YearMonth之間差幾個月（ABS+CEIL），
		// 接著將NextIntDate加上此月差後，
		// 轉回NUMBER，即為新的下次應繳日。
		// 不考慮月份日數差異時，應該會是YearMonth || NextIntDate的DD。
		// 考慮時，如20210531/202106，Oracle SQL的邏輯會得出20210630。
		//
		// 但這是月曆日，如果要考慮會計日，可能需要等MonthlyFacBal.DueDate邏輯完善。
		//
		// 2021.08.09 xiangwei
		sql += "             THEN TO_NUMBER(TO_CHAR(ADD_MONTHS(TO_DATE(TO_CHAR(M.\"NextIntDate\"),'YYYYMMDD'),CEIL(ABS(MONTHS_BETWEEN(TO_DATE(TO_CHAR(M.\"NextIntDate\"),'YYYYMMDD'), TO_DATE(TO_CHAR(M.\"YearMonth\"),'YYYYMM'))))),'YYYYMMDD')) ";
		sql += "             ELSE M.\"NextIntDate\"  ";
		sql += "        END                                        F9 ";
		sql += "       ,M.\"OvduDays\"                               F10 ";
		sql += "       ,M.\"UnpaidPrincipal\"                        F11 ";
		sql += "       ,M.\"UnpaidInterest\"                         F12 ";
		sql += "       ,M.\"UnpaidBreachAmt\" + M.\"UnpaidDelayInt\" F13 ";
		sql += "       ,M.\"UnpaidPrincipal\" + M.\"UnpaidInterest\" + M.\"UnpaidBreachAmt\" + M.\"UnpaidDelayInt\" - M.\"TempAmt\" F14 ";
		sql += "       ,F.\"CreditOfficer\"                          F15 ";
		sql += "       ,M.\"AccCollPsn\"                             F16 ";
		sql += "       ,M.\"CityCode\"                               F17 ";
		sql += "       ,M.\"ClCode1\"                                F18 ";
		sql += "       ,M.\"ClCode2\"                                F19 ";
		sql += "       ,CB.\"BdLocation\"                            F20 ";
		sql += "       ,F.\"MaturityDate\"                           F21 ";
		sql += "       ,M.\"AcctCode\"                               F22 ";
		sql += "       ,F.\"LineAmt\"                                F23 ";
		sql += "       ,CB.\"TotalFloor\"                            F24 ";
		sql += "       ,TO_NUMBER(F.\"UsageCode\")                   F25 ";
		sql += "       ,F.\"ProdNo\"                                 F26 ";
		sql += "       ,M.\"EntCode\"                                F27 ";
		sql += "       ,E.\"Fullname\"                               F28 ";
		sql += " FROM \"MonthlyFacBal\" M ";
		sql += " LEFT JOIN \"MonthlyFacBal\" L ON L.\"YearMonth\" = :lastdy ";
		sql += "                              AND L.\"CustNo\"    = M.\"CustNo\" ";
		sql += "                              AND L.\"FacmNo\"    = M.\"FacmNo\" ";
		sql += " LEFT JOIN \"FacMain\" F ON F.\"CustNo\" = M.\"CustNo\" ";
		sql += "                        AND F.\"FacmNo\" = M.\"FacmNo\" ";
		sql += " LEFT JOIN \"CdAcBook\" B ON B.\"AcSubBookCode\" = M.\"AcSubBookCode\" ";
		sql += " LEFT JOIN \"CustMain\" C ON C.\"CustNo\" = M.\"CustNo\" ";
		sql += " LEFT JOIN \"ClBuilding\" CB ON CB.\"ClCode1\" = M.\"ClCode1\" ";
		sql += "                            AND CB.\"ClCode2\" =  M.\"ClCode2\" ";
		sql += "                            AND CB.\"ClNo\"    =  M.\"ClNo\" ";
		sql += " LEFT JOIN \"CdEmp\" E ON E.\"EmployeeNo\" = M.\"AccCollPsn\" ";
		sql += " LEFT JOIN \"AcReceivable\" A ON A.\"CustNo\" = M.\"CustNo\" ";
		sql += "                             AND A.\"AcctCode\" = 'TAV' ";
		sql += " WHERE M.\"YearMonth\" = :entdy ";
		sql += "   AND M.\"Status\" IN (0, 4) ";
		sql += "   AND TRUNC(M.\"NextIntDate\" / 100) <= :entdy ";
		sql += "   AND TRUNC(M.\"PrevIntDate\" / 100) <> :entdy ";
		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("entdy", entdy);
		query.setParameter("lastdy", lastdy);
		return this.convertToMap(query.getResultList());
	}

}