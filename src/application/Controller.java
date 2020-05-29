package application;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;

import java.util.ArrayList;
import java.io.File;

public class Controller {

	/* association avec le contrôleur de toutes les interfaces du FXML
	 * 
	 */
    
    @FXML
    private AnchorPane anchor; // fenetre principale 
    
	@FXML
	private AnchorPane canvas; // zone de dessin . J'avais commence avec un Canvas mais je n'ai pas trouve le moyen de récupérer en "shape" les figures réalisees pour les sauvegarder.
	@FXML
	private RadioButton selmove;
	@FXML
	private Button delete;
	@FXML
	private Button clone;
	@FXML
	private ColorPicker couleur;
	@FXML
	private RadioButton rectangle;
	@FXML
	private RadioButton ellipse;
	@FXML
	private RadioButton line;
	
	/* ici on stocke les 3 types d'objets  que l'on peut dessiner dans 3 listes respectives, cela permettra par la suite de les déplacer, les supprimer et les cloner.
	 * Je n'ai pas trouve d'heritage permettant de stocker les 3 types ensembles. 
	 */
	ArrayList<Rectangle> listeRectangle;
	ArrayList<Ellipse> listeEllipse;
	ArrayList<Line> listeLine;
	boolean newDraw; // newDraw permet de signaler par la suite la création d'une nouvelle figure
	int posX; // PosX et PosY correspondent aux coordonnees X Y du pointeur de la souris au debut de la creation de la figure
	int posY;
	
    public Controller() {
    	super();
    	listeRectangle = new ArrayList<Rectangle>();
    	listeEllipse = new ArrayList<Ellipse>();
    	listeLine = new ArrayList<Line>();
    	newDraw=false; 
    }
    
