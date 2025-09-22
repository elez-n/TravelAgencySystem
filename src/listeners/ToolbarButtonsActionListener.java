package listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import model.states.EditState;
import view.MainView;
import report.ReportGenerator;




/**
 *Klasa koja implementira listener za izvrsavanje akcija pri kliku na JButton instancu iz toolbar-a.
 * 
 * @author G4
 */
public class ToolbarButtonsActionListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		MainView appView = (MainView) SwingUtilities.getWindowAncestor((JButton) e.getSource());
		switch (e.getActionCommand()) {
		case "new":
			appView.getAppState().handleCreate();
			break;
		case "edit":
			appView.setAppState(new EditState(appView));
			appView.getAppState().handleChange();
			break;
		case "next":
			appView.getAppState().handleNext();
			break;
		case "previous":
			appView.getAppState().handlePrev();
			break;
		case "last":
			appView.getAppState().handleLast();
			break;
		case "first":
			appView.getAppState().handleFirst();
			break;
		case "accept":
			appView.getAppState().handleSubmit();
			break;
		case "cancel":
			appView.getAppState().handleCancel();
			break;
		case "delete":
			appView.getAppState().handleDelete();
			break;
		case "report":
			try {
		        appView = (MainView) SwingUtilities.getWindowAncestor((JButton) e.getSource());
		        ReportGenerator reportGenerator = new ReportGenerator();
		        reportGenerator.generateReportFinal(appView);
				
		    } catch (Exception ex) {
		        ex.printStackTrace();
		        JOptionPane.showMessageDialog(null, "Greška prilikom generisanja izveštaja: " + ex.getMessage());
		    }
		    break;
		}
	}
}
