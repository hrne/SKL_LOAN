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
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.springjpa.ASpringJpaParm;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.util.parse.Parse;

@Service("l2919ServiceImpl")
@Repository
public class L2919ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	/* DB服務注入 */
	@Autowired
	public CustMainService sCustMainService;
	
	/* 轉換工具 */
	@Autowired
	public Parse parse;


	private Query query;

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
	}

	public List<Map<String, String>> FindAll(int index, int limit ,TitaVo titaVo) throws Exception {
		this.info("L2919ServiceImpl.find");

		// *** 折返控制相關 ***
		this.index = index;
		// *** 折返控制相關 ***
		this.limit = limit;
				
		String  sql =  "SELECT  cm.\"ClCode1\"                    AS \"ClCode1\"";
			    sql += "       ,cm.\"ClCode2\"                    AS \"ClCode2\"";
			    sql += "       ,cm.\"ClNo\"                       AS \"ClNo\"";
			    sql += "       ,MIN(cm.\"ClTypeCode\")                 AS \"ClTypeCode\"";
			    sql += "       ,MIN(cm.\"CityCode\")                   AS \"CityCode\"";
			    sql += "       ,MIN(cu.\"CustId\")                     AS \"CustId\"";
			    sql += "       ,MIN(cu.\"CustName\")                   AS \"CustName\"";
			    sql += "       ,MIN(cu.\"CustNo\")                     AS \"CustNo\"";
			    sql += "       FROM \"ClMain\" cm";
			    
			    sql += "       LEFT JOIN (SELECT  \"CustId\"";
			    sql += "                         ,\"CustName\"";
			    sql += "                         ,\"CustNo\"";
			    sql += "                         ,\"CustUKey\"";
			    sql += "       FROM \"CustMain\" ";
			    sql += "               ) cu ON cu.\"CustId\" = :custId";
			    
			    sql += "       LEFT JOIN \"ClStock\" cs ON cs.\"ClCode1\" = cm.\"ClCode1\"";
				sql += "                               AND cs.\"ClCode2\" = cm.\"ClCode2\"";
				sql += "                               AND cs.\"ClNo\"    = cm.\"ClNo\"";
				sql += "                               AND cs.\"OwnerCustUKey\"   = cu.\"CustUKey\"";
				
				sql += "       LEFT JOIN \"ClOther\" co ON co.\"ClCode1\" = cm.\"ClCode1\"";
				sql += "                               AND co.\"ClCode2\" = cm.\"ClCode2\"";
				sql += "                               AND co.\"ClNo\"    = cm.\"ClNo\"";
				sql += "                               AND co.\"OwnerCustUKey\"   = cu.\"CustUKey\"";
				  
				sql += "       LEFT JOIN \"ClMovables\" cmv ON cmv.\"ClCode1\" = cm.\"ClCode1\"";
				sql += "                                   AND cmv.\"ClCode2\" = cm.\"ClCode2\"";
				sql += "                                   AND cmv.\"ClNo\"    = cm.\"ClNo\"";
				sql += "                                   AND cmv.\"OwnerCustUKey\"   = cu.\"CustUKey\"";
				
				sql += "       LEFT JOIN \"ClBuildingOwner\" co ON co.\"ClCode1\" = cm.\"ClCode1\"";
				sql += "                                 AND co.\"ClCode2\" = cm.\"ClCode2\"";
				sql += "                                 AND co.\"ClNo\"    = cm.\"ClNo\"";
				sql += "                                 AND co.\"OwnerCustUKey\"   = cu.\"CustUKey\"";
				 
				sql += "       LEFT JOIN \"ClLandOwner\" cmv ON cmv.\"ClCode1\" = cm.\"ClCode1\"";
				sql += "                                    AND cmv.\"ClCode2\" = cm.\"ClCode2\"";
				sql += "                                    AND cmv.\"ClNo\"    = cm.\"ClNo\"";
				sql += "                                    AND cmv.\"OwnerCustUKey\"   = cu.\"CustUKey\"";			
				
				sql += "       WHERE cu.\"CustId\" = :custId";
				sql += "       GROUP BY cm.\"ClCode1\",cm.\"ClCode2\",cm.\"ClNo\"";
				sql += "       ORDER BY cm.\"ClCode1\",cm.\"ClCode2\",cm.\"ClNo\"";
			   
		
		this.info("sql = " + sql);
				
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		
		query = em.createNativeQuery(sql);
		
		query.setParameter("custId", titaVo.get("CustId").trim());
		
		
		cnt = query.getResultList().size();
		this.info("Total cnt ..." + cnt);
	
		
//		// *** 折返控制相關 ***
//		// 設定從第幾筆開始抓,需在createNativeQuery後設定
		query.setFirstResult(this.index * this.limit);
		
		// *** 折返控制相關 ***
		// 設定每次撈幾筆,需在createNativeQuery後設定
		query.setMaxResults(this.limit);

		@SuppressWarnings("unchecked")
		List<Object> result = query.getResultList();

		size = result.size();
		this.info("Total size ..." + size);
		
		return this.convertToMap(result);
	}


	public int getSize() {
		return cnt;
	}
}