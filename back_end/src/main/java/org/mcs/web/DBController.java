package org.mcs.web;

import org.mcs.entity.NoiseMessage;
import org.mcs.entity.Value;
import org.mcs.service.CollectDb;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DecimalFormat;

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
        record.setUploadTime(value.getUploadTime());
        record.setCollectTime(value.getCollectTime());
        DecimalFormat df = new DecimalFormat("0.##########");
        System.out.println("经纬度=============>>>"+df.format(value.getLongtitude())
                + " " + df.format(value.getLatitude()));
        record.setLongtitude(value.getLongtitude());
        record.setLatitude(value.getLatitude());
        collectDb.createDbRecord(record);

        return "=====>>>connection success";
    }


    //http://localhost:8080/tools/map
    @RequestMapping(value = "/map", method = RequestMethod.GET)
    @ResponseBody
    public String map() {
        return "map";
    }


}
