import request from '@/utils/request'
export default {

    //添加小节
    addVideo(video) {
        return request({
            //url: '/eduservice/teacher/pageTeacherCondition/'+current+"/"+limit,
            url: '/eduservice/eduvideo/addVideo',
            method: 'post',
            data: video
          })
    },

    //删除章节信息
    deleteVideo(id) {
        return request({
            //url: '/eduservice/teacher/pageTeacherCondition/'+current+"/"+limit,
            url: '/eduservice/eduvideo/deleteVideo/'+id,
            method: 'delete'
          })
    },

    //根据id查询视频
    getVideo(id) {
        return request({
            //url: '/eduservice/teacher/pageTeacherCondition/'+current+"/"+limit,
            url: '/eduservice/eduvideo/getVideo/'+id,
            method: 'get'
          })
    },

    //修改视频信息
    updateVideo(video) {
        return request({
            //url: '/eduservice/teacher/pageTeacherCondition/'+current+"/"+limit,
            url: '/eduservice/eduvideo/updateVideo',
            method: 'post',
            data: video
          })
    },

    //删除阿里云中的视频
    deleteAliyunvod(id){
        return request({
            //url: '/eduservice/teacher/pageTeacherCondition/'+current+"/"+limit,
            url: '/eduvod/video/removeAlyVideo/'+id,
            method: 'delete',
          })
    }


}
