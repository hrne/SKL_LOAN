package com.st1.ifx.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.st1.ifx.domain.Sbctl;
import com.st1.ifx.domain.SbctlId;

public interface SbctlRepository extends JpaRepository<Sbctl, SbctlId> {

}
