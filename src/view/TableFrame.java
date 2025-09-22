package view;

import model.CustomTableModel;
import model.TreeElement;

import javax.swing.*;
import javax.swing.table.*;

import controller.LoginModel;
import listeners.TableSelectionListener;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

/** {@code TableFrame} predstavlja panel za prikaz tabele u glavnom prozoru
* aplikacije. Panel sadrži JTable sa podacima, row number tabelu sa lijeve strane,
* JScrollPane sa horizontalnim i vertikalnim scrollbar-ovima, toolbar sa nazivom
* tabele i dugmetom za osvježavanje.
* 
* @author G4
*/
public class TableFrame extends JPanel {

    private final JTable table;
    private final CustomTableModel tableModel;
    private final TreeElement.Table tableMeta;
    private final JTable rowNumberTable;
    private final JScrollPane scrollPane;
	private boolean fullAccess=false;
    

    /**
     * Konstruktor kreira TableFrame sa JTable-om, scroll pane-om, toolbar-om
     * i inicijalizuje sve potrebne komponente.
     * 
     * @param tableMeta metadata tabele
     * @param mainView referenca na glavni prozor (MainView)
     */
    public TableFrame(TreeElement.Table tableMeta, MainView mainView) {
        this.tableMeta = tableMeta;
        this.tableModel = new CustomTableModel(tableMeta);
        this.table = new JTable(tableModel);
        formPanel = new FormPanel();
        if((LoginModel.accessLevel==5 && tableModel.getTable().isAdmin())||(LoginModel.accessLevel==4 && tableModel.getTable().isMenadzer())||
				(LoginModel.accessLevel==3 && tableModel.getTable().isOperater())||(LoginModel.accessLevel==2 && tableModel.getTable().isUpravnik())) {

			setFullAccess(true);
		}
		else
		{
			setFullAccess(false);
		}

        table.getSelectionModel().addListSelectionListener(new TableSelectionListener(mainView));

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        setupTable();
        this.rowNumberTable = createRowNumberTable();
        this.scrollPane = createScrollPane();

        add(createToolBar(mainView), BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        tableModel.getAllData();
        adjustColumnWidths();

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                adjustColumnWidths();
            }
        });

        setVisible(true);
    }



	private void setupTable() {
        table.setDefaultRenderer(Object.class, new TooltipCellRenderer());
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowSelectionAllowed(true);
        table.setColumnSelectionAllowed(false);
        table.setGridColor(Color.LIGHT_GRAY);
        table.setSelectionBackground(new Color(51, 153, 255));
        table.setSelectionForeground(Color.WHITE);
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setResizingAllowed(true);
        table.setFont(new Font("Arial", Font.PLAIN, 11));
        table.setRowHeight(25);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.setFillsViewportHeight(false);


        JTableHeader header = table.getTableHeader();
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus,
                                                           int row, int column) {
                JLabel lbl = (JLabel) super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);
                lbl.setBackground(new Color(70, 130, 180));
                lbl.setForeground(Color.WHITE);
                lbl.setFont(lbl.getFont().deriveFont(Font.BOLD));
                lbl.setHorizontalAlignment(SwingConstants.CENTER);
                lbl.setOpaque(true);

                FontMetrics fm = lbl.getFontMetrics(lbl.getFont());
                int colWidth = table.getColumnModel().getColumn(column).getWidth();
                if (fm.stringWidth(value != null ? value.toString() : "") > colWidth) {
                    lbl.setToolTipText(value.toString());
                } else {
                    lbl.setToolTipText(null);
                }

                return lbl;
            }
        });
    }

    private JTable createRowNumberTable() {
        JTable rowTable = new JTable(new RowNumberTableModel());
        rowTable.setPreferredScrollableViewportSize(new Dimension(50, 0));
        rowTable.setRowHeight(table.getRowHeight());
        rowTable.setFocusable(false);
        rowTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        rowTable.setGridColor(Color.LIGHT_GRAY);
        rowTable.setBackground(new Color(240, 240, 240));
        rowTable.setFont(new Font("Arial", Font.BOLD, 11));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        rowTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);

        rowTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        rowTable.getColumnModel().getColumn(0).setMaxWidth(50);
        return rowTable;
    }

    private JScrollPane createScrollPane() {
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setRowHeaderView(rowNumberTable);
        scrollPane.setCorner(JScrollPane.UPPER_LEFT_CORNER, createCornerComponent());
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setWheelScrollingEnabled(true);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getHorizontalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        return scrollPane;
    }

    private JComponent createCornerComponent() {
        JPanel corner = new JPanel();
        corner.setBackground(new Color(70, 130, 180));
        corner.setPreferredSize(new Dimension(50, 25));
        return corner;
    }

    private JToolBar createToolBar(MainView mainview) {
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        toolBar.setBackground(new Color(240, 240, 240));
        JLabel titleLabel = new JLabel("Tabela: " + tableMeta.getName());
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        toolBar.add(titleLabel);
        toolBar.addSeparator();
        JButton refreshBtn = new JButton("Osvježi");
        refreshBtn.addActionListener(e -> {
            tableModel.getAllData();
            adjustColumnWidths();
        });
        toolBar.add(refreshBtn);
        
        toolBar.add(Box.createHorizontalGlue()); 
        JButton closeBtn = new JButton("Zatvori tabelu");
        closeBtn.addActionListener(e -> {
            mainview.closeAll();
        });

        toolBar.add(closeBtn);
        return toolBar;
    }

    /**
     * Prilagođava širinu kolona tabele u skladu sa sadržajem i širinom viewport-a.
     */
    public void adjustColumnWidths() {
        TableColumnModel columnModel = table.getColumnModel();
        int totalWidth = scrollPane.getViewport().getWidth();
        int totalPreferredWidth = 0;
        int[] preferredWidths = new int[columnModel.getColumnCount()];

        for (int i = 0; i < columnModel.getColumnCount(); i++) {
            int width = calculateColumnWidth(i);
            preferredWidths[i] = width;
            totalPreferredWidth += width;
        }

        if (totalPreferredWidth < totalWidth) {
            double scale = (double) totalWidth / totalPreferredWidth;
            for (int i = 0; i < columnModel.getColumnCount(); i++) {
                int newWidth = (int) Math.round(preferredWidths[i] * scale);
                columnModel.getColumn(i).setPreferredWidth(newWidth);
                columnModel.getColumn(i).setMinWidth(50);
            }
        } else {
            for (int i = 0; i < columnModel.getColumnCount(); i++) {
                columnModel.getColumn(i).setPreferredWidth(preferredWidths[i]);
                columnModel.getColumn(i).setMinWidth(50);
            }
        }
    }

    private int calculateColumnWidth(int columnIndex) {
        FontMetrics fm = table.getFontMetrics(table.getFont());
        int maxWidth = 50;
        for (int row = 0; row < tableModel.getRowCount(); row++) {
            Object value = tableModel.getValueAt(row, columnIndex);
            if (value != null) {
                int cellWidth = fm.stringWidth(value.toString());
                maxWidth = Math.max(maxWidth, cellWidth);
            }
        }
        maxWidth += 20;
        if (maxWidth > 300) {
            maxWidth = 300;
            table.getColumnModel().getColumn(columnIndex)
                 .setCellRenderer(new TooltipEllipsisRenderer());
        }
        return maxWidth;
    }


    /**
     * {@code TooltipCellRenderer} je renderer koji omogućava centriranje teksta,
     * bojenje selektovanog reda, alternativno bojenje pozadine redova i tooltippove
     * za sve ćelije.
     */
    private class TooltipCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            JLabel c = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            if (isSelected) {
                c.setBackground(table.getSelectionBackground());
                c.setForeground(table.getSelectionForeground());
            } else {
                c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(245, 245, 245));
                c.setForeground(Color.BLACK);
            }

            c.setHorizontalAlignment(SwingConstants.CENTER);
            c.setToolTipText(value != null ? value.toString() : null);

            c.setOpaque(true);

            return c;
        }
    }



    /**
     * {@code TooltipEllipsisRenderer} prikazuje skraćeni tekst sa "..." ukoliko
     * tekst ne stane u kolonu, dok puni tekst ostaje dostupan preko tooltippa.
     */
    private class TooltipEllipsisRenderer extends TooltipCellRenderer {
        @Override
        protected void setValue(Object value) {
            if (value != null) {
                String text = value.toString();
                setToolTipText(text);
                FontMetrics fm = getFontMetrics(getFont());
                int availableWidth = getWidth() - 6;
                if (fm.stringWidth(text) > availableWidth && availableWidth > 0) {
                    while (fm.stringWidth(text + "...") > availableWidth && text.length() > 0) {
                        text = text.substring(0, text.length() - 1);
                    }
                    super.setValue(text + "...");
                } else {
                    super.setValue(text);
                }
            } else {
                setToolTipText(null);
                super.setValue("");
            }
        }
    }


    /**
     * {@code RowNumberTableModel} je model za JTable koji prikazuje brojeve redova
     * sa lijeve strane tabele.
     */
    private class RowNumberTableModel extends AbstractTableModel {
        @Override
        public int getRowCount() { return tableModel.getRowCount(); }
        @Override
        public int getColumnCount() { return 1; }
        @Override
        public Object getValueAt(int rowIndex, int columnIndex) { return rowIndex + 1; }
        @Override
        public String getColumnName(int column) { return "#"; }
    }

    public JTable getTable() { return table; }
    public CustomTableModel getTableModel() { return tableModel; }
    public TreeElement.Table getTableMeta() { return tableMeta; }
    
    private FormPanel formPanel;
    
    public void setFormPanel(FormPanel formPanel) {
		this.formPanel = formPanel;
	}

    public FormPanel getFormPanel() {
        return formPanel;
    }
    
    public boolean isFullAccess() {
 		return fullAccess;
 	}

 	public void setFullAccess(boolean fullAccess) {
 		this.fullAccess = fullAccess;
 	}

}
