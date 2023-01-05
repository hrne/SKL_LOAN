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
//import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.db.service.springjpa.ASpringJpaParm;
import com.st1.itx.db.transaction.BaseEntityManager;

@Service("l6101ServiceImpl")
@Repository
public class L6101ServiceImpl extends ASpringJpaParm implements InitializingBean {

//  @Autowired
//  private CdCodeService sCdCodeDefService;

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
		// 創建程式碼後,檢查初始值
		// org.junit.Assert.assertNotNull(sPfItDetailService);
	}

	public List<Map<String, String>> findAcMainAll(int iAcDate, TitaVo titaVo) throws Exception {
		String sql = "";
		sql += " Select";
		sql += "    A.*                     ";
		sql += "   ,C.\"AcNoItem\"          ";
		sql += "   ,C.\"AcctItem\"          ";
		sql += "   ,C.\"ClsChkFlag\"        ";
		sql += " FROM \"AcMain\" A  ";
		sql += " LEFT JOIN \"CdAcCode\" C   ";
		sql += "        on C.\"AcNoCode\"  = A.\"AcNoCode\"  ";
		sql += "       and C.\"AcSubCode\" = A.\"AcSubCode\"  ";
		sql += "       and C.\"AcDtlCode\" = A.\"AcDtlCode\"  ";
		sql += " where A.\"AcDate\" = :iAcDate ";

		this.info("L6101 FindAcMain SQL=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("iAcDate", iAcDate);
		query.setFirstResult(0);// 因為已經在語法中下好限制條件(筆數),所以每次都從新查詢即可
		query.setMaxResults(Integer.MAX_VALUE);

		return this.convertToMap(query);

	}

}