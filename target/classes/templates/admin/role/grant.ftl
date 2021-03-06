<!DOCTYPE html>
<html>

<head>

    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">


    <title> - 表单验证 jQuery Validation</title>
    <meta name="keywords" content="">
    <meta name="description" content="">

    <link rel="shortcut icon" href="favicon.ico">


    <link href="${ctx!}/assets/css/bootstrap.min.css?v=3.3.6" rel="stylesheet">
    <link href="${ctx!}/assets/css/font-awesome.css?v=4.4.0" rel="stylesheet">
    <link href="${ctx!}/assets/css/animate.css" rel="stylesheet">
    <link href="${ctx!}/assets/css/style.css?v=4.1.0" rel="stylesheet">
    <link href="${ctx!}/assets/css/plugins/zTree/zTreeStyle/zTreeStyle.css" rel="stylesheet">
   <!-- <link href="${ctx!}/bookstrapfile/css/bootstrap-multiselect.css" rel="stylesheet"> -->

</head>

<body class="gray-bg">
    <div class="wrapper wrapper-content animated fadeInRight">
        <div class="row">
            <div class="col-sm-12">
                <div class="ibox float-e-margins">

                    <div class="ibox-content">
                        <p>为【${role.name}】<#if zt!=2> <h5>分配资源</h5> <#else >  <h5>分配人员</h5></#if>
                            </p>
                    </div>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-sm-12">
                <div class="ibox float-e-margins">
                    <div class="ibox-title">
