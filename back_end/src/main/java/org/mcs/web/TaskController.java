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
        return null;
    }

    @RequestMapping(value = "delete", method = RequestMethod.DELETE)
    @ResponseBody
    public boolean deleteTask(@RequestParam(value = "id") Long id) {
        return taskService.removeById(id);
    }

    @RequestMapping(value = "update", method = RequestMethod.PUT)
    @ResponseBody
    public boolean changeTask(@RequestBody TaskRecord taskRecord){
        taskService.changeByIdSelective(taskRecord);
        return false;
    }

    @RequestMapping(value = "byTime", method = RequestMethod.GET)
    @ResponseBody
    public void getTaskByTime(
            @RequestParam(value = "min_time") Long minTime,
            @RequestParam(value = "max_time") Long maxTime
            ){
        taskService.getByTime(minTime, maxTime);
    }

    @RequestMapping(value = "byLoc", method = RequestMethod.GET)
    @ResponseBody
    public void getByLocation(
            @RequestParam (value = "longtitude") Double longtitude,
            @RequestParam (value = "latitude") Double latitude
            ){
        taskService.getByLocation(longtitude, latitude);
    }

}

