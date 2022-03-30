package com.st1.ifx.service.springjpa;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

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
import com.google.gson.reflect.TypeToken;
import com.querydsl.core.NonUniqueResultException;
import com.st1.ifx.domain.HelpList;
import com.st1.ifx.etc.Pair;
import com.st1.ifx.etc.SomeHelper;
import com.st1.ifx.filter.FilterUtils;
import com.st1.ifx.repository.HelpListRepository;
import com.st1.ifx.service.HelpListService;

@Service("helpListService")
@Repository
public class HelpListServiceImpl implements HelpListService, InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(HelpListServiceImpl.class);

	@Autowired
	private HelpListRepository helpListRepository;

	@PersistenceContext
	private EntityManager em;

	@Override
	@Transactional(readOnly = true)
	public Optional<HelpList> findById(Long id) {
		return helpListRepository.findById(id);
	}

	private static final String QUERY_ALL_DB2 = "select help,segment from " + " fx_help_list h group by help,segment "
			+ " order by help, segment ";

	@Override
	@Transactional(readOnly = true)
	public List<Pair<String, String>> findAllSegments() {
		List<Pair<String, String>> list = new ArrayList<Pair<String, String>>();
		List result = em.createNativeQuery(QUERY_ALL_DB2).getResultList();
		for (Iterator iter = result.iterator(); iter.hasNext();) {
			Object[] values = (Object[]) iter.next();
			list.add(Pair.of((String) values[0], (String) values[1]));
		}
		return list;
	}

	private static final String QUERY_ACTIVE_JSON_DB2 = "SELECT json FROM fx_help_list "
			+ " WHERE help=:help AND segment=:segment "
			+ " AND active_date <= VARCHAR_FORMAT(CURRENT DATE, 'YYYYMMDD') " + " ORDER BY  active_date, version DESC "
			+ " FETCH FIRST 1 ROWS ONLY ";

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.st1.ifx.service.HelpListService#findJson(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	@Cacheable("helpList")
	@Transactional(readOnly = true)
	public String findJson(String help, String segment) {
		logger.info(FilterUtils.escape("finding json for :" + help + ", " + segment));
		// String query =
		// "select h from HelpList h where h.help=:help and h.segment=:segment and
		// h.activeDate <= :dt order by h.activeDate desc, h.version desc";
		TypedQuery<HelpList> q = em.createNamedQuery("HelpList.findActiveHelp", HelpList.class);
		q.setParameter("help", help);
		q.setParameter("segment", segment);
		q.setParameter("dt", SomeHelper.formatDate8(new Date()));
		q.setMaxResults(1);
		// HelpList helpList = q.getSingleResult();

		HelpList helpList = getSingleResultOrNull(q.getResultList());
		return helpList.getJson();

	}

	@Override
	@Cacheable("helpList")
	public List<Pair<String, String>> findList(String help, String segment, String valueTag, String labelTag) {
		List<Map<String, String>> tempObj = getSegment(help, segment);

		List<Pair<String, String>> pairs = new ArrayList<Pair<String, String>>();
		for (Map<String, String> m : tempObj) {
			pairs.add(Pair.of(m.get(valueTag), m.get(labelTag)));
		}

		return pairs;
	}

	@Override
	@Cacheable("helpList")
	public String[] findValues(String help, String segment, int colIndex) {
		try {
			List<Map<String, String>> list = getSegment(help, segment);
			String[] colNames = new String[0];
			colNames = list.get(0).keySet().toArray(colNames);
			for (int i = 0; i < colNames.length; i++)
				logger.debug(colNames[i]);

			String name = colNames[colIndex];

			String[] result = new String[list.size()];
			int i = 0;
			for (Map<String, String> m : list) {
				result[i++] = m.get(name);
			}
			return result;
		} catch (Exception ex) {
			StringWriter errors = new StringWriter();
			ex.printStackTrace(new PrintWriter(errors));
			logger.warn(errors.toString());
			return new String[0];
		}
	}

	private List<Map<String, String>> getSegment(String help, String segment) {
		String json = findJson(help, segment);
		logger.info(FilterUtils.escape("json:" + json));
		// Gson gson = new Gson();
		Gson gson = new GsonBuilder().setPrettyPrinting().create();

		Type type = new TypeToken<List<Map<String, String>>>() {
		}.getType();
		return gson.fromJson(json, type);

	}

	@Override
	@Transactional
	public HelpList save(HelpList helpList) {
		if (helpList.getId() == null) {
			logger.info("inserting new HelpList");
			em.persist(helpList);
		} else {
			logger.info("updating existing HelpList");
			em.merge(helpList);
		}
		logger.info("HelpList saved with id:" + helpList.getId());
		return helpList;
	}

	@Override
	@Transactional
	public void delete(HelpList helpList) {
		HelpList mergedHelpList = em.merge(helpList);
		em.remove(mergedHelpList);
		logger.info("HelpList with id: " + helpList.getId() + " deleted successfully");
	}

	@Override
	@CacheEvict(value = "helpList", allEntries = true)
	public void evict() {
		logger.info("evict helpList");

	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.assertNotNull(em);
		Assert.assertNotNull(helpListRepository);

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
}
