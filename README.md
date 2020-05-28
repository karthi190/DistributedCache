# Distributed Cache
Simple Distributed Key Value Cache in pure Java

This is a simple pure Java implementation of a distributed cache. Essentials like HTTP Server, Http Request handler, and in-mem cache are implemented using JDK classes only. For replication, HTTP is used over old school RMI/JGroups based replication. 

**Installation:**

The config.properties files should mention the deployed host and port. Along with that list of hosts need to be specified for multiple deployments of this cache to enable replication. This is a Master to Master replication

**Usage:**
1. http://localhost:8001/dkv?key=value : POST - To set new key value
2. http://localhost:8001/dkv?key=value : GET - To get value for the key
3. http://localhost:8001/dkv?key=value : PUT - To replicate the key & value (Not for external usage)

**Note**

If you face access restrictions in eclipse, like “Access restriction: The type 'HttpServer' is not API (restriction on required library '/Library/Java/JavaVirtualMachines/jdk1.8.0_05.jdk/Contents/Home/jre/lib/rt.jar’)” in eclipse, then change the Access Restriction to “warning” in the quick fix ‘Configure problem severity’ that would pop up.
