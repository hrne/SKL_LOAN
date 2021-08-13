package com.st1.itx.db.service.springjpa.cm;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.ASpringJpaParm;
import com.st1.itx.db.transaction.BaseEntityManager;
import javax.persistence.EntityManager;
import javax.persistence.Query;

@Service("l1001ServiceImpl")
public class L1001ServiceImpl extends ASpringJpaParm implements InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(L1001ServiceImpl.class);

	@Autowired
	private BaseEntityManager baseEntityManager;

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
	
	public List<Map<String, String>> FindData(int index, int limit,int Kind,int CustNoSt , int CustNoEd, String CustId,String CustName, String TelNo,int IdKind, TitaVo titaVo) throws Exception{
		Query query;
		// *** 折返控制相關 ***
		this.index = index;
		// *** 折返控制相關 ***
		this.limit = limit;
		
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		String sql = "select * from ("
				+ "select a.\"CustId\","
				+ " a.\"CustNo\","
				+ " b.\"TelNo\","
				+ " a.\"CustTypeCode\","
				+ " a.\"CustName\","
				+ " a.\"CustUKey\","
				+ " length(a.\"CustId\") as \"length\" "
				+ " from \"CustMain\" a "
				+ " left join \"CustTelNo\" b"
				+ " on a.\"CustUKey\" = b.\"CustUKey\"";
				switch(Kind) {
				case 1:
					sql += " where a.\"CustNo\" between '"+CustNoSt+ "' and '" +CustNoEd+"'";
					break;
				case 2:
					sql += " where a.\"CustId\" ='"+CustId+"'";
					break;
				case 3:
					sql += " where a.\"CustName\" like '%"+CustName+"%'";
					break;
				case 4:
					sql += " where b.\"TelNo\" = '"+TelNo + "' and b.\"TelTypeCode\" = 03";
					break;
				default:
					break;
				}
				sql += " order by a.\"CustId\" asc , a.\"CustNo\" asc ) c ";
				
				switch(IdKind) {
				case 0:
					break;
				case 1:
					sql += " where c.\"length\" = '10'";
					break;	
				case 2:
					sql += " where c.\"length\" = '8'";
					break;
				}
				sql += sqlRow;
				
		logger.info("sql = "+sql); 
		

		query = em.createNativeQuery(sql);
		query.setParameter("ThisIndex", index);
		query.setParameter("ThisLimit", limit);
		
		query.setFirstResult(0);// 因為已經在語法中下好限制條件(筆數),所以每次都從新查詢即可

		// *** 折返控制相關 ***
		// 設定每次撈幾筆,需在createNativeQuery後設定
		query.setMaxResults(this.limit);
		
		logger.info("L1001Service FindData=" + query.toString());
		return this.convertToMap(query.getResultList());
	}
}
