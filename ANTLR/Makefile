build:
	java -jar antlr-4.7.1-complete.jar Prog.g4 -visitor
	javac -cp ./antlr-4.7.1-complete.jar *.java

run:
	java -cp .:antlr-4.7.1-complete.jar Tema input

clean:
	rm *.class *.interp *.tokens Prog*.java
