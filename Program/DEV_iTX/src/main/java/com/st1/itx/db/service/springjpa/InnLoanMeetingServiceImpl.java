package com.st1.itx.db.service.springjpa;

import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.InnLoanMeeting;
import com.st1.itx.db.repository.online.InnLoanMeetingRepository;
import com.st1.itx.db.repository.day.InnLoanMeetingRepositoryDay;
import com.st1.itx.db.repository.mon.InnLoanMeetingRepositoryMon;
import com.st1.itx.db.repository.hist.InnLoanMeetingRepositoryHist;
import com.st1.itx.db.service.InnLoanMeetingService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("innLoanMeetingService")
@Repository
public class InnLoanMeetingServiceImpl implements InnLoanMeetingService, InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(InnLoanMeetingServiceImpl.class);

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private InnLoanMeetingRepository innLoanMeetingRepos;

	@Autowired
	private InnLoanMeetingRepositoryDay innLoanMeetingReposDay;

	@Autowired
	private InnLoanMeetingRepositoryMon innLoanMeetingReposMon;

	@Autowired
	private InnLoanMeetingRepositoryHist innLoanMeetingReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(innLoanMeetingRepos);
		org.junit.Assert.assertNotNull(innLoanMeetingReposDay);
		org.junit.Assert.assertNotNull(innLoanMeetingReposMon);
		org.junit.Assert.assertNotNull(innLoanMeetingReposHist);
	}

	@Override
	public InnLoanMeeting findById(int meetNo, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		logger.info("findById " + dbName + " " + meetNo);
		Optional<InnLoanMeeting> innLoanMeeting = null;
		if (dbName.equals(ContentName.onDay))
			innLoanMeeting = innLoanMeetingReposDay.findById(meetNo);
		else if (dbName.equals(ContentName.onMon))
			innLoanMeeting = innLoanMeetingReposMon.findById(meetNo);
		else if (dbName.equals(ContentName.onHist))
			innLoanMeeting = innLoanMeetingReposHist.findById(meetNo);
		else
			innLoanMeeting = innLoanMeetingRepos.findById(meetNo);
		InnLoanMeeting obj = innLoanMeeting.isPresent() ? innLoanMeeting.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<InnLoanMeeting> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<InnLoanMeeting> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "MeetNo"));
		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		logger.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = innLoanMeetingReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = innLoanMeetingReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = innLoanMeetingReposHist.findAll(pageable);
		else
			slice = innLoanMeetingRepos.findAll(pageable);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<InnLoanMeeting> meetingDateRg(int meetingDate_0, int meetingDate_1, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<InnLoanMeeting> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = PageRequest.of(index, limit);
		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		logger.info("meetingDateRg " + dbName + " : " + "meetingDate_0 : " + meetingDate_0 + " meetingDate_1 : " + meetingDate_1);
		if (dbName.equals(ContentName.onDay))
			slice = innLoanMeetingReposDay.findAllByMeetingDateGreaterThanEqualAndMeetingDateLessThanEqualOrderByMeetingDateAscMeetNoAsc(meetingDate_0, meetingDate_1, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = innLoanMeetingReposMon.findAllByMeetingDateGreaterThanEqualAndMeetingDateLessThanEqualOrderByMeetingDateAscMeetNoAsc(meetingDate_0, meetingDate_1, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = innLoanMeetingReposHist.findAllByMeetingDateGreaterThanEqualAndMeetingDateLessThanEqualOrderByMeetingDateAscMeetNoAsc(meetingDate_0, meetingDate_1, pageable);
		else
			slice = innLoanMeetingRepos.findAllByMeetingDateGreaterThanEqualAndMeetingDateLessThanEqualOrderByMeetingDateAscMeetNoAsc(meetingDate_0, meetingDate_1, pageable);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public InnLoanMeeting holdById(int meetNo, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		logger.info("Hold " + dbName + " " + meetNo);
		Optional<InnLoanMeeting> innLoanMeeting = null;
		if (dbName.equals(ContentName.onDay))
			innLoanMeeting = innLoanMeetingReposDay.findByMeetNo(meetNo);
		else if (dbName.equals(ContentName.onMon))
			innLoanMeeting = innLoanMeetingReposMon.findByMeetNo(meetNo);
		else if (dbName.equals(ContentName.onHist))
			innLoanMeeting = innLoanMeetingReposHist.findByMeetNo(meetNo);
		else
			innLoanMeeting = innLoanMeetingRepos.findByMeetNo(meetNo);
		return innLoanMeeting.isPresent() ? innLoanMeeting.get() : null;
	}

	@Override
	public InnLoanMeeting holdById(InnLoanMeeting innLoanMeeting, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		logger.info("Hold " + dbName + " " + innLoanMeeting.getMeetNo());
		Optional<InnLoanMeeting> innLoanMeetingT = null;
		if (dbName.equals(ContentName.onDay))
			innLoanMeetingT = innLoanMeetingReposDay.findByMeetNo(innLoanMeeting.getMeetNo());
		else if (dbName.equals(ContentName.onMon))
			innLoanMeetingT = innLoanMeetingReposMon.findByMeetNo(innLoanMeeting.getMeetNo());
		else if (dbName.equals(ContentName.onHist))
			innLoanMeetingT = innLoanMeetingReposHist.findByMeetNo(innLoanMeeting.getMeetNo());
		else
			innLoanMeetingT = innLoanMeetingRepos.findByMeetNo(innLoanMeeting.getMeetNo());
		return innLoanMeetingT.isPresent() ? innLoanMeetingT.get() : null;
	}

	@Override
	public InnLoanMeeting insert(InnLoanMeeting innLoanMeeting, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		logger.info("Insert..." + dbName + " " + innLoanMeeting.getMeetNo());
		if (this.findById(innLoanMeeting.getMeetNo()) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			innLoanMeeting.setCreateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return innLoanMeetingReposDay.saveAndFlush(innLoanMeeting);
		else if (dbName.equals(ContentName.onMon))
			return innLoanMeetingReposMon.saveAndFlush(innLoanMeeting);
		else if (dbName.equals(ContentName.onHist))
			return innLoanMeetingReposHist.saveAndFlush(innLoanMeeting);
		else
			return innLoanMeetingRepos.saveAndFlush(innLoanMeeting);
	}

	@Override
	public InnLoanMeeting update(InnLoanMeeting innLoanMeeting, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		logger.info("Update..." + dbName + " " + innLoanMeeting.getMeetNo());
		if (!empNot.isEmpty())
			innLoanMeeting.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return innLoanMeetingReposDay.saveAndFlush(innLoanMeeting);
		else if (dbName.equals(ContentName.onMon))
			return innLoanMeetingReposMon.saveAndFlush(innLoanMeeting);
		else if (dbName.equals(ContentName.onHist))
			return innLoanMeetingReposHist.saveAndFlush(innLoanMeeting);
		else
			return innLoanMeetingRepos.saveAndFlush(innLoanMeeting);
	}

	@Override
	public InnLoanMeeting update2(InnLoanMeeting innLoanMeeting, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		logger.info("Update..." + dbName + " " + innLoanMeeting.getMeetNo());
		if (!empNot.isEmpty())
			innLoanMeeting.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			innLoanMeetingReposDay.saveAndFlush(innLoanMeeting);
		else if (dbName.equals(ContentName.onMon))
			innLoanMeetingReposMon.saveAndFlush(innLoanMeeting);
		else if (dbName.equals(ContentName.onHist))
			innLoanMeetingReposHist.saveAndFlush(innLoanMeeting);
		else
			innLoanMeetingRepos.saveAndFlush(innLoanMeeting);
		return this.findById(innLoanMeeting.getMeetNo());
	}

	@Override
	public void delete(InnLoanMeeting innLoanMeeting, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		logger.info("Delete..." + dbName + " " + innLoanMeeting.getMeetNo());
		if (dbName.equals(ContentName.onDay)) {
			innLoanMeetingReposDay.delete(innLoanMeeting);
			innLoanMeetingReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			innLoanMeetingReposMon.delete(innLoanMeeting);
			innLoanMeetingReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			innLoanMeetingReposHist.delete(innLoanMeeting);
			innLoanMeetingReposHist.flush();
		} else {
			innLoanMeetingRepos.delete(innLoanMeeting);
			innLoanMeetingRepos.flush();
		}
	}

	@Override
	public void insertAll(List<InnLoanMeeting> innLoanMeeting, TitaVo... titaVo) throws DBException {
		if (innLoanMeeting == null || innLoanMeeting.size() == 0)
			throw new DBException(6);
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		logger.info("InsertAll...");
		for (InnLoanMeeting t : innLoanMeeting)
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			innLoanMeeting = innLoanMeetingReposDay.saveAll(innLoanMeeting);
			innLoanMeetingReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			innLoanMeeting = innLoanMeetingReposMon.saveAll(innLoanMeeting);
			innLoanMeetingReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			innLoanMeeting = innLoanMeetingReposHist.saveAll(innLoanMeeting);
			innLoanMeetingReposHist.flush();
		} else {
			innLoanMeeting = innLoanMeetingRepos.saveAll(innLoanMeeting);
			innLoanMeetingRepos.flush();
		}
	}

	@Override
	public void updateAll(List<InnLoanMeeting> innLoanMeeting, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		logger.info("UpdateAll...");
		if (innLoanMeeting == null || innLoanMeeting.size() == 0)
			throw new DBException(6);

		for (InnLoanMeeting t : innLoanMeeting)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			innLoanMeeting = innLoanMeetingReposDay.saveAll(innLoanMeeting);
			innLoanMeetingReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			innLoanMeeting = innLoanMeetingReposMon.saveAll(innLoanMeeting);
			innLoanMeetingReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			innLoanMeeting = innLoanMeetingReposHist.saveAll(innLoanMeeting);
			innLoanMeetingReposHist.flush();
		} else {
			innLoanMeeting = innLoanMeetingRepos.saveAll(innLoanMeeting);
			innLoanMeetingRepos.flush();
		}
	}

	@Override
	public void deleteAll(List<InnLoanMeeting> innLoanMeeting, TitaVo... titaVo) throws DBException {
		logger.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (innLoanMeeting == null || innLoanMeeting.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			innLoanMeetingReposDay.deleteAll(innLoanMeeting);
			innLoanMeetingReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			innLoanMeetingReposMon.deleteAll(innLoanMeeting);
			innLoanMeetingReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			innLoanMeetingReposHist.deleteAll(innLoanMeeting);
			innLoanMeetingReposHist.flush();
		} else {
			innLoanMeetingRepos.deleteAll(innLoanMeeting);
			innLoanMeetingRepos.flush();
		}
	}

}
