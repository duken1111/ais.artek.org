package util;


import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.net.CookieManager.*;


/**
 * Created by DLepeshko on 14.03.2017.
 */
public class ConnectUtil {
    private static final String URL = "http://artek.org";
    private final String USER_AGENT = "Mozilla/5.0";
    RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(30 * 1000).build();
    private HttpClient client = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build();
    private String cookies;
    private static String urlPagingTemplate = "http://artek.org/admin/news/news/?p=";


    public static void main(String[] args) throws Exception {
        String url = "http://artek.org/admin/login/?next=/admin/";
        String mainPage = "http://artek.org/admin/";

        //включаем кукисы
        CookieHandler.setDefault(new CookieManager());

        ConnectUtil conn = new ConnectUtil( );

        String loginPage = conn.GetPageContent(url);

        List<NameValuePair> params = conn.getFormParams(loginPage, "dlepeshko@artek.org", "lol106tt");

        conn.sendPost(url, params);

        Integer linksCount = conn.getAllPageLinks(mainPage + "news/news/");
        //Thread.sleep(1000);




        //List<String> links = conn.getNewsLinks(linksCount);
        List<String> link = new ArrayList<>();
        link.add("http://artek.org/admin/news/news/583/");
        //System.out.println("Всего:" + links.size());

        conn.makeChange(link);

    }

    private void makeChange(List<String> links) throws Exception {
        for(String url : links) {
            String page = GetPageContent(url);
            sendPost(url,getFormParamsNews(page));
        }
    }

    private int getAllPageLinks(String url) throws Exception {

        String page = GetPageContent(url);
        Document doc = Jsoup.parse(page);
        Element el = doc.select(".pagination ul li").last();

        Integer totalPages = Integer.parseInt(el.text());
        System.out.println(totalPages);
        System.out.println("-------------------------------------------");


        return totalPages;
    }

    private List<String> getNewsLinks(Integer totalPages) throws Exception {
        List<String> links = new ArrayList<>();
        for (int i = 0; i < totalPages; i++) {
            String pageUrl = urlPagingTemplate + i;
            String page = GetPageContent(pageUrl);

            Document doc = Jsoup.parse(page);
            Elements elements = doc.select("#result_list tbody tr");

            for(Element el : elements) {
                Element a = el.select(".field-title a").first();
                links.add(URL + a.attributes().get("href"));
            }

        }

        return links;
    }

    private void sendPost(String url, List<NameValuePair> postParams)
            throws Exception {
        System.out.println("try to send post");
        HttpPost post = new HttpPost(url);

        // add header
        post.setHeader("Host", "artek.org");
        post.setHeader("User-Agent", USER_AGENT);
        post.setHeader("Accept",
                "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        post.setHeader("Accept-Language", "ru-RU,ru;q=0.8,en-US;q=0.6,en;q=0.4");
        post.setHeader("Cookie", getCookies());
        post.setHeader("Connection", "keep-alive");
        post.setHeader("Referer", "http://artek.org/admin/logout/");
        post.setHeader("Content-Type", "application/x-www-form-urlencoded");

        post.setEntity(new UrlEncodedFormEntity(postParams));


        HttpResponse response = client.execute(post);

        int responseCode = response.getStatusLine().getStatusCode();

        System.out.println("\nSending 'POST' request to URL : " + url);
        System.out.println("Post parameters : " + postParams);
        System.out.println("Response Code : " + responseCode);

        BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));

        StringBuilder result = new StringBuilder();

        //rd.lines().forEach(s -> result.append(s).append("\n"));

        post.releaseConnection();
    }



    private String GetPageContent(String url) throws Exception {

        HttpGet request = new HttpGet(url);

        request.setHeader("User-Agent", USER_AGENT);
        request.setHeader("Accept",
                "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        request.setHeader("Accept-Language", "en-US,en;q=0.5");


        HttpResponse response = client.execute(request);

        int responseCode = response.getStatusLine().getStatusCode();

        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);

        BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));

        StringBuilder result = new StringBuilder();

        rd.lines().forEach(s -> result.append(s).append("\n"));

        // set cookies

        this.setCookies(response.getFirstHeader("Set-Cookie") == null ? "" :
                response.getFirstHeader("Set-Cookie").toString());
        //System.out.println(getCookies());
        request.releaseConnection();
        return result.toString();

    }

    public List<NameValuePair> getFormParams(String html, String username, String password)throws UnsupportedEncodingException {

        System.out.println("Extracting form's data...");

        Document doc = Jsoup.parse(html);

        // Google form id
        Element loginform = doc.select("form").first();
        Elements inputElements = loginform.getElementsByTag("input");

        List<NameValuePair> paramList = new ArrayList<NameValuePair>();

        for (Element inputElement : inputElements) {
            String key = inputElement.attr("name");
            String value = inputElement.attr("value");

            if (key.equals("username"))
                value = username;
            else if (key.equals("password"))
                value = password;

            paramList.add(new BasicNameValuePair(key, value));

        }

        return paramList;
    }

    public List<NameValuePair> getFormParamsNews(String html)throws UnsupportedEncodingException {

        Document doc = Jsoup.parse(html);

        // Google form id
        Element loginform = doc.select("#news_form").first();

        List<NameValuePair> paramList = new ArrayList<NameValuePair>();

        //Все input
        Elements inputElements = loginform.getElementsByTag("input");
        for (Element inputElement : inputElements) {

                String key = inputElement.attr("name");
                String value = inputElement.attr("value");
                if(key.equals("title_en")) {
                    value = "LOLOLOLOLOOOLOOOLOOOL";
                    System.out.println(value);
                }


                paramList.add(new BasicNameValuePair(key, value));



        }

        return paramList;
    }

    public String getCookies() {
        return cookies;
    }

    public void setCookies(String cookies) {
        this.cookies = cookies;
    }
}
