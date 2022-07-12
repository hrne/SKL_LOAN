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
import com.st1.itx.db.repository.online.LoanBorMainRepository;
import com.st1.itx.db.service.springjpa.ASpringJpaParm;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.util.parse.Parse;

@Service("L6023ServiceImpl")
@Repository
/* 逾期放款明細 */
public class L6023ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private LoanBorMainRepository loanBorMainRepos;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	// *** 折返控制相關 ***
	private int index;

	// *** 折返控制相關 ***
	private int limit;

	// *** 折返控制相關 ***
	private int cnt;

	// *** 折返控制相關 ***
	private String sqlRow = "OFFSET :ThisIndex * :ThisLimit ROWS FETCH NEXT :ThisLimit ROW ONLY ";

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(loanBorMainRepos);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> findAll(int index, int limit, TitaVo titaVo) throws Exception {

		String iCityCode = titaVo.getParam("CityCode");
		String iLandOfficeCode = titaVo.getParam("LandOfficeCode");
		String iRecWord = titaVo.getParam("RecWord");

		// work area
		int wkFacmNoStart = 0;

		String sql = "SELECT";
		sql += "  MIN(co.\"LandOfficeCode\") AS \"LandOfficeCode\", ";
		sql += " 	    MIN(co.\"RecWord\") AS \"RecWord\", ";
		sql += " 	    MIN(co.\"RecWordItem\") AS \"RecWordItem\", ";
		sql += " 	    MIN(co.\"LastUpdate\") AS \"LastUpdate\", ";
		sql += " 	    MIN(co.\"LastUpdateEmpNo\") AS \"LastUpdateEmpNo\" ";
		sql += " 	FROM ";
		sql += " 	    \"CdLandOffice\"    co ";
		sql += " 	    LEFT JOIN \"CdLandSection\"   cs ON co.\"LandOfficeCode\" = cs.\"LandOfficeCode\" ";

		if (!iCityCode.isEmpty() || !iLandOfficeCode.isEmpty() || !iRecWord.isEmpty()) {
			sql += " 	WHERE ";
		}
		if (!iCityCode.isEmpty()) {
			sql += " 	    cs.\"CityCode\" = :cityCode ";

			if (!iLandOfficeCode.isEmpty()) {
				sql += " 	AND ";
			}
		}
		if (!iLandOfficeCode.isEmpty()) {
			sql += "  co.\"LandOfficeCode\" = :landOfficeCode ";
		}
		if (!iRecWord.isEmpty()) {

			if (!iCityCode.isEmpty() || !iLandOfficeCode.isEmpty()) {
				sql += " 	AND ";
			}
			sql += "  co.\"RecWord\" = :recWord ";
		}
		sql += " 	GROUP BY co.\"LandOfficeCode\", ";
		sql += " 	    co.\"RecWord\" ";
		sql += " 	    ORDER BY co.\"LandOfficeCode\" ASC , co.\"RecWord\" ASC ";
		sql += sqlRow;

		// *** 折返控制相關 ***
		this.index = index;
		// *** 折返控制相關 ***
		this.limit = limit;

		this.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("ThisIndex", index);
		query.setParameter("ThisLimit", limit);

		if (!iCityCode.isEmpty()) {
			query.setParameter("cityCode", iCityCode);
		}
		if (!iLandOfficeCode.isEmpty()) {
			query.setParameter("landOfficeCode", iLandOfficeCode);
		}
		if (!iRecWord.isEmpty()) {
			query.setParameter("recWord", iRecWord);
		}

		cnt = query.getResultList().size();
		this.info("Total cnt ..." + cnt);

		// *** 折返控制相關 ***
		// *** 折返控制相關 ***
		// 設定每次撈幾筆,需在createNativeQuery後設定
		query.setMaxResults(this.limit);
		List<Object> result = query.getResultList();

		// *** 折返控制相關 ***
		// 設定從第幾筆開始抓,需在createNativeQuery後設定
		// query.setFirstResult(this.index*this.limit);
		query.setFirstResult(0);// 因為已經在語法中下好限制條件(筆數),所以每次都從新查詢即可

		// *** 折返控制相關 ***
		// 設定每次撈幾筆,需在createNativeQuery後設定
		query.setMaxResults(this.limit);

		return this.convertToMap(query);
	}

	public int getSize() {
		return cnt;
	}

}