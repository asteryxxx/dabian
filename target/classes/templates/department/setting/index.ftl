<!DOCTYPE html>
<html>

<head>

    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">


    <title>部门列表</title>
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
                    <h5>部门管理</h5>
                </div>
                <div class="ibox-content">
                    <p>
                          <#if logUserrole.id==1>
                                <button class="btn btn-success " type="button" onclick="add();"><i class="fa fa-plus"></i>&nbsp;添加</button>
                    </#if>
                                <button class="btn btn-info" id="anniu3" name="anniu3" type="button" onclick="exportexcel();"><i class="fa fa-plus"></i>&nbsp;导出</button>
<#if logUserrole.id==1>   <input id="files" name="mutipartfile" multiple type="file"></#if>
                    <hr>
                    <div class="row row-lg">
                        <div class="col-sm-12">
                            <div class="form-group"><center>
                                <label class="control-label">部门名称： <input type="text" class="input-small" id="sn-val" placeholder="请输入部门"/></label>&nbsp;&nbsp;&nbsp;&nbsp;
                                <label class="control-label">部门简称： <input type="text" class="input-small" id="jc" placeholder="请输入部门简称"/></label>
                                <button class="btn btn-default" type="button" id="anniu" name="anniu">&nbsp;查询</button>
                            <#--<input id="name" name="name" class="input-small" type="text" data-field="name" >
                        <input id="deptKey" name="deptKey" class="input-small" type="text" data-field="deptKey" >
                        <button class="btn btn-success " type="button" onclick="query();"><i class="fa fa-plus"></i>&nbsp;查询</button>-->
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

    $("#files").fileinput({
        language: 'zh',
        allowedFileExtensions: ['xls','xlsx'],//接收的文件后缀
        enctype:'multipart/form-data',
        uploadUrl: "${ctx!}/department/setting/import",
        resizeImage :true,
        maxImageWidth :80,
        showPreview: false,
    }).on("fileuploaded", function(event, data) {
        if (data.response) {
            if(data.response.status==200){
            alert('上传成功');
         }else{
                 alert("上传错误")
    }
        }
        //清空上传文件框并设置可用
        $("#files").fileinput('refresh').fileinput('enable');
    }).on('fileerror', function(event, data, msg) {  //一个文件上传失败
        alert('文件上传失败！');
    }).on('filesuccessremove', function(event, id) {
        alert('文件删除');
    }).on('filedeleteerror', function(event, id) {
        console.log('文件删除错误');
    }).on('filedeleted', function(event, key,data) {
        //删除返回结果
        $("#"+imgId).val(data.responseJSON["imageIdStr"]);
        $('#dg').datagrid('reload');
    });







    $('#anniu2').click(function(){
        var getSelectRows = $("#table_list").bootstrapTable('getSelections', function (row) {
            return row;
        });
        alert("--导入--"+getSelectRows.length);
    });

    $('#anniu3').click(function(){


        $.ajax({
            type: "POST",
            dataType: 'json',
            url: "${ctx!}/department/setting/export",
            data: {ids:selectionIds},
           traditional: true,
            success: function(msg){
                layer.msg(msg.message, {time: 2000},function(){
                    var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
                    parent.layer.close(index);
                    selectionIds = [];
                });
            }
        });

       // alert(selectionIds.length);

    });


    //选中事件操作数组添加
    function isInArray55(array,value){
            return $.inArray(value,array);
    };

    //选中事件操作数组添加
     function isInArray2(array,value){
            if($.inArray(value,array)==-1){
                //array[array.length] = value;
                array.push(value);
            }
            return array;
    };
    //取消选择删除数组里元素
    function deletearray(array,value){
         //   var index = $.inArray(id,array);
        if($.inArray(value,array)==-1){
            return;
        }else {
            var index=$.inArray(value,array);
                array.splice(index, 1);
        }
        return array;
    };



    var selectionIds = [];	//保存选中ids



    $('#anniu').click(function(){
        var deptKey=$('#jc').val();
        var snVal=$('#sn-val').val();
        $("#table_list").bootstrapTable('refresh',{
            url:　"${ctx!}/department/setting/list?name="+encodeURIComponent(snVal)+"&deptKey="+encodeURIComponent(deptKey),
                    silent: true,
                    query: {
                name : snVal,
                        deptKey: deptKey
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
            url: "${ctx!}/department/setting/list",
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
            onClickRow : function(row, tr,flied){
                query(row.id);
            },
            onCheckAll:function(rows){
                for(var i=0;i<rows.length;i++){
                    temparray=isInArray2(selectionIds,rows[i].id);
                    selectionIds=temparray;
                }
            },
            onCheck:function(row){
              //  var zhh=$.inArray(1,selectionIds);
                temparray=isInArray2(selectionIds,row.id);
                selectionIds=temparray;
               // alert("单选-"+row.id);
            },
            onUncheck:function(row){
                tempdeletearray=deletearray(selectionIds,row.id);
                selectionIds=tempdeletearray;
            },
            onUncheckAll:function(rows){
                for(var i=0;i<rows.length;i++){
                    temparray=deletearray(selectionIds,rows[i].id);
                    selectionIds=temparray;
                }
            },
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
                  checkbox: true,
                     visible: true,
                formatter: function (value, row, index) {
                    var zhi=isInArray55(selectionIds,row.id);
                      if (zhi!=-1)
                      return {
                            checked : true//设置选中
                   };
                //  return value;
                  }

                //是否显示复选框
             },{
                title: '序号',
                align: 'center',
                halign: 'center',
                formatter: function (value, row, index) {
                    var options =   $("#table_list").bootstrapTable('getOptions');
                    return options.pageSize * (options.pageNumber - 1) + index + 1;
                }
            },{
                title: "部门名字",
                field: "name"
            }, {
                title: "部门简称",
                field: "deptKey"
            },{
                title: "创建时间",
                field: "createTime",
                sortable: true
            },
                {
                    title: "上级部门",
                    field: "parent",
                    formatter: function(value,row,index){
                        if (value !=null)
                            return '<span class="label label-primary">总经理部</span>';
                        return '<span class="label label-danger">无</span>';
                    }
                },
                {
                title: "操作",
                field: "empty",
                formatter: function (value, row, index) {
<#if logUserrole.id==1>
                    var operateHtml = '<@shiro.hasPermission name="department:setting:edit"><button class="btn btn-primary btn-xs" type="button" onclick="edit(\''+row.id+'\')"><i class="fa fa-edit"></i>&nbsp;编辑</button> &nbsp;</@shiro.hasPermission>';
                    operateHtml = operateHtml + '<@shiro.hasPermission name="department:setting:delete"><button class="btn btn-danger btn-xs" type="button" onclick="del(\''+row.id+'\')"><i class="fa fa-remove"></i>&nbsp;删除</button> &nbsp;</@shiro.hasPermission>';

                    return operateHtml;
</#if>
                    //通过formatter可以自定义列显示的内容
                    //value：当前field的值，即id
                    //row：当前行的数据
                }
            }],

        });


    });

    function query(id){
        layer.open({
            type: 2,
            title: '岗位查看',
            shadeClose: true,
            shade: false,
            area: ['803px', '500px'],
            content: '${ctx!}/post/setting/get/' + id,
            end: function(index){
                $('#table_list').bootstrapTable("refresh");
            }
        });
    }

    function edit(id){
        layer.open({
            type: 2,
            title: '部门修改',
            shadeClose: true,
            shade: false,
            area: ['853px', '600px'],
            content: '${ctx!}/department/setting/edit/' + id,
            end: function(index){
                $('#table_list').bootstrapTable("refresh");
            }
        });
    }
    function add(){
        layer.open({
            type: 2,
            title: '部门添加',
            shadeClose: true,
            shade: false,
            area: ['853px', '600px'],
            content: '${ctx!}/department/setting/add',
            end: function(index){
                $('#table_list').bootstrapTable("refresh");
            }
        });
    }

    function del(id){
        layer.confirm('确定删除吗?', {icon: 3, title:'提示'}, function(index){
            $.ajax({
                type: "POST",
                dataType: "json",
                url: "${ctx!}/department/setting/delete/" + id,
                success: function(msg){
                    layer.msg(msg.message, {time: 2000},function(){
                        $('#table_list').bootstrapTable("refresh");
                        layer.close(index);
                    });
                }
            });
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






