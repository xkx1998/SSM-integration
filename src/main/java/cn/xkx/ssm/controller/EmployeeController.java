package cn.xkx.ssm.controller;

import cn.xkx.ssm.pojo.Employee;
import cn.xkx.ssm.pojo.Message;
import cn.xkx.ssm.service.EmployeeService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @ResponseBody
    @RequestMapping(value = "/emp/{id}",method = RequestMethod.DELETE)
    public Message deleteEmpById(@PathVariable("id")Integer id) {
        employeeService.deleteEmp(id);
        return Message.success();
    }

    @RequestMapping(value = "/emp/{id}",method = RequestMethod.GET)
    @ResponseBody
    public Message getEmp(@PathVariable("id")Integer id) {
        Employee employee = employeeService.getEmp(id);
        return Message.success().add("emp",employee);
    }

    /**
     * 查询员工数据(分页查询)
     */

    //@responseBody注解的作用是将controller的方法返回的对象通过适当的转换器转换为指定的格式之后
    // 写入到response对象的body区，通常用来返回JSON数据或者是XML
    //导入jackson包
    @RequestMapping("/empList")
    @ResponseBody
    public Message getEmpsWithJson(@RequestParam(value = "pn", defaultValue = "1") Integer pn, Model model) {
        //引入PageHelper分页插件
        //在查询之前调用,传入页码和页的大小
        PageHelper.startPage(pn, 5);
        //后面紧跟查询是一个分页查询
        List<Employee> emps = employeeService.getAll();
        //使用pageInfo包装查询后的结果，只需要将pageInfo交给页面
        //封装分页信息,传入连续显示的页数
        PageInfo pageInfo = new PageInfo(emps, 5);
        return Message.success().add("pageInfo", pageInfo);
    }

    //保存员工信息

    @RequestMapping(value = "emp", method = RequestMethod.POST)
    @ResponseBody
    public Message saveEmp(@Validated Employee employee, BindingResult result) {
        if (result.hasErrors()) {
            //校验失败，应该返回失败，在模态框中显示校验失败的错误信息
            Map<String,Object> map = new HashMap<>();
            List<FieldError> errors = result.getFieldErrors();
            for (FieldError error : errors) {
                //getField是字段名,getDefaultMessage错误信息
                map.put(error.getField(),error.getDefaultMessage());
            }
            return Message.fail().add("errorFields",map);
        } else {
            employeeService.saveEmp(employee);
            return Message.success();
        }
    }
/**
 * 如果直接发送ajax=PUT形式的请求
 * 封装的数据
 * 除路径上的empId其他的全为Null
 * 问题:
 * 请求体中有数据:
 * 但是Employee对象封装不上
 *  update tbl_emp    where emp_id = 1014
 *
 *  原因:Tomcat:
 *  1.将请求体中的数据,封装一个map.
 *  2.request.getparameter("empName")就会从这个map中取值
 *  3.SpringMVC封装POJO对象的时候
 *  	会把POJO中中每个属性的值调用request.getparameter("email);
 *  AJAX发送PUT请求引发的血案
 *  PUT请求，请求体中的数据,request.getparameter("email),拿不到数据
 *  Tomcat一看是PUT请求不会封装请求体中的数据为map,只有POST形式的请求才封装请求体为map
 *  org.apache.catalina.connector.Request ;
 *  protected String parseBodyMethods = "POST"
 *  if(!getConnector().isParseBodyMethod(getMethod())){
 *  		success = true;
 *  		return ;
 *  }
 *
 *
 *
 */

    /**解决方案
     * 我们要能支持直接发送PUT之类的请求还要封装请求体中的数据
     * 1.配上HttpPutFormContentFilter
     * 2.他的作用:将请求提中的数据解析包装成一个map. request被重新包装
     * 3.request被重新包装:request.getparameter被()重写,就会从自己封装的map中取数据
     * 员工更新方法
     * @param employee
     * @return
     */
    @ResponseBody
    @RequestMapping(value="/emp/{empId}",method=RequestMethod.PUT)
    public Message saveEmp(Employee employee){
        System.out.println(employee);
        employeeService.updateEmp(employee);
        return Message.success();
    }


    //用户名校验
    @RequestMapping(value = "checkEmpName", method = RequestMethod.POST)
    @ResponseBody
    public Message checkEmpName(@RequestParam("empName") String empName) {
        //先判断用户名是否是合法的表达式
        String regx = "(^[a-zA-Z0-9_-]{6,16}$)|(^[\\u2E80-\\u9FFF]{2,5})";
        if (!empName.matches(regx)) {
            return Message.fail().add("va_msg", "用户名必须是2-5位中文或者6-16位英文和数字的组合");
        }

        boolean b = employeeService.checkEmpName(empName);
        if (b) {
            return Message.success();
        } else {
            return Message.fail().add("va_msg", "用户名不可用");
        }
    }


    //@RequestMapping("/empList")
    public String getEmps(@RequestParam(value = "pn", defaultValue = "1") Integer pn, Model model) {

        //引入PageHelper分页插件
        //在查询之前调用,传入页码和页的大小
        PageHelper.startPage(pn, 5);
        //后面紧跟查询是一个分页查询
        List<Employee> emps = employeeService.getAll();
        //使用pageInfo包装查询后的结果，只需要将pageInfo交给页面
        //封装分页信息,传入连续显示的页数
        PageInfo pageInfo = new PageInfo(emps, 5);

        model.addAttribute("pageInfo", pageInfo);
        return "list";
    }
}
