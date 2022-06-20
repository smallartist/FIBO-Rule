export const GetdeepObj = (obj, length = true ,first=false) => {
	if (typeof obj == 'object' && !Array.isArray(obj)) {
		let arr = []
		for (let key in obj) {
			if (obj.hasOwnProperty(key)) {
				if (Array.isArray(obj[key])) {
					let obj2 = {
						value: key,
						label: key,
					}
					obj2.children = []
					if (length) {
						obj2.children.push({
							value: 'length()',
							label: '长度',
							valueType: 1,
						})
					} 
					if (first&&obj[key][0]) {
						obj2.children.push({
							value: '[]',
							label: '第一项',
							children: GetdeepObj(obj[key][0], length,first)
						})
					} 
					arr.push(obj2)
				} else if (typeof obj[key] == 'object' && obj[key] != null) {
					arr.push({
						value: key,
						label: key,
						children: GetdeepObj(obj[key], length,first)
					})
				} else {
					arr.push({
						value: key,
						label: key,
						valueType: typeof obj[key] == 'string' ? 2 : 1,
					})
				}

			}
		}
		return arr
	} else if (Array.isArray(obj)) {
		let obj = []
		if (length) {
			obj.push({
				value: 'length()',
				label: '长度',
				valueType: 1,
			})
		} 
		if (first&&obj[0]) {
			obj.push({
				value: '[0]',
				label: '第一项',
				children: GetdeepObj(obj[0], length,first)
			})
		} 

		return obj
	}
}


export const deepexamine = (is, arr, index = 1) => {
	arr.forEach(value => {
		if (value.children && value.children.length > 0) {
			deepexamine(is, value.children, index + 1)
		}
		if (value.conditionType == 2) {


			if (!value.fieldId) {
				is.is = true
				is.msg = '请选择 条件 的 入Key'
			}
			if (!value.operator) {
				is.is = true
				is.msg = '请选择 条件 的 运算符'
			}
			if (!value.variableValue || !value.variableValue.trim()) {
				is.is = true
				is.msg = '请选择 条件 的 值'
			}


		}
		if (value.conditionType == 1 && value.children.length == 0 && index != 1) {
			is.is = true
			is.msg = '关系符下不允许为空'
		}


	})


}

export const ruleEnformat = (arr, str) => {

	deepformat(arr)

	function deepformat(arr) {
		arr.forEach(value => {
			if (value.children && value.children.length > 0) {
				deepformat(value.children)
			}

			if (value.conditionType == 2) {
				value.fieldId = getformat(value.fieldId)

				if (value.variableType == 2) {
					value.variableValue = getformat(value.variableValue)
				}

			}


		})
	}


	function getformat(arr) {
		if (str == "String") {
			// debugger
			return arr.join('.')
		} else {
			return arr.split('.')
		}
	}

}
