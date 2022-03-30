package com.st1.ifx.service.springjpa;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.junit.Assert;

import com.st1.ifx.domain.TranDoc;
import com.st1.ifx.domain.TranDocBuf;
import com.st1.ifx.domain.TranDoc_;
import com.st1.ifx.filter.FilterUtils;
import com.st1.ifx.repository.TranDocRepository;
import com.st1.ifx.service.TranDocService;

@Service("tranDocService")
@Repository
@Transactional
public class TranDocServiceImpl implements TranDocService, InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(TranDocServiceImpl.class);

	@Autowired
	private TranDocRepository repository;

	@PersistenceContext
	private EntityManager em;

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.assertNotNull(em);
		Assert.assertNotNull(repository);

	}

	@Override
	@Transactional(readOnly = true)
	public List<TranDoc> findByJnlId(Long jnlId, boolean bLazy) {
		List<TranDoc> list = this.repository.findByJnlId(jnlId);
		if (!bLazy) {
			for (TranDoc d : list)
				d.getBuffers().size();
		}
		return list;
	}

	@Override
	public void save(List<TranDoc> tranDocList) {
		this.repository.saveAll(tranDocList);
	}

	@Override
	public void deleteByJnlId(Long jnlId) {
		List<TranDoc> list = this.findByJnlId(jnlId, true);
		this.repository.deleteAll(list);
	}

	static int BUFSIZE = 1500;

	@Override
	public TranDoc save(TranDoc tmpTranDoc, Long jnlId, String docName, String docPrompt, String docParameter,
			String content) {
		TranDoc doc = new TranDoc();
		doc.setJnlId(jnlId);
		doc.setDocName(docName);
		doc.setDocPrompt(docPrompt);
		doc.setDocParameter(docParameter);
		// 新加入看看
		logger.info("Save Now jnl id:" + jnlId);
		logger.info("Save global jnl id:" + tmpTranDoc.getJnlId());
		if (tmpTranDoc.getJnlId().equals(jnlId)) {
			logger.info("相同 把暫存值塞回");
			logger.info("getSrhRbrno:" + FilterUtils.escape(tmpTranDoc.getSrhRbrno()));
			logger.info("getSrhFbrno:" + FilterUtils.escape(tmpTranDoc.getSrhFbrno()));
			logger.info("getSrhAcbrno:" + FilterUtils.escape(tmpTranDoc.getSrhAcbrno()));
			logger.info("getSrhPbrno:" + FilterUtils.escape(tmpTranDoc.getSrhPbrno()));
			logger.info("getSrhCifkey:" + FilterUtils.escape(tmpTranDoc.getSrhCifkey()));
			logger.info("getSrhKinbr:" + FilterUtils.escape(tmpTranDoc.getSrhKinbr()));
			logger.info("getSrhMrkey:" + FilterUtils.escape(tmpTranDoc.getSrhMrkey()));
			logger.info("getSrhCurrency:" + FilterUtils.escape(tmpTranDoc.getSrhCurrency()));
			logger.info("getSrhTxamt:" + FilterUtils.escape(tmpTranDoc.getSrhTxamt()));
			logger.info("getSrhBusdate:" + FilterUtils.escape(tmpTranDoc.getSrhBusdate()));
			logger.info("getSrhBatno:" + FilterUtils.escape(tmpTranDoc.getSrhBatno()));
			logger.info("getSrhTxcode:" + FilterUtils.escape(tmpTranDoc.getSrhTxcode()));
			logger.info("getSrhTemp:" + FilterUtils.escape(tmpTranDoc.getSrhTemp()));
			doc.setSrhMrkey(tmpTranDoc.getSrhMrkey());
			doc.setSrhCurrency(tmpTranDoc.getSrhCurrency());
			doc.setSrhTxamt(tmpTranDoc.getSrhTxamt());

			doc.setSrhRbrno(tmpTranDoc.getSrhRbrno());
			doc.setSrhFbrno(tmpTranDoc.getSrhFbrno());
			doc.setSrhKinbr(tmpTranDoc.getSrhKinbr());
			doc.setSrhAcbrno(tmpTranDoc.getSrhAcbrno());
			doc.setSrhPbrno(tmpTranDoc.getSrhPbrno());
			doc.setSrhCifkey(tmpTranDoc.getSrhCifkey());
			doc.setSrhBatno(tmpTranDoc.getSrhBatno());
			doc.setSrhBusdate(tmpTranDoc.getSrhBusdate());
			doc.setSrhTxcode(tmpTranDoc.getSrhTxcode());
			doc.setSrhTemp(tmpTranDoc.getSrhTemp());
		}
		// tmpTranDoc........
		int bufIndex = 0;
		int offset = 0;
		int endPos;
		while (offset < content.length()) {
			TranDocBuf buf = new TranDocBuf();
			buf.setBufIndex(bufIndex++);
			endPos = offset + BUFSIZE;
			endPos = endPos > content.length() ? content.length() : endPos;
			String text = content.substring(offset, endPos);
			buf.setBuffer(text);
			doc.addBuffer(buf);
			offset += BUFSIZE;
		}

		TranDoc temp = this.repository.save(doc);
		return temp;
	}

	@Override
	@Transactional(readOnly = true)
	public TranDoc getByDocId(Long docId) {
		TranDoc doc = this.repository.findByDocIdEager(docId);
		return doc;
	}

	@Override
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	public List<TranDoc> findByCriteriaQuery(HashMap<String, String> requestMap) {
		String brn = requestMap.get("brn");
		String supbrn = requestMap.get("supbrn");
		String tlrno = requestMap.get("tlrno");
		String docname = requestMap.get("docname");
		String txcode = requestMap.get("txcode");
		String cifkey = requestMap.get("srhcifkey");
		String batno = requestMap.get("batno");
		String mrkey1 = requestMap.get("mrkey1");
		String mrkey2 = requestMap.get("mrkey2");
		String dateFrom = requestMap.get("dateFrom");
		String dateTo = requestMap.get("dateTo");
		logger.info(FilterUtils.escape("brn:" + brn + ",supbrn:" + supbrn + ",tlrno:" + tlrno + ",docname:" + docname
				+ ",txcode:" + txcode + ",cifkey:" + cifkey + ",batno:" + batno + ",mrkey1:" + mrkey1 + ",mrkey2:"
				+ mrkey2 + ",dateFrom:" + dateFrom + ",dateTo:" + dateTo));

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<TranDoc> criteriaQuery = cb.createQuery(TranDoc.class);

		Root<TranDoc> trnRoot = criteriaQuery.from(TranDoc.class);
		criteriaQuery.select(trnRoot);
		Predicate criteria = cb.conjunction();

		Predicate p;
		if (isNotEmpty(docname)) {
			p = cb.equal(trnRoot.get(TranDoc_.docName), docname);
			criteria = cb.and(criteria, p);
		}

		if (isNotEmpty(supbrn)) {
			p = cb.or(cb.equal(trnRoot.get(TranDoc_.srhRbrno), supbrn),
					cb.equal(trnRoot.get(TranDoc_.srhFbrno), supbrn), cb.equal(trnRoot.get(TranDoc_.srhKinbr), supbrn),
					cb.equal(trnRoot.get(TranDoc_.srhAcbrno), supbrn),
					cb.equal(trnRoot.get(TranDoc_.srhPbrno), supbrn));
			criteria = cb.and(criteria, p);
		}
		if (isNotEmpty(brn)) {
			p = cb.equal(trnRoot.get(TranDoc_.srhKinbr), brn);
			criteria = cb.and(criteria, p);
		}
		if (isNotEmpty(cifkey)) {
			p = cb.equal(trnRoot.get(TranDoc_.srhCifkey), cifkey);
			criteria = cb.and(criteria, p);
		}
		if (isNotEmpty(batno)) {
			p = cb.equal(trnRoot.get(TranDoc_.srhBatno), batno);
			criteria = cb.and(criteria, p);
		}

		// 缺少 mrkey 要如何量化??

		if (isNotEmpty(dateFrom)) {
			if (isNotEmpty(dateTo)) { // beteeen dateFrom and dateTo
				p = cb.greaterThanOrEqualTo(trnRoot.get(TranDoc_.srhBusdate), dateFrom);
				criteria = cb.and(criteria, p);
				p = cb.lessThanOrEqualTo(trnRoot.get(TranDoc_.srhBusdate), dateTo);
				criteria = cb.and(criteria, p);
			} else { // equals to dateFrom
				p = cb.equal(trnRoot.get(TranDoc_.srhBusdate), dateFrom);
				criteria = cb.and(criteria, p);
			}
		}
		if (isNotEmpty(mrkey1)) {
			if (isNotEmpty(mrkey2)) { // beteeen mrkey1 and mrkey2
				p = cb.greaterThanOrEqualTo(trnRoot.get(TranDoc_.srhMrkey), mrkey1);
				criteria = cb.and(criteria, p);
				p = cb.lessThanOrEqualTo(trnRoot.get(TranDoc_.srhMrkey), mrkey2);
				criteria = cb.and(criteria, p);
			} else { // equals to dateFrom
				p = cb.equal(trnRoot.get(TranDoc_.srhMrkey), mrkey1);
				criteria = cb.and(criteria, p);
			}
		}

		criteriaQuery.where(criteria);

		criteriaQuery.orderBy(cb.desc(trnRoot.get(TranDoc_.srhBusdate)));

		List<TranDoc> result = em.createQuery(criteriaQuery).getResultList();
		return result;
	}

	@SuppressWarnings({ "rawtypes" })
	@Override
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	public List findByCriteriaQuerylast(HashMap<String, String> requestMap) {

		List<String> docnames = null;

		String brn = requestMap.get("brn");
		String rbrno = requestMap.get("rbrno");
		String fbrno = requestMap.get("fbrno");
		String acbrno = requestMap.get("acbrno");
		String pbrno = requestMap.get("pbrno");
		String tlrno = requestMap.get("tlrno");
		String docname = requestMap.get("docname");
		String txcode = requestMap.get("txcode");
		String cifkey = requestMap.get("srhcifkey");
		String batno = requestMap.get("batno");
		String mrkey1 = requestMap.get("mrkey1");
		String mrkey2 = requestMap.get("mrkey2");
		String dateFrom = requestMap.get("dateFrom");
		String dateTo = requestMap.get("dateTo");
		String firsttime = requestMap.get("firsttime");
		if (isNotEmpty(brn)) {
			brn = brn.trim();
		} else {
			brn = null;
		}
		if (isNotEmpty(rbrno)) {
			rbrno = rbrno.trim();
		} else {
			rbrno = null;
		}
		if (isNotEmpty(fbrno)) {
			fbrno = fbrno.trim();
		} else {
			fbrno = null;
		}
		if (isNotEmpty(acbrno)) {
			acbrno = acbrno.trim();
		} else {
			acbrno = null;
		}
		if (isNotEmpty(pbrno)) {
			pbrno = pbrno.trim();
		} else {
			pbrno = null;
		}
		if (isNotEmpty(tlrno)) {
			tlrno = tlrno.trim();
		} else {
			tlrno = null;
		}

		if (isNotEmpty(docname)) {
			logger.info(FilterUtils.escape("docname:" + docname));
			String[] items = docname.split(":");
			logger.info("items len:" + items.length);
			docnames = Arrays.asList(items);
			logger.info("docnames size:" + docnames.size());
			// List<String> docnames = new
			// ArrayList<String>(Arrays.asList(docname.split(",")));
		}

		if (isNotEmpty(txcode)) {
			txcode = txcode.trim();
		} else {
			txcode = null;
		}
		if (isNotEmpty(cifkey)) {
			cifkey = cifkey.trim();
		} else {
			cifkey = null;
		}
		if (isNotEmpty(batno)) {
			batno = batno.trim();
		} else {
			batno = null;
		}
		if (isNotEmpty(mrkey1)) {
			mrkey1 = mrkey1.trim();
		} else {
			mrkey1 = null;
		}
		if (isNotEmpty(mrkey2)) {
			mrkey2 = mrkey2.trim();
		} else {
			mrkey2 = null;
		}
		if (isNotEmpty(dateFrom)) {
			dateFrom = dateFrom.trim();
		} else {
			dateFrom = null;
		}
		if (isNotEmpty(dateTo)) {
			dateTo = dateTo.trim();
		} else {
			dateTo = null;
		}
		if (isNotEmpty(firsttime)) {
			firsttime = firsttime.trim();
		} else {
			firsttime = null;
		}
		logger.info(FilterUtils.escape("brn:" + brn + ",rbrno:" + rbrno + ",fbrno:" + fbrno + ",acbrno:" + acbrno
				+ ",pbrno:" + pbrno + ",tlrno:" + tlrno + ",docname:" + docname + ",txcode:" + txcode + ",cifkey:"
				+ cifkey + ",batno:" + batno + ",mrkey1:" + mrkey1 + ",mrkey2:" + mrkey2 + ",dateFrom:" + dateFrom
				+ ",dateTo:" + dateTo + ",firsttime:" + firsttime));

		String queryttext = "SELECT a.docId,a.jnlId,a.srhKinbr,a.srhRbrno,a.srhFbrno,a.srhAcbrno,a.srhPbrno,a.srhCifkey,a.srhMrkey,a.srhCurrency,a.srhTxamt,a.srhBatno,a.srhBusdate,a.docName,a.docPrompt,a.srhTxcode FROM TranDoc a ";
		String fastqueryd = "", fastqueryk = "";

		Query query;
		queryttext += " WHERE a.docName IN (:docnames) "; // 一定要必填了
		queryttext += " AND a.srhKinbr = COALESCE(:brn,a.srhKinbr) ";
		queryttext += " AND a.srhCifkey = COALESCE(:cifkey,a.srhCifkey) ";
		queryttext += " AND a.srhBatno = COALESCE(:batno,a.srhBatno) ";
		queryttext += " AND a.srhRbrno = COALESCE(:rbrno,a.srhRbrno) ";
		// 合庫增加需求,如果是查詢受理行,則不需要查到 受理=指定的資料
		if (rbrno != null) {
			queryttext += " AND a.srhFbrno != :rbrno ";
		}
		queryttext += " AND a.srhFbrno = COALESCE(:fbrno,a.srhFbrno) ";
		queryttext += " AND a.srhAcbrno = COALESCE(:acbrno,a.srhAcbrno) ";
		queryttext += " AND a.srhPbrno = COALESCE(:pbrno,a.srhPbrno) ";
		logger.info("queryttext supbrn:" + queryttext);
		if (isNotEmpty(mrkey1) && isNotEmpty(mrkey2)) { // beteeen dateFrom and dateTo
			queryttext += "AND a.srhMrkey BETWEEN :mrkey1 AND :mrkey2 ";
		} else { // equals to dateFrom
			queryttext += "AND a.srhMrkey = COALESCE(:mrkey1,a.srhMrkey) AND a.srhMrkey = COALESCE(:mrkey2,a.srhMrkey) ";
		}
		logger.info("queryttext mrkey:" + queryttext);

		if (isNotEmpty(dateFrom) && isNotEmpty(dateTo)) { // beteeen mrkey1 and mrkey2
			queryttext += "AND a.srhBusdate BETWEEN :dateFrom AND :dateTo ";
			fastqueryd = "AND d.srhBusdate BETWEEN :dateFrom AND :dateTo ";
			fastqueryk = "AND k.srhBusdate BETWEEN :dateFrom AND :dateTo ";
		} else { // equals to dateFrom
			queryttext += "AND a.srhBusdate = COALESCE(:dateFrom,a.srhBusdate) AND a.srhBusdate = COALESCE(:dateTo,a.srhBusdate) ";
			fastqueryd = "AND d.srhBusdate = COALESCE(:dateFrom,d.srhBusdate) AND d.srhBusdate = COALESCE(:dateTo,d.srhBusdate) ";
			fastqueryk = "AND k.srhBusdate = COALESCE(:dateFrom,k.srhBusdate) AND k.srhBusdate = COALESCE(:dateTo,k.srhBusdate) ";
		}
		logger.info("queryttext date:" + queryttext);
		// queryttext += "AND a.jnlId IN (SELECT MAX(d.jnlId) AS JNLID FROM TranDoc
		// d,Journal g WHERE d.jnlId = g.id AND g.txcode = COALESCE(:txcode,g.txcode)
		// AND
		// d.docName = COALESCE(:docName,d.docName) GROUP BY d.srhMrkey)";
		// queryttext += "AND a.jnlId IN (SELECT MAX(d.id) AS JNLID FROM Journal d WHERE
		// d.txcode = COALESCE(:txcode,d.txcode) GROUP BY d.mrkey , d.txcode) ";

		// 備註:srhTemp = 1時 是 放行交易 (ACTFG=4 or =6)
		// queryttext += "AND a.jnlId IN (SELECT MAX(d.jnlId) AS JNLID FROM TranDoc d
		// WHERE d.srhTxcode = COALESCE(:txcode,d.srhTxcode) AND d.srhTemp != '1' GROUP
		// BY
		// d.srhMrkey , d.srhTxcode) ";
		// 存款XS和XF是全部搜尋
		// for存款需求
		queryttext += "AND (a.jnlId IN (SELECT MAX(d.jnlId) AS JNLID FROM TranDoc d WHERE d.srhTxcode LIKE COALESCE(:txcode,d.srhTxcode) AND d.srhTemp != '1' "
				+ fastqueryd + " GROUP BY d.srhMrkey , d.srhTxcode) "
				+ "OR a.jnlId IN (SELECT k.jnlId AS JNLID FROM TranDoc k WHERE k.srhTxcode LIKE COALESCE(:txcode,k.srhTxcode) AND k.srhTemp != '1' AND (k.srhTxcode LIKE 'XS%' OR k.srhTxcode LIKE 'XF%') "
				+ fastqueryk + " ))";
		logger.info("queryttext MAX:" + queryttext);

		if (firsttime != null && firsttime.toLowerCase(Locale.TAIWAN).equals("true")) {
			queryttext += "AND a.docId NOT IN (SELECT c.docId FROM TranDocLog c) "; // 首次列印?
		}

		query = em.createQuery(queryttext);
		query.setParameter("docnames", docnames);
		query.setParameter("brn", brn);
		query.setParameter("cifkey", cifkey);
		query.setParameter("batno", batno);
		query.setParameter("rbrno", rbrno);
		query.setParameter("fbrno", fbrno);
		query.setParameter("acbrno", acbrno);
		query.setParameter("pbrno", pbrno);
		query.setParameter("mrkey1", mrkey1);
		query.setParameter("mrkey2", mrkey2);
		query.setParameter("dateFrom", dateFrom);
		query.setParameter("dateTo", dateTo);
		query.setParameter("txcode", txcode);

		return query.getResultList();

	}

	private boolean isNotEmpty(String s) {
		return s != null && s.trim().length() > 0;
	}
}
