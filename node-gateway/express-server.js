var express = require('express');

var PORT = 1234;

//启动web服务器
var app = express();
app.use(express.static('public'));
app.all('*',function (req,res) {
    // 处理图标请求
    if(req.path == '/favicon.ico'){
        res.end();
        return;
    }
    //获取服务名称
    var serviceName = req.get('Service-Name');
    console.log('serviceName: %s',serviceName);
    if(!serviceName){
        console.log('Service-Name request header is not exist');
        res.end();
        return;
    }
    // TODO

});

app.listen(PORT,function () {
    console.log('server is running at %d ',PORT)
})