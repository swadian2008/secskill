package secskill;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Administrator
 * @date 2019/11/6/006 19:43
 * @Version 1.0
 */

@SpringBootApplication // 指定扫描的包
@MapperScan("secskill.dao") // 不要一个个在类名上加mapper了
public class MyApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyApplication.class,args);// 启动项目
        System.out.println("My Appliction start...");
    }

}
