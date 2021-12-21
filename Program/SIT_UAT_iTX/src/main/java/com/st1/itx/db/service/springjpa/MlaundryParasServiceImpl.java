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
import com.st1.itx.db.domain.MlaundryParas;
import com.st1.itx.db.repository.online.MlaundryParasRepository;
import com.st1.itx.db.repository.day.MlaundryParasRepositoryDay;
import com.st1.itx.db.repository.mon.MlaundryParasRepositoryMon;
import com.st1.itx.db.repository.hist.MlaundryParasRepositoryHist;
import com.st1.itx.db.service.MlaundryParasService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("mlaundryParasService")
@Repository
public class MlaundryParasServiceImpl extends ASpringJpaParm implements MlaundryParasService, InitializingBean {
	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private MlaundryParasRepository mlaundryParasRepos;

	@Autowired
	private MlaundryParasRepositoryDay mlaundryParasReposDay;

	@Autowired
	private MlaundryParasRepositoryMon mlaundryParasReposMon;

	@Autowired
	private MlaundryParasRepositoryHist mlaundryParasReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(mlaundryParasRepos);
		org.junit.Assert.assertNotNull(mlaundryParasReposDay);
		org.junit.Assert.assertNotNull(mlaundryParasReposMon);
		org.junit.Assert.assertNotNull(mlaundryParasReposHist);
	}

	@Override
	public MlaundryParas findById(String businessType, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findById " + dbName + " " + businessType);
		Optional<MlaundryParas> mlaundryParas = null;
		if (dbName.equals(ContentName.onDay))
			mlaundryParas = mlaundryParasReposDay.findById(businessType);
		else if (dbName.equals(ContentName.onMon))
			mlaundryParas = mlaundryParasReposMon.findById(businessType);
		else if (dbName.equals(ContentName.onHist))
			mlaundryParas = mlaundryParasReposHist.findById(businessType);
		else
			mlaundryParas = mlaundryParasRepos.findById(businessType);
		MlaundryParas obj = mlaundryParas.isPresent() ? mlaundryParas.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<MlaundryParas> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<MlaundryParas> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;
		if (limit == Integer.MAX_VALUE)
			pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "BusinessType"));
		else
			pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "BusinessType"));
		this.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = mlaundryParasReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = mlaundryParasReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = mlaundryParasReposHist.findAll(pageable);
		else
			slice = mlaundryParasRepos.findAll(pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public MlaundryParas holdById(String businessType, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + businessType);
		Optional<MlaundryParas> mlaundryParas = null;
		if (dbName.equals(ContentName.onDay))
			mlaundryParas = mlaundryParasReposDay.findByBusinessType(businessType);
		else if (dbName.equals(ContentName.onMon))
			mlaundryParas = mlaundryParasReposMon.findByBusinessType(businessType);
		else if (dbName.equals(ContentName.onHist))
			mlaundryParas = mlaundryParasReposHist.findByBusinessType(businessType);
		else
			mlaundryParas = mlaundryParasRepos.findByBusinessType(businessType);
		return mlaundryParas.isPresent() ? mlaundryParas.get() : null;
	}

	@Override
	public MlaundryParas holdById(MlaundryParas mlaundryParas, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + mlaundryParas.getBusinessType());
		Optional<MlaundryParas> mlaundryParasT = null;
		if (dbName.equals(ContentName.onDay))
			mlaundryParasT = mlaundryParasReposDay.findByBusinessType(mlaundryParas.getBusinessType());
		else if (dbName.equals(ContentName.onMon))
			mlaundryParasT = mlaundryParasReposMon.findByBusinessType(mlaundryParas.getBusinessType());
		else if (dbName.equals(ContentName.onHist))
			mlaundryParasT = mlaundryParasReposHist.findByBusinessType(mlaundryParas.getBusinessType());
		else
			mlaundryParasT = mlaundryParasRepos.findByBusinessType(mlaundryParas.getBusinessType());
		return mlaundryParasT.isPresent() ? mlaundryParasT.get() : null;
	}

	@Override
	public MlaundryParas insert(MlaundryParas mlaundryParas, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Insert..." + dbName + " " + mlaundryParas.getBusinessType());
		if (this.findById(mlaundryParas.getBusinessType()) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			mlaundryParas.setCreateEmpNo(empNot);

		if (mlaundryParas.getLastUpdateEmpNo() == null || mlaundryParas.getLastUpdateEmpNo().isEmpty())
			mlaundryParas.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return mlaundryParasReposDay.saveAndFlush(mlaundryParas);
		else if (dbName.equals(ContentName.onMon))
			return mlaundryParasReposMon.saveAndFlush(mlaundryParas);
		else if (dbName.equals(ContentName.onHist))
			return mlaundryParasReposHist.saveAndFlush(mlaundryParas);
		else
			return mlaundryParasRepos.saveAndFlush(mlaundryParas);
	}

	@Override
	public MlaundryParas update(MlaundryParas mlaundryParas, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + mlaundryParas.getBusinessType());
		if (!empNot.isEmpty())
			mlaundryParas.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return mlaundryParasReposDay.saveAndFlush(mlaundryParas);
		else if (dbName.equals(ContentName.onMon))
			return mlaundryParasReposMon.saveAndFlush(mlaundryParas);
		else if (dbName.equals(ContentName.onHist))
			return mlaundryParasReposHist.saveAndFlush(mlaundryParas);
		else
			return mlaundryParasRepos.saveAndFlush(mlaundryParas);
	}

	@Override
	public MlaundryParas update2(MlaundryParas mlaundryParas, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + mlaundryParas.getBusinessType());
		if (!empNot.isEmpty())
			mlaundryParas.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			mlaundryParasReposDay.saveAndFlush(mlaundryParas);
		else if (dbName.equals(ContentName.onMon))
			mlaundryParasReposMon.saveAndFlush(mlaundryParas);
		else if (dbName.equals(ContentName.onHist))
			mlaundryParasReposHist.saveAndFlush(mlaundryParas);
		else
			mlaundryParasRepos.saveAndFlush(mlaundryParas);
		return this.findById(mlaundryParas.getBusinessType());
	}

	@Override
	public void delete(MlaundryParas mlaundryParas, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Delete..." + dbName + " " + mlaundryParas.getBusinessType());
		if (dbName.equals(ContentName.onDay)) {
			mlaundryParasReposDay.delete(mlaundryParas);
			mlaundryParasReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			mlaundryParasReposMon.delete(mlaundryParas);
			mlaundryParasReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			mlaundryParasReposHist.delete(mlaundryParas);
			mlaundryParasReposHist.flush();
		} else {
			mlaundryParasRepos.delete(mlaundryParas);
			mlaundryParasRepos.flush();
		}
	}

	@Override
	public void insertAll(List<MlaundryParas> mlaundryParas, TitaVo... titaVo) throws DBException {
		if (mlaundryParas == null || mlaundryParas.size() == 0)
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
		for (MlaundryParas t : mlaundryParas) {
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);
			if (t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
				t.setLastUpdateEmpNo(empNot);
		}

		if (dbName.equals(ContentName.onDay)) {
			mlaundryParas = mlaundryParasReposDay.saveAll(mlaundryParas);
			mlaundryParasReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			mlaundryParas = mlaundryParasReposMon.saveAll(mlaundryParas);
			mlaundryParasReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			mlaundryParas = mlaundryParasReposHist.saveAll(mlaundryParas);
			mlaundryParasReposHist.flush();
		} else {
			mlaundryParas = mlaundryParasRepos.saveAll(mlaundryParas);
			mlaundryParasRepos.flush();
		}
	}

	@Override
	public void updateAll(List<MlaundryParas> mlaundryParas, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("UpdateAll...");
		if (mlaundryParas == null || mlaundryParas.size() == 0)
			throw new DBException(6);

		for (MlaundryParas t : mlaundryParas)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			mlaundryParas = mlaundryParasReposDay.saveAll(mlaundryParas);
			mlaundryParasReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			mlaundryParas = mlaundryParasReposMon.saveAll(mlaundryParas);
			mlaundryParasReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			mlaundryParas = mlaundryParasReposHist.saveAll(mlaundryParas);
			mlaundryParasReposHist.flush();
		} else {
			mlaundryParas = mlaundryParasRepos.saveAll(mlaundryParas);
			mlaundryParasRepos.flush();
		}
	}

	@Override
	public void deleteAll(List<MlaundryParas> mlaundryParas, TitaVo... titaVo) throws DBException {
		this.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (mlaundryParas == null || mlaundryParas.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			mlaundryParasReposDay.deleteAll(mlaundryParas);
			mlaundryParasReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			mlaundryParasReposMon.deleteAll(mlaundryParas);
			mlaundryParasReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			mlaundryParasReposHist.deleteAll(mlaundryParas);
			mlaundryParasReposHist.flush();
		} else {
			mlaundryParasRepos.deleteAll(mlaundryParas);
			mlaundryParasRepos.flush();
		}
	}

}
