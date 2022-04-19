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
import com.st1.itx.db.domain.BatxHead;
import com.st1.itx.db.domain.BatxHeadId;
import com.st1.itx.db.repository.online.BatxHeadRepository;
import com.st1.itx.db.repository.day.BatxHeadRepositoryDay;
import com.st1.itx.db.repository.mon.BatxHeadRepositoryMon;
import com.st1.itx.db.repository.hist.BatxHeadRepositoryHist;
import com.st1.itx.db.service.BatxHeadService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("batxHeadService")
@Repository
public class BatxHeadServiceImpl extends ASpringJpaParm implements BatxHeadService, InitializingBean {
	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private BatxHeadRepository batxHeadRepos;

	@Autowired
	private BatxHeadRepositoryDay batxHeadReposDay;

	@Autowired
	private BatxHeadRepositoryMon batxHeadReposMon;

	@Autowired
	private BatxHeadRepositoryHist batxHeadReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(batxHeadRepos);
		org.junit.Assert.assertNotNull(batxHeadReposDay);
		org.junit.Assert.assertNotNull(batxHeadReposMon);
		org.junit.Assert.assertNotNull(batxHeadReposHist);
	}

	@Override
	public BatxHead findById(BatxHeadId batxHeadId, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findById " + dbName + " " + batxHeadId);
		Optional<BatxHead> batxHead = null;
		if (dbName.equals(ContentName.onDay))
			batxHead = batxHeadReposDay.findById(batxHeadId);
		else if (dbName.equals(ContentName.onMon))
			batxHead = batxHeadReposMon.findById(batxHeadId);
		else if (dbName.equals(ContentName.onHist))
			batxHead = batxHeadReposHist.findById(batxHeadId);
		else
			batxHead = batxHeadRepos.findById(batxHeadId);
		BatxHead obj = batxHead.isPresent() ? batxHead.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<BatxHead> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<BatxHead> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;
		if (limit == Integer.MAX_VALUE)
			pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "AcDate", "BatchNo"));
		else
			pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "AcDate", "BatchNo"));
		this.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = batxHeadReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = batxHeadReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = batxHeadReposHist.findAll(pageable);
		else
			slice = batxHeadRepos.findAll(pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<BatxHead> acDateRange(int acDate_0, int acDate_1, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<BatxHead> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("acDateRange " + dbName + " : " + "acDate_0 : " + acDate_0 + " acDate_1 : " + acDate_1);
		if (dbName.equals(ContentName.onDay))
			slice = batxHeadReposDay.findAllByAcDateGreaterThanEqualAndAcDateLessThanEqualOrderByBatchNoAsc(acDate_0, acDate_1, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = batxHeadReposMon.findAllByAcDateGreaterThanEqualAndAcDateLessThanEqualOrderByBatchNoAsc(acDate_0, acDate_1, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = batxHeadReposHist.findAllByAcDateGreaterThanEqualAndAcDateLessThanEqualOrderByBatchNoAsc(acDate_0, acDate_1, pageable);
		else
			slice = batxHeadRepos.findAllByAcDateGreaterThanEqualAndAcDateLessThanEqualOrderByBatchNoAsc(acDate_0, acDate_1, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public BatxHead batchNoFirst(int acDate_0, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("batchNoFirst " + dbName + " : " + "acDate_0 : " + acDate_0);
		Optional<BatxHead> batxHeadT = null;
		if (dbName.equals(ContentName.onDay))
			batxHeadT = batxHeadReposDay.findTopByAcDateIsOrderByBatchNoDesc(acDate_0);
		else if (dbName.equals(ContentName.onMon))
			batxHeadT = batxHeadReposMon.findTopByAcDateIsOrderByBatchNoDesc(acDate_0);
		else if (dbName.equals(ContentName.onHist))
			batxHeadT = batxHeadReposHist.findTopByAcDateIsOrderByBatchNoDesc(acDate_0);
		else
			batxHeadT = batxHeadRepos.findTopByAcDateIsOrderByBatchNoDesc(acDate_0);

		return batxHeadT.isPresent() ? batxHeadT.get() : null;
	}

	@Override
	public BatxHead titaTxCdFirst(int acDate_0, String titaTxCd_1, String batxExeCode_2, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("titaTxCdFirst " + dbName + " : " + "acDate_0 : " + acDate_0 + " titaTxCd_1 : " + titaTxCd_1 + " batxExeCode_2 : " + batxExeCode_2);
		Optional<BatxHead> batxHeadT = null;
		if (dbName.equals(ContentName.onDay))
			batxHeadT = batxHeadReposDay.findTopByAcDateIsAndTitaTxCdIsAndBatxExeCodeNotOrderByBatchNoDesc(acDate_0, titaTxCd_1, batxExeCode_2);
		else if (dbName.equals(ContentName.onMon))
			batxHeadT = batxHeadReposMon.findTopByAcDateIsAndTitaTxCdIsAndBatxExeCodeNotOrderByBatchNoDesc(acDate_0, titaTxCd_1, batxExeCode_2);
		else if (dbName.equals(ContentName.onHist))
			batxHeadT = batxHeadReposHist.findTopByAcDateIsAndTitaTxCdIsAndBatxExeCodeNotOrderByBatchNoDesc(acDate_0, titaTxCd_1, batxExeCode_2);
		else
			batxHeadT = batxHeadRepos.findTopByAcDateIsAndTitaTxCdIsAndBatxExeCodeNotOrderByBatchNoDesc(acDate_0, titaTxCd_1, batxExeCode_2);

		return batxHeadT.isPresent() ? batxHeadT.get() : null;
	}

	@Override
	public BatxHead holdById(BatxHeadId batxHeadId, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + batxHeadId);
		Optional<BatxHead> batxHead = null;
		if (dbName.equals(ContentName.onDay))
			batxHead = batxHeadReposDay.findByBatxHeadId(batxHeadId);
		else if (dbName.equals(ContentName.onMon))
			batxHead = batxHeadReposMon.findByBatxHeadId(batxHeadId);
		else if (dbName.equals(ContentName.onHist))
			batxHead = batxHeadReposHist.findByBatxHeadId(batxHeadId);
		else
			batxHead = batxHeadRepos.findByBatxHeadId(batxHeadId);
		return batxHead.isPresent() ? batxHead.get() : null;
	}

	@Override
	public BatxHead holdById(BatxHead batxHead, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + batxHead.getBatxHeadId());
		Optional<BatxHead> batxHeadT = null;
		if (dbName.equals(ContentName.onDay))
			batxHeadT = batxHeadReposDay.findByBatxHeadId(batxHead.getBatxHeadId());
		else if (dbName.equals(ContentName.onMon))
			batxHeadT = batxHeadReposMon.findByBatxHeadId(batxHead.getBatxHeadId());
		else if (dbName.equals(ContentName.onHist))
			batxHeadT = batxHeadReposHist.findByBatxHeadId(batxHead.getBatxHeadId());
		else
			batxHeadT = batxHeadRepos.findByBatxHeadId(batxHead.getBatxHeadId());
		return batxHeadT.isPresent() ? batxHeadT.get() : null;
	}

	@Override
	public BatxHead insert(BatxHead batxHead, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Insert..." + dbName + " " + batxHead.getBatxHeadId());
		if (this.findById(batxHead.getBatxHeadId()) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			batxHead.setCreateEmpNo(empNot);

		if (batxHead.getLastUpdateEmpNo() == null || batxHead.getLastUpdateEmpNo().isEmpty())
			batxHead.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return batxHeadReposDay.saveAndFlush(batxHead);
		else if (dbName.equals(ContentName.onMon))
			return batxHeadReposMon.saveAndFlush(batxHead);
		else if (dbName.equals(ContentName.onHist))
			return batxHeadReposHist.saveAndFlush(batxHead);
		else
			return batxHeadRepos.saveAndFlush(batxHead);
	}

	@Override
	public BatxHead update(BatxHead batxHead, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + batxHead.getBatxHeadId());
		if (!empNot.isEmpty())
			batxHead.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return batxHeadReposDay.saveAndFlush(batxHead);
		else if (dbName.equals(ContentName.onMon))
			return batxHeadReposMon.saveAndFlush(batxHead);
		else if (dbName.equals(ContentName.onHist))
			return batxHeadReposHist.saveAndFlush(batxHead);
		else
			return batxHeadRepos.saveAndFlush(batxHead);
	}

	@Override
	public BatxHead update2(BatxHead batxHead, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + batxHead.getBatxHeadId());
		if (!empNot.isEmpty())
			batxHead.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			batxHeadReposDay.saveAndFlush(batxHead);
		else if (dbName.equals(ContentName.onMon))
			batxHeadReposMon.saveAndFlush(batxHead);
		else if (dbName.equals(ContentName.onHist))
			batxHeadReposHist.saveAndFlush(batxHead);
		else
			batxHeadRepos.saveAndFlush(batxHead);
		return this.findById(batxHead.getBatxHeadId());
	}

	@Override
	public void delete(BatxHead batxHead, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Delete..." + dbName + " " + batxHead.getBatxHeadId());
		if (dbName.equals(ContentName.onDay)) {
			batxHeadReposDay.delete(batxHead);
			batxHeadReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			batxHeadReposMon.delete(batxHead);
			batxHeadReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			batxHeadReposHist.delete(batxHead);
			batxHeadReposHist.flush();
		} else {
			batxHeadRepos.delete(batxHead);
			batxHeadRepos.flush();
		}
	}

	@Override
	public void insertAll(List<BatxHead> batxHead, TitaVo... titaVo) throws DBException {
		if (batxHead == null || batxHead.size() == 0)
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
		for (BatxHead t : batxHead) {
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);
			if (t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
				t.setLastUpdateEmpNo(empNot);
		}

		if (dbName.equals(ContentName.onDay)) {
			batxHead = batxHeadReposDay.saveAll(batxHead);
			batxHeadReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			batxHead = batxHeadReposMon.saveAll(batxHead);
			batxHeadReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			batxHead = batxHeadReposHist.saveAll(batxHead);
			batxHeadReposHist.flush();
		} else {
			batxHead = batxHeadRepos.saveAll(batxHead);
			batxHeadRepos.flush();
		}
	}

	@Override
	public void updateAll(List<BatxHead> batxHead, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("UpdateAll...");
		if (batxHead == null || batxHead.size() == 0)
			throw new DBException(6);

		for (BatxHead t : batxHead)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			batxHead = batxHeadReposDay.saveAll(batxHead);
			batxHeadReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			batxHead = batxHeadReposMon.saveAll(batxHead);
			batxHeadReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			batxHead = batxHeadReposHist.saveAll(batxHead);
			batxHeadReposHist.flush();
		} else {
			batxHead = batxHeadRepos.saveAll(batxHead);
			batxHeadRepos.flush();
		}
	}

	@Override
	public void deleteAll(List<BatxHead> batxHead, TitaVo... titaVo) throws DBException {
		this.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (batxHead == null || batxHead.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			batxHeadReposDay.deleteAll(batxHead);
			batxHeadReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			batxHeadReposMon.deleteAll(batxHead);
			batxHeadReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			batxHeadReposHist.deleteAll(batxHead);
			batxHeadReposHist.flush();
		} else {
			batxHeadRepos.deleteAll(batxHead);
			batxHeadRepos.flush();
		}
	}

}
