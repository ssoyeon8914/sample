package com.simple.batch;

import com.simple.util.AES256;
import com.simple.util.CommonUtil;
import com.simple.util.DateTimeUtil;
import com.simple.util.GlobalVariables;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class BatchService {


    // 매월 1일, 15일 02:30 분에 실행
//    @Scheduled(cron = "0 30 2 1,15 * *")
    public void getTotalCMSContent() {
        /**
         * 1일15일에 풀 배치 작업
         * 데이터 백업 *_back table
         *
         * 1. 날짜를 암호화 해서 daily_content에 값 전송
         * 		- "/daily_content?service=mteacher&params=" + aes.encryptAES256(getParamString(today)));
         *
         * 2. 1번에서 받은 json 주소를 다시 조회해서 전체 항목의 누적데이터를 json으로 수신
         *
         * 3. meta_keyword는 별도의 테이블이 아닌 | 구분자를 통해서 cms_content 테이블에 입력
         *
         * (아래 생략)
         * 4. cms_content insert
         *
         * 5. cms_content_detail, cms_cntent_contract insert
         * 		- insert하기 전에 해당 항목을 delete 하고 insert
         */

        Map<String, String> param = new HashMap<String, String>();

        AES256 aes = new AES256();

        BufferedReader br = null;
        FileReader fr = null;

        int loop_index = 0;			// 전체 line수 count
        int thread_count = 10;		// Thread 생성 개수
        int page_count = 1000;		// Thread당 한번에 해야 할 작업 수
        // thread당 전체 작업 묶음 수 ==> page_count 만큼 몇번의 일을 하는지 체크
        int[] thread_work_count = new int[thread_count];

        param.put("apiDomain", GlobalVariables.CmsValue.CMS_API_URL);
        try{
            System.out.println("!==================== start : ");
            File file = null;

            String today = DateTimeUtil.getSystemDate("yyyy-MM-dd");

            log.info("today : " + today);

            // 파라미터 암호화 해서 일일 주소를 전달 받음
            param.put("apiUrl", "/total_content?service=" + GlobalVariables.CmsValue.CMS_SERVICE + "&params=" + aes.encryptAES256(getParamString(today)));

            log.info("apiDomain : " + GlobalVariables.CmsValue.CMS_API_URL);
            log.info("apiUrl : " + param.get("apiUrl"));

            String result = CommonUtil.apiCallGet(param);

            log.info("result : " + result);

            JSONObject json = new JSONObject(result);

            String return_url = json.getJSONObject("body").getString("result");

            // 전달받은 주소를 가지고 다시 접속해서 일일 누적 수정데이터 수신
            param.clear();
            param.put("apiDomain", return_url);
            param.put("apiUrl", "");

            log.info("total_url : " + return_url);

            file = new File(GlobalVariables.LocalValue.CMS_ROOT + CommonUtil.getUUID("TF") + ".txt");

            FileUtils.copyURLToFile(new URL(return_url), file);
        }catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("!==================== : finish File Down load");
        log.info("finish File Down load");
    }


    private String getParamString(String today) {
        StringBuffer ap = new StringBuffer().append("{\"today\": \"" + today + "\"}");
        return ap.toString();
    }
}
