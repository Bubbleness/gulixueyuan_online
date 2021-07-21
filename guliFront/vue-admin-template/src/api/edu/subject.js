import request from '@/utils/request'
export default {
    //查询所有课程分类
    getSubgectList() {
        return request({
            //url: '/eduservice/teacher/pageTeacherCondition/'+current+"/"+limit,
            url: `/eduservice/edusubject/getAllSubject`,
            method: 'get'
          })
    }
}
