<template> 
  <div class="app-container" v-if="!add">
    <el-card class="operate-container" shadow="never">
      <i class="el-icon-tickets mr"></i>
      <span>数据列表</span>
      <el-button
        class="btn-add"
        @click="addBrand()"
        size="mini">
        添加
      </el-button>
    </el-card>
    <div class="table-container">
      <el-table ref="brandTable"
                :data="tableData"
                style="width: 100%"
                v-loading="listLoading"
                border>
        <el-table-column label="模型ID" width="70" align="center">
          <template slot-scope="scope">{{scope.row.id}}</template>
        </el-table-column>
        <el-table-column label="模型名称" width="200" align="center">
          <template slot-scope="scope">{{scope.row.modelName}}</template>
        </el-table-column>
        <el-table-column label="描述" align="center">
          <template slot-scope="scope">{{scope.row.description}}</template>
        </el-table-column>
        <el-table-column label="模型类型" width="100" align="center">
          <template slot-scope="scope">{{scope.row.modelType}}</template>
        </el-table-column>
        <el-table-column label="模型文件" width="300" align="center">
          <template slot-scope="scope">{{scope.row.fileName}}</template>
        </el-table-column>
        <el-table-column label="创建时间" width="200" align="center">
          <template slot-scope="scope">{{scope.row.createTime | formatDate}}</template>
        </el-table-column>
        <el-table-column label="操作" width="200" align="center">
          <template slot-scope="scope">
            <el-button
              size="mini"
              @click="handleUpdate(scope.$index, scope.row)">编辑
            </el-button>
            <el-button
              size="mini"
              type="danger"
              @click="handleDelete(scope.$index, scope.row)">删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
	
  </div>
  <BrandDetail v-else :is-edit='isEdit' @close ='close' :id='upadteId'></BrandDetail>
</template>

<script>
  import { getModelsList, deleteModel } from '../../api/index';
  import BrandDetail from './detail'
  export default {
	  components:{
		 BrandDetail 
	  },
    data() {
      return {
		  add:false,
        tableData: [],
        listLoading: true,
		isEdit:null,
		upadteId:0
      }
    },

    created() {
        this.getModelsList();
    },

    methods: {
		close(){
			this.add= false
			this.isEdit = null
			this.upadteId = 0
		},
        async getModelsList(){
          this.listLoading = true;
          const data = await getModelsList();
          //console.log('模型列表返回的数据',data);
          this.listLoading = false;
          this.tableData = data.data;
        },

        handleClick(obj){
            console.log(obj);
        },

        addBrand() {
			this.isEdit = false
            // this.$router.push({path: '/addModels'})
			this.add = true
        },

        handleUpdate(index, row) {
            // this.$router.push({path: '/updateModels', query: {id: row.id}})
			this.upadteId = row.id
			this.isEdit = true
			this.add = true
        },

        handleDelete(index, row) {
          this.$confirm('是否要删除该模型', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            deleteModel(row.id).then(response => {
              if(response.error == '00000000'){
                this.$message({
                  message: '删除成功',
                  type: 'success',
                  duration: 1000
                });
                this.getModelsList();
              } else {
                this.$message({
                  message: response.msg,
                  type: 'error',
                  duration: 1000
                });
              }
            });
          });
        }
    },

    filters: {
      formatDate: function (value) {
        let date = new Date(value);
        let y = date.getFullYear();
        let MM = date.getMonth() + 1;
        MM = MM < 10 ? ('0' + MM) : MM;
        let d = date.getDate();
        d = d < 10 ? ('0' + d) : d;
        let h = date.getHours();
        h = h < 10 ? ('0' + h) : h;
        let m = date.getMinutes();
        m = m < 10 ? ('0' + m) : m;
        let s = date.getSeconds();
        s = s < 10 ? ('0' + s) : s;
        return y + '-' + MM + '-' + d + ' ' + h + ':' + m + ':' + s;
      }
    }

  }
</script>
<style>
  .mr {
    margin-right: 6px;
  }
</style>