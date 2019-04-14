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

</head>

<body class="gray-bg">
    <div class="wrapper wrapper-content animated fadeInRight">
        <div class="row">
            <div class="col-sm-12">
                <div class="ibox float-e-margins">
                    <div class="ibox-title">
                        <h5>jQuery Validate 简介</h5>
                    </div>
                    <div class="ibox-content">
                        <p>为【${role.name}】分配人员</p>
                    </div>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-sm-12">
                <div class="ibox float-e-margins">
                    <div class="ibox-title">
                        <h5>选择人员</h5>
                    </div>
                    <div class="ibox-content">
                            <#list alllist as workman>

                               <input type="checkbox" value="${workman.id}" checked="true" >${workman.name}
                            </#list>
                    </div>
                </div>
				<div class="col-sm-6 col-sm-offset-6">
					<button class="btn btn-primary" type="button" id="btnSave">提交</button>
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

	<script type = "text/javascript">


	$(document).ready(function() {



		$("#btnSave").click(function (){
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
	}); 
	
	</script>
</body>
</html>
