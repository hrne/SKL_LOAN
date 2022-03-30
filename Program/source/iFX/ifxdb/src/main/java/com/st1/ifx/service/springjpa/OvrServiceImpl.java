package com.st1.ifx.service.springjpa;

import java.sql.Time;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.junit.Assert;

import com.st1.ifx.domain.Ovr;
import com.st1.ifx.domain.OvrScreen;
import com.st1.ifx.repository.OvrRepository;
import com.st1.ifx.service.OvrService;

@Service("ovrService")
@Repository
@Transactional
public class OvrServiceImpl implements OvrService, InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(OvrServiceImpl.class);

	@Autowired
	private OvrRepository repository;

	@PersistenceContext
	private EntityManager em;

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.assertNotNull(em);
		Assert.assertNotNull(repository);

	}

	@Override
	@Transactional(readOnly = true)
	public Ovr get(Long id, boolean bLazy) {
		Ovr ovr = null;
		if (bLazy) {
			Optional<Ovr> o = this.repository.findById(id);
			if (o.isPresent())
				ovr = o.get();
		} else {
			ovr = this.repository.findOneEager(id);
		}
		return ovr;
	}

	static int BUFSIZE = 1500;

	@Override
	public Ovr save(Ovr ovr, String content) {
		ovr.touch();
		if (content != null) {
			int bufIndex = 0;
			int offset = 0;
			int endPos;
			while (offset < content.length()) {
				OvrScreen buf = new OvrScreen();
				buf.setIndx(bufIndex++);
				endPos = offset + BUFSIZE;
				endPos = endPos > content.length() ? content.length() : endPos;
				String text = content.substring(offset, endPos);
				logger.info(text);
				buf.setBuf(text);
				ovr.addScreenBuffer(buf);
				offset += BUFSIZE;
			}
		}
		return repository.saveAndFlush(ovr);
	}

	@Override
	public Ovr update(Long id, String supBrn, String supNo, int status, String message) {
		Optional<Ovr> o = this.repository.findById(id);
		if (o.isPresent()) {
			Ovr ovr = o.get();
			ovr.setSupBrn(supBrn);
			ovr.setSupNo(supNo);
			ovr.setStatus(status);
			ovr.setMessage(message);

			java.util.Date now = new java.util.Date();
			ovr.setSupTime(new Time(now.getTime()));
			this.repository.save(ovr);
			return ovr;
		} else {
			logger.warn("Ovr update not found!!return null");
			return null;
		}
	}

}
