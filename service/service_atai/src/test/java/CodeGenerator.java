import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import org.junit.Test;

public class CodeGenerator {

    @Test
    public void run() {
        // 全局配置
        GlobalConfig config = new GlobalConfig();
        config.setActiveRecord(true) //是否支持AR模式
                .setAuthor("baiyunRain") //作者
                .setOutputDir("D:\\Projects\\JavaProjects\\ATAI_BigData_Backend\\service\\service_ucenter\\src\\main\\java")
                //生成路径
                .setFileOverride(true) //文件覆盖
                .setServiceName("%sService") //设置生成的service接口名 首字母是否为I
                .setOpen(false)     //生成后是否打开资源管理器
                .setIdType(IdType.ID_WORKER_STR) //主键策略
                .setDateType(DateType.ONLY_DATE) //定义生成的实体类中日期类型
                .setSwagger2(true)  //开启Swagger2模式
        ;


        // 数据源配置
        DataSourceConfig dsConfig = new DataSourceConfig();
        dsConfig.setDbType(DbType.MYSQL)
                .setUrl("jdbc:mysql://localhost:3306/atai?serverTimezone=UTC")
                .setDriverName("com.mysql.cj.jdbc.Driver")
                .setUsername("root")
                .setPassword("sfz200108");

        // 策略配置
        StrategyConfig stConfig = new StrategyConfig();
        stConfig.setCapitalMode(true) // 全局大写命名
                .setNaming(NamingStrategy.underline_to_camel) // 数据库表映射到实体的命名策略
                .setColumnNaming(NamingStrategy.underline_to_camel)
                .setInclude("ucenter_basic") //生成的表
                .setTablePrefix("") // 表前缀
                .setEntityLombokModel(true) // lombok模型
                .setRestControllerStyle(true)   //restful api风格转换器
                .setControllerMappingHyphenStyle(true)  //url中驼峰转连字符
        ;

        // 包名策略
        PackageConfig pkConfig = new PackageConfig();
        pkConfig.setParent("com.atai")
                .setModuleName("ucenter")
                .setController("controller")
                .setEntity("entity")
                .setService("service");

        // 整合配置
        AutoGenerator mpg = new
                AutoGenerator().setGlobalConfig(config)
                .setDataSource(dsConfig)
                .setStrategy(stConfig)
                .setPackageInfo(pkConfig);

        mpg.execute();

    }
}