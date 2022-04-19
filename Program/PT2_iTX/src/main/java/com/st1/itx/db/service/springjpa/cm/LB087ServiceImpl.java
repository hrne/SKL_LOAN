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
import com.st1.itx.eum.ContentName;

@Service("lB087ServiceImpl")
@Repository

/*
 * LB087 聯貸案首次動撥後６個月內發生違約之實際主導金融機構註記檔
 */

public class LB087ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {
//		boolean onLineMode = true;
		boolean onLineMode = false;

		this.info("----------- LB087.findAll ---------------");
		this.info("-----LB087 TitaVo=" + titaVo);
		this.info("-----LB087 Tita ENTDY=" + titaVo.getEntDy().substring(0, 6));

		String sql = ""; // 無 table

		// LB087 聯貸案首次動撥後６個月內發生違約之實際主導金融機構註記檔
		sql = "";

		this.info("sql=" + sql);

		// 無查詢
		Query query;
		EntityManager em;
		if (onLineMode) {
			em = this.baseEntityManager.getCurrentEntityManager(ContentName.onLine); // onLine 資料庫
		} else {
			em = this.baseEntityManager.getCurrentEntityManager(titaVo); // 從 LB087.java 帶入資料庫環境
		}
		query = em.createNativeQuery(sql); // 無 table

		// 轉成 List<HashMap<String, String>>
//    	return this.convertToMap(query);
		return this.convertToMap(query);
	}
}
