package net.sppan.base.dao;

import com.sun.istack.internal.Nullable;
import net.sppan.base.entity.Resource;
import net.sppan.base.entity.ResourceExt;
import net.sppan.base.entity.Role;
import net.sppan.base.entity.User;
import net.sppan.base.entity.excel.ExportDept;
import net.sppan.base.entity.test.TbDept;
import net.sppan.base.entity.test.TbPost;
import net.sppan.base.entity.test.TbWorkman;
import net.sppan.base.service.IDeptService;
import net.sppan.base.service.IResourceService;
import net.sppan.base.service.specification.SimpleSpecificationBuilder;
import net.sppan.base.service.specification.SpecificationOperator;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.*;

import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.criteria.*;
import javax.servlet.ServletOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TestDao {
    @Autowired
    ResourceMapper resourceMapper;

    @Test
    public void Test(){
        List<ResourceExt> list = resourceMapper.selectList(1);
        for (ResourceExt key : list) {
            System.out.println(key.getText()+","+key.getSourceUrl());
            List<ResourceExt> nodes = key.getNodes();
            for (ResourceExt yy : nodes) {
                System.out.println("-----"+yy.getSourceUrl()+","+yy.getText());
            }
        /*    if(key.getNodes().size()>0){
                List<Resource> tt = key.getNodes();
                for (ResourceExt yy : list) {
                    System.out.println("-----"+yy.getText()+","+yy.getSourceUrl());
                }
            }*/
        }
    }

    public Page<Resource> pageQuery(final Resource model, Pageable pageable) {
        Specification<Resource> example = new Specification<Resource>() {
            @Override
            public Predicate toPredicate(Root<Resource> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                //获取客户端查询条件
                Integer id = model.getId();
                Integer level = model.getLevel();

                //定义集合来确定Predicate[] 的长度，因为CriteriaBuilder的or方法需要传入的是断言数组
                List<Predicate> predicates = new ArrayList<>();

                Predicate predicate = cb.equal(root.get("id").as(Integer.class), id);
                predicates.add(predicate);
                Predicate p1 = cb.equal(root.get("level").as(Integer.class), level);
                predicates.add(p1);
                //判断结合中是否有数据
                if (predicates.size() == 0) {
                    return null;
                }
                //将集合转化为CriteriaBuilder所需要的Predicate[]
                Predicate[] predicateArr = new Predicate[predicates.size()];
                predicateArr = predicates.toArray(predicateArr);

                // 返回所有获取的条件： 条件 or 条件 or 条件 or 条件
                return cb.or(predicateArr);
            }
        };
        //调用Dao方法进行条件查询
        Page<Resource> page = resourceReposity.findAll(example, pageable);
        return page;
    }
    @Autowired
    ResourceReposity resourceReposity;
    @Test
    public void tt(){
        Resource model=new Resource();
        model.setId(1);model.setLevel(2);
        Pageable pageable = new PageRequest(0, 3, Sort.Direction.DESC,"name");
        Page<Resource> list = pageQuery(model, pageable);
        System.out.println(list.getSize());
    }

    @Test
    public void test3_13(){
        /*List<String> sortProperties = new ArrayList<>();
        sortProperties.add("level");
        sortProperties.add("sort");
        Sort sort1 =  new Sort(Sort.Direction.ASC, sortProperties);*/
        List<Resource> list = resourceReposity.findByLevel(0, 2);
        System.out.println(list.size());
    }

    public Page<Resource> findAll(final  List<Integer> idList, Pageable pageable){
        Page<Resource> page = resourceReposity.findAll(new Specification() {
            public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {
                List<Predicate> list = new ArrayList<>();
                Expression<Integer> exp = root.<Integer>get("level");
                list.add(exp.in(idList)); // 往in中添加所有id 实现in 查询
                if (list.size() != 0) {
                    Predicate[] p = new Predicate[list.size()];
                    return cb.and(list.toArray(p));
                } else {
                    return null;
                }
            }
        },new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort()));
        return page;
    }
    @Test
    public void tt2(){
        List<Integer> idList=new ArrayList<>();
        idList.add(1) ;idList.add(2);
        List<String> sortProperties = new ArrayList<>();
        sortProperties.add("level");
        sortProperties.add("sort");
        Sort sort1 =  new Sort(Sort.Direction.ASC, sortProperties);
        Pageable pageable = new PageRequest(0, 10,sort1);
        Page<Resource> list = findAll(idList, pageable);
        System.out.println(list.getContent());
    }
    @Autowired
    IResourceService resourceService;

    @Autowired
    IDeptDao deptDao;

    @Autowired
    IPostDao postDao;
    @Autowired
    IRoleDao roleDao;
    @Autowired
    IWorkmanDao workmanDao;
    @Autowired
    IUserDao userDao;
    @Test
    public  void a3_14(){
        Role adminrole = roleDao.findOne(1);
        Set<TbWorkman> mans = adminrole.getManes();
        String str=null;
        List<TbWorkman> cunzailist=new ArrayList<>();

        for (TbWorkman workman : mans) {
            cunzailist.add(workman);
        }



        List<TbWorkman> workmanList = workmanDao.findAll();
        System.out.println(workmanList.size());

            for (TbWorkman xixi : cunzailist) {
                if (workmanList.contains(xixi)) {
                    workmanList.remove(xixi);
                }

        }
        System.out.println(workmanList.size());
    }

    @Autowired
    IDeptService deptService;
    @Test
    public  void b3_14(){
        List<String> sortProperties = new ArrayList<>();
        sortProperties.add("id");
        Sort sort1 =  new Sort(Sort.Direction.ASC, sortProperties);
        List<TbDept> list = deptService.findList(null,sort1);
        System.out.println(list.size());
    }


    @Test
    public void export()throws  Exception{
        List<TbDept> deptlist = deptService.findAll();
        List<ExportDept> list=new ArrayList<>();
        for(int i=0;i<deptlist.size();i++){
            TbDept dd = deptlist.get(i);
            ExportDept ed=new ExportDept();
            BeanUtils.copyProperties(dd,ed);
            list.add(ed);
        }
        System.out.println(list.size());
      /*  List<String> lietitles=new ArrayList<>();
        lietitles.add("id");lietitles.add("name");lietitles.add("createTime");lietitles.add("deptKey");
        createExcel(list,lietitles,4,"部门信息");*/
    }

   // response.setHeader("Content-disposition","attachment;filename=" +  fileName +";filename*=utf-8''"+ URLEncoder.encode(fileName,"UTF-8"));
   /*public void createExcel(List list, List<String> lietitles, Integer lie, String shouhangbiaoti,ServletOutputStream out) throws Exception {

       //创建工作簿
       XSSFWorkbook wb = new XSSFWorkbook();
       //创建一个sheet
       XSSFSheet sheet = wb.createSheet();
       sheet.setColumnWidth(2, 30 * 256);

       XSSFFont font = wb.createFont();
       font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//粗体显示

       // 创建单元格样式
       XSSFCellStyle style =  wb.createCellStyle();
       style.setFillForegroundColor(IndexedColors.WHITE.getIndex()); //设置要添加表格北京颜色
       style.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
      style.setAlignment(XSSFCellStyle.ALIGN_CENTER); //文字水平居中
      style.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);//文字垂直居中
       style.setAlignment(HorizontalAlignment.CENTER);
       style.setBorderBottom(BorderStyle.THIN); //底边框加黑
       style.setBorderLeft(BorderStyle.THIN);  //左边框加黑
       style.setBorderRight(BorderStyle.THIN); // 有边框加黑
       style.setBorderTop(BorderStyle.THIN); //上边框加黑
       style.setFont(font);
       //为单元格添加背景样式

       for (int i = 0; i <list.size()+2 ; i++) { //需要6行表格
           Row row=null;
           if(i==0||i==1){
                 row =sheet.createRow(i); //创建行
                  row.setHeightInPoints(2*sheet.getDefaultRowHeightInPoints());
           }
                 row =sheet.createRow(i); //创建行
           for (int j = 0; j < lie; j++) {//需要6列
               row.createCell(j).setCellStyle(style);
           }
       }


       //合并单元格 //参数1：行号 参数2：起始列号 参数3：行号 参数4：终止列号
       sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, lie-1));

       //tian入数据
       XSSFRow row = sheet.getRow(0); //获取第一行
       row.getCell(0).setCellValue(shouhangbiaoti); //在第一行中创建一个单元格并赋值
       row.setHeightInPoints(2*sheet.getDefaultRowHeightInPoints());

       XSSFRow row1 = sheet.getRow(1); //获取第二行，为每一列添加字段
        for(int i=0;i<lietitles.size();i++) {
            row1.getCell(i).setCellValue(lietitles.get(i));
        }
       row1.setHeightInPoints(2*sheet.getDefaultRowHeightInPoints());

       XSSFCellStyle neirongStyle = wb.createCellStyle();
       // 竖向居中
       neirongStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER); //文字水平居中
       neirongStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);//文字垂直居中
       neirongStyle.setAlignment(HorizontalAlignment.CENTER);
       neirongStyle.setBorderBottom(BorderStyle.THIN); //底边框加黑
       neirongStyle.setBorderLeft(BorderStyle.THIN);  //左边框加黑
       neirongStyle.setBorderRight(BorderStyle.THIN); // 有边框加黑
       neirongStyle.setBorderTop(BorderStyle.THIN); //上边框加黑
       Date date = new Date();
       //设置要获取到什么样的时间
     //  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
       //获取String类型的时间

       int rowIndex = 2 ;
       for(int j=0;j<list.size();j++) {
           Row row666=sheet.createRow(rowIndex);
           row666.setHeightInPoints(2*sheet.getDefaultRowHeightInPoints());
           for(int i=0;i<lietitles.size();i++) {
               //反射根据对象的属性获取到值
               Cell r = row666.createCell(i);
               r.setCellStyle(neirongStyle);
               Object t = list.get(j);
               String zhi = getValueByPropName(t, lietitles.get(i));
               if(zhi.equals("")){
                   r.setCellValue("");
               }
               r.setCellValue(zhi);
           }
               rowIndex++;
       }
       //将数据写入文件
     //  FileOutputStream out = new FileOutputStream(new File("d://zhh.xlsx"));
       wb.write(out);
       out.flush();
       out.close();
   }

    // 通过属性获取传入对象的指定属性的值
    public  String getValueByPropName(Object t, String propName)  {
        String value = null;
        try {
            // 通过属性获取对象的属性
            Field field = t.getClass().getDeclaredField(propName);

            //判断属性类型是不是时间类型是就转成字符串

            // 对象的属性的访问权限设置为可访问
            field.setAccessible(true);
                // 获取属性的对应的值
            Object tempvalue = field.get(t);

            if (field.getGenericType().toString().equals(
                    "class java.util.Date")){
                System.out.println("是时间类型的---");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date da = (Date) tempvalue;
                // 对象的属性的访问权限设置为可访问
                String sj = sdf.format(da);
                value=sj;
            }else {
                value = tempvalue.toString();
            }
            System.out.println("值="+value);
        } catch (Exception e) {
            return null;
        }

        return value;
    }*/
}
