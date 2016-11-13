package ru.loadtest.app.LoadTest;

import ru.loadtest.app.LoadTest.AppCore.ConnectionAPI;

/**
 * The class provide you several public methods to operate with LoadTest Application. Use this class in your applications.
 * Default constructor uses localhost with port 8802. You should call constructor with URL and port or only with port if you want
 * to use localhost with other port.
 * <p>
 * Use other constructors if you want to get access to remote web-sites.
 */
public class LoadTestAPI {
    private String URL;
    private int port = 8802;

    public LoadTestAPI() {
        URL = "localhost:" + port;
    }

    public LoadTestAPI(String URL, int port) {
        this.URL = URL + port;
    }

    public LoadTestAPI(String URL) {
        this.URL = URL;
    }

    /**
     * Execute test with random visits of pages.
     *
     * @param startURL This is the first point of exploring. Explore is started from address.zone if @param startURL is empty string.
     */
    public void executeRandomTest(String startURL) {
        ConnectionAPI connection = new ConnectionAPI(this.URL, startURL);
        connection.start();
    }

    /**
     * Overloaded method with manual timeout value
     * @param startURL This is the first point of exploring. Explore is started from address.zone if @param startURL is empty string.
     * @param timeout value in ms that set timeout for testing time
     */
    public void executeRandomTest(String startURL, long timeout, int userNumber) {
        ConnectionAPI.setTimeout(timeout);
        Thread[] threads = new Thread[userNumber];
        for(int i=0; i<threads.length; i++) {
            threads[i] = new ConnectionAPI(this.URL, startURL);
            threads[i].start();
        }
    }
}
