package mesh.budget.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class AppState {
	
	//private boolean bankStatementRowChanged=false;
	private BooleanProperty bankStatementRowChanged = new SimpleBooleanProperty();

	public boolean getBankStatementRowChanged() {
		return bankStatementRowChanged.get();
	}

	public void setBankStatementRowChanged(boolean bankStatementRowChanged) {
		this.bankStatementRowChanged.set(bankStatementRowChanged);
	}
	
	public BooleanProperty bankStatementRowChangedProperty() {
		return bankStatementRowChanged;
	}
}
