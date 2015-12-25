make:
	rm -r out
	mkdir out
	javac -d out -sourcepath src src/Main.java
run:
	java -cp out Main
runlocal:
	java -cp out Main localhost
clean:
	rm -r out
	mkdir out

