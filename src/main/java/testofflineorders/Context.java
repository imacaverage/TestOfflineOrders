package testofflineorders;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

/**
 * Конфигурационный класс Spring
 * Created by iMacAverage on 06.02.16.
 */
@Configuration
public class Context {

    @Bean
    public DriverManagerDataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("oracle.jdbc.driver.OracleDriver");
        dataSource.setUrl("jdbc:oracle:thin:@10.49.197.116:1522:mcpsdb");
        dataSource.setUsername("MCPS_DEV");
        dataSource.setPassword("TC*\"h9`i1h}@=RB");
        return dataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource());
    }

    @Bean
    public OracleMCPSDao oracleMCPSDao() {
        return new OracleMCPSDao(jdbcTemplate());
    }

    @Bean
    public Grabber oldGrabber() {
        return new Grabber(oracleMCPSDao(), TestOfflineOrders.COUNT_GRAB_ORDERS, true);
    }

    @Bean
    public Grabber newGrabber() {
        return new Grabber(oracleMCPSDao(), TestOfflineOrders.COUNT_GRAB_ORDERS, false);
    }

}
