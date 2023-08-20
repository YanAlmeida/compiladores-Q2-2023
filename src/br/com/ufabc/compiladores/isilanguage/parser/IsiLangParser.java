// Generated from IsiLang.g4 by ANTLR 4.7.1
package br.com.ufabc.compiladores.isilanguage.parser;

	import br.com.ufabc.compiladores.isilanguage.datastructures.IsiSymbol;
	import br.com.ufabc.compiladores.isilanguage.datastructures.IsiVariable;
	import br.com.ufabc.compiladores.isilanguage.datastructures.IsiSymbolTable;
	import br.com.ufabc.compiladores.isilanguage.exceptions.IsiSemanticException;
	import br.com.ufabc.compiladores.isilanguage.ast.IsiProgram;
	import br.com.ufabc.compiladores.isilanguage.ast.AbstractCommand;
	import br.com.ufabc.compiladores.isilanguage.ast.CommandLeitura;
	import br.com.ufabc.compiladores.isilanguage.ast.CommandEscrita;
	import br.com.ufabc.compiladores.isilanguage.ast.CommandAtribuicao;
	import br.com.ufabc.compiladores.isilanguage.ast.CommandDecisao;
	import br.com.ufabc.compiladores.isilanguage.ast.CommandRepeticao;
	import java.util.ArrayList;
	import java.util.Stack;

