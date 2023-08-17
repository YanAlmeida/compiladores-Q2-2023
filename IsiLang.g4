grammar IsiLang;

@header{
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
}

@members{
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
		_varName = _input.LT(-1).getText();
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
}

prog	: 'programa' decl bloco  'fimprog;'
           {  program.setVarTable(symbolTable);
           	  program.setComandos(stack.pop());
           	 
           } 
		;
		
decl    :  (declaravar)+
        ;
        
        
declaravar :  tipo ID  { efetuaDeclaracaoVariavel(_input.LT(-1).getText()); } 
              (  VIR 
              	 ID { efetuaDeclaracaoVariavel(_input.LT(-1).getText()); } 
              )* 
               SC
           ;
           
tipo       : 'numero' { _tipo = IsiVariable.NUMBER;  }
           | 'texto'  { _tipo = IsiVariable.TEXT;  }
           ;
        
bloco	: { curThread = new ArrayList<AbstractCommand>(); 
	        stack.push(curThread);  
          }
          (cmd)+
		;
		

cmd		:  cmdleitura  
 		|  cmdescrita 
 		|  cmdattrib
 		|  cmdselecao
		|  cmdrepeticao
		;
		
cmdleitura	: 'leia' AP
                     ID { verificaID(_input.LT(-1).getText());
                     	  _readID = _input.LT(-1).getText();
                        } 
                     FP 
                     SC 
                     
              {
              	IsiVariable var = (IsiVariable)symbolTable.get(_readID);
              	CommandLeitura cmd = new CommandLeitura(_readID, var);
              	stack.peek().add(cmd);
				symbolTable.setAttr(_readID);
              }   
			;
			
cmdescrita	: 'escreva' 
                 AP 
                 expr
                 FP 
                 SC
               {
               	  CommandEscrita cmd = new CommandEscrita(_exprContent);
               	  stack.peek().add(cmd);
				  _exprContent = "";
               }
			;
			
cmdattrib	:  ID { verificaID(_input.LT(-1).getText());
                    _exprID = _input.LT(-1).getText();
                   } 
               ATTR { _exprContent = ""; } 
               expr 
               SC
               {
				 verificaTipoIDExpr(_exprID, _exprContent);
               	 CommandAtribuicao cmd = new CommandAtribuicao(_exprID, _exprContent);
               	 stack.peek().add(cmd);
				 symbolTable.setAttr(_exprID);
				 _exprContent = "";
               }
			;
			

comparacao :    ID    { efetuaVerificacoesIDComparacao(_input.LT(-1).getText()); 
						_exprDecision = _input.LT(-1).getText(); 
						_lastTermo = _input.LT(-1).getText(); }
            	OPREL { _exprDecision += _input.LT(-1).getText(); }
                (ID { efetuaVerificacoesIDComparacao(_input.LT(-1).getText()); } | NUMBER)
				{	_exprDecision += _input.LT(-1).getText(); 
					_lastTermo = ""; }
			;

cmdselecao  :  'se' AP
					comparacao
                    FP 
                    ACH 
                    { curThread = new ArrayList<AbstractCommand>(); 
                      stack.push(curThread);
                    }
                    (cmd)+ 
                    
                    FCH 
                    {
                       listaTrue = stack.pop();	
                    } 
                   ('senao' 
                   	 ACH
                   	 {
                   	 	curThread = new ArrayList<AbstractCommand>();
                   	 	stack.push(curThread);
                   	 } 
                   	(cmd+) 
                   	FCH
                   	{
                   		listaFalse = stack.pop();
                   		CommandDecisao cmd = new CommandDecisao(_exprDecision, listaTrue, listaFalse);
                   		stack.peek().add(cmd);
                   	}
                   )?
            ;

cmdrepeticao : 'enquanto' 	AP
							comparacao
						 	FP
						 	ACH 
							{ 
								curThread = new ArrayList<AbstractCommand>();
								stack.push(curThread);
							}
							(cmd)+ 
							FCH
							{
								CommandRepeticao cmd = new CommandRepeticao(_exprDecision, stack.pop());
								stack.peek().add(cmd);
							}
			;
						 
			
expr		:  termo {_lastTermo = _input.LT(-1).getText();}( 
	             OP  { _exprContent += _input.LT(-1).getText(); }
	            termo { verificaTermosMesmoTipo(_lastTermo, _input.LT(-1).getText());
				        _lastTermo = _input.LT(-1).getText(); }
	            )*
				{_lastTermo = "";}
			;
			
termo		: ID { 	verificaID(_input.LT(-1).getText());
					verificaIDInicializado(_input.LT(-1).getText());
					symbolTable.setRead(_input.LT(-1).getText());
	               _exprContent += _input.LT(-1).getText();
                 } 
            | 
              (NUMBER | TEXTO)
              {
              	_exprContent += _input.LT(-1).getText();
              }
			;
			
	
AP	: '('
	;
	
FP	: ')'
	;
	
SC	: ';'
	;
	
OP	: '+' | '-' | '*' | '/'
	;
	
ATTR : '='
	 ;
	 
VIR  : ','
     ;
     
ACH  : '{'
     ;
     
FCH  : '}'
     ;
	 
	 
OPREL : '>' | '<' | '>=' | '<=' | '==' | '!='
      ;
      
ID	: [a-z] ([a-z] | [A-Z] | [0-9])*
	;

TEXTO : ["]([a-z] | [A-Z] | [0-9] | ' ' | '\t' | '\n' | '\r')*["]
 	 ;

NUMBER	: [0-9]+ ('.' [0-9]+)?
		;
		
WS	: (' ' | '\t' | '\n' | '\r') -> skip;
