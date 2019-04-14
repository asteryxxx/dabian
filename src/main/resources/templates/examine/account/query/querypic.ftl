<!DOCTYPE html>
<html>

<head>

    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">


    <title>统计列表</title>
    <meta name="keywords" content="">
    <meta name="description" content="">

    <link rel="shortcut icon" href="favicon.ico">
    <link href="${ctx!}/assets/css/bootstrap.min.css?v=3.3.6" rel="stylesheet">
    <link href="${ctx!}/assets/css/font-awesome.css?v=4.4.0" rel="stylesheet">

    <link href="${ctx!}/bookstrapfile/css/fileinput.css" rel="stylesheet">
    <link href="${ctx!}/assets/css/plugins/bootstrap-table/bootstrap-table.min.css" rel="stylesheet">

    <link href="${ctx!}/assets/css/animate.css" rel="stylesheet">
    <link href="${ctx!}/assets/css/style.css?v=4.1.0" rel="stylesheet">

</head>

<body class="gray-bg">
<div class="wrapper wrapper-content  animated fadeInRight">
    <div class="row">
        <div class="col-sm-12">
            <div class="ibox ">
                <div class="ibox-title">
                    <h5>统计列表</h5>
                </div>
                <div class="ibox-content">
                    <div class="form-group"><center>

                    </div>
                    <hr>
                 <!--   <div class="row row-lg">
                        <div class="col-sm-12">
                            <div class="form-group"><center>
                                <label class="control-label">报销名字： <input type="text" class="input-small" id="mz" placeholder="请输入名字"/></label>&nbsp;&nbsp;&nbsp;&nbsp;
                                <button class="btn btn-default" type="button" id="anniu" name="anniu">&nbsp;查询</button>
                            </div>
                            <hr>
                            <!-- Example Card View
                            <div class="example-wrap">
                                <div class="example"> -->
                    <div id="main" style="width: 600px;height:400px;"></div>
                    <!--      </div>
                     </div>
                     <!-- End Example Card View -->
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- 全局js -->
<script src="${ctx!}/assets/js/jquery.min.js?v=2.1.4"></script>
<script src="${ctx!}/assets/js/bootstrap.min.js?v=3.3.6"></script>

<!-- Bootstrap table -->
<script src="${ctx!}/assets/js/plugins/bootstrap-table/bootstrap-table.min.js"></script>
<script src="${ctx!}/assets/js/plugins/bootstrap-table/bootstrap-table-mobile.min.js"></script>
<script src="${ctx!}/assets/js/plugins/bootstrap-table/locale/bootstrap-table-zh-CN.min.js"></script>

<!-- Peity -->
<script src="${ctx!}/assets/js/plugins/peity/jquery.peity.min.js"></script>

<script src="${ctx!}/assets/js/plugins/layer/layer.min.js"></script>
<script src="${ctx!}/assets/js/echarts.min.js"></script>
<!-- 自定义js -->
<script src="${ctx!}/assets/js/content.js?v=1.0.0"></script>
<!-- 上传文件的js-->


<!-- Page-Level Scripts -->
<script>

        // 基于准备好的dom，初始化echarts实例
        var myChart = echarts.init(document.getElementById('main'));//main是<div id="main" style="width: 600px;height:400px;"></div>的id

        // 指定图表的配置项和数据
        myChart.setOption({
            color: ['#3398DB'],
            tooltip : {
                trigger: 'axis',
                axisPointer : {            // 坐标轴指示器，坐标轴触发有效
                    type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                }
            },
            title: {
                text: '显示七天内的数据'
            },
            tooltip: {},
            legend: {
                data:['数量']
            },
            xAxis: {
                data: []
            },
            yAxis: {},
            series: [{
                name: '数量',
                type: 'bar',
                data: []
            }]
        });


        $.ajax({
            type : "post",
            async : true,            //异步请求（同步请求将会锁住浏览器，用户其他操作必须等待请求完成才可以执行）
            url : '${ctx!}/money/reimbursement/querypicbydate/7' ,    //请求发送到TestServlet处
            data : {},
            dataType : "json",        //返回数据形式为json
            success : function(result) {

                //请求成功时执行该函数内容，result即为服务器返回的json对象
                var list1=result.nameList;
                var list2=result.numList;

                if (result) {
                    for(var i=0;i<list1.length;i++){
                        names.push(list1[i]);    //挨个取出类别并填入类别数组
                    }
                    for(var i=0;i<list2.length;i++){
                        nums.push(list2[i]);    //挨个取出销量并填入销量数组
                    }
                    myChart.hideLoading();    //隐藏加载动画
                    myChart.setOption({        //加载数据图表
                        xAxis: {
                            data: names
                        },
                        series: [{
                            // 根据名字对应到相应的系列
                            name: '数量',
                            data: nums
                        }]
                    });

                }

                }
        })
        // 使用刚指定的配置项和数据显示图表。
       // myChart.setOption(option);

        myChart.showLoading();    //数据加载完之前先显示一段简单的loading动画

        var names=[];    //类别数组（实际用来盛放X轴坐标值）
        var nums=[];    //销量数组（实际用来盛放Y坐标值）





    function detailFormatter(index, row) {
        var html = [];
        html.push('<p><b>描述:</b> ' + row.description + '</p>');
        return html.join('');
    }
</script>




</body>

</html>






