package org.mcs.web;

import org.mcs.entity.TaskRecord;
import org.mcs.service.TaskService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * created by SunHongbin on 2018/10/29
 */
@Controller
@RequestMapping("/task")
public class TaskController {

    @Resource
    private TaskService taskService;

    @RequestMapping(value = "list", method = RequestMethod.POST)
    @ResponseBody
    public String publishTask(HttpServletRequest request) {
        TaskRecord taskRecord = new TaskRecord();
        String userPhone = request.getParameter("userPhone");
        taskRecord.setTaskDescription(request.getParameter("taskDescription"));
        taskRecord.setTaskLocation(request.getParameter("taskLocation"));
        taskRecord.setTaskExecuteTime(Long.valueOf(request.getParameter("taskExecuteTime")));
        taskService.create(taskRecord, Long.valueOf(userPhone));
        return null;
    }

    @RequestMapping(value = "delete", method = RequestMethod.DELETE)
    @ResponseBody
    public boolean deleteTask(@RequestParam(value = "id") Long id) {
        if (taskService.removeById(id) == false) {
            System.out.println("无法删除！");
        }
        return true;
    }

    @RequestMapping(value = "update", method = RequestMethod.PUT)
    @ResponseBody
    public boolean changeTask(
            @RequestParam(value = "publisher_id", required = false) Long publisherId,
            @RequestParam(value = "task_description", required = false) String taskDescription,
            @RequestParam(value = "task_location", required = false) String taskLocation,
            @RequestParam(value = "task_execute_time", required = false) Long taskExecuteTime
    ) {
        TaskRecord taskRecord = new TaskRecord();
        taskRecord.setPublisherId(publisherId);
        taskRecord.setTaskDescription(taskDescription);
        taskRecord.setTaskLocation(taskLocation);
        taskRecord.setTaskExecuteTime(taskExecuteTime);
        if (taskService.changeByIdSelective(taskRecord) == true) {
            System.out.println("成功更新");
        }
        return false;
    }

    @RequestMapping(value = "byTime", method = RequestMethod.GET)
    @ResponseBody
    public void getTaskByTime(
            @RequestParam(value = "min_time") Long minTime,
            @RequestParam(value = "max_time") Long maxTime
    ) {
        taskService.getByTime(minTime, maxTime);
    }

    @RequestMapping(value = "byLoc", method = RequestMethod.GET)
    @ResponseBody
    public void getByLocation(
            @RequestParam(value = "longtitude") Double longtitude,
            @RequestParam(value = "latitude") Double latitude
    ) {
        taskService.getByLocation(longtitude, latitude);
    }

}

