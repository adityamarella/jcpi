/**
 * EngineDebugCommand.java
 * 
 * Copyright 2007 Java Chess Protocol Interface Project
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.sourceforge.jcpi.commands;

import net.sourceforge.jcpi.IEngine;

/**
 * EngineDebugCommand
 *
 * @author Phokham Nonava
 */
public class EngineDebugCommand implements IEngineCommand {

	public final boolean debug;
	public final boolean toggle;
	
	public EngineDebugCommand(boolean toggle, boolean debug) {
		this.toggle = toggle;
		if (toggle) {
			// Force debug to false
			this.debug = false;
		} else {
			this.debug = debug;
		}
	}
	
	public void accept(IEngine v) {
		v.visit(this);
	}

}