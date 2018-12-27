package cn.xkx.ssm.controller;

import cn.xkx.ssm.pojo.Department;
import cn.xkx.ssm.pojo.Message;
import cn.xkx.ssm.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 处理和部门有关的请求
 */
@Controller
public class DepartmentController {
    @Autowired
    private DepartmentService departmentService;

    /**
     * 返回所有的部门信息
     */
    @RequestMapping("/depts")
    @ResponseBody
    public Message getDepts() {
        List<Department> list = departmentService.getDepts();
        return Message.success().add("depts",list);
    }
}
