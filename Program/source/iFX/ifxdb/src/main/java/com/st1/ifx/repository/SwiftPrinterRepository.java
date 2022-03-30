package com.st1.ifx.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.st1.ifx.domain.SwiftPrinter;

public interface SwiftPrinterRepository extends JpaRepository<SwiftPrinter, String> {
	public List<SwiftPrinter> findByBrn(String brn);
}