<#if zt!=2> <h5>选择资源</h5> <#else >  <h5>选择人员</h5></#if>
                    </div>
                    <div class="ibox-content">
						<#if zt!=2>
                    	<ul id="tree" class="ztree"></ul>
						<#else >
  <fieldset>

      <table class="table table-bordered dchannel-table">
          <tbody>
          <tr class="item-default">
              <td align="right" style="width: 50%;">
                  <select name="from" id="multiselect" class="js-multiselect form-control" size="8" multiple="multiple">

                  </select>
              </td>
              <td style="width: 50px;" valign="middle">
                  <button type="button" id="js_right_All_1" class="btn btn-block"><i class="glyphicon glyphicon-forward"></i></button>
                  <button type="button" id="js_right_Selected_1" class="btn btn-block"><i class="glyphicon glyphicon-chevron-right"></i></button>
                  <button type="button" id="js_left_Selected_1" class="btn btn-block"><i class="glyphicon glyphicon-chevron-left"></i></button>
                  <button type="button" id="js_left_All_1" class="btn btn-block"><i class="glyphicon glyphicon-backward"></i></button>
              </td>

              <td style="width: 50%;">
                  <select name="to" id="js_multiselect_to_1" class="form-control" size="8" multiple="multiple"></select>
              </td>
          </tr>
          </tbody>
      </table>
  </fieldset>
						</#if>
                    </div>
                </div>
				<div class="col-sm-6 col-sm-offset-6">
                    <#if zt!=2>
					<button class="btn btn-primary" type="button" id="btnSavezy">分配资源</button>

                    <#else >

					<button class="btn btn-primary" type="button" id="btnSavery">分配人员</button>

                    </#if>
				</div>
            </div>
        </div>

    </div>


    <!-- 全局js -->

    <script src="${ctx!}/assets/js/jquery.min.js?v=2.1.4"></script>
    <script src="${ctx!}/assets/js/bootstrap.min.js?v=3.3.6"></script>
    
    <script src="${ctx!}/assets/js/content.js?v=1.0.0"></script>
    
    
    <script src="${ctx!}/assets/js/plugins/validate/jquery.validate.min.js"></script>
    <script src="${ctx!}/assets/js/plugins/validate/messages_zh.min.js"></script>
    <script src="${ctx!}/assets/js/plugins/layer/layer.min.js"></script>
    <script src="${ctx!}/assets/js/plugins/layer/laydate/laydate.js"></script> 
    <script src="${ctx!}/assets/js/plugins/zTree/jquery.ztree.all.min.js"></script>

    <script src="${ctx!}/bookstrapfile/css/bootstrap-multiselect.js"></script>

	<script type = "text/javascript">
	var setting = {
		check : {
			enable : true
		},
		data : {
			simpleData : {
				enable : true
			}
		}
	};
  /*  在setting中check下的chkboxType = { "Y" : "s", "N" : "s" };
    Y指的是勾选checkbox的时候对父结点或子结点产生的影响
    N指的是取消checkbox的时候对父结点或子结点产生的影响
    大小写是有区别的。s指子结点，p指父结点*/
	setting.check.chkboxType = {
		"Y" : "ps",
		"N" : "s"
	};

    //选择全部
    $("#js_right_All_1").click(function(){
        $("#multiselect option").clone().appendTo("#js_multiselect_to_1");
        $("#multiselect option").remove();
    });

    $("#js_right_Selected_1").click(function(){
        $("#multiselect option:selected").clone().appendTo("#js_multiselect_to_1");
        $("#multiselect option:selected").remove();
    });



    //移除一项
    $("#js_left_Selected_1").click(function(){
        $("#js_multiselect_to_1 option:selected").clone().appendTo("#multiselect");
        $("#js_multiselect_to_1 option:selected").remove();
    });

    //移除全部
    $("#js_left_All_1").click(function(){
        $("#js_multiselect_to_1 option").clone().appendTo("#multiselect");
        $("#js_multiselect_to_1 option").remove();
    });

	$(document).ready(function() {
        $('.js-multiselect').multiselect({//左侧选择框
            right: '#js_multiselect_to_1',//右侧选择框id
            rightAll: '#js_right_All_1', //全部右移
            rightSelected: '#js_right_Selected_1',//选中右移
            leftSelected: '#js_left_Selected_1',//选中左移
            leftAll: '#js_left_All_1',//全部左移
            keepRenderingSort: true //保存排序
        });

        //加载所有复选框数据
        //动态选中角色，根据角色的对应表
        $.ajax({
            type: "post",
            url: "${ctx!}/admin/role/grant/man/select/"+ ${role.id},
            dataType : "json",
            success:function(result){
                var alist=result.noexistlist;
                var blist=result.existlist;

                for (var i = 0; i < alist.length; i++) {//加载未拥有的角色  这里根据后台传递的对象循环添加
                    $("#multiselect").append("<option value='" + alist[i].wid + "'>" + alist[i].name + "</option>");
                }
                for (var i = 0; i < blist.length; i++) {//加载已拥有的角色
                    $("#js_multiselect_to_1").append("<option value='" +  blist[i].wid + "'>" +  blist[i].name + "</option>");
                }
            }
        });



		$.ajax({
			type : "POST",
			url : "${ctx!}/admin/resource/tree/" + ${role.id},
			dataType : 'json',
			success : function(msg) {
				$.fn.zTree.init($("#tree"), setting, msg);
			}
		});
		
		$("#btnSavezy").click(function (){
			var treeObj = $.fn.zTree.getZTreeObj("tree");
			var nodes = treeObj.getCheckedNodes(true);
			var selectIds="";
			for(var index in nodes){
				var item=nodes[index];
				selectIds+=item.id+",";
			} 
			$.ajax({
				url : "${ctx!}/admin/role/grant/" + ${role.id},
				type : "post",
				dataType : "json",
				data : {"resourceIds":selectIds},
				success : function(msg) {
					layer.msg(msg.message, {time: 2000},function(){
   						var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
						alert(index);
   						parent.layer.close(index); //关闭弹出的子页面窗口
   					});
				},
				error : function(r,s,m) {
				}
			});
		
		});


        $("#btnSavery").click(function (){
            var selectIds="";
            $("#js_multiselect_to_1 option").map(function(){
                var item =$(this).val();
                selectIds+=item+",";

            })
            $.ajax({
                url : "${ctx!}/admin/role/grant/man/" + ${role.id},
                type : "post",
                dataType : "json",
                data : {"workmanIds":selectIds},
                success : function(msg) {
                    layer.msg(msg.message, {time: 2000},function(){
                        var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
                        parent.layer.close(index); //关闭弹出的子页面窗口
                    });
                },
                error : function(r,s,m) {
                }
            });
        });
	}); 
	
	</script>
</body>
</html>
