package com.simple.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.net.ssl.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
public class CommonUtil {

    /**
     *
     * Header와 본문 전송 내용을 전달
     * Common RestApi 호출 post
     * @param param
     * @return
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> apiCallPostByHeader(Map<String, String> param, Object body, Map<String, String> header, HttpServletRequest request) {
        log.info("apiCallPostByHeader POST Call : " + param);
        String callUrl = param.get("apiUrl");
        JSONObject json = new JSONObject(body);

        Map<String, Object> resultMap = null;

        String inputLine = null;
        URL url = null;
        HttpURLConnection conn = null;

        StringBuffer outResult = new StringBuffer();
        try {
            url = new URL(callUrl);
            conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST"); // http 메서드
            conn.setRequestProperty("Content-Type", "application/json"); // header Content-Type 정보
            conn.setRequestProperty("Accept-Charset", "UTF-8");

            if(header != null)
            {
                Iterator<String> keys = header.keySet().iterator();

                while (keys.hasNext()){
                    String key = keys.next();
                    conn.setRequestProperty(key, header.get(key));
                }
            }

            conn.setDoOutput(true); // 서버로부터 받는 값이 있다면 true

            OutputStream os = conn.getOutputStream();
            os.write(json.toString().getBytes("UTF-8"));
            os.flush();

            int responseCode = conn.getResponseCode();

            InputStream ins = null;

            if(responseCode == 200)
            {
                ins = conn.getInputStream();
            }
            else
            {
                ins = conn.getErrorStream();
            }

            // 리턴된 결과 읽기
            BufferedReader in = new BufferedReader(new InputStreamReader(ins, "UTF-8"));
            while ((inputLine = in.readLine()) != null) {
                outResult.append(inputLine);
            }

            conn.disconnect();

            resultMap = new ObjectMapper().readValue(outResult.toString(), Map.class);

            // email 인증 요청 성공시 세션 세팅
            //if (String.valueOf(resultMap.get("count")).equals("1")) {
            if(request != null && "1".equals(String.valueOf(resultMap.get("count"))))
            {
                String code = param.get("code");
                log.info("cert: "+code);
                request.getSession().setAttribute("emailCertTime", System.currentTimeMillis());
                request.getSession().setAttribute("emailCertNum", code);

                resultMap.put("result", "OK");
            }

        } catch (Exception e) {
            //e.printStackTrace();
            log.info(e.toString());



        }

        log.info(resultMap.toString());

        return resultMap;
    }

    public static String apiCallGet(Map<String, String> param) {

        URL url = null;
        HttpsURLConnection con = null;
        StringBuffer ret = new StringBuffer();

        try {
            url = new URL(param.get("apiDomain")  + param.get("apiUrl"));

            ignoreSsl();

            con = (HttpsURLConnection) url.openConnection();

            BufferedReader br = null;
            br = new BufferedReader(new InputStreamReader(con.getInputStream()));

            String input = null;

            while ((input = br.readLine()) != null) {
                ret.append(input);
            }

            br.close();
        } catch (IOException e) {
            // ExceptionUtil.getStackTrace(e);
            e.getStackTrace();
        } catch (Exception e) {
            // ExceptionUtil.getStackTrace(e);
            e.getStackTrace();

        } finally {
            if (con != null) {
                con.disconnect();
            }
        }

        return ret.toString();
    }

    public static void ignoreSsl() throws Exception {
        HostnameVerifier hv = new HostnameVerifier() {
            public boolean verify(String urlHostName, SSLSession session) {
                return true;
            }
        };

        trustAllHttpsCertificates();
        HttpsURLConnection.setDefaultHostnameVerifier(hv);
    }

    private static void trustAllHttpsCertificates() throws Exception {
        TrustManager[] trustAllCerts = new TrustManager[1];
        TrustManager tm = new miTM();
        trustAllCerts[0] = tm;
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, null);
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
    }

    static class miTM implements TrustManager, X509TrustManager {
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        public boolean isServerTrusted(X509Certificate[] certs) {
            return true;
        }

        public boolean isClientTrusted(X509Certificate[] certs) {
            return true;
        }

        public void checkServerTrusted(X509Certificate[] certs, String authType) throws CertificateException {
            return;
        }

        public void checkClientTrusted(X509Certificate[] certs, String authType) throws CertificateException {
            return;
        }
    }


    /**
     * ===============================================================
     */
    public static String getOrdNoSequence()
    {
        StringBuffer temp = new StringBuffer();
        Random rnd = new Random();
        for (int i = 0; i < 10; i++) {
            int rIndex = rnd.nextInt(3);
            switch (rIndex) {
                case 0:
                    // a-z
                    temp.append((char) ((int) (rnd.nextInt(26)) + 97));
                    break;
                case 1:
                    // A-Z
                    temp.append((char) ((int) (rnd.nextInt(26)) + 65));
                    break;
                case 2:
                    // 0-9
                    temp.append((rnd.nextInt(10)));
                    break;
            }
        }

        SimpleDateFormat vans = new SimpleDateFormat("yyyyMM");
        return vans.format(new Date()) + "-" + temp.toString();
    }

    public static String getFileID(String prefix) {
        return getUUID(prefix);
    }

    public static String getUUID(String preFix)
    {
        StringBuffer temp = new StringBuffer();
        Random rnd = new Random();
        for (int i = 0; i < 10; i++) {
//			rnd.setSeed(System.currentTimeMillis()); // 랜덤 중복 방지 위해 Seed를 현재시간으로 심기
            int rIndex = rnd.nextInt(3);
            switch (rIndex) {
                case 0:
                    // a-z
                    temp.append((char) ((int) (rnd.nextInt(26)) + 97));
                    break;
                case 1:
                    // A-Z
                    temp.append((char) ((int) (rnd.nextInt(26)) + 65));
                    break;
                case 2:
                    // 0-9
                    temp.append((rnd.nextInt(10)));
                    break;
            }
        }

        return preFix + "-" + Long.toString(System.currentTimeMillis(), 36) + "-" + temp.toString();
    }

    public static boolean isNullEmpty(String str) {

        if (str == null) return true;
        if (str.trim().length() == 0) return true;

        return false;
    }

    public static String getRemoteIP(HttpServletRequest request){
        String ip = request.getHeader("X-FORWARDED-FOR");

        //proxy 환경일 경우
        if (ip == null || ip.length() == 0) {
            ip = request.getHeader("Proxy-Client-IP");
        }

        //웹로직 서버일 경우
        if (ip == null || ip.length() == 0) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }

        if (ip == null || ip.length() == 0) {
            ip = request.getRemoteAddr() ;
        }

        return ip;
    }

    public static void beanCopy(Object source, Object target)
    {
        BeanUtils.copyProperties(source, target);
    }

    public static String getRandomUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static void setPageParam(Map<String, Object> param) {
        String page;
        int start = 1;
        if(param.get("start") != null) start = Integer.parseInt(param.get("start").toString());
        int length = 1;
        if(param.get("length") != null) length = Integer.parseInt(param.get("length").toString());
        page = Integer.toString((start/length) + 1);

        param.put("pageCount", param.get("length"));
        param.put("page", page);
    }
}
