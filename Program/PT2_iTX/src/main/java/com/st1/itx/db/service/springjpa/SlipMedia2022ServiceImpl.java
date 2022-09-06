package com.st1.itx.db.service.springjpa;

import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;

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
import com.st1.itx.db.domain.SlipMedia2022;
import com.st1.itx.db.domain.SlipMedia2022Id;
import com.st1.itx.db.repository.online.SlipMedia2022Repository;
import com.st1.itx.db.repository.day.SlipMedia2022RepositoryDay;
import com.st1.itx.db.repository.mon.SlipMedia2022RepositoryMon;
import com.st1.itx.db.repository.hist.SlipMedia2022RepositoryHist;
import com.st1.itx.db.service.SlipMedia2022Service;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("slipMedia2022Service")
@Repository
public class SlipMedia2022ServiceImpl extends ASpringJpaParm implements SlipMedia2022Service, InitializingBean {
	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private SlipMedia2022Repository slipMedia2022Repos;

	@Autowired
	private SlipMedia2022RepositoryDay slipMedia2022ReposDay;

	@Autowired
	private SlipMedia2022RepositoryMon slipMedia2022ReposMon;

	@Autowired
	private SlipMedia2022RepositoryHist slipMedia2022ReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(slipMedia2022Repos);
		org.junit.Assert.assertNotNull(slipMedia2022ReposDay);
		org.junit.Assert.assertNotNull(slipMedia2022ReposMon);
		org.junit.Assert.assertNotNull(slipMedia2022ReposHist);
	}

	@Override
	public SlipMedia2022 findById(SlipMedia2022Id slipMedia2022Id, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findById " + dbName + " " + slipMedia2022Id);
		Optional<SlipMedia2022> slipMedia2022 = null;
		if (dbName.equals(ContentName.onDay))
			slipMedia2022 = slipMedia2022ReposDay.findById(slipMedia2022Id);
		else if (dbName.equals(ContentName.onMon))
			slipMedia2022 = slipMedia2022ReposMon.findById(slipMedia2022Id);
		else if (dbName.equals(ContentName.onHist))
			slipMedia2022 = slipMedia2022ReposHist.findById(slipMedia2022Id);
		else
			slipMedia2022 = slipMedia2022Repos.findById(slipMedia2022Id);
		SlipMedia2022 obj = slipMedia2022.isPresent() ? slipMedia2022.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<SlipMedia2022> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<SlipMedia2022> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;
		if (limit == Integer.MAX_VALUE)
			pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "MediaSlipNo", "Seq"));
		else
			pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "MediaSlipNo", "Seq"));
		this.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = slipMedia2022ReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = slipMedia2022ReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = slipMedia2022ReposHist.findAll(pageable);
		else
			slice = slipMedia2022Repos.findAll(pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<SlipMedia2022> findMediaSeq(int acDate_0, int batchNo_1, int mediaSeq_2, String latestFlag_3, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<SlipMedia2022> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("findMediaSeq " + dbName + " : " + "acDate_0 : " + acDate_0 + " batchNo_1 : " + batchNo_1 + " mediaSeq_2 : " + mediaSeq_2 + " latestFlag_3 : " + latestFlag_3);
		if (dbName.equals(ContentName.onDay))
			slice = slipMedia2022ReposDay.findAllByAcDateIsAndBatchNoIsAndMediaSeqIsAndLatestFlagIsOrderByMediaSlipNoAscSeqAsc(acDate_0, batchNo_1, mediaSeq_2, latestFlag_3, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = slipMedia2022ReposMon.findAllByAcDateIsAndBatchNoIsAndMediaSeqIsAndLatestFlagIsOrderByMediaSlipNoAscSeqAsc(acDate_0, batchNo_1, mediaSeq_2, latestFlag_3, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = slipMedia2022ReposHist.findAllByAcDateIsAndBatchNoIsAndMediaSeqIsAndLatestFlagIsOrderByMediaSlipNoAscSeqAsc(acDate_0, batchNo_1, mediaSeq_2, latestFlag_3, pageable);
		else
			slice = slipMedia2022Repos.findAllByAcDateIsAndBatchNoIsAndMediaSeqIsAndLatestFlagIsOrderByMediaSlipNoAscSeqAsc(acDate_0, batchNo_1, mediaSeq_2, latestFlag_3, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<SlipMedia2022> findBatchNo(int acDate_0, int batchNo_1, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<SlipMedia2022> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("findBatchNo " + dbName + " : " + "acDate_0 : " + acDate_0 + " batchNo_1 : " + batchNo_1);
		if (dbName.equals(ContentName.onDay))
			slice = slipMedia2022ReposDay.findAllByAcDateIsAndBatchNoIsOrderByMediaSlipNoAscSeqAsc(acDate_0, batchNo_1, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = slipMedia2022ReposMon.findAllByAcDateIsAndBatchNoIsOrderByMediaSlipNoAscSeqAsc(acDate_0, batchNo_1, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = slipMedia2022ReposHist.findAllByAcDateIsAndBatchNoIsOrderByMediaSlipNoAscSeqAsc(acDate_0, batchNo_1, pageable);
		else
			slice = slipMedia2022Repos.findAllByAcDateIsAndBatchNoIsOrderByMediaSlipNoAscSeqAsc(acDate_0, batchNo_1, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public SlipMedia2022 findMediaSeqFirst(int acDate_0, int batchNo_1, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findMediaSeqFirst " + dbName + " : " + "acDate_0 : " + acDate_0 + " batchNo_1 : " + batchNo_1);
		Optional<SlipMedia2022> slipMedia2022T = null;
		if (dbName.equals(ContentName.onDay))
			slipMedia2022T = slipMedia2022ReposDay.findTopByAcDateIsAndBatchNoIsOrderByMediaSlipNoAscSeqAsc(acDate_0, batchNo_1);
		else if (dbName.equals(ContentName.onMon))
			slipMedia2022T = slipMedia2022ReposMon.findTopByAcDateIsAndBatchNoIsOrderByMediaSlipNoAscSeqAsc(acDate_0, batchNo_1);
		else if (dbName.equals(ContentName.onHist))
			slipMedia2022T = slipMedia2022ReposHist.findTopByAcDateIsAndBatchNoIsOrderByMediaSlipNoAscSeqAsc(acDate_0, batchNo_1);
		else
			slipMedia2022T = slipMedia2022Repos.findTopByAcDateIsAndBatchNoIsOrderByMediaSlipNoAscSeqAsc(acDate_0, batchNo_1);

		return slipMedia2022T.isPresent() ? slipMedia2022T.get() : null;
	}

	@Override
	public SlipMedia2022 holdById(SlipMedia2022Id slipMedia2022Id, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + slipMedia2022Id);
		Optional<SlipMedia2022> slipMedia2022 = null;
		if (dbName.equals(ContentName.onDay))
			slipMedia2022 = slipMedia2022ReposDay.findBySlipMedia2022Id(slipMedia2022Id);
		else if (dbName.equals(ContentName.onMon))
			slipMedia2022 = slipMedia2022ReposMon.findBySlipMedia2022Id(slipMedia2022Id);
		else if (dbName.equals(ContentName.onHist))
			slipMedia2022 = slipMedia2022ReposHist.findBySlipMedia2022Id(slipMedia2022Id);
		else
			slipMedia2022 = slipMedia2022Repos.findBySlipMedia2022Id(slipMedia2022Id);
		return slipMedia2022.isPresent() ? slipMedia2022.get() : null;
	}

	@Override
	public SlipMedia2022 holdById(SlipMedia2022 slipMedia2022, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + slipMedia2022.getSlipMedia2022Id());
		Optional<SlipMedia2022> slipMedia2022T = null;
		if (dbName.equals(ContentName.onDay))
			slipMedia2022T = slipMedia2022ReposDay.findBySlipMedia2022Id(slipMedia2022.getSlipMedia2022Id());
		else if (dbName.equals(ContentName.onMon))
			slipMedia2022T = slipMedia2022ReposMon.findBySlipMedia2022Id(slipMedia2022.getSlipMedia2022Id());
		else if (dbName.equals(ContentName.onHist))
			slipMedia2022T = slipMedia2022ReposHist.findBySlipMedia2022Id(slipMedia2022.getSlipMedia2022Id());
		else
			slipMedia2022T = slipMedia2022Repos.findBySlipMedia2022Id(slipMedia2022.getSlipMedia2022Id());
		return slipMedia2022T.isPresent() ? slipMedia2022T.get() : null;
	}

	@Override
	public SlipMedia2022 insert(SlipMedia2022 slipMedia2022, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Insert..." + dbName + " " + slipMedia2022.getSlipMedia2022Id());
		if (this.findById(slipMedia2022.getSlipMedia2022Id(), titaVo) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			slipMedia2022.setCreateEmpNo(empNot);

		if (slipMedia2022.getLastUpdateEmpNo() == null || slipMedia2022.getLastUpdateEmpNo().isEmpty())
			slipMedia2022.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return slipMedia2022ReposDay.saveAndFlush(slipMedia2022);
		else if (dbName.equals(ContentName.onMon))
			return slipMedia2022ReposMon.saveAndFlush(slipMedia2022);
		else if (dbName.equals(ContentName.onHist))
			return slipMedia2022ReposHist.saveAndFlush(slipMedia2022);
		else
			return slipMedia2022Repos.saveAndFlush(slipMedia2022);
	}

	@Override
	public SlipMedia2022 update(SlipMedia2022 slipMedia2022, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + slipMedia2022.getSlipMedia2022Id());
		if (!empNot.isEmpty())
			slipMedia2022.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return slipMedia2022ReposDay.saveAndFlush(slipMedia2022);
		else if (dbName.equals(ContentName.onMon))
			return slipMedia2022ReposMon.saveAndFlush(slipMedia2022);
		else if (dbName.equals(ContentName.onHist))
			return slipMedia2022ReposHist.saveAndFlush(slipMedia2022);
		else
			return slipMedia2022Repos.saveAndFlush(slipMedia2022);
	}

	@Override
	public SlipMedia2022 update2(SlipMedia2022 slipMedia2022, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + slipMedia2022.getSlipMedia2022Id());
		if (!empNot.isEmpty())
			slipMedia2022.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			slipMedia2022ReposDay.saveAndFlush(slipMedia2022);
		else if (dbName.equals(ContentName.onMon))
			slipMedia2022ReposMon.saveAndFlush(slipMedia2022);
		else if (dbName.equals(ContentName.onHist))
			slipMedia2022ReposHist.saveAndFlush(slipMedia2022);
		else
			slipMedia2022Repos.saveAndFlush(slipMedia2022);
		return this.findById(slipMedia2022.getSlipMedia2022Id());
	}

	@Override
	public void delete(SlipMedia2022 slipMedia2022, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Delete..." + dbName + " " + slipMedia2022.getSlipMedia2022Id());
		if (dbName.equals(ContentName.onDay)) {
			slipMedia2022ReposDay.delete(slipMedia2022);
			slipMedia2022ReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			slipMedia2022ReposMon.delete(slipMedia2022);
			slipMedia2022ReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			slipMedia2022ReposHist.delete(slipMedia2022);
			slipMedia2022ReposHist.flush();
		} else {
			slipMedia2022Repos.delete(slipMedia2022);
			slipMedia2022Repos.flush();
		}
	}

	@Override
	public void insertAll(List<SlipMedia2022> slipMedia2022, TitaVo... titaVo) throws DBException {
		if (slipMedia2022 == null || slipMedia2022.size() == 0)
			throw new DBException(6);
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("InsertAll...");
		for (SlipMedia2022 t : slipMedia2022) {
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);
			if (t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
				t.setLastUpdateEmpNo(empNot);
		}

		if (dbName.equals(ContentName.onDay)) {
			slipMedia2022 = slipMedia2022ReposDay.saveAll(slipMedia2022);
			slipMedia2022ReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			slipMedia2022 = slipMedia2022ReposMon.saveAll(slipMedia2022);
			slipMedia2022ReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			slipMedia2022 = slipMedia2022ReposHist.saveAll(slipMedia2022);
			slipMedia2022ReposHist.flush();
		} else {
			slipMedia2022 = slipMedia2022Repos.saveAll(slipMedia2022);
			slipMedia2022Repos.flush();
		}
	}

	@Override
	public void updateAll(List<SlipMedia2022> slipMedia2022, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("UpdateAll...");
		if (slipMedia2022 == null || slipMedia2022.size() == 0)
			throw new DBException(6);

		for (SlipMedia2022 t : slipMedia2022)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			slipMedia2022 = slipMedia2022ReposDay.saveAll(slipMedia2022);
			slipMedia2022ReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			slipMedia2022 = slipMedia2022ReposMon.saveAll(slipMedia2022);
			slipMedia2022ReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			slipMedia2022 = slipMedia2022ReposHist.saveAll(slipMedia2022);
			slipMedia2022ReposHist.flush();
		} else {
			slipMedia2022 = slipMedia2022Repos.saveAll(slipMedia2022);
			slipMedia2022Repos.flush();
		}
	}

	@Override
	public void deleteAll(List<SlipMedia2022> slipMedia2022, TitaVo... titaVo) throws DBException {
		this.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (slipMedia2022 == null || slipMedia2022.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			slipMedia2022ReposDay.deleteAll(slipMedia2022);
			slipMedia2022ReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			slipMedia2022ReposMon.deleteAll(slipMedia2022);
			slipMedia2022ReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			slipMedia2022ReposHist.deleteAll(slipMedia2022);
			slipMedia2022ReposHist.flush();
		} else {
			slipMedia2022Repos.deleteAll(slipMedia2022);
			slipMedia2022Repos.flush();
		}
	}

}
