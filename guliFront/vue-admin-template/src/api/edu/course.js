import request from '@/utils/request'
export default {
    //添加课程信息
    addCourseInfo(courseInfo) {
        return request({
            //url: '/eduservice/teacher/pageTeacherCondition/'+current+"/"+limit,
            url: `/eduservice/educourse/addCourseInfo`,
            method: 'post',
            data: courseInfo
          })
    },

    //查询所有讲师
    getListTeacher(courseInfo) {
        return request({
            //url: '/eduservice/teacher/pageTeacherCondition/'+current+"/"+limit,
            url: `/eduservice/teacher/findAll`,
            method: 'get',
          })
    },

    //根据课程id查询出课程信息
    getCourseInfo(id) {
        return request({
            //url: '/eduservice/teacher/pageTeacherCondition/'+current+"/"+limit,
            url: '/eduservice/educourse/getCourseInfo/'+id,
            method: 'get',
          })
    },

    //修改课程信息
    updateCourseInfo(courseInfoVo) {
        return request({
            //url: '/eduservice/teacher/pageTeacherCondition/'+current+"/"+limit,
            url: '/eduservice/educourse/updateCourseInfo',
            method: 'post',
            data: courseInfoVo
          })
    },

    //课程信息确认
    //修改课程信息
    getPublishCourseInfo(id) {
        return request({
            //url: '/eduservice/teacher/pageTeacherCondition/'+current+"/"+limit,
            url: '/eduservice/educourse/getPublishCourseInfo/'+id,
            method: 'get',
          })
    },

    //课程的最终发布
    publishCourse(id) {
        return request({
            //url: '/eduservice/teacher/pageTeacherCondition/'+current+"/"+limit,
            url: '/eduservice/educourse/publishCourse/'+id,
            method: 'post',
          })
    },

    //查询所有课程信息
    getListCourse(){
        return request({
            //url: '/eduservice/teacher/pageTeacherCondition/'+current+"/"+limit,
            url: '/eduservice/educourse/getCourseList',
            method: 'get',
          })
    },

    //分页查询所有课程信息
    //多条件组合分页查询
    getCourseListPage(current,limit,courseQuery) {
        return request({
            //url: '/eduservice/teacher/pageTeacherCondition/'+current+"/"+limit,
            url: `/eduservice/educourse/pageCourseCondition/${current}/${limit}`,
            method: 'post',
            //teacherQuery条件对象，后端使用RequestBody获取数据
            //data表示把对象转换json进行传递到接口里面
            data: courseQuery
          })
    },

    //删除课程信息
    deleteCourseById(id) {
        return request({
            url: `/eduservice/educourse/${id}`,
            method: 'delete'
          })
    }
    

}
