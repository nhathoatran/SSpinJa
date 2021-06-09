// Copyright 2010, University of Twente, Formal Methods and Tools group
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//    http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package spinja.promela.compiler.variable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import espinja.CompileOnlyTest;
import spinja.promela.compiler.parser.ParseException;
import spinja.util.StringUtil;
import spinja.util.StringWriter;
import sspinja.CompileScheduler;
import sspinja.Config;
/**
 * A variable container is an object that can hold one or more variables 
 * (e.g. a specification or a proctype is Promela). This container can 
 * then be used to create the encode and decode functions easily.
 *
 * @author Marc de Jonge
 */
public class VariableStore implements VariableContainer {
    public static boolean canSkipVar(Variable var) {
        return !(var.isWritten() || var.getType() instanceof ChannelType);
    }

    private final Map<String, Variable> varNames; 
    private final List<Variable> vars;

    private final Map<String, String> mapping;
    
    public void addVariableMapping(String s, String v) {
    	mapping.put(s, v);
    }
    
    /* 
     * variable store is empty ?
     */
    public boolean isEmpty(){
    	return vars.size() <= 2 ; //_pc, _pid
    }
    
    public String getVariableMapping(String s) {
    	return mapping.get(s);
    }

    public Map<String, String> getVariableMappings() {
    	return mapping;
    }

    /**
     * Creates a new VariableStore.
     */
    public VariableStore() {
        vars = new LinkedList<Variable>();
        varNames = new HashMap<String, Variable>();
        mapping = new HashMap<String, String>();
    }

    /**
     * Adds the given variable to this store. The variable may not be null, 
     * otherwise an IllegalArgumentException will be thrown.
     *
     * @param var
     *            The variable that is to be added to this store.
     */
    public void addVariable(final Variable var) {
        if (var == null) {
            throw new IllegalArgumentException();
        }
        vars.add(var);
        varNames.put(var.getName(), var);
    }
    
    public void prependVariable(final Variable var) {
        if (var == null) {
            throw new IllegalArgumentException();
        }
        vars.add(0, var);
        varNames.put(var.getName(), var);
    }
    
    /**
     * @return The number of bytes that is needed to store all the 
     *         variables currently in the container.
     */
    public int getBufferSize() {
        int nrBytes = 0;
        for (final Variable var : vars) {
            if (canSkipVar(var)) {
                continue;
            }
           
            if(var.getArraySize()!=-1){
            for (int i = 0; i < var.getArraySize(); i++) {
                nrBytes += (var.getType().getBits() - 1) / 8 + 1;
            }
            }else{//HIL 05/26/2015:To fix the var size issue
            	 nrBytes += (var.getType().getBits() - 1) / 8 + 1;
            }
            //System.out.println("#####getBufferSize#######"+var.getName()+ nrBytes+"##"+var.getType());
        }
        return nrBytes;
    }

    /**
     * Returns the variable that is defined by the name that is given.
     *
     * @param name
     *            The name that is used to find a variable.
     * @return The variable that is defined by the name that is given or null if there was no such
     *         variable accesable.
     */
    public Variable getVariable(final String name) {
        Variable var = varNames.get(name);
        if (var != null)
            return var;
    	String to = getVariableMapping(name);
    	if (null == to)
    	    return null;
        return getVariable(to);
    }

    /**
     * @return A list with all the variables that are stored here.
     */
    public List<Variable> getVariables() {
        return new ArrayList<Variable>(vars);
    }

    /**
     * Checks if a variable with the given name exists in the current store.
     *
     * @param name
     *            The name that is to be checked.
     * @return True when there was already any variable with that name, otherwise false.
     */
    public boolean hasVariable(final String name) {
        Variable var = varNames.get(name);
        if (var != null)
            return true;
        String to = getVariableMapping(name);
        if (null == to)
            return false;
        return hasVariable(to);
    }
    
    /**
     * @return The number of assigned channel variables in the current store.
     */
    public int getChannelCount() {
    	int count = 0;
    	for (final Variable var : vars) {
    		if (var instanceof ChannelVariable && var.getType() != ChannelType.UNASSIGNED_CHANNEL) {
    			count += var.getArraySize();
    		}
    	}
    	return count;
    }

