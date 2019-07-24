var zookeeper = require('node-zookeeper-client');

var CONNECTION_STRING = '127.0.0.1:2181';
var OPTIONS = {
    sessionTimeout: 5000,
    retries: 2
};

var zk = zookeeper.createClient(CONNECTION_STRING, OPTIONS);

zk.on('connected', function () {
    console.log('Connected to the server.');
    console.log(zk);

    console.log('-------------getChildren("/",fun(err,children,stat){})----------------');
    // 获取子节点
    zk.getChildren("/",function (err,children,stat) {
        console.log(children)
    });

    // 获取子节点
    console.log('-------------exists("/",fun(err,stat){})----------------');
    zk.exists("/foo",function (err,stat) {
        if(stat){
            console.log('node exists')
        }else{
            console.log('node does not exists')
        }
    });

    // 创建子节点
    console.log('-------------create("/",fun(err,path){})----------------');
    zk.create("/nodejs",new Buffer('nodejs'),function(error,path) {
            console.log('create:'+path)
    });

    // 修改节点数据
    console.log('-------------setData("/",fun(err,stat){})----------------');
    zk.setData("/nodejs",new Buffer('node-hhh'),function(error,stat) {
        console.log("setData:"+stat)
    });

    // 获取节点数据
    console.log('-------------create("/",fun(err,path){})----------------');
    zk.getData("/nodejs",function(error,data,stat) {
        console.log("getData:"+data.toString())
    });

    // 删除节点数据
    console.log('-------------create("/",fun(err,path){})----------------');
    zk.remove("/nodejs",function(error) {
        if(!error){
            console.log('node is deleted')
        }
    });

    zk.close();
});

zk.connect();