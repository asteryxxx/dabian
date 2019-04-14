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

</head>

<body class="gray-bg">
    <div class="wrapper wrapper-content animated fadeInRight">
        <div class="row">
            <div class="col-sm-12">
                <div class="ibox float-e-margins">
                    <div class="ibox-title">
                    </div>
                    <div class="ibox-content">
                    </div>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-sm-12">
                <div class="ibox float-e-margins">
                    <div class="ibox-title">
                        <h5>验证表单</h5>
                    </div>
                    <div class="ibox-content">
                        <form class="form-horizontal m-t" id="frm" method="post" action="${ctx!}/money/reimbursement/addform">
                        <input type="hidden" id="userId" name="userId" value="${user.id}">
                            <div class="form-group">
                                <label class="col-sm-3 control-label">费用报销名称：</label>
                                <div class="col-sm-8">
                                    <input id="formname" name="formname" class="form-control" type="text" value="">
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-sm-3 control-label">报销描述：</label>
                                <div class="col-sm-8">
                                    <input id="description" name="description" class="form-control" type="text" value="">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label">申请人：</label>
                                <div class="col-sm-8">
                                    <input id="applyname" name="applyname" class="form-control" type="text" value="${user.userName}" >
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-sm-3 control-label">收款人：</label>
                                <div class="col-sm-8">
                                    <input id="acceptname" name="acceptname" class="form-control" type="text" value="${user.userName}" >
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-sm-3 control-label">金额：</label>
                                <div class="col-sm-8">
                                    <input id="sum" name="sum" class="form-control" type="text" value="">
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-sm-3 control-label">费用类型：</label>
                                <div class="col-sm-8">
                                    <select name="costtype" class="form-control">
                                        <#list costList as cost>
                                        <option value="${cost.id}" >${cost.name}</option>
                                     </#list>
                                    </select>
                                </div>
                            </div>



                            <div class="form-group">
                                <div class="col-sm-8 col-sm-offset-3">
                                    <button class="btn btn-primary" type="submit">提交</button>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>

    </div>


    <!-- 全局js -->
    <script src="${ctx!}/assets/js/jquery.min.js?v=2.1.4"></script>
    <script src="${ctx!}/assets/js/bootstrap.min.js?v=3.3.6"></script>

    <!-- 自定义js -->
    <script src="${ctx!}/assets/js/content.js?v=1.0.0"></script>

    <!-- jQuery Validation plugin javascript-->
    <script src="${ctx!}/assets/js/plugins/validate/jquery.validate.min.js"></script>
    <script src="${ctx!}/assets/js/plugins/validate/messages_zh.min.js"></script>
    <script src="${ctx!}/assets/js/plugins/layer/layer.min.js"></script>
    <script src="${ctx!}/assets/js/plugins/layer/laydate/laydate.js"></script>
    <script type="text/javascript">
    $(document).ready(function () {

	    $("#frm").validate({
    	    rules: {
    	    	sum: {
    	        required: true
    	      },costtype: {
                    required: true
                },formname: {
                    required: true
                },sum: {
                    required: true
                }
    	    },
    	    messages: {},
    	    submitHandler:function(form){
    	    	$.ajax({
   	    		   type: "POST",
   	    		   dataType: "json",
   	    		   url: "${ctx!}/money/reimbursement/addform",
   	    		   data: $(form).serialize(),
   	    		   success: function(msg){
                       layer.msg(msg.message, {time: 2000},function(){
                           $('form')[0].reset();
                       });
   	    		   }
   	    		});
            } 
    	});
    });
    </script>

</body>

</html>
