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
import com.st1.itx.db.domain.HlCusData;
import com.st1.itx.db.repository.online.HlCusDataRepository;
import com.st1.itx.db.repository.day.HlCusDataRepositoryDay;
import com.st1.itx.db.repository.mon.HlCusDataRepositoryMon;
import com.st1.itx.db.repository.hist.HlCusDataRepositoryHist;
import com.st1.itx.db.service.HlCusDataService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("hlCusDataService")
@Repository
public class HlCusDataServiceImpl extends ASpringJpaParm implements HlCusDataService, InitializingBean {
	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private HlCusDataRepository hlCusDataRepos;

	@Autowired
	private HlCusDataRepositoryDay hlCusDataReposDay;

	@Autowired
	private HlCusDataRepositoryMon hlCusDataReposMon;

	@Autowired
	private HlCusDataRepositoryHist hlCusDataReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(hlCusDataRepos);
		org.junit.Assert.assertNotNull(hlCusDataReposDay);
		org.junit.Assert.assertNotNull(hlCusDataReposMon);
		org.junit.Assert.assertNotNull(hlCusDataReposHist);
	}

	@Override
	public HlCusData findById(Long hlCusNo, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findById " + dbName + " " + hlCusNo);
		Optional<HlCusData> hlCusData = null;
		if (dbName.equals(ContentName.onDay))
			hlCusData = hlCusDataReposDay.findById(hlCusNo);
		else if (dbName.equals(ContentName.onMon))
			hlCusData = hlCusDataReposMon.findById(hlCusNo);
		else if (dbName.equals(ContentName.onHist))
			hlCusData = hlCusDataReposHist.findById(hlCusNo);
		else
			hlCusData = hlCusDataRepos.findById(hlCusNo);
		HlCusData obj = hlCusData.isPresent() ? hlCusData.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<HlCusData> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<HlCusData> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;
		if (limit == Integer.MAX_VALUE)
			pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "HlCusNo"));
		else
			pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "HlCusNo"));
		this.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = hlCusDataReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = hlCusDataReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = hlCusDataReposHist.findAll(pageable);
		else
			slice = hlCusDataRepos.findAll(pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public HlCusData holdById(Long hlCusNo, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + hlCusNo);
		Optional<HlCusData> hlCusData = null;
		if (dbName.equals(ContentName.onDay))
			hlCusData = hlCusDataReposDay.findByHlCusNo(hlCusNo);
		else if (dbName.equals(ContentName.onMon))
			hlCusData = hlCusDataReposMon.findByHlCusNo(hlCusNo);
		else if (dbName.equals(ContentName.onHist))
			hlCusData = hlCusDataReposHist.findByHlCusNo(hlCusNo);
		else
			hlCusData = hlCusDataRepos.findByHlCusNo(hlCusNo);
		return hlCusData.isPresent() ? hlCusData.get() : null;
	}

	@Override
	public HlCusData holdById(HlCusData hlCusData, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + hlCusData.getHlCusNo());
		Optional<HlCusData> hlCusDataT = null;
		if (dbName.equals(ContentName.onDay))
			hlCusDataT = hlCusDataReposDay.findByHlCusNo(hlCusData.getHlCusNo());
		else if (dbName.equals(ContentName.onMon))
			hlCusDataT = hlCusDataReposMon.findByHlCusNo(hlCusData.getHlCusNo());
		else if (dbName.equals(ContentName.onHist))
			hlCusDataT = hlCusDataReposHist.findByHlCusNo(hlCusData.getHlCusNo());
		else
			hlCusDataT = hlCusDataRepos.findByHlCusNo(hlCusData.getHlCusNo());
		return hlCusDataT.isPresent() ? hlCusDataT.get() : null;
	}

	@Override
	public HlCusData insert(HlCusData hlCusData, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Insert..." + dbName + " " + hlCusData.getHlCusNo());
		if (this.findById(hlCusData.getHlCusNo()) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			hlCusData.setCreateEmpNo(empNot);

		if (hlCusData.getLastUpdateEmpNo() == null || hlCusData.getLastUpdateEmpNo().isEmpty())
			hlCusData.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return hlCusDataReposDay.saveAndFlush(hlCusData);
		else if (dbName.equals(ContentName.onMon))
			return hlCusDataReposMon.saveAndFlush(hlCusData);
		else if (dbName.equals(ContentName.onHist))
			return hlCusDataReposHist.saveAndFlush(hlCusData);
		else
			return hlCusDataRepos.saveAndFlush(hlCusData);
	}

	@Override
	public HlCusData update(HlCusData hlCusData, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + hlCusData.getHlCusNo());
		if (!empNot.isEmpty())
			hlCusData.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return hlCusDataReposDay.saveAndFlush(hlCusData);
		else if (dbName.equals(ContentName.onMon))
			return hlCusDataReposMon.saveAndFlush(hlCusData);
		else if (dbName.equals(ContentName.onHist))
			return hlCusDataReposHist.saveAndFlush(hlCusData);
		else
			return hlCusDataRepos.saveAndFlush(hlCusData);
	}

	@Override
	public HlCusData update2(HlCusData hlCusData, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + hlCusData.getHlCusNo());
		if (!empNot.isEmpty())
			hlCusData.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			hlCusDataReposDay.saveAndFlush(hlCusData);
		else if (dbName.equals(ContentName.onMon))
			hlCusDataReposMon.saveAndFlush(hlCusData);
		else if (dbName.equals(ContentName.onHist))
			hlCusDataReposHist.saveAndFlush(hlCusData);
		else
			hlCusDataRepos.saveAndFlush(hlCusData);
		return this.findById(hlCusData.getHlCusNo());
	}

	@Override
	public void delete(HlCusData hlCusData, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Delete..." + dbName + " " + hlCusData.getHlCusNo());
		if (dbName.equals(ContentName.onDay)) {
			hlCusDataReposDay.delete(hlCusData);
			hlCusDataReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			hlCusDataReposMon.delete(hlCusData);
			hlCusDataReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			hlCusDataReposHist.delete(hlCusData);
			hlCusDataReposHist.flush();
		} else {
			hlCusDataRepos.delete(hlCusData);
			hlCusDataRepos.flush();
		}
	}

	@Override
	public void insertAll(List<HlCusData> hlCusData, TitaVo... titaVo) throws DBException {
		if (hlCusData == null || hlCusData.size() == 0)
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
		for (HlCusData t : hlCusData) {
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);
			if (t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
				t.setLastUpdateEmpNo(empNot);
		}

		if (dbName.equals(ContentName.onDay)) {
			hlCusData = hlCusDataReposDay.saveAll(hlCusData);
			hlCusDataReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			hlCusData = hlCusDataReposMon.saveAll(hlCusData);
			hlCusDataReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			hlCusData = hlCusDataReposHist.saveAll(hlCusData);
			hlCusDataReposHist.flush();
		} else {
			hlCusData = hlCusDataRepos.saveAll(hlCusData);
			hlCusDataRepos.flush();
		}
	}

	@Override
	public void updateAll(List<HlCusData> hlCusData, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("UpdateAll...");
		if (hlCusData == null || hlCusData.size() == 0)
			throw new DBException(6);

		for (HlCusData t : hlCusData)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			hlCusData = hlCusDataReposDay.saveAll(hlCusData);
			hlCusDataReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			hlCusData = hlCusDataReposMon.saveAll(hlCusData);
			hlCusDataReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			hlCusData = hlCusDataReposHist.saveAll(hlCusData);
			hlCusDataReposHist.flush();
		} else {
			hlCusData = hlCusDataRepos.saveAll(hlCusData);
			hlCusDataRepos.flush();
		}
	}

	@Override
	public void deleteAll(List<HlCusData> hlCusData, TitaVo... titaVo) throws DBException {
		this.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (hlCusData == null || hlCusData.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			hlCusDataReposDay.deleteAll(hlCusData);
			hlCusDataReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			hlCusDataReposMon.deleteAll(hlCusData);
			hlCusDataReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			hlCusDataReposHist.deleteAll(hlCusData);
			hlCusDataReposHist.flush();
		} else {
			hlCusDataRepos.deleteAll(hlCusData);
			hlCusDataRepos.flush();
		}
	}

}
