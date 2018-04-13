package com.app.wte;

import javax.annotation.Resource;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.app.wte.util.StorageService;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@SpringBootApplication
public class SpringAngularApplication implements CommandLineRunner {

	@Resource
	StorageService storageService;

	public static void main(String[] args) {
		SpringApplication.run(SpringAngularApplication.class, args);
	}

	@Bean
	public Docket propertyServiceApi() {
		return new Docket(DocumentationType.SWAGGER_2).apiInfo(propertyServiceInfo()).select()
				.apis(RequestHandlerSelectors.basePackage("com.app.wte.controller")).paths(PathSelectors.any()).build()
				.pathMapping("/");
	}

	private ApiInfo propertyServiceInfo() {
		return new ApiInfoBuilder().title("WTE Service API").description("WTE Service information").version("1.0")
				.build();
	}

	@Override
	public void run(String... args) throws Exception {
		storageService.init();
	}

}
