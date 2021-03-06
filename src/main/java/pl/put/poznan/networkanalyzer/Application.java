package pl.put.poznan.networkanalyzer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import pl.put.poznan.networkanalyzer.service.DbFiller;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.io.IOException;

@SpringBootApplication
@EnableSwagger2
@Slf4j
public class Application {

    public static void main(String[] args) throws IOException {
        ApplicationContext ctxt = SpringApplication.run(Application.class, args);
        // fill db
        DbFiller dbFiller = ctxt.getBean(DbFiller.class);
        dbFiller.fillFromJson("graphs/graph1_v2.json");
    }

    @Bean
    public Docket apiDocket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }
}
