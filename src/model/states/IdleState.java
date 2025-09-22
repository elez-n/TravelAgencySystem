/***********************************************************************
 * Module:  IdleState.java
 * Author:  Korisnik
 * Purpose: Defines the Class IdleState
 ***********************************************************************/

package model.states;

import java.util.*;

import view.MainView;

/**
 * Klasa koja opisuje pocetno stanje aplikacije i azurira korisnicki interfejs u skladu sa tim.
 * 
 *  @author G4
 *  */
public class IdleState extends AbstractAppState {

	public IdleState(MainView appView) {
		super(appView);
		
		appView.getStatusbar().setState("Idle");
		this.toolbar.setVisible(false);
		// this.toolbar.setButtonsEnabled(false);
		this.menubar.acceptItem.setEnabled(false);
		this.menubar.cancelItem.setEnabled(false);
		this.menubar.deleteItem.setEnabled(false);
		this.menubar.firstItem.setEnabled(false);
		this.menubar.lastItem.setEnabled(false);
		this.menubar.previousItem.setEnabled(false);
		this.menubar.nextItem.setEnabled(false);
		this.menubar.editItem.setEnabled(false);
		this.menubar.newItem.setEnabled(false);
	}

	@Override
	public void handleNext() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleFirst() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handlePrev() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleLast() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void handleSpecificSelection() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void handleSpecificSubmit() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleSubmit() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void handleCreate() {
		
	}
}