    /**
     * Prints the definitions needed to define the variables that are stored in this store.
     *
     * @param w
     *            The StringWriter which is used to print the definition.
     * @throws ParseException
     *             When something went wrong while calculating the initializing expression.
     */
    public void printDefinitions(final StringWriter w) throws ParseException {
        for (final Variable var : vars) {
            final int initValue = var.getInitExpr().getConstantValue();
            if (var.getArraySize() == 1) {
                w.appendLine("private ", var.getType().getJavaName(), " ", var.getName(), " = ",
                    initValue, ";");
            } else {
                if (initValue != 0) {
                    throw new ParseException(
                        "A initializing expression is not possible for an array declaration.");
                }
                w.appendLine("private ", var.getType().getJavaName(), " ", var.getName(),
                    "[] = new ", var.getType().getJavaName(), "[", var.getArraySize(), "];");
            }
        }
    }

    /**
     * Creates the java code for encoding the variables in a byte[] name _buffer. It uses a counter
     * named _cnt for the index of the buffer.
     */
    public void printEncode(final StringWriter w) {
        for (final Variable var : vars) {
        	if (Config.isCompileScheduler && 
        			StringUtil.containSchedulerKeyword(var.getName())) { //obmit _pid
            	continue ;
        	}        	
        	 
            if (canSkipVar(var)) {
                continue;
            }
            if (var.getType() instanceof CustomVariableType) {
                w.appendLine(var.getName(), ".encode(_writer);");
            } else {
                if (var.getArraySize() > 1) {
                    w.appendLine("for(int _i = 0; _i < ", var.getArraySize(), "; _i++) {").indent();
                }
                w.appendPrefix().append("_writer.write");
                
                switch (var.getType().getBits()) {
                    case 1:
                        w.append("Boolean");
                        break;
                    case 8:
                        w.append("Byte");
                        break;
                    case 16:
                        w.append("Short");
                        break;
                    case 32:
                        w.append("Int");
                        break;
                    default:
                        throw new Error("Unknown number of bytes: " + var.getType().getBits());
                }
                w.append("(")
                        .append(var.getName())
                        .appendIf(var.getArraySize() > 1, "[_i]")
                        .append(");")
                        .appendPostfix();
                if (var.getArraySize() > 1) {
                    w.outdent().appendLine("}");
                }
            }
        }
    }

    /**
     * Creates the java code for decoding the variables in a byte[] name _buffer. It uses a counter
     * named _cnt for the index of the buffer.
     */
    public void printDecode(final StringWriter w) {
        for (final Variable var : vars) {
        	if (Config.isCompileScheduler && 
        			StringUtil.containSchedulerKeyword(var.getName())) { //remove _pid, _pc ?
            	continue ;
        	}
        	if (canSkipVar(var)) {
                continue;
            }
            if (var.getType() instanceof CustomVariableType) {
                w.appendLine("if(!", var.getName(), ".decode(_reader)) return false;");
            } else {
                if (var.getArraySize() > 1) {
                    w.appendLine("for(int _i = 0; _i < ", var.getArraySize(), "; _i++) {").indent();
                }
                w.appendPrefix().append(var.getName()).appendIf(var.getArraySize() > 1,
                    "[_i]").append(" = _reader.read");
                switch (var.getType().getBits()) {
                    case 1:
                        w.append("Boolean");
                        break;
                    case 8:
                        w.append("Byte");
                        break;
                    case 16:
                        w.append("Short");
                        break;
                    case 32:
                        w.append("Int");
                        break;
                    default:
                        throw new Error("Unknown number of bytes: " + var.getType().getBits());
                }
                w.append("();").appendPostfix();
                if (var.getArraySize() > 1) {
                    w.outdent().appendLine("}");
                }
            }
        }
    }

    /**
     * Creates the java code for converting the variables to a String using 
     * a StringWriter called w. Afterwards w contains the java code that 
     * can convert the variables int to a String.
     */
    public void printToString(final StringWriter w) {
        for (final Variable var : vars) {
        	if ((Config.isCompileScheduler || Config.isCompileOnlyTest) &&
        			StringUtil.containSchedulerKeyword(var.getName()))
        		continue ;
            if (var.getArraySize() == 1 ) {                
            	w.appendLine("sb.append(\"", var.getName(), " = \").append(",
                    var.getName() + ").append(\'\\t\');");
            } else {
            	if (var.getArraySize() == -1) { 
            		if (var.isRead() && var.isWritten()) {
            			w.appendLine("sb.append(\"", var.getName(), " = \").append(",
            					var.getName() + ").append(\'\\t\');");
            		} else
            		if (!var.getName().equals("_")) {
            			w.appendLine("sb.append(\"", var.getName(), " = \").append(",
            					var.getName() + ").append(\'\\t\');");
            		}
                } else {            
	                for (int i = 0; i < var.getArraySize(); i++) {
	                    w.appendLine("sb.append(\"", var.getName(), "[", i, "] = \").append(",
	                        var.getName(), "[", i, "]).append(\'\\t\');");
	                }
                }
            }
        }
    }
}