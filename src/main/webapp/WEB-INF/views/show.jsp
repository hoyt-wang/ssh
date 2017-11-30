<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title></title>
    <link rel="stylesheet" href="/static/css/bootstrap.min.css">
</head>
<body>
    <div class="container">
        <h3 class="page-header">
            ${product.productName}
                <a href="/product/${product.id}/edit" class="btn btn-info pull-right">编辑</a>
                <a href="javascript:;" id="delete" rel="${product.id}" class="btn btn-danger pull-right">删除</a>
        </h3>
        <ul>
            <li>产地： ${product.place}</li>
            <li>市场价： ${product.marketPrice}</li>
            <li>考拉价:  ${product.price}</li>
            <li>评论数量:  ${product.commentNum}</li>
            <li>商品类型： ${product.productType.typeName}</li>
        </ul>
        <a href="/product">返回列表</a>
    </div>

    <script src="/static/js/jquery.min.js"></script>
    <script>
        $(function () {
            $("#delete").click(function () {
                var id = $(this).attr("rel");
                if(confirm("你确定要删除吗？")) {
                    window.location.href = "/product/" + id + "/delete";
                }
            });
        });
    </script>
</body>
</html>