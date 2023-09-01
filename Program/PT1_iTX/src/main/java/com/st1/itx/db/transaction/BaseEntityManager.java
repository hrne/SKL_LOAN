package com.st1.itx.db.transaction;

import java.util.Objects;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.eum.ContentName;
import com.st1.itx.util.log.SysLogger;

@Service("baseEntityManager")
@Scope("prototype")
public class BaseEntityManager extends SysLogger {

	@PersistenceContext(unitName = "emf")
	private EntityManager emf;

	@PersistenceContext(unitName = "emfDay")
	private EntityManager emfDay;

	@PersistenceContext(unitName = "emfMon")
	private EntityManager emfMon;

	@PersistenceContext(unitName = "emfHist")
	private EntityManager emfHist;

	public EntityManager getCurrentEntityManager(TitaVo titaVo) {
		EntityManager em = null;
		if (titaVo.getDataBase().equals(ContentName.onLine)) {
			this.info("em To OnLine");
			em = emf;
		}
		if (titaVo.getDataBase().equals(ContentName.onDay)) {
			this.info("em To Day");
			em = emfDay;
		}
		if (titaVo.getDataBase().equals(ContentName.onMon)) {
			this.info("em To Mon");
			em = emfMon;
		}
		if (titaVo.getDataBase().equals(ContentName.onHist)) {
			this.info("em To Hist");
			em = emfHist;
		}
		if (em == null)
			em = emf;
		return em;
	}

	public EntityManager getCurrentEntityManager(String dbName) {
		EntityManager em = null;
		if (dbName == null)
			dbName = ContentName.onLine;
		if (dbName.equals(ContentName.onLine))
			em = emf;
		if (dbName.equals(ContentName.onDay))
			em = emfDay;
		if (dbName.equals(ContentName.onMon))
			em = emfMon;
		if (dbName.equals(ContentName.onHist))
			em = emfHist;
		if (em == null)
			em = emf;
		return em;
	}

	public void clearEntityManager(String dbName) {
		this.info("Clear Entity For Find..");
		if (Objects.isNull(dbName) || dbName.trim().isEmpty())
			dbName = ContentName.onLine;
		if (dbName.equals(ContentName.onLine))
			this.emf.clear();
		if (dbName.equals(ContentName.onDay))
			this.emfDay.clear();
		if (dbName.equals(ContentName.onMon))
			this.emfMon.clear();
		if (dbName.equals(ContentName.onHist))
			this.emfMon.clear();
	}
}
