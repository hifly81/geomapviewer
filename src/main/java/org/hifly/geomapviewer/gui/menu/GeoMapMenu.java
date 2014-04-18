package org.hifly.geomapviewer.gui.menu;

import org.hifly.geomapviewer.domain.gps.SlopeSegment;
import org.hifly.geomapviewer.graph.WaypointElevationGraph;
import org.hifly.geomapviewer.graph.WaypointGraph;
import org.hifly.geomapviewer.gui.dialog.GraphViewer;
import org.hifly.geomapviewer.storage.GeoMapStorage;
import org.hifly.geomapviewer.utility.SlopeUtility;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author
 * @date 11/02/14
 */
public class GeoMapMenu extends JMenuBar {

    private JMenuItem itemImportFile;
    private JMenuItem itemImportFolder;
    private JMenuItem itemOptionsSetting;
    private JFrame currentFrame;

    public GeoMapMenu(JFrame currentFrame) {
        super();
        this.currentFrame = currentFrame;
        this.add(createFileMenu());
        this.add(createSettingMenu());
        this.add(createLibraryMenu());
        URL helpImageUrl = getClass().getResource("/img/help.png");
        ImageIcon helpImageIcon = new ImageIcon(helpImageUrl);
        Image img = helpImageIcon.getImage();
        img = img.getScaledInstance(16, 16,  java.awt.Image.SCALE_SMOOTH);
        helpImageIcon = new ImageIcon(img);
        JMenu help = new JMenu("Help");
        help.setIcon(helpImageIcon);
        this.add(new JMenu("Help"));
    }

