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
import com.st1.itx.db.domain.HlAreaLnYg6Pt;
import com.st1.itx.db.domain.HlAreaLnYg6PtId;
import com.st1.itx.db.repository.online.HlAreaLnYg6PtRepository;
import com.st1.itx.db.repository.day.HlAreaLnYg6PtRepositoryDay;
import com.st1.itx.db.repository.mon.HlAreaLnYg6PtRepositoryMon;
import com.st1.itx.db.repository.hist.HlAreaLnYg6PtRepositoryHist;
import com.st1.itx.db.service.HlAreaLnYg6PtService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("hlAreaLnYg6PtService")
@Repository
public class HlAreaLnYg6PtServiceImpl extends ASpringJpaParm implements HlAreaLnYg6PtService, InitializingBean {
	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private HlAreaLnYg6PtRepository hlAreaLnYg6PtRepos;

	@Autowired
	private HlAreaLnYg6PtRepositoryDay hlAreaLnYg6PtReposDay;

	@Autowired
	private HlAreaLnYg6PtRepositoryMon hlAreaLnYg6PtReposMon;

	@Autowired
	private HlAreaLnYg6PtRepositoryHist hlAreaLnYg6PtReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(hlAreaLnYg6PtRepos);
		org.junit.Assert.assertNotNull(hlAreaLnYg6PtReposDay);
		org.junit.Assert.assertNotNull(hlAreaLnYg6PtReposMon);
		org.junit.Assert.assertNotNull(hlAreaLnYg6PtReposHist);
	}

	@Override
	public HlAreaLnYg6Pt findById(HlAreaLnYg6PtId hlAreaLnYg6PtId, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findById " + dbName + " " + hlAreaLnYg6PtId);
		Optional<HlAreaLnYg6Pt> hlAreaLnYg6Pt = null;
		if (dbName.equals(ContentName.onDay))
			hlAreaLnYg6Pt = hlAreaLnYg6PtReposDay.findById(hlAreaLnYg6PtId);
		else if (dbName.equals(ContentName.onMon))
			hlAreaLnYg6Pt = hlAreaLnYg6PtReposMon.findById(hlAreaLnYg6PtId);
		else if (dbName.equals(ContentName.onHist))
			hlAreaLnYg6Pt = hlAreaLnYg6PtReposHist.findById(hlAreaLnYg6PtId);
		else
			hlAreaLnYg6Pt = hlAreaLnYg6PtRepos.findById(hlAreaLnYg6PtId);
		HlAreaLnYg6Pt obj = hlAreaLnYg6Pt.isPresent() ? hlAreaLnYg6Pt.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<HlAreaLnYg6Pt> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<HlAreaLnYg6Pt> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;
		if (limit == Integer.MAX_VALUE)
			pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "WorkYM", "AreaUnitNo"));
		else
			pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "WorkYM", "AreaUnitNo"));
		this.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = hlAreaLnYg6PtReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = hlAreaLnYg6PtReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = hlAreaLnYg6PtReposHist.findAll(pageable);
		else
			slice = hlAreaLnYg6PtRepos.findAll(pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<HlAreaLnYg6Pt> FindWorkYM(String workYM_0, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<HlAreaLnYg6Pt> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("FindWorkYM " + dbName + " : " + "workYM_0 : " + workYM_0);
		if (dbName.equals(ContentName.onDay))
			slice = hlAreaLnYg6PtReposDay.findAllByWorkYMIsOrderByWorkYMAscAreaUnitNoAsc(workYM_0, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = hlAreaLnYg6PtReposMon.findAllByWorkYMIsOrderByWorkYMAscAreaUnitNoAsc(workYM_0, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = hlAreaLnYg6PtReposHist.findAllByWorkYMIsOrderByWorkYMAscAreaUnitNoAsc(workYM_0, pageable);
		else
			slice = hlAreaLnYg6PtRepos.findAllByWorkYMIsOrderByWorkYMAscAreaUnitNoAsc(workYM_0, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<HlAreaLnYg6Pt> FindAreaUnitNo(String areaUnitNo_0, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<HlAreaLnYg6Pt> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("FindAreaUnitNo " + dbName + " : " + "areaUnitNo_0 : " + areaUnitNo_0);
		if (dbName.equals(ContentName.onDay))
			slice = hlAreaLnYg6PtReposDay.findAllByAreaUnitNoIsOrderByWorkYMAscAreaUnitNoAsc(areaUnitNo_0, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = hlAreaLnYg6PtReposMon.findAllByAreaUnitNoIsOrderByWorkYMAscAreaUnitNoAsc(areaUnitNo_0, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = hlAreaLnYg6PtReposHist.findAllByAreaUnitNoIsOrderByWorkYMAscAreaUnitNoAsc(areaUnitNo_0, pageable);
		else
			slice = hlAreaLnYg6PtRepos.findAllByAreaUnitNoIsOrderByWorkYMAscAreaUnitNoAsc(areaUnitNo_0, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public HlAreaLnYg6Pt holdById(HlAreaLnYg6PtId hlAreaLnYg6PtId, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + hlAreaLnYg6PtId);
		Optional<HlAreaLnYg6Pt> hlAreaLnYg6Pt = null;
		if (dbName.equals(ContentName.onDay))
			hlAreaLnYg6Pt = hlAreaLnYg6PtReposDay.findByHlAreaLnYg6PtId(hlAreaLnYg6PtId);
		else if (dbName.equals(ContentName.onMon))
			hlAreaLnYg6Pt = hlAreaLnYg6PtReposMon.findByHlAreaLnYg6PtId(hlAreaLnYg6PtId);
		else if (dbName.equals(ContentName.onHist))
			hlAreaLnYg6Pt = hlAreaLnYg6PtReposHist.findByHlAreaLnYg6PtId(hlAreaLnYg6PtId);
		else
			hlAreaLnYg6Pt = hlAreaLnYg6PtRepos.findByHlAreaLnYg6PtId(hlAreaLnYg6PtId);
		return hlAreaLnYg6Pt.isPresent() ? hlAreaLnYg6Pt.get() : null;
	}

	@Override
	public HlAreaLnYg6Pt holdById(HlAreaLnYg6Pt hlAreaLnYg6Pt, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + hlAreaLnYg6Pt.getHlAreaLnYg6PtId());
		Optional<HlAreaLnYg6Pt> hlAreaLnYg6PtT = null;
		if (dbName.equals(ContentName.onDay))
			hlAreaLnYg6PtT = hlAreaLnYg6PtReposDay.findByHlAreaLnYg6PtId(hlAreaLnYg6Pt.getHlAreaLnYg6PtId());
		else if (dbName.equals(ContentName.onMon))
			hlAreaLnYg6PtT = hlAreaLnYg6PtReposMon.findByHlAreaLnYg6PtId(hlAreaLnYg6Pt.getHlAreaLnYg6PtId());
		else if (dbName.equals(ContentName.onHist))
			hlAreaLnYg6PtT = hlAreaLnYg6PtReposHist.findByHlAreaLnYg6PtId(hlAreaLnYg6Pt.getHlAreaLnYg6PtId());
		else
			hlAreaLnYg6PtT = hlAreaLnYg6PtRepos.findByHlAreaLnYg6PtId(hlAreaLnYg6Pt.getHlAreaLnYg6PtId());
		return hlAreaLnYg6PtT.isPresent() ? hlAreaLnYg6PtT.get() : null;
	}

	@Override
	public HlAreaLnYg6Pt insert(HlAreaLnYg6Pt hlAreaLnYg6Pt, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Insert..." + dbName + " " + hlAreaLnYg6Pt.getHlAreaLnYg6PtId());
		if (this.findById(hlAreaLnYg6Pt.getHlAreaLnYg6PtId()) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			hlAreaLnYg6Pt.setCreateEmpNo(empNot);

		if (hlAreaLnYg6Pt.getLastUpdateEmpNo() == null || hlAreaLnYg6Pt.getLastUpdateEmpNo().isEmpty())
			hlAreaLnYg6Pt.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return hlAreaLnYg6PtReposDay.saveAndFlush(hlAreaLnYg6Pt);
		else if (dbName.equals(ContentName.onMon))
			return hlAreaLnYg6PtReposMon.saveAndFlush(hlAreaLnYg6Pt);
		else if (dbName.equals(ContentName.onHist))
			return hlAreaLnYg6PtReposHist.saveAndFlush(hlAreaLnYg6Pt);
		else
			return hlAreaLnYg6PtRepos.saveAndFlush(hlAreaLnYg6Pt);
	}

	@Override
	public HlAreaLnYg6Pt update(HlAreaLnYg6Pt hlAreaLnYg6Pt, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + hlAreaLnYg6Pt.getHlAreaLnYg6PtId());
		if (!empNot.isEmpty())
			hlAreaLnYg6Pt.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return hlAreaLnYg6PtReposDay.saveAndFlush(hlAreaLnYg6Pt);
		else if (dbName.equals(ContentName.onMon))
			return hlAreaLnYg6PtReposMon.saveAndFlush(hlAreaLnYg6Pt);
		else if (dbName.equals(ContentName.onHist))
			return hlAreaLnYg6PtReposHist.saveAndFlush(hlAreaLnYg6Pt);
		else
			return hlAreaLnYg6PtRepos.saveAndFlush(hlAreaLnYg6Pt);
	}

	@Override
	public HlAreaLnYg6Pt update2(HlAreaLnYg6Pt hlAreaLnYg6Pt, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + hlAreaLnYg6Pt.getHlAreaLnYg6PtId());
		if (!empNot.isEmpty())
			hlAreaLnYg6Pt.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			hlAreaLnYg6PtReposDay.saveAndFlush(hlAreaLnYg6Pt);
		else if (dbName.equals(ContentName.onMon))
			hlAreaLnYg6PtReposMon.saveAndFlush(hlAreaLnYg6Pt);
		else if (dbName.equals(ContentName.onHist))
			hlAreaLnYg6PtReposHist.saveAndFlush(hlAreaLnYg6Pt);
		else
			hlAreaLnYg6PtRepos.saveAndFlush(hlAreaLnYg6Pt);
		return this.findById(hlAreaLnYg6Pt.getHlAreaLnYg6PtId());
	}

	@Override
	public void delete(HlAreaLnYg6Pt hlAreaLnYg6Pt, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Delete..." + dbName + " " + hlAreaLnYg6Pt.getHlAreaLnYg6PtId());
		if (dbName.equals(ContentName.onDay)) {
			hlAreaLnYg6PtReposDay.delete(hlAreaLnYg6Pt);
			hlAreaLnYg6PtReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			hlAreaLnYg6PtReposMon.delete(hlAreaLnYg6Pt);
			hlAreaLnYg6PtReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			hlAreaLnYg6PtReposHist.delete(hlAreaLnYg6Pt);
			hlAreaLnYg6PtReposHist.flush();
		} else {
			hlAreaLnYg6PtRepos.delete(hlAreaLnYg6Pt);
			hlAreaLnYg6PtRepos.flush();
		}
	}

	@Override
	public void insertAll(List<HlAreaLnYg6Pt> hlAreaLnYg6Pt, TitaVo... titaVo) throws DBException {
		if (hlAreaLnYg6Pt == null || hlAreaLnYg6Pt.size() == 0)
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
		for (HlAreaLnYg6Pt t : hlAreaLnYg6Pt) {
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);
			if (t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
				t.setLastUpdateEmpNo(empNot);
		}

		if (dbName.equals(ContentName.onDay)) {
			hlAreaLnYg6Pt = hlAreaLnYg6PtReposDay.saveAll(hlAreaLnYg6Pt);
			hlAreaLnYg6PtReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			hlAreaLnYg6Pt = hlAreaLnYg6PtReposMon.saveAll(hlAreaLnYg6Pt);
			hlAreaLnYg6PtReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			hlAreaLnYg6Pt = hlAreaLnYg6PtReposHist.saveAll(hlAreaLnYg6Pt);
			hlAreaLnYg6PtReposHist.flush();
		} else {
			hlAreaLnYg6Pt = hlAreaLnYg6PtRepos.saveAll(hlAreaLnYg6Pt);
			hlAreaLnYg6PtRepos.flush();
		}
	}

	@Override
	public void updateAll(List<HlAreaLnYg6Pt> hlAreaLnYg6Pt, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("UpdateAll...");
		if (hlAreaLnYg6Pt == null || hlAreaLnYg6Pt.size() == 0)
			throw new DBException(6);

		for (HlAreaLnYg6Pt t : hlAreaLnYg6Pt)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			hlAreaLnYg6Pt = hlAreaLnYg6PtReposDay.saveAll(hlAreaLnYg6Pt);
			hlAreaLnYg6PtReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			hlAreaLnYg6Pt = hlAreaLnYg6PtReposMon.saveAll(hlAreaLnYg6Pt);
			hlAreaLnYg6PtReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			hlAreaLnYg6Pt = hlAreaLnYg6PtReposHist.saveAll(hlAreaLnYg6Pt);
			hlAreaLnYg6PtReposHist.flush();
		} else {
			hlAreaLnYg6Pt = hlAreaLnYg6PtRepos.saveAll(hlAreaLnYg6Pt);
			hlAreaLnYg6PtRepos.flush();
		}
	}

	@Override
	public void deleteAll(List<HlAreaLnYg6Pt> hlAreaLnYg6Pt, TitaVo... titaVo) throws DBException {
		this.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (hlAreaLnYg6Pt == null || hlAreaLnYg6Pt.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			hlAreaLnYg6PtReposDay.deleteAll(hlAreaLnYg6Pt);
			hlAreaLnYg6PtReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			hlAreaLnYg6PtReposMon.deleteAll(hlAreaLnYg6Pt);
			hlAreaLnYg6PtReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			hlAreaLnYg6PtReposHist.deleteAll(hlAreaLnYg6Pt);
			hlAreaLnYg6PtReposHist.flush();
		} else {
			hlAreaLnYg6PtRepos.deleteAll(hlAreaLnYg6Pt);
			hlAreaLnYg6PtRepos.flush();
		}
	}

}
