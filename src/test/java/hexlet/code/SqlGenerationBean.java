//package hexlet.code;
//
//import hexlet.code.utils.TestUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.PostConstruct;
//import javax.sql.DataSource;
//
//@Component
//public class SqlGenerationBean {
//
//    @Autowired
//    private DataSource dataSource;
//
//    @Autowired
//    private TestUtils testUtils;
//
//    @PostConstruct
//    public void initDB() {
//        testUtils.tearDown();
//
//        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
//        populator.addScript(new ClassPathResource("data.sql"));
//        populator.execute(dataSource);
//    }
//}
