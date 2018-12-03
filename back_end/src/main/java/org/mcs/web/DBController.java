package org.mcs.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.mcs.entity.NoiseMessage;
import org.mcs.entity.Value;
import org.mcs.service.NoiseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * created by SunHongbin on 2018/10/20
 */
@Controller
@RequestMapping("/tools")
public class DBController {

    private File audioFile;

    private String fileName = "temp1.amr";

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private static ObjectMapper objectMapper = new ObjectMapper();

    @Resource
    private NoiseService collectDb;

    /**
     * 传分贝值+录音文件（temp.amr）
     */
    @RequestMapping(value = "/db_File", method = RequestMethod.POST)
    @ResponseBody
    public String collectDBAndFile(HttpServletRequest request) {

        if (audioFile == null) {
            logger.error("传文件失败：" + audioFile);
        }
        try {
            byte[] bytes = IOUtils.toByteArray(new FileInputStream(audioFile));
            String userPhone = request.getParameter("userPhone");
            System.out.println("电话: " + userPhone);
            NoiseMessage noiseMessage = new NoiseMessage();
            noiseMessage.setMp3File(bytes);
            request.getParameterMap().toString();
            collectDb.create(noiseMessage, Long.valueOf(userPhone));
            return "=====>>> “collectDB” success: " + noiseMessage.toString();
        } catch (IOException e) {
            logger.error("collectDB: ", e);
        }
        return null;
    }

    /**
     * 只传分贝值
     */
    @RequestMapping(value = "/db", method = RequestMethod.POST)
    @ResponseBody
    public String collectDB(@RequestBody Value value) {

        NoiseMessage record = new NoiseMessage();
        record.setDb(value.getUploadDbValue());
        record.setCollectTime(value.getCollectTime());
        record.setLongitude(value.getLongitude());
        record.setLatitude(value.getLatitude());
        collectDb.create(record, Long.valueOf(value.getUserPhone()));

        return "=====>>>collectDB success: " + record.toString();
    }

    //http://localhost:8080/tools/map?startTime=1543592029999&endTime=1543592050500L&taskId=1
    @RequestMapping(value = "/map", method = RequestMethod.GET)
    @ResponseBody
    public String map(HttpServletRequest request) {

        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        response.setContentType("application/json;charset=UTF-8");

        String min = request.getParameter("startTime");
        String max = request.getParameter("endTime");
        String taskId = request.getParameter("taskId");//URL不传参接收为：""，url里“&taskId="都不含会接收到"null"

        try {
            PrintWriter writer = response.getWriter();
            List<NoiseMessage> list = collectDb.formMap(min,max, taskId);
            if (list == null) {
                writer.write("select no results");
            } else {
                String json = objectMapper.writeValueAsString(list);
                writer.write(json);
            }
            writer.flush();
            writer.close();
            return "map";
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
