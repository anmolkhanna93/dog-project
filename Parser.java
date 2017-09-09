package cop5556sp17;

import cop5556sp17.Scanner.Kind;
import static cop5556sp17.Scanner.Kind.*;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cop5556sp17.Scanner.Token;

public class Parser {

	/**
	 * Exception to be thrown if a syntax error is detected in the input.
	 * You will want to provide a useful error message.
	 *
	 */


	@SuppressWarnings("serial")
	public static class SyntaxException extends Exception {
		public SyntaxException(String message) {
			super(message);
		}
	}

	/**
	 * Useful during development to ensure unimplemented routines are
	 * not accidentally called during development.  Delete it when 
	 * the Parser is finished.
	 *
	 */
	@SuppressWarnings("serial")	
	public static class UnimplementedFeatureException extends RuntimeException {
		public UnimplementedFeatureException() {
			super();
		}
	}

	Scanner scanner;
	Token t;

	Parser(Scanner scanner) {
		this.scanner = scanner;
		t = scanner.nextToken();
	}

	/**
	 * parse the input using tokens from the scanner.
	 * Check for EOF (i.e. no trailing junk) when finished
	 * 
	 * @throws SyntaxException
	 */
	void parse() throws SyntaxException {
		program();
		matchEOF();
		return;
	}

	void expression() throws SyntaxException {
		//TODO
		term();
		while(t.kind == Kind.LE || t.kind == Kind.GT ||
				t.kind == Kind.GE || t.kind == Kind.EQUAL || t.kind == Kind.NOTEQUAL ||t.kind ==  Kind.LT){
			consume();
			term();
		}
		//throw new UnimplementedFeatureException();
	}

	void term() throws SyntaxException {
		//TODO
		elem();
		while(t.kind == Kind.PLUS || t.kind == Kind.MINUS || t.kind == Kind.OR){
			consume();
			elem();
		}
		//throw new UnimplementedFeatureException();
	}

	void elem() throws SyntaxException {
		//TODO
		factor();
		while(t.kind == Kind.TIMES || t.kind == Kind.DIV || t.kind == Kind.AND 
				|| t.kind == Kind.MOD){
			consume();
			factor();
		}
		//throw new UnimplementedFeatureException();
	}

	void factor() throws SyntaxException {
		Kind kind = t.kind;
		switch (kind) {
		case IDENT: {
			consume();
		}
		break;
		case INT_LIT: {
			consume();
		}
		break;
		case KW_TRUE:
		case KW_FALSE: {
			consume();
		}
		break;
		case KW_SCREENWIDTH:
		case KW_SCREENHEIGHT: {
			consume();
		}
		break;
		case LPAREN: {
			consume();
			expression();
			match(RPAREN);
		}
		break;
		default:
			//you will want to provide a more useful error message
			throw new SyntaxException("illegal factor");
		}
	}

	void block() throws SyntaxException {
		//TODO
		
		match(LBRACE);//
		while ( t.kind  == Kind.KW_BOOLEAN || t.kind == Kind.KW_INTEGER ||
				t.kind  == Kind.KW_IMAGE || t.kind == Kind.KW_FRAME ||
				t.kind  == Kind.OP_SLEEP || t.kind == Kind.KW_WHILE ||
				t.kind  == Kind.KW_IF || t.kind == Kind.IDENT ||
				t.kind  == Kind.OP_BLUR || t.kind == Kind.OP_GRAY ||
				t.kind  == Kind.OP_CONVOLVE || t.kind == Kind.KW_SHOW ||
				t.kind  == Kind.KW_HIDE || t.kind == Kind.KW_MOVE ||
				t.kind  == Kind.KW_XLOC || t.kind == Kind.KW_YLOC ||
				t.kind  == Kind.OP_WIDTH || t.kind == Kind.OP_HEIGHT ||
				t.kind  == Kind.KW_SCALE ){
			
			
			if(t.kind  == Kind.KW_BOOLEAN){
				dec();
			}else if(t.kind == Kind.KW_INTEGER){
				dec();
			}else if(t.kind  == Kind.KW_IMAGE){
				dec();
			}else if(t.kind == Kind.KW_FRAME){
				dec();
			}else {
				statement();
			}
			/*if (t.kind  == Kind.KW_BOOLEAN || t.kind == Kind.KW_INTEGER ||
					t.kind  == Kind.KW_IMAGE || t.kind == Kind.KW_FRAME)
				dec();*/
			
		}
		match(Kind.RBRACE);
		//throw new UnimplementedFeatureException();
	}

	void program() throws SyntaxException {
		//TODO
		match(IDENT);
		if(t.kind==LBRACE){
			block();
		}else{
			paramDec();
			while(t.kind==Kind.COMMA){
				consume();
				paramDec();
			}
			block();
		}
		matchEOF();
		//throw new UnimplementedFeatureException();
	}

	void paramDec() throws SyntaxException {
		//TODO
		
		if(t.kind==KW_URL){
			consume();
			match(IDENT);//check
		}else if(t.kind==KW_FILE){
			consume();
			match(IDENT);
		}else if(t.kind==KW_INTEGER){
			consume();
			match(IDENT);
		}else if(t.kind==KW_BOOLEAN){
			consume();
			match(IDENT);


		}else{
			throw new SyntaxException("Illegal");
		}

		//throw new UnimplementedFeatureException();
	}

