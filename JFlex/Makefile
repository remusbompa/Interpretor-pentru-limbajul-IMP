build: Tema.java Expression.java TemaLexer.java TemaException.java
	javac Tema.java Expression.java TemaLexer.java TemaException.java

TemaLexer.java: Tema.jflex
	jflex Tema.jflex

run:
	java Tema input

clean:
	rm *.class
	rm TemaLexer.java
