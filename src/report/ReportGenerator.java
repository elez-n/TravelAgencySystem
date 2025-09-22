package report;

import java.awt.Color;
import java.awt.GridLayout;
import java.io.File;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import ar.com.fdvs.dj.core.DynamicJasperHelper;
import ar.com.fdvs.dj.core.layout.ClassicLayoutManager;
import ar.com.fdvs.dj.domain.AutoText;
import ar.com.fdvs.dj.domain.DynamicReport;
import ar.com.fdvs.dj.domain.ImageBanner;
import ar.com.fdvs.dj.domain.Style;
import ar.com.fdvs.dj.domain.builders.FastReportBuilder;
import ar.com.fdvs.dj.domain.builders.StyleBuilder;
import ar.com.fdvs.dj.domain.constants.Font;
import ar.com.fdvs.dj.domain.constants.HorizontalAlign;
import ar.com.fdvs.dj.domain.constants.Page;
import model.TreeElement.Column;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import view.MainView;
import model.GeneralTableModel;

/**
 * Klasa {@code ReportGenerator} omogućava kreiranje i prikaz dinamičkih izvještaja
 * na osnovu podataka iz GUI aplikacije. 
 * 
 * Korisniku se nudi mogućnost odabira kolona koje će biti prikazane, 
 * a izvještaj se zatim može pregledati ili sačuvati u PDF formatu.
 * 
 */
public class ReportGenerator {

    /**
     * Prikazuje dijalog sa listom kolona i omogućava korisniku da izabere 
     * koje će kolone biti uključene u izvještaj.
     *
     * @param columns lista kolona iz tabele
     * @return lista indeksa selektovanih kolona, ili {@code null} ako je korisnik otkazao izbor
     */
    public List<Integer> showColumnSelectionDialog(List<Column> columns) {
        JPanel panel = new JPanel(new GridLayout(0, 1));
        JCheckBox[] checkBoxes = new JCheckBox[columns.size()];

        for (int i = 0; i < columns.size(); i++) {
            checkBoxes[i] = new JCheckBox(columns.get(i).getName(), true);
            panel.add(checkBoxes[i]);
        }

        int result = JOptionPane.showConfirmDialog(
                null, panel, "Izaberite kolone za izveštaj",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            List<Integer> selected = new ArrayList<>();
            for (int i = 0; i < checkBoxes.length; i++) {
                if (checkBoxes[i].isSelected()) {
                    selected.add(i);
                }
            }
            return selected;
        } else {
            return null;
        }
    }

    /**
     * Generiše finalni izvještaj na osnovu podataka iz {@link MainView}.
     * 
     * Korisnik bira kolone koje želi uključiti, a zatim može:
     * 
     *   pregledati izvještaj u JasperViewer-u,
     *   sačuvati ga u PDF formatu,
     *   ili otkazati operaciju.
     * 
     * 
     *
     * @param appView glavna aplikaciona forma iz koje se izvlače podaci o tabeli i modelu
     */
    public void generateReportFinal(MainView appView) {
        GeneralTableModel tableModel = appView.getTablePanel().getTableModel();
        List<Column> allColumns = tableModel.getColumns();
        String tableName = appView.getCurrentTable().getName();

        List<Integer> selectedIndices = showColumnSelectionDialog(allColumns);
        if (selectedIndices == null || selectedIndices.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Niste izabrali nijednu kolonu.");
            return;
        }

        // Definicija prilagođenog JRDataSource-a koji povezuje Jasper izvještaj sa podacima iz JTable-a
        JRDataSource dataSource = new JRDataSource() {
            private int index = -1;

            @Override
            public boolean next() {
                index++;
                return index < tableModel.getRowCount();
            }

            @Override
            public Object getFieldValue(JRField jrField) {
                String fieldName = jrField.getName();
                for (int idx : selectedIndices) {
                    String colName = allColumns.get(idx).getName();
                    if (colName.equals(fieldName)) {
                        return tableModel.getValueAt(index, idx);
                    }
                }
                return null;
            }
        };

        FastReportBuilder drb = new FastReportBuilder();

        Font titleFont = Font.TIMES_NEW_ROMAN_BIG_BOLD;
        Style columnStyle = new StyleBuilder(false)
                .setHorizontalAlign(HorizontalAlign.LEFT)
                .build();
        Style titleStyle = new StyleBuilder(false)
                .setFont(titleFont)
                .setHorizontalAlign(HorizontalAlign.LEFT)
                .build();
        Style headerStyle = new StyleBuilder(false)
                .setBackgroundColor(Color.GRAY)
                .setFont(titleFont)
                .setHorizontalAlign(HorizontalAlign.CENTER)
                .build();

        // Dodavanje selektovanih kolona u izvještaj
        for (int idx : selectedIndices) {
            String colName = allColumns.get(idx).getName();
            Object firstValue = tableModel.getValueAt(0, idx);
            Class<?> clazz = firstValue != null ? firstValue.getClass() : String.class;
            drb.addColumn(colName, colName, clazz.getName(), 50, columnStyle, headerStyle);
        }

        // Konfigurisanje izgleda izvještaja
        DynamicReport dr = drb
                .setTitle(tableName)
                .setTitleStyle(titleStyle)
                .setOddRowBackgroundStyle(
                        new StyleBuilder(false)
                                .setBackgroundColor(new Color(173, 216, 230))
                                .build()
                )
                .setPrintBackgroundOnOddRows(true)
                .setUseFullPageWidth(true)
                .setPageSizeAndOrientation(Page.Page_Legal_Landscape())
                .addFirstPageImageBanner("resources/logo.png", 100, 60, ImageBanner.Alignment.Right)
                .setHeaderVariablesHeight(50)
                .addAutoText(
                        "Datum i vrijeme: " + new java.text.SimpleDateFormat("dd.MM.yyyy. HH:mm").format(new Date()),
                        AutoText.POSITION_HEADER,
                        AutoText.ALIGNMENT_LEFT,
                        200,
                        new StyleBuilder(true).build()
                )
                .addAutoText(
                        AutoText.AUTOTEXT_PAGE_X_SLASH_Y,
                        AutoText.POSITION_FOOTER,
                        AutoText.ALIGNMENT_RIGHT,
                        30,
                        20
                )
                .build();

        // Generisanje i prikaz/čuvanje izvještaja
        try {
            JasperPrint jasperPrint = DynamicJasperHelper.generateJasperPrint(dr, new ClassicLayoutManager(), dataSource);

            Object[] options = {"Pregled", "Sačuvaj PDF", "Otkaži"};
            int choice = JOptionPane.showOptionDialog(null, "Izaberite opciju:", "Opcije izvještaja",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

            if (choice == 0) {
                JasperViewer.viewReport(jasperPrint, false);
            } else if (choice == 1) {
                JFileChooser fc = new JFileChooser();
                fc.setSelectedFile(new File("DinamickiIzvjestaj.pdf"));
                if (fc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                    JasperExportManager.exportReportToPdfFile(jasperPrint, fc.getSelectedFile().getAbsolutePath());
                    JOptionPane.showMessageDialog(null, "PDF sačuvan.");
                }
            }
        } catch (JRException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Greška pri generisanju izvještaja: " + ex.getMessage());
        }
    }
}
