package com.st1.ifx.service.springjpa;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.junit.Assert;

import com.st1.ifx.domain.Journal;
import com.st1.ifx.domain.Journal_;
import com.st1.ifx.domain.Rqsp;
import com.st1.ifx.repository.JournalRepository;
import com.st1.ifx.repository.RqspRepository;
import com.st1.ifx.service.JournalService;

@Service("journalService")
@Repository
@Transactional
public class JournalServiceImpl implements JournalService, InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(JournalServiceImpl.class);

	@Autowired
	private JournalRepository repository;

	@Autowired
	private RqspRepository rqspRepository;

	@PersistenceContext
	private EntityManager em;

	@Override
	@Transactional(readOnly = true)
	public Journal get(Long id) {
		Optional<Journal> j = repository.findById(id);
		if (j.isPresent()) {
			Journal journal = j.get();
			return journal;
		} else
			return null;
	}

	@Override
	public Journal save(Journal journal) {

		logger.info(journal.toString());
		return repository.save(journal);

	}

	@Override
	@Transactional(readOnly = true)
	public int getLastSeq(String busDate, String brn, String tlrno) {
		List<Journal> list = repository.findByBusdateAndBrnAndTlrnoOrderByTxnoDesc(busDate, brn, tlrno);
		if (list != null && list.size() > 0) {
			String txno = list.get(0).getTxno();
			return Integer.parseInt(txno);
		} else {
			return 0;
		}
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Journal> findAllByPage(Pageable pageable) {
		// TODO Auto-generated method stub
		return null;
	}

	// TODO: 只select需要的欄位, 不要select fields, tita等長欄位
	// @Override
	// public List<Journal> findByCriteriaQuery(JournalScanReq req) {
	// log.info("finding journal for " + req.toString());

	// CriteriaBuilder cb = em.getCriteriaBuilder();
	// CriteriaQuery<Journal> criteriaQuery = cb.createQuery(Journal.class);
	//
	// Root<Journal> jnlRoot = criteriaQuery.from(Journal.class);
	// criteriaQuery.select(jnlRoot);
	//
	// Predicate criteria = cb.conjunction();
	// if (isNotEmpty(req.getBrn())) {
	// Predicate p = cb.equal(jnlRoot.get(Journal_.brn), req.getBrn());
	// criteria = cb.and(criteria, p);
	// }
	//
	// if (isNotEmpty(req.getTlrno())) {
	// Predicate p = cb.equal(jnlRoot.get(Journal_.tlrno), req.getTlrno());
	// criteria = cb.and(criteria, p);
	// }
	// if (isNotEmpty(req.getTxcode())) {
	// Predicate p = cb.equal(jnlRoot.get(Journal_.txcode),
	// req.getTxcode());
	// criteria = cb.and(criteria, p);
	// }
	// if (isNotEmpty(req.getTxno())) {
	// Predicate p = cb.equal(jnlRoot.get(Journal_.txno), req.getTxno());
	// criteria = cb.and(criteria, p);
	// }
	// if (isNotEmpty(req.getBusDateFrom())) {
	// Predicate p = cb.greaterThanOrEqualTo(
	// jnlRoot.get(Journal_.busDate), req.getBusDateFrom());
	// criteria = cb.and(criteria, p);
	// }
	// if (isNotEmpty(req.getBusDateTo())) {
	// Predicate p = cb.lessThanOrEqualTo(jnlRoot.get(Journal_.busDate),
	// req.getBusDateTo());
	// criteria = cb.and(criteria, p);
	// }
	//
	// if (isNotEmpty(req.getSeqFrom())) {
	// Predicate p = cb.greaterThanOrEqualTo(jnlRoot.get(Journal_.jnlSeq),
	// Integer.parseInt(req.getSeqFrom()));
	// criteria = cb.and(criteria, p);
	// }
	// if (isNotEmpty(req.getSeqTo())) {
	// Predicate p = cb.lessThanOrEqualTo(jnlRoot.get(Journal_.jnlSeq),
	// Integer.parseInt(req.getSeqTo()));
	// criteria = cb.and(criteria, p);
	// }
	//
	// if (isNotEmpty(req.getSysDateFrom())) {
	// Predicate p = cb.greaterThanOrEqualTo(
	// jnlRoot.get(Journal_.jnlDate),
	// SomeHelper.strToDate(req.getSysDateFrom()));
	// criteria = cb.and(criteria, p);
	// }
	//
	// if (isNotEmpty(req.getSysDateTo())) {
	// Predicate p = cb.lessThanOrEqualTo(jnlRoot.get(Journal_.jnlDate),
	// SomeHelper.strToDate(req.getSysDateTo()));
	// criteria = cb.and(criteria, p);
	// }
	// if (isNotEmpty(req.getSysTimeFrom())) {
	// Predicate p = cb.greaterThanOrEqualTo(
	// jnlRoot.get(Journal_.jnlTime),
	// SomeHelper.parseTime6(req.getSysTimeFrom()));
	// criteria = cb.and(criteria, p);
	// }
	//
	// if (isNotEmpty(req.getSysTimeTo())) {
	// Predicate p = cb.lessThanOrEqualTo(jnlRoot.get(Journal_.jnlTime),
	// SomeHelper.parseTime6(req.getSysTimeTo()));
	// criteria = cb.and(criteria, p);
	// }
	//
	// criteriaQuery.where(criteria);
	// if (req.getOrder().equals("1"))
	// criteriaQuery.orderBy(cb.asc(jnlRoot.get(Journal_.id)));
	// else
	// criteriaQuery.orderBy(cb.desc(jnlRoot.get(Journal_.id)));
	//
	// List<Journal> result = em.createQuery(criteriaQuery).getResultList();
	// return result;
	// return null;
	// }

	private boolean isNotEmpty(String s) {
		return s != null && s.trim().length() > 0;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.assertNotNull(em);
		Assert.assertNotNull(repository);
		Assert.assertNotNull(rqspRepository);

	}

	@Override
	@Transactional(readOnly = true)
	public List<Journal> findByF4(String busdate, String brn, String tlrno, String txno) {
		return this.repository.findByBusdateAndBrnAndTlrnoAndTxno(busdate, brn, tlrno, txno);
	}

	@Override
	public List<Journal> findByF4MK2(int calDay, String brn, String tlrno, String txno) {
		return this.repository.findByCalDayAndBrnAndTlrnoAndTxno(calDay, brn, tlrno, txno);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Journal> findByCriteriaQuery(HashMap<String, String> requestMap) {
		String txno = requestMap.get("txno");
		String txcd = requestMap.get("txcode"); // txcd->txcode
		String brn = requestMap.get("brn");

		String rbrno = requestMap.get("rbrno");
		String fbrno = requestMap.get("fbrno");
		String acbrno = requestMap.get("acbrno");
		String pbrno = requestMap.get("pbrno");
		String cifkey = requestMap.get("cifkey");
		String mrkey = requestMap.get("mrkey");
		String currency = requestMap.get("currency");
		String txamt = requestMap.get("txamt");

		String tlrno = requestMap.get("tlrno");
		String level = requestMap.get("level");
		String dateFrom = requestMap.get("dateFrom");
		String dateTo = requestMap.get("dateTo");
		String timeFrom = requestMap.get("timeFrom");
		String timeTo = requestMap.get("timeTo");
		String order = requestMap.get("order");

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Journal> criteriaQuery = cb.createQuery(Journal.class);

		Root<Journal> jnlRoot = criteriaQuery.from(Journal.class);
		criteriaQuery.select(jnlRoot);
		Predicate criteria = cb.conjunction();
		Predicate p;

		if (isNotEmpty(brn)) {
			p = cb.equal(jnlRoot.get(Journal_.brn), brn);
			criteria = cb.and(criteria, p);
		}
		if (isNotEmpty(rbrno)) {
			p = cb.equal(jnlRoot.get(Journal_.rbrno), rbrno);
			criteria = cb.and(criteria, p);
		}
		if (isNotEmpty(fbrno)) {
			p = cb.equal(jnlRoot.get(Journal_.fbrno), fbrno);
			criteria = cb.and(criteria, p);
		}
		if (isNotEmpty(acbrno)) {
			p = cb.equal(jnlRoot.get(Journal_.acbrno), acbrno);
			criteria = cb.and(criteria, p);
		}
		if (isNotEmpty(pbrno)) {
			p = cb.equal(jnlRoot.get(Journal_.pbrno), pbrno);
			criteria = cb.and(criteria, p);
		}
		if (isNotEmpty(cifkey)) {
			p = cb.equal(jnlRoot.get(Journal_.cifkey), cifkey);
			criteria = cb.and(criteria, p);
		}
		if (isNotEmpty(mrkey)) {
			p = cb.equal(jnlRoot.get(Journal_.mrkey), mrkey);
			criteria = cb.and(criteria, p);
		}
		// TODO supervisor can query every journal?
		if (isNotEmpty(tlrno)) {
			p = cb.equal(jnlRoot.get(Journal_.tlrno), tlrno);
			criteria = cb.and(criteria, p);
		}

		// 應該不要登入與登出交易? start
		p = cb.notEqual(jnlRoot.get(Journal_.txcode), "LC100");
		criteria = cb.and(criteria, p);
		p = cb.notEqual(jnlRoot.get(Journal_.txcode), "LC101");
		criteria = cb.and(criteria, p);
		// end

		if (isNotEmpty(txcd)) {
			txcd = txcd.trim();
			if (txcd.length() == 5) {
				p = cb.equal(jnlRoot.get(Journal_.txcode), txcd);
				criteria = cb.and(criteria, p);
			} else {
				p = cb.like(jnlRoot.get(Journal_.txcode), txcd + "%");
				criteria = cb.and(criteria, p);
			}
		}

		// 全部去除查詢類 __9,__0,__R
		p = cb.notLike(jnlRoot.get(Journal_.txcode), "__9%");
		criteria = cb.and(criteria, p);
		p = cb.notLike(jnlRoot.get(Journal_.txcode), "__0%");
		criteria = cb.and(criteria, p);
		p = cb.notLike(jnlRoot.get(Journal_.txcode), "__R%");
		criteria = cb.and(criteria, p);
		// 全部去除沒成功的資料 100->成功 1->失敗
		p = cb.notEqual(jnlRoot.get(Journal_.tranStatus), 1);
		criteria = cb.and(criteria, p);
		// 只選擇櫃員交易 START
		// lessThanOrEqualTo
		p = cb.greaterThanOrEqualTo(jnlRoot.get(Journal_.lvel), "3");
		criteria = cb.and(criteria, p);
		// p = cb.equal(jnlRoot.get(Journal_.level),"4");
		// criteria = cb.and(criteria, p); 潘
		// END

		if (isNotEmpty(dateFrom)) {
			if (isNotEmpty(dateTo)) { // between dateFrom and dateTo
				p = cb.greaterThanOrEqualTo(jnlRoot.get(Journal_.busdate), dateFrom);
				criteria = cb.and(criteria, p);
				p = cb.lessThanOrEqualTo(jnlRoot.get(Journal_.busdate), dateTo);
				criteria = cb.and(criteria, p);
			} else { // equals to dateFrom
				p = cb.equal(jnlRoot.get(Journal_.busdate), dateFrom);
				criteria = cb.and(criteria, p);
			}
		}
		if (isNotEmpty(txno)) {
			p = cb.equal(jnlRoot.get(Journal_.txno), txno);
			criteria = cb.and(criteria, p);
		}
		criteriaQuery.where(criteria);

		if (!isNotEmpty(order))
			order = "1";
		// order by 應該改為日期+時間 而非id
		if (order.equals("1")) {
			criteriaQuery.orderBy(cb.desc(jnlRoot.get(Journal_.jnlDate)), cb.desc(jnlRoot.get(Journal_.jnlTime)));
		} else {
			// criteriaQuery.orderBy(cb.asc(jnlRoot.get(Journal_.id)));
			criteriaQuery.orderBy(cb.asc(jnlRoot.get(Journal_.jnlDate)), cb.asc(jnlRoot.get(Journal_.jnlTime)));
		}

		List<Journal> result = em.createQuery(criteriaQuery).getResultList();
		return result;
	}

	@Override
	public Journal saveJnlAndRqsp(Journal journal, Rqsp rqsp) {
		journal = repository.save(journal);
		if (rqsp != null) {
			logger.info("in saveJnlAndRqsp!");
			if (rqsp.getJnlId() == null) {
				rqsp.setJnlId(journal.getId());
			}
			rqspRepository.save(rqsp);
		}

		return journal;
	}
}
