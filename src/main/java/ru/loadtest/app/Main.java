package ru.loadtest.app;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.loadtest.app.LoadTest.LoadTestAPI;

public class Main {
    public static final Logger logger = LogManager.getLogger(Main.class.getName());

    public static void main(String[] args) {
        LoadTestAPI test = new LoadTestAPI("http://sampra.sampra1.ru");
//        test.executeRandomTest("");
        test.setMaxIntervalVal(10000);
        test.executeRandomTest("", 120, 300);
        test.printStatistic();
    }
}
