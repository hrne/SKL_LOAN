package com.st1.ifx.service.springjpa;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.junit.Assert;

import com.st1.ifx.domain.MsgBox;
import com.st1.ifx.filter.FilterUtils;
import com.st1.ifx.repository.MsgBoxReporitory;
import com.st1.ifx.service.MsgService;

@Service("msgService")
@Repository
@Transactional
public class MsgServiceImpl implements MsgService, InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(MsgServiceImpl.class);
	@PersistenceContext
	// (type=PersistenceContextType.EXTENDED)
	private EntityManager em;

	@Autowired
	private MsgBoxReporitory msgBoxRepository;

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.assertNotNull(em);
	}

	@Override
	public void save(List<MsgBox> boxes) {
		for (MsgBox m : boxes) {
			logger.info("Msgno:" + m.getMsgno());
			if (m.isUniqueMessage() || m.isUniqueSkipmessage()) {
				logger.info("Before findByBrnoAndTlrnoAndMsgno.");
				MsgBox mOld = msgBoxRepository.findByBrnoAndTlrnoAndMsgno(m.getBrno(), m.getTlrno(), m.getMsgno());
				if (mOld != null) {
					logger.info("mOld:" + mOld.getBrno() + "," + mOld.getTlrno() + "," + mOld.getMsgno());
					// 刪除舊訊息
					if (m.isUniqueMessage()) {
						logger.info("isUniqueMessage!");
						msgBoxRepository.deleteById(mOld.getId());
					} else {
						logger.info("isUniqueSkipmessage!");
						// 跳過該訊息
						continue;
					}
				} else {
					logger.info("no exist mOld!");
				}
			}
			logger.info("Before save.");
			msgBoxRepository.save(m);
		}

	}

	@Override
	public void save(MsgBox m) {
		logger.info("Msgno:" + m.getMsgno());
		if (m.isUniqueMessage() || m.isUniqueSkipmessage()) {
			logger.info("Before findByBrnoAndTlrnoAndMsgno.");
			MsgBox mOld = msgBoxRepository.findByBrnoAndTlrnoAndMsgno(m.getBrno(), m.getTlrno(), m.getMsgno());
			if (mOld != null) {
				logger.info("mOld:" + mOld.getBrno() + "," + mOld.getTlrno() + "," + mOld.getMsgno());
				// 刪除舊訊息
				if (m.isUniqueMessage()) {
					logger.info("isUniqueMessage!");
					msgBoxRepository.deleteById(mOld.getId());
				} else {
					logger.info("isUniqueSkipmessage!");
				}
			} else {
				logger.info("no exist mOld!");
			}
		}
		logger.info("Before save.");
		msgBoxRepository.save(m);
	}

	@Override
	@Transactional(readOnly = true)
	public List<MsgBox> get(String brno, String tlrno, int option, String fdate) {
		// option: 0:已讀, 1:未讀 , 9:全部
		// 要改成營業日期?
		String nowString = new SimpleDateFormat("yyyyMMddHHmm").format(new Date());
		if (!fdate.isEmpty()) {
			nowString = fdate + nowString.substring(8, 12);
			logger.info("nowString:" + nowString);
		}
		Long now = Long.parseLong(nowString);
		String queryStr = "select b from MsgBox b  where b.brno=:brno and b.tlrno = :tlrno and b.validTime >= :now ";// order
																														// by
																														// b.rcvDate
																														// desc,b.rcvTime
																														// desc";
		String orderBy = " order by b.rcvDate desc, b.rcvTime desc";
		// if(option!= 9) {
		// queryStr += " and b.done=:done ";
		// }
		TypedQuery<MsgBox> q = em.createQuery(queryStr + orderBy, MsgBox.class);
		q.setParameter("now", now);
		q.setParameter("brno", brno);
		q.setParameter("tlrno", tlrno);
		// if (option != 9) {
		// q.setParameter("done", option == 0 ? 'Y' : 'N');
		// }

		return q.getResultList();

	}

	@Override
	public MsgBox updateMsgBox(Long id) {
		java.util.Date today = new java.util.Date();
		long t = today.getTime();

		// query.setParameter("id", id);
		// query.setParameter("viewDate", new java.sql.Date(t));
		// query.setParameter("viewTime", new Time(t));
		//
		Optional<MsgBox> m = msgBoxRepository.findById(id);

		if (m.isPresent()) {
			MsgBox b = m.get();
			logger.info(FilterUtils.escape(b.toString()));

			b.setViewDate(new java.sql.Date(t));
			b.setViewTime(new Time(t));
			b.setDone('Y');

			logger.info(FilterUtils.escape(b.toString()));

			b = msgBoxRepository.saveAndFlush(b);
			return b;
		} else {
			return null;
		}
	}

}
