<template>

	<div>
		<el-dialog width="40%" title="选择规则" :visible="visible" @update:visible="$emit('update:visible',$event)"
			@close="close" append-to-body>

			<div style="display: flex;justify-content: flex-end;padding: 10px;">

				<el-select v-if="folderList.length>0" v-model="folderid" placeholder="请选择文件夹" size="mini">
					<el-option v-for="item in folderList" :key="item.id" :label="item.name" :value="item.id">
					</el-option>
				</el-select>

				<el-input v-model="search" placeholder="搜索" size="mini" style="width: 200px;margin: 0 10px;"></el-input>
			</div>

			<el-checkbox-group v-model="tempRule">
				<el-table :data="list" style="width: 100%">

					<el-table-column width="80">
						<template slot-scope="scope">
							<el-checkbox :label="scope.row.id+'/'+scope.row.versionId">{{""}}</el-checkbox>
						</template>
					</el-table-column>

					<el-table-column prop="name" label="名称">
					</el-table-column>
					<el-table-column prop="code" label="code">
					</el-table-column>
					<el-table-column label="版本">
						<template slot-scope="scope">
							<el-select v-model="scope.row.versionId" placeholder="请选择版本" size="mini"
								@change="versionChange(scope.row.id)">
								<el-option v-for="item in scope.row.versionList" :key="item.id"
									:label="item.versionCode" :value="item.id">
								</el-option>
							</el-select>

						</template>
					</el-table-column>
				</el-table>
			</el-checkbox-group>
			<div style="display: flex;justify-content: flex-end;padding: 10px;">
				<el-pagination layout="prev, pager, next" :current-page.sync="page" :total="list.length">
				</el-pagination>
			</div>
			<span slot="footer" class="dialog-footer">
				<el-button @click="close();$emit('update:visible',false)">取 消</el-button>
				<el-button type="primary" @click="$emit('OK',tempRule);close()">确 定</el-button>
			</span>
		</el-dialog>
	</div>



</template>

<script>
	export default {
		props: {
			listOr: {
				type: Array,
				default () {
					return []
				}
			},
			visible: {
				type: Boolean,
				default: false
			},
			tempRuleOr: {
				type: Array,
				default () {
					return []
				}
			},
			folderList: {
				type: Array,
				default () {
					return []
				}
			}

		},
		data() {
			return {
				page: 1,
				tempRule: [],
				search: '',
				folderid: '',
			}
		},
		created() {
			this.tempRule = JSON.parse(JSON.stringify(this.tempRuleOr))
		},
		computed: {

			listOr2() {
				return this.listOr.map(value => {
					this.$set(value, 'versionId', value.versionList[0].id)
					return value
				})
			},
			list() {

				let arr

				if (this.folderid) {
					arr = this.listOr2.filter(value => value.folderId == this.folderid)
				}else{
					arr = this.listOr2
				}

				arr = arr.filter(value => value.name.indexOf(this.search) != -1)
				arr = arr.filter((value, index) => {
					return index >= (this.page - 1) * 10 && index < this.page * 10
				})

				return arr
			}
		},
		methods: {
			close() {
				this.tempRule = []
				this.search = ''
				this.page = 1,
					this.$emit('close')
			},
			versionChange(id) {
				// console.log(this.tempRule)
				this.tempRule = this.tempRule.filter(value => {
					return Number(value.split('/')[0]) != id
				})
			}
		}






	}
</script>

<style>
</style>
