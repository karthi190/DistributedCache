# Distributed Cache
Distributed Key-Value Cache in pure Java 8

This is a simple pure Java implementation of a distributed cache. Essentials like HTTP Server, Http Request handler, and in-mem cache, JSON generation are implemented using Oracle JDK 8 classes only. For replication, HTTP is used over old school RMI/JGroups based replication. 

**Installation:**

Deployment and running can be done in just two steps. The application by default runs in 8001 port, which can be customized.

1. Configuration - The following property in config.properties file has to list (comma-separated) all the processes/nodes where this application is installed. This is for replication. Since I am using the same machine, I am using localhost with a different port number.

```
dkv.replication.host_list=http://localhost:8002/dkv
```

2. Export as an executable JAR with a name such as dkv.jar (can be done in a single click in an IDE) and Run from any machine!

```
java -jar dkv.jar
```

**Usage:**

1. http://localhost:8001/dkv?key=value : POST - To set new key value
2. http://localhost:8001/dkv?key=value : GET - To get value for the key (Here, 'value' is the actual key')
3. http://localhost:8001/dkv?key=value : PUT - To replicate the key & value (Not for external usage)

**Note**
1. This is not a production ready code and can only be used for educational and experimental purposes.

2. If you face access restrictions in eclipse, like “Access restriction: The type 'HttpServer' is not API (restriction on required library '/Library/Java/JavaVirtualMachines/jdk1.8.0_05.jdk/Contents/Home/jre/lib/rt.jar’)” in eclipse, then change the Access Restriction to “warning” in the quick fix ‘Configure problem severity’ that would pop up.
