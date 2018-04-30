/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  org.dom4j.Document
 *  org.dom4j.io.SAXReader
 */
package documentacion;

import documentacion.afip_wsaa_client;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.Properties;
import org.dom4j.Document;
import org.dom4j.io.SAXReader;

public class wsaa_test {
    public static void main(String[] args) {
        String LoginTicketResponse = null;
        System.setProperty("http.proxyHost", "");
        System.setProperty("http.proxyPort", "80");
        Properties config = new Properties();
        try {
            config.load(new FileInputStream("./wsaa_client.properties"));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        String endpoint = config.getProperty("endpoint", "http://wsaahomo.afip.gov.ar/ws/services/LoginCms");
        String service = config.getProperty("service", "test");
        String dstDN = config.getProperty("dstdn", "cn=wsaahomo,o=afip,c=ar,serialNumber=CUIT 33693450239");
        String p12file = config.getProperty("keystore", "test-keystore.p12");
        String signer = config.getProperty("keystore-signer", "coqui");
        String p12pass = config.getProperty("keystore-password", "miclaveprivada");
        System.setProperty("http.proxyHost", config.getProperty("http_proxy", ""));
        System.setProperty("http.proxyPort", config.getProperty("http_proxy_port", ""));
        System.setProperty("http.proxyUser", config.getProperty("http_proxy_user", ""));
        System.setProperty("http.proxyPassword", config.getProperty("http_proxy_password", ""));
        System.setProperty("javax.net.ssl.trustStore", config.getProperty("trustStore", ""));
        System.setProperty("javax.net.ssl.trustStorePassword", config.getProperty("trustStore_password", ""));
        Long TicketTime = new Long(config.getProperty("TicketTime", "36000"));
        byte[] LoginTicketRequest_xml_cms = afip_wsaa_client.create_cms(p12file, p12pass, signer, dstDN, service, TicketTime);
        try {
            LoginTicketResponse = afip_wsaa_client.invoke_wsaa(LoginTicketRequest_xml_cms, endpoint);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        try {
            StringReader tokenReader = new StringReader(LoginTicketResponse);
            Document tokenDoc = new SAXReader(false).read((Reader)tokenReader);
            String token = tokenDoc.valueOf("/loginTicketResponse/credentials/token");
            String sign = tokenDoc.valueOf("/loginTicketResponse/credentials/sign");
            System.out.println("TOKEN: " + token);
            System.out.println("SIGN: " + sign);
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }
}

