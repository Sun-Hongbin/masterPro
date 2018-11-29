package org.mcs.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.mcs.entity.TaskRecord;
import org.mcs.service.TaskService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * created by SunHongbin on 2018/10/29
 */
@Controller
@RequestMapping("/task")
public class TaskController {

    private static ObjectMapper objectMapper = new ObjectMapper();

    @Resource
    private TaskService taskService;

    @RequestMapping(value = "/publish", method = RequestMethod.POST,
            produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String publishTask(@RequestBody TaskRecord record) {
        try {
            TaskRecord taskRecord = new TaskRecord();
            taskRecord.setTaskDescription(record.getTaskDescription());
            taskRecord.setTaskLocation(record.getTaskLocation());
            Date startTimeDate = record.getTaskStartTime();
            Date endTimeDate = record.getTaskEndTime();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String startTime = format.format(startTimeDate);
            String endTime = format.format(endTimeDate);
            taskRecord.setTaskStartTime(startTime);
            taskRecord.setTaskEndTime(endTime);
            taskService.create(taskRecord, record.getUserPhone());
            return "success!!!";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    @ResponseBody
    public boolean deleteTask(@RequestParam(value = "id") Long id) {
        if (taskService.removeById(id) == false) {
            System.out.println("无法删除！");
        }
        return true;
    }

    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    @ResponseBody
    public boolean changeTask(
            @RequestParam(value = "publisher_id", required = false) Long publisherId,
            @RequestParam(value = "task_description", required = false) String taskDescription,
            @RequestParam(value = "task_location", required = false) String taskLocation,
            @RequestParam(value = "task_execute_time", required = false) Date taskExecuteTime
    ) {
        TaskRecord taskRecord = new TaskRecord();
        taskRecord.setPublisherId(publisherId);
        taskRecord.setTaskDescription(taskDescription);
        taskRecord.setTaskLocation(taskLocation);
        if (taskService.changeByIdSelective(taskRecord) == true) {
            System.out.println("成功更新");
        }
        return false;
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public void listAllTask() throws Exception {
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter writer = response.getWriter();//该行打印出json结果
        List<TaskRecord> taskRecords = taskService.getTaskByMultiCondition(null);

        String ljson = objectMapper.writeValueAsString(taskRecords);
        if (taskRecords == null) {
            writer.write("select no results");
        } else {
            writer.write(ljson);
        }
        writer.flush();
        writer.close();
    }

    @RequestMapping(value = "/byTime", method = RequestMethod.GET)
    @ResponseBody
    public void getTaskByTime(
            @RequestParam(value = "min_time") Long minTime,
            @RequestParam(value = "max_time") Long maxTime
    ) {
        taskService.getByTime(minTime, maxTime);
    }

    @RequestMapping(value = "/byLoc", method = RequestMethod.GET)
    @ResponseBody
    public void getByLocation(
            @RequestParam(value = "longitude") Double longitude,
            @RequestParam(value = "latitude") Double latitude
    ) {
        taskService.getByLocation(longitude, latitude);
    }

}

