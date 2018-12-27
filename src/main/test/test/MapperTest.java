package test;


import cn.xkx.ssm.dao.DepartmentMapper;
import cn.xkx.ssm.dao.EmployeeMapper;
import cn.xkx.ssm.pojo.Department;
import cn.xkx.ssm.pojo.Employee;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.UUID;

//@ContextConfiguration指定Spring配置文件的位置
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:config/spring/applicationContext.xml"})
public class MapperTest {
    @Autowired
    private DepartmentMapper departmentMapper;

    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private SqlSession sqlSession;
    /**
     * 测试departmentMapper
     */
    @Test
    public void test()
    {
        //插入
        /*
        departmentMapper.insertSelective(new Department(null,"开发部"));
        departmentMapper.insertSelective(new Department(null,"售后部"));
        departmentMapper.insertSelective(new Department(null,"财务部"));
        */

        //插入员工
       // employeeMapper.insertSelective(new Employee(null,"张三","男","123@qq.com",1));
       // employeeMapper.insertSelective(new Employee(null,"李四","女","12223@qq.com",2));

       /* EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);
        //批量处理
        for (int i=0;i<1000;i++)
        {
            String name = UUID.randomUUID().toString().substring(0,5);
            mapper.insertSelective(new Employee(null,name,"男",name+"123@qq.com",1));
        }*/
        employeeMapper.insertSelective(new Employee(null,"张三","男","123@qq.com",1));
    }
}
