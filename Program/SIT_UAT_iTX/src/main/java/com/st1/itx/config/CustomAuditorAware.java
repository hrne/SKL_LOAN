package com.st1.itx.config;

import java.util.Optional;
import java.util.Random;

import org.springframework.data.domain.AuditorAware;

public class CustomAuditorAware implements AuditorAware<Integer> {

	@Override
	public Optional<Integer> getCurrentAuditor() {
		return Optional.of(new Random().nextInt(1000));
	}

}
