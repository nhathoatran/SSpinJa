package sspinja.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import spinja.model.Transition;

import sspinja.RunSchedulerSimulation;
import sspinja.SchedulerPanModel;
import sspinja.scheduler.promela.model.SchedulerPromelaModel;
import sspinja.scheduler.search.SchedulerSimulation;

public class MainUI extends javax.swing.JFrame {
	private static final long serialVersionUID = 1L;
	private GraphPanel graphPanel;
	private JScrollPane transitionPanel;
	private JScrollPane systemStatePanel;
	private JScrollPane tracePanel;
	
	private JPanel controlPanel;
	public ListAction lNextAction;
	
	public JButton btnGo;
	public JButton btnSimu;
	public JButton btnLoadingTrail;
	public JButton btnStop;
	
	public ListView lTraces ;
	private ListView lTransitions ;
	public ListView lSystemStates;
	@SuppressWarnings("rawtypes")
	SchedulerSimulation schSimu;
	boolean ready;
	
	/**
	 * Launch the application.
	 */
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainUI mainUI = new MainUI();
					mainUI.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainUI() {
		execute();
		initialize(); //set up the UI
		schSimu.execute(); //execute the simulation		
	}
	public RunSchedulerSimulation run;
	
	public void execute(){ //init the SchedulerSimulation
		run = new RunSchedulerSimulation();
		run.parseArguments(new String[0],"SchedulerPan");
		if (SchedulerPanModel.scheduler.InitSchedulerObject(run.scheduleropt.getOption()))
		schSimu = run.search(SchedulerPanModel.class, this);
		schSimu.setMainUI(this);
	}
	

	/**
	 * Initialize the contents of the frame.
	 */

