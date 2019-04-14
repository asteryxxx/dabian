<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>

    <title> - 表单验证 jQuery Validation</title>
    <meta name="keywords" content="">
    <meta name="description" content="">

    <link rel="shortcut icon" href="favicon.ico">
    <link href="${ctx!}/assets/css/bootstrap.min.css?v=3.3.6" rel="stylesheet">
    <link href="${ctx!}/assets/css/font-awesome.css?v=4.4.0" rel="stylesheet">
    <link href="${ctx!}/assets/css/animate.css" rel="stylesheet">
    <link href="${ctx!}/assets/css/style.css?v=4.1.0" rel="stylesheet">
</head>
<body>  <div class="row">
    <div class="col-sm-12">
        <div class="ibox float-e-margins">
            <div class="ibox-title">
            </div>
            <div class="ibox-content"><p id="1p"></p><br/>
                <p id="2p"></p>
            </div>
            <div class="container">
                当前用户登录的位置：<strong>${weizhi}</strong>~~~</br>
              <p align="center"> 该用户所在${weizhi}的三天内天气预报:</p></br>
                <table class="table" style="border:1px solid #DDD;border-collapse: collapse;">
                    <tr >
                        <td colspan="2" width="25%" align="center" style="background-color:#DBE3FA" ><b>日期</b></td>

                        <td align="center" style="background-color:#DBE3FA"><b>天气状况</b></td>
                        <td align="center" style="background-color:#DBE3FA"><b>气温</b></td>
                        <td align="center" style="background-color:#DBE3FA"><b>风力风向</b></td>
                    </tr>

                    <tbody>
                    <#list weatherList as weather>
                    <tr style="border:1px solid #DDD;">
                        <td  align="center">
                            <#if (weather_index<2)>${zuotian}
                            <#elseif (weather_index>1&&weather_index<4)>${jintian}
                            <#else> ${houtian}
                            </#if>
                    </td>
                        <td width="9%" align="center">${weather.mornornight}</td>
                        <td align="center">
                           <b>${weather.name}</b></td>
                        <td style="color:#E54600" align="center"><b>${weather.wendu}</b></td>
                        <td align="center">${weather.fengli}</td>
                    </tr>
                    </#list>

                    </tbody>
                </table>
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


                $.ajax({
                    type: "Get",
                    dataType: "json",
                    url: "${ctx!}/money/reimbursement/welcome",
                    success: function(msg){
                        if(msg.status!=null){
                            $("#1p").html(msg.usermessage);
                            $("#2p").html(msg.taskmessage);

                        }else{
                            $("#1p").html(msg.usermessage);

                        }
                    }
                });

    });
</script>

</body>
</html>