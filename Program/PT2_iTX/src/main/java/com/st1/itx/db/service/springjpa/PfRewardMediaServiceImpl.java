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
import com.st1.itx.db.domain.PfRewardMedia;
import com.st1.itx.db.repository.online.PfRewardMediaRepository;
import com.st1.itx.db.repository.day.PfRewardMediaRepositoryDay;
import com.st1.itx.db.repository.mon.PfRewardMediaRepositoryMon;
import com.st1.itx.db.repository.hist.PfRewardMediaRepositoryHist;
import com.st1.itx.db.service.PfRewardMediaService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("pfRewardMediaService")
@Repository
public class PfRewardMediaServiceImpl extends ASpringJpaParm implements PfRewardMediaService, InitializingBean {
	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private PfRewardMediaRepository pfRewardMediaRepos;

	@Autowired
	private PfRewardMediaRepositoryDay pfRewardMediaReposDay;

	@Autowired
	private PfRewardMediaRepositoryMon pfRewardMediaReposMon;

	@Autowired
	private PfRewardMediaRepositoryHist pfRewardMediaReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(pfRewardMediaRepos);
		org.junit.Assert.assertNotNull(pfRewardMediaReposDay);
		org.junit.Assert.assertNotNull(pfRewardMediaReposMon);
		org.junit.Assert.assertNotNull(pfRewardMediaReposHist);
	}

	@Override
	public PfRewardMedia findById(Long bonusNo, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findById " + dbName + " " + bonusNo);
		Optional<PfRewardMedia> pfRewardMedia = null;
		if (dbName.equals(ContentName.onDay))
			pfRewardMedia = pfRewardMediaReposDay.findById(bonusNo);
		else if (dbName.equals(ContentName.onMon))
			pfRewardMedia = pfRewardMediaReposMon.findById(bonusNo);
		else if (dbName.equals(ContentName.onHist))
			pfRewardMedia = pfRewardMediaReposHist.findById(bonusNo);
		else
			pfRewardMedia = pfRewardMediaRepos.findById(bonusNo);
		PfRewardMedia obj = pfRewardMedia.isPresent() ? pfRewardMedia.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<PfRewardMedia> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<PfRewardMedia> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;
		if (limit == Integer.MAX_VALUE)
			pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "BonusNo"));
		else
			pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "BonusNo"));
		this.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = pfRewardMediaReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = pfRewardMediaReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = pfRewardMediaReposHist.findAll(pageable);
		else
			slice = pfRewardMediaRepos.findAll(pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<PfRewardMedia> findWorkMonth(int workMonth_0, List<Integer> bonusType_1, int mediaFg_2, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<PfRewardMedia> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("findWorkMonth " + dbName + " : " + "workMonth_0 : " + workMonth_0 + " bonusType_1 : " + bonusType_1 + " mediaFg_2 : " + mediaFg_2);
		if (dbName.equals(ContentName.onDay))
			slice = pfRewardMediaReposDay.findAllByWorkMonthIsAndBonusTypeInAndMediaFgIs(workMonth_0, bonusType_1, mediaFg_2, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = pfRewardMediaReposMon.findAllByWorkMonthIsAndBonusTypeInAndMediaFgIs(workMonth_0, bonusType_1, mediaFg_2, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = pfRewardMediaReposHist.findAllByWorkMonthIsAndBonusTypeInAndMediaFgIs(workMonth_0, bonusType_1, mediaFg_2, pageable);
		else
			slice = pfRewardMediaRepos.findAllByWorkMonthIsAndBonusTypeInAndMediaFgIs(workMonth_0, bonusType_1, mediaFg_2, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<PfRewardMedia> findFacmNo(int custNo_0, int facmNo_1, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<PfRewardMedia> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("findFacmNo " + dbName + " : " + "custNo_0 : " + custNo_0 + " facmNo_1 : " + facmNo_1);
		if (dbName.equals(ContentName.onDay))
			slice = pfRewardMediaReposDay.findAllByCustNoIsAndFacmNoIsOrderByPerfDateAsc(custNo_0, facmNo_1, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = pfRewardMediaReposMon.findAllByCustNoIsAndFacmNoIsOrderByPerfDateAsc(custNo_0, facmNo_1, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = pfRewardMediaReposHist.findAllByCustNoIsAndFacmNoIsOrderByPerfDateAsc(custNo_0, facmNo_1, pageable);
		else
			slice = pfRewardMediaRepos.findAllByCustNoIsAndFacmNoIsOrderByPerfDateAsc(custNo_0, facmNo_1, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public PfRewardMedia findDupFirst(int custNo_0, int facmNo_1, int bormNo_2, int bonusType_3, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findDupFirst " + dbName + " : " + "custNo_0 : " + custNo_0 + " facmNo_1 : " + facmNo_1 + " bormNo_2 : " + bormNo_2 + " bonusType_3 : " + bonusType_3);
		Optional<PfRewardMedia> pfRewardMediaT = null;
		if (dbName.equals(ContentName.onDay))
			pfRewardMediaT = pfRewardMediaReposDay.findTopByCustNoIsAndFacmNoIsAndBormNoIsAndBonusTypeIs(custNo_0, facmNo_1, bormNo_2, bonusType_3);
		else if (dbName.equals(ContentName.onMon))
			pfRewardMediaT = pfRewardMediaReposMon.findTopByCustNoIsAndFacmNoIsAndBormNoIsAndBonusTypeIs(custNo_0, facmNo_1, bormNo_2, bonusType_3);
		else if (dbName.equals(ContentName.onHist))
			pfRewardMediaT = pfRewardMediaReposHist.findTopByCustNoIsAndFacmNoIsAndBormNoIsAndBonusTypeIs(custNo_0, facmNo_1, bormNo_2, bonusType_3);
		else
			pfRewardMediaT = pfRewardMediaRepos.findTopByCustNoIsAndFacmNoIsAndBormNoIsAndBonusTypeIs(custNo_0, facmNo_1, bormNo_2, bonusType_3);

		return pfRewardMediaT.isPresent() ? pfRewardMediaT.get() : null;
	}

	@Override
	public PfRewardMedia holdById(Long bonusNo, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + bonusNo);
		Optional<PfRewardMedia> pfRewardMedia = null;
		if (dbName.equals(ContentName.onDay))
			pfRewardMedia = pfRewardMediaReposDay.findByBonusNo(bonusNo);
		else if (dbName.equals(ContentName.onMon))
			pfRewardMedia = pfRewardMediaReposMon.findByBonusNo(bonusNo);
		else if (dbName.equals(ContentName.onHist))
			pfRewardMedia = pfRewardMediaReposHist.findByBonusNo(bonusNo);
		else
			pfRewardMedia = pfRewardMediaRepos.findByBonusNo(bonusNo);
		return pfRewardMedia.isPresent() ? pfRewardMedia.get() : null;
	}

	@Override
	public PfRewardMedia holdById(PfRewardMedia pfRewardMedia, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + pfRewardMedia.getBonusNo());
		Optional<PfRewardMedia> pfRewardMediaT = null;
		if (dbName.equals(ContentName.onDay))
			pfRewardMediaT = pfRewardMediaReposDay.findByBonusNo(pfRewardMedia.getBonusNo());
		else if (dbName.equals(ContentName.onMon))
			pfRewardMediaT = pfRewardMediaReposMon.findByBonusNo(pfRewardMedia.getBonusNo());
		else if (dbName.equals(ContentName.onHist))
			pfRewardMediaT = pfRewardMediaReposHist.findByBonusNo(pfRewardMedia.getBonusNo());
		else
			pfRewardMediaT = pfRewardMediaRepos.findByBonusNo(pfRewardMedia.getBonusNo());
		return pfRewardMediaT.isPresent() ? pfRewardMediaT.get() : null;
	}

	@Override
	public PfRewardMedia insert(PfRewardMedia pfRewardMedia, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Insert..." + dbName + " " + pfRewardMedia.getBonusNo());
		if (this.findById(pfRewardMedia.getBonusNo()) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			pfRewardMedia.setCreateEmpNo(empNot);

		if (pfRewardMedia.getLastUpdateEmpNo() == null || pfRewardMedia.getLastUpdateEmpNo().isEmpty())
			pfRewardMedia.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return pfRewardMediaReposDay.saveAndFlush(pfRewardMedia);
		else if (dbName.equals(ContentName.onMon))
			return pfRewardMediaReposMon.saveAndFlush(pfRewardMedia);
		else if (dbName.equals(ContentName.onHist))
			return pfRewardMediaReposHist.saveAndFlush(pfRewardMedia);
		else
			return pfRewardMediaRepos.saveAndFlush(pfRewardMedia);
	}

	@Override
	public PfRewardMedia update(PfRewardMedia pfRewardMedia, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + pfRewardMedia.getBonusNo());
		if (!empNot.isEmpty())
			pfRewardMedia.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return pfRewardMediaReposDay.saveAndFlush(pfRewardMedia);
		else if (dbName.equals(ContentName.onMon))
			return pfRewardMediaReposMon.saveAndFlush(pfRewardMedia);
		else if (dbName.equals(ContentName.onHist))
			return pfRewardMediaReposHist.saveAndFlush(pfRewardMedia);
		else
			return pfRewardMediaRepos.saveAndFlush(pfRewardMedia);
	}

	@Override
	public PfRewardMedia update2(PfRewardMedia pfRewardMedia, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + pfRewardMedia.getBonusNo());
		if (!empNot.isEmpty())
			pfRewardMedia.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			pfRewardMediaReposDay.saveAndFlush(pfRewardMedia);
		else if (dbName.equals(ContentName.onMon))
			pfRewardMediaReposMon.saveAndFlush(pfRewardMedia);
		else if (dbName.equals(ContentName.onHist))
			pfRewardMediaReposHist.saveAndFlush(pfRewardMedia);
		else
			pfRewardMediaRepos.saveAndFlush(pfRewardMedia);
		return this.findById(pfRewardMedia.getBonusNo());
	}

	@Override
	public void delete(PfRewardMedia pfRewardMedia, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Delete..." + dbName + " " + pfRewardMedia.getBonusNo());
		if (dbName.equals(ContentName.onDay)) {
			pfRewardMediaReposDay.delete(pfRewardMedia);
			pfRewardMediaReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			pfRewardMediaReposMon.delete(pfRewardMedia);
			pfRewardMediaReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			pfRewardMediaReposHist.delete(pfRewardMedia);
			pfRewardMediaReposHist.flush();
		} else {
			pfRewardMediaRepos.delete(pfRewardMedia);
			pfRewardMediaRepos.flush();
		}
	}

	@Override
	public void insertAll(List<PfRewardMedia> pfRewardMedia, TitaVo... titaVo) throws DBException {
		if (pfRewardMedia == null || pfRewardMedia.size() == 0)
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
		for (PfRewardMedia t : pfRewardMedia) {
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);
			if (t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
				t.setLastUpdateEmpNo(empNot);
		}

		if (dbName.equals(ContentName.onDay)) {
			pfRewardMedia = pfRewardMediaReposDay.saveAll(pfRewardMedia);
			pfRewardMediaReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			pfRewardMedia = pfRewardMediaReposMon.saveAll(pfRewardMedia);
			pfRewardMediaReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			pfRewardMedia = pfRewardMediaReposHist.saveAll(pfRewardMedia);
			pfRewardMediaReposHist.flush();
		} else {
			pfRewardMedia = pfRewardMediaRepos.saveAll(pfRewardMedia);
			pfRewardMediaRepos.flush();
		}
	}

	@Override
	public void updateAll(List<PfRewardMedia> pfRewardMedia, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("UpdateAll...");
		if (pfRewardMedia == null || pfRewardMedia.size() == 0)
			throw new DBException(6);

		for (PfRewardMedia t : pfRewardMedia)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			pfRewardMedia = pfRewardMediaReposDay.saveAll(pfRewardMedia);
			pfRewardMediaReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			pfRewardMedia = pfRewardMediaReposMon.saveAll(pfRewardMedia);
			pfRewardMediaReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			pfRewardMedia = pfRewardMediaReposHist.saveAll(pfRewardMedia);
			pfRewardMediaReposHist.flush();
		} else {
			pfRewardMedia = pfRewardMediaRepos.saveAll(pfRewardMedia);
			pfRewardMediaRepos.flush();
		}
	}

	@Override
	public void deleteAll(List<PfRewardMedia> pfRewardMedia, TitaVo... titaVo) throws DBException {
		this.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (pfRewardMedia == null || pfRewardMedia.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			pfRewardMediaReposDay.deleteAll(pfRewardMedia);
			pfRewardMediaReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			pfRewardMediaReposMon.deleteAll(pfRewardMedia);
			pfRewardMediaReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			pfRewardMediaReposHist.deleteAll(pfRewardMedia);
			pfRewardMediaReposHist.flush();
		} else {
			pfRewardMediaRepos.deleteAll(pfRewardMedia);
			pfRewardMediaRepos.flush();
		}
	}

}