	private void initFrame(){
		//the frame
		setTitle("SSpinJa Simulation: " + SchedulerPromelaModel.scheduler.getSchedulingPolicy());
		setPreferredSize(new Dimension(1100, 800));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout(0, 0));
	}
	

	private void setupTransitionPanel(){
		//process action
		transitionPanel = new JScrollPane();
		transitionPanel.setBorder(new TitledBorder(null, "Transitions", TitledBorder.CENTER, TitledBorder.TOP, null, null));

		transitionPanel.setPreferredSize(new Dimension(200, 700));

		lTransitions = new ListView(false, null, false);//selected color
		lTransitions.setTextColor(Color.BLACK);
		transitionPanel.setViewportView(lTransitions.listLog);
	}
	
	public void setLastActionEdge(String edgeID){
		graphPanel.setEdgeIsVisited(edgeID);
	}
	
	public void setGoBackLastActionEdge(String edgeID){
		graphPanel.setEdgeIsUndo(edgeID);
	}
	
	private void setupGraphPanel(){
		//process graph
		graphPanel = new GraphPanel();
	}
	
	private void setupTraceListSelection(){
	    ListSelectionListener listSelectionListener = new ListSelectionListener() {
	        public void valueChanged(ListSelectionEvent listSelectionEvent) {
	          boolean adjust = listSelectionEvent.getValueIsAdjusting();
	          if (!adjust) {
	            JList<String> list = (JList<String>) listSelectionEvent.getSource();
	            int index = list.getSelectedIndex();
	            gotoSystemState(index);
	          }
	        }
	      };
	      lTraces.getList().addListSelectionListener(listSelectionListener);
	}
	
	private void setupSystemStateListSelection(){
	    ListSelectionListener listSelectionListener = new ListSelectionListener() {
	        public void valueChanged(ListSelectionEvent listSelectionEvent) {
	          boolean adjust = listSelectionEvent.getValueIsAdjusting();
	          if (!adjust) {
	            JList<String> list = (JList<String>) listSelectionEvent.getSource();
	            int index = list.getSelectedIndex();
	            gotoTrace(index);
	          }
	        }
	      };
	      lSystemStates.getList().addListSelectionListener(listSelectionListener);
	}
	
	public void gotoSystemState(int index) {
		lSystemStates.listLog.setSelectedIndex(index);
		lSystemStates.listLog.ensureIndexIsVisible(index);
    }
	
	public void gotoTrace(int index) {
		lTraces.listLog.setSelectedIndex(index);
		lTraces.listLog.ensureIndexIsVisible(index);
    }
	
	private void setupSystemStatePanel(){
		systemStatePanel = new JScrollPane();
		systemStatePanel.setEnabled(true);
		systemStatePanel.setPreferredSize(new Dimension(80, 200));
		systemStatePanel.setBorder(new TitledBorder(null, "System states (before taking an action)", TitledBorder.CENTER, TitledBorder.TOP, null, null));

		lSystemStates = new ListView(false, Color.CYAN, true); //system state
		lSystemStates.setTextColor(Color.BLACK);
		lSystemStates.setSelectionModel(0);
		systemStatePanel.setViewportView(lSystemStates.listLog);
	}
	

	private void setupTracePanel(){
		tracePanel = new JScrollPane();
		tracePanel.setEnabled(false);
		tracePanel.setPreferredSize(new Dimension(0, 550));
		tracePanel.setBorder(new TitledBorder(null, "Traces", TitledBorder.CENTER, TitledBorder.TOP, null, null));

		lTraces = new ListView(false, null, false); //transition
		lTraces.setTextColor(Color.BLACK);
		lTraces.setSelectionModel(0);
		tracePanel.setViewportView(lTraces.listLog);
	}
	
	private void updateList(String item) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
            	lTraces.addItem(item);
            	
            	if (schSimu.isSimulate) {
            		btnSimu.doClick();
            	}
            }
        });
    }
	
	private void setupControlPanel(){
		JFrame mainFrame = this;
		controlPanel = new JPanel();
		controlPanel.setPreferredSize(new Dimension(200, 210));
		controlPanel.setBorder(new TitledBorder(null, "Simulation", TitledBorder.CENTER, TitledBorder.TOP, null, null));
		
		GridBagLayout layout = new GridBagLayout();
		controlPanel.setLayout(layout);
        GridBagConstraints gbc = new GridBagConstraints();
		
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
		btnSimu = new JButton("Next multiple selections...");
		try {
			BufferedImage image = ImageIO.read(ResourceLoader.load("gonext.gif"));
			btnSimu.setIcon(new ImageIcon(image));
//			btnSimu.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/icons/gonext.gif"))));
//			btnSimu.setIcon(new ImageIcon(Class.class.getResource("/icons//img/gonext.gif")));
        } catch (Exception ex) {
        }
		btnSimu.setForeground(Color.BLACK);
		btnSimu.setHorizontalAlignment(SwingConstants.CENTER);
		btnSimu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				schSimu.setSimulate();
				schSimu.goToNextSelection();
			}
		});		
		
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        controlPanel.add(btnSimu, gbc);
        
        btnLoadingTrail = new JButton("Loading...");
        btnLoadingTrail.setForeground(Color.BLACK);
        btnLoadingTrail.setHorizontalAlignment(SwingConstants.CENTER);
        btnLoadingTrail.setVisible(false);
        
        btnLoadingTrail.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				schSimu.setTrailSimulate();
				schSimu.goToNextTrailSelection();
			}
		});		
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        controlPanel.add(btnLoadingTrail, gbc);
        
        
        btnStop = new JButton("Stop");
        try {
        	BufferedImage image = ImageIO.read(ResourceLoader.load("stop.gif"));
        	btnStop.setIcon(new ImageIcon(image));
//        	btnStop.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/icons/stop.gif"))));
        } catch (Exception ex) {
        }
        
        btnStop.setForeground(Color.BLACK);
        btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				schSimu.isStop = true;
			}
		});
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        controlPanel.add(btnStop, gbc);
        
   
		lNextAction = new ListAction();
		lNextAction.getListActionComboBox().setForeground(Color.BLACK);
		lNextAction.getListActionComboBox().setPrototypeDisplayValue("XXXXXXXXXXXXXXXXXXXX");
		lNextAction.getListActionComboBox().setMaximumSize( lNextAction.getListActionComboBox().getPreferredSize() );
		controlPanel.add(lNextAction.getListActionComboBox());
		gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        controlPanel.add(lNextAction.getListActionComboBox(), gbc);
		
		
		btnGo = new JButton("Go");
		try {
			BufferedImage image = ImageIO.read(ResourceLoader.load("go.gif"));
			btnGo.setIcon(new ImageIcon(image));
//			btnGo.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/icons/go.gif"))));
        } catch (Exception ex) {
        }
		btnGo.setForeground(Color.BLACK);
		btnGo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (lNextAction.size() != 0) {
					if (lNextAction.getSelectedAction().contains("Select")) {
						//select process to run
						schSimu.takeProcessAndDisplayNextActions(lNextAction.getSelectedItem());
					} else {
						//select action to take
						schSimu.takeActionAndDisplayNextActions(lNextAction.getSelectedItem());
					}
				}
			}
		});
		gbc.gridx = 1;
        gbc.gridy = 1;
		controlPanel.add(btnGo,gbc);

		JButton btnUndoTrail = new JButton("Go back");
		try {
			BufferedImage image = ImageIO.read(ResourceLoader.load("undo.gif"));
			btnUndoTrail.setIcon(new ImageIcon(image));
//			btnUndoTrail.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/icons/undo.gif"))));
        } catch (Exception ex) {
        }
		btnUndoTrail.setForeground(Color.BLACK);
		btnUndoTrail.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {          
				schSimu.isStop = true;
				Transition t = schSimu.callUndoTransition();
				
				lTraces.removeLastItem();
				lSystemStates.removeLastItem();
				if (SchedulerPanModel.scheduler.running_process != null) { //go back action
					//display set actions from current process (running)
					schSimu.displaySetActions(false);
					if (t != null && !t.toString().contains("Select"))
						setGoBackLastActionEdge(t.getId() + "");
				} else { //go back select process
					//select process to run
					schSimu.displaySetProcesses();
				}
			}
		});
		gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
		controlPanel.add(btnUndoTrail,gbc);
		
		
		
		JButton btnExportTrailtext = new JButton("Save trail");
		try {
			BufferedImage image = ImageIO.read(ResourceLoader.load("save.gif"));
			btnExportTrailtext.setIcon(new ImageIcon(image));
//			btnExportTrailtext.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/icons/save.gif"))));
        } catch (Exception ex) {
        }
		btnExportTrailtext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {          
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setDialogTitle("Specify a file to save");  
				File workingDirectory = new File(System.getProperty("user.dir"));
				fileChooser.setCurrentDirectory(workingDirectory);
				
				fileChooser.setAcceptAllFileFilterUsed(false);
		        FileNameExtensionFilter filter = new FileNameExtensionFilter("(trail files *.trail)", "trail");
		        fileChooser.addChoosableFileFilter(filter);
		         
				int userSelection = fileChooser.showSaveDialog(mainFrame);
				 
				if (userSelection == JFileChooser.APPROVE_OPTION) {
				    File fileToSave = fileChooser.getSelectedFile();
				    schSimu.writeTrailFile(fileToSave);				    
				}
			}
		});
		gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
		controlPanel.add(btnExportTrailtext,gbc);
		
		JButton btnLoadTrail = new JButton("Load trail");
		try {
			BufferedImage image = ImageIO.read(ResourceLoader.load("load.gif"));
			btnLoadTrail.setIcon(new ImageIcon(image));
//			btnLoadTrail.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/icons/load.gif"))));
        } catch (Exception ex) {
        }
		btnLoadTrail.setHorizontalAlignment(SwingConstants.CENTER);
		btnLoadTrail.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {  
				if (lTraces.getLogCount() == 0) {
					JFileChooser fileChooser = new JFileChooser();
					fileChooser.setDialogTitle("Specify a file to open");  
					File workingDirectory = new File(System.getProperty("user.dir"));
					fileChooser.setCurrentDirectory(workingDirectory);
					
					fileChooser.setAcceptAllFileFilterUsed(false);
			        FileNameExtensionFilter filter = new FileNameExtensionFilter("(trail files *.trail)", "trail");
			        fileChooser.addChoosableFileFilter(filter);
			         
					int userSelection = fileChooser.showOpenDialog(mainFrame);
					 
					if (userSelection == JFileChooser.APPROVE_OPTION) {
					    File fileToOpen = fileChooser.getSelectedFile();
					    schSimu.readTrailFile(fileToOpen);	
					    schSimu.setTrailSimulate();
					    
						System.out.println("Start trail simulate!");
						schSimu.goToNextTrailSelection();
					}
				} else {
					JOptionPane.showMessageDialog(null, 
							"Can not load trail during simulating!", 
	                        "ERROR", 
	                        JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
		controlPanel.add(btnLoadTrail,gbc);
		
		JButton btnExportpng = new JButton("Take a screenshot");
		try {
			BufferedImage image = ImageIO.read(ResourceLoader.load("screenshot.gif"));
			btnExportpng.setIcon(new ImageIcon(image));
//			btnExportpng.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/icons/screenshot.gif"))));
        } catch (Exception ex) {
        }
		btnExportpng.setHorizontalAlignment(SwingConstants.CENTER);
		btnExportpng.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveGraphImage();
			}
		});
		controlPanel.add(btnExportpng);
		gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
		controlPanel.add(btnExportpng,gbc);

		
		JButton btnClose = new JButton("Close");
		try {
			BufferedImage image = ImageIO.read(ResourceLoader.load("close.gif"));
			btnClose.setIcon(new ImageIcon(image));
//			btnClose.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("close.gif"))));
        } catch (Exception ex) {
        }
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		btnClose.setForeground(Color.BLACK);
		

		gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.gridwidth = 1;
		controlPanel.add(btnClose,gbc);
	}
	
	public void saveImage(String name,String type) {
		BufferedImage image = new BufferedImage(getWidth(),getHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics2D g2 = image.createGraphics();
		printAll(g2);
		try{
			ImageIO.write(image, type, new File(name+"."+type));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void saveGraphImage() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Specify a file to save");  
		File workingDirectory = new File(System.getProperty("user.dir"));
		fileChooser.setCurrentDirectory(workingDirectory);
		
		fileChooser.setAcceptAllFileFilterUsed(false);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("(png files *.png)", "png");
        fileChooser.addChoosableFileFilter(filter);
         
        int userSelection = fileChooser.showSaveDialog(this);
		 
		if (userSelection == JFileChooser.APPROVE_OPTION) {
			File fileToSave = fileChooser.getSelectedFile();
			saveImage(fileToSave.getAbsolutePath(),"png");
	        
		}
   }
	
	public void setNextSelections(ArrayList<String> lAct){
		lNextAction.addListAction(lAct);
	}
	
	private void setupData(){
		int i = 0;
		for (String st : GraphPanel.actionList) {
			if (st.contains("Edge")) {
				lTransitions.addItem("\t-" + st);
			} else {
				lTransitions.addItem(st);
			}
		}
	}
	
	
	private void initialize() {
		initFrame();
		setupTransitionPanel();
		setupGraphPanel();
		setupSystemStatePanel();
		setupTracePanel();
		setupControlPanel();
		
		
		JPanel lPanel = new JPanel();
		lPanel.setLayout(new BorderLayout());
		lPanel.add(transitionPanel, BorderLayout.WEST);
		lPanel.add(graphPanel.getView(), BorderLayout.CENTER);
		
		lPanel.setPreferredSize(new Dimension(800, 550)); 
		systemStatePanel.setPreferredSize(new Dimension(200, 100));
		JSplitPane splitLPane = new JSplitPane();
		splitLPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitLPane.setLeftComponent(lPanel); 
		splitLPane.setRightComponent(systemStatePanel);
		
		JPanel rPanel = new JPanel();
		
		rPanel.setLayout(new BorderLayout());	
		rPanel.add(tracePanel, BorderLayout.CENTER);
		rPanel.add(controlPanel, BorderLayout.SOUTH);
		rPanel.setPreferredSize(new Dimension(0, 80));
		
		JSplitPane windowSplitPane = new JSplitPane();
		windowSplitPane.setLeftComponent(splitLPane);
		windowSplitPane.setRightComponent(rPanel);
		
		getContentPane().add(windowSplitPane, BorderLayout.CENTER);
		
		setupTraceListSelection();
		setupSystemStateListSelection();
		setupData();
		pack();
	}
}
