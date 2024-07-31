package lol.aabss.skhttp.objects.server;

import com.sun.net.httpserver.HttpsConfigurator;
import com.sun.net.httpserver.HttpsParameters;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.TrustManagerFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.security.*;

public class HttpsServer extends HttpServer {

    public HttpsServer(int port) throws IOException {
        this(port, 0);
    }

    public HttpsServer(int port, int backlog) throws IOException {
        this(com.sun.net.httpserver.HttpsServer.create(new InetSocketAddress(port), backlog));
    }

    public HttpsServer(com.sun.net.httpserver.HttpsServer server) {
        super(server);
        if (configuratorSet() == null) {
            setupSSL();
        }
    }

    private void setupSSL() {
        try {
            File keyStoreFile = new File("skhttp.keystore");
            if (!keyStoreFile.exists()) {
                generateKeyStore();
            }
            char[] password = "skhttppassword".toCharArray();
            KeyStore ks = KeyStore.getInstance("JKS");
            ks.load(new FileInputStream(keyStoreFile), password);
            KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
            kmf.init(ks, password);
            TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
            tmf.init(ks);
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

            HttpsConfigurator config = new HttpsConfigurator(sslContext) {
                @Override
                public void configure(HttpsParameters params) {
                    try {
                        SSLParameters sslParams = sslContext.getDefaultSSLParameters();
                        params.setNeedClientAuth(false);
                        params.setCipherSuites(sslParams.getCipherSuites());
                        params.setProtocols(sslParams.getProtocols());
                        params.setSSLParameters(sslParams);
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                }
            };

            ((com.sun.net.httpserver.HttpsServer) this.server).setHttpsConfigurator(config);

        } catch (Exception e) {
            System.err.println("Error setting up SSL: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private void generateKeyStore() throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder(
                "keytool", "-genkeypair", "-alias", "myalias", "-keyalg", "RSA", "-keysize", "2048",
                "-keystore", "skhttp.keystore", "-storepass", "skhttppassword", "-validity", "365",
                "-dname", "CN=localhost, OU=Development, O=MyCompany, L=MyCity, S=MyState, C=US"
        );
        processBuilder.inheritIO();
        Process process = processBuilder.start();
        int exitCode = process.waitFor();
        if (exitCode == 0) {
            System.out.println("KeyStore generated successfully");
        } else {
            System.err.println("Error generating KeyStore, exit code: " + exitCode);
        }
    }

    private HttpsConfigurator configuratorSet() {
        return ((com.sun.net.httpserver.HttpsServer) this.server).getHttpsConfigurator();
    }
}
