var http = require('http');
var httpProxy = require('http-proxy');// 记载HTTP Proxy 模块

var PORT = 1234;

var proxy = httpProxy.createProxyServer();

proxy.on('error',function(err,req,res){
    res.end();// 输出空白
})

var app = http.createServer(function (req,res) {
    // 执行代理
    proxy.web(req,res,{
        target: 'http://localhost:8080'
    })
})

app.listen(PORT,function () {
    console.log('server is running at %d', PORT)
})