package com.st1.itx.db.service.springjpa.cm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.ASpringJpaParm;
import com.st1.itx.db.transaction.BaseEntityManager;

@Service("l5500ServiceImpl")
@Repository
public class L5500ServiceImpl extends ASpringJpaParm implements InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(L5500ServiceImpl.class);

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

	public List<String[]> FindData(int index, int limit, String sql, Map<String, String> queryKey, TitaVo titaVo) throws LogicException {
		logger.info("FindData");

		// *** 折返控制相關 ***
		this.index = index;
		// *** 折返控制相關 ***
		this.limit = limit;
		logger.info("JcicServiceImpl sql=[" + sql + "]");

		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		logger.info("JcicServiceImpl this.index=[" + this.index + "],this.limit=[" + this.limit + "]");
		query.setParameter("ThisIndex", index);
		query.setParameter("ThisLimit", limit);

		if (queryKey != null && queryKey.size() != 0) {
			for (String key : queryKey.keySet()) {
				logger.info("JcicService FindJcic Key=[" + key + "],keyValue=[" + queryKey.get(key) + "]");
				query.setParameter(key, queryKey.get(key));
			}
		}

		// *** 折返控制相關 ***
		// 設定從第幾筆開始抓,需在createNativeQuery後設定
		// query.setFirstResult(this.index*this.limit);
		query.setFirstResult(0);// 因為已經在語法中下好限制條件(筆數),所以每次都從新查詢即可

		// *** 折返控制相關 ***
		// 設定每次撈幾筆,需在createNativeQuery後設定
		query.setMaxResults(this.limit);

		@SuppressWarnings("unchecked")
		List<Object> lObject = this.convertToMap(query.getResultList());

		return FindData(lObject);
	}

	@SuppressWarnings("unchecked")
	public List<String[]> FindData(List<Object> lObject) throws LogicException {
		List<String[]> data = new ArrayList<String[]>();
		if (lObject != null && lObject.size() != 0) {
			int col = ((Map<String, String>) lObject.get(0)).keySet().size();
			for (Object obj : lObject) {
				Map<String, String> MapObj = (Map<String, String>) obj;
				String row[] = new String[col];
				for (int i = 0; i < col; i++) {
					row[i] = MapObj.get("F" + String.valueOf(i));
					if (row[i] != null && row[i].length() != 0) {

					} else {
						row[i] = "";
					}
				}
				data.add(row);
			}
		}
		return data;
	}

}