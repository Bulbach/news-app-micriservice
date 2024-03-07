package by.alex.newsappmicriservice;

import org.springframework.boot.SpringApplication;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

//@TestConfiguration(proxyBeanMethods = false)
public class TestNewsAppMicriserviceApplication {

//    @Bean
//    @ServiceConnection
    PostgreSQLContainer<?> postgresContainer() {
        return new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"));
    }

    public static void main(String[] args) {
        SpringApplication.from(NewsAppMicriserviceApplication::main).with(TestNewsAppMicriserviceApplication.class).run(args);
    }

}
