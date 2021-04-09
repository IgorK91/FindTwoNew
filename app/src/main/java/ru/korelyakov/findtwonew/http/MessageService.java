package ru.korelyakov.findtwonew.http;

import org.apache.http.auth.AuthenticationException;
import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;

public class MessageService {
    private static final Logger log = LoggerFactory.getLogger(ru.korelyakov.findtwonew.http.MessageService.class);

    public static String sendGetRequest(String uri, String path, LogTypeEnum logType, Map<String, String> paramValue) throws Exception {
        HttpURLConnection con = getConnectionWithUrl(uri, path, paramValue);

        // Setting basic post request
        con.setRequestMethod("GET");
        con.setRequestProperty("Content-Type", "application/json");
        con.setConnectTimeout(1000);
        // Send get request
        con.setDoOutput(false);

        if (logType != LogTypeEnum.LogOff)
            log.info(String.format("отправка GET url %s", con.getURL().toString()));

        return processResponse(con, logType);
    }

    public static String sendPostRequest(String uri, String path, LogTypeEnum logType, Map<String, String> paramValue, String bodyJsonValue) throws Exception {
        HttpURLConnection con = getConnectionWithUrl(uri, path, paramValue);

        // Setting basic post request
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Accept", "application/json");
        con.setConnectTimeout(1000);
        // Send get request
        con.setDoOutput(false);

        if (logType != LogTypeEnum.LogOff) {
            if (logType == LogTypeEnum.LogAll || logType == LogTypeEnum.LogUrlAndBody) {
                String replace = bodyJsonValue.trim().replace("\n", "").
                        replace("\r", "").replaceAll(" +", " ");
                log.info(String.format("отправка POST url %s body %s", con.getURL().toString(), replace));
            } else
                log.info(String.format("отправка POST url %s", con.getURL().toString()));
        }

        try (OutputStream os = con.getOutputStream()) {
            byte[] input = bodyJsonValue.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        return processResponse(con, logType);
    }

    private static String processResponse(HttpURLConnection con, LogTypeEnum logType) throws Exception {
        int responseCode = con.getResponseCode();
        if (logType != LogTypeEnum.LogOff)
            log.info(String.format("response code %s", responseCode));
        if (responseCode != 200) {
            if (responseCode == 403)
                throw new AuthenticationException("Неверное имя пользователя или пароль!");
            throw new Exception("Failed : HTTP error code : " + responseCode);
        }

        String answer = getAnswer(con);
        if (answer != null && !answer.isEmpty() &&
                (logType == LogTypeEnum.LogAll || logType == LogTypeEnum.LogUrlAndAnswer))
            log.info(String.format("Ответ сервера %s", answer));
        return answer;
    }

    private static HttpURLConnection getConnectionWithUrl(String uri, String path, Map<String, String> paramValue) throws URISyntaxException, IOException {
        URIBuilder builder = new URIBuilder(uri);
        builder.setPath(path);
        if (paramValue != null) paramValue.forEach(builder::setParameter);

        URL obj = builder.build().toURL();
        return (HttpURLConnection) obj.openConnection();
    }

    private static String getAnswer(HttpURLConnection con) throws IOException {
        String output;
        try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
            output = in.lines().collect(Collectors.joining());
        }
        con.getInputStream().close();
        con.disconnect();
        return output;
    }

    /**
     * Enum вариантов записей запросов в лог
     */
    public enum LogTypeEnum {
        /**
         * Ничего не пишем
         */
        LogOff,
        /**
         * Пишем только URL запроса
         */
        LogUrl,
        /**
         * Пишем URL и тело запроса
         */
        LogUrlAndBody,
        /**
         * Пишем URL и ответ
         */
        LogUrlAndAnswer,
        /**
         * Пишем URL, тело запроса и ответ
         */
        LogAll
    }
}