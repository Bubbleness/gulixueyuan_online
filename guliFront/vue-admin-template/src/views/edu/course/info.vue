<template>

  <div class="app-container">

    <h2 style="text-align: center;">发布新课程</h2>

    <el-steps :active="1" process-status="wait" align-center style="margin-bottom: 40px;">
      <el-step title="填写课程基本信息"/>
      <el-step title="创建课程大纲"/>
      <el-step title="最终发布"/>
    </el-steps>

    <el-form label-width="120px">

        <el-form-item label="课程标题">
            <el-input v-model="courseInfo.title" placeholder=" 示例：机器学习项目课：从基础到搭建项目视频课程。专业名称注意大小写"/>
        </el-form-item>

        <!-- 所属分类 TODO -->
        <!-- 课程分类 -->
        <el-form-item label="课程分类">
            <el-select
                v-model="courseInfo.subjectParentId"
                placeholder="一级分类" @change="subjectLevelOneChanged">
                <el-option
                    v-for="subject in subjectOneList"
                    :key="subject.id"
                    :label="subject.title"
                    :value="subject.id"/>
            </el-select>

            <!-- 二级分类 -->
            <el-select v-model="courseInfo.subjectId" placeholder="二级分类">
            <el-option
                v-for="subject in subjectTwoList"
                :key="subject.id"
                :label="subject.title"
                :value="subject.id"/>
            </el-select>

        </el-form-item>

        <!-- 课程讲师 TODO -->
        <!-- 课程讲师 -->
        <el-form-item label="课程讲师">
            <el-select
                v-model="courseInfo.teacherId"
                placeholder="请选择">
                <el-option
                v-for="teacher in teacherList"
                :key="teacher.id"
                :label="teacher.name"
                :value="teacher.id"/>
            </el-select>
        </el-form-item>

        <el-form-item label="总课时">
            <el-input-number :min="0" v-model="courseInfo.lessonNum" controls-position="right" placeholder="请填写课程的总课时数"/>
        </el-form-item>


        <!-- 课程简介 TODO -->
        <!-- 课程简介-->
        <el-form-item label="课程简介">
            <tinymce :height="300" v-model="courseInfo.description"/>
        </el-form-item>

        <!-- 课程封面 TODO -->
        <!-- 课程封面-->
        <el-form-item label="课程封面">

            <el-upload
                :show-file-list="false"
                :on-success="handleAvatarSuccess"
                :before-upload="beforeAvatarUpload"
                :action="BASE_API+'/eduoss/fileoss/upload'"
                class="avatar-uploader">
                <img :src="courseInfo.cover">
            </el-upload>

        </el-form-item>

        <el-form-item label="课程价格">
            <el-input-number :min="0" v-model="courseInfo.price" controls-position="right" placeholder="免费课程请设置为0元"/> 元
        </el-form-item>

        <el-form-item>
            <el-button :disabled="saveBtnDisabled" type="primary" @click="saveOrUpdate">保存并下一步</el-button>
        </el-form-item>
    </el-form>

    
  </div>
</template>
<script>
import course from '@/api/edu/course'
import subject from '@/api/edu/subject'
import Tinymce from '@/components/Tinymce'
export default {

  components: { Tinymce },

  data() {
    return {
      saveBtnDisabled: false, // 保存按钮是否禁用
      courseInfo: {
            title: '',
            subjectId: '',//二级分类id
            subjectParentId: '',//一级分类id
            teacherId: '',
            lessonNum: 0,
            description: '',
            cover: '/static/02.jpg',
            price: 0,
            
      },

      courseId:'',
      teacherList: [],
      subjectOneList: [],
      subjectTwoList: [],
      BASE_API: process.env.BASE_API // 接口API地址
    }
  },

  created() {

      if(this.$route.params && this.$route.params.id){
            this.courseId = this.$route.params.id
            this.getInfo()
      }
      else{
        //初始化所有讲师
        this.getListTeacher()
        //初始化一级分类
        this.getOneSubject()

      }
    
  },

  watch: {  //监听
    $route(to, from) { //路由变化方式，路由发生变化，方法就会执行
      this.courseInfoVo = {}
    }
  },

  methods: {
    

    //根据id查询课程
    getInfo(){
        course.getCourseInfo(this.courseId)
        .then(response =>{
            this.courseInfo = response.data.courseInfoVo
            subject.getSubgectList()
            .then(response =>{
                //查询所有一级分类和二级分类
                this.subjectOneList = response.data.list
                //遍历找当前一级分类下的二级分类
                for(var i=0;i<this.subjectOneList.length;i++){
                    var oneSubject = this.subjectOneList[i];
                    if(this.courseInfo.subjectParentId == oneSubject.id){
                        this.subjectTwoList = oneSubject.children
                    }
                }
            })
            //初始化所有讲师
            this.getListTeacher()
        })
    },

    //上传成功调用的方法
    handleAvatarSuccess(res,file){

       this.courseInfo.cover = res.data.url
    },


    //上传之前要调用的方法
    beforeAvatarUpload(file){
        const isJPG = file.type === 'image/jpeg'
        const isLt2M = file.size / 1024 / 1024 < 2

        if (!isJPG) {
            this.$message.error('上传头像图片只能是 JPG 格式!')
        }
        if (!isLt2M) {
            this.$message.error('上传头像图片大小不能超过 2MB!')
        }
        return isJPG && isLt2M
    },

    //点击一级分类触发事件  显示二级分类
    //通过加参数  即可得到一级分类的id  框架封装
    subjectLevelOneChanged(value){

        //alert(value);
        for(var i=0;i<this.subjectOneList.length;i++){
            //取每个一级分类
            var oneSubject = this.subjectOneList[i]
            if(oneSubject.id === value){
                this.subjectTwoList = oneSubject.children
                //把二级分类id值清空
                this.courseInfo.subjectId = ''
            }
        }
    },

    //查询所有的一级分类
    getOneSubject(){
        subject.getSubgectList()
        .then(response => {
            this.subjectOneList = response.data.list
        })
    },

    //查询所有讲师
    getListTeacher(){
        course.getListTeacher()
        .then(response => {
            this.teacherList = response.data.items
        })
    },

    //添加课程
    addCourse(){

        course.addCourseInfo(this.courseInfo)
        .then(response => {
            //提示信息
            this.$message({
                type: 'success',
                message: '添加课程信息成功！'
            });
            //跳转下一步
            this.$router.push({ path: '/course/chapter/'+response.data.courseId })
        })
    },

    //修改课程信息
    updateCourse(){
        course.updateCourseInfo(this.courseInfo)
        .then(response => {
            //提示信息
            this.$message({
                type: 'success',
                message: '修改课程信息成功！'
            });
            //跳转下一步
            this.$router.push({ path: '/course/chapter/'+this.courseId })
        })
    },

    saveOrUpdate() {
        
        //判断是添加操作还是更新操作
        if(!this.courseInfo.id)
            //添加
            this.addCourse()
        else
            //修改
            this.updateCourse()
      
    }
  }
}
</script>

<style scoped>
    .tinymce-container {
    line-height: 29px;
    }
</style>