package io.pivotal.pal.tracker;

import com.mysql.cj.jdbc.MysqlDataSource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class PalTrackerApplication {
    public static void main(String[] args) {
        SpringApplication.run(PalTrackerApplication.class, args);
    }

    @Bean
    public TimeEntryRepository getTimeImpl(){
     //  TimeEntryRepository timeEntryRepository = new InMemoryTimeEntryRepository();
        MysqlDataSource mysqlDataSource = new MysqlDataSource();
        mysqlDataSource.setURL(System.getenv("SPRING_DATASOURCE_URL"));
        JdbcTimeEntryRepository timeEntryRepository = new JdbcTimeEntryRepository(mysqlDataSource);
       return  timeEntryRepository;
    }
}
