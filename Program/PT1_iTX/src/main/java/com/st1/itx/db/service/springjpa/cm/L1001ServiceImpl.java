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
import com.st1.itx.util.parse.Parse;;

@Service("l1001ServiceImpl")
@Repository
public class L1001ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	@Override
	public void afterPropertiesSet() throws Exception {
		// 創建程式碼後,檢查初始值
		// org.junit.Assert.assertNotNull(sPfItDetailService);
	}

	// *** 折返控制相關 ***
	private int index;

	// *** 折返控制相關 ***
	private int limit;

	private String sqlRow = "OFFSET :ThisIndex * :ThisLimit ROWS FETCH NEXT :ThisLimit ROW ONLY ";

//	public String FindL5051(String FunctionCd, String PerfDateFm, String PerfDateTo, String CustNo, String FacmNo) throws Exception {
	public List<Map<String, String>> FindData(TitaVo titaVo, int iKind, int index, int limit) throws Exception {
		this.info("L1001FindData");

		int iCustNoSt = parse.stringToInteger(titaVo.get("CustNoSt"));
		int iCustNoEd = parse.stringToInteger(titaVo.get("CustNoEd"));
		String iCustId = titaVo.getParam("CustId");
		String iCustNm = titaVo.get("CustName").trim();
		String iMobile = titaVo.get("Mobile").trim();
		String iIndustryCode = titaVo.getParam("IndustryCode");
		String iCuscCd = titaVo.getParam("IdKind");

		String sql = "SELECT A.\"CustUKey\" ";
		sql += "FROM \"CustMain\" A ";
		if (iKind == 1) {
			sql += "where \"CustNo\">=:custno1 and \"CustNo\" <= :custno2 ";
		} else if (iKind == 2) {
			sql += "where \"CustId\"=:custid ";
		} else if (iKind == 3) {
			sql += "where \"CustName\" like :custname ";
		} else if (iKind == 4) {
			sql += "where \"CustUKey\" in (select distinct(\"CustUKey\") from \"CustTelNo\" where \"TelTypeCode\" in ('03','05') and \"TelNo\" like :telno ) ";
		} else if (iKind == 5) {
			sql += "where \"IndustryCode\"=:industrycode ";
		}

		if (!"0".equals(iCuscCd)) {
			sql += "and \"CuscCd\" = :cuscCd ";
		}

		sql += "ORDER BY A.\"CustNo\",A.\"CustId\" ";

		sql += sqlRow;

		this.info("FindL1001 sql=" + sql);

		// *** 折返控制相關 ***
		this.index = index;
		// *** 折返控制相關 ***
		this.limit = limit;

		Query query;
//			query = em.createNativeQuery(sql,L5051Vo.class);//進SQL 所以不要用.class (要用.class 就要使用HQL)
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("ThisIndex", index);
		query.setParameter("ThisLimit", limit);

		if (iKind == 1) {
			query.setParameter("custno1", iCustNoSt);
			query.setParameter("custno2", iCustNoEd);
		} else if (iKind == 2) {
			query.setParameter("custid", iCustId);
		} else if (iKind == 3) {
			query.setParameter("custname", iCustNm + "%");
		} else if (iKind == 4) {
			query.setParameter("telno", iMobile + "%");
		} else if (iKind == 5) {
			query.setParameter("industrycode", iIndustryCode);
		}

		if (!"0".equals(iCuscCd)) {
			query.setParameter("cuscCd", iCuscCd);
		}

		this.info("L1001Service FindData=" + query);

		// *** 折返控制相關 ***
		// 設定從第幾筆開始抓,需在createNativeQuery後設定
		// query.setFirstResult(this.index*this.limit);
		query.setFirstResult(0);// 因為已經在語法中下好限制條件(筆數),所以每次都從新查詢即可

		// *** 折返控制相關 ***
		// 設定每次撈幾筆,需在createNativeQuery後設定
		query.setMaxResults(this.limit);

		// List<L5051Vo> L5051VoList =this.convertToMap(query.getResultList());

		return this.convertToMap(query);
	}

}