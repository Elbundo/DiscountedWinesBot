package com.elbundo.Discountedwinesbot.handers;

import com.elbundo.Discountedwinesbot.DiscountedWinesBotApplication;
import com.elbundo.Discountedwinesbot.data.Wine;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class WineStyle implements Handler{
    private final String DISCOUNT = "discount-circle";
    private final String LOGO = "itemprop=\"logo\"";
    private final String TITLE = "data-prodname";
    private final String ALIAS = "<a href=\"";
    private final String DISCOUNTPRICE = "word-price";
    private final String PRICE = "price-old";
    @Override
    public List<Wine> handle() {
        String site = "https://winestyle.ru/promo/champagnes-and-sparkling/wine/russia/1500ml/750ml/discount_ll/";
        List<Wine> list = new ArrayList<>();
        int page = 1;
        while (true) {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(site + "?page=" + page);
            httpGet.setHeader(HttpHeaders.USER_AGENT, "Chrome/1.0");
            CloseableHttpResponse httpResponse;
            InputStream stream = null;
            try {
                httpResponse = httpClient.execute(httpGet);
                log.info("" + httpResponse.getStatusLine().getStatusCode());
                if (httpResponse.getStatusLine().getStatusCode() != 200) {
                    break;
                }
                stream = httpResponse.getEntity().getContent();
            } catch (IOException e) {
                //Сделать что-нибудь
            }
            try (BufferedReader in = new BufferedReader(new InputStreamReader(stream))) {
                String line;
                StringBuilder document = new StringBuilder();
                int i = 0, start, end;
                while ((line = in.readLine()) != null) {
                    document.append(line);
                }
                log.info(String.format("Get document page %d", page));
                while (true) {
                    Wine wine = new Wine();
                    i = document.indexOf(DISCOUNT, i + 1);
                    log.info(String.format("1 = %d", i));
                    if (i == -1)
                        break;
                    start = i = document.indexOf(">", i + 1);
                    end = i = document.indexOf("%", i + 1);
                    wine.setDiscount(Integer.parseInt(document.substring(start + 1, end)));

                    i = document.indexOf(LOGO, i + 1);
                    log.info(String.format("2 = %d", i));
                    start = i = document.indexOf("'", i + 1);
                    end = i = document.indexOf("'", i + 1);
                    wine.setImage(document.substring(start + 1, end));

                    i = document.indexOf(TITLE, i + 1);
                    log.info(String.format("3 = %d", i));
                    i = document.indexOf(">", i + 1);
                    start = i = document.indexOf(">", i + 1);
                    end = i = document.indexOf("<", i + 1);
                    wine.setName(document.substring(start + 1, end));

                    i = document.indexOf(ALIAS, i + 1);
                    log.info(String.format("4 = %d", i));
                    start = i = document.indexOf(">", i + 1);
                    end = i = document.indexOf("<", i + 1);
                    wine.setAlias(document.substring(start + 1, end));

                    i = document.indexOf(DISCOUNTPRICE, i + 1);
                    log.info(String.format("5 = %d", i));
                    i = document.indexOf(">", i + 1);
                    i = document.indexOf(">", i + 1);
                    start = i = document.indexOf(">", i + 1);
                    end = i = document.indexOf("<", i + 1);
                    wine.setPriceWithDiscount(Double.parseDouble(document.substring(start + 1, end).replaceAll(" ", "")));

                    i = document.indexOf(PRICE, i + 1);
                    log.info(String.format("6 = %d", i));
                    start = i = document.indexOf(">", i + 1);
                    end = i = document.indexOf("<", i + 1);
                    wine.setPrice(Double.parseDouble(document.substring(start + 1, end).replaceAll(" ", "")));

                    if (wine.getPrice() < DiscountedWinesBotApplication.MIN_PRICE)
                        continue;
                    wine.setSite("winestyle");
                    list.add(wine);
                }
            } catch (Exception ignored) {

            }
            page++;
        }
        log.info("Result: " + list);
        return list;
    }
}
