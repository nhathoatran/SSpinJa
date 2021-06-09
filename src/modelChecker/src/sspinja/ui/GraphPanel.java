package sspinja.ui;


import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.JPanel;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Element;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.stream.file.FileSinkImages;
import org.graphstream.stream.file.images.Resolutions;
import org.graphstream.ui.geom.Point3;
import org.graphstream.ui.layout.HierarchicalLayout;
import org.graphstream.ui.swing.util.SwingFileSinkImages;
import org.graphstream.ui.swing_viewer.SwingViewer;
import org.graphstream.ui.view.Viewer;
import org.graphstream.ui.view.camera.Camera;


public class GraphPanel extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2945963531877297003L;
	private final MultiGraph graph ;
	private final JPanel viewPanel;
	public SwingViewer viewer;
    public static ArrayList<String> actionList ;
    public static HashMap<Integer,Edge> mapIdEdge ;
    
	String style = 
			"graph {" 
				+ "fill-mode: plain;"
	            + "}" +	
		            
			"node {" 
				+ "shape: rounded-box; "
				+ "fill-color: white;" 
				+ "stroke-mode: plain;" 
				+ "padding: 10px; "
				+ "size-mode: fit; "
				+ "}" +
			
			"node.head {" 
				+ "shape: circle; "
				+ "fill-color: gray;" 
				+ "stroke-mode: plain;" 
				+ "size-mode: fit; "
				+ "}" +

			"node.end {" 
				+ "shape: circle; "
				+ "fill-color: lightgray;" 
				+ "stroke-mode: plain;" 
				+ "size-mode: fit; "
				+ "}" +
				
//	        "node.unmarked {" 
//	            + "fill-color: white;" 
//	            + "}" +
//	                      
//	        "node.current {" 
//	            + "fill-color: yellow;" 
//	            + "}" +
//	            
//	        "node.visited {" 
//	            + "fill-color: purple;" 
//	            + "}" +
//	            
//	        "node.marked {" 
//	            + "fill-color: red;"
//	            + "}" +
		
	        "edge { "
	            + "	shape: line;"
	    		+ "	fill-color: black;"
	    		+ "	fill-mode: plain;"
