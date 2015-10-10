make:
	javac -d out -sourcepath src src/Main.java
run:
	java -cp out Main
clean:
	rm *.class

