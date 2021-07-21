import request from '@/utils/request'
export default {
    //根据课程id获取章节和小节数据列表
    getAllChapterVideo(courseId) {
        return request({
            //url: '/eduservice/teacher/pageTeacherCondition/'+current+"/"+limit,
            url: '/eduservice/educhapter/getChapterVideo/'+courseId,
            method: 'get',
          })
    },

    //添加章节
    addChapter(chapter) {
        return request({
            //url: '/eduservice/teacher/pageTeacherCondition/'+current+"/"+limit,
            url: '/eduservice/educhapter/addChapter',
            method: 'post',
            data: chapter
          })
    },

    //根据id查询章节
    getChapter(chapterId) {
        return request({
            //url: '/eduservice/teacher/pageTeacherCondition/'+current+"/"+limit,
            url: '/eduservice/educhapter/getChapterInfo/'+chapterId,
            method: 'get'
          })
    },

    //修改章节信息
    updateChapter(chapter) {
        return request({
            //url: '/eduservice/teacher/pageTeacherCondition/'+current+"/"+limit,
            url: '/eduservice/educhapter/updateChapter',
            method: 'post',
            data: chapter
          })
    },

    //删除章节信息
    deleteChapter(chapterId) {
        return request({
            //url: '/eduservice/teacher/pageTeacherCondition/'+current+"/"+limit,
            url: '/eduservice/educhapter/deleteChapter/'+chapterId,
            method: 'delete'
          })
    },



}