import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class IsiLangParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.7.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		AP=10, FP=11, SC=12, OP=13, ATTR=14, VIR=15, ACH=16, FCH=17, OPREL=18, 
		ID=19, TEXTO=20, NUMBER=21, WS=22;
	public static final int
		RULE_prog = 0, RULE_decl = 1, RULE_declaravar = 2, RULE_tipo = 3, RULE_bloco = 4, 
		RULE_cmd = 5, RULE_cmdleitura = 6, RULE_cmdescrita = 7, RULE_cmdattrib = 8, 
		RULE_comparacao = 9, RULE_cmdselecao = 10, RULE_cmdrepeticao = 11, RULE_exprbase = 12, 
		RULE_exprpos = 13, RULE_expr = 14, RULE_termo = 15;
	public static final String[] ruleNames = {
		"prog", "decl", "declaravar", "tipo", "bloco", "cmd", "cmdleitura", "cmdescrita", 
		"cmdattrib", "comparacao", "cmdselecao", "cmdrepeticao", "exprbase", "exprpos", 
		"expr", "termo"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "'programa'", "'fimprog;'", "'numero'", "'texto'", "'leia'", "'escreva'", 
		"'se'", "'senao'", "'enquanto'", "'('", "')'", "';'", null, "'='", "','", 
		"'{'", "'}'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, null, null, null, null, null, null, null, null, null, "AP", "FP", 
		"SC", "OP", "ATTR", "VIR", "ACH", "FCH", "OPREL", "ID", "TEXTO", "NUMBER", 
		"WS"
	};
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "IsiLang.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }


		private int _tipo;
		private String _varName;
		private String _varValue;
		private IsiSymbolTable symbolTable = new IsiSymbolTable();
		private IsiSymbol symbol;
		private IsiProgram program = new IsiProgram();
		private ArrayList<AbstractCommand> curThread;
		private Stack<ArrayList<AbstractCommand>> stack = new Stack<ArrayList<AbstractCommand>>();
		private String _readID;
		private String _writeID;
		private String _exprID;
		private String _exprContent;
		private String _exprDecision;
		private String _lastTermo;
		private ArrayList<AbstractCommand> listaTrue;
		private ArrayList<AbstractCommand> listaFalse;
		
		public void verificaID(String id){
			if (!symbolTable.exists(id)){
				throw new IsiSemanticException("Variable " + "\'" + id + "\'" + " not declared");
			}
		}

		public void verificaIDInicializado(String id){
			if (!symbolTable.isAttr(id)) {
				throw new IsiSemanticException("Variable " + "\'" + id + "\'" + " not initialized");
			}
		}

		public Integer getTermoType(String termo){
			if(symbolTable.exists(termo)){
				return ((IsiVariable) symbolTable.get(termo)).getType();
			}
			if(termo.indexOf("\"") >= 0){
				return 1;
			}
			return 0;
		}

		public void verificaTermosMesmoTipo(String termo1, String termo2){
			Integer termo1Type = getTermoType(termo1);
			Integer termo2Type = getTermoType(termo2);

			if(termo1Type != termo2Type){
				throw new IsiSemanticException("Incompatible types in expression: " + "\'" + termo1 + "\'" + " and " + "\'" + termo2 + "\'");
			}
		}

		public void verificaTipoIDExpr(String id, String exp){
			IsiVariable var = (IsiVariable)symbolTable.get(id);
			if (var.getType() == 0 & exp.indexOf("\"") >=0) {
				throw new IsiSemanticException("Symbol "+ "\"" + id + "\"" + " is declared as number");
			}
			else if(var.getType() == 1 & exp.indexOf("\"") < 0) {
				throw new IsiSemanticException("Symbol " + "\"" + id + "\"" + " is declared as string");
			}
		}

		public void efetuaVerificacoesIDComparacao(String id){
			verificaID(id);
			verificaIDInicializado(id);
			if(getTermoType(id) != 0){ 
				throw new IsiSemanticException("Variable "+ "\'" + id + "\'" + " of type string used in comparation");
			}
			symbolTable.setRead(id);
		}

		public void efetuaDeclaracaoVariavel(String id) {
			_varName = id;
			_varValue = null;
			symbol = new IsiVariable(_varName, _tipo, _varValue);
			if (!symbolTable.exists(_varName)){
				symbolTable.add(symbol);	
			}
			else{
				throw new IsiSemanticException("Variable "+ "\'" + _varName + "\'" + " already declared");
			}
		}
		
		public void exibeComandos(){
			for (AbstractCommand c: program.getComandos()){
				System.out.println(c);
			}
		}

		public void exibeWarnings() {
			for (String varName: symbolTable.getAllIdStrings()) {
				if(!symbolTable.isRead(varName)){
					System.out.println("[Warning] The value of the variable " + "\'" + varName + "\'" + " is never used");
				}
			}
		}
		
		public void generateCode(){
			program.generateTarget();
		}

	public IsiLangParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class ProgContext extends ParserRuleContext {
		public DeclContext decl() {
			return getRuleContext(DeclContext.class,0);
		}
		public BlocoContext bloco() {
			return getRuleContext(BlocoContext.class,0);
		}
		public ProgContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_prog; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IsiLangListener ) ((IsiLangListener)listener).enterProg(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IsiLangListener ) ((IsiLangListener)listener).exitProg(this);
		}
	}

	public final ProgContext prog() throws RecognitionException {
		ProgContext _localctx = new ProgContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_prog);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(32);
			match(T__0);
			setState(33);
			decl();
			setState(34);
			bloco();
			setState(35);
			match(T__1);
			  program.setVarTable(symbolTable);
			           	  program.setComandos(stack.pop());
			           	 
			           
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DeclContext extends ParserRuleContext {
		public List<DeclaravarContext> declaravar() {
			return getRuleContexts(DeclaravarContext.class);
		}
		public DeclaravarContext declaravar(int i) {
			return getRuleContext(DeclaravarContext.class,i);
		}
		public DeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_decl; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IsiLangListener ) ((IsiLangListener)listener).enterDecl(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IsiLangListener ) ((IsiLangListener)listener).exitDecl(this);
		}
	}

	public final DeclContext decl() throws RecognitionException {
		DeclContext _localctx = new DeclContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_decl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(39); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(38);
				declaravar();
				}
				}
				setState(41); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==T__2 || _la==T__3 );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DeclaravarContext extends ParserRuleContext {
		public TipoContext tipo() {
			return getRuleContext(TipoContext.class,0);
		}
		public List<TerminalNode> ID() { return getTokens(IsiLangParser.ID); }
		public TerminalNode ID(int i) {
			return getToken(IsiLangParser.ID, i);
		}
		public TerminalNode SC() { return getToken(IsiLangParser.SC, 0); }
		public List<TerminalNode> VIR() { return getTokens(IsiLangParser.VIR); }
		public TerminalNode VIR(int i) {
			return getToken(IsiLangParser.VIR, i);
		}
		public DeclaravarContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_declaravar; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IsiLangListener ) ((IsiLangListener)listener).enterDeclaravar(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IsiLangListener ) ((IsiLangListener)listener).exitDeclaravar(this);
		}
	}

	public final DeclaravarContext declaravar() throws RecognitionException {
		DeclaravarContext _localctx = new DeclaravarContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_declaravar);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(43);
			tipo();
			setState(44);
			match(ID);
			 efetuaDeclaracaoVariavel(_input.LT(-1).getText()); 
			setState(51);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==VIR) {
				{
				{
				setState(46);
				match(VIR);
				setState(47);
				match(ID);
				 efetuaDeclaracaoVariavel(_input.LT(-1).getText()); 
				}
				}
				setState(53);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(54);
			match(SC);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TipoContext extends ParserRuleContext {
		public TipoContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_tipo; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IsiLangListener ) ((IsiLangListener)listener).enterTipo(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IsiLangListener ) ((IsiLangListener)listener).exitTipo(this);
		}
	}

	public final TipoContext tipo() throws RecognitionException {
		TipoContext _localctx = new TipoContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_tipo);
		try {
			setState(60);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__2:
				enterOuterAlt(_localctx, 1);
				{
				setState(56);
				match(T__2);
				 _tipo = IsiVariable.NUMBER;  
				}
				break;
			case T__3:
				enterOuterAlt(_localctx, 2);
				{
				setState(58);
				match(T__3);
				 _tipo = IsiVariable.TEXT;  
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class BlocoContext extends ParserRuleContext {
		public List<CmdContext> cmd() {
			return getRuleContexts(CmdContext.class);
		}
		public CmdContext cmd(int i) {
			return getRuleContext(CmdContext.class,i);
		}
		public BlocoContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_bloco; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IsiLangListener ) ((IsiLangListener)listener).enterBloco(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IsiLangListener ) ((IsiLangListener)listener).exitBloco(this);
		}
	}

	public final BlocoContext bloco() throws RecognitionException {
		BlocoContext _localctx = new BlocoContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_bloco);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			 curThread = new ArrayList<AbstractCommand>(); 
				        stack.push(curThread);  
			          
			setState(64); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(63);
				cmd();
				}
				}
				setState(66); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__4) | (1L << T__5) | (1L << T__6) | (1L << T__8) | (1L << ID))) != 0) );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class CmdContext extends ParserRuleContext {
		public CmdleituraContext cmdleitura() {
			return getRuleContext(CmdleituraContext.class,0);
		}
		public CmdescritaContext cmdescrita() {
			return getRuleContext(CmdescritaContext.class,0);
		}
		public CmdattribContext cmdattrib() {
			return getRuleContext(CmdattribContext.class,0);
		}
		public CmdselecaoContext cmdselecao() {
			return getRuleContext(CmdselecaoContext.class,0);
		}
		public CmdrepeticaoContext cmdrepeticao() {
			return getRuleContext(CmdrepeticaoContext.class,0);
		}
		public CmdContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_cmd; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IsiLangListener ) ((IsiLangListener)listener).enterCmd(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IsiLangListener ) ((IsiLangListener)listener).exitCmd(this);
		}
	}

	public final CmdContext cmd() throws RecognitionException {
		CmdContext _localctx = new CmdContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_cmd);
		try {
			setState(73);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__4:
				enterOuterAlt(_localctx, 1);
				{
				setState(68);
				cmdleitura();
				}
				break;
			case T__5:
				enterOuterAlt(_localctx, 2);
				{
				setState(69);
				cmdescrita();
				}
				break;
			case ID:
				enterOuterAlt(_localctx, 3);
				{
				setState(70);
				cmdattrib();
				}
				break;
			case T__6:
				enterOuterAlt(_localctx, 4);
				{
				setState(71);
				cmdselecao();
				}
				break;
			case T__8:
				enterOuterAlt(_localctx, 5);
				{
				setState(72);
				cmdrepeticao();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class CmdleituraContext extends ParserRuleContext {
		public TerminalNode AP() { return getToken(IsiLangParser.AP, 0); }
		public TerminalNode ID() { return getToken(IsiLangParser.ID, 0); }
		public TerminalNode FP() { return getToken(IsiLangParser.FP, 0); }
		public TerminalNode SC() { return getToken(IsiLangParser.SC, 0); }
		public CmdleituraContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_cmdleitura; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IsiLangListener ) ((IsiLangListener)listener).enterCmdleitura(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IsiLangListener ) ((IsiLangListener)listener).exitCmdleitura(this);
		}
	}

	public final CmdleituraContext cmdleitura() throws RecognitionException {
		CmdleituraContext _localctx = new CmdleituraContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_cmdleitura);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(75);
			match(T__4);
			setState(76);
			match(AP);
			setState(77);
			match(ID);
			 verificaID(_input.LT(-1).getText());
			                     	  _readID = _input.LT(-1).getText();
			                        
			setState(79);
			match(FP);
			setState(80);
			match(SC);

			              	IsiVariable var = (IsiVariable)symbolTable.get(_readID);
			              	CommandLeitura cmd = new CommandLeitura(_readID, var);
			              	stack.peek().add(cmd);
							symbolTable.setAttr(_readID);
			              
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class CmdescritaContext extends ParserRuleContext {
		public TerminalNode AP() { return getToken(IsiLangParser.AP, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode FP() { return getToken(IsiLangParser.FP, 0); }
		public TerminalNode SC() { return getToken(IsiLangParser.SC, 0); }
		public CmdescritaContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_cmdescrita; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IsiLangListener ) ((IsiLangListener)listener).enterCmdescrita(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IsiLangListener ) ((IsiLangListener)listener).exitCmdescrita(this);
		}
	}

	public final CmdescritaContext cmdescrita() throws RecognitionException {
		CmdescritaContext _localctx = new CmdescritaContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_cmdescrita);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(83);
			match(T__5);
			setState(84);
			match(AP);
			setState(85);
			expr();
			setState(86);
			match(FP);
			setState(87);
			match(SC);

			               	  CommandEscrita cmd = new CommandEscrita(_exprContent);
			               	  stack.peek().add(cmd);
			               
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class CmdattribContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(IsiLangParser.ID, 0); }
		public TerminalNode ATTR() { return getToken(IsiLangParser.ATTR, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode SC() { return getToken(IsiLangParser.SC, 0); }
		public CmdattribContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_cmdattrib; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IsiLangListener ) ((IsiLangListener)listener).enterCmdattrib(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IsiLangListener ) ((IsiLangListener)listener).exitCmdattrib(this);
		}
	}

	public final CmdattribContext cmdattrib() throws RecognitionException {
		CmdattribContext _localctx = new CmdattribContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_cmdattrib);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(90);
			match(ID);
			 verificaID(_input.LT(-1).getText());
			                    _exprID = _input.LT(-1).getText();
			                   
			setState(92);
			match(ATTR);
			setState(93);
			expr();
			setState(94);
			match(SC);

							 verificaTipoIDExpr(_exprID, _exprContent);
			               	 CommandAtribuicao cmd = new CommandAtribuicao(_exprID, _exprContent);
			               	 stack.peek().add(cmd);
							 symbolTable.setAttr(_exprID);
			               
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ComparacaoContext extends ParserRuleContext {
		public TerminalNode OPREL() { return getToken(IsiLangParser.OPREL, 0); }
		public List<TerminalNode> ID() { return getTokens(IsiLangParser.ID); }
		public TerminalNode ID(int i) {
			return getToken(IsiLangParser.ID, i);
		}
		public List<TerminalNode> NUMBER() { return getTokens(IsiLangParser.NUMBER); }
		public TerminalNode NUMBER(int i) {
			return getToken(IsiLangParser.NUMBER, i);
		}
		public ComparacaoContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_comparacao; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IsiLangListener ) ((IsiLangListener)listener).enterComparacao(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IsiLangListener ) ((IsiLangListener)listener).exitComparacao(this);
		}
	}

	public final ComparacaoContext comparacao() throws RecognitionException {
		ComparacaoContext _localctx = new ComparacaoContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_comparacao);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(100);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case ID:
				{
				setState(97);
				match(ID);
				 efetuaVerificacoesIDComparacao(_input.LT(-1).getText()); 
				}
				break;
			case NUMBER:
				{
				setState(99);
				match(NUMBER);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
				_exprDecision = _input.LT(-1).getText(); 
								_lastTermo = ""; 
			setState(103);
			match(OPREL);
			 _exprDecision += _input.LT(-1).getText(); 
			setState(108);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case ID:
				{
				setState(105);
				match(ID);
				 efetuaVerificacoesIDComparacao(_input.LT(-1).getText()); 
				}
				break;
			case NUMBER:
				{
				setState(107);
				match(NUMBER);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
				_exprDecision += _input.LT(-1).getText(); 
								_lastTermo = ""; 
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class CmdselecaoContext extends ParserRuleContext {
		public TerminalNode AP() { return getToken(IsiLangParser.AP, 0); }
		public ComparacaoContext comparacao() {
			return getRuleContext(ComparacaoContext.class,0);
		}
		public TerminalNode FP() { return getToken(IsiLangParser.FP, 0); }
		public List<TerminalNode> ACH() { return getTokens(IsiLangParser.ACH); }
		public TerminalNode ACH(int i) {
			return getToken(IsiLangParser.ACH, i);
		}
		public List<TerminalNode> FCH() { return getTokens(IsiLangParser.FCH); }
		public TerminalNode FCH(int i) {
			return getToken(IsiLangParser.FCH, i);
		}
		public List<CmdContext> cmd() {
			return getRuleContexts(CmdContext.class);
		}
		public CmdContext cmd(int i) {
			return getRuleContext(CmdContext.class,i);
		}
		public CmdselecaoContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_cmdselecao; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IsiLangListener ) ((IsiLangListener)listener).enterCmdselecao(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IsiLangListener ) ((IsiLangListener)listener).exitCmdselecao(this);
		}
	}

	public final CmdselecaoContext cmdselecao() throws RecognitionException {
		CmdselecaoContext _localctx = new CmdselecaoContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_cmdselecao);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(112);
			match(T__6);
			setState(113);
			match(AP);
			setState(114);
			comparacao();
			setState(115);
			match(FP);
			setState(116);
			match(ACH);
			 curThread = new ArrayList<AbstractCommand>(); 
			                      stack.push(curThread);
			                    
			setState(119); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(118);
				cmd();
				}
				}
				setState(121); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__4) | (1L << T__5) | (1L << T__6) | (1L << T__8) | (1L << ID))) != 0) );
			setState(123);
			match(FCH);

			                       listaTrue = stack.pop();	
			                    
			setState(136);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__7) {
				{
				setState(125);
				match(T__7);
				setState(126);
				match(ACH);

				                   	 	curThread = new ArrayList<AbstractCommand>();
				                   	 	stack.push(curThread);
				                   	 
				{
				setState(129); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(128);
					cmd();
					}
					}
					setState(131); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__4) | (1L << T__5) | (1L << T__6) | (1L << T__8) | (1L << ID))) != 0) );
				}
				setState(133);
				match(FCH);

				                   		listaFalse = stack.pop();
				                   		CommandDecisao cmd = new CommandDecisao(_exprDecision, listaTrue, listaFalse);
				                   		stack.peek().add(cmd);
				                   	
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class CmdrepeticaoContext extends ParserRuleContext {
		public TerminalNode AP() { return getToken(IsiLangParser.AP, 0); }
		public ComparacaoContext comparacao() {
			return getRuleContext(ComparacaoContext.class,0);
		}
		public TerminalNode FP() { return getToken(IsiLangParser.FP, 0); }
		public TerminalNode ACH() { return getToken(IsiLangParser.ACH, 0); }
		public TerminalNode FCH() { return getToken(IsiLangParser.FCH, 0); }
		public List<CmdContext> cmd() {
			return getRuleContexts(CmdContext.class);
		}
		public CmdContext cmd(int i) {
			return getRuleContext(CmdContext.class,i);
		}
		public CmdrepeticaoContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_cmdrepeticao; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IsiLangListener ) ((IsiLangListener)listener).enterCmdrepeticao(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IsiLangListener ) ((IsiLangListener)listener).exitCmdrepeticao(this);
		}
	}

	public final CmdrepeticaoContext cmdrepeticao() throws RecognitionException {
		CmdrepeticaoContext _localctx = new CmdrepeticaoContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_cmdrepeticao);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(138);
			match(T__8);
			setState(139);
			match(AP);
			setState(140);
			comparacao();
			setState(141);
			match(FP);
			setState(142);
			match(ACH);
			 
											curThread = new ArrayList<AbstractCommand>();
											stack.push(curThread);
										
			setState(145); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(144);
				cmd();
				}
				}
				setState(147); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__4) | (1L << T__5) | (1L << T__6) | (1L << T__8) | (1L << ID))) != 0) );
			setState(149);
			match(FCH);

											CommandRepeticao cmd = new CommandRepeticao(_exprDecision, stack.pop());
											stack.peek().add(cmd);
										
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ExprbaseContext extends ParserRuleContext {
		public List<TermoContext> termo() {
			return getRuleContexts(TermoContext.class);
		}
		public TermoContext termo(int i) {
			return getRuleContext(TermoContext.class,i);
		}
		public List<TerminalNode> OP() { return getTokens(IsiLangParser.OP); }
		public TerminalNode OP(int i) {
			return getToken(IsiLangParser.OP, i);
		}
		public ExprbaseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_exprbase; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IsiLangListener ) ((IsiLangListener)listener).enterExprbase(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IsiLangListener ) ((IsiLangListener)listener).exitExprbase(this);
		}
	}

	public final ExprbaseContext exprbase() throws RecognitionException {
		ExprbaseContext _localctx = new ExprbaseContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_exprbase);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(152);
			termo();
			_lastTermo = _input.LT(-1).getText();
			setState(161);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,11,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(154);
					match(OP);
					 _exprContent += _input.LT(-1).getText(); 
					setState(156);
					termo();
					 verificaTermosMesmoTipo(_lastTermo, _input.LT(-1).getText());
									        _lastTermo = _input.LT(-1).getText(); 
					}
					} 
				}
				setState(163);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,11,_ctx);
			}
			_lastTermo = "";
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ExprposContext extends ParserRuleContext {
		public ExprbaseContext exprbase() {
			return getRuleContext(ExprbaseContext.class,0);
		}
		public TerminalNode AP() { return getToken(IsiLangParser.AP, 0); }
		public TerminalNode FP() { return getToken(IsiLangParser.FP, 0); }
		public ExprposContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_exprpos; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IsiLangListener ) ((IsiLangListener)listener).enterExprpos(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IsiLangListener ) ((IsiLangListener)listener).exitExprpos(this);
		}
	}

	public final ExprposContext exprpos() throws RecognitionException {
		ExprposContext _localctx = new ExprposContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_exprpos);
		try {
			setState(173);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case ID:
			case TEXTO:
			case NUMBER:
				enterOuterAlt(_localctx, 1);
				{
				setState(166);
				exprbase();
				}
				break;
			case AP:
				enterOuterAlt(_localctx, 2);
				{
				setState(167);
				match(AP);
				 _exprContent += _input.LT(-1).getText(); 
				setState(169);
				exprbase();
				setState(170);
				match(FP);
				 _exprContent += _input.LT(-1).getText(); 
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ExprContext extends ParserRuleContext {
		public List<ExprposContext> exprpos() {
			return getRuleContexts(ExprposContext.class);
		}
		public ExprposContext exprpos(int i) {
			return getRuleContext(ExprposContext.class,i);
		}
		public List<TerminalNode> OP() { return getTokens(IsiLangParser.OP); }
		public TerminalNode OP(int i) {
			return getToken(IsiLangParser.OP, i);
		}
		public ExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IsiLangListener ) ((IsiLangListener)listener).enterExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IsiLangListener ) ((IsiLangListener)listener).exitExpr(this);
		}
	}

	public final ExprContext expr() throws RecognitionException {
		ExprContext _localctx = new ExprContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_expr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			 _exprContent = ""; 
			setState(176);
			exprpos();
			setState(182);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==OP) {
				{
				{
				setState(177);
				match(OP);
				_exprContent += _input.LT(-1).getText();
				setState(179);
				exprpos();
				}
				}
				setState(184);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TermoContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(IsiLangParser.ID, 0); }
		public TerminalNode NUMBER() { return getToken(IsiLangParser.NUMBER, 0); }
		public TerminalNode TEXTO() { return getToken(IsiLangParser.TEXTO, 0); }
		public TermoContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_termo; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IsiLangListener ) ((IsiLangListener)listener).enterTermo(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IsiLangListener ) ((IsiLangListener)listener).exitTermo(this);
		}
	}

	public final TermoContext termo() throws RecognitionException {
		TermoContext _localctx = new TermoContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_termo);
		int _la;
		try {
			setState(189);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case ID:
				enterOuterAlt(_localctx, 1);
				{
				setState(185);
				match(ID);
				 	verificaID(_input.LT(-1).getText());
									verificaIDInicializado(_input.LT(-1).getText());
									symbolTable.setRead(_input.LT(-1).getText());
					               _exprContent += _input.LT(-1).getText();
				                 
				}
				break;
			case TEXTO:
			case NUMBER:
				enterOuterAlt(_localctx, 2);
				{
				setState(187);
				_la = _input.LA(1);
				if ( !(_la==TEXTO || _la==NUMBER) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}

				              	_exprContent += _input.LT(-1).getText();
				              
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\30\u00c2\4\2\t\2"+
		"\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\3\2\3\2"+
		"\3\2\3\2\3\2\3\2\3\3\6\3*\n\3\r\3\16\3+\3\4\3\4\3\4\3\4\3\4\3\4\7\4\64"+
		"\n\4\f\4\16\4\67\13\4\3\4\3\4\3\5\3\5\3\5\3\5\5\5?\n\5\3\6\3\6\6\6C\n"+
		"\6\r\6\16\6D\3\7\3\7\3\7\3\7\3\7\5\7L\n\7\3\b\3\b\3\b\3\b\3\b\3\b\3\b"+
		"\3\b\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\13\3\13"+
		"\3\13\5\13g\n\13\3\13\3\13\3\13\3\13\3\13\3\13\5\13o\n\13\3\13\3\13\3"+
		"\f\3\f\3\f\3\f\3\f\3\f\3\f\6\fz\n\f\r\f\16\f{\3\f\3\f\3\f\3\f\3\f\3\f"+
		"\6\f\u0084\n\f\r\f\16\f\u0085\3\f\3\f\3\f\5\f\u008b\n\f\3\r\3\r\3\r\3"+
		"\r\3\r\3\r\3\r\6\r\u0094\n\r\r\r\16\r\u0095\3\r\3\r\3\r\3\16\3\16\3\16"+
		"\3\16\3\16\3\16\3\16\7\16\u00a2\n\16\f\16\16\16\u00a5\13\16\3\16\3\16"+
		"\3\17\3\17\3\17\3\17\3\17\3\17\3\17\5\17\u00b0\n\17\3\20\3\20\3\20\3\20"+
		"\3\20\7\20\u00b7\n\20\f\20\16\20\u00ba\13\20\3\21\3\21\3\21\3\21\5\21"+
		"\u00c0\n\21\3\21\2\2\22\2\4\6\b\n\f\16\20\22\24\26\30\32\34\36 \2\3\3"+
		"\2\26\27\2\u00c3\2\"\3\2\2\2\4)\3\2\2\2\6-\3\2\2\2\b>\3\2\2\2\n@\3\2\2"+
		"\2\fK\3\2\2\2\16M\3\2\2\2\20U\3\2\2\2\22\\\3\2\2\2\24f\3\2\2\2\26r\3\2"+
		"\2\2\30\u008c\3\2\2\2\32\u009a\3\2\2\2\34\u00af\3\2\2\2\36\u00b1\3\2\2"+
		"\2 \u00bf\3\2\2\2\"#\7\3\2\2#$\5\4\3\2$%\5\n\6\2%&\7\4\2\2&\'\b\2\1\2"+
		"\'\3\3\2\2\2(*\5\6\4\2)(\3\2\2\2*+\3\2\2\2+)\3\2\2\2+,\3\2\2\2,\5\3\2"+
		"\2\2-.\5\b\5\2./\7\25\2\2/\65\b\4\1\2\60\61\7\21\2\2\61\62\7\25\2\2\62"+
		"\64\b\4\1\2\63\60\3\2\2\2\64\67\3\2\2\2\65\63\3\2\2\2\65\66\3\2\2\2\66"+
		"8\3\2\2\2\67\65\3\2\2\289\7\16\2\29\7\3\2\2\2:;\7\5\2\2;?\b\5\1\2<=\7"+
		"\6\2\2=?\b\5\1\2>:\3\2\2\2><\3\2\2\2?\t\3\2\2\2@B\b\6\1\2AC\5\f\7\2BA"+
		"\3\2\2\2CD\3\2\2\2DB\3\2\2\2DE\3\2\2\2E\13\3\2\2\2FL\5\16\b\2GL\5\20\t"+
		"\2HL\5\22\n\2IL\5\26\f\2JL\5\30\r\2KF\3\2\2\2KG\3\2\2\2KH\3\2\2\2KI\3"+
		"\2\2\2KJ\3\2\2\2L\r\3\2\2\2MN\7\7\2\2NO\7\f\2\2OP\7\25\2\2PQ\b\b\1\2Q"+
		"R\7\r\2\2RS\7\16\2\2ST\b\b\1\2T\17\3\2\2\2UV\7\b\2\2VW\7\f\2\2WX\5\36"+
		"\20\2XY\7\r\2\2YZ\7\16\2\2Z[\b\t\1\2[\21\3\2\2\2\\]\7\25\2\2]^\b\n\1\2"+
		"^_\7\20\2\2_`\5\36\20\2`a\7\16\2\2ab\b\n\1\2b\23\3\2\2\2cd\7\25\2\2dg"+
		"\b\13\1\2eg\7\27\2\2fc\3\2\2\2fe\3\2\2\2gh\3\2\2\2hi\b\13\1\2ij\7\24\2"+
		"\2jn\b\13\1\2kl\7\25\2\2lo\b\13\1\2mo\7\27\2\2nk\3\2\2\2nm\3\2\2\2op\3"+
		"\2\2\2pq\b\13\1\2q\25\3\2\2\2rs\7\t\2\2st\7\f\2\2tu\5\24\13\2uv\7\r\2"+
		"\2vw\7\22\2\2wy\b\f\1\2xz\5\f\7\2yx\3\2\2\2z{\3\2\2\2{y\3\2\2\2{|\3\2"+
		"\2\2|}\3\2\2\2}~\7\23\2\2~\u008a\b\f\1\2\177\u0080\7\n\2\2\u0080\u0081"+
		"\7\22\2\2\u0081\u0083\b\f\1\2\u0082\u0084\5\f\7\2\u0083\u0082\3\2\2\2"+
		"\u0084\u0085\3\2\2\2\u0085\u0083\3\2\2\2\u0085\u0086\3\2\2\2\u0086\u0087"+
		"\3\2\2\2\u0087\u0088\7\23\2\2\u0088\u0089\b\f\1\2\u0089\u008b\3\2\2\2"+
		"\u008a\177\3\2\2\2\u008a\u008b\3\2\2\2\u008b\27\3\2\2\2\u008c\u008d\7"+
		"\13\2\2\u008d\u008e\7\f\2\2\u008e\u008f\5\24\13\2\u008f\u0090\7\r\2\2"+
		"\u0090\u0091\7\22\2\2\u0091\u0093\b\r\1\2\u0092\u0094\5\f\7\2\u0093\u0092"+
		"\3\2\2\2\u0094\u0095\3\2\2\2\u0095\u0093\3\2\2\2\u0095\u0096\3\2\2\2\u0096"+
		"\u0097\3\2\2\2\u0097\u0098\7\23\2\2\u0098\u0099\b\r\1\2\u0099\31\3\2\2"+
		"\2\u009a\u009b\5 \21\2\u009b\u00a3\b\16\1\2\u009c\u009d\7\17\2\2\u009d"+
		"\u009e\b\16\1\2\u009e\u009f\5 \21\2\u009f\u00a0\b\16\1\2\u00a0\u00a2\3"+
		"\2\2\2\u00a1\u009c\3\2\2\2\u00a2\u00a5\3\2\2\2\u00a3\u00a1\3\2\2\2\u00a3"+
		"\u00a4\3\2\2\2\u00a4\u00a6\3\2\2\2\u00a5\u00a3\3\2\2\2\u00a6\u00a7\b\16"+
		"\1\2\u00a7\33\3\2\2\2\u00a8\u00b0\5\32\16\2\u00a9\u00aa\7\f\2\2\u00aa"+
		"\u00ab\b\17\1\2\u00ab\u00ac\5\32\16\2\u00ac\u00ad\7\r\2\2\u00ad\u00ae"+
		"\b\17\1\2\u00ae\u00b0\3\2\2\2\u00af\u00a8\3\2\2\2\u00af\u00a9\3\2\2\2"+
		"\u00b0\35\3\2\2\2\u00b1\u00b2\b\20\1\2\u00b2\u00b8\5\34\17\2\u00b3\u00b4"+
		"\7\17\2\2\u00b4\u00b5\b\20\1\2\u00b5\u00b7\5\34\17\2\u00b6\u00b3\3\2\2"+
		"\2\u00b7\u00ba\3\2\2\2\u00b8\u00b6\3\2\2\2\u00b8\u00b9\3\2\2\2\u00b9\37"+
		"\3\2\2\2\u00ba\u00b8\3\2\2\2\u00bb\u00bc\7\25\2\2\u00bc\u00c0\b\21\1\2"+
		"\u00bd\u00be\t\2\2\2\u00be\u00c0\b\21\1\2\u00bf\u00bb\3\2\2\2\u00bf\u00bd"+
		"\3\2\2\2\u00c0!\3\2\2\2\21+\65>DKfn{\u0085\u008a\u0095\u00a3\u00af\u00b8"+
		"\u00bf";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}