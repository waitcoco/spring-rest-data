FROM maven:3.5.2-jdk-8
RUN cat /usr/share/maven/conf/settings.xml | sed 's~</mirrors>~<mirror><id>alimaven</id><name>aliyunmaven</name><url>http://maven.aliyun.com/nexus/content/groups/public/</url><mirrorOf>central</mirrorOf></mirror></mirrors>~' > temp
RUN mv temp /usr/share/maven/conf/settings.xml