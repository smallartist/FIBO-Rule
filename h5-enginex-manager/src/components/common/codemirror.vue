<template>
	<textarea :ref="MYname" class="codesql" :value="value" style="height:200px;width:600px;" :key="keynum"></textarea>
</template>

<script>
	// import "codemirror/theme/ambiance.css";
	import "codemirror/theme/ayu-dark.css";
	import "codemirror/lib/codemirror.css";
	import "codemirror/addon/hint/show-hint.css";

	let CodeMirror = require("codemirror/lib/codemirror");
	require("codemirror/addon/edit/matchbrackets");
	require("codemirror/addon/selection/active-line");
	require("codemirror/mode/javascript/javascript");
	require("codemirror/mode/sql/sql");
	require("codemirror/mode/lua/lua");
	require("codemirror/addon/hint/show-hint");
	require("codemirror/addon/hint/sql-hint");
	require("codemirror/addon/hint/javascript-hint");
	export default {
		name: "codeMirror",
		props: {
			value: {
				type: String,
				default: '123'
			},
			mime: {
				type: String,
				default: 'text/javascript'
			},
			autocomplete: {
				type: Boolean,
				default: true
			},
			MYname: {
				type: String,
				default: 'mycode'
			}
		},
		data() {
			return {
				keynum: 0,
				editor: {}
			}
		},
		created() {
			this.keynum = 1
			this.keynum = 0
			console.log(this.value)
		},
		watch:{
			mime() {
			
				this.editor.setOption("mode", this.mime)
			}
		},
		mounted() {
			// let mime = 'text/x-sql'
			let mime = this.mime
			let theme = 'ayu-dark' //设置主题，不设置的会使用默认主题
			let extraKeys = this.autocomplete ? {
				'Tab': 'autocomplete'
			} : {}

			this.editor = CodeMirror.fromTextArea(this.$refs[this.MYname], {
				mode: mime, //选择对应代码编辑器的语言，我这边选的是数据库，根据个人情况自行设置即可
				indentWithTabs: true,
				smartIndent: true,
				lineNumbers: true,
				matchBrackets: true,
				theme: theme,
				// autofocus: true,
				extraKeys: extraKeys, //自定义快捷键
				hintOptions: { //自定义提示选项
					tables: {
						users: ['name', 'score', 'birthDate'],
						countries: ['name', 'population', 'size']
					}
				}
			})
			this.editor.on('changes', (e,e2) => {
				// console.log( e.display.maxLine.text)
				let str = e.display.view?e.display.view.map(x=>x.line.text).join(''):''
				this.$emit('input', str)
			})
			console.log(this.editor)
			// 
		},
		beforeUnmount() {
			console.log(1)

		}
	}
</script>

<style>
	.codesql {
		font-size: 11pt;
		font-family: Consolas, Menlo, Monaco, Lucida Console, Liberation Mono, DejaVu Sans Mono, Bitstream Vera Sans Mono, Courier New, monospace, serif;
	}
</style>
