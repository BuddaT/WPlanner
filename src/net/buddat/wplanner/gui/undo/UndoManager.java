package net.buddat.wplanner.gui.undo;

public class UndoManager {

	public static final int QUEUE_SIZE = 10;
	
	private UndoableAction[] undoQueue = new UndoableAction[QUEUE_SIZE];
	private int pos = 0;
	
	public UndoManager() {
		
	}
	
	public void addAction(UndoableAction action) {
		if (pos < QUEUE_SIZE) {
			undoQueue[pos++] = action;
			
			for (int i = pos; i < QUEUE_SIZE; i++)
				undoQueue[i] = null;
		} else {
			UndoableAction[] tempQueue = new UndoableAction[QUEUE_SIZE];
			System.arraycopy(undoQueue, 1, tempQueue, 0, QUEUE_SIZE - 1);
			
			undoQueue = tempQueue;
			
			undoQueue[pos - 1] = action;
		}
	}
	
	public boolean canUndo() {
		return pos > 0;
	}
	
	public boolean canRedo() {
		return (pos < QUEUE_SIZE) && (undoQueue[pos] != null);
	}
	
	public void undo() {
		if (canUndo())
			undoQueue[--pos].undo();
	}
	
	public void redo() {
		if (canRedo())
			undoQueue[pos++].redo();
	}
}
