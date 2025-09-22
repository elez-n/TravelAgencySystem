

package controller;

import java.util.*;

import model.Model;
import model.states.IdleState;
import view.MainView;

/** 
 * Klasa Controller predstavlja kontroler u MVC arhitekturi aplikacije.
 * Povezuje model i prikaz (view), i inicijalizuje početno stanje aplikacije.
 *  @author G4  
 */
public class Controller {
	
	private MainView mainview;
	private Model model;
	/**
	 * Konstruktor koji kreira novi kontroler i povezuje model i prikaz.
	 * Postavlja inicijalno stanje aplikacije na {@link IdleState}.
	 *
	 * @param model instanca modela podataka
	 * @param mainview instanca glavnog prikaza aplikacije
	 */
	public Controller(Model model, MainView mainview) {
		this.mainview = mainview;
		this.model = model;
		mainview.setAppState(new IdleState(mainview));
	}
}