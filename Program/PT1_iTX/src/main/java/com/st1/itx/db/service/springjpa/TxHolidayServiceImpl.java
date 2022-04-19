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
import com.st1.itx.db.domain.TxHoliday;
import com.st1.itx.db.domain.TxHolidayId;
import com.st1.itx.db.repository.online.TxHolidayRepository;
import com.st1.itx.db.repository.day.TxHolidayRepositoryDay;
import com.st1.itx.db.repository.mon.TxHolidayRepositoryMon;
import com.st1.itx.db.repository.hist.TxHolidayRepositoryHist;
import com.st1.itx.db.service.TxHolidayService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("txHolidayService")
@Repository
public class TxHolidayServiceImpl extends ASpringJpaParm implements TxHolidayService, InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private TxHolidayRepository txHolidayRepos;

	@Autowired
	private TxHolidayRepositoryDay txHolidayReposDay;

	@Autowired
	private TxHolidayRepositoryMon txHolidayReposMon;

	@Autowired
	private TxHolidayRepositoryHist txHolidayReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(txHolidayRepos);
		org.junit.Assert.assertNotNull(txHolidayReposDay);
		org.junit.Assert.assertNotNull(txHolidayReposMon);
		org.junit.Assert.assertNotNull(txHolidayReposHist);
	}

	@Override
	public TxHoliday findById(TxHolidayId txHolidayId, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findById " + dbName + " " + txHolidayId);
		Optional<TxHoliday> txHoliday = null;
		if (dbName.equals(ContentName.onDay))
			txHoliday = txHolidayReposDay.findById(txHolidayId);
		else if (dbName.equals(ContentName.onMon))
			txHoliday = txHolidayReposMon.findById(txHolidayId);
		else if (dbName.equals(ContentName.onHist))
			txHoliday = txHolidayReposHist.findById(txHolidayId);
		else
			txHoliday = txHolidayRepos.findById(txHolidayId);
		TxHoliday obj = txHoliday.isPresent() ? txHoliday.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<TxHoliday> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<TxHoliday> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;
		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "Country", "Holiday"));
		this.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = txHolidayReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = txHolidayReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = txHolidayReposHist.findAll(pageable);
		else
			slice = txHolidayRepos.findAll(pageable);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<TxHoliday> findHoliday(String country_0, int holiday_1, int holiday_2, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<TxHoliday> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("findHoliday " + dbName + " : " + "country_0 : " + country_0 + " holiday_1 : " + holiday_1 + " holiday_2 : " + holiday_2);
		if (dbName.equals(ContentName.onDay))
			slice = txHolidayReposDay.findAllByCountryIsAndHolidayGreaterThanEqualAndHolidayLessThanEqualOrderByHolidayAsc(country_0, holiday_1, holiday_2, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = txHolidayReposMon.findAllByCountryIsAndHolidayGreaterThanEqualAndHolidayLessThanEqualOrderByHolidayAsc(country_0, holiday_1, holiday_2, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = txHolidayReposHist.findAllByCountryIsAndHolidayGreaterThanEqualAndHolidayLessThanEqualOrderByHolidayAsc(country_0, holiday_1, holiday_2, pageable);
		else
			slice = txHolidayRepos.findAllByCountryIsAndHolidayGreaterThanEqualAndHolidayLessThanEqualOrderByHolidayAsc(country_0, holiday_1, holiday_2, pageable);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public TxHoliday holdById(TxHolidayId txHolidayId, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + txHolidayId);
		Optional<TxHoliday> txHoliday = null;
		if (dbName.equals(ContentName.onDay))
			txHoliday = txHolidayReposDay.findByTxHolidayId(txHolidayId);
		else if (dbName.equals(ContentName.onMon))
			txHoliday = txHolidayReposMon.findByTxHolidayId(txHolidayId);
		else if (dbName.equals(ContentName.onHist))
			txHoliday = txHolidayReposHist.findByTxHolidayId(txHolidayId);
		else
			txHoliday = txHolidayRepos.findByTxHolidayId(txHolidayId);
		return txHoliday.isPresent() ? txHoliday.get() : null;
	}

	@Override
	public TxHoliday holdById(TxHoliday txHoliday, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + txHoliday.getTxHolidayId());
		Optional<TxHoliday> txHolidayT = null;
		if (dbName.equals(ContentName.onDay))
			txHolidayT = txHolidayReposDay.findByTxHolidayId(txHoliday.getTxHolidayId());
		else if (dbName.equals(ContentName.onMon))
			txHolidayT = txHolidayReposMon.findByTxHolidayId(txHoliday.getTxHolidayId());
		else if (dbName.equals(ContentName.onHist))
			txHolidayT = txHolidayReposHist.findByTxHolidayId(txHoliday.getTxHolidayId());
		else
			txHolidayT = txHolidayRepos.findByTxHolidayId(txHoliday.getTxHolidayId());
		return txHolidayT.isPresent() ? txHolidayT.get() : null;
	}

	@Override
	public TxHoliday insert(TxHoliday txHoliday, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		}
		this.info("Insert..." + dbName + " " + txHoliday.getTxHolidayId());
		if (this.findById(txHoliday.getTxHolidayId()) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			txHoliday.setCreateEmpNo(empNot);

		if (txHoliday.getLastUpdateEmpNo() == null || txHoliday.getLastUpdateEmpNo().isEmpty())
			txHoliday.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return txHolidayReposDay.saveAndFlush(txHoliday);
		else if (dbName.equals(ContentName.onMon))
			return txHolidayReposMon.saveAndFlush(txHoliday);
		else if (dbName.equals(ContentName.onHist))
			return txHolidayReposHist.saveAndFlush(txHoliday);
		else
			return txHolidayRepos.saveAndFlush(txHoliday);
	}

	@Override
	public TxHoliday update(TxHoliday txHoliday, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("Update..." + dbName + " " + txHoliday.getTxHolidayId());
		if (!empNot.isEmpty())
			txHoliday.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return txHolidayReposDay.saveAndFlush(txHoliday);
		else if (dbName.equals(ContentName.onMon))
			return txHolidayReposMon.saveAndFlush(txHoliday);
		else if (dbName.equals(ContentName.onHist))
			return txHolidayReposHist.saveAndFlush(txHoliday);
		else
			return txHolidayRepos.saveAndFlush(txHoliday);
	}

	@Override
	public TxHoliday update2(TxHoliday txHoliday, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("Update..." + dbName + " " + txHoliday.getTxHolidayId());
		if (!empNot.isEmpty())
			txHoliday.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			txHolidayReposDay.saveAndFlush(txHoliday);
		else if (dbName.equals(ContentName.onMon))
			txHolidayReposMon.saveAndFlush(txHoliday);
		else if (dbName.equals(ContentName.onHist))
			txHolidayReposHist.saveAndFlush(txHoliday);
		else
			txHolidayRepos.saveAndFlush(txHoliday);
		return this.findById(txHoliday.getTxHolidayId());
	}

	@Override
	public void delete(TxHoliday txHoliday, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Delete..." + dbName + " " + txHoliday.getTxHolidayId());
		if (dbName.equals(ContentName.onDay)) {
			txHolidayReposDay.delete(txHoliday);
			txHolidayReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			txHolidayReposMon.delete(txHoliday);
			txHolidayReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			txHolidayReposHist.delete(txHoliday);
			txHolidayReposHist.flush();
		} else {
			txHolidayRepos.delete(txHoliday);
			txHolidayRepos.flush();
		}
	}

	@Override
	public void insertAll(List<TxHoliday> txHoliday, TitaVo... titaVo) throws DBException {
		if (txHoliday == null || txHoliday.size() == 0)
			throw new DBException(6);
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		}
		this.info("InsertAll...");
		for (TxHoliday t : txHoliday) {
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);
			if (t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
				t.setLastUpdateEmpNo(empNot);
		}

		if (dbName.equals(ContentName.onDay)) {
			txHoliday = txHolidayReposDay.saveAll(txHoliday);
			txHolidayReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			txHoliday = txHolidayReposMon.saveAll(txHoliday);
			txHolidayReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			txHoliday = txHolidayReposHist.saveAll(txHoliday);
			txHolidayReposHist.flush();
		} else {
			txHoliday = txHolidayRepos.saveAll(txHoliday);
			txHolidayRepos.flush();
		}
	}

	@Override
	public void updateAll(List<TxHoliday> txHoliday, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("UpdateAll...");
		if (txHoliday == null || txHoliday.size() == 0)
			throw new DBException(6);

		for (TxHoliday t : txHoliday)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			txHoliday = txHolidayReposDay.saveAll(txHoliday);
			txHolidayReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			txHoliday = txHolidayReposMon.saveAll(txHoliday);
			txHolidayReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			txHoliday = txHolidayReposHist.saveAll(txHoliday);
			txHolidayReposHist.flush();
		} else {
			txHoliday = txHolidayRepos.saveAll(txHoliday);
			txHolidayRepos.flush();
		}
	}

	@Override
	public void deleteAll(List<TxHoliday> txHoliday, TitaVo... titaVo) throws DBException {
		this.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (txHoliday == null || txHoliday.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			txHolidayReposDay.deleteAll(txHoliday);
			txHolidayReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			txHolidayReposMon.deleteAll(txHoliday);
			txHolidayReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			txHolidayReposHist.deleteAll(txHoliday);
			txHolidayReposHist.flush();
		} else {
			txHolidayRepos.deleteAll(txHoliday);
			txHolidayRepos.flush();
		}
	}

}
