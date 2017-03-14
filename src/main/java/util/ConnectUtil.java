package util;

import org.apache.http.Header;
import org.apache.http.HeaderIterator;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by DLepeshko on 14.03.2017.
 */
public class ConnectUtil {

    private final String TO_CONNECT = "http://world-of-legends.su/grecheskaya/gress_myths";
    private final String USER_AGENT = "Mozilla/5.0";

    public static void main(String[] args) throws Exception {

        ConnectUtil http = new ConnectUtil();

        String html = http.sendGet(http.TO_CONNECT);
        List<String> urlToParse = http.getUrlStrings(html);
        System.out.println(urlToParse.size());
        List<Element> elements = new ArrayList<>();

        for(String url : urlToParse) {
            String page = http.sendGet(url);
            elements.addAll(http.getElements(page));
        }

        System.out.println(elements.size());
    }

    private String sendGet(String url) throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);

        try (CloseableHttpResponse httpResponse = httpClient.execute(httpGet)) {
            int status = httpResponse.getStatusLine().getStatusCode();
            System.out.println(url + ": " + httpResponse.getStatusLine().toString());


            if (status >= 200 && status < 300) {
                HttpEntity entity = httpResponse.getEntity();
                return entity != null ? EntityUtils.toString(entity) : null;
            } else {
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
        }

    }

    private List<String> getUrlStrings(String html) {
        List<String> result = new ArrayList<>();
        if(html == null)
            return result;

        Document doc = Jsoup.parse(html);

        Element lastPage = doc.select("#pagenation a").last();
        int totalPages = Integer.parseInt(lastPage.html());
        String urlTemplate = lastPage.attr("href").replaceAll("\\d","");

        for(int i = 1; i <= totalPages;i++) {
            result.add(urlTemplate + i);
        }

        return result;
    }

    private List<Element> getElements(String html) {
        List<Element> result = new ArrayList<>();
        if(html == null)
            return null;

        Document doc = Jsoup.parse(html);
        Elements el = doc.select("#main_collum .link a");

        el.forEach(result::add);

        return result;
    }

}
