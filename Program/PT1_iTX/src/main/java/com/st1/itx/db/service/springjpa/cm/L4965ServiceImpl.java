package com.st1.itx.db.service.springjpa.cm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.ASpringJpaParm;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.util.parse.Parse;

@Service("l4965ServiceImpl")
@Repository
public class L4965ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;
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
		// 創建程式碼後,檢查初始值
		// org.junit.Assert.assertNotNull(sPfItDetailService);
	}

	@SuppressWarnings("unchecked")
	public List<String[]> findData(int index, int limit, TitaVo titaVo) throws LogicException {
		// * 1:擔保品編號; 2:戶號; 3:核准號螞; 4:保險單號碼; 5:保險公司; 6:保險類別
		int iSearchFlag = parse.stringToInteger(titaVo.getParam("SearchFlag"));
		int iClCode1 = parse.stringToInteger(titaVo.getParam("ClCode1"));
		int iClCode2 = parse.stringToInteger(titaVo.getParam("ClCode2"));
		int iClNo = parse.stringToInteger(titaVo.getParam("ClNo"));
		int iCustNo = parse.stringToInteger(titaVo.getParam("CustNo"));
		int iFacmNo = parse.stringToInteger(titaVo.getParam("FacmNo"));
		int iApplNo = parse.stringToInteger(titaVo.getParam("ApplNo"));
		String iNowInsuNo = titaVo.getParam("NowInsuNo").trim();
		String iInsuCompany = titaVo.getParam("InsuCompany");
		String iInsuTypeCode = titaVo.getParam("InsuTypeCode");
		int iEffectiveCode = parse.stringToInteger(titaVo.getParam("EffectiveCode"));
		int iInsuEndDateFrom = parse.stringToInteger(titaVo.getParam("InsuEndDateFrom")) + 19110000;
		int iInsuEndDateTo = parse.stringToInteger(titaVo.getParam("InsuEndDateTo")) + 19110000;
		//
		String sqlL4965 = "";

		String searchStr = "";
		String searchStr1 = "";
		String searchInsuNo1 = "";
		String searchInsuNo2 = "";
		switch (iSearchFlag) {
		case 1:
			searchStr += "  where o.\"ClCode1\" = :iClCode1" + " and o.\"ClCode2\" = :iClCode2" + " and  o.\"ClNo\" = :iClNo ";
			searchStr1 += " and i.\"ClCode1\" = :iClCode1" + " and i.\"ClCode2\" = :iClCode2" + " and  i.\"ClNo\" = :iClNo ";
			sqlL4965 = makeSql1(searchStr, "", "", searchStr1);
			break;
		case 2:
			if (iFacmNo == 0) {
				searchStr += "  where c.\"CustNo\" = :iCustNo";
				searchStr1 += " and o.\"CustNo\" = :iCustNo";

			} else {
				searchStr += "  where c.\"CustNo\" = :iCustNo" + " and c.\"FacmNo\" = :iFacmNo";
				searchStr1 += " and o.\"CustNo\" = :iCustNo" + " and o.\"FacmNo\" = :iFacmNo";
			}
			sqlL4965 = makeSql2(searchStr, searchStr1, titaVo);
			break;
		case 3:
			searchStr += "  where c.\"ApproveNo\" =  :iApplNo";
			sqlL4965 = makeSql2(searchStr, searchStr1, titaVo);
			break;
		case 4:
			searchInsuNo1 += "  where o.\"OrigInsuNo\" = :iNowInsuNo";
			searchInsuNo2 += "  and ( o.\"PrevInsuNo\" = :iNowInsuNo" + " or  o.\"NowInsuNo\" = :iNowInsuNo" + ")   ";
			searchStr1 += " and i.\"NowInsuNo\" = :iNowInsuNo";
			sqlL4965 = makeSql1(searchStr, searchInsuNo1, searchInsuNo2, searchStr1);
			break;
		case 5:
			searchStr += "  where o.\"InsuCompany\" = :iInsuCompany";
			searchStr1 += " and i.\"InsuCompany\" = :iInsuCompany";
			sqlL4965 = makeSql1(searchStr, "", "", searchStr1);
			break;
		case 6:
			searchStr += "  where o.\"InsuTypeCode\" = :iInsuTypeCode";
			searchStr1 += " and i.\"InsuTypeCode\" = :iInsuTypeCode";
			sqlL4965 = makeSql1(searchStr, "", "", searchStr1);
			break;
		}

		if (iEffectiveCode == 0) {
			searchStr += "  and o.\"InsuEndDate\" >= :iInsuEndDateFrom" + " and o.\"InsuEndDate\" <= :iInsuEndDateTo";
			searchStr1 += " and i.\"InsuEndDate\" >= :iInsuEndDateFrom" + " and i.\"InsuEndDate\" <= :iInsuEndDateTo";

			if (iSearchFlag == 4) {
				sqlL4965 = makeSql1(searchStr, searchInsuNo1, searchInsuNo2, searchStr1);
			} else if (iSearchFlag == 2) {
				searchStr1 = "";

				if (iFacmNo == 0) {
					searchStr1 += " and i.\"InsuEndDate\" >= :iInsuEndDateFrom" + " and i.\"InsuEndDate\" <= :iInsuEndDateTo";
					searchStr1 += " and i.\"CustNo\" = :iCustNo";

				} else {
					searchStr1 += " and i.\"InsuEndDate\" >= :iInsuEndDateFrom" + " and i.\"InsuEndDate\" <= :iInsuEndDateTo";
					searchStr1 += " and i.\"CustNo\" = :iCustNo" + " and i.\"FacmNo\" = :iFacmNo";

				}
				sqlL4965 = makeSql1(searchStr, "", "", searchStr1);
			} else if (iSearchFlag == 3) {
				sqlL4965 = makeSql2(searchStr, "", titaVo);
			} else {
				sqlL4965 = makeSql1(searchStr, "", "", searchStr1);
			}
		}

		if (iSearchFlag == 3 && iEffectiveCode == 9) {
			sqlL4965 = makeSql1(searchStr, "", "", "");
		}

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sqlL4965);

		switch (iSearchFlag) {
		case 1:
			query.setParameter("iClCode1", iClCode1);
			query.setParameter("iClCode2", iClCode2);
			query.setParameter("iClNo", iClNo);
			break;
		case 2:
			if (iFacmNo == 0) {
				query.setParameter("iCustNo", iCustNo);
			} else {
				query.setParameter("iCustNo", iCustNo);
				query.setParameter("iFacmNo", iFacmNo);
			}
			break;
		case 3:
			query.setParameter("iApplNo", iApplNo);
			break;
		case 4:
			query.setParameter("iNowInsuNo", iNowInsuNo);
			break;
		case 5:
			query.setParameter("iInsuCompany", iInsuCompany);
			break;
		case 6:
			query.setParameter("iInsuTypeCode", iInsuTypeCode);
			break;
		}

		if (iEffectiveCode == 0) {
			query.setParameter("iInsuEndDateFrom", iInsuEndDateFrom);
			query.setParameter("iInsuEndDateTo", iInsuEndDateTo);
		}

		this.info("L5959.sqlL4965=" + sqlL4965);

		cnt = query.getResultList().size();
		this.info("Total cnt ..." + cnt);

		this.index = index;
		this.limit = limit;

		// *** 折返控制相關 ***
		// 設定從第幾筆開始抓,需在createNativeQuery後設定
		query.setFirstResult(this.index * this.limit);

		// *** 折返控制相關 ***
		// 設定每次撈幾筆,需在createNativeQuery後設定
		query.setMaxResults(this.limit);

		List<Object> result = query.getResultList();

		size = result.size();
		this.info("Total size ..." + size);

		return findItem(this.convertToMap(query));

	}

	private String makeSql1(String searchStr, String searchInsuNo1, String searchInsuNo2, String searchStr1) throws LogicException {
		String sqlL4965 = "";
		sqlL4965 += "  select i.\"CustNo\"            as  F0";
		sqlL4965 += "      ,c.\"CustName\"          as  F1";
		sqlL4965 += "      ,i.\"FacmNo\"            as  F2";
		sqlL4965 += "      ,i.\"ApproveNo\"         as  F3";
		sqlL4965 += "      ,i.\"ClCode1\"           as  F4";
		sqlL4965 += "      ,i.\"ClCode2\"           as  F5";
		sqlL4965 += "      ,i.\"ClNo\"              as  F6";
		sqlL4965 += "      ,i.\"NowInsuNo\"         as  F7";
		sqlL4965 += "      ,i.\"EndoInsuNo\"        as  F8";
		sqlL4965 += "      ,i.\"InsuCompany\"       as  F9";
		sqlL4965 += "      ,i.\"FireInsuCovrg\"     as  F10";
		sqlL4965 += "      ,i.\"FireInsuPrem\"      as  F11";
		sqlL4965 += "      ,i.\"EthqInsuCovrg\"     as  F12";
		sqlL4965 += "      ,i.\"EthqInsuPrem\"      as  F13";
		sqlL4965 += "      ,case when(i.\"InsuStartDate\" - 19110000) > 0 then  i.\"InsuStartDate\" - 19110000 else 0   end  as  F14";
		sqlL4965 += "      ,case when(i.\"InsuEndDate\"   - 19110000) > 0 then  i.\"InsuEndDate\" - 19110000 else 0   end  as  F15";
		sqlL4965 += "      ,i.\"PrevInsuNo\"       as  F16";
		sqlL4965 += "      ,i.\"InsuTypeCode\"       as  F17";
		sqlL4965 += "  from ( select ";
		sqlL4965 += "         c.\"CustNo\"          as  \"CustNo\" ";
		sqlL4965 += "        ,c.\"FacmNo\"          as  \"FacmNo\" ";
		sqlL4965 += "        ,c.\"ApproveNo\"       as  \"ApproveNo\" ";
		sqlL4965 += "        ,o.\"ClCode1\"         as  \"ClCode1\" ";
		sqlL4965 += "        ,o.\"ClCode2\"         as  \"ClCode2\" ";
		sqlL4965 += "        ,o.\"ClNo\"            as  \"ClNo\" ";
		sqlL4965 += "        ,o.\"OrigInsuNo\"      as  \"NowInsuNo\" ";
		sqlL4965 += "        ,o.\"EndoInsuNo\"      as  \"EndoInsuNo\" ";
		sqlL4965 += "        ,o.\"InsuCompany\"     as  \"InsuCompany\" ";
		sqlL4965 += "        ,o.\"FireInsuCovrg\"   as  \"FireInsuCovrg\" ";
		sqlL4965 += "        ,o.\"FireInsuPrem\"    as  \"FireInsuPrem\"  ";
		sqlL4965 += "        ,o.\"EthqInsuCovrg\"   as  \"EthqInsuCovrg\" ";
		sqlL4965 += "        ,o.\"EthqInsuPrem\"    as  \"EthqInsuPrem\"  ";
		sqlL4965 += "        ,o.\"InsuStartDate\"   as  \"InsuStartDate\" ";
		sqlL4965 += "        ,o.\"InsuEndDate\"     as  \"InsuEndDate\" ";
		sqlL4965 += "        ,o.\"InsuTypeCode\"    as  \"InsuTypeCode\" ";
		sqlL4965 += "        ,null                  as  \"PrevInsuNo\" ";
		sqlL4965 += "        ,ROW_NUMBER() Over (Partition By o.\"ClCode1\", o.\"ClCode2\", o.\"ClNo\", o.\"OrigInsuNo\", o.\"EndoInsuNo\" ";
		sqlL4965 += "                             order By c.\"ApproveNo\" ASC)";
		sqlL4965 += "                             as   \"RowNo\"";
		sqlL4965 += "        from \"InsuOrignal\" o";
		sqlL4965 += "        left join \"ClFac\" c on c.\"ClCode1\" = o.\"ClCode1\"";
		sqlL4965 += "                   and c.\"ClCode2\" = o.\"ClCode2\"";
		sqlL4965 += "                   and c.\"ClNo\" = o.\"ClNo\"";
		sqlL4965 += searchStr;
		sqlL4965 += searchInsuNo1;
		sqlL4965 += "        union all";
		sqlL4965 += "        select";
		sqlL4965 += "         o.\"CustNo\"             as \"CustNo\"       ";
		sqlL4965 += "        ,o.\"FacmNo\"             as \"FacmNo\"       ";
		sqlL4965 += "        ,c.\"ApproveNo\"          as \"ApproveNo\"    ";
		sqlL4965 += "        ,o.\"ClCode1\"            as \"ClCode1\"      ";
		sqlL4965 += "        ,o.\"ClCode2\"            as \"ClCode2\"      ";
		sqlL4965 += "        ,o.\"ClNo\"               as \"ClNo\"         ";
		sqlL4965 += "        ,o.\"NowInsuNo\"          as \"NowInsuNo\"    ";
		sqlL4965 += "        ,o.\"EndoInsuNo\"         as \"EndoInsuNo\"   ";
		sqlL4965 += "        ,o.\"InsuCompany\"        as \"InsuCompany\"  ";
		sqlL4965 += "        ,o.\"FireInsuCovrg\"      as \"FireInsuCovrg\"";
		sqlL4965 += "        ,o.\"FireInsuPrem\"       as \"FireInsuPrem\" ";
		sqlL4965 += "        ,o.\"EthqInsuCovrg\"      as \"EthqInsuCovrg\"";
		sqlL4965 += "        ,o.\"EthqInsuPrem\"       as \"EthqInsuPrem\" ";
		sqlL4965 += "        ,o.\"InsuStartDate\"      as \"InsuStartDate\"";
		sqlL4965 += "        ,o.\"InsuEndDate\"        as \"InsuEndDate\"  ";
		sqlL4965 += "        ,o.\"InsuTypeCode\"       as  \"InsuTypeCode\" ";
		sqlL4965 += "        ,o.\"PrevInsuNo\"        as \"PrevInsuNo\"  ";
		sqlL4965 += "        ,ROW_NUMBER() Over (Partition By o.\"ClCode1\", o.\"ClCode2\", o.\"ClNo\", o.\"PrevInsuNo\", o.\"EndoInsuNo\"";
		sqlL4965 += "                             order By c.\"ApproveNo\" ASC)";
		sqlL4965 += "                             as   \"RowNo\"";
		sqlL4965 += "        from \"InsuRenew\" o ";
		sqlL4965 += "        left join \"ClFac\" c on c.\"ClCode1\" = o.\"ClCode1\"";
		sqlL4965 += "                   and c.\"ClCode2\" = o.\"ClCode2\"";
		sqlL4965 += "                   and c.\"ClNo\" = o.\"ClNo\"";
		sqlL4965 += "                   and c.\"CustNo\" = o.\"CustNo\"";
		sqlL4965 += "                   and c.\"FacmNo\" = o.\"FacmNo\"";
		sqlL4965 += searchStr;
		sqlL4965 += searchInsuNo2;
		sqlL4965 += "     ) i";
		sqlL4965 += "  left join \"CustMain\" c on c.\"CustNo\" = i.\"CustNo\"";
		sqlL4965 += "  where i.\"RowNo\" = 1";
		sqlL4965 += searchStr1;
		sqlL4965 += "  order by i.\"InsuEndDate\" desc";
		sqlL4965 += "        ,i.\"ClCode1\"";
		sqlL4965 += "        ,i.\"ClCode2\"";
		sqlL4965 += "        ,i.\"ClNo\"";
		return sqlL4965;
	}

	private String makeSql2(String searchStr, String searchStr1, TitaVo titaVo) throws LogicException {

		String sqlL4965 = "";
		sqlL4965 += "select i.\"CustNo\"            as  F0";
		sqlL4965 += "      ,c.\"CustName\"          as  F1";
		sqlL4965 += "      ,i.\"FacmNo\"            as  F2";
		sqlL4965 += "      ,i.\"ApproveNo\"         as  F3";
		sqlL4965 += "      ,i.\"ClCode1\"           as  F4";
		sqlL4965 += "      ,i.\"ClCode2\"           as  F5";
		sqlL4965 += "      ,i.\"ClNo\"              as  F6";
		sqlL4965 += "      ,i.\"NowInsuNo\"         as  F7";
		sqlL4965 += "      ,i.\"EndoInsuNo\"        as  F8";
		sqlL4965 += "      ,i.\"InsuCompany\"       as  F9";
		sqlL4965 += "      ,i.\"FireInsuCovrg\"     as  F10";
		sqlL4965 += "      ,i.\"FireInsuPrem\"      as  F11";
		sqlL4965 += "      ,i.\"EthqInsuCovrg\"     as  F12";
		sqlL4965 += "      ,i.\"EthqInsuPrem\"      as  F13";
		sqlL4965 += "      ,case when(i.\"InsuStartDate\" - 19110000) > 0 then  i.\"InsuStartDate\" - 19110000 else 0   end  as  F14";
		sqlL4965 += "      ,case when(i.\"InsuEndDate\"   - 19110000) > 0 then  i.\"InsuEndDate\" - 19110000 else 0   end  as  F15";
		sqlL4965 += "      ,i.\"PrevInsuNo\"       as  F16";
		sqlL4965 += "      ,i.\"InsuTypeCode\"      as  F17";
		sqlL4965 += "  from ( select ";
		sqlL4965 += "         c.\"CustNo\"          as  \"CustNo\" ";
		sqlL4965 += "        ,c.\"FacmNo\"          as  \"FacmNo\" ";
		sqlL4965 += "        ,c.\"ApproveNo\"       as  \"ApproveNo\" ";
		sqlL4965 += "        ,c.\"ClCode1\"         as  \"ClCode1\" ";
		sqlL4965 += "        ,c.\"ClCode2\"         as  \"ClCode2\" ";
		sqlL4965 += "        ,c.\"ClNo\"            as  \"ClNo\" ";
		sqlL4965 += "        ,o.\"OrigInsuNo\"      as  \"NowInsuNo\" ";
		sqlL4965 += "        ,o.\"EndoInsuNo\"      as  \"EndoInsuNo\" ";
		sqlL4965 += "        ,o.\"InsuCompany\"     as  \"InsuCompany\" ";
		sqlL4965 += "        ,o.\"FireInsuCovrg\"   as  \"FireInsuCovrg\" ";
		sqlL4965 += "        ,o.\"FireInsuPrem\"    as  \"FireInsuPrem\"  ";
		sqlL4965 += "        ,o.\"EthqInsuCovrg\"   as  \"EthqInsuCovrg\" ";
		sqlL4965 += "        ,o.\"EthqInsuPrem\"    as  \"EthqInsuPrem\"  ";
		sqlL4965 += "        ,o.\"InsuStartDate\"   as  \"InsuStartDate\" ";
		sqlL4965 += "        ,o.\"InsuEndDate\"     as  \"InsuEndDate\" ";
		sqlL4965 += "        ,o.\"InsuTypeCode\"    as  \"InsuTypeCode\" ";
		sqlL4965 += "        ,null                  as \"PrevInsuNo\"  ";
		sqlL4965 += "        from   \"ClFac\" c     ";
		sqlL4965 += "        left join \"InsuOrignal\" o";
		sqlL4965 += "                    on o.\"ClCode1\" = c.\"ClCode1\"";
		sqlL4965 += "                   and o.\"ClCode2\" = c.\"ClCode2\"";
		sqlL4965 += "                   and o.\"ClNo\" = c.\"ClNo\"";
		sqlL4965 += searchStr;
		sqlL4965 += "        union all";
		sqlL4965 += "        select";
		sqlL4965 += "         c.\"CustNo\"             as \"CustNo\"       ";
		sqlL4965 += "        ,c.\"FacmNo\"             as \"FacmNo\"       ";
		sqlL4965 += "        ,c.\"ApproveNo\"          as \"ApproveNo\"    ";
		sqlL4965 += "        ,c.\"ClCode1\"            as \"ClCode1\"      ";
		sqlL4965 += "        ,c.\"ClCode2\"            as \"ClCode2\"      ";
		sqlL4965 += "        ,c.\"ClNo\"               as \"ClNo\"         ";
		sqlL4965 += "        ,o.\"NowInsuNo\"          as \"NowInsuNo\"    ";
		sqlL4965 += "        ,o.\"EndoInsuNo\"         as \"EndoInsuNo\"   ";
		sqlL4965 += "        ,o.\"InsuCompany\"        as \"InsuCompany\"  ";
		sqlL4965 += "        ,o.\"FireInsuCovrg\"      as \"FireInsuCovrg\"";
		sqlL4965 += "        ,o.\"FireInsuPrem\"       as \"FireInsuPrem\" ";
		sqlL4965 += "        ,o.\"EthqInsuCovrg\"      as \"EthqInsuCovrg\"";
		sqlL4965 += "        ,o.\"EthqInsuPrem\"       as \"EthqInsuPrem\" ";
		sqlL4965 += "        ,o.\"InsuStartDate\"      as \"InsuStartDate\"";
		sqlL4965 += "        ,o.\"InsuEndDate\"        as \"InsuEndDate\"  ";
		sqlL4965 += "        ,o.\"InsuTypeCode\"    as  \"InsuTypeCode\" ";
		sqlL4965 += "        ,o.\"PrevInsuNo\"         as \"PrevInsuNo\"  ";
		sqlL4965 += "        from   \"ClFac\" c  ";
		sqlL4965 += "        left join \"InsuRenew\" o";
		sqlL4965 += "                    on o.\"ClCode1\" = c.\"ClCode1\"";
		sqlL4965 += "                   and o.\"ClCode2\" = c.\"ClCode2\"";
		sqlL4965 += "                   and o.\"ClNo\" = c.\"ClNo\"";

		sqlL4965 += searchStr;
		sqlL4965 += searchStr1;
		sqlL4965 += "     ) i";
		sqlL4965 += "  left join \"CustMain\" c on c.\"CustNo\" = i.\"CustNo\"";
		sqlL4965 += "  order by i.\"InsuEndDate\" desc";
		sqlL4965 += "        ,i.\"ClCode1\"";
		sqlL4965 += "        ,i.\"ClCode2\"";
		sqlL4965 += "        ,i.\"ClNo\"";
		return sqlL4965;
	}

	public List<String[]> findItem(List<Map<String, String>> lObject) throws LogicException {
		List<String[]> data = new ArrayList<String[]>();
		if (lObject != null && lObject.size() != 0) {
			int col = lObject.get(0).keySet().size();
			for (Map<String, String> MapObj : lObject) {
				String row[] = new String[col];
				for (int i = 0; i < col; i++) {
					row[i] = MapObj.get("F" + String.valueOf(i));
					if (row[i] != null && row[i].length() != 0)
						;
					else
						row[i] = "";

				}
				data.add(row);
			}
		}
		return data;
	}

	public int getSize() {
		return cnt;
	}

}