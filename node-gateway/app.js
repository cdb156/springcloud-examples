var express = require('express');
var zookeeper = require('node-zookeeper-client');
var httpProxy = require('http-proxy');

var PORT = 1234;

// zk配置
var CONNECTION_STRING = '127.0.0.1:2181';
var REGISTRY_ROOT = '/registry';
var OPTIONS = {
    sessionTimeout: 5000,
    retries: 2
};

// 连接ZooKeeper
var zk = zookeeper.createClient(CONNECTION_STRING, OPTIONS);
zk.connect();

// 创建代理服务器对象，并监听错误时间
var proxy = httpProxy.createProxyServer();
proxy.on('error',function(err,req,res){
    res.end();// 输出空白响应表数据
})


//启动web服务器
var app = express();
app.use(express.static('public'));

// 拦截所有请求
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

    var servicePath = REGISTRY_ROOT + '/'+serviceName;
    console.log('servicePath: %s',servicePath);

    // 获取服务路径下的地址节点
    zk.getChildren(servicePath,function(err,addressNodes){
        if(err){
            console.log(err.stack);
            res.end();
            return;
        }
        var size = addressNodes.length;
        if(size == 0){
            console.log('address node is not exist');
            res.end();
            return;
        }
        // 生成地址路径,获取实际值
        var addressPath = servicePath + "/";
        if(size == 1){
            addressPath += addressNodes[0];
        }else{
            // 当存在多个是，随机获取一个地址
            addressPath += addressNodes[parseInt(Math.random()*size)]
        }
        console.log('addressPath: %s',addressPath);
        // 获取服务数据
        zk.getData(addressPath,function(err,serviceAddress){
            if(err){
                console.log(err.stack);
                res.end();
                return;
            }
            console.log('serviceAddress: %s',serviceAddress);
            if(!serviceAddress){
                console.log('service address is not exist');
                res.end();
                return;
            }
            // TODO
            // 执行代理
            proxy.web(req,res,{
                target: 'http://'+serviceAddress //目标地址
            });
        });
    });
});

app.listen(PORT,function () {
    console.log('server is running at %d ',PORT)
})