    private JMenu createFileMenu() {
        JMenu file = new JMenu("File");
        file.setMnemonic(KeyEvent.VK_F);

        URL importImageUrl = getClass().getResource("/img/import.png");
        ImageIcon importImageIcon = new ImageIcon(importImageUrl);
        Image img = importImageIcon.getImage();
        img = img.getScaledInstance(16, 16,  java.awt.Image.SCALE_SMOOTH);
        importImageIcon = new ImageIcon(img);
        JMenu importMenu = new JMenu("Import");
        importMenu.setIcon(importImageIcon);
        importMenu.setMnemonic(KeyEvent.VK_I);

        URL fileImageUrl = getClass().getResource("/img/file.png");
        ImageIcon fileImageIcon = new ImageIcon(fileImageUrl);
        img = fileImageIcon.getImage();
        img = img.getScaledInstance(16, 16,  java.awt.Image.SCALE_SMOOTH);
        fileImageIcon = new ImageIcon(img);
        JMenuItem importFile = new JMenuItem("Import from file...",fileImageIcon);
        importFile.setMnemonic(KeyEvent.VK_F);
        importFile.setToolTipText("Import from file...");

        URL folderImageUrl = getClass().getResource("/img/folder.png");
        ImageIcon folderImageIcon = new ImageIcon(folderImageUrl);
        img = folderImageIcon.getImage();
        img = img.getScaledInstance(16, 16,  java.awt.Image.SCALE_SMOOTH);
        folderImageIcon = new ImageIcon(img);
        JMenuItem importFolder = new JMenuItem("Import from folder...",folderImageIcon);

        importMenu.add(importFile);
        importMenu.add(importFolder);

        URL exitImageUrl = getClass().getResource("/img/quit.png");
        ImageIcon exitImageIcon = new ImageIcon(exitImageUrl);
        img = exitImageIcon.getImage();
        img = img.getScaledInstance(16, 16,  java.awt.Image.SCALE_SMOOTH);
        exitImageIcon = new ImageIcon(img);
        JMenuItem exit = new JMenuItem("Exit",exitImageIcon);
        exit.setMnemonic(KeyEvent.VK_C);
        exit.setToolTipText("Exit application");
        exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W,ActionEvent.CTRL_MASK));

        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                System.exit(0);
            }
        });

        file.add(importMenu);
        file.addSeparator();
        file.add(exit);

        this.itemImportFile = importFile;
        this.itemImportFolder = importFolder;

        return file;
    }

    private JMenu createSettingMenu() {
        JMenu setting = new JMenu("Tool");
        setting.setMnemonic(KeyEvent.VK_S);

        URL optionImageUrl = getClass().getResource("/img/option.png");
        ImageIcon optionImageIcon = new ImageIcon(optionImageUrl);
        Image img = optionImageIcon.getImage();
        img = img.getScaledInstance(16, 16,  java.awt.Image.SCALE_SMOOTH);
        optionImageIcon = new ImageIcon(img);
        JMenuItem optionsItem = new JMenuItem("Options",optionImageIcon);
        optionsItem.setMnemonic(KeyEvent.VK_P);
        optionsItem.setToolTipText("Setting options");

        setting.add(optionsItem);

        this.itemOptionsSetting = optionsItem;

        return setting;
    }

    private JMenu createLibraryMenu() {
        //list of saved climbs
        Map<String,List<SlopeSegment>> savedClimbs = SlopeUtility.organizeSlopesBySteepness(GeoMapStorage.savedClimbsList);

        JMenu library = new JMenu("Library");
        library.setMnemonic(KeyEvent.VK_L);

        JMenu climbs = new JMenu("Climbs");
        URL mountainImageUrl = getClass().getResource("/img/mountain.png");
        ImageIcon mountainImageIcon = new ImageIcon(mountainImageUrl);
        Image img = mountainImageIcon.getImage();
        img = img.getScaledInstance(16, 16,  java.awt.Image.SCALE_SMOOTH);
        mountainImageIcon = new ImageIcon(img);
        climbs.setIcon(mountainImageIcon);
        climbs.setMnemonic(KeyEvent.VK_C);
        climbs.setToolTipText("Climbs");

        JMenu climbsItem1 = new JMenu("0% - 4%");
        climbsItem1.setToolTipText("0% - 4%");
        addSavedClimbs(savedClimbs, climbsItem1);

        JMenu climbsItem2 = new JMenu("4% - 8%");
        climbsItem2.setToolTipText("4% - 8%");
        addSavedClimbs(savedClimbs, climbsItem2);

        JMenu climbsItem3 = new JMenu("8% - 10%");
        climbsItem3.setToolTipText("8% - 10%");
        addSavedClimbs(savedClimbs, climbsItem3);

        JMenu climbsItem4 = new JMenu("10% - 15%");
        climbsItem4.setToolTipText("10% - 15%");
        addSavedClimbs(savedClimbs, climbsItem4);

        JMenu climbsItem5 = new JMenu(">15%");
        climbsItem5.setToolTipText(">15%");
        addSavedClimbs(savedClimbs, climbsItem5);

        climbs.add(climbsItem1);
        climbs.add(climbsItem2);
        climbs.add(climbsItem3);
        climbs.add(climbsItem4);
        climbs.add(climbsItem5);

        library.add(climbs);


        return library;
    }

    private void addSavedClimbs(Map<String, List<SlopeSegment>> savedClimbs, JMenu climbsItem) {
        if(savedClimbs!=null) {
        List<SlopeSegment> slopes = savedClimbs.get(climbsItem.getText());
        if(slopes!=null && !slopes.isEmpty()) {
            for(final SlopeSegment slope:slopes) {
                JMenuItem temp = new JMenuItem(slope.getName());
                temp.setToolTipText(slope.getName());
                climbsItem.add(temp);
                temp.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent event) {
                        //open graph
                        WaypointGraph waypointElevationGraph =
                                new WaypointElevationGraph(slope.getWaypoints(),true,false);
                        GraphViewer graphViewer =
                                new GraphViewer(currentFrame,
                                        Arrays.asList(waypointElevationGraph));
                    }
                });
            }
        }
        }
    }

    public JMenuItem getItemImportFile() {
        return itemImportFile;
    }

    public void setItemImportFile(JMenuItem itemImportFile) {
        this.itemImportFile = itemImportFile;
    }

    public JMenuItem getItemImportFolder() {
        return itemImportFolder;
    }

    public void setItemImportFolder(JMenuItem itemImportFolder) {
        this.itemImportFolder = itemImportFolder;
    }

    public JMenuItem getItemOptionsSetting() {
        return itemOptionsSetting;
    }

    public void setItemOptionsSetting(JMenuItem itemOptionsSetting) {
        this.itemOptionsSetting = itemOptionsSetting;
    }
}
