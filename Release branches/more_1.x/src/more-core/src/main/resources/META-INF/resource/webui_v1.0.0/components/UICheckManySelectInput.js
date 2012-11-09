﻿/* -------------------------------------------------------------------- */
/* UICheckManySelectInput Component */
/* -------------------------------------------------------------------- */
WebUI.Component.$extends("UICheckManySelectInput", "UISelectInput", {
	/** （重写方法）从服务器上载入数据 */
	loadData : function(paramData, funOK, funError) {
		if (WebUI.isNaN(this.getState().get("onLoadDataEL")) == true)
			return;
		var $this = this;
		this.doEvent("OnLoadData", paramData, function(event) {
			// A.成功装载
			if (WebUI.isFun(funOK) == true)
				funOK.call($this, event);
			else {
				var k = $this.keyField();
				var v = $this.varField();
				var arrayData = eval(event.result);
				var jqObject = $($this.getElement());
				var titleFirst = $this.titleFirst();
				var selectValues = $this.getState().get('value');
				try {
					selectValues = selectValues.split(",");
				} catch (e) {
					selectValues = [ selectValues ];
				}
				jqObject.html('');
				for ( var i = 0; i < arrayData.length; i++) {
					var _id = jqObject.attr("id");
					var span = "<a id='" + _id + "_Span' href='javascript:void(0)'>" + arrayData[i][v] + "</a>";
					var input = '<input type="checkbox" forComID="' + $this.componentID + '" name="' + $this.name() + '" value="' + arrayData[i][k] + '" varValue="' + arrayData[i][v] + '"';
					for ( var j = 0; j < selectValues.length; j++)
						if (selectValues[j] == arrayData[i][k])
							input += " checked='checked'";
					input += "/>";
					if (titleFirst == true)
						jqObject.append("<li><label id='" + _id + "_Label'>" + span + input + '</label></li>');
					else
						jqObject.append("<li><label id='" + _id + "_Label'>" + input + span + '</label></li>');
				}
			}
		}, function(event) {
			// B.装载失败
			if (WebUI.isFun(funError) == true)
				funError.call($this, {
					event : event
				});
		});
	},
	/** 根据索引号获取被指定的元素 */
	indexOf : function(index) {
		return $("#" + this.clientID + " label")[index];
	},
	/** （重写方法）获取被选择的值索引（数组结构）。 */
	selectIndexs : function() {
		var dataList = new Array();
		var index = 0;
		$("#" + this.clientID + " input[type=checkbox]").each(function() {
			if (this.checked == true)
				dataList.push(index);
			index++;
		});
		return dataList; // 选中值
	},
	/** （重写方法）获取选中的值 */
	selectValues : function() {
		var $this = this;
		var dataList = new Array();
		$("#" + this.clientID + " input[type=checkbox]").each(function() {
			if (this.checked == true) {
				var atItem = {};
				atItem[$this.keyField()] = $(this).attr('value');
				atItem[$this.varField()] = $(this).attr('varValue');
				dataList.push(atItem);
			}
		});
		return dataList; // 选中值
	},
	/** （重写方法）数据（R） */
	getListData : function() {
		var dataList = new Array();
		$("#" + this.clientID + " input[type=checkbox]").each(function() {
			var atItem = {};
			var jqObject = $(this);
			atItem[jqObject.attr('value')] = jqObject.attr('varValue');
			dataList.push(atItem);
		});
		return dataList;
	},
	/** 构造方法 */
	"<init>" : function() {
		/** 绑定事件 */
		var fun = this.onchange;
		$("#" + this.clientID + " input[type=checkbox]").bind("change", function() {
			var comID = $(this).attr("forComID");
			var $this = WebUI(comID);
			fun.call($this);
		});
		$("#" + this.clientID + " label").bind("click", function() {
			this.click();
		});
		/** titleFirst */
		this.defineProperty("titleFirst", "RW");
		/** value */
		this.defineProperty("value", function() {
			var selectData = new Array();
			$("#" + this.clientID + " input[type=checkbox]").each(function() {
				if (this.checked == true)
					selectData.push(this.value);
			});
			return selectData; // 选中值
		}, function(newValue) {
			var selectData = "";
			if (WebUI.isArray(newValue) == false)
				newValue = [ newValue ];
			$("#" + this.clientID + " input[type=checkbox]").each(function() {
				this.checked = false;
				for ( var v in newValue)
					if (v == this.value) {
						this.checked = true;
						selectData = selectData + "," + this.value;
					}
			});
			if (selectData.length > 0)
				selectData = selectData.substr(1);
			this.getState().set("value", selectData);
		});
	}
});