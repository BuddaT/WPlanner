package net.buddat.wplanner.gui.action;

import java.util.ArrayList;

import net.buddat.wplanner.gui.undo.UndoableAction;

public class DraggedAction implements UndoableAction {
	
	private ArrayList<UndoableAction> actionList = new ArrayList<UndoableAction>();

	@Override
	public void execute() {
		// TODO Auto-generated method stub

	}

	@Override
	public void undo() {
		for (UndoableAction action : actionList) {
			action.undo();
		}
	}

	@Override
	public void redo() {
		for (UndoableAction action : actionList) {
			action.redo();
		}
	}
	
	public void addAction(UndoableAction action) {
		actionList.add(action);
	}
	
	public boolean isEmpty() {
		return actionList.isEmpty();
	}

}
