package net.buddat.wplanner.gui.undo;

public interface UndoableAction {

	public void execute();
	public void undo();
	public void redo();
	
}
