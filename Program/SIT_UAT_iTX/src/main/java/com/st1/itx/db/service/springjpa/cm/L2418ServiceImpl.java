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
import com.st1.itx.eum.ContentName;
import com.st1.itx.util.parse.Parse;

@Service("L2418ServiceImpl")
@Repository
/* 逾期放款明細 */
public class L2418ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private LoanBorMainRepository loanBorMainRepos;

	@Autowired
	private Parse parse;

	// *** 折返控制相關 ***
	private int index;

	// *** 折返控制相關 ***
	private int limit;

	// *** 折返控制相關 ***
	private int cnt;

	// *** 折返控制相關 ***
	private int size;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(loanBorMainRepos);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {

		this.info("L2418.findAll");

		String sql = "";
		String searchstatus = "";
		String searchMediaCode = "";

		String iCityCode = titaVo.getParam("CityCode");

		sql += " select distinct                            ";
		sql += "    \"LandOfficeCode\"                      ";
		sql += "  , \"Item\"                                ";
		sql += " from                                       ";
		sql += "   \"CdLandSection\"    s                   ";
		sql += " left join                                  ";
		sql += "   \"CdCode\"           c                   ";
		sql += " on                                         ";
		sql += "   s.\"LandOfficeCode\" = c.\"Code\"        ";
		sql += " where                                      ";
		sql += "   c.\"DefCode\" = " + "'LandOfficeCode'    ";

//		switch (iCityCode) {
//		//台北含新北
//		case "05":
//			sql += "   or \"CityCode\" = '10'               ";
//			break;
//
//		
//		}

//		排序用
		sql += "   order by S.\"LandOfficeCode\" ASC    ";

		this.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(ContentName.onLine);
		query = em.createNativeQuery(sql);

		cnt = query.getResultList().size();
		this.info("Total cnt ..." + cnt);

		// *** 折返控制相關 ***
		// 設定從第幾筆開始抓,需在createNativeQuery後設定
		query.setFirstResult(this.index * this.limit);

		// *** 折返控制相關 ***
		// 設定每次撈幾筆,需在createNativeQuery後設定
		query.setMaxResults(this.limit);

		List<Object> result = query.getResultList();

		size = result.size();
		this.info("Total size ..." + size);

		return this.convertToMap(query);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> findCityEq(TitaVo titaVo) throws Exception {

		this.info("L2418.findCityEq");

		String sql = "";
		String searchstatus = "";
		String searchMediaCode = "";

		String iCityCode = titaVo.getParam("CityCode");

		this.info("iCityCode = " + iCityCode);

		sql += " select distinct                            ";
		sql += "    \"LandOfficeCode\"                      ";
		sql += "  , \"Item\"                                ";
		sql += " from                                       ";
		sql += "   \"CdLandSection\"    s                   ";
		sql += " left join                                  ";
		sql += "   \"CdCode\"           c                   ";
		sql += " on                                         ";
		sql += "   s.\"LandOfficeCode\" = c.\"Code\"        ";
		sql += " where                                      ";
		sql += "   s.\"CityCode\" = " + "'" + iCityCode + "'";
		sql += " and                                        ";
		sql += "   c.\"DefCode\" = " + "'LandOfficeCode'    ";

//		switch (iCityCode) {
//		//台北含新北
//		case "05":
//			sql += "   or \"CityCode\" = '10'               ";
//			break;
//
//		
//		}

//		排序用
		sql += "   order by S.\"LandOfficeCode\" ASC    ";

		this.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(ContentName.onLine);
		query = em.createNativeQuery(sql);

		cnt = query.getResultList().size();
		this.info("Total cnt ..." + cnt);

		// *** 折返控制相關 ***
		// 設定從第幾筆開始抓,需在createNativeQuery後設定
		query.setFirstResult(this.index * this.limit);

		// *** 折返控制相關 ***
		// 設定每次撈幾筆,需在createNativeQuery後設定
		query.setMaxResults(this.limit);

		List<Object> result = query.getResultList();

		size = result.size();
		this.info("Total size ..." + size);

		return this.convertToMap(query);
	}

	public List<Map<String, String>> findAll(int index, int limit, TitaVo titaVo) throws Exception {
		this.index = index;
		this.limit = limit;

		return findAll(titaVo);
	}

	public List<Map<String, String>> findCityEq(int index, int limit, TitaVo titaVo) throws Exception {
		this.index = index;
		this.limit = limit;

		return findCityEq(titaVo);
	}

	public int getSize() {
		return cnt;
	}

}