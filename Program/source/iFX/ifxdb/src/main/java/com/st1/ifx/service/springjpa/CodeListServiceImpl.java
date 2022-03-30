package com.st1.ifx.service.springjpa;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.junit.Assert;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.querydsl.core.NonUniqueResultException;
import com.st1.ifx.domain.CodeList;
import com.st1.ifx.etc.Pair;
import com.st1.ifx.file.item.general.GeneralHLine;
import com.st1.ifx.filter.FilterUtils;
import com.st1.ifx.repository.CodeListRepository;
import com.st1.ifx.service.CodeListService;

//TODO 中心如果寫錯參數，則會導致刪除失敗，故可能需要在刪除前 先查詢一次。 例:UserPubServiceImpl removeKeyDo()
@Service("codeListService")
@Repository
@Transactional
public class CodeListServiceImpl implements CodeListService, InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(CodeListServiceImpl.class);

	@Autowired
	private CodeListRepository repository;

	@PersistenceContext
	private EntityManager em;

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.assertNotNull(em);
		Assert.assertNotNull(repository);
	}

	@Override
	public CodeList save(CodeList codeList) {
		logger.info("save CodeList:" + codeList);
		codeList.touch();
		logger.info("getDisplayOrder:" + codeList.getDisplayOrder() + ".");
		logger.info("getKey trim:" + codeList.getXey().trim() + ".");
		if (codeList.getXey().trim().equals("HEAD")) {
			logger.info("is HEAD!");
			codeList.setDisplayOrder(0);
		} else if (codeList.getDisplayOrder() == 0) {
			logger.info("not HEAD! but Order 0!");
			codeList.setDisplayOrder(countSegment(codeList.getHelp(), codeList.getSegment()));
			logger.info("CodeList DisplayOrder: 0 ->" + codeList.getDisplayOrder());
		}
		return this.repository.save(codeList);
	}

	@Override
	public void updateList(List<GeneralHLine> list) {
		for (GeneralHLine generalHLine : list) {
			updateByFlag(generalHLine);
			logger.info("GeneralHLine:" + generalHLine);
			logger.info("list:" + list);
		}
	}

	@Override
	public void updateByFlag(GeneralHLine r) {
		logger.info("Bhelp:" + r.getHelp());
		logger.info("BgetId:" + r.getId());
		logger.info("BgetKey:" + r.getKi());
		logger.info("BgetOpfg:" + r.getOpfg());
		logger.info("BgetDisp:" + r.getDisp());
		logger.info("BgetContent:" + r.getContent());
		logger.info("BtoCodeList" + r.toCodeList());

		logger.info("GeneralHLine ??:" + r.isinsertupdate());
		if (r.isinsertupdate()) {
			logger.info("isinsertupdate");
			this.insertupdate(r.getHelp(), r.getId(), r.getKi(), r.getContent(), r.toCodeList());
		} else if (r.isDeleteRecord()) {
			logger.info("isDeleteRecord");
			this.removeKey(r.getHelp(), r.getId(), r.getKi());
		} else if (r.isDeleteSegment()) {
			logger.info("isDeleteSegment");
			this.removeSegment(r.getHelp(), r.getId());
		} else {
			logger.info("nono");
			this.save(r.toCodeList());
		}
	}

	@Override
	public void removeHelp(String help) {
		logger.info("remove all help:" + help);
		Query q = em.createQuery("delete from CodeList c where c.help=:help");
		q.setParameter("help", help);
		int i = q.executeUpdate();
		em.flush();
		logger.info("remove...done!!, remove " + i + " records");
	}

	@Override
	public void removeSegment(String help, String segment) {
		logger.info("remove by help:" + help + ", segment:" + segment);
		Query q = em.createQuery("delete from CodeList c where c.help=:help and c.segment=:segment");
		q.setParameter("help", help);
		q.setParameter("segment", segment);
		int i = q.executeUpdate();
		logger.info("remove...done!!, remove " + i + " records");
		em.flush();
	}

	@Override
	public int countSegment(String help, String segment) {
		logger.info("count by help:" + help + ", segment:" + segment);
		Query q = em.createQuery("select count(*) from CodeList c where c.help=:help and c.segment=:segment");
		q.setParameter("help", help);
		q.setParameter("segment", segment);
		List listresult = q.getResultList();
		// Object i = q.getSingleResult();
		Object i = getSingleResultOrNull(listresult);
		if (i == null) {
			i = "0";
		}
		logger.info("count...done!!, count:" + FilterUtils.escape(i));
		// 不清楚 SELECT 是否有需要 flush?
		em.flush();
		return Integer.parseInt(i.toString());
	}

	@Override
	public void removeKey(String help, String segment, String key) {
		logger.info("remove by help:" + help + ", segment:" + segment + ", key:" + key);
		Query q = em.createQuery("delete from CodeList c where c.help=:help and c.segment=:segment and c.xey=:key");
		q.setParameter("help", help);
		q.setParameter("segment", segment);
		q.setParameter("key", key);
		int i = q.executeUpdate();
		em.flush(); // 潘 20171220
		logger.info("remove...done!!, remove " + i + " records");

	}

	public void insertupdate(String help, String segment, String key, String data, CodeList codeList) {
		logger.info("insertupdate");
		logger.info("remove by help:" + help + ", segment:" + segment + ", key:" + key + ", data:" + data);
		Query q = em.createQuery("delete from CodeList c where c.help=:help and c.segment=:segment and c.xey=:key");
		q.setParameter("help", help);
		q.setParameter("segment", segment);
		q.setParameter("key", key);
		int i = q.executeUpdate();

		logger.info("remove...done!!, remove " + i + " records");
		em.flush(); // 潘 20171220

		logger.info("insertupdate save");
		this.save(codeList);

	}

	@Override
	@Transactional(readOnly = true)
	public List<CodeList> findBySegment(String help, String segment) {
		return repository.findByHelpAndSegment(help, segment);
	}

	@Override
	@Transactional(readOnly = true)
	public CodeList findByKey(String help, String segment, String key) {
		return repository.findByHelpAndSegmentAndXey(help, segment, key);

	}

	@SuppressWarnings("rawtypes")
	@Override
	@Transactional(readOnly = true)
	public List getBySegment(String help, String segment) {
		// create a JPA QUERY
		List result = em.createQuery("select c.xey,c.content from CodeList c where c.help=:help and c.segment=:segment order by c.displayOrder").setParameter("help", help)
				.setParameter("segment", segment).getResultList();
		return result;
	}

	@SuppressWarnings("rawtypes")
	@Override
	@Transactional(readOnly = true)
	public Map<String, List<String>> getBySegmentAsMap(String help, String segment) {
		List r = getBySegment(help, segment);
		if (r == null || r.size() == 0)
			return null;

		Map<String, List<String>> result = new HashMap<String, List<String>>();

		List<String> fild = null;
		int i = 0;
		String key, content;
		for (Iterator iter = r.iterator(); iter.hasNext(); i++) {
			Object[] values = (Object[]) iter.next();
			key = (String) values[0] == null ? "" : (String) values[0];
			content = (String) values[1];
			if (i == 0) {
				fild = new ArrayList<String>(Arrays.asList(content.split(",")));
				result.put("FILD", fild);
				for (String colName : fild) {
					result.put(colName, new ArrayList<String>());
				}
			} else {
				content = key + "," + content;
				String[] ss = content.split(",");
				int j = 0;
				if (fild != null) {
					for (String colName : fild) {
						if (j < ss.length) {
							result.get(colName).add(ss[j]);
						}
						j++;
					}
				}
			}
		}
		return result;
	}

	// private static final String NATIVE_QUERY_ALL_DB2 = "select help,segment from
	// " + "
	// IFX.FX_CODE_LIST h group by help,segment "
	// + " order by help, segment ";

	@SuppressWarnings("rawtypes")
	@Override
	@Transactional(readOnly = true)
	public List<Pair<String, String>> findAllSegments() {
		String NATIVE_QUERY_ALL_DB2 = "select help,segment from " + "FX_CODE_LIST h group by help,segment " + " order by help, segment ";
		List<Pair<String, String>> list = new ArrayList<Pair<String, String>>();
		List result = em.createNativeQuery(NATIVE_QUERY_ALL_DB2).getResultList();
		for (Iterator iter = result.iterator(); iter.hasNext();) {
			Object[] values = (Object[]) iter.next();
			list.add(Pair.of((String) values[0], (String) values[1]));
		}
		logger.info("Help.js findAllSegments list size :" + list.size());
		return list;
	}

	@Override
	@Transactional(readOnly = true)
	@Cacheable("codeList")
	public String buildJS() {
		logger.info("generate help.js from code_list (buildJS)");
		Map<String, Map<String, List<String>>> result = readAllSegments();
		Gson gson = new Gson();
		String s = "var Helpfile=\n" + gson.toJson(result) + ";\n";
//		logger.info(FilterUtils.escape("===> help.js:\n" + s));
		return s;
	}

	@Override
	@Transactional(readOnly = true)
	public String buildJS_noCache(boolean pretty) {
		logger.info("generate help.js from code_list (buildJS_noCache)");
		Map<String, Map<String, List<String>>> result = readAllSegments();
		Gson gson;
		if (pretty)
			gson = new GsonBuilder().setPrettyPrinting().create();
		else
			gson = new Gson();

		String s = "var Helpfile=\n" + gson.toJson(result) + ";\n";
//		logger.info(FilterUtils.escape("===> help.js:\n" + s));
		return s;
	}

	private Map<String, Map<String, List<String>>> readAllSegments() {
		Map<String, Map<String, List<String>>> result = new HashMap<String, Map<String, List<String>>>();

		List<Pair<String, String>> pairList = findAllSegments();
		for (Pair<String, String> pair : pairList) {
			String k = pair.first + "." + pair.second;
			Map<String, List<String>> m = getBySegmentAsMap(pair.first, pair.second);
			result.put(k, m);
		}
		return result;
	}

	@Override
	@CacheEvict(value = "codeList", allEntries = true)
	public void evict() {
		logger.info("evict code List");

	}

	public static <PP> PP getSingleResultOrNull(List<PP> results) {
		PP foundEntity = null;
		if (!results.isEmpty()) {
			foundEntity = results.get(0);
		}
		if (results.size() > 1) {
			for (PP result : results) {
				if (result != foundEntity) {
					throw new NonUniqueResultException();
				}
			}
		}
		return foundEntity;
	}

};
