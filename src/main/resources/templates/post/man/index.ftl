<!DOCTYPE html>
<html>

<head>

    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">


    <title>岗位人员列表</title>
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
                    <h5>岗位人员查看</h5>
                </div>
                <div class="ibox-content">

                    <hr>
                    <div class="row row-lg">
                        <div class="col-sm-12">
                            <div class="form-group"><center>
                                <label class="control-label">岗位名称： <input type="text" class="input-small" id="sn-val" placeholder="请输入岗位"/></label>&nbsp;&nbsp;&nbsp;&nbsp;
                                <label class="control-label">人员名字： <input type="text" class="input-small" id="jc" placeholder="请输入人员名字"/></label>
                                <button class="btn btn-default" type="button" id="anniu" name="anniu">&nbsp;查询</button>

                            </div>
                            <!-- Example Card View -->
                            <div class="example-wrap">
                                <div class="example">
                                    <table id="table_list"></table>
                                </div>
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

<!-- 自定义js -->
<script src="${ctx!}/assets/js/content.js?v=1.0.0"></script>
<!-- 上传文件的js-->
<script src="${ctx!}/bookstrapfile/js/fileinput.js"></script>
<script src="${ctx!}/bookstrapfile/js/locales/zh.js"></script>
<!-- Page-Level Scripts -->
<script>

    var selectionIds = [];	//保存选中ids



    $('#anniu').click(function(){
        var manName=$('#jc').val();
        var postName=$('#sn-val').val();
        $("#table_list").bootstrapTable('refresh',{
            url:　"${ctx!}/post/man/list?postName="+encodeURIComponent(postName)+"&manName="+encodeURIComponent(manName),
                    silent: true,
                    query: {
                        postName : postName,
                        manName: manName
            }
        });
    });


    $(document).ready(function () {
        //初始化表格,动态从服务器加载数据
        $("#table_list").bootstrapTable({
            //使用get请求到服务器获取数据
            method: "POST",
            //工具按钮用哪个容器


            //必须设置，不然request.getParameter获取不到请求参数
            contentType: "application/x-www-form-urlencoded",
            //获取数据的Servlet地址
            url: "${ctx!}/post/man/list",
            //表格显示条纹
            striped: true,
            //启动分页
            pagination: true,
            //每页显示的记录数
            pageSize: 5,
            //当前第几页
            pageNumber: 1,
            //记录数可选列表
            pageList: [5, 10],
            //是否启用查询
            search: false,

            //是否启用详细信息视图
            detailView:false,
            //表示服务端请求
            sidePagination: "server",
            //设置为undefined可以获取pageNumber，pageSize，searchText，sortName，sortOrder
            //设置为limit可以获取limit, offset, search, sort, order
            queryParamsType: "undefined",//设置请求参数格式


            //json数据解析
            responseHandler: function(res) {
                return {
                    "rows": res.content,
                    "total": res.totalElements
                };
            },
            //数据列
            columns: [ {
                title: '序号',
                align: 'center',
                halign: 'center',
                formatter: function (value, row, index) {
                    var options =   $("#table_list").bootstrapTable('getOptions');
                    return options.pageSize * (options.pageNumber - 1) + index + 1;
                }
            },
                {
                title: "岗位名称",
                field: "postman"
            }, {
                title: "人员名称",
                field: "manname"
            },{
                title: "人员ID",
                field: "manId",
            },{
                title: "状态",
                field: "zt",
                formatter: function(value,row,index){
                    if (value == '1')
                        return '<span class="label label-primary">正常</span>';
                    return '<span class="label label-danger">禁用</span>';
                }
            }]

        });

    });

        function grantman(id){
            layer.open({
                type: 2,
                title: '分配岗位人员',
                shadeClose: true,
                shade: false,
                area: ['893px', '600px'],
                content: '${ctx!}/post/man/grant/'+ id,
                end: function(index){
                    $('#table_list').bootstrapTable("refresh");
                }
            });
        }




</script>




</body>

</html>