//	    		+ " text-color: blue;"
	    		+ "	shadow-mode: none;"
	    		+ "	shadow-color: rgba(0,0,0,100);"
	    		+ "	shadow-offset: 3px, -3px;"
	    		+ "	shadow-width: 0px;"
	    		+ " text-offset: -5, -10;"
	    		+ "	arrow-shape: arrow;"
	    		+ "	arrow-size: 6px, 3px;"
	            + "}" +
	    	
			"edge.head {" 
				+ "	fill-color: lightgray;"
				+ " text-offset: 5, 10;"
				+ "}" +

			"edge.back {" 
				+ "	fill-color: black;"	    		
				+ " text-offset: 5, 10;"
				+ "}" +
			
			"edge.visited {" 
				+ " fill-color: red;" 
				+ " text-offset: -5, -10;"
				+ "}" +
			
			"edge.backvisited {" 
				+ "	fill-color: red;" 
				+ " text-offset: 5, 10;"
				+ "}" +

			"edge.undovisited {" 
				+ " fill-color: blue;" 
				+ " text-offset: -5, -10;"
				+ "}" +
			
			"edge.undobackvisited {" 
				+ "	fill-color: blue;"
				+ " text-offset: 5, 10;"
				+ "}"
				; 


		
		
	        
	public GraphPanel() {
		actionList = new ArrayList<String>();
	    mapIdEdge =new HashMap<>();
        System.setProperty("org.graphstream.ui", "swing");        
        graph  = new MultiGraph( "g1" );
        graph.setAttribute("ui.quality");
        graph.setAttribute("ui.antialias");
       
        graph.setAttribute("ui.stylesheet", style);
        viewer=new SwingViewer(graph, Viewer.ThreadingModel.GRAPH_IN_GUI_THREAD);
        viewer.enableAutoLayout();
           
        viewPanel = (JPanel) viewer.addDefaultView(false);
        viewPanel.setBackground(Color.LIGHT_GRAY);
        
        Camera camera = viewer.getDefaultView().getCamera();
        viewPanel.removeMouseListener(viewPanel.getMouseListeners()[0]);
        viewPanel.addMouseMotionListener(new MouseMotionListener() {

            private int preX = -1;
            private int preY = -1;

            @Override
            public void mouseDragged(MouseEvent mouseEvent) {
                int currentX = mouseEvent.getX();
                int currentY = mouseEvent.getY();

                camera.setAutoFitView(false);
                Point3 pointView = camera.getViewCenter();

                if (preX != -1 && preY != -1) {
                    if (preX < currentX) {
                        pointView.x -= 0.1;
                    }
                    else if (preX > currentX) {
                        pointView.x += 0.1;
                    }

                    if (preY < currentY) {
                        pointView.y += 0.1;
                    }
                    else if (preY > currentY) {
                        pointView.y -= 0.1;
                    }
                }
                camera.setViewCenter(pointView.x, pointView.y, pointView.z);

                preX = currentX;
                preY = currentY;
            }

			@Override
			public void mouseMoved(MouseEvent e) {
				// TODO Auto-generated method stub							
			}            
        });
        viewPanel.addMouseWheelListener(mouseWheelEvent -> {
            if (mouseWheelEvent.getWheelRotation() < 0) {
                camera.setViewPercent(camera.getViewPercent() * 0.95);
            }
            else {
                camera.setViewPercent(camera.getViewPercent() * 1.05);
            }
        });
        
        
        InitData();
    }  
	
	ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
	
	
	
	
	
	private void InitData() {
		//display ok
//		Read from file
		Node n1;
		Edge e1;
		
		File userDir = new File(System.getProperty("user.dir"));
		String sDir = userDir.getAbsolutePath();
		String path = sDir  + "/src/sspinja/system.pml.graph";
		String[] parts;		
    	BufferedReader bufReader;
        try {
            //read per line
            bufReader = new BufferedReader(new FileReader(path));
            String line = "";
            
            boolean startedHead = false;
            Node head = null;
            String processName = "";
            String nodeName = "";
            String nodeFrom = "";
            String nodeTo = "";
            
            while (line != null) {
        		line = bufReader.readLine();
        		if (line != null) {
	        		parts = line.split("->");
	        		if (parts.length == 1){
	        			processName = line;
	        			
	        			actionList.add("Process: " + processName);
	        			head = graph.addNode(processName); //head of process
	        			
	        			head.setAttribute("label", processName);
	        			head.setAttribute("ui.class", "head");
	        			startedHead = true;
	        		} else { //4 parts
	        			//add Node from
	        			nodeName = parts[0];
        				nodeFrom = nodeName.replace(processName+"_", "");
	        			if (graph.getNode(parts[0]) == null) {	        				
		        			n1 = graph.addNode(parts[0]);
		        	        n1.setAttribute("label", nodeFrom);
	        			}
	        			
	        			//add Node to
	        			nodeName = parts[1];
        				nodeTo = nodeName.replace(processName+"_", "");
	        			if (graph.getNode(parts[1]) == null) {	        				
		        	        n1 = graph.addNode(parts[1]);
		        	        n1.setAttribute("label", nodeTo);
		        	        if (nodeTo.equals("end")) {
		        	        	n1.setAttribute("ui.class", "end");
		        	        }
	        			}
	        	      	
	        			if (graph.getEdge(parts[2]) == null) {
		        	        e1 = graph.addEdge(parts[2],parts[0],parts[1],true);
		        	        mapIdEdge.put(Integer.parseInt(parts[2]), e1);
		        	        
		        	        actionList.add("Edge " + parts[2] + ": "  + parts[3] );
		        	        e1.setAttribute("ui.label", parts[2]);
		        	        
		        	        if (!nodeTo.equals("end")) {
			        	        if (Integer.parseInt(nodeFrom) > Integer.parseInt(nodeTo)){
			        	        	e1.setAttribute("ui.class", "back");
			        	        }
		        	        }
		        	        	
	        			}
	        	        
	        			if (startedHead) { //setup the head of a process
	        				if (graph.getEdge(processName) == null) {
	        					
			        	        e1 = graph.addEdge(processName,processName, parts[0],true);		
			        	        e1.setAttribute("ui.class", "head");
		        			}
	        				head = null;
	        				startedHead = false;
	        			}
		        		
	        		} 
	        			
        		}
        	} 
            bufReader.close();
        } catch (IOException e) {
        	//graph file not found or error
        } finally {
        	
        }
	}
	
	
	public void repaintView(){
		viewPanel.repaint();
	}
	public MultiGraph getGraph() {
        return graph;
    }

    public JPanel getView() {
        return viewPanel;
    }

    public List<String> getAdjectiveNodes(String baseNode) {
        return graph
                .nodes()
                .map(Element::getId)
                .filter(node -> graph.getNode(baseNode).hasEdgeToward(node))
                .collect(Collectors.toList());
    }

    public void setNodeIsCurrent(String node) {
        graph.getNode(node).setAttribute("ui.class", "current");
    }

    public void setEdgeIsCurrent(String edge){
    	graph.getEdge(edge).setAttribute("ui.class", "current");
    }
    
    public void setEdgeIsVisited(String edge){

    	if (graph.getEdge(edge).getAttribute("ui.class") == null 
    			|| graph.getEdge(edge).getAttribute("ui.class") == "undovisited") {
    		graph.getEdge(edge).setAttribute("ui.class", "visited");
    	} else {
    		if (graph.getEdge(edge).getAttribute("ui.class") == "back"
    				|| graph.getEdge(edge).getAttribute("ui.class") == "undobackvisited")
    			graph.getEdge(edge).setAttribute("ui.class", "backvisited");
    	}	
    }
    
    public void setEdgeIsUndo(String edge){

    	if (graph.getEdge(edge).getAttribute("ui.class") == "visited") {
    		graph.getEdge(edge).setAttribute("ui.class", "undovisited");
    	} else {
    		if (graph.getEdge(edge).getAttribute("ui.class") == "backvisited")
    			graph.getEdge(edge).setAttribute("ui.class", "undobackvisited");
    	}	
    }
    
    public void removeColor() {
        graph.nodes().forEach(node -> node.setAttribute("ui.class", "unmarked"));
        graph.edges().forEach(edge -> edge.setAttribute("ui.class", "unmarked"));
    }

    public void saveGraphImage() {
        SwingFileSinkImages sfsi = new SwingFileSinkImages(FileSinkImages.OutputType.PNG, Resolutions.HD720);

        sfsi.setLayoutPolicy(FileSinkImages.LayoutPolicy.COMPUTED_FULLY_AT_NEW_IMAGE);
        try {
            sfsi.writeAll(graph, Instant.now().toString() + ".png");
        } catch (IOException e) {
            e.printStackTrace();
        }
   }
}