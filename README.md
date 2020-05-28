# Distributed Cache
Simple Distributed Cache in pure Java

This is a simple pure Java implementation of a distributed cache. Essentials like HTTP Server, Http Request handler, and in-mem cache are implemented using JDK classes only. For replication, HTTP is used over old school RMI/JGroups based replication. 

#Note
If you face access restrictions in eclipse, like “Access restriction: The type 'HttpServer' is not API (restriction on required library '/Library/Java/JavaVirtualMachines/jdk1.8.0_05.jdk/Contents/Home/jre/lib/rt.jar’)” in eclipse, then change the Access Restriction to “warning” in the quick fix ‘Configure problem severity’ that would pop up.
