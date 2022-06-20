<template> 
  <div>
    <el-upload
	:headers="header"
      class="upload-demo"
      action="Riskmanage/models/uploadAndParseFile"
      :on-remove="handleRemove"
      :before-remove="beforeRemove"
      :before-upload="beforeUpload"
      :on-success="handleUploadSuccess"
      :file-list="fileList">
      <el-button size="small" type="primary">点击上传</el-button>
      <div slot="tip" class="el-upload__tip">只能上传pmml模型文件，且不超过10MB</div>
    </el-upload>
  </div>
</template>
<script>
  export default {
    name: 'singleUpload',
    props: {
      fileList: Array
    },
    data() {
      return {
		  header:{
			  token:localStorage.getItem('token')
		  }
      };
    },
    created() {
      
    },
    methods: {
      handleRemove(file, fileList) {
        console.log(file, fileList);
      },
      beforeRemove(file, fileList) {
        return this.$confirm(`确定移除 ${ file.name }？`);
      },
      beforeUpload(file) {
        var fileType = file.name.substring(file.name.lastIndexOf('.') + 1);
        const extension = fileType === 'pmml';
        const isLt10M = file.size / 1024 / 1024 < 10;
        if(!extension) {
            this.$message({
                message: '上传文件只能是pmml格式!',
                type: 'warning'
            });
        }
        
        if(!isLt10M) {
            this.$message({
                message: '上传文件大小不能超过 10MB!',
                type: 'warning'
            });
        }
        return extension && isLt10M;
      },
      handleUploadSuccess(response, file, fileList) {
        console.log(file, fileList);
        this.$emit('getMessage', response.data);
      }
    }
  }
</script>
<style>

</style>


