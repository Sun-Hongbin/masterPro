package org.mcs.web;

import org.mcs.entity.NoiseMessage;
import org.mcs.entity.Value;
import org.mcs.service.CollectDb;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.util.List;

/**
 * created by SunHongbin on 2018/10/20
 */
@Controller
@RequestMapping("/tools")
public class DBController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private CollectDb collectDb;

    @RequestMapping(value = "/db", method = RequestMethod.POST)
    @ResponseBody
    public String collectDB(@RequestBody Value value) {

        NoiseMessage record = new NoiseMessage();
        record.setDb(value.getUploadDbValue());
        record.setCollectTime(value.getCollectTime());
        record.setLongtitude(value.getLongtitude());
        record.setLatitude(value.getLatitude());
        long userPhone = value.getUserPhone();
        collectDb.createDbRecord(record, userPhone);

        return "=====>>> “collectDB” success: "+record.toString();
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