    @FXML
    public void initialize() {
    	
    	anchor.setOnMouseClicked(eventglob -> { // anchor ici est notre fênetre principal , la gestion de cet evenement global permet de changer passer entre les différents cas : dessin, selection.
    		canvas.setOnMouseDragged(event -> {
                if(rectangle.isSelected()) {  // creation du rectangle
                    if(!newDraw) { // cas d'un nouveau dessin
                    	newDraw = true;
                        posX = (int)event.getSceneX()-200; // le -200 permet de centrer la figure dessinée étant donné le positionement de notre zone de dessin à droite
                        posY = (int)event.getSceneY();
                        Rectangle r = new Rectangle();
                        r.setX(posX);
                        r.setY(posY);
                        r.setFill(couleur.getValue()); // récupération de la couleur du color Picker
                        canvas.getChildren().add(r); // ajout de la figure à la zone de dessin
                        listeRectangle.add(r); // ajout de la figure à la liste
                    }else {
                        if(event.getSceneX()-200<420) {
                        	listeRectangle.get(listeRectangle.size()-1).setWidth(Math.abs((int)event.getSceneX()-200-posX));
                        }
                        if(event.getSceneY()<600) {
                        	listeRectangle.get(listeRectangle.size()-1).setHeight(Math.abs((int)event.getSceneY()-posY));
                        }
                    }
                    canvas.setOnMouseReleased(eventEnd -> {
                    	newDraw = false; // fin du dessin
                    });
                }
                
                if(line.isSelected()) { // idem pour la ligne, basee sur les pos de x1 x2 y1 y2
                    if(!newDraw) {
                    	newDraw = true;
                        posX = (int)event.getSceneX();
                        posY = (int)event.getSceneY();
                        Line l = new Line();
                        l.setStartX(posX);
                        l.setStartY(posY);
                        l.setFill(couleur.getValue());
                        canvas.getChildren().add(l);
                        listeLine.add(l);
                    }else {
                        
                        	listeLine.get(listeLine.size()-1).setEndX(Math.abs((int)event.getSceneX()-posX)-200);   
                        	listeLine.get(listeLine.size()-1).setEndY(Math.abs((int)event.getSceneY()-posY));
                        
                    }
                    canvas.setOnMouseReleased(eventEnd -> {
                    	newDraw= false;
                    });
                }
                if(ellipse.isSelected()) { // idem pour l'ellipse, on a juste une dimension basee sur le rayon
                    if(!newDraw) {
                    	newDraw = true;
                        posX = (int)event.getSceneX()-200;
                        posY = (int)event.getSceneY();
                        Ellipse e = new Ellipse();
                        e.setCenterX(posX); 
                        e.setCenterY(posY);
                        e.setFill(couleur.getValue()); 
                        canvas.getChildren().add(e);
                        listeEllipse.add(e);
                    }else {
                        
                        	listeEllipse.get(listeEllipse.size()-1).setRadiusX(Math.abs((int)event.getSceneX()-200-posX));   
                        	listeEllipse.get(listeEllipse.size()-1).setRadiusY(Math.abs((int)event.getSceneY()-posY));
                        
                    }
                    canvas.setOnMouseReleased(eventEnd -> {
                    	newDraw = false;
                    });
                }
               
                
                
                
    		});
    		
    		///////////////////////////////// Select / move ///////////////////////// 
    			
    		
    		for(int i = 0; i<listeRectangle.size(); i++) { // recherche dans la liste de rectangle pour savoir lesquels sont selectionnes 
    			if(selmove.isSelected()) {
        		Rectangle shape = listeRectangle.get(i);
        		
        		shape.setOnMouseClicked(event2 -> {
        			
        			for(int j = 0; j<listeRectangle.size(); j++) {
                		couleur.setOnAction(eventy ->{
                			shape.setFill(couleur.getValue());
                		});
        			}
            		shape.setStroke(new Color(1,0,0,1)); // l'element est entoure en rouge
            		shape.setStrokeWidth(2);          // mise en evidence de l'element selectionne
            		
            	});
            		shape.setOnMouseDragged(event3 -> {
            			if(shape.getStrokeWidth()==2) {
            				
            				canvas.setLeftAnchor(shape,event3.getSceneX()-200); // redefinition des parametres de la fenetre pour deplacer l'objet
                    		canvas.setTopAnchor(shape,event3.getSceneY());
                    		
                    		
            			}
                		
            		});
            	} 
    		}
    		
    		
    		
    		for(int i = 0; i<listeLine.size(); i++) { // idem pour la ligne 
    			if(selmove.isSelected()) {
        		Line shape = listeLine.get(i);
        		
        		shape.setOnMouseClicked(event2 -> {
        			
        			for(int j = 0; j<listeLine.size(); j++) {
        				
                		couleur.setOnAction(eventy ->{
                			shape.setFill(couleur.getValue());
                		});
        			}
            		shape.setStroke(new Color(1,0,0,1));
            		shape.setStrokeWidth(2);
            		
            	});
            		shape.setOnMouseDragged(event3 -> {
            			if(shape.getStrokeWidth()==2) {
            				
            				canvas.setLeftAnchor(shape,event3.getSceneX()-200);
                    		canvas.setTopAnchor(shape,event3.getSceneY());
                    		
                    		
            			}
                		
            		});
            	} 
    		}
    		for(int i = 0; i<listeEllipse.size(); i++) { // idem pour l'ellipse 
    			if(selmove.isSelected()) {
        		Ellipse shape = listeEllipse.get(i);
        		
        		shape.setOnMouseClicked(event2 -> {
        			
        			for(int j = 0; j<listeEllipse.size(); j++) {
                		couleur.setOnAction(eventy ->{
                			shape.setFill(couleur.getValue());
                		});
        			}
            		shape.setStroke(new Color(1,0,0,1));
            		shape.setStrokeWidth(2);
            		
            	});
            		shape.setOnMouseDragged(event3 -> {
            			if(shape.getStrokeWidth()==2) {
            				
            				canvas.setLeftAnchor(shape,event3.getSceneX()-300);
                    		canvas.setTopAnchor(shape,event3.getSceneY());
                    		
                    		
            			}
                		
            		});
            	} 
    		}
    		
    		
    			
    		
    		
    		 
    		
            //////////////////////////////////////////////////// Clonage ////////////////////////////////////
    		
    		clone.setOnAction(event -> {
    			for(int j = 0; j<listeRectangle.size(); j++) { // recherche dans la liste de rectangle de l'element a cloner
    				Rectangle shape = listeRectangle.get(j);
    				if(shape.getStrokeWidth()==2) {   // condition de l'element en surbrillance
    					Rectangle r = new Rectangle(); // on recreer un rectangle avec les memes proprietes en le decalant legerement 
    					r.setWidth(shape.getWidth());
    					r.setHeight(shape.getHeight());
    					r.setFill(shape.getFill());
    					shape.setStroke(new Color(0,0,0,1));
                        shape.setStrokeWidth(1);
    					r.setX(shape.getX()+10);
    					r.setY(shape.getY()+10);
    					canvas.getChildren().add(r); // ajout du nouvel element a la zone de dessin
    					listeRectangle.add(r); // ajout du nouvel element a la liste de rectangle 
    					
    					
    					
    				}
    			}
    			for(int j = 0; j<listeEllipse.size(); j++) { // idem pour l'ellipse
    				Ellipse shape = listeEllipse.get(j);
    				if(shape.getStrokeWidth()==2) {
    					Ellipse r = new Ellipse();
    					r.setRadiusX(shape.getRadiusX());
    					r.setRadiusY(shape.getRadiusY());
    					r.setFill(shape.getFill());
    					shape.setStroke(new Color(0,0,0,1));
                        shape.setStrokeWidth(1);
    					r.setCenterX(shape.getCenterX()+10);
    					r.setCenterY(shape.getCenterY()+10);
    					canvas.getChildren().add(r);
    					listeEllipse.add(r);
    					
    					
    					
    				}
    			}
    			
    			
    			for(int j = 0; j<listeEllipse.size(); j++) { // idem pour l'ellipse
    				Ellipse shape = listeEllipse.get(j);
    				if(shape.getStrokeWidth()==2) {
    					Ellipse r = new Ellipse();
    					r.setRadiusX(shape.getRadiusX());
    					r.setRadiusY(shape.getRadiusY());
    					r.setFill(shape.getFill());
    					shape.setStroke(new Color(0,0,0,1));
    					shape.setStrokeWidth(1);
    					r.setCenterX(shape.getCenterX()+10);
    					r.setCenterY(shape.getCenterY()+10);
    					canvas.getChildren().add(r);
    					listeEllipse.add(r);
					
					
					
    				}
    			}
    		});
	
    		////////////////////////////////////////////////////Delete ////////////////////////////////////
    		
    		delete.setOnAction(event -> {                             
    			for(int j = 0; j<listeRectangle.size(); j++) { // recherche dans la liste de rectangle de l'element a supprimer
    				Rectangle shape = listeRectangle.get(j);
    				if(shape.getStrokeWidth()==2) {          // condition de l'element en surbrillance 
    					listeRectangle.remove(shape); // suppression de la figure dans la liste 
    					canvas.getChildren().remove(shape); // suppression de la figure dans la zone de dessin
    				}
    			}
    			for(int j = 0; j<listeLine.size(); j++) { // idem pour la ligne 
    				Line shape = listeLine.get(j);
    				if(shape.getStrokeWidth()==2) {
    					listeLine.remove(shape);
    					canvas.getChildren().remove(shape);
    				}
    			}
    		
    			for(int j = 0; j<listeEllipse.size(); j++) { // idem pour l'ellipse 
    				Ellipse shape = listeEllipse.get(j);
    				if(shape.getStrokeWidth()==2) {
    					listeEllipse.remove(shape);
    					canvas.getChildren().remove(shape);
    				}
    			}
    			
    		});		
    	});
    	
    	
    	
    	
    	
    	
    	
    	
                  	
    	                   	
                          
                    
                 
           
      
               
    	
    	
    	
    	
        	
        
   
    	   
    	
    }
}