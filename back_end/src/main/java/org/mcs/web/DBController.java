package org.mcs.web;

import org.apache.commons.io.IOUtils;
import org.mcs.entity.NoiseMessage;
import org.mcs.entity.Value;
import org.mcs.service.NoiseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

/**
 * created by SunHongbin on 2018/10/20
 */
@Controller
@RequestMapping("/tools")
public class DBController{

    private File audioFile;
    private String fileName = "temp1.amr";
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private NoiseService collectDb;

    @RequestMapping(value = "/db", method = RequestMethod.POST)
    @ResponseBody
    public String collectDB(HttpServletRequest request) {

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

    //http://localhost:8080/tools/map
    @RequestMapping(value = "/map", method = RequestMethod.POST)
    @ResponseBody
    public String map(@RequestBody Value value) {
        NoiseMessage record = new NoiseMessage();
        record.setCollectTime(value.getCollectTime());
        record.setUploadTime(value.getUploadTime());
        List<NoiseMessage> list = collectDb.formMap(record);
        for(NoiseMessage noiseMessage:list){
            System.out.println(noiseMessage);
        }
        return "map";
    }
}
