package org.example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Slf4jTest {
    private static final Logger logger = LoggerFactory.getLogger(Slf4jTest.class);

    public static void main(String[] args) {
        // ref: https://www.cnblogs.com/youzhibing/p/6849843.html
        logger.info("測試 SLF4J 是否正常");
        System.out.println("SLF4J Version: " + LoggerFactory.class.getPackage().getImplementationVersion());
    }
}