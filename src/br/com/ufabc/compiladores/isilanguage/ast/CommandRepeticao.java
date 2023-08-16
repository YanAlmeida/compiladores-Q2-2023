package br.com.ufabc.compiladores.isilanguage.ast;

import java.util.ArrayList;

public class CommandRepeticao extends AbstractCommand {
 
	private String condition;
	private ArrayList<AbstractCommand> comandos;
	
	public CommandRepeticao(String condition, ArrayList<AbstractCommand> cmds) {
		this.condition = condition;
		this.comandos = cmds;
	}
	@Override
	public String generateJavaCode() {
		// TODO Auto-generated method stub
		StringBuilder str = new StringBuilder();
		str.append("while ("+condition+") {\n");
		for (AbstractCommand cmd: comandos) {
			str.append(cmd.generateJavaCode());
		}
		str.append("}");
		return str.toString();
	}
	@Override
	public String toString() {
		return "CommandControle [condition=" + condition + ", comandos=" + comandos + "]";
	}


}