	void dec() throws SyntaxException {
		//TODO
		Kind kind = t.kind;
		if(t.kind==KW_INTEGER){
			consume();
			match(IDENT);
		}else if(t.kind==KW_BOOLEAN){
			consume();
			match(IDENT);
		}else if(t.kind==KW_IMAGE){
			consume();
			match(IDENT);
		}else if(t.kind==KW_FRAME){
			consume();
			match(IDENT);
		}else{
			throw new SyntaxException("illegal symbol");
		}
		//throw new UnimplementedFeatureException();
	}

	void statement() throws SyntaxException {
		//TODO
		if(t.kind == Kind.OP_SLEEP){
			consume();
			expression();
			match(SEMI);
		}
		else if (t.kind == Kind.KW_WHILE || t.kind == Kind.KW_IF){
			consume();
			match(LPAREN);
			expression();
			match(RPAREN);
			block();
		}
		else if (t.kind == Kind.IDENT){
			Token next_token = scanner.peek();
			if (next_token.kind == Kind.ASSIGN){
				consume();
				match(ASSIGN);
				expression();
				match(SEMI);
			} else {
				chain();
				match(SEMI);
			}
		}
		else if (t.kind == Kind.OP_BLUR || t.kind == Kind.OP_GRAY ||
				t.kind == Kind.OP_CONVOLVE || t.kind == Kind.KW_SHOW ||
				t.kind == Kind.KW_HIDE || t.kind == Kind.KW_MOVE ||
				t.kind == Kind.KW_XLOC || t.kind == Kind.KW_YLOC ||
				t.kind == Kind.OP_WIDTH || t.kind == Kind.OP_HEIGHT ||
				t.kind == Kind.KW_SCALE){
		
		//	consume(); check for the error
 			chain();
			match(Kind.SEMI);
		}
		else{

			throw new SyntaxException("Illegal Statement");
		} 


		//throw new UnimplementedFeatureException();
	}

	void chain() throws SyntaxException {
		//TODO
		chainElem();
		if(t.kind == Kind.ARROW || t.kind == Kind.BARARROW){
			consume();
			chainElem();
			while(t.kind == Kind.ARROW || t.kind == Kind.BARARROW){
				consume();
				chainElem();
			}
		}else{
			throw new SyntaxException("illegal chain");
		}
		//throw new UnimplementedFeatureException();
	}

	void chainElem() throws SyntaxException {
		//TODO
		

		if(t.kind==Kind.IDENT){
			consume();
			arg();			
		}else if(t.kind==Kind.OP_BLUR){
			consume();
			arg();
		}else if(t.kind==Kind.OP_GRAY){
			consume();
			arg();
		}else if(t.kind==Kind.OP_CONVOLVE){
			consume();
			arg();
		}else if(t.kind==Kind.KW_SHOW){
			consume();
			arg();
		}else if(t.kind==Kind.KW_HIDE){
			consume();
			arg();
		}else if(t.kind==Kind.KW_MOVE){
			consume();
			arg();
		}else if(t.kind==Kind.KW_XLOC){
			consume();
			arg();
		}else if(t.kind==Kind.KW_YLOC){
			consume();
			arg();
		}else if(t.kind==Kind.OP_HEIGHT){
			consume();
			arg();
		}else if(t.kind==Kind.OP_WIDTH){
			consume();
			arg();
		}else if(t.kind==Kind.KW_SCALE){
			consume();
			arg();

		}
		else{
			throw new SyntaxException("illegal chain element");
		}
		//throw new UnimplementedFeatureException();
	}

	void arg() throws SyntaxException {
		//TODO

		if(t.kind==Kind.LPAREN){//check
			consume();
			expression();
			while(t.kind==Kind.COMMA){
				consume();
				expression();
			}
			match(RPAREN);
		}
		//throw new UnimplementedFeatureException();
	}

	/**
	 * Checks whether the current token is the EOF token. If not, a
	 * SyntaxException is thrown.
	 * 
	 * @return
	 * @throws SyntaxException
	 */
	private Token matchEOF() throws SyntaxException {
		if (t.kind==EOF) {
			//t.isKind(EOF)
			return t;
		}
		throw new SyntaxException("expected EOF");
	}

	/**
	 * Checks if the current token has the given kind. If so, the current token
	 * is consumed and returned. If not, a SyntaxException is thrown.
	 * 
	 * Precondition: kind != EOF
	 * 
	 * @param kind
	 * @return
	 * @throws SyntaxException
	 */
	private Token match(Kind kind) throws SyntaxException {
		if (t.kind==kind) {

			//t.isKind(kind)
			return consume();
		}
		throw new SyntaxException("saw " + t.kind + "expected " + kind);
	}

	/**
	 * Checks if the current token has one of the given kinds. If so, the
	 * current token is consumed and returned. If not, a SyntaxException is
	 * thrown.
	 * 
	 * * Precondition: for all given kinds, kind != EOF
	 * 
	 * @param kinds
	 *            list of kinds, matches any one
	 * @return
	 * @throws SyntaxException
	 */
	private Token match(Kind... kinds) throws SyntaxException {
		// TODO. Optional but handy
		return null; //replace this statement
	}

	/**
	 * Gets the next token and returns the consumed token.
	 * 
	 * Precondition: t.kind != EOF
	 * 
	 * @return
	 * 
	 */
	private Token consume() throws SyntaxException {
		Token tmp = t;
		t = scanner.nextToken();
		return tmp;
	}

}
