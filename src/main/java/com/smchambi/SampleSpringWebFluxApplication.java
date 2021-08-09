package com.smchambi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SampleSpringWebFluxApplication {

	public static void main(String[] args) {
		SpringApplication.run(SampleSpringWebFluxApplication.class, args).registerShutdownHook();
	}

}
