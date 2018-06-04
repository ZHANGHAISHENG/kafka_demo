入门操作示例：
 进入下载的好的kafka目录，例如：I:\kafka\kafka_2.12-1.0.0_1
启动zookepper:   .\bin\windows\zookeeper-server-start.bat  .\config\zookeeper.properties （没有用其他的zookepper）
启动kafka:   .\bin\windows\kafka-server-start.bat .\config\server.properties   
创建topic:    .\bin\windows\kafka-topics.bat --create --zookeeper 127.0.0.1:2181 --replication-factor 3 --partitions 1 --topic my-replicated-topic
获取topic信息:      .\bin\windows\kafka-topics.bat --describe --zookeeper localhost:2181 --topic my-replicated-topic
生产者发送消息:   .\bin\windows\kafka-console-producer.bat --broker-list localhost:9092 --topic my-replicated-topic
消费者订阅消息:   .\bin\windows\kafka-console-consumer.bat --bootstrap-server localhost:9092 --from-beginning --topic my-replicated-topic
-- 如果机子比较慢，可以只是启动一台，并且使用kafka自带的zookeeper(启动zookepper、启动kafka)

demo:
 java ： kafka_demo （git）
  scala流式处理kafka：scala_frame(projectJson) 下的kafka目录 (git)


集群规划：
直接在本机windows，复制三份。
window ip : 192.168.0.104

zookeeper安装
-- 参考hadoop搭建

kafka_2.12配置：
目录：I:\kafka
kafka_2.12-1.0.0_1\config\server.properties:
broker.id=0
listeners=PLAINTEXT://192.168.0.104:9092
log.dirs=I\:\\kafka\\kafka_2.12-1.0.0_1\\tmp\\kafka-logs
zookeeper.connect=127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183

kafka_2.12-1.0.0_2\config\server.properties:
broker.id=1
listeners=PLAINTEXT://192.168.0.104:9093
log.dirs=I\:\\kafka\\kafka_2.12-1.0.0_2\\tmp\\kafka-logs
zookeeper.connect=127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183

kafka_2.12-1.0.0_3\config\server.properties:
broker.id=2
listeners=PLAINTEXT://192.168.0.104:9094
log.dirs=I\:\\kafka\\kafka_2.12-1.0.0_3\\tmp\\kafka-logs
zookeeper.connect=127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183

kafka 监控： kafka-manager-1.3.3.15
 直接下载kafka-manager-1.3.3.15.zip 解压即可

制作window启动脚本：
 rm_all_logs.bat ： 删除所有日志
cd I:\kafka\kafka_2.12-1.0.0_1\tmp
rd /s /Q kafka-logs
md kafka-logs

cd I:\kafka\kafka_2.12-1.0.0_2\tmp
rd /s /Q kafka-logs
md kafka-logs

cd I:\kafka\kafka_2.12-1.0.0_3\tmp
rd /s /Q kafka-logs
md kafka-logs

start_all_server.bat： 启动各个kafka
@echo off
start cmd /k "cd/d I:\kafka\kafka_2.12-1.0.0_1&&.\bin\windows\kafka-server-start.bat .\config\server.properties"
start cmd /k "cd/d I:\kafka\kafka_2.12-1.0.0_2&&.\bin\windows\kafka-server-start.bat .\config\server.properties"
start cmd /k "cd/d I:\kafka\kafka_2.12-1.0.0_3&&.\bin\windows\kafka-server-start.bat .\config\server.properties"

启动：
目录：kafka/配置/
   1：执行 rm_all_logs.bat 
   2： 执行 zkServer1.cmd 、 zkServer2.cmd 、 zkServer3.cmd
   3：执行 start_all_server.bat
    rm_all_logs.bat 删除所有的日志文件，因为windows下kafka存在bug
   4：启动kafka监控：执行 kafka-manager.bat  -Dhttp.port=8080    默认是9000端口


kafka bug:
启动kafka的时候报错：
ERROR Error while deleting the clean shutdown file in dir E:\kafka_2.11-1.0.0\tmp\kafka-logs (kafka.server.LogDirFailureChannel)
java.nio.file.FileSystemException: E:\kafka_2.11-1.0.0\tmp\kafka-logs\__consumer_offsets-9\00000000000000000000.timeindex: 另一个程序正在使用此文件，进程无法访问。
kafka在windows平台就是有这个BUG，没办法。只能手动删除\kafka-logs里的日志文件重启kafka。
参考：http://www.zhuhongliang.com/archives/508

内存不够，设置kafka-server-start.bat：
set KAFKA_HEAP_OPTS=-Xmx256M -Xms128M


发布订阅模式：
消费者使用多个group既可，Zookeerper中保存这每个topic下的每个partition在每个group中消费的offset 
偏移量不提交与提交的区别是：如果不提交偏移量会重启后将从上一次开始的位置获取订阅消息

参考：
英文官网：http://kafka.apache.org/quickstart
中文官网翻译：http://orchome.com/295
Akka Streams Kafka：https://doc.akka.io/docs/akka-stream-kafka/current/home.html

概念入门参考：https://www.cnblogs.com/likehua/p/3999538.html
scala api(官网没有提供scala 操作kafka接口): http://colobu.com/2015/03/13/kafka-example-in-scala/


