make:
	rm -r out
	mkdir out
	javac -d out -sourcepath src src/Main.java
run:
	java -cp out Main
runserver:
	java -cp out Main -s
clean:
	rm -r out
	mkdir out

