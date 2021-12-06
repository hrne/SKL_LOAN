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

@Service("L2023RServiceImpl")
@Repository
/* 逾期放款明細 */
public class L2023ServiceImpl extends ASpringJpaParm implements InitializingBean {

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
	private int size;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(loanBorMainRepos);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> findAll(int index, int limit, TitaVo titaVo) throws Exception {

		// 輸入參數檢核
		String CustId = titaVo.getParam("CustId");
        
		// 額度
		String sql = "  SELECT     " ; 
		sql +=	"    LPAD(fm.\"CreditSysNo\",7,'0')   AS \"CreditSysNo\"," ; 
		sql +=	"    fm.\"ApplNo\"        AS \"ApplNo\"," ; 
		sql +=	"    CASE" ; 
		sql +=	"        WHEN fm.\"CustNo\" IS NOT NULL THEN"; 
		sql +=	"        lpad(fm.\"CustNo\",7,'0') "; 
		sql +=	"        ELSE null end   AS \"CustNo\"," ; 
		sql +=	"    CASE" ; 
		sql +=	"        WHEN fm.\"FacmNo\" IS NOT NULL THEN"; 
		sql +=	"        lpad(fm.\"FacmNo\",3,'0') "; 
		sql +=	"        ELSE null end   AS \"FacmNo\"," ; 
		sql +=	"    cu.\"CustName\"      AS \"CustName\"," ; 
		sql +=	"    cu.\"CustUKey\"     AS \"UKey\"," ; 
		sql +=	"    cu.\"CustId\"       AS \"Id\"," ; 
		sql +=	"    cu.\"CustName\"     AS \"Name\"," ; 
		sql +=	"    n'授信戶' AS \"Type\"," ; 
		sql +=	"    CASE" ; 
		sql +=	"        WHEN length(cu.\"CustId\") >= 8 THEN" ; 
		sql +=	"            n'授信戶本人'" ; 
		sql +=	"        ELSE" ; 
		sql +=	"            n'授信戶企業'" ; 
		sql +=	"    END AS \"Relation\"," ; 
		sql +=	"    NULL AS \"ClCode1\"," ; 
		sql +=	"    NULL AS \"ClCode2\"," ; 
		sql +=	"    NULL AS \"ClNo\"," ; 
		sql +=	"    0 AS \"Modify\"" ; 
		sql +=	"  FROM" ; 
		sql +=	"    \"FacMain\"    fm" ; 
		sql +=	"    LEFT JOIN \"CustMain\"   cu ON cu.\"CustUKey\" = fm.\"CustUKey\"" ; 
		sql +=	"  WHERE" ; 
		sql +=	"    cu.\"CustId\" = :custid" ; 
		
		sql +=	"  UNION" ; 
		// 共同借款人
		sql +=	"  SELECT     " ; 
		sql +=	"    LPAD(fm.\"CreditSysNo\",7,'0')   AS \"CreditSysNo\"," ; 
		sql +=	"    fm.\"ApplNo\"        AS \"ApplNo\"," ; 
		sql +=	"    CASE" ; 
		sql +=	"        WHEN fm.\"CustNo\" IS NOT NULL THEN"; 
		sql +=	"        lpad(fm.\"CustNo\",7,'0') "; 
		sql +=	"        ELSE null end   AS \"CustNo\"," ; 
		sql +=	"    CASE" ; 
		sql +=	"        WHEN fm.\"FacmNo\" IS NOT NULL THEN"; 
		sql +=	"        lpad(fm.\"FacmNo\",3,'0') "; 
		sql +=	"        ELSE null end   AS \"FacmNo\"," ; 
		sql +=	"    cu.\"CustName\"      AS \"CustName\"," ; 
		sql +=	"    cu2.\"CustUKey\"     AS \"UKey\"," ; 
		sql +=	"    cu2.\"CustId\"       AS \"Id\"," ; 
		sql +=	"    cu2.\"CustName\"     AS \"Name\"," ; 
		sql +=	"    n'共同借款人' AS \"Type\"," ; 
		sql +=	"    CASE" ; 
		sql +=	"        WHEN nvl(fsr.\"RelCode\", ' ') != ' ' THEN" ; 
		sql +=	"            cdg.\"GuaRelItem\"" ; 
		sql +=	"        ELSE" ; 
		sql +=	"            n'**與授信戶關係未登錄**'" ; 
		sql +=	"    END AS \"Relation\"," ; 
		sql +=	"    null AS \"ClCode1\"," ; 
		sql +=	"    null AS \"ClCode2\"," ; 
		sql +=	"    null AS \"ClNo\"," ; 
		sql +=	"    CASE" ; 
		sql +=	"        WHEN nvl(fsr.\"RelCode\", ' ') != ' ' THEN" ; 
		sql +=	"            0" ; 
		sql +=	"        ELSE" ; 
		sql +=	"            1" ; 
		sql +=	"  END AS \"Modify\"" ; 
		sql +=	"  FROM" ; 
		sql +=	"    \"FacMain\"            fm" ; 
		sql +=	"    LEFT JOIN \"CustMain\"           cu ON cu.\"CustUKey\" = fm.\"CustUKey\"" ; 
		sql +=	"    LEFT JOIN \"FacShareAppl\"       fs ON fs.\"ApplNo\" = fm.\"ApplNo\"" ; 
		sql +=	"    LEFT JOIN \"FacShareAppl\"       fs2 ON fs2.\"MainApplNo\" = fs.\"MainApplNo\"" ; 
		sql +=	"    LEFT JOIN \"FacShareRelation\"   fsr ON fsr.\"ApplNo\" = fs.\"ApplNo\"" ; 
		sql +=	"                                        AND fsr.\"RelApplNo\" = fs2.\"ApplNo\"" ; 
		sql +=	"    LEFT JOIN \"CdGuarantor\"        cdg ON cdg.\"GuaRelCode\" = fsr.\"RelCode\"" ; 
		sql +=	"    LEFT JOIN \"CustMain\"           cu2 ON cu2.\"CustNo\" = fs2.\"CustNo\"" ; 
		sql +=	"  WHERE" ; 
		sql +=	"    cu.\"CustId\" = :custid" ; 
		sql +=	"    and cu2.\"CustNo\" = fs2.\"CustNo\"" ; 
		
		sql +=	"  UNION" ; 
		// 保證人
		sql +=	"  SELECT     " ; 
		sql +=	"    LPAD(fm.\"CreditSysNo\",7,'0')   AS \"CreditSysNo\"," ; 
		sql +=	"    fm.\"ApplNo\"        AS \"ApplNo\"," ; 
		sql +=	"    CASE" ; 
		sql +=	"        WHEN fm.\"CustNo\" IS NOT NULL THEN"; 
		sql +=	"        lpad(fm.\"CustNo\",7,'0') "; 
		sql +=	"        ELSE null end   AS \"CustNo\"," ; 
		sql +=	"    CASE" ; 
		sql +=	"        WHEN fm.\"FacmNo\" IS NOT NULL THEN"; 
		sql +=	"        lpad(fm.\"FacmNo\",3,'0') "; 
		sql +=	"        ELSE null end   AS \"FacmNo\"," ; 
		sql +=	"    cu.\"CustName\"      AS \"CustName\"," ; 
		sql +=	"    cu2.\"CustUKey\"     AS \"UKey\"," ; 
		sql +=	"    cu2.\"CustId\"       AS \"Id\"," ; 
		sql +=	"    cu2.\"CustName\"     AS \"Name\"," ; 
		sql +=	"    n'保證人' AS \"Type\"," ; 
		sql +=	"    cdg.\"GuaRelItem\"   AS \"Relation\"," ; 
		sql +=	"    NULL AS \"ClCode1\"," ; 
		sql +=	"    NULL AS \"ClCode2\"," ; 
		sql +=	"    NULL AS \"ClNo\"," ; 
		sql +=	"    0 AS \"Modify\"" ; 
		sql +=	"  FROM" ; 
		sql +=	"    \"FacMain\"       fm" ; 
		sql +=	"    LEFT JOIN \"CustMain\"      cu ON cu.\"CustUKey\" = fm.\"CustUKey\"" ; 
		sql +=	"    LEFT JOIN \"Guarantor\"     gu ON gu.\"ApproveNo\" = fm.\"ApplNo\"" ; 
		sql +=	"    LEFT JOIN \"CdGuarantor\"   cdg ON cdg.\"GuaRelCode\" = gu.\"GuaRelCode\"" ; 
		sql +=	"    LEFT JOIN \"CustMain\"      cu2 ON cu2.\"CustUKey\" = gu.\"GuaUKey\"" ; 
		sql +=	"  WHERE" ; 
		sql +=	"    cu.\"CustId\" = :custid" ; 
		sql +=	"    AND gu.\"ApproveNo\" = fm.\"ApplNo\"" ; 
		
		sql +=	"  UNION" ; 
		// 擔保品提供人
		sql +=	"  SELECT     " ; 
		sql +=	"    LPAD(fm.\"CreditSysNo\",7,'0')   AS \"CreditSysNo\"," ; 
		sql +=	"    fm.\"ApplNo\"        AS \"ApplNo\"," ; 
		sql +=	"    CASE" ; 
		sql +=	"        WHEN fm.\"CustNo\" IS NOT NULL THEN"; 
		sql +=	"        lpad(fm.\"CustNo\",7,'0') "; 
		sql +=	"        ELSE null end   AS \"CustNo\"," ; 
		sql +=	"    CASE" ; 
		sql +=	"        WHEN fm.\"FacmNo\" IS NOT NULL THEN"; 
		sql +=	"        lpad(fm.\"FacmNo\",3,'0') "; 
		sql +=	"        ELSE null end   AS \"FacmNo\"," ; 
		sql +=	"    cu.\"CustName\"      AS \"CustName\"," ; 
		sql +=	"    cu2.\"CustUKey\"     AS \"UKey\"," ; 
		sql +=	"    cu2.\"CustId\"       AS \"Id\"," ; 
		sql +=	"    cu2.\"CustName\"     AS \"Name\"," ; 
		sql +=	"    n'擔保品提供人' AS \"Type\"," ; 
		sql +=	"    CASE" ; 
		sql +=	"        WHEN nvl(clr.\"OwnerRelCode\", ' ') != ' ' THEN" ; 
		sql +=	"            cdg.\"GuaRelItem\"" ; 
		sql +=	"        ELSE" ; 
		sql +=	"            n'**與授信戶關係未登錄**'" ; 
		sql +=	"    END AS \"Relation\"," ; 
		sql +=	"    cf.\"ClCode1\"       AS \"ClCode1\"," ; 
		sql +=	"    CASE" ; 
		sql +=	"        WHEN cf.\"ClCode2\" IS NOT NULL THEN"; 
		sql +=	"        lpad(cf.\"ClCode2\",2,'0') "; 
		sql +=	"        ELSE null end   AS \"ClCode2\"," ; 
		sql +=	"    CASE" ; 
		sql +=	"        WHEN cf.\"ClNo\" IS NOT NULL THEN"; 
		sql +=	"        lpad(cf.\"ClNo\",7,'0') "; 
		sql +=	"        ELSE null end   AS \"ClNo\"," ; 
		sql +=	"    CASE" ; 
		sql +=	"        WHEN nvl(clr.\"OwnerRelCode\", ' ') != ' ' THEN" ; 
		sql +=	"            0" ; 
		sql +=	"        ELSE" ; 
		sql +=	"            1" ; 
		sql +=	"    END AS \"Modify\"" ; 
		sql +=	"  FROM" ; 
		sql +=	"    \"FacMain\"           fm" ; 
		sql +=	"    LEFT JOIN \"CustMain\"          cu ON cu.\"CustUKey\" = fm.\"CustUKey\"" ; 
		sql +=	"    LEFT JOIN \"ClFac\"             cf ON cf.\"ApproveNo\" = fm.\"ApplNo\"" ; 
		sql +=	"                            AND cf.\"CustNo\" = cu.\"CustNo\"" ; 
		sql +=	"                            AND cf.\"FacmNo\" = fm.\"FacmNo\"" ; 
		sql +=	"    LEFT JOIN \"ClBuildingOwner\"   cbo ON cbo.\"ClCode1\" = cf.\"ClCode1\"" ; 
		sql +=	"                                       AND cbo.\"ClCode2\" = cf.\"ClCode2\"" ; 
		sql +=	"                                       AND cbo.\"ClNo\" = cf.\"ClNo\"" ; 
		sql +=	"    LEFT JOIN \"ClLandOwner\"       clo ON clo.\"ClCode1\" = cf.\"ClCode1\"" ; 
		sql +=	"                                   AND clo.\"ClCode2\" = cf.\"ClCode2\"" ; 
		sql +=	"                                   AND clo.\"ClNo\" = cf.\"ClNo\"" ; 
		sql +=	"    LEFT JOIN \"ClStock\"           cs ON cs.\"ClCode1\" = cf.\"ClCode1\"" ; 
		sql +=	"                              AND cs.\"ClCode2\" = cf.\"ClCode2\"" ; 
		sql +=	"                              AND cs.\"ClNo\" = cf.\"ClNo\"" ; 
		sql +=	"    LEFT JOIN \"ClOther\"           co ON co.\"ClCode1\" = cf.\"ClCode1\"" ; 
		sql +=	"                              AND co.\"ClCode2\" = cf.\"ClCode2\"" ; 
		sql +=	"                              AND co.\"ClNo\" = cf.\"ClNo\"" ; 
		sql +=	"    LEFT JOIN \"ClMovables\"        cmv ON cmv.\"ClCode1\" = cf.\"ClCode1\"" ; 
		sql +=	"                                  AND cmv.\"ClCode2\" = cf.\"ClCode2\"" ; 
		sql +=	"                                  AND cmv.\"ClNo\" = cf.\"ClNo\"" ; 
		sql +=	"    LEFT JOIN \"ClOwnerRelation\"   clr ON clr.\"CreditSysNo\" = fm.\"CreditSysNo\"" ; 
		sql +=	"                                       AND clr.\"CustNo\" = fm.\"CustNo\"" ; 
		sql +=	"                                       AND ( clr.\"OwnerCustUKey\" = cbo.\"OwnerCustUKey\"" ; 
		sql +=	"                                             OR clr.\"OwnerCustUKey\" = clo.\"OwnerCustUKey\"" ; 
		sql +=	"                                             OR clr.\"OwnerCustUKey\" = cs.\"OwnerCustUKey\"" ; 
		sql +=	"                                             OR clr.\"OwnerCustUKey\" = co.\"OwnerCustUKey\"" ; 
		sql +=	"                                             OR clr.\"OwnerCustUKey\" = cmv.\"OwnerCustUKey\" )" ; 
		sql +=	"    LEFT JOIN \"CustMain\"          cu2 ON cu2.\"CustUKey\" = clr.\"OwnerCustUKey\"" ; 
		sql +=	"    LEFT JOIN \"CdGuarantor\"       cdg ON cdg.\"GuaRelCode\" = clr.\"OwnerRelCode\"" ; 
		sql +=	"  WHERE" ;
		sql +=	"    cu.\"CustId\" = :custid" ; 
		sql +=	"    AND cf.\"CustNo\" = cu.\"CustNo\"" ; 
		sql +=	"    AND cf.\"FacmNo\" = fm.\"FacmNo\"" ; 
		
		sql +=	"  UNION" ; 
		// 交易關係人
		sql +=	"  SELECT DISTINCT " ; 
		sql +=	"    LPAD(fm.\"CreditSysNo\",7,'0')   AS \"CreditSysNo\"," ; 
		sql +=	"    NULL AS \"ApplNo\"," ; 
		sql +=	"    NULL AS \"CustNo\"," ; 
		sql +=	"    NULL AS \"FacmNo\"," ; 
		sql +=	"    NULL AS \"CustName\"," ; 
		sql +=	"    cu2.\"CustUKey\"     AS \"UKey\"," ; 
		sql +=	"    cu2.\"CustId\"       AS \"Id\"," ; 
		sql +=	"    cu2.\"CustName\"     AS \"Name\"," ; 
		sql +=	"    n'交易關係人' AS \"Type\"," ; 
		sql +=	"    cd.\"Item\"          AS \"Relation\"," ; 
		sql +=	"    NULL AS \"ClCode1\"," ; 
		sql +=	"    NULL AS \"ClCode2\"," ; 
		sql +=	"    NULL AS \"ClNo\"," ; 
		sql +=	"    0 AS \"Modify\"" ; 
		sql +=	"  FROM" ; 
		sql +=	"    \"FacMain\"       fm" ; 
		sql +=	"    LEFT JOIN \"CustMain\"      cu ON cu.\"CustUKey\" = fm.\"CustUKey\"" ; 
		sql +=	"    LEFT JOIN \"FacRelation\"   fr ON fr.\"CreditSysNo\" = fm.\"CreditSysNo\"" ; 
		sql +=	"    LEFT JOIN \"CustMain\"      cu2 ON cu2.\"CustUKey\" = fr.\"CustUKey\"" ; 
		sql +=	"    LEFT JOIN \"CdCode\"        cd ON cd.\"DefCode\" = 'FacRelationCode'" ; 
		sql +=	"                             AND cd.\"Code\" = fr.\"FacRelationCode\"" ; 
		sql +=	"  WHERE" ; 
		sql +=	"    cu.\"CustId\" = :custid" ; 
		sql +=	"    AND fr.\"CreditSysNo\" = fm.\"CreditSysNo\"";
		sql +=	"  ORDER BY \"CreditSysNo\", \"ApplNo\"";

		this.info("sql=" + sql);
		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(ContentName.onLine);
		query = em.createNativeQuery(sql);
		query.setParameter("custid", CustId);
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

	public int getSize() {
		return cnt;
	}

}