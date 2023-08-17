package br.com.ufabc.compiladores.isilanguage.datastructures;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class IsiSymbolTable {
	
	private HashMap<String, IsiSymbol> map;
	private HashMap<String, Boolean> attrMap;
	private HashMap<String, Boolean> readMap;
	
	public IsiSymbolTable() {
		map = new HashMap<String, IsiSymbol>();
		attrMap = new HashMap<String, Boolean>();
		readMap = new HashMap<String, Boolean>();
		
	}
	
	public void add(IsiSymbol symbol) {
		map.put(symbol.getName(), symbol);
		attrMap.put(symbol.getName(), false);
		readMap.put(symbol.getName(), false);
	}

	public void setAttr(String symbolName){
		attrMap.put(symbolName, true);
	}

	public Boolean isAttr(String symbolName){
		return attrMap.get(symbolName);
	}

	public void setRead(String symbolName){
		readMap.put(symbolName, true);
	}

	public Boolean isRead(String symbolName){
		return readMap.get(symbolName);
	}
	
	public boolean exists(String symbolName) {
		return map.get(symbolName) != null;
	}
	
	public IsiSymbol get(String symbolName) {
		return map.get(symbolName);
	}

	public void remove(String symbolName) {
		map.remove(symbolName);
		attrMap.remove(symbolName);
	}
	
	public ArrayList<IsiSymbol> getAll(){
		ArrayList<IsiSymbol> lista = new ArrayList<IsiSymbol>();
		for (IsiSymbol symbol : map.values()) {
			lista.add(symbol);
		}
		return lista;
	}

	public ArrayList<String> getAllIdStrings(){
		ArrayList<String> lista = new ArrayList<String>();
		for (String id : map.keySet()) {
			lista.add(id);
		}
		return lista;
	}

	
	
}
