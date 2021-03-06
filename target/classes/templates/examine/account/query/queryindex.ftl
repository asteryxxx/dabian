<!DOCTYPE html>
<html>

<head>

    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">


    <title>查看报销的列表</title>
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
                    <h5>报销管理</h5>
                </div>
                <div class="ibox-content">
                    <p>

                                  <button class="btn btn-success " type="button" onclick="querypic();"><i class="fa fa-plus"></i>&nbsp;查看统计图</button>
                    <hr>
                    <div class="row row-lg">
                        <div class="col-sm-12">
                            <div class="form-group"><center>
                                <label class="control-label">报销名字： <input type="text" class="input-small" id="mz" placeholder="请输入名字"/></label>&nbsp;&nbsp;&nbsp;&nbsp;
                                <label class="control-label">报销详情： <input type="text" class="input-small" id="xq" placeholder="请输入详细信息"/></label>&nbsp;&nbsp;&nbsp;&nbsp;

                                <label class="control-label">费用类型：
                                    <select name="costtype" class="input-small" id="lx">
                                        <option value="" selected="selected"></option>
                                		<#list costList as r>
                                            <option value="${r.id}" >${r.name}</option>
                                        </#list>
                                    </select>
                                </label>
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
        var name=$('#mz').val();
        var costtype=$('#lx').val();
        var description=$('#xq').val();

        alert(name);
        $("#table_list").bootstrapTable('refresh',{
            url:　"${ctx!}/money/reimbursement/query/list?formname="+encodeURIComponent(name)+"&description="+encodeURIComponent(description)+"&costtype="+encodeURIComponent(costtype),
                    silent: true,
                    query: {
                        formname : name,
                        costtype: costtype,
                        description: description
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
            url: "${ctx!}/money/reimbursement/query/list",
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
            detailFormatter:detailFormatter,
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
            columns: [{
                title: '序号',
                align: 'center',
                halign: 'center',
                formatter: function (value, row, index) {
                    var options =   $("#table_list").bootstrapTable('getOptions');
                    return options.pageSize * (options.pageNumber - 1) + index + 1;
                }
            },{
                title: "报销名字",
                field: "formname"
            }, {
                title: "报销流水号",
                field: "formkey"
            },{
                title: "报销描述",
                field: "description"
            },{
                title: "申请人",
                field: "applyname"
            },{
                title: "金额",
                field: "sum"
            },{
                title: "状态",
                field: "status",
                formatter: function(value,row,index) {
                    if (value == '1')
                        return '<span class="label label-primary">未审核</span>';

                        if (value == '2')
                            return '<span class="label label-danger">驳回</span>';
                        if (value == '3')
                            return '<span class="label label-success">通过</span>';

                }},{
                title: "费用类型",
                field: "costtype",
                formatter: function(value,row,index){
                    if (value == '1'){
                        return '<span class="label label-primary">租金</span>';}
                    if (value == '2'){
                        return '<span class="label label-primary">办公费用</span>';}
                    if (value == '3'){
                        return '<span class="label label-primary">补贴</span>';}
                    if (value == '4'){
                        return '<span class="label label-primary">工资</span>';}
                }
            },

                {
                title: "操作",
                field: "empty",
                formatter: function (value, row, index) {
                    var operateHtml ='';
                    if (row.status == '2'){

                     operateHtml +=  '<@shiro.hasPermission name="money:reimbursement:query"><button class="btn btn-danger btn-xs" type="button" onclick="edit(\''+row.formkey+'\')"><i class="fa fa-remove"></i>&nbsp;修改</button> &nbsp;</@shiro.hasPermission>';
                    }

                    return operateHtml;
                    //通过formatter可以自定义列显示的内容
                    //value：当前field的值，即id
                    //row：当前行的数据
                }
            }],

        });


    });

    function edit(id){
        layer.open({
            type: 2,
            title: '报销修改',
            shadeClose: true,
            shade: false,
            area: ['853px', '600px'],
            content: '${ctx!}/money/reimbursement/editform/' + id,
            end: function(index){
                $("#table_list").bootstrapTable('refresh',{
                    url:　"${ctx!}/money/reimbursement/query/list?formname="+encodeURIComponent(name)+"&description="+encodeURIComponent(description)+"&costtype="+encodeURIComponent(costtype),
                    silent: true,
                    query: {
                        formname : name,
                        costtype: costtype,
                        description: description
                    }
                });
            }
        });
    }
    function querypic(){
        layer.open({
            type: 2,
            title: '统计',
            shadeClose: true,
            shade: false,
            area: ['803px', '650px'],
            content: '${ctx!}/money/reimbursement/querypic',
            end: function(index){
                $('#table_list').bootstrapTable("refresh");
            }
        });
    }


    function detailFormatter(index, row) {
        var html = [];
        html.push('<p><b>描述:</b> ' + row.description + '</p>');
        return html.join('');
    }
</script>




</body>

</html>






