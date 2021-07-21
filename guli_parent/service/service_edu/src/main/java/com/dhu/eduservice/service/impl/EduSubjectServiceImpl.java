package com.dhu.eduservice.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dhu.eduservice.entity.EduSubject;
import com.dhu.eduservice.entity.excel.SubjectData;
import com.dhu.eduservice.entity.subject.OneSubject;
import com.dhu.eduservice.entity.subject.TwoSubject;
import com.dhu.eduservice.listener.SubjectExcelListener;
import com.dhu.eduservice.mapper.EduSubjectMapper;
import com.dhu.eduservice.service.EduSubjectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author Yushui
 * @since 2021-05-17
 */
@Service
public class EduSubjectServiceImpl extends ServiceImpl<EduSubjectMapper, EduSubject> implements EduSubjectService {

    /**
     * 添加课程分类  通过读取excel生成
     * @param file
     */
    @Override
    public void saveSubject(MultipartFile file,EduSubjectService eduSubjectService) {

        try {
            //文件输入流
            InputStream in = file.getInputStream();
            //调用方法读取
            EasyExcel.read(in, SubjectData.class,new SubjectExcelListener(eduSubjectService)).sheet().doRead();


        }catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     * 课程分类树形列表
     * @return
     */
    @Override
    public List<OneSubject> getAllOneTwoSubject() {

        //1 查询所有一级分类
        QueryWrapper<EduSubject> wrapperOne = new QueryWrapper<>();
        wrapperOne.eq("parent_id","0");
        //ServiceImpl中已经注入了BaseMapper
        List<EduSubject> oneSubjectList = baseMapper.selectList(wrapperOne);
        //也可以使用service本身的查询方法
        //this.list(wrapperOne);

        //2 查询所有二级分类
        QueryWrapper<EduSubject> wrapperTwo = new QueryWrapper<>();
        wrapperTwo.ne("parent_id","0");
        List<EduSubject> twoSubjectList = baseMapper.selectList(wrapperTwo);

        //创建保存最终封装数据的集合
        List<OneSubject> findSubjectList = new ArrayList<>();

        //3 封装所有一级分类
        //遍历查询出的一级分类集合 取数据
        for (EduSubject item : oneSubjectList) {

            OneSubject oneSubject = new OneSubject();
            //复杂写法
            //oneSubject.setId(item.getId());
            //oneSubject.setTitle(item.getTitle());

            //简单写法  使用BeanUtils来取值，并放入新对象
            BeanUtils.copyProperties(item,oneSubject);
            findSubjectList.add(oneSubject);

            //在一级分类的循环中查询所有二级分类
            List<TwoSubject> twoFinalSubjects = new ArrayList<>();

            for(EduSubject elem : twoSubjectList){

                if(elem.getParentId().equals(oneSubject.getId())){
                    TwoSubject twoSubject = new TwoSubject();
                    BeanUtils.copyProperties(elem,twoSubject);
                    twoFinalSubjects.add(twoSubject);
                }
            }

            //4 封装所有二级分类
            oneSubject.setChildren(twoFinalSubjects);
        }

        return findSubjectList;
    }
